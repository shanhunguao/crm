package com.ykhd.office.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ykhd.office.controller.BaseController;
import com.ykhd.office.domain.bean.LoginCache;
import com.ykhd.office.domain.bean.OASimpleInfo;
import com.ykhd.office.domain.bean.common.PageHelper;
import com.ykhd.office.domain.entity.EditRecord;
import com.ykhd.office.domain.entity.OASchedule;
import com.ykhd.office.domain.req.OAScheduleEdit;
import com.ykhd.office.domain.resp.EditRecordCondition;
import com.ykhd.office.domain.resp.OAScheduleEditDto;
import com.ykhd.office.repository.EditRecordMapper;
import com.ykhd.office.service.IEditRecordService;
import com.ykhd.office.service.IOAScheduleService;
import com.ykhd.office.service.IPaymentAppService;
import com.ykhd.office.service.IRoleService;
import com.ykhd.office.util.JacksonHelper;
import com.ykhd.office.util.MathUtil;
import com.ykhd.office.util.dictionary.StateEnums.State4EditRecord;
import com.ykhd.office.util.dictionary.StateEnums.State4OASchedule;
import com.ykhd.office.util.dictionary.SystemEnums.RoleSign;
import com.ykhd.office.util.dictionary.TypeEnums.Type4ApprovalMsg;
import com.ykhd.office.util.dictionary.TypeEnums.Type4Edit;

@Service
public class EditRecordService extends ServiceImpl<BaseMapper<EditRecord>, EditRecord> implements IEditRecordService {

	@Autowired
	private EditRecordMapper editRecordMapper;
	@Autowired
	private IOAScheduleService oAScheduleService;
	@Autowired
	private IRoleService roleService;
	@Autowired
	private ApprovalMsgService approvalMsgService;
	@Autowired
	private IPaymentAppService paymentAppService;
	
	/**
	 * 新增修改记录
	 */
	private boolean addEditRecord(Integer type, Integer identity, String content, Integer reviewer, State4EditRecord... state) {
		EditRecord record = new EditRecord();
		record.setType(type);
		record.setIdentity(identity);
		record.setContent(content);
		if (state.length == 0)
			record.setState(State4EditRecord.待审核.code());
		else
			record.setState(state[0].code());
		LoginCache managerInfo = BaseController.getManagerInfo();
		record.setCreator(managerInfo.getId());
		record.setCreatorName(managerInfo.getName());
		record.setCreateTime(new Date());
		record.setReviewer(reviewer);
		return save(record);
	}
	
	@Override
	@Transactional
	public boolean addOAScheduleEditRecord(OAScheduleEdit edit) {
		//Assert.state(edit.getCostPrice().doubleValue() < edit.getExecutePrice().doubleValue(), "执行价必须大于成本价");
		OASchedule schedule = oAScheduleService.getById(edit.getId());
		Assert.notNull(schedule, "未知排期");
		int state = schedule.getState().intValue();
		// 不涉及到金钱的修改，则直接审核通过并修改成功。
		if (edit.getExecutePrice().doubleValue() == schedule.getExecutePrice().doubleValue() &&
				edit.getCostPrice().doubleValue() == schedule.getCostPrice().doubleValue() &&
				(edit.getSellExpense() == null ? 0 :edit.getSellExpense().doubleValue()) == 
				(schedule.getSellExpense() == null ? 0 :schedule.getSellExpense().doubleValue()) && 
				(edit.getChannelExpense() == null ? 0 :edit.getChannelExpense().doubleValue()) == 
				(schedule.getChannelExpense() == null ? 0 :schedule.getChannelExpense().doubleValue())) {
			Assert.state(state != State4OASchedule.作废.code() && state != State4OASchedule.已完成.code(), "作废或已完成不可修改");
			Assert.state(addEditRecord(Type4Edit.排期.code(), schedule.getId(), JacksonHelper.toJsonStr(edit), schedule.getReviewer(), State4EditRecord.审核通过), "申请失败");
			Assert.state(oAScheduleService.updateEdit(schedule, null, edit), "排期修改失败");
			return true;
		} else {
			Assert.state(state == State4OASchedule.提交待审核.code() ||
					state == State4OASchedule.待确认.code() || state == State4OASchedule.待结算.code(), "仅限待审核待确认待结算状态");
			if (state == State4OASchedule.待结算.code()) {
				// 判断是否存在有效的支付申请
				Assert.state(!paymentAppService.existPaymentApp(schedule.getId()), "已申请过支付，不能修改。");
			}
			boolean boo = addEditRecord(Type4Edit.排期.code(), schedule.getId(), JacksonHelper.toJsonStr(edit), schedule.getReviewer());
			if (boo)
				// msg
				approvalMsgService.sendApprovalMsg(Type4ApprovalMsg.公众号排期修改申请, schedule.getReviewer());
			return boo;
		}
	}
	
	@Override
	public int deleteEditRecord(Integer... id) {
		Integer loginer = BaseController.getManagerInfo().getId();
		QueryWrapper<EditRecord> queryWrapper;
		int success = 0;
		for (Integer _id : id) {
			queryWrapper = new QueryWrapper<EditRecord>()
					.eq("id", _id)
					.eq("creator", loginer)
					.eq("state", State4EditRecord.审核未通过.code());
			success += editRecordMapper.delete(queryWrapper);
		}
		Assert.state(success > 0, "仅限自己创建并未通过审核状态");
		return success;
	}
	
	/**
	 * 查看修改记录列表
	 * @param searcherType  creator|reviewer
	 */
	private IPage<EditRecord> getList(Type4Edit type, Integer searcherId, EditRecordCondition condition) {
		QueryWrapper<EditRecord> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("type", type.code());
		RoleSign sign = roleService.judgeRole(BaseController.getManagerInfo().getRole());
		// AE角色
		if (sign == RoleSign.ae)
			queryWrapper.eq("creator", searcherId);
		else if (sign == RoleSign.dept)
			queryWrapper.eq("reviewer", searcherId);
		if (condition.getState() != null)
			queryWrapper.eq("state", condition.getState());
		queryWrapper.orderByDesc("id");
		IPage<EditRecord> iPage = page(new Page<>(condition.getPage(), condition.getSize()), queryWrapper);
		return iPage;
	}

	@Override
	public PageHelper<OAScheduleEditDto> oAScheduleEditList(EditRecordCondition condition) {
		List<OAScheduleEditDto> list;
		IPage<EditRecord> iPage = getList(Type4Edit.排期, BaseController.getManagerInfo().getId(), condition);
		List<EditRecord> records = iPage.getRecords();
		if (records.isEmpty())
			list = Collections.emptyList();
		else {
			list = new ArrayList<>(records.size());
			Set<Integer> identitySet = records.stream().map(v -> v.getIdentity()).collect(Collectors.toSet());
			Map<Integer, OASimpleInfo> oaMap = oAScheduleService.queryIdOaNameWechatBrand(identitySet);
			Map<Integer, String> nameMap = oAScheduleService.queryMediumName(identitySet);
			records.forEach(v -> {
				OAScheduleEditDto dto = new OAScheduleEditDto();
				BeanUtils.copyProperties(v, dto);
				dto.setSchedule(v.getIdentity());
				OAScheduleEdit edit = JacksonHelper.toParseObj(v.getContent(), OAScheduleEdit.class);
				BeanUtils.copyProperties(edit, dto, "id");
				// 显示客户名称公众号名称微信号
				OASimpleInfo info = oaMap.get(v.getIdentity());
				if (info != null) {
					dto.setOaName(info.getName());
					dto.setWechat(info.getWechat());
					dto.setCustomerName(info.getBrand());
				}
				// 计算税额，利润。
				String[] array = MathUtil.calculateProfitMore(dto.getExecutePrice(), dto.getCostPrice(), dto.getCustFapiao() == -1 ? 0 : dto.getCustFapiao(),
						dto.getOaFapiaoType() != null && dto.getOaFapiaoType().intValue() == 0 ? (dto.getOaFapiao() == -1 ? 0 : dto.getOaFapiao()) : 0, dto.getSellExpense(), dto.getChannelExpense());
				dto.setCustomerTax(array[0]);
				dto.setChannelTax(array[1]);
				dto.setProfit(array[2]);
				dto.setProfitRate(array[3]);
				// 显示媒介名称
				dto.setMediumName(nameMap.get(v.getIdentity()));
				list.add(dto);
			});
		}
		return new PageHelper<OAScheduleEditDto>(condition.getPage(), condition.getSize(), iPage.getTotal(), list);
	}

	@Override
	@Transactional
	public int reviewOAScheduleEditRecord(int result, String reason, Integer... id) {
		Assert.state(result == 0 || result == 1, "result参数非法");
		UpdateWrapper<EditRecord> updateWrapper;
		int success = 0;
		for (Integer _id : id) {
			updateWrapper = new UpdateWrapper<EditRecord>().eq("id", _id)
				.eq("type", Type4Edit.排期.code())
				.eq("state", State4EditRecord.待审核.code());
			if (result == 0) {
				updateWrapper.set("state", State4EditRecord.审核通过.code());
				if (editRecordMapper.update(null, updateWrapper) == 1) {
					success += 1;
					// 更新排期
					EditRecord record = getById(_id);
					Assert.state(oAScheduleService.updateEdit(null, record.getIdentity(), 
							JacksonHelper.toParseObj(record.getContent(), OAScheduleEdit.class)), "排期修改失败");
				}
			} else {
				updateWrapper.set("state", State4EditRecord.审核未通过.code())
					.set("failure_reason", reason);
				success += editRecordMapper.update(null, updateWrapper);
			}
		}
		Assert.state(success > 0, "仅限待审核的状态");
		// msg
		approvalMsgService.sendApprovalMsg(Type4ApprovalMsg.公众号排期修改申请, BaseController.getManagerInfo().getId());
		return success;
	}

	@Override
	public boolean existsOngoing(Type4Edit type, Integer identity) {
		QueryWrapper<EditRecord> queryWrapper = new QueryWrapper<EditRecord>()
				.eq("type", type.code()).eq("identity", identity)
				.eq("state", State4EditRecord.待审核.code());
		return count(queryWrapper) > 0 ? true : false;
	}

	@Override
	public boolean existsOngoing(Type4Edit type, List<Integer> identitys) {
		QueryWrapper<EditRecord> queryWrapper = new QueryWrapper<EditRecord>()
				.eq("type", type.code()).in("identity", identitys)
				.eq("state", State4EditRecord.待审核.code());
		return count(queryWrapper) > 0 ? true : false;
	}

}
