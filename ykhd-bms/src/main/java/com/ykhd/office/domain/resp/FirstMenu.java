package com.ykhd.office.domain.resp;

import java.util.List;

/**
 * 一级菜单
 */
public class FirstMenu {

	private String title;
	private String sign; // web前端交互标记
	private String icon; // 图标
	private List<SecondMenu> children;
	
	public FirstMenu(String title, String sign, String icon, List<SecondMenu> children) {
		this.title = title;
		this.sign = sign;
		this.icon = icon;
		this.children = children;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public List<SecondMenu> getChildren() {
		return children;
	}
	public void setChildren(List<SecondMenu> children) {
		this.children = children;
	}
}
