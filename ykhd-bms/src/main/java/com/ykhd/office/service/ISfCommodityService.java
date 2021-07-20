package com.ykhd.office.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ykhd.office.domain.bean.common.PageHelper;
import com.ykhd.office.domain.entity.SfCommodity;
import com.ykhd.office.domain.entity.SfCommodityDetails;
import com.ykhd.office.domain.req.SfCommodityCondition;
import com.ykhd.office.domain.resp.SfCommodityDto;

import java.util.List;

public interface ISfCommodityService extends IService<SfCommodity> {
    /**
     * 查询商品信息
     */
    PageHelper<SfCommodityDto> list(SfCommodityCondition commodityCondition);

    void add(SfCommodity commodity);

    SfCommodityDto findId(Integer id);

    void addhistory(Integer orderid,List<SfCommodityDetails> list,List<SfCommodityDetails> list2);

    void addhistory(SfCommodity newcommodity,SfCommodity oldcommodity);
}
