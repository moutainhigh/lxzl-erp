package com.lxzl.erp.common.domain.dingding.approve;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.dingding.DingdingBaseDTO;
import com.lxzl.erp.common.util.CollectionUtil;

import java.util.Set;

/**
 * 钉钉审批数据传输对象
 *
 * @author daiqi
 * @create 2018-04-20 10:58
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DingdingApproveDTO extends DingdingBaseDTO {
    /**
     * 审批人用户id列表
     */
    private Set<Integer> approverList;
    /**
     * 抄送人用户id列表
     */
    private Set<Integer> ccUserIdsList;
    /**
     * 抄送时间---START,FINISH,START_FINISH
     */
    private String ccPosition;
    /**
     * 发起人部门id---erp部门id
     */
    private Integer deptId;
    /**
     * 审批流表单对象
     */
    private Object formComponentObj;
    /**
     * 审批实例发起人的用户id
     */
    private Integer originatorUserId;
    /**
     * 审批流的唯一码
     */
    private String processCode;

    /**
     * 工作流单号
     */
    private String instanceMarking;


    @JSONField(serialize = false)
    public Set<Integer> getApproverList() {
        return approverList;
    }

    public void setApproverList(Set<Integer> approverList) {
        this.approverList = approverList;
    }

    @JSONField(serialize = false)
    public Set<Integer> getCcUserIdsList() {
        return ccUserIdsList;
    }

    public void setCcUserIdsList(Set<Integer> ccUserIdsList) {
        this.ccUserIdsList = ccUserIdsList;
    }

    public Integer getDeptId() {
        return deptId;
    }

    public String getCcPosition() {
        return ccPosition;
    }

    public void setCcPosition(String ccPosition) {
        this.ccPosition = ccPosition;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    public Object getFormComponentObj() {
        return formComponentObj;
    }

    public void setFormComponentObj(Object formComponentObj) {
        this.formComponentObj = formComponentObj;
    }

    public Integer getOriginatorUserId() {
        return originatorUserId;
    }

    public void setOriginatorUserId(Integer originatorUserId) {
        this.originatorUserId = originatorUserId;
    }

    public String getProcessCode() {
        return processCode;
    }

    public void setProcessCode(String processCode) {
        this.processCode = processCode;
    }

    public String getInstanceMarking() {
        return instanceMarking;
    }

    public void setInstanceMarking(String instanceMarking) {
        this.instanceMarking = instanceMarking;
    }

    /**
     * 获取审批人userid列表(逗号分割)
     */
    public String getApprovers() {
        return getCommaStr(approverList);
    }

    /**
     * 抄送人userid列表(逗号分割)
     */
    public String getCcList() {
        return getCommaStr(ccUserIdsList);
    }

    private String getCommaStr(Set<Integer> list) {
        if (CollectionUtil.isEmpty(list)) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (Integer value : list) {
            sb.append(value);
            if (i < list.size() - 1) {
                sb.append(",");
            }
            i++;
        }
        return sb.toString();
    }
}
