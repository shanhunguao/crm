package com.ykhd.office.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ykhd.office.domain.entity.Manager;
import com.ykhd.office.domain.entity.SfMedium;
import com.ykhd.office.domain.entity.SfOrder;
import com.ykhd.office.domain.req.SfOrderCondition;
import com.ykhd.office.domain.req.SfOrderSubimt;
import com.ykhd.office.service.IManagerService;
import com.ykhd.office.service.ISfMediumService;
import com.ykhd.office.service.ISfOrderService;
import com.ykhd.office.util.dictionary.StateEnums;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author zhoufan
 * @Date 2020/12/23
 * 赛峰档期
 */
@RestController
@RequestMapping("/sforder")
public class SfOrderController extends BaseController {

    @Autowired
    private ISfOrderService orderService;

    @Autowired
    private ISfMediumService sfMediumService;

    @Autowired
    private IManagerService managerService;


    /**
     *  获取赛峰档期列表
     */
    @GetMapping("/list")
    public Object list(SfOrderCondition SfOrderCondition) {
        return orderService.list(SfOrderCondition);
    }

    /**
     *  添加赛峰档期
     */
    @PostMapping("/save")
    public Object save(@RequestBody SfOrderSubimt sfOrderSubimt) {
//        更新渠道对接信息
        SfMedium sfMedium = new SfMedium();
        sfMedium.setId(sfOrderSubimt.getMedium());
        sfMedium.setUpdateTime(new Date());
        sfMedium.setDockingor(BaseController.getManagerInfo().getId());
        sfMediumService.updateById(sfMedium);
//      添加档期
        orderService.save(sfOrderSubimt);
        return success("添加档期成功");
    }

    /**
     *  修改档期
     */
    @PutMapping("/update")
    public Object update(@RequestBody SfOrderSubimt sfOrderSubimt) {
        orderService.update(sfOrderSubimt);
        return success("修改档期成功");
    }


    /**
     *  删除档期
     */
    @DeleteMapping("/delete")
    public Object delete(Integer id) {
        orderService.delete(id);
        return success("删除档期成功");
    }

    /**
     *  获取赛峰档期修改历史
     */
    @GetMapping("getOrderHistory")
    public Object getOrderHistory(Integer id, Integer type) {
        return orderService.getOrderHistory(id, type);
    }

    /**
     * id 档期ID returnRemark 回款备注 returnDate 回款时间
     *
     *  档期勾选回款
     */
    @PutMapping("/returnedMoney")
    public Object returnedMoney(SfOrder sfOrder) {
        sfOrder.setIsReturnMoney(0);
        return orderService.updateById(sfOrder);
    }


    /**
     *  勾选开团
     */
    @PutMapping("/openGroup")
    public Object openGroup(String ids) {
        List<String> list = Arrays.asList(ids.split(","));
        list.forEach(e -> {
            Integer id = Integer.valueOf(e);
            Assert.state(orderService.getById(id).getState().equals(StateEnums.sfOrderState.待开团.code()), "非待开团状态");
            SfOrder sfOrder = new SfOrder();
            sfOrder.setId(id);
            sfOrder.setState(StateEnums.sfOrderState.已开团.code());
            orderService.updateById(sfOrder);
        });
        return success("确定开团成功");
    }

    /**
     *  勾选未开团
     */
    @PutMapping("/shutGroup")
    public Object shutGroup(String ids) {
        List<String> list = Arrays.asList(ids.split(","));
        list.forEach(e -> {
            Integer id = Integer.valueOf(e);
            Assert.state(orderService.getById(id).getState().equals(StateEnums.sfOrderState.待开团.code()), "非待开团状态");
            SfOrder sfOrder = new SfOrder();
            sfOrder.setId(id);
            sfOrder.setState(StateEnums.sfOrderState.未开团.code());
            orderService.updateById(sfOrder);
        });
        return success("确定未团成功");
    }

    /**
     *  获取渠道运营列表
     */
    @GetMapping("/channel")
    public Object channel() {
        QueryWrapper<Manager> wrapper = new QueryWrapper<>();
        wrapper.in("department", 8,12).select("id", "name");
        return managerService.list(wrapper);
    }


}
