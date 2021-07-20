package com.ykhd.office.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ykhd.office.domain.bean.common.PageHelper;
import com.ykhd.office.domain.entity.SfMedium;
import com.ykhd.office.domain.entity.SfOrder;
import com.ykhd.office.domain.req.SfMediumCondition;
import com.ykhd.office.domain.resp.SfMediumDto;
import com.ykhd.office.repository.ManagerMapper;
import com.ykhd.office.repository.SfMediumMapper;
import com.ykhd.office.repository.SfOrderMapper;
import com.ykhd.office.service.ISfMediumService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SfMediumService extends ServiceImpl<BaseMapper<SfMedium>, SfMedium> implements ISfMediumService {

    @Autowired
    private SfMediumMapper sfMediumMapper;

    @Autowired
    private ManagerMapper managerMapper;

    @Autowired
    private SfOrderMapper sfOrderMapper;

    @Override
    public PageHelper<SfMediumDto> list(SfMediumCondition condition) {
        QueryWrapper<SfMedium> wrapper = getSfMediumQueryWrapper(condition);
        IPage<SfMedium> iPage = sfMediumMapper.selectPage(new Page<>(condition.getPage(), condition.getSize()), wrapper);
        List<SfMedium> records = iPage.getRecords();
//        对返回参数二次封装
        List<SfMediumDto> list = new ArrayList<>();
        records.forEach(e -> {
            SfMediumDto sfMediumDto = new SfMediumDto();
            BeanUtils.copyProperties(e, sfMediumDto);
            sfMediumDto.setCreateUserName(managerMapper.selectById(e.getCreateUserid()).getName());
            if (e.getDockingor() != null) {
                sfMediumDto.setDockingorName(managerMapper.selectById(e.getDockingor()).getName());
            }
//           判断渠道是否合作
            List<SfOrder> medium = sfOrderMapper.selectList(new QueryWrapper<SfOrder>().eq("medium", e.getId()));
            if (medium != null && medium.size() > 0) {
                sfMediumDto.setIsCooperation(0);
            } else {
                sfMediumDto.setIsCooperation(1);
            }
            list.add(sfMediumDto);
        });

        if (condition.getIsCooperation() != null) {
            List<SfMediumDto> collect = list.stream().filter(e -> e.getIsCooperation().equals(condition.getIsCooperation())).collect(Collectors.toList());
            return new PageHelper<>(condition.getPage(), condition.getSize(), iPage.getTotal(), collect);
        }

        return new PageHelper<>(condition.getPage(), condition.getSize(), iPage.getTotal(), list);
    }

    /**
     * 封装查询条件
     */
    private QueryWrapper<SfMedium> getSfMediumQueryWrapper(SfMediumCondition condition) {
        QueryWrapper<SfMedium> wrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(condition.getId())) {
            wrapper.eq("id", condition.getId());
        }
        if (StringUtils.isNotEmpty(condition.getCategory())) {
            wrapper.eq("category", condition.getCategory());
        }
        if (StringUtils.isNotEmpty(condition.getName())) {
            wrapper.like("name", condition.getName());
        }
        if (condition.getCreateUserid() != null) {
            wrapper.eq("create_userid", condition.getCreateUserid()).or().eq("dockingor", condition.getCreateUserid());
        }
        if (condition.getCreateDateStart() != null) {
            wrapper.apply(StringUtils.isNotEmpty(condition.getCreateDateStart()),
                    "date_format (create_time,'%Y-%m-%d') >= date_format('" + condition.getCreateDateStart() + "','%Y-%m-%d')");
        }
        if (condition.getCreateDateEnd() != null) {
            wrapper.apply(StringUtils.isNotEmpty(condition.getCreateDateEnd()),
                    "date_format (create_time,'%Y-%m-%d') <= date_format('" + condition.getCreateDateEnd() + "','%Y-%m-%d')");
        }
        wrapper.orderByDesc("update_time");
        return wrapper;
    }
}
