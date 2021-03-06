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
import com.ykhd.office.domain.entity.JcFapiao;
import com.ykhd.office.domain.req.JcFapiaoCondition;
import com.ykhd.office.domain.req.JcFapiaoSubmit;
import com.ykhd.office.domain.resp.Fapiao_Excel;
import com.ykhd.office.domain.resp.Fapiao_Excel2;
import com.ykhd.office.domain.resp.Invoice;

public interface IJcFapiaoService extends IService<JcFapiao> {


    PageHelpers<Invoice> list(JcFapiaoCondition condition);

    PageHelpers<Invoice> list2(JcFapiaoCondition condition);

    PageHelpers<Invoice> list3(JcFapiaoCondition condition);

    boolean updateMakeDate(JcFapiaoSubmit submit);

    PageHelper2 toBillPaymentPage(Integer userid, String s_pay_account, String s_rec_name, String s_medium, Integer page, Integer size);

    boolean saveBillInfo(JcFapiaoSubmit JcFapiaoSubmit, String ids, LoginCache user);

    int auditBillOrderNopass(List<String> ids, String audit_desc);

    boolean auditBillPass(JcFapiaoSubmit JcFapiaoSubmit);

    int auditBillNopass(List<String> ids, String audit_desc);


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


    boolean addJcFapiao(JcFapiaoSubmit submit, LoginCache managerInfo, MultipartFile file);

    boolean updateJcFapiao(JcFapiaoSubmit submit);

    boolean delJcFapiao(List<String> strings);

    boolean auditBillOrderPass2(JcFapiaoSubmit jcFapiaoSubmit, MultipartFile[] files);

    boolean saveBillInfo2(JcFapiaoSubmit jcFapiaoSubmit, String ids, LoginCache managerInfo, MultipartFile[] files);

    Integer autoGenerationBill(BigDecimal total_money, Integer fapiaoType, BigDecimal fapiaoRates, Integer creator, String creatorName);

    List<Fapiao_Excel> excelDrive(JcFapiaoCondition fapiaoCondition);

    List<Fapiao_Excel2> excelDrive2(JcFapiaoCondition fapiaoCondition);
}
