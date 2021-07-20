package com.ykhd.office.domain.req;

import com.ykhd.office.domain.bean.common.PageCondition;

import java.math.BigDecimal;

/**
 * @author zhoufan
 * @Date 2020/8/27
 */
public class JcFapiaoCondition extends PageCondition {
    private Integer id;
    private String billNo;        //票据号码
    private String enterName;      //单位名称
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
    private Integer auditUserid;   //审核员工id
    private Integer sellUserid;
    private String auditDesc;      //审核意见信息
    private String state;           //状态（0-填制，1-报送，2-审核通过，3-审核不通过，4-删除）
    private String isBalance;      //是否已结算（0-否，1-是）
    private BigDecimal actualMoney;     //实收合计
    private String sType; //审核状态
    private String dateType; //日期类型
    private String startdate;
    private String enddate;
    private String createDateStart;
    private String createDateEnd;
    private String makeDateStart;
    private String makeDateEnd;
    private String verificatioTimeStart;
    private String verificatioTimeEnd;
    private String[] status;
    private String order_xs;
    private String society;
    private String invoiceProject;      //发票项目
    private String mediumName;
    private Integer source;

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

    public Integer getAuditUserid() {
        return auditUserid;
    }

    public void setAuditUserid(Integer auditUserid) {
        this.auditUserid = auditUserid;
    }

    public Integer getSellUserid() {
        return sellUserid;
    }

    public void setSellUserid(Integer sellUserid) {
        this.sellUserid = sellUserid;
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

    public String getsType() {
        return sType;
    }

    public void setsType(String sType) {
        this.sType = sType;
    }

    public String getDateType() {
        return dateType;
    }

    public void setDateType(String dateType) {
        this.dateType = dateType;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public String getCreateDateStart() {
        return createDateStart;
    }

    public void setCreateDateStart(String createDateStart) {
        this.createDateStart = createDateStart;
    }

    public String getCreateDateEnd() {
        return createDateEnd;
    }

    public void setCreateDateEnd(String createDateEnd) {
        this.createDateEnd = createDateEnd;
    }

    public String getMakeDateStart() {
        return makeDateStart;
    }

    public void setMakeDateStart(String makeDateStart) {
        this.makeDateStart = makeDateStart;
    }

    public String getMakeDateEnd() {
        return makeDateEnd;
    }

    public void setMakeDateEnd(String makeDateEnd) {
        this.makeDateEnd = makeDateEnd;
    }

    public String getVerificatioTimeStart() {
        return verificatioTimeStart;
    }

    public void setVerificatioTimeStart(String verificatioTimeStart) {
        this.verificatioTimeStart = verificatioTimeStart;
    }

    public String getVerificatioTimeEnd() {
        return verificatioTimeEnd;
    }

    public void setVerificatioTimeEnd(String verificatioTimeEnd) {
        this.verificatioTimeEnd = verificatioTimeEnd;
    }

    public String[] getStatus() {
        return status;
    }

    public void setStatus(String[] status) {
        this.status = status;
    }

    public String getOrder_xs() {
        return order_xs;
    }

    public void setOrder_xs(String order_xs) {
        this.order_xs = order_xs;
    }

    public String getSociety() {
        return society;
    }

    public void setSociety(String society) {
        this.society = society;
    }

    public String getInvoiceProject() {
        return invoiceProject;
    }

    public void setInvoiceProject(String invoiceProject) {
        this.invoiceProject = invoiceProject;
    }

    public String getMediumName() {
        return mediumName;
    }

    public void setMediumName(String mediumName) {
        this.mediumName = mediumName;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }
}
