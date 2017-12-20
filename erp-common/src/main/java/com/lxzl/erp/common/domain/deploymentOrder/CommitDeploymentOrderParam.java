package com.lxzl.erp.common.domain.deploymentOrder;

import java.io.Serializable;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-11-28 15:18
 */
public class CommitDeploymentOrderParam implements Serializable {
    private String deploymentOrderNo;
    private Integer verifyUser;
    private String commitRemark;
    private String backup;

    public String getDeploymentOrderNo() {
        return deploymentOrderNo;
    }

    public void setDeploymentOrderNo(String deploymentOrderNo) {
        this.deploymentOrderNo = deploymentOrderNo;
    }

    public Integer getVerifyUser() {
        return verifyUser;
    }

    public void setVerifyUser(Integer verifyUser) {
        this.verifyUser = verifyUser;
    }

    public String getCommitRemark() {
        return commitRemark;
    }

    public void setCommitRemark(String commitRemark) {
        this.commitRemark = commitRemark;
    }

    public String getBackup() {
        return backup;
    }

    public void setBackup(String backup) {
        this.backup = backup;
    }
}
