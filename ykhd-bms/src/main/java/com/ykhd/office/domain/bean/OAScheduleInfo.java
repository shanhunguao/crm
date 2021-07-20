package com.ykhd.office.domain.bean;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 支付申请列表显示的排期相关信息
 */
public class OAScheduleInfo {

	private Integer id;
	private String customerBrand; //客户品牌名称
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date putDate; //投放日期
	private String putPosition; //投放位置
	private String scheduler; //排期人
	private String oaName; //公众号名称
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCustomerBrand() {
		return customerBrand;
	}
	public void setCustomerBrand(String customerBrand) {
		this.customerBrand = customerBrand;
	}
	public String getOaName() {
		return oaName;
	}
	public void setOaName(String oaName) {
		this.oaName = oaName;
	}
	public String getScheduler() {
		return scheduler;
	}
	public void setScheduler(String scheduler) {
		this.scheduler = scheduler;
	}
	public Date getPutDate() {
		return putDate;
	}
	public void setPutDate(Date putDate) {
		this.putDate = putDate;
	}
	public String getPutPosition() {
		return putPosition;
	}
	public void setPutPosition(String putPosition) {
		this.putPosition = putPosition;
	}
}
