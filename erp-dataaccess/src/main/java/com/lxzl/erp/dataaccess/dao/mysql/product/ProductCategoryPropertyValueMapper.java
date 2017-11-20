package com.lxzl.erp.dataaccess.dao.mysql.product;

import com.lxzl.erp.dataaccess.domain.product.ProductCategoryPropertyValueDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ProductCategoryPropertyValueMapper extends BaseMysqlDAO<ProductCategoryPropertyValueDO> {

    List<ProductCategoryPropertyValueDO> findByParams(@Param("maps") Map<String, Object> paramMap);
}
