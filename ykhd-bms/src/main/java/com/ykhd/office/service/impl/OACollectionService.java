package com.ykhd.office.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ykhd.office.controller.BaseController;
import com.ykhd.office.domain.entity.OACollection;
import com.ykhd.office.service.IOACollectionService;

@Service
public class OACollectionService extends ServiceImpl<BaseMapper<OACollection>, OACollection> implements IOACollectionService {

	@Override
	public boolean collection(Integer oaId) {
		Integer manager = BaseController.getManagerInfo().getId();
		OACollection one = getOne(new QueryWrapper<OACollection>().eq("office_account", oaId).eq("collector", manager));
		if (one == null)
			return save(new OACollection(oaId, manager));
		else
			return removeById(one.getId());
	}

	@Override
	public List<Integer> getMyCollection() {
		return list(new QueryWrapper<OACollection>().eq("collector", BaseController.getManagerInfo().getId()))
				.stream().map(OACollection::getOfficeAccount).collect(Collectors.toList());
	}

}
