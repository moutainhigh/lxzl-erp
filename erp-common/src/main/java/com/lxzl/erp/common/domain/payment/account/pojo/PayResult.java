package com.lxzl.erp.common.domain.payment.account.pojo;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2018-01-29 14:50
 */
public class PayResult {
    private String businessOrderNo;
    private String respOrderNo;
    private String payStatus;
    private String recordStatus;
    private String respAmount;
    private String refundStatus;
    private String requestNo;
    private String responseDate;
    private String status;

    public String getBusinessOrderNo() {
        return businessOrderNo;
    }

    public void setBusinessOrderNo(String businessOrderNo) {
        this.businessOrderNo = businessOrderNo;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public String getRecordStatus() {
        return recordStatus;
    }

    public void setRecordStatus(String recordStatus) {
        this.recordStatus = recordStatus;
    }

    public String getRespAmount() {
        return respAmount;
    }

    public void setRespAmount(String respAmount) {
        this.respAmount = respAmount;
    }

    public String getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(String refundStatus) {
        this.refundStatus = refundStatus;
    }

    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo;
    }

    public String getResponseDate() {
        return responseDate;
    }

    public void setResponseDate(String responseDate) {
        this.responseDate = responseDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRespOrderNo() {
        return respOrderNo;
    }

    public void setRespOrderNo(String respOrderNo) {
        this.respOrderNo = respOrderNo;
    }
}
