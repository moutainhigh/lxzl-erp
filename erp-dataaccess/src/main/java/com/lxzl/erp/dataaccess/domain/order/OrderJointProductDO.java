package com.lxzl.erp.dataaccess.domain.order;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderJointProductDO extends BaseDO {
    private Integer id;
    private Integer orderId;
    private Integer jointProductId;
    private String jointProductName;
    private Integer jointProductCount;
    private Integer dataStatus;
    private String remark;
    private Integer isNew;

    List<OrderProductDO> orderProductDOList;
    List<OrderMaterialDO> orderMaterialDOList;

    public Integer getIsNew() {
        return isNew;
    }

    public void setIsNew(Integer isNew) {
        this.isNew = isNew;
    }

    public String getJointProductName() {
        return jointProductName;
    }

    public void setJointProductName(String jointProductName) {
        this.jointProductName = jointProductName;
    }

    public List<OrderProductDO> getOrderProductDOList() {
        return orderProductDOList;
    }

    public void setOrderProductDOList(List<OrderProductDO> orderProductDOList) {
        this.orderProductDOList = orderProductDOList;
    }

    public List<OrderMaterialDO> getOrderMaterialDOList() {
        return orderMaterialDOList;
    }

    public void setOrderMaterialDOList(List<OrderMaterialDO> orderMaterialDOList) {
        this.orderMaterialDOList = orderMaterialDOList;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(Integer dataStatus) {
        this.dataStatus = dataStatus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getJointProductId() {
        return jointProductId;
    }

    public void setJointProductId(Integer jointProductId) {
        this.jointProductId = jointProductId;
    }

    public Integer getJointProductCount() {
        return jointProductCount;
    }

    public void setJointProductCount(Integer jointProductCount) {
        this.jointProductCount = jointProductCount;
    }
}
