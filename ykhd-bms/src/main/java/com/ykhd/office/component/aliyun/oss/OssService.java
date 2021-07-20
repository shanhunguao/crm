package com.ykhd.office.component.aliyun.oss;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.DeleteObjectsRequest;
import com.aliyun.oss.model.DeleteObjectsResult;

/**
 * aliyun OSS操作类
 */
@Component
public class OssService {

	private static Logger log = LoggerFactory.getLogger(OssService.class);
	@Value("${accessKeyId}")
	private String accessKeyId;
	@Value("${accessKeySecret}")
	private String accessKeySecret;
	@Value("${oss_endpoint}")
	private String endPoint;
	@Value("${oss_bucket}")
	private String bucketName;

	private OSSClient ossClient;
	
	private OSSClient getOSSClient() {
		if (ossClient == null)
			ossClient = new OSSClient(endPoint, accessKeyId, accessKeySecret);
		return ossClient;
	}
	
	@PreDestroy
	public void shutdown() {
		if (ossClient != null) {
			ossClient.shutdown();
		}
	}
	
	/**
	 * 上传文件
	 * @param objectPath
	 * @param files
	 * @return
	 */
	public String uploadFile(String objectPath, MultipartFile file) {
		OSSClient ossClient = getOSSClient();
		String fileName = file.getOriginalFilename();
		String fileType = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
		String objectKey = objectPath + "/" + UUID.randomUUID().toString().replace("-", "") + fileType;
		try {
			ossClient.putObject(bucketName, objectKey, file.getInputStream());
			return objectKey;
		} catch (OSSException | ClientException | IOException e) {
			log.error("文件{}上传失败 --> {}", fileName, e.getMessage());
			throw new RuntimeException("文件上传失败");
		}
	}
	
	public String uploadByteArray(String objectPath, byte[] bytes) {
		OSSClient ossClient = getOSSClient();
		String objectKey = objectPath + "/" + UUID.randomUUID().toString().replace("-", "") + ".jpg";
		try {
			ossClient.putObject(bucketName, objectKey, new ByteArrayInputStream(bytes));
		} catch (OSSException | ClientException e) {
			log.error("base64图片转移失败 --> {}", e.getMessage());
		}
		return objectKey;
	}
	
	/**
	 * 批量上传
	 */
	public List<String> uploadFiles(String objectPath, MultipartFile... files) {
		List<String> fileList = new ArrayList<>();
		OSSClient ossClient = getOSSClient();
		String fileName, fileType, objectKey;
		for (MultipartFile file : files) {
			fileName = file.getOriginalFilename();
			fileType = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
			objectKey = objectPath + "/" + UUID.randomUUID().toString().replace("-", "") + fileType;
			try {
				ossClient.putObject(bucketName, objectKey, file.getInputStream());
				fileList.add(objectKey);
			} catch (OSSException | ClientException | IOException e) {
				log.error("文件{}上传失败 --> {}", fileName, e.getMessage());
			}
		}
		Assert.state(fileList.size() > 0, "上传失败");
		return fileList;
	}
	
	/**
	 * 删除资源
	 * @return boolean
	 */
	public boolean deleteFile(String objectKey) {
		return deleteFile(bucketName, objectKey);
	}
	
	/**
	 * 批量删除资源
	 * @return list 成功删除的结果集
	 */
	public List<String> deleteMultipleFile(List<String> objectKeys) {
		return deleteMultipleFile(bucketName, objectKeys);
	}
	
	private boolean deleteFile(String bucketNmae, String objectKey) {
		OSSClient ossClient = getOSSClient();
		try {
			ossClient.deleteObject(bucketNmae, objectKey);
			return true;
		} catch (OSSException | ClientException e) {
			log.error(e.getMessage(), e);
		}
		return false;
	}
	
	private List<String> deleteMultipleFile(String bucketNmae, List<String> objectKeys) {
		OSSClient ossClient = getOSSClient();
		try {
			DeleteObjectsResult deleteObjectsResult = ossClient.deleteObjects(new DeleteObjectsRequest(bucketNmae).withKeys(objectKeys));
			List<String> deletedObjects = deleteObjectsResult.getDeletedObjects();
			return deletedObjects;
		} catch (OSSException | ClientException e) {
			log.error(e.getMessage(), e);
		}
		return Collections.emptyList();
	}
	
//	/**
//	 * 生成带签名的OSS文件临时访问URL
//	 * @param bucketName
//	 * @param objectKey
//	 * @param seconds
//	 * @return
//	 */
//	public static String getAccessURL(String bucketName, String objectKey, int expire_seconds) {
//		OSSClient ossClient = new OSSClient(dto.getEndPoint(), dto.getAccessKeyId(), dto.getAccessKeySecret(), dto.getSecurityToken());
//		GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, objectKey);
//		// 设置过期时间
//		if (expire_seconds <= 0 || expire_seconds > 3600)
//			expire_seconds = 3600;
//		Date expires = new Date (new Date().getTime() + 1000 * expire_seconds);
//		generatePresignedUrlRequest.setExpiration(expires);
//		try {
//			// 生成以GET方法访问的签名URL，访客可以直接通过浏览器访问相关内容。
//			URL url = ossClient.generatePresignedUrl(generatePresignedUrlRequest);
//			return url.toString();
//		} catch (ClientException e) {
//			e.printStackTrace();
//		} finally {
//			ossClient.shutdown();
//		}
//		return null;
//	}
	
}
