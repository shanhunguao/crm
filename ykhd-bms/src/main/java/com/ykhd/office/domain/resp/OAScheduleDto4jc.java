package com.ykhd.office.domain.resp;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 集采看到的排期列表dto
 */
public class OAScheduleDto4jc {

	private Integer id;
	@JsonIgnore
	private Integer officeAccount;
	private String oaName; //公众号名称
	private String wechat; //微信号
	@JsonIgnore
	private Integer customer;
	private String customerName; //客户品牌名称
	private String company; //客户公司名称
	private String cooperateMode; //合作模式
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date putDate; //投放日期
	private String putPosition; //投放位置
	private Integer count; //CPA投放数量
	//private BigDecimal executePrice; //执行价
	private BigDecimal costPrice; //成本价
	private BigDecimal sellExpense; //销售费用
	//private BigDecimal channelExpense;//渠道费用
	//private Integer advancePay; //已垫付天数
	//private Integer custFapiao; //客户是否开票
	private Integer oaFapiao; //号主是否含票
	private Integer oaFapiaoType; //号主含票类型
	private String mediumName; //媒介姓名
	private String confirmRemark; //媒介确认备注
	//private String remark; //创建备注
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
	private Date createTime; //创建时间
	private String creatorName; //创建人姓名
	//private Integer state; //状态
	//private String customerTax; //客户税额
	//private String channelTax; //渠道税额
	//private String profit; //利润
	//private String profitRate; //利润率
	//private Integer isPay; //是否支付 0未支付  1已支付
	//private BigDecimal payRatio; //支付比例
	//private BigDecimal returnRatio; //回款比例
	//private Integer isReturnMoney; //是否支付 0未回款  1已回款
	//@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    //private Date returnTime; //回款日期
	//private String returnRemark; //回款备注
	//private String payRemark; //付款备注
	//private Integer appCancel;
	//private String failureReason; // 审核不通过原因
	private BigDecimal fans; // 公众号粉丝
	//private boolean haveOaFapiao; // 是否已开进项发票
	//private boolean haveCustFapiao; // 是否已开销项发票
	//private Integer advantage; //优势号的排期标记
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getOfficeAccount() {
		return officeAccount;
	}
	public void setOfficeAccount(Integer officeAccount) {
		this.officeAccount = officeAccount;
	}
	public String getOaName() {
		return oaName;
	}
	public void setOaName(String oaName) {
		this.oaName = oaName;
	}
	public String getWechat() {
		return wechat;
	}
	public void setWechat(String wechat) {
		this.wechat = wechat;
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
	public String getCooperateMode() {
		return cooperateMode;
	}
	public void setCooperateMode(String cooperateMode) {
		this.cooperateMode = cooperateMode;
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
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public BigDecimal getCostPrice() {
		return costPrice;
	}
	public void setCostPrice(BigDecimal costPrice) {
		this.costPrice = costPrice;
	}
	public BigDecimal getSellExpense() {
		return sellExpense;
	}
	public void setSellExpense(BigDecimal sellExpense) {
		this.sellExpense = sellExpense;
	}
	public Integer getOaFapiao() {
		return oaFapiao;
	}
	public void setOaFapiao(Integer oaFapiao) {
		this.oaFapiao = oaFapiao;
	}
	public Integer getOaFapiaoType() {
		return oaFapiaoType;
	}
	public void setOaFapiaoType(Integer oaFapiaoType) {
		this.oaFapiaoType = oaFapiaoType;
	}
	public String getMediumName() {
		return mediumName;
	}
	public void setMediumName(String mediumName) {
		this.mediumName = mediumName;
	}
	public String getConfirmRemark() {
		return confirmRemark;
	}
	public void setConfirmRemark(String confirmRemark) {
		this.confirmRemark = confirmRemark;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getCreatorName() {
		return creatorName;
	}
	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}
	public Integer getCustomer() {
		return customer;
	}
	public void setCustomer(Integer customer) {
		this.customer = customer;
	}
	public BigDecimal getFans() {
		return fans;
	}
	public void setFans(BigDecimal fans) {
		this.fans = fans;
	}
}
