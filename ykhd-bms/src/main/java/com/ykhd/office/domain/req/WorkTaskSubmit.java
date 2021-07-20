package com.ykhd.office.domain.req;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class WorkTaskSubmit {

	/**执行者id*/
	private Integer executor;
	/**类型*/
	@NotNull
	private Integer type;
	/**任务数据单元集合*/
	@NotEmpty
	private String taskJsonStr;
	
	public Integer getExecutor() {
		return executor;
	}
	public void setExecutor(Integer executor) {
		this.executor = executor;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getTaskJsonStr() {
		return taskJsonStr;
	}
	public void setTaskJsonStr(String taskJsonStr) {
		this.taskJsonStr = taskJsonStr;
	}

	public static class TaskUnit {
		
		/**任务名称*/
		private String name;
		/**描述*/
		private String descrip;
		/**计划完成日期*/
		private String planFinishDate;
		/**优先级 1较低 2普通 3重要 4非常紧急*/
		private Integer priority;
		/**注意事项（标准）*/
		private String standard;
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getDescrip() {
			return descrip;
		}
		public void setDescrip(String descrip) {
			this.descrip = descrip;
		}
		public String getPlanFinishDate() {
			return planFinishDate;
		}
		public void setPlanFinishDate(String planFinishDate) {
			this.planFinishDate = planFinishDate;
		}
		public Integer getPriority() {
			return priority;
		}
		public void setPriority(Integer priority) {
			this.priority = priority;
		}
		public String getStandard() {
			return standard;
		}
		public void setStandard(String standard) {
			this.standard = standard;
		}
	}
}
