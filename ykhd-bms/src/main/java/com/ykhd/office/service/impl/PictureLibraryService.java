package com.ykhd.office.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ykhd.office.controller.BaseController;
import com.ykhd.office.domain.bean.common.PageHelper;
import com.ykhd.office.domain.entity.PictureLibrary;
import com.ykhd.office.domain.req.PictureLibraryCondition;
import com.ykhd.office.repository.PictureLibraryMapper;
import com.ykhd.office.service.IPictureLibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PictureLibraryService extends ServiceImpl<BaseMapper<PictureLibrary>, PictureLibrary> implements IPictureLibraryService {

    @Autowired
    private PictureLibraryMapper pictureLibraryMapper;

    @Override
    public PageHelper<PictureLibrary> list(PictureLibraryCondition condition) {
        Integer userid = BaseController.getManagerInfo().getId();
        QueryWrapper<PictureLibrary> wrapper = new QueryWrapper<>();
        wrapper.eq("supplier_userid", userid);
        wrapper.eq("type",1);
        if (StringUtils.isNotEmpty(condition.getImagesName())) {
            wrapper.like("images_name", condition.getImagesName());
        }
        wrapper.orderByDesc("create_time");
        IPage<PictureLibrary> iPage = pictureLibraryMapper.selectPage(new Page<>(condition.getPage(), condition.getSize()), wrapper);
        return new PageHelper<>(condition.getPage(), condition.getSize(), iPage.getTotal(), iPage.getRecords());
    }
}
