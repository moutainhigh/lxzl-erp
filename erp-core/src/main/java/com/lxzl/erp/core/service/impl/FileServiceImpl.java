package com.lxzl.erp.core.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lxzl.erp.core.service.FileService;
import com.lxzl.erp.dataaccess.dao.fastdfs.FileFastdfsDAO;
import com.lxzl.se.common.util.file.FileInfo;
import com.lxzl.se.common.util.validate.ValidateUtil;

@Service("fileService")
public class FileServiceImpl implements FileService {

	@Autowired
	private FileFastdfsDAO fileFastdfsDAO;

	@Override
	public String uploadFile(String fileName, byte[] fileBytes, long size, String author, Map<String, String> extraInfo, InputStream inputStream)
			throws IOException {
		ValidateUtil.notEmpty(fileName, "fileName不允许为空");
		ValidateUtil.notNull(fileBytes, "fileBytes不允许为空");
		String fileId = fileFastdfsDAO.upload(fileName, fileBytes, size, inputStream, author, extraInfo);
		return fileId;
	}

	@Override
	public FileInfo downloadFile(String fileId) {
		ValidateUtil.isNotBlank(fileId, "fileId不允许为空");
		FileInfo fileInfo = fileFastdfsDAO.download(fileId, null);
		return fileInfo;
	}

}
