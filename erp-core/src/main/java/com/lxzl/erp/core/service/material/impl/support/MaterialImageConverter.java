package com.lxzl.erp.core.service.material.impl.support;

import com.lxzl.erp.common.domain.ConstantConfig;
import com.lxzl.erp.common.domain.material.pojo.MaterialImg;
import com.lxzl.erp.dataaccess.domain.material.MaterialImgDO;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-11-16 17:59
 */
public class MaterialImageConverter {


    public static MaterialImg convertMaterialImgDO(MaterialImgDO materialImgDO) {
        MaterialImg materialImg = new MaterialImg();
        if (materialImgDO.getId() != null) {
            materialImg.setMaterialImgId(materialImgDO.getId());
        }
        BeanUtils.copyProperties(materialImgDO, materialImg);
        materialImg.setImgDomain(ConstantConfig.imageDomain);

        return materialImg;
    }

    public static MaterialImgDO convertMaterialImg(MaterialImg materialImg) {
        MaterialImgDO materialImgDO = new MaterialImgDO();
        if (materialImg.getMaterialImgId() != null) {
            materialImgDO.setId(materialImg.getMaterialImgId());
        }
        BeanUtils.copyProperties(materialImg, materialImgDO);
        return materialImgDO;
    }
}
