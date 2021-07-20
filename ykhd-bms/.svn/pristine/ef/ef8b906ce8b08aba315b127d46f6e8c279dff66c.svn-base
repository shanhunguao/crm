package com.ykhd.office.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ykhd.office.domain.bean.common.PageHelper;
import com.ykhd.office.domain.entity.Manager;
import com.ykhd.office.domain.entity.SystemLog;
import com.ykhd.office.domain.req.SystemLogCondition;
import com.ykhd.office.domain.resp.SystemLogDto;
import com.ykhd.office.service.ISystemLogService;
import com.ykhd.office.util.DateUtil;
import com.ykhd.office.util.dictionary.TypeEnums.Type4SystemLog;

@Service
public class SystemLogService extends ServiceImpl<BaseMapper<SystemLog>, SystemLog> implements ISystemLogService {

	@Override
	public boolean addLoginLog(HttpServletRequest request, Manager manager) {
		SystemLog log = new SystemLog();
		log.setType(Type4SystemLog.用户登陆.code());
		log.setManager(manager.getId());
		log.setName(manager.getName());
		log.setLoginIp(request.getRemoteAddr());
		log.setCreateTime(new Date());
		return save(log);
	}

	@Override
	public PageHelper<SystemLogDto> getListByPage(SystemLogCondition condition) {
		QueryWrapper<SystemLog> queryWrapper = new QueryWrapper<>();
		if (StringUtils.hasText(condition.getName()))
			queryWrapper.like("name", condition.getName().trim());
		if (StringUtils.hasText(condition.getStartTime()))
			queryWrapper.gt("create_time", condition.getStartTime());
		if (StringUtils.hasText(condition.getEndTime()))
			queryWrapper.lt("create_time", DateUtil.nextDay(condition.getEndTime()));
		IPage<SystemLog> iPage = page(new Page<>(condition.getPage(), condition.getSize()), queryWrapper.orderByDesc("id"));
		List<SystemLog> records = iPage.getRecords();
		Type4SystemLog[] types = Type4SystemLog.values();
		List<SystemLogDto> list = records.stream().flatMap(v -> {
			SystemLogDto dto = new SystemLogDto();
			BeanUtils.copyProperties(v, dto);
			dto.setTypeName(Arrays.stream(types).filter(ty -> ty.code() == dto.getType()).findFirst().get().name());
			return Stream.of(dto);
		}).collect(Collectors.toList());
		return new PageHelper<SystemLogDto>(condition.getPage(), condition.getSize(), iPage.getTotal(), list);
	}

}
