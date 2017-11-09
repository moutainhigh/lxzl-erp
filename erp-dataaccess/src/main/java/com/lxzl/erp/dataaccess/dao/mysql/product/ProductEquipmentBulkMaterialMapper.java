package com.lxzl.erp.dataaccess.dao.mysql.product;

import com.lxzl.erp.dataaccess.domain.product.ProductEquipmentBulkMaterialDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

public interface ProductEquipmentBulkMaterialMapper extends BaseMysqlDAO<ProductEquipmentBulkMaterialDO> {

	Integer saveList(@Param("productEquipmentBulkMaterialDOList")List<ProductEquipmentBulkMaterialDO> productEquipmentBulkMaterialDOList);

	List<ProductEquipmentBulkMaterialDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);
}