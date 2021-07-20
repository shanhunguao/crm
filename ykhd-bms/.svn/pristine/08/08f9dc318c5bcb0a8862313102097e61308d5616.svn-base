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
public class SfCommodityDetailsLog {
    private static final Map<String, String> propertyTranslationMap = new HashMap<>();

    static {
        propertyTranslationMap.put("officialRetailPrice", "官方零售价");
        propertyTranslationMap.put("minimumActivePrice", "最低活动价");
        propertyTranslationMap.put("livePrice", "直播价");
        propertyTranslationMap.put("groupPurchasePrice", "团购价");
        propertyTranslationMap.put("supplyPrice", "供货价（含税）+运费");
        propertyTranslationMap.put("supplyNoPrice", "供货价（不含税）+运费");
        propertyTranslationMap.put("channelPrice", "终端渠道供货价");
        propertyTranslationMap.put("isgift", "是否赠品扶持");
        propertyTranslationMap.put("repertory", "库存");
        propertyTranslationMap.put("merchantNumber", "Sku编码");
    }

    public static CompareResult getCompareResult(Object source, Object target) {
        return BeanComparator.getCompareResult(source, target, propertyTranslationMap);
    }

}
