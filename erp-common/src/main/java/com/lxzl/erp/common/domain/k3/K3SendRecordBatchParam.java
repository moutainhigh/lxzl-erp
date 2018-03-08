package com.lxzl.erp.common.domain.k3;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class K3SendRecordBatchParam {

    private Integer recordType;   //记录类型，1推送客户、2推送商品、3推送配件、4推送供应商、5推送订单、6推送用户、7推送退货单
    private Integer batchType;   //类型，1 全部 2 成功 3 失败
    private Date startTime;
    private Date endTime;
    private Integer bufferTime; //缓冲时间

    public Integer getRecordType() { return recordType; }

    public void setRecordType(Integer recordType) { this.recordType = recordType; }

    public Integer getBatchType() { return batchType; }

    public void setBatchType(Integer batchType) { this.batchType = batchType; }

    public Date getStartTime() { return startTime; }

    public void setStartTime(Date startTime) { this.startTime = startTime; }

    public Date getEndTime() { return endTime; }

    public void setEndTime(Date endTime) { this.endTime = endTime; }

    public Integer getBufferTime() { return bufferTime; }

    public void setBufferTime(Integer bufferTime) { this.bufferTime = bufferTime; }
}
