package com.lxzl.erp.dataaccess.domain.workflow;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import org.springframework.data.annotation.Transient;

import java.util.Date;
import java.util.List;


public class WorkflowLinkDO extends BaseDO {

    private Integer id;
    private String workflowLinkNo;
    private Integer workflowType;
    private Integer workflowTemplateId;
    private String workflowReferNo;
    private Integer workflowStep;
    private Integer workflowLastStep;
    private Integer workflowCurrentNodeId;
    private Integer commitUser;
    private Integer currentVerifyUser;
    private Integer currentVerifyStatus;
    private Integer dataStatus;
    private String remark;
    private List<WorkflowLinkDetailDO> workflowLinkDetailDOList;

    @Transient
    private String currentVerifyUserName;
    @Transient
    private String workflowCurrentNodeName;
    @Transient
    private String commitUserName;
    @Transient
    private Boolean isLastStep;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWorkflowLinkNo() {
        return workflowLinkNo;
    }

    public void setWorkflowLinkNo(String workflowLinkNo) {
        this.workflowLinkNo = workflowLinkNo;
    }

    public Integer getWorkflowType() {
        return workflowType;
    }

    public void setWorkflowType(Integer workflowType) {
        this.workflowType = workflowType;
    }

    public Integer getWorkflowTemplateId() {
        return workflowTemplateId;
    }

    public void setWorkflowTemplateId(Integer workflowTemplateId) {
        this.workflowTemplateId = workflowTemplateId;
    }

    public String getWorkflowReferNo() {
        return workflowReferNo;
    }

    public void setWorkflowReferNo(String workflowReferNo) {
        this.workflowReferNo = workflowReferNo;
    }

    public Integer getWorkflowStep() {
        return workflowStep;
    }

    public void setWorkflowStep(Integer workflowStep) {
        this.workflowStep = workflowStep;
    }

    public Integer getWorkflowLastStep() {
        return workflowLastStep;
    }

    public void setWorkflowLastStep(Integer workflowLastStep) {
        this.workflowLastStep = workflowLastStep;
    }

    public Integer getWorkflowCurrentNodeId() {
        return workflowCurrentNodeId;
    }

    public void setWorkflowCurrentNodeId(Integer workflowCurrentNodeId) {
        this.workflowCurrentNodeId = workflowCurrentNodeId;
    }

    public Integer getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(Integer dataStatus) {
        this.dataStatus = dataStatus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<WorkflowLinkDetailDO> getWorkflowLinkDetailDOList() {
        return workflowLinkDetailDOList;
    }

    public void setWorkflowLinkDetailDOList(List<WorkflowLinkDetailDO> workflowLinkDetailDOList) {
        this.workflowLinkDetailDOList = workflowLinkDetailDOList;
    }

    public Integer getCurrentVerifyUser() {
        return currentVerifyUser;
    }

    public void setCurrentVerifyUser(Integer currentVerifyUser) {
        this.currentVerifyUser = currentVerifyUser;
    }

    public Integer getCurrentVerifyStatus() {
        return currentVerifyStatus;
    }

    public void setCurrentVerifyStatus(Integer currentVerifyStatus) {
        this.currentVerifyStatus = currentVerifyStatus;
    }

    public String getCurrentVerifyUserName() {
        return currentVerifyUserName;
    }

    public void setCurrentVerifyUserName(String currentVerifyUserName) {
        this.currentVerifyUserName = currentVerifyUserName;
    }

    public Integer getCommitUser() {
        return commitUser;
    }

    public void setCommitUser(Integer commitUser) {
        this.commitUser = commitUser;
    }

    public String getWorkflowCurrentNodeName() {
        return workflowCurrentNodeName;
    }

    public void setWorkflowCurrentNodeName(String workflowCurrentNodeName) {
        this.workflowCurrentNodeName = workflowCurrentNodeName;
    }

    public String getCommitUserName() {
        return commitUserName;
    }

    public void setCommitUserName(String commitUserName) {
        this.commitUserName = commitUserName;
    }

    public Boolean getLastStep() {
        return isLastStep;
    }

    public void setLastStep(Boolean lastStep) {
        isLastStep = lastStep;
    }
}