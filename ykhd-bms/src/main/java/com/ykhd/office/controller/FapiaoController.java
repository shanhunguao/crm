package com.ykhd.office.controller;

import cn.gjing.tools.excel.driven.ExcelWrite;
import com.ykhd.office.domain.req.FapiaoCondition;
import com.ykhd.office.domain.req.FapiaoSubmit;
import com.ykhd.office.domain.resp.Fapiao_Excel;
import com.ykhd.office.domain.resp.Fapiao_Excel2;
import com.ykhd.office.service.IFapiaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/fapiao")
public class FapiaoController extends BaseController {

    @Autowired
    private IFapiaoService fapiaoService;

    /**
     * 查询进项发票列表
     */
    @GetMapping("/list")
    public Object list(FapiaoCondition fapiaoCondition) {
        return fapiaoService.list(fapiaoCondition);
    }

    /**
     * 查询销项发票列表
     */
    @GetMapping("/list2")
    public Object list2(FapiaoCondition fapiaoCondition) {
        return fapiaoService.list2(fapiaoCondition);
    }


    /**
     * 查询销项发票对应的排期信息
     */
    @GetMapping("/mediumOrderPage")
    public Object mediumOrderPage(Integer page, Integer size, String customer, String fapiaoid) {
        return fapiaoService.mediumOrderPage(page, size, customer, fapiaoid);
    }

    /**
     * 获取客户历史开票信息
     *
     * @param customer
     * @return
     */
    @GetMapping(value = "getBillInfoByCustomer")
    public Map<String, Object> getBillInfoByCustomer(String type, String customer) {
        return fapiaoService.getBillInfoByCustomer(type, customer);
    }

    /**
     * 查询进项发票带审核列表
     */
    @GetMapping("/toAuditBillPage")
    public Object toAuditBillPage(FapiaoCondition fapiaoCondition) {
        fapiaoCondition.setAuditUserid(getManagerInfo().getId());
        return fapiaoService.list(fapiaoCondition);
    }


    /**
     * 查询销项发票待审核列表
     */
    @GetMapping("/toAuditBillPage2")
    public Object toAuditBillPage2(FapiaoCondition fapiaoCondition) {
        return fapiaoService.list3(fapiaoCondition);
    }


    /**
     * 新增(补充)发票
     */
    @PostMapping("/add")
    public Object add(FapiaoSubmit submit, MultipartFile file) {
        return fapiaoService.addFapiao(submit, getManagerInfo(), file) ? success("添加发票成功") : failure("添加发票失败");
    }


    /**
     * 数据校正 更正认证时间
     */
    @PutMapping("/update")
    public Object update(FapiaoSubmit submit) {
        return fapiaoService.updateFapiao(submit) ? success("修改发票成功") : failure("修改发票失败");
    }

    /**
     * 销项发票修改开票时间
     */
    @PutMapping("/updateMakeDate")
    public Object updateMakeDate(FapiaoSubmit submit) {
        return fapiaoService.updateMakeDate(submit) ? success("修改开票时间成功") : failure("修改开票时间失败");
    }

    /**
     * 批量删除发票
     */
    @DeleteMapping("/delete")
    public Object delete(String ids) {
        return fapiaoService.delFapiao(Arrays.asList(ids.split(","))) ? success("删除发票成功") : failure("删除发票失败");
    }


    /**
     * 待收款发票的付款记录
     */
    @GetMapping("toBillPaymentPage")
    public Object toBillPaymentPage(@RequestParam(required = false) String s_pay_account,
                                    @RequestParam(required = false) String s_rec_name,
                                    @RequestParam(required = false) String s_medium, Integer page, Integer size) {
        return fapiaoService.toBillPaymentPage(getManagerInfo().getId(), s_pay_account, s_rec_name, s_medium, page, size);
    }

//    /**
//     * 标记为不开票
//     */
//    @PutMapping("noBillPayment")
//    public Object noBillPayment(String ids) {
//        return fapiaoService.noBillPayment(Arrays.asList(ids.split(",")));
//    }

    /**
     * 添加销项发票（销售、销售主管）
     */
    @PostMapping("/saveBillInfo")
    public Object saveBillInfo(@Valid FapiaoSubmit fapiaoSubmit, String ids) {
        return fapiaoService.saveBillInfo(fapiaoSubmit, ids, getManagerInfo()) ? success("开具销项发票成功") : failure("开具销项发票失败");
    }

    /*
    * 代开发票(主管)
    * */
    @PostMapping("/issuedBill")
    public Object issuedBill(@Valid FapiaoSubmit fapiaoSubmit, String ids) {
        return fapiaoService.issuedBill(fapiaoSubmit, ids, getManagerInfo()) ? success("开具销项发票成功") : failure("开具销项发票失败");
    }

    /**
     * 报送销项发票（销售）
     */
    @PutMapping("/reportBillByIds")
    public Object reportBillByIds(String ids) {
        return fapiaoService.reportBillByIds(Arrays.asList(ids.split(",")));
    }

    /**
     * 销项发票审核通过(销售主管)
     */
    @PutMapping("/auditBillOrderPass")
    public Object auditBillOrderPass(FapiaoSubmit fapiaoSubmit) {
        return fapiaoService.auditBillOrderPass(fapiaoSubmit);
    }


    /**
     * 销项发票审核通过(财务)
     */
    @PutMapping("/auditBillOrderPass2")
    public Object auditBillOrderPass2(FapiaoSubmit fapiaoSubmit, MultipartFile... files) {
        return fapiaoService.auditBillOrderPass2(fapiaoSubmit, files);
    }


    /**
     * 主管销项发票审核不通过
     */
    @PutMapping("/auditBillOrderNopass")
    public Object auditBillOrderNopass(String ids, String audit_desc) {
        return fapiaoService.auditBillOrderNopass(Arrays.asList(ids.split(",")), audit_desc);
    }


    /**
     * 主管销项发票审核不通过
     */
    @PutMapping("/auditBillOrderNopass2")
    public Object auditBillOrderNopass2(String ids, String audit_desc) {
        return fapiaoService.auditBillOrderNopass2(Arrays.asList(ids.split(",")), audit_desc);
    }


    /**
     * 添加进项发票
     */
    @PostMapping("/saveBillInfo2")
    public Object saveBillInfo2(FapiaoSubmit fapiaoSubmit, String ids, MultipartFile... files) {
        return fapiaoService.saveBillInfo2(fapiaoSubmit, ids, getManagerInfo(), files) ? success("开具进项发票成功") : failure("开具进项发票失败");
    }

    /**
     * 进项发票审核通过
     */
    @PutMapping("/auditBillPass")
    public Object auditBillPass(FapiaoSubmit fapiaoSubmit) {
        return fapiaoService.auditBillPass(fapiaoSubmit);
    }

    /**
     * 进项发票审核不通过
     */
    @PutMapping("/auditBillNopass")
    public Object auditBillNopass(String ids, String audit_desc) {
        return fapiaoService.auditBillNopass(Arrays.asList(ids.split(",")), audit_desc);
    }


    /**
     * 导出进项发票
     */
    @GetMapping("/export")
    @ExcelWrite(mapping = Fapiao_Excel.class)
    public List<Fapiao_Excel> excelDrive(FapiaoCondition fapiaoCondition) {
        return fapiaoService.excelDrive(fapiaoCondition);
    }

    /**
     * 导出销项发票
     */
    @GetMapping("/export2")
    @ExcelWrite(mapping = Fapiao_Excel2.class)
    public List<Fapiao_Excel2> excelDrive2(FapiaoCondition fapiaoCondition) {
        return fapiaoService.excelDrive2(fapiaoCondition);
    }

//    @PostMapping("/test")
//    public Object test(BigDecimal total_money, Integer fapiaoType, BigDecimal fapiaoRates, Integer medium, String mediumName) {
//
//        return success(fapiaoService.autoGenerationBill(total_money,fapiaoType,fapiaoRates,medium,mediumName));
//    }


}
