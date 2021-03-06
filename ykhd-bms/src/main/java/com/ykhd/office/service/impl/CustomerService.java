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
import com.ykhd.office.domain.entity.Customer;
import com.ykhd.office.domain.entity.Department;
import com.ykhd.office.domain.entity.Industry;
import com.ykhd.office.domain.req.CustomerCondition;
import com.ykhd.office.domain.req.CustomerSubmit;
import com.ykhd.office.domain.resp.CustomerDto;
import com.ykhd.office.domain.resp.Workbench;
import com.ykhd.office.repository.CustomerMapper;
import com.ykhd.office.repository.DepartmentMapper;
import com.ykhd.office.repository.IndustryMapper;
import com.ykhd.office.service.ICustomerService;
import com.ykhd.office.util.DateUtil;
import com.ykhd.office.util.dictionary.SystemEnums;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CustomerService extends ServiceImpl<BaseMapper<Customer>, Customer> implements ICustomerService {

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private IndustryMapper industryMapper;

    @Autowired
    private ManagerService managerService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private DepartmentMapper departmentMapper;


    @Override
    public PageHelper<CustomerDto> getListByPage(CustomerCondition condition) {
        IPage<CustomerDto> iPage = customerMapper.getListByPage(new Page<>(condition.getPage(), condition.getSize()), condition);
        List<CustomerDto> list = iPage.getRecords();
//        ?????????????????????????????? ??????????????????
        for (CustomerDto customerDto : list) {
            if (condition.getIsCooperate() != null) {
                if ("0".equals(condition.getIsCooperate())) {
                    String endorder = customerMapper.getendOrder(customerDto.getId());
                    customerDto.setEndOrder(endorder);
                    customerDto.setIsCooperate("0");
                } else {
                    customerDto.setIsCooperate("1");
                }
            } else {
                String endorder = customerMapper.getendOrder(customerDto.getId());
                if (StringUtils.isNotEmpty(endorder)) {
                    customerDto.setEndOrder(endorder);
                    customerDto.setIsCooperate("0");
                } else {
                    customerDto.setIsCooperate("1");
                }
            }
            customerDto.setEndOrder(customerMapper.getendOrder(customerDto.getId()));
            QueryWrapper<Industry> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("industry_id", customerDto.getIndustry());
            Industry industry = industryMapper.selectOne(queryWrapper);
            if (industry != null) {
                customerDto.setIndustryName(industry.getIndustryName());
            }
            QueryWrapper<Industry> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.eq("industry_id", customerDto.getDomain());
            Industry industry1 = industryMapper.selectOne(queryWrapper2);
            if (industry1 != null) {
                customerDto.setDomainName(industry1.getIndustryName());

            }
        }

        return new PageHelper<>(condition.getPage(), condition.getSize(), customerMapper.getListByPageCount(condition), list);
    }

//    @Override
//    public PageHelper<CustomerDto> getShareListByPage(CustomerCondition condition) {
//        QueryWrapper<CustomerShare> wrapper = new QueryWrapper<>();
//        wrapper.eq("user_id", BaseController.getManagerInfo().getId());
//        IPage<CustomerShare> customerShareIPage = customerShareMapper.selectPage(new Page<>(condition.getPage(), condition.getSize()), wrapper);
//        List<CustomerShare> customerShares = customerShareIPage.getRecords();
//        List<CustomerDto> list = new ArrayList<>();
//        if (customerShares != null && customerShares.size() > 0) {
//            customerShares.forEach(e -> {
//                CustomerDto customerDto = new CustomerDto();
//                Customer customer = customerMapper.selectById(e.getCustomerId());
//                BeanUtils.copyProperties(customer, customerDto);
//                list.add(customerDto);
//            });
//        }
//        return new PageHelper<>(condition.getPage(), condition.getSize(), customerShareIPage.getTotal(), list);
//    }


    @Override
    public boolean addCustomerApp(Integer userId, CustomerSubmit customerSubmit) {
        check(customerSubmit);
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerSubmit, customer);
        customer.setCreator(userId);
        customer.setCreateTime(new Date());
        return save(customer);
    }

    private void check(CustomerSubmit customerSubmit) {
        Map<String, String> condition = new HashMap<>();
        List<Customer> customer = null;
        if (StringUtils.isNotEmpty(customerSubmit.getBrand())) {
            condition.put("brand", customerSubmit.getBrand());
            customer = customerMapper.find(condition);
            Assert.state(customer.size() == 0, "??????????????????????????????????????????????????????????????????????????????????????????????????????");
        }

    }


    @Override
    public boolean updateCustomerApp(CustomerSubmit customerSubmit) {
        Customer customer = getById(customerSubmit.getId());
        if (!customer.getBrand().equals(customerSubmit.getBrand())) {
            check(customerSubmit);
        }
        BeanUtils.copyProperties(customerSubmit, customer);
        UpdateWrapper<Customer> wrapper = new UpdateWrapper<>();
        wrapper.set("company", customer.getCompany());
        wrapper.set("brand", customer.getBrand());
        wrapper.set("industry", customer.getIndustry());
        wrapper.set("domain", customer.getDomain());
        wrapper.set("linkman", customer.getLinkman());
        wrapper.set("phone", customer.getPhone());
        wrapper.set("qq", customer.getQq());
        wrapper.set("wx", customer.getWx());
        wrapper.set("belong_user", customer.getBelongUser());
        wrapper.eq("id", customer.getId());
        return update(wrapper);
    }

    @Override
    public boolean deleteCustomer(String id) {
        return removeById(id);
    }

    @Override
    public boolean getCustomer(Integer id) {
        Customer customer = customerMapper.selectById(id);
        UpdateWrapper<Customer> customerUpdateWrapper = new UpdateWrapper<>();
        customerUpdateWrapper.set("belong_user", BaseController.getManagerInfo().getId());
        customerUpdateWrapper.set("is_lock", 0);
        customerUpdateWrapper.set("draw_time", new Date());
        customerUpdateWrapper.eq("id", customer.getId());
        return customerMapper.update(customer, customerUpdateWrapper) > 0;
    }

    @Override
    public boolean delCustomer(Integer id) {
        Customer customer = customerMapper.selectById(id);
        UpdateWrapper<Customer> customerUpdateWrapper = new UpdateWrapper<>();
        customerUpdateWrapper.set("belong_user", null);
        customerUpdateWrapper.set("is_lock", 1);
        customerUpdateWrapper.set("high_sea_time", new Date());
        customerUpdateWrapper.set("draw_time", null);
        customerUpdateWrapper.eq("id", customer.getId());
        //??????????????????
        return customerMapper.update(customer, customerUpdateWrapper) > 0;
    }

    @Override
    public List<Customer> getBrandByUserid(Integer userId) {
//       ????????????????????????????????????????????? ????????????????????????????????????
        Department department = departmentMapper.selectById(BaseController.getManagerInfo().getDepartment());
        if (department.getName().contains("???")) {
            List<Integer> userids = managerService.queryIdsByDepartment(BaseController.getManagerInfo().getDepartment());
            QueryWrapper<Customer> queryWrapper = new QueryWrapper<>();
            queryWrapper.in("belong_user", userids);
            return customerMapper.selectList(queryWrapper);
        }
        QueryWrapper<Customer> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("belong_user", userId);
        return customerMapper.selectList(queryWrapper);
////       ???????????????????????????
//        QueryWrapper<CustomerShare> wrapper = new QueryWrapper<>();
//        wrapper.eq("user_id", userId);
//        List<CustomerShare> customerShares = customerShareMapper.selectList(wrapper);
//        List<Customer> customers2 = new ArrayList<>();
//        if (customerShares != null && customerShares.size() > 0) {
//            customerShares.forEach(e -> {
//                customers2.add(customerMapper.selectById(e.getCustomerId()));
//            });
//        }
////        ?????????????????????????????????
//        customers.addAll(customers2);
//        return customers;
    }

    @Override
    public List<Integer> queryIdsBybrand(String brand) {
        return list(new QueryWrapper<Customer>().select("id").like("brand", brand)).stream().map(v -> v.getId()).collect(Collectors.toList());
    }

    @Override
    public List<Integer> queryIdsByCompany(String company) {
        return list(new QueryWrapper<Customer>().select("id").like("company", company)).stream().map(v -> v.getId()).collect(Collectors.toList());
    }

    @Override
    public List<Integer> queryIdsByBusiness(Integer businessid) {
        return list(new QueryWrapper<Customer>().select("id").eq("creator", businessid)).stream().map(v -> v.getId()).collect(Collectors.toList());
    }

    @Override
    public List<Customer> toHighseasCustomer() {
        return customerMapper.toHighseasCustomer();
    }

    @Override
    public Workbench workbenchInfo(String id, String dept_id, String date_type, String startdate, String enddate) {
        Map<String, Object> condition = getMap(id, dept_id, date_type, startdate, enddate);
        Workbench thisMonth = customerMapper.workbenchInfo(condition);
        LocalDateTime date = LocalDateTime.now().minusMonths(1);
        LocalDateTime firstday = date.with(TemporalAdjusters.firstDayOfMonth());
        startdate = firstday.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Date date2 = DateUtil.monthsAgo(new Date(), 1);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        enddate = formatter.format(date2);
        condition = getMap(id, dept_id, date_type, startdate, enddate);
        Workbench lastMonth = customerMapper.workbenchInfo(condition);
//        ?????????????????????
        if (lastMonth.getNewCustomer() != 0) {
            thisMonth.setNewCusRate(BigDecimal.valueOf((thisMonth.getNewCustomer() - lastMonth.getNewCustomer()) * 100 / lastMonth.getNewCustomer()));
        }
        if ((lastMonth.getOrders() != 0)) {
            thisMonth.setOrdersRate(BigDecimal.valueOf((thisMonth.getOrders() - lastMonth.getOrders()) * 100 / lastMonth.getOrders()));
        }
        if (lastMonth.getMonthExe() != null && thisMonth.getMonthExe() != null) {
            thisMonth.setMonthExeRate(thisMonth.getMonthExe().subtract(lastMonth.getMonthExe()).multiply(new BigDecimal(100)).divide(lastMonth.getMonthExe(), 2, RoundingMode.HALF_UP));
        }
        if (lastMonth.getMonthPro() != null && thisMonth.getMonthPro() != null) {
            thisMonth.setMonthProRate(thisMonth.getMonthPro().subtract(lastMonth.getMonthPro()).multiply(new BigDecimal(100)).divide(lastMonth.getMonthPro(), 2, RoundingMode.HALF_UP));
        }
        thisMonth.setMonthExe(thisMonth.getMonthExe().setScale(2, RoundingMode.HALF_UP));
        thisMonth.setMonthPro(thisMonth.getMonthPro().setScale(2, RoundingMode.HALF_UP));
        return thisMonth;
    }

    /**
     * ???????????????????????????
     */
    private Map<String, Object> getMap(String id, String dept_id, String date_type, String startdate, String enddate) {
        Map<String, Object> condition = new HashMap<>();
        SystemEnums.RoleSign sign = roleService.judgeRole(BaseController.getManagerInfo().getRole());
//        ?????????????????????????????????
        if (sign == SystemEnums.RoleSign.dept || sign == SystemEnums.RoleSign.group) {
            if (StringUtils.isNotEmpty(dept_id)) {
                Department department = departmentMapper.selectById(dept_id);
                finddept(condition, department.getId(), department.getLeader());
            } else {
                Department department = departmentMapper.selectById(BaseController.getManagerInfo().getDepartment());
                finddeptall(id, department.getId(), condition, department);
            }
        } else if (sign == SystemEnums.RoleSign.ae) {
            List<String> list = new ArrayList<>();
            list.add(String.valueOf(BaseController.getManagerInfo().getId()));
            condition.put("createrids", list);
        } else {
            if (StringUtils.isNotEmpty(dept_id)) {
                Department department = departmentMapper.selectById(dept_id);
                finddeptall(id, department.getId(), condition, department);
            } else if (StringUtils.isNotEmpty(id)) {
                List<String> list = new ArrayList<>();
                list.add(id);
                condition.put("createrids", list);
            }
        }
        //????????????
        if (StringUtils.isNotEmpty(startdate)) {
            if (date_type.equals("1")) {//????????????
                condition.put("createrdate_start", startdate);
            } else if (date_type.equals("2")) {//????????????
                condition.put("putdate_start", startdate);
            } else if (date_type.equals("3")) {//????????????
                condition.put("remoneydate_start", startdate);
            } else if (date_type.equals("4")) {//????????????
                condition.put("paymentdate_start", startdate);
            }
        }
        if (StringUtils.isNotEmpty(enddate)) {
            if (date_type.equals("1")) {
                condition.put("createrdate_end", enddate);
            } else if (date_type.equals("2")) {
                condition.put("putdate_end", enddate);
            } else if (date_type.equals("3")) {
                condition.put("remoneydate_end", enddate);
            } else if (date_type.equals("4")) {//????????????
                condition.put("paymentdate_end", enddate);
            }
        }
        return condition;
    }

    private void finddept(Map<String, Object> condition, Integer department, Integer learder) {
        List<Integer> ae = managerService.queryIdsByDeptIdRoleName(department, "AE");
        ae.add(learder);
        condition.put("createrids", ae);
    }

    /**
     * ??????????????????
     */
    private void finddeptall(String id, Integer dept_id, Map<String, Object> condition, Department department) {
        QueryWrapper<Department> wrapper = new QueryWrapper<>();
        wrapper.eq("parent", dept_id);
        Department department1 = departmentMapper.selectOne(wrapper);
//                    ?????????????????????????????????????????????????????????
        if (department1 != null) {
            List<Integer> ae = managerService.queryIdsByDeptIdRoleName(department1.getId(), "AE");
            ae.add(department1.getLeader());
            List<Integer> ae2 = managerService.queryIdsByDeptIdRoleName(department1.getParent(), "AE");
            ae2.add(department.getLeader());
            ae.addAll(ae2);
            condition.put("createrids", ae);
            if (StringUtils.isNotEmpty(id)) {
                List<String> list = new ArrayList<>();
                list.add(id);
                condition.put("createrids", list);
            }
        } else {
            finddept(condition, department.getId(), department.getLeader());
            if (StringUtils.isNotEmpty(id)) {
                List<String> list = new ArrayList<>();
                list.add(id);
                condition.put("createrids", list);
            }
        }

    }


    @Override
    public List<Map<String, Object>> industryRate(String id, String dept_id, String date_type, String startdate, String enddate) {
        Map<String, Object> condition = getMap(id, dept_id, date_type, startdate, enddate);
        return customerMapper.industryRate(condition);
    }

    @Override
    public List<Map<String, Object>> findInfoByCustomerId(Integer customerid) {
        return customerMapper.findInfoByCustomerId(customerid);
    }

    @Override
    public boolean updateDrawTime(Integer id) {
        return update(new UpdateWrapper<Customer>().eq("id", id).set("draw_time", new Date()));
    }

//    @Override
//    public void share(Integer customerid, Integer userid) {
//        QueryWrapper<Customer> wrapper1 = new QueryWrapper<>();
//        wrapper1.eq("id", customerid);
//        Assert.state(BaseController.getManagerInfo().getId().equals(customerMapper.selectOne(wrapper1).getBelongUser()), "??????????????????????????????");
////        ??????????????????????????????
//        QueryWrapper<CustomerShare> wrapper = new QueryWrapper<>();
//        wrapper.eq("customer_id", customerid);
//        wrapper.eq("share_id", BaseController.getManagerInfo().getId());
//        wrapper.eq("user_id", userid);
//        CustomerShare customerShare = customerShareMapper.selectOne(wrapper);
//        Assert.state(customerShare == null, "????????????????????????");
////        ??????????????????
//        customerShare = new CustomerShare();
//        customerShare.setShareId(BaseController.getManagerInfo().getId());
//        customerShare.setUserId(userid);
//        customerShare.setCustomerId(customerid);
//        customerShareMapper.insert(customerShare);
//    }
//
//    @Override
//    public void noshare(Integer customerid) {
//        QueryWrapper<CustomerShare> wrapper = new QueryWrapper<>();
//        wrapper.eq("customer_id", customerid);
//        wrapper.eq("user_id", BaseController.getManagerInfo().getId());
//        customerShareMapper.delete(wrapper);
//    }


}
