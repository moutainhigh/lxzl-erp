package com.lxzl.erp.common.domain.purchaseApply;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePageParam;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PurchaseApplyOrderPageParam extends BasePageParam{

    private String purchaseApplyOrderNo;
    private String applyUserName;
    private String warehouseName;
    private String departmentName;
    private Integer purchaseApplyOrderStatus;
    private Date useStartTime;
    private Date useEndTime;
    private Date createStartTime;
    private Date createEndTime;

    public String getPurchaseApplyOrderNo() {
        return purchaseApplyOrderNo;
    }

    public void setPurchaseApplyOrderNo(String purchaseApplyOrderNo) {
        this.purchaseApplyOrderNo = purchaseApplyOrderNo;
    }

    public String getApplyUserName() {
        return applyUserName;
    }

    public void setApplyUserName(String applyUserName) {
        this.applyUserName = applyUserName;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Integer getPurchaseApplyOrderStatus() {
        return purchaseApplyOrderStatus;
    }

    public void setPurchaseApplyOrderStatus(Integer purchaseApplyOrderStatus) {
        this.purchaseApplyOrderStatus = purchaseApplyOrderStatus;
    }

    public Date getUseStartTime() {
        return useStartTime;
    }

    public void setUseStartTime(Date useStartTime) {
        this.useStartTime = useStartTime;
    }

    public Date getUseEndTime() {
        return useEndTime;
    }

    public void setUseEndTime(Date useEndTime) {
        this.useEndTime = useEndTime;
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
