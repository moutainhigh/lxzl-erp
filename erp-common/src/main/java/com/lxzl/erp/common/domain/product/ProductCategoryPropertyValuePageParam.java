package com.lxzl.erp.common.domain.product;

import com.lxzl.erp.common.domain.base.BasePageParam;

/**
 * @Author: your name
 * @Description：
 * @Date: Created in 9:43 2018/8/16
 * @Modified By:
 */
public class ProductCategoryPropertyValuePageParam extends BasePageParam {

    private Integer propertyId; //所属属性ID
    private Integer categoryId; //所属类目ID

    public Integer getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Integer propertyId) {
        this.propertyId = propertyId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }
}
