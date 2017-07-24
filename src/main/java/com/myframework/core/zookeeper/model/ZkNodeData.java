package com.myframework.core.zookeeper.model;

import java.io.Serializable;

/**
 * 对zknode节点数据封装<br> 
 * 〈功能详细描述〉
 *
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class ZkNodeData implements Serializable {

    /**
     */
    private static final long serialVersionUID = -9167812198817633489L;

    private String path;

    private String value;

    private String version;

    public ZkNodeData(String path, String value, String version) {
        super();
        this.path = path;
        this.value = value;
        this.version = version;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "ZkNodeData [path=" + path + ", value=" + value + ", version=" + version + "]";
    }
}
