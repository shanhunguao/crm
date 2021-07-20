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

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ykhd.office.controller.BaseController;
import com.ykhd.office.domain.bean.LoginCache;
import com.ykhd.office.domain.bean.OASimpleInfo2;
import com.ykhd.office.domain.bean.common.PageHelper;
import com.ykhd.office.domain.entity.Xunhao;
import com.ykhd.office.domain.req.XunhaoCondition;
import com.ykhd.office.domain.req.XunhaoSubmit;
import com.ykhd.office.domain.resp.XunhaoDto;
import com.ykhd.office.service.IManagerService;
import com.ykhd.office.service.IOfficeAccountService;
import com.ykhd.office.service.IRoleService;
import com.ykhd.office.service.IXunhaoService;
import com.ykhd.office.util.dictionary.StateEnums.State4Xunhao;
import com.ykhd.office.util.dictionary.SystemEnums.RoleSign;
import com.ykhd.office.util.dictionary.TypeEnums.Type4ApprovalMsg;

@Service
public class XunhaoService extends ServiceImpl<BaseMapper<Xunhao>, Xunhao> implements IXunhaoService {

	@Autowired
	private IRoleService roleService;
	@Autowired
	private IManagerService managerService;
	@Autowired
	private IOfficeAccountService officeAccountService;
	@Autowired
	private ApprovalMsgService approvalMsgService;
	
	@Override
	public PageHelper<XunhaoDto> getList(XunhaoCondition condition) {
		List<XunhaoDto> list;
		QueryWrapper<Xunhao> queryWrapper = new QueryWrapper<>();
		if (condition.getState() != null)
			queryWrapper.eq("state", condition.getState());
		LoginCache managerInfo = BaseController.getManagerInfo();
		// 【查询限制】
		RoleSign sign = roleService.judgeRole(managerInfo.getRole());
		if (sign == RoleSign.medium)
			queryWrapper.eq("medium", managerInfo.getId());
		else if (sign == RoleSign.ae)
			queryWrapper.eq("creator", managerInfo.getId());
		IPage<Xunhao> iPage = page(new Page<>(condition.getPage(), condition.getSize()), queryWrapper.orderByDesc("id"));
		List<Xunhao> records = iPage.getRecords();
		if (records.isEmpty())
			list = Collections.emptyList();
		else {
			Map<Integer, String> name_map = managerService.queryName(records.stream().flatMap(v -> Stream.of(v.getCreator(), v.getMedium())).collect(Collectors.toSet()));
			Map<Integer, OASimpleInfo2> oa_map = officeAccountService.queryNameWechatCategory(records.stream().map(Xunhao::getOfficeAccount).collect(Collectors.toSet()));
			list = records.stream().flatMap(v -> {
				XunhaoDto dto = new XunhaoDto();
				BeanUtils.copyProperties(v, dto);
				dto.setCreatorName(name_map.get(v.getCreator()));
				dto.setMediumName(name_map.get(v.getMedium()));
				OASimpleInfo2 info2 = oa_map.get(v.getOfficeAccount());
				if (info2 != null) {
					dto.setCategory(info2.getCategory());
					dto.setOaName(info2.getName());
					dto.setWechat(info2.getWechat());
				}
				if (managerInfo.getId().intValue() == v.getMedium().intValue() && v.getState() == State4Xunhao.发起.code())
					dto.setReply(true);
				return Stream.of(dto);
			}).collect(Collectors.toList());
		}
		return new PageHelper<XunhaoDto>(condition.getPage(), condition.getSize(), iPage.getTotal(), list);
	}

	@Override
	public boolean addXunhao(XunhaoSubmit sumbit) {
		Xunhao xunhao = new Xunhao();
		BeanUtils.copyProperties(sumbit, xunhao);
		xunhao.setCreator(BaseController.getManagerInfo().getId());
		xunhao.setCreateTime(new Date());
		xunhao.setState(State4Xunhao.发起.code());
		boolean boo = save(xunhao);
		if (boo)
			approvalMsgService.sendApprovalMsg(Type4ApprovalMsg.询号待答复, sumbit.getMedium());
		return boo;
	}

	@Override
	public boolean reply(Integer id, String content) {
		Integer myId = BaseController.getManagerInfo().getId();
		boolean boo = update(new UpdateWrapper<Xunhao>().eq("id", id).eq("medium", myId)
				.set("state", State4Xunhao.已答复.code()).set("reply_time", new Date()).set("content", content));
		if (boo)
			approvalMsgService.sendApprovalMsg(Type4ApprovalMsg.询号待答复, myId);
		return boo;
	}

}
