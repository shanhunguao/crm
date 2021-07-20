package com.ykhd.office.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ykhd.office.domain.bean.common.PageHelper;
import com.ykhd.office.domain.entity.SfSupplier;
import com.ykhd.office.domain.req.SupplierCondition;
import com.ykhd.office.domain.resp.SupplierDto;
import com.ykhd.office.repository.SfSupplierMapper;
import com.ykhd.office.service.ISfSupplierService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SupplierService extends ServiceImpl<BaseMapper<SfSupplier>, SfSupplier> implements ISfSupplierService {

    @Autowired
    private SfSupplierMapper supplierMapper;

    @Override
    public PageHelper<SupplierDto> getListByPage(SupplierCondition condition) {
        QueryWrapper<SfSupplier> wrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(condition.getName())) {
            wrapper.like("name", condition.getName());
        }
        wrapper.orderByDesc("create_time");
        IPage<SfSupplier> supplierIPage = supplierMapper.selectPage(new Page<>(condition.getPage(), condition.getSize()), wrapper);
        Integer total = supplierMapper.selectCount(wrapper);
        List<SfSupplier> records = supplierIPage.getRecords();
        List<SupplierDto> list = new ArrayList<>();
        records.forEach(e -> {
            SupplierDto supplierDto = new SupplierDto();
            BeanUtils.copyProperties(e, supplierDto);
            list.add(supplierDto);
        });
        return new PageHelper<>(condition.getPage(), condition.getSize(), total, list);
    }
}
