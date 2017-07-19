package com.myframework.core.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 基础的Entity类主要实现ID.
 * Created by Administrator on 2016/8/21.
 */
public class BaseEntity implements IdEntity,Serializable {

    /** 序列标识. */
    protected static final long serialVersionUID = 119185790113566379L;

    /** 实体标识. */
    private Long id;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * status
     */
    private String status;

    /**
     * creator_id
     */
    private Long createId;

    /**
     * create_time
     */
    private Date createTime;

    /**
     * modifyier_id
     */
    private Long updateId;

    /**
     * bas_pd_dic.modify_time
     *
     * Tue Jul 21 15:19:56 CST 2015
     */
    private Date updateTime;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getCreateId() {
        return createId;
    }

    public void setCreateId(Long createId) {
        this.createId = createId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateId() {
        return updateId;
    }

    public void setUpdateId(Long updateId) {
        this.updateId = updateId;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
