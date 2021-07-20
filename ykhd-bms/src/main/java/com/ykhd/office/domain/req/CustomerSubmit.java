package com.ykhd.office.domain.req;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author zhoufan
 * @Date 2020/8/21
 */
public class CustomerSubmit {
    private Integer id;
    /**
     * 公司名称
     */
    @NotNull(message = "公司名称不能为空")
    private String company;
    /**
     * 品牌
     */
    @NotNull(message = "品牌不能为空")
    private String brand;
    /**
     * 行业
     */
    @NotNull(message = "行业不能为空")
    private String industry;
    /**
     * 领域
     */
    @NotNull(message = "领域不能空")
    private String domain;
    /**
     * 联系人名称
     */
    @NotNull(message = "联系人不能为空")
    private String linkman;
    /**
     * 联系电话
     */
    private String phone;
    /**
     * qq
     */
    private String qq;
    /**
     * 微信
     */
    private String wx;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 创建人id
     */
    private Integer creator;

    /**
     * 所属人
     */
    @NotNull(message = "所属人不能空")
    private Integer belongUser;

    /**
     * 是否锁定（0-否，1-是，2-公海）
     */
    private Integer isLock;
    /**
     * 进入公海时间
     */
    private Date highSeaTimeStart;

    private Date highSeaTimeEnd;

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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getCreator() {
        return creator;
    }

    public void setCreator(Integer creator) {
        this.creator = creator;
    }

    public Integer getBelongUser() {
        return belongUser;
    }

    public void setBelongUser(Integer belongUser) {
        this.belongUser = belongUser;
    }

    public Integer getIsLock() {
        return isLock;
    }

    public void setIsLock(Integer isLock) {
        this.isLock = isLock;
    }

    public Date getHighSeaTimeStart() {
        return highSeaTimeStart;
    }

    public void setHighSeaTimeStart(Date highSeaTimeStart) {
        this.highSeaTimeStart = highSeaTimeStart;
    }

    public Date getHighSeaTimeEnd() {
        return highSeaTimeEnd;
    }

    public void setHighSeaTimeEnd(Date highSeaTimeEnd) {
        this.highSeaTimeEnd = highSeaTimeEnd;
    }
}
