package com.ykhd.office.domain.resp;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 回款信息列表dto
 */
public class OASReturnDto {

	private Integer id;
	private Integer schedule; //排期编号
	private String customerName; //客户公司名称
	private String customerBrand; //客户品牌名称
	private String oaName; //公众号名称
	private String scheduler; //排期人
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date putDate; //投放日期
	private BigDecimal amount; //回款金额
	private String receiveAccount; //收款账户
	private String bankName; //收方开户行
	private String accountHolder; //收方开户人
	private String accountNumber; //收方银行卡号
	private String remark; //申请备注
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
	private Date createTime;
	private Integer state; //状态
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date returnTime; //回款日期
	private String returnRemark; //回款备注
	private String returnImage; //回款截图存储路径
	private Integer custFapiao; //客户税点
	private Integer fapiao; //销项发票id
	private String reason; //驳回原因
	
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
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
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
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getReceiveAccount() {
		return receiveAccount;
	}
	public void setReceiveAccount(String receiveAccount) {
		this.receiveAccount = receiveAccount;
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
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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
	public Date getReturnTime() {
		return returnTime;
	}
	public void setReturnTime(Date returnTime) {
		this.returnTime = returnTime;
	}
	public String getReturnRemark() {
		return returnRemark;
	}
	public void setReturnRemark(String returnRemark) {
		this.returnRemark = returnRemark;
	}
	public String getReturnImage() {
		return returnImage;
	}
	public void setReturnImage(String returnImage) {
		this.returnImage = returnImage;
	}
	public Integer getCustFapiao() {
		return custFapiao;
	}
	public void setCustFapiao(Integer custFapiao) {
		this.custFapiao = custFapiao;
	}
	public Integer getFapiao() {
		return fapiao;
	}
	public void setFapiao(Integer fapiao) {
		this.fapiao = fapiao;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
}
