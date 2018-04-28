package com.lxzl.erp.common.domain.dingding.approve.formvalue;

import org.apache.commons.collections.MapUtils;

import java.util.HashMap;

/**
 * 钉钉FormComponentValue数据基础传输对象
 *
 * @author daiqi
 * @create 2018-04-23 17:01
 */
public class DingdingFormComponentValueDTO extends HashMap<String, Object> {
    /**
     * 审批事项
     */
    private static String verifyMattersKey = "verifyMatters";
    /**
     * 工作流模板名称
     */
    private static String workflowTemplateNameKey = "workflowTemplateName";
    /**
     * 工作流单号
     */
    private static String workflowLinkNoKey = "workflowLinkNo";

    public String getVerifyMatters() {
        return MapUtils.getString(this, verifyMattersKey);
    }

    public void setVerifyMatters(String verifyMatters) {
        put(verifyMattersKey, verifyMatters);
    }

    public String getWorkflowTemplateName() {
        return MapUtils.getString(this, workflowTemplateNameKey);
    }

    public void setWorkflowTemplateName(String workflowTemplateName) {
        put(workflowTemplateNameKey, workflowTemplateName);
    }

    public String getWorkflowLinkNo() {
        return MapUtils.getString(this, workflowLinkNoKey);
    }

    public void setWorkflowLinkNo(String workflowLinkNo) {
        put(workflowLinkNoKey, workflowLinkNo);
    }
}
