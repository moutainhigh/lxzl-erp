package com.lxzl.erp.core.service.product.impl.support;

import com.lxzl.erp.common.domain.product.pojo.ProductEquipment;
import com.lxzl.erp.dataaccess.domain.product.ProductEquipmentDO;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

public class ConvertProductEquipment {
    public static ProductEquipment convertProductEquipmentDO(ProductEquipmentDO productEquipmentDO){
        ProductEquipment productEquipment = new ProductEquipment();
        BeanUtils.copyProperties(productEquipmentDO,productEquipment);
        if(productEquipmentDO.getProductImgDOList() != null){
            productEquipment.setProductImgList(ConvertProductImage.convertProductImgDOList(productEquipmentDO.getProductImgDOList()));
        }
        return productEquipment;
    }

    public static List<ProductEquipment> convertProductEquipmentDOList(List<ProductEquipmentDO> productEquipmentDOList){
        List<ProductEquipment> productEquipmentList = new ArrayList<>();
        if(productEquipmentDOList != null && !productEquipmentDOList.isEmpty()){
            for(ProductEquipmentDO productEquipmentDO : productEquipmentDOList){
                productEquipmentList.add(convertProductEquipmentDO(productEquipmentDO));
            }
        }

        return productEquipmentList;
    }
}
