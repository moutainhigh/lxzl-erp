package com.lxzl.erp.common.domain.workflow;

import java.io.Serializable;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-11-07 19:26
 */
public class VerifyWorkflowParam implements Serializable {
    /**
     * 工作流ID
     */
    private Integer workflowLinkId;
    /**
     * 审核状态
     */
    private Integer verifyStatus;
    /**
     * 回退类型，0直接退回根部，1退回上一层,只针对拒绝的时候有效
     */
    private Integer returnType;
    /**
     * 审核意见
     */
    private String verifyOpinion;
    /**
     * 下一个审核人，可空
     */
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

    public Integer getReturnType() {
        return returnType;
    }

    public void setReturnType(Integer returnType) {
        this.returnType = returnType;
    }
}
