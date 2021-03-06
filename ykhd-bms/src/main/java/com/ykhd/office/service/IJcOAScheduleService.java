package com.ykhd.office.service;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ykhd.office.domain.bean.OAScheduleInfo;
import com.ykhd.office.domain.bean.OAScheduleInfo2;
import com.ykhd.office.domain.bean.OASimpleInfo;
import com.ykhd.office.domain.bean.common.PageHelpers;
import com.ykhd.office.domain.entity.JcOASchedule;
import com.ykhd.office.domain.req.OAScheduleCondition;
import com.ykhd.office.domain.resp.OAScheduleDto;

public interface IJcOAScheduleService extends IService<JcOASchedule> {

	/**
	 * 确认公众号排期，生成协议排期
	 */
	boolean confirm_create(Integer oaScheduleId, Integer oaFapiao, Integer oaFapiaoType);
	
	/**
	 * 排期列表
	 */
	PageHelpers<OAScheduleDto> getListByPage(OAScheduleCondition condition);
	
	/**
	 * 导出excel
	 */
	void exportExcel(OAScheduleCondition condition, HttpServletResponse response);
	
//	/**
//	 * 新增排期
//	 */
//	boolean addOASchedule(OAScheduleSubmit submit);
//	
//	/**
//	 * 更改排期为提交状态
//	 */
//	int submitState(Integer... id);
//	
//	/**
//	 * 审核排期
//	 * @param result 0不通过  1通过
//	 */
//	int review(int result, String reason, Integer... id);
//	
//	/**
//	 * 确认排期
//	 */
//	int confirm(String remark, Integer... id);
//	
//	/**
//	 * 申请作废
//	 */
//	int appCancel(Integer... id);
//	
//	/**
//	 * 审核作废
//	 */
//	int reviewCancel(int result, String reason, Integer... id);
//	
//	/**
//	 * 确认作废
//	 */
//	int confirmCancel(int result, String reason, Integer... id);
//	
//	/**
//	 * 直接修改未提交或审核失败的排期
//	 */
//	boolean updateInfo(OAScheduleEdit edit);
//	
//	/**
//	 * 删除排期
//	 */
//	int deleteOASchedule(Integer... id);
	
	/**
	 * API: 检查“已回款”后，检查排期是否达到“已完成”
	 */
	void checkReturnFinish(Integer id, BigDecimal total, Date return_time);
	
	/**
	 * API: 检查排期是否达到“已支付”、“已完成”状态
	 * <p> 1成本价已全额支付    2销售费用已支付   3已回款 </p>
	 */
	void checkPayFinish(Integer id);
	
//	/**
//	 * API：申请修改通过审核后更新排期内容
//	 */
//	boolean updateEdit(OASchedule schedule, Integer id, OAScheduleEdit edit);
	
	/**
	 * API: 排期id --> 查询公众号名称，微信号，客户品牌名称
	 */
	Map<Integer, OASimpleInfo> queryIdOaNameWechatBrand(Collection<Integer> collection);
	
	/**
	 * API: 排期id --> 客户品牌名称、排期人、投放位置、投放日期   [join]  公众号名称
	 */
	Map<Integer, OAScheduleInfo> info4PaymentAppList(Collection<Integer> collection);
	
	/**
	 * API: 排期id --> 排期人、投放时间、公众号名称、客户公司、客户品牌
	 */
	Map<Integer, OAScheduleInfo2> info4OASReturnList(Collection<Integer> collection);
	
	/**
	 * API: creator --> id
	 */
	List<Integer> queryIdByCreator(Integer creator);
	
	/**
	 * API: oaName  -->  排期id
	 */
	List<Integer> queryIdByOAName(String name);
	
	/**
	 * 客户公司名或品牌名  -->  排期id
	 */
	List<Integer> queryIdByCustomer(String name);
	
	/**
	 * API: 公众号id --> 排期次数
	 */
	Map<Integer, Integer> queryScheduleCount(Collection<Integer> collection);
	
//	/**
//	 * API: id -> mediumName
//	 */
//	Map<Integer, String> queryMediumName(Collection<Integer> collection);
	
	/**
	 * API : 移除已支付状态
	 */
	void removeIsPay(Integer id);
}
