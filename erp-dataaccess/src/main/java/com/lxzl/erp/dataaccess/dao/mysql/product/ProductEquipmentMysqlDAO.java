package com.lxzl.erp.dataaccess.dao.mysql.product;

import com.lxzl.erp.dataaccess.domain.product.ProductEquipmentDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ProductEquipmentMysqlDAO extends BaseMysqlDAO<ProductEquipmentDO> {

    ProductEquipmentDO findByEquipmentNo(@Param("equipmentNo") String equipmentNo);
    Integer findProductEquipmentCountByParams(@Param("maps") Map<String, Object> paramMap);
    List<ProductEquipmentDO> findProductEquipmentByParams(@Param("maps") Map<String, Object> paramMap);
}
