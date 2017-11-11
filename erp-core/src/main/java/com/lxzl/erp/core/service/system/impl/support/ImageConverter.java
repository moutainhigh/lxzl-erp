package com.lxzl.erp.core.service.system.impl.support;

import com.lxzl.erp.common.domain.ConstantConfig;
import com.lxzl.erp.common.domain.system.pojo.Image;
import com.lxzl.erp.dataaccess.domain.system.ImageDO;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-11-10 9:20
 */
public class ImageConverter {

    public static Image convertImageDO(ImageDO imageDO){
        Image image = new Image();
        if(imageDO.getId() != null){
            image.setImgId(imageDO.getId());
        }
        if(imageDO.getImgType() != null){
            image.setImgType(imageDO.getImgType());
        }
        if(imageDO.getOriginalName() != null){
            image.setOriginalName(imageDO.getOriginalName());
        }
        if(imageDO.getRefId() != null){
            image.setRefId(imageDO.getRefId());
        }
        if(imageDO.getImgUrl() != null){
            image.setImgUrl(imageDO.getImgUrl());
        }
        if(imageDO.getImgOrder() != null){
            image.setImgOrder(imageDO.getImgOrder());
        }
        if(imageDO.getDataStatus() != null){
            image.setDataStatus(imageDO.getDataStatus());
        }
        if(imageDO.getRemark() != null){
            image.setRemark(imageDO.getRemark());
        }
        image.setImgDomain(ConstantConfig.imageDomain);
        return image;
    }


    public static List<Image> convertImageDOList(List<ImageDO> imageDOList){
        List<Image> imageList = new ArrayList<>();
        if(imageDOList != null && !imageDOList.isEmpty()){
            for(ImageDO imageDO : imageDOList){
                imageList.add(convertImageDO(imageDO));
            }
        }
        return imageList;
    }

}
