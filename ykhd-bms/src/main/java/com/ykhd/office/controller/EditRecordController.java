package com.ykhd.office.controller;

import java.util.Arrays;
import java.util.stream.Collectors;

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

import com.ykhd.office.domain.req.OAScheduleEdit;
import com.ykhd.office.domain.resp.EditRecordCondition;
import com.ykhd.office.service.IEditRecordService;

@RestController
@RequestMapping("/editRecord")
public class EditRecordController extends BaseController {

	@Autowired
	private IEditRecordService editRecordService;
	
	/**
	 * 创建公众号排期修改申请
	 */
	@PostMapping("/oasche")
	public Object addOASchedule(@Valid OAScheduleEdit edit, Integer scheduleId) {
		Assert.notNull(scheduleId, "scheduleId" + nullValue);
		edit.setId(scheduleId);
		return editRecordService.addOAScheduleEditRecord(edit) ? success(null) : failure(save_failure);
	}
	
	/**
	 * 无差别删除
	 */
	@DeleteMapping("/{id}")
	public Object delete(@PathVariable String id) {
		if (id.contains(",")) {
			String[] array = id.split(",");
			Integer[] ids = Arrays.stream(array).map(v -> Integer.valueOf(v)).collect(Collectors.toList()).toArray(new Integer[array.length]);
			return editRecordService.deleteEditRecord(ids);
		} else
			return editRecordService.deleteEditRecord(Integer.valueOf(id));
	}
	
	/**
	 * 查看申请修改的公众号排期
	 */
	@GetMapping("/oasche")
	public Object getOAScheduleList(EditRecordCondition condition) {
		return editRecordService.oAScheduleEditList(condition);
	}
	
	/**
	 * 审核修改的公众号排期（销售主管）
	 */
	@PutMapping("/oasche/review/{id}")
	public Object reviewOASchedule(@PathVariable String id, Integer result, String reason) {
		Assert.notNull(result, "result" + nullValue);
		if (id.contains(",")) {
			String[] array = id.split(",");
			Integer[] ids = Arrays.stream(array).map(v -> Integer.valueOf(v)).collect(Collectors.toList()).toArray(new Integer[array.length]);
			return editRecordService.reviewOAScheduleEditRecord(result, reason, ids);
		} else
			return editRecordService.reviewOAScheduleEditRecord(result, reason, Integer.valueOf(id));
	}
	
}
