package com.lxzl.erp.dataaccess.dao.mysql.product;

import com.lxzl.erp.dataaccess.domain.product.ProductSkuDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ProductSkuMysqlDAO extends BaseMysqlDAO<ProductSkuDO> {

    List<ProductSkuDO> findByProductId(@Param("productId") Integer productId);
    Integer findProductSkuCountByParams(@Param("maps") Map<String, Object> paramMap);
    List<ProductSkuDO> findProductSkuByParams(@Param("maps") Map<String, Object> paramMap);
}
