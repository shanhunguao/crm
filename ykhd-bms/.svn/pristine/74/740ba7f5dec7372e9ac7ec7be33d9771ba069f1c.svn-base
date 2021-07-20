package com.ykhd.office.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ykhd.office.domain.entity.SfOrder;
import com.ykhd.office.domain.resp.SfOrderDto;
import org.apache.ibatis.annotations.Param;


import java.util.List;
import java.util.Map;

public interface SfOrderMapper extends BaseMapper<SfOrder> {

    void add(SfOrder sfOrder);

    Map<String, Object> statsData(@Param("condition") Map<String, Object> conditions);

    List<SfOrderDto> queryPage(Page<?> page, @Param("condition") Map<String, Object> conditions);

    Integer count(@Param("condition") Map<String, Object> conditions);

}
