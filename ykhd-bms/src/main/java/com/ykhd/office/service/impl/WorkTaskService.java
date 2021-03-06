package com.ykhd.office.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
import com.ykhd.office.domain.bean.ManagerSimpleInfo;
import com.ykhd.office.domain.bean.common.PageHelpers;
import com.ykhd.office.domain.entity.Manager;
import com.ykhd.office.domain.entity.WorkTask;
import com.ykhd.office.domain.req.WorkTaskCondition;
import com.ykhd.office.domain.req.WorkTaskSubmit;
import com.ykhd.office.domain.req.WorkTaskSubmit.TaskUnit;
import com.ykhd.office.domain.req.WorkTaskUpdateSubmit;
import com.ykhd.office.domain.resp.DepartmentDto;
import com.ykhd.office.domain.resp.WorkTaskDetail;
import com.ykhd.office.domain.resp.WorkTaskDto;
import com.ykhd.office.domain.resp.WorkTaskDto2;
import com.ykhd.office.service.IContentChangeService;
import com.ykhd.office.service.IDepartmentService;
import com.ykhd.office.service.IManagerService;
import com.ykhd.office.service.IRoleService;
import com.ykhd.office.service.IWorkTaskService;
import com.ykhd.office.util.DateUtil;
import com.ykhd.office.util.JacksonHelper;
import com.ykhd.office.util.dictionary.StateEnums.State4WorkTask;
import com.ykhd.office.util.dictionary.SystemEnums.OSSFolder;
import com.ykhd.office.util.dictionary.SystemEnums.RoleSign;
import com.ykhd.office.util.dictionary.TypeEnums.Type4ApprovalMsg;
import com.ykhd.office.util.dictionary.TypeEnums.Type4Change;

@Service
public class WorkTaskService extends ServiceImpl<BaseMapper<WorkTask>, WorkTask> implements IWorkTaskService {

	@Autowired
	private IManagerService managerService;
	@Autowired
	private OssService ossService;
	@Autowired
	private IDepartmentService departmentService;
	@Autowired
	private IRoleService roleService;
	@Autowired
	private IContentChangeService contentChangeService;
	@Autowired
	private ApprovalMsgService approvalMsgService;
	
	@Override
	public PageHelpers<WorkTaskDto> getListByPage(WorkTaskCondition condition) {
		LoginCache managerInfo = BaseController.getManagerInfo();
		QueryWrapper<WorkTask> queryWrapper = new QueryWrapper<>();
		if (condition.getState() != null)
			queryWrapper.eq("state", condition.getState());
		if (condition.getType() != null)
			queryWrapper.eq("type", condition.getType());
		if (StringUtils.hasText(condition.getExecutor())) {
			List<Integer> ids = managerService.queryIdsByName(condition.getExecutor().trim());
			queryWrapper.in("executor", ids);
		}
		if (StringUtils.hasText(condition.getName()))
			queryWrapper.like("name", condition.getName().trim());
		if (condition.getDateType() != null) {
			String field = condition.getDateType() == 1 ? "create_time" : "actual_finish_date";
			if (StringUtils.hasText(condition.getStartTime()))
				queryWrapper.ge(field, condition.getStartTime());
			if (StringUtils.hasText(condition.getEndTime()))
				queryWrapper.lt(field, DateUtil.nextDay(condition.getEndTime()));
		}
		// ??????????????????
		boolean leader = managerInfo.getId().equals(departmentService.getLeader(managerInfo.getDepartment()));
		DepartmentDto deptTree = null;
		List<ManagerSimpleInfo> managerList = null;
		if (leader) { //?????????????????????????????????????????????????????????
			deptTree = departmentService.departmentTree(managerInfo.getDepartment());
			List<Integer> deptIds = departmentService.subcollection(deptTree, null);
			List<Integer> ids = managerService.queryIdsByDepartments(deptIds);
			managerList = managerService.managerInfoOnJob(ids);
			if (condition.getDept() != null && deptIds.contains(condition.getDept()))
				ids = managerService.queryIdsByDepartment(condition.getDept());
			queryWrapper.in("executor", ids);
		} else
			queryWrapper.eq("executor", managerInfo.getId()); //???????????????????????????
		IPage<WorkTask> iPage = page(new Page<>(condition.getPage(), condition.getSize()), queryWrapper.orderByAsc("state", "type"));
		List<WorkTask> records = iPage.getRecords();
		Map<Integer, String> name_map = managerService.queryName(records.stream().flatMap(v -> Stream.of(v.getCreator(), v.getExecutor())).collect(Collectors.toSet()));
		List<WorkTaskDto> list = records.stream().flatMap(v -> {
			WorkTaskDto dto = new WorkTaskDto();
			BeanUtils.copyProperties(v, dto);
			dto.setCreatorName(name_map.get(v.getCreator()));
			dto.setExecutorName(name_map.get(v.getExecutor()));
			return Stream.of(dto);
		}).collect(Collectors.toList());
		Map<String, Object> map = new HashMap<>();
		map.put("leader", leader);
		if (leader) { // ???????????????????????????????????????????????????
			map.put("deptTree", deptTree);
			map.put("managerList", managerList);
		}
		return new PageHelpers<WorkTaskDto>(condition.getPage(), condition.getSize(), iPage.getTotal(), list, map);
	}

	@Override
	public List<WorkTaskDto2> getListByMonth(Integer executor, String month) {
		return list(new QueryWrapper<WorkTask>().select("id", "name", "type", "state")
				.eq("executor", executor).ne("state", State4WorkTask.??????.code()).likeRight("create_time", month))
				.stream().flatMap(v -> {
					WorkTaskDto2 dto = new WorkTaskDto2();
					BeanUtils.copyProperties(v, dto);
					return Stream.of(dto);
				}).collect(Collectors.toList());
	}

	@Override
	@Transactional
	public void add(WorkTaskSubmit submit) {
		List<TaskUnit> taskList = JacksonHelper.toParseObjArray(submit.getTaskJsonStr(), TaskUnit.class);
		Assert.state(!taskList.isEmpty(), "???????????????");
		Integer id = BaseController.getManagerInfo().getId();
		Integer executor = submit.getExecutor() == null ? id : submit.getExecutor();
		taskList.forEach(v -> {
			WorkTask workTask = new WorkTask();
			workTask.setType(submit.getType());
			workTask.setCreator(id);
			workTask.setExecutor(executor);
			workTask.setCreateTime(new Date());
			workTask.setState(State4WorkTask.?????????.code());
			BeanUtils.copyProperties(v, workTask);
			Assert.state(save(workTask), "????????????");
		});
//		if (!id.equals(submit.getExecutor()))
//			MsgSentHelper.sendMsg(new MsgSentBean(submit.getExecutor(), Type4ApprovalMsg.???????????????.code(), taskList.stream().map(TaskUnit::getName).collect(Collectors.joining("\n"))));
//		approvalMsgService.sendApprovalMsg(Type4ApprovalMsg.???????????????, executor);
	}

	@Override
	@Transactional
	public boolean update(WorkTaskUpdateSubmit submit) {
		WorkTask workTask = getById(submit.getId());
		Assert.state(workTask != null && (workTask.getState() == State4WorkTask.?????????.code() || workTask.getState() == State4WorkTask.??????.code()), "????????????????????????");
		//Assert.state(workTask.getCreator().equals(BaseController.getManagerInfo().getId()), "?????????????????????"); 
		String change = contentChange(workTask, submit);
		BeanUtils.copyProperties(submit, workTask);
		boolean boo = updateById(workTask);
		if (boo && change.length() > 0)
			Assert.state(contentChangeService.addContentChange(Type4Change.????????????, submit.getId(), change), "????????????????????????");
		return boo;
	}

	@Override
	public WorkTaskDetail detail(Integer id) {
		WorkTask workTask = getById(id);
		Assert.notNull(workTask, "????????????");
		WorkTaskDetail detail = new WorkTaskDetail();
		BeanUtils.copyProperties(workTask, detail);
		Map<Integer, String> name_map = managerService.queryName(Arrays.asList(workTask.getCreator(), workTask.getExecutor()));
		detail.setCreatorName(name_map.get(workTask.getCreator()));
		detail.setExecutorName(name_map.get(workTask.getExecutor()));
		if (workTask.getFileList() != null)
			detail.setFileList(JacksonHelper.toParseObjArray(workTask.getFileList(), String.class));
		LoginCache managerInfo = BaseController.getManagerInfo();
		if ((workTask.getCreator().equals(workTask.getExecutor()) && departmentService.directSuperior(managerInfo.getId(), workTask.getExecutor())) || 
				(!workTask.getCreator().equals(workTask.getExecutor()) && managerInfo.getId().equals(workTask.getCreator())) || 
				(roleService.judgeRole(managerInfo.getRole()) == RoleSign.general_mgr && roleService.judgeRole(executorRole(workTask.getExecutor())) == RoleSign.dept))
			detail.setLeader(true); //???????????? ??? ??????????????? ??? ????????????????????????????????????
		return detail;
	}
	
	/**
	 * ?????????????????????id
	 */
	private Integer executorRole(Integer executor) {
		return managerService.getOne(new QueryWrapper<Manager>().select("role").eq("id", executor)).getRole();
	}
	
	@Override
	@Transactional
	public boolean setStandard(Integer id, String standard) {
		WorkTask workTask = getOne(new QueryWrapper<WorkTask>().select("id", "creator", "executor", "state", "standard").eq("id", id));
		Assert.state(workTask != null && (workTask.getState() == State4WorkTask.?????????.code() || workTask.getState() == State4WorkTask.??????.code()), "????????????????????????");
		LoginCache managerInfo = BaseController.getManagerInfo();
		if (workTask.getCreator().equals(workTask.getExecutor()))
			Assert.state(departmentService.directSuperior(managerInfo.getId(), workTask.getExecutor()) || 
					(roleService.judgeRole(managerInfo.getRole()) == RoleSign.general_mgr && roleService.judgeRole(executorRole(workTask.getExecutor())) == RoleSign.dept), 
					"??????????????????????????????");
		else
			Assert.state(managerInfo.getId().equals(workTask.getCreator()), "?????????????????????");
		boolean boo = update(new UpdateWrapper<WorkTask>().eq("id", id).set("standard", standard));
		if (boo) {
			StringBuilder sb = new StringBuilder();
			if (!formatStr(workTask.getStandard()).equals(formatStr(standard)))
				sb.append("?????????????????????").append(formatStr(workTask.getStandard())).append(" ????????? ").append(formatStr(standard));
			Assert.state(contentChangeService.addContentChange(Type4Change.????????????, id, sb.toString()), "????????????????????????");
		}
		return boo;
	}

	@Override
	public boolean finishTask(Integer id, String summary) {
		WorkTask workTask = getOne(new QueryWrapper<WorkTask>().select("id", "executor", "state").eq("id", id));
		Assert.state(workTask != null && (workTask.getState() == State4WorkTask.?????????.code() || workTask.getState() == State4WorkTask.??????.code()), "????????????????????????");
		Assert.state(workTask.getExecutor().equals(BaseController.getManagerInfo().getId()), "?????????????????????");
		boolean boo = update(new UpdateWrapper<WorkTask>().eq("id", id)
				.set("state", workTask.getState() == State4WorkTask.?????????.code() ? State4WorkTask.??????.code() : State4WorkTask.????????????.code())
				.set("summary", summary).set("actual_finish_date", DateUtil.date2yyyyMMdd(new Date())));
		if (boo && workTask.getState() == State4WorkTask.??????.code())
			approvalMsgService.sendApprovalMsg(Type4ApprovalMsg.????????????, workTask.getExecutor());
		return boo;
	}

	@Override
	public boolean cancel(Integer id) {
		WorkTask workTask = getOne(new QueryWrapper<WorkTask>().select("creator", "executor", "state").eq("id", id));
		Assert.state(BaseController.getManagerInfo().getId().equals(workTask.getCreator()), "?????????????????????");
		boolean boo = update(new UpdateWrapper<WorkTask>().eq("id", id).set("state", State4WorkTask.??????.code()));
		if (boo && workTask.getState() == State4WorkTask.??????.code())
			approvalMsgService.sendApprovalMsg(Type4ApprovalMsg.????????????, workTask.getExecutor());
		return boo;
	}

	@Override
	public boolean uploadFile(Integer id, MultipartFile... files) {
		WorkTask workTask = getOne(new QueryWrapper<WorkTask>().select("executor", "state", "file_list").eq("id", id));
		Assert.state(workTask != null && workTask.getState() != State4WorkTask.??????.code(), "???????????????");
		Assert.state(workTask.getExecutor().equals(BaseController.getManagerInfo().getId()), "?????????????????????");
		List<String> list = ossService.uploadFiles(OSSFolder.BMS.name(), files);
		if (workTask.getFileList() != null) {
			List<String> fileList = JacksonHelper.toParseObjArray(workTask.getFileList(), String.class);
			fileList.addAll(list);
			return update(new UpdateWrapper<WorkTask>().eq("id", id).set("file_list", JacksonHelper.toJsonStr(fileList)));
		} else
			return update(new UpdateWrapper<WorkTask>().eq("id", id).set("file_list", JacksonHelper.toJsonStr(list)));
	}

	@Override
	public boolean removeFile(Integer id, String filePath) {
		WorkTask workTask = getOne(new QueryWrapper<WorkTask>().select("executor", "state", "file_list").eq("id", id));
		Assert.state(workTask != null && workTask.getState() != State4WorkTask.??????.code(), "???????????????");
		Assert.state(workTask.getExecutor().equals(BaseController.getManagerInfo().getId()), "?????????????????????");
		if (workTask.getFileList() != null) {
			List<String> fileList = JacksonHelper.toParseObjArray(workTask.getFileList(), String.class);
			fileList.remove(filePath);
			boolean boo;
			if (fileList.isEmpty())
				boo = update(new UpdateWrapper<WorkTask>().eq("id", id).set("file_list", null));
			else
				boo = update(new UpdateWrapper<WorkTask>().eq("id", id).set("file_list", JacksonHelper.toJsonStr(fileList)));
			if (boo)
				ossService.deleteFile(filePath);
			return boo;
		}
		return false;
	}

	/**
	 * ????????????
	 */
	private String contentChange(WorkTask workTask, WorkTaskUpdateSubmit submit) {
		StringBuilder sb = new StringBuilder();
		if (!formatStr(workTask.getName()).equals(formatStr(submit.getName())))
			sb.append("???????????????").append(formatStr(workTask.getName())).append(" ????????? ").append(formatStr(submit.getName())).append("\r\n");
		if (!workTask.getType().equals(submit.getType()))
			sb.append("???????????????").append(typeName(workTask.getType())).append(" ????????? ").append(typeName(submit.getType())).append("\r\n");
		if (!formatStr(workTask.getDescrip()).equals(formatStr(submit.getDescrip())))
			sb.append("???????????????").append(formatStr(workTask.getDescrip())).append(" ????????? ").append(formatStr(submit.getDescrip())).append("\r\n");
		if (!formatStr(workTask.getPlanFinishDate()).equals(formatStr(submit.getPlanFinishDate())))
			sb.append("???????????????????????????").append(formatStr(workTask.getPlanFinishDate())).append(" ????????? ").append(formatStr(submit.getPlanFinishDate())).append("\r\n");
		if (!workTask.getPriority().equals(submit.getPriority()))
			sb.append("??????????????????").append(priorityName(workTask.getPriority())).append(" ????????? ").append(priorityName(submit.getPriority())).append("\r\n");
		if (!workTask.getExecutor().equals(submit.getExecutor()))
			sb.append("??????????????????").append(workTask.getExecutor()).append(" ????????? ").append(submit.getExecutor()).append("\r\n");
		return sb.toString();
	}
	
	private String formatStr(String str) {
		return str == null ? "" : str.trim();
	}
	private String typeName(Integer type) {
		return type == 1 ? "?????????" : type == 2 ? "?????????" : type == 3 ? "????????????" : "????????????";
	}
	private String priorityName(Integer priority) {
		return priority == 1 ? "??????" : priority == 2 ? "??????" : priority == 3 ? "??????" : "????????????";
	}
}
