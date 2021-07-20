package com.ykhd.office.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ykhd.office.domain.bean.common.PageHelper;
import com.ykhd.office.domain.entity.SfSupplier;
import com.ykhd.office.domain.req.SupplierCondition;
import com.ykhd.office.domain.resp.SupplierDto;

public interface ISfSupplierService extends IService<SfSupplier> {
    /**
     * 分页查询商户信息
     */
    PageHelper<SupplierDto> getListByPage(SupplierCondition condition);

}
