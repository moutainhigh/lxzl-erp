package com.lxzl.erp.core.service.product.impl.support;

import com.lxzl.erp.common.domain.product.ProductEquipment;
import com.lxzl.erp.dataaccess.domain.product.ProductEquipmentDO;

import java.util.ArrayList;
import java.util.List;

public class ConvertProductEquipment {
    public static ProductEquipment convertProductEquipmentDO(ProductEquipmentDO productEquipmentDO){
        ProductEquipment productEquipment = new ProductEquipment();
        if(productEquipmentDO.getId() != null){
            productEquipment.setProductEquipmentId(productEquipmentDO.getId());
        }
        if(productEquipmentDO.getEquipmentNo() != null){
            productEquipment.setEquipmentNo(productEquipmentDO.getEquipmentNo());
        }
        if(productEquipmentDO.getProductName() != null){
            productEquipment.setProductName(productEquipmentDO.getProductName());
        }
        if(productEquipmentDO.getProductId() != null){
            productEquipment.setProductId(productEquipmentDO.getProductId());
        }
        if(productEquipmentDO.getSkuId() != null){
            productEquipment.setSkuId(productEquipmentDO.getSkuId());
        }
        if(productEquipmentDO.getEquipmentStatus() != null){
            productEquipment.setEquipmentStatus(productEquipmentDO.getEquipmentStatus());
        }
        if(productEquipmentDO.getDataStatus() != null){
            productEquipment.setDataStatus(productEquipmentDO.getDataStatus());
        }
        if(productEquipmentDO.getRemark() != null){
            productEquipment.setRemark(productEquipmentDO.getRemark());
        }
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
