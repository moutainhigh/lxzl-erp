package com.lxzl.erp.common.domain.peerDeploymentOrder;

import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.BaseCommitParam;
import com.lxzl.erp.common.domain.validGroup.ExtendGroup;
import org.hibernate.validator.constraints.NotBlank;

public class PeerDeploymentOrderCommitParam extends BaseCommitParam {

    @NotBlank(message = ErrorCode.PEER_DEPLOYMENT_ORDER_NO_NOT_NULL, groups = {ExtendGroup.class})
    private String peerDeploymentOrderNo;

    public String getPeerDeploymentOrderNo() { return peerDeploymentOrderNo; }

    public void setPeerDeploymentOrderNo(String peerDeploymentOrderNo) { this.peerDeploymentOrderNo = peerDeploymentOrderNo; }
}
