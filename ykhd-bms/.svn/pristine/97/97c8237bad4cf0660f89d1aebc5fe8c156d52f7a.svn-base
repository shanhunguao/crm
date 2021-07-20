package com.ykhd.office.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ykhd.office.domain.entity.CustomerFollowUp;
import com.ykhd.office.domain.req.RecordsCondition;
import com.ykhd.office.domain.req.RecordsSubmit;
import com.ykhd.office.domain.resp.CustomerFollowUpDto;
import com.ykhd.office.service.impl.ManagerService;
import com.ykhd.office.service.impl.RecordsService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author zhoufan
 * @Date 2020/8/24
 */
@RestController
@RequestMapping("/records")
public class RecordsController extends BaseController {

    @Autowired
    private RecordsService recordsService;

    @Autowired
    private ManagerService managerService;

    @RequestMapping("list")
    public Object list(RecordsCondition recordsCondition) {

        QueryWrapper<CustomerFollowUp> wrapper = new QueryWrapper<>();
        if (recordsCondition.getCustomerId() != null) {
            wrapper.eq("customer_id", recordsCondition.getCustomerId());

        }
        if (!StringUtils.isEmpty(recordsCondition.getRecord())) {
            wrapper.like("record", recordsCondition.getRecord());
        }
        List<CustomerFollowUp> list = recordsService.list(wrapper);
        List<CustomerFollowUpDto> list2 = new ArrayList<>();
        for (CustomerFollowUp customerFollowUp : list) {
            CustomerFollowUpDto followUpDto = new CustomerFollowUpDto();
            BeanUtils.copyProperties(customerFollowUp, followUpDto);
            followUpDto.setManagerName(managerService.getById(customerFollowUp.getManagerId()).getName());
            list2.add(followUpDto);
        }
        return list2 != null ? success(list2) : failure("查询拜访列表失败");
    }


    @RequestMapping("add")
    public Object add(@Valid RecordsSubmit recordsSubmit) {
        CustomerFollowUp records = new CustomerFollowUp();
        BeanUtils.copyProperties(recordsSubmit, records);
        records.setManagerId(BaseController.getManagerInfo().getId());
        records.setCreateTime(new Date());
        return recordsService.save(records) == true ? success("添加拜访记录成功") : failure(save_failure);
    }

}
