package com.tqwc.feastcommon.utils;

import java.util.List;

/**
 * @author Tang
 * @data 2026/3/12 11:22
 */

public class PageResult<T>{
    //返回记录总数
    private long total;
    //每页显示条数

    //当前页码

    //返回当前页码数据
    private List<T> rows;

    public PageResult(long total, List<T> rows) {
        this.total = total;
        this.rows = rows;
    }

    public long getTotal() {
        return total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    @Override
    public String toString() {
        return "PageResult{" +
                "total=" + total +
                ", rows=" + rows +
                '}';
    }
}