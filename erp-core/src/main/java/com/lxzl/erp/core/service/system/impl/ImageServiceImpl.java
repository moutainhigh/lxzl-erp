package com.lxzl.erp.core.service.system.impl;

import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.system.pojo.Image;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.common.util.FileUtil;
import com.lxzl.erp.core.service.FileService;
import com.lxzl.erp.core.service.system.ImageService;
import com.lxzl.erp.core.service.system.impl.support.ImageConverter;
import com.lxzl.erp.dataaccess.dao.mysql.system.ImgMysqlMapper;
import com.lxzl.erp.dataaccess.domain.system.ImageDO;
import com.lxzl.se.core.service.impl.BaseServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.InputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service("imageService")
public class ImageServiceImpl extends BaseServiceImpl implements ImageService {

    private static final Logger logger = LoggerFactory.getLogger(ImageServiceImpl.class);

    @Autowired(required = false)
    private HttpSession session;

    @Autowired
    private FileService fileService;

    @Autowired
    private ImgMysqlMapper imgMysqlMapper;

    @Override
    public ServiceResult<String, List<Image>> uploadImage(MultipartFile[] files) {
        Date currentTime = new Date();
        ServiceResult<String, List<Image>> result = new ServiceResult<>();

        User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
        List<ImageDO> imgDOList = new ArrayList<>();

        try {
            for (MultipartFile file : files) {
                String filePath = FileUtil.uploadFile(file);
                Map<String, String> extraInfo = new HashMap<>();
                InputStream inputStream = file.getInputStream();
                byte[] fileBytes = file.getBytes();
                long size = file.getSize();
                String fileId = fileService.uploadFile(filePath, fileBytes, size, CommonConstant.UPLOAD_USER, extraInfo, inputStream);
                ImageDO imgDO = new ImageDO();
                imgDO.setOriginalName(file.getOriginalFilename());
                imgDO.setImgUrl(fileId);
                imgDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                imgDO.setCreateUser(loginUser.getUserId().toString());
                imgDO.setUpdateUser(loginUser.getUserId().toString());
                imgDO.setCreateTime(currentTime);
                imgDO.setUpdateTime(currentTime);
                imgMysqlMapper.save(imgDO);

                imgDOList.add(imgDO);
                FileUtil.deleteFile(filePath);
            }
        } catch (Exception e) {
            logger.error("upload image file error");
            result.setErrorCode(ErrorCode.PRODUCT_IMAGE_UPLOAD_ERROR);
            return result;
        }

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(ImageConverter.convertImageDOList(imgDOList));
        return result;
    }

    @Override
    public ServiceResult<String, Integer> deleteImage(Integer imgId) {
        ServiceResult<String, Integer> result = new ServiceResult<>();
        Date currentTime = new Date();
        User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
        ImageDO imageDO = imgMysqlMapper.findByImgId(imgId);
        if (imageDO == null) {
            result.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return result;
        }

        ImageDO imgDO = new ImageDO();
        imgDO.setId(imgId);
        imgDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        imgDO.setCreateUser(loginUser.getUserId().toString());
        imgDO.setUpdateUser(loginUser.getUserId().toString());
        imgDO.setCreateTime(currentTime);
        imgDO.setUpdateTime(currentTime);
        imgMysqlMapper.update(imgDO);
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(imgDO.getId());
        return result;
    }

    @Override
    public ServiceResult<String, List<Image>> uploadDisposableImage(MultipartFile[] files) {
        ServiceResult<String, List<Image>> result = new ServiceResult<>();
        List<Image> imgList = new ArrayList<>();
        try {
            for (MultipartFile file : files) {
                String filePath = FileUtil.uploadFile(file);
                Map<String, String> extraInfo = new HashMap<>();
                InputStream inputStream = file.getInputStream();
                byte[] fileBytes = file.getBytes();
                long size = file.getSize();
                String fileId = fileService.uploadFile(filePath, fileBytes, size, CommonConstant.UPLOAD_USER, extraInfo, inputStream);
                Image image = new Image();
                image.setOriginalName(file.getOriginalFilename());
                image.setImgUrl(fileId);
                image.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);

                imgList.add(image);
                FileUtil.deleteFile(filePath);
            }
        } catch (Exception e) {
            logger.error("upload image file error");
            result.setErrorCode(ErrorCode.PRODUCT_IMAGE_UPLOAD_ERROR);
            return result;
        }

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(imgList);
        return result;

    }

    public static int getCharacterPosition(String filename, String character, int index) {
        //这里是获取"/"符号的位置
        Matcher slashMatcher = Pattern.compile(character).matcher(filename);
        int mIdx = 0;
        while (slashMatcher.find()) {
            mIdx++;
            //当"/"符号第三次出现的位置
            if (mIdx == index) {
                break;
            }
        }
        return slashMatcher.start();
    }

}
