package com.ykhd.office.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ykhd.office.domain.entity.SfMedium;
import com.ykhd.office.domain.req.SfMediumCondition;
import com.ykhd.office.service.ISfMediumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

/**
 * @author zhoufan
 * @Date 2020/12/23
 * 赛峰渠道
 */
@RestController
@RequestMapping("/sfmedium")
public class SfMediumController {
    @Autowired
    private ISfMediumService mediumService;

    /**
     * 获取赛峰渠道列表
     */
    @GetMapping("/list")
    public Object list(SfMediumCondition sfMediumCondition) {
        return mediumService.list(sfMediumCondition);
    }

    /**
     * 添加赛峰渠道
     */
    @PostMapping("/save")
    public Object save(SfMedium sfMedium) {
        QueryWrapper<SfMedium> wrapper = new QueryWrapper<>();
        wrapper.eq("name", sfMedium.getName());
        SfMedium medium = mediumService.getOne(wrapper);
        Assert.state(medium == null, "渠道名称已存在");
        sfMedium.setCreateUserid(BaseController.getManagerInfo().getId());
        return mediumService.save(sfMedium);
    }

    /**
     * 删除赛峰渠道
     */
    @DeleteMapping("/delete")
    public Object delete(Integer id) {
        return mediumService.removeById(id);
    }

    /**
     * 修改赛峰渠道
     */
    @PutMapping("/update")
    public Object update(SfMedium sfMedium) {
        return mediumService.updateById(sfMedium);
    }


}
