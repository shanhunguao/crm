package com.ykhd.office.controller;

import com.ykhd.office.domain.entity.Department;
import com.ykhd.office.domain.req.CustomerCondition;
import com.ykhd.office.domain.req.CustomerSubmit;
import com.ykhd.office.service.ICustomerService;
import com.ykhd.office.service.IManagerService;
import com.ykhd.office.service.IRoleService;
import com.ykhd.office.service.impl.DepartmentService;
import com.ykhd.office.util.dictionary.SystemEnums;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/customer")
public class CustomerController extends BaseController {

    @Autowired
    private ICustomerService customerService;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IManagerService managerService;

    @Autowired
    private DepartmentService departmentService;

    /**
     * 客户列表
     */
    @GetMapping("/list")
    public Object list(CustomerCondition condition) {
        if (condition.getIsLock() != null && condition.getIsLock() == 0) {
            if (condition.getBelongUser() == null) {
                Department department = departmentService.getById(BaseController.getManagerInfo().getDepartment());
                if (department.getName().contains("组")) {
                    condition.setDeptbelongUser(managerService.queryIdsByDepartment(BaseController.getManagerInfo().getDepartment()));
                } else {
                    SystemEnums.RoleSign sign = roleService.judgeRole(BaseController.getManagerInfo().getRole());
                    if (sign == SystemEnums.RoleSign.dept) {
                        condition.setDeptbelongUser(managerService.queryIdsByDepartment(BaseController.getManagerInfo().getDepartment()));
                    } else if (sign == SystemEnums.RoleSign.ae) {
                        condition.setBelongUser(BaseController.getManagerInfo().getId());
                    }
                }
            }
        }
        return customerService.getListByPage(condition);
    }

    /**
     * 共享客户列表
     */
//    @GetMapping("/sharelist")
//    public Object sharelist(CustomerCondition condition) {
//        return customerService.getShareListByPage(condition);
//
//    }


    /**
     * 添加客户
     */
    @PostMapping("/add")
    public Object add(@Valid CustomerSubmit customerSubmit) {
        return customerService.addCustomerApp(BaseController.getManagerInfo().getId(), customerSubmit) == true ? success("添加客户成功") : failure(save_failure);
    }

    /**
     * 删除客户
     */
    @DeleteMapping("/delete")
    public Object delete(String id) {
        return customerService.deleteCustomer(id) == true ? success("删除客户成功") : failure(delete_failure);

    }

    /**
     * 修改客户
     */
    @PutMapping("/update")
    public Object update(@Valid CustomerSubmit customerSubmit) {
        return customerService.updateCustomerApp(customerSubmit) == true ? success("修改客户成功") : failure(update_failure);
    }

    /**
     * 获取自己的客户品牌列表
     */
    @GetMapping("getBrandByUserid")
    public Object getBrandByUserid() {
        return customerService.getBrandByUserid(BaseController.getManagerInfo().getId());
    }

    /**
     * 领取客户
     */
    @PutMapping("getCustomer")
    public Object getCustomer(Integer id) {
        return customerService.getCustomer(id) == true ? success("领取客户成功") : failure(update_failure);
    }


    /**
     * 踢出客户
     */
    @PutMapping("delCustomer")
    public Object delCustomer(Integer id) {
        return customerService.delCustomer(id) == true ? success("踢出公海成功") : failure(update_failure);
    }


    /**
     * 工作台统计信息
     *
     * @return
     */
    @GetMapping("workbenchInfo")
    public Object workbenchInfo(@RequestParam(required = false) String id,
                                @RequestParam(required = false) String dept_id,
                                @RequestParam(required = false) String date_type,
                                @RequestParam(required = false) String startdate,
                                @RequestParam(required = false) String enddate) {
        return customerService.workbenchInfo(id, dept_id,date_type, startdate, enddate);
    }

    /**
     * 客户行业分类流水环比
     *
     * @return
     */
    @GetMapping("industryRate")
    public Object industryRate(@RequestParam(required = false) String id,
                               @RequestParam(required = false) String dept_id,
                               @RequestParam(required = false) String date_type,
                               @RequestParam(required = false) String startdate,
                               @RequestParam(required = false) String enddate) {
        return customerService.industryRate(id, dept_id,date_type, startdate, enddate);
    }

    /**
     * 合作客户详情
     */
    @GetMapping("findInfoByCustomerId")
    public Object findInfoByCustomerId(Integer customerid) {
        return customerService.findInfoByCustomerId(customerid);
    }

    /**
     * 共享客户
     */
//    @PostMapping("share")
//    public Object share(Integer customerid, Integer userid) {
//        customerService.share(customerid, userid);
//        return success("共享客户成功");
//    }
//
//    /**
//     * 取消共享客户
//     */
//    @DeleteMapping("noshare")
//    public Object noshare(Integer customerid) {
//        customerService.noshare(customerid);
//        return success("取消共享客户成功");
//    }

}
