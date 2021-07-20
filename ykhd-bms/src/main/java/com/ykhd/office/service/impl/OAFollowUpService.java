package com.ykhd.office.service.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ykhd.office.controller.BaseController;
import com.ykhd.office.domain.bean.LoginCache;
import com.ykhd.office.domain.entity.OAFollowUp;
import com.ykhd.office.domain.resp.OAFollowUpDto;
import com.ykhd.office.service.IOAFollowUpService;

@Service
public class OAFollowUpService extends ServiceImpl<BaseMapper<OAFollowUp>, OAFollowUp> implements IOAFollowUpService {

	@Override
	public boolean addOAFollowUp(Integer officeAccount, String content) {
		OAFollowUp record = new OAFollowUp();
		record.setOfficeAccount(officeAccount);
		record.setContent(content);
		LoginCache loginCache = BaseController.getManagerInfo();
		record.setCreator(loginCache.getId());
		record.setCreatorName(loginCache.getName());
		record.setCreateTime(new Date());
		return save(record);
	}

	@Override
	public List<OAFollowUpDto> getList(Integer officeAccount) {
		List<OAFollowUp> all = list(new QueryWrapper<OAFollowUp>().eq("office_account", officeAccount));
		return all.stream().flatMap(v -> {
			OAFollowUpDto dto = new OAFollowUpDto();
			BeanUtils.copyProperties(v, dto);
			return Stream.of(dto);
		}).collect(Collectors.toList());
	}

}
