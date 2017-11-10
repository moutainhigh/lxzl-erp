package com.lxzl.erp.dataaccess.dao.mysql.material;


import com.lxzl.erp.dataaccess.domain.material.MaterialDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;

public interface MaterialMapper extends BaseMysqlDAO<MaterialDO>{
    MaterialDO findByPropertyAndValueId(@Param("propertyId") Integer propertyId,
                                        @Param("propertyValueId") Integer propertyValueId);
}