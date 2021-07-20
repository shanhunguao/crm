package com.ykhd.office.domain.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * @author zhoufan
 * @Date 2020/11/7
 * 绩效模板
 */
@TableName("bms_assess_template")
public class AssessTemplate {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String content; //考核内容
    private String weight; //权重
    private String standard; //考核标准
    private String type; //类型
    private String target;//目标
    private Integer auditRoleId; //考核角色
    private Integer roleId; //被考核角色
    private Date createTime; //创建时间
    private String groupName; //考核组
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getAuditRoleId() {
        return auditRoleId;
    }

    public void setAuditRoleId(Integer auditRoleId) {
        this.auditRoleId = auditRoleId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
