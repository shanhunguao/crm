package com.ykhd.office.domain.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 公众号排期
 */
@TableName("wm_oa_schedule")
public class OASchedule {

	@TableId(type = IdType.AUTO)
	private Integer id;
	/**公众号id*/
	private Integer officeAccount;
	/**客户（品牌）id*/
	private Integer customer;
	private String brand;
	/**合作模式*/
	private String cooperateMode;
	/**投放日期*/
	private Date putDate;
	/**投放位置（直投）*/
	private String putPosition;
	/**投放数量（CPA）*/
	private Integer count;
	/**执行价*/
	private BigDecimal executePrice;
	/**成本价*/
	private BigDecimal costPrice;
	/**销售费用（额外支出）*/
	private BigDecimal sellExpense;
	/**渠道费用（额外收入）*/
	private BigDecimal channelExpense;
	/**客户是否开票（-1不开票，开票税点）*/
	private Integer custFapiao;
	/**号主是否含票（-1不含票，开票税点）*/
	private Integer oaFapiao;
	/**号主含票类型（0 专票  1普票）*/
	private Integer oaFapiaoType;
	/**媒介id*/
	private Integer medium;
	/**媒介名称*/
	private String mediumName;
	/**媒介确认备注*/
	private String confirmRemark;
	/**创建备注*/
	private String remark;
	/**创建时间*/
	private Date createTime;
	/**创建人id*/
	private Integer creator;
	/**创建人姓名*/
	private String creatorName;
	/**状态*/
	private Integer state;
	/**审核人*/
	private Integer reviewer;
	/**审核不通过原因*/
	private String failureReason;
	
	/**是否已支付 null未 1支付完成*/
	private Integer isPay;
	/**是否已回款 null未 1已回款*/
	private Integer isReturnMoney;
	/**回款日期*/
	private Date returnTime;
	
    /**申请作废状态*/
    private Integer appCancel;
    /**作废不通过原因*/
    private String cancelFailureReason;
    
    /**优势号的排期标记*/
    private Integer advantage;
    
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getOfficeAccount() {
		return officeAccount;
	}
	public void setOfficeAccount(Integer officeAccount) {
		this.officeAccount = officeAccount;
	}
	public Integer getCustomer() {
		return customer;
	}
	public void setCustomer(Integer customer) {
		this.customer = customer;
	}
	public String getCooperateMode() {
		return cooperateMode;
	}
	public void setCooperateMode(String cooperateMode) {
		this.cooperateMode = cooperateMode;
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
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public BigDecimal getExecutePrice() {
		return executePrice;
	}
	public void setExecutePrice(BigDecimal executePrice) {
		this.executePrice = executePrice;
	}
	public BigDecimal getCostPrice() {
		return costPrice;
	}
	public void setCostPrice(BigDecimal costPrice) {
		this.costPrice = costPrice;
	}
	public BigDecimal getSellExpense() {
		return sellExpense;
	}
	public void setSellExpense(BigDecimal sellExpense) {
		this.sellExpense = sellExpense;
	}
	public BigDecimal getChannelExpense() {
		return channelExpense;
	}
	public void setChannelExpense(BigDecimal channelExpense) {
		this.channelExpense = channelExpense;
	}
	public Integer getCustFapiao() {
		return custFapiao;
	}
	public void setCustFapiao(Integer custFapiao) {
		this.custFapiao = custFapiao;
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
	public Integer getMedium() {
		return medium;
	}
	public void setMedium(Integer medium) {
		this.medium = medium;
	}
	public String getMediumName() {
		return mediumName;
	}
	public void setMediumName(String mediumName) {
		this.mediumName = mediumName;
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
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public Integer getReviewer() {
		return reviewer;
	}
	public void setReviewer(Integer reviewer) {
		this.reviewer = reviewer;
	}
	public String getConfirmRemark() {
		return confirmRemark;
	}
	public void setConfirmRemark(String confirmRemark) {
		this.confirmRemark = confirmRemark;
	}
	public String getFailureReason() {
		return failureReason;
	}
	public void setFailureReason(String failureReason) {
		this.failureReason = failureReason;
	}
	public Integer getIsPay() {
		return isPay;
	}
	public void setIsPay(Integer isPay) {
		this.isPay = isPay;
	}
	public Integer getIsReturnMoney() {
		return isReturnMoney;
	}
	public void setIsReturnMoney(Integer isReturnMoney) {
		this.isReturnMoney = isReturnMoney;
	}
	public Date getReturnTime() {
		return returnTime;
	}
	public void setReturnTime(Date returnTime) {
		this.returnTime = returnTime;
	}
	public Integer getAppCancel() {
		return appCancel;
	}
	public void setAppCancel(Integer appCancel) {
		this.appCancel = appCancel;
	}
	public String getCancelFailureReason() {
		return cancelFailureReason;
	}
	public void setCancelFailureReason(String cancelFailureReason) {
		this.cancelFailureReason = cancelFailureReason;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public Integer getAdvantage() {
		return advantage;
	}
	public void setAdvantage(Integer advantage) {
		this.advantage = advantage;
	}
}
