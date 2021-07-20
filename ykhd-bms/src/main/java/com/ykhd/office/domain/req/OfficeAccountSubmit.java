package com.ykhd.office.domain.req;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 新增公众号提交信息
 */
public class OfficeAccountSubmit {
	
	private Integer id;
	@NotEmpty(message = "公众号名称为空")
	private String name;
	@NotEmpty(message = "公众号分类为空")
	private String category;
	@NotEmpty(message = "微信号为空")
	private String wechat;
	private String company;
	@DecimalMin(value = "0.01", message = "粉丝数量不低于0.01")
	private BigDecimal fans;
	private String fansSource;
	private String sexRatio;
	@DecimalMin(value = "0", message = "直投刊例价不低于0")
	private BigDecimal headlinePrice;
	@DecimalMin(value = "0", message = "直投成本价不低于0")
	private BigDecimal headlineCost;
	@DecimalMin(value = "0", message = "其他条直投刊例价不低于0")
	private BigDecimal notheadPrice;
	@DecimalMin(value = "0", message = "其他条直投成本价不低于0")
	private BigDecimal notheadCost;
	@DecimalMin(value = "0", message = "CPA单价不低于0")
	private BigDecimal cpaPrice;
	@NotNull(message = "是否含发票为空")
	@Min(0)
	@Max(1)
	private Integer isFapiao;
	@DecimalMin(value = "0", message = "税点不低于0")
	private BigDecimal fapiaoPoint;
	private String refuseType;
	@NotEmpty(message = "是否刷号为空")
	private String isBrush;
	//@NotEmpty(message = "号主联系方式为空")
	private String phone;
	private String remark;
	private String cooperateMode;
	private String customerType;
	private int unDevelop; //0正常  1待开发
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getWechat() {
		return wechat;
	}
	public void setWechat(String wechat) {
		this.wechat = wechat;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public BigDecimal getFans() {
		return fans;
	}
	public String getSexRatio() {
		return sexRatio;
	}
	public void setSexRatio(String sexRatio) {
		this.sexRatio = sexRatio;
	}
	public void setFans(BigDecimal fans) {
		this.fans = fans;
	}
	public BigDecimal getHeadlinePrice() {
		return headlinePrice;
	}
	public void setHeadlinePrice(BigDecimal headlinePrice) {
		this.headlinePrice = headlinePrice;
	}
	public BigDecimal getHeadlineCost() {
		return headlineCost;
	}
	public void setHeadlineCost(BigDecimal headlineCost) {
		this.headlineCost = headlineCost;
	}
	public BigDecimal getNotheadPrice() {
		return notheadPrice;
	}
	public void setNotheadPrice(BigDecimal notheadPrice) {
		this.notheadPrice = notheadPrice;
	}
	public BigDecimal getNotheadCost() {
		return notheadCost;
	}
	public void setNotheadCost(BigDecimal notheadCost) {
		this.notheadCost = notheadCost;
	}
	public Integer getIsFapiao() {
		return isFapiao;
	}
	public void setIsFapiao(Integer isFapiao) {
		this.isFapiao = isFapiao;
	}
	public BigDecimal getFapiaoPoint() {
		return fapiaoPoint;
	}
	public void setFapiaoPoint(BigDecimal fapiaoPoint) {
		this.fapiaoPoint = fapiaoPoint;
	}
	public String getRefuseType() {
		return refuseType;
	}
	public void setRefuseType(String refuseType) {
		this.refuseType = refuseType;
	}
	public String getIsBrush() {
		return isBrush;
	}
	public void setIsBrush(String isBrush) {
		this.isBrush = isBrush;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public int getUnDevelop() {
		return unDevelop;
	}
	public void setUnDevelop(int unDevelop) {
		this.unDevelop = unDevelop;
	}
	public String getFansSource() {
		return fansSource;
	}
	public void setFansSource(String fansSource) {
		this.fansSource = fansSource;
	}
	public BigDecimal getCpaPrice() {
		return cpaPrice;
	}
	public void setCpaPrice(BigDecimal cpaPrice) {
		this.cpaPrice = cpaPrice;
	}
	public String getCooperateMode() {
		return cooperateMode;
	}
	public void setCooperateMode(String cooperateMode) {
		this.cooperateMode = cooperateMode;
	}
	public String getCustomerType() {
		return customerType;
	}
	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}
}
