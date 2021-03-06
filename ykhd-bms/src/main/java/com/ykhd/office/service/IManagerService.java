package com.ykhd.office.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ykhd.office.domain.bean.ManagerSimpleInfo;
import com.ykhd.office.domain.bean.common.PageHelper;
import com.ykhd.office.domain.bean.common.PageHelpers;
import com.ykhd.office.domain.entity.Manager;
import com.ykhd.office.domain.req.ManagerArchiveSubmit;
import com.ykhd.office.domain.req.ManagerCondition;
import com.ykhd.office.domain.req.ManagerSubmit;
import com.ykhd.office.domain.resp.ManagerDetail;
import com.ykhd.office.domain.resp.ManagerDto;
import com.ykhd.office.domain.resp.ManagerDto2;
import com.ykhd.office.domain.resp.ManagerDto3;
import com.ykhd.office.util.dictionary.StateEnums.State4Manager;

public interface IManagerService extends IService<Manager> {

	/**
	 * 分页查询列表
	 */
	PageHelper<ManagerDto> getListByPage(ManagerCondition condition);
	
	/**
	 * 分页查询列表（组织架构）
	 */
	PageHelpers<ManagerDto2> getListByPage2(ManagerCondition condition);
	
	/**
	 * 分页查询列表（薪酬）
	 */
	PageHelper<ManagerDto3> getListByPage3(ManagerCondition condition);
	
	/**
	 * 详情
	 */
	ManagerDetail getDetail(Integer id);
	
	/**
	 * 添加管理员
	 */
	boolean addManager(ManagerSubmit submit);
	
	/**
	 * 修改管理员账号信息
	 */
	boolean updateInfo(ManagerSubmit submit);
	
	/**
	 * 修改管理员人事档案信息
	 */
	boolean updateArchive(ManagerArchiveSubmit submit);
	
	/**
	 * 修改状态
	 */
	boolean updateStatus(Integer id, State4Manager state);
	
	/**
	 * 删除管理员
	 */
	int deleteManager(Integer... id);
	
	/**
	 * 重置密码
	 */
	boolean resetPwd(Integer id);
	
	/**
	 * 修改密码
	 */
	boolean modifyPwd(Integer id, String password);
	
	/**
	 * 修改员工状态
	 */
	boolean updatePositionStatus(Integer id, String positionStatus);
	
	/**
	 * API: ids --> 员工（在职）id,名称
	 */
	List<ManagerSimpleInfo> managerInfoOnJob(List<Integer> ids);
	
	/**
	 * API：部门下的员工数量
	 */
	int managerCount(Integer department);
	
	/**
	 * API：department -->  ids
	 */
	List<Integer> queryIdsByDepartment(Integer department);
	/**
	 * API：departments -->  ids
	 */
	List<Integer> queryIdsByDepartments(List<Integer> departments);
	
	/**
	 * API：name -->  ids
	 */
	List<Integer> queryIdsByName(String name);
	
	/**
	 * API: 某角色是否被使用
	 */
	boolean roleUsed(Integer role);
	
	/**
	 * API: id --> id, name
	 */
	Map<Integer, String> queryName(Collection<Integer> collction);
	
	/**
	 * API: id --> name
	 */
	String queryName(Integer id);
	
	/**
	 * 全部媒介id,名称列表
	 */
	List<ManagerSimpleInfo> allMedium();
	
	/**
	 * 媒介 id,名称列表
	 */
	List<ManagerSimpleInfo> mediumList();

	/**
	 * AE id,名称列表
	 */
	List<ManagerSimpleInfo> AEList();
	
	/**
	 * 出纳 id,名称列表
	 */
	List<ManagerSimpleInfo> cashierList();
	
	/**
	 * API: xxx部门id下的所有xxx角色名称的managerId
	 */
	List<Integer> queryIdsByDeptIdRoleName(Integer deptId, String roleName);
}
