package com.lxzl.erp.dataaccess.dao.mysql.product;

import com.lxzl.erp.dataaccess.domain.product.ProductCategoryDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
@Repository
public interface ProductCategoryMapper extends BaseMysqlDAO<ProductCategoryDO> {
    List<ProductCategoryDO> findAllCategory(@Param("maps") Map<String, Object> paramMap);

    Integer findProductCategoryCountByParams(@Param("maps")Map<String, Object> maps);

    List<ProductCategoryDO> findProductCategoryByParams(@Param("maps")Map<String, Object> maps);

    List<ProductCategoryDO> findByParentCategoryId(@Param("parentCategoryId")Integer parentCategoryId);
}
