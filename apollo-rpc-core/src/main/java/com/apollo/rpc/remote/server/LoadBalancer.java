package com.apollo.rpc.remote.server;

import com.apollo.rpc.service.RPCTaskRunner;
import com.apollo.rpc.comm.CommonUtil;

import java.util.*;

/**
 * 负载均衡器
 * 基于优先级的负载均衡算法
 * 优先级取决于当前服务实例最新响应时间（从发送请求到接受应答所花费的时间，减去服务实例代码执行时间），时间越短优先级越高
 */
public class LoadBalancer {

    private static final long priority_update_time = 2000;
    private Random random;
    private List<Integer> priority;
    private List<Long> times;
    private List<Long> lastTime;
    private int priorityMax = 0;//最大优先级
    private boolean registered;

    public LoadBalancer(){
        lastTime = new ArrayList<>();
        priority = new ArrayList<>();
        times = new ArrayList<>();
        random = new Random();
        registered = true;
    }

    public void destroy(){
        registered = false;
    }

    public void start(){
        RPCTaskRunner.execute(new PriorityUpdateRunner());
    }

    public int get(){
        if(priorityMax == 0){
            return -1;
        }
        int sum = random.nextInt((priorityMax+1)*priorityMax/2)+1;
        for(int i=0;i<priority.size();i++){
            sum -= priority.get(i);
            if(sum <= 0){
                return i;
            }
        }
        return -1;
    }

    public int getPriority(int index){
        return priority.get(index);
    }

    public void add(){
        priority.add(priority.size(),0);
        times.add(times.size(),Long.MAX_VALUE);
        lastTime.add(lastTime.size(),System.currentTimeMillis());
    }

    public void remove(int index){
        priority.remove(index);
        times.remove(index);
        lastTime.remove(index);
        updatePriority();
    }

    public void setTime(int index,long responseTime,long requestTime){
        if(requestTime > lastTime.get(index)){
            lastTime.set(index,requestTime);
            times.set(index,responseTime-requestTime);
        }
    }

    public void fuse(int index){
        priority.set(index,0);
        times.set(index,Long.MAX_VALUE);
        updatePriority();
    }

    /**
     * 激活内部实例，不需要加锁
     * @param index
     */
    public void active(int index){
        priority.set(index,++priorityMax);
    }

    /**
     * 更新优先级的内部Runner
     */
    private class PriorityUpdateRunner implements Runnable{
        @Override
        public void run() {
            while(registered){
                updatePriority();
                CommonUtil.sleep(priority_update_time);
            }
        }
    }

    /**
     * 优先级更新方法,此方法使用需要加锁
     */
    private synchronized void updatePriority(){
        TreeSet<Tuple> prioritySet = new TreeSet<>();
        for(int i=0;i<priority.size();i++){
            if(priority.get(i) != 0){
                Tuple tuple = new Tuple(i, times.get(i));
                prioritySet.add(tuple);
            }
        }
        int i = prioritySet.size();
        Iterator<Tuple> iterator = prioritySet.iterator();
        while (iterator.hasNext()){
            Tuple tuple = iterator.next();
            priority.set(tuple.index,i);
            i--;
        }
        priorityMax = prioritySet.size();
    }

    /**
     * 响应时间与服务实例关联的二元组，用于优先级的排序
     */
    private class Tuple implements Comparable{

        int index;
        long time;

        public Tuple(int index, long time){
            this.index = index;
            this.time = time;
        }

        @Override
        public int compareTo(Object o) {
            if(this.time == ((Tuple) o).time){
                return 0;
            }else if(this.time > ((Tuple) o).time){
                return 1;
            }else{
                return -1;
            }
        }
    }

}
