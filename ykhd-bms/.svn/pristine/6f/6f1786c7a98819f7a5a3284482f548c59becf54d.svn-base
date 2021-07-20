package com.ykhd.office.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xkzhangsan.xkbeancomparator.CompareResult;
import com.ykhd.office.controller.BaseController;
import com.ykhd.office.domain.bean.CommodityDetails;
import com.ykhd.office.domain.bean.common.PageHelper;
import com.ykhd.office.domain.entity.SfCommodity;
import com.ykhd.office.domain.entity.SfCommodityDetails;
import com.ykhd.office.domain.entity.SfOrderHistory;
import com.ykhd.office.domain.entity.SfSupplierDetails;
import com.ykhd.office.domain.req.SfCommodityCondition;
import com.ykhd.office.domain.resp.SfCommodityDto;
import com.ykhd.office.repository.SfCommodityMapper;
import com.ykhd.office.repository.SfOrderHistoryMapper;
import com.ykhd.office.repository.SfSupplierDetailsMapper;
import com.ykhd.office.service.ISfCommodityService;
import com.ykhd.office.util.bean_compare.SfCommodityDetailsLog;
import com.ykhd.office.util.bean_compare.SfCommodityLog;
import com.ykhd.office.util.dictionary.StateEnums;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class SfCommodityService extends ServiceImpl<BaseMapper<SfCommodity>, SfCommodity> implements ISfCommodityService {

    @Autowired
    private SfCommodityMapper commodityMapper;

    @Autowired
    private SfSupplierDetailsMapper supplierDetailsMapper;

    @Autowired
    private SfOrderHistoryMapper sfOrderHistoryMapper;

    @Override
    public PageHelper<SfCommodityDto> list(SfCommodityCondition condition) {
        QueryWrapper<SfCommodity> wrapper = getSfCommodityQueryWrapper(condition);
        IPage<SfCommodity> commodityIPage = commodityMapper.selectPage(new Page<>(condition.getPage(), condition.getSize()), wrapper);
        List<SfCommodity> records = commodityIPage.getRecords();
        List<SfCommodityDto> list = new ArrayList<>();
//        查询商户公司
        records.forEach(e -> {
            SfCommodityDto sfCommodityDto = new SfCommodityDto();
            BeanUtils.copyProperties(e, sfCommodityDto);
            QueryWrapper<SfSupplierDetails> wrapper1 = new QueryWrapper<>();
            wrapper1.eq("supplier_userid", e.getSupplierUserid());
            SfSupplierDetails sfSupplierDetails = supplierDetailsMapper.selectOne(wrapper1);
            if (sfSupplierDetails != null) {
                sfCommodityDto.setCompanyName(sfSupplierDetails.getCompanyName());
            }
            list.add(sfCommodityDto);
        });
        return new PageHelper<>(condition.getPage(), condition.getSize(), commodityIPage.getTotal(), list);
    }

    /**
     * 封装查询条件
     */
    private QueryWrapper<SfCommodity> getSfCommodityQueryWrapper(SfCommodityCondition condition) {
        QueryWrapper<SfCommodity> wrapper = new QueryWrapper<>();
        if (condition.getState() != null) {
            wrapper.eq("state", condition.getState());
        } else {
            wrapper.ne("state", StateEnums.commodityState.未审核.code());
        }
        if (condition.getCompanyName() != null) {
            QueryWrapper<SfSupplierDetails> wrapper1 = new QueryWrapper<>();
            wrapper1.eq("company_name", condition.getCompanyName());
            wrapper1.select("supplier_userid");
            List<SfSupplierDetails> supplierDetails = supplierDetailsMapper.selectList(wrapper1);
            List<Integer> list = new ArrayList<>();
            supplierDetails.forEach(e -> {
                list.add(e.getSupplierUserid());
            });
            wrapper.in("supplier_userid", list);
        }
        if (StringUtils.isNotEmpty(condition.getBrandName())) {
            wrapper.like("brand_name", condition.getBrandName());
        }
        if (StringUtils.isNotEmpty(condition.getProductName())) {
            wrapper.like("product_name", condition.getProductName());
        }
        if (StringUtils.isNotEmpty(condition.getCreateUsername())) {
            wrapper.like("create_username", condition.getCreateUsername());
        }
        wrapper.apply(StringUtils.isNotEmpty(condition.getCreateDateStart()),
                "date_format (create_time,'%Y-%m-%d') >= date_format('" + condition.getCreateDateStart() + "','%Y-%m-%d')");
        wrapper.apply(StringUtils.isNotEmpty(condition.getCreateDateEnd()),
                "date_format (create_time,'%Y-%m-%d') <= date_format('" + condition.getCreateDateEnd() + "','%Y-%m-%d')");
        wrapper.orderByDesc("create_time");
        return wrapper;
    }


    @Override
    public void add(SfCommodity commodity) {
        commodityMapper.add(commodity);
    }

    @Override
    public SfCommodityDto findId(Integer id) {
        return commodityMapper.findId(id);
    }

    @Override
    public void addhistory(Integer orderid, List<SfCommodityDetails> list, List<SfCommodityDetails> list2) {
        List<CommodityDetails> olddata = new ArrayList<>();
        List<CommodityDetails> newdata = new ArrayList<>();
        list.forEach(e -> {
            CommodityDetails commodityDetails = new CommodityDetails();
            BeanUtils.copyProperties(e, commodityDetails);
            commodityDetails.setOfficialRetailPrice(e.getOfficialRetailPrice().setScale(2,BigDecimal.ROUND_DOWN));
            commodityDetails.setMinimumActivePrice(e.getMinimumActivePrice().setScale(2,BigDecimal.ROUND_DOWN));
            commodityDetails.setLivePrice(e.getLivePrice().setScale(2,BigDecimal.ROUND_DOWN));
            commodityDetails.setGroupPurchasePrice(e.getGroupPurchasePrice().setScale(2,BigDecimal.ROUND_DOWN));
            commodityDetails.setSupplyPrice(e.getSupplyPrice().setScale(2,BigDecimal.ROUND_DOWN));
            commodityDetails.setSupplyNoPrice(e.getSupplyNoPrice().setScale(2,BigDecimal.ROUND_DOWN));
            commodityDetails.setChannelPrice(e.getChannelPrice().setScale(2,BigDecimal.ROUND_DOWN));
            newdata.add(commodityDetails);
        });
        list2.forEach(e -> {
            CommodityDetails commodityDetails = new CommodityDetails();
            BeanUtils.copyProperties(e, commodityDetails);
            olddata.add(commodityDetails);
        });
        for (int i = 0; i < newdata.size(); i++) {
            for (int j = 0; j < olddata.size(); j++) {
                if (newdata.get(i).getSpecificationName().equals(olddata.get(j).getSpecificationName())) {
                    CompareResult compareResult = SfCommodityDetailsLog.getCompareResult(olddata.get(j), newdata.get(i));
                    if (compareResult.isChanged()) {
                        String content = "规格:" + newdata.get(i).getSpecificationName() + "," + compareResult.getChangeContent();
                        SfOrderHistory sfOrderHistory = new SfOrderHistory();
                        sfOrderHistory.setOrderId(orderid);
                        sfOrderHistory.setCreateUsername(BaseController.getManagerInfo().getName());
                        sfOrderHistory.setType(0);
                        sfOrderHistory.setContent(content);
                        sfOrderHistoryMapper.insert(sfOrderHistory);
                    }
                }
            }
        }
    }

    @Override
    public void addhistory(SfCommodity newcommodity, SfCommodity oldcommodity) {
        CompareResult compareResult = SfCommodityLog.getCompareResult(oldcommodity, newcommodity);
        if (compareResult.isChanged()) {
            SfOrderHistory sfOrderHistory = new SfOrderHistory();
            sfOrderHistory.setOrderId(oldcommodity.getId());
            sfOrderHistory.setCreateUsername(BaseController.getManagerInfo().getName());
            sfOrderHistory.setType(0);
            sfOrderHistory.setContent(compareResult.getChangeContent());
            sfOrderHistoryMapper.insert(sfOrderHistory);
        }
    }
}
