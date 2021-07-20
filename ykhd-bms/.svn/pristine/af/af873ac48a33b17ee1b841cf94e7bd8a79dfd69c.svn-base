package com.ykhd.office.domain.resp;

import java.math.BigDecimal;
import java.util.Date;

import cn.gjing.tools.excel.Excel;
import cn.gjing.tools.excel.ExcelField;
import cn.gjing.tools.excel.convert.ExcelDataConvert;

@Excel("排期回款申请")
public class OASReturn_Excel {

	@ExcelField("编号")
	private Integer id;
	@ExcelField("排期编号")
	private Integer schedule;
	@ExcelField("客户公司名称")
	private String customerName;
	@ExcelField("客户品牌名称")
	private String customerBrand;
	@ExcelField("公众号名称")
	private String oaName;
	@ExcelField("排期人")
	private String scheduler;
	@ExcelField(value = "投放时间", format = "yyyy-MM-dd")
	private Date putDate;
	@ExcelField("金额")
	private BigDecimal amount;
	@ExcelField("收款账户")
	private String receiveAccount;
	@ExcelField("收方开户行")
	private String bankName;
	@ExcelField("收方开户人")
	private String accountHolder;
	@ExcelField("收方银行卡号")
	private String accountNumber;
	@ExcelField("申请备注")
	private String remark;
	@ExcelField(value = "创建时间", format = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	@ExcelField("状态")
	@ExcelDataConvert(expr1 = "#state==1?'待确认':#state==2?'驳回':#state==3?'待复核':#state==4?'已回款':''")
	private Integer state;
	@ExcelField(value = "回款时间", format = "yyyy-MM-dd")
	private Date returnTime;
	@ExcelField("回款备注")
	private String returnRemark;
	
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
}
