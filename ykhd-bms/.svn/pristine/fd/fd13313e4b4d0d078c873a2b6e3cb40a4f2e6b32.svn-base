package com.ykhd.office.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ykhd.office.domain.entity.Assess;
import org.apache.ibatis.annotations.Update;

/**
 * @author zhoufan
 * @Date 2020/11/2
 */
public interface AssessMapper extends BaseMapper<Assess> {
    //清空指定表
    @Update("truncate table bms_assess")
    void delAssess();
}
