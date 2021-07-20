package com.ykhd.office.service;

import com.ykhd.office.domain.resp.LoginReturn;

public interface ILoginService {

	/**
	 * 账号密码登录
	 */
	LoginReturn loginByAccountPwd(String username, String password);
	
	/**
	 * 删除登陆者的相关登陆信息
	 */
	void deleteLoginInfo(Integer managerId);
}
