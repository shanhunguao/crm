package com.ykhd.office.repository;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ykhd.office.domain.bean.IdDecimal;
import com.ykhd.office.domain.bean.IdIdDecimal;
import com.ykhd.office.domain.entity.JcPaymentApp;

public interface JcPaymentAppMapper extends BaseMapper<JcPaymentApp> {

	/**
	 * 排期历史已支付+申请支付中+待复核的总金额
	 */
	@Select("select ifnull(sum(amount), 0) from jc_payment_app where schedule = ${schedule} and type = ${type} and state in (${states})")
	BigDecimal totalByPayAndApp(Integer schedule, Integer type, String states);
	
	/**
	 * 排期历史已支付的总金额
	 */
	@Select("select schedule as id, sum(amount) as amount from jc_payment_app where type = ${type} and state = ${state} and schedule in (${scheduleIds}) group by schedule")
	List<IdDecimal> totalPayByScheduleIds(Integer type, Integer state, String scheduleIds);
	
	/**
	 * 是否存在关于排期的有效支付申请
	 */
	@Select("select count(1) from jc_payment_app where schedule = ${schedule} and state in (${states})")
	int existPaymentApp(Integer schedule, String states);
	
	/**
	 * 根据公众号的名称或微信号查询 支付申请id
	 */
	@Select("select p.id from jc_payment_app p, jc_oa_schedule oas, wm_office_account oa where p.schedule = oas.id and oas.office_account = oa.id "
			+ "and (oa.name like '%${str}%' or oa.wechat like '%${str}%')")
	List<Integer> queryIdsByOaNameWechat(String str);
	
	/**
	 * 发票id -> 支付申请金额、发票id、对应排期的创建人
	 */
	@Select("select p.fapiao as fapiao, p.amount as amount, oas.creator as id, oas.advantage as advantage "
			+ "from jc_payment_app p "
			+ "left join jc_oa_schedule oas on p.schedule = oas.id "
			+ "where p.fapiao in (${fapiao}) and p.type = 0 and oas.state != 9") //成本金额支付，排除作废的排期
	List<IdIdDecimal> queryAmountOasCreatorByFapiao(String fapiaoIds);
}
