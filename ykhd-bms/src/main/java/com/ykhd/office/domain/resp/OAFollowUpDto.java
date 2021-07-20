package com.ykhd.office.domain.resp;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 跟进记录集合dto
 */
public class OAFollowUpDto {

	private String creatorName;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
	private Date createTime;
	private String content;
	
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
