package com.myframework.util;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;

/**
 * Mybatis 工具类
 * 主要提供接口实现的类方法代码实现，例如ResultHandler导出等
 * Created by zw on 2017/7/17.
 */
public class MybatisUtil {

    private SqlSession sqlSessionTemplate;

    private Class class1;

    private String namespace;

    private MybatisUtil() {

    }

    public static MybatisUtil withNameSpace(Class class1) {
        MybatisUtil mybatisUtil = new MybatisUtil();
        mybatisUtil.setSqlSessionTemplate((SqlSession) SpringContextUtil.getBean(SqlSession.class));
        mybatisUtil.setClass1(class1);
        return mybatisUtil;
    }

    public static MybatisUtil withNameSpace(Class class1, ExecutorType executorType) {
        MybatisUtil mybatisUtil = new MybatisUtil();
        mybatisUtil.setSqlSessionTemplate((SqlSession) SpringContextUtil.getBean(SqlSession.class));
        if (mybatisUtil.sqlSessionTemplate instanceof SqlSessionTemplate) {
            ((SqlSessionTemplate) mybatisUtil.sqlSessionTemplate).getSqlSessionFactory().openSession(executorType);
        }
        mybatisUtil.setClass1(class1);
        return mybatisUtil;
    }

    public static MybatisUtil withNameSpace(Class class1, ExecutorType executorType, boolean isAutoCommit) {
        MybatisUtil mybatisUtil = new MybatisUtil();
        mybatisUtil.setSqlSessionTemplate((SqlSession) SpringContextUtil.getBean(SqlSession.class));
        if (mybatisUtil.sqlSessionTemplate instanceof SqlSessionTemplate) {
            mybatisUtil.sqlSessionTemplate = ((SqlSessionTemplate) mybatisUtil.sqlSessionTemplate).getSqlSessionFactory().openSession(executorType, isAutoCommit);
        }
        mybatisUtil.setClass1(class1);
        return mybatisUtil;
    }


    public <T> T selectOne(String sqlName) {
        return sqlSessionTemplate.selectOne(getNamespaceDot() + sqlName);
    }

    public <T> T selectOne(String sqlName, Object parameter) {
        return sqlSessionTemplate.selectOne(getNamespaceDot() + sqlName, parameter);
    }

    public <E> List<E> selectList(String sqlName) {
        return sqlSessionTemplate.selectList(getNamespaceDot() + sqlName);
    }

    public <E> List<E> selectList(String sqlName, Object parameter) {
        return sqlSessionTemplate.selectList(getNamespaceDot() + sqlName, parameter);
    }

    public <K, V> Map<K, V> selectMap(String sqlName, String parameter) {
        return sqlSessionTemplate.selectMap(getNamespaceDot() + sqlName, parameter);
    }

    public <K, V> Map<K, V> selectMap(String sqlName, Object parameter, String key) {
        return sqlSessionTemplate.selectMap(getNamespaceDot() + sqlName, parameter, key);
    }

    public int insert(String sqlName) {
        return sqlSessionTemplate.insert(getNamespaceDot() + sqlName);
    }

    public int insert(String sqlName, Object parameter) {
        return sqlSessionTemplate.insert(getNamespaceDot() + sqlName, parameter);
    }

    public int update(String sqlName) {
        return sqlSessionTemplate.update(getNamespaceDot() + sqlName);
    }

    public int update(String sqlName, Object parameter) {
        return sqlSessionTemplate.update(getNamespaceDot() + sqlName, parameter);
    }

    public int delete(String sqlName) {
        return sqlSessionTemplate.delete(getNamespaceDot() + sqlName);
    }

    public int delete(String sqlName, Object parameter) {
        return sqlSessionTemplate.delete(getNamespaceDot() + sqlName, parameter);
    }

    public void select(String sqlName, ResultHandler handler) {
        sqlSessionTemplate.select(getNamespaceDot() + sqlName, handler);
    }

    public void select(String sqlName, Object parameter, ResultHandler handler) {
        sqlSessionTemplate.select(getNamespaceDot() + sqlName, parameter, handler);
    }

    public void commit() {
        sqlSessionTemplate.commit();
    }

    public void commit(boolean var1) {
        sqlSessionTemplate.commit(var1);
    }

    public void rollback() {
        sqlSessionTemplate.rollback();
    }

    public void rollback(boolean var1) {
        sqlSessionTemplate.rollback(var1);
    }

    public void close() {
        sqlSessionTemplate.close();
    }

    public void clearCache() {
        sqlSessionTemplate.clearCache();
    }

    public <T> T getMapper() {
        return sqlSessionTemplate.getMapper((Class<T>) class1);
    }

    public String getNamespaceDot() {
        return this.class1.getName() + ".";
    }

    public Class getClass1() {
        return class1;
    }

    public void setClass1(Class class1) {
        this.class1 = class1;
    }

    public void setSqlSessionTemplate(SqlSession sqlSessionTemplate) {
        this.sqlSessionTemplate = sqlSessionTemplate;
    }
}
