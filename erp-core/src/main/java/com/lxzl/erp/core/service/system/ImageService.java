package com.lxzl.erp.core.service.system;

import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.system.pojo.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageService {

    // 上传图片保存数据库
    ServiceResult<String, List<Image>> uploadImage(MultipartFile[] files);

    // 删除图片
    ServiceResult<String, Integer> deleteImage(Integer imgId);

    // 上传图片不保存数据库，直接返回图片信息
    ServiceResult<String, List<Image>> uploadDisposableImage(MultipartFile[] files);
}
