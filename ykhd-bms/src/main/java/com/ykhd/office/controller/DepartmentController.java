package com.ykhd.office.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ykhd.office.service.IDepartmentService;

@RestController
@RequestMapping("/dept")
public class DepartmentController extends BaseController {
	
	@Autowired
	private IDepartmentService departmentService;
	
	/**
	 * 新增部门
	 */
	@PostMapping
	public Object add(String name, Integer parent) {
		Assert.hasText(name, "name" + nullValue);
		return departmentService.addDepartment(name, parent) ? success(null) : failure(save_failure);
	}
	
	/**
	 * 修改部门
	 */
	@PutMapping("/{id}")
	public Object update(@PathVariable Integer id, String name, Integer parent) {
		Assert.hasText(name, "name" + nullValue);
		return departmentService.updateDepartment(id, name, parent) ? success(null) : failure(update_failure);
	}
	
	/**
	 * 删除部门
	 */
	@DeleteMapping("/{id}")
	public Object delete(@PathVariable Integer id) {
		return departmentService.deleteDepartment(id) ? success(null) : failure(delete_failure);
	}
	
	/**
	 * 部门列表
	 */
	@GetMapping
	public Object list() {
		return departmentService.departmentTree();
	}
	
	/**
	 * 设置部门领导
	 */
	@PutMapping("/setLeader/{id}")
	public Object setLeader(@PathVariable Integer id, Integer leader) {
		return departmentService.setLeader(id, leader);
	}
	
	/**
	 * 广告部战队、小组列表
	 */
	@GetMapping("/team_group")
	public Object team_group() {
		return departmentService.team_group_list();
	}
	
}
