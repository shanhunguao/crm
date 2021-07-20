package com.ykhd.office.domain.req;

import com.ykhd.office.domain.bean.common.PageCondition;

public class XunhaoCondition extends PageCondition {

	private Integer state; // 0发起  1已答复  2已确认

	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
}
