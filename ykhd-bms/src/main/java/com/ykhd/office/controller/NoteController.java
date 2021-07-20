package com.ykhd.office.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ykhd.office.service.INoteService;

@RestController
@RequestMapping("/note")
public class NoteController extends BaseController {

	@Autowired
	private INoteService noteService;
	
	/**
	 * 批注历史
	 */
	@GetMapping
	public Object list(Integer manager) {
		Assert.notNull(manager, "manager" + nullValue);
		return noteService.getList(manager);
	}
	
	/**
	 * 绩效考核月内的批注
	 */
	@GetMapping("/4Examine")
	public Object examine(Integer manager, String month) {
		Assert.notNull(manager, "manager" + nullValue);
		Assert.hasText(month, "month" + nullValue);
		return noteService.getList4Examine(manager, month);
	}
	
	/**
	 * 添加批注
	 */
	@PostMapping
	public Object add(Integer manager, String text) {
		Assert.notNull(manager, "manager" + nullValue);
		Assert.hasText(text, "text" + nullValue);
		return noteService.add(manager, text);
	}
}
