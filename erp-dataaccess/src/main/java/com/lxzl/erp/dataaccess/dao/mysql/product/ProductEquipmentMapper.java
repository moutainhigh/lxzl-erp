package com.lxzl.erp.dataaccess.dao.mysql.product;

import com.lxzl.erp.dataaccess.domain.product.ProductEquipmentDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ProductEquipmentMapper extends BaseMysqlDAO<ProductEquipmentDO> {

    Integer saveList(@Param("productEquipmentDOList")List<ProductEquipmentDO> productEquipmentDOList);

    Integer updateList(@Param("productEquipmentDOList")List<ProductEquipmentDO> productEquipmentDOList);

    ProductEquipmentDO findByEquipmentNo(@Param("equipmentNo") String equipmentNo);
    Integer listCount(@Param("maps") Map<String, Object> paramMap);
    List<ProductEquipmentDO> listPage(@Param("maps") Map<String, Object> paramMap);

    List<ProductEquipmentDO> findByCustomerId(@Param("customerId") Integer customerId);
}
