package com.lxzl.erp.dataaccess.domain.materiel;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;

import java.math.BigDecimal;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-10-30 19:57
 */
public class MaterielDO extends BaseDO {
    private Integer id;
    private String materielNo;
    private String materielName;
    private Integer brandId;
    private Integer categoryId;
    private Integer propertyId;
    private Integer propertyValueId;
    private BigDecimal materielPrice;
    private BigDecimal originalPrice;
    private BigDecimal rentPrice;
    private Integer dataStatus;
    private String remark;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMaterielNo() {
        return materielNo;
    }

    public void setMaterielNo(String materielNo) {
        this.materielNo = materielNo == null ? null : materielNo.trim();
    }

    public String getMaterielName() {
        return materielName;
    }

    public void setMaterielName(String materielName) {
        this.materielName = materielName == null ? null : materielName.trim();
    }

    public BigDecimal getMaterielPrice() {
        return materielPrice;
    }

    public void setMaterielPrice(BigDecimal materielPrice) {
        this.materielPrice = materielPrice;
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
        this.remark = remark == null ? null : remark.trim();
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Integer propertyId) {
        this.propertyId = propertyId;
    }

    public Integer getPropertyValueId() {
        return propertyValueId;
    }

    public void setPropertyValueId(Integer propertyValueId) {
        this.propertyValueId = propertyValueId;
    }

    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(BigDecimal originalPrice) {
        this.originalPrice = originalPrice;
    }

    public BigDecimal getRentPrice() {
        return rentPrice;
    }

    public void setRentPrice(BigDecimal rentPrice) {
        this.rentPrice = rentPrice;
    }
}
