package com.ykhd.office.domain.resp;

import java.math.BigDecimal;
import java.util.Date;

import cn.gjing.tools.excel.Excel;
import cn.gjing.tools.excel.ExcelField;
import cn.gjing.tools.excel.convert.ExcelDataConvert;

@Excel("支付申请")
public class PaymentApp_Excel {

	@ExcelField("编号")
	private Integer id;
	@ExcelField("排期编号")
	private Integer schedule;
	@ExcelField("支付类型")
	@ExcelDataConvert(expr1 = "#type==0?'成本金额':#type==1?'销售费用':''")
	private Integer type;
	@ExcelField("金额")
	private BigDecimal amount;
	@ExcelField("支付账户")
	private String payAccount;
	@ExcelField("开户行")
	private String bankName;
	@ExcelField("开户人")
	private String accountHolder;
	@ExcelField("银行账号")
	private String accountNumber;
	@ExcelField("身份证号码")
	private String idCardNo;
	@ExcelField("备注")
	private String remark;
	@ExcelField("申请媒介")
	private String creatorName;
	@ExcelField(value = "创建时间", format = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	@ExcelField("状态")
	@ExcelDataConvert(expr1 = "#state==0?'待支付':#state==1?'驳回':#state==2?'待复核':#state==3?'':#state==4?'已付款':''")
	private Integer state;
	@ExcelField(value = "支付时间", format = "yyyy-MM-dd")
	private Date paytime;
	@ExcelField("支付备注")
	private String payRemark;
	@ExcelField("客户品牌")
	private String customerBrand;
	@ExcelField("公众号名称")
	private String oaName;
	@ExcelField("排期人")
	private String scheduler;
	@ExcelField(value = "投放日期", format = "yyyy-MM-dd")
	private Date putDate;
	@ExcelField("投放位置")
	private String putPosition;
	private Integer fapiao; //不输出
	@ExcelField("发票编号")
	private String fapiaoCode;
	@ExcelField("开票公司")
	private String fapiaoCompany;
	
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
