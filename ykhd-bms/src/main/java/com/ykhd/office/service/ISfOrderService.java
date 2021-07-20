package com.ykhd.office.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ykhd.office.domain.bean.common.PageHelper;
import com.ykhd.office.domain.entity.SfOrder;
import com.ykhd.office.domain.entity.SfOrderHistory;
import com.ykhd.office.domain.req.SfOrderCondition;
import com.ykhd.office.domain.req.SfOrderSubimt;
import com.ykhd.office.domain.resp.SfOrderDto;

public interface ISfOrderService extends IService<SfOrder> {

    /**
     *  分页查询档期
     */
    PageHelper<SfOrderDto> list(SfOrderCondition sfOrderCondition);

    /**
     *  添加档期
     */
    void save(SfOrderSubimt sfOrderSubimt);

    /**
     *  修改档期
     */
    void update(SfOrderSubimt sfOrderSubimt);

    /**
     *  作废档期
     */
    void delete(Integer orderid);

    /**
     *  获取档期历史
     */
    List<SfOrderHistory> getOrderHistory(Integer orderid,Integer type);
}
