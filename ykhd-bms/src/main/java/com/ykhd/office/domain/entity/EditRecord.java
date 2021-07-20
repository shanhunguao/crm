package com.ykhd.office.domain.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 修改申请记录
 */
@TableName("wm_edit_record")
public class EditRecord {

	@TableId(type = IdType.AUTO)
	private Integer id;
	/**主体类别*/
	private Integer type;
	/**主体id*/
	private Integer identity;
	/**内容jsonString*/
	private String content;
	/**状态*/
	private Integer state;
	/**创建人*/
	private Integer creator;
	/**创建人姓名*/
	private String creatorName;
	/**创建日期*/
	private Date createTime;
	/**审核人*/
	private Integer reviewer;
	/**不通过原因*/
	private String failureReason;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getIdentity() {
		return identity;
	}
	public void setIdentity(Integer identity) {
		this.identity = identity;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
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
	public Integer getReviewer() {
		return reviewer;
	}
	public void setReviewer(Integer reviewer) {
		this.reviewer = reviewer;
	}
	public String getFailureReason() {
		return failureReason;
	}
	public void setFailureReason(String failureReason) {
		this.failureReason = failureReason;
	}
}
