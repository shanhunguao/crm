package com.ykhd.office.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ykhd.office.domain.bean.common.PageHelper;
import com.ykhd.office.domain.entity.AssessTemplate;
import com.ykhd.office.domain.req.AssessTemplateCondition;
import com.ykhd.office.domain.resp.AssessTemplateDto;

/**
 * @author zhoufan
 * @Date 2020/11/2
 */
public interface IAssessTemplateService extends IService<AssessTemplate> {

    /**
     * 查询所有模板
     */
    PageHelper<AssessTemplateDto> list(AssessTemplateCondition assessTemplateCondition);

    /**
     * 获取被考核人的模板
     */
    AssessTemplate getTemplate(AssessTemplate assessTemplate);

    /**
     * 校验模板占比百分
     */
    Boolean checkTemplate(AssessTemplate assessTemplate);



}
