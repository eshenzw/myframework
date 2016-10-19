package com.myframework.autocode.entity;

import java.util.List;

/**
 * 数据表信息
 */
public class TableInfo {

    /*
     * 模块名
     */
    private String moduleName;

    /*
     * 表中文名称
     */
    private String tableName;

    /*
     * 数据库表名称
     */
    private String tableDbName;

    /*
     * 数据表列
     */
    private List<ColumnInfo> columns;

    @Override
    public String toString() {
        return "TableInfo [moduleName=" + moduleName + ",tableName=" + tableName + ", tableDbName="
                + tableDbName + ", columns=" + columns + "]";
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableDbName() {
        return tableDbName;
    }

    public void setTableDbName(String tableDbName) {
        this.tableDbName = tableDbName;
    }

    public List<ColumnInfo> getColumns() {
        return columns;
    }

    public void setColumns(List<ColumnInfo> columns) {
        this.columns = columns;
    }

}
