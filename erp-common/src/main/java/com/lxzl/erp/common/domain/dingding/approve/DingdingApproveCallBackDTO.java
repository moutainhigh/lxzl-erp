package com.lxzl.erp.common.domain.dingding.approve;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 钉钉工作流回调数据传输对象
 * @author daiqi
 * @create 2018-04-23 15:04
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DingdingApproveCallBackDTO extends DingdingApproveResultDTO{
    /** 响应的时间类型--- bpms_task_change:审批任务类型 --- bpms_instance_change:审批实例类型*/
    private String eventType;
    /** 双类id */
    private String bizCategoryId;
    /** 公司id */
    private String corpId;
    /** 创建时间戳 */
    private Long createTime;
    /** 完成时间戳 */
    private Long finishTime;
    /** 实例模板代码 */
    private String processCode;
    /** 审批结果 */
    private String result;
    /** 职员id---钉钉的用户id */
    private String staffId;
    /** 主题 */
    private String title;
    /** 类型---start:任务或实例开始---finish任务或实例完成 */
    private String type;
    /** 工作流单号 */
    private String instanceMarking;

    public String getEventType() {
        return eventType;
    }

    @JsonProperty(value = "EventType")
    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getBizCategoryId() {
        return bizCategoryId;
    }

    public void setBizCategoryId(String bizCategoryId) {
        this.bizCategoryId = bizCategoryId;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Long finishTime) {
        this.finishTime = finishTime;
    }

    public String getProcessCode() {
        return processCode;
    }

    public void setProcessCode(String processCode) {
        this.processCode = processCode;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getInstanceMarking() {
        return instanceMarking;
    }

    public void setInstanceMarking(String instanceMarking) {
        this.instanceMarking = instanceMarking;
    }
}
