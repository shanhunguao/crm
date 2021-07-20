package com.ykhd.office.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ykhd.office.domain.bean.common.PageHelpers;
import com.ykhd.office.domain.entity.WorkTask;
import com.ykhd.office.domain.req.WorkTaskCondition;
import com.ykhd.office.domain.req.WorkTaskSubmit;
import com.ykhd.office.domain.req.WorkTaskUpdateSubmit;
import com.ykhd.office.domain.resp.WorkTaskDetail;
import com.ykhd.office.domain.resp.WorkTaskDto;
import com.ykhd.office.domain.resp.WorkTaskDto2;

public interface IWorkTaskService extends IService<WorkTask> {

	/**
	 * 分页查询
	 */
	PageHelpers<WorkTaskDto> getListByPage(WorkTaskCondition condition);
	
	/**
	 * 月度工作, 不分页
	 */
	List<WorkTaskDto2> getListByMonth(Integer executor, String month);
	
	/**
	 * 创建工作任务
	 */
	void add(WorkTaskSubmit submit);
	
	/**
	 * 修改
	 */
	boolean update(WorkTaskUpdateSubmit submit);
	
	/**
	 * 详情
	 */
	WorkTaskDetail detail(Integer id);
	
	/**
	 * 设置注意事项
	 */
	boolean setStandard(Integer id, String standard);
	
	/**
	 * 完成工作任务
	 */
	boolean finishTask(Integer id, String summary);
	
	/**
	 * 作废
	 */
	boolean cancel(Integer id);
	
	/**
	 * 上传文件
	 */
	boolean uploadFile(Integer id, MultipartFile... files);
	
	/**
	 * 删除文件
	 */
	boolean removeFile(Integer id, String filePath);
	
}
