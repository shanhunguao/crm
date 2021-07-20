package com.ykhd.office.domain.req;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 添加管理员提交信息
 */
public class ManagerSubmit {

	private Integer id;
	@NotEmpty(message = "姓名为空")
	private String name;
	private String password;
	@NotNull(message = "部门为空")
	private Integer department;
	@NotNull(message = "角色为空")
	private Integer role;
	
	public String getName() {
		return name;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Integer getDepartment() {
		return department;
	}
	public void setDepartment(Integer department) {
		this.department = department;
	}
	public Integer getRole() {
		return role;
	}
	public void setRole(Integer role) {
		this.role = role;
	}
}
