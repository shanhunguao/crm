package com.ykhd.office.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ykhd.office.domain.entity.Industry;
import com.ykhd.office.service.IIndustryService;

/**
 * @author zhoufan
 * @Date 2020/8/21
 */
@Service
public class IndustryService extends ServiceImpl<BaseMapper<Industry>, Industry> implements IIndustryService{
    

}
