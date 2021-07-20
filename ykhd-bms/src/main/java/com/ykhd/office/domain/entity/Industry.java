package com.ykhd.office.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * @author zhoufan
 * @Date 2020/8/21
 */
@TableName("wm_industry")
public class Industry {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer industryId;
    private Integer industryPid;
    private String industryName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIndustryId() {
        return industryId;
    }

    public void setIndustryId(Integer industryId) {
        this.industryId = industryId;
    }

    public Integer getIndustryPid() {
        return industryPid;
    }

    public void setIndustryPid(Integer industryPid) {
        this.industryPid = industryPid;
    }

    public String getIndustryName() {
        return industryName;
    }

    public void setIndustryName(String industryName) {
        this.industryName = industryName;
    }
}
