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

import com.ykhd.office.domain.bean.*;
import com.ykhd.office.domain.entity.Manager;
import com.ykhd.office.repository.ManagerMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ykhd.office.controller.BaseController;
import com.ykhd.office.domain.bean.common.KeyValuePair;
import com.ykhd.office.domain.bean.common.PageCondition;
import com.ykhd.office.domain.bean.common.PageHelper;
import com.ykhd.office.domain.bean.common.PageHelpers;
import com.ykhd.office.domain.entity.Department;
import com.ykhd.office.domain.entity.OASchedule;
import com.ykhd.office.domain.entity.PaymentApp;
import com.ykhd.office.domain.req.OAScheduleCondition;
import com.ykhd.office.domain.req.OAScheduleCondition2;
import com.ykhd.office.domain.req.OAScheduleEdit;
import com.ykhd.office.domain.req.OAScheduleSubmit;
import com.ykhd.office.domain.resp.OAScheduleDto;
import com.ykhd.office.domain.resp.OAScheduleDto2;
import com.ykhd.office.domain.resp.OAScheduleDto4jc;
import com.ykhd.office.domain.resp.OASchedule_Excel;
import com.ykhd.office.repository.OAScheduleMapper;
import com.ykhd.office.service.ICustomerService;
import com.ykhd.office.service.IDepartmentService;
import com.ykhd.office.service.IEditRecordService;
import com.ykhd.office.service.IManagerService;
import com.ykhd.office.service.IOASReturnService;
import com.ykhd.office.service.IOAScheduleService;
import com.ykhd.office.service.IOfficeAccountService;
import com.ykhd.office.service.IPaymentAppService;
import com.ykhd.office.service.IRoleService;
import com.ykhd.office.util.DateUtil;
import com.ykhd.office.util.MathUtil;
import com.ykhd.office.util.dictionary.StateEnums.State4AppCancel;
import com.ykhd.office.util.dictionary.StateEnums.State4OASReturn;
import com.ykhd.office.util.dictionary.StateEnums.State4OASchedule;
import com.ykhd.office.util.dictionary.StateEnums.State4PaymentApp;
import com.ykhd.office.util.dictionary.SystemEnums.RoleSign;
import com.ykhd.office.util.dictionary.TypeEnums.Type4ApprovalMsg;
import com.ykhd.office.util.dictionary.TypeEnums.Type4Edit;

import cn.gjing.tools.excel.ExcelFactory;

@Service
public class OAScheduleService extends ServiceImpl<BaseMapper<OASchedule>, OASchedule> implements IOAScheduleService {

	@Autowired
	private OAScheduleMapper oAScheduleMapper;
	@Autowired
	private IOfficeAccountService officeAccountService;
	@Autowired
	private IEditRecordService editRecordService;
	@Autowired
	private IManagerService managerService;
	@Autowired
	private ICustomerService customerService;
	@Autowired
	private IPaymentAppService paymentAppService;
	@Autowired
	private IRoleService roleService;
	@Autowired
	private ApprovalMsgService approvalMsgService;
	@Autowired
	private IDepartmentService departmentService;
	@Autowired
	private IOASReturnService oasReturnService;

	
	@Override
	public PageHelpers<OAScheduleDto> getListByPage(OAScheduleCondition condition) {
		List<OAScheduleDto> list;
		Map<String, Object> summary_map;
		QueryWrapper<OASchedule> queryWrapper = new QueryWrapper<>();
		// 【查询限制】
		LoginCache managerInfo = BaseController.getManagerInfo();
		RoleSign sign = roleService.judgeRole(managerInfo.getRole());
		if (sign == RoleSign.ae) // 若是AE则只能看到自己创建的排期
			queryWrapper.eq("creator", managerInfo.getId());
		else if (sign == RoleSign.medium) // 若是媒介则只能看到自己跟进的排期
			queryWrapper.eq("medium", managerInfo.getId());
		else if (sign == RoleSign.group) { // 若是广告部组长则只能看到自己部门里AE及自己创建的排期
			List<Integer> ids = managerService.queryIdsByDeptIdRoleName(managerInfo.getDepartment(), "AE");
			ids.add(managerInfo.getId());
			queryWrapper.in("creator", ids);
		} else if (sign == RoleSign.dept) { // 若是广告部主管则能看到自己创建的及部门里AE创建的排期、各个组里AE及组长本人的排期
			List<Integer> ids = new ArrayList<>();
			List<Integer> deptList = departmentService.subcollection(departmentService.departmentTree(managerInfo.getDepartment()), null);
			for (Integer dept : deptList) {
				ids.addAll(managerService.queryIdsByDeptIdRoleName(dept, "AE"));
				ids.add(departmentService.getOne(new QueryWrapper<Department>().select("leader").eq("id", dept)).getLeader()); //主管或组长也能建排期
			}
			queryWrapper.in("creator", ids);
		}
		if (condition.getCode() != null)
			queryWrapper.eq("id", condition.getCode());
		if (condition.getState() != null)
			queryWrapper.eq("state", condition.getState());
		else // 默认排除（已作废，不通过，创建）
			queryWrapper.notIn("state", State4OASchedule.作废.code(), State4OASchedule.审核不通过.code(), State4OASchedule.创建.code());
		if (condition.getMedium() != null)
			queryWrapper.eq("medium", condition.getMedium());
		if (condition.getCreator() != null)
			queryWrapper.eq("creator", condition.getCreator());
		if (condition.getIsPay() != null) {
			if (condition.getIsPay() == 1)
				queryWrapper.eq("is_pay", 1);
			else 
				queryWrapper.isNull("is_pay");
		}
		if (condition.getIsReturnMoney() != null) {
			if (condition.getIsReturnMoney() == 1)
				queryWrapper.eq("is_return_money", 1);
			else if (condition.getIsReturnMoney() == 0)
				queryWrapper.isNull("is_return_money");
			else if (condition.getIsReturnMoney() == 2) { // 部分回款
				queryWrapper.isNull("is_return_money")
					.apply("id in(select distinct schedule from wm_oas_return where state = {0} and wm_oa_schedule.id = schedule)", State4OASReturn.已回款.code());
			}
		}
		if (StringUtils.hasText(condition.getCustomerName())) {
			List<Integer> cusIds = customerService.queryIdsBybrand(condition.getCustomerName());
			if (cusIds.isEmpty())
				return new PageHelpers<OAScheduleDto>(condition.getPage(), condition.getSize(), 0, Collections.emptyList(), Collections.emptyMap());
			queryWrapper.in("customer", cusIds);
		}
		if (StringUtils.hasText(condition.getCompany())) {
			List<Integer> cusIds = customerService.queryIdsByCompany(condition.getCompany());
			if (cusIds.isEmpty())
				return new PageHelpers<OAScheduleDto>(condition.getPage(), condition.getSize(), 0, Collections.emptyList(), Collections.emptyMap());
			queryWrapper.in("customer", cusIds);
		}
		if(StringUtils.hasText(condition.getBusiness())){
			QueryWrapper<Manager> wrapper = new QueryWrapper<>();
			wrapper.select("id").eq("name",condition.getBusiness());
			Manager manager = managerService.getOne(wrapper);
			if(manager!=null){
				List<Integer> cusIds = customerService.queryIdsByBusiness(manager.getId());
				if (cusIds.isEmpty())
					return new PageHelpers<OAScheduleDto>(condition.getPage(), condition.getSize(), 0, Collections.emptyList(), Collections.emptyMap());
				queryWrapper.in("customer", cusIds);
			}
		}
		if(StringUtils.hasText(condition.getReviewername())){
			QueryWrapper<Manager> wrapper = new QueryWrapper<>();
			wrapper.select("id").eq("name",condition.getReviewername());
			Manager manager = managerService.getOne(wrapper);
			if(manager!=null){
				queryWrapper.eq("reviewer",manager.getId());
			}
		}
		if (StringUtils.hasText(condition.getOaName())) {
			List<Integer> oaIds = officeAccountService.queryIdsByName(condition.getOaName().trim());
			if (oaIds.isEmpty())
				return new PageHelpers<OAScheduleDto>(condition.getPage(), condition.getSize(), 0, Collections.emptyList(), Collections.emptyMap());
			queryWrapper.in("office_account", oaIds);
		}
		if (StringUtils.hasText(condition.getRemark())) {
			List<Integer> scheduleIds = paymentAppService.queryOascheIdByPayRemark(condition.getRemark().trim());
			List<Integer> scheduleIds2 = oasReturnService.queryOascheIdByReturnRemark(condition.getRemark().trim());
			scheduleIds.addAll(scheduleIds2);
			List<Integer> all = scheduleIds.stream().distinct().collect(Collectors.toList());
			if (all.isEmpty())
				queryWrapper.like("remark", condition.getRemark().trim());
			else
				queryWrapper.and(wrap -> wrap.like("remark", condition.getRemark()).or().in("id", all));
		}
		if (condition.getAdvancePay() != null) {
			if (condition.getAdvancePay() == 1) { // 超期回款 或 超期未回款
				// put_date 与 return_time 两个日期都是yyyy-MM-dd ，不需要进行date_format(put_date,'%Y-%m-%d')转换。
				queryWrapper.apply("((execute_price != 0 and return_time is null and put_date < {0}) or (return_time is not null and put_date < return_time))", DateUtil.date2yyyyMMdd(new Date()));
			} else {  // 未到期  或  提前、当天已回款
				queryWrapper.apply("(execute_price = 0 or put_date > {0} or (return_time is not null and put_date >= return_time))", new Date());
			}
		}
		// 作废申请列表页面使用
		if (condition.getAppCancel() != null) {
			if (condition.getAppCancel() == 0)
				queryWrapper.isNotNull("app_cancel");
			else
				queryWrapper.eq("app_cancel", condition.getAppCancel());
		}
		if (condition.getDateType() != null) {
			Integer date_type = condition.getDateType();
			if (date_type == 4) { //付款时间
				QueryWrapper<PaymentApp> payQuery = new QueryWrapper<>();
				payQuery.select("distinct schedule").in("state", State4PaymentApp.待复核.code(), State4PaymentApp.已付款.code());
				if (StringUtils.hasText(condition.getStartTime())) {
					payQuery.ge("paytime", condition.getStartTime());
				}
				if (StringUtils.hasText(condition.getEndTime())) {
					payQuery.lt("paytime", DateUtil.nextDay(condition.getEndTime()));
				}
				List<Integer> scheIds = paymentAppService.list(payQuery).stream().map(v -> v.getSchedule()).collect(Collectors.toList());
				if (scheIds.isEmpty())
					return new PageHelpers<OAScheduleDto>(condition.getPage(), condition.getSize(), 0, Collections.emptyList(), Collections.emptyMap());
				else
					queryWrapper.in("id", scheIds);
			} else {
				String field = date_type == 1 ? "create_time" : date_type == 2 ? "put_date" : "return_time"; //创建、投放、回款时间
				if (StringUtils.hasText(condition.getStartTime())) {
					queryWrapper.ge(field, condition.getStartTime());
				}
				if (StringUtils.hasText(condition.getEndTime())) {
					queryWrapper.lt(field, DateUtil.nextDay(condition.getEndTime()));
				}
			}
		}
		if (condition.getPriceType() != null) {
			String field = condition.getPriceType() == 1 ? "execute_price" : "cost_price"; //执行价、成本价
			if (condition.getPrice_lower() != null)
				queryWrapper.ge(field, condition.getPrice_lower());
			if (condition.getPrice_upper() != null)
				queryWrapper.le(field, condition.getPrice_upper());
		}
		if (condition.getHaveOaFapiao() != null) {
			if (condition.getHaveOaFapiao() == 0) { //已开进项票
				List<Integer> scheIds = paymentAppService.havaFapiao();
				if (scheIds.isEmpty())
					return new PageHelpers<OAScheduleDto>(condition.getPage(), condition.getSize(), 0, Collections.emptyList(), Collections.emptyMap());
				queryWrapper.in("id", scheIds);
			} else if (condition.getHaveOaFapiao() == 1) { //已支付未开进项票
				List<Integer> scheIds = paymentAppService.noHavaFapiao();
				if (scheIds.isEmpty())
					return new PageHelpers<OAScheduleDto>(condition.getPage(), condition.getSize(), 0, Collections.emptyList(), Collections.emptyMap());
				queryWrapper.in("id", scheIds);
			}
		}
		if (condition.getHaveCustFapiao() != null) {
			if (condition.getHaveCustFapiao() == 0) { //已开销项票
				List<Integer> scheIds = oasReturnService.makeFapiao();
				if (scheIds.isEmpty())
					return new PageHelpers<OAScheduleDto>(condition.getPage(), condition.getSize(), 0, Collections.emptyList(), Collections.emptyMap());
				queryWrapper.in("id", scheIds);
			} else if (condition.getHaveCustFapiao() == 1) { //未开销项票
				List<Integer> scheIds = oasReturnService.noMakeFapiao();
				if (scheIds.isEmpty())
					return new PageHelpers<OAScheduleDto>(condition.getPage(), condition.getSize(), 0, Collections.emptyList(), Collections.emptyMap());
				queryWrapper.in("id", scheIds);
			}
		}
		if (condition.getAdvantage() != null)
			queryWrapper.eq("advantage", 1);
		// 战队、小组查询（媒介、AE、组长 不可查）
		if (condition.getTeam() != null) {
			Assert.state(sign != RoleSign.ae && sign != RoleSign.medium && sign != RoleSign.group, "无权查询");
			List<Integer> AE_ids = null;
			List<Integer> deptList = departmentService.subcollection(departmentService.departmentTree(condition.getTeam()), null);
			if (deptList.size() == 1) { // 小组 或 无小组的战队
				AE_ids = managerService.queryIdsByDeptIdRoleName(condition.getTeam(), "AE");
				AE_ids.add(departmentService.getOne(new QueryWrapper<Department>().select("leader").eq("id", condition.getTeam())).getLeader()); //主管或组长也能建排期
			} else { // 有小组的战队
				AE_ids = new ArrayList<>();
				for (Integer dept : deptList) {
					AE_ids.addAll(managerService.queryIdsByDeptIdRoleName(dept, "AE"));
					AE_ids.add(departmentService.getOne(new QueryWrapper<Department>().select("leader").eq("id", dept)).getLeader()); //主管或组长也能建排期
				}
			}
			if (AE_ids.isEmpty())
				return new PageHelpers<OAScheduleDto>(condition.getPage(), condition.getSize(), 0, Collections.emptyList(), Collections.emptyMap());
			queryWrapper.in("creator", AE_ids);
		}
		// 排序
		if (StringUtils.hasText(condition.getSort())) {
			if ("p".equals(condition.getSort())) // 投放日期 升序
				queryWrapper.orderByAsc("put_date");
			else if ("pd".equals(condition.getSort())) // 投放日期 降序
				queryWrapper.orderByDesc("put_date");
		} else
			queryWrapper.orderByDesc("id");
		IPage<OASchedule> iPage = page(new Page<>(condition.getPage(), condition.getSize()), queryWrapper);
		List<OASchedule> records = iPage.getRecords();
		if (records.isEmpty()) {
			list = Collections.emptyList();
			summary_map = Collections.emptyMap();
		} else {
			list = new ArrayList<>(records.size());
			records.forEach(v -> {
				OAScheduleDto dto = new OAScheduleDto();
				BeanUtils.copyProperties(v, dto);
				dto.setCustomerName(v.getBrand()); //兼顾旧版客户品牌名称
				QueryWrapper<Manager> wrapper = new QueryWrapper<>();
				wrapper.select("name").eq("id",v.getReviewer());
				Manager manager = managerService.getOne(wrapper);
				if(manager!=null){
					dto.setReviewername(manager.getName());
				}
				list.add(dto);
			});
			Set<Integer> idSet = list.stream().map(v -> v.getId()).collect(Collectors.toSet());
			Map<Integer, OASimpleInfo> oaMap = queryIdOaNameWechatBrand(idSet);
			Map<Integer, String> payRemarkMap = paymentAppService.queryPayRemarkByScheId(idSet);
			Map<Integer, String> returnRemarkMap = oasReturnService.queryReturnRemarkByScheId(idSet);
			Map<Integer, BigDecimal> totalPayMap = paymentAppService.queryTotalPayByScheId(
					list.stream().filter(v -> v.getIsPay() == null).map(v -> v.getId()).collect(Collectors.toSet()));
			Map<Integer, BigDecimal> totalReturnMap = oasReturnService.queryTotalReturnByScheId(
					list.stream().filter(v -> v.getIsReturnMoney() == null).map(v -> v.getId()).collect(Collectors.toSet()));
			Map<Integer, Boolean> haveOaFapiaoMap = null;
			if (condition.getHaveOaFapiao() == null)
				haveOaFapiaoMap = paymentAppService.ifHaveFapiao(idSet);
			Map<Integer, Boolean> haveOaFapiaoMap2 = haveOaFapiaoMap; // final
			Map<Integer, Boolean> haveCustFapiaoMap = null;
			if (condition.getHaveCustFapiao() == null)
				haveCustFapiaoMap = oasReturnService.ifMakeFapiao(idSet);
			Map<Integer, Boolean> haveCustFapiaoMap2 = haveCustFapiaoMap; // final
			Date now = new Date();
			list.forEach(v -> {
				// 显示公众号名称，微信号，粉丝数，客户品牌名称，客户公司名称
				OASimpleInfo info = oaMap.get(v.getId());
				if (info != null) {
					v.setOaName(info.getName());
					v.setWechat(info.getWechat());
					v.setFans(info.getFans());
					if (info.getBrand() != null)
						v.setCustomerName(info.getBrand());
					v.setCompany(info.getCompany());
				}
				// 计算税额，利润。
				String[] array = MathUtil.calculateProfitMore(v.getExecutePrice(), v.getCostPrice(), v.getCustFapiao() == -1 ? 0 : v.getCustFapiao(),
						v.getOaFapiaoType() != null && v.getOaFapiaoType() == 0 ? (v.getOaFapiao() == -1 ? 0 : v.getOaFapiao()) : 0, v.getSellExpense(), v.getChannelExpense());
				v.setCustomerTax(array[0]);
				v.setChannelTax(array[1]);
				v.setProfit(array[2]);
				v.setProfitRate(array[3]);
				// 显示垫付天数
				if (v.getExecutePrice().doubleValue() == 0) // 赠送的排期不显示垫付
					v.setAdvancePay(null);
				else if (now.after(v.getPutDate())) {
					int days = DateUtil.intervalDays(v.getPutDate(), v.getReturnTime() == null ? now : v.getReturnTime());
					v.setAdvancePay(days > 0 ? days : null);
				}
				// 显示付款备注
				v.setPayRemark(payRemarkMap.get(v.getId()));
				// 显示回款备注
				v.setReturnRemark(returnRemarkMap.get(v.getId()));
				// 显示付款百分比
				if (totalPayMap.get(v.getId()) != null)
					v.setPayRatio(totalPayMap.get(v.getId()).multiply(BigDecimal.valueOf(100)).divide(v.getCostPrice(), 2, BigDecimal.ROUND_HALF_UP));
				// 显示回款百分比
				if (totalReturnMap.get(v.getId()) != null)
					v.setReturnRatio(totalReturnMap.get(v.getId()).multiply(BigDecimal.valueOf(100)).divide(v.getExecutePrice(), 2, BigDecimal.ROUND_HALF_UP));
				// 显示是否已开进项发票
				if (condition.getHaveOaFapiao() == null) {
					v.setHaveOaFapiao(haveOaFapiaoMap2.get(v.getId()));
				} else if (condition.getHaveOaFapiao() == 0) //已开票
					v.setHaveOaFapiao(true);
				// 显示是否已开销项发票
				if (condition.getHaveCustFapiao() == null) {
					v.setHaveCustFapiao(haveCustFapiaoMap2.get(v.getId()));
				} else if (condition.getHaveCustFapiao() == 0) //已开票
					v.setHaveCustFapiao(true);
			});
			// 显示列表数据汇总
			List<OASchedule> all = list(queryWrapper.select("execute_price", "cost_price", "sell_expense", "channel_expense", "cust_fapiao", "oa_fapiao", "oa_fapiao_type"));
			List<String[]> arrays = all.stream().map(v -> MathUtil.calculateProfitMore(v.getExecutePrice(), v.getCostPrice(), v.getCustFapiao() == -1 ? 0 : v.getCustFapiao(),
					v.getOaFapiaoType() != null && v.getOaFapiaoType() == 0 ? (v.getOaFapiao() == -1 ? 0 : v.getOaFapiao()) : 0, v.getSellExpense(), v.getChannelExpense())).collect(Collectors.toList());
			BigDecimal total_execute = BigDecimal.valueOf(all.stream().mapToDouble(v -> v.getExecutePrice().doubleValue()).summaryStatistics().getSum()).setScale(2, BigDecimal.ROUND_HALF_UP);
			BigDecimal total_cost = BigDecimal.valueOf(all.stream().mapToDouble(v -> v.getCostPrice().doubleValue()).summaryStatistics().getSum()).setScale(2, BigDecimal.ROUND_HALF_UP);
			BigDecimal total_sellExpense = BigDecimal.valueOf(all.stream().mapToDouble(v -> (v.getSellExpense() == null ? 0 : v.getSellExpense()).doubleValue()).summaryStatistics().getSum()).setScale(2, BigDecimal.ROUND_HALF_UP);
			BigDecimal total_channelExpense = BigDecimal.valueOf(all.stream().mapToDouble(v -> (v.getChannelExpense() == null ? 0 : v.getChannelExpense()).doubleValue()).summaryStatistics().getSum()).setScale(2, BigDecimal.ROUND_HALF_UP);
			BigDecimal total_customer_tax = BigDecimal.valueOf(arrays.stream().mapToDouble(v-> Double.valueOf(v[0])).summaryStatistics().getSum()).setScale(2, BigDecimal.ROUND_HALF_UP);
			BigDecimal total_channel_tax = BigDecimal.valueOf(arrays.stream().mapToDouble(v-> Double.valueOf(v[1])).summaryStatistics().getSum()).setScale(2, BigDecimal.ROUND_HALF_UP);
			BigDecimal total_profit = BigDecimal.valueOf(arrays.stream().mapToDouble(v-> Double.valueOf(v[2])).summaryStatistics().getSum()).setScale(2, BigDecimal.ROUND_HALF_UP);
			BigDecimal total_profit_rate = total_execute.doubleValue() == 0 ? BigDecimal.ZERO : total_profit.multiply(BigDecimal.valueOf(100)).divide(total_execute, 2, BigDecimal.ROUND_HALF_UP);
			summary_map = new HashMap<>();
			summary_map.put("total_execute", total_execute);
			summary_map.put("total_cost", total_cost.add(total_sellExpense).subtract(total_channelExpense)); //两种费用并入总成本显示
			summary_map.put("total_customer_tax", total_customer_tax);
			summary_map.put("total_channel_tax", total_channel_tax);
			summary_map.put("total_profit", total_profit);
			summary_map.put("total_profit_rate", total_profit_rate);
			// 显示开票成本占比（仅适用角色：媒介、广告部主管、业务总监）
			if (sign == RoleSign.medium || sign == RoleSign.dept || sign == RoleSign.director || sign==RoleSign.medium_director) {
				double fapiao_cost = all.stream().filter(v -> v.getOaFapiao() != -1).mapToDouble(v -> v.getCostPrice().doubleValue()).summaryStatistics().getSum();
				if (total_cost.doubleValue() == 0) //针对只查询到赠送的排期价格为0
					summary_map.put("fapiao_cost", 0);
				else
					summary_map.put("fapiao_cost", BigDecimal.valueOf(fapiao_cost).multiply(BigDecimal.valueOf(100)).divide(total_cost, 2, BigDecimal.ROUND_HALF_UP));
			}
		}
		return new PageHelpers<OAScheduleDto>(condition.getPage(), condition.getSize(), iPage.getTotal(), list, summary_map);
	}

	@Override
	public void exportExcel(OAScheduleCondition condition, HttpServletResponse response) {
		List<OASchedule_Excel> list;
		QueryWrapper<OASchedule> queryWrapper = new QueryWrapper<>();
		// 【查询限制】
		LoginCache managerInfo = BaseController.getManagerInfo();
		RoleSign sign = roleService.judgeRole(managerInfo.getRole());
		if (sign == RoleSign.ae) // 若是AE则只能看到自己创建的排期
			queryWrapper.eq("creator", managerInfo.getId());
		else if (sign == RoleSign.medium) // 若是媒介则只能看到自己跟进的排期
			queryWrapper.eq("medium", managerInfo.getId());
		else if (sign == RoleSign.group) { // 若是广告部组长则只能看到自己部门里AE及自己创建的排期
			List<Integer> ids = managerService.queryIdsByDeptIdRoleName(managerInfo.getDepartment(), "AE");
			ids.add(managerInfo.getId());
			queryWrapper.in("creator", ids);
		} else if (sign == RoleSign.dept) { // 若是广告部主管则能看到自己创建的及部门里AE创建的排期、各个组里AE及组长本人的排期
			List<Integer> ids = new ArrayList<>();
			List<Integer> deptList = departmentService.subcollection(departmentService.departmentTree(managerInfo.getDepartment()), null);
			for (Integer dept : deptList) {
				ids.addAll(managerService.queryIdsByDeptIdRoleName(dept, "AE"));
				ids.add(departmentService.getOne(new QueryWrapper<Department>().select("leader").eq("id", dept)).getLeader()); //主管或组长也能建排期
			}
			queryWrapper.in("creator", ids);
		}
		if (condition.getCode() != null)
			queryWrapper.eq("id", condition.getCode());
		if (condition.getState() != null)
			queryWrapper.eq("state", condition.getState());
		else // 默认排除（已作废，不通过，创建）
			queryWrapper.notIn("state", State4OASchedule.作废.code(), State4OASchedule.审核不通过.code(), State4OASchedule.创建.code());
		if (condition.getMedium() != null)
			queryWrapper.eq("medium", condition.getMedium());
		if (condition.getCreator() != null)
			queryWrapper.eq("creator", condition.getCreator());
		if (condition.getIsPay() != null) {
			if (condition.getIsPay() == 1)
				queryWrapper.eq("is_pay", 1);
			else 
				queryWrapper.isNull("is_pay");
		}
		if (condition.getIsReturnMoney() != null) {
			if (condition.getIsReturnMoney() == 1)
				queryWrapper.eq("is_return_money", 1);
			else if (condition.getIsReturnMoney() == 0)
				queryWrapper.isNull("is_return_money");
			else if (condition.getIsReturnMoney() == 2) { // 部分回款
				queryWrapper.isNull("is_return_money")
					.exists("select 1 from wm_oas_return where schedule = wm_oa_schedule.id and state = " + State4OASReturn.已回款.code());
			}
		}
		if (StringUtils.hasText(condition.getCustomerName())) {
			List<Integer> cusIds = customerService.queryIdsBybrand(condition.getCustomerName());
			if (cusIds.isEmpty()) {
				ExcelFactory.createWriter(OASchedule_Excel.class, response).write(null).flush();
				return;
			}
			queryWrapper.in("customer", cusIds);
		}
		if (StringUtils.hasText(condition.getCompany())) {
			List<Integer> cusIds = customerService.queryIdsByCompany(condition.getCompany());
			if (cusIds.isEmpty()) {
				ExcelFactory.createWriter(OASchedule_Excel.class, response).write(null).flush();
				return;
			}
			queryWrapper.in("customer", cusIds);
		}
		if(StringUtils.hasText(condition.getBusiness())){
			QueryWrapper<Manager> wrapper = new QueryWrapper<>();
			wrapper.select("id").eq("name",condition.getBusiness());
			Manager manager = managerService.getOne(wrapper);
			if(manager!=null){
				List<Integer> cusIds = customerService.queryIdsByBusiness(manager.getId());
				if (cusIds.isEmpty()){
					ExcelFactory.createWriter(OASchedule_Excel.class, response).write(null).flush();
				return;}
				queryWrapper.in("customer", cusIds);
			}
		}
		if(StringUtils.hasText(condition.getReviewername())){
			QueryWrapper<Manager> wrapper = new QueryWrapper<>();
			wrapper.select("id").eq("name",condition.getReviewername());
			Manager manager = managerService.getOne(wrapper);
			if(manager!=null){
				queryWrapper.eq("reviewer",manager.getId());
			}
		}
		if (StringUtils.hasText(condition.getOaName())) {
			List<Integer> oaIds = officeAccountService.queryIdsByName(condition.getOaName().trim());
			if (oaIds.isEmpty()) {
				ExcelFactory.createWriter(OASchedule_Excel.class, response).write(null).flush();
				return;
			}
			queryWrapper.in("office_account", oaIds);
		}
		if (StringUtils.hasText(condition.getRemark())) {
			List<Integer> scheduleIds = paymentAppService.queryOascheIdByPayRemark(condition.getRemark());
			List<Integer> scheduleIds2 = oasReturnService.queryOascheIdByReturnRemark(condition.getRemark().trim());
			scheduleIds.addAll(scheduleIds2);
			List<Integer> all = scheduleIds.stream().distinct().collect(Collectors.toList());
			if (all.isEmpty())
				queryWrapper.like("remark", condition.getRemark().trim());
			else
				queryWrapper.and(wrap -> wrap.like("remark", condition.getRemark()).or().in("id", all));
		}
		if (condition.getAdvancePay() != null) {
			if (condition.getAdvancePay() == 1) { // 超期回款 或 超期未回款
				// put_date 与 return_time 两个日期都是yyyy-MM-dd ，不需要进行date_format(put_date,'%Y-%m-%d')转换。
				queryWrapper.apply("((execute_price != 0 and return_time is null and put_date < {0}) or (return_time is not null and put_date < return_time))", DateUtil.date2yyyyMMdd(new Date()));
			} else {  // 未到期  或  提前、当天已回款
				queryWrapper.apply("(execute_price = 0 or put_date > {0} or (return_time is not null and put_date >= return_time))", new Date());
			}
		}
		if (condition.getDateType() != null) {
			Integer date_type = condition.getDateType();
			Date start, end;
			if (date_type == 4) { //付款时间
				if (StringUtils.hasText(condition.getEndTime()))
					end = DateUtil.nextDay(condition.getEndTime());
				else
					end = DateUtil.nextDay(new Date());
				if (StringUtils.hasText(condition.getStartTime())) {
					start = DateUtil.yyyyMMdd2date(condition.getStartTime().trim());
					if (start.before(DateUtil.monthsAgo(end, 2))) //最大间隔2个月
						start = DateUtil.monthsAgo(end, 2);
				} else
					start = DateUtil.monthsAgo(end, 2);
				QueryWrapper<PaymentApp> payQuery = new QueryWrapper<>();
				payQuery.select("distinct schedule").in("state", State4PaymentApp.待复核.code(), State4PaymentApp.已付款.code());
				payQuery.ge("paytime", start).lt("paytime", end);
				List<Integer> scheIds = paymentAppService.list(payQuery).stream().map(v -> v.getSchedule()).collect(Collectors.toList());
				if (scheIds.isEmpty()) {
					ExcelFactory.createWriter(OASchedule_Excel.class, response).write(null).flush();
					return;
				} else
					queryWrapper.in("id", scheIds);
			} else {
				String field = date_type == 1 ? "create_time" : date_type == 2 ? "put_date" : "return_time"; //创建、投放、回款时间
				if (StringUtils.hasText(condition.getEndTime()))
					end = DateUtil.nextDay(condition.getEndTime().trim());
				else
					end = DateUtil.nextDay(new Date());
				if (StringUtils.hasText(condition.getStartTime())) {
					start = DateUtil.yyyyMMdd2date(condition.getStartTime().trim());
					if (start.before(DateUtil.monthsAgo(end, 2))) //最大间隔2个月
						start = DateUtil.monthsAgo(end, 2);
				} else
					start = DateUtil.monthsAgo(end, 2);
				queryWrapper.ge(field, start).lt(field, end);
			}
		}
		if (condition.getPriceType() != null) {
			String field = condition.getPriceType() == 1 ? "execute_price" : "cost_price"; //执行价、成本价
			if (condition.getPrice_lower() != null)
				queryWrapper.ge(field, condition.getPrice_lower());
			if (condition.getPrice_upper() != null)
				queryWrapper.le(field, condition.getPrice_upper());
		}
		if (condition.getHaveOaFapiao() != null) {
			if (condition.getHaveOaFapiao() == 0) { //已开进项票
				List<Integer> scheIds = paymentAppService.havaFapiao();
				if (scheIds.isEmpty()) {
					ExcelFactory.createWriter(OASchedule_Excel.class, response).write(null).flush();
					return;
				}
				queryWrapper.in("id", scheIds);
			} else if (condition.getHaveOaFapiao() == 1) { //已支付未开进项票
				List<Integer> scheIds = paymentAppService.noHavaFapiao();
				if (scheIds.isEmpty()) {
					ExcelFactory.createWriter(OASchedule_Excel.class, response).write(null).flush();
					return;
				}
				queryWrapper.in("id", scheIds);
			}
		}
		if (condition.getHaveCustFapiao() != null) {
			if (condition.getHaveCustFapiao() == 0) { //已开销项票
				List<Integer> scheIds = oasReturnService.makeFapiao();
				if (scheIds.isEmpty()) {
					ExcelFactory.createWriter(OASchedule_Excel.class, response).write(null).flush();
					return;
				}
			} else if (condition.getHaveCustFapiao() == 1) { //未开销项票
				List<Integer> scheIds = oasReturnService.noMakeFapiao();
				if (scheIds.isEmpty()) {
					ExcelFactory.createWriter(OASchedule_Excel.class, response).write(null).flush();
					return;
				}
				queryWrapper.in("id", scheIds);
			}
		}
		if (condition.getAdvantage() != null)
			queryWrapper.eq("advantage", 1);
		// 战队查询（媒介、AE、队主管 不可查）
		if (condition.getTeam() != null) {
			Assert.state(sign != RoleSign.ae && sign != RoleSign.medium && sign != RoleSign.group, "无权查询");
			List<Integer> AE_ids = null;
			List<Integer> deptList = departmentService.subcollection(departmentService.departmentTree(condition.getTeam()), null);
			if (deptList.size() == 1) { // 小组 或 无小组的战队
				AE_ids = managerService.queryIdsByDeptIdRoleName(condition.getTeam(), "AE");
				AE_ids.add(departmentService.getOne(new QueryWrapper<Department>().select("leader").eq("id", condition.getTeam())).getLeader()); //主管或组长也能建排期
			} else { // 有小组的战队
				AE_ids = new ArrayList<>();
				for (Integer dept : deptList) {
					AE_ids.addAll(managerService.queryIdsByDeptIdRoleName(dept, "AE"));
					AE_ids.add(departmentService.getOne(new QueryWrapper<Department>().select("leader").eq("id", dept)).getLeader()); //主管或组长也能建排期
				}
			}
			if (AE_ids.isEmpty())
				queryWrapper.in("creator", -1);
			queryWrapper.in("creator", AE_ids);
		}
		// 排序
		if (StringUtils.hasText(condition.getSort())) {
			if ("p".equals(condition.getSort())) // 投放日期 升序
				queryWrapper.orderByAsc("put_date");
			else if ("pd".equals(condition.getSort())) // 投放日期 降序
				queryWrapper.orderByDesc("put_date");
		} else
			queryWrapper.orderByDesc("id");
		List<OASchedule> records = list(queryWrapper);
		if (records.isEmpty())
			list = Collections.emptyList();
		else {
			list = new ArrayList<>(records.size());
			records.forEach(v -> {
				OASchedule_Excel dto = new OASchedule_Excel();
				BeanUtils.copyProperties(v, dto);
				dto.setCustomerName(v.getBrand()); //兼顾旧版客户品牌名称
				list.add(dto);
			});
			Set<Integer> idSet = list.stream().map(v -> v.getId()).collect(Collectors.toSet());
			Map<Integer, OASimpleInfo> oaMap = queryIdOaNameWechatBrand(idSet);
			Map<Integer, String> payRemarkMap = paymentAppService.queryPayRemarkByScheId(idSet);
			Map<Integer, String> returnRemarkMap = oasReturnService.queryReturnRemarkByScheId(idSet);
			Date now = new Date();
			list.forEach(v -> {
				// 显示公众号名称，微信号，粉丝数，客户品牌名称，客户公司名称
				OASimpleInfo info = oaMap.get(v.getId());
				if (info != null) {
					v.setOaName(info.getName());
					v.setWechat(info.getWechat());
					v.setFans(info.getFans());
					if (info.getBrand() != null)
						v.setCustomerName(info.getBrand());
					v.setCompany(info.getCompany());
				}
				// 计算税额，利润。
				String[] array = MathUtil.calculateProfitMore(v.getExecutePrice(), v.getCostPrice(), v.getCustFapiao() == -1 ? 0 : v.getCustFapiao(),
						v.getOaFapiaoType() != null && v.getOaFapiaoType() == 0 ? (v.getOaFapiao() == -1 ? 0 : v.getOaFapiao()) : 0, v.getSellExpense(), v.getChannelExpense());
				v.setCustomerTax(array[0]);
				v.setChannelTax(array[1]);
				v.setProfit(array[2]);
				v.setProfitRate(array[3]);
				// 显示垫付天数
				if (v.getExecutePrice().doubleValue() == 0) // 赠送的排期不显示垫付
					v.setAdvancePay(null);
				else if (now.after(v.getPutDate())) {
					int days = DateUtil.intervalDays(v.getPutDate(), v.getReturnTime() == null ? now : v.getReturnTime());
					v.setAdvancePay(days > 0 ? days : null);
				}
				// 显示付款备注
				v.setPayRemark(payRemarkMap.get(v.getId()));
				// 显示回款备注
				v.setReturnRemark(returnRemarkMap.get(v.getId()));
			});
		}
		ExcelFactory.createWriter(OASchedule_Excel.class, response).write(list).flush();
	}
	
	@Override
	public PageHelper<OAScheduleDto2> getListByPage2(OAScheduleCondition2 condition2) {
		List<OAScheduleDto2> list;
		QueryWrapper<OASchedule> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("office_account", condition2.getOaId());
		queryWrapper.notIn("state", State4OASchedule.审核不通过.code(), State4OASchedule.作废.code());
		if (StringUtils.hasText(condition2.getCustomerName())) {
			List<Integer> cusIds = customerService.queryIdsBybrand(condition2.getCustomerName());
			if (cusIds.isEmpty())
				return new PageHelper<OAScheduleDto2>(condition2.getPage(), condition2.getSize(), 0, Collections.emptyList());
			queryWrapper.in("customer", cusIds);
		}
		queryWrapper.orderByDesc("id");
		IPage<OASchedule> iPage = page(new Page<>(condition2.getPage(), condition2.getSize()), queryWrapper);
		List<OASchedule> records = iPage.getRecords();
		if (records.isEmpty())
			list = Collections.emptyList();
		else {
			list = new ArrayList<>(records.size());
			records.forEach(v -> {
				OAScheduleDto2 dto = new OAScheduleDto2();
				BeanUtils.copyProperties(v, dto);
				dto.setCustomerName(v.getBrand()); //兼顾旧版客户品牌名称
				list.add(dto);
			});
			Map<Integer, OASimpleInfo> oaMap = queryIdOaNameWechatBrand(list.stream().map(v -> v.getId()).collect(Collectors.toSet()));
			Map<Integer, Boolean> repeatMap = ifRepeat(condition2.getOaId(), list.stream().map(v -> v.getCustomer()).collect(Collectors.toSet()));
			LoginCache managerInfo = BaseController.getManagerInfo();
			RoleSign sign = roleService.judgeRole(managerInfo.getRole());
			list.forEach(v -> {
				// 显示公众号名称，微信号，粉丝数，客户品牌名称
				OASimpleInfo info = oaMap.get(v.getId());
				if (info != null) {
					v.setOaName(info.getName());
					v.setWechat(info.getWechat());
					if (info.getBrand() != null)
						v.setCustomerName(info.getBrand());
				}
				// 客户是否复投
				v.setIsRepeat(repeatMap.get(v.getCustomer()));
				// 只有广告部主管、业务总监可以看到价格税点信息
				if (sign != RoleSign.dept && sign != RoleSign.director && sign != RoleSign.general_mgr) {
					v.setExecutePrice(null);
					v.setCostPrice(null);
					v.setCustFapiao(null);
					v.setOaFapiao(null);
					v.setOaFapiaoType(null);
				}
			});
		}
		return new PageHelper<OAScheduleDto2>(condition2.getPage(), condition2.getSize(), iPage.getTotal(), list);
	}
	
	/**
	 * 客户是否复投
	 */
	private Map<Integer, Boolean> ifRepeat(Integer oaId, Set<Integer> customerIds) {
		return customerIds.isEmpty() ? Collections.emptyMap() : 
			oAScheduleMapper.queryRepeatCount(oaId, customerIds.stream().map(String::valueOf).collect(Collectors.joining(",")), 
					State4OASchedule.审核不通过.code() + "," + State4OASchedule.作废.code())
			.stream().collect(Collectors.toMap(IdCount::getId, v -> v.getCount() > 1 ? true : false));
	}

	@Override
	@Transactional
	public boolean addOASchedule(OAScheduleSubmit submit) {
		//Assert.state(submit.getCostPrice().doubleValue() < submit.getExecutePrice().doubleValue(), "执行价必须大于成本价");
		OASchedule schedule = new OASchedule();
		BeanUtils.copyProperties(submit, schedule);
		schedule.setPutDate(DateUtil.yyyyMMdd2date(submit.getPutDate()));
		schedule.setState(State4OASchedule.创建.code());
		LoginCache managerInfo = BaseController.getManagerInfo();
		schedule.setCreator(managerInfo.getId());
		schedule.setCreatorName(managerInfo.getName());
		schedule.setCreateTime(new Date());
		if (officeAccountService.isAdvantage(schedule.getOfficeAccount()))
			schedule.setAdvantage(1); //优势号排期
		boolean boo = save(schedule);
		if (boo) {
			// 更新公众号的对接媒介、最后更新时间
			Assert.state(officeAccountService.changeMedium(schedule.getOfficeAccount(), schedule.getMedium()), "公众号未启用");
			// 更新客户的活跃时间
			customerService.updateDrawTime(submit.getCustomer());
		}
		return boo;
	}

	@Override
	public int submitState(Integer... id) {
		Integer reviewer = departmentService.getSuperiorLeader(BaseController.getManagerInfo().getDepartment());
		Assert.notNull(reviewer, "当前没有上级主管");
		UpdateWrapper<OASchedule> updateWrapper;
		int success = 0;
		for (Integer _id : id) {
			updateWrapper = new UpdateWrapper<OASchedule>()
				.eq("id", _id).eq("state", State4OASchedule.创建.code())
				.set("state", State4OASchedule.提交待审核.code()).set("reviewer", reviewer);
			success += oAScheduleMapper.update(null, updateWrapper);
		}
		Assert.state(success > 0, "仅限未提交状态的排期");
		// msg
		approvalMsgService.sendApprovalMsg(Type4ApprovalMsg.公众号排期待审核, reviewer);
		return success;
	}

	@Override
	public int review(int result, String reason, Integer... id) {
		Assert.state(result == 0 || result == 1, "result参数非法");
		LoginCache managerInfo = BaseController.getManagerInfo();
		UpdateWrapper<OASchedule> updateWrapper;
		int success = 0;
		for (Integer _id : id) {
			updateWrapper = new UpdateWrapper<OASchedule>()
				.eq("id", _id).eq("state", State4OASchedule.提交待审核.code())
				.eq("reviewer", managerInfo.getId());
			if (result == 0)
				updateWrapper.set("state", State4OASchedule.审核不通过.code()).set("failure_reason", reason);
			else
				updateWrapper.set("state", State4OASchedule.待确认.code());
			success += oAScheduleMapper.update(null, updateWrapper);
		}
		Assert.state(success > 0, "仅限待审核状态的排期");
		// msg
		approvalMsgService.sendApprovalMsg(Type4ApprovalMsg.公众号排期待审核, BaseController.getManagerInfo().getId());
		list(new QueryWrapper<OASchedule>().select("medium").in("id", Arrays.asList(id))).stream().map(OASchedule::getMedium)
			.collect(Collectors.toSet()).forEach(v -> approvalMsgService.sendApprovalMsg(Type4ApprovalMsg.公众号排期待确认, v));
		return success;
	}

	@Override
	public int confirm(String remark, Integer... id) {
		List<OASchedule> list = list(new QueryWrapper<OASchedule>().select("id", "office_account", "execute_price", "cost_price").in("id", Arrays.asList(id)));
		Date date = new Date();
		Integer managerId = BaseController.getManagerInfo().getId();
		UpdateWrapper<OASchedule> updateWrapper;
		int success = 0;
		for (OASchedule oasche : list) {
			updateWrapper = new UpdateWrapper<OASchedule>()
				.eq("id", oasche.getId())
				.eq("state", State4OASchedule.待确认.code())
				.eq("medium", managerId)
				.set("confirm_remark", remark);
			if (oasche.getExecutePrice().doubleValue() == 0 && oasche.getCostPrice().doubleValue() == 0) // 赠送的排队确认后直接“已支付、已回款、已完成”
				updateWrapper.set("is_pay", 1).set("is_return_money", 1).set("return_time", date).set("state", State4OASchedule.已完成.code());
			else if (oasche.getExecutePrice().doubleValue() == 0)
				updateWrapper.set("is_return_money", 1).set("return_time", date).set("state", State4OASchedule.待结算.code());
			else if (oasche.getCostPrice().doubleValue() == 0)
				updateWrapper.set("is_pay", 1).set("state", State4OASchedule.待结算.code());
			else
				updateWrapper.set("state", State4OASchedule.待结算.code());
			// 集采流程
			if (officeAccountService.isAgreement(oasche.getOfficeAccount()))
				updateWrapper.set("state", State4OASchedule.待集采确认.code());
			success += oAScheduleMapper.update(null, updateWrapper);
		}
		Assert.state(success > 0, "仅限待确认状态的排期");
		// msg
		approvalMsgService.sendApprovalMsg(Type4ApprovalMsg.公众号排期待确认, managerId);
		approvalMsgService.sendApprovalMsg(Type4ApprovalMsg.公众号排期待集采确认, null);
		return success;
	}

	@Override
	public int appCancel(Integer... id) {
		UpdateWrapper<OASchedule> updateWrapper;
		int success = 0;
		for (Integer _id : id) {
			OASchedule schedule = getById(_id);
			Assert.notNull(schedule, "未知排期");
			// 直接作废，无需经过审核。
			if (schedule.getState().intValue() == State4OASchedule.创建.code() ||
					schedule.getState().intValue() == State4OASchedule.审核不通过.code()) {
				updateWrapper = new UpdateWrapper<OASchedule>()
						.eq("id", _id)
						.set("state", State4OASchedule.作废.code());
				success += oAScheduleMapper.update(null, updateWrapper);
			} else {
				// 判断是否存在进行中的其他流程（此处为修改流程）
				boolean exists = editRecordService.existsOngoing(Type4Edit.排期, _id);
				if (!exists) {
					// 判断是否存在有效的支付申请
					exists = paymentAppService.existPaymentApp(_id);
					if (!exists) {
						updateWrapper = new UpdateWrapper<OASchedule>()
								.eq("id", _id)
								.in("state", State4OASchedule.提交待审核.code(), State4OASchedule.待确认.code(), State4OASchedule.待结算.code())
								.set("app_cancel", State4AppCancel.待主管审核.code());
						success += oAScheduleMapper.update(null, updateWrapper);
					}
				}
			}
		}
		Assert.state(success > 0, "仅限待审核待确认或未支付的排期，且不处于修改流程。");
		// msg
		approvalMsgService.sendApprovalMsg(Type4ApprovalMsg.公众号排期作废申请, null);
		return success;
	}

	@Override
	public int reviewCancel(int result, String reason, Integer... id) {
		Assert.state(result == 0 || result == 1, "result参数非法");
		UpdateWrapper<OASchedule> updateWrapper;
		int success = 0;
		for (Integer _id : id) {
			updateWrapper = new UpdateWrapper<OASchedule>().eq("id", _id)
					.eq("app_cancel", State4AppCancel.待主管审核.code());
			if (result == 0)
				updateWrapper.set("app_cancel", State4AppCancel.待媒介审核.code());
			else
				updateWrapper.set("app_cancel", State4AppCancel.主管审核未通过.code())
				.set("cancel_failure_reason", reason);
			success += oAScheduleMapper.update(null, updateWrapper);
		}
		Assert.state(success > 0, "仅限待主管审核状态");
		// msg
		approvalMsgService.sendApprovalMsg(Type4ApprovalMsg.公众号排期作废申请, BaseController.getManagerInfo().getId());
		list(new QueryWrapper<OASchedule>().select("medium").in("id", Arrays.asList(id))).stream().map(OASchedule::getMedium)
			.collect(Collectors.toSet()).forEach(v -> approvalMsgService.sendApprovalMsg(Type4ApprovalMsg.公众号排期作废待确认, v));
		return success;
	}

	@Override
	public int confirmCancel(int result, String reason, Integer... id) {
		Assert.state(result == 0 || result == 1, "result参数非法");
		UpdateWrapper<OASchedule> updateWrapper;
		int success = 0;
		for (Integer _id : id) {
			updateWrapper = new UpdateWrapper<OASchedule>().eq("id", _id)
					.eq("app_cancel", State4AppCancel.待媒介审核.code());
			if (result == 0)
				updateWrapper.set("app_cancel", State4AppCancel.同意作废.code())
					.set("state", State4OASchedule.作废.code());
			else
				updateWrapper.set("app_cancel", State4AppCancel.媒介审核未通过.code())
					.set("cancel_failure_reason", reason);
			success += oAScheduleMapper.update(null, updateWrapper);
		}
		Assert.state(success > 0, "仅限待媒介审核状态");
		// msg
		approvalMsgService.sendApprovalMsg(Type4ApprovalMsg.公众号排期作废待确认, BaseController.getManagerInfo().getId());
		return success;
	}

	@Override
	public boolean updateInfo(OAScheduleEdit edit) {
		OASchedule schedule = getById(edit.getId());
		Assert.notNull(schedule, "未知排期");
		Assert.state(schedule.getState().intValue() == State4OASchedule.创建.code() ||
				schedule.getState().intValue() == State4OASchedule.审核不通过.code(), "仅限创建或审核不通过状态");
		BeanUtils.copyProperties(edit, schedule);
		schedule.setPutDate(DateUtil.yyyyMMdd2date(edit.getPutDate()));
		return updateById(schedule);
	}

	@Override
	public int deleteOASchedule(Integer... id) {
		QueryWrapper<OASchedule> queryWrapper;
		int success = 0;
		for (Integer _id : id) {
			queryWrapper = new QueryWrapper<OASchedule>().eq("id", _id)
					.eq("state", State4OASchedule.创建.code());
			success += oAScheduleMapper.delete(queryWrapper);
		}
		Assert.state(success > 0, "仅限创建状态的排期");
		return success;
	}

	@Override
	public void checkReturnFinish(Integer id, BigDecimal total, Date return_time) {
		OASchedule schedule = getOne(new QueryWrapper<OASchedule>().select("id", "execute_price", "sell_expense", "is_pay").eq("id", id));
		if (schedule.getExecutePrice().compareTo(total) == 0)
			Assert.state(update(new UpdateWrapper<OASchedule>().eq("id", id).set("is_return_money", 1).set("return_time", return_time)), "回款检查完成：已回款状态修改失败");
		KeyValuePair<BigDecimal, BigDecimal> cost_sell = paymentAppService.totalPayByScheId(id);
		BigDecimal sellTotal = cost_sell.getValue();
		if (schedule.getIsPay() != null && schedule.getIsPay() == 1)
			if (schedule.getSellExpense() == null || schedule.getSellExpense().doubleValue() == sellTotal.doubleValue())
				Assert.state(update(new UpdateWrapper<OASchedule>().eq("id", id).set("state", State4OASchedule.已完成.code())), "回款检查完成：已完成状态修改失败");
	}

	@Override
	public void checkPayFinish(Integer id) {
		OASchedule schedule = getOne(new QueryWrapper<OASchedule>().select("cost_price", "sell_expense", "is_pay", "is_return_money").eq("id", id));
		if (schedule.getIsPay() == null) { //未支付
			KeyValuePair<BigDecimal, BigDecimal> cost_sell = paymentAppService.totalPayByScheId(id);
			BigDecimal costTotal = cost_sell.getKey();
			BigDecimal sellTotal = cost_sell.getValue();
			if (schedule.getCostPrice().doubleValue() == costTotal.doubleValue()) {
				Assert.state(update(new UpdateWrapper<OASchedule>().eq("id", id).set("is_pay", 1)), "支付检查完成：已支付状态修改失败");
				if (schedule.getSellExpense() == null || schedule.getSellExpense().doubleValue() == sellTotal.doubleValue())
					if (schedule.getIsReturnMoney() != null && schedule.getIsReturnMoney() == 1)
						Assert.state(update(new UpdateWrapper<OASchedule>().eq("id", id).set("state", State4OASchedule.已完成.code())), "支付检查完成：已完成状态修改失败");
			}
		} else { // 最后复核的销售费用支付
			KeyValuePair<BigDecimal, BigDecimal> cost_sell = paymentAppService.totalPayByScheId(id);
			BigDecimal sellTotal = cost_sell.getValue();
			if (schedule.getSellExpense() == null || schedule.getSellExpense().doubleValue() == sellTotal.doubleValue())
				if (schedule.getIsReturnMoney() != null && schedule.getIsReturnMoney() == 1)
					Assert.state(update(new UpdateWrapper<OASchedule>().eq("id", id).set("state", State4OASchedule.已完成.code())), "支付检查完成：已完成状态修改失败");
		}
	}

	@Override
	public boolean updateEdit(OASchedule schedule, Integer id, OAScheduleEdit edit) {
		if (schedule == null)
			schedule = getById(id);
		Assert.notNull(schedule, "未知排期");
		// 不涉及到金钱的修改
		if (edit.getExecutePrice().doubleValue() == schedule.getExecutePrice().doubleValue() &&
				edit.getCostPrice().doubleValue() == schedule.getCostPrice().doubleValue() &&
				(edit.getSellExpense() == null ? 0 :edit.getSellExpense().doubleValue()) == 
				(schedule.getSellExpense() == null ? 0 :schedule.getSellExpense().doubleValue()) && 
				(edit.getChannelExpense() == null ? 0 :edit.getChannelExpense().doubleValue()) == 
				(schedule.getChannelExpense() == null ? 0 :schedule.getChannelExpense().doubleValue())) ;
		else
			schedule.setState(State4OASchedule.待确认.code());
		BeanUtils.copyProperties(edit, schedule);
		schedule.setPutDate(DateUtil.yyyyMMdd2date(edit.getPutDate()));
		boolean boo = updateById(schedule);
		if (boo)
			approvalMsgService.sendApprovalMsg(Type4ApprovalMsg.公众号排期待确认, schedule.getMedium());
		return boo;
	}

	@Override
	public Map<Integer, OASimpleInfo> queryIdOaNameWechatBrand(Collection<Integer> idSet) {
		return idSet.isEmpty() ? Collections.emptyMap() : 
			oAScheduleMapper.queryIdNameWechatBrand(idSet.stream().map(String::valueOf).collect(Collectors.joining(",")))
			.stream().collect(Collectors.toMap(OASimpleInfo::getId, v -> v));
	}

	@Override
	public Map<Integer, OAScheduleInfo> info4PaymentAppList(Collection<Integer> collection) {
		return collection.isEmpty() ? Collections.emptyMap() : 
			oAScheduleMapper.info4PaymentAppList(collection.stream().map(String::valueOf).collect(Collectors.joining(",")))
			.stream().collect(Collectors.toMap(OAScheduleInfo::getId, v -> v));
	}

	@Override
	public Map<Integer, OAScheduleInfo2> info4OASReturnList(Collection<Integer> collection) {
		return collection.isEmpty() ? Collections.emptyMap() : 
			oAScheduleMapper.info4OASReturnList(collection.stream().map(String::valueOf).collect(Collectors.joining(",")))
			.stream().collect(Collectors.toMap(OAScheduleInfo2::getId, v -> v));
	}

	@Override
	public List<Integer> queryIdByCreator(Integer creator) {
		List<OASchedule> list = list(new QueryWrapper<OASchedule>().select("id").eq("creator", creator));
		return list.stream().map(OASchedule::getId).collect(Collectors.toList());
	}
	
	@Override
	public List<Integer> queryIdByOAName(String name) {
		return oAScheduleMapper.queryIdByOAName(name);
	}

	@Override
	public List<Integer> queryIdByCustomer(String name) {
		return oAScheduleMapper.queryIdByCustomer(name);
	}

	@Override
	public Map<Integer, Integer> queryScheduleCount(Collection<Integer> collection) {
		return collection.isEmpty() ? Collections.emptyMap() : 
			oAScheduleMapper.queryScheduleCount(collection.stream().map(String::valueOf).collect(Collectors.joining(",")), 
					State4OASchedule.审核不通过.code() + "," + State4OASchedule.作废.code())
			.stream().collect(Collectors.toMap(IdCount::getId, IdCount::getCount));
	}
	
	@Override
	public Map<Integer, String> queryMediumName(Collection<Integer> collection) {
		return collection.isEmpty() ? Collections.emptyMap() : 
			list(new QueryWrapper<OASchedule>().select("id", "medium_name").in("id", collection))
			.stream().collect(Collectors.toMap(OASchedule::getId, OASchedule::getMediumName));
	}

	@Override
	public void removeIsPay(Integer id) {
		OASchedule oASchedule = getOne(new QueryWrapper<OASchedule>().select("state", "is_pay").eq("id", id));
		Assert.notNull(oASchedule, "未知排期");
		if (oASchedule.getIsPay() != null) {
			if (oASchedule.getState().intValue() == State4OASchedule.已完成.code())
				Assert.state(update(new UpdateWrapper<OASchedule>().eq("id", id).set("is_pay", null).set("state", State4OASchedule.待结算.code())), "回退已支付：已完成_失败");
			else
				Assert.state(update(new UpdateWrapper<OASchedule>().eq("id", id).set("is_pay", null)), "回退已支付：失败");
		}
	}

	@Override
	@Transactional
	public void callback4JcConfirm(Integer id) {
		// 已支付、待结算
		UpdateWrapper<OASchedule> updateWrapper = new UpdateWrapper<OASchedule>()
				.eq("id", id).eq("state", State4OASchedule.待集采确认.code())
				.set("is_pay", 1).set("state", State4OASchedule.待结算.code());
		Assert.state(update(updateWrapper), "回调修改排期状态失败");
		// 生成支付记录、收回进项发票
		Assert.state(paymentAppService.autoCreate(id), "回调生成支付记录失败");
	}

	@Override
	public PageHelper<OAScheduleDto4jc> list4jc(PageCondition condition) {
		List<OAScheduleDto4jc> list;
		QueryWrapper<OASchedule> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("state", State4OASchedule.待集采确认.code());
		IPage<OASchedule> iPage = page(new Page<>(condition.getPage(), condition.getSize()), queryWrapper.orderByDesc("id"));
		List<OASchedule> records = iPage.getRecords();
		if (records.isEmpty())
			list = Collections.emptyList();
		else {
			list = new ArrayList<>(records.size());
			records.forEach(v -> {
				OAScheduleDto4jc dto = new OAScheduleDto4jc();
				BeanUtils.copyProperties(v, dto);
				dto.setCustomerName(v.getBrand()); //兼顾旧版客户品牌名称
				list.add(dto);
			});
			Set<Integer> idSet = list.stream().map(v -> v.getId()).collect(Collectors.toSet());
			Map<Integer, OASimpleInfo> oaMap = queryIdOaNameWechatBrand(idSet);
			list.forEach(v -> {
				// 显示公众号名称，微信号，粉丝数，客户品牌名称，客户公司名称
				OASimpleInfo info = oaMap.get(v.getId());
				if (info != null) {
					v.setOaName(info.getName());
					v.setWechat(info.getWechat());
					v.setFans(info.getFans());
					if (info.getBrand() != null)
						v.setCustomerName(info.getBrand());
					v.setCompany(info.getCompany());
				}
			});
		}
		return new PageHelper<OAScheduleDto4jc>(condition.getPage(), condition.getSize(), iPage.getTotal(), list);
	}

}