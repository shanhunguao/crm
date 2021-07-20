package com.ykhd.office.domain.bean.common;

import java.util.List;

/**
 * 分页助手
 */
public final class PageHelper<T> {

	private int page;
	private int size;
	private long total;
	private List<T> data;
	
	public PageHelper(int page, int size, long total, List<T> data) {
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
	public List<T> getData() {
		return data;
	}
	public void setData(List<T> data) {
		this.data = data;
	}
}
