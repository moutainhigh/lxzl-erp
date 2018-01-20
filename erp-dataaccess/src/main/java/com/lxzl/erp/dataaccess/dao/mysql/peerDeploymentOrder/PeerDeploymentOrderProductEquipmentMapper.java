package com.lxzl.erp.dataaccess.dao.mysql.peerDeploymentOrder;

import com.lxzl.erp.dataaccess.domain.transferOrder.TransferOrderProductEquipmentDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.peerDeploymentOrder.PeerDeploymentOrderProductEquipmentDO;import org.apache.ibatis.annotations.Param;
import java.util.List;
import org.springframework.stereotype.Repository;
import java.util.Map;

@Repository
public interface PeerDeploymentOrderProductEquipmentMapper extends BaseMysqlDAO<PeerDeploymentOrderProductEquipmentDO> {

	List<PeerDeploymentOrderProductEquipmentDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);

	Integer saveList(List<PeerDeploymentOrderProductEquipmentDO> peerDeploymentOrderProductEquipmentDOList);

	Integer findPeerDeploymentOrderProductEquipmentCountByParams(@Param("maps")Map<String, Object> maps);

	List<PeerDeploymentOrderProductEquipmentDO> findPeerDeploymentOrderProductEquipmentByParams(@Param("maps")Map<String, Object> maps);

    List<PeerDeploymentOrderProductEquipmentDO> findByPeerDeploymentOrderProductId(@Param("peerDeploymentOrderId") Integer peerDeploymentOrderId);

    Integer updateBatchReturnTime(@Param("maps")Map<String, Object> maps);
}