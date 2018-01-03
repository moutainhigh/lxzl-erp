package com.lxzl.erp.common.domain.workflow;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePageParam;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WorkflowTemplateQueryParam extends BasePageParam {

    private Integer workflowTemplateId;
    private String workflowTemplateName;

    public Integer getWorkflowTemplateId() {
        return workflowTemplateId;
    }

    public void setWorkflowTemplateId(Integer workflowTemplateId) {
        this.workflowTemplateId = workflowTemplateId;
    }

    public String getWorkflowTemplateName() {
        return workflowTemplateName;
    }

    public void setWorkflowTemplateName(String workflowTemplateName) {
        this.workflowTemplateName = workflowTemplateName;
    }
}
