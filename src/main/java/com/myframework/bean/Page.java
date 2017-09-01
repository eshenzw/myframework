package com.myframework.bean;

import java.util.List;

/**
 * Created by zw on 2017/9/1.
 */
public class Page<T> {

    /**
     * 结果数据
     */
    private List<T> rows;

    /**
     * 总数量
     */
    private long records;

    /**
     * 总页数
     */
    private long total;

    /**
     * 每页的数量
     */
    private int rowNum;

    /**
     * 当前page数
     */
    private int page;

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public long getRecords() {
        return records;
    }

    public void setRecords(long records) {
        this.records = records;
        if (records == -1) {
            total = 1;
            return;
        }
        if (rowNum > 0) {
            total = (int) (records / rowNum + ((records % rowNum == 0) ? 0 : 1));
        } else {
            total = 0;
        }
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public int getRowNum() {
        return rowNum;
    }

    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

}