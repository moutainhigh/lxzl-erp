package com.lxzl.erp.dataaccess.dao.mysql.deploymentOrder;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.deploymentOrder.DeploymentOrderMaterialDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
@Repository
public interface DeploymentOrderMaterialMapper extends BaseMysqlDAO<DeploymentOrderMaterialDO> {

    List<DeploymentOrderMaterialDO> listPage(@Param("maps") Map<String, Object> paramMap);

    List<DeploymentOrderMaterialDO> findByDeploymentOrderNo(@Param("deploymentOrderNo") String deploymentOrderNo);

    DeploymentOrderMaterialDO findByDeploymentOrderNoAndMaterialId(@Param("deploymentOrderNo") String deploymentOrderNo,
                                                                   @Param("materialId") Integer materialId);

    Integer listCount(@Param("maps") Map<String, Object> paramMap);

    Integer saveList(@Param("deploymentOrderMaterialDOList") List<DeploymentOrderMaterialDO> deploymentOrderMaterialDOList);
}