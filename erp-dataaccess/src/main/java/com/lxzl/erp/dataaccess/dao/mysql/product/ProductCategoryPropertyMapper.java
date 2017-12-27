package com.lxzl.erp.dataaccess.dao.mysql.product;

import com.lxzl.erp.dataaccess.domain.product.ProductCategoryPropertyDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
@Repository
public interface ProductCategoryPropertyMapper extends BaseMysqlDAO<ProductCategoryPropertyDO> {
    List<ProductCategoryPropertyDO> findProductCategoryPropertyListByCategoryId(@Param("maps") Map<String, Object> paramMap);
    List<ProductCategoryPropertyDO> findProductCategoryPropertyListByProductId(@Param("maps") Map<String, Object> paramMap);
}
