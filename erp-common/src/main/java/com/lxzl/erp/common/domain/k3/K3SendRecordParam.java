package com.lxzl.erp.common.domain.k3;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePageParam;

import java.io.Serializable;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class K3SendRecordParam extends BasePageParam implements Serializable {

    private Integer k3SendRecordId;   //唯一标识
    private Integer recordType;   //记录类型，1推送客户、2推送供应商、3推送商品、4推送配件、5推送订单、6推送用户、7推送退货单
    private Integer sendResult;   //是否推送成功，1是0否
    private Integer receiveResult;   //是否接收成功，1是0否
    private Date sendStartTime;   //发送起始时间
    private Date sendEndTime;   //发送最后时间
    private Integer recordReferId;	//记录关联ID
    private String recordReferNo;	//记录关联编号

    public Integer getK3SendRecordId() { return k3SendRecordId; }

    public void setK3SendRecordId(Integer k3SendRecordId) { this.k3SendRecordId = k3SendRecordId; }

    public Integer getRecordType() { return recordType; }

    public void setRecordType(Integer recordType) { this.recordType = recordType; }

    public Integer getSendResult() { return sendResult; }

    public void setSendResult(Integer sendResult) { this.sendResult = sendResult; }

    public Integer getReceiveResult() { return receiveResult; }

    public void setReceiveResult(Integer receiveResult) { this.receiveResult = receiveResult; }

    public Date getSendStartTime() { return sendStartTime; }

    public void setSendStartTime(Date sendStartTime) { this.sendStartTime = sendStartTime; }

    public Date getSendEndTime() { return sendEndTime; }

    public void setSendEndTime(Date sendEndTime) { this.sendEndTime = sendEndTime; }

    public Integer getRecordReferId() { return recordReferId; }

    public void setRecordReferId(Integer recordReferId) { this.recordReferId = recordReferId; }

    public String getRecordReferNo() { return recordReferNo; }

    public void setRecordReferNo(String recordReferNo) { this.recordReferNo = recordReferNo; }
}
