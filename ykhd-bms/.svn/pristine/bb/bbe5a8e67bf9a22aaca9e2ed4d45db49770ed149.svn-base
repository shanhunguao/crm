package com.ykhd.office.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 部门
 */
@TableName("bms_department")
public class Department {

	@TableId(type = IdType.AUTO)
	private Integer id;
	/**部门名称*/
	private String name;
	/**上级部门id*/
	private Integer parent;
	/**部门领导*/
	private Integer leader;
	
	public Department() {}
	
	public Department(Integer id, String name, Integer parent) {
		this.id = id;
		this.name = name;
		this.parent = parent;
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
	public Integer getParent() {
		return parent;
	}
	public void setParent(Integer parent) {
		this.parent = parent;
	}
	public Integer getLeader() {
		return leader;
	}
	public void setLeader(Integer leader) {
		this.leader = leader;
	}
}
