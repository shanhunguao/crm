package com.ykhd.office.service;

import java.util.Collection;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ykhd.office.domain.entity.Department;
import com.ykhd.office.domain.resp.DepartmentDto;

public interface IDepartmentService extends IService<Department> {

	/**
	 * 添加部门
	 */
	boolean addDepartment(String name, Integer parent);
	
	/**
	 * 修改部门
	 */
	boolean updateDepartment(Integer id, String name, Integer parent);
	
	/**
	 * 删除部门
	 */
	boolean deleteDepartment(Integer id);
	
	/**
	 * 全部部门结构树
	 */
	List<DepartmentDto> departmentTree();
	
	/**
	 * 单个部门结构树
	 */
	DepartmentDto departmentTree(Integer id);
	
	/**
	 * 设置部门领导
	 */
	boolean setLeader(Integer id, Integer leader);
	
	/**
	 * API: 某部门及所有子孙部门ids
	 */
	List<Integer> subcollection(DepartmentDto dto, List<Integer> ids);
	
	/**
	 * API：查询部门领导
	 */
	Integer getLeader(Integer id);
	
	/**
	 * API：查询多个部门领导
	 */
	List<Integer> getLeaders(Collection<Integer> ids);
	
	/**
	 * API：业务流程：查询部门领导或上级部门领导
	 */
	Integer getSuperiorLeader(Integer id);
	
	/**
	 * API：A 是不是 B 的直属上级
	 */
	boolean directSuperior(Integer A, Integer B);
	
	/**
	 * API: 人事部门主管id
	 */
	Integer queryPersonnelMrg();
	
	/**
	 * API: 集采部门主管id
	 */
	Integer queryCollectionMrg();
	
	/**
	 * API: 总经理id
	 */
	Integer queryGeneralMrg();
	
	/**
	 * 广告部战队、小组列表（基于角色限制查询）
	 */
	List<DepartmentDto> team_group_list();
}
