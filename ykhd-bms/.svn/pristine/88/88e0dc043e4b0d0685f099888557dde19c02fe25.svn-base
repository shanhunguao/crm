package com.ykhd.office.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ykhd.office.domain.entity.Assess;
import com.ykhd.office.domain.entity.AssessHistory;
import com.ykhd.office.domain.entity.Manager;
import com.ykhd.office.domain.req.AssessCondition;
import com.ykhd.office.service.IAssessService;
import com.ykhd.office.service.IManagerService;

/**
 * @author zhoufan
 * @Date 2020/11/2
 */
@RestController
@RequestMapping("/assess")
public class AssessController extends BaseController {

    @Autowired
    private IAssessService assessService;

    @Autowired
    private IManagerService managerService;


    /**
     * 查看绩效
     */
    @GetMapping("/list")
    public Object list(AssessCondition assessCondition) {
        return assessService.list(assessCondition);
    }


    /**
     * 补充绩效
     *
     * @param name 员工名 type 0当月绩效 1上月绩效
     */
    @PostMapping("/add")
    public Object add(String name, String assessTime) {
        if (assessTime.contains("/0")) {
            assessTime = assessTime.replace("/0", "/");
        }
        QueryWrapper<Manager> wrapper = new QueryWrapper<>();
        wrapper.eq("name", name);
        Manager manager = managerService.getOne(wrapper);
        Assert.state(manager != null, "员工信息不存在");
        Assess assess = new Assess();
        assess.setName(name);
        assess.setUserId(manager.getId());
        Assert.state(manager.getDepartment() != null, "部门信息不存在");
        assess.setDeptId(manager.getDepartment());
        Assert.state(manager.getRole() != null, "角色信息不存在");
        assess.setRoleId(manager.getRole());
        assess.setCreateTime(new Date());
        assess.setAssessTime(assessTime);
//        检验绩效是否已存在
        QueryWrapper<Assess> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("name", name);
        Assert.state(assessService.getOne(wrapper1) == null, "考核员工信息已存在");
        return assessService.save(assess);
    }


    /**
     * 查看绩效打分历史记录
     */
    @GetMapping("getHistory")
    public Object getHistory(Integer userid, String assessTime) {
        return assessService.getHistory(userid, assessTime);
    }


    /**
     * 查看绩效打分历史记录列表
     */
    @GetMapping("getHistoryList")
    public Object getHistoryList(Integer userid) {
        return assessService.getHistoryList(userid);
    }

    /**
     * 查看上次打分历史
     */
    @GetMapping("getScoreHistory")
    public Object getScoreHistory(Integer userid, Integer templateid, String assessTime) {
        AssessHistory scoreHistory = assessService.getScoreHistory(userid, templateid, assessTime);
        if (scoreHistory == null) {
            return success(null);
        }
        return scoreHistory;
    }

    /**
     * 绩效打分
     */
    @PutMapping("/competentAuditPage/{userid}")
    public Object competentAuditPage(@PathVariable("userid") Integer id, @RequestBody List<AssessHistory> assessHistories) {
        return assessService.competentAuditPage(id, assessHistories);
    }


    /**
     * 总经理考核绩效
     */
    @PutMapping("/BossAuditPage")
    public Object BosssAuditPage(Integer id) {
        return assessService.BossAuditPage(id);
    }

    /**
     * 员工确认考核绩效
     */
    @PutMapping("/ConfirmAuditPage")
    public Object ConfirmAuditPage(Integer id) {
        return assessService.ConfirmAuditPage(id);
    }


    /**
     * 实时刷新状态
     */
    @GetMapping("findstatus")
    public Object findstatus(Integer id, String assessTime) {
        QueryWrapper<Assess> wrapper = new QueryWrapper<>();
        wrapper.eq("id", id);
        wrapper.eq("assess_time", assessTime);
        return assessService.getOne(wrapper);
    }


    /**
     * 人事经理每月发起考核
     */
    @PostMapping("/initiate")
    public Object initiate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        // 获取当前日
        int day = cal.get(Calendar.DATE);
        int month;
//       若发起绩效考核的时间小于15号 就是发起上个月 反之是发起当月绩效考核
        if (day <= 15) {
            month = cal.get(Calendar.MONTH);
        } else {
            month = cal.get(Calendar.MONTH) + 1;
        }
        String thismonth = year + "/" + month;
        QueryWrapper<Assess> assess2 = new QueryWrapper<>();
        assess2.eq("assess_time", thismonth);
        List<Assess> list1 = assessService.list(assess2);
        if (list1 != null) {
         Assert.state(list1.size() <= 0, "不允许重复发起绩效考核");
        }
        boolean i = false;
        QueryWrapper<Manager> wrapper = new QueryWrapper<>();
        wrapper.eq("status", 0);
        wrapper.eq("is_deleted", 0);
        wrapper.eq("position_status", "正式");
        wrapper.isNotNull("department");
        wrapper.isNotNull("role");
        List<Manager> list = managerService.list(wrapper);
//        重新生成绩效
        assessService.delAssess();
        for (Manager manager : list) {
            if (manager.getId().equals(1)) {
                continue;
            }
            Assess assess = new Assess();
            assess.setName(manager.getName());
            assess.setUserId(manager.getId());
            assess.setDeptId(manager.getDepartment());
            assess.setRoleId(manager.getRole());
            assess.setCreateTime(new Date());
            assess.setAssessTime(thismonth);
            i = assessService.save(assess);
        }
        //情况当月绩效历史
//        assessService.delhistory(thismonth);
        return i;
    }

    /**
     * 获取绩效权限
     */
    @GetMapping("/getAssesspermission")
    public Object getAssesspermission(Integer id) {
        return assessService.getAssesspermission(id);
    }


}
