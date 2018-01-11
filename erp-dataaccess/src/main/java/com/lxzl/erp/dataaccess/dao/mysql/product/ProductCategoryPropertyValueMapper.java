package com.lxzl.erp.dataaccess.dao.mysql.product;

import com.lxzl.erp.dataaccess.domain.product.ProductCategoryPropertyValueDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ProductCategoryPropertyValueMapper extends BaseMysqlDAO<ProductCategoryPropertyValueDO> {

    List<ProductCategoryPropertyValueDO> findByProductAndSkuId(@Param("productId") Integer productId, @Param("skuId") Integer skuId);
    List<ProductCategoryPropertyValueDO> findByMaterialModelId(@Param("materialModelId") Integer materialModelId);
    List<ProductCategoryPropertyValueDO> findByPropertyId(@Param("propertyId") Integer propertyId);
    List<ProductCategoryPropertyValueDO> findByCategoryId(@Param("categoryId") Integer categoryId);
    List<ProductCategoryPropertyValueDO> findByMaterialTypeAndCapacityValue(@Param("materialType") Integer materialType,
                                                                            @Param("capacityValue") Double capacityValue);
}
