package com.ykhd.office.domain.bean.common;

import java.util.List;
import java.util.Map;

/**
 * @author zhoufan
 * @Date 2020/8/28
 */
public final class PageHelpers<T> {

    private int page;
    private int size;
    private long total;
    private List<T> data;
    private Map<String, Object> statsdata;

    public PageHelpers(int page, int size, long total, List<T> data, Map<String, Object> statsdata) {
        this.page = page;
        this.size = size;
        this.total = total;
        this.data = data;
        this.statsdata = statsdata;
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
    public List<T> getData() {
        return data;
    }
    public void setData(List<T> data) {
        this.data = data;
    }

    public Map<String, Object> getStatsdata() {
        return statsdata;
    }

    public void setStatsdata(Map<String, Object> statsdata) {
        this.statsdata = statsdata;
    }
}