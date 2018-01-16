package com.lxzl.erp.dataaccess.dao.mysql.peerDeploymentOrder;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.peerDeploymentOrder.PeerDeploymentOrderDO;import org.apache.ibatis.annotations.Param;
import java.util.List;
import org.springframework.stereotype.Repository;
import java.util.Map;

@Repository
public interface PeerDeploymentOrderMapper extends BaseMysqlDAO<PeerDeploymentOrderDO> {

	List<PeerDeploymentOrderDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);
	//todo
	PeerDeploymentOrderDO findByPeerDeploymentOrderNo(@Param("peerDeploymentOrderNo") String peerDeploymentOrderNo);
	//todo
	PeerDeploymentOrderDO findByNo(@Param("peerDeploymentOrderNo") String peerDeploymentOrderNo);




	PeerDeploymentOrderDO findDetailByNo(@Param("peerDeploymentOrderNo")String peerDeploymentOrderNo);

    Integer findPeerDeploymentOrderCountByParams(@Param("maps")Map<String, Object> maps);

	List<PeerDeploymentOrderDO> findPeerDeploymentOrderByParams(@Param("maps")Map<String, Object> maps);
}