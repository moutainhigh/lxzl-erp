package com.lxzl.erp.common.domain.product.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductCategoryProperty implements Serializable {

    private Integer categoryPropertyId;
    private String propertyName;
    private Integer categoryId;
    private Integer propertyType;
    private Integer isMaterial;
    private Integer materialType;
    private Integer isInput;
    private Integer isCheckbox;
    private Integer isRequired;
    private Integer dataOrder;
    private Integer dataStatus;
    private String remark;
    private List<ProductCategoryPropertyValue> productCategoryPropertyValueList;

    public Integer getCategoryPropertyId() {
        return categoryPropertyId;
    }

    public void setCategoryPropertyId(Integer categoryPropertyId) {
        this.categoryPropertyId = categoryPropertyId;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(Integer propertyType) {
        this.propertyType = propertyType;
    }

    public Integer getIsInput() {
        return isInput;
    }

    public void setIsInput(Integer isInput) {
        this.isInput = isInput;
    }

    public Integer getIsCheckbox() {
        return isCheckbox;
    }

    public void setIsCheckbox(Integer isCheckbox) {
        this.isCheckbox = isCheckbox;
    }

    public Integer getIsRequired() {
        return isRequired;
    }

    public void setIsRequired(Integer isRequired) {
        this.isRequired = isRequired;
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

    public List<ProductCategoryPropertyValue> getProductCategoryPropertyValueList() {
        return productCategoryPropertyValueList;
    }

    public void setProductCategoryPropertyValueList(List<ProductCategoryPropertyValue> productCategoryPropertyValueList) {
        this.productCategoryPropertyValueList = productCategoryPropertyValueList;
    }

    public Integer getIsMaterial() {
        return isMaterial;
    }

    public void setIsMaterial(Integer isMaterial) {
        this.isMaterial = isMaterial;
    }

    public Integer getMaterialType() {
        return materialType;
    }

    public void setMaterialType(Integer materialType) {
        this.materialType = materialType;
    }
}
