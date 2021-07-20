package com.ykhd.office.service.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ykhd.office.domain.entity.Holiday;
import com.ykhd.office.repository.HolidayMapper;
import com.ykhd.office.service.IHolidayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * @author zhoufan
 * @Date 2020/10/22
 */
@Service
public class HolidayService extends ServiceImpl<BaseMapper<Holiday>, Holiday> implements IHolidayService {

    @Autowired
    private HolidayMapper holidayMapper;


    @Override
    public List<Integer> getHolidayByMonth(String start, String end) {
        return holidayMapper.getHolidayByMonth(start, end);
    }

    @Override
    public Integer getworkingDays() {
        Calendar cale = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:OO"));
        int daycount = cale.get(Calendar.DAY_OF_MONTH);
        int lastcount = cale.getActualMaximum(Calendar.DAY_OF_MONTH);    //获取本月最大天数
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String first = dateFormat.format(date);
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        String last = dateFormat.format(ca.getTime());
        List<Integer> list = holidayMapper.getHolidayByMonth(first, last);
        int i = 0;
//        判断当前日期是否为节假日
        if (list.contains(daycount)) {
            i = (lastcount - daycount + 1) - (list.size());
        } else {
            i = (lastcount - daycount + 1) - list.size();
        }
        return i;
    }


}
