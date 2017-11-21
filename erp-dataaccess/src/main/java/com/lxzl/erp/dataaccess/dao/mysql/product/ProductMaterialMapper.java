package com.lxzl.erp.dataaccess.dao.mysql.product;

import com.lxzl.erp.dataaccess.domain.product.ProductMaterialDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ProductMaterialMapper extends BaseMysqlDAO<ProductMaterialDO> {

    List<ProductMaterialDO> listPage(@Param("maps") Map<String, Object> paramMap);

    Integer listCount(@Param("maps") Map<String, Object> paramMap);

    ProductMaterialDO findBySkuAndMaterial(@Param("productSkuId") Integer productSkuId,
                                           @Param("materialId") Integer materialId);

    List<ProductMaterialDO> findBySkuId(@Param("productSkuId") Integer productSkuId);
}