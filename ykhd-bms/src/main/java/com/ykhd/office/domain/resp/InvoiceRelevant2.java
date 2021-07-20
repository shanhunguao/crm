package com.ykhd.office.domain.resp;

/**
 * @author zhoufan
 * @Date 2020/12/3
 */
public class InvoiceRelevant2 {

    private String society;//公众号号

    private String orderTime; //排期时间

    private String sellName; //负责销售

    private Integer sellId; //负责销售

    public String getSociety() {
        return society;
    }

    public void setSociety(String society) {
        this.society = society;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getSellName() {
        return sellName;
    }

    public void setSellName(String sellName) {
        this.sellName = sellName;
    }

    public Integer getSellId() {
        return sellId;
    }

    public void setSellId(Integer sellId) {
        this.sellId = sellId;
    }
}
