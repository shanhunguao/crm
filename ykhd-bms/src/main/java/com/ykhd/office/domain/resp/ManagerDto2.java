package com.ykhd.office.domain.resp;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 管理员分页列表信息dto（组织架构）
 */
public class ManagerDto2 {

	private Integer id;
	private String name;
	private String mobile;
	@JsonIgnore
	private Integer department;
	private String deptName;
	@JsonIgnore
	private Integer role;
	private String roleName;
	private boolean leader;
	
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
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public Integer getDepartment() {
		return department;
	}
	public void setDepartment(Integer department) {
		this.department = department;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public Integer getRole() {
		return role;
	}
	public void setRole(Integer role) {
		this.role = role;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public boolean isLeader() {
		return leader;
	}
	public void setLeader(boolean leader) {
		this.leader = leader;
	}
}
