package com.ykhd.office.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

/**
 * @author zhoufan
 * @Date 2020/10/22
 */
@TableName("bms_holiday")
public class Holiday {
    private Date holiday;

    public Date getHoliday() {
        return holiday;
    }

    public void setHoliday(Date holiday) {
        this.holiday = holiday;
    }
}
