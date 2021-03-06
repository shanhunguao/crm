package com.ykhd.office.util.menu;

/**
 * 权限菜单
 */
public final class MenuAuthority {

    private final static String GET = "GET", POST = "POST", PUT = "PUT", DELETE = "DELETE";

    /**
     * 一级菜单
     */
    public enum MenuFirst {
        a("系统管理", "xtgl", "setting"), b("公众号", "gzh", "wechat"), c("客户管理", "khgl", "team"), d("发票管理", "fpgl", "transaction"),
        e("人事管理", "rsgl", "solution"), f("赛蜂电商", "sf", "shop"), g("任务管理", "rwgl", "file-text"), h("集采", "jc", "shopping-cart");

        private String title;
        private String sign; // web前端交互标记
        private String icon; // 菜单图标

        private MenuFirst(String title, String sign, String icon) {
            this.title = title;
            this.sign = sign;
            this.icon = icon;
        }

        public String getTitle() {
            return title;
        }

        public String getSign() {
            return sign;
        }

        public String getIcon() {
            return icon;
        }
    }

    /**
     * 二级菜单
     */
    public enum MenuSecond {
        aa("角色管理", "/role", GET), ab("系统日志", "/system/log", GET), ac("系统设置", "/system", GET),

        ba("公众号列表", "/oa", GET), bb("公众号排期", "/oasche", GET), bc("申请修改的排期", "/editRecord/oasche", GET),
        bd("申请作废的排期", "/oasche/4cancel", GET), be("排期付款申请", "/paymentApp", GET), bf("排期回款申请", "/oas_return", GET),

        ca("工作台", "/customer/workbenchInfo", GET), cb("客户信息", "/customer/list", GET), cc("公海客户信息", "/customer/list2", GET), //cd("共享客户信息", "/customer/sharelist", GET),

        da("销项发票审核", "/fapiao/toAuditBillPage2", GET), db("收回进项发票", "/fapaio/toBillPaymentPage", GET),
        dc("进项发票审核", "/fapiao/toAuditBillPage", GET), dd("发票查询", "/fapiao/list", GET),

        ea("部门与员工", "/manager", GET), eb("组织架构", "/manager/organization", GET), ec("薪酬", "/manager/salary", GET),
        ed("绩效", "/assess/list", GET), ef("绩效模板", "/assessTemplate/list", GET), 

        fa("品牌方信息", "/supplier/list", GET),fb("商品信息", "/commodity/list", GET),fc("渠道销售列表","/sfmedium/list",GET),fd("渠道排期列表","/sforder/list",GET),
        
        ga("工作任务", "/workTask", GET), gb("询号信息", "/xunhao", GET),
    	
    	ha("待确认排期", "/oasche/4jc", GET), hb("协议号排期", "/jc_oasche", GET), hc("排期付款申请", "/jc_paymentApp", GET), hd("排期回款申请", "/jc_oas_return", GET),
    	he("销项发票审核", "/jc_fapiao/toAuditBillPage2", GET), hf("收回进项发票", "/jc_fapaio/toBillPaymentPage", GET),
        hg("进项发票审核", "/jc_fapiao/toAuditBillPage", GET), hh("发票查询", "/jc_fapiao/list", GET),
    	;

        private String title;
        private String uri;
        private String method;

        private MenuSecond(String title, String uri, String method) {
            this.title = title;
            this.uri = uri;
            this.method = method;
        }

        public String getTitle() {
            return title;
        }

        public String getUri() {
            return uri;
        }

        public String getMethod() {
            return method;
        }
    }

    /**
     * 三级菜单（操作菜单）
     */
    public enum MenuThird {
        aaa("添加角色", "/role", POST, "role-add"), aab("修改角色", "/role", PUT, "role-update"), aac("删除角色", "/role", DELETE, "role-delete"),
        aad("查询角色权限设置", "/role/auth/", GET, "get-auth"), aae("设置角色权限", "/role/auth/", PUT, "set-auth"),

        baa("添加公众号", "/oa", POST, "oa-add"), bab("修改公众号", "/oa/info/", PUT, "oa-update"), bac("提交为启用", "/oa/submitUsing/", PUT, "oa-submitUsing"),
        bad("待开发转化为启用", "/oa/changeUsing/", PUT, "oa-changeUsing"), bae("申请作废", "/oa/appCancel/", PUT, "oa-appCancel"),
        baf("同意或驳回作废申请", "/oa/dealCancel/", PUT, "oa-dealCancel"), bag("直接作废", "/oa/cancel/", PUT, "oa-cancel"),
        bah("添加跟进记录", "/oa/followUp", POST, "oa-followUp-add"), bai("查询跟进记录", "/oa/followUp", GET, "oa-followUp"),
        baj("收藏公众号", "/oa/collection/", PUT, "oa-collection"), bak("上传数据截图", "/upload/oa/dataImage", POST, "oa-dataImage"),
        bal("修改刷号信息", "/oa/brush/", PUT, "oa-brush"), bam("评分", "oa/score/", PUT, "oa-score"),
        ban("号主收款账户", "/ba/oa", GET, "ba-oa"),
        baq("申请成为优势号", "/oa/advantageApp/", PUT, "oa-advantageApp"), bar("审核优势号申请", "/oa/advantageReview/", PUT, "oa-advantageReview"),
        bas("申请取消优势号", "/oa/advantageCannel/", PUT, "oa-advantageCannel"), bat("审核优势号取消", "/oa/advantageReview2/", PUT, "oa-advantageReview2"),
        bau("修改合作模式", "/oa/coopMode/", PUT, "oa-coopMode"), bav("修改适合客户类型", "/oa/custType/", PUT, "oa-custType"),
        bao("升级协议号", "/oa/agreement/", PUT, "oa-agreement"), bap("修改采购价", "/oa/collectPrice/", PUT, "oa-collectPrice"),
        baw("发起询号", "/xunhao", POST, "xunhao-add"),

        bba("新建排期", "/oasche", POST, "oasche-add"), bbb("提交排期", "/oasche/submit/", PUT, "oasche-submit"), bbc("审核排期", "/oasche/review/", PUT, "oasche-review"),
        bbd("确认排期", "/oasche/confirm/", PUT, "oasche-confirm"), bbe("申请作废", "/oasche/appCancel/", PUT, "oasche-appCancel"),
        bbf("直接修改排期", "/oasche/info/", PUT, "oasche-update"), bbg("申请修改排期", "/editRecord/oasche", POST, "editRecord-add"),
        bbh("删除排期", "/oasche/", DELETE, "oasche-delete"), bbi("导出excel", "/oasche/export", GET, "oasche-export"),
        bbj("发起回款申请", "/oas_return", POST, "oas_return-add"), bbk("发起支付申请", "/paymentApp", POST, "paymentApp-add"),
        bbl("集采确认排期", "/oasche/confirm2/", PUT, "oasche-confirm2"),

        bca("删除修改申请", "/editRecord/", DELETE, "editRecord-delete"), bcb("审核修改申请的排期", "/oasche/review/", PUT, "editRecord-oasche-review"),

        bda("审核作废", "/oasche/reviewCancel/", PUT, "oasche-reviewCancel"), bdb("确认作废", "/oasche/confirmCancel/", PUT, "oasche-confirmCancel"),

        bea("驳回申请", "/paymentApp/refuse/", PUT, "paymentApp-refuse"), beb("支付", "/paymentApp/pay/", PUT, "paymentApp-pay"),
        bec("复核支付", "/paymentApp/check/", PUT, "paymentApp-check"), bed("驳回支付", "/paymentApp/checkFailure/", PUT, "paymentApp-checkFailure"),
        bee("导出excel", "/paymentApp/export", GET, "paymentApp-export"), //bef("修改支付信息", "/paymentApp/payInfo/", PUT, "paymentApp-payInfo"),
        beg("删除支付", "/paymentApp/", DELETE, "paymentApp-delete"), beh("批量复核通过", "/paymentApp/checkBatch/", PUT, "paymentApp-checkBatch"),

        bfa("确认回款", "/oas_return/confirm/", PUT, "oas_return-confirm"), bfb("驳回申请", "/oas_return/refuse/", PUT, "oas_return-refuse"),
        bfc("复核回款", "/oas_return/check/", PUT, "oas_return-check"), bfd("驳回回款", "/oas_return/checkFailure/", PUT, "oas_return-checkFailure"),
        bfg("批量复核通过", "/oas_return/checkBatch/", PUT, "oas_return-checkBatch"), bfe("导出excel", "/oas_return/export", GET, "oas_return-export"),
        bff("开具销项发票", "/fapiao/saveBillInfo", POST, "fapiao-saveBillInfo"),bfh("代开销项发票", "/fapiao/issuedBill", POST, "fapiao-issuedBill"),

        cba("添加客户", "/customer/add", POST, "customer-add"), cbb("删除客户", "/customer/delete", DELETE, "customer-delete"),
        cbc("修改客户", "/customer/update", PUT, "customer-update"), cbd("退回公海", "/customer/delCustomer", PUT, "customer-delCustomer"),
        cbe("新增拜访记录", "/records/add", POST, "records-add"), cbf("客户付款账户", "/ba/cust", GET, "ba-cust"),
        //cbg("共享客户", "/customer/share", POST,"customer-share"),

        cca("领取客户", "/customer/getCustomer", PUT, "customer-getCustomer"),
        //cda("取消共享客户", "/customer/noshare", DELETE, "customer-noshare"),

        daa("销项发票报送审核", "/fapiao/reportBillByIds", PUT, "fapiao-reportBillByIds"), dab("主管销项发票审核通过", "/fapiao/auditBillOrderPass", PUT, "fapiao-auditBillOrderPass"),
        dac("财务销项发票审核通过", "/fapiao/auditBillOrderPass2", PUT, "fapiao-auditBillOrderPass2"),
        dad("主管销项发票审核不通过", "/fapiao/auditBillOrderNopass", PUT, "fapiao-auditBillOrderNopass"),
        dae("财务销项发票审核不通过", "/fapiao/auditBillOrderNopass2", PUT, "fapiao-auditBillOrderNopass2"), daf("删除销项发票", "/fapiao/delete", DELETE, "fapiao-delete"),
        dag("修改销项发票开票时间", "/fapiao/updateMakeDate", PUT, "fapiao-updateMakeDate"),

        dba("收回发票", "/fapiao/saveBillInfo2", POST, "fapiao-saveBillInfo2"),

        dca("补充发票", "/fapiao/add", POST, "fapiao-add"), dcb("进项发票审核通过", "/fapiao/auditBillPass", PUT, "fapiao-auditBillPass"),
        dcc("进项发票审核不通过", "/fapiao/auditBillNopass", PUT, "fapiao-auditBillNopass"),
        dcd("数据校正", "/fapiao/update", PUT, "fapiao-update"),

        dda("导出进项发票", "/fapiao/export", GET, "fapiao-export"), ddb("导出销项发票", "/fapiao/export2", GET, "fapiao-export2"),

        eaa("添加部门", "/dept", POST, "dep-add"), eab("修改部门", "/dept/", PUT, "dep-update"), eac("删除部门", "/dept/", DELETE, "dep-delete"),
        ead("添加员工", "/manager", POST, "mannger-add"), eae("修改员工", "/manager/info/", PUT, "mannger-update"), eaf("删除员工", "/manager/", DELETE, "mannger-delete"),
        eag("启用或禁用", "/manager/status/", PUT, "mannger-status"), eah("重置密码", "/manager/resetPwd/", PUT, "mannger-resetPwd"),
        eai("员工详情", "/manager/", GET, "mannger-detail"), eaj("修改员工人事档案", "/manager/archive/", PUT, "mannger-archive"),


        eda("发起绩效考核", "/assess/initiate", POST, "assess-initiate"),
        edb("补充绩效", "/assess/add", POST, "assess-add"),
        efa("查看模板", "/assessTemplate/getTemplate", GET, "assessTemplate-getTemplate"),
        efb("编辑模板", "/assessTemplate/toAuditTemplate", POST, "assessTemplate-toAuditTemplate"),
        efc("删除模板", "/assessTemplate/delete", DELETE, "assessTemplate-delete"),


        faa("添加商户账号","/supplier/addSupplier",POST,"supplier-addSupplier"),
        fab("删除商户账号","/supplier/delSupplier",DELETE,"supplier-delSupplier"),
        fac("修改商户账号","/supplier/updateSupplier",PUT,"supplier-updateSupplier"),
        fad("重置商户账号","/supplier/resetPwd",PUT,"supplier-resetPwd"),

        fba("商品买手审核通过","/commodity/CommodityBuyerPass",PUT,"commodity-CommodityBuyerPass"),
        fbb("商品主管审核通过","/commodity/CommodityGovernorPass",PUT,"commodity-CommodityGovernorPass"),
        fbc("商品财务审核通过","/commodity/CommodityFinancePass",PUT,"commodity-CommodityFinancePass"),
        fbd("商品审核不通过","/commodity/CommodityNoPass",PUT,"commodity-CommodityNoPass"),
        fbe("删除商品","/commodity/delete",PUT,"commodity-delete"),

        fca("添加销售渠道","/sfmedium/save",POST,"sfmedium-save"),
        fcb("删除销售渠道","/sfmedium/delete",DELETE,"sfmedium-delete"),
        fcc("修改销售渠道","/sfmedium/update",PUT,"sfmedium-update"),
        fcd("添加销售渠道档期","/sforder/save",POST,"sforder-save"),
        fce("删除销售渠道档期","/sforder/delete",DELETE,"sforder-delete"),
        fcf("修改销售渠道档期","/sforder/update",PUT,"sforder-update"),
        fcg("销售渠道档期回款","/sforder/returnedMoney",PUT,"sforder-returnedMoney"),
        
        hca("驳回申请", "/jc_paymentApp/refuse/", PUT, "jc_paymentApp-refuse"), hcb("支付", "/jc_paymentApp/pay/", PUT, "jc_paymentApp-pay"),
        hcc("复核支付", "/jc_paymentApp/check/", PUT, "jc_paymentApp-check"), hcd("驳回支付", "/jc_paymentApp/checkFailure/", PUT, "jc_paymentApp-checkFailure"),
        hce("导出excel", "/jc_paymentApp/export", GET, "jc_paymentApp-export"),
        hcg("删除支付", "/jc_paymentApp/", DELETE, "jc_paymentApp-delete"), hch("批量复核通过", "/jc_paymentApp/checkBatch/", PUT, "jc_paymentApp-checkBatch"),

        hda("确认回款", "/jc_oas_return/confirm/", PUT, "jc_oas_return-confirm"), hdb("驳回申请", "/jc_oas_return/refuse/", PUT, "jc_oas_return-refuse"),
        hdc("复核回款", "/jc_oas_return/check/", PUT, "jc_oas_return-check"), hdd("驳回回款", "/jc_oas_return/checkFailure/", PUT, "jc_oas_return-checkFailure"),
        hde("批量复核通过", "/jc_oas_return/checkBatch/", PUT, "jc_oas_return-checkBatch"), hdf("导出excel", "/jc_oas_return/export", GET, "jc_oas_return-export"),
        hdg("开具销项发票", "/jc_fapiao/saveBillInfo", POST, "jc_fapiao-saveBillInfo"),

        hea("财务销项发票审核通过", "/jc_fapiao/auditBillOrderPass2", PUT, "jc_fapiao-auditBillOrderPass2"),
        heb("财务销项发票审核不通过", "/jc_fapiao/auditBillOrderNopass2", PUT, "jc_fapiao-auditBillOrderNopass2"),
        hec("删除销项发票", "/jc_fapiao/delete", DELETE, "jc_fapiao-delete"),
        hed("修改销项发票开票时间", "/jc_fapiao/updateMakeDate", PUT, "jc_fapiao-updateMakeDate"),

        hfa("收回发票", "/jc_fapiao/saveBillInfo2", POST, "jc_fapiao-saveBillInfo2"),

        hga("补充发票", "/jc_fapiao/add", POST, "jc_fapiao-add"),
        hgb("进项发票审核通过", "/jc_fapiao/auditBillPass", PUT, "jc_fapiao-auditBillPass"),
        hgc("进项发票审核不通过", "/jc_fapiao/auditBillNopass", PUT, "jc_fapiao-auditBillNopass"),
        hgd("数据校正", "/jc_fapiao/update", PUT, "jc_fapiao-update"),

        hha("导出进项发票", "/jc_fapiao/export", GET, "jc_fapiao-export"),
        hhb("导出销项发票", "/jc_fapiao/export2", GET, "jc_fapiao-export2")
        ;

        private String title;
        private String uri;
        private String method;
        private String sign;

        private MenuThird(String title, String uri, String method, String sign) {
            this.title = title;
            this.uri = uri;
            this.method = method;
            this.sign = sign;
        }

        public String getTitle() {
            return title;
        }

        public String getUri() {
            return uri;
        }

        public String getMethod() {
            return method;
        }

        public String getSign() {
            return sign;
        }
    }
}
