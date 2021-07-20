package com.ykhd.office.domain.entity;

import java.math.BigDecimal;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ykhd.office.domain.bean.SpecificationGroup;

/**
 * @author zhoufan
 * @Date 2020/12/18
 */
@TableName("sf_commodity_details")
public class SfCommodityDetails {
    @TableId(type = IdType.AUTO)
    private Integer id;
    @TableField(exist = false)
    private List<SpecificationGroup> specificationGroup;
    private String specificationName;//规格名
    private String specificationValue;//规格值
    private String specificationImage;//规格图片
    private BigDecimal officialRetailPrice;//官方零售价
    private BigDecimal minimumActivePrice;//最低活动价
    private BigDecimal livePrice;//直播价
    private BigDecimal groupPurchasePrice;//团购价
    private BigDecimal supplyPrice;//供货价（含税）+运费
    private BigDecimal supplyNoPrice;//供货价（不含税）+运费
    private BigDecimal channelPrice;//终端渠道供货价
    private String isgift;//是否赠品扶持(0是1否)
    private Integer repertory;//库存
    private String merchantNumber;//Sku编码
    private Integer commodityId;//商品ID
    private Integer specificationProject;
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<SpecificationGroup> getSpecificationGroup() {
        return specificationGroup;
    }

    public void setSpecificationGroup(List<SpecificationGroup> specificationGroup) {
        this.specificationGroup = specificationGroup;
    }

    public String getSpecificationName() {
        return specificationName;
    }

    public void setSpecificationName(String specificationName) {
        this.specificationName = specificationName;
    }

    public String getSpecificationValue() {
        return specificationValue;
    }

    public void setSpecificationValue(String specificationValue) {
        this.specificationValue = specificationValue;
    }

    public String getSpecificationImage() {
        return specificationImage;
    }

    public void setSpecificationImage(String specificationImage) {
        this.specificationImage = specificationImage;
    }

    public BigDecimal getOfficialRetailPrice() {
        return officialRetailPrice;
    }

    public void setOfficialRetailPrice(BigDecimal officialRetailPrice) {
        this.officialRetailPrice = officialRetailPrice;
    }

    public BigDecimal getMinimumActivePrice() {
        return minimumActivePrice;
    }

    public void setMinimumActivePrice(BigDecimal minimumActivePrice) {
        this.minimumActivePrice = minimumActivePrice;
    }

    public BigDecimal getLivePrice() {
        return livePrice;
    }

    public void setLivePrice(BigDecimal livePrice) {
        this.livePrice = livePrice;
    }

    public BigDecimal getGroupPurchasePrice() {
        return groupPurchasePrice;
    }

    public void setGroupPurchasePrice(BigDecimal groupPurchasePrice) {
        this.groupPurchasePrice = groupPurchasePrice;
    }

    public BigDecimal getSupplyPrice() {
        return supplyPrice;
    }

    public void setSupplyPrice(BigDecimal supplyPrice) {
        this.supplyPrice = supplyPrice;
    }

    public BigDecimal getSupplyNoPrice() {
        return supplyNoPrice;
    }

    public void setSupplyNoPrice(BigDecimal supplyNoPrice) {
        this.supplyNoPrice = supplyNoPrice;
    }

    public BigDecimal getChannelPrice() {
        return channelPrice;
    }

    public void setChannelPrice(BigDecimal channelPrice) {
        this.channelPrice = channelPrice;
    }

    public String getIsgift() {
        return isgift;
    }

    public void setIsgift(String isgift) {
        this.isgift = isgift;
    }

    public Integer getRepertory() {
        return repertory;
    }

    public void setRepertory(Integer repertory) {
        this.repertory = repertory;
    }

    public String getMerchantNumber() {
        return merchantNumber;
    }

    public void setMerchantNumber(String merchantNumber) {
        this.merchantNumber = merchantNumber;
    }

    public Integer getSfCommodityId() {
        return commodityId;
    }

    public void setSfCommodityId(Integer commodityId) {
        this.commodityId = commodityId;
    }

    public Integer getSpecificationProject() {
        return specificationProject;
    }

    public void setSpecificationProject(Integer specificationProject) {
        this.specificationProject = specificationProject;
    }
}
