package com.ykhd.office.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ykhd.office.domain.entity.Note;
import com.ykhd.office.domain.resp.NoteDto;

public interface INoteService extends IService<Note> {

	/**
	 * 批注历史
	 * @param manager 被批注人
	 */
	List<NoteDto> getList(Integer manager);
	
	/**
	 * 绩效考核月内的批注
	 * @param yyyyMM年-月
	 */
	List<NoteDto> getList4Examine(Integer manager, String yyyyMM);
	
	/**
	 * 添加批注
	 * @param manager 被批注人
	 */
	boolean add(Integer manager, String text);
}
