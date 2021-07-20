package com.ykhd.office.domain.resp;

import java.math.BigDecimal;

/**
 * 媒介业绩排行榜
 */
public class MediumRanking {

	private String name;
	private BigDecimal total;
	private int count;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public BigDecimal getTotal() {
		return total;
	}
	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
}
