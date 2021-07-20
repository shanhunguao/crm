package com.ykhd.office.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ykhd.office.component.RedisService;
import com.ykhd.office.domain.entity.Manager;
import com.ykhd.office.domain.entity.Role;
import com.ykhd.office.service.IManagerService;
import com.ykhd.office.service.IRoleService;
import com.ykhd.office.util.dictionary.Consts;
import com.ykhd.office.util.dictionary.SystemEnums.RoleSign;

@Service
public class RoleService extends ServiceImpl<BaseMapper<Role>, Role> implements IRoleService {

	@Autowired
	private IManagerService managerService;
	@Autowired
	private RedisService redisService;
	
	@Override
	public List<Role> list() {
		return list(new QueryWrapper<Role>().select("id", "name"));
	}

	@Override
	public boolean addRole(String name) {
		Role role = new Role(name);
		return save(role);
	}

	@Override
	public boolean updateRole(Integer id, String name) {
		return update(new UpdateWrapper<Role>().eq("id", id).set("name", name));
	}
	
	@Override
	public boolean deleteRole(Integer id) {
		Assert.state(!managerService.roleUsed(id), "角色已被使用");
		return removeById(id);
	}

	@Override
	public String getAuth(Integer id) {
		Role role = getById(id);
		Assert.notNull(role, "未知角色");
		return role.getMenuAuth();
	}
	
	@Override
	public boolean setAuth(Integer id, String auth) {
		boolean boo = update(new UpdateWrapper<Role>().eq("id", id).set("menu_auth", auth));
		if (boo) {
			managerService.list(new QueryWrapper<Manager>().select("id").eq("role", id)).forEach(v -> {
				String key = Consts.LOGIN_AUTH_PREFIX + v.getId();
				if (redisService.delKV(key))
					redisService.setSet(Consts.LOGIN_EXPIRE, key, auth.split(","));
			});
		}
		return boo;
	}

	@Override
	public Integer mediumRole() {
		Role role = getOne(new QueryWrapper<Role>().select("id").eq("name", "媒介").last("limit 1"));
		return role != null ? role.getId() : null;
	}

	@Override
	public List<Integer> AERole() {
		return list(new QueryWrapper<Role>().select("id").eq("name", "AE").or().eq("name", "广告部主管").or().eq("name", "广告部组长"))
				.stream().map(v -> v.getId()).collect(Collectors.toList());
	}
	
	@Override
	public Integer deptRole() {
		Role role = getOne(new QueryWrapper<Role>().select("id").eq("name", "广告部主管").last("limit 1"));
		return role != null ? role.getId() : null;
	}
	
	@Override
	public Integer cashierRole() {
		Role role = getOne(new QueryWrapper<Role>().select("id").eq("name", "出纳").last("limit 1"));
		return role != null ? role.getId() : null;
	}
	
	@Override
	public List<Integer> collectorRole() {
		return list(new QueryWrapper<Role>().select("id").like("name", "集采")).stream().filter(v -> v != null).map(Role::getId).collect(Collectors.toList());
	}

	@Override
	public RoleSign judgeRole(Integer roleId) {
		String roleName = getById(roleId).getName();
		if (roleName.contains("总经理"))
			return RoleSign.general_mgr;
		else if (roleName.contains("业务总监"))
			return RoleSign.director;
		else if (roleName.contains("广告部主管"))
			return RoleSign.dept;
		else if (roleName.contains("广告部组长"))
			return RoleSign.group;
		if (roleName.contains("AE"))
			return RoleSign.ae;
		else if(roleName.contains("媒介主管")){
			return RoleSign.medium_director;
		}
		else if (roleName.contains("媒介"))
			return RoleSign.medium;
		else if (roleName.contains("人事"))
			return RoleSign.personnel;
		else if (roleName.contains("集采"))
			return RoleSign.collector;
		else 
			return RoleSign.other; //其他角色
	}
	
}
