package com.ykhd.office.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ykhd.office.domain.entity.Assess;
import com.ykhd.office.domain.entity.AssessTemplate;
import com.ykhd.office.service.IAssessService;
import com.ykhd.office.service.IAssessTemplateService;
import com.ykhd.office.service.IRoleService;

/**
 * @author zhoufan
 * @Date 2020/11/2
 */
@RestController
@RequestMapping("/assessTemplate")
public class AssessTemplateController extends BaseController {

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IAssessTemplateService assessTemplateService;

    @Autowired
    private IAssessService AssessService;

    @GetMapping("list")
    public Object list() {
        return roleService.list();
    }

    /**
     * 查看模板
     */
    @GetMapping("/getTemplate")
    public Object getTemplate(Integer roleid) {
        QueryWrapper<AssessTemplate> wrapper = new QueryWrapper<>();
        wrapper.eq("role_id", roleid);
        return assessTemplateService.list(wrapper);
    }


    /**
     * 编辑模板
     */
    @PostMapping("/toAuditTemplate/{roleid}")
    public Object add(@PathVariable("roleid") Integer roleid, @RequestBody List<AssessTemplate> assessTemplates) {
        //判断模板是否正在使用
        QueryWrapper<Assess> assess = new QueryWrapper<>();
        assess.ne("state", 0);
        Assert.state(AssessService.count(assess) <= 0, "模板正在使用");
//        添加模板
        QueryWrapper<AssessTemplate> wrapper = new QueryWrapper<>();
        wrapper.eq("role_id", roleid);
        List<AssessTemplate> list = assessTemplateService.list(wrapper);
        if (list != null && list.size() > 0) {
            assessTemplateService.remove(wrapper);
        }
        for (AssessTemplate assessTemplate : assessTemplates) {
            assessTemplate.setRoleId(roleid);
        }
        return assessTemplateService.saveBatch(assessTemplates);
    }


    /**
     * 删除模板
     */
    @DeleteMapping("/delete")
    public Object delete(Integer roleid) {
        QueryWrapper<Assess> assess = new QueryWrapper<>();
        assess.ne("state", 0);
        Assert.state(AssessService.count(assess) <= 0, "模板正在使用");
        QueryWrapper<AssessTemplate> wrapper = new QueryWrapper<>();
        wrapper.eq("role_id", roleid);
        return assessTemplateService.remove(wrapper);
    }


}
