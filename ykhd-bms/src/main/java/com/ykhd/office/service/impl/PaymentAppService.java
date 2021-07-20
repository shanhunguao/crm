package com.ykhd.office.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ykhd.office.component.aliyun.oss.OssService;
import com.ykhd.office.controller.BaseController;
import com.ykhd.office.domain.bean.FapiaoSimpleInfo;
import com.ykhd.office.domain.bean.IdDecimal;
import com.ykhd.office.domain.bean.LoginCache;
import com.ykhd.office.domain.bean.OAScheduleInfo;
import com.ykhd.office.domain.bean.common.KeyValuePair;
import com.ykhd.office.domain.bean.common.PageHelpers;
import com.ykhd.office.domain.entity.OASchedule;
import com.ykhd.office.domain.entity.PaymentApp;
import com.ykhd.office.domain.req.PaymentAppCondition;
import com.ykhd.office.domain.req.PaymentAppSubmit;
import com.ykhd.office.domain.resp.PaymentAppDto;
import com.ykhd.office.domain.resp.PaymentApp_Excel;
import com.ykhd.office.repository.PaymentAppMapper;
import com.ykhd.office.service.IEditRecordService;
import com.ykhd.office.service.IFapiaoService;
import com.ykhd.office.service.IOAScheduleService;
import com.ykhd.office.service.IPaymentAppService;
import com.ykhd.office.service.IRoleService;
import com.ykhd.office.util.DateUtil;
import com.ykhd.office.util.JacksonHelper;
import com.ykhd.office.util.dictionary.Consts;
import com.ykhd.office.util.dictionary.StateEnums.State4AppCancel;
import com.ykhd.office.util.dictionary.StateEnums.State4OASchedule;
import com.ykhd.office.util.dictionary.StateEnums.State4PaymentApp;
import com.ykhd.office.util.dictionary.SystemEnums.OSSFolder;
import com.ykhd.office.util.dictionary.SystemEnums.RoleSign;
import com.ykhd.office.util.dictionary.TypeEnums.Type4ApprovalMsg;
import com.ykhd.office.util.dictionary.TypeEnums.Type4Edit;
import com.ykhd.office.util.dictionary.TypeEnums.Type4PaymentApp;

import cn.gjing.tools.excel.ExcelFactory;

@Service
public class PaymentAppService extends ServiceImpl<BaseMapper<PaymentApp>, PaymentApp> implements IPaymentAppService {

	@Autowired
	private IOAScheduleService oAScheduleService;
	@Autowired
	private IEditRecordService editRecordService;
	@Autowired
	private PaymentAppMapper paymentAppMapper;
	@Autowired
	private OssService ossService;
	@Autowired
	private ApprovalMsgService approvalMsgService;
	@Autowired
	private IRoleService roleService;
	@Autowired
	private IFapiaoService fapiaoService;
	
	@Override
	public PageHelpers<PaymentAppDto> getListByPage(PaymentAppCondition condition) {
		QueryWrapper<PaymentApp> queryWrapper = new QueryWrapper<>();
		if (condition.getSchedule() != null)
			queryWrapper.eq("schedule", condition.getSchedule());
		if (condition.getState() != null)
			queryWrapper.eq("state", condition.getState());
		if (condition.getCreator() != null)
			queryWrapper.eq("creator", condition.getCreator());
		if (condition.getScheduler() != null) {
			List<Integer> schedule = oAScheduleService.queryIdByCreator(condition.getScheduler());
			if (schedule.isEmpty())
				return new PageHelpers<PaymentAppDto>(condition.getPage(), condition.getSize(), 0, Collections.emptyList(), Collections.emptyMap());
			queryWrapper.in("schedule", schedule);
		}
		if (StringUtils.hasText(condition.getAccountHolder()))
			queryWrapper.like("account_holder", condition.getAccountHolder().trim());
		if (StringUtils.hasText(condition.getCustomerBrand())) {
			List<Integer> schedule = oAScheduleService.queryIdByCustomer(condition.getCustomerBrand().trim());
			if (schedule.isEmpty())
				return new PageHelpers<PaymentAppDto>(condition.getPage(), condition.getSize(), 0, Collections.emptyList(), Collections.emptyMap());
			queryWrapper.in("schedule", schedule);
		}
		if (condition.getDateType() != null) {
			String field = condition.getDateType() == 1 ? "create_time" : "paytime";
			if (StringUtils.hasLength(condition.getStartTime()))
				queryWrapper.ge(field, condition.getStartTime());
			if (StringUtils.hasLength(condition.getEndTime()))
				queryWrapper.lt(field, DateUtil.nextDay(condition.getEndTime()));
		}
		if (StringUtils.hasText(condition.getOaName())) {
			List<Integer> ids = paymentAppMapper.queryIdsByOaNameWechat(condition.getOaName().trim());
			if (ids.isEmpty())
				return new PageHelpers<PaymentAppDto>(condition.getPage(), condition.getSize(), 0, Collections.emptyList(), Collections.emptyMap());
			queryWrapper.in("id", ids);
		}
		if (condition.getAmount() != null)
			queryWrapper.eq("amount", condition.getAmount());
		if (StringUtils.hasText(condition.getPayAccount()))
			queryWrapper.like("pay_account", condition.getPayAccount().trim());
		if (condition.getMakeFapiao() != null) {
			if (condition.getMakeFapiao() == 0) //已开票
				queryWrapper.isNotNull("fapiao");
			else
				queryWrapper.ne("oa_fapiao", -1).isNull("fapiao");
		}
		// 【查询限制】
		LoginCache managerInfo = BaseController.getManagerInfo();
		RoleSign sign = roleService.judgeRole(managerInfo.getRole());
		if (sign == RoleSign.medium) // 若是媒介则只能看到自己申请的支付
			queryWrapper.eq("creator", managerInfo.getId());
		queryWrapper.orderByDesc("id");
		IPage<PaymentApp> iPage = page(new Page<>(condition.getPage(), condition.getSize()), queryWrapper);
		List<PaymentApp> records = iPage.getRecords();
		// 数据处理
		Map<Integer, OAScheduleInfo> map = oAScheduleService.info4PaymentAppList(records.stream().map(PaymentApp::getSchedule).collect(Collectors.toSet()));
		Set<Integer> fapiaoIds = records.stream().filter(v -> v.getFapiao() != null).map(PaymentApp::getFapiao).collect(Collectors.toSet());
		Map<Integer, FapiaoSimpleInfo> fapiao_map = fapiaoService.queryCodeCompanyById(fapiaoIds);
		List<PaymentAppDto> list = records.stream().flatMap(v -> {
			PaymentAppDto dto = new PaymentAppDto();
			BeanUtils.copyProperties(v, dto);
			// 排期信息
			OAScheduleInfo info = map.get(v.getSchedule());
			if (info != null)
				BeanUtils.copyProperties(info, dto, "id");
			// 发票信息
			FapiaoSimpleInfo info2 = fapiao_map.get(dto.getFapiao());
			if (info2 != null) {
				dto.setFapiaoCode(info2.getBillNo());
				dto.setFapiaoCompany(info2.getEnterName());
			}
			return Stream.of(dto);
		}).collect(Collectors.toList());
		// 计算汇总数据
		Map<String, Object> summary_map = new HashMap<>();
		if (condition.getDateType() == 2) { // 付款日期
			List<PaymentApp> payList = list(queryWrapper.select("amount"));
			summary_map.put("total_pay", 
					BigDecimal.valueOf(payList.stream().filter(v -> v != null).mapToDouble(v -> v.getAmount().doubleValue()).summaryStatistics().getSum()).setScale(2, BigDecimal.ROUND_HALF_UP));
		}
		return new PageHelpers<PaymentAppDto>(condition.getPage(), condition.getSize(), iPage.getTotal(), list, summary_map);
	}

	@Override
	public void exportExcel(PaymentAppCondition condition, HttpServletResponse response) {
		QueryWrapper<PaymentApp> queryWrapper = new QueryWrapper<>();
		if (condition.getSchedule() != null)
			queryWrapper.eq("schedule", condition.getSchedule());
		if (condition.getState() != null)
			queryWrapper.eq("state", condition.getState());
		if (condition.getCreator() != null)
			queryWrapper.eq("creator", condition.getCreator());
		if (condition.getScheduler() != null) {
			List<Integer> schedule = oAScheduleService.queryIdByCreator(condition.getScheduler());
			if (schedule.isEmpty()) {
				ExcelFactory.createWriter(PaymentApp_Excel.class, response).write(null).flush();
				return;
			}
			queryWrapper.in("schedule", schedule);
		}
		if (StringUtils.hasText(condition.getAccountHolder()))
			queryWrapper.like("account_holder", condition.getAccountHolder().trim());
		if (StringUtils.hasText(condition.getCustomerBrand())) {
			List<Integer> schedule = oAScheduleService.queryIdByCustomer(condition.getCustomerBrand().trim());
			if (schedule.isEmpty()) {
				ExcelFactory.createWriter(PaymentApp_Excel.class, response).write(null).flush();
				return;
			}
			queryWrapper.in("schedule", schedule);
		}
		if (StringUtils.hasText(condition.getOaName())) {
			List<Integer> ids = paymentAppMapper.queryIdsByOaNameWechat(condition.getOaName().trim());
			if (ids.isEmpty())
				ExcelFactory.createWriter(PaymentApp_Excel.class, response).write(null).flush();
			queryWrapper.in("in", ids);
		}
		if (condition.getDateType() != null) {
			String field = condition.getDateType() == 1 ? "create_time" : "paytime";
			Date start, end;
			if (StringUtils.hasLength(condition.getEndTime()))
				end = DateUtil.nextDay(condition.getEndTime());
			else
				end = DateUtil.nextDay(new Date());
			if (StringUtils.hasLength(condition.getStartTime())) {
				start = DateUtil.yyyyMMdd2date(condition.getStartTime());
				if (start.before(DateUtil.monthsAgo(end, 2))) //最大间隔2个月
					start = DateUtil.monthsAgo(end, 2);
			} else
				start = DateUtil.monthsAgo(end, 2);
			queryWrapper.ge(field, start).lt(field, end);
		}
		if (condition.getAmount() != null)
			queryWrapper.eq("amount", condition.getAmount());
		if (StringUtils.hasText(condition.getPayAccount()))
			queryWrapper.like("pay_account", condition.getPayAccount().trim());
		if (condition.getMakeFapiao() != null) {
			if (condition.getMakeFapiao() == 0) //已开票
				queryWrapper.isNotNull("fapiao");
			else
				queryWrapper.ne("oa_fapiao", -1).isNull("fapiao");
		}
		// 【查询限制】
		LoginCache managerInfo = BaseController.getManagerInfo();
		RoleSign sign = roleService.judgeRole(managerInfo.getRole());
		if (sign == RoleSign.medium) // 若是媒介则只能看到自己申请的支付
			queryWrapper.eq("creator", managerInfo.getId());
		List<PaymentApp> records = list(queryWrapper.orderByAsc("id"));
		// 数据处理
		Map<Integer, OAScheduleInfo> map = oAScheduleService.info4PaymentAppList(records.stream().map(PaymentApp::getSchedule).collect(Collectors.toSet()));
		Set<Integer> fapiaoIds = records.stream().filter(v -> v.getFapiao() != null).map(PaymentApp::getFapiao).collect(Collectors.toSet());
		Map<Integer, FapiaoSimpleInfo> fapiao_map = fapiaoService.queryCodeCompanyById(fapiaoIds);
		List<PaymentApp_Excel> list = records.stream().flatMap(v -> {
			PaymentApp_Excel dto = new PaymentApp_Excel();
			BeanUtils.copyProperties(v, dto);
			// 排期信息
			OAScheduleInfo info = map.get(dto.getSchedule());
			if (info != null)
				BeanUtils.copyProperties(info, dto, "id");
			// 发票信息
			FapiaoSimpleInfo info2 = fapiao_map.get(dto.getFapiao());
			if (info2 != null) {
				dto.setFapiaoCode(info2.getBillNo());
				dto.setFapiaoCompany(info2.getEnterName());
			}
			return Stream.of(dto);
		}).collect(Collectors.toList());
		ExcelFactory.createWriter(PaymentApp_Excel.class, response).write(list).flush();
	}

	@Override
	public boolean addPaymentApp(PaymentAppSubmit submit) {
		Assert.state(Arrays.stream(Type4PaymentApp.values()).anyMatch(v -> v.code() == submit.getType().intValue()), "type参数非法");
		OASchedule schedule = oAScheduleService.getById(submit.getSchedule());
		Assert.state(schedule.getState().intValue() == State4OASchedule.待结算.code(), "仅限待结算状态的排期");
		Assert.state(schedule.getAppCancel() == null || (schedule.getAppCancel().intValue() != State4AppCancel.待主管审核.code() &&
				schedule.getAppCancel().intValue() != State4AppCancel.待媒介审核.code()), "此排期处在作废流程中");
		Assert.state(!editRecordService.existsOngoing(Type4Edit.排期, schedule.getId()), "此排期处在修改流程中");
		if (submit.getType().intValue() == Type4PaymentApp.成本金额.code()) {
			// 成本金额可以拆分多笔支付（已支付+申请中金额 <= 成本价）
			BigDecimal existPay = totalByPayAndApp(schedule.getId());
			Assert.state(existPay.add(submit.getAmount()).compareTo(schedule.getCostPrice()) <= 0, "总支付金额不能大于成本价");
		} else {
			Assert.state(schedule.getSellExpense() != null && schedule.getSellExpense().doubleValue() > 0, "此排期不含销售费用");
			Assert.state(!existSellApp(schedule.getId()), "销售费用只能申请一次");
		}
		PaymentApp paymentApp = new PaymentApp();
		BeanUtils.copyProperties(submit, paymentApp);
		if(submit.getType().intValue() == Type4PaymentApp.销售费用.code())
			paymentApp.setAmount(schedule.getSellExpense()); //只能一次付清
		paymentApp.setOaFapiao("私账".equals(submit.getPayAccount()) ? -1 : schedule.getOaFapiao());
		paymentApp.setOaFapiaoType(schedule.getOaFapiaoType());
		paymentApp.setState(State4PaymentApp.待支付.code());
		paymentApp.setCreateTime(new Date());
		LoginCache managerInfo = BaseController.getManagerInfo();
		paymentApp.setCreator(managerInfo.getId());
		paymentApp.setCreatorName(managerInfo.getName());
		boolean boo = save(paymentApp);
		if (boo)
			approvalMsgService.sendApprovalMsg(Type4ApprovalMsg.公众号排期支付申请, null);
		return boo;
	}
	
	/**
	 * 排期是否存在申请中或已支付的销售费用
	 */
	private boolean existSellApp(Integer schedule) {
		return count(new QueryWrapper<PaymentApp>().eq("schedule", schedule)
				.eq("type", Type4PaymentApp.销售费用.code())
				.in("state", State4PaymentApp.待支付.code(), State4PaymentApp.待复核.code(), State4PaymentApp.已付款.code())) > 0 ? true : false;
	}

	@Override
	public int refuse(Integer... id) {
		UpdateWrapper<PaymentApp> updateWrapper;
		int success = 0;
		for (Integer _id : id) {
			updateWrapper = new UpdateWrapper<PaymentApp>()
					.eq("id", _id).eq("state", State4PaymentApp.待支付.code())
					.set("state", State4PaymentApp.驳回.code());
			success += paymentAppMapper.update(null, updateWrapper);
		}
		Assert.state(success > 0, "仅限待支付状态");
		approvalMsgService.sendApprovalMsg(Type4ApprovalMsg.公众号排期支付申请, null);
		return success;
	}

	@Override
	public boolean pay(List<Integer> ids, MultipartFile[] files, String date, String remark) {
		List<PaymentApp> list = list(new QueryWrapper<PaymentApp>().select("id", "pay_account", "account_holder").in("id", ids));
		Assert.state(list.stream().map(PaymentApp::getAccountHolder).collect(Collectors.toSet()).size() == 1, "收方名称不一致");
		Assert.state(list.stream().map(PaymentApp::getPayAccount).collect(Collectors.toSet()).size() == 1, "付款账户不一致");
		UpdateWrapper<PaymentApp> updateWrapper = new UpdateWrapper<PaymentApp>();
		updateWrapper.in("id", ids).eq("state", State4PaymentApp.待支付.code())
			.set("payer", BaseController.getManagerInfo().getId()).set("paytime", DateUtil.yyyyMMdd2date(date))
			.set("pay_remark", remark).set("state", State4PaymentApp.待复核.code());
		if ("私账".equals(list.get(0).getPayAccount())) {
			Assert.state(files != null && files.length > 0, "未上传图片");
			updateWrapper.set("pay_image", JacksonHelper.toJsonStr(ossService.uploadFiles(OSSFolder.BMS.name(), files)));
		}
		boolean boo = update(updateWrapper);
		if (boo) {
			approvalMsgService.sendApprovalMsg(Type4ApprovalMsg.公众号排期支付申请, null);
			approvalMsgService.sendApprovalMsg(Type4ApprovalMsg.支付待复核, null);
		}
		return boo;
	}

	@Override
	@Transactional
	public boolean check(Integer id, List<String> oldPath, MultipartFile[] files, String date, String remark) {
		PaymentApp app = getById(id);
		Assert.notNull(app, "未知申请");
		Assert.state(app.getState().intValue() == State4PaymentApp.待复核.code(), "仅限待复核状态");
		if (oldPath == null)
			oldPath = new ArrayList<>();
		if (files != null && files.length > 0)
			oldPath.addAll(ossService.uploadFiles(OSSFolder.BMS.name(), files));
		Assert.state(oldPath.size() > 0, "未上传付款截图");
		app.setPayImage(JacksonHelper.toJsonStr(oldPath));
		app.setPaytime(DateUtil.yyyyMMdd2date(date));
		app.setPayRemark(remark);
		app.setState(State4PaymentApp.已付款.code());
		boolean boo = updateById(app);
		if (boo) {
			oAScheduleService.checkPayFinish(app.getSchedule());
			approvalMsgService.sendApprovalMsg(Type4ApprovalMsg.支付待复核, BaseController.getManagerInfo().getId());
		}
		return boo;
	}

	@Override
	@Transactional
	public boolean checkBatch(List<Integer> ids, MultipartFile[] files) {
		UpdateWrapper<PaymentApp> updateWrapper = new UpdateWrapper<PaymentApp>().in("id", ids).eq("state", State4PaymentApp.待复核.code()).set("state", State4PaymentApp.已付款.code());
		if (files != null && files.length > 0)
			updateWrapper.set("pay_image", JacksonHelper.toJsonStr(ossService.uploadFiles(OSSFolder.BMS.name(), files)));
		boolean boo = update(updateWrapper);
		if (boo) {
			List<Integer> scheIds = list(new QueryWrapper<PaymentApp>().select("schedule").in("id", ids)).stream().map(PaymentApp::getSchedule).distinct().collect(Collectors.toList());
			scheIds.forEach(v -> oAScheduleService.checkPayFinish(v));
			approvalMsgService.sendApprovalMsg(Type4ApprovalMsg.支付待复核, BaseController.getManagerInfo().getId());
		}
		return boo;
	}

	@Override
	public int checkFailure(Integer... id) {
		UpdateWrapper<PaymentApp> updateWrapper;
		int success = 0;
		for (Integer _id : id) {
			updateWrapper = new UpdateWrapper<PaymentApp>()
					.eq("id", _id).eq("state", State4PaymentApp.待复核.code())
					.set("state", State4PaymentApp.待支付.code());
			success += paymentAppMapper.update(null, updateWrapper);
		}
		Assert.state(success > 0, "仅限待复核状态");
		return success;
	}

//	@Override
//	public boolean updatePayInfo(Integer id, MultipartFile file, String date, String remark) {
//		UpdateWrapper<PaymentApp> updateWrapper = new UpdateWrapper<PaymentApp>()
//				.eq("id", id).eq("state", State4PaymentApp.已付款.code())
//				.set("paytime", DateUtil.yyyyMMdd2date(date)).set("pay_remark", remark);
//		if (file != null)
//			updateWrapper.set("pay_image", ossService.uploadFile(OSSFolder.BMS.name(), file));
//		Assert.state(update(updateWrapper), "仅限已付款状态");
//		return true;
//	}

	@Override
	@Transactional
	public boolean deletePaymentApp(Integer id) {
		PaymentApp paymentApp = getOne(new QueryWrapper<PaymentApp>().select("schedule", "state", "pay_image").eq("id", id));
		boolean boo = removeById(id);
		if (boo && paymentApp.getState().intValue() == State4PaymentApp.已付款.code()) {
			oAScheduleService.removeIsPay(paymentApp.getSchedule());
			if (paymentApp.getPayImage() != null)
				ossService.deleteFile(paymentApp.getPayImage());
		}
		return boo;
	}

	@Override
	public Map<Integer, BigDecimal> queryTotalPayByScheId(Collection<Integer> collection) {
		return collection.isEmpty() ? Collections.emptyMap() : 
			paymentAppMapper.totalPayByScheduleIds(Type4PaymentApp.成本金额.code(), State4PaymentApp.已付款.code(), 
					collection.stream().map(String::valueOf).collect(Collectors.joining(",")))
			.stream().collect(Collectors.toMap(IdDecimal::getId, IdDecimal::getAmount));
	}

	@Override
	public BigDecimal totalByPayAndApp(Integer schedule) {
		String states = Stream.of(State4PaymentApp.待支付.code(), State4PaymentApp.待复核.code(), State4PaymentApp.已付款.code())
				.map(String::valueOf).collect(Collectors.joining(","));
		return paymentAppMapper.totalByPayAndApp(schedule, Type4PaymentApp.成本金额.code(), states);
	}

	@Override
	public boolean existPaymentApp(Integer schedule) {
		String states = Stream.of(State4PaymentApp.待支付.code(), State4PaymentApp.待复核.code(), State4PaymentApp.已付款.code())
				.map(String::valueOf).collect(Collectors.joining(","));
		return paymentAppMapper.existPaymentApp(schedule, states) > 0 ? true : false;
	}

	@Override
	public boolean setFapiao(Integer fapiaoId, List<Integer> ids) {
		return update(new UpdateWrapper<PaymentApp>().in("id", ids).set("fapiao", fapiaoId));
	}

	@Override
	public boolean clearFapiao(Integer fapiaoId) {
		return update(new UpdateWrapper<PaymentApp>().eq("fapiao", fapiaoId).set("fapiao", null));
	}

	@Override
	public Map<Integer, String> queryPayRemarkByScheId(Collection<Integer> collection) {
		return collection.isEmpty() ? Collections.emptyMap() : 
			list(new QueryWrapper<PaymentApp>().select("schedule", "pay_remark").in("schedule", collection).isNotNull("pay_remark").ne("pay_remark", ""))
			.stream().collect(Collectors.toMap(PaymentApp::getSchedule, PaymentApp::getPayRemark, (v1,v2) -> v1 + " && " + v2));
	}

	@Override
	public List<Integer> queryOascheIdByPayRemark(String payRemark) {
		return list(new QueryWrapper<PaymentApp>().select("schedule").like("pay_remark", payRemark))
				.stream().filter(v -> v != null).map(PaymentApp::getSchedule).collect(Collectors.toList());
	}

	@Override
	public Map<Integer, Boolean> ifHaveFapiao(Collection<Integer> collection) {
		Map<Integer, Boolean> map = new HashMap<>(collection.size());
		collection.forEach(v -> map.put(v, false));
		List<Integer> list = list(new QueryWrapper<PaymentApp>().select("schedule").in("schedule", collection).eq("state", State4PaymentApp.已付款.code()).isNotNull("fapiao"))
				.stream().map(PaymentApp::getSchedule).collect(Collectors.toList());
		list.forEach(v -> map.replace(v, true));
		return map;
	}

	@Override
	public List<Integer> havaFapiao() {
		return list(new QueryWrapper<PaymentApp>().select("distinct schedule").eq("state", State4PaymentApp.已付款.code()).isNotNull("fapiao"))
				.stream().map(PaymentApp::getSchedule).collect(Collectors.toList());
	}

	@Override
	public List<Integer> noHavaFapiao() {
		return list(new QueryWrapper<PaymentApp>().select("schedule").eq("state", State4PaymentApp.已付款.code()).ne("oa_fapiao", -1).isNull("fapiao").notIn("schedule", havaFapiao())) //完全不含开票状态
				.stream().map(PaymentApp::getSchedule).collect(Collectors.toList());
	}


	@Override
	public KeyValuePair<BigDecimal, BigDecimal> totalPayByScheId(Integer scheduleId) {
		List<PaymentApp> list = list(new QueryWrapper<PaymentApp>().select("type", "amount").eq("schedule", scheduleId).eq("state", State4PaymentApp.已付款.code()));
		BigDecimal total_pay = BigDecimal.valueOf(list.stream().filter(v -> v.getType().intValue() == Type4PaymentApp.成本金额.code())
				.mapToDouble(v -> v.getAmount().doubleValue()).summaryStatistics().getSum()).setScale(2, BigDecimal.ROUND_HALF_UP);
		BigDecimal total_sell = BigDecimal.valueOf(list.stream().filter(v -> v.getType().intValue() == Type4PaymentApp.销售费用.code())
				.mapToDouble(v -> v.getAmount().doubleValue()).summaryStatistics().getSum()).setScale(2, BigDecimal.ROUND_HALF_UP);
		return new KeyValuePair<BigDecimal, BigDecimal>(total_pay, total_sell);
	}

	@Override
	public boolean autoCreate(Integer scheduleId) {
		OASchedule schedule = oAScheduleService.getOne(
				new QueryWrapper<OASchedule>().select("cost_price", "medium", "medium_name", "oa_fapiao", "oa_fapiao_type").eq("id", scheduleId));
		if (schedule.getCostPrice().doubleValue() == 0)
			return true;
		Date date = new Date();
		PaymentApp paymentApp = new PaymentApp();
		paymentApp.setSchedule(scheduleId);
		paymentApp.setType(Type4PaymentApp.成本金额.code());
		paymentApp.setPayAccount(Consts.yk_payAccount);
		paymentApp.setBankName(Consts.yk_bankName);
		paymentApp.setAccountHolder(Consts.yk_accountHolder);
		paymentApp.setAccountNumber(Consts.yk_accountNumber);
		paymentApp.setAmount(schedule.getCostPrice()); //默认全款
		paymentApp.setOaFapiao(schedule.getOaFapiao());
		paymentApp.setOaFapiaoType(schedule.getOaFapiaoType());
		paymentApp.setCreator(schedule.getMedium());
		paymentApp.setCreatorName(schedule.getMediumName());
		paymentApp.setCreateTime(date);
		paymentApp.setState(State4PaymentApp.已付款.code());
		paymentApp.setPaytime(date);
		// 生成进项发票
		if (schedule.getOaFapiao() > -1) {
			Integer fapiaoId = fapiaoService.autoGenerationBill(schedule.getCostPrice(), schedule.getOaFapiaoType(), BigDecimal.valueOf(schedule.getOaFapiao()), schedule.getMedium(), schedule.getMediumName());
			Assert.notNull(fapiaoId, "回调生成进项发票失败");
			paymentApp.setFapiao(fapiaoId);
		}
		return save(paymentApp);
	}

}