package com.lxzl.erp.common.domain.dingding.approve;

/**
 * 钉钉审批结果数据传输对象
 * @author daiqi
 * @create 2018-04-20 10:58
 */
public class DingdingApproveResultDTO {
    /** 钉钉的审批实例编号 */
    private String processInstanceId;

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }
}
