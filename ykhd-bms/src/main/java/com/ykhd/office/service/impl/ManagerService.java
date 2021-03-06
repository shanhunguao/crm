package com.ykhd.office.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ykhd.office.component.aliyun.oss.OssService;
import com.ykhd.office.controller.BaseController;
import com.ykhd.office.domain.bean.LoginCache;
import com.ykhd.office.domain.bean.ManagerDeptAndRole;
import com.ykhd.office.domain.bean.ManagerSimpleInfo;
import com.ykhd.office.domain.bean.common.PageHelper;
import com.ykhd.office.domain.bean.common.PageHelpers;
import com.ykhd.office.domain.entity.Department;
import com.ykhd.office.domain.entity.Manager;
import com.ykhd.office.domain.req.ManagerArchiveSubmit;
import com.ykhd.office.domain.req.ManagerCondition;
import com.ykhd.office.domain.req.ManagerSubmit;
import com.ykhd.office.domain.resp.ManagerDetail;
import com.ykhd.office.domain.resp.ManagerDto;
import com.ykhd.office.domain.resp.ManagerDto2;
import com.ykhd.office.domain.resp.ManagerDto3;
import com.ykhd.office.repository.ManagerMapper;
import com.ykhd.office.service.IContentChangeService;
import com.ykhd.office.service.IDepartmentService;
import com.ykhd.office.service.IManagerService;
import com.ykhd.office.service.IRoleService;
import com.ykhd.office.util.SecurityUtil;
import com.ykhd.office.util.dictionary.StateEnums.State4Manager;
import com.ykhd.office.util.dictionary.SystemEnums.OSSFolder;
import com.ykhd.office.util.dictionary.SystemEnums.RoleSign;
import com.ykhd.office.util.dictionary.TypeEnums.Type4Change;

@Service
public class ManagerService extends ServiceImpl<BaseMapper<Manager>, Manager> implements IManagerService {

    @Autowired
    private ManagerMapper managerMapper;
    @Autowired
    private IRoleService roleService;
    @Autowired
    private OssService ossService;
    @Autowired
    private IContentChangeService contentChangeService;
    @Autowired
    private IDepartmentService departmentService;

    @Override
    public PageHelper<ManagerDto> getListByPage(ManagerCondition condition) {
        QueryWrapper<Manager> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id", "name", "mobile", "department", "role", "number", "position_status", "status");
        if (condition.getStatus() != null)
            queryWrapper.eq("status", condition.getStatus());
        if (condition.getDepartment() != null)
            queryWrapper.eq("department", condition.getDepartment());
        if (StringUtils.hasText(condition.getName()))
            queryWrapper.like("name", condition.getName().trim());
        IPage<Manager> iPage = page(new Page<>(condition.getPage(), condition.getSize()), queryWrapper.orderByDesc("id"));
        List<Manager> records = iPage.getRecords();
        // ???????????????????????????
        Map<Integer, ManagerDeptAndRole> deptNameRoleName = queryDeptNameRoleNameByIds(records.stream().map(Manager::getId).collect(Collectors.toSet()));
        // ??????????????????
        List<Integer> leaders;
        if (condition.getDepartment() != null) {
            Integer leader = departmentService.getLeader(condition.getDepartment());
            leaders = leader == null ? Collections.emptyList() : Arrays.asList(leader);
        } else {
            Set<Integer> deptSet = records.stream().map(v -> v.getDepartment()).collect(Collectors.toSet());
            leaders = departmentService.getLeaders(deptSet);
        }
        List<ManagerDto> list = records.stream().flatMap(v -> {
            ManagerDto dto = new ManagerDto();
            BeanUtils.copyProperties(v, dto);
            ManagerDeptAndRole name = deptNameRoleName.get(v.getId());
            if (name != null) {
                dto.setDeptName(name.getDeptName());
                dto.setRoleName(name.getRoleName());
            }
            if (leaders.contains(dto.getId()))
                dto.setLeader(true);
            return Stream.of(dto);
        }).collect(Collectors.toList());
        return new PageHelper<ManagerDto>(condition.getPage(), condition.getSize(), iPage.getTotal(), list);
    }

    @Override
    public PageHelpers<ManagerDto2> getListByPage2(ManagerCondition condition) {
        QueryWrapper<Manager> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id", "name", "mobile", "department", "role");
        queryWrapper.ne("position_status", "??????");
        if (condition.getDepartment() != null)
            queryWrapper.eq("department", condition.getDepartment());
        if (StringUtils.hasText(condition.getName()))
            queryWrapper.like("name", condition.getName().trim());
        IPage<Manager> iPage = page(new Page<>(condition.getPage(), condition.getSize()), queryWrapper.orderByDesc("id"));
        List<Manager> records = iPage.getRecords();
        // ???????????????????????????
        Map<Integer, ManagerDeptAndRole> deptNameRoleName = queryDeptNameRoleNameByIds(records.stream().map(Manager::getId).collect(Collectors.toSet()));
        // ??????????????????
        List<Integer> leaders;
        if (condition.getDepartment() != null) {
            Integer leader = departmentService.getLeader(condition.getDepartment());
            leaders = leader == null ? Collections.emptyList() : Arrays.asList(leader);
        } else {
            Set<Integer> deptSet = records.stream().map(v -> v.getDepartment()).collect(Collectors.toSet());
            leaders = departmentService.getLeaders(deptSet);
        }
        List<ManagerDto2> list = records.stream().flatMap(v -> {
            ManagerDto2 dto = new ManagerDto2();
            BeanUtils.copyProperties(v, dto);
            ManagerDeptAndRole name = deptNameRoleName.get(v.getId());
            if (name != null) {
                dto.setDeptName(name.getDeptName());
                dto.setRoleName(name.getRoleName());
            }
            if (leaders.contains(dto.getId()))
                dto.setLeader(true);
            return Stream.of(dto);
        }).collect(Collectors.toList());
        // ?????? + ????????????????????????
        Map<String, Object> map = null;
        LoginCache managerInfo = BaseController.getManagerInfo();
        if (managerInfo.getId().equals(departmentService.getLeader(managerInfo.getDepartment())) ||
                roleService.judgeRole(managerInfo.getRole()) == RoleSign.personnel) {
            map = new HashMap<>();
            map.put("note_auth", true);
        }
        return new PageHelpers<ManagerDto2>(condition.getPage(), condition.getSize(), iPage.getTotal(), list, map);
    }

    @Override
    public PageHelper<ManagerDto3> getListByPage3(ManagerCondition condition) {
        QueryWrapper<Manager> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id", "name", "number", "department", "role", "probation_salary", "formal_salary", "current_salary");
        queryWrapper.ne("position_status", "??????");
        if (condition.getDepartment() != null)
            queryWrapper.eq("department", condition.getDepartment());
        if (StringUtils.hasText(condition.getName()))
            queryWrapper.like("name", condition.getName().trim());
        IPage<Manager> iPage = page(new Page<>(condition.getPage(), condition.getSize()), queryWrapper.orderByDesc("id"));
        List<Manager> records = iPage.getRecords();
        // ???????????????????????????
        Map<Integer, ManagerDeptAndRole> deptNameRoleName = queryDeptNameRoleNameByIds(records.stream().map(Manager::getId).collect(Collectors.toSet()));
        List<ManagerDto3> list = records.stream().flatMap(v -> {
            ManagerDto3 dto = new ManagerDto3();
            BeanUtils.copyProperties(v, dto);
            ManagerDeptAndRole name = deptNameRoleName.get(v.getId());
            if (name != null) {
                dto.setDeptName(name.getDeptName());
                dto.setRoleName(name.getRoleName());
            }
            return Stream.of(dto);
        }).collect(Collectors.toList());
        return new PageHelper<ManagerDto3>(condition.getPage(), condition.getSize(), iPage.getTotal(), list);
    }

    @Override
    public ManagerDetail getDetail(Integer id) {
        Manager manager = getById(id);
        Assert.notNull(manager, "????????????");
        ManagerDetail detail = new ManagerDetail();
        BeanUtils.copyProperties(manager, detail);
        ManagerDeptAndRole deptNameRoleName = managerMapper.queryDeptRoleName(id);
        if (deptNameRoleName != null) {
            detail.setDeptName(deptNameRoleName.getDeptName());
            detail.setRoleName(deptNameRoleName.getRoleName());
        }
        return detail;
    }

    @Override
    public boolean addManager(ManagerSubmit submit) {
        Assert.state(count(new QueryWrapper<Manager>().eq("name", submit.getName().trim())) == 0, "???????????????");
        Manager manager = new Manager();
        BeanUtils.copyProperties(submit, manager);
        manager.setPassword(SecurityUtil.md5Encode(submit.getPassword()));
        manager.setStatus(State4Manager.??????.code());
        return save(manager);
    }

    @Override
    public boolean updateInfo(ManagerSubmit submit) {
        Assert.state(count(new QueryWrapper<Manager>().ne("id", submit.getId()).eq("name", submit.getName().trim())) == 0, "???????????????");
        Manager manager = getById(submit.getId());
        Integer old_role = manager.getRole();
        Integer new_role = submit.getRole();
        BeanUtils.copyProperties(submit, manager);
        boolean boo = updateById(manager);
        if (boo && !old_role.equals(new_role)) {
            //??????????????????
            String old_name = roleService.getById(old_role).getName();
            String new_name = roleService.getById(new_role).getName();
            StringBuilder sb = new StringBuilder("???????????????").append(old_name == null ? "" : old_name).append(" ????????? ").append(new_name);
            contentChangeService.addContentChange(Type4Change.????????????, submit.getId(), sb.toString(), BaseController.getManagerInfo().getId());
        }
        return boo;
    }

    @Override
    public boolean updateArchive(ManagerArchiveSubmit submit) {
        Manager manager = getById(submit.getId());
        String old = manager.getCurrentSalary() == null ? "" : manager.getCurrentSalary();
        String new_ = submit.getCurrentSalary() == null ? "" : submit.getCurrentSalary();
        BeanUtils.copyProperties(submit, manager);
        if (submit.getIdCardFile() != null)
            manager.setIdCardImg(ossService.uploadFile(OSSFolder.BMS.name(), submit.getIdCardFile()));
        if (submit.getIdCard2File() != null)
            manager.setIdCardImg2(ossService.uploadFile(OSSFolder.BMS.name(), submit.getIdCard2File()));
        if (submit.getEducationFile() != null)
            manager.setEducationImg(ossService.uploadFile(OSSFolder.BMS.name(), submit.getEducationFile()));
        if (submit.getDepartureFile() != null)
            manager.setDepartureImg(ossService.uploadFile(OSSFolder.BMS.name(), submit.getDepartureFile()));
        if (submit.getPictureFile() != null)
            manager.setPicture(ossService.uploadFile(OSSFolder.BMS.name(), submit.getPictureFile()));
        boolean boo = updateById(manager);
        if (boo && !old.equals(new_)) {
            //????????????????????????
            StringBuilder sb = new StringBuilder("?????????????????????").append(old).append(" ????????? ").append(new_);
            contentChangeService.addContentChange(Type4Change.????????????, submit.getId(), sb.toString(), BaseController.getManagerInfo().getId());
        }
        return boo;
    }

    @Override
    public boolean updateStatus(Integer id, State4Manager state) {
        boolean boo = update(new UpdateWrapper<Manager>().eq("id", id).set("status", state.code()));
        // ???????????????????????????????????????????????????????????????
        if (boo && state == State4Manager.??????) {
            Manager manager = getOne(new QueryWrapper<Manager>().select("id", "department").eq("id", id));
            Integer leader = departmentService.getLeader(manager.getDepartment());
            if (id.equals(leader))
                departmentService.setLeader(manager.getDepartment(), null);
        }
        return boo;
    }

    @Override
    public int deleteManager(Integer... id) {
        QueryWrapper<Manager> query = new QueryWrapper<>();
        query.eq("status", State4Manager.??????.code()).in("id", Arrays.asList(id));
        int delete = managerMapper.delete(query);
        if (delete > 0)
            return delete;
        throw new IllegalStateException("????????????????????????");
    }

    @Override
    public boolean resetPwd(Integer id) {
        return update(new UpdateWrapper<Manager>().eq("id", id).set("password", SecurityUtil.md5Encode("123456")));
    }

    @Override
    public boolean modifyPwd(Integer id, String password) {
        return update(new UpdateWrapper<Manager>().eq("id", id).set("password", SecurityUtil.md5Encode(password)));
    }

    @Override
    public boolean updatePositionStatus(Integer id, String positionStatus) {
        return update(new UpdateWrapper<Manager>().eq("id", id).set("position_status", positionStatus));
    }

    @Override
    public List<ManagerSimpleInfo> managerInfoOnJob(List<Integer> ids) {
        return list(new QueryWrapper<Manager>().select("id", "name").in("id", ids).ne("position_status", "??????"))
                .stream().flatMap(v -> Stream.of(new ManagerSimpleInfo(v.getId(), v.getName()))).collect(Collectors.toList());
    }

    @Override
    public int managerCount(Integer department) {
        return count(new QueryWrapper<Manager>().eq("department", department));
    }

    @Override
    public List<Integer> queryIdsByDepartment(Integer department) {
        return list(new QueryWrapper<Manager>().select("id").eq("department", department)).stream().map(Manager::getId).collect(Collectors.toList());
    }

    @Override
    public List<Integer> queryIdsByDepartments(List<Integer> departments) {
        return list(new QueryWrapper<Manager>().select("id").in("department", departments)).stream().map(Manager::getId).collect(Collectors.toList());
    }

    @Override
    public List<Integer> queryIdsByName(String name) {
        return list(new QueryWrapper<Manager>().select("id").like("name", name)).stream().map(Manager::getId).collect(Collectors.toList());
    }

    @Override
    public boolean roleUsed(Integer role) {
        return count(new QueryWrapper<Manager>().eq("role", role)) > 0 ? true : false;
    }

    /**
     * ??????????????????????????????????????????
     */
    private Map<Integer, ManagerDeptAndRole> queryDeptNameRoleNameByIds(Set<Integer> ids) {
        return ids.isEmpty() ? Collections.emptyMap() :
                managerMapper.queryDeptRoleNameByIds(ids.stream().map(String::valueOf).collect(Collectors.joining(",")))
                        .stream().collect(Collectors.toMap(ManagerDeptAndRole::getId, v -> v));
    }

    @Override
    public Map<Integer, String> queryName(Collection<Integer> collction) {
        return collction.isEmpty() ? Collections.emptyMap() :
                list(new QueryWrapper<Manager>().select("id", "name").in("id", collction)).stream().collect(Collectors.toMap(Manager::getId, Manager::getName));
    }

    @Override
    public String queryName(Integer id) {
        Manager manager = getOne(new QueryWrapper<Manager>().select("name").eq("id", id));
        return manager == null ? "" : manager.getName();
    }

    @Override
    public List<ManagerSimpleInfo> allMedium() {
        Integer mediumRole = roleService.mediumRole();
        return mediumRole == null ? Collections.emptyList() :
                sortList(
                        list(new QueryWrapper<Manager>().select("id", "name").eq("role", mediumRole)).stream()
                                .flatMap(v -> Stream.of(new ManagerSimpleInfo(v.getId(), v.getName()))).collect(Collectors.toList())
                );
    }

    @Override
    public List<ManagerSimpleInfo> mediumList() {
        LoginCache managerInfo = BaseController.getManagerInfo();
        RoleSign sign = roleService.judgeRole(managerInfo.getRole());
        List<ManagerSimpleInfo> list;
        switch (sign) {
            case general_mgr:
            case director:
            case ae:
            case group:
            case dept:
            case medium_director:
            case other:
                Integer mediumRole = roleService.mediumRole();
                list = mediumRole == null ? Collections.emptyList() :
                        list(new QueryWrapper<Manager>().select("id", "name").eq("role", mediumRole)).stream()
                                .flatMap(v -> Stream.of(new ManagerSimpleInfo(v.getId(), v.getName()))).collect(Collectors.toList());
                break;
//		case group:
//			list = managerMapper.queryIdNameByDeptIdRoleName(managerInfo.getDepartment(), "??????");
//			break;
//		case dept:
//			List<Integer> deptList = departmentService.subcollection(departmentService.departmentTree(managerInfo.getDepartment()), null);
//			list = new ArrayList<>();
//			for (Integer dept : deptList) {
//				list.addAll(managerMapper.queryIdNameByDeptIdRoleName(dept, "??????"));
//			}
//			break;
            case medium:
//                if (managerInfo.getId().equals(departmentService.getLeader(BaseController.getManagerInfo().getDepartment()))) {
//                    QueryWrapper<Manager> wrapper = new QueryWrapper<>();
//                    wrapper.eq("department", managerInfo.getDepartment());
//                    wrapper.eq("status", 0);
//                    List<Manager> list1 = managerMapper.selectList(wrapper);
//                    list = list1.stream().flatMap(v -> Stream.of(new ManagerSimpleInfo(v.getId(), v.getName()))).collect(Collectors.toList());
//                } else {
                    list = Arrays.asList(new ManagerSimpleInfo(managerInfo.getId(), managerInfo.getName()));
//                }
                break;
            default:
                list = Collections.emptyList();
        }
        return sortList(list);
    }

    @Override
    public List<ManagerSimpleInfo> AEList() {
        LoginCache managerInfo = BaseController.getManagerInfo();
        RoleSign sign = roleService.judgeRole(managerInfo.getRole());
        List<ManagerSimpleInfo> list;
        switch (sign) {
            case general_mgr:
            case director:
            case medium_director:
            case medium:
            case other:
                List<Integer> aeRole = roleService.AERole();
                list = aeRole.isEmpty() ? Collections.emptyList() :
                        list(new QueryWrapper<Manager>().select("id", "name").in("role", aeRole)).stream()
                                .flatMap(v -> Stream.of(new ManagerSimpleInfo(v.getId(), v.getName()))).collect(Collectors.toList());
                break;
            case group:
                list = managerMapper.queryIdNameByDeptIdRoleName(managerInfo.getDepartment(), "AE");
                list.add(new ManagerSimpleInfo(managerInfo.getId(), managerInfo.getName())); // ????????????????????????
                break;
            case dept:
                List<Integer> deptList = departmentService.subcollection(departmentService.departmentTree(managerInfo.getDepartment()), null);
                list = new ArrayList<>();
                for (Integer dept : deptList) {
                    list.addAll(managerMapper.queryIdNameByDeptIdRoleName(dept, "AE"));
                    Integer leader = departmentService.getOne(new QueryWrapper<Department>().select("leader").eq("id", dept)).getLeader();
                    if (leader != null)
                        list.add(new ManagerSimpleInfo(leader, queryName(leader)));
                }
                break;
            case ae:
                list = Arrays.asList(new ManagerSimpleInfo(managerInfo.getId(), managerInfo.getName()));
                break;
            default:
                list = Collections.emptyList();
        }
        return sortList(list);
    }

    @Override
    public List<ManagerSimpleInfo> cashierList() {
        Integer cashierRole = roleService.cashierRole();
        return cashierRole == null ? Collections.emptyList() :
                sortList(
                        list(new QueryWrapper<Manager>().select("id", "name").eq("role", cashierRole)).stream()
                                .flatMap(v -> Stream.of(new ManagerSimpleInfo(v.getId(), v.getName()))).collect(Collectors.toList())
                );
    }

    /**
     * ?????????????????????
     */
    private List<ManagerSimpleInfo> sortList(List<ManagerSimpleInfo> list) {
        if (list.isEmpty())
            return Collections.emptyList();
        List<Integer> departure = list(new QueryWrapper<Manager>().select("id").in("id", list.stream().map(ManagerSimpleInfo::getId).collect(Collectors.toList()))
                .and(wrapper -> wrapper.eq("position_status", "??????").or().isNull("position_status"))).stream().map(Manager::getId).collect(Collectors.toList());
        List<ManagerSimpleInfo> collect1 = list.stream().filter(v -> !departure.contains(v.getId())).collect(Collectors.toList());
        List<ManagerSimpleInfo> collect2 = list.stream().filter(v -> departure.contains(v.getId())).collect(Collectors.toList());
        collect1.addAll(collect2);
        return collect1;
    }

    @Override
    public List<Integer> queryIdsByDeptIdRoleName(Integer deptId, String roleName) {
        return managerMapper.queryIdsByDeptIdRoleName(deptId, roleName);
    }
}
