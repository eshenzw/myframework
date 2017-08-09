package com.myframework.util;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.SqlSession;

/**
 * Mybatis 工具类
 * 主要提供接口实现的类方法代码实现，例如ResultHandler导出等
 * Created by zw on 2017/7/17.
 */
public class MybatisUtil {

    private SqlSession sqlSessionTemplate;

    private String namespace;

    private MybatisUtil() {

    }

    public static MybatisUtil withNameSpace(Class class1) {
        MybatisUtil mybatisUtil = new MybatisUtil();
        mybatisUtil.setSqlSessionTemplate((SqlSession) SpringContextUtil.getBean(SqlSession.class));
        mybatisUtil.setNamespace(class1.getName());
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

    public String getNamespaceDot() {
        return namespace + ".";
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public void setSqlSessionTemplate(SqlSession sqlSessionTemplate) {
        this.sqlSessionTemplate = sqlSessionTemplate;
    }
}
