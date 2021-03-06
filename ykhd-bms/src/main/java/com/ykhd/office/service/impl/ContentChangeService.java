package com.ykhd.office.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ykhd.office.controller.BaseController;
import com.ykhd.office.domain.bean.LoginCache;
import com.ykhd.office.domain.entity.ContentChange;
import com.ykhd.office.domain.resp.ContentChangeDto;
import com.ykhd.office.service.IContentChangeService;
import com.ykhd.office.service.IManagerService;
import com.ykhd.office.service.IRoleService;
import com.ykhd.office.util.dictionary.SystemEnums.RoleSign;
import com.ykhd.office.util.dictionary.TypeEnums.Type4Change;

@Service
public class ContentChangeService extends ServiceImpl<BaseMapper<ContentChange>, ContentChange> implements IContentChangeService {

	@Autowired
	private IManagerService managerService;
	@Autowired
	private IRoleService roleService;
	
	@Override
	public boolean addContentChange(Type4Change type, Integer identity, String changeDesc, Integer... creator) {
		ContentChange change = new ContentChange();
		change.setType(type.code());
		change.setIdentity(identity);
		change.setChangeDesc(changeDesc);
		if (creator.length > 0) {
			change.setCreator(creator[0]);
			change.setCreatorName(managerService.queryName(Arrays.asList(creator[0])).get(creator[0]));
		} else {
			LoginCache managerInfo = BaseController.getManagerInfo();
			change.setCreator(managerInfo.getId());
			change.setCreatorName(managerInfo.getName());
		}
		change.setCreateTime(new Date());
		return save(change);
	}

	@Override
	public List<ContentChangeDto> getContentChangeList(Type4Change type, Integer identity) {
		List<ContentChangeDto> list = list(new QueryWrapper<ContentChange>().select("change_desc", "creator_name", "create_time")
				.eq("type", type.code()).eq("identity", identity).orderByDesc("id")).stream().flatMap(v -> {
			ContentChangeDto dto = new ContentChangeDto();
			BeanUtils.copyProperties(v, dto);
			return Stream.of(dto);
		}).collect(Collectors.toList());
		RoleSign sign = roleService.judgeRole(BaseController.getManagerInfo().getRole());
		if (sign == RoleSign.collector || sign == RoleSign.general_mgr)
			return list;
		return list.stream().filter(v -> !v.getChangeDesc().contains("?????????")).collect(Collectors.toList()); // ?????????????????????
	}

}
