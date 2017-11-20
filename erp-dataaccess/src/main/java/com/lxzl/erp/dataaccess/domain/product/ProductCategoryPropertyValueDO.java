package com.lxzl.erp.dataaccess.domain.product;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Transient;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductCategoryPropertyValueDO extends BaseDO {

    private Integer id;
    private String propertyValueName;
    private Integer propertyId;
    private Integer categoryId;
    private Integer referId;
    private Double propertyCapacityValue;
    private Integer dataOrder;
    private Integer dataStatus;
    private String remark;

    @Transient
    private String propertyName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPropertyValueName() {
        return propertyValueName;
    }

    public void setPropertyValueName(String propertyValueName) {
        this.propertyValueName = propertyValueName;
    }

    public Integer getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Integer propertyId) {
        this.propertyId = propertyId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getDataOrder() {
        return dataOrder;
    }

    public void setDataOrder(Integer dataOrder) {
        this.dataOrder = dataOrder;
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

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public Integer getReferId() {
        return referId;
    }

    public void setReferId(Integer referId) {
        this.referId = referId;
    }

    public Double getPropertyCapacityValue() {
        return propertyCapacityValue;
    }

    public void setPropertyCapacityValue(Double propertyCapacityValue) {
        this.propertyCapacityValue = propertyCapacityValue;
    }
}
