package com.lxzl.erp.common.domain.workflow;

import com.lxzl.erp.common.domain.base.BasePageParam;

import java.io.Serializable;

/**
 * 描述: 查询工作流
 *
 * @author gaochao
 * @date 2017-11-16 10:45
 */
public class WorkflowLinkQueryParam extends BasePageParam implements Serializable {

    private Integer workflowType;
    private String workflowReferNo;
    private Integer verifyStatus;
    private Integer commitUserId;
    private Integer currentVerifyStatus;

    public Integer getWorkflowType() {
        return workflowType;
    }

    public void setWorkflowType(Integer workflowType) {
        this.workflowType = workflowType;
    }

    public String getWorkflowReferNo() {
        return workflowReferNo;
    }

    public void setWorkflowReferNo(String workflowReferNo) {
        this.workflowReferNo = workflowReferNo;
    }

    public Integer getVerifyStatus() {
        return verifyStatus;
    }

    public void setVerifyStatus(Integer verifyStatus) {
        this.verifyStatus = verifyStatus;
    }

    public Integer getCommitUserId() {
        return commitUserId;
    }

    public void setCommitUserId(Integer commitUserId) {
        this.commitUserId = commitUserId;
    }

    public Integer getCurrentVerifyStatus() {
        return currentVerifyStatus;
    }

    public void setCurrentVerifyStatus(Integer currentVerifyStatus) {
        this.currentVerifyStatus = currentVerifyStatus;
    }
}
