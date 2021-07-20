package com.ykhd.office.domain.req;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 发起支付申请提交信息
 */
public class PaymentAppSubmit {

	@NotNull(message = "排期为空")
	private Integer schedule;
	@NotNull(message = "付款类型为空")
	private Integer type;
	@NotEmpty(message = "支付账户为空")
	private String payAccount;
	@NotNull(message = "支付金额为空")
	@DecimalMin(value = "0.01", message = "不能低于最小金额0.01元")
	private BigDecimal amount;
	@NotEmpty(message = "开户行为空")
	private String bankName;
	@NotEmpty(message = "开户人为空")
	private String accountHolder;
	@NotEmpty(message = "银行卡为空")
	private String accountNumber;
	private String idCardNo;
	private String remark;
	
	public Integer getSchedule() {
		return schedule;
	}
	public void setSchedule(Integer schedule) {
		this.schedule = schedule;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getPayAccount() {
		return payAccount;
	}
	public void setPayAccount(String payAccount) {
		this.payAccount = payAccount;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
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
}
