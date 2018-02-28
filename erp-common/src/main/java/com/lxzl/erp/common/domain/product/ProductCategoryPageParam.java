package com.lxzl.erp.common.domain.product;

import com.lxzl.erp.common.domain.base.BasePageParam;

/**
 * @Author: your name
 * @Description：
 * @Date: Created in 19:04 2018/2/26
 * @Modified By:
 */
public class ProductCategoryPageParam extends BasePageParam {

    private String categoryName;
    private Integer categoryType;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(Integer categoryType) {
        this.categoryType = categoryType;
    }
}
