package com.ykhd.office.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ykhd.office.domain.bean.common.PageHelper;
import com.ykhd.office.domain.entity.Xunhao;
import com.ykhd.office.domain.req.XunhaoCondition;
import com.ykhd.office.domain.req.XunhaoSubmit;
import com.ykhd.office.domain.resp.XunhaoDto;

public interface IXunhaoService extends IService<Xunhao> {

	/**
	 * 询号列表
	 */
	PageHelper<XunhaoDto> getList(XunhaoCondition condition);
	
	/**
	 * 新建询号
	 */
	boolean addXunhao(XunhaoSubmit sumbit);
	
	/**
	 * 确认答复
	 */
	boolean reply(Integer id, String content);
}
