package com.lxzl.erp.dataaccess.dao.mysql.product;

import com.lxzl.erp.dataaccess.domain.product.ProductImgDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductImgMysqlDAO extends BaseMysqlDAO<ProductImgDO> {
    List<ProductImgDO> findByProductId(@Param("productId") Integer productId, @Param("imgType") Integer imgType);
}
