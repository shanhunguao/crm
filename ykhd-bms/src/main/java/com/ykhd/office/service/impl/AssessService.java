package com.ykhd.office.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ykhd.office.controller.BaseController;
import com.ykhd.office.domain.bean.common.PageHelper;
import com.ykhd.office.domain.entity.Assess;
import com.ykhd.office.domain.entity.AssessHistory;
import com.ykhd.office.domain.entity.Department;
import com.ykhd.office.domain.entity.Role;
import com.ykhd.office.domain.req.AssessCondition;
import com.ykhd.office.domain.resp.AssessDto;
import com.ykhd.office.repository.AssessHistoryMappper;
import com.ykhd.office.repository.AssessMapper;
import com.ykhd.office.repository.DepartmentMapper;
import com.ykhd.office.repository.RoleMapper;
import com.ykhd.office.service.IAssessService;
import com.ykhd.office.service.IDepartmentService;
import com.ykhd.office.util.dictionary.StateEnums;
import com.ykhd.office.util.dictionary.TypeEnums;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zhoufan
 * @Date 2020/11/2
 */
@Service
public class AssessService extends ServiceImpl<BaseMapper<Assess>, Assess> implements IAssessService {

    @Autowired
    private AssessMapper assessMapper;

    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private RoleMapper roleMapper;


    @Autowired
    private AssessHistoryMappper assessHistoryMappper;

    @Autowired
    private IDepartmentService departmentService;
    @Autowired
    private ApprovalMsgService approvalMsgService;

    @Override
    public PageHelper<AssessDto> list(AssessCondition condition) {
        List<AssessDto> assessDtos = new ArrayList<>();
        Assess assess = new Assess();
        BeanUtils.copyProperties(condition, assess);
        QueryWrapper<Assess> wrapper = new QueryWrapper<>();
//        ????????????????????????
        Integer userid = BaseController.getManagerInfo().getId();
        if (userid.equals(departmentService.queryGeneralMrg()) || userid.equals(departmentService.queryPersonnelMrg())) {
            if (condition.getDeptId() != null) {
                wrapper.eq("dept_id", condition.getDeptId());
            }
            if ((StringUtils.isNotEmpty(condition.getName()))) {
                wrapper.like("name", condition.getName());
            }
            if (condition.getState() != null) {
                wrapper.eq("state", condition.getState());
            }
        } else if (userid.equals(departmentService.getLeader(BaseController.getManagerInfo().getDepartment()))) {
            QueryWrapper<Department> wrapper1 = new QueryWrapper<>();
            wrapper1.eq("parent", BaseController.getManagerInfo().getDepartment());
            Department department1 = departmentMapper.selectOne(wrapper1);
            if (condition.getDeptId() != null) {
                if (department1 != null) {
                    if (condition.getDeptId().equals(BaseController.getManagerInfo().getDepartment())) {
                        wrapper.in("dept_id", BaseController.getManagerInfo().getDepartment(), department1.getId());
                    } else if (condition.getDeptId().equals(department1.getId())) {
                        wrapper.eq("dept_id", condition.getDeptId());
                    } else {
                        Assert.state(false, "???????????????");
                    }
                } else {
                    Assert.state(condition.getDeptId().equals(BaseController.getManagerInfo().getDepartment()), "???????????????");
                    wrapper.eq("dept_id", condition.getDeptId());
                }
            } else {
                if (department1 != null) {
                    wrapper.in("dept_id", BaseController.getManagerInfo().getDepartment(), department1.getId());
                } else {
                    wrapper.eq("dept_id", BaseController.getManagerInfo().getDepartment());
                }

            }
            if (condition.getState() != null) {
                wrapper.eq("state", condition.getState());
            }
            if ((StringUtils.isNotEmpty(condition.getName()))) {
                wrapper.like("name", condition.getName());
            }
        } else {
            wrapper.eq("user_id", BaseController.getManagerInfo().getId());
            wrapper.in("state", StateEnums.State4Assess.?????????.code(), StateEnums.State4Assess.?????????.code());
        }
        wrapper.orderByDesc("create_time");
        IPage<Assess> assessIPage = getAssessIPage(condition, assessDtos, wrapper);
        return new PageHelper<>(condition.getPage(), condition.getSize(), assessIPage.getTotal(), assessDtos);
    }

    @Override
    public List<AssessHistory> getHistory(Integer userid, String assessTime) {
        QueryWrapper<AssessHistory> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userid);
        if (StringUtils.isNotEmpty(assessTime)) {
            wrapper.eq("assess_time", assessTime);
        }
        return assessHistoryMappper.selectList(wrapper);
    }

    @Override
    public AssessHistory getScoreHistory(Integer userid, Integer templateid, String assessTime) {
        QueryWrapper<AssessHistory> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userid);
        wrapper.eq("template_id", templateid);
        wrapper.lt("assess_time", assessTime);
        wrapper.orderByDesc("assess_time");
        wrapper.last("limit 1");
        return assessHistoryMappper.selectOne(wrapper);
    }

    @Override
    public LinkedHashMap<String, BigDecimal> getHistoryList(Integer userid) {
        QueryWrapper<AssessHistory> wrapper = new QueryWrapper<>();
        wrapper.select("MAX( id ) as sid", "assess_time", "user_id", "gross_score");
        wrapper.eq("user_id", userid);
        wrapper.ne("assess_time", gettime());
        wrapper.groupBy("assess_time");
        wrapper.orderByDesc("sid");
        List<AssessHistory> assessHistories = assessHistoryMappper.selectList(wrapper);
        return assessHistories.stream().collect(Collectors.toMap(AssessHistory::getAssessTime, AssessHistory::getGrossScore, (k1, k2) -> k2, LinkedHashMap::new));
    }


    @Override
    public BigDecimal competentAuditPage(Integer id, List<AssessHistory> assessHistories) {
        QueryWrapper<Assess> wrapper2 = new QueryWrapper<>();
        wrapper2.eq("user_id", id);
        Assess assess = assessMapper.selectOne(wrapper2);
        Assert.state(!assess.getState().equals(StateEnums.State4Assess.?????????.code()), "??????????????????");
        Assert.state(!assess.getState().equals(StateEnums.State4Assess.?????????.code()), "??????????????????");
        String time = null;
//       ??????????????????
        for (AssessHistory assessHistory : assessHistories) {
            assessHistory.setUserId(id);
            assessHistory.setAssessTime(assessHistory.getAssessTime());
            time = assessHistory.getAssessTime();
            QueryWrapper<AssessHistory> wrapper = new QueryWrapper<>();
            wrapper.eq("user_id", id);
            wrapper.eq("template_id", assessHistory.getTemplateId());
            wrapper.eq("assess_time", assessHistory.getAssessTime());
            AssessHistory assessHistory1 = assessHistoryMappper.selectOne(wrapper);
            if (assessHistory1 != null) {
                UpdateWrapper<AssessHistory> wrapper1 = new UpdateWrapper<>();
                wrapper1.set("score", assessHistory.getScore());
                wrapper1.set("remark", assessHistory.getRemark());
                wrapper1.eq("user_id", id);
                wrapper1.eq("template_id", assessHistory.getTemplateId());
                wrapper1.eq("assess_time", assessHistory.getAssessTime());
                assessHistoryMappper.update(assessHistory, wrapper1);
                continue;
            }
            assessHistoryMappper.insert(assessHistory);
        }
//        ??????????????????????????????
        assessHistories = getAssessHistories(id, time);
//        ????????????
        BigDecimal score = getScore(assessHistories);
//        ?????????????????????
        for (AssessHistory assessHistory : assessHistories) {
            UpdateWrapper<AssessHistory> wrapper1 = new UpdateWrapper<>();
            wrapper1.set("gross_score", score);
            wrapper1.eq("user_id", id);
            wrapper1.eq("template_id", assessHistory.getTemplateId());
            wrapper1.eq("assess_time", assessHistory.getAssessTime());
            assessHistoryMappper.update(assessHistory, wrapper1);
        }
        assess.setScore(score);
        assess.setCreateTime(new Date());
        assess.setState(StateEnums.State4Assess.?????????.code());
        assessMapper.updateById(assess);
        return score;
    }

    /**
     * ??????????????????
     */
    private List<AssessHistory> getAssessHistories(Integer id, String time) {
        List<AssessHistory> assessHistories;
        QueryWrapper<AssessHistory> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", id);
        wrapper.eq("assess_time", time);
        assessHistories = assessHistoryMappper.selectList(wrapper);
        return assessHistories;
    }

    /**
     * ??????????????????????????????
     */
    private BigDecimal getScore(List<AssessHistory> assessHistories) {
        BigDecimal score = BigDecimal.ZERO;
        BigDecimal plus = BigDecimal.ZERO;
        BigDecimal minus = BigDecimal.ZERO;
        for (AssessHistory assessHistory : assessHistories) {
            if ("1".equals(assessHistory.getType())) {
                if (assessHistory.getScore() != null) {
                    plus = plus.add(assessHistory.getScore());
                }
            } else if ("2".equals(assessHistory.getType())) {
                if (assessHistory.getScore() != null) {
                    minus = minus.add(assessHistory.getScore());
                }
            } else {
                if (assessHistory.getScore() != null && assessHistory.getWeight() != null) {
                    BigDecimal weight = new BigDecimal(Double.valueOf(assessHistory.getWeight()) / 100);
                    score = score.add(assessHistory.getScore().multiply(weight));
                }
            }
        }
        score = score.add(plus).subtract(minus).setScale(1, RoundingMode.HALF_UP);
        return score;
    }

    private String gettime() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        return year + "/" + month;
    }


    /**
     * ?????????????????????
     */
    private IPage<Assess> getAssessIPage(AssessCondition condition, List<AssessDto> assessDtos, QueryWrapper<Assess> wrapper) {
        IPage<Assess> assessIPage = assessMapper.selectPage(new Page<>(condition.getPage(), condition.getSize()), wrapper);
        List<Assess> records = assessIPage.getRecords();
        for (Assess record : records) {
            AssessDto assessDto = new AssessDto();
            BeanUtils.copyProperties(record, assessDto);
            Department department = departmentMapper.selectById(record.getDeptId());
            assessDto.setDeptName(department.getName());
            Role role = roleMapper.selectById(record.getRoleId());
            assessDto.setRoleName(role.getName());
            assessDtos.add(assessDto);
        }
        return assessIPage;
    }


    @Override
    public Integer BossAuditPage(Integer id) {
        Assess assess = assessMapper.selectById(id);
        Assert.state(!assess.getState().equals(StateEnums.State4Assess.?????????.code()), "???????????????????????????????????????");
        assess.setState(StateEnums.State4Assess.?????????.code());
        int i = assessMapper.updateById(assess);
        approvalMsgService.sendApprovalMsg(TypeEnums.Type4ApprovalMsg.???????????????, assess.getUserId());
        return i;
    }

    @Override
    public Integer ConfirmAuditPage(Integer id) {
        Assess assess = assessMapper.selectById(id);
        Assert.state(assess.getState().equals(StateEnums.State4Assess.?????????.code()), "??????????????????");
//        ???????????????????????????????????????
        Assert.state(BaseController.getManagerInfo().getId().equals(assess.getUserId()), "?????????????????????????????????");
        assess.setState(StateEnums.State4Assess.?????????.code());
        int i = assessMapper.updateById(assess);
        approvalMsgService.sendApprovalMsg(TypeEnums.Type4ApprovalMsg.???????????????, assess.getUserId());
        return i;
    }

    @Override
    public int delhistory(String month) {
        QueryWrapper<AssessHistory> wrapper = new QueryWrapper<>();
        wrapper.eq("assess_time", month);
        return assessHistoryMappper.delete(wrapper);
    }

    @Override
    public void delAssess() {
        assessMapper.delAssess();
    }

    @Override
    public Integer getAssesspermission(Integer id) {
        Integer userid = BaseController.getManagerInfo().getId();
        if (userid.equals(departmentService.queryGeneralMrg())) {
            return 1;
        } else if (userid.equals(departmentService.queryPersonnelMrg())) {
            return 2;
        } else if (userid.equals(departmentService.getLeader(BaseController.getManagerInfo().getDepartment()))) {
            Assess assess = assessMapper.selectById(id);
            if (assess.getUserId().equals(userid)) {
                return 4;
            }
            return 3;
        }
        return 4;
    }


}
