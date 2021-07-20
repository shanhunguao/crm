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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ykhd.office.domain.req.PaymentAppCondition;
import com.ykhd.office.domain.req.PaymentAppSubmit;
import com.ykhd.office.service.IPaymentAppService;

@RestController
@RequestMapping("/paymentApp")
public class PaymentAppController extends BaseController {

	@Autowired
	private IPaymentAppService paymentAppService;
	
	/**
	 * 支付申请列表
	 */
	@GetMapping
	public Object list(PaymentAppCondition condition) {
		return paymentAppService.getListByPage(condition);
	}
	
	/**
	 * 导出excel
	 */
	@GetMapping("/export")
	public void exportExcel(PaymentAppCondition condition, HttpServletResponse response) {
		paymentAppService.exportExcel(condition, response);
	}
	
	/**
	 * 发起排期支付申请（媒介）
	 */
	@PostMapping
	public Object add(@Valid PaymentAppSubmit submit) {
		return paymentAppService.addPaymentApp(submit) ? success(null) :failure(save_failure);
	}
	
	/**
	 * 驳回申请（出纳）
	 */
	@PutMapping("/refuse/{id}")
	public Object refuse(@PathVariable String id) {
		if (id.contains(",")) {
			String[] array = id.split(",");
			Integer[] ids = Arrays.stream(array).map(v -> Integer.valueOf(v)).collect(Collectors.toList()).toArray(new Integer[array.length]);
			return paymentAppService.refuse(ids);
		} else
			return paymentAppService.refuse(Integer.valueOf(id));
	}
	
	/**
	 * 支付（出纳）
	 */
	@PutMapping("/pay/{id}")
	public Object pay(@PathVariable String id, MultipartFile[] files, String date, String remark) {
		return paymentAppService.pay(Stream.of(id.split(",")).map(Integer::valueOf).collect(Collectors.toList()), files, date, remark) ? success(null) :failure(update_failure);
	}
	
	/**
	 * 复核支付（财务主管）
	 */
	@PutMapping("/check/{id}")
	public Object check(@PathVariable Integer id, String filePath, MultipartFile[] files, String date, String remark) {
		Assert.hasText(date, "date" + nullValue);
		List<String> oldPath = null;
		if (StringUtils.hasText(filePath))
			oldPath = new ArrayList<String>(Arrays.asList(filePath.split(",")));
		return paymentAppService.check(id, oldPath, files, date, remark) ? success(null) :failure(update_failure);
	}
	
	/**
	 * 批量复核通过
	 */
	@PutMapping("/checkBatch/{id}")
	public Object checkBatch(@PathVariable String id, MultipartFile[] files) {
		return paymentAppService.checkBatch(Stream.of(id.split(",")).map(Integer::valueOf).collect(Collectors.toList()), files);
	}
	
	/**
	 * 驳回支付到待支付（财务主管）
	 */
	@PutMapping("/checkFailure/{id}")
	public Object checkFailure(@PathVariable String id) {
		if (id.contains(",")) {
			String[] array = id.split(",");
			Integer[] ids = Arrays.stream(array).map(v -> Integer.valueOf(v)).collect(Collectors.toList()).toArray(new Integer[array.length]);
			return paymentAppService.checkFailure(ids);
		} else
			return paymentAppService.checkFailure(Integer.valueOf(id));
	}
	
//	/**
//	 * 修改支付信息
//	 */
//	@PutMapping("/payInfo/{id}")
//	public Object updatePayInfo(@PathVariable Integer id, MultipartFile file, String date, String remark) {
//		Assert.hasText(date, "date" + nullValue);
//		return paymentAppService.updatePayInfo(id, file, date, remark) ? success(null) :failure(update_failure);
//	}
	
	/**
	 * 查询已申请支付的总金额
	 */
	@GetMapping("/paid")
	public Object paid(Integer scheduleId) {
		Assert.notNull(scheduleId, "scheduleId" + nullValue);
		return paymentAppService.totalByPayAndApp(scheduleId);
	}
	
	/**
	 * 删除
	 */
	@DeleteMapping("/{id}")
	public Object delete(@PathVariable Integer id) {
		return paymentAppService.deletePaymentApp(id) ? success(null) :failure(delete_failure);
	}
	
}
