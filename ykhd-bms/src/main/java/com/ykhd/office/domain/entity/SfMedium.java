package com.ykhd.office.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

/**
 * @author zhoufan
 * @Date 2020/12/23
 * 赛峰公众号
 */
@TableName("sf_medium")
public class SfMedium {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;//渠道名称
    private String category;//渠道类别
    private String abutmentName;//对接人
    private String phone;//号主联系方式
    private Date createTime;//'创建时间'
    private Integer dockingor;//渠道对接人ID
    private Integer createUserid;//创建用户
    private Date updateTime;//更新时间

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAbutmentName() {
        return abutmentName;
    }

    public void setAbutmentName(String abutmentName) {
        this.abutmentName = abutmentName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getDockingor() {
        return dockingor;
    }

    public void setDockingor(Integer dockingor) {
        this.dockingor = dockingor;
    }

    public Integer getCreateUserid() {
        return createUserid;
    }

    public void setCreateUserid(Integer createUserid) {
        this.createUserid = createUserid;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
