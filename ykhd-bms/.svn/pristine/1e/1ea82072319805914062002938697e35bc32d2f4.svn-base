package com.ykhd.office.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ykhd.office.domain.bean.common.PageHelper;
import com.ykhd.office.domain.entity.EditRecord;
import com.ykhd.office.domain.req.OAScheduleEdit;
import com.ykhd.office.domain.resp.EditRecordCondition;
import com.ykhd.office.domain.resp.OAScheduleEditDto;
import com.ykhd.office.util.dictionary.TypeEnums.Type4Edit;

public interface IEditRecordService extends IService<EditRecord> {

	/**
	 * 删除修改记录
	 */
	int deleteEditRecord(Integer... id);
	
	/**
	 * 新增公众号排期修改记录
	 */
	boolean addOAScheduleEditRecord(OAScheduleEdit edit);
	
	/**
	 * 查看申请修改的公众号排期
	 */
	PageHelper<OAScheduleEditDto> oAScheduleEditList(EditRecordCondition condition);
	
	/**
	 * 审核修改的公众号排期
	 * @param result 0通过 1不通过
	 */
	int reviewOAScheduleEditRecord(int result, String reason, Integer... id);
	
	/**
	 * API: 是否存在进行中的修改流程
	 */
	boolean existsOngoing(Type4Edit type, Integer identity);
	
	/**
	 * API: 是否存在进行中的修改流程
	 */
	boolean existsOngoing(Type4Edit type, List<Integer> identitys);
	
}
