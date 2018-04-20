package com.lxzl.erp.common.domain.dingding.approve;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 钉钉审批数据传输对象
 *
 * @author daiqi
 * @create 2018-04-20 10:58
 */
public class DingdingApproveDTO {
    /**
     * 审批人钉钉用户id列表
     */
    private Set<String> approvers;
    /**
     * 抄送人钉钉用户id列表
     */
    private Set<String> ccList;
    /**
     * 发起人部门id---erp部门id
     */
    private String deptId;
    /**
     * 审批流表单参数
     */
    private List<Map<String, Object>> formComponentValue;
    /**
     * 审批实例发起人的钉钉用户id
     */
    private String originatorUserId;
    /**
     * 审批流的唯一码
     */
    private String processCode;

    public Set<String> getApprovers() {
        return approvers;
    }

    public void setApprovers(Set<String> approvers) {
        this.approvers = approvers;
    }

    public Set<String> getCcList() {
        return ccList;
    }

    public void setCcList(Set<String> ccList) {
        this.ccList = ccList;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public List<Map<String, Object>> getFormComponentValue() {
        return formComponentValue;
    }

    public void setFormComponentValue(List<Map<String, Object>> formComponentValue) {
        this.formComponentValue = formComponentValue;
    }

    public String getOriginatorUserId() {
        return originatorUserId;
    }

    public void setOriginatorUserId(String originatorUserId) {
        this.originatorUserId = originatorUserId;
    }

    public String getProcessCode() {
        return processCode;
    }

    public void setProcessCode(String processCode) {
        this.processCode = processCode;
    }


}
