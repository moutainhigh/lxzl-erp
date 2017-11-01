package com.lxzl.erp.core.service.product.impl.support;

import com.lxzl.erp.common.domain.product.ProductCategoryProperty;
import com.lxzl.erp.common.domain.product.ProductCategoryPropertyValue;
import com.lxzl.erp.dataaccess.domain.product.ProductCategoryPropertyDO;
import com.lxzl.erp.dataaccess.domain.product.ProductCategoryPropertyValueDO;

import java.util.ArrayList;
import java.util.List;

public class ConvertProductCategoryProperty {

    public static ProductCategoryProperty convertProductCategoryPropertyDO(ProductCategoryPropertyDO productCategoryPropertyDO) {
        ProductCategoryProperty productCategoryProperty = new ProductCategoryProperty();
        if (productCategoryPropertyDO.getId() != null) {
            productCategoryProperty.setCategoryPropertyId(productCategoryPropertyDO.getId());
        }
        if (productCategoryPropertyDO.getPropertyName() != null) {
            productCategoryProperty.setPropertyName(productCategoryPropertyDO.getPropertyName());
        }
        if (productCategoryPropertyDO.getCategoryId() != null) {
            productCategoryProperty.setCategoryId(productCategoryPropertyDO.getCategoryId());
        }
        if (productCategoryPropertyDO.getPropertyType() != null) {
            productCategoryProperty.setPropertyType(productCategoryPropertyDO.getPropertyType());
        }
        if (productCategoryPropertyDO.getIsInput() != null) {
            productCategoryProperty.setIsInput(productCategoryPropertyDO.getIsInput());
        }
        if (productCategoryPropertyDO.getIsCheckbox() != null) {
            productCategoryProperty.setIsCheckbox(productCategoryPropertyDO.getIsCheckbox());
        }
        if (productCategoryPropertyDO.getIsRequired() != null) {
            productCategoryProperty.setIsRequired(productCategoryPropertyDO.getIsRequired());
        }
        if (productCategoryPropertyDO.getDataOrder() != null) {
            productCategoryProperty.setDataOrder(productCategoryPropertyDO.getDataOrder());
        }
        if (productCategoryPropertyDO.getDataStatus() != null) {
            productCategoryProperty.setDataStatus(productCategoryPropertyDO.getDataStatus());
        }
        if (productCategoryPropertyDO.getRemark() != null) {
            productCategoryProperty.setRemark(productCategoryPropertyDO.getRemark());
        }
        if (productCategoryPropertyDO.getProductCategoryPropertyValueDOList() != null && productCategoryPropertyDO.getProductCategoryPropertyValueDOList().size() > 0) {
            productCategoryProperty.setProductCategoryPropertyValueList(convertProductCategoryPropertyValueDOList(productCategoryPropertyDO.getProductCategoryPropertyValueDOList()));
        }
        return productCategoryProperty;
    }

    public static List<ProductCategoryProperty> convertProductCategoryPropertyDOList(List<ProductCategoryPropertyDO> productCategoryPropertyDOList) {
        List<ProductCategoryProperty> productCategoryPropertyList = new ArrayList<>();
        if (productCategoryPropertyDOList != null && productCategoryPropertyDOList.size() > 0) {
            for (ProductCategoryPropertyDO productCategoryPropertyDO : productCategoryPropertyDOList) {
                productCategoryPropertyList.add(convertProductCategoryPropertyDO(productCategoryPropertyDO));
            }
        }
        return productCategoryPropertyList;
    }


    public static ProductCategoryPropertyValue convertProductCategoryPropertyValueDO(ProductCategoryPropertyValueDO productCategoryPropertyValueDO) {
        ProductCategoryPropertyValue productCategoryPropertyValue = new ProductCategoryPropertyValue();
        if(productCategoryPropertyValueDO.getId() != null){
            productCategoryPropertyValue.setCategoryPropertyValueId(productCategoryPropertyValueDO.getId());
        }
        if(productCategoryPropertyValueDO.getPropertyValueName() != null){
            productCategoryPropertyValue.setPropertyValueName(productCategoryPropertyValueDO.getPropertyValueName());
        }
        if(productCategoryPropertyValueDO.getPropertyId() != null){
            productCategoryPropertyValue.setPropertyId(productCategoryPropertyValueDO.getPropertyId());
        }
        if(productCategoryPropertyValueDO.getCategoryId() != null){
            productCategoryPropertyValue.setCategoryId(productCategoryPropertyValueDO.getCategoryId());
        }
        if(productCategoryPropertyValueDO.getDataOrder() != null){
            productCategoryPropertyValue.setDataOrder(productCategoryPropertyValueDO.getDataOrder());
        }
        if(productCategoryPropertyValueDO.getDataStatus() != null){
            productCategoryPropertyValue.setDataStatus(productCategoryPropertyValueDO.getDataStatus());
        }
        if(productCategoryPropertyValueDO.getRemark() != null){
            productCategoryPropertyValue.setRemark(productCategoryPropertyValueDO.getRemark());
        }
        return productCategoryPropertyValue;
    }


    public static List<ProductCategoryPropertyValue> convertProductCategoryPropertyValueDOList(List<ProductCategoryPropertyValueDO> productCategoryPropertyValueDOList) {
        List<ProductCategoryPropertyValue> productCategoryPropertyValueList = new ArrayList<>();
        if (productCategoryPropertyValueDOList != null && productCategoryPropertyValueDOList.size() > 0) {
            for(ProductCategoryPropertyValueDO productCategoryPropertyValueDO : productCategoryPropertyValueDOList){
                productCategoryPropertyValueList.add(convertProductCategoryPropertyValueDO(productCategoryPropertyValueDO));
            }
        }
        return productCategoryPropertyValueList;
    }
}
