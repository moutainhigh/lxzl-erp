package com.lxzl.erp.core.service.product;

import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.product.pojo.ProductCategory;
import com.lxzl.erp.common.domain.product.pojo.ProductCategoryProperty;
import com.lxzl.erp.common.domain.product.pojo.ProductCategoryPropertyValue;
import com.lxzl.se.core.service.BaseService;

import java.util.List;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-11-09 19:02
 */
public interface ProductCategoryService extends BaseService {

    // 查询商品所有的类目
    ServiceResult<String, List<ProductCategory>> queryAllProductCategory();

    // 根据类目ID，查询类目下所有的属性
    ServiceResult<String, List<ProductCategoryProperty>> queryProductCategoryPropertyListByCategoryId(Integer categoryId);

    // 查询该商品下该有的类目
    ServiceResult<String, List<ProductCategoryProperty>> queryPropertiesByProductId(Integer productId);

    ServiceResult<String, Integer> addProductCategoryPropertyValue(ProductCategoryPropertyValue productCategoryPropertyValue);
}
