package com.ykhd.office.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ykhd.office.domain.bean.common.PageHelper;
import com.ykhd.office.domain.entity.Assess;
import com.ykhd.office.domain.entity.AssessHistory;
import com.ykhd.office.domain.req.AssessCondition;
import com.ykhd.office.domain.resp.AssessDto;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author zhoufan
 * @Date 2020/11/2
 */
public interface IAssessService extends IService<Assess> {

    /**
     * 查询员工绩效
     */
    PageHelper<AssessDto> list(AssessCondition assessCondition);


    /**
     * 查看绩效考核记录
     */
    List<AssessHistory> getHistory(Integer userid, String assessTime);

    /**
     * 查看上次打分历史
     */
    AssessHistory getScoreHistory(Integer userid, Integer templateid,String assessTime);

    /**
     * 查看用户绩效历史列表
     */
    LinkedHashMap<String, BigDecimal> getHistoryList(Integer userid);

    /**
     * 绩效打分
     */
    BigDecimal competentAuditPage(Integer id, List<AssessHistory> assessHistories);


    /**
     * 总经理考核绩效
     */
    Integer BossAuditPage(Integer id);

    /**
     * 员工确认考核绩效
     */
    Integer ConfirmAuditPage(Integer id);

    /**
     * 删除当月绩效历史
     */
    int delhistory(String month);

    /**
     * 清空绩效数据
     */
    void delAssess();


    Integer getAssesspermission(Integer id);
}
