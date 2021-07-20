package com.ykhd.office.domain.req;

import com.ykhd.office.domain.bean.common.PageCondition;

/**
 * 系统日志查询条件
 */
public class SystemLogCondition extends PageCondition {

	private String name;
	private String startTime;
	private String endTime;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
