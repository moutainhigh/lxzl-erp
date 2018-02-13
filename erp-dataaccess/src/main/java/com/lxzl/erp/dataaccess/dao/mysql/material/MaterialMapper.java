package com.lxzl.erp.dataaccess.dao.mysql.material;


import com.lxzl.erp.dataaccess.domain.material.MaterialDO;
import com.lxzl.erp.dataaccess.domain.product.ProductEquipmentDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
@Repository
public interface MaterialMapper extends BaseMysqlDAO<MaterialDO> {
    MaterialDO findByMaterialTypeAndCapacity(@Param("materialType") Integer materialType, @Param("materialCapacityValue") Double materialCapacityValue);

    MaterialDO findByMaterialTypeAndModelId(@Param("materialType") Integer materialType,@Param("materialModelId") Integer materialModelId);

    MaterialDO findByNo(@Param("materialNo") String materialNo);

    List<MaterialDO> listPage(@Param("maps") Map<String, Object> paramMap);

    Integer listCount(@Param("maps") Map<String, Object> paramMap);

    List<MaterialDO> findMaterialRent (@Param("maps") Map<String, Object> paramMap);

    Integer findMaterialRentCount (@Param("maps") Map<String, Object> paramMap);

    List<String> findMaterialByName(@Param("materialName") String materialName);

    List<MaterialDO> findAllMaterial();
}