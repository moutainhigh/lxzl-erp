package com.lxzl.erp.core.service.product.impl.support;

import com.lxzl.erp.common.domain.product.pojo.ProductEquipment;
import com.lxzl.erp.core.service.material.impl.support.MaterialConverter;
import com.lxzl.erp.dataaccess.domain.product.ProductEquipmentDO;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

public class ProductEquipmentConverter {
    public static ProductEquipment convertProductEquipmentDO(ProductEquipmentDO productEquipmentDO) {
        ProductEquipment productEquipment = new ProductEquipment();
        BeanUtils.copyProperties(productEquipmentDO, productEquipment);
        if (productEquipmentDO.getProductImgDOList() != null) {
            productEquipment.setProductImgList(ProductImageConverter.convertProductImgDOList(productEquipmentDO.getProductImgDOList()));
        }
        if (productEquipmentDO.getBulkMaterialDOList() != null) {
            productEquipment.setBulkMaterialList(MaterialConverter.convertProductBulkMaterialDOList(productEquipmentDO.getBulkMaterialDOList()));
        }
        return productEquipment;
    }

    public static List<ProductEquipment> convertProductEquipmentDOList(List<ProductEquipmentDO> productEquipmentDOList) {
        List<ProductEquipment> productEquipmentList = new ArrayList<>();
        if (productEquipmentDOList != null && !productEquipmentDOList.isEmpty()) {
            for (ProductEquipmentDO productEquipmentDO : productEquipmentDOList) {
                productEquipmentList.add(convertProductEquipmentDO(productEquipmentDO));
            }
        }

        return productEquipmentList;
    }

}
