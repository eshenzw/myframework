package com.myframework.pagehelper.parser.impl;

import java.util.Map;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;

import com.myframework.pagehelper.Page;

/**
 * Take note that at least one column
 * needs to be defined for ORDER BY
 * in oder for OFFSET .. ROWS to work
 */
public class SqlServer2012Dialect extends AbstractParser {
    //with(nolock)
    protected static final String WITHNOLOCK = ", PAGEWITHNOLOCK";

    @Override
    public String getCountSql(String sql) {
        sql = sql.replaceAll("((?i)with\\s*\\(nolock\\))", WITHNOLOCK);
        sql = super.getCountSql(sql);
        sql = sql.replace(WITHNOLOCK, " with(nolock)");
        return sql;
    }

    @Override
    public String getPageSql(String sql) {
        StringBuilder sqlBuilder = new StringBuilder(sql.length() + 14);
        sqlBuilder.append(sql);
        sqlBuilder.append(" OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");
        return sqlBuilder.toString();
    }

    @Override
    public Map<String, Object> setPageParameter(MappedStatement ms, Object parameterObject, BoundSql boundSql, Page<?> page) {
        Map<String, Object> paramMap = super.setPageParameter(ms, parameterObject, boundSql, page);
        // OFFSET (@PageNumber-1)*@RowsPerPage ROWS
        paramMap.put(PAGEPARAMETER_FIRST, page.getStartRow());
        paramMap.put(PAGEPARAMETER_SECOND, page.getPageSize());
        return paramMap;
    }

}
