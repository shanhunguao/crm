package com.ykhd.office.domain.resp;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 公众号列表dto
 */
public class OfficeAccountDto {

	private Integer id;
	private String name;
	private String category;
	private String wechat;
	private BigDecimal headlineCost;
	private BigDecimal cpaPrice;
	private Integer state;
	private Integer scheduleCount;
	private Boolean isCollection;
	private String score;
	private String comment;
	private String cooperateMode;
	private String customerType;
	private String creatorName; // 创建媒介
	private String mediumName; // 对接媒介
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date createTime;
	private Integer advantage; // 优势号
	private Integer advantageMedium; // 优势号专属媒介
	@JsonIgnore
	private Integer creator; //创建媒介
	private Integer mediumId; //对接媒介
	private Integer agreement; // 协议号
	/**最后更新时间*/
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date lastUpdateTime;
	
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
	public BigDecimal getHeadlineCost() {
		return headlineCost;
	}
	public void setHeadlineCost(BigDecimal headlineCost) {
		this.headlineCost = headlineCost;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public Integer getScheduleCount() {
		return scheduleCount;
	}
	public void setScheduleCount(Integer scheduleCount) {
		this.scheduleCount = scheduleCount;
	}
	public Boolean getIsCollection() {
		return isCollection;
	}
	public void setIsCollection(Boolean isCollection) {
		this.isCollection = isCollection;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
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
	public String getCreatorName() {
		return creatorName;
	}
	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}
	public String getMediumName() {
		return mediumName;
	}
	public void setMediumName(String mediumName) {
		this.mediumName = mediumName;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Integer getAdvantage() {
		return advantage;
	}
	public void setAdvantage(Integer advantage) {
		this.advantage = advantage;
	}
	public Integer getAdvantageMedium() {
		return advantageMedium;
	}
	public void setAdvantageMedium(Integer advantageMedium) {
		this.advantageMedium = advantageMedium;
	}
	public Integer getCreator() {
		return creator;
	}
	public void setCreator(Integer creator) {
		this.creator = creator;
	}
	public Integer getMediumId() {
		return mediumId;
	}
	public void setMediumId(Integer mediumId) {
		this.mediumId = mediumId;
	}
	public Integer getAgreement() {
		return agreement;
	}
	public void setAgreement(Integer agreement) {
		this.agreement = agreement;
	}

	public Boolean getCollection() {
		return isCollection;
	}

	public void setCollection(Boolean collection) {
		isCollection = collection;
	}

	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
}
