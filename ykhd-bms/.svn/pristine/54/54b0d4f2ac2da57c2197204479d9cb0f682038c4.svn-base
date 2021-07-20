package com.ykhd.office.domain.resp;

import cn.gjing.tools.excel.Excel;
import cn.gjing.tools.excel.ExcelField;
import cn.gjing.tools.excel.convert.ExcelDataConvert;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author zhoufan
 * @Date 40004000/9/18
 * 进项发票 EXcel 导出类
 */
@Excel("进项发票")
public class Fapiao_Excel {
    @ExcelField(value = "编号", width = 4000)
    private Integer id;
    @ExcelField(value = "发票号码", width = 4000)
    private String billNo;        //票据号码
    @ExcelField(value = "单位名称", width = 4000)
    private String enterName;      //单位名称
    @ExcelField(value = "金额", width = 4000)
    private BigDecimal money;           //金额
    @ExcelField(value = "税率", width = 4000)
    private BigDecimal rates;           //税率
    @ExcelField(value = "税额", width = 4000)
    private BigDecimal ratesMoney;     //税额
    @ExcelField(value = "税价合计", width = 4000)
    private BigDecimal totalMoney;     //价税合计
    @ExcelField(value = "发票类型", width = 4000)
    @ExcelDataConvert(expr1 = "#type=='0'?'普票':'专票'")
    private String type;            //发票类型（0-普票，1-专票）
    @ExcelField(value = "是否进项", width = 4000)
    @ExcelDataConvert(expr1 = "#is_add=='0'?'销项':'进项'")
    private String is_add;          //是否进项（0-销项，1-进项）
    @ExcelField(value = "创建用户", width = 4000)
    private String creatorName; //创建用户名称
    @ExcelField(value = "开票日期", format = "yyyy-MM-dd", width = 4000)
    private Date makeDate;         //开票日期
    @ExcelField(value = "认证时间", format = "yyyy-MM-dd", width = 4000)
    private Date verificatioTime;   //认证时间
    @ExcelField(value = "递交财务", width = 4000)
    private String auditUsername;
    @ExcelField(value = "发票状态", width = 4000)
    @ExcelDataConvert(expr1 = "#state=='0'?'填制':#state=='1'?'报送':#state=='2'?'审核通过':#state=='3'?'审核不通过':#state=='4'?'待结算':#state=='5'?'已完成':#state=='9'?'作废':'未知'")
    private String state;           //状态（0-填制，1-报送，2-审核通过，3-审核不通过，4-删除）
    @ExcelField("公众号")
    private String society;//公众号号
    @ExcelField(value = "排期日期", format = "yyyy-MM-dd")
    private String orderTime; //排期时间
    @ExcelField("负责销售")
    private String sellName; //负责销售
    private List<InvoiceRelevant2> invoiceRelevantList;
    @ExcelField(value = "实际税额", width = 4000)
    private String actualTax;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getEnterName() {
        return enterName;
    }

    public void setEnterName(String enterName) {
        this.enterName = enterName;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public BigDecimal getRates() {
        return rates;
    }

    public void setRates(BigDecimal rates) {
        this.rates = rates;
    }

    public BigDecimal getRatesMoney() {
        return ratesMoney;
    }

    public void setRatesMoney(BigDecimal ratesMoney) {
        this.ratesMoney = ratesMoney;
    }

    public BigDecimal getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(BigDecimal totalMoney) {
        this.totalMoney = totalMoney;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIs_add() {
        return is_add;
    }

    public void setIs_add(String is_add) {
        this.is_add = is_add;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public Date getMakeDate() {
        return makeDate;
    }

    public void setMakeDate(Date makeDate) {
        this.makeDate = makeDate;
    }

    public Date getVerificatioTime() {
        return verificatioTime;
    }

    public void setVerificatioTime(Date verificatioTime) {
        this.verificatioTime = verificatioTime;
    }

    public String getAuditUsername() {
        return auditUsername;
    }

    public void setAuditUsername(String auditUsername) {
        this.auditUsername = auditUsername;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

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

    public String getActualTax() {
        return actualTax;
    }

    public void setActualTax(String actualTax) {
        this.actualTax = actualTax;
    }

    public List<InvoiceRelevant2> getInvoiceRelevantList() {
        return invoiceRelevantList;
    }

    public void setInvoiceRelevantList(List<InvoiceRelevant2> invoiceRelevantList) {
        this.invoiceRelevantList = invoiceRelevantList;
    }
}
