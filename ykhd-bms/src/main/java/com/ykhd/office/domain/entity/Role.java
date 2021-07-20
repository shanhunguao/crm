package com.ykhd.office.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 角色（岗位）
 */
@TableName("bms_role")
public class Role {

	@TableId(type = IdType.AUTO)
	private Integer id;
	/**名称*/
	private String name;
	/**拥有的权限菜单集*/
	private String menuAuth;
	
	public Role() {}
	public Role(String name) {
		this.name = name;
	}
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
	public String getMenuAuth() {
		return menuAuth;
	}
	public void setMenuAuth(String menuAuth) {
		this.menuAuth = menuAuth;
	}
}
