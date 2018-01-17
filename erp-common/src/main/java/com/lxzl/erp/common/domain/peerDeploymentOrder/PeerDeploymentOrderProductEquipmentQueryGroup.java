package com.lxzl.erp.common.domain.peerDeploymentOrder;

import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.base.BasePageParam;
import com.lxzl.erp.common.domain.validGroup.IdGroup;

import javax.validation.constraints.NotNull;

/**
 * @Author: your name
 * @Description：
 * @Date: Created in 18:08 2018/1/16
 * @Modified By:
 */
public class PeerDeploymentOrderProductEquipmentQueryGroup extends BasePageParam {
    @NotNull(message = ErrorCode.PEER_DEPLOYMENT_ORDER_PRODUCT_ID_NOT_NULL, groups = {IdGroup.class})
    private Integer peerDeploymentOrderProductId;   //货物调拨商品项ID

    public Integer getPeerDeploymentOrderProductId() {
        return peerDeploymentOrderProductId;
    }

    public void setPeerDeploymentOrderProductId(Integer peerDeploymentOrderProductId) {
        this.peerDeploymentOrderProductId = peerDeploymentOrderProductId;
    }
}
