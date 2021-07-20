package com.ykhd.office.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ykhd.office.domain.bean.SpecificationGroup;
import com.ykhd.office.domain.entity.SfCommodity;
import com.ykhd.office.domain.entity.SfCommodityDetails;
import com.ykhd.office.domain.entity.SfOrder;
import com.ykhd.office.domain.req.CommoditySubmit;
import com.ykhd.office.domain.req.SfCommodityCondition;
import com.ykhd.office.service.ISfCommodityDetailsService;
import com.ykhd.office.service.ISfCommodityService;
import com.ykhd.office.service.ISfMediumService;
import com.ykhd.office.service.ISfOrderService;
import com.ykhd.office.util.dictionary.StateEnums;

/**
 * @author zhoufan
 * @Date 2020/12/18
 */
@RestController
@RequestMapping("/commodity")
public class SfCommodityController extends BaseController {

    @Autowired
    private ISfCommodityService commodityService;
    @Autowired
    private ISfCommodityDetailsService commodityDetailsService;
    @Autowired
    private ISfOrderService sfOrderService;
    @Autowired
    private ISfMediumService sfMediumService;

    /**
     * 查看商品信息
     */
    @GetMapping("/list")
    public Object list(SfCommodityCondition commodityCondition) {
        return commodityService.list(commodityCondition);
    }


    /**
     * 修改商品信息
     */
    @PutMapping("/update")
    public Object update(@RequestBody CommoditySubmit commoditySubmit) {
//        更新商品信息
        SfCommodity commodity = new SfCommodity();
        BeanUtils.copyProperties(commoditySubmit, commodity);
        commodityService.addhistory(commodity, commodityService.getById(commodity.getId()));
        commodityService.updateById(commodity);
//        更新商品规格
        QueryWrapper<SfCommodityDetails> wrapper = new QueryWrapper<>();
        wrapper.eq("commodity_id", commoditySubmit.getId());
//        新数据
        List<SfCommodityDetails> list = new ArrayList<>();
//        旧数据
        List<SfCommodityDetails> list2 = commodityDetailsService.list(wrapper);
        List<SfCommodityDetails> commodityDetailsList = commoditySubmit.getCommodityDetailsList();
        if (commodityDetailsList != null && commodityDetailsList.size() > 0) {
            for (int i = 0; i < commodityDetailsList.size(); i++) {
                for (SpecificationGroup specificationGroup : commodityDetailsList.get(i).getSpecificationGroup()) {
                    SfCommodityDetails commodityDetails = new SfCommodityDetails();
                    BeanUtils.copyProperties(commodityDetailsList.get(i), commodityDetails, "specificationGroup");
                    commodityDetails.setSpecificationName(specificationGroup.getGroupId());
                    commodityDetails.setSpecificationValue(specificationGroup.getGroupName());
                    commodityDetails.setSfCommodityId(commoditySubmit.getId());
                    commodityDetails.setSpecificationProject(i);
                    list.add(commodityDetails);
                }
            }
            //        添加修改历史
            commodityService.addhistory(commoditySubmit.getId(), list, list2);
            //        修改商品详情
            commodityDetailsService.remove(wrapper);
            commodityDetailsService.saveBatch(list);
        }

        return success("修改商品信息成功");
    }


    /**
     * 查看商品详情
     */
    @GetMapping("findId")
    public Object findId(Integer id) {
        return commodityService.findId(id);
    }


    /**
     * 删除商品信息
     */
    @DeleteMapping("/delete")
    public Object delete(String ids) {
        List<String> list = Arrays.asList(ids.split(","));
        if (list.size() > 0) {
            list.forEach(e -> {
                commodityService.removeById(e);
                QueryWrapper<SfCommodityDetails> wrapper = new QueryWrapper<>();
                wrapper.eq("commodity_id", e);
                commodityDetailsService.remove(wrapper);
            });
            return success("删除商品成功");
        }
        return failure("删除商品失败");
    }


    /**
     * 商品买手审核通过
     */
    @PutMapping("/CommodityBuyerPass")
    public Object CommodityBuyerPass(String ids) {
        pass(ids, StateEnums.commodityState.待买手审核, "非待买手审核状态", StateEnums.commodityState.待主管审核);
        return success("审核通过");
    }

    /**
     * 封装审核通过公共方法
     */
    private void pass(String ids, StateEnums.commodityState check, String message, StateEnums.commodityState end) {
        List<String> list = Arrays.asList(ids.split(","));
        list.forEach(e -> {
            Integer id = Integer.valueOf(e);
            SfCommodity commodity = commodityService.getById(id);
            Assert.state(commodity.getState().equals(check.code()), message);
            UpdateWrapper<SfCommodity> wrapper = new UpdateWrapper<>();
            wrapper.eq("id", id);
            wrapper.set("state", end.code());
            commodityService.update(wrapper);
        });
    }

    /**
     * 商品主管审核通过
     */
    @PutMapping("/CommodityGovernorPass")
    public Object CommodityGovernorPass(String ids) {
        pass(ids, StateEnums.commodityState.待主管审核, "非待主管审核状态", StateEnums.commodityState.待财务审核);
        return success("审核通过");
    }


    /**
     * 商品财务审核通过
     */
    @PutMapping("/CommodityFinancePass")
    public Object CommodityFinancePass(String ids) {
        pass(ids, StateEnums.commodityState.待财务审核, "非待财务审核状态", StateEnums.commodityState.已审过);
        return success("审核通过");
    }


    /**
     * 商品审核不通过
     */
    @PutMapping("/CommodityNoPass")
    public Object SfCommodityNoPass(String ids, String audit_desc) {
        List<String> list = Arrays.asList(ids.split(","));
        list.forEach(e -> {
            Integer id = Integer.valueOf(e);
            SfCommodity commodity = commodityService.getById(id);
            if (commodity.getState().equals(StateEnums.commodityState.未审核.code())) {
                Assert.state(false, "禁止提交未审核状态");
            }
            if (commodity.getState().equals(StateEnums.commodityState.未审过.code())) {
                Assert.state(false, "禁止重复操作");
            }
            if (commodity.getState().equals(StateEnums.commodityState.已审过.code())) {
                Assert.state(false, "禁止提交已审核状态");
            }
            UpdateWrapper<SfCommodity> wrapper = new UpdateWrapper<>();
            wrapper.eq("id", id);
            wrapper.set("state", StateEnums.commodityState.未审过.code());
            wrapper.set("audit_desc", audit_desc);
            commodityService.update(wrapper);
        });
        return success("审核不通过");
    }


    /**
     * 获取品牌列表
     */
    @GetMapping("/findBrandName")
    public Object findBrandName() {
        QueryWrapper<SfCommodity> wrapper = new QueryWrapper<SfCommodity>().select("brand_name");
        wrapper.eq("state", StateEnums.commodityState.已审过.code());
        List<SfCommodity> list = commodityService.list(wrapper);
        List<String> list1 = new ArrayList<>();
        list.forEach(e -> {
            list1.add(e.getBrandName());
        });
        List<String> collect = list1.stream().distinct().collect(Collectors.toList());
        return collect;
    }

    /**
     * 获取产品列表
     */
    @GetMapping("/findProductName")
    public Object findProductName(String brandName) {
        QueryWrapper<SfCommodity> wrapper = new QueryWrapper<SfCommodity>().select("product_name");
        wrapper.eq("brand_name", brandName);
        wrapper.eq("state", StateEnums.commodityState.已审过.code());
        List<SfCommodity> list = commodityService.list(wrapper);
        List<String> list1 = new ArrayList<>();
        list.forEach(e -> {
            list1.add(e.getProductName());
        });
        List<String> collect = list1.stream().distinct().collect(Collectors.toList());
        return collect;
    }

    /**
     * @param productName 品牌 brandName 产品
     * @deprecated 查看商品对应的渠道
     */
    @GetMapping("/findMedium")
    public Object findMedium(String productName, String brandName) {
        QueryWrapper<SfOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("product_name", productName);
        wrapper.eq("brand_name", brandName);
        List<SfOrder> list = sfOrderService.list(wrapper);
        if (list != null && list.size() > 0) {
            List<Integer> list1 = new ArrayList<>();
            list.forEach(e -> {
                list1.add(e.getMedium());
            });
            List<Integer> collect = list1.stream().distinct().collect(Collectors.toList());
            return sfMediumService.listByIds(collect);
        }
        return success(list);
    }


}
