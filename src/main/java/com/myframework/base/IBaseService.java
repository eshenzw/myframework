package com.myframework.base;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.myframework.entity.BaseEntity;
import com.myframework.pagehelper.Page;
import com.myframework.pagehelper.PageInfo;

/**
 * Created by zw on 2016/8/21.
 */
public interface IBaseService<T extends BaseEntity> {
    /**
     * 根据参数获取对象列表
     *
     * @param params
     * @return
     */
    public List<T> queryList(Map<String, Object> params);

    /**
     * 根据主键id获取对象
     *
     * @param id
     * @return
     */
    public T getByID(Serializable id);

    /**
     * 根据名获取对象
     *
     * @param name
     * @return 对象
     */
    public  T getByName(String name);

    /**
     * 根据对象插入*所有字段*到db
     *
     * @param t
     * @return 插入结果（数量）
     * @deprecated 已经被删除的方法
     */
    public int add(T t);

    /**
     * 根据对象有选择性的只插入*字段不为空*到db
     *
     * @param t  对象
     * @return 插入结果（数量）
     */
    public int addSelective(T t);

    /**
     * 根据主键ID查询并*更新所有字段*到db
     *
     * @param t
     * @return 更新结果（数量）
     * @deprecated  已经被抛弃了的方法
     */
    public int updateByID(T t);

    /**
     * 根据主键ID查询并有选择性的只更新*字段不为空*到db
     *
     * @param t
     * @return 更新结果（数量）
     */
    public int updateByIDSelective(T t);

    /**
     * 根据主键ID查询并删除对象
     *
     * @param id
     * @return 删除结果（数量）
     */
    public int delByID(Serializable id);

    /**
     * 根据主键ID查询并删除对象
     * 批量删除ID
     *
     * @param ids
     * @return 删除结果（数量）
     */
    public int batchDelByIDs(Serializable[] ids);

    /**
     * 根据对象批量插入*所有字段*到db
     *
     * @param list
     * @return 插入结果（数量）
     * @deprecated 已经被删除的方法
     */
    public List<T> batchAdd(List<T> list);

    /**
     * 根据对象批量更新*所有字段*到db
     *
     * @param list
     * @return 更新结果（数量）
     * @deprecated 已经被删除的方法
     */
    public int batchUpdate(List<T> list);

    /**
     * 页查询
     * @param params
     * @param page
     * @return
     */
    public PageInfo<T> queryPage(Map<String, Object> params, Page<T> page);
}
