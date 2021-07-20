package com.ykhd.office.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import com.ykhd.office.domain.bean.LoginCache;
import com.ykhd.office.domain.bean.OAScheduleInfo2;
import com.ykhd.office.domain.bean.common.PageHelpers;
import com.ykhd.office.domain.entity.JcOASReturn;
import com.ykhd.office.domain.entity.JcOASchedule;
import com.ykhd.office.domain.req.OASReturnCondition;
import com.ykhd.office.domain.req.OASReturnSubmit;
import com.ykhd.office.domain.req.OASReturnSubmit.ScheduleAmount;
import com.ykhd.office.domain.resp.OASReturnDto;
import com.ykhd.office.domain.resp.OASReturn_Excel;
import com.ykhd.office.repository.JcOASReturnMapper;
import com.ykhd.office.service.IJcFapiaoService;
import com.ykhd.office.service.IJcOASReturnService;
import com.ykhd.office.service.IJcOAScheduleService;
import com.ykhd.office.util.DateUtil;
import com.ykhd.office.util.JacksonHelper;
import com.ykhd.office.util.dictionary.Consts;
import com.ykhd.office.util.dictionary.StateEnums.State4AppCancel;
import com.ykhd.office.util.dictionary.StateEnums.State4OASReturn;
import com.ykhd.office.util.dictionary.StateEnums.State4OASchedule;
import com.ykhd.office.util.dictionary.SystemEnums.OSSFolder;
import com.ykhd.office.util.dictionary.TypeEnums.Type4ApprovalMsg;

import cn.gjing.tools.excel.ExcelFactory;

@Service
public class JcOASReturnService extends ServiceImpl<BaseMapper<JcOASReturn>, JcOASReturn> implements IJcOASReturnService {

	@Autowired
	private JcOASReturnMapper jcOASReturnMapper;
	@Autowired
	private IJcOAScheduleService jcOAScheduleService;
	@Autowired
	private OssService ossService;
	@Autowired
	private ApprovalMsgService approvalMsgService;
	@Autowired
	private IJcFapiaoService jcFapiaoService;

	@Override
	public boolean autoCreate(JcOASchedule schedule) {
		Date date = new Date();
		LoginCache managerInfo = BaseController.getManagerInfo();
		JcOASReturn _return = new JcOASReturn();
		_return.setSchedule(schedule.getId());
		_return.setAmount(schedule.getExecutePrice());
		_return.setReceiveAccount(Consts.yk_payAccount);
		_return.setBankName(Consts.yk_bankName);
		_return.setAccountHolder(Consts.yk_accountHolder);
		_return.setAccountNumber(Consts.yk_accountNumber);
		_return.setCreateTime(date);
		_return.setReturnTime(date);
		_return.setState(State4OASReturn.已回款.code());
		_return.setCustFapiao(schedule.getCustFapiao());
		// 生成销项发票
		if (schedule.getCustFapiao() > -1) {
			Integer fapiaoId = jcFapiaoService.autoGenerationBill(schedule.getExecutePrice(), 0, BigDecimal.valueOf(schedule.getCustFapiao()), managerInfo.getId(), managerInfo.getName());
			Assert.notNull(fapiaoId, "自动生成销项发票失败");
			_return.setFapiao(fapiaoId);
		}
		return save(_return);
	}
	
	@Override
	public PageHelpers<OASReturnDto> getListByPage(OASReturnCondition condition) {
		QueryWrapper<JcOASReturn> queryWrapper = new QueryWrapper<>();
		if (condition.getState() != null)
			queryWrapper.eq("state", condition.getState());
		if (condition.getSchedule() != null)
			queryWrapper.eq("schedule", condition.getSchedule());
		if (condition.getScheduler() != null) {
			List<Integer> schedule = jcOAScheduleService.queryIdByCreator(condition.getScheduler());
			if (schedule.isEmpty())
				return new PageHelpers<OASReturnDto>(condition.getPage(), condition.getSize(), 0, Collections.emptyList(), Collections.emptyMap());
			queryWrapper.in("schedule", schedule);
		}
		if (StringUtils.hasText(condition.getOaName())) {
			List<Integer> schedule = jcOAScheduleService.queryIdByOAName(condition.getOaName().trim());
			if (schedule.isEmpty())
				return new PageHelpers<OASReturnDto>(condition.getPage(), condition.getSize(), 0, Collections.emptyList(), Collections.emptyMap());
			queryWrapper.in("schedule", schedule);
		}
		if (StringUtils.hasText(condition.getCustomer())) {
			List<Integer> schedule = jcOAScheduleService.queryIdByCustomer(condition.getCustomer().trim());
			if (schedule.isEmpty())
				return new PageHelpers<OASReturnDto>(condition.getPage(), condition.getSize(), 0, Collections.emptyList(), Collections.emptyMap());
			queryWrapper.in("schedule", schedule);
		}
		if (StringUtils.hasText(condition.getAccountHolder()))
			queryWrapper.like("account_holder", condition.getAccountHolder().trim());
		if (condition.getDateType() != null) {
			String field = condition.getDateType() == 1 ? "create_time" : "return_time";
			if (StringUtils.hasText(condition.getStartTime()))
				queryWrapper.ge(field, condition.getStartTime());
			if (StringUtils.hasText(condition.getEndTime()))
				queryWrapper.lt(field, DateUtil.nextDay(condition.getEndTime()));
		}
		if (condition.getMakeFapiao() != null) {
			if (condition.getMakeFapiao() == 0) //已开票
				queryWrapper.isNotNull("fapiao");
			else
				queryWrapper.ne("cust_fapiao", -1).isNull("fapiao");
		}
		// 【查询限制】
//		LoginCache managerInfo = BaseController.getManagerInfo();
//		RoleSign sign = roleService.judgeRole(managerInfo.getRole());
//		if (sign == RoleSign.ae) //AE只能看到自己申请的回款
//			queryWrapper.eq("creator", managerInfo.getId());
		IPage<JcOASReturn> iPage = page(new Page<>(condition.getPage(), condition.getSize()), queryWrapper.orderByDesc("id"));
		List<JcOASReturn> records = iPage.getRecords();
		// 查询排期人、投放时间、公众号名称、客户公司、客户品牌
		Map<Integer, OAScheduleInfo2> info_map = jcOAScheduleService.info4OASReturnList(records.stream().map(JcOASReturn::getSchedule).collect(Collectors.toSet()));
		List<OASReturnDto> list = records.stream().flatMap(v -> {
			OASReturnDto dto = new OASReturnDto();
			BeanUtils.copyProperties(v, dto);
			OAScheduleInfo2 info = info_map.get(dto.getSchedule());
			if (info != null)
				BeanUtils.copyProperties(info, dto, "id");
			return Stream.of(dto);
		}).collect(Collectors.toList());
		// 计算汇总数据
		Map<String, Object> summary_map = new HashMap<>();
		List<JcOASReturn> returnList = list(queryWrapper.select("amount"));
		summary_map.put("total_return", 
				BigDecimal.valueOf(returnList.stream().filter(v -> v != null).mapToDouble(v -> v.getAmount().doubleValue()).summaryStatistics().getSum()).setScale(2, BigDecimal.ROUND_HALF_UP));
		return new PageHelpers<OASReturnDto>(condition.getPage(), condition.getSize(), iPage.getTotal(), list, summary_map);
	}

	@Override
	public void exportExcel(OASReturnCondition condition, HttpServletResponse response) {
		QueryWrapper<JcOASReturn> queryWrapper = new QueryWrapper<>();
		if (condition.getState() != null)
			queryWrapper.eq("state", condition.getState());
		if (condition.getSchedule() != null)
			queryWrapper.eq("schedule", condition.getSchedule());
		if (condition.getScheduler() != null) {
			List<Integer> schedule = jcOAScheduleService.queryIdByCreator(condition.getScheduler());
			if (schedule.isEmpty()) {
				ExcelFactory.createWriter(OASReturn_Excel.class, response).write(null).flush();
				return;
			}
			queryWrapper.in("schedule", schedule);
		}
		if (StringUtils.hasText(condition.getOaName())) {
			List<Integer> schedule = jcOAScheduleService.queryIdByOAName(condition.getOaName().trim());
			if (schedule.isEmpty()) {
				ExcelFactory.createWriter(OASReturn_Excel.class, response).write(null).flush();
				return;
			}
			queryWrapper.in("schedule", schedule);
		}
		if (StringUtils.hasText(condition.getCustomer())) {
			List<Integer> schedule = jcOAScheduleService.queryIdByCustomer(condition.getCustomer().trim());
			if (schedule.isEmpty()) {
				ExcelFactory.createWriter(OASReturn_Excel.class, response).write(null).flush();
				return;
			}
			queryWrapper.in("schedule", schedule);
		}
		if (StringUtils.hasText(condition.getAccountHolder()))
			queryWrapper.like("account_holder", condition.getAccountHolder().trim());
		if (condition.getDateType() != null) {
			String field = condition.getDateType() == 1 ? "create_time" : "return_time";
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
		if (condition.getMakeFapiao() != null) {
			if (condition.getMakeFapiao() == 0) //已开票
				queryWrapper.isNotNull("fapiao");
			else
				queryWrapper.ne("cust_fapiao", -1).isNull("fapiao");
		}
		// 【查询限制】
//		LoginCache managerInfo = BaseController.getManagerInfo();
//		RoleSign sign = roleService.judgeRole(managerInfo.getRole());
//		if (sign == RoleSign.ae) //AE只能看到自己申请的回款
//			queryWrapper.eq("creator", managerInfo.getId());
		List<JcOASReturn> records = list(queryWrapper.orderByAsc("id"));
		// 查询排期人、投放时间、公众号名称、客户公司、客户品牌
		Map<Integer, OAScheduleInfo2> info_map = jcOAScheduleService.info4OASReturnList(records.stream().map(JcOASReturn::getSchedule).collect(Collectors.toSet()));
		List<OASReturn_Excel> list = records.stream().flatMap(v -> {
			OASReturn_Excel dto = new OASReturn_Excel();
			BeanUtils.copyProperties(v, dto);
			OAScheduleInfo2 info = info_map.get(dto.getSchedule());
			if (info != null)
				BeanUtils.copyProperties(info, dto, "id");
			return Stream.of(dto);
		}).collect(Collectors.toList());
		ExcelFactory.createWriter(OASReturn_Excel.class, response).write(list).flush();
	}

	@Override
	public List<BigDecimal> totalReturn(List<Integer> schedule) {
		List<JcOASReturn> list = list(new QueryWrapper<JcOASReturn>().select("schedule", "amount").in("schedule", schedule)
				.in("state", State4OASReturn.待确认.code(), State4OASReturn.待复核.code(), State4OASReturn.已回款.code()));
		Map<Integer, BigDecimal> map = list.stream().collect(Collectors.toMap(JcOASReturn::getSchedule, JcOASReturn::getAmount, (v1, v2) -> v1.add(v2)));
		return schedule.stream().flatMap(v -> Stream.of(map.containsKey(v) ? map.get(v) : BigDecimal.ZERO)).collect(Collectors.toList());
	}
	
	/**
	 * 排期对应的回款申请金额
	 */
	private Map<Integer, BigDecimal> totalReturnMap(List<Integer> schedule) {
		List<JcOASReturn> list = list(new QueryWrapper<JcOASReturn>().select("schedule", "amount").in("schedule", schedule)
				.in("state", State4OASReturn.待确认.code(), State4OASReturn.待复核.code(), State4OASReturn.已回款.code()));
		Map<Integer, BigDecimal> map2 = list.stream().collect(Collectors.toMap(JcOASReturn::getSchedule, JcOASReturn::getAmount, (v1, v2) -> v1.add(v2)));
		Map<Integer, BigDecimal> map = new HashMap<>(schedule.size());
		schedule.forEach(v -> map.put(v, BigDecimal.ZERO));
		map2.entrySet().forEach(v -> map.replace(v.getKey(), v.getValue()));
		return map;
	}
	
	@Override
	public BigDecimal totalReturnFinish(Integer schedule) {
		List<JcOASReturn> list = list(new QueryWrapper<JcOASReturn>().select("amount").eq("schedule", schedule)
				.eq("state", State4OASReturn.已回款.code()));
		return BigDecimal.valueOf(list.stream().mapToDouble(v -> v.getAmount().doubleValue()).summaryStatistics().getSum()).setScale(2, BigDecimal.ROUND_HALF_UP);
	}

	@Override
	public boolean addOASReturn(OASReturnSubmit submit) {
		List<ScheduleAmount> objList = JacksonHelper.toParseObjArray(submit.getSchedule_amount(), ScheduleAmount.class);
		List<Integer> schedIds = objList.stream().map(ScheduleAmount::getSchedule).collect(Collectors.toList());
		List<JcOASchedule> scheduleList = jcOAScheduleService.list(new QueryWrapper<JcOASchedule>().select("id", "app_cancel", "execute_price", "cust_fapiao")
				.in("id", schedIds).eq("state", State4OASchedule.待结算.code()).isNull("is_return_money"));
		Assert.state(schedIds.size() == scheduleList.size(), "包含非待结算或已回款的排期！");
		Assert.state(!scheduleList.stream().filter(v -> v.getAppCancel()!=null && 
				(v.getAppCancel() == State4AppCancel.待主管审核.code() || v.getAppCancel() == State4AppCancel.待媒介审核.code())).findAny().isPresent(), "包含作废流程中的排期");
//		Assert.state(!editRecordService.existsOngoing(Type4Edit.排期, schedIds), "包含修改流程中的排期");
		Map<Integer, BigDecimal> totalReturnMap = totalReturnMap(schedIds);
		Map<Integer, JcOASchedule> map = scheduleList.stream().collect(Collectors.toMap(JcOASchedule::getId, v -> v));
		List<JcOASReturn> returnList = new ArrayList<JcOASReturn>(objList.size());
		Integer managerId = BaseController.getManagerInfo().getId();
		Date date = new Date();
		StringBuilder sb = new StringBuilder();
		BigDecimal sum = BigDecimal.valueOf(objList.stream().mapToDouble(v -> v.getAmount().doubleValue()).summaryStatistics().getSum()).setScale(2, BigDecimal.ROUND_HALF_UP);
		JcOASchedule schedule;
		for (ScheduleAmount obj : objList) {
			schedule = map.get(obj.getSchedule());
			Assert.state(obj.getAmount().add(totalReturnMap.get(obj.getSchedule())).compareTo(schedule.getExecutePrice()) <= 0, obj.getSchedule() + "总回款已超过排期报价");
			JcOASReturn oasReturn = new JcOASReturn();
			BeanUtils.copyProperties(submit, oasReturn);
			oasReturn.setSchedule(obj.getSchedule());
			oasReturn.setAmount(obj.getAmount());
			oasReturn.setCreator(managerId);
			oasReturn.setCreateTime(date);
			oasReturn.setState(State4OASReturn.待确认.code());
			if (objList.size() > 1)
				oasReturn.setRemark(sb.append(sum).append("（").append(obj.getAmount()).append("）").append("&& ").append(submit.getRemark()).toString());
			oasReturn.setCustFapiao(schedule.getCustFapiao());
			returnList.add(oasReturn);
			sb.setLength(0);
		}
		boolean boo = saveBatch(returnList);
		if (boo)
			approvalMsgService.sendApprovalMsg(Type4ApprovalMsg.集采排期回款待确认, null);
		return boo;
	}

	@Override
	public int confirm(List<Integer> id, MultipartFile[] files, String date, String remark) {
		String imgPath = null;
		if (files != null && files.length > 0)
			imgPath = JacksonHelper.toJsonStr(ossService.uploadFiles(OSSFolder.BMS.name(), files));
		Date return_time = DateUtil.yyyyMMdd2date(date);
		UpdateWrapper<JcOASReturn> updateWrapper;
		int success = 0;
		for (Integer _id : id) {
			updateWrapper = new UpdateWrapper<JcOASReturn>().eq("id", _id).eq("state", State4OASReturn.待确认.code());
			updateWrapper.set("state", State4OASReturn.待复核.code()).set("return_time", return_time)
				.set("return_remark", remark).set("return_image", imgPath);
			success += jcOASReturnMapper.update(null, updateWrapper);
		}
		Assert.state(success > 0, "仅限待确认状态");
		approvalMsgService.sendApprovalMsg(Type4ApprovalMsg.集采排期回款待确认, null);
		approvalMsgService.sendApprovalMsg(Type4ApprovalMsg.集采排期回款待复核, null);
		return success;
	}

	@Override
	public int refuse(List<Integer> id) {
		UpdateWrapper<JcOASReturn> updateWrapper;
		int success = 0;
		for (Integer _id : id) {
			updateWrapper = new UpdateWrapper<JcOASReturn>()
					.eq("id", _id).eq("state", State4OASReturn.待确认.code()).isNull("fapiao")
					.set("state", State4OASReturn.驳回.code());
			success += jcOASReturnMapper.update(null, updateWrapper);
		}
		Assert.state(success > 0, "仅限待确认状态且未开销项票");
		approvalMsgService.sendApprovalMsg(Type4ApprovalMsg.集采排期回款待确认, null);
		return success;
	}

	@Override
	@Transactional
	public boolean check(Integer id, List<String> oldPath, MultipartFile[] files, String date, String remark) {
		Date return_time = DateUtil.yyyyMMdd2date(date);
		JcOASReturn oasReturn = getOne(new QueryWrapper<JcOASReturn>().select("id", "schedule", "state").eq("id", id));
		Assert.state(oasReturn != null && oasReturn.getState() == State4OASReturn.待复核.code(), "仅限待复核状态");
		if (oldPath == null)
			oldPath = new ArrayList<>();
		if (files != null && files.length > 0)
			oldPath.addAll(ossService.uploadFiles(OSSFolder.BMS.name(), files));
		Assert.state(oldPath.size() > 0, "未上传回款截图");
		UpdateWrapper<JcOASReturn> updateWrapper = new UpdateWrapper<JcOASReturn>().eq("id", id);
		updateWrapper.set("state", State4OASReturn.已回款.code()).set("return_time", return_time).set("return_remark", remark)
			.set("return_image", JacksonHelper.toJsonStr(oldPath));
		boolean boo = update(updateWrapper);
		if (boo) {
			jcOAScheduleService.checkReturnFinish(oasReturn.getSchedule(), totalReturnFinish(oasReturn.getSchedule()), return_time);
			approvalMsgService.sendApprovalMsg(Type4ApprovalMsg.集采排期回款待复核, BaseController.getManagerInfo().getId());
		}
		return boo;
	}

	@Override
	@Transactional
	public boolean checkBatch(List<Integer> id, MultipartFile[] files, String date) {
		UpdateWrapper<JcOASReturn> updateWrapper = new UpdateWrapper<JcOASReturn>().in("id", id).eq("state", State4OASReturn.待复核.code()).set("state", State4OASReturn.已回款.code());
		if (files != null && files.length > 0)
			updateWrapper.set("return_image", JacksonHelper.toJsonStr(ossService.uploadFiles(OSSFolder.BMS.name(), files)));
		if (StringUtils.hasText(date))
			updateWrapper.set("return_time", DateUtil.yyyyMMdd2date(date));
		boolean boo = update(updateWrapper);
		if (boo) {
			Map<Integer, Date> map = list(new QueryWrapper<JcOASReturn>().select("schedule", "return_time").in("id", id)).stream().collect(Collectors.toMap(JcOASReturn::getSchedule, JcOASReturn::getReturnTime, (v1,v2) -> v2));
			map.entrySet().forEach(v -> jcOAScheduleService.checkReturnFinish(v.getKey(), totalReturnFinish(v.getKey()), v.getValue()));
			approvalMsgService.sendApprovalMsg(Type4ApprovalMsg.集采排期回款待复核, BaseController.getManagerInfo().getId());
		}
		return boo;
	}

	@Override
	public int checkFailure(List<Integer> id, String reason) {
		UpdateWrapper<JcOASReturn> updateWrapper;
		int success = 0;
		for (Integer _id : id) {
			updateWrapper = new UpdateWrapper<JcOASReturn>()
					.eq("id", _id).eq("state", State4OASReturn.待复核.code())
					.set("state", State4OASReturn.待确认.code()).set("reason", reason);
			success += jcOASReturnMapper.update(null, updateWrapper);
		}
		Assert.state(success > 0, "仅限待复核状态");
		approvalMsgService.sendApprovalMsg(Type4ApprovalMsg.集采排期回款待确认, null);
		approvalMsgService.sendApprovalMsg(Type4ApprovalMsg.集采排期回款待复核, BaseController.getManagerInfo().getId());
		return success;
	}

	@Override
	public Map<Integer, BigDecimal> queryTotalReturnByScheId(Collection<Integer> collection) {
		return collection.isEmpty() ? Collections.emptyMap() : 
			list(new QueryWrapper<JcOASReturn>().select("schedule", "amount").in("schedule", collection).eq("state", State4OASReturn.已回款.code()))
			.stream().collect(Collectors.toMap(JcOASReturn::getSchedule, v -> v.getAmount() != null ? v.getAmount() : BigDecimal.ZERO, (v1, v2) -> v1.add(v2)));
	}

	@Override
	public Map<Integer, String> queryReturnRemarkByScheId(Collection<Integer> collection) {
		return collection.isEmpty() ? Collections.emptyMap() : 
			list(new QueryWrapper<JcOASReturn>().select("schedule", "return_remark").in("schedule", collection).isNotNull("return_remark").ne("return_remark", ""))
			.stream().collect(Collectors.toMap(JcOASReturn::getSchedule, JcOASReturn::getReturnRemark, (v1,v2) -> v1 + " && " + v2));
	}

	@Override
	public List<Integer> queryOascheIdByReturnRemark(String returnRemark) {
		return list(new QueryWrapper<JcOASReturn>().select("schedule").like("return_remark", returnRemark))
				.stream().filter(v -> v != null).map(JcOASReturn::getSchedule).collect(Collectors.toList());
	}

	@Override
	public boolean setFapiao(Integer fapiaoId, List<Integer> ids) {
		return update(new UpdateWrapper<JcOASReturn>().in("id", ids).set("fapiao", fapiaoId));
	}

	@Override
	public boolean clearFapiao(List<Integer> fapiaoIds) {
		return update(new UpdateWrapper<JcOASReturn>().in("fapiao", fapiaoIds).set("fapiao", null));
	}

	@Override
	public Map<Integer, Boolean> ifMakeFapiao(Collection<Integer> collection) {
		Map<Integer, Boolean> map = new HashMap<>(collection.size());
		collection.forEach(v -> map.put(v, false));
		List<Integer> list = list(new QueryWrapper<JcOASReturn>().select("schedule").in("schedule", collection).isNotNull("fapiao"))
				.stream().map(JcOASReturn::getSchedule).collect(Collectors.toList());
		list.forEach(v -> map.replace(v, true));
		return map;
	}

	@Override
	public List<Integer> makeFapiao() {
		return list(new QueryWrapper<JcOASReturn>().select("distinct schedule").ne("state", State4OASReturn.驳回.code()).ne("cust_fapiao", -1).isNotNull("fapiao"))
				.stream().map(JcOASReturn::getSchedule).collect(Collectors.toList());
	}

	@Override
	public List<Integer> noMakeFapiao() {
		List<Integer> list = jcOAScheduleService.list(new QueryWrapper<JcOASchedule>().select("id").in("state", State4OASchedule.待结算.code(), State4OASchedule.已完成.code()).ne("cust_fapiao", -1))
				.stream().map(JcOASchedule::getId).collect(Collectors.toList());
		list.removeAll(makeFapiao());
		return list;
	}

}
