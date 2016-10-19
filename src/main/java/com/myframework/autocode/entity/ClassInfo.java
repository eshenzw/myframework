package com.myframework.autocode.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 类信息
 */
public class ClassInfo {

    /*
     * 类名
     */
    private String className;

    /*
     * 属性
     */
    private List<PropertyInfo> properties;
    
    public ClassInfo() {
        
    }
    
    public ClassInfo(TableInfo tableInfo) {
        // 转换数据表名格式到JAVA类名格式，再加上"Entity"
        this.className = convertTableDbName(tableInfo.getTableDbName()) + "Entity";
        this.properties = new ArrayList<PropertyInfo>();
        for (ColumnInfo columnInfo : tableInfo.getColumns()) {
            PropertyInfo propertyInfo = new PropertyInfo(columnInfo);
            this.properties.add(propertyInfo);
        }
    }
    
    public String getClassNameWithoutEntity() {
        return className.replace("Entity", "");
    }
    
    public String getClassObjectName() {
        char[] chars = getClassNameWithoutEntity().toCharArray();
        chars[0] = Character.toLowerCase(chars[0]);
        return String.valueOf(chars);
    }
    
    public String getDaoName() {
        return getClassNameWithoutEntity() + "Dao";
    }

    public String getIDaoName() {
        return "I" + getClassNameWithoutEntity() + "Dao";
    }
    
    @Override
    public String toString() {
        return "ClassInfo [className=" + className + ", properties="
                + properties + "]";
    }

    /*
     * 转换数据列名格式到JAVA类名格式
     */
    private String convertTableDbName(String tableDbName) {
        String[] tableDbNameWords = tableDbName.toLowerCase().split("_");
        String className = "";
        for (String tableDbNameWord : tableDbNameWords) {
            char[] chars = tableDbNameWord.toCharArray();
            chars[0] = Character.toUpperCase(chars[0]);
            className += String.valueOf(chars);
        }
        return className;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<PropertyInfo> getProperties() {
        return properties;
    }

    public void setProperties(List<PropertyInfo> properties) {
        this.properties = properties;
    }

}
