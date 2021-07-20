package com.ykhd.office.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ykhd.office.domain.bean.IdIdDecimal;
import com.ykhd.office.domain.bean.LoginCache;
import com.ykhd.office.domain.entity.Department;
import com.ykhd.office.domain.entity.Fapiao;
import com.ykhd.office.domain.entity.OASchedule;
import com.ykhd.office.domain.req.PerformanceCondition;
import com.ykhd.office.domain.resp.AERanking;
import com.ykhd.office.domain.resp.ChampByMonth;
import com.ykhd.office.domain.resp.ChampByWeek;
import com.ykhd.office.repository.OAScheduleMapper;
import com.ykhd.office.repository.PaymentAppMapper;
import com.ykhd.office.service.IDepartmentService;
import com.ykhd.office.service.IFapiaoService;
import com.ykhd.office.service.IManagerService;
import com.ykhd.office.service.IRoleService;
import com.ykhd.office.util.DateUtil;
import com.ykhd.office.util.MathUtil;
import com.ykhd.office.util.dictionary.StateEnums.State4Fapiao;
import com.ykhd.office.util.dictionary.StateEnums.State4OASchedule;
import com.ykhd.office.util.dictionary.SystemEnums.RoleSign;

/**
 * 主页控制台
 */
@RestController
@RequestMapping("/home")
public class HomepageController extends BaseController {

	@Autowired
	private OAScheduleMapper oAScheduleMapper;
	@Autowired
	private IRoleService roleService;
	@Autowired
	private IManagerService managerService;
	@Autowired
	private IFapiaoService fapiaoService;
	@Autowired
	private PaymentAppMapper paymentAppMapper;
	@Autowired
	private IDepartmentService departmentService;
	
	/**
	 * 媒介业绩排行榜
	 */
	@GetMapping("/mediumRanking")
	public Object mediumRanking(String startTime, String endTime) {
		if (StringUtils.hasText(startTime) && StringUtils.hasText(startTime)) {
			endTime = DateUtil.date2yyyyMMdd(DateUtil.nextDay(endTime));
		} else { // 默认显示本月数据
			startTime = DateUtil.firstDayOfMonth();
			endTime = DateUtil.date2yyyyMMdd(DateUtil.nextDay(new Date()));
		}
		StringBuilder sb = new StringBuilder();
		sb.append(State4OASchedule.待确认.code()).append(",").append(State4OASchedule.待结算.code()).append(",").append(State4OASchedule.已完成.code());
		return oAScheduleMapper.mediumRankingByExecutePrice(startTime, endTime, sb.toString());
	}
	
	/**
	 * AE业绩排行榜
	 */
	@GetMapping("/AERanking")
	public Object aeRanking(String startTime, String endTime) {
		if (StringUtils.hasText(startTime) && StringUtils.hasText(startTime)) {
			endTime = DateUtil.date2yyyyMMdd(DateUtil.nextDay(endTime));
		} else { // 默认显示本月数据
			startTime = DateUtil.firstDayOfMonth();
			endTime = DateUtil.date2yyyyMMdd(DateUtil.nextDay(new Date()));
		}
		StringBuilder sb = new StringBuilder();
		sb.append(State4OASchedule.待结算.code()).append(",").append(State4OASchedule.已完成.code());
		List<AERanking> list = oAScheduleMapper.AERankingByProfit(startTime, endTime, sb.toString());
		Map<String, Double> map = list.stream().collect(Collectors.toMap(AERanking::getDept, v -> v.getProfit().doubleValue(), (v1, v2) -> v1 + v2));
		if (!map.isEmpty()) {
			Entry<String, Double> entry = map.entrySet().stream().max(Comparator.comparingDouble(Entry::getValue)).get();
			list.forEach(v -> v.setChampTeam(v.getDept().equals(entry.getKey())));
		}
		return list;
	}
	
	/**
	 * AE全年月冠&周冠
	 */
	@GetMapping("/AEChampByMonthAndWeek") 
	public Object champByMonth(String year) {
		if (!StringUtils.hasText(year))
			year = DateUtil.date2yyyy(new Date());
		List<Integer> states = Arrays.asList(State4OASchedule.待结算.code(), State4OASchedule.已完成.code());
		// 月冠
		List<ChampByMonth> list = new ArrayList<>();
		List<String> monthList = new ArrayList<>();
		List<ChampByMonth> datas = oAScheduleMapper.monthProfitByYear(year, states.stream().map(v -> String.valueOf(v)).collect(Collectors.joining(",")));
		datas.forEach(v -> {
			if (!monthList.contains(v.getMonth())) { //已按月份、业绩降序排列，取每月第一条数据
				monthList.add(v.getMonth());
				list.add(v);
			}
		});
		if (!list.isEmpty()) {
			// 周冠
			List<OASchedule> all = oAScheduleMapper.selectList(new QueryWrapper<OASchedule>()
					.select("creator", "creator_name", "create_time", "execute_price", "cost_price", "sell_expense", "channel_expense", "cust_fapiao", "oa_fapiao", "oa_fapiao_type")
					.in("state", states).gt("create_time", year).lt("create_time", String.valueOf(Integer.valueOf(year) + 1)).orderByAsc("id"));
			all.stream().collect(Collectors.toMap(
					k -> DateUtil.date2yearMonthWeek(k.getCreateTime()), v -> new ArrayList<>(Arrays.asList(v)), 
						(v1, v2) -> {
							v1.addAll(v2);
							return v1;
						}))
			.entrySet().stream().sorted(Comparator.comparing(Entry::getKey)).forEach(kv -> {   // kv  -->  <"2020-09@1", [oas, oas, oas]>
				Map<String, Double> ae_profit_map = new HashMap<>();
				kv.getValue().forEach(oas -> {
					String AE = oas.getCreator() + ":" + oas.getCreatorName();
					BigDecimal profit = MathUtil.calculateProfit(oas.getExecutePrice(), oas.getCostPrice(), oas.getCustFapiao() == -1 ? 0 : oas.getCustFapiao(),
							oas.getOaFapiao() != null && oas.getOaFapiao().intValue() == 0 ? (oas.getOaFapiao() == -1 ? 0 : oas.getOaFapiao()) : 0, oas.getSellExpense(), oas.getChannelExpense());
					Double money = ae_profit_map.getOrDefault(AE, 0d);
					ae_profit_map.put(AE, money + profit.doubleValue());
				});
				Entry<String, Double> entry = ae_profit_map.entrySet().stream().max(Comparator.comparingDouble(Entry::getValue)).get();
				String[] str = kv.getKey().split("@");
				list.forEach(month -> {
					if (month.getMonth().equals(str[0])) {
						List<ChampByWeek> weeks = month.getWeeks();
						if (weeks == null)
							weeks = new ArrayList<>();
						weeks.add(new ChampByWeek(Integer.valueOf(str[1]), entry.getKey().split(":")[1]));
						month.setWeeks(weeks);
					}
				});
			});
		}
		return list;
	}
	
	/**
	 * 公众号广告部业绩查询
	 */
	@GetMapping("/performance")
	public Object performance(PerformanceCondition condition) {
		Assert.state(StringUtils.hasText(condition.getStartTime()) || StringUtils.hasText(condition.getEndTime()), "未选择日期");
		QueryWrapper<OASchedule> queryWrapper = new QueryWrapper<OASchedule>().ne("state", State4OASchedule.作废.code()).eq("is_return_money", 1);
		// 税款分成AE集合
		List<Integer> AE = new ArrayList<>();
		if (condition.getAE() != null) {
			queryWrapper.eq("creator", condition.getAE());
			AE.add(condition.getAE());
		}
		if (condition.getMedium() != null)
			queryWrapper.eq("medium", condition.getMedium());
		if (StringUtils.hasText(condition.getStartTime()))
			queryWrapper.ge("return_time", condition.getStartTime());
		if (StringUtils.hasText(condition.getEndTime()))
			queryWrapper.lt("return_time", DateUtil.nextDay(condition.getEndTime()));
		LoginCache managerInfo = BaseController.getManagerInfo();
		RoleSign sign = roleService.judgeRole(managerInfo.getRole());
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
			AE.clear();
			if (AE_ids.isEmpty()) {
				queryWrapper.eq("creator", -1);
				AE.add(-1);
			} else {
				queryWrapper.in("creator", AE_ids);
				AE.addAll(AE_ids);
			}
		}
		/*【查询限制】*/
		if (sign == RoleSign.ae) { // 若是AE则只能看到自己创建的排期
			queryWrapper.eq("creator", managerInfo.getId());
			AE.clear();
			AE.add(managerInfo.getId());
		} else if (sign == RoleSign.medium) { // 若是媒介则只能看到自己跟进的排期
			queryWrapper.eq("medium", managerInfo.getId());
			condition.setMedium(managerInfo.getId());
		} else if (sign == RoleSign.group) { // 若是广告部组长则只能看到自己创建的及部门里AE创建的排期【，若选择媒介则可以看到全部相关排期。】
			List<Integer> AE_ids = managerService.queryIdsByDeptIdRoleName(managerInfo.getDepartment(), "AE");
			AE_ids.add(managerInfo.getId());
			AE.clear();
			if (condition.getAE() != null) {
				if (AE_ids.contains(condition.getAE())) {
					queryWrapper.eq("creator", condition.getAE());
					AE.add(condition.getAE());
				} else {
					queryWrapper.eq("creator", -1);
					AE.add(-1);
				}
			} else {
				queryWrapper.in("creator", AE_ids);
				AE.addAll(AE_ids);
			}
		} else if (sign == RoleSign.dept) { // 若是广告部主管则能看到自己创建的及部门里AE创建的排期、各个组里AE及组长本人的排期【，若选择媒介则可以看到全部相关排期。】
			List<Integer> AE_ids = new ArrayList<>();
			List<Integer> deptList = departmentService.subcollection(departmentService.departmentTree(managerInfo.getDepartment()), null);
			for (Integer dept : deptList) {
				AE_ids.addAll(managerService.queryIdsByDeptIdRoleName(dept, "AE"));
				AE_ids.add(departmentService.getOne(new QueryWrapper<Department>().select("leader").eq("id", dept)).getLeader()); //主管或组长也能建排期
			}
			AE.clear();
			if (condition.getAE() != null) {
				if (AE_ids.contains(condition.getAE())) {
					queryWrapper.eq("creator", condition.getAE());
					AE.add(condition.getAE());
				} else {
					queryWrapper.eq("creator", -1);
					AE.add(-1);
				}
			} else {
				queryWrapper.in("creator", AE_ids);
				AE.addAll(AE_ids);
			}
		}
		/*【end】*/
		List<OASchedule> all = oAScheduleMapper.selectList(queryWrapper.select("id", "execute_price", "cost_price", "sell_expense", "channel_expense", "cust_fapiao", "oa_fapiao", "oa_fapiao_type", "advantage"));
		// 正常排期
		List<OASchedule> normal = all.stream().filter(v -> v.getAdvantage() == null).collect(Collectors.toList());
		int count = normal.size();
		BigDecimal total_execute = BigDecimal.valueOf(normal.stream().mapToDouble(v -> v.getExecutePrice().doubleValue()).summaryStatistics().getSum()).setScale(2, BigDecimal.ROUND_HALF_UP);
		BigDecimal total_cost = BigDecimal.valueOf(normal.stream().mapToDouble(v -> v.getCostPrice().doubleValue()).summaryStatistics().getSum()).setScale(2, BigDecimal.ROUND_HALF_UP);
		BigDecimal total_sell = BigDecimal.valueOf(normal.stream().mapToDouble(v -> v.getSellExpense() == null ? 0 : v.getSellExpense().doubleValue()).summaryStatistics().getSum()).setScale(2, BigDecimal.ROUND_HALF_UP);
		BigDecimal total_channel = BigDecimal.valueOf(normal.stream().mapToDouble(v -> v.getChannelExpense() == null ? 0 : v.getChannelExpense().doubleValue()).summaryStatistics().getSum()).setScale(2, BigDecimal.ROUND_HALF_UP);
		BigDecimal total_customer_tax = BigDecimal.valueOf(normal.stream().mapToDouble(v -> MathUtil.calculateTaxMoney(v.getExecutePrice(), v.getCustFapiao() == -1 ? 0 : v.getCustFapiao()).doubleValue()).summaryStatistics().getSum()).setScale(2, BigDecimal.ROUND_HALF_UP);
		// 优势号排期
		List<OASchedule> advantage = all.stream().filter(v -> v.getAdvantage() != null).collect(Collectors.toList());
		int count2 = advantage.size();
		BigDecimal total_execute2 = BigDecimal.valueOf(advantage.stream().mapToDouble(v -> v.getExecutePrice().doubleValue()).summaryStatistics().getSum()).setScale(2, BigDecimal.ROUND_HALF_UP);
		BigDecimal total_cost2 = BigDecimal.valueOf(advantage.stream().mapToDouble(v -> v.getCostPrice().doubleValue()).summaryStatistics().getSum()).setScale(2, BigDecimal.ROUND_HALF_UP);
		BigDecimal total_sell2 = BigDecimal.valueOf(advantage.stream().mapToDouble(v -> v.getSellExpense() == null ? 0 : v.getSellExpense().doubleValue()).summaryStatistics().getSum()).setScale(2, BigDecimal.ROUND_HALF_UP);
		BigDecimal total_channel2 = BigDecimal.valueOf(advantage.stream().mapToDouble(v -> v.getChannelExpense() == null ? 0 : v.getChannelExpense().doubleValue()).summaryStatistics().getSum()).setScale(2, BigDecimal.ROUND_HALF_UP);
		BigDecimal total_customer_tax2 = BigDecimal.valueOf(advantage.stream().mapToDouble(v -> MathUtil.calculateTaxMoney(v.getExecutePrice(), v.getCustFapiao() == -1 ? 0 : v.getCustFapiao()).doubleValue()).summaryStatistics().getSum()).setScale(2, BigDecimal.ROUND_HALF_UP);
		// 认证日期范围内（审核通过&进项&专票）总税额
		QueryWrapper<Fapiao> fapiaoQuery = new QueryWrapper<Fapiao>().select("id", "rates_money").eq("state", State4Fapiao.审核通过.code()).eq("is_add", 1).eq("type", 1).eq("source", 1);
		if (condition.getMedium() != null)
			fapiaoQuery.eq("create_userid", condition.getMedium());
		if (StringUtils.hasText(condition.getStartTime()))
			fapiaoQuery.ge("verificatio_time", condition.getStartTime());
		if (StringUtils.hasText(condition.getEndTime()))
			fapiaoQuery.lt("verificatio_time", DateUtil.nextDay(condition.getEndTime()));
		List<Fapiao> fapiaoList = fapiaoService.list(fapiaoQuery);
		BigDecimal get_channel_tax = BigDecimal.ZERO, get_channel_tax2 = BigDecimal.ZERO;
		if (!fapiaoList.isEmpty()) {
			// 列举每个发票对应的税额
			Map<Integer, BigDecimal> fp_tax_map = fapiaoList.stream().collect(Collectors.toMap(Fapiao::getId, Fapiao::getRatesMoney));
			// 按发票id分组
			List<IdIdDecimal> dataList = paymentAppMapper.queryAmountOasCreatorByFapiao(fapiaoList.stream().map(v -> String.valueOf(v.getId())).collect(Collectors.joining(",")));
			Map<Integer, List<IdIdDecimal>> normalMap = dataList.stream().filter(v -> v.getAdvantage() == null)
					.collect(Collectors.toMap(IdIdDecimal::getFapiao, v -> new ArrayList<>(Arrays.asList(v)), 
							(v1, v2) -> {
								v1.addAll(v2);
								return v1;
							}));
			Map<Integer, List<IdIdDecimal>> advantageMap = dataList.stream().filter(v -> v.getAdvantage() != null)
					.collect(Collectors.toMap(IdIdDecimal::getFapiao, v -> new ArrayList<>(Arrays.asList(v)), 
							(v1, v2) -> {
								v1.addAll(v2);
								return v1;
							}));
			// 查询单个媒介
			if (AE.isEmpty()) {
				Map<String, BigDecimal> medium_tax_map = new HashMap<>(2);
				normalMap.values().forEach(list -> {
					//单个发票id对应的支付申请总支付额（假定：单个排期税率=发票税率）
					double sum = list.stream().mapToDouble(v -> v.getAmount().doubleValue()).summaryStatistics().getSum();
					Assert.state(sum != 0, "支付金额0");
					BigDecimal totalPay = BigDecimal.valueOf(sum).setScale(2, BigDecimal.ROUND_HALF_UP);
					list.forEach(v -> {
						BigDecimal tax = medium_tax_map.getOrDefault("get_channel_tax", BigDecimal.ZERO);
						tax = tax.add(v.getAmount().multiply(fp_tax_map.get(v.getFapiao())).divide(totalPay, 4, BigDecimal.ROUND_HALF_UP));
						medium_tax_map.put("get_channel_tax", tax);
					});
				});
				advantageMap.values().forEach(list -> {
					//单个发票id对应的支付申请总支付额（假定：单个排期税率=发票税率）
					double sum = list.stream().mapToDouble(v -> v.getAmount().doubleValue()).summaryStatistics().getSum();
					Assert.state(sum != 0, "支付金额0");
					BigDecimal totalPay = BigDecimal.valueOf(sum).setScale(2, BigDecimal.ROUND_HALF_UP);
					list.forEach(v -> {
						BigDecimal tax = medium_tax_map.getOrDefault("get_channel_tax2", BigDecimal.ZERO);
						tax = tax.add(v.getAmount().multiply(fp_tax_map.get(v.getFapiao())).divide(totalPay, 2, BigDecimal.ROUND_HALF_UP));
						medium_tax_map.put("get_channel_tax2", tax);
					});
				});
				if (medium_tax_map.containsKey("get_channel_tax"))
					get_channel_tax = medium_tax_map.get("get_channel_tax").setScale(2, BigDecimal.ROUND_HALF_UP);
				if (medium_tax_map.containsKey("get_channel_tax2"))
					get_channel_tax2 = medium_tax_map.get("get_channel_tax2").setScale(2, BigDecimal.ROUND_HALF_UP);
			}
			// 查询单个AE或全战队AE
			else {
				// 每个AE分摊的正常税额
				Map<Integer, BigDecimal> AE_tax_map = new HashMap<>();
				// 每个AE分摊的优势号税额
				Map<Integer, BigDecimal> AE_tax_map2 = new HashMap<>();
				normalMap.values().forEach(list -> {
					//单个发票id对应的支付申请总支付额（假定：单个排期税率=发票税率）
					double sum = list.stream().mapToDouble(v -> v.getAmount().doubleValue()).summaryStatistics().getSum();
					Assert.state(sum != 0, "支付金额0");
					BigDecimal totalPay = BigDecimal.valueOf(sum).setScale(2, BigDecimal.ROUND_HALF_UP);
					list.forEach(v -> {
						if (AE.contains(v.getId())) { //命中AE
							BigDecimal tax = AE_tax_map.getOrDefault(v.getId(), BigDecimal.ZERO);
							tax = tax.add(v.getAmount().multiply(fp_tax_map.get(v.getFapiao())).divide(totalPay, 2, BigDecimal.ROUND_HALF_UP));
							AE_tax_map.put(v.getId(), tax);
						}
					});
				});
				advantageMap.values().forEach(list -> {
					//单个发票id对应的支付申请总支付额（假定：单个排期税率=发票税率）
					double sum = list.stream().mapToDouble(v -> v.getAmount().doubleValue()).summaryStatistics().getSum();
					Assert.state(sum != 0, "支付金额0");
					BigDecimal totalPay = BigDecimal.valueOf(sum).setScale(2, BigDecimal.ROUND_HALF_UP);
					list.forEach(v -> {
						if (AE.contains(v.getId())) { //命中AE
							BigDecimal tax = AE_tax_map2.getOrDefault(v.getId(), BigDecimal.ZERO);
							tax = tax.add(v.getAmount().multiply(fp_tax_map.get(v.getFapiao())).divide(totalPay, 2, BigDecimal.ROUND_HALF_UP));
							AE_tax_map2.put(v.getId(), tax);
						}
					});
				});
				// 汇总命中AE的税额
				get_channel_tax = BigDecimal.valueOf(AE_tax_map.values().stream().mapToDouble(v -> v.doubleValue()).summaryStatistics().getSum()).setScale(2, BigDecimal.ROUND_HALF_UP);
				get_channel_tax2 = BigDecimal.valueOf(AE_tax_map2.values().stream().mapToDouble(v -> v.doubleValue()).summaryStatistics().getSum()).setScale(2, BigDecimal.ROUND_HALF_UP);
			}
		}
		BigDecimal total_profit = total_execute.subtract(total_cost).subtract(total_sell).add(total_channel).subtract(total_customer_tax).add(get_channel_tax);
		BigDecimal total_profit2 = total_execute2.subtract(total_cost2).subtract(total_sell2).add(total_channel2).subtract(total_customer_tax2).add(get_channel_tax2);
		Map<String, Object> map = new HashMap<>();
		map.put("count", count);
		map.put("total_execute", total_execute);
		map.put("total_cost", total_cost);
		map.put("total_sell", total_sell);
		map.put("total_channel", total_channel);
		map.put("total_customer_tax", total_customer_tax);
		map.put("get_channel_tax", get_channel_tax);
		map.put("total_profit", total_profit);
		map.put("count2", count2);
		map.put("total_execute2", total_execute2);
		map.put("total_cost2", total_cost2);
		map.put("total_sell2", total_sell2);
		map.put("total_channel2", total_channel2);
		map.put("total_customer_tax2", total_customer_tax2);
		map.put("get_channel_tax2", get_channel_tax2);
		map.put("total_profit2", total_profit2);
		return map;
	}
}
