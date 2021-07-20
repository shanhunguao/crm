package com.ykhd.office.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ykhd.office.domain.entity.Industry;
import com.ykhd.office.service.IIndustryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhoufan
 * @Date 2020/8/21
 * 行业
 */
@RestController
@RequestMapping("/industry")
public class IndustryController {

    @Autowired
    private IIndustryService iIndustryService;

    /**
     * 获取行业列表
     */
    @GetMapping("/list")
    public Object list(Integer pid) {
        QueryWrapper<Industry> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("industry_pid", pid);
        return iIndustryService.list(queryWrapper);
    }


}
