package com.lxzl.erp.core.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.system.pojo.Image;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.common.util.FileUtil;
import com.lxzl.erp.core.service.system.impl.ImageServiceImpl;
import com.lxzl.erp.core.service.system.impl.support.ImageConverter;
import com.lxzl.erp.dataaccess.dao.fastdfs.ImageFastdfsDAO;
import com.lxzl.erp.dataaccess.domain.system.ImageDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lxzl.erp.core.service.FileService;
import com.lxzl.se.common.util.file.FileInfo;
import com.lxzl.se.common.util.validate.ValidateUtil;
import org.springframework.web.multipart.MultipartFile;

@Service("fileService")
public class FileServiceImpl implements FileService {

	@Autowired
	private ImageFastdfsDAO imageFastdfsDAO;

	private static final Logger logger = LoggerFactory.getLogger(ImageServiceImpl.class);

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

	@Override
	public ServiceResult<String, String> uploadFile(MultipartFile file) {
		ServiceResult<String, String> result = new ServiceResult<>();
		String fileId = null;
		try {
				String filePath = FileUtil.uploadFile(file);
				Map<String, String> extraInfo = new HashMap<>();
				InputStream inputStream = file.getInputStream();
				byte[] fileBytes = file.getBytes();
				long size = file.getSize();
				fileId = uploadFile(filePath, fileBytes, size, CommonConstant.UPLOAD_USER, extraInfo, inputStream);
				FileUtil.deleteFile(filePath);
		} catch (Exception e) {
			logger.error("upload file error");
			result.setErrorCode(ErrorCode.EXCEL_UPLOAD_ERROR);
			return result;
		}

		result.setErrorCode(ErrorCode.SUCCESS);
		result.setResult(fileId);
		return result;
	}

}
