package com.lxzl.erp.core.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lxzl.erp.core.service.ImageService;
import com.lxzl.erp.dataaccess.dao.fastdfs.ImageFastdfsDAO;
import com.lxzl.se.common.util.validate.ValidateUtil;
import com.lxzl.se.core.service.impl.BaseServiceImpl;
//import com.lxzl.se.dataaccess.fastdfs.config.ImageInfo;

@Service("imageService")
public class ImageServiceImpl extends BaseServiceImpl implements ImageService {

	@Autowired
	private ImageFastdfsDAO imageFastdfsDAO;
	
	@Override
	public String uploadImage(String filePath, String author, Map<String, String> extraInfo) {
		ValidateUtil.isNotBlank(filePath, "文件路径不允许为空");
//		return imageFastdfsDAO.uploadImage(filePath, author, extraInfo);
		return null;
	}

//	@Override
//	public ImageInfo downloadImage(String fileId) {
//		ValidateUtil.isNotBlank(fileId, "文件id不允许为空");
////		return imageFastdfsDAO.downloadImage(fileId);
//	}

}
