package com.ykhd.office.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 银行账户信息
 */
@TableName("wm_bank_account")
public class BankAccount {
	
	@TableId(type = IdType.AUTO)
	private Integer id;
	/**主体类别*/
	private Integer type;
	/**主体id*/
	private Integer identity;
	/**开户行*/
	private String bankName;
	/**开户人*/
	private String accountHolder;
	/**银行卡号*/
	private String accountNumber;
	/**身份证号码*/
	private String idCardNo;
	
	public BankAccount() {}
	public BankAccount(Integer type, Integer identity, String bankName, String accountHolder,
			String accountNumber) {
		this.type = type;
		this.identity = identity;
		this.bankName = bankName;
		this.accountHolder = accountHolder;
		this.accountNumber = accountNumber;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getIdentity() {
		return identity;
	}
	public void setIdentity(Integer identity) {
		this.identity = identity;
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
