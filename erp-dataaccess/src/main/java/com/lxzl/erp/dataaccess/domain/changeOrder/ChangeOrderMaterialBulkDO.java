package com.lxzl.erp.dataaccess.domain.changeOrder;

import com.lxzl.erp.dataaccess.domain.material.BulkMaterialDO;
import com.lxzl.erp.dataaccess.domain.material.MaterialDO;
import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import org.springframework.data.annotation.Transient;

import java.math.BigDecimal;

public class ChangeOrderMaterialBulkDO extends BaseDO {

    private Integer id;
    private Integer changeOrderMaterialId;
    private Integer changeOrderId;
    private String changeOrderNo;
    private Integer srcEquipmentId;
    private String srcEquipmentNo;
    private Integer srcBulkMaterialId;
    private String srcBulkMaterialNo;
    private Integer destBulkMaterialId;
    private String destBulkMaterialNo;
    private String orderNo;
    private BigDecimal priceDiff;
    private Integer dataStatus;
    private String remark;

    @Transient
    private BulkMaterialDO srcBulkMaterialDO;
    @Transient
    private BulkMaterialDO destBulkMaterialDO;
    @Transient
    private Integer srcMaterialId;
    @Transient
    private Integer destMaterialId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public BulkMaterialDO getSrcBulkMaterialDO() {
        return srcBulkMaterialDO;
    }

    public void setSrcBulkMaterialDO(BulkMaterialDO srcBulkMaterialDO) {
        this.srcBulkMaterialDO = srcBulkMaterialDO;
    }

    public BulkMaterialDO getDestBulkMaterialDO() {
        return destBulkMaterialDO;
    }

    public void setDestBulkMaterialDO(BulkMaterialDO destBulkMaterialDO) {
        this.destBulkMaterialDO = destBulkMaterialDO;
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

    public Integer getSrcMaterialId() {
        return srcMaterialId;
    }

    public void setSrcMaterialId(Integer srcMaterialId) {
        this.srcMaterialId = srcMaterialId;
    }

    public Integer getDestMaterialId() {
        return destMaterialId;
    }

    public void setDestMaterialId(Integer destMaterialId) {
        this.destMaterialId = destMaterialId;
    }
}