package com.ykhd.office.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ykhd.office.domain.entity.Fapiao;
import com.ykhd.office.domain.resp.Fapiao_Excel;
import com.ykhd.office.domain.resp.Fapiao_Excel2;
import com.ykhd.office.domain.resp.Invoice;
import com.ykhd.office.domain.resp.OAScheduleDto;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface FapiaoMapper extends BaseMapper<Fapiao> {


    List<Invoice> queryPage(Page<?> page, @Param("condition") Map<String, Object> conditions);

    Integer count(@Param("condition") Map<String, Object> conditions);

    Map<String, Object> statsData(@Param("condition") Map<String, Object> conditions);

    List<Map<String, Object>> toBillPaymentPage(Page<?> page, @Param("condition") Map<String, Object> conditions);

    Integer toBillPaymentPageCount(@Param("condition") Map<String, Object> conditions);

    List<Fapiao_Excel> excelDrive(@Param("condition") Map<String, Object> conditions);

    List<Fapiao_Excel2> excelDrive2(@Param("condition") Map<String, Object> conditions);

    Map<String, Object> getBillInfoByCustomer(@Param("conditions") Map<String, Object> conditions);

    List<OAScheduleDto> mediumOrdersByPage(Page<?> page, @Param("condition") Map<String, Object> conditions);

    Integer mediumOrdersCount(@Param("condition") Map<String, Object> conditions);

    /**
     * 查看销售进项发票ID
     *
     * @return 进项发票ID
     */
    List<Integer> findFapiaoid(@Param("condition") Map<String, Object> conditions);

    /**
     * 查看进项发票开票百分比
     *
     * @param creator 销售人员ID
     * @return 开票占比
     * @Param list 进项发票ID
     */
    BigDecimal findDuty(@Param("fapiaoid") Integer fapiaoid, @Param("creator") Integer creator);

}
