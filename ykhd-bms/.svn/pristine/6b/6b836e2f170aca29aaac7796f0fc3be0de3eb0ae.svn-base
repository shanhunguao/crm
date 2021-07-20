package com.ykhd.office.domain.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 客户
 */
@TableName("wm_customer")
public class Customer {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private String company;         //公司名称
    private String brand;           //品牌
    private String industry;        //一级行业
    private String domain;   //二级行业
    private String linkman;         //联系人名称
    private String phone;           //手机
    private String qq;              //qq
    private String wx;              //微信
    private Integer creator;    //创建人
    private Date createTime;
    private Integer belongUser;    //所属人
    private String isLock;         //是否锁定（0-否，1-是，2-公海）
    private Date highSeaTime;        //退入公海时间
    private Date drawTime;  //领取时间
    @TableLogic
    private Integer isDeleted;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getLinkman() {
        return linkman;
    }

    public void setLinkman(String linkman) {
        this.linkman = linkman;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getWx() {
        return wx;
    }

    public void setWx(String wx) {
        this.wx = wx;
    }

    public Integer getCreator() {
        return creator;
    }

    public void setCreator(Integer creator) {
        this.creator = creator;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getBelongUser() {
        return belongUser;
    }

    public void setBelongUser(Integer belongUser) {
        this.belongUser = belongUser;
    }

    public String getIsLock() {
        return isLock;
    }

    public void setIsLock(String isLock) {
        this.isLock = isLock;
    }

    public Date getHighSeaTime() {
        return highSeaTime;
    }

    public void setHighSeaTime(Date highSeaTime) {
        this.highSeaTime = highSeaTime;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Date getDrawTime() {
        return drawTime;
    }

    public void setDrawTime(Date drawTime) {
        this.drawTime = drawTime;
    }
}
