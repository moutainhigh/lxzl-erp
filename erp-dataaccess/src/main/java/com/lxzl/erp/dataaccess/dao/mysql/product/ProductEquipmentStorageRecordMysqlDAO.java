package com.lxzl.erp.dataaccess.dao.mysql.product;

import com.lxzl.erp.dataaccess.domain.product.ProductEquipmentStorageRecordDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;

public interface ProductEquipmentStorageRecordMysqlDAO extends BaseMysqlDAO<ProductEquipmentStorageRecordDO> {

    ProductEquipmentStorageRecordDO findLastRecord(@Param("equipmentNo") String equipmentNo);
}
