package com.lxzl.erp.common.domain.peerDeploymentOrder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePageParam;

import java.util.Date;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/1/15
 * @Time : Created in 10:24
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PeerDeploymentOrderQueryParam extends BasePageParam {

    private String peerDeploymentOrderNo;   //同行调配单编号
    private Integer peerId;   //同行ID
    private Integer rentType;   //租赁方式，1按天租，2按月租
    private Integer rentTimeLength;   //租赁期限
    private Integer warehouseId;   //目标仓库ID
    private Integer deliveryMode;   //发货方式，1快递，2自提
    private Double taxRate;   //税率
    private Integer peerDeploymentOrderStatus;   //调配单状态，0未提交，4审批中，8处理中，16确认收货，20退回审批中，24退回处理中，28已退回，32取消
    private Date createStartTime;
    private Date createEndTime;

    public String getPeerDeploymentOrderNo() {
        return peerDeploymentOrderNo;
    }

    public void setPeerDeploymentOrderNo(String peerDeploymentOrderNo) {
        this.peerDeploymentOrderNo = peerDeploymentOrderNo;
    }

    public Integer getPeerId() {
        return peerId;
    }

    public void setPeerId(Integer peerId) {
        this.peerId = peerId;
    }

    public Integer getRentType() {
        return rentType;
    }

    public void setRentType(Integer rentType) {
        this.rentType = rentType;
    }

    public Integer getRentTimeLength() {
        return rentTimeLength;
    }

    public void setRentTimeLength(Integer rentTimeLength) {
        this.rentTimeLength = rentTimeLength;
    }

    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }

    public Integer getDeliveryMode() {
        return deliveryMode;
    }

    public void setDeliveryMode(Integer deliveryMode) {
        this.deliveryMode = deliveryMode;
    }

    public Double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(Double taxRate) {
        this.taxRate = taxRate;
    }

    public Integer getPeerDeploymentOrderStatus() {
        return peerDeploymentOrderStatus;
    }

    public void setPeerDeploymentOrderStatus(Integer peerDeploymentOrderStatus) {
        this.peerDeploymentOrderStatus = peerDeploymentOrderStatus;
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
