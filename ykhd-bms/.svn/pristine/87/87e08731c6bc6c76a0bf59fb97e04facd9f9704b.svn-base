package com.ykhd.office.domain.resp;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 部门结构树
 */
public class DepartmentDto {

	private Integer id;
	private String name;
	@JsonIgnore
	private Integer parent;
	private List<DepartmentDto> children;
	
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
	public Integer getParent() {
		return parent;
	}
	public void setParent(Integer parent) {
		this.parent = parent;
	}
	public List<DepartmentDto> getChildren() {
		return children;
	}
	public void setChildren(List<DepartmentDto> children) {
		this.children = children;
	}
}
