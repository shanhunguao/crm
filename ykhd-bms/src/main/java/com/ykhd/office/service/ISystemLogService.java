package com.ykhd.office.service;

import javax.servlet.http.HttpServletRequest;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ykhd.office.domain.entity.SystemLog;
import com.ykhd.office.domain.req.SystemLogCondition;
import com.ykhd.office.domain.resp.SystemLogDto;
import com.ykhd.office.domain.bean.common.PageHelper;
import com.ykhd.office.domain.entity.Manager;

public interface ISystemLogService extends IService<SystemLog> {

	/**
	 * 新增登陆日志
	 */
	boolean addLoginLog(HttpServletRequest request, Manager manager);
	
	/**
	 * 分页查询日志
	 */
	PageHelper<SystemLogDto> getListByPage(SystemLogCondition condition);
}
