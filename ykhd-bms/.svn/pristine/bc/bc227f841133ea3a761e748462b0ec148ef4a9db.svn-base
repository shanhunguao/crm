package com.ykhd.office.service;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ykhd.office.domain.bean.OASimpleInfo2;
import com.ykhd.office.domain.bean.common.PageHelper;
import com.ykhd.office.domain.entity.OfficeAccount;
import com.ykhd.office.domain.req.OfficeAccountCondition;
import com.ykhd.office.domain.req.OfficeAccountSubmit;
import com.ykhd.office.domain.resp.OfficeAccountDto;

public interface IOfficeAccountService extends IService<OfficeAccount> {

	/**
	 * 公众号分页列表
	 */
	PageHelper<OfficeAccountDto> getListByPage(OfficeAccountCondition condition);

	/**
	 * 公众号详情
	 */
	OfficeAccount getDetail(Integer id);
	
	/**
	 * 添加公众号
	 */
	boolean addOfficeAccount(OfficeAccountSubmit submit);
	
	/**
	 * 修改公众号
	 */
	boolean updateOfficeAccount(OfficeAccountSubmit submit);
	
	/**
	 * 提交启用
	 */
	int submitUsing(Integer... id);
	
	/**
	 * 待开发转化启用
	 */
	boolean changeUsing(OfficeAccountSubmit submit);
	
	/**
	 * 申请作废
	 */
	int appCancel(Integer... id);
	
	/**
	 * 处理作废申请
	 * @param result 0同意  1驳回
	 */
	int dealCancel(int result, Integer... id);
	
	/**
	 * 直接作废
	 */
	int cancel(Integer... id);
	
	/**
	 * 评分
	 */
	boolean score(Integer id, String score, String comment);
	
	/**
	 * 申请成为优势号
	 */
	boolean advanageApp(Integer id);
	
	/**
	 * 申请取消优势号
	 */
	boolean advanageCancel(Integer id);
	
	/**
	 * 审批优势号申请
	 */
	int advantageReview(Integer result, Integer... id);
	
	/**
	 * 审批优势号取消
	 */
	int advantageReview2(Integer result, Integer... id);
	
	/**
	 * 升级为协议号
	 */
	boolean up2Agreement(Integer id, BigDecimal price);
	
	/**
	 * API：更换对接媒介、最后更新时间
	 */
	boolean changeMedium(Integer id, Integer mediumId);
	
	/**
	 * API: name -> ids
	 */
	List<Integer> queryIdsByName(String name);
	
	/**
	 * API: 是否优势号
	 */
	boolean isAdvantage(Integer id);
	
	/**
	 * API: 是否协议号
	 */
	boolean isAgreement(Integer id);
	
	/**
	 * API: 查询采购价
	 */
	BigDecimal queryCollectPrice(Integer id);
	
	/**
	 * API: 查询公众号的名称、微信号、所属分类
	 */
	Map<Integer, OASimpleInfo2> queryNameWechatCategory(Collection<Integer> ids);

}
