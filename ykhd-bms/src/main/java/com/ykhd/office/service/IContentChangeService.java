package com.ykhd.office.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ykhd.office.domain.entity.ContentChange;
import com.ykhd.office.domain.resp.ContentChangeDto;
import com.ykhd.office.util.dictionary.TypeEnums.Type4Change;

public interface IContentChangeService extends IService<ContentChange> {

	/**
	 * 新增内容变更记录
	 */
	boolean addContentChange(Type4Change type, Integer identity, String changeDesc, Integer... creator);
	
	/**
	 * 查询内容变更记录
	 */
	List<ContentChangeDto> getContentChangeList(Type4Change type, Integer identity);
}
