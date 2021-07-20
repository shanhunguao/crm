package com.ykhd.office.service.impl;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
import com.ykhd.office.domain.bean.LoginCache;
import com.ykhd.office.domain.bean.OASimpleInfo2;
import com.ykhd.office.domain.bean.common.PageHelper;
import com.ykhd.office.domain.entity.OfficeAccount;
import com.ykhd.office.domain.req.OfficeAccountCondition;
import com.ykhd.office.domain.req.OfficeAccountSubmit;
import com.ykhd.office.domain.resp.OfficeAccountDto;
import com.ykhd.office.repository.OfficeAccountMapper;
import com.ykhd.office.service.IContentChangeService;
import com.ykhd.office.service.IManagerService;
import com.ykhd.office.service.IOACollectionService;
import com.ykhd.office.service.IOAScheduleService;
import com.ykhd.office.service.IOfficeAccountService;
import com.ykhd.office.service.IRoleService;
import com.ykhd.office.util.dictionary.StateEnums.State4OASchedule;
import com.ykhd.office.util.dictionary.StateEnums.State4OfficeAccount;
import com.ykhd.office.util.dictionary.SystemEnums.RoleSign;
import com.ykhd.office.util.dictionary.TypeEnums.Type4ApprovalMsg;
import com.ykhd.office.util.dictionary.TypeEnums.Type4Change;

@Service
public class OfficeAccountService extends ServiceImpl<BaseMapper<OfficeAccount>, OfficeAccount> implements IOfficeAccountService {
	
	@Autowired
	private OfficeAccountMapper officeAccountMapper;
	@Autowired
	private IOAScheduleService oAScheduleService;
	@Autowired
	private IOACollectionService oACollectionService;
	@Autowired
	private ApprovalMsgService approvalMsgService;
	@Autowired
	private IRoleService roleService;
	@Autowired
	private IContentChangeService contentChangeService;
	@Autowired
	private IManagerService managerService;

	@Override
	public PageHelper<OfficeAccountDto> getListByPage(OfficeAccountCondition condition) {
		List<Integer> collectionList = oACollectionService.getMyCollection();
		/*【按排期次数排序查询】*/
		if ("s".equals(condition.getSort()) || "sd".equals(condition.getSort())) {
			StringBuilder sb = new StringBuilder();
			if (condition.getState() != null)
				sb.append("oa.state = ").append(condition.getState());
			else
				sb.append("oa.state != ").append(State4OfficeAccount.已作废.code());
			if (StringUtils.hasText(condition.getCategory()))
				sb.append(" and oa.category = '").append(condition.getCategory().trim()).append("'");
			if (StringUtils.hasText(condition.getName()))
				sb.append(" and (oa.name like '%").append(condition.getName().trim()).append("%'").append(" or oa.wechat like '%").append(condition.getName().trim()).append("%')");
			if (condition.getMedium() != null)
				sb.append(" and (oa.creator = ").append(condition.getMedium()).append(" or oa.advantage_medium = ").append(condition.getMedium()).append(")");
			if (condition.getFans_lower() != null)
				sb.append(" and oa.fans >= ").append(condition.getFans_lower());
			if (condition.getFans_upper() != null)
				sb.append(" and oa.fans <= ").append(condition.getFans_upper());
			if (StringUtils.hasText(condition.getCooperateMode()))
				sb.append(" and oa.cooperate_mode like '%").append(condition.getCooperateMode()).append("%'");
			if (StringUtils.hasText(condition.getCustomerType()))
				sb.append(" and oa.customer_type like '%").append(condition.getCustomerType()).append("%'");
			if (condition.getHeadlineCost_lower() != null)
				sb.append(" and oa.headline_cost >= ").append(condition.getHeadlineCost_lower());
			if (condition.getHeadlineCost_upper() != null)
				sb.append(" and oa.headline_cost <= ").append(condition.getHeadlineCost_upper());
			if (condition.getCpaPrice_lower() != null)
				sb.append(" and oa.cpa_price >= ").append(condition.getCpaPrice_lower());
			if (condition.getCpaPrice_upper() != null)
				sb.append(" and oa.cpa_price <= ").append(condition.getCpaPrice_upper());
			if (condition.getCollection() != null) {
				if (collectionList.isEmpty())
					return new PageHelper<OfficeAccountDto>(condition.getPage(), condition.getSize(), 0, Collections.emptyList());
				sb.append(" and oa.id in (").append(collectionList.stream().map(v -> String.valueOf(v)).collect(Collectors.joining(","))).append(")");
			}
			if (condition.getAdvantage() != null)
				sb.append(" and oa.advantage = ").append(condition.getAdvantage());
			if (StringUtils.hasText(condition.getPhone()) && roleService.judgeRole(BaseController.getManagerInfo().getRole()) == RoleSign.medium)
				sb.append(" and oa.phone = '").append(condition.getPhone().trim()).append("'");
			if (condition.getAgreement() != null)
				sb.append(" and oa.agreement = 1");
			String notStates = State4OASchedule.审核不通过.code() + "," + State4OASchedule.作废.code();
			List<OfficeAccountDto> list = officeAccountMapper.getOAListByScheduleCount(notStates, sb.toString(), "s".equals(condition.getSort()) ? "asc": "desc", 
					(condition.getPage() - 1) * condition.getSize(), condition.getSize());
			int total = officeAccountMapper.total(sb.toString());
			Set<Integer> mediumIds = list.stream().flatMap(v -> Stream.of(v.getCreator(), v.getMediumId(), v.getAdvantageMedium())).collect(Collectors.toSet());
			Map<Integer, String> mediumName = managerService.queryName(mediumIds);
			list.forEach(v -> {
				// 查询收藏
				v.setIsCollection(collectionList.contains(v.getId()));
				// 三种媒介名称
				v.setCreatorName(v.getCreator() == null ? "" : mediumName.get(v.getCreator()));
				v.setMediumName(v.getMediumId() == null ? "" : mediumName.get(v.getMediumId()));
				if (v.getAdvantage() != null && v.getAdvantage() == 1 && v.getAdvantageMedium() != null)
					v.setMediumName(mediumName.get(v.getAdvantageMedium()));
			});
			return new PageHelper<OfficeAccountDto>(condition.getPage(), condition.getSize(), total, list);
		}
		/*【默认按最近更新时间降序查询】*/
		else {
			List<OfficeAccountDto> list;
			QueryWrapper<OfficeAccount> queryWrapper = new QueryWrapper<>();
			if (condition.getState() != null)
				queryWrapper.eq("state", condition.getState());
			else // 默认排除已作废
				queryWrapper.ne("state", State4OfficeAccount.已作废.code());
			if (StringUtils.hasText(condition.getCategory()))
				queryWrapper.eq("category", condition.getCategory().trim());
			if (StringUtils.hasText(condition.getName()))
				queryWrapper.and(wrap -> wrap.like("name", condition.getName().trim()).or().like("wechat", condition.getName().trim()));
			if (condition.getMedium() != null)
				queryWrapper.and(wrap -> wrap.eq("creator", condition.getMedium()).or().eq("advantage_medium", condition.getMedium()));
			if (condition.getFans_lower() != null)
				queryWrapper.ge("fans", condition.getFans_lower());
			if (condition.getFans_upper() != null)
				queryWrapper.le("fans", condition.getFans_upper());
			if (StringUtils.hasText(condition.getCooperateMode()))
				queryWrapper.like("cooperate_mode", condition.getCooperateMode());
			if (StringUtils.hasText(condition.getCustomerType()))
				queryWrapper.like("customer_type", condition.getCustomerType());
			if (condition.getHeadlineCost_lower() != null)
				queryWrapper.ge("headline_cost", condition.getHeadlineCost_lower());
			if (condition.getHeadlineCost_upper() != null)
				queryWrapper.le("headline_cost", condition.getHeadlineCost_upper());
			if (condition.getCpaPrice_lower() != null)
				queryWrapper.ge("cpa_price", condition.getCpaPrice_lower());
			if (condition.getCpaPrice_upper() != null)
				queryWrapper.le("cpa_price", condition.getCpaPrice_upper());
			if (StringUtils.hasText(condition.getPhone()) && roleService.judgeRole(BaseController.getManagerInfo().getRole()) == RoleSign.medium)
				queryWrapper.eq("phone", condition.getPhone().trim());
			if (condition.getCollection() != null) {
				if (collectionList.isEmpty())
					return new PageHelper<OfficeAccountDto>(condition.getPage(), condition.getSize(), 0, Collections.emptyList());
				queryWrapper.in("id", collectionList);
			}
			if (condition.getAdvantage() != null)
				queryWrapper.eq("advantage", condition.getAdvantage());
			if (condition.getAgreement() != null)
				queryWrapper.eq("agreement", 1);
			// 排序
			if ("c".equals(condition.getSort())) //直投成本价 升序
				queryWrapper.orderByAsc("headline_cost");
			else if ("cd".equals(condition.getSort())) //直投成本价 降序
				queryWrapper.orderByDesc("headline_cost");
			else
				queryWrapper.orderByDesc("last_update_time");
			IPage<OfficeAccount> iPage = page(new Page<>(condition.getPage(), condition.getSize()), queryWrapper);
			List<OfficeAccount> records = iPage.getRecords();
			if (records.isEmpty())
				list = Collections.emptyList();
			else {
				list = new ArrayList<>(records.size());
				List<Integer> oaIds = records.stream().map(v -> v.getId()).collect(Collectors.toList());
				Map<Integer, Integer> countMap = oAScheduleService.queryScheduleCount(oaIds);
				Set<Integer> mediumIds = records.stream().flatMap(v -> Stream.of(v.getCreator(), v.getMediumId(), v.getAdvantageMedium())).collect(Collectors.toSet());
				Map<Integer, String> mediumName = managerService.queryName(mediumIds);
				records.forEach(v -> {
					OfficeAccountDto dto = new OfficeAccountDto();
					BeanUtils.copyProperties(v, dto);
					// 查询排期次数
					Integer count = countMap.get(dto.getId());
					dto.setScheduleCount(count == null ? 0 : count);
					// 查询收藏
					dto.setIsCollection(collectionList.contains(v.getId()));
					// 三种媒介名称
					dto.setCreatorName(v.getCreator() == null ? "" : mediumName.get(v.getCreator()));
					dto.setMediumName(v.getMediumId() == null ? "" : mediumName.get(v.getMediumId()));
					if (v.getAdvantage() != null && v.getAdvantage() == 1 && v.getAdvantageMedium() != null)
						dto.setMediumName(mediumName.get(v.getAdvantageMedium()));
					list.add(dto);
				});
			}
			return new PageHelper<OfficeAccountDto>(condition.getPage(), condition.getSize(), iPage.getTotal(), list);
		}
	}

	@Override
	public OfficeAccount getDetail(Integer id) {
		OfficeAccount officeAccount = getById(id);
		Assert.notNull(officeAccount, "未知公众号");
		LoginCache managerInfo = BaseController.getManagerInfo();
		RoleSign sign = roleService.judgeRole(managerInfo.getRole());
		// 媒介、集采、待开发状态显示联系方式
		if (sign != RoleSign.medium && sign != RoleSign.collector && officeAccount.getState() != State4OfficeAccount.待开发.code())
			officeAccount.setPhone(null);
		// 非集采人员及总经理不显示采购价
		if (sign != RoleSign.collector && sign != RoleSign.general_mgr)
			officeAccount.setCollectPrice(null);
		return officeAccount;
	}
	
	@Override
	@Transactional
	public boolean addOfficeAccount(OfficeAccountSubmit submit) {
		Assert.state(count(new QueryWrapper<OfficeAccount>().eq("wechat", submit.getWechat())) == 0, "公众号已存在");
		OfficeAccount officeAccount = new OfficeAccount();
		BeanUtils.copyProperties(submit, officeAccount);
		LoginCache managerInfo = BaseController.getManagerInfo();
		officeAccount.setCreator(managerInfo.getId());
		officeAccount.setCreateTime(new Date());
		officeAccount.setLastUpdateTime(officeAccount.getCreateTime());
		if (submit.getUnDevelop() == 0)
			officeAccount.setState(State4OfficeAccount.创建.code());
		else
			officeAccount.setState(State4OfficeAccount.待开发.code());
//		if (roleService.judgeRole(managerInfo.getRole()) == RoleSign.collector)
//			officeAccount.setAgreement(1); // 协议号
		return save(officeAccount);
	}
	
	@Override
	public boolean updateOfficeAccount(OfficeAccountSubmit submit) {
		Assert.state(count(new QueryWrapper<OfficeAccount>().ne("id", submit.getId()).eq("wechat", submit.getWechat())
				.ne("state", State4OfficeAccount.已作废.code())) == 0, "公众号已存在");
		OfficeAccount officeAccount = getById(submit.getId());
		Assert.state(officeAccount.getState().intValue() != State4OfficeAccount.申请作废中.code() && 
				officeAccount.getState().intValue() != State4OfficeAccount.已作废.code(), "作废申请中或已作废的不可修改");
		String change = contentChange(officeAccount, submit);
		BeanUtils.copyProperties(submit, officeAccount);
		officeAccount.setLastUpdateTime(new Date());
		boolean boo = updateById(officeAccount);
		if (boo && change.length() > 0)
			Assert.state(contentChangeService.addContentChange(Type4Change.公众号, submit.getId(), change), "内容变更记录失败");
		return boo;
	}
	
	@Override
	public int submitUsing(Integer... id) {
		UpdateWrapper<OfficeAccount> updateWrapper;
		int success = 0;
		for (Integer _id : id) {
			updateWrapper = new UpdateWrapper<OfficeAccount>()
				.eq("id", _id)
				.eq("state", State4OfficeAccount.创建.code())
				.set("state", State4OfficeAccount.提交启用.code())
				.set("last_update_time", new Date());
			success += officeAccountMapper.update(null, updateWrapper);
		}
		Assert.state(success > 0, "非创建状态不能提交");
		return success;
	}

	@Override
	public boolean changeUsing(OfficeAccountSubmit submit) {
		OfficeAccount officeAccount = getById(submit.getId());
		Assert.notNull(officeAccount, "未知公众号");
		Assert.state(officeAccount.getState().intValue() == State4OfficeAccount.待开发.code(), "无需转化");
		BeanUtils.copyProperties(submit, officeAccount);
		officeAccount.setState(State4OfficeAccount.提交启用.code());
		officeAccount.setLastUpdateTime(new Date());
		officeAccount.setMediumId(BaseController.getManagerInfo().getId()); // 转化并领用
		return updateById(officeAccount);
	}

	@Override
	public int appCancel(Integer... id) {
		UpdateWrapper<OfficeAccount> updateWrapper;
		int success = 0;
		for (Integer _id : id) {
			updateWrapper = new UpdateWrapper<OfficeAccount>()
				.eq("id", _id)
				.eq("state", State4OfficeAccount.提交启用.code())
				.set("state", State4OfficeAccount.申请作废中.code())
				.set("last_update_time", new Date());
			success += officeAccountMapper.update(null, updateWrapper);
		}
		Assert.state(success > 0, "仅限启用中的公众号");
		// 普通号广告部主管审批，协议号集采部门审批。
		List<OfficeAccount> list = list(new QueryWrapper<OfficeAccount>().select("agreement").in("id", Arrays.asList(id)));
		int agree = list.stream().filter(v -> v != null).collect(Collectors.toList()).size();
		if (agree == 0)
			approvalMsgService.sendApprovalMsg(Type4ApprovalMsg.公众号作废申请, null);
		else if (agree == list.size())
			approvalMsgService.sendApprovalMsg(Type4ApprovalMsg.协议号作废申请, null);
		else {
			approvalMsgService.sendApprovalMsg(Type4ApprovalMsg.公众号作废申请, null);
			approvalMsgService.sendApprovalMsg(Type4ApprovalMsg.协议号作废申请, null);
		}
		return success;
	}

	@Override
	public int dealCancel(int result, Integer... id) {
		Assert.state(result == 0 || result == 1, "result参数非法");
		UpdateWrapper<OfficeAccount> updateWrapper;
		int success = 0;
		for (Integer _id : id) {
			updateWrapper = new UpdateWrapper<OfficeAccount>()
				.eq("id", _id)
				.eq("state", State4OfficeAccount.申请作废中.code())
				.set("last_update_time", new Date());
			if (result == 0)
				updateWrapper.set("state", State4OfficeAccount.已作废.code());
			else
				updateWrapper.set("state", State4OfficeAccount.提交启用.code());
			success += officeAccountMapper.update(null, updateWrapper);
		}
		Assert.state(success > 0, "仅限待审核的公众号");
		// 普通号广告部主管审批，协议号集采部门审批。
		List<OfficeAccount> list = list(new QueryWrapper<OfficeAccount>().select("agreement").in("id", Arrays.asList(id)));
		int agree = list.stream().filter(v -> v != null).collect(Collectors.toList()).size();
		if (agree == 0)
			approvalMsgService.sendApprovalMsg(Type4ApprovalMsg.公众号作废申请, null);
		else if (agree == list.size())
			approvalMsgService.sendApprovalMsg(Type4ApprovalMsg.协议号作废申请, null);
		else {
			approvalMsgService.sendApprovalMsg(Type4ApprovalMsg.公众号作废申请, null);
			approvalMsgService.sendApprovalMsg(Type4ApprovalMsg.协议号作废申请, null);
		}
		return success;
	}

	@Override
	public int cancel(Integer... id) {
		UpdateWrapper<OfficeAccount> updateWrapper;
		int success = 0;
		for (Integer _id : id) {
			updateWrapper = new UpdateWrapper<OfficeAccount>()
				.eq("id", _id)
				.eq("state", State4OfficeAccount.提交启用.code())
				.set("state", State4OfficeAccount.已作废.code())
				.set("last_update_time", new Date());
			success += officeAccountMapper.update(null, updateWrapper);
		}
		Assert.state(success > 0, "仅限启用中的公众号");
		return success;
	}

	@Override
	public boolean score(Integer id, String score, String comment) {
		OfficeAccount officeAccount = getOne(new QueryWrapper<OfficeAccount>().select("score", "comment").eq("id", id));
		if (score.equals(officeAccount.getScore()) && comment.equals(officeAccount.getComment()))
			return true;
		boolean boo = update(new UpdateWrapper<OfficeAccount>().eq("id", id).set("score", score).set("comment", comment));
		if (boo) {
			StringBuilder sb = new StringBuilder();
			sb.append("【评分】：").append(officeAccount.getScore() == null ? "" : officeAccount.getScore()).append(" ")
			.append(officeAccount.getComment() == null ? "" : officeAccount.getComment()).append("  变为：").append(score).append(" ").append(comment);
			contentChangeService.addContentChange(Type4Change.公众号, id, sb.toString());
		}
		return boo;
	}

	@Override
	public boolean advanageApp(Integer id) {
		UpdateWrapper<OfficeAccount> updateWrapper = new UpdateWrapper<OfficeAccount>()
				.eq("id", id).eq("state", State4OfficeAccount.提交启用.code()).isNull("advantage")
				.set("advantage", 0).set("advantage_medium", BaseController.getManagerInfo().getId());
		Assert.state(update(updateWrapper), "此公众号未启用或已被申请优势号");
		return true;
	}

	@Override
	public boolean advanageCancel(Integer id) {
		UpdateWrapper<OfficeAccount> updateWrapper = new UpdateWrapper<OfficeAccount>()
				.eq("id", id).eq("advantage", 1).eq("advantage_medium", BaseController.getManagerInfo().getId())
				.set("advantage", -1);
		Assert.state(update(updateWrapper), "仅限优势号及专属媒介或正在申请取消中");
		return true;
	}

	@Override
	public int advantageReview(Integer result, Integer... id) {
		UpdateWrapper<OfficeAccount> updateWrapper;
		int success = 0;
		for (Integer _id : id) {
			updateWrapper = new UpdateWrapper<OfficeAccount>()
					.eq("id", _id)
					.eq("state", State4OfficeAccount.提交启用.code())
					.eq("advantage", 0)
					.set("last_update_time", new Date());
			if (result == 1) {
				updateWrapper.set("advantage", 1);
				int update = officeAccountMapper.update(null, updateWrapper);
				if (update == 1)
					contentChangeService.addContentChange(Type4Change.公众号, _id, "升级为优势号", 
							officeAccountMapper.selectOne(new QueryWrapper<OfficeAccount>().select("advantage_medium").eq("id", _id)).getAdvantageMedium());
				success += update;
			} else {
				updateWrapper.set("advantage", null).set("advantage_medium", null);
				success += officeAccountMapper.update(null, updateWrapper);
			}
		}
		Assert.state(success > 0, "公众号未启用或无需审批");
		return success;
	}

	@Override
	public int advantageReview2(Integer result, Integer... id) {
		UpdateWrapper<OfficeAccount> updateWrapper;
		int success = 0;
		for (Integer _id : id) {
			updateWrapper = new UpdateWrapper<OfficeAccount>()
					.eq("id", _id)
					.eq("advantage", -1)
					.set("last_update_time", new Date());
			if (result == 1) {
				Integer creator = officeAccountMapper.selectOne(new QueryWrapper<OfficeAccount>().select("advantage_medium").eq("id", _id)).getAdvantageMedium();
				updateWrapper.set("advantage", null).set("advantage_medium", null);
				int update = officeAccountMapper.update(null, updateWrapper);
				if (update == 1)
					contentChangeService.addContentChange(Type4Change.公众号, _id, "取消优势号", creator);
				success += update;
			} else {
				updateWrapper.set("advantage", 1);
				success += officeAccountMapper.update(null, updateWrapper);
			}
		}
		Assert.state(success > 0, "公众号未申请取消优势号或无需审批");
		return success;
	}
	
	@Override
	public boolean up2Agreement(Integer id, BigDecimal price) {
		return update(new UpdateWrapper<OfficeAccount>().eq("id", id).isNull("agreement").set("agreement", 1).set("collect_price", price));
	}

	@Override
	public boolean changeMedium(Integer id, Integer mediumId) {
		UpdateWrapper<OfficeAccount> updateWrapper = new UpdateWrapper<OfficeAccount>()
			.eq("id", id).eq("state", State4OfficeAccount.提交启用.code())
			.set("last_update_time", new Date());
		// 若此时没有对接媒介
		if (getOne(new QueryWrapper<OfficeAccount>().select("medium_id").eq("id", id)) == null)
			updateWrapper.set("medium_id", mediumId);
		return update(updateWrapper);
	}
	
	@Override
	public List<Integer> queryIdsByName(String name) {
		return list(new QueryWrapper<OfficeAccount>().select("id").like("name", name))
				.stream().filter(v -> v != null).map(OfficeAccount::getId).collect(Collectors.toList());
	}

	@Override
	public boolean isAdvantage(Integer id) {
		OfficeAccount oa = getOne(new QueryWrapper<OfficeAccount>().select("advantage").eq("id", id));
		if (oa != null && oa.getAdvantage() != null && (oa.getAdvantage() == 1 || oa.getAdvantage() == -1))
			return true;
		return false;
	}

	@Override
	public boolean isAgreement(Integer id) {
		return getOne(new QueryWrapper<OfficeAccount>().select("agreement").eq("id", id)) != null ? true : false;
	}

	@Override
	public BigDecimal queryCollectPrice(Integer id) {
		OfficeAccount oa = getOne(new QueryWrapper<OfficeAccount>().select("collect_price").eq("id", id));
		return oa == null ? null : oa.getCollectPrice();
	}

	/**
	 * 内容变更
	 */
	private String contentChange(OfficeAccount acount, OfficeAccountSubmit submit) {
		StringBuilder sb = new StringBuilder();
		if (!formatStr(submit.getName()).equals(formatStr(acount.getName())))
			sb.append("【名称】：").append(formatStr(acount.getName())).append(" 变为： ").append(formatStr(submit.getName())).append("\r\n");
		if (!formatStr(submit.getCategory()).equals(formatStr(acount.getCategory())))
			sb.append("【类别】：").append(formatStr(acount.getCategory())).append(" 变为： ").append(formatStr(submit.getCategory())).append("\r\n");
		if (!formatStr(submit.getWechat()).equals(formatStr(acount.getWechat())))
			sb.append("【微信号】：").append(formatStr(acount.getWechat())).append(" 变为： ").append(formatStr(submit.getWechat())).append("\r\n");
		if (!formatStr(submit.getCompany()).equals(formatStr(acount.getCompany())))
			sb.append("【公司主体】：").append(acount.getCompany()).append(" 变为： ").append(submit.getCompany()).append("\r\n");
		if (formatDecimal(submit.getFans()) != formatDecimal(acount.getFans()))
			sb.append("【粉丝数】：").append(formatDecimal(acount.getFans())).append(" 变为： ").append(formatDecimal(submit.getFans())).append("\r\n");
		if (!formatStr(submit.getFansSource()).equals(formatStr(acount.getFansSource())))
			sb.append("【粉丝来源】：").append(formatStr(acount.getFansSource())).append(" 变为： ").append(formatStr(submit.getFansSource())).append("\r\n");
		if (!formatStr(submit.getSexRatio()).equals(formatStr(acount.getSexRatio())))
			sb.append("【男女比例】：").append(formatStr(acount.getSexRatio())).append(" 变为： ").append(formatStr(submit.getSexRatio())).append("\r\n");
		if (formatDecimal(submit.getHeadlinePrice()) != formatDecimal(acount.getHeadlinePrice()))
			sb.append("【直投刊例价】：").append(formatDecimal(acount.getHeadlinePrice())).append(" 变为： ").append(formatDecimal(submit.getHeadlinePrice())).append("\r\n");
		if (formatDecimal(submit.getHeadlineCost()) != formatDecimal(acount.getHeadlineCost()))
			sb.append("【直投成本价】：").append(formatDecimal(acount.getHeadlineCost())).append(" 变为： ").append(formatDecimal(submit.getHeadlineCost())).append("\r\n");
		if (formatDecimal(submit.getNotheadPrice()) != formatDecimal(acount.getNotheadPrice()))
			sb.append("【其他条直投刊例价】：").append(formatDecimal(acount.getNotheadPrice())).append(" 变为： ").append(formatDecimal(submit.getNotheadPrice())).append("\r\n");
		if (formatDecimal(submit.getNotheadCost()) != formatDecimal(acount.getNotheadCost()))
			sb.append("【其他条直投成本价】：").append(formatDecimal(acount.getNotheadCost())).append(" 变为： ").append(formatDecimal(submit.getNotheadCost())).append("\r\n");
		if (formatDecimal(submit.getCpaPrice()) != formatDecimal(acount.getCpaPrice()))
			sb.append("【CPA单价】：").append(formatDecimal(acount.getCpaPrice())).append(" 变为： ").append(formatDecimal(submit.getCpaPrice())).append("\r\n");
		if (!submit.getIsFapiao().equals(acount.getIsFapiao()))
			sb.append("【是否含票】：").append(acount.getIsFapiao()).append(" 变为： ").append(submit.getIsFapiao()).append("\r\n");
		if (formatDecimal(submit.getFapiaoPoint()) != formatDecimal(acount.getFapiaoPoint()))
			sb.append("【发票税点】：").append(formatDecimal(acount.getFapiaoPoint())).append(" 变为： ").append(formatDecimal(submit.getFapiaoPoint())).append("\r\n");
		if (!formatStr(submit.getRefuseType()).equals(formatStr(acount.getRefuseType())))
			sb.append("【接\\拒绝广告类型】：").append(formatStr(acount.getRefuseType())).append(" 变为： ").append(formatStr(submit.getRefuseType())).append("\r\n");
		if (!formatStr(submit.getIsBrush()).equals(formatStr(acount.getIsBrush())))
			sb.append("【是否刷号】：").append(formatStr(acount.getIsBrush())).append(" 变为： ").append(formatStr(submit.getIsBrush())).append("\r\n");
		if (!formatStr(submit.getPhone()).equals(formatStr(acount.getPhone())))
			sb.append("【号主联系方式】：").append(formatStr(acount.getPhone())).append(" 变为： ").append(formatStr(submit.getPhone())).append("\r\n");
		if (!formatStr(submit.getRemark()).equals(formatStr(acount.getRemark())))
			sb.append("【备注】：").append(formatStr(acount.getRemark())).append(" 变为： ").append(formatStr(submit.getRemark())).append("\r\n");
		if (!submit.getCooperateMode().equals(acount.getCooperateMode()))
			sb.append("【合作方式】：").append(acount.getCooperateMode()).append(" 变为： ").append(submit.getCooperateMode()).append("\r\n");
		if (!formatStr(submit.getCustomerType()).equals(formatStr(acount.getCustomerType())))
			sb.append("【适合客户类型】：").append(formatStr(acount.getCustomerType())).append(" 变为： ").append(formatStr(submit.getCustomerType())).append("\r\n");
		return sb.toString();
	}
	
	private String formatStr(String str) {
		return str == null ? "" : str.trim();
	}
	private double formatDecimal(BigDecimal decimal) {
		return decimal == null ? 0 : decimal.doubleValue();
	}

	@Override
	public Map<Integer, OASimpleInfo2> queryNameWechatCategory(Collection<Integer> ids) {
		return ids.isEmpty() ? Collections.emptyMap() :
			list(new QueryWrapper<OfficeAccount>().select("id", "category", "name", "wechat").in("id", ids))
				.stream().collect(Collectors.toMap(OfficeAccount::getId, v -> new OASimpleInfo2(v.getCategory(), v.getName(), v.getWechat())));
	}

}
