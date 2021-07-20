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
public class SfOrderLog {
    private static final Map<String, String> propertyTranslationMap = new HashMap<>();

    static {
        propertyTranslationMap.put("productName", "产品名");
        propertyTranslationMap.put("brandName", "品牌名");
        propertyTranslationMap.put("terrace", "订单平台");
        propertyTranslationMap.put("orderDayNumber", "开团当日订单数");
        propertyTranslationMap.put("orderWeekNumber", "开团一周订单数");
        propertyTranslationMap.put("groupDate", "开团日期");
    }

    public static CompareResult getCompareResult(Object source, Object target) {
        return BeanComparator.getCompareResult(source, target, propertyTranslationMap);
    }

}
