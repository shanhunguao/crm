package com.ykhd.office.domain.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 排期回款申请
 */
@TableName("wm_oas_return")
public class OASReturn {

	@TableId(type = IdType.AUTO)
	private Integer id;
	/**排期id*/
	private Integer schedule;
	/**回款金额*/
	private BigDecimal amount;
	/**收款账户*/
	private String receiveAccount;
	/**付方开户行*/
	private String bankName;
	/**付方开户人*/
	private String accountHolder;
	/**付方银行卡号*/
	private String accountNumber;
	/**申请备注*/
	private String remark;
	/**创建人*/
    private Integer creator;
	/**创建时间*/
	private Date createTime;
	/**状态*/
	private Integer state;
	/**回款日期*/
	private Date returnTime;
	/**回款备注*/
	private String returnRemark;
	/**回款截图存储路径*/
	private String returnImage;
	/**客户是否开票（-1不开票，开票税点）*/
	private Integer custFapiao;
	/**销项发票id*/
	private Integer fapiao;
	/**驳回原因*/
	private String reason;
	
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
	public Integer getCreator() {
		return creator;
	}
	public void setCreator(Integer creator) {
		this.creator = creator;
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
