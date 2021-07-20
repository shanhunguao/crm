package com.ykhd.office.service.impl;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ykhd.office.controller.BaseController;
import com.ykhd.office.domain.bean.LoginCache;
import com.ykhd.office.domain.entity.Note;
import com.ykhd.office.domain.resp.NoteDto;
import com.ykhd.office.service.IManagerService;
import com.ykhd.office.service.INoteService;
import com.ykhd.office.service.IRoleService;
import com.ykhd.office.util.dictionary.SystemEnums.RoleSign;

@Service
public class NoteService extends ServiceImpl<BaseMapper<Note>, Note> implements INoteService {

	@Autowired
	private IManagerService managerService;
	@Autowired
	private IRoleService roleService;
	
	@Override
	public List<NoteDto> getList(Integer manager) {
		LoginCache managerInfo = BaseController.getManagerInfo();
		Assert.state(!managerInfo.getId().equals(manager), "不能查对自己的批注");
		RoleSign roleSign = roleService.judgeRole(managerInfo.getRole());
		QueryWrapper<Note> queryWrapper = new QueryWrapper<Note>().eq("manager", manager);
		if (roleSign != RoleSign.general_mgr) //不是总经理只能看自己批注的
			queryWrapper.eq("creator", managerInfo.getId());
		List<Note> list = list(queryWrapper.orderByDesc("id"));
		Map<Integer, String> map;
		if (roleSign == RoleSign.general_mgr) //总经理显示备注人名称
			map = managerService.queryName(list.stream().map(Note::getCreator).collect(Collectors.toSet()));
		else
			map = Collections.emptyMap();
		return list.stream().flatMap(v -> {
			NoteDto dto = new NoteDto();
			BeanUtils.copyProperties(v, dto);
			dto.setCreatorName(map.get(v.getCreator()));
			return Stream.of(dto);
		}).collect(Collectors.toList());
	}

	@Override
	public List<NoteDto> getList4Examine(Integer manager, String yyyyMM) {
		LoginCache managerInfo = BaseController.getManagerInfo();
		Assert.state(!managerInfo.getId().equals(manager), "不能查对自己的批注");
		RoleSign roleSign = roleService.judgeRole(managerInfo.getRole());
		QueryWrapper<Note> queryWrapper = new QueryWrapper<Note>().eq("manager", manager).likeRight("create_time", yyyyMM);
		List<Note> list = list(queryWrapper.orderByDesc("id"));
		Map<Integer, String> map;
		if (roleSign == RoleSign.general_mgr) //总经理显示备注人名称
			map = managerService.queryName(list.stream().map(Note::getCreator).collect(Collectors.toSet()));
		else
			map = Collections.emptyMap();
		return list.stream().flatMap(v -> {
			NoteDto dto = new NoteDto();
			BeanUtils.copyProperties(v, dto);
			dto.setCreatorName(map.get(v.getCreator()));
			return Stream.of(dto);
		}).collect(Collectors.toList());
	}

	@Override
	public boolean add(Integer manager, String text) {
		Integer id = BaseController.getManagerInfo().getId();
		Assert.state(!id.equals(manager), "仅限别人");
		Note note = new Note();
		note.setCreator(id);
		note.setManager(manager);
		note.setText(text);
		note.setCreateTime(new Date());
		return save(note);
	}

}
