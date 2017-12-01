package com.lxzl.erp.common.domain.returnOrder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePageParam;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReturnOrderPageParam extends BasePageParam{

    private String returnOrderNo;
    private String customerNo;
    private Integer returnOrderStatus;
    private String ownerName;
    private Date createStartTime;
    private Date createEndTime;

    public String getReturnOrderNo() {
        return returnOrderNo;
    }

    public void setReturnOrderNo(String returnOrderNo) {
        this.returnOrderNo = returnOrderNo;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public Integer getReturnOrderStatus() {
        return returnOrderStatus;
    }

    public void setReturnOrderStatus(Integer returnOrderStatus) {
        this.returnOrderStatus = returnOrderStatus;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public Date getCreateStartTime() {
        return createStartTime;
    }

    public void setCreateStartTime(Date createStartTime) {
        this.createStartTime = createStartTime;
    }

    public Date getCreateEndTime() {
        return createEndTime;
    }

    public void setCreateEndTime(Date createEndTime) {
        this.createEndTime = createEndTime;
    }
}
