package com.lxzl.erp.common.domain.dingding.approve;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.util.CollectionUtil;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 钉钉审批数据传输对象
 *
 * @author daiqi
 * @create 2018-04-20 10:58
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DingdingApproveDTO {
    /**
     * 审批人钉钉用户id列表
     */
    private Set<String> approverList;
    /**
     * 抄送人钉钉用户id列表
     */
    private Set<String> ccUserIdsList;
    /**
     * 抄送时间---START,FINISH,START_FINISH
     */
    private String ccPosition;
    /**
     * 发起人部门id---erp部门id
     */
    private String deptId;
    /**
     * 审批流表单参数
     */
    private List<DingdingFormComponentValueDTO> formComponentValueList;
    /**
     * 审批实例发起人的钉钉用户id
     */
    private String originatorUserId;
    /**
     * 审批流的唯一码
     */
    private String processCode;

    /**
     * 审批回调url
     */
    private String callbackUrl;

    @JSONField(serialize = false)
    public Set<String> getApproverList() {
        return approverList;
    }

    public void setApproverList(Set<String> approverList) {
        this.approverList = approverList;
    }

    @JSONField(serialize = false)
    public Set<String> getCcUserIdsList() {
        return ccUserIdsList;
    }

    public void setCcUserIdsList(Set<String> ccUserIdsList) {
        this.ccUserIdsList = ccUserIdsList;
    }

    public String getDeptId() {
        return deptId;
    }

    public String getCcPosition() {
        return ccPosition;
    }

    public void setCcPosition(String ccPosition) {
        this.ccPosition = ccPosition;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    @JSONField(serialize = false)
    public List<DingdingFormComponentValueDTO> getFormComponentValueList() {
        return formComponentValueList;
    }

    public void setFormComponentValueList(List<DingdingFormComponentValueDTO> formComponentValueList) {
        this.formComponentValueList = formComponentValueList;
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

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public String getFormComponentValues() {
        if (CollectionUtil.isEmpty(formComponentValueList)) {
            return null;
        }
        return JSONArray.toJSONString(formComponentValueList);
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

    private String getCommaStr(Set<String> list) {
        if (CollectionUtil.isEmpty(list)) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (String value : list) {
            sb.append(value);
            if (i < list.size() - 1) {
                sb.append(",");
            }
            i++;
        }
        return sb.toString();
    }
}
