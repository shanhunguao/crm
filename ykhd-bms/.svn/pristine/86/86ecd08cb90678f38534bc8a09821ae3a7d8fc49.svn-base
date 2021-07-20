package com.ykhd.office.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

/**
 * @author zhoufan
 * @Date 2020/12/18
 */
@TableName("sf_picture_library")
public class PictureLibrary {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private String imagesName;
    private String imagesPath;
    private Integer supplierUserid;
    private Date createTime;
    private Integer type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImagesName() {
        return imagesName;
    }

    public void setImagesName(String imagesName) {
        this.imagesName = imagesName;
    }

    public String getImagesPath() {
        return imagesPath;
    }

    public void setImagesPath(String imagesPath) {
        this.imagesPath = imagesPath;
    }

    public Integer getSupplierUserid() {
        return supplierUserid;
    }

    public void setSupplierUserid(Integer supplierUserid) {
        this.supplierUserid = supplierUserid;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
