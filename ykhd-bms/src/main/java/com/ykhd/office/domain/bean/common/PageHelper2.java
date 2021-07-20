package com.ykhd.office.domain.bean.common;

import java.util.List;
import java.util.Map;

/**
 * @author zhoufan
 * @Date 2020/9/16
 */
public final class PageHelper2 {
    private int page;
    private int size;
    private long total;
    private List<Map<String, Object>> data;

    public PageHelper2(int page, int size, long total, List<Map<String, Object>> data) {
        this.page = page;
        this.size = size;
        this.total = total;
        this.data = data;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<Map<String, Object>> getData() {
        return data;
    }

    public void setData(List<Map<String, Object>> data) {
        this.data = data;
    }
}
