package com.ykhd.office.domain.bean;

import java.math.BigDecimal;

/**
 * 排期列表显示的公众号简单信息
 */
public class OASimpleInfo {

	private Integer id; //排期id
	private String name; //公众号名称
	private String wechat; //公众号微信号
	private BigDecimal fans; //公众号粉丝
	private String brand; //客户品牌名称
	private String company; //客户公司
	
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
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
}
