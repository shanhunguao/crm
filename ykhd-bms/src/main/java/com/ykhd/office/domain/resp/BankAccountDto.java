package com.ykhd.office.domain.resp;

/**
 * 银行账户列表dto
 */
public class BankAccountDto {
	
	private Integer id;
	/**开户行*/
	private String bankName;
	/**开户人*/
	private String accountHolder;
	/**银行卡号*/
	private String accountNumber;
	/**身份证号码*/
	private String idCardNo;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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
}
