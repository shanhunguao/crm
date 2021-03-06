package com.ykhd.office.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ykhd.office.domain.req.OASReturnCondition;
import com.ykhd.office.domain.req.OASReturnSubmit;
import com.ykhd.office.service.IOASReturnService;

@RestController
@RequestMapping("/oas_return")
public class OASReturnController extends BaseController {

	@Autowired
	private IOASReturnService oASReturnService;
	
	/**
	 * 排期回款申请分页列表
	 */
	@GetMapping
	public Object list(OASReturnCondition condition) {
		return oASReturnService.getListByPage(condition);
	}
	
	/**
	 * 导出excel
	 */
	@GetMapping("/export")
	public void exportExcel(OASReturnCondition condition, HttpServletResponse response) {
		oASReturnService.exportExcel(condition, response);
	}
	
	/**
	 * 排期已回款总金额（待确认+待复核+已回款）
	 */
	@GetMapping("/total")
	public Object totalReturn(String schedule) {
		Assert.hasText(schedule, "schedule" + nullValue);
		return oASReturnService.totalReturn(Stream.of(schedule.split(",")).map(Integer::valueOf).collect(Collectors.toList()));
	}
	
	/**
	 * 新增回款申请
	 */
	@PostMapping
	public Object add(@Valid OASReturnSubmit submit) {
		return oASReturnService.addOASReturn(submit) ? success(null) : failure(save_failure);
	}
	
	/**
	 * 确认回款
	 */
	@PutMapping("/confirm/{id}")
	public Object confirm(@PathVariable String id, MultipartFile[] files, String date, String remark) {
		Assert.hasText(date, "date" + nullValue);
		return oASReturnService.confirm(Stream.of(id.split(",")).map(Integer::valueOf).collect(Collectors.toList()), files, date, remark);
	}
	
	/**
	 * 驳回申请
	 */
	@PutMapping("/refuse/{id}")
	public Object refuse(@PathVariable String id) {
		return oASReturnService.refuse(Stream.of(id.split(",")).map(Integer::valueOf).collect(Collectors.toList()));
	}
	
	/**
	 * 复核回款
	 */
	@PutMapping("/check/{id}")
	public Object check(@PathVariable Integer id, String filePath, MultipartFile[] files, String date, String remark) {
		Assert.hasText(date, "date" + nullValue);
		List<String> oldPath = null;
		if (StringUtils.hasText(filePath))
			oldPath = new ArrayList<String>(Arrays.asList(filePath.split(",")));
		return oASReturnService.check(id, oldPath, files, date, remark) ? success(null) : failure(update_failure);
	}
	
	/**
	 * 批量复核通过
	 */
	@PutMapping("/checkBatch/{id}")
	public Object checkBatch(@PathVariable String id, MultipartFile[] files, String date) {
		return oASReturnService.checkBatch(Stream.of(id.split(",")).map(Integer::valueOf).collect(Collectors.toList()), files, date);
	}
	
	/**
	 * 驳回回款到待确认状态
	 */
	@PutMapping("/checkFailure/{id}")
	public Object checkFailure(@PathVariable String id, String reason) {
		return oASReturnService.checkFailure(Stream.of(id.split(",")).map(Integer::valueOf).collect(Collectors.toList()), reason);
	}
	
}
