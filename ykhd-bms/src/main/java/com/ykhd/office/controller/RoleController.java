package com.ykhd.office.controller;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ykhd.office.service.IRoleService;
import com.ykhd.office.util.menu.MenuAuthorityUtil;

@RestController
@RequestMapping("/role")
public class RoleController extends BaseController {

	@Autowired
	private IRoleService roleService;
	
	/**
	 * 角色列表
	 */
	@GetMapping
	public Object list() {
		return roleService.list();
	}
	
	/**
	 * 新建角色
	 */
	@PostMapping
	public Object add(String name) {
		Assert.hasText(name, "name" + nullValue);
		return roleService.addRole(name) ? success(null) :failure(save_failure);
	}
	
	/**
	 * 编辑角色
	 */
	@PutMapping("/{id}")
	public Object update(@PathVariable Integer id, String name) {
		Assert.hasText(name, "name" + nullValue);
		return roleService.updateRole(id, name) ? success(null) :failure(update_failure);
	}
	
	/**
	 * 删除角色
	 */
	@DeleteMapping("/{id}")
	public Object delete(@PathVariable Integer id) {
		return roleService.deleteRole(id) ? success(null) :failure(delete_failure);
	}
	
	/**
	 * 查询角色的权限设置
	 */
	@GetMapping("/auth/{id}")
	public Object getAuth(@PathVariable Integer id) {
		String auth = roleService.getAuth(id);
		return MenuAuthorityUtil.menuSetupInfo(auth == null ? null : Arrays.asList(auth.split(",")));
	}
	
	/**
	 * 设置角色的权限
	 */
	@PutMapping("/auth/{id}")
	public Object setAuth(@PathVariable Integer id, @RequestParam String auth) {
		Assert.hasText(auth, "auth" + nullValue);
		return roleService.setAuth(id, auth) ? success(null) :failure(update_failure);
	}
	
}