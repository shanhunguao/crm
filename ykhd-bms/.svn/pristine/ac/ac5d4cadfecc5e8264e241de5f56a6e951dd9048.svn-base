package com.ykhd.office.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ykhd.office.domain.entity.SfCommodityTemplate;

import com.ykhd.office.service.ISfCommodityTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhoufan
 * @Date 2020/12/18
 */
@RestController
@RequestMapping("/template")
public class SfCommodityTemplateController {
    @Autowired
    private ISfCommodityTemplateService commodityTemplateService;

    /**
     * 获取模板信息
     *
     * @param type 0规格 1产地 2运费模板
     */
    @GetMapping("/list")
    public Object list(Integer type) {
        QueryWrapper<SfCommodityTemplate> wrapper = new QueryWrapper<>();
        wrapper.eq("type", type);
        wrapper.select("template_id", "template_name");
        return commodityTemplateService.list(wrapper);
    }


}
