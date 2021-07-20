package com.ykhd.office.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ykhd.office.domain.entity.OAFollowUp;
import com.ykhd.office.domain.resp.OAFollowUpDto;

public interface IOAFollowUpService extends IService<OAFollowUp> {

	/**
	 * 新增跟进记录
	 */
	boolean addOAFollowUp(Integer officeAccount, String content);
	
	/**
	 * 查询历史跟进记录
	 */
	List<OAFollowUpDto> getList(Integer officeAccount);
}
