package com.ykhd.office.domain.req;

import com.ykhd.office.domain.bean.common.PageCondition;

import java.util.Date;

/**
 * 供应商账户信息
 */
public class SupplierCondition extends PageCondition {

    private Integer id;
    private String name;
    private String password;
    private Date createTime;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
