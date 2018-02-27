package com.lxzl.erp.common.domain.deploymentOrder;

import com.lxzl.erp.common.domain.BaseCommitParam;

import java.io.Serializable;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-11-28 15:18
 */
public class CommitDeploymentOrderParam extends BaseCommitParam {
    private String deploymentOrderNo;
    private String backup;

    public String getDeploymentOrderNo() {
        return deploymentOrderNo;
    }

    public void setDeploymentOrderNo(String deploymentOrderNo) {
        this.deploymentOrderNo = deploymentOrderNo;
    }

    public String getBackup() {
        return backup;
    }

    public void setBackup(String backup) {
        this.backup = backup;
    }
}
