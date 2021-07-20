package com.ykhd.office.repository;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ykhd.office.domain.entity.Customer;
import com.ykhd.office.domain.resp.Workbench;
import com.ykhd.office.domain.req.CustomerCondition;
import com.ykhd.office.domain.resp.CustomerDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CustomerMapper extends BaseMapper<Customer> {

    @SqlParser(filter = true)
    IPage<CustomerDto> getListByPage(Page<?> page, @Param("condition") CustomerCondition condition);

    @SqlParser(filter = true)
    Integer getListByPageCount(@Param("condition") CustomerCondition condition);

    List<Customer> find(@Param("condition") Map<?, ?> condition);

    List<Customer> toHighseasCustomer();

    Workbench workbenchInfo(@Param("condition") Map<?, ?> condition);


    List<Map<String, Object>> industryRate(@Param("condition") Map<String, Object> condition);

    Integer getcooperation(Integer userid);

    String getendOrder(Integer userid);

    List<Map<String, Object>> findInfoByCustomerId(Integer customerid);

}
