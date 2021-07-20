package com.ykhd.office.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ykhd.office.domain.entity.UploadEvent;
import com.ykhd.office.domain.resp.UploadEventDto;
import com.ykhd.office.util.dictionary.TypeEnums.Type4Upload;

public interface IUploadEventService extends IService<UploadEvent> {
	
	/**
	 * 新增上传事件
	 */
	boolean addUploadEvent(Type4Upload type, Integer identity, MultipartFile... files);
	
	/**
	 * 查询上传事件
	 */
	List<UploadEventDto> getUploadEventList(Type4Upload type, Integer identity);
	
	/**
	 * 查询最近几次上传事件
	 */
	List<UploadEventDto> getUploadEventList(Type4Upload type, Integer identity, Integer count);
	
	/**
	 * 删除上传事件
	 */
	boolean deleteUploadEvent(Integer id);

	/**
	 * 删除单个文件
	 */
	boolean deleteSingleFile(Integer id, String filePath);
	
	/**
	 * 补传文件
	 */
	boolean addSingleFile(Integer id, MultipartFile... files);
	
}
