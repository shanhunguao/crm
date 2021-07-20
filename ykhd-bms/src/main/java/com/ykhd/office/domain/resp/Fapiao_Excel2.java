package com.ykhd.office.domain.resp;

import java.math.BigDecimal;
import java.util.Date;

import cn.gjing.tools.excel.Excel;
import cn.gjing.tools.excel.ExcelField;
import cn.gjing.tools.excel.convert.ExcelDataConvert;

/**
 * @author zhoufan
 * @Date 2020/9/18
 * 进项发票 EXcel 导出类
 */
@Excel("销项发票")
public class Fapiao_Excel2 {
    @ExcelField(value = "编号",width = 4000)
    private Integer id;
    @ExcelField(value = "发票号码",width = 4000)
    private String billNo;        //票据号码
    @ExcelField(value = "单位名称",width = 4000)
    private String enterName;      //单位名称
    @ExcelField(value = "开票单位",width = 4000)
    private String makeName;
    @ExcelField(value = "开票日期", format = "yyyy-MM-dd",width = 4000)
    private Date makeDate;
    @ExcelField(value = "税号",width = 4000)
    private String dutyNo;
    @ExcelField(value = "开户行",width = 4000)
    private String bankName;
    @ExcelField(value = "银行账号",width = 4000)
    private String bankNo;
    @ExcelField(value = "地址",width = 4000)
    private String address;
    @ExcelField(value = "电话",width = 4000)
    private String phone;
    @ExcelField(value = "金额",width = 4000)
    private BigDecimal money;           //金额
    @ExcelField(value = "税率",width = 4000)
    private BigDecimal rates;           //税率
    @ExcelField(value = "税额",width = 4000)
    private BigDecimal ratesMoney;     //税额
    @ExcelField(value = "税价合计",width = 4000)
    private BigDecimal totalMoney;     //价税合计
    @ExcelField(value = "发票类型",width = 4000)
    @ExcelDataConvert(expr1 = "#type=='0'?'普票':'专票'")
    private String type;            //发票类型（0-普票，1-专票）
    @ExcelField(value = "是否进项",width = 4000)
    @ExcelDataConvert(expr1 = "#is_add=='0'?'进项':'销项'")
    private String is_add;          //是否进项（0-销项，1-进项）
    @ExcelField(value = "发票项目",width = 4000)
    private String invoiceProject;
    @ExcelField(value = "创建用户",width = 4000)
    private String creatorName; //创建用户名称
    @ExcelField(value = "创建时间", format = "yyyy-MM-dd")
    private String createDate;
    @ExcelField(value = "审核人",width = 4000)
    private String auditUsername;
    @ExcelField(value = "发票状态",width = 4000)
    @ExcelDataConvert(expr1 = "#state=='0'?'填制':#state=='1'?'报送':#state=='2'?'审核通过':#state=='3'?'审核不通过':#state=='4'?'待结算':#state=='5'?'已完成':#state=='9'?'作废':'未知'")
    private String state;           //状态（0-填制，1-报送，2-审核通过，3-审核不通过，4-删除）

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

    public String getMakeName() {
        return makeName;
    }

    public void setMakeName(String makeName) {
        this.makeName = makeName;
    }

    public Date getMakeDate() {
        return makeDate;
    }

    public void setMakeDate(Date makeDate) {
        this.makeDate = makeDate;
    }

    public String getDutyNo() {
        return dutyNo;
    }

    public void setDutyNo(String dutyNo) {
        this.dutyNo = dutyNo;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankNo() {
        return bankNo;
    }

    public void setBankNo(String bankNo) {
        this.bankNo = bankNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getInvoiceProject() {
        return invoiceProject;
    }

    public void setInvoiceProject(String invoiceProject) {
        this.invoiceProject = invoiceProject;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
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
}
