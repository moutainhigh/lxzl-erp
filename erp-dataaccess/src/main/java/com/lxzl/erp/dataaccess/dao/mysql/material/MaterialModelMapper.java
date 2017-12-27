package com.lxzl.erp.dataaccess.dao.mysql.material;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.material.MaterialModelDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
@Repository
public interface MaterialModelMapper extends BaseMysqlDAO<MaterialModelDO> {

    List<MaterialModelDO> listPage(@Param("maps") Map<String, Object> paramMap);

    Integer listCount(@Param("maps") Map<String, Object> paramMap);

    MaterialModelDO findByTypeAndName(@Param("materialType") Integer materialType,
                                      @Param("modelName") String modelName);
}