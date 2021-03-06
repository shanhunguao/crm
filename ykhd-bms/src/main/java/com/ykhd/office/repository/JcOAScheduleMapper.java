package com.ykhd.office.repository;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ykhd.office.domain.bean.IdCount;
import com.ykhd.office.domain.bean.OAScheduleInfo;
import com.ykhd.office.domain.bean.OAScheduleInfo2;
import com.ykhd.office.domain.bean.OASimpleInfo;
import com.ykhd.office.domain.entity.JcOASchedule;

public interface JcOAScheduleMapper extends BaseMapper<JcOASchedule> {

	/**
	 * 排期id --> id, [join] 公众号名称，微信号，[join] 客户品牌名称
	 * @param ids --> "1,2,3"
	 */
	@Select("select oas.id, oa.name, oa.wechat, oa.fans, c.brand, c.company from jc_oa_schedule oas "
			+ "left join wm_office_account oa on oas.office_account = oa.id "
			+ "left join wm_customer c on oas.customer = c.id "
			+ "where oas.id in (${ids})")
	List<OASimpleInfo> queryIdNameWechatBrand(String ids);
	
	/**
	 * 排期id --> 客户名称、排期人、投放位置、投放日期   [join]  公众号名称
	 * @param ids --> "1,2,3"
	 */
	@Select("select oas.id, oas.creator_name as scheduler, oas.put_date, oas.put_position, "
			+ "oa.name as oaName, c.brand as customerBrand from jc_oa_schedule oas "
			+ "left join wm_office_account oa on oas.office_account = oa.id "
			+ "left join wm_customer c on oas.customer = c.id "
			+ "where oas.id in (${ids})")
	List<OAScheduleInfo> info4PaymentAppList(String ids);
	
	/**
	 * 排期id --> 排期人、投放时间、公众号名称、客户公司、客户品牌
	 * @param ids --> "1,2,3"
	 */
	@Select("select oas.id, oas.creator_name as scheduler, oas.put_date, oa.name as oaName, "
			+ "c.company as customerName, c.brand as customerBrand from jc_oa_schedule oas "
			+ "left join wm_office_account oa on oas.office_account = oa.id "
			+ "left join wm_customer c on oas.customer = c.id "
			+ "where oas.id in (${ids})")
	List<OAScheduleInfo2> info4OASReturnList(String ids);
	
	/**
	 * 公众号id --> 公众号排期次数
	 * @param oaIds  "1,2,3"
	 * @param notStates  排除的状态
	 */
	@Select("select count(*) as count, office_account as id from jc_oa_schedule where office_account in (${oaIds}) and state not in (${notStates}) group by office_account")
	List<IdCount> queryScheduleCount(String oaIds, String notStates);
	
	/**
	 * 客户复投某个公众号的次数
	 * @param oaId
	 * @param customerIds  "1,2,3"
	 * @param notStates  排除的状态
	 */
	@Select("select count(*) as count, customer as id from jc_oa_schedule where office_account = ${oaId} and customer in (${customerIds}) and state not in (${notStates}) group by customer")
	List<IdCount> queryRepeatCount(Integer oaId, String customerIds, String notStates);
	
	/**
	 * 公众号名称  -->  排期id
	 */
	@Select("select oas.id from jc_oa_schedule oas, wm_office_account oa where oas.office_account = oa.id and oa.name like '%${name}%'")
	List<Integer> queryIdByOAName(String name);
	
	/**
	 * 客户公司名或品牌名  -->  排期id
	 */
	@Select("select oas.id from jc_oa_schedule oas, wm_customer c where oas.customer = c.id and (c.company like '%${name}%' or c.brand like '%${name}%')")
	List<Integer> queryIdByCustomer(String name);
	
//	/**
//	 * 媒介业绩排行（按执行价总和从高到低）
//	 */
//	@Select("select sum(oas.execute_price) as total, count(1) as count, m.name as name from wm_oa_schedule oas "
//			+ "left join bms_manager m on oas.medium = m.id "
//			+ "where oas.state in (${states}) and oas.create_time between '${startTime}' and '${endTime}' "
//			+ "group by oas.medium order by total desc")
//	List<MediumRanking> mediumRankingByExecutePrice(String startTime, String endTime, String states);
//	
//	/**
//	 * AE业绩排行（按利润总和从高到低）
//	 */
//	@Select("select sum(oas.execute_price - oas.cost_price - (oas.execute_price * (case oas.cust_fapiao when -1 then 0 else oas.cust_fapiao end) / "
//			+ "(100 + (case oas.cust_fapiao when -1 then 0 else oas.cust_fapiao end))) + "
//			+ "case oas.oa_fapiao_type when 0 then oas.cost_price * (case oas.oa_fapiao when -1 then 0 else oas.oa_fapiao end) / "
//			+ "(100 + (case oas.oa_fapiao when -1 then 0 else oas.oa_fapiao end)) else 0 end - ifnull(sell_expense, 0) + ifnull(channel_expense, 0)) as profit, "
//			+ "count(1) as count, m.name as name, d.name as dept from wm_oa_schedule oas "
//			+ "left join bms_manager m on oas.creator = m.id "
//			+ "left join bms_department d on m.department = d.id "
//			+ "where oas.state in (${states}) and oas.create_time between '${startTime}' and '${endTime}' "
//			+ "group by oas.creator order by profit desc")
//	List<AERanking> AERankingByProfit(String startTime, String endTime, String states);
//	
//	/**
//	 * 每个AE全年月度汇总
//	 */
//	@Select("select m.name, date_format(oas.create_time, '%Y-%m') as month, sum(execute_price - cost_price - (execute_price * "
//			+ "(case cust_fapiao when -1 then 0 else cust_fapiao end) / (100 + (case cust_fapiao when -1 then 0 else cust_fapiao end))) + "
//			+ "case oa_fapiao_type when 0 then cost_price * (case oa_fapiao when -1 then 0 else oa_fapiao end) / (100 + (case oa_fapiao when -1 then 0 else oa_fapiao end)) "
//			+ "else 0 end - ifnull(sell_expense, 0) + ifnull(channel_expense, 0)) AS profit "
//			+ "from wm_oa_schedule oas left JOIN bms_manager m on oas.creator = m.id "
//			+ "where oas.state in(${states}) and date_format(oas.create_time, '%Y') = '${year}' "
//			+ "GROUP BY oas.creator, date_format(oas.create_time, '%Y-%m') order by month desc, profit desc")
//	List<ChampByMonth> monthProfitByYear(String year, String states);
}
