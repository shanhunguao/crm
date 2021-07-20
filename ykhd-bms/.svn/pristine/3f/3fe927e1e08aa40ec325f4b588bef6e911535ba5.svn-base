package com.ykhd.office.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ykhd.office.domain.entity.Holiday;

import java.util.List;

public interface IHolidayService extends IService<Holiday> {
    /**
     * 获取时间段内的节假日
     *
     * @param start
     * @param end
     * @return
     */
    List<Integer> getHolidayByMonth(String start, String end);

    /**
     * 获取距离当月除节假日外的工作日
     */
    Integer getworkingDays();

}
