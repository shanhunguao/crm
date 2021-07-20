package com.ykhd.office.domain.req;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 公众号排期修改提交信息
 */
public class OAScheduleEdit {

	private Integer id;
	@NotNull(message = "客户名称为空")
	private Integer customer;
	@NotEmpty(message = "投放方式为空")
	private String cooperateMode;
	@NotEmpty(message = "投放日期为空")
	private String putDate;
	//@NotEmpty(message = "投放位置为空")
	private String putPosition;
	private Integer count;
	@NotNull(message = "执行价为空")
	@DecimalMin(value = "0", message = "执行价不低于0")
	private BigDecimal executePrice;
	@NotNull(message = "成本价为空")
	@DecimalMin(value = "0", message = "成本价不低于0")
	private BigDecimal costPrice;
	@DecimalMin(value = "0", message = "销售费用不低于0")
	private BigDecimal sellExpense;
	@DecimalMin(value = "0", message = "渠道费用不低于0")
	private BigDecimal channelExpense;
	@NotNull(message = "客户是否开票为空")
	@Min(-1)
	@Max(6)
	private Integer custFapiao;
	@NotNull(message = "号主是否含票为空")
	@Min(-1)
	@Max(6)
	private Integer oaFapiao;
	@Min(0)
	@Max(1)
	private Integer oaFapiaoType;
	private String remark;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getCustomer() {
		return customer;
	}
	public void setCustomer(Integer customer) {
		this.customer = customer;
	}
	public String getPutDate() {
		return putDate;
	}
	public void setPutDate(String putDate) {
		this.putDate = putDate;
	}
	public String getPutPosition() {
		return putPosition;
	}
	public void setPutPosition(String putPosition) {
		this.putPosition = putPosition;
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
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getCooperateMode() {
		return cooperateMode;
	}
	public void setCooperateMode(String cooperateMode) {
		this.cooperateMode = cooperateMode;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
}
