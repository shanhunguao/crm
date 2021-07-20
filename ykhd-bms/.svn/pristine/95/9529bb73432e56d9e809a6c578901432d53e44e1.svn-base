package com.ykhd.office.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ykhd.office.util.dictionary.TypeEnums;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ykhd.office.component.aliyun.oss.OssService;
import com.ykhd.office.controller.BaseController;
import com.ykhd.office.domain.bean.FapiaoSimpleInfo;
import com.ykhd.office.domain.bean.LoginCache;
import com.ykhd.office.domain.bean.common.PageHelper2;
import com.ykhd.office.domain.bean.common.PageHelpers;
import com.ykhd.office.domain.entity.JcFapiao;
import com.ykhd.office.domain.entity.JcPaymentApp;
import com.ykhd.office.domain.req.JcFapiaoCondition;
import com.ykhd.office.domain.req.JcFapiaoSubmit;
import com.ykhd.office.domain.resp.Fapiao_Excel;
import com.ykhd.office.domain.resp.Fapiao_Excel2;
import com.ykhd.office.domain.resp.Invoice;
import com.ykhd.office.domain.resp.InvoiceRelevant2;
import com.ykhd.office.repository.JcFapiaoMapper;
import com.ykhd.office.service.IJcFapiaoService;
import com.ykhd.office.service.IJcPaymentAppService;
import com.ykhd.office.util.JacksonHelper;
import com.ykhd.office.util.dictionary.StateEnums;
import com.ykhd.office.util.dictionary.SystemEnums;

@Service
public class JcFapiaoService extends ServiceImpl<BaseMapper<JcFapiao>, JcFapiao> implements IJcFapiaoService {

    @Autowired
    private OssService ossService;

    @Autowired
    private JcFapiaoMapper JcFapiaoMapper;

    @Autowired
    private IJcPaymentAppService paymentAppService;

    @Autowired
    private ApprovalMsgService approvalMsgService;

    @Override
    public PageHelpers<Invoice> list(JcFapiaoCondition condition) {
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("is_add", 1);
        conditions.put("audit_userid", condition.getAuditUserid());
        conditions.put("bill_no", condition.getBillNo());
        conditions.put("enter_name", condition.getEnterName());
        conditions.put("type", condition.getType());
        conditions.put("society", condition.getSociety());
        conditions.put("source", condition.getSource());
        conditions.put("create_userid", condition.getCreateUserid());
        conditions.put("order_xs", condition.getOrder_xs());
        settime(condition, conditions);
        setstatus(condition, conditions);
        Map<String, Object> map = JcFapiaoMapper.statsData(conditions);
//        setRatesMoney(condition, conditions, map);
        return new PageHelpers<>(condition.getPage(), condition.getSize(), JcFapiaoMapper.count(conditions), JcFapiaoMapper.queryPage(new Page<>(condition.getPage(), condition.getSize()), conditions), map);
    }


    @Override
    public PageHelpers<Invoice> list2(JcFapiaoCondition condition) {
        Map<String, Object> conditions = new HashMap<String, Object>();
        conditions.put("is_add", 0);
        conditions.put("bill_no", condition.getBillNo());
        conditions.put("enter_name", condition.getEnterName());
        conditions.put("type", condition.getType());
        conditions.put("make_name", condition.getMakeName());
//        conditions.put("create_userid", BaseController.getManagerInfo().getId());
        settime(condition, conditions);
        conditions.put("state", new String[]{String.valueOf(StateEnums.State4Fapiao.审核通过.code())});
        return new PageHelpers<>(condition.getPage(), condition.getSize(), JcFapiaoMapper.count(conditions), JcFapiaoMapper.queryPage(new Page<>(condition.getPage(), condition.getSize()), conditions), JcFapiaoMapper.statsData(conditions));
    }


    @Override
    public PageHelpers<Invoice> list3(JcFapiaoCondition condition) {
        Map<String, Object> conditions = new HashMap<String, Object>();
        conditions.put("audit_userid", BaseController.getManagerInfo().getId());
        conditions.put("is_add", 0);
        conditions.put("bill_no", condition.getBillNo());
        conditions.put("enter_name", condition.getEnterName());
        conditions.put("type", condition.getType());
        settime(condition, conditions);
        setstatus2(condition, conditions);
        return new PageHelpers<>(condition.getPage(), condition.getSize(), JcFapiaoMapper.count(conditions), JcFapiaoMapper.queryPage(new Page<>(condition.getPage(), condition.getSize()), conditions), JcFapiaoMapper.statsData(conditions));
    }

    /**
     * 销项发票状态
     */
    private void setstatus2(JcFapiaoCondition condition, Map<String, Object> conditions) {
        if ("0".equals(condition.getsType())) {//待审核
            conditions.put("state", new String[]{String.valueOf(StateEnums.State4Fapiao.创建.code()), String.valueOf(StateEnums.State4Fapiao.销项票待主管审核.code()), String.valueOf(StateEnums.State4Fapiao.审核通过_待财务审核.code())});
        } else if ("1".equals(condition.getsType())) {//审核通过
            conditions.put("state", new String[]{String.valueOf(StateEnums.State4Fapiao.审核通过.code())});
        } else if ("2".equals(condition.getsType())) {//审核驳回
            conditions.put("state", new String[]{String.valueOf(StateEnums.State4Fapiao.审核不通过.code())});
        }
    }

    /**
     * 进项发票状态
     */
    private void setstatus(JcFapiaoCondition condition, Map<String, Object> conditions) {
        if ("0".equals(condition.getsType())) {//待审核
            conditions.put("state", new String[]{String.valueOf(StateEnums.State4Fapiao.审核通过_待财务审核.code())});
        } else if ("1".equals(condition.getsType())) {//通过
            conditions.put("state", new String[]{String.valueOf(StateEnums.State4Fapiao.审核通过.code())});
        } else if ("2".equals(condition.getsType())) {//驳回
            conditions.put("state", new String[]{String.valueOf(StateEnums.State4Fapiao.审核不通过.code())});
        } else {
            //默认查询正常的发票
            conditions.put("state", new String[]{String.valueOf(StateEnums.State4Fapiao.审核通过.code())});
        }
    }

    private void settime(JcFapiaoCondition condition, Map<String, Object> conditions) {
        //日期区间
        if (StringUtils.isNotEmpty(condition.getStartdate())) {
            if (condition.getDateType().equals("2")) {//创建日期
                conditions.put("createrdate_start", condition.getStartdate());
            } else if (condition.getDateType().equals("1")) {//开票日期
                conditions.put("makedate_start", condition.getStartdate());
            } else if (condition.getDateType().equals("3")) {//认证时间
                conditions.put("verificatio_start", condition.getStartdate());
            }
        }
        if (StringUtils.isNotEmpty(condition.getEnddate())) {
            if (condition.getDateType().equals("2")) {
                conditions.put("createrdate_end", condition.getEnddate());
            } else if (condition.getDateType().equals("1")) {
                conditions.put("makedate_end", condition.getEnddate());
            } else if (condition.getDateType().equals("3")) {//认证时间
                conditions.put("verificatio_end", condition.getEnddate());
            }
        }
    }


    @Override
    public boolean addJcFapiao(JcFapiaoSubmit submit, LoginCache loginCache, MultipartFile file) {
        String uploadFile = ossService.uploadFile(SystemEnums.OSSFolder.BMS.name(), file);
        JcFapiao JcFapiao = new JcFapiao();
        BeanUtils.copyProperties(submit, JcFapiao);
        JcFapiao.setIsAdd("1");        //是否进项（0-销项，1-进项）
        JcFapiao.setIsBalance("1");    //进项发票不用结算薪资，使用付款记录进行结算
        JcFapiao.setCreateUserid(loginCache.getId());
        JcFapiao.setCreateDate(new Date());
        JcFapiao.setCreateUsername(loginCache.getName());
        JcFapiao.setAuditUserid(loginCache.getId());
        if (JcFapiao.getVerificatioTime() == null) {
            JcFapiao.setVerificatioTime(new Date());
        }
        JcFapiao.setFapiaoImg(uploadFile);
        JcFapiao.setFapiaoCode(submit.getFapiaoCode());
        JcFapiao.setState(String.valueOf(StateEnums.State4Fapiao.审核通过.code()));        //新增后直接到已完成状态
        JcFapiao.setSource(0);
        return save(JcFapiao);
    }


    @Override
    public boolean updateJcFapiao(JcFapiaoSubmit submit) {
        JcFapiao JcFapiao = new JcFapiao();
        BeanUtils.copyProperties(submit, JcFapiao);
        UpdateWrapper<JcFapiao> wrapper = new UpdateWrapper<>();
        if (JcFapiao.getMoney() != null) {
            wrapper.set("money", JcFapiao.getMoney());
        }
        if (JcFapiao.getRatesMoney() != null) {
            wrapper.set("rates_money", JcFapiao.getRatesMoney());
        }

        wrapper.set("verificatio_time", JcFapiao.getVerificatioTime());
        wrapper.eq("id", JcFapiao.getId());
        return update(wrapper);
    }

    @Override
    public boolean delJcFapiao(List<String> ids) {
        List<JcFapiao> list = JcFapiaoMapper.selectBatchIds(ids);
        for (JcFapiao fapiao : list) {
            Assert.state(fapiao.getState().equals((String.valueOf(StateEnums.State4Fapiao.创建.code()))), "所选条目非待提交状态，不可提交");
        }
        boolean b = removeByIds(ids);
//        if (b) {
//            boolean boo = oasReturnService.clearFapiao(ids.stream().map(Integer::valueOf).collect(Collectors.toList()));
//            Assert.state(boo, "清空排期销项发票失败");
//        }
        return b;
    }

    @Override
    public boolean updateMakeDate(JcFapiaoSubmit submit) {
        JcFapiao JcFapiao = new JcFapiao();
        BeanUtils.copyProperties(submit, JcFapiao);
        UpdateWrapper<JcFapiao> wrapper = new UpdateWrapper<>();
        wrapper.set("make_date", JcFapiao.getMakeDate());
        wrapper.eq("id", JcFapiao.getId());
        return update(wrapper);
    }


    @Override
    public PageHelper2 toBillPaymentPage(Integer userid, String s_pay_account, String s_rec_name, String s_medium, Integer page, Integer size) {
        Map<String, Object> conditions = new HashMap<>();
        //只统计支付完成的开票付款记录
        conditions.put("state", new String[]{String.valueOf(StateEnums.State4PaymentApp.已付款.code())});
        conditions.put("creator", userid);
        if (StringUtils.isNotEmpty(s_pay_account)) {
            conditions.put("pay_account", s_pay_account);
        }
        if (StringUtils.isNotEmpty(s_rec_name)) {
            conditions.put("s_rec_name", s_rec_name);
        }
        if (StringUtils.isNotEmpty(s_medium)) {
            conditions.put("s_medium", s_medium);
        }
        return new PageHelper2(page, size, JcFapiaoMapper.toBillPaymentPageCount(conditions), JcFapiaoMapper.toBillPaymentPage(new Page<>(page, size), conditions));
    }


    @Override
    @Transactional
    public boolean saveBillInfo(JcFapiaoSubmit JcFapiaoSubmit, String ids, LoginCache loginCache) {
        JcFapiao JcFapiao = new JcFapiao();
        BeanUtils.copyProperties(JcFapiaoSubmit, JcFapiao);
        JcFapiao.setAuditUserid(BaseController.getManagerInfo().getId());
//        添加发票信息
        BigDecimal total_money = JcFapiao.getTotalMoney().setScale(2, BigDecimal.ROUND_CEILING); //价税合计
        BigDecimal actual_money = JcFapiao.getActualMoney().setScale(2, BigDecimal.ROUND_CEILING);    //实际价税合计
        BigDecimal rates = JcFapiao.getRates().setScale(2, BigDecimal.ROUND_CEILING);                    //税率
        BigDecimal money = actual_money.divide(rates.divide(new BigDecimal(100)).add(new BigDecimal(1)), 2, BigDecimal.ROUND_DOWN);    //金额=价税合计/（1+税率）
        BigDecimal rates_money = actual_money.subtract(money);
        JcFapiao.setBillNo(null);
        JcFapiao.setMoney(money);
        JcFapiao.setRates(rates);
        JcFapiao.setRatesMoney(rates_money);
        JcFapiao.setTotalMoney(total_money);
        JcFapiao.setActualMoney(actual_money);
        JcFapiao.setIsAdd("0");
        JcFapiao.setCreateUserid(loginCache.getId());
        JcFapiao.setCreateUsername(loginCache.getName());
        JcFapiao.setCreateDate(new Date());
        JcFapiao.setState(String.valueOf(StateEnums.State4Fapiao.审核通过_待财务审核.code()));
        JcFapiao.setIsBalance("1");    //销项发票不需要结算薪资，默认已结算
        JcFapiao.setAuditUserid(42);
        if (save(JcFapiao)) {
            //修改排期发票信息
//            boolean boo = oasReturnService.setFapiao(JcFapiao.getId(), idList.stream().map(Integer::valueOf).collect(Collectors.toList()));
//            Assert.state(boo, "修改排期发票信息失败");
        }
        return true;
    }


    @Override
    public boolean auditBillOrderPass2(JcFapiaoSubmit JcFapiaoSubmit, MultipartFile[] files) {
        Assert.state(!StringUtils.isEmpty(JcFapiaoSubmit.getBillNo()), "发票号码不能为空");
        JcFapiao JcFapiao = getById(JcFapiaoSubmit.getId());
        JcFapiao.setFapiaoImg(JacksonHelper.toJsonStr(ossService.uploadFiles(SystemEnums.OSSFolder.BMS.name(), files)));
        JcFapiao.setBillNo(JcFapiaoSubmit.getBillNo());
        JcFapiao.setMakeDate(JcFapiaoSubmit.getMakeDate());
        JcFapiao.setActualMoney(JcFapiaoSubmit.getActualMoney());
        JcFapiao.setRatesMoney(JcFapiaoSubmit.getRatesMoney());
        JcFapiao.setMoney(JcFapiaoSubmit.getActualMoney().subtract(JcFapiao.getRatesMoney()));
        Assert.state(JcFapiao.getState().equals(String.valueOf(StateEnums.State4Fapiao.审核通过_待财务审核.code())), "所选条目非待财务审核状态，操作失败");
        JcFapiao.setState(String.valueOf(StateEnums.State4Fapiao.审核通过.code()));
        updateById(JcFapiao);
        return true;
    }

    @Override
    @Transactional
    public int auditBillOrderNopass(List<String> ids, String audit_desc) {
//        checkLeader();
        int success = 0;
        for (String id : ids) {
            JcFapiao JcFapiao = getById(id);
            Assert.state(audit_desc.length() < 200, "不能超过长度");
            UpdateWrapper<JcFapiao> updateWrapper = new UpdateWrapper<JcFapiao>()
                    .eq("id", id)
                    .set("audit_desc", audit_desc)
                    .set("state", StateEnums.State4Fapiao.审核不通过.code());
            success += JcFapiaoMapper.update(JcFapiao, updateWrapper);
        }
//         取消排期发票已开状态
        if (success > 0) {
//            boolean boo = oasReturnService.clearFapiao(ids.stream().map(Integer::valueOf).collect(Collectors.toList()));
//            Assert.state(boo, "清空排期销项发票失败");
        }
        return success;
    }


    @Override
    @Transactional
    public boolean saveBillInfo2(JcFapiaoSubmit JcFapiaoSubmit, String ids, LoginCache user, MultipartFile[] files) {
//        String uploadFile = ossService.uploadFile(SystemEnums.OSSFolder.BMS.name(), file);
        JcFapiao JcFapiao = new JcFapiao();
        BeanUtils.copyProperties(JcFapiaoSubmit, JcFapiao);
//        判断支付记录是否存在
        List<String> idList = Arrays.asList(ids.split(","));
        List<Integer> paymentIds = new ArrayList<>();
        BigDecimal total_money = new BigDecimal(0);        //价税合计
        for (String id : idList) {
            JcPaymentApp payment = paymentAppService.getById(id);
            Assert.state(payment != null, "付款记录不存在，操作失败");
            total_money = total_money.add(payment.getAmount());
            paymentIds.add(payment.getId());
        }
        BigDecimal rates = JcFapiao.getRates().setScale(2, BigDecimal.ROUND_CEILING);                    //税率
        BigDecimal money = total_money.divide(rates.divide(new BigDecimal(100)).add(new BigDecimal(1)), 2, BigDecimal.ROUND_DOWN);    //金额=价税合计/（1+税率）
        BigDecimal rates_money = total_money.subtract(money); //税额(税价合计-金额)
        JcFapiao.setMoney(money);
        JcFapiao.setRates(rates);
        JcFapiao.setRatesMoney(rates_money);
        JcFapiao.setTotalMoney(total_money);
        JcFapiao.setIsAdd("1");        //是否进项（0-销项，1-进项）
        JcFapiao.setIsBalance("1");    //进项发票不用结算薪资，使用付款记录进行结算
        JcFapiao.setCreateUserid(user.getId());
        JcFapiao.setCreateDate(new Date());
        JcFapiao.setCreateUsername(user.getName());
        JcFapiao.setFapiaoImg(JacksonHelper.toJsonStr(ossService.uploadFiles(SystemEnums.OSSFolder.BMS.name(), files)));
        JcFapiao.setFapiaoCode(JcFapiaoSubmit.getFapiaoCode());
        JcFapiao.setState(String.valueOf(StateEnums.State4Fapiao.审核通过_待财务审核.code()));        //新增后直接到待审核状态
        JcFapiao.setSource(1);
//        添加进项发票
        save(JcFapiao);
        //更改付款记录信息
        boolean boo = paymentAppService.setFapiao(JcFapiao.getId(), paymentIds);
        Assert.state(boo, "回填进项发票信息失败");
        approvalMsgService.sendApprovalMsg(TypeEnums.Type4ApprovalMsg.集采进项发票出纳待审核, JcFapiaoSubmit.getAuditUserid());
        return true;
    }


    @Override
    public boolean auditBillPass(JcFapiaoSubmit JcFapiaoSubmit) {
        JcFapiao JcFapiao = getById(JcFapiaoSubmit.getId());
        Assert.state(JcFapiao.getState().equals(String.valueOf(StateEnums.State4Fapiao.审核通过_待财务审核.code())), "所选条目非待财务审核状态，操作失败");
        UpdateWrapper<JcFapiao> wrapper = new UpdateWrapper<>();
        if (JcFapiaoSubmit.getVerificatioTime() == null) {
            wrapper.set("verificatio_time", new Date());
        } else {
            wrapper.set("verificatio_time", JcFapiaoSubmit.getVerificatioTime());
        }
        wrapper.eq("id", JcFapiaoSubmit.getId());
        wrapper.set("state", StateEnums.State4Fapiao.审核通过.code());
        update(wrapper);
        approvalMsgService.sendApprovalMsg(TypeEnums.Type4ApprovalMsg.集采进项发票出纳待审核, BaseController.getManagerInfo().getId());
        return true;
    }

    @Override
    @Transactional
    public int auditBillNopass(List<String> ids, String audit_desc) {
        int success = 0;
        for (String id : ids) {
            JcFapiao JcFapiao = getById(id);
            Assert.state(JcFapiao.getState().equals(String.valueOf(StateEnums.State4Fapiao.审核通过_待财务审核.code())), "所选条目非待财务审核状态，操作失败")
            ;
            Assert.state(audit_desc.length() < 200, "不能超过长度");
            UpdateWrapper<JcFapiao> updateWrapper = new UpdateWrapper<JcFapiao>()
                    .eq("id", id)
                    .set("audit_desc", audit_desc)
                    .set("state", StateEnums.State4Fapiao.审核不通过.code());
            success += JcFapiaoMapper.update(null, updateWrapper);
//         进项发票审核不通过时需要更改付款记录发票状态
            boolean boo = paymentAppService.clearFapiao(Integer.valueOf(id));
            Assert.state(boo, "删除付款记录发票信息失败");
        }
        approvalMsgService.sendApprovalMsg(TypeEnums.Type4ApprovalMsg.集采进项发票出纳待审核, BaseController.getManagerInfo().getId());
        return success;
    }


    @Override
    public Map<Integer, FapiaoSimpleInfo> queryCodeCompanyById(Collection<Integer> collection) {
        if (!collection.isEmpty()) {
            List<JcFapiao> list = list(new QueryWrapper<JcFapiao>().select("id", "bill_no", "enter_name").in("id", collection));
            if (!list.isEmpty()) {
                Map<Integer, FapiaoSimpleInfo> map = new HashMap<>(list.size());
                list.forEach(v -> {
                    FapiaoSimpleInfo info = new FapiaoSimpleInfo();
                    BeanUtils.copyProperties(v, info);
                    map.put(v.getId(), info);
                });
                return map;
            }
        }
        return Collections.emptyMap();
    }

    @Override
    public Map<String, Object> getBillInfoByCustomer(String type, String customer) {
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("create_userid", BaseController.getManagerInfo().getId());
        if (StringUtils.isNotEmpty(type)) {
            conditions.put("type", type);
        }
        if (StringUtils.isNotEmpty(customer)) {
            conditions.put("enter_name", customer);
        }
        return JcFapiaoMapper.getBillInfoByCustomer(conditions);
    }


    @Override
    public Integer autoGenerationBill(BigDecimal total_money, Integer fapiaoType, BigDecimal fapiaoRates, Integer creator, String creatorName) {
        Date date = new Date();
        JcFapiao fapiao = new JcFapiao();
        fapiao.setAuditUserid(BaseController.getManagerInfo().getId());
//        添加发票信息
        BigDecimal money = total_money.divide(fapiaoRates.divide(new BigDecimal(100)).add(new BigDecimal(1)), 2, BigDecimal.ROUND_DOWN);    //金额=价税合计/（1+税率）
        BigDecimal rates_money = total_money.subtract(money);
        fapiao.setBillNo(null);
        fapiao.setMoney(money);
        fapiao.setRates(fapiaoRates);
        fapiao.setRatesMoney(rates_money);
        fapiao.setTotalMoney(total_money);
        fapiao.setActualMoney(total_money);
        fapiao.setType(fapiaoType == 0 ? "1" : "0");
        fapiao.setIsAdd("0");
        fapiao.setCreateUserid(creator);
        fapiao.setCreateUsername(creatorName);
        fapiao.setCreateDate(date);
        fapiao.setState(String.valueOf(StateEnums.State4Fapiao.审核通过.code()));
        fapiao.setIsBalance("1");    //销项发票不需要结算薪资，默认已结算
        fapiao.setBillNo("system generate");
        fapiao.setSource(1);
        fapiao.setEnterName("/");
        fapiao.setMakeName("杭州优客互动网络科技有限公司");
        fapiao.setMakeDate(date);
        save(fapiao);
        return fapiao.getId();
    }


    @Override
    public List<Fapiao_Excel> excelDrive(JcFapiaoCondition condition) {
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("is_add", 1);
        conditions.put("create_userid", condition.getCreateUserid());
        conditions.put("audit_userid", condition.getAuditUserid());
        conditions.put("bill_no", condition.getBillNo());
        conditions.put("enter_name", condition.getEnterName());
        conditions.put("type", condition.getType());
        conditions.put("order_xs", condition.getOrder_xs());
        conditions.put("society", condition.getSociety());
        settime(condition, conditions);
        setstatus(condition, conditions);
        List<Fapiao_Excel> fapiao = JcFapiaoMapper.excelDrive(conditions);
//        处理invoiceRelevantList属性集合包含公众号，排期日期，负责销售集合数据转换成字符串
        fapiao.forEach(e -> {
            String sellName = "\n";
            String society = "\n";
            String orderTime = "\n";
            String actualTax = "\n";
            List<InvoiceRelevant2> invoiceRelevantList = e.getInvoiceRelevantList();
            for (int i = 0; i < invoiceRelevantList.size(); i++) {
                sellName += invoiceRelevantList.get(i).getSellName() + "\n";
                society += invoiceRelevantList.get(i).getSociety() + "\n";
                orderTime += invoiceRelevantList.get(i).getOrderTime() + "\n";
                Integer sellId = invoiceRelevantList.get(i).getSellId();
                if (sellId != null && sellId != 0) {
                    BigDecimal duty = JcFapiaoMapper.findDuty(e.getId(), sellId);
                    JcFapiao fapiao2 = JcFapiaoMapper.selectById(e.getId());
//                    税额
                    BigDecimal duty_money = fapiao2.getRatesMoney();
                    duty_money = duty_money.multiply(duty).setScale(2, RoundingMode.HALF_UP);
                    actualTax += duty_money + "\n";
                }

            }
            e.setSellName(sellName);
            e.setSociety(society);
            e.setOrderTime(orderTime);
            e.setActualTax(actualTax);

        });

        return fapiao;
    }


    @Override
    public List<Fapiao_Excel2> excelDrive2(JcFapiaoCondition condition) {
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("is_add", 0);
        conditions.put("bill_no", condition.getBillNo());
        conditions.put("enter_name", condition.getEnterName());
        conditions.put("make_name", condition.getMakeName());
        conditions.put("type", condition.getType());
        conditions.put("audit_userid", condition.getAuditUserid());
        conditions.put("create_userid", condition.getOrder_xs());
        settime(condition, conditions);
        conditions.put("state", new String[]{String.valueOf(StateEnums.State4Fapiao.审核通过.code())});
        return JcFapiaoMapper.excelDrive2(conditions);
    }


}
