package com.myframework.core.zookeeper.election;


/**
 * 实现leader选举，实现这个抽象类，注册到sprinng 
 * 
 * created by zw
 *
 */
public abstract class ZkLeaderLatcherListener   {


    protected  volatile boolean isLeader = false;

    public boolean isLeader(){
        return this.isLeader;
    }

    public void setupLeader(boolean isLeader){
      this.isLeader = isLeader;
    }


    /**
     * 那种类型的选举，例如 PUSH, COUPON_TASK
     * @return 选举的类型
     */
    public abstract  String getLatchType();
}
