package com.ykhd.office.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ykhd.office.domain.req.WorkTaskCondition;
import com.ykhd.office.domain.req.WorkTaskSubmit;
import com.ykhd.office.domain.req.WorkTaskUpdateSubmit;
import com.ykhd.office.service.IContentChangeService;
import com.ykhd.office.service.IWorkTaskService;
import com.ykhd.office.util.dictionary.TypeEnums.Type4Change;

@RestController
@RequestMapping("/workTask")
public class WorkTaskController extends BaseController {

	@Autowired
	private IWorkTaskService workTaskService;
	@Autowired
	private IContentChangeService contentChangeService;
	
	/**
	 * 分页列表
	 */
	@GetMapping
	public Object list(WorkTaskCondition condition) {
		return workTaskService.getListByPage(condition);
	}
	
	/**
	 * 月度工作, 不分页
	 */
	@GetMapping("/month")
	public Object list(Integer manager, String month) {
		Assert.notNull(manager, "manager" + nullValue);
		Assert.notNull(month, "month" + nullValue);
		return workTaskService.getListByMonth(manager, month);
	}
	
	/**
	 * 创建工作任务
	 */
	@PostMapping
	public Object create(@Valid WorkTaskSubmit submit) {
		workTaskService.add(submit);
		return success(null);
	}
	
	/**
	 * 发布工作任务
	 */
	@PostMapping("/publish")
	public Object publish(@Valid WorkTaskSubmit submit) {
		Assert.notNull(submit.getExecutor(), "executor" + nullValue);
		workTaskService.add(submit);
		return success(null);
	}
	
	/**
	 * 修改工作任务
	 */
	@PutMapping("/{id}")
	public Object update(@PathVariable Integer id, @Valid WorkTaskUpdateSubmit submit) {
		submit.setId(id);
		return workTaskService.update(submit) ? success(null) : failure(update_failure);
	}
	
	/**
	 * 详情
	 */
	@GetMapping("/{id}")
	public Object detail(@PathVariable Integer id) {
		return workTaskService.detail(id);
	}
	
	/**
	 * 设置注意事项
	 */
	@PutMapping("/standard/{id}")
	public Object setStandard(@PathVariable Integer id, String standard) {
		return workTaskService.setStandard(id, standard) ? success(null) : failure(update_failure);
	}
	
	/**
	 * 完成工作任务
	 */
	@PutMapping("/finish/{id}")
	public Object finishTask(@PathVariable Integer id, String summary) {
		return workTaskService.finishTask(id, summary) ? success(null) : failure(update_failure);
	}
	
	/**
	 * 作废
	 */
	@PutMapping("/cancel/{id}")
	public Object cancel(@PathVariable Integer id) {
		return workTaskService.cancel(id) ? success(null) : failure(update_failure);
	}
	
	/**
	 * 补充附件图片
	 */
	@PostMapping("/file/{id}")
	public Object uploadFile(@PathVariable Integer id, MultipartFile... files) {
		Assert.notNull(files, "未选择文件");
		Assert.noNullElements(files, "包含空文件");
		return workTaskService.uploadFile(id, files) ? success(null) : failure(save_failure);
	}
	
	/**
	 * 移除附件图片
	 */
	@DeleteMapping("/file/{id}")
	public Object removeFile(@PathVariable Integer id, String filePath) {
		Assert.hasText(filePath, "filePath" + nullValue);
		return workTaskService.removeFile(id, filePath) ? success(null) : failure(delete_failure);
	}
	
	/**
	 * 查询修改记录列表
	 */
	@GetMapping("/contentChange/{id}")
	public Object change(@PathVariable Integer id) {
		return contentChangeService.getContentChangeList(Type4Change.工作任务, id);
	}
}
