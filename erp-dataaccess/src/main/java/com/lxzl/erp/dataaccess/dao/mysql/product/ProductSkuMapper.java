package com.lxzl.erp.dataaccess.dao.mysql.product;

import com.lxzl.erp.dataaccess.domain.product.ProductSkuDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ProductSkuMapper extends BaseMysqlDAO<ProductSkuDO> {

    /**
     * @param productId 商品ID
     * @return SKU集合（不带物料信息）
     */
    List<ProductSkuDO> findByProductId(@Param("productId") Integer productId);

    /**
     * @param productId 商品ID
     * @return SKU集合（带物料信息）
     */
    List<ProductSkuDO> findDetailByProductId(@Param("productId") Integer productId);

    Integer findProductSkuCountByParams(@Param("maps") Map<String, Object> paramMap);
    List<ProductSkuDO> findProductSkuByParams(@Param("maps") Map<String, Object> paramMap);

    List<ProductSkuDO> findSkuRent (@Param("maps") Map<String, Object> paramMap);
    Integer findSkuRentCount (@Param("maps") Map<String, Object> paramMap);
    //查看id是否存在
    Integer findSkuIdCount(@Param("skuId") Integer skuId);

}
