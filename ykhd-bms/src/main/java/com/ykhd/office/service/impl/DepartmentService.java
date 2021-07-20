package com.ykhd.office.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ykhd.office.controller.BaseController;
import com.ykhd.office.domain.bean.LoginCache;
import com.ykhd.office.domain.entity.Department;
import com.ykhd.office.domain.entity.Manager;
import com.ykhd.office.domain.resp.DepartmentDto;
import com.ykhd.office.service.IDepartmentService;
import com.ykhd.office.service.IManagerService;
import com.ykhd.office.service.IRoleService;
import com.ykhd.office.util.dictionary.SystemEnums.RoleSign;

@Service
public class DepartmentService extends ServiceImpl<BaseMapper<Department>, Department> implements IDepartmentService {

	@Autowired
	private IManagerService managerService;
	@Autowired
	private IRoleService roleService;
	
	@Override
	public boolean addDepartment(String name, Integer parent) {
		return save(new Department(null, name, parent));
	}

	@Override
	public boolean updateDepartment(Integer id, String name, Integer parent) {
		return update(new UpdateWrapper<Department>().eq("id", id).set("name", name).set("parent", parent));
	}

	@Override
	public boolean deleteDepartment(Integer id) {
		Assert.state(count(new QueryWrapper<Department>().eq("parent", id)) == 0, "删除失败，包含子部门！");
		Assert.state(managerService.managerCount(id) == 0, "删除失败，包含员工！");
		return removeById(id);
	}

	@Override
	public List<DepartmentDto> departmentTree() {
		List<DepartmentDto> list = list().stream().flatMap(v -> {
			DepartmentDto dto = new DepartmentDto();
			BeanUtils.copyProperties(v, dto);
			return Stream.of(dto);
		}).collect(Collectors.toList());
		Map<Integer, List<DepartmentDto>> map = list.stream().filter(v -> v.getParent() != null)
			.collect(Collectors.toMap(DepartmentDto::getParent, v -> new ArrayList<>(Arrays.asList(v)), 
				(v1, v2) -> {
					v1.addAll(v2);
					return v1;
				})
			);
		return list.stream().peek(v -> v.setChildren(map.get(v.getId()))).filter(v -> v.getParent() == null).collect(Collectors.toList());
	}

	@Override
	public DepartmentDto departmentTree(Integer id) {
		List<DepartmentDto> list = list().stream().flatMap(v -> {
			DepartmentDto dto = new DepartmentDto();
			BeanUtils.copyProperties(v, dto);
			return Stream.of(dto);
		}).collect(Collectors.toList());
		Map<Integer, List<DepartmentDto>> map = list.stream().filter(v -> v.getParent() != null)
			.collect(Collectors.toMap(DepartmentDto::getParent, v -> new ArrayList<>(Arrays.asList(v)), 
				(v1, v2) -> {
					v1.addAll(v2);
					return v1;
				})
			);
		return list.stream().peek(v -> v.setChildren(map.get(v.getId()))).filter(v -> v.getId().equals(id)).collect(Collectors.toList()).get(0);
	}

	@Override
	public boolean setLeader(Integer id, Integer leader) {
		return update(new UpdateWrapper<Department>().eq("id", id).set("leader", leader));
	}

	@Override
	public List<Integer> subcollection(DepartmentDto dto, List<Integer> ids) {
		if (ids == null)
			ids = new ArrayList<>();
		ids.add(dto.getId());
		if (dto.getChildren() != null) {
			for (DepartmentDto dto2 : dto.getChildren()) {
				subcollection(dto2, ids);
			}
		}
		return ids;
	}
	
	@Override
	public Integer getLeader(Integer id) {
		Department department = getById(id);
		Assert.notNull(department, "未知部门");
		return department.getLeader();
	}

	@Override
	public List<Integer> getLeaders(Collection<Integer> ids) {
		return ids.isEmpty() ? Collections.emptyList() : 
			list(new QueryWrapper<Department>().select("leader").in("id", ids)).stream().filter(v -> v != null)
			.map(Department::getLeader).collect(Collectors.toList());
	}

	@Override
	public Integer getSuperiorLeader(Integer id) {
		if (id == null)
			return null;
		Department department = getById(id);
		if (department == null)
			return null;
		Integer leader = department.getLeader();
		if (leader == null) {
			Integer parent = department.getParent();
			if (parent == null)
				return null;
			leader = getSuperiorLeader(parent);
		}
		return leader;
	}

	@Override
	public boolean directSuperior(Integer A, Integer B) {
		if (A.equals(B)) return false;
		Manager manager_B = managerService.getOne(new QueryWrapper<Manager>().select("id", "department").eq("id", B));
		if (manager_B != null && manager_B.getDepartment() != null) {
			Department dept_B = getById(manager_B.getDepartment());
			if (B.equals(dept_B.getLeader())) { //部门领导找上一级部门
				if (dept_B.getParent() == null) return false;
				Department parent_dept_B = getById(dept_B.getParent());
				if (A.equals(parent_dept_B.getLeader()))
					return true;
			} else if (A.equals(dept_B.getLeader()))
				return true;
		}
		return false;
	}

	@Override
	public Integer queryPersonnelMrg() {
		Department dept = getOne(new QueryWrapper<Department>().and(wrap -> wrap.like("name", "人事").or().like("name", "人力资源")));
		return dept == null ? null : dept.getLeader();
	}

	@Override
	public Integer queryCollectionMrg() {
		Department dept = getOne(new QueryWrapper<Department>().like("name", "集采"));
		return dept == null ? null : dept.getLeader();
	}

	@Override
	public Integer queryGeneralMrg() {
		Department dept = getOne(new QueryWrapper<Department>().eq("name", "总经办"));
		return dept == null ? null : dept.getLeader();
	}

	@Override
	public List<DepartmentDto> team_group_list() {
		LoginCache managerInfo = BaseController.getManagerInfo();
		RoleSign sign = roleService.judgeRole(managerInfo.getRole());
		if (sign == RoleSign.ae || sign == RoleSign.medium || sign == RoleSign.group)
			return Collections.emptyList();
		else if (sign == RoleSign.dept) {
			DepartmentDto dto = departmentTree(managerInfo.getDepartment());
			return dto.getChildren() == null ? Collections.emptyList() : dto.getChildren();
		} else
//			return departmentTree(getOne(new QueryWrapper<Department>().eq("name", "公众号广告部")).getId()).getChildren();
		return departmentTree(getOne(new QueryWrapper<Department>().eq("name", "新媒体广告部")).getId()).getChildren();
	}

}
