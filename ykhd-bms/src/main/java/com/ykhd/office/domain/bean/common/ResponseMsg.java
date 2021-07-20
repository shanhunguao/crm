package com.ykhd.office.domain.bean.common;

/**
 * 响应消息类
 */
public final class ResponseMsg {
	
	/**HTTP状态码*/
	private int code;
	/**消息提示*/
	private String msg;
	/**主体数据*/
	private Object data;
	
	public ResponseMsg(int code, String msg, Object data) {
		this.code = code;
		this.msg = msg;
		this.data = data;
	}
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
}
