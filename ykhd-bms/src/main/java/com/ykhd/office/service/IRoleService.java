package com.ykhd.office.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ykhd.office.domain.entity.Role;
import com.ykhd.office.util.dictionary.SystemEnums.RoleSign;

public interface IRoleService extends IService<Role> {

	/**
	 * 角色列表
	 */
	List<Role> list();
	
	/**
	 * 新建角色
	 */
	boolean addRole(String name);
	
	/**
	 * 修改角色
	 */
	boolean updateRole(Integer id, String name);
	
	/**
	 * 删除角色
	 */
	boolean deleteRole(Integer id);
	
	/**
	 * 查询权限
	 */
	String getAuth(Integer id);
	
	/**
	 * 设置权限
	 */
	boolean setAuth(Integer id, String auth);
	
	/**
	 * API: 媒介角色id
	 */
	Integer mediumRole();
	
	/**
	 * API: AE角色id （泛指能创建排期的角色，包含广告部主管、组长。）
	 */
	List<Integer> AERole();
	
	/**
	 * API: 广告部主管 角色id
	 */
	Integer deptRole();
	
	/**
	 * API: 出纳角色id
	 */
	Integer cashierRole();
	
	/**
	 * API: 集采角色id
	 */
	List<Integer> collectorRole();
	
	/**
	 * API：判断角色的业务身份
	 */
	RoleSign judgeRole(Integer roleId);
	
}
