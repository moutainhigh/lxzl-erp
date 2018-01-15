package com.lxzl.erp.dataaccess.dao.mysql.peerDeploymentOrder;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.peerDeploymentOrder.PeerDeploymentOrderConsignInfoDO;import org.apache.ibatis.annotations.Param;
import java.util.List;
import org.springframework.stereotype.Repository;
import java.util.Map;

@Repository
public interface PeerDeploymentOrderConsignInfoMapper extends BaseMysqlDAO<PeerDeploymentOrderConsignInfoDO> {

	List<PeerDeploymentOrderConsignInfoDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);
	//todo
	PeerDeploymentOrderConsignInfoDO findByPeerDeploymentOrderConsignInfoId(@Param("peerDeploymentOrderId") Integer peerDeploymentOrderId);
}