package com.ykhd.office.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ykhd.office.domain.entity.JcFapiao;
import com.ykhd.office.domain.resp.Fapiao_Excel;
import com.ykhd.office.domain.resp.Fapiao_Excel2;
import com.ykhd.office.domain.resp.Invoice;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface JcFapiaoMapper extends BaseMapper<JcFapiao> {


    List<Invoice> queryPage(Page<?> page, @Param("condition") Map<String, Object> conditions);

    Integer count(@Param("condition") Map<String, Object> conditions);

    Map<String, Object> statsData(@Param("condition") Map<String, Object> conditions);

    List<Map<String, Object>> toBillPaymentPage(Page<?> page, @Param("condition") Map<String, Object> conditions);

    Integer toBillPaymentPageCount(@Param("condition") Map<String, Object> conditions);

    Map<String, Object> getBillInfoByCustomer(@Param("conditions") Map<String, Object> conditions);


    List<Fapiao_Excel> excelDrive(Map<String, Object> conditions);

    BigDecimal findDuty(Integer id, Integer sellId);

    List<Fapiao_Excel2> excelDrive2(Map<String, Object> conditions);
}
