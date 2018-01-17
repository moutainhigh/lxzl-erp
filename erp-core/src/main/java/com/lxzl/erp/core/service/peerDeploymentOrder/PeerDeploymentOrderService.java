package com.lxzl.erp.core.service.peerDeploymentOrder;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.peerDeploymentOrder.PeerDeploymentOrderCommitParam;
import com.lxzl.erp.common.domain.peerDeploymentOrder.PeerDeploymentOrderMaterialBulkQueryGroup;
import com.lxzl.erp.common.domain.peerDeploymentOrder.PeerDeploymentOrderProductEquipmentQueryGroup;
import com.lxzl.erp.common.domain.peerDeploymentOrder.PeerDeploymentOrderQueryParam;
import com.lxzl.erp.common.domain.peerDeploymentOrder.pojo.PeerDeploymentOrder;
import com.lxzl.erp.common.domain.peerDeploymentOrder.pojo.PeerDeploymentOrderMaterialBulk;
import com.lxzl.erp.common.domain.peerDeploymentOrder.pojo.PeerDeploymentOrderProductEquipment;
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
     * @param peerDeploymentOrderCommitParam
     * @return
     */
    ServiceResult<String,String> commitPeerDeploymentOrderInto(PeerDeploymentOrderCommitParam peerDeploymentOrderCommitParam);

    /**
     * 同行调拨单确认收货
     * @param peerDeploymentOrder
     * @return
     */
    ServiceResult<String,String> confirmPeerDeploymentOrderInto(String peerDeploymentOrder);

    /**
     * 取消同行调拨单
     * @param peerDeploymentOrder
     * @return
     */
    ServiceResult<String,String> cancelPeerDeploymentOrder(PeerDeploymentOrder peerDeploymentOrder);

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

    /**
     * 同行调拨单确认退回
     * @param peerDeploymentOrder
     * @return
     */
    ServiceResult<String,String> endPeerDeploymentOrderOut(PeerDeploymentOrder peerDeploymentOrder);

    /**
     * 同行调拨单商品详情列表
     *
     * @param peerDeploymentOrderProductEquipmentQueryGroup
     * @return
     */
    ServiceResult<String,Page<PeerDeploymentOrderProductEquipment>> detailPeerDeploymentOrderProductEquipment(PeerDeploymentOrderProductEquipmentQueryGroup peerDeploymentOrderProductEquipmentQueryGroup);

    /**
     * 同行调拨单散料详情列表
     *
     * @param peerDeploymentOrderMaterialBulkQueryGroup
     * @return
     */
    ServiceResult<String,Page<PeerDeploymentOrderMaterialBulk>> detailPeerDeploymentOrderMaterialBulk(PeerDeploymentOrderMaterialBulkQueryGroup peerDeploymentOrderMaterialBulkQueryGroup);


}
