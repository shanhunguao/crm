package com.ykhd.office.domain.req;

import java.math.BigDecimal;

import javax.validation.constraints.NotEmpty;

/**
 * 确认回款提交信息
 */
public class OASReturnSubmit {

	/**排期id+回款金额 jsonStr*/
	@NotEmpty
	private String schedule_amount;
	/**收款账户*/
    @NotEmpty
	private String receiveAccount;
	/**付方开户行*/
    @NotEmpty
	private String bankName;
	/**付方开户人*/
    @NotEmpty
	private String accountHolder;
	/**付方银行卡号*/
    @NotEmpty
	private String accountNumber;
    /**申请备注*/
    private String remark;
    
	public String getSchedule_amount() {
		return schedule_amount;
	}
	public void setSchedule_amount(String schedule_amount) {
		this.schedule_amount = schedule_amount;
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
	
	public static class ScheduleAmount {
		
		private Integer schedule;
		private BigDecimal amount;
		public Integer getSchedule() {
			return schedule;
		}
		public void setSchedule(Integer schedule) {
			this.schedule = schedule;
		}
		public BigDecimal getAmount() {
			return amount;
		}
		public void setAmount(BigDecimal amount) {
			this.amount = amount;
		}
	}
}
