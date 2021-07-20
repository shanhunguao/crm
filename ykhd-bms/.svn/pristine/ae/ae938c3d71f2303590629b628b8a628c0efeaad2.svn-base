package com.ykhd.office.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xkzhangsan.xkbeancomparator.CompareResult;
import com.ykhd.office.controller.BaseController;
import com.ykhd.office.domain.bean.common.PageHelper;
import com.ykhd.office.domain.entity.*;
import com.ykhd.office.domain.req.SfOrderCondition;
import com.ykhd.office.domain.req.SfOrderSubimt;
import com.ykhd.office.domain.resp.SfOrderDto;
import com.ykhd.office.repository.*;
import com.ykhd.office.service.ISfOrderService;
import com.ykhd.office.util.bean_compare.SfOrderDetailsLog;
import com.ykhd.office.util.bean_compare.SfOrderLog;
import com.ykhd.office.util.dictionary.StateEnums;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SfOrderService extends ServiceImpl<BaseMapper<SfOrder>, SfOrder> implements ISfOrderService {

    @Autowired
    private SfMediumMapper sfMediumMapper;

    @Autowired
    private SfOrderMapper sfOrderMapper;

    @Autowired
    private SfOrderDetailsMapper sfOrderDetailsMapper;

    @Autowired
    private SfOrderHistoryMapper sfOrderHistoryMapper;

    @Autowired
    private ManagerMapper managerMapper;


    @Override
    public PageHelper<SfOrderDto> list(SfOrderCondition condition) {
        QueryWrapper<SfOrder> wrapper = getSfOrderQueryWrapper(condition);
        IPage<SfOrder> sfOrderIPage = sfOrderMapper.selectPage(new Page<>(condition.getPage(), condition.getSize()), wrapper);
        List<SfOrder> records = sfOrderIPage.getRecords();
        List<SfOrderDto> list = new ArrayList<>();
        records.forEach(e -> {
            SfOrderDto sfOrderDto = new SfOrderDto();
            BeanUtils.copyProperties(e, sfOrderDto);
            QueryWrapper<SfOrderDetails> wrapper1 = new QueryWrapper<>();
            wrapper1.eq("order_id", e.getId());
            sfOrderDto.setSfOrderDetailsList(sfOrderDetailsMapper.selectList(wrapper1));

            QueryWrapper<Manager> wrapper2 = new QueryWrapper<>();
            wrapper2.eq("id", e.getCreateUserid()).select("name");
            Manager manager = managerMapper.selectOne(wrapper2);
            if (manager != null) {
                sfOrderDto.setCreateUsername(manager.getName());
            }

            QueryWrapper<SfMedium> wrapper3 = new QueryWrapper<>();
            wrapper3.eq("id", e.getMedium()).select("name");
            SfMedium sfMedium = sfMediumMapper.selectOne(wrapper3);
            if (sfMedium != null) {
                sfOrderDto.setMediumName(sfMedium.getName());
            }
            list.add(sfOrderDto);
        });
        return new PageHelper<>(condition.getPage(), condition.getSize(), sfOrderIPage.getTotal(), list);

    }

    /**
     * 封装查询条件
     */
    private QueryWrapper<SfOrder> getSfOrderQueryWrapper(SfOrderCondition condition) {
        QueryWrapper<SfOrder> wrapper = new QueryWrapper<>();
        if (condition.getId() != null) {
            wrapper.eq("id", condition.getId());
        }
        if (StringUtils.isNotEmpty(condition.getBrandName())) {
            wrapper.like("brand_name", condition.getBrandName());
        }
        if (StringUtils.isNotEmpty(condition.getProductName())) {
            wrapper.like("product_name", condition.getProductName());
        }
        if (condition.getState() != null) {
            wrapper.eq("state", condition.getState());
        }
        if (condition.getIsReturnMoney() != null) {
            wrapper.eq("is_return_money", condition.getIsReturnMoney());
        }
        if (condition.getCreateUserid() != null) {
            wrapper.eq("create_userid", condition.getCreateUserid());
        }


//        渠道名称
        if (StringUtils.isNotEmpty(condition.getMediumName())) {
            QueryWrapper<SfMedium> wrapper2 = new QueryWrapper<>();
            wrapper2.like("name", condition.getMediumName());
            SfMedium sfMedium = sfMediumMapper.selectOne(wrapper2);
            if (sfMedium != null) {
                wrapper.eq("medium", sfMedium.getId());
            }
        }
        if (condition.getMedium() != null) {
            wrapper.eq("medium", condition.getMedium());
        }

//        时间类型
        if (condition.getType().equals(1)) {
            if (condition.getCreateDateStart() != null) {
                wrapper.apply(StringUtils.isNotEmpty(condition.getCreateDateStart()),
                        "date_format (create_time,'%Y-%m-%d') >= date_format('" + condition.getCreateDateStart() + "','%Y-%m-%d')");
            }
            if (condition.getCreateDateEnd() != null) {
                wrapper.apply(StringUtils.isNotEmpty(condition.getCreateDateEnd()),
                        "date_format (create_time,'%Y-%m-%d') <= date_format('" + condition.getCreateDateEnd() + "','%Y-%m-%d')");
            }
        } else {
            if (condition.getCreateDateStart() != null) {
                wrapper.apply(StringUtils.isNotEmpty(condition.getCreateDateStart()),
                        "date_format (group_date,'%Y-%m-%d') >= date_format('" + condition.getCreateDateStart() + "','%Y-%m-%d')");
            }
            if (condition.getCreateDateEnd() != null) {
                wrapper.apply(StringUtils.isNotEmpty(condition.getCreateDateEnd()),
                        "date_format (group_date,'%Y-%m-%d') <= date_format('" + condition.getCreateDateEnd() + "','%Y-%m-%d')");
            }
        }
        wrapper.orderByDesc("create_time");
        return wrapper;
    }


    @Override
    @Transactional(rollbackFor = SQLException.class)
    public void save(SfOrderSubimt sfOrderSubimt) {
        SfOrder sfOrder = new SfOrder();
        BeanUtils.copyProperties(sfOrderSubimt, sfOrder, "sfOrderDetailsList");
        sfOrder.setCreateUserid(BaseController.getManagerInfo().getId());
        sfOrder.setState(StateEnums.sfOrderState.待开团.code());
        sfOrderMapper.add(sfOrder);
        //添加档期对应的规格信息
        setdetails(sfOrderSubimt, sfOrder);
    }


    @Override
    @Transactional(rollbackFor = SQLException.class)
    public void update(SfOrderSubimt sfOrderSubimt) {
//        对比新老订单内容
        SfOrder sfOrder = sfOrderMapper.selectById(sfOrderSubimt.getId());
        updatecontent(sfOrderSubimt, sfOrder);
//        对比新老订单内容详情
        updatecontent(sfOrderSubimt);
        BeanUtils.copyProperties(sfOrderSubimt, sfOrder, "sfOrderDetailsList");
        sfOrderMapper.updateById(sfOrder);
        sfOrderDetailsMapper.delete(new QueryWrapper<SfOrderDetails>().eq("order_id", sfOrder.getId()));
        setdetails(sfOrderSubimt, sfOrder);
    }

    /**
     * 记录更新订单信息
     */
    private void updatecontent(SfOrderSubimt sfOrderSubimt, SfOrder sfOrder) {
        SfOrder newSforder = new SfOrder();
        BeanUtils.copyProperties(sfOrderSubimt, newSforder);
        CompareResult compareResult = SfOrderLog.getCompareResult(sfOrder, newSforder);
        addhistory(sfOrderSubimt, compareResult);
    }

    /**
     * 记录更新订单详细信息
     */
    private void updatecontent(SfOrderSubimt sfOrderSubimt) {
        QueryWrapper<SfOrderDetails> wrapper = new QueryWrapper<>();
        wrapper.eq("order_id", sfOrderSubimt.getId());
//        旧内容
        List<SfOrderDetails> sfOrderDetails = sfOrderDetailsMapper.selectList(wrapper);
        for (int i = 0; i < sfOrderDetails.size(); i++) {
//            新内容
            List<SfOrderDetails> sfOrderDetailsList = sfOrderSubimt.getSfOrderDetailsList();
            sfOrderDetailsList.forEach(e -> {
                e.setGroupPurchasePrice(e.getGroupPurchasePrice().setScale(2,BigDecimal.ROUND_DOWN));
                e.setSupplyPrice(e.getSupplyPrice().setScale(2,BigDecimal.ROUND_DOWN));
            });
            for (int j = 0; j < sfOrderDetailsList.size(); j++) {
                if (sfOrderDetails.get(i).getSpecificationName().equals(sfOrderDetailsList.get(j).getSpecificationName())) {
                    CompareResult compareResult1 = SfOrderDetailsLog.getCompareResult(sfOrderDetails.get(i), sfOrderDetailsList.get(j));
                    if (compareResult1.isChanged()) {
                        String content = "规格:" + sfOrderDetails.get(i).getSpecificationName() + "," + compareResult1.getChangeContent();
                        compareResult1.setChangeContent(content);
                        addhistory(sfOrderSubimt, compareResult1);
                    }
                }
            }
        }
    }

    private void addhistory(SfOrderSubimt sfOrderSubimt, CompareResult compareResult1) {
        if (compareResult1.isChanged()) {
            SfOrderHistory sfOrderHistory = new SfOrderHistory();
            sfOrderHistory.setOrderId(sfOrderSubimt.getId());
            sfOrderHistory.setCreateUsername(BaseController.getManagerInfo().getName());
            sfOrderHistory.setType(1);
            sfOrderHistory.setContent(compareResult1.getChangeContent());
            sfOrderHistoryMapper.insert(sfOrderHistory);
        }
    }


    @Override
    @Transactional(rollbackFor = SQLException.class)
    public void delete(Integer orderid) {
        sfOrderMapper.deleteById(orderid);
        QueryWrapper<SfOrderDetails> wrapper = new QueryWrapper<>();
        wrapper.eq("order_id", orderid);
        sfOrderDetailsMapper.delete(wrapper);
    }

    /**
     * 添加档期历史
     */
    @Override
    public List<SfOrderHistory> getOrderHistory(Integer orderid, Integer type) {
        QueryWrapper<SfOrderHistory> wrapper = new QueryWrapper<>();
        wrapper.eq("order_id", orderid);
        wrapper.eq("type", type);
        wrapper.orderByDesc("create_time");
        return sfOrderHistoryMapper.selectList(wrapper);
    }

    /**
     * 添加档期详情
     */
    private void setdetails(SfOrderSubimt sfOrderSubimt, SfOrder sfOrder) {
        List<SfOrderDetails> sfOrderDetailsList = sfOrderSubimt.getSfOrderDetailsList();
        sfOrderDetailsList.forEach(e -> {
//           更改档期规格
            SfOrderDetails sfOrderDetails = new SfOrderDetails();
            BeanUtils.copyProperties(e, sfOrderDetails);
            sfOrderDetails.setOrderId(sfOrder.getId());
            sfOrderDetails.setCreateTime(new Date());
            sfOrderDetailsMapper.insert(sfOrderDetails);
        });
    }

}
