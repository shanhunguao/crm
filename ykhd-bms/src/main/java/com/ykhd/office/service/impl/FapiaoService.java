package com.ykhd.office.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

import com.ykhd.office.domain.entity.*;
import com.ykhd.office.util.JacksonHelper;
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
import com.ykhd.office.domain.req.FapiaoCondition;
import com.ykhd.office.domain.req.FapiaoSubmit;
import com.ykhd.office.domain.resp.Fapiao_Excel;
import com.ykhd.office.domain.resp.Fapiao_Excel2;
import com.ykhd.office.domain.resp.Invoice;
import com.ykhd.office.domain.resp.InvoiceRelevant2;
import com.ykhd.office.domain.resp.OAScheduleDto;
import com.ykhd.office.repository.CustomerMapper;
import com.ykhd.office.repository.FapiaoMapper;
import com.ykhd.office.service.IDepartmentService;
import com.ykhd.office.service.IFapiaoService;
import com.ykhd.office.service.IOASReturnService;
import com.ykhd.office.service.IPaymentAppService;
import com.ykhd.office.service.IRoleService;
import com.ykhd.office.util.MathUtil;
import com.ykhd.office.util.dictionary.StateEnums;
import com.ykhd.office.util.dictionary.SystemEnums;
import com.ykhd.office.util.dictionary.SystemEnums.RoleSign;
import com.ykhd.office.util.dictionary.TypeEnums;

@Service
public class FapiaoService extends ServiceImpl<BaseMapper<Fapiao>, Fapiao> implements IFapiaoService {

    @Autowired
    private OssService ossService;

    @Autowired
    private FapiaoMapper fapiaoMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private IPaymentAppService paymentAppService;

    @Autowired
    private ApprovalMsgService approvalMsgService;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IDepartmentService departmentService;

    @Autowired
    private IOASReturnService oasReturnService;

    @Override
    public PageHelpers<Invoice> list(FapiaoCondition condition) {
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("is_add", 1);
        conditions.put("audit_userid", condition.getAuditUserid());
        conditions.put("bill_no", condition.getBillNo());
        conditions.put("enter_name", condition.getEnterName());
        conditions.put("type", condition.getType());
        conditions.put("society", condition.getSociety());
        conditions.put("source", condition.getSource());
        RoleSign sign = roleService.judgeRole(BaseController.getManagerInfo().getRole());
        if (sign == RoleSign.medium) {
            conditions.put("create_userid", BaseController.getManagerInfo().getId());
            conditions.put("order_xs", condition.getOrder_xs());
        } else if (sign == RoleSign.ae) {
            conditions.put("create_userid", condition.getCreateUserid());
            conditions.put("order_xs", BaseController.getManagerInfo().getId());
        } else if (sign == RoleSign.dept || sign == RoleSign.group) {
            conditions.put("create_userid", condition.getCreateUserid());
            if (StringUtils.isNotEmpty(condition.getOrder_xs())) {
                conditions.put("order_xs", condition.getOrder_xs());
            } else {
                conditions.put("order_xs", BaseController.getManagerInfo().getId());
            }
            conditions.put("create_userid", condition.getCreateUserid());
        } else {
            conditions.put("create_userid", condition.getCreateUserid());
            conditions.put("order_xs", condition.getOrder_xs());
        }
        settime(condition, conditions);
        setstatus(condition, conditions);
        Map<String, Object> map = fapiaoMapper.statsData(conditions);
        setRatesMoney(condition, conditions, map);
        return new PageHelpers<>(condition.getPage(), condition.getSize(), fapiaoMapper.count(conditions), fapiaoMapper.queryPage(new Page<>(condition.getPage(), condition.getSize()), conditions), map);
    }

    /**
     * ???????????????????????????
     */
    private void setRatesMoney(FapiaoCondition condition, Map<String, Object> conditions, Map<String, Object> map) {
        if (StringUtils.isNotEmpty(condition.getOrder_xs())) {
            List<Integer> fapiaoid = fapiaoMapper.findFapiaoid(conditions);
            if (fapiaoid != null && fapiaoid.size() > 0) {
                BigDecimal rates_money = BigDecimal.ZERO;
                for (Integer id : fapiaoid) {
//                    ??????????????????
                    BigDecimal duty = fapiaoMapper.findDuty(id, Integer.valueOf(condition.getOrder_xs()));
                    Fapiao fapiao = fapiaoMapper.selectById(id);
//                    ??????
                    BigDecimal duty_money = fapiao.getRatesMoney();
                    rates_money = rates_money.add(duty_money.multiply(duty).setScale(2, RoundingMode.HALF_UP));
                }
                map.put("rates_money", rates_money);
            }
        }
    }


    @Override
    public PageHelpers<Invoice> list2(FapiaoCondition condition) {
        Map<String, Object> conditions = new HashMap<String, Object>();
        conditions.put("is_add", 0);
        conditions.put("bill_no", condition.getBillNo());
        conditions.put("enter_name", condition.getEnterName());
        conditions.put("type", condition.getType());
        conditions.put("make_name", condition.getMakeName());
        RoleSign sign = roleService.judgeRole(BaseController.getManagerInfo().getRole());
        if (sign == RoleSign.general_mgr || sign == RoleSign.director || sign == RoleSign.other) {
            if (StringUtils.isNotEmpty(condition.getOrder_xs())) {
                conditions.put("create_userid", condition.getOrder_xs());
            }
        } else if (sign == RoleSign.dept) {
            if (StringUtils.isNotEmpty(condition.getOrder_xs())) {
                conditions.put("create_userid", condition.getOrder_xs());
            } else {
                conditions.put("create_userid", BaseController.getManagerInfo().getId());
            }
        } else {
            conditions.put("create_userid", BaseController.getManagerInfo().getId());
        }
        settime(condition, conditions);
        conditions.put("state", new String[]{String.valueOf(StateEnums.State4Fapiao.????????????.code())});
        return new PageHelpers<>(condition.getPage(), condition.getSize(), fapiaoMapper.count(conditions), fapiaoMapper.queryPage(new Page<>(condition.getPage(), condition.getSize()), conditions), fapiaoMapper.statsData(conditions));
    }

    @Override
    public PageHelpers<OAScheduleDto> mediumOrderPage(Integer page, Integer size, String customer, String fapiaoid) {
        Map<String, Object> conditions = new HashMap<>();
        if (StringUtils.isNotEmpty(customer)) {
            QueryWrapper<Customer> wrapper = new QueryWrapper<>();
            wrapper.like("brand", customer);
            List<Customer> customers = customerMapper.selectList(wrapper);
            if (customers != null && customers.size() > 0) {
                customer = String.valueOf(customers.get(0).getId());
            }
            conditions.put("customer", customer);
        }
        conditions.put("fapiaoid", fapiaoid);
        List<OAScheduleDto> list = fapiaoMapper.mediumOrdersByPage(new Page<>(page, size), conditions);
        list.forEach(v -> {

            String[] array = MathUtil.calculateProfitMore(v.getExecutePrice(), v.getCostPrice(), v.getCustFapiao() == -1 ? 0 : v.getCustFapiao(),
                    v.getOaFapiaoType() != null && v.getOaFapiaoType() == 0 ? (v.getOaFapiao() == -1 ? 0 : v.getOaFapiao()) : 0, v.getSellExpense(), v.getChannelExpense());
            v.setCustomerTax(array[0]);
            v.setChannelTax(array[1]);
            v.setProfit(array[2]);
            v.setProfitRate(array[3]);

        });
        return new PageHelpers<>(page, size, fapiaoMapper.mediumOrdersCount(conditions), list, null);
    }


    @Override
    public PageHelpers<Invoice> list3(FapiaoCondition condition) {
        Map<String, Object> conditions = new HashMap<String, Object>();
        if (condition.getCreateUserid() != null) {
            conditions.put("create_userid", condition.getCreateUserid());
        } else {
            conditions.put("audit_userid", BaseController.getManagerInfo().getId());
        }
        conditions.put("is_add", 0);
        conditions.put("bill_no", condition.getBillNo());
        conditions.put("enter_name", condition.getEnterName());
        conditions.put("type", condition.getType());
        settime(condition, conditions);
        setstatus2(condition, conditions);
        return new PageHelpers<>(condition.getPage(), condition.getSize(), fapiaoMapper.count(conditions), fapiaoMapper.queryPage(new Page<>(condition.getPage(), condition.getSize()), conditions), fapiaoMapper.statsData(conditions));
    }

    /**
     * ??????????????????
     */
    private void setstatus2(FapiaoCondition condition, Map<String, Object> conditions) {
        if ("0".equals(condition.getsType())) {//?????????
            conditions.put("state", new String[]{String.valueOf(StateEnums.State4Fapiao.??????.code()), String.valueOf(StateEnums.State4Fapiao.????????????????????????.code()), String.valueOf(StateEnums.State4Fapiao.????????????_???????????????.code())});
        } else if ("1".equals(condition.getsType())) {//????????????
            conditions.put("state", new String[]{String.valueOf(StateEnums.State4Fapiao.????????????.code())});
        } else if ("2".equals(condition.getsType())) {//????????????
            conditions.put("state", new String[]{String.valueOf(StateEnums.State4Fapiao.???????????????.code())});
        }
    }

    /**
     * ??????????????????
     */
    private void setstatus(FapiaoCondition condition, Map<String, Object> conditions) {
        if ("0".equals(condition.getsType())) {//?????????
            conditions.put("state", new String[]{String.valueOf(StateEnums.State4Fapiao.????????????_???????????????.code())});
        } else if ("1".equals(condition.getsType())) {//??????
            conditions.put("state", new String[]{String.valueOf(StateEnums.State4Fapiao.????????????.code())});
        } else if ("2".equals(condition.getsType())) {//??????
            conditions.put("state", new String[]{String.valueOf(StateEnums.State4Fapiao.???????????????.code())});
        } else {
            //???????????????????????????
            conditions.put("state", new String[]{String.valueOf(StateEnums.State4Fapiao.????????????.code())});
        }
    }

    private void settime(FapiaoCondition condition, Map<String, Object> conditions) {
        //????????????
        if (StringUtils.isNotEmpty(condition.getStartdate())) {
            if (condition.getDateType().equals("2")) {//????????????
                conditions.put("createrdate_start", condition.getStartdate());
            } else if (condition.getDateType().equals("1")) {//????????????
                conditions.put("makedate_start", condition.getStartdate());
            } else if (condition.getDateType().equals("3")) {//????????????
                conditions.put("verificatio_start", condition.getStartdate());
            }
        }
        if (StringUtils.isNotEmpty(condition.getEnddate())) {
            if (condition.getDateType().equals("2")) {
                conditions.put("createrdate_end", condition.getEnddate());
            } else if (condition.getDateType().equals("1")) {
                conditions.put("makedate_end", condition.getEnddate());
            } else if (condition.getDateType().equals("3")) {//????????????
                conditions.put("verificatio_end", condition.getEnddate());
            }
        }
    }


    @Override
    public boolean addFapiao(FapiaoSubmit submit, LoginCache loginCache, MultipartFile file) {
        String uploadFile = ossService.uploadFile(SystemEnums.OSSFolder.BMS.name(), file);
        Fapiao fapiao = new Fapiao();
        BeanUtils.copyProperties(submit, fapiao);
        fapiao.setIsAdd("1");        //???????????????0-?????????1-?????????
        fapiao.setIsBalance("1");    //???????????????????????????????????????????????????????????????
        fapiao.setCreateUserid(loginCache.getId());
        fapiao.setCreateDate(new Date());
        fapiao.setCreateUsername(loginCache.getName());
        fapiao.setAuditUserid(loginCache.getId());
        if (fapiao.getVerificatioTime() == null) {
            fapiao.setVerificatioTime(new Date());
        }
        fapiao.setFapiaoImg(uploadFile);
        fapiao.setFapiaoCode(submit.getFapiaoCode());
        fapiao.setState(String.valueOf(StateEnums.State4Fapiao.????????????.code()));        //?????????????????????????????????
        fapiao.setSource(0);
        return save(fapiao);
    }


    @Override
    public boolean updateFapiao(FapiaoSubmit submit) {
        Fapiao fapiao = new Fapiao();
        BeanUtils.copyProperties(submit, fapiao);
        UpdateWrapper<Fapiao> wrapper = new UpdateWrapper<>();
        if (fapiao.getMoney() != null) {
            wrapper.set("money", fapiao.getMoney());
        }
        if (fapiao.getRatesMoney() != null) {
            wrapper.set("rates_money", fapiao.getRatesMoney());
        }

        wrapper.set("verificatio_time", fapiao.getVerificatioTime());
        wrapper.eq("id", fapiao.getId());
        return update(wrapper);
    }

    @Override
    public boolean updateMakeDate(FapiaoSubmit submit) {
        Fapiao fapiao = new Fapiao();
        BeanUtils.copyProperties(submit, fapiao);
        UpdateWrapper<Fapiao> wrapper = new UpdateWrapper<>();
        wrapper.set("make_date", fapiao.getMakeDate());
        wrapper.eq("id", fapiao.getId());
        return update(wrapper);
    }


    @Override
    public boolean delFapiao(List<String> ids) {
        List<Fapiao> list = fapiaoMapper.selectBatchIds(ids);
        for (Fapiao fapiao : list) {
            Assert.state(fapiao.getState().equals((String.valueOf(StateEnums.State4Fapiao.??????.code()))), "?????????????????????????????????????????????");
        }
        boolean b = removeByIds(ids);
        if (b) {
            boolean boo = oasReturnService.clearFapiao(ids.stream().map(Integer::valueOf).collect(Collectors.toList()));
            Assert.state(boo, "??????????????????????????????");
            approvalMsgService.sendApprovalMsg(TypeEnums.Type4ApprovalMsg.??????????????????, BaseController.getManagerInfo().getId());
        }
        return b;
    }


    @Override
    public PageHelper2 toBillPaymentPage(Integer userid, String s_pay_account, String s_rec_name, String s_medium, Integer page, Integer size) {
        Map<String, Object> conditions = new HashMap<>();
        //??????????????????????????????????????????
        conditions.put("state", new String[]{String.valueOf(StateEnums.State4PaymentApp.?????????.code())});
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
        return new PageHelper2(page, size, fapiaoMapper.toBillPaymentPageCount(conditions), fapiaoMapper.toBillPaymentPage(new Page<>(page, size), conditions));
    }


    @Override
    public int reportBillByIds(List<String> ids) {
        int success = 0;
        Integer reviewer;
        RoleSign sign = roleService.judgeRole(BaseController.getManagerInfo().getRole());
        if (sign == RoleSign.ae) {
            reviewer = departmentService.getSuperiorLeader(BaseController.getManagerInfo().getDepartment());
            Assert.notNull(reviewer, "????????????????????????");
        } else {
            reviewer = BaseController.getManagerInfo().getId();
        }
        UpdateWrapper<Fapiao> updateWrapper;
        List<Fapiao> list = fapiaoMapper.selectBatchIds(ids);
        for (Fapiao fapiao : list) {
            Assert.state(fapiao.getState().equals((String.valueOf(StateEnums.State4Fapiao.??????.code()))), "?????????????????????????????????????????????");
        }
        for (String id : ids) {
            updateWrapper = new UpdateWrapper<Fapiao>()
                    .eq("id", id)
                    .set("state", StateEnums.State4Fapiao.????????????????????????.code())
                    .set("audit_userid", reviewer)
            ;
            success += fapiaoMapper.update(null, updateWrapper);
        }
        Assert.state(success > 0, "??????????????????????????????");
        approvalMsgService.sendApprovalMsg(TypeEnums.Type4ApprovalMsg.??????????????????, BaseController.getManagerInfo().getId());
        return success;
    }

    @Override
    @Transactional
    public boolean saveBillInfo(FapiaoSubmit fapiaoSubmit, String ids, LoginCache loginCache) {
        //???????????????????????????????????????
        List<String> idList = Arrays.asList(ids.split(","));
        for (String id : idList) {
            OASReturn oasReturn = oasReturnService.getById(id);
            Assert.state(oasReturn.getFapiao() == null, "??????????????????????????????" + id);
            Assert.state(!oasReturn.getState().equals(StateEnums.State4OASReturn.??????.code()), "???????????????????????????");
        }
        Fapiao fapiao = new Fapiao();
        BeanUtils.copyProperties(fapiaoSubmit, fapiao);
        fapiao.setAuditUserid(BaseController.getManagerInfo().getId());
//        ??????????????????
        BigDecimal total_money = fapiao.getTotalMoney().setScale(2, BigDecimal.ROUND_CEILING); //????????????
        BigDecimal actual_money = fapiao.getActualMoney().setScale(2, BigDecimal.ROUND_CEILING);    //??????????????????
        BigDecimal rates = fapiao.getRates().setScale(2, BigDecimal.ROUND_CEILING);                    //??????
        BigDecimal money = actual_money.divide(rates.divide(new BigDecimal(100)).add(new BigDecimal(1)), 2, BigDecimal.ROUND_DOWN);    //??????=????????????/???1+?????????
        BigDecimal rates_money = actual_money.subtract(money);
        fapiao.setBillNo(null);
        fapiao.setMoney(money);
        fapiao.setRates(rates);
        fapiao.setRatesMoney(rates_money);
        fapiao.setTotalMoney(total_money);
        fapiao.setActualMoney(actual_money);
        fapiao.setIsAdd("0");
        fapiao.setCreateUserid(loginCache.getId());
        fapiao.setCreateUsername(loginCache.getName());
        fapiao.setCreateDate(new Date());
        fapiao.setState(String.valueOf(StateEnums.State4Fapiao.??????.code()));
        fapiao.setIsBalance("1");    //???????????????????????????????????????????????????
        if (save(fapiao)) {
            //????????????????????????
            boolean boo = oasReturnService.setFapiao(fapiao.getId(), idList.stream().map(Integer::valueOf).collect(Collectors.toList()));
            Assert.state(boo, "??????????????????????????????");
        }
        approvalMsgService.sendApprovalMsg(TypeEnums.Type4ApprovalMsg.??????????????????, loginCache.getId());
        return true;
    }

    @Override
    public boolean issuedBill(FapiaoSubmit fapiaoSubmit, String ids, LoginCache user) {
        //???????????????????????????????????????
        List<String> idList = Arrays.asList(ids.split(","));
        for (String id : idList) {
            OASReturn oasReturn = oasReturnService.getById(id);
            Assert.state(oasReturn.getFapiao() == null, "??????????????????????????????" + id);
            Assert.state(!oasReturn.getState().equals(StateEnums.State4OASReturn.??????.code()), "???????????????????????????");
        }
        Fapiao fapiao = new Fapiao();
        BeanUtils.copyProperties(fapiaoSubmit, fapiao);
        fapiao.setAuditUserid(BaseController.getManagerInfo().getId());
//        ??????????????????
        BigDecimal total_money = fapiao.getTotalMoney().setScale(2, BigDecimal.ROUND_CEILING); //????????????
        BigDecimal actual_money = fapiao.getActualMoney().setScale(2, BigDecimal.ROUND_CEILING);    //??????????????????
        BigDecimal rates = fapiao.getRates().setScale(2, BigDecimal.ROUND_CEILING);                    //??????
        BigDecimal money = actual_money.divide(rates.divide(new BigDecimal(100)).add(new BigDecimal(1)), 2, BigDecimal.ROUND_DOWN);    //??????=????????????/???1+?????????
        BigDecimal rates_money = actual_money.subtract(money);
        fapiao.setBillNo(null);
        fapiao.setMoney(money);
        fapiao.setRates(rates);
        fapiao.setRatesMoney(rates_money);
        fapiao.setTotalMoney(total_money);
        fapiao.setActualMoney(actual_money);
        fapiao.setIsAdd("0");
        fapiao.setCreateUserid(user.getId());
        fapiao.setCreateUsername(user.getName());
        fapiao.setCreateDate(new Date());
        fapiao.setState(String.valueOf(StateEnums.State4Fapiao.????????????????????????.code()));
        fapiao.setIsBalance("1");    //???????????????????????????????????????????????????
        if (save(fapiao)) {
            //????????????????????????
            boolean boo = oasReturnService.setFapiao(fapiao.getId(), idList.stream().map(Integer::valueOf).collect(Collectors.toList()));
            Assert.state(boo, "??????????????????????????????");
        }
        approvalMsgService.sendApprovalMsg(TypeEnums.Type4ApprovalMsg.??????????????????, user.getId());
        return true;
    }


    @Override
    public boolean auditBillOrderPass(FapiaoSubmit fapiaoSubmit) {
//        checkLeader();
        Fapiao fapiao = getById(fapiaoSubmit.getId());
        fapiao.setMakeDate(fapiaoSubmit.getMakeDate());
        fapiao.setAuditUserid(fapiaoSubmit.getAuditUserid());
        Assert.state(fapiao.getState().equals(String.valueOf(StateEnums.State4Fapiao.????????????????????????.code())), "???????????????????????????????????????????????????");
        fapiao.setState(String.valueOf(StateEnums.State4Fapiao.????????????_???????????????.code()));
        if (updateById(fapiao)) {
            approvalMsgService.sendApprovalMsg(TypeEnums.Type4ApprovalMsg.??????????????????, BaseController.getManagerInfo().getId());
        }
        return true;
    }

    /**
     * ????????????AE?????????????????????
     */
//    private void checkLeader() {
//        QueryWrapper<Department> wrapper = new QueryWrapper<>();
//        wrapper.eq("id", BaseController.getManagerInfo().getDepartment());
//        Department department = departmentService.getOne(wrapper);
//        if (department != null) {
//            Assert.state(department.getLeader().equals(BaseController.getManagerInfo().getId()), "???????????????");
//        }
//    }
    @Override
    public boolean auditBillOrderPass2(FapiaoSubmit fapiaoSubmit, MultipartFile... files) {
        Assert.state(!StringUtils.isEmpty(fapiaoSubmit.getBillNo()), "????????????????????????");
//        String uploadFile = ossService.uploadFile(SystemEnums.OSSFolder.BMS.name(), file);
        Fapiao fapiao = getById(fapiaoSubmit.getId());
        fapiao.setFapiaoImg(JacksonHelper.toJsonStr(ossService.uploadFiles(SystemEnums.OSSFolder.BMS.name(), files)));
//        fapiao.setFapiaoImg(uploadFile);
        fapiao.setBillNo(fapiaoSubmit.getBillNo());
        fapiao.setMakeDate(fapiaoSubmit.getMakeDate());
        fapiao.setActualMoney(fapiaoSubmit.getActualMoney());
        fapiao.setRatesMoney(fapiaoSubmit.getRatesMoney());
        fapiao.setMoney(fapiaoSubmit.getActualMoney().subtract(fapiao.getRatesMoney()));
        Assert.state(fapiao.getState().equals(String.valueOf(StateEnums.State4Fapiao.????????????_???????????????.code())), "???????????????????????????????????????????????????");
        fapiao.setState(String.valueOf(StateEnums.State4Fapiao.????????????.code()));
        if (updateById(fapiao)) {
            approvalMsgService.sendApprovalMsg(TypeEnums.Type4ApprovalMsg.???????????????????????????, BaseController.getManagerInfo().getId());
        }
        return true;
    }

    @Override
    @Transactional
    public int auditBillOrderNopass(List<String> ids, String audit_desc) {
//        checkLeader();
        int success = 0;
        for (String id : ids) {
            Fapiao fapiao = getById(id);
            Assert.state(audit_desc.length() < 200, "??????????????????");
            UpdateWrapper<Fapiao> updateWrapper = new UpdateWrapper<Fapiao>()
                    .eq("id", id)
                    .set("audit_desc", audit_desc)
                    .set("state", StateEnums.State4Fapiao.???????????????.code());
            success += fapiaoMapper.update(fapiao, updateWrapper);
            approvalMsgService.sendApprovalMsg(TypeEnums.Type4ApprovalMsg.??????????????????, BaseController.getManagerInfo().getId());
        }
//         ??????????????????????????????
        if (success > 0) {
            boolean boo = oasReturnService.clearFapiao(ids.stream().map(Integer::valueOf).collect(Collectors.toList()));
            Assert.state(boo, "??????????????????????????????");
        }
        return success;
    }

    @Override
    public int auditBillOrderNopass2(List<String> ids, String audit_desc) {
        int success = 0;
        for (String id : ids) {
            Fapiao fapiao = getById(id);
            Assert.state(audit_desc.length() < 200, "??????????????????");
            UpdateWrapper<Fapiao> updateWrapper = new UpdateWrapper<Fapiao>()
                    .eq("id", id)
                    .set("audit_desc", audit_desc)
                    .set("state", StateEnums.State4Fapiao.???????????????.code());
            success += fapiaoMapper.update(fapiao, updateWrapper);
            approvalMsgService.sendApprovalMsg(TypeEnums.Type4ApprovalMsg.???????????????????????????, BaseController.getManagerInfo().getId());
        }
//         ??????????????????????????????
        if (success > 0) {
            boolean boo = oasReturnService.clearFapiao(ids.stream().map(Integer::valueOf).collect(Collectors.toList()));
            Assert.state(boo, "??????????????????????????????");
        }
        return success;
    }

    @Override
    @Transactional
    public boolean saveBillInfo2(FapiaoSubmit fapiaoSubmit, String ids, LoginCache user, MultipartFile... files) {
//        String uploadFile = ossService.uploadFile(SystemEnums.OSSFolder.BMS.name(), file);
        Fapiao fapiao = new Fapiao();
        BeanUtils.copyProperties(fapiaoSubmit, fapiao);
//        ??????????????????????????????
        List<String> idList = Arrays.asList(ids.split(","));
        List<Integer> paymentIds = new ArrayList<>();
        BigDecimal total_money = new BigDecimal(0);        //????????????
        for (String id : idList) {
            PaymentApp payment = paymentAppService.getById(id);
            Assert.state(payment != null, "????????????????????????????????????");
            total_money = total_money.add(payment.getAmount());
            paymentIds.add(payment.getId());
        }
        BigDecimal rates = fapiao.getRates().setScale(2, BigDecimal.ROUND_CEILING);                    //??????
        BigDecimal money = total_money.divide(rates.divide(new BigDecimal(100)).add(new BigDecimal(1)), 2, BigDecimal.ROUND_DOWN);    //??????=????????????/???1+?????????
        BigDecimal rates_money = total_money.subtract(money); //??????(????????????-??????)
        fapiao.setMoney(money);
        fapiao.setRates(rates);
        fapiao.setRatesMoney(rates_money);
        fapiao.setTotalMoney(total_money);
        fapiao.setIsAdd("1");        //???????????????0-?????????1-?????????
        fapiao.setIsBalance("1");    //???????????????????????????????????????????????????????????????
        fapiao.setCreateUserid(user.getId());
        fapiao.setCreateDate(new Date());
        fapiao.setCreateUsername(user.getName());
        fapiao.setFapiaoImg(JacksonHelper.toJsonStr(ossService.uploadFiles(SystemEnums.OSSFolder.BMS.name(), files)));
        fapiao.setFapiaoCode(fapiaoSubmit.getFapiaoCode());
        fapiao.setState(String.valueOf(StateEnums.State4Fapiao.????????????_???????????????.code()));        //?????????????????????????????????
        fapiao.setSource(1);
//        ??????????????????
        save(fapiao);
        //????????????????????????
        boolean boo = paymentAppService.setFapiao(fapiao.getId(), paymentIds);
        Assert.state(boo, "??????????????????????????????");
        approvalMsgService.sendApprovalMsg(TypeEnums.Type4ApprovalMsg.???????????????????????????, fapiaoSubmit.getAuditUserid());
        return true;
    }


    @Override
    public boolean auditBillPass(FapiaoSubmit fapiaoSubmit) {
        Fapiao fapiao = getById(fapiaoSubmit.getId());
        Assert.state(fapiao.getState().equals(String.valueOf(StateEnums.State4Fapiao.????????????_???????????????.code())), "???????????????????????????????????????????????????");
        UpdateWrapper<Fapiao> wrapper = new UpdateWrapper<>();
        if (fapiaoSubmit.getVerificatioTime() == null) {
            wrapper.set("verificatio_time", new Date());
        } else {
            wrapper.set("verificatio_time", fapiaoSubmit.getVerificatioTime());
        }
        wrapper.eq("id", fapiaoSubmit.getId());
        wrapper.set("state", StateEnums.State4Fapiao.????????????.code());
        if (update(wrapper)) {
            approvalMsgService.sendApprovalMsg(TypeEnums.Type4ApprovalMsg.???????????????????????????, BaseController.getManagerInfo().getId());
        }
        return true;
    }

    @Override
    @Transactional
    public int auditBillNopass(List<String> ids, String audit_desc) {
        int success = 0;
        for (String id : ids) {
            Fapiao fapiao = getById(id);
            Assert.state(fapiao.getState().equals(String.valueOf(StateEnums.State4Fapiao.????????????_???????????????.code())), "???????????????????????????????????????????????????")
            ;
            Assert.state(audit_desc.length() < 200, "??????????????????");
            UpdateWrapper<Fapiao> updateWrapper = new UpdateWrapper<Fapiao>()
                    .eq("id", id)
                    .set("audit_desc", audit_desc)
                    .set("state", StateEnums.State4Fapiao.???????????????.code());
            success += fapiaoMapper.update(null, updateWrapper);
//         ??????????????????????????????????????????????????????????????????
            boolean boo = paymentAppService.clearFapiao(Integer.valueOf(id));
            Assert.state(boo, "????????????????????????????????????");
        }
        approvalMsgService.sendApprovalMsg(TypeEnums.Type4ApprovalMsg.???????????????????????????, BaseController.getManagerInfo().getId());
        return success;
    }

//    @Override
//    public int noBillPayment(List<String> ids) {
//        int success = 0;
//        for (String id : ids) {
//            success += paymentAppService.noFapiao(Integer.valueOf(id)) ? 1 : 0;
//        }
//        return success;
//    }

    @Override
    public List<Fapiao_Excel> excelDrive(FapiaoCondition condition) {
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
        List<Fapiao_Excel> fapiao = fapiaoMapper.excelDrive(conditions);
//        ??????invoiceRelevantList???????????????????????????????????????????????????????????????????????????????????????
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
                    BigDecimal duty = fapiaoMapper.findDuty(e.getId(), sellId);
                    Fapiao fapiao2 = fapiaoMapper.selectById(e.getId());
//                    ??????
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
    public List<Fapiao_Excel2> excelDrive2(FapiaoCondition condition) {
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("is_add", 0);
        conditions.put("bill_no", condition.getBillNo());
        conditions.put("enter_name", condition.getEnterName());
        conditions.put("make_name", condition.getMakeName());
        conditions.put("type", condition.getType());
        conditions.put("audit_userid", condition.getAuditUserid());
        conditions.put("create_userid", condition.getOrder_xs());
        settime(condition, conditions);
        conditions.put("state", new String[]{String.valueOf(StateEnums.State4Fapiao.????????????.code())});
        return fapiaoMapper.excelDrive2(conditions);
    }


    @Override
    public Map<Integer, FapiaoSimpleInfo> queryCodeCompanyById(Collection<Integer> collection) {
        if (!collection.isEmpty()) {
            List<Fapiao> list = list(new QueryWrapper<Fapiao>().select("id", "bill_no", "enter_name").in("id", collection));
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
        return fapiaoMapper.getBillInfoByCustomer(conditions);
    }

    @Override
    public Integer autoGenerationBill(BigDecimal total_money, Integer fapiaoType, BigDecimal fapiaoRates, Integer medium, String mediumName) {
    	Date date = new Date();
        Fapiao fapiao = new Fapiao();
        BigDecimal money = total_money.divide(fapiaoRates.divide(new BigDecimal(100)).add(new BigDecimal(1)), 2, BigDecimal.ROUND_DOWN);    //??????=????????????/???1+?????????
        BigDecimal rates_money = total_money.subtract(money); //??????(????????????-??????)
        fapiao.setMoney(money);
        fapiao.setRates(fapiaoRates);
        fapiao.setRatesMoney(rates_money);
        fapiao.setTotalMoney(total_money);
        fapiao.setType(fapiaoType == 0 ? "1" : "0");
        fapiao.setIsAdd("1");        //???????????????0-?????????1-?????????
        fapiao.setIsBalance("1");    //???????????????????????????????????????????????????????????????
        fapiao.setCreateUserid(medium);
        fapiao.setCreateUsername(mediumName);
        fapiao.setCreateDate(date);
        fapiao.setFapiaoCode("/");
        fapiao.setState(String.valueOf(StateEnums.State4Fapiao.????????????.code()));
        fapiao.setSource(1);
        fapiao.setEnterName("/");
        fapiao.setMakeName("??????????????????????????????????????????");
        fapiao.setMakeDate(date);
        fapiao.setVerificatioTime(date);
        fapiao.setBillNo("system generate");
        save(fapiao);
        return fapiao.getId();
    }


}
