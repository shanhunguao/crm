package com.ykhd.office.domain.resp;

/**
 * 绩效打分, 工作任务列表dto
 */
public class WorkTaskDto2 {

	private Integer id;
	private String name;
	private Integer type;
	private Integer state;
	
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
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
}
