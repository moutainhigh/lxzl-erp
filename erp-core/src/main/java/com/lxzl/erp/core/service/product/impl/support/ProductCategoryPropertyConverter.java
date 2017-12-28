package com.lxzl.erp.core.service.product.impl.support;

import com.lxzl.erp.common.domain.product.pojo.ProductCategoryProperty;
import com.lxzl.erp.common.domain.product.pojo.ProductCategoryPropertyValue;
import com.lxzl.erp.dataaccess.domain.product.ProductCategoryPropertyDO;
import com.lxzl.erp.dataaccess.domain.product.ProductCategoryPropertyValueDO;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

public class ProductCategoryPropertyConverter {

    public static ProductCategoryProperty convertProductCategoryPropertyDO(ProductCategoryPropertyDO productCategoryPropertyDO) {
        ProductCategoryProperty productCategoryProperty = new ProductCategoryProperty();
        if (productCategoryPropertyDO.getId() != null) {
            productCategoryProperty.setCategoryPropertyId(productCategoryPropertyDO.getId());
        }
        BeanUtils.copyProperties(productCategoryPropertyDO,productCategoryProperty);
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
        BeanUtils.copyProperties(productCategoryPropertyValueDO,productCategoryPropertyValue);
        return productCategoryPropertyValue;
    }

    public static ProductCategoryPropertyValueDO convertProductCategoryPropertyValue(ProductCategoryPropertyValue productCategoryPropertyValue) {
        ProductCategoryPropertyValueDO productCategoryPropertyValueDO = new ProductCategoryPropertyValueDO();
        if(productCategoryPropertyValue.getCategoryPropertyValueId() != null){
            productCategoryPropertyValueDO.setId(productCategoryPropertyValue.getCategoryPropertyValueId());
        }
        BeanUtils.copyProperties(productCategoryPropertyValue, productCategoryPropertyValueDO);
        return productCategoryPropertyValueDO;
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
