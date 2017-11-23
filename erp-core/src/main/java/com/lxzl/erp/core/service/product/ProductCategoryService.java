package com.lxzl.erp.core.service.product;

import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.product.ProductCategoryQueryParam;
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


    /**
     * 查询商品所有的类目
     *
     * @param productCategoryQueryParam 查询类目参数
     * @return 类目集合
     */
    ServiceResult<String, List<ProductCategory>> queryAllProductCategory(ProductCategoryQueryParam productCategoryQueryParam);


    /**
     * 根据类目ID，查询类目下所有的属性
     *
     * @param categoryId 类别ID
     * @return 类别集合
     */
    ServiceResult<String, List<ProductCategoryProperty>> queryProductCategoryPropertyListByCategoryId(Integer categoryId);


    /**
     * 查询该商品下该有的类目
     *
     * @param productId 商品ID
     * @return 类别集合
     */
    ServiceResult<String, List<ProductCategoryProperty>> queryPropertiesByProductId(Integer productId);

    /**
     * 添加属性值
     *
     * @param productCategoryPropertyValue 属性值信息
     * @return 唯一标识
     */
    ServiceResult<String, Integer> addProductCategoryPropertyValue(ProductCategoryPropertyValue productCategoryPropertyValue);

    /**
     * @param productCategoryPropertyValue 分类属性值
     * @return 修改ID
     */
    ServiceResult<String, Integer> updateProductCategoryPropertyValue(ProductCategoryPropertyValue productCategoryPropertyValue);
}
