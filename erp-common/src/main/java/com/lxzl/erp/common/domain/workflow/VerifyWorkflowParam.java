package com.lxzl.erp.common.domain.workflow;

import java.io.Serializable;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-11-07 19:26
 */
public class VerifyWorkflowParam implements Serializable{
    private Integer workflowLinkId;
    private Integer verifyStatus;
    private String verifyOpinion;
    private Integer nextVerifyUser;

    public Integer getWorkflowLinkId() {
        return workflowLinkId;
    }

    public void setWorkflowLinkId(Integer workflowLinkId) {
        this.workflowLinkId = workflowLinkId;
    }

    public Integer getVerifyStatus() {
        return verifyStatus;
    }

    public void setVerifyStatus(Integer verifyStatus) {
        this.verifyStatus = verifyStatus;
    }

    public String getVerifyOpinion() {
        return verifyOpinion;
    }

    public void setVerifyOpinion(String verifyOpinion) {
        this.verifyOpinion = verifyOpinion;
    }

    public Integer getNextVerifyUser() {
        return nextVerifyUser;
    }

    public void setNextVerifyUser(Integer nextVerifyUser) {
        this.nextVerifyUser = nextVerifyUser;
    }
}
