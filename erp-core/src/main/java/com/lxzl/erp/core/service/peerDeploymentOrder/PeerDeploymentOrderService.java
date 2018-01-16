package com.lxzl.erp.core.service.peerDeploymentOrder;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.peerDeploymentOrder.PeerDeploymentOrderCommitParam;
import com.lxzl.erp.common.domain.peerDeploymentOrder.PeerDeploymentOrderQueryParam;
import com.lxzl.erp.common.domain.peerDeploymentOrder.pojo.PeerDeploymentOrder;
import com.lxzl.erp.core.service.VerifyReceiver;

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
     * 修改同行调拨单
     * @param peerDeploymentOrder
     * @return
     */
    ServiceResult<String,String> updatePeerDeploymentOrder(PeerDeploymentOrder peerDeploymentOrder);

    /**
     * 提交同行调拨单
     * @param peerDeploymentOrderNo 同行调拨单编号
     * @param verifyUser    审核人员
     * @param commitRemark  提交备注
     * @return
     */
    ServiceResult<String,String> commitPeerDeploymentOrder(PeerDeploymentOrderCommitParam peerDeploymentOrderCommitParam);

    /**
     * 同行调拨单分页显示
     * @param peerDeploymentOrderQueryParam
     * @return
     */
    ServiceResult<String,Page<PeerDeploymentOrder>> page(PeerDeploymentOrderQueryParam peerDeploymentOrderQueryParam);

    /**
     * 同行调拨单详情
     * @param peerDeploymentOrder
     * @return
     */
    ServiceResult<String,PeerDeploymentOrder> detailPeerDeploymentOrder(PeerDeploymentOrder peerDeploymentOrder);
}
