package com.myframework.core.zookeeper.model;

/**
 * 节点枚举类型
 * 
 * @author zhangjun
 *
 */
public enum CreateMode {
	//创建节点后，不删除就永久存在
    PERSISTENT(0), 
    //节点path末尾会追加一个10位数的单调递增的序列
    PERSISTENT_SEQUENTIAL(1), 
    //创建后，回话结束节点会自动删除
    EPHEMERAL(2), 
    //节点path末尾会追加一个10位数的单调递增的序列
    EPHEMERAL_SEQUENTIAL(3);

    private final int value;

    private CreateMode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static org.apache.zookeeper.CreateMode findByValue(int value) {
        switch (value) {
            case 0:
                return org.apache.zookeeper.CreateMode.PERSISTENT;
            case 1:
                return org.apache.zookeeper.CreateMode.PERSISTENT_SEQUENTIAL;
            case 2:
                return org.apache.zookeeper.CreateMode.EPHEMERAL;
            case 3:
                return org.apache.zookeeper.CreateMode.EPHEMERAL_SEQUENTIAL;
            default:
                return null;
        }
    }
}
