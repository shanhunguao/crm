package com.ykhd.office.service.impl;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ykhd.office.component.aliyun.oss.OssService;
import com.ykhd.office.controller.BaseController;
import com.ykhd.office.domain.entity.UploadEvent;
import com.ykhd.office.domain.resp.UploadEventDto;
import com.ykhd.office.service.IManagerService;
import com.ykhd.office.service.IUploadEventService;
import com.ykhd.office.util.JacksonHelper;
import com.ykhd.office.util.dictionary.SystemEnums.OSSFolder;
import com.ykhd.office.util.dictionary.TypeEnums.Type4Upload;

@Service
public class UploadEventService extends ServiceImpl<BaseMapper<UploadEvent>, UploadEvent> implements IUploadEventService {

	@Autowired
	private OssService ossService;
	@Autowired
	private IManagerService managerService;
	
	@Override
	public boolean addUploadEvent(Type4Upload type, Integer identity, MultipartFile... files) {
		UploadEvent event = new UploadEvent();
		event.setType(type.code());
		event.setIdentity(identity);
		event.setFileList(JacksonHelper.toJsonStr(ossService.uploadFiles(OSSFolder.BMS.name(), files)));
		event.setUploader(BaseController.getManagerInfo().getId());
		event.setCreateTime(new Date());
		return save(event);
	}

	@Override
	public List<UploadEventDto> getUploadEventList(Type4Upload type, Integer identity) {
		List<UploadEvent> all = list(new QueryWrapper<UploadEvent>().eq("type", type.code()).eq("identity", identity).orderByDesc("id"));
		if (!all.isEmpty()) {
			Map<Integer, String> map = managerService.queryName(all.stream().map(UploadEvent::getUploader).collect(Collectors.toList()));
			return all.stream().flatMap(v -> {
				UploadEventDto dto = new UploadEventDto();
				dto.setId(v.getId());
				dto.setFileList(JacksonHelper.toParseObjArray(v.getFileList(), String.class));
				dto.setUploader(v.getUploader());
				dto.setUploaderName(map.get(v.getUploader()));
				dto.setCreateTime(v.getCreateTime());
				return Stream.of(dto);
			}).collect(Collectors.toList());
		}
		return Collections.emptyList();
	}

	@Override
	public List<UploadEventDto> getUploadEventList(Type4Upload type, Integer identity, Integer count) {
		List<UploadEvent> all = list(new QueryWrapper<UploadEvent>().eq("type", type.code()).eq("identity", identity).orderByDesc("id").last("limit " + (count == null ? 1 : count)));
		if (!all.isEmpty()) {
			Map<Integer, String> map = managerService.queryName(all.stream().map(UploadEvent::getUploader).collect(Collectors.toList()));
			return all.stream().flatMap(v -> {
				UploadEventDto dto = new UploadEventDto();
				dto.setId(v.getId());
				dto.setFileList(JacksonHelper.toParseObjArray(v.getFileList(), String.class));
				dto.setUploader(v.getUploader());
				dto.setUploaderName(map.get(v.getUploader()));
				dto.setCreateTime(v.getCreateTime());
				return Stream.of(dto);
			}).collect(Collectors.toList());
		}
		return Collections.emptyList();
	}

	@Override
	public boolean deleteUploadEvent(Integer id) {
		UploadEvent event = getById(id);
		Assert.notNull(event, "未知资源");
		Assert.state(event.getUploader().equals(BaseController.getManagerInfo().getId()), "非上传者本人不能操作");
		List<String> fileList = JacksonHelper.toParseObjArray(event.getFileList(), String.class);
		boolean boo = removeById(id);
		if (boo)
			ossService.deleteMultipleFile(fileList);
		return boo;
	}

	@Override
	public boolean deleteSingleFile(Integer id, String filePath) {
		UploadEvent event = getById(id);
		Assert.notNull(event, "未知资源");
		Assert.state(event.getUploader().equals(BaseController.getManagerInfo().getId()), "非上传者本人不能操作");
		List<String> fileList = JacksonHelper.toParseObjArray(event.getFileList(), String.class);
		fileList.remove(filePath);
		boolean boo;
		if (fileList.size() == 0)
			boo = removeById(event.getId());
		else {
			event.setFileList(JacksonHelper.toJsonStr(fileList));
			boo =  updateById(event);
		}
		if (boo)
			ossService.deleteFile(filePath);
		return boo;
	}

	@Override
	public boolean addSingleFile(Integer id, MultipartFile... files) {
		UploadEvent event = getById(id);
		Assert.notNull(event, "未知资源");
		Assert.state(event.getUploader().equals(BaseController.getManagerInfo().getId()), "非上传者本人不能操作");
		List<String> uploadFiles = ossService.uploadFiles(OSSFolder.BMS.name(), files);
		List<String> fileList = JacksonHelper.toParseObjArray(event.getFileList(), String.class);
		fileList.addAll(uploadFiles);
		event.setFileList(JacksonHelper.toJsonStr(fileList));
		return updateById(event);
	}

}
