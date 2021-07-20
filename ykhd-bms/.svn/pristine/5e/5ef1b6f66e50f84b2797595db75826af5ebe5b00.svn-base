package com.ykhd.office.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ykhd.office.service.IUploadEventService;
import com.ykhd.office.util.dictionary.TypeEnums.Type4Upload;

@RestController
@RequestMapping("/upload")
public class UploadEventController extends BaseController {

	@Autowired
	private IUploadEventService uploadEventService;
	
	/**
	 * 上传公众号数据截图（事件）
	 */
	@PostMapping("/oa/dataImage")
	public Object oaDataImage(Integer oaId, MultipartFile... files) {
		Assert.notNull(oaId, "oaId" + nullValue);
		Assert.notNull(files, "未选择文件");
		Assert.noNullElements(files, "包含空文件");
		return uploadEventService.addUploadEvent(Type4Upload.公众号后台数据截图, oaId, files) ? success(null) : failure(save_failure);
	}
	
	/**
	 * 查询公众号所有数据截图（事件）
	 */
	@Deprecated
	@GetMapping("/oa/dataImage")
	public Object oaDataImage(Integer oaId) {
		Assert.notNull(oaId, "oaId" + nullValue);
		return uploadEventService.getUploadEventList(Type4Upload.公众号后台数据截图, oaId);
	}
	
	/**
	 * 查询公众号最近几次数据截图（事件）
	 */
	@GetMapping("/oa/dataImage/newly")
	public Object oaDataImage(Integer oaId, Integer count) {
		Assert.notNull(oaId, "oaId" + nullValue);
		return uploadEventService.getUploadEventList(Type4Upload.公众号后台数据截图, oaId, count);
	}
	
	/**
	 * 删除事件记录
	 */
	@Deprecated
	@DeleteMapping("/{id}")
	public Object delete(@PathVariable Integer id) {
		return uploadEventService.deleteUploadEvent(id) ? success(null) : failure(delete_failure);
	}
	
	/**
	 * 删除单个文件
	 */
	@DeleteMapping("/single/{id}")
	public Object delete(@PathVariable Integer id, String filePath) {
		Assert.hasText(filePath, "filePath" + nullValue);
		return uploadEventService.deleteSingleFile(id, filePath) ? success(null) : failure(delete_failure);
	}
	
	/**
	 * 补传文件
	 */
	@PostMapping("/single/{id}")
	public Object add(@PathVariable Integer id, MultipartFile... files) {
		Assert.notNull(files, "未选择文件");
		Assert.noNullElements(files, "包含空文件");
		return uploadEventService.addSingleFile(id, files) ? success(null) : failure(delete_failure);
	}
}
