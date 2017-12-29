package com.lxzl.erp.dataaccess.dao.mysql.product;

import com.lxzl.erp.dataaccess.domain.product.ProductEquipmentBulkMaterialDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ProductEquipmentBulkMaterialMapper extends BaseMysqlDAO<ProductEquipmentBulkMaterialDO> {

    Integer saveList(List<ProductEquipmentBulkMaterialDO> productEquipmentBulkMaterialDOList);

    List<ProductEquipmentBulkMaterialDO> findByEquipmentId(@Param("equipmentId") Integer equipmentId);

    List<ProductEquipmentBulkMaterialDO> listPage(@Param("maps") Map<String, Object> paramMap);

    Integer listCount(@Param("maps") Map<String, Object> paramMap);

    ProductEquipmentBulkMaterialDO findByBulkMaterialId(@Param("bulkMaterialId") Integer bulkMaterialId);

}