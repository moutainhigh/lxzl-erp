package com.lxzl.erp.dataaccess.dao.mysql.product;

import com.lxzl.erp.dataaccess.domain.product.ProductSkuPropertyDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ProductSkuPropertyMysqlDAO extends BaseMysqlDAO<ProductSkuPropertyDO> {
    Integer findSkuIdByParams(@Param("maps") Map<String, Object> paramMap);

    List<ProductSkuPropertyDO> findSkuProperties(@Param("skuId") Integer skuId);

    List<ProductSkuPropertyDO> findProductProperties(@Param("productId") Integer productId);

    // 根据productId与propertyValueId确定数据唯一性
    ProductSkuPropertyDO findByProductIdAndPropertyValue(@Param("productId") Integer productId, @Param("propertyValueId") Integer propertyValueId);
}
