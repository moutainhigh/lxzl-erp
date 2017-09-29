package com.lxzl.erp.dataaccess.dao.fastdfs.impl;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.lxzl.erp.dataaccess.dao.fastdfs.ImageFastdfsDAO;
//import com.lxzl.se.dataaccess.fastdfs.config.ImageInfo;
import com.lxzl.se.dataaccess.fastdfs.impl.BaseFastdfsDAOImpl;

@Repository("imageFastdfsDAO")
public class ImageFastdfsDAOImpl extends BaseFastdfsDAOImpl implements ImageFastdfsDAO {

/*
	@Override
	public String uploadImage(String filePath, String author, Map<String, String> extraInfo) {
		return this.upload(filePath, author, extraInfo, true);
	}

	@Override
	public ImageInfo downloadImage(String fileId) {
		return this.download(fileId, null, true);
	}
*/

}
