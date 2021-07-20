package com.ykhd.office.domain.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 公众号跟进记录
 */
@TableName("wm_oa_followup")
public class OAFollowUp {

	@TableId(type = IdType.AUTO)
	private Integer id;
	/**公众号id*/
	private Integer officeAccount;
	/**创建人id*/
	private Integer creator;
    /**创建人姓名*/
	private String creatorName;
	/**创建时间*/
	private Date createTime;
	/**跟进内容*/
	private String content;
	
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
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
}
