package com.ykhd.office.domain.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 排期支付申请
 */
@TableName("wm_payment_app")
public class PaymentApp {

	@TableId(type = IdType.AUTO)
	private Integer id;
	/**排期id*/
	private Integer schedule;
	/**支付类型*/
	private Integer type;
	/**支付金额*/
	private BigDecimal amount;
	/**付款账户*/
	private String payAccount;
	/**收方开户行*/
	private String bankName;
	/**收方开户人*/
	private String accountHolder;
	/**收方银行卡号*/
	private String accountNumber;
	/**收方私账下的身份证号码*/
	private String idCardNo;
	/**申请备注*/
	private String remark;
	/**创建人*/
	private Integer creator;
	/**创建人名称*/
	private String creatorName;
	/**创建时间*/
	private Date createTime;
	/**状态*/
	private Integer state;
	/**付款人*/
	private Integer payer;
	/**付款时间*/
	private Date paytime;
	/**付款备注*/
	private String payRemark;
	/**号主是否开进项发票(-1不开票，开票税点)*/
	private Integer oaFapiao;
	/**号主开票类型（0 专票  1普票）*/
	private Integer oaFapiaoType;
	/**进项发票id*/
	private Integer fapiao;
	/**支付截图存储路径*/
	private String payImage;
	
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
	public Integer getCreator() {
		return creator;
	}
	public void setCreator(Integer creator) {
		this.creator = creator;
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
	public Integer getPayer() {
		return payer;
	}
	public void setPayer(Integer payer) {
		this.payer = payer;
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
	public Integer getFapiao() {
		return fapiao;
	}
	public void setFapiao(Integer fapiao) {
		this.fapiao = fapiao;
	}
	public String getPayImage() {
		return payImage;
	}
	public void setPayImage(String payImage) {
		this.payImage = payImage;
	}
}
