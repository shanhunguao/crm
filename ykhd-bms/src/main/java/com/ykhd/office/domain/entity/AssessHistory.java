package com.ykhd.office.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;


/**
 * @author zhoufan
 * @Date 2020/11/11
 */
@TableName("bms_assess_history")
public class AssessHistory {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer templateId;
    private String content;
    private String weight;
    private String standard;
    private String type;
    private String target;
    private Integer userId;
    private Integer auditRoleId;
    private BigDecimal score;
    private BigDecimal grossScore;
    private String remark;
    private String assessTime;
    private String groupName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getAuditRoleId() {
        return auditRoleId;
    }

    public void setAuditRoleId(Integer auditRoleId) {
        this.auditRoleId = auditRoleId;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public BigDecimal getGrossScore() {
        return grossScore;
    }

    public void setGrossScore(BigDecimal grossScore) {
        this.grossScore = grossScore;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAssessTime() {
        return assessTime;
    }

    public void setAssessTime(String assessTime) {
        this.assessTime = assessTime;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
