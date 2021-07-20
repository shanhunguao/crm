package com.ykhd.office.domain.resp;

import java.util.List;

public class ChampByMonth {

	private String month;
	private String name;
	private List<ChampByWeek> weeks;
	
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<ChampByWeek> getWeeks() {
		return weeks;
	}
	public void setWeeks(List<ChampByWeek> weeks) {
		this.weeks = weeks;
	}
}
