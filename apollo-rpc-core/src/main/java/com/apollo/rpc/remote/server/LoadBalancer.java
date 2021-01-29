package com.apollo.rpc.remote.server;

import com.apollo.rpc.task.RPCScheduledRunnable;
import com.apollo.rpc.task.RPCTaskRunner;

import java.util.*;

/**
 * 负载均衡器
 * 基于优先级的负载均衡算法
 * 优先级取决于远程服务实例对心跳包的最新响应时间，时间越短优先级越高
 * 非线程安全类
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
        RPCTaskRunner.schedule(new PriorityUpdateRunner(),priority_update_time);
    }

    /**
     * 根据优先级算法，产生一个序号
     * @return
     */
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

    /**
     * 获取特定序号的优先级
     * @param index
     * @return
     */
    public int getPriority(int index){
        return priority.get(index);
    }

    /**
     * 列表头部中添加一个项，此项的优先级被初始化为0，不会被选择
     */
    public synchronized void add(){
        priority.add(priority.size(),0);
        times.add(times.size(),Long.MAX_VALUE);
        lastTime.add(lastTime.size(),System.currentTimeMillis());
    }

    /**
     * 移除列表中对应序号的项
     * @param index
     */
    public synchronized void remove(int index){
        priority.remove(index);
        times.remove(index);
        lastTime.remove(index);
        updatePriority();
    }

    /**
     * 设置对应项的请求时间和应答时间
     * @param index
     * @param responseTime
     * @param requestTime
     */
    public void setTime(int index,long responseTime,long requestTime){
        if(requestTime > lastTime.get(index)){
            lastTime.set(index,requestTime);
            times.set(index,responseTime-requestTime);
        }
    }

    /**
     * 熔断列表中对应的项，此项的优先级被设为0，不会被选择
     * @param index
     */
    public void fuse(int index){
        priority.set(index,0);
        times.set(index,Long.MAX_VALUE);
        updatePriority();
    }

    /**
     * 激活列表中对应的项，并设置此项的优先级为列表中最高
     * @param index
     */
    public void active(int index){
        priority.set(index,++priorityMax);
    }

    /**
     * 更新优先级的内部Runner
     */
    private class PriorityUpdateRunner extends RPCScheduledRunnable {
        @Override
        public void run() {
            if(registered){
                updatePriority();
            }else{
                cancel();//取消任务
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
