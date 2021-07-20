package com.ykhd.office.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ykhd.office.component.RedisService;
import com.ykhd.office.domain.bean.LoginCache;
import com.ykhd.office.domain.bean.MsgSentBean;
import com.ykhd.office.domain.entity.Manager;
import com.ykhd.office.domain.entity.Role;
import com.ykhd.office.domain.resp.LoginReturn;
import com.ykhd.office.service.ILoginService;
import com.ykhd.office.service.IManagerService;
import com.ykhd.office.service.IRoleService;
import com.ykhd.office.service.ISystemLogService;
import com.ykhd.office.util.JacksonHelper;
import com.ykhd.office.util.MsgSentHelper;
import com.ykhd.office.util.SecurityUtil;
import com.ykhd.office.util.dictionary.Consts;
import com.ykhd.office.util.dictionary.StateEnums.State4Manager;
import com.ykhd.office.util.dictionary.TypeEnums.Type4ApprovalMsg;
import com.ykhd.office.util.menu.MenuAuthorityUtil;

@Service
public class LoginService implements ILoginService {

	@Autowired
	private IManagerService managerService;
	@Autowired
	private RedisService redisService;
	@Autowired
	private IRoleService roleService;
	@Autowired
	private ISystemLogService systemLogService;
	
	@Override
	public LoginReturn loginByAccountPwd(String username, String password) {
		Manager manager = managerService.getOne(new QueryWrapper<Manager>().select("id", "name", "role", "department", "status")
				.eq("name", username).eq("password", SecurityUtil.md5Encode(password)).last("limit 1"));
		Assert.notNull(manager, "用户名或密码错误");
		Assert.state(manager.getStatus().intValue() == State4Manager.启用.code(), "账号已停用");
		Integer mid = manager.getId();
		/*单处登陆*/
		String current_token = redisService.getValue(Consts.LOGIN_TOKEN_PREFIX + mid);
		if (current_token != null) {
			redisService.delKV(current_token);
			if (MsgSentHelper.online(mid))
                MsgSentHelper.sendMsg(new MsgSentBean(mid, Type4ApprovalMsg.别处登陆被迫下线.code(), "")); // msg
		}
		/*返回信息*/
		LoginReturn return_ = new LoginReturn();
		String token = createToken();
		return_.setToken(token);
		return_.setName(manager.getName());
		Role role = roleService.getById(manager.getRole());
		return_.setRoleName(role.getName());
		String auth = role.getMenuAuth();
		if (auth != null) {
			String[] array = auth.split(",");
			List<String> auths = Arrays.asList(array);
			return_.setMenuList(MenuAuthorityUtil.adminMenu(auths));
			return_.setOperateMenuSign(MenuAuthorityUtil.operateMenuSign(auths));
			redisService.setSet(Consts.LOGIN_EXPIRE, Consts.LOGIN_AUTH_PREFIX + manager.getId(), array); // cache auths
		}
		/*缓存信息*/
		LoginCache cache = new LoginCache();
		BeanUtils.copyProperties(manager, cache);
		redisService.setKV(Consts.TOKEN_PREFIX + token, JacksonHelper.toJsonStr(cache), Consts.LOGIN_EXPIRE); // cache token
		redisService.setKV(Consts.LOGIN_TOKEN_PREFIX + mid, Consts.TOKEN_PREFIX + token, Consts.LOGIN_EXPIRE); // cache id
		// 登陆日志
		systemLogService.addLoginLog(((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest(), manager);
		return return_;
	}
	
	private String createToken() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	@Override
	public void deleteLoginInfo(Integer managerId) {
		String token = redisService.getValue(Consts.LOGIN_TOKEN_PREFIX + managerId);
		if (token != null) {
			redisService.delKV(token);
			redisService.delKV(Consts.LOGIN_TOKEN_PREFIX + managerId);
			redisService.delKV(Consts.LOGIN_AUTH_PREFIX + managerId);
		}
	}

}
