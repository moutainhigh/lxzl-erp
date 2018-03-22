package com.lxzl.erp.core.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.system.pojo.Image;
import com.lxzl.se.common.util.file.FileInfo;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

	/**
	 * 上传文件
	 * 
	 * @param fileName
	 * @param fileBytes
	 * @param size
	 * @param author
	 * @param extraInfo
	 * @param inputStream
	 * @return
	 */
	String uploadFile(String fileName, byte[] fileBytes, long size, String author, Map<String, String> extraInfo, InputStream inputStream) throws IOException;

	/**
	 * 下载文件
	 * 
	 * @param <T>
	 * 
	 * @param fileId
	 * @return
	 */
	FileInfo downloadFile(String fileId);

	// 上传文件保存数据库
	ServiceResult<String, String> uploadFile(MultipartFile file);
}
