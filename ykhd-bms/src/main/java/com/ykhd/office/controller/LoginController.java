package com.ykhd.office.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ykhd.office.component.RedisService;
import com.ykhd.office.service.ILoginService;
import com.ykhd.office.util.SecurityUtil;

@RestController
public class LoginController extends BaseController {

	@Autowired
	private ILoginService loginService;
	@Autowired
	private RedisService redisService;
	
	/**
	 * 账号密码登录
	 */
	@PostMapping("/login/account")
	public Object loginByAccount(String username, String password) {
		Assert.hasText(username, "username" + nullValue);
		Assert.hasText(password, "password" + nullValue);
		Assert.state(htmlEscape(username), illegal);
		password = SecurityUtil.privateKeyDecryption(password);
		return loginService.loginByAccountPwd(username, password);
	}
	
	/**
	 * 退出登陆
	 */
	@DeleteMapping("/logout")
	public Object logout() {
		loginService.deleteLoginInfo(BaseController.getManagerInfo().getId());
		return success(null);
	}
	
	/**
	 * 登录页显示系统名称
	 */
	@GetMapping("/login/sysName")
	public Object sysName() {
		return success(redisService.getValue("system_name"));
	}
}
