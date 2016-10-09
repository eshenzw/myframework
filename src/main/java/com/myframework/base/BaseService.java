package com.myframework.base;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.myframework.entity.BaseEntity;
import com.myframework.pagehelper.Page;
import com.myframework.pagehelper.PageInfo;

public abstract class BaseService<T extends BaseEntity, D extends BaseDao<T>> implements IBaseService<T> {
    /** dao实现类. */
    private D dao;
    /** 服务id. */
    protected Long serviceId;
    /** 服务名. */
    protected Long serviceName;

    public D getDao() {
        return dao;
    }

    @Autowired
    public void setDao(D dao) {
        this.dao = dao;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public Long getServiceName() {
        return serviceName;
    }

    public void setServiceName(Long serviceName) {
        this.serviceName = serviceName;
    }

    @Override
    public List<T> queryList(Map<String, Object> params) {
        return dao.queryList(params);
    }

    @Override
    public T getByID(Serializable id) {
        return dao.getByID(id);
    }

    @Override
    public T getByName(String name) {
        return dao.getByName(name);
    }

    @Override
    public int add(T t) {
        return dao.add(t);
    }

    @Override
    public int addSelective(T t) {
        return dao.addSelective(t);
    }

    @Override
    public int updateByID(T t) {
        return dao.updateByID(t);
    }

    @Override
    public int updateByIDSelective(T t) {
        return dao.updateByIDSelective(t);
    }

    @Override
    public int delByID(Serializable id) {
        return dao.delByID(id);
    }

    @Override
    public int batchDelByIDs(Serializable[] ids) {
        dao.batchDelByIDs(ids);
        return ids.length;
    }

    @Override
    public List<T> batchAdd(List<T> list) {
        return dao.batchAdd(list);
    }

    @Override
    public int batchUpdate(List<T> list) {
        return dao.batchUpdate(list);
    }

    @Override
    public PageInfo<T> queryPage(Map<String, Object> params, Page<T> page) {
        return dao.queryPage(params, page);
    }
}
