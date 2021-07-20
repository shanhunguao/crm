package com.ykhd.office.domain.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 工作任务
 */
@TableName("bms_work_task")
public class WorkTask {

	@TableId(type = IdType.AUTO)
	private Integer id;
	/**创建者*/
	private Integer creator;
	/**执行者*/
	private Integer executor;
	/**名称*/
	private String name;
	/**类型 1月计划 2周计划  3日常任务  4其他任务*/
	private Integer type;
	/**描述*/
	private String descrip;
	/**注意事项（任务标准）*/
	private String standard;
	/**创建时间*/
	private Date createTime;
	/**计划完成日期*/
	private String planFinishDate;
	/**实际完成日期*/
	private String actualFinishDate;
	/**优先级 1较低 2普通 3重要 4非常紧急*/
	private Integer priority;
	/**状态*/
	private Integer state;
	/**完成总结*/
	private String summary;
	/**附件图片jsonStr*/
	private String fileList;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getCreator() {
		return creator;
	}
	public void setCreator(Integer creator) {
		this.creator = creator;
	}
	public Integer getExecutor() {
		return executor;
	}
	public void setExecutor(Integer executor) {
		this.executor = executor;
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
	public String getDescrip() {
		return descrip;
	}
	public void setDescrip(String descrip) {
		this.descrip = descrip;
	}
	public String getStandard() {
		return standard;
	}
	public void setStandard(String standard) {
		this.standard = standard;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getPlanFinishDate() {
		return planFinishDate;
	}
	public void setPlanFinishDate(String planFinishDate) {
		this.planFinishDate = planFinishDate;
	}
	public String getActualFinishDate() {
		return actualFinishDate;
	}
	public void setActualFinishDate(String actualFinishDate) {
		this.actualFinishDate = actualFinishDate;
	}
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getFileList() {
		return fileList;
	}
	public void setFileList(String fileList) {
		this.fileList = fileList;
	}
}
