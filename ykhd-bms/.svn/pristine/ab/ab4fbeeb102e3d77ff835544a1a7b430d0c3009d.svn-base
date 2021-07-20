package com.ykhd.office.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 公众号收藏
 */
@TableName("wm_oa_collection")
public class OACollection {

	@TableId(type = IdType.AUTO)
	private Integer id;
	private Integer officeAccount;
	private Integer collector;
	
	public OACollection() {}
	public OACollection(Integer officeAccount, Integer collector) {
		this.officeAccount = officeAccount;
		this.collector = collector;
	}
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
	public Integer getCollector() {
		return collector;
	}
	public void setCollector(Integer collector) {
		this.collector = collector;
	}
}
