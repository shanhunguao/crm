package com.ykhd.office.domain.req;

import javax.validation.constraints.NotNull;

import com.ykhd.office.domain.bean.common.PageCondition;

/**
 * 公众号排期次数列表查询条件
 */
public class OAScheduleCondition2 extends PageCondition {

	@NotNull(message = "公众号id为空")
	private Integer oaId;
	private String customerName; // 客户品牌名称
	
	public Integer getOaId() {
		return oaId;
	}
	public void setOaId(Integer oaId) {
		this.oaId = oaId;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
}
