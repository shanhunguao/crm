package com.ykhd.office.domain.bean.common;

/**
 * 分页基础查询条件
 */
public class PageCondition {

	private int page = 1;
	private int size = 20;
	
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
		this.size = size > 0 && size <= 100 ? size : 20;
	}
}
