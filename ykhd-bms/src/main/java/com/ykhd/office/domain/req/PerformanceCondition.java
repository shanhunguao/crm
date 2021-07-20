package com.ykhd.office.domain.req;

/**
 * 业绩查询条件
 */
public class PerformanceCondition {

	private Integer AE; //ae
	private Integer medium; //媒介
	private Integer team; //战队
	private String startTime; //回款日日期 起
	private String endTime; //回款日日期 止
	
	public Integer getAE() {
		return AE;
	}
	public void setAE(Integer aE) {
		AE = aE;
	}
	public Integer getMedium() {
		return medium;
	}
	public void setMedium(Integer medium) {
		this.medium = medium;
	}
	public Integer getTeam() {
		return team;
	}
	public void setTeam(Integer team) {
		this.team = team;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
}
