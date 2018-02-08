package com.lxzl.erp.common.domain.changeOrder.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.base.BasePO;
import com.lxzl.erp.common.domain.material.pojo.BulkMaterial;
import com.lxzl.erp.common.domain.order.pojo.OrderMaterial;
import com.lxzl.erp.common.domain.validGroup.changeOrder.UpdatePriceDiffGroup;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ChangeOrderMaterialBulk extends BasePO {
    @NotNull(message = ErrorCode.ID_NOT_NULL,groups = {UpdatePriceDiffGroup.class})
    private Integer changeOrderMaterialBulkId;   //唯一标识
    private Integer changeOrderMaterialId;   //租赁换货物料项ID
    private Integer changeOrderId;   //换货ID
    private String changeOrderNo;   //换货编号
    private Integer srcEquipmentId;    //原设备ID,如果更换的散料是租赁设备上的，此字段有值
    private String srcEquipmentNo;    //原设备编号,如果更换的散料是租赁设备上的，此字段有值
    private Integer srcBulkMaterialId;   //原散料ID
    private String srcBulkMaterialNo;   //原散料编号
    private Integer destBulkMaterialId;   //目标散料ID
    private String destBulkMaterialNo;   //目标散料编号
    private String orderNo;   //订单编号
    @Min(value=0,message = ErrorCode.DIFF_PRICE_ERROR, groups = {UpdatePriceDiffGroup.class})
    @NotNull(message = ErrorCode.DIFF_PRICE_ERROR, groups = {UpdatePriceDiffGroup.class})
    private BigDecimal priceDiff;   //差价
    private Integer dataStatus;   //状态：0不可用；1可用；2删除
    private String remark;   //备注
    private Date createTime;   //添加时间
    private String createUser;   //添加人
    private Date updateTime;   //添加时间
    private String updateUser;   //修改人

    private BulkMaterial srcBulkMaterial;
    private BulkMaterial destBulkMaterial;
    private OrderMaterial orderMaterial;

    public Integer getChangeOrderMaterialBulkId() {
        return changeOrderMaterialBulkId;
    }

    public void setChangeOrderMaterialBulkId(Integer changeOrderMaterialBulkId) {
        this.changeOrderMaterialBulkId = changeOrderMaterialBulkId;
    }

    public Integer getChangeOrderMaterialId() {
        return changeOrderMaterialId;
    }

    public void setChangeOrderMaterialId(Integer changeOrderMaterialId) {
        this.changeOrderMaterialId = changeOrderMaterialId;
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

    public Integer getSrcBulkMaterialId() {
        return srcBulkMaterialId;
    }

    public void setSrcBulkMaterialId(Integer srcBulkMaterialId) {
        this.srcBulkMaterialId = srcBulkMaterialId;
    }

    public String getSrcBulkMaterialNo() {
        return srcBulkMaterialNo;
    }

    public void setSrcBulkMaterialNo(String srcBulkMaterialNo) {
        this.srcBulkMaterialNo = srcBulkMaterialNo;
    }

    public Integer getDestBulkMaterialId() {
        return destBulkMaterialId;
    }

    public void setDestBulkMaterialId(Integer destBulkMaterialId) {
        this.destBulkMaterialId = destBulkMaterialId;
    }

    public String getDestBulkMaterialNo() {
        return destBulkMaterialNo;
    }

    public void setDestBulkMaterialNo(String destBulkMaterialNo) {
        this.destBulkMaterialNo = destBulkMaterialNo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
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

    public BulkMaterial getSrcBulkMaterial() {
        return srcBulkMaterial;
    }

    public void setSrcBulkMaterial(BulkMaterial srcBulkMaterial) {
        this.srcBulkMaterial = srcBulkMaterial;
    }

    public BulkMaterial getDestBulkMaterial() {
        return destBulkMaterial;
    }

    public void setDestBulkMaterial(BulkMaterial destBulkMaterial) {
        this.destBulkMaterial = destBulkMaterial;
    }

    public OrderMaterial getOrderMaterial() {
        return orderMaterial;
    }

    public void setOrderMaterial(OrderMaterial orderMaterial) {
        this.orderMaterial = orderMaterial;
    }
}