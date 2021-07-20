package com.ykhd.office.domain.req;

import com.ykhd.office.domain.bean.common.PageCondition;

/**
 * 公众号排期列表查询条件
 */
public class OAScheduleCondition extends PageCondition {

	private Integer creator; // AE
	private Integer medium; //媒介
	private Integer state; //状态
	private Integer advancePay; //垫付
	private Integer isPay; //支付  0未 1已
	private Integer isReturnMoney; //回款  0未 1已 2部分
	private String customerName; // 客户品牌名称
	private String company; //客户公司
	private String oaName; //公众号名称
	private Integer appCancel; //申请作废状态
	private String startTime; // 起
	private String endTime; // 止
	private Integer dateType; // 1创建时间   2投放日期  3回款日期  4付款日期
	private Integer team; //战队id
	private Integer code; //排期编号
	private String sort; //排序
	private String remark; //备注
	private Integer priceType; // 1报价  2成本价
	private Double price_lower;
	private Double price_upper;
	private Integer haveOaFapiao; // 进项发票是否已开  0已开 1已支付未开票
	private Integer haveCustFapiao; // 销项发票是否已开  0已开 1未开票
	private Integer advantage; //筛选优势号
	private String business;//商务
	private String reviewername;//审核人
	
	public Integer getCreator() {
		return creator;
	}
	public void setCreator(Integer creator) {
		this.creator = creator;
	}
	public Integer getMedium() {
		return medium;
	}
	public void setMedium(Integer medium) {
		this.medium = medium;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public Integer getAdvancePay() {
		return advancePay;
	}
	public void setAdvancePay(Integer advancePay) {
		this.advancePay = advancePay;
	}
	public Integer getIsPay() {
		return isPay;
	}
	public void setIsPay(Integer isPay) {
		this.isPay = isPay;
	}
	public Integer getIsReturnMoney() {
		return isReturnMoney;
	}
	public void setIsReturnMoney(Integer isReturnMoney) {
		this.isReturnMoney = isReturnMoney;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getOaName() {
		return oaName;
	}
	public void setOaName(String oaName) {
		this.oaName = oaName;
	}
	public Integer getAppCancel() {
		return appCancel;
	}
	public void setAppCancel(Integer appCancel) {
		this.appCancel = appCancel;
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
	public Integer getDateType() {
		return dateType;
	}
	public void setDateType(Integer dateType) {
		this.dateType = dateType;
	}
	public Integer getTeam() {
		return team;
	}
	public void setTeam(Integer team) {
		this.team = team;
	}
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getPriceType() {
		return priceType;
	}
	public void setPriceType(Integer priceType) {
		this.priceType = priceType;
	}
	public Double getPrice_lower() {
		return price_lower;
	}
	public void setPrice_lower(Double price_lower) {
		this.price_lower = price_lower;
	}
	public Double getPrice_upper() {
		return price_upper;
	}
	public void setPrice_upper(Double price_upper) {
		this.price_upper = price_upper;
	}
	public Integer getHaveOaFapiao() {
		return haveOaFapiao;
	}
	public void setHaveOaFapiao(Integer haveOaFapiao) {
		this.haveOaFapiao = haveOaFapiao;
	}
	public Integer getHaveCustFapiao() {
		return haveCustFapiao;
	}
	public void setHaveCustFapiao(Integer haveCustFapiao) {
		this.haveCustFapiao = haveCustFapiao;
	}
	public Integer getAdvantage() {
		return advantage;
	}
	public void setAdvantage(Integer advantage) {
		this.advantage = advantage;
	}

	public String getBusiness() {
		return business;
	}

	public void setBusiness(String business) {
		this.business = business;
	}

	public String getReviewername() {
		return reviewername;
	}

	public void setReviewername(String reviewername) {
		this.reviewername = reviewername;
	}
}
