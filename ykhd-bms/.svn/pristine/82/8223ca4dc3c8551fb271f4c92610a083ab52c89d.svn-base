package com.ykhd.office.service;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ykhd.office.domain.bean.common.KeyValuePair;
import com.ykhd.office.domain.bean.common.PageHelpers;
import com.ykhd.office.domain.entity.PaymentApp;
import com.ykhd.office.domain.req.PaymentAppCondition;
import com.ykhd.office.domain.req.PaymentAppSubmit;
import com.ykhd.office.domain.resp.PaymentAppDto;

public interface IPaymentAppService extends IService<PaymentApp> {

	/**
	 * 分页列表
	 */
	PageHelpers<PaymentAppDto> getListByPage(PaymentAppCondition condition);
	
	/**
	 * 导出excel
	 */
	void exportExcel(PaymentAppCondition condition, HttpServletResponse response);
	
	/**
	 * 新增支付申请
	 */
	boolean addPaymentApp(PaymentAppSubmit submit);
	
	/**
	 * 驳回申请
	 */
	int refuse(Integer... id);
	
	/**
	 * 付款
	 */
	boolean pay(List<Integer> ids, MultipartFile[] files, String date, String remark);
	
	/**
	 * 付款复核
	 */
	boolean check(Integer id, List<String> oldPath, MultipartFile[] files, String date, String remark);
	
	/**
	 * 批量复核通过
	 */
	boolean checkBatch(List<Integer> ids, MultipartFile[] files);
	
	/**
	 * 驳回支付
	 */
	int checkFailure(Integer... id);
	
//	/**
//	 * 更新支付信息
//	 */
//	boolean updatePayInfo(Integer id, MultipartFile file, String date, String remark);
	
	/**
	 * 删除
	 */
	boolean deletePaymentApp(Integer id);
	
	/**
	 * API: 排期已支付总额  scheduleId -->  sum(amount)
	 */
	Map<Integer, BigDecimal> queryTotalPayByScheId(Collection<Integer> collection);
	
	/**
	 * API: 排期历史已支付+申请支付中+待复核的总金额
	 */
	BigDecimal totalByPayAndApp(Integer schedule);
	
	/**
	 * API: 是否存在关于排期的有效支付申请
	 */
	boolean existPaymentApp(Integer schedule);
	
	/**
	 * API: 回填进项发票id
	 */
	boolean setFapiao(Integer fapiaoId, List<Integer> ids);
	
	/**
	 * API: 清空进项发票id
	 */
	boolean clearFapiao(Integer fapiaoId);
	
	/**
	 * API: scheduleId --> payRemark
	 */
	Map<Integer, String> queryPayRemarkByScheId(Collection<Integer> collection);
	/**
	 * API：payRemark --> scheduleId
	 */
	List<Integer> queryOascheIdByPayRemark(String payRemark);
	
	/**
	 * API：scheduleId --> 是否已收到进项发票
	 */
	Map<Integer, Boolean> ifHaveFapiao(Collection<Integer> collection);
	
	/**
	 * API：已收进项发票的 scheduleId
	 */
	List<Integer> havaFapiao();
	/**
	 * API：已支付未收进项发票的 scheduleId
	 */
	List<Integer> noHavaFapiao();
	
	/**
	 * API: 排期id -> 已完成支付的<支付费用总额,  销售费用总额>
	 */
	KeyValuePair<BigDecimal, BigDecimal> totalPayByScheId(Integer scheduleId);
	
	/**
	 * API: 自动创建（全款支付记录 + 进项发票）
	 */
	boolean autoCreate(Integer scheduleId);
}
