package com.ykhd.office.domain.resp;

public class ChampByWeek {

	private Integer week;
	private String name;
	
	public ChampByWeek(Integer week, String name) {
		this.week = week;
		this.name = name;
	}
	public Integer getWeek() {
		return week;
	}
	public void setWeek(Integer week) {
		this.week = week;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
