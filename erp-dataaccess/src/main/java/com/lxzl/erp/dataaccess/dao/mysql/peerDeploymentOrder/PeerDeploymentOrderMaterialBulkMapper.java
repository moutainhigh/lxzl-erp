package com.lxzl.erp.dataaccess.dao.mysql.peerDeploymentOrder;

import com.lxzl.erp.dataaccess.domain.peerDeploymentOrder.PeerDeploymentOrderMaterialBulkDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface PeerDeploymentOrderMaterialBulkMapper extends BaseMysqlDAO<PeerDeploymentOrderMaterialBulkDO> {

	List<PeerDeploymentOrderMaterialBulkDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);


    Integer findPeerDeploymentOrderMaterialBulkCountByParams(@Param("maps")Map<String, Object> maps);

	List<PeerDeploymentOrderMaterialBulkDO> findPeerDeploymentOrderMaterialBulkByParams(@Param("maps")Map<String, Object> maps);

    List<PeerDeploymentOrderMaterialBulkDO> findByPeerDeploymentOrderId(@Param("peerDeploymentOrderId") Integer peerDeploymentOrderId);

	Integer saveList(List<PeerDeploymentOrderMaterialBulkDO> peerDeploymentOrderMaterialBulkDOList);
}