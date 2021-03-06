package com.ykhd.office.service;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ykhd.office.domain.bean.FapiaoSimpleInfo;
import com.ykhd.office.domain.bean.LoginCache;
import com.ykhd.office.domain.bean.common.PageHelper2;
import com.ykhd.office.domain.bean.common.PageHelpers;
import com.ykhd.office.domain.entity.Fapiao;
import com.ykhd.office.domain.req.FapiaoCondition;
import com.ykhd.office.domain.req.FapiaoSubmit;
import com.ykhd.office.domain.resp.Fapiao_Excel;
import com.ykhd.office.domain.resp.Fapiao_Excel2;
import com.ykhd.office.domain.resp.Invoice;
import com.ykhd.office.domain.resp.OAScheduleDto;

public interface IFapiaoService extends IService<Fapiao> {


    PageHelpers<Invoice> list(FapiaoCondition condition);

    PageHelpers<Invoice> list2(FapiaoCondition condition);

    /**
     * 销项发票对应的排期
     */
    PageHelpers<OAScheduleDto> mediumOrderPage(Integer page, Integer size, String customer, String fapiaoid);

    PageHelpers<Invoice> list3(FapiaoCondition condition);

    boolean addFapiao(FapiaoSubmit submit, LoginCache loginCache, MultipartFile file);

    boolean updateFapiao(FapiaoSubmit submit);

    boolean updateMakeDate(FapiaoSubmit submit);

    boolean delFapiao(List<String> ids);

    PageHelper2 toBillPaymentPage(Integer userid, String s_pay_account, String s_rec_name, String s_medium, Integer page, Integer size);

    int reportBillByIds(List<String> ids);

    boolean saveBillInfo(FapiaoSubmit fapiaoSubmit, String ids, LoginCache user);

    boolean issuedBill(FapiaoSubmit fapiaoSubmit, String ids, LoginCache user);

    boolean auditBillOrderPass(FapiaoSubmit fapiaoSubmit);

    boolean auditBillOrderPass2(FapiaoSubmit fapiaoSubmit, MultipartFile... files);

    int auditBillOrderNopass(List<String> ids, String audit_desc);

    int auditBillOrderNopass2(List<String> ids, String audit_desc);

    boolean saveBillInfo2(FapiaoSubmit fapiaoSubmit, String ids, LoginCache user, MultipartFile... files);

    boolean auditBillPass(FapiaoSubmit fapiaoSubmit);

    int auditBillNopass(List<String> ids, String audit_desc);

//    int noBillPayment(List<String> ids);

    List<Fapiao_Excel> excelDrive(FapiaoCondition fapiaoCondition);

    List<Fapiao_Excel2> excelDrive2(FapiaoCondition fapiaoCondition);

    /**
     * API: 发票id -> id, billNo, enterName
     */
    Map<Integer, FapiaoSimpleInfo> queryCodeCompanyById(Collection<Integer> ids);

    /**
     * 获取客户历史开票信息
     *
     * @param
     * @return
     */
    Map<String, Object> getBillInfoByCustomer(String type, String customer);


    /**
     * 通过支付记录自动生成进项票
     */
    Integer autoGenerationBill(BigDecimal total_money, Integer fapiaoType, BigDecimal fapiaoRates, Integer medium, String mediumName);
}
