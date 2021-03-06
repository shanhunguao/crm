package com.ykhd.office.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ykhd.office.domain.req.XunhaoCondition;
import com.ykhd.office.domain.req.XunhaoSubmit;
import com.ykhd.office.service.IXunhaoService;

@RestController
@RequestMapping("/xunhao")
public class XunhaoController extends BaseController {

	@Autowired
	private IXunhaoService xunhaoService;
	
	/**
	 * 分页列表
	 */
	@GetMapping
	public Object list(XunhaoCondition condition) {
		return xunhaoService.getList(condition);
	}
	
	/**
	 * 发起询号
	 */
	@PostMapping
	public Object add(@Valid XunhaoSubmit submit) {
		return xunhaoService.addXunhao(submit) ? success(null) : failure(save_failure);
	}
	
	/**
	 * 确认答复
	 */
	@PutMapping("/{id}")
	public Object reply(@PathVariable Integer id, String content) {
		return xunhaoService.reply(id, content) ? success(null) : failure(update_failure);
	}

}
