package com.lxzl.erp.dataaccess.domain.changeOrder;

import com.lxzl.erp.dataaccess.domain.product.ProductEquipmentDO;
import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import org.springframework.data.annotation.Transient;

import java.math.BigDecimal;

public class ChangeOrderProductEquipmentDO extends BaseDO {

    private Integer id;
    private Integer changeOrderProductId;
    private Integer changeOrderId;
    private String changeOrderNo;
    private String orderNo;
    private Integer srcEquipmentId;
    private String srcEquipmentNo;
    private Integer destEquipmentId;
    private String destEquipmentNo;
    private BigDecimal priceDiff;
    private Integer dataStatus;
    private String remark;

    @Transient
    private ProductEquipmentDO srcProductEquipmentDO;
    @Transient
    private ProductEquipmentDO destProductEquipmentDO;
    @Transient
    private Integer srcSkuId;
    @Transient
    private Integer destSkuId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public ProductEquipmentDO getSrcProductEquipmentDO() {
        return srcProductEquipmentDO;
    }

    public void setSrcProductEquipmentDO(ProductEquipmentDO srcProductEquipmentDO) {
        this.srcProductEquipmentDO = srcProductEquipmentDO;
    }

    public ProductEquipmentDO getDestProductEquipmentDO() {
        return destProductEquipmentDO;
    }

    public void setDestProductEquipmentDO(ProductEquipmentDO destProductEquipmentDO) {
        this.destProductEquipmentDO = destProductEquipmentDO;
    }

    public Integer getSrcSkuId() {
        return srcSkuId;
    }

    public void setSrcSkuId(Integer srcSkuId) {
        this.srcSkuId = srcSkuId;
    }

    public Integer getDestSkuId() {
        return destSkuId;
    }

    public void setDestSkuId(Integer destSkuId) {
        this.destSkuId = destSkuId;
    }
}