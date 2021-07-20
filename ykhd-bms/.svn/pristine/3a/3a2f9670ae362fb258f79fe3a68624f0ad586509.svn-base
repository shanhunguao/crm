package com.ykhd.office.domain.bean;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class MsgSentBean {
	
	private Integer sender;
	private Integer receiver;
	private Integer type;
	private Object data;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;
	
	public MsgSentBean() {}
	public MsgSentBean(Integer receiver, Integer type, Object data) {
		this.receiver = receiver;
		this.type = type;
		this.data = data;
	}
	public Integer getSender() {
		return sender;
	}
	public void setSender(Integer sender) {
		this.sender = sender;
	}
	public Integer getReceiver() {
		return receiver;
	}
	public void setReceiver(Integer receiver) {
		this.receiver = receiver;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
