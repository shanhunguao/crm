package com.ykhd.office.controller;

import java.util.Arrays;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;
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

import com.ykhd.office.domain.bean.common.PageCondition;
import com.ykhd.office.domain.req.OAScheduleCondition;
import com.ykhd.office.domain.req.OAScheduleCondition2;
import com.ykhd.office.domain.req.OAScheduleEdit;
import com.ykhd.office.domain.req.OAScheduleSubmit;
import com.ykhd.office.service.IOAScheduleService;

@RestController
@RequestMapping("/oasche")
public class OAScheduleController extends BaseController {

	@Autowired
	private IOAScheduleService oAScheduleService;
	
	/**
	 * 公众号排期分页列表
	 */
	@GetMapping
	public Object list(OAScheduleCondition condition) {
		return oAScheduleService.getListByPage(condition);
	}
	
	/**
	 * 排期导出excel
	 */
	@GetMapping("/export")
	public void exportExcel(OAScheduleCondition condition, HttpServletResponse response) {
		oAScheduleService.exportExcel(condition, response);
	}
	
	/**
	 * 公众号排期“次数”分页列表
	 */
	@GetMapping("/4oa")
	public Object list2(@Valid OAScheduleCondition2 condition2) {
		return oAScheduleService.getListByPage2(condition2);
	}
	
	/**
	 * 新建排期（销售）
	 */
	@PostMapping
	public Object add(@Valid OAScheduleSubmit submit) {
		return oAScheduleService.addOASchedule(submit) ? success(null) : failure(save_failure);
	}
	
	/**
	 * 提交排期（销售）
	 */
	@PutMapping("/submit/{id}")
	public Object submit(@PathVariable String id) {
		if (id.contains(",")) {
			String[] array = id.split(",");
			Integer[] ids = Arrays.stream(array).map(v -> Integer.valueOf(v)).collect(Collectors.toList()).toArray(new Integer[array.length]);
			return oAScheduleService.submitState(ids);
		} else
			return oAScheduleService.submitState(Integer.valueOf(id));
	}
	
	/**
	 * 审核排期（销售主管）
	 */
	@PutMapping("/review/{id}")
	public Object review(@PathVariable String id, Integer result, String reason) {
		Assert.notNull(result, "result" + nullValue);
		if (id.contains(",")) {
			String[] array = id.split(",");
			Integer[] ids = Arrays.stream(array).map(v -> Integer.valueOf(v)).collect(Collectors.toList()).toArray(new Integer[array.length]);
			return oAScheduleService.review(result, reason, ids);
		} else
			return oAScheduleService.review(result, reason, Integer.valueOf(id));
	}
	
	/**
	 * 确认排期（媒介）
	 */
	@PutMapping("/confirm/{id}")
	public Object confirm(@PathVariable String id, String remark) {
		if (id.contains(",")) {
			String[] array = id.split(",");
			Integer[] ids = Arrays.stream(array).map(v -> Integer.valueOf(v)).collect(Collectors.toList()).toArray(new Integer[array.length]);
			return oAScheduleService.confirm(remark, ids);
		} else
			return oAScheduleService.confirm(remark, Integer.valueOf(id));
	}
	
	/**
	 * 申请作废（销售）
	 */
	@PutMapping("/appCancel/{id}")
	public Object appCancel(@PathVariable String id) {
		if (id.contains(",")) {
			String[] array = id.split(",");
			Integer[] ids = Arrays.stream(array).map(v -> Integer.valueOf(v)).collect(Collectors.toList()).toArray(new Integer[array.length]);
			return oAScheduleService.appCancel(ids);
		} else
			return oAScheduleService.appCancel(Integer.valueOf(id));
	}
	
	/**
	 * 审核作废（销售主管）
	 */
	@PutMapping("/reviewCancel/{id}")
	public Object reviewCancel(@PathVariable String id, Integer result, String reason) {
		Assert.notNull(result, "result" + nullValue);
		if (id.contains(",")) {
			String[] array = id.split(",");
			Integer[] ids = Arrays.stream(array).map(v -> Integer.valueOf(v)).collect(Collectors.toList()).toArray(new Integer[array.length]);
			return oAScheduleService.reviewCancel(result, reason, ids);
		} else
			return oAScheduleService.reviewCancel(result, reason, Integer.valueOf(id));
	}
	
	/**
	 * 确认作废（媒介）
	 */
	@PutMapping("/confirmCancel/{id}")
	public Object confirmCancel(@PathVariable String id, Integer result, String reason) {
		Assert.notNull(result, "result" + nullValue);
		if (id.contains(",")) {
			String[] array = id.split(",");
			Integer[] ids = Arrays.stream(array).map(v -> Integer.valueOf(v)).collect(Collectors.toList()).toArray(new Integer[array.length]);
			return oAScheduleService.confirmCancel(result, reason, ids);
		} else
			return oAScheduleService.confirmCancel(result, reason, Integer.valueOf(id));
	}
	
	/**
	 * 直接修改未提交或审核失败的排期（销售）
	 */
	@PutMapping("/info/{id}")
	public Object updateInfo(@Valid OAScheduleEdit edit, @PathVariable Integer id) {
		edit.setId(id);
		return oAScheduleService.updateInfo(edit) ? success(null) : failure(update_failure);
	}
	
	/**
	 * 申请作废的公众号排期（马甲）
	 */
	@GetMapping("/4cancel")
	public Object list3(OAScheduleCondition condition) {
		return oAScheduleService.getListByPage(condition);
	}
	
	/**
	 * 删除排期
	 */
	@DeleteMapping("/{id}")
	public Object delete(@PathVariable String id) {
		if (id.contains(",")) {
			String[] array = id.split(",");
			Integer[] ids = Arrays.stream(array).map(v -> Integer.valueOf(v)).collect(Collectors.toList()).toArray(new Integer[array.length]);
			return oAScheduleService.deleteOASchedule(ids);
		} else
			return oAScheduleService.deleteOASchedule(Integer.valueOf(id));
	}
	
	/**
	 * 待集采确认的排期列表
	 */
	@GetMapping("/4jc")
	public Object list4jc(PageCondition condition) {
		return oAScheduleService.list4jc(condition);
	}
	
}
