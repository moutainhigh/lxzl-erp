package com.lxzl.erp.dataaccess.dao.mysql.product;

import com.lxzl.erp.dataaccess.domain.product.ProductDO;
import com.lxzl.erp.dataaccess.domain.product.ProductSkuDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ProductMapper extends BaseMysqlDAO<ProductDO> {
    ProductDO findByProductId(@Param("productId") Integer productId);
    List<ProductDO> findProductByParams(@Param("maps") Map<String, Object> paramMap);
    Integer findProductCountByParams(@Param("maps") Map<String, Object> paramMap);


    List<ProductSkuDO> findSkuRentByCustomerId(@Param("customerId") Integer customerId);
}
