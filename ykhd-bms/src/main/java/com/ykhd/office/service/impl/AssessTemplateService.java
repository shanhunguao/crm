package com.ykhd.office.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ykhd.office.domain.bean.common.PageHelper;
import com.ykhd.office.domain.entity.*;
import com.ykhd.office.domain.req.AssessTemplateCondition;
import com.ykhd.office.domain.resp.AssessTemplateDto;
import com.ykhd.office.repository.*;
import com.ykhd.office.service.IAssessTemplateService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhoufan
 * @Date 2020/11/2
 */
@Service
public class AssessTemplateService extends ServiceImpl<BaseMapper<AssessTemplate>, AssessTemplate> implements IAssessTemplateService {

    @Autowired
    private AssessTemplateMapper assessTemplateMapper;


    @Override
    public PageHelper<AssessTemplateDto> list(AssessTemplateCondition condition) {
        List<AssessTemplateDto> assessTemplateDtos = new ArrayList<>();
        AssessTemplate assessTemplate = new AssessTemplate();
        BeanUtils.copyProperties(condition, assessTemplate);
        QueryWrapper<AssessTemplate> wrapper = new QueryWrapper<>(assessTemplate);
        IPage<AssessTemplate> assessIPage = getAssessIPage(condition, assessTemplateDtos, wrapper);
        return new PageHelper<>(condition.getPage(), condition.getSize(), assessIPage.getTotal(), assessTemplateDtos);
    }

    @Override
    public AssessTemplate getTemplate(AssessTemplate assessTemplate) {
        QueryWrapper<AssessTemplate> wrapper = new QueryWrapper<>(assessTemplate);
        wrapper.in("type", assessTemplate.getType(), "3", "4");
        wrapper.eq("role_id", assessTemplate.getRoleId());
        return assessTemplateMapper.selectOne(wrapper);
    }

    @Override
    public Boolean checkTemplate(AssessTemplate assessTemplate) {
        int weight = 0;
        QueryWrapper<AssessTemplate> wrapper = new QueryWrapper<>();
        wrapper.in("type", "1", "2");
        wrapper.eq("role_id", assessTemplate.getRoleId());
        List<AssessTemplate> assessTemplates = assessTemplateMapper.selectList(wrapper);
        for (AssessTemplate template : assessTemplates) {
            weight += Integer.valueOf(template.getWeight());
        }
        return weight < 100;
    }

    /**
     * 封装绩效返结果
     */
    private IPage<AssessTemplate> getAssessIPage(AssessTemplateCondition condition, List<AssessTemplateDto> templateDtos, QueryWrapper<AssessTemplate> wrapper) {
        IPage<AssessTemplate> assessIPage = assessTemplateMapper.selectPage(new Page<>(condition.getPage(), condition.getSize()), wrapper);
        List<AssessTemplate> records = assessIPage.getRecords();
        for (AssessTemplate record : records) {
            AssessTemplateDto assessTemplateDto = new AssessTemplateDto();
            BeanUtils.copyProperties(record, assessTemplateDto);
            templateDtos.add(assessTemplateDto);
        }
        return assessIPage;
    }


}
