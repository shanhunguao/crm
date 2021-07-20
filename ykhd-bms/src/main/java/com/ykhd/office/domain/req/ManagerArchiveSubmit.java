package com.ykhd.office.domain.req;

import org.springframework.web.multipart.MultipartFile;

import com.ykhd.office.domain.entity.Manager;

/**
 * 管理员人事档案提交信息
 */
public class ManagerArchiveSubmit extends Manager {

	private MultipartFile idCardFile;
	private MultipartFile idCard2File;
	private MultipartFile educationFile;
	private MultipartFile departureFile;
	private MultipartFile pictureFile;
	
	public MultipartFile getIdCardFile() {
		return idCardFile;
	}
	public void setIdCardFile(MultipartFile idCardFile) {
		this.idCardFile = idCardFile;
	}
	public MultipartFile getIdCard2File() {
		return idCard2File;
	}
	public void setIdCard2File(MultipartFile idCard2File) {
		this.idCard2File = idCard2File;
	}
	public MultipartFile getEducationFile() {
		return educationFile;
	}
	public void setEducationFile(MultipartFile educationFile) {
		this.educationFile = educationFile;
	}
	public MultipartFile getDepartureFile() {
		return departureFile;
	}
	public void setDepartureFile(MultipartFile departureFile) {
		this.departureFile = departureFile;
	}
	public MultipartFile getPictureFile() {
		return pictureFile;
	}
	public void setPictureFile(MultipartFile pictureFile) {
		this.pictureFile = pictureFile;
	}
}
