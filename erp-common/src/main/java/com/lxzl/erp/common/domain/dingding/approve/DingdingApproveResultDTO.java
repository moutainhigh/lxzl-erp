package com.lxzl.erp.common.domain.dingding.approve;

import com.alibaba.fastjson.annotation.JSONField;

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

    @JSONField(name = "process_instance_id")
    public void setProcess_instance_id(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }
}
