package com.lxzl.erp.dataaccess.dao.mysql.material;

import com.lxzl.erp.dataaccess.domain.material.BulkMaterialDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface BulkMaterialMapper extends BaseMysqlDAO<BulkMaterialDO> {

    Integer saveList(@Param("bulkMaterialDOList") List<BulkMaterialDO> bulkMaterialDOList);

    Integer updateList(@Param("bulkMaterialDOList") List<BulkMaterialDO> bulkMaterialDOList);

    List<BulkMaterialDO> listPage(@Param("maps") Map<String, Object> paramMap);

    Integer listCount(@Param("maps") Map<String, Object> paramMap);

    BulkMaterialDO findByNo(@Param("bulkMaterialNo") String bulkMaterialNo);
}