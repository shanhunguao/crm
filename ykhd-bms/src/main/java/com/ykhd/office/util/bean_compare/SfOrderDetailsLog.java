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
public class SfOrderDetailsLog {
    private static final Map<String, String> propertyTranslationMap = new HashMap<>();

    static {
        propertyTranslationMap.put("specificationName", "规格值");
        propertyTranslationMap.put("supplyPrice", "供货价");
        propertyTranslationMap.put("groupPurchasePrice", "团购价");

    }

    public static CompareResult getCompareResult(Object source, Object target) {
        return BeanComparator.getCompareResult(source, target, propertyTranslationMap);
    }

}
