package com.lxzl.erp.common.domain.workflow;

import com.lxzl.erp.common.domain.base.BasePageParam;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 描述: 查询工作流
 *
 * @author gaochao
 * @date 2017-11-16 10:45
 */
public class WorkflowLinkQueryParam extends BasePageParam implements Serializable {

    private String workflowLinkNo;
    private Integer workflowType;
    private String workflowReferNo;
    private Integer verifyStatus;
    private String verifyMatters;
    private Integer commitUserId;
    private Integer currentVerifyUser;
    private Date createStartTime;
    private Date createEndTime;
    private Boolean isWorkbench;

    //控制数据权限
    private List<Integer> passiveUserIdList;

    public Boolean getIsWorkbench() {
        return isWorkbench;
    }

    public void setIsWorkbench(Boolean workbench) {
        isWorkbench = workbench;
    }

    public Date getCreateStartTime() {
        return createStartTime;
    }

    public void setCreateStartTime(Date createStartTime) {
        this.createStartTime = createStartTime;
    }

    public Date getCreateEndTime() {
        return createEndTime;
    }

    public void setCreateEndTime(Date createEndTime) {
        this.createEndTime = createEndTime;
    }

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

    public Integer getCurrentVerifyUser() {
        return currentVerifyUser;
    }

    public void setCurrentVerifyUser(Integer currentVerifyUser) {
        this.currentVerifyUser = currentVerifyUser;
    }

    public String getWorkflowLinkNo() {
        return workflowLinkNo;
    }

    public void setWorkflowLinkNo(String workflowLinkNo) {
        this.workflowLinkNo = workflowLinkNo;
    }

    public String getVerifyMatters() {
        return verifyMatters;
    }

    public void setVerifyMatters(String verifyMatters) {
        this.verifyMatters = verifyMatters;
    }

    public List<Integer> getPassiveUserIdList() { return passiveUserIdList; }

    public void setPassiveUserIdList(List<Integer> passiveUserIdList) { this.passiveUserIdList = passiveUserIdList; }
}
