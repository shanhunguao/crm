package com.ykhd.office.domain.resp;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author zhoufan
 * @Date 2020/8/26
 */
public class FaPiaoDto {
    private Integer id;
    private String billNo;        //票据号码
    private String enterName;      //单位名称
    private Date makeDate;         //开票日期
    private Date verificatioTime;   //认证时间
    private String makeName;       //开具单位
    private String dutyNo;         //税号
    private String bankName;       //开户行
    private String bankNo;         //银行账号
    private String bankAddress;    //银行地址
    private String address;         //地址
    private String phone;           //电话
    private BigDecimal money;           //金额
    private BigDecimal rates;           //税率
    private BigDecimal ratesMoney;     //税额
    private BigDecimal totalMoney;     //价税合计
    private String type;            //发票类型（0-普票，1-专票）
    private String isAdd;          //是否进项（0-销项，1-进项）
    private Integer createUserid;  //创建用户id
    private String createUsername; //创建用户名称
    private Date createDate;     //创建时间
    private Integer auditUserid;   //审核员工id
    private String auditDesc;      //审核意见信息
    private String state;           //状态（0-填制，1-报送，2-审核通过，3-审核不通过，4-删除）
    private String isBalance;      //是否已结算（0-否，1-是）
    private BigDecimal actualMoney;     //实收合计
    private String fapiaoImg;
    private String invoiceProject;      //发票项目
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

    public String getMakeName() {
        return makeName;
    }

    public void setMakeName(String makeName) {
        this.makeName = makeName;
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

    public String getBankAddress() {
        return bankAddress;
    }

    public void setBankAddress(String bankAddress) {
        this.bankAddress = bankAddress;
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

    public String getIsAdd() {
        return isAdd;
    }

    public void setIsAdd(String isAdd) {
        this.isAdd = isAdd;
    }

    public Integer getCreateUserid() {
        return createUserid;
    }

    public void setCreateUserid(Integer createUserid) {
        this.createUserid = createUserid;
    }

    public String getCreateUsername() {
        return createUsername;
    }

    public void setCreateUsername(String createUsername) {
        this.createUsername = createUsername;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getAuditUserid() {
        return auditUserid;
    }

    public void setAuditUserid(Integer auditUserid) {
        this.auditUserid = auditUserid;
    }

    public String getAuditDesc() {
        return auditDesc;
    }

    public void setAuditDesc(String auditDesc) {
        this.auditDesc = auditDesc;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getIsBalance() {
        return isBalance;
    }

    public void setIsBalance(String isBalance) {
        this.isBalance = isBalance;
    }

    public BigDecimal getActualMoney() {
        return actualMoney;
    }

    public void setActualMoney(BigDecimal actualMoney) {
        this.actualMoney = actualMoney;
    }

    public String getFapiaoImg() {
        return fapiaoImg;
    }

    public void setFapiaoImg(String fapiaoImg) {
        this.fapiaoImg = fapiaoImg;
    }

    public String getInvoiceProject() {
        return invoiceProject;
    }

    public void setInvoiceProject(String invoiceProject) {
        this.invoiceProject = invoiceProject;
    }
}
