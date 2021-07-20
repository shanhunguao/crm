package com.ykhd.office.util.bean_compare;

import com.xkzhangsan.xkbeancomparator.BeanComparator;
import com.xkzhangsan.xkbeancomparator.CompareResult;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhoufan
 * @Date 2020/12/30
 * java bean对比修改并输出差异
 */
public class SfCommodityLog {
    private static final Map<String, String> propertyTranslationMap = new HashMap<>();

    static {
        propertyTranslationMap.put("createUsername", "创建用户");
        propertyTranslationMap.put("brandName", "品牌名称");
        propertyTranslationMap.put("productName", "产品名称");
        propertyTranslationMap.put("originPlace", "产地");
        propertyTranslationMap.put("logistics", "发货物流");
        propertyTranslationMap.put("freight_template", "运费模板");
        propertyTranslationMap.put("shipAddress", "发货地址");
        propertyTranslationMap.put("returnAddress", "退货地址");
        propertyTranslationMap.put("brandIntroduction", "品牌介绍");
        propertyTranslationMap.put("sellingPoints", "卖点介绍");
        propertyTranslationMap.put("analyse", "竞品分析");
        propertyTranslationMap.put("channel", "已经开团渠道");
        propertyTranslationMap.put("photo_block", "产品背面");
    }

    public static CompareResult getCompareResult(Object source, Object target) {
        return BeanComparator.getCompareResult(source, target, propertyTranslationMap);
    }

}
