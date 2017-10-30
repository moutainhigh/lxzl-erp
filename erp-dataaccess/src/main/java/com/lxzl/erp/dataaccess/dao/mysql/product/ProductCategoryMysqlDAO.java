package com.lxzl.erp.dataaccess.dao.mysql.product;

import com.lxzl.erp.dataaccess.domain.product.ProductCategoryDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ProductCategoryMysqlDAO extends BaseMysqlDAO<ProductCategoryDO> {
    List<ProductCategoryDO> findAllCategory(@Param("maps") Map<String, Object> paramMap);
}
