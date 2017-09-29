package com.lxzl.erp.core.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import com.lxzl.erp.dataaccess.dao.fastdfs.ImageFastdfsDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lxzl.erp.core.service.FileService;
import com.lxzl.se.common.util.file.FileInfo;
import com.lxzl.se.common.util.validate.ValidateUtil;

@Service("fileService")
public class FileServiceImpl implements FileService {

	@Autowired
	private ImageFastdfsDAO imageFastdfsDAO;

	@Override
	public String uploadFile(String fileName, byte[] fileBytes, long size, String author, Map<String, String> extraInfo, InputStream inputStream)
			throws IOException {
		ValidateUtil.notEmpty(fileName, "fileName不允许为空");
		ValidateUtil.notNull(fileBytes, "fileBytes不允许为空");
		String fileId = imageFastdfsDAO.upload(fileName, fileBytes, size, inputStream, author, extraInfo);
		return fileId;
	}

	@Override
	public FileInfo downloadFile(String fileId) {
		ValidateUtil.isNotBlank(fileId, "fileId不允许为空");
		FileInfo fileInfo = imageFastdfsDAO.download(fileId, null);
		return fileInfo;
	}

}
