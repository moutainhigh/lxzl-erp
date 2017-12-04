package com.lxzl.erp.core.service.product.impl.support;

import com.lxzl.erp.common.domain.ConstantConfig;
import com.lxzl.erp.common.domain.product.pojo.ProductImg;
import com.lxzl.erp.dataaccess.domain.product.ProductImgDO;

import java.util.ArrayList;
import java.util.List;

public class ProductImageConverter {
    public static ProductImg convertProductImgDO(ProductImgDO productImgDO) {
        ProductImg productImg = new ProductImg();
        if (productImgDO.getId() != null) {
            productImg.setProductImgId(productImgDO.getId());
        }
        if (productImgDO.getImgType() != null) {
            productImg.setImgType(productImgDO.getImgType());
        }
        if (productImgDO.getOriginalName() != null) {
            productImg.setOriginalName(productImgDO.getOriginalName());
        }
        if (productImgDO.getProductId() != null) {
            productImg.setProductId(productImgDO.getProductId());
        }
        if (productImgDO.getImgUrl() != null) {
            productImg.setImgUrl(productImgDO.getImgUrl());
        }
        if (productImgDO.getIsMain() != null) {
            productImg.setIsMain(productImgDO.getIsMain());
        }
        if (productImgDO.getImgOrder() != null) {
            productImg.setImgOrder(productImgDO.getImgOrder());
        }
        if (productImgDO.getDataStatus() != null) {
            productImg.setDataStatus(productImgDO.getDataStatus());
        }
        if (productImgDO.getRemark() != null) {
            productImg.setRemark(productImgDO.getRemark());
        }
        productImg.setImgDomain(ConstantConfig.imageDomain);

        return productImg;
    }

    public static List<ProductImg> convertProductImgDOList(List<ProductImgDO> productImgDOList) {
        List<ProductImg> productImgList = new ArrayList<>();

        if (productImgDOList != null && !productImgDOList.isEmpty()) {
            for (ProductImgDO productImgDO : productImgDOList) {
                productImgList.add(convertProductImgDO(productImgDO));
            }
        }
        return productImgList;
    }

    public static ProductImgDO convertProductImg(ProductImg productImg) {
        ProductImgDO productImgDO = new ProductImgDO();
        if (productImg.getProductImgId() != null) {
            productImgDO.setId(productImg.getProductImgId());
        }
        if (productImg.getImgType() != null) {
            productImgDO.setImgType(productImg.getImgType());
        }
        if (productImg.getOriginalName() != null) {
            productImgDO.setOriginalName(productImg.getOriginalName());
        }
        if (productImg.getProductId() != null) {
            productImgDO.setProductId(productImg.getProductId());
        }
        if (productImg.getImgUrl() != null) {
            productImgDO.setImgUrl(productImg.getImgUrl());
        }
        if (productImg.getIsMain() != null) {
            productImgDO.setIsMain(productImg.getIsMain());
        }
        if (productImg.getImgOrder() != null) {
            productImgDO.setImgOrder(productImg.getImgOrder());
        }
        if (productImg.getDataStatus() != null) {
            productImgDO.setDataStatus(productImg.getDataStatus());
        }
        if (productImg.getRemark() != null) {
            productImgDO.setRemark(productImg.getRemark());
        }
        return productImgDO;
    }
}
