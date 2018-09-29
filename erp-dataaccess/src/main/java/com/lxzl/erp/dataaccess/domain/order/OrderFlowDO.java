package com.lxzl.erp.dataaccess.domain.order;

import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.se.dataaccess.mysql.domain.BaseDO;

import java.util.Date;


public class OrderFlowDO extends BaseDO {

    private Integer id;
    private String originalOrderNo;
    private String nodeOrderNo;
    private String orderNo;
    private Integer dataStatus;
    private Date originalExpectReturnTime;
    private Date originalRentStartTime;
    private Date rentStartTime;
    private Date expectReturnTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOriginalOrderNo() {
        return originalOrderNo;
    }

    public void setOriginalOrderNo(String originalOrderNo) {
        this.originalOrderNo = originalOrderNo;
    }

    public String getNodeOrderNo() {
        return nodeOrderNo;
    }

    public void setNodeOrderNo(String nodeOrderNo) {
        this.nodeOrderNo = nodeOrderNo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(Integer dataStatus) {
        this.dataStatus = dataStatus;
    }

    public Date getOriginalExpectReturnTime() {
        return originalExpectReturnTime;
    }

    public void setOriginalExpectReturnTime(Date originalExpectReturnTime) {
        this.originalExpectReturnTime = originalExpectReturnTime;
    }

    public Date getOriginalRentStartTime() {
        return originalRentStartTime;
    }

    public void setOriginalRentStartTime(Date originalRentStartTime) {
        this.originalRentStartTime = originalRentStartTime;
    }

    public Date getRentStartTime() {
        return rentStartTime;
    }

    public void setRentStartTime(Date rentStartTime) {
        this.rentStartTime = rentStartTime;
    }

    public Date getExpectReturnTime() {
        return expectReturnTime;
    }

    public void setExpectReturnTime(Date expectReturnTime) {
        this.expectReturnTime = expectReturnTime;
    }

}