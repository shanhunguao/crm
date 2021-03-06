package com.ykhd.office.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ykhd.office.domain.req.OAScheduleCondition;
import com.ykhd.office.service.IJcOAScheduleService;

/**
 * 集采
 */
@RestController
@RequestMapping("/jc_oasche")
public class JcOAScheduleController extends BaseController {
	
	@Autowired
	private IJcOAScheduleService jcOAScheduleService;

	/**
	 * 确认公众号排期
	 */
	@PostMapping("/confirm_create")
	public Object confirm_create(Integer schedule, Integer oaFapiao, Integer oaFapiaoType) {
		return jcOAScheduleService.confirm_create(schedule, oaFapiao, oaFapiaoType) ? success(null) :failure(save_failure);
	}
	
	/**
	 * 公众号排期分页列表
	 */
	@GetMapping
	public Object list(OAScheduleCondition condition) {
		return jcOAScheduleService.getListByPage(condition);
	}
	
	/**
	 * 排期导出excel
	 */
	@GetMapping("/export")
	public void exportExcel(OAScheduleCondition condition, HttpServletResponse response) {
		jcOAScheduleService.exportExcel(condition, response);
	}
}
