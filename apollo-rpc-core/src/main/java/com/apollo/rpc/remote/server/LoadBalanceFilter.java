package com.apollo.rpc.remote.server;

import com.apollo.rpc.exception.RPCException;
import com.apollo.rpc.exception.RemoteServerLimitException;
import com.apollo.rpc.exception.ResponseOutOfTimeException;
import com.apollo.rpc.msg.RPCReqBase;
import com.apollo.rpc.msg.impl.RPCServerCheckReqMsg;
import com.apollo.rpc.task.RPCScheduledRunnable;
import com.apollo.rpc.task.RPCTaskRunner;
import com.apollo.rpc.remote.instance.RemoteServerInstance;
import com.apollo.rpc.exception.RemoteServerDisabledException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.concurrent.locks.StampedLock;

/**
 * 负载均衡和熔断过滤器
 */
public class LoadBalanceFilter extends RemoteServerInstanceHolder {

    private Log log = LogFactory.getLog(LoadBalanceFilter.class);
    private static final int checkingTime = 200;

    private LoadBalancer balancer;
    private StampedLock stampedLock;//高性能读写锁
    private boolean registered;

    public LoadBalanceFilter(){
        balancer = new LoadBalancer();
        stampedLock = new StampedLock();
        registered = true;
        balancer.start();
    }

    public void destroy(){
        this.registered = false;
        super.instances.clear();
        this.balancer.destroy();
    }

    public Object doRequest(RPCReqBase reqBase){
        RemoteServerInstance instance = getInstance();
        Object res;
        if(instance != null){
            try {
                res = instance.doRequest(reqBase);
            }catch (ResponseOutOfTimeException e){
                requestOutOfTime(instance);
                throw e;
            }
        }else{
            throw new RemoteServerDisabledException();
        }
        return res;
    }

    public void addInstance(RemoteServerInstance instance){
        long stamp = stampedLock.writeLock();
        try{
            super.addInstance(instance);
            balancer.add();
        }finally {
            stampedLock.unlock(stamp);
        }
    }

    public void removeInstance(RemoteServerInstance instance){
        long stamp = stampedLock.writeLock();
        try{
            int index = super.getIndex(instance);
            if(index >= 0){
                super.removeInstance(index);
                balancer.remove(index);
            }
        }finally {
            stampedLock.unlock(stamp);
        }
    }

    public void active(RemoteServerInstance instance){
        int index = super.getIndex(instance);
        balancer.active(index);
        log.info(instance.toString()+" has been activated");
        RPCTaskRunner.schedule(new ServerCheckingTask(instance),checkingTime);
    }

    public void requestOutOfTime(RemoteServerInstance instance){
        long stamp = stampedLock.writeLock();
        try{
            int index = super.getIndex(instance);
            if(index >= 0){
                balancer.fuse(index);
            }
        }finally {
            stampedLock.unlock(stamp);
        }
    }

    public void setTime(RemoteServerInstance instance,long responseTime,long requestTime){
        long stamp = stampedLock.writeLock();
        try{
            int index = getIndex(instance);
            if(index >= 0){
                int priority = balancer.getPriority(index);
                if(priority == 0){
                    balancer.active(index);
                }
                balancer.setTime(index,responseTime,requestTime);
            }
        }finally {
            stampedLock.unlock(stamp);
        }
    }

    /**
     *
     * @return
     */
    public RemoteServerInstance getInstance(){
        long stamp = stampedLock.readLock();
        try{
            int index = balancer.get();
            if(index >= 0){
                return super.getInstance(index);
            }
            return null;
        }finally {
            stampedLock.unlock(stamp);
        }
    }

    /**
     * 服务可用性检测异步任务
     */
    private class ServerCheckingTask extends RPCScheduledRunnable {

        private RemoteServerInstance instance;

        public ServerCheckingTask(RemoteServerInstance instance){
            this.instance = instance;
        }

        @Override
        public void run() {
            if(registered || instance.isActive()){
                if(getIndex(instance) >= 0){//实例仍然存在
                    checking();
                }
            }else{
                cancel();//取消任务
            }
        }

        public void checking(){
            RPCServerCheckReqMsg checkingReqMsg = new RPCServerCheckReqMsg();
            try{
                instance.doRequest(checkingReqMsg);
                setTime(instance,checkingReqMsg.rspBase.responseTime,checkingReqMsg.requestTime);
            }catch (ResponseOutOfTimeException e){
                log.info("instance unavailable:" + instance.toString());
                requestOutOfTime(instance);
            }catch (RemoteServerLimitException e){
                log.info("the RemoteServer is busy:" + instance.toString());
                requestOutOfTime(instance);
            }catch (RPCException e){
                log.error("unchecked exception",e);
            }
        }
    }
}
