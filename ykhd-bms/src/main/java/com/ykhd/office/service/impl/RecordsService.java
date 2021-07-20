package com.ykhd.office.service.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ykhd.office.domain.entity.CustomerFollowUp;
import com.ykhd.office.service.IRecordsService;
import org.springframework.stereotype.Service;

/**
 * @author zhoufan
 * @Date 2020/8/24
 */
@Service
public class RecordsService extends ServiceImpl<BaseMapper<CustomerFollowUp>, CustomerFollowUp> implements IRecordsService {
}
