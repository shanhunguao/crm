package com.ykhd.office.domain.resp;

/**
 * 二级菜单
 */
public class SecondMenu {

	private String title;
	private String uri;
	private String method;
	
	public SecondMenu(String title, String uri, String method) {
		this.title = title;
		this.uri = uri;
		this.method = method;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
}
