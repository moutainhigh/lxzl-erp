package com.lxzl.erp.core.service.peerDeploymentOrder;

import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.peerDeploymentOrder.pojo.PeerDeploymentOrder;
import com.lxzl.erp.common.domain.peerDeploymentOrder.pojo.PeerDeploymentOrderMaterial;
import com.lxzl.erp.core.service.VerifyReceiver;

import java.util.List;

/**
 * @Author: kai
 * @Description：
 * @Date: Created in 15:39 2018/1/13
 * @Modified By:
 */
public interface PeerDeploymentOrderService extends VerifyReceiver {

    /**
     * 创建同行调拨单
     * @param peerDeploymentOrder
     * @return
     */
    ServiceResult<String,String> createPeerDeploymentOrder(PeerDeploymentOrder peerDeploymentOrder);

    /**
     * 同行调拨单详情
     * @param peerDeploymentOrderNo
     * @return
     */
    ServiceResult<String,PeerDeploymentOrder> detailPeerDeploymentOrderNo(String peerDeploymentOrderNo);

}
