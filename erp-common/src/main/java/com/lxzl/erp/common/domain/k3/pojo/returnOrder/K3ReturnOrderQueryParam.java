package com.lxzl.erp.common.domain.k3.pojo.returnOrder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePageParam;

import java.io.Serializable;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class K3ReturnOrderQueryParam extends BasePageParam implements Serializable {

    private String returnOrderNo;   //退还编号
    private String k3CustomerNo;   //K3客户编码
    private String k3CustomerName;   //K3客户名称
    private Date returnStartTime;   //退货开始时间
    private Date returnEndTime;   //退货结束时间
    private Integer returnOrderStatus;   // 归还订单状态，0-待提交，4-审核中，16-已取消，20-已完成，24已驳回
    private String orderNo;  //订单编号

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getReturnOrderStatus() {
        return returnOrderStatus;
    }

    public void setReturnOrderStatus(Integer returnOrderStatus) {
        this.returnOrderStatus = returnOrderStatus;
    }

    public String getK3CustomerNo() {
        return k3CustomerNo;
    }

    public void setK3CustomerNo(String k3CustomerNo) {
        this.k3CustomerNo = k3CustomerNo;
    }

    public String getK3CustomerName() {
        return k3CustomerName;
    }

    public void setK3CustomerName(String k3CustomerName) {
        this.k3CustomerName = k3CustomerName;
    }

    public String getReturnOrderNo() {
        return returnOrderNo;
    }

    public void setReturnOrderNo(String returnOrderNo) {
        this.returnOrderNo = returnOrderNo;
    }

    public Date getReturnStartTime() {
        return returnStartTime;
    }

    public void setReturnStartTime(Date returnStartTime) {
        this.returnStartTime = returnStartTime;
    }

    public Date getReturnEndTime() {
        return returnEndTime;
    }

    public void setReturnEndTime(Date returnEndTime) {
        this.returnEndTime = returnEndTime;
    }
}
