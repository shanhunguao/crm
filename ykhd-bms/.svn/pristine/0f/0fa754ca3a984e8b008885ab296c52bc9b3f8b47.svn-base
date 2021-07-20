package com.ykhd.office.domain.resp;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * AE业绩排行榜
 */
public class AERanking {

	private String name;
	private String dept;
	@JsonIgnore
	private BigDecimal profit;
	private int count;
	private boolean champTeam;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public BigDecimal getProfit() {
		return profit.setScale(2, BigDecimal.ROUND_HALF_UP);
	}
	public void setProfit(BigDecimal profit) {
		this.profit = profit;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public boolean isChampTeam() {
		return champTeam;
	}
	public void setChampTeam(boolean champTeam) {
		this.champTeam = champTeam;
	}
}
