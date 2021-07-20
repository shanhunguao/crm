package com.ykhd.office.domain.resp;

import java.math.BigDecimal;
import java.util.Date;

import cn.gjing.tools.excel.Excel;
import cn.gjing.tools.excel.ExcelField;
import cn.gjing.tools.excel.convert.ExcelDataConvert;

@Excel("公众号排期")
public class OASchedule_Excel {

	@ExcelField("编号")
	private Integer id;
	@ExcelField("公众号名称")
	private String oaName;
	@ExcelField("微信号")
	private String wechat;
	@ExcelField("粉丝（万）")
	private BigDecimal fans;
	@ExcelField("客户品牌")
	private String customerName;
	@ExcelField("客户公司")
	private String company;
	@ExcelField("合作模式")
	private String cooperateMode;
	@ExcelField(value = "投放日期", format = "yyyy-MM-dd")
	private Date putDate;
	@ExcelField("投放位置")
	private String putPosition;
	@ExcelField("cap投放数量")
	private Integer count;
	@ExcelField("执行价")
	private BigDecimal executePrice;
	@ExcelField("成本价")
	private BigDecimal costPrice;
	@ExcelField("销售费用")
	private BigDecimal sellExpense;
	@ExcelField("渠道费用")
	private BigDecimal channelExpense;
	@ExcelField("已垫付天数")
	private Integer advancePay;
	@ExcelField("客户是否开票")
	@ExcelDataConvert(expr1 = "#custFapiao==-1?'':#custFapiao+'%'")
	private Integer custFapiao;
	@ExcelField("号主是否含票")
	@ExcelDataConvert(expr1 = "#oaFapiao==-1?'':#oaFapiao+'%'")
	private Integer oaFapiao;
	@ExcelField("号主含票类型")
	@ExcelDataConvert(expr1 = "#oaFapiaoType==0?'专票':'普票'")
	private Integer oaFapiaoType;
	@ExcelField("对接媒介")
	private String mediumName;
	@ExcelField("媒介确认备注")
	private String confirmRemark;
	@ExcelField("创建备注")
	private String remark;
	@ExcelField(value = "创建时间", format = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	@ExcelField("创建AE")
	private String creatorName;
	@ExcelField("状态")
	@ExcelDataConvert(expr1 = "#state==0?'创建':#state==1?'提交待审核':#state==2?'待确认':#state==3?'审核不通过':#state==4?'待结算':#state==5?'已完成':#state==9?'作废':'未知'")
	private Integer state;
	@ExcelField("客户税额")
	private String customerTax;
	@ExcelField("渠道税额")
	private String channelTax;
	@ExcelField("利润")
	private String profit;
	@ExcelField("利润率")
	private String profitRate;
	@ExcelField("是否支付")
	@ExcelDataConvert(expr1 = "#isPay==null?'未支付':'已支付'")
	private Integer isPay;
	@ExcelField("是否回款")
	@ExcelDataConvert(expr1 = "#isReturnMoney==null?'未回款':'已回款'")
	private Integer isReturnMoney;
	@ExcelField(value = "回款日期", format = "yyyy-MM-dd")
    private Date returnTime;
	@ExcelField("回款备注")
	private String returnRemark;
	@ExcelField("付款备注")
	private String payRemark;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getOaName() {
		return oaName;
	}
	public void setOaName(String oaName) {
		this.oaName = oaName;
	}
	public String getWechat() {
		return wechat;
	}
	public void setWechat(String wechat) {
		this.wechat = wechat;
	}
	public BigDecimal getFans() {
		return fans;
	}
	public void setFans(BigDecimal fans) {
		this.fans = fans;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
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
	public Integer getAdvancePay() {
		return advancePay;
	}
	public void setAdvancePay(Integer advancePay) {
		this.advancePay = advancePay;
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
	public String getMediumName() {
		return mediumName;
	}
	public void setMediumName(String mediumName) {
		this.mediumName = mediumName;
	}
	public String getConfirmRemark() {
		return confirmRemark;
	}
	public void setConfirmRemark(String confirmRemark) {
		this.confirmRemark = confirmRemark;
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
	public String getCustomerTax() {
		return customerTax;
	}
	public void setCustomerTax(String customerTax) {
		this.customerTax = customerTax;
	}
	public String getChannelTax() {
		return channelTax;
	}
	public void setChannelTax(String channelTax) {
		this.channelTax = channelTax;
	}
	public String getProfit() {
		return profit;
	}
	public void setProfit(String profit) {
		this.profit = profit;
	}
	public String getProfitRate() {
		return profitRate;
	}
	public void setProfitRate(String profitRate) {
		this.profitRate = profitRate;
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
	public String getReturnRemark() {
		return returnRemark;
	}
	public void setReturnRemark(String returnRemark) {
		this.returnRemark = returnRemark;
	}
	public String getPayRemark() {
		return payRemark;
	}
	public void setPayRemark(String payRemark) {
		this.payRemark = payRemark;
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
