package com.ykhd.office.controller;

import cn.gjing.tools.excel.driven.ExcelWrite;
import com.ykhd.office.domain.req.JcFapiaoCondition;
import com.ykhd.office.domain.req.JcFapiaoSubmit;
import com.ykhd.office.domain.resp.Fapiao_Excel;
import com.ykhd.office.domain.resp.Fapiao_Excel2;
import com.ykhd.office.service.IJcFapiaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/jc_fapiao")
public class JcFapiaoController extends BaseController {

    @Autowired
    private IJcFapiaoService JcFapiaoService;

    /**
     * 查询进项发票列表
     */
    @GetMapping("/list")
    public Object list(JcFapiaoCondition JcFapiaoCondition) {
        return JcFapiaoService.list(JcFapiaoCondition);
    }

    /**
     * 查询销项发票列表
     */
    @GetMapping("/list2")
    public Object list2(JcFapiaoCondition JcFapiaoCondition) {
        return JcFapiaoService.list2(JcFapiaoCondition);
    }


    /**
     * 查询进项发票带审核列表
     */
    @GetMapping("/toAuditBillPage")
    public Object toAuditBillPage(JcFapiaoCondition JcFapiaoCondition) {
        JcFapiaoCondition.setAuditUserid(getManagerInfo().getId());
        return JcFapiaoService.list(JcFapiaoCondition);
    }


    /**
     * 查询销项发票待审核列表
     */
    @GetMapping("/toAuditBillPage2")
    public Object toAuditBillPage2(JcFapiaoCondition JcFapiaoCondition) {
        return JcFapiaoService.list3(JcFapiaoCondition);
    }


    /**
     * 新增(补充)发票
     */
    @PostMapping("/add")
    public Object add(JcFapiaoSubmit submit, MultipartFile file) {
        return JcFapiaoService.addJcFapiao(submit, getManagerInfo(), file) ? success("添加发票成功") : failure("添加发票失败");
    }


    /**
     * 数据校正 更正认证时间
     */
    @PutMapping("/update")
    public Object update(JcFapiaoSubmit submit) {
        return JcFapiaoService.updateJcFapiao(submit) ? success("修改发票成功") : failure("修改发票失败");
    }

    /**
     * 销项发票修改开票时间
     */
    @PutMapping("/updateMakeDate")
    public Object updateMakeDate(JcFapiaoSubmit submit) {
        return JcFapiaoService.updateMakeDate(submit) ? success("修改开票时间成功") : failure("修改开票时间失败");
    }

    /**
     * 批量删除发票
     */
    @DeleteMapping("/delete")
    public Object delete(String ids) {
        return JcFapiaoService.delJcFapiao(Arrays.asList(ids.split(","))) ? success("删除发票成功") : failure("删除发票失败");
    }

    /**
     * 待收款发票的付款记录
     */
    @GetMapping("toBillPaymentPage")
    public Object toBillPaymentPage(@RequestParam(required = false) String s_pay_account,
                                    @RequestParam(required = false) String s_rec_name,
                                    @RequestParam(required = false) String s_medium, Integer page, Integer size) {
        return JcFapiaoService.toBillPaymentPage(getManagerInfo().getId(), s_pay_account, s_rec_name, s_medium, page, size);
    }

    /**
     * 添加销项发票（销售、销售主管）
     */
    @PostMapping("/saveBillInfo")
    public Object saveBillInfo(@Valid JcFapiaoSubmit JcFapiaoSubmit, String ids) {
        return JcFapiaoService.saveBillInfo(JcFapiaoSubmit, ids, getManagerInfo()) ? success("开具销项发票成功") : failure("开具销项发票失败");
    }


    /**
     * 销项发票审核通过(财务)
     */
    @PutMapping("/auditBillOrderPass2")
    public Object auditBillOrderPass2(JcFapiaoSubmit JcFapiaoSubmit, MultipartFile... files) {
        return JcFapiaoService.auditBillOrderPass2(JcFapiaoSubmit, files);
    }


    /**
     * 主管销项发票审核不通过
     */
    @PutMapping("/auditBillOrderNopass2")
    public Object auditBillOrderNopass2(String ids, String audit_desc) {
        return JcFapiaoService.auditBillOrderNopass(Arrays.asList(ids.split(",")), audit_desc);
    }

    /**
     * 添加进项发票
     */
    @PostMapping("/saveBillInfo2")
    public Object saveBillInfo2(JcFapiaoSubmit JcFapiaoSubmit, String ids, MultipartFile... files) {
        return JcFapiaoService.saveBillInfo2(JcFapiaoSubmit, ids, getManagerInfo(), files) ? success("开具进项发票成功") : failure("开具进项发票失败");
    }

    /**
     * 进项发票审核通过
     */
    @PutMapping("/auditBillPass")
    public Object auditBillPass(JcFapiaoSubmit JcFapiaoSubmit) {
        return JcFapiaoService.auditBillPass(JcFapiaoSubmit);
    }

    /**
     * 进项发票审核不通过
     */
    @PutMapping("/auditBillNopass")
    public Object auditBillNopass(String ids, String audit_desc) {
        return JcFapiaoService.auditBillNopass(Arrays.asList(ids.split(",")), audit_desc);
    }

    /**
     * 获取客户历史开票信息
     *
     * @param customer
     * @return
     */
    @GetMapping(value = "getBillInfoByCustomer")
    public Map<String, Object> getBillInfoByCustomer(String type, String customer) {
        return JcFapiaoService.getBillInfoByCustomer(type, customer);
    }


    /**
     * 导出进项发票
     */
    @GetMapping("/export")
    @ExcelWrite(mapping = Fapiao_Excel.class)
    public List<Fapiao_Excel> excelDrive(JcFapiaoCondition fapiaoCondition) {
        return JcFapiaoService.excelDrive(fapiaoCondition);
    }

    /**
     * 导出销项发票
     */
    @GetMapping("/export2")
    @ExcelWrite(mapping = Fapiao_Excel2.class)
    public List<Fapiao_Excel2> excelDrive2(JcFapiaoCondition fapiaoCondition) {
        return JcFapiaoService.excelDrive2(fapiaoCondition);
    }



}
