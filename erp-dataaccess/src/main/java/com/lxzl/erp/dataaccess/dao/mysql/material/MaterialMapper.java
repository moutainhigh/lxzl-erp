package com.lxzl.erp.dataaccess.dao.mysql.material;


import com.lxzl.erp.dataaccess.domain.material.MaterialDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface MaterialMapper extends BaseMysqlDAO<MaterialDO> {
    MaterialDO findByPropertyAndValueId(@Param("propertyId") Integer propertyId,
                                        @Param("propertyValueId") Integer propertyValueId);

    MaterialDO findByNo(@Param("materialNo") String materialNo);

    List<MaterialDO> listPage(@Param("maps") Map<String, Object> paramMap);

    Integer listCount(@Param("maps") Map<String, Object> paramMap);
}