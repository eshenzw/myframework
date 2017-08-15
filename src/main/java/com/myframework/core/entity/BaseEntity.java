package com.myframework.core.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 基础的Entity类主要实现ID.
 * Created by Administrator on 2016/8/21.
 */
public class BaseEntity implements IdEntity, Serializable {

    /**
     * 序列标识.
     */
    protected static final long serialVersionUID = 119185790113566379L;

    /**
     * 实体标识.
     */
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
    private Long creatorId;

    /**
     * create_time
     */
    private Date createTime;

    /**
     * updator_id
     */
    private Long updatorId;

    /**
     * bas_pd_dic.modify_time
     * <p>
     * Tue Jul 21 15:19:56 CST 2015
     */
    private Date updateTime;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getUpdatorId() {
        return updatorId;
    }

    public void setUpdatorId(Long updatorId) {
        this.updatorId = updatorId;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
