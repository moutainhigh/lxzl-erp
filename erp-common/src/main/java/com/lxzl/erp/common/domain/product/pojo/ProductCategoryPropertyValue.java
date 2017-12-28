package com.lxzl.erp.common.domain.product.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePO;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductCategoryPropertyValue extends BasePO {

    private Integer categoryPropertyValueId;
    private String propertyValueName;
    private Integer propertyId;
    private Integer categoryId;
    private Integer referId;
    private Double propertyCapacityValue;
    private Integer materialModelId;
    private Integer dataOrder;
    private Integer dataStatus;
    private String remark;

    private String propertyName;
    private Integer materialType;

    public Integer getCategoryPropertyValueId() {
        return categoryPropertyValueId;
    }

    public void setCategoryPropertyValueId(Integer categoryPropertyValueId) {
        this.categoryPropertyValueId = categoryPropertyValueId;
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

    public Integer getMaterialType() {
        return materialType;
    }

    public void setMaterialType(Integer materialType) {
        this.materialType = materialType;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public Integer getMaterialModelId() {
        return materialModelId;
    }

    public void setMaterialModelId(Integer materialModelId) {
        this.materialModelId = materialModelId;
    }
}
