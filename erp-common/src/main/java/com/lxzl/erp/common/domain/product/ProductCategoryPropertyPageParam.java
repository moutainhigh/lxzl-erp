package com.lxzl.erp.common.domain.product;

import com.lxzl.erp.common.domain.base.BasePageParam;

/**
 * @Author: your name
 * @Description：
 * @Date: Created in 19:51 2018/8/14
 * @Modified By:
 */
public class ProductCategoryPropertyPageParam extends BasePageParam {

    private Integer categoryId; //分类Id

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }
}
