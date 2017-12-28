package com.lxzl.erp.common.domain.deploymentOrder;

import com.lxzl.erp.common.domain.base.BasePageParam;

import java.util.Date;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-11-29 13:40
 */
public class DeploymentOrderQueryParam extends BasePageParam {

    private Integer deploymentOrderId;   //唯一标识
    private String deploymentOrderNo;   //调配单编号
    private Integer deploymentType;   //调配类型，1借调，2售调
    private Integer srcWarehouseId;   //源仓库ID
    private Integer targetWarehouseId;   //目标仓库ID
    private Integer deploymentOrderStatus;   //调配单状态，0未提交，1审批中，2处理中，3确认收货
    private Date createStartTime;
    private Date createEndTime;

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

    public Integer getDeploymentOrderId() {
        return deploymentOrderId;
    }

    public void setDeploymentOrderId(Integer deploymentOrderId) {
        this.deploymentOrderId = deploymentOrderId;
    }

    public String getDeploymentOrderNo() {
        return deploymentOrderNo;
    }

    public void setDeploymentOrderNo(String deploymentOrderNo) {
        this.deploymentOrderNo = deploymentOrderNo;
    }

    public Integer getDeploymentType() {
        return deploymentType;
    }

    public void setDeploymentType(Integer deploymentType) {
        this.deploymentType = deploymentType;
    }

    public Integer getSrcWarehouseId() {
        return srcWarehouseId;
    }

    public void setSrcWarehouseId(Integer srcWarehouseId) {
        this.srcWarehouseId = srcWarehouseId;
    }

    public Integer getTargetWarehouseId() {
        return targetWarehouseId;
    }

    public void setTargetWarehouseId(Integer targetWarehouseId) {
        this.targetWarehouseId = targetWarehouseId;
    }

    public Integer getDeploymentOrderStatus() {
        return deploymentOrderStatus;
    }

    public void setDeploymentOrderStatus(Integer deploymentOrderStatus) {
        this.deploymentOrderStatus = deploymentOrderStatus;
    }
}
