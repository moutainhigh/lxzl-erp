package com.lxzl.erp.dataaccess.dao.mysql.product;

import com.lxzl.erp.dataaccess.domain.product.ProductCategoryPropertyDO;
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

    ProductCategoryPropertyValueDO findByPropertyValueNameAndCategoryId(@Param("propertyValueName") String propertyValueName,
                                                                        @Param("categoryId") Integer categoryId);

    List<ProductCategoryPropertyValueDO> findListByPropertyIdAndCategoryId(@Param("propertyId") Integer propertyId, @Param("categoryId") Integer categoryId);

    Integer findCategoryPropertyValueCountByParams(@Param("maps") Map<String, Object> maps);

    List<ProductCategoryPropertyValueDO> findCategoryPropertyValueByParams(@Param("maps") Map<String, Object> maps);

    ProductCategoryPropertyValueDO findByPropertyValueNameAndCategoryIdAndPropertyId(@Param("propertyValueName") String propertyValueName,
                                                                                     @Param("categoryId") Integer categoryId,
                                                                                     @Param("propertyId") Integer propertyId,
                                                                                     @Param("id") Integer id);
}
