package com.lxzl.erp.common.domain.changeOrder.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.product.pojo.ProductEquipment;
import com.lxzl.erp.common.domain.validGroup.ExtendGroup;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ChangeOrderProductEquipment implements Serializable {


    private Integer changeOrderProductEquipmentId;   //唯一标识
    private Integer changeOrderProductId;   //租赁换货商品项ID
    private Integer changeOrderId;   //换货ID
    @NotNull(message = ErrorCode.CHANGE_ORDER_NO_NOT_NULL)
    private String changeOrderNo;   //换货编号
    private String orderNo;   //订单编号
    private Integer srcEquipmentId;   //原设备ID
    @NotNull(message = ErrorCode.CHANGE_ORDER_SRC_EQUIPMENT_NO_NOT_NULL, groups = {ExtendGroup.class})
    private String srcEquipmentNo;   //原设备编号
    private Integer destEquipmentId;   //目标设备ID
    private String destEquipmentNo;   //目标设备编号
    private BigDecimal priceDiff;   //差价，可以是正值或负值，差价计算标准为每月
    private Integer dataStatus;   //状态：0不可用；1可用；2删除
    private String remark;   //备注
    private Date createTime;   //添加时间
    private String createUser;   //添加人
    private Date updateTime;   //添加时间
    private String updateUser;   //修改人

    private ProductEquipment srcProductEquipment;
    private ProductEquipment destProductEquipment;

    public Integer getChangeOrderProductEquipmentId() {
        return changeOrderProductEquipmentId;
    }

    public void setChangeOrderProductEquipmentId(Integer changeOrderProductEquipmentId) {
        this.changeOrderProductEquipmentId = changeOrderProductEquipmentId;
    }

    public Integer getChangeOrderProductId() {
        return changeOrderProductId;
    }

    public void setChangeOrderProductId(Integer changeOrderProductId) {
        this.changeOrderProductId = changeOrderProductId;
    }

    public Integer getChangeOrderId() {
        return changeOrderId;
    }

    public void setChangeOrderId(Integer changeOrderId) {
        this.changeOrderId = changeOrderId;
    }

    public String getChangeOrderNo() {
        return changeOrderNo;
    }

    public void setChangeOrderNo(String changeOrderNo) {
        this.changeOrderNo = changeOrderNo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getSrcEquipmentId() {
        return srcEquipmentId;
    }

    public void setSrcEquipmentId(Integer srcEquipmentId) {
        this.srcEquipmentId = srcEquipmentId;
    }

    public String getSrcEquipmentNo() {
        return srcEquipmentNo;
    }

    public void setSrcEquipmentNo(String srcEquipmentNo) {
        this.srcEquipmentNo = srcEquipmentNo;
    }

    public Integer getDestEquipmentId() {
        return destEquipmentId;
    }

    public void setDestEquipmentId(Integer destEquipmentId) {
        this.destEquipmentId = destEquipmentId;
    }

    public String getDestEquipmentNo() {
        return destEquipmentNo;
    }

    public void setDestEquipmentNo(String destEquipmentNo) {
        this.destEquipmentNo = destEquipmentNo;
    }

    public BigDecimal getPriceDiff() {
        return priceDiff;
    }

    public void setPriceDiff(BigDecimal priceDiff) {
        this.priceDiff = priceDiff;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public ProductEquipment getSrcProductEquipment() {
        return srcProductEquipment;
    }

    public void setSrcProductEquipment(ProductEquipment srcProductEquipment) {
        this.srcProductEquipment = srcProductEquipment;
    }

    public ProductEquipment getDestProductEquipment() {
        return destProductEquipment;
    }

    public void setDestProductEquipment(ProductEquipment destProductEquipment) {
        this.destProductEquipment = destProductEquipment;
    }
}