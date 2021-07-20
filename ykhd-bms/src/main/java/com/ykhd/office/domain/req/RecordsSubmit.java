package com.ykhd.office.domain.req;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author zhoufan
 * @Date 2020/8/24
 */
public class RecordsSubmit {

    private Integer id;
    @NotNull(message = "客户不能为空")
    private Integer customerId;//关联客户id

    private Integer managerId;//录入记录销售id
    @NotNull(message = "跟进记录")
    private String record;//跟进记录

    private Date createTime;//跟进记录创建时间
    @NotNull(message = "回访时间")
    private String appointTime;//下次回访时间

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getManagerId() {
        return managerId;
    }

    public void setManagerId(Integer managerId) {
        this.managerId = managerId;
    }

    public String getRecord() {
        return record;
    }

    public void setRecord(String record) {
        this.record = record;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getAppointTime() {
        return appointTime;
    }

    public void setAppointTime(String appointTime) {
        this.appointTime = appointTime;
    }
}
