package com.ykhd.office.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ykhd.office.domain.bean.common.PageHelper;
import com.ykhd.office.domain.entity.Customer;
import com.ykhd.office.domain.resp.Workbench;
import com.ykhd.office.domain.req.CustomerCondition;
import com.ykhd.office.domain.req.CustomerSubmit;
import com.ykhd.office.domain.resp.CustomerDto;

import java.util.List;
import java.util.Map;

public interface ICustomerService extends IService<Customer> {

    /**
     * 查询客户列表
     */
    PageHelper<CustomerDto> getListByPage(CustomerCondition condition);

//    PageHelper<CustomerDto> getShareListByPage(CustomerCondition condition);

    boolean addCustomerApp(Integer userId, CustomerSubmit customerSubmit);

    boolean updateCustomerApp(CustomerSubmit customerSubmit);

    boolean deleteCustomer(String id);

    boolean getCustomer(Integer id);

    boolean delCustomer(Integer id);

    List<Customer> getBrandByUserid(Integer userId);

    /**
     * api: brand --> id
     */
    List<Integer> queryIdsBybrand(String brand);

    /**
     * api: company --> id
     */
    List<Integer> queryIdsByCompany(String company);

   /* *
    * 查询商务创建的客户ID
    */
    List<Integer> queryIdsByBusiness(Integer businessid);

    List<Customer> toHighseasCustomer();

    Workbench workbenchInfo(String id,String dept_id, String date_type, String startdate, String enddate);

    List<Map<String, Object>> industryRate(String id,String dept_id,String date_type, String startdate, String enddate);

    List<Map<String, Object>> findInfoByCustomerId(Integer customerid);

    /**
     * api: 更新客户的领取时间
     */
    boolean updateDrawTime(Integer id);

    /**
     * 共享客户
     */
//    void share(Integer customerid, Integer userid);
//
//    /**
//     * 取消共享客户
//     */
//    void noshare(Integer customerid);


}
