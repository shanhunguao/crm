package com.ykhd.office.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ykhd.office.domain.entity.SfSupplier;
import com.ykhd.office.domain.entity.SfSupplierDetails;
import com.ykhd.office.domain.req.SupplierCondition;
import com.ykhd.office.service.ISfSupplierDetailsService;
import com.ykhd.office.service.ISfSupplierService;
import com.ykhd.office.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhoufan
 * @Date 2020/12/17
 * 商家账户
 */
@RestController
@RequestMapping("/supplier")
public class SfSupplierController extends BaseController {

    @Autowired
    private ISfSupplierService supplierService;

    @Autowired
    private ISfSupplierDetailsService supplierDetailsService;


    /**
     * 分页查看商户账号
     */
    @GetMapping("/list")
    public Object list(SupplierCondition condition) {
        return supplierService.getListByPage(condition);
    }

    /**
     * 分页查看商户账号
     */
    @GetMapping("/list2")
    public Object list2() {
        QueryWrapper<SfSupplierDetails> wrapper = new QueryWrapper<SfSupplierDetails>().select("company_name");
        List<SfSupplierDetails> list = supplierDetailsService.list(wrapper);
        List<String> list1 = new ArrayList<>();
        list.forEach(e->{
            list1.add(e.getCompanyName());
        });
        List<String> collect = list1.stream().distinct().collect(Collectors.toList());
        return collect;
    }


    /**
     * 获取商户详情
     */
    @GetMapping("/find")
    public Object find(Integer id) {
        QueryWrapper<SfSupplierDetails> wrapper = new QueryWrapper<>();
        wrapper.eq("supplier_userid", id);
        SfSupplierDetails supplierDetails = supplierDetailsService.getOne(wrapper);
        if (supplierDetails != null) {
            return supplierDetails;
        }
        return success(null);
    }

    /**
     * 添加商户账号
     */
    @PostMapping("/addSupplier")
    public Object addSupplier(SfSupplier supplier) {
        supplier.setPassword(SecurityUtil.md5Encode(supplier.getPassword()));
        return supplierService.save(supplier);
    }


    /**
     * 删除商户账号以及详情
     */
    @DeleteMapping("/delSupplier")
    public Object delSupplier(Integer id) {
        QueryWrapper<SfSupplierDetails> wrapper = new QueryWrapper<>();
        wrapper.eq("supplier_userid", id);
        supplierDetailsService.remove(wrapper);
        return supplierService.removeById(id);
    }

    /**
     * 修改商户信息
     */
    @PutMapping("/updateSupplier")
    public Object updateSupplier(SfSupplier supplier) {
        supplier.setPassword(SecurityUtil.md5Encode(supplier.getPassword()));
        return supplierService.updateById(supplier);
    }

    /**
     * 重置商户账号
     */
    @PutMapping("/resetPwd")
    public Object resetPwd(SfSupplier supplier) {
        supplier.setPassword(SecurityUtil.md5Encode("123456"));
        return supplierService.updateById(supplier);
    }

}
