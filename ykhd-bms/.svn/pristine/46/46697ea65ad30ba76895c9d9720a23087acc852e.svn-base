package com.ykhd.office.repository;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ykhd.office.domain.bean.ManagerDeptAndRole;
import com.ykhd.office.domain.bean.ManagerSimpleInfo;
import com.ykhd.office.domain.entity.Manager;

public interface ManagerMapper extends BaseMapper<Manager> {

	/**
	 * id  -->  id, deptName, roleName
	 * @param ids "1,2,3"
	 */
	@Select("select m.id, d.name as deptName, r.name as roleName from bms_manager m "
			+ "left join bms_department d on m.department = d.id "
			+ "left join bms_role r on m.role = r.id where m.id in (${ids})")
	List<ManagerDeptAndRole> queryDeptRoleNameByIds(String ids);
	
	@Select("select m.id, d.name as deptName, r.name as roleName from bms_manager m "
			+ "left join bms_department d on m.department = d.id "
			+ "left join bms_role r on m.role = r.id where m.id = ${id}")
	ManagerDeptAndRole queryDeptRoleName(Integer id);
	
	/**
	 * xxx部门id下的所有xxx角色名称的managerId
	 */
	@Select("select m.id from bms_manager m, bms_department d, bms_role r where m.department = d.id and m.role = r.id and d.id = ${deptId} and r.name = '${roleName}'")
	List<Integer> queryIdsByDeptIdRoleName(Integer deptId, String roleName);
	
	/**
	 * xxx部门id下的所有xxx角色名称的managerId,name
	 */
	@Select("select m.id, m.name from bms_manager m, bms_department d, bms_role r where m.department = d.id and m.role = r.id and d.id = ${deptId} and r.name = '${roleName}'")
	List<ManagerSimpleInfo> queryIdNameByDeptIdRoleName(Integer deptId, String roleName);
}
