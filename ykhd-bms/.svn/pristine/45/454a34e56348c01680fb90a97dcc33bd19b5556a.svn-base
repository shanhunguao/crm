package com.ykhd.office.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ykhd.office.domain.entity.Manager;
import com.ykhd.office.domain.req.ManagerArchiveSubmit;
import com.ykhd.office.domain.req.ManagerCondition;
import com.ykhd.office.domain.req.ManagerSubmit;
import com.ykhd.office.domain.req.Manager_excel;
import com.ykhd.office.service.IContentChangeService;
import com.ykhd.office.service.IManagerService;
import com.ykhd.office.util.SecurityUtil;
import com.ykhd.office.util.dictionary.StateEnums.State4Manager;
import com.ykhd.office.util.dictionary.TypeEnums.Type4Change;

import cn.gjing.tools.excel.ExcelFactory;

/**
 * 管理员（员工）
 */
@RestController
@RequestMapping("/manager")
public class ManagerController extends BaseController {

	@Autowired
	private IManagerService managerService;
	@Autowired
	private IContentChangeService contentChangeService;
	
	/**
	 * 分页查询管理员列表
	 */
	@GetMapping
	public Object list(ManagerCondition condition) {
		return managerService.getListByPage(condition);
	}
	
	/**
	 * 分页查询管理员列表（组织架构）
	 */
	@GetMapping("/organization")
	public Object organization(ManagerCondition condition) {
		return managerService.getListByPage2(condition);
	}
	
	/**
	 * 分页查询管理员列表（薪酬）
	 */
	@GetMapping("/salary")
	public Object salary(ManagerCondition condition) {
		return managerService.getListByPage3(condition);
	}
	
	/**
	 * 管理员详情
	 */
	@GetMapping("/{id}")
	public Object detail(@PathVariable Integer id) {
		return managerService.getDetail(id);
	}
	
	/**
	 * 添加管理员账号
	 */
	@PostMapping
	public Object add(@Valid ManagerSubmit submit) {
		Assert.hasText(submit.getPassword(), "密码为空");
		return managerService.addManager(submit) ? success(null) : failure(save_failure);
	}
	
	/**
	 * 修改管理员账号信息
	 */
	@PutMapping("/info/{id}")
	public Object updateInfo(@Valid ManagerSubmit submit, @PathVariable Integer id) {
		submit.setId(id);
		return managerService.updateInfo(submit) ? success(null) : failure(update_failure);
	}
	
	/**
	 * 修改管理员人事档案信息
	 */
	@PutMapping("/archive/{id}")
	public Object updateArchive(ManagerArchiveSubmit submit, @PathVariable Integer id) {
		submit.setId(id);
		return managerService.updateArchive(submit);
	}
	
	/**
	 * 启用 或 禁用
	 */
	@PutMapping("/status/{id}")
	public Object updateStatus(@PathVariable("id") Integer id, Integer status) {
		Assert.notNull(status, "status" + nullValue);
		Optional<State4Manager> optional = Arrays.stream(State4Manager.values()).filter(v -> v.code() == status.intValue()).findFirst();
		Assert.state(optional.isPresent(), "status" + illegal);
		return managerService.updateStatus(id, optional.get()) ? success(null) : failure(update_failure);
	}
	
	/**
	 * 删除管理员
	 */
	@DeleteMapping("/{id}")
	public Object delete(@PathVariable String id) {
		if (id.contains(",")) {
			String[] ids = id.split(",");
			Integer[] array = Arrays.stream(ids).map(v -> Integer.valueOf(v)).collect(Collectors.toList()).toArray(new Integer[ids.length]);
			return success(managerService.deleteManager(array));
		} else
			return success(managerService.deleteManager(Integer.valueOf(id)));
	}
	
	/**
	 * 重置密码
	 */
	@PutMapping("/resetPwd/{id}")
	public Object resetPwd(@PathVariable Integer id) {
		return managerService.resetPwd(id) ? success(null) : failure(update_failure);
	}
	
	/**
	 * 修改密码
	 */
	@PutMapping("/modifyPwd")
	public Object modifyPwd(String password) {
		return managerService.modifyPwd(BaseController.getManagerInfo().getId(), SecurityUtil.privateKeyDecryption(password)) ? success(null) : failure(update_failure);
	}
	
	/**
	 * 修改员工状态
	 */
	@PutMapping("/position/{id}")
	public Object position(@PathVariable Integer id, String positionStatus) {
		return managerService.updatePositionStatus(id, positionStatus) ? success(null) : failure(update_failure);
	}
	
	/**
	 * 查看升职加薪记录
	 */
	@GetMapping("/upRecord/{id}")
	public Object upRecord(@PathVariable Integer id, Integer type) {
		Assert.notNull(type, "type" + nullValue);
		if (type == 0)
			return contentChangeService.getContentChangeList(Type4Change.员工职位, id);
		else
			return contentChangeService.getContentChangeList(Type4Change.员工薪水, id);
	}
	
	/**
	 * 全部媒介名称列表
	 */
	@GetMapping("/allMedium")
	public Object allMedium() {
		return managerService.allMedium();
	}
	
	/**
	 * 媒介名称列表
	 * <p>（基于当前角色）</p>
	 */
	@GetMapping("/medium")
	public Object medium() {
		return managerService.mediumList();
	}
	
	/**
	 * AE名称列表
	 * <p>（基于当前角色）</p>
	 */
	@GetMapping("/AE")
	public Object ae() {
		return managerService.AEList();
	}
	
	/**
	 * 出纳名称列表
	 */
	@GetMapping("/cashier")
	public Object cashier() {
		return managerService.cashierList();
	}
	
	/**
	 * 导入人事档案excel
	 */
	@PostMapping("/importArchive")
	public void importArchive(MultipartFile file) {
		ExcelFactory.createReader(file, Manager_excel.class).subscribe(e -> updateArchive(e)).read().finish();
	}
	
	private void updateArchive(List<Manager_excel> list) {
		list.forEach(v -> {
			Manager manager = managerService.getOne(new QueryWrapper<Manager>().eq("name", v.getName()));
			if (manager != null) {
				BeanUtils.copyProperties(v, manager);
				managerService.saveOrUpdate(manager);
			}
		});
	}
}
