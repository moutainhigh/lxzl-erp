package com.lxzl.erp.core.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import com.lxzl.se.common.util.file.FileInfo;

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
}
