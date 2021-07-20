package com.ykhd.office.domain.resp;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 支付申请列表dto
 */
public class PaymentAppDto {

	private Integer id;
	private Integer schedule;
	private Integer type;
	private BigDecimal amount;
	private String payAccount;
	private String bankName;
	private String accountHolder;
	private String accountNumber;
	private String idCardNo;
	private String remark;
	private String creatorName;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
	private Date createTime;
	private Integer state;
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date paytime;
	private String payRemark;
	private String payImage;
	
	private String customerBrand; //客户品牌
	private String oaName; //公众号名称
	private String scheduler; //排期人
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date putDate; //投放日期
	private String putPosition; //投放位置
	
	private Integer oaFapiao; //发票税率
	@JsonIgnore
	private Integer fapiao;
	private String fapiaoCode; //发票编号
	private String fapiaoCompany; //开票公司
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getSchedule() {
		return schedule;
	}
	public void setSchedule(Integer schedule) {
		this.schedule = schedule;
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
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getPayAccount() {
		return payAccount;
	}
	public void setPayAccount(String payAccount) {
		this.payAccount = payAccount;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getAccountHolder() {
		return accountHolder;
	}
	public void setAccountHolder(String accountHolder) {
		this.accountHolder = accountHolder;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getIdCardNo() {
		return idCardNo;
	}
	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getCreatorName() {
		return creatorName;
	}
	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public Date getPaytime() {
		return paytime;
	}
	public void setPaytime(Date paytime) {
		this.paytime = paytime;
	}
	public String getPayRemark() {
		return payRemark;
	}
	public void setPayRemark(String payRemark) {
		this.payRemark = payRemark;
	}
	public String getPayImage() {
		return payImage;
	}
	public void setPayImage(String payImage) {
		this.payImage = payImage;
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
	public Integer getOaFapiao() {
		return oaFapiao;
	}
	public void setOaFapiao(Integer oaFapiao) {
		this.oaFapiao = oaFapiao;
	}
	public Integer getFapiao() {
		return fapiao;
	}
	public void setFapiao(Integer fapiao) {
		this.fapiao = fapiao;
	}
	public String getFapiaoCode() {
		return fapiaoCode;
	}
	public void setFapiaoCode(String fapiaoCode) {
		this.fapiaoCode = fapiaoCode;
	}
	public String getFapiaoCompany() {
		return fapiaoCompany;
	}
	public void setFapiaoCompany(String fapiaoCompany) {
		this.fapiaoCompany = fapiaoCompany;
	}
}
