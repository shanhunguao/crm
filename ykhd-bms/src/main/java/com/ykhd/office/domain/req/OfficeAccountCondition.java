package com.ykhd.office.domain.req;

import java.math.BigDecimal;

import com.ykhd.office.domain.bean.common.PageCondition;

/**
 * 分页列表查询条件
 */
public class OfficeAccountCondition extends PageCondition {

	private String name;
	private String category;
	private Integer state;
	private Double fans_lower;
	private Double fans_upper;
	private String cooperateMode;
	private String customerType;
	private BigDecimal headlineCost_lower;
	private BigDecimal headlineCost_upper;
	private BigDecimal cpaPrice_lower;
	private BigDecimal cpaPrice_upper;
	private String phone;
	private Integer collection; //我的收藏
	private Integer medium; //创建媒介 或 优势号专属媒介
	private Integer advantage; //=1优势号
	private String sort; //排序
	private Integer agreement; //协议号
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public Double getFans_lower() {
		return fans_lower;
	}
	public void setFans_lower(Double fans_lower) {
		this.fans_lower = fans_lower;
	}
	public Double getFans_upper() {
		return fans_upper;
	}
	public void setFans_upper(Double fans_upper) {
		this.fans_upper = fans_upper;
	}
	public Integer getCollection() {
		return collection;
	}
	public void setCollection(Integer collection) {
		this.collection = collection;
	}
	public String getCooperateMode() {
		return cooperateMode;
	}
	public void setCooperateMode(String cooperateMode) {
		this.cooperateMode = cooperateMode;
	}
	public String getCustomerType() {
		return customerType;
	}
	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}
	public BigDecimal getHeadlineCost_lower() {
		return headlineCost_lower;
	}
	public void setHeadlineCost_lower(BigDecimal headlineCost_lower) {
		this.headlineCost_lower = headlineCost_lower;
	}
	public BigDecimal getHeadlineCost_upper() {
		return headlineCost_upper;
	}
	public void setHeadlineCost_upper(BigDecimal headlineCost_upper) {
		this.headlineCost_upper = headlineCost_upper;
	}
	public BigDecimal getCpaPrice_lower() {
		return cpaPrice_lower;
	}
	public void setCpaPrice_lower(BigDecimal cpaPrice_lower) {
		this.cpaPrice_lower = cpaPrice_lower;
	}
	public BigDecimal getCpaPrice_upper() {
		return cpaPrice_upper;
	}
	public void setCpaPrice_upper(BigDecimal cpaPrice_upper) {
		this.cpaPrice_upper = cpaPrice_upper;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Integer getMedium() {
		return medium;
	}
	public void setMedium(Integer medium) {
		this.medium = medium;
	}
	public Integer getAdvantage() {
		return advantage;
	}
	public void setAdvantage(Integer advantage) {
		this.advantage = advantage;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public Integer getAgreement() {
		return agreement;
	}
	public void setAgreement(Integer agreement) {
		this.agreement = agreement;
	}
}
