package com.ykhd.office.domain.resp;

import com.ykhd.office.domain.bean.common.PageCondition;

public class EditRecordCondition extends PageCondition {

	private Integer state;

	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
}
