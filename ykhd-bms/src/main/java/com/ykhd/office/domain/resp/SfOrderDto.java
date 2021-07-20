package com.ykhd.office.domain.resp;

import com.ykhd.office.domain.entity.SfOrderDetails;

import java.util.Date;
import java.util.List;

/**
 * @author zhoufan
 * @Date 2020/12/23
 * 赛峰档期
 */
public class SfOrderDto {
    private Integer id;
    private String brandName;//'产品名'
    private String productName;//'品牌'
    private String terrace;//'订单平台'
    private Integer orderDayNumber;//'开团当日订单数
    private Integer orderWeekNumber;//开团一周订单数
    private Integer isReturnMoney;//是否回款
    private String groupDate;//开团日期
    private Date createTime;//创建时间
    private Integer medium;//公众号
    private String returnRemark;//回款备注
    private String returnDate;//回款时间
    private Integer createUserid;//创建用户
    private String createUsername;
    private Integer state;//状态(1待开团 2已开团 3已完成 4删除)
    private String mediumName;//公众号名称
    private List<SfOrderDetails> sfOrderDetailsList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getTerrace() {
        return terrace;
    }

    public void setTerrace(String terrace) {
        this.terrace = terrace;
    }

    public Integer getOrderDayNumber() {
        return orderDayNumber;
    }

    public void setOrderDayNumber(Integer orderDayNumber) {
        this.orderDayNumber = orderDayNumber;
    }

    public Integer getOrderWeekNumber() {
        return orderWeekNumber;
    }

    public void setOrderWeekNumber(Integer orderWeekNumber) {
        this.orderWeekNumber = orderWeekNumber;
    }

    public Integer getIsReturnMoney() {
        return isReturnMoney;
    }

    public void setIsReturnMoney(Integer isReturnMoney) {
        this.isReturnMoney = isReturnMoney;
    }

    public String getGroupDate() {
        return groupDate;
    }

    public void setGroupDate(String groupDate) {
        this.groupDate = groupDate;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getMedium() {
        return medium;
    }

    public void setMedium(Integer medium) {
        this.medium = medium;
    }

    public String getReturnRemark() {
        return returnRemark;
    }

    public void setReturnRemark(String returnRemark) {
        this.returnRemark = returnRemark;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public Integer getCreateUserid() {
        return createUserid;
    }

    public void setCreateUserid(Integer createUserid) {
        this.createUserid = createUserid;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public List<SfOrderDetails> getSfOrderDetailsList() {
        return sfOrderDetailsList;
    }

    public void setSfOrderDetailsList(List<SfOrderDetails> sfOrderDetailsList) {
        this.sfOrderDetailsList = sfOrderDetailsList;
    }

    public String getCreateUsername() {
        return createUsername;
    }

    public void setCreateUsername(String createUsername) {
        this.createUsername = createUsername;
    }

    public String getMediumName() {
        return mediumName;
    }

    public void setMediumName(String mediumName) {
        this.mediumName = mediumName;
    }
}
