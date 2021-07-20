package com.ykhd.office.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ykhd.office.domain.entity.Holiday;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface HolidayMapper extends BaseMapper<Holiday> {

    /**
     * 获取时间段内的节假日
     *
     * @param start
     * @param end
     * @return
     */
    List<Integer> getHolidayByMonth(@Param("start") String start, @Param("end") String end);
}
