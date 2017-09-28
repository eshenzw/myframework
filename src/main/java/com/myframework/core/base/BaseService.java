package com.myframework.core.base;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.myframework.core.entity.BaseEntity;
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
    public List<T> list(Map<String, Object> params) {
        return dao.list(params);
    }

    @Override
    public T get(Serializable id) {
        return dao.get(id);
    }

    @Override
    public T getByName(String name) {
        return dao.getByName(name);
    }

    @Override
    public int insert(T t) {
        return dao.insert(t);
    }

    @Override
    public int insertSelective(T t) {
        return dao.insertSelective(t);
    }

    @Override
    public int update(T t) {
        return dao.update(t);
    }

    @Override
    public int updateSelective(T t) {
        return dao.updateSelective(t);
    }

    @Override
    public int delete(Serializable id) {
        return dao.delete(id);
    }

    @Override
    public int batchDelete(Serializable[] ids) {
        dao.batchDelete(ids);
        return ids.length;
    }

    @Override
    public List<T> batchInsert(List<T> list) {
        return dao.batchInsert(list);
    }

    @Override
    public int batchUpdate(List<T> list) {
        return dao.batchUpdate(list);
    }

    @Override
    public PageInfo<T> page(Map<String, Object> params, Page<T> page) {
        return dao.page(params, page);
    }
}
