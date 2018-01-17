package com.lxzl.erp.common.domain.peerDeploymentOrder;

import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.base.BasePageParam;
import com.lxzl.erp.common.domain.validGroup.IdGroup;

import javax.validation.constraints.NotNull;

/**
 * @Author: your name
 * @Description：
 * @Date: Created in 20:05 2018/1/16
 * @Modified By:
 */
public class PeerDeploymentOrderMaterialBulkQueryGroup extends BasePageParam {
    @NotNull(message = ErrorCode.PEER_DEPLOYMENT_ORDER_ID_NOT_NULL, groups = {IdGroup.class})
    private Integer peerDeploymentOrderMaterialId;

    public Integer getPeerDeploymentOrderMaterialId() {
        return peerDeploymentOrderMaterialId;
    }

    public void setPeerDeploymentOrderMaterialId(Integer peerDeploymentOrderMaterialId) {
        this.peerDeploymentOrderMaterialId = peerDeploymentOrderMaterialId;
    }
}
