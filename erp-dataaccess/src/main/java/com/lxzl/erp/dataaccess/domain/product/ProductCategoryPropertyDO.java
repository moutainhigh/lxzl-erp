package com.lxzl.erp.dataaccess.domain.product;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductCategoryPropertyDO extends BaseDO {

    private Integer id;
    private String propertyName;
    private Integer categoryId;
    private Integer propertyType;
    private Integer isMaterial;
    private Integer isInput;
    private Integer isCheckbox;
    private Integer isRequired;
    private Integer dataOrder;
    private Integer dataStatus;
    private String remark;
    private List<ProductCategoryPropertyValueDO> productCategoryPropertyValueDOList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public List<ProductCategoryPropertyValueDO> getProductCategoryPropertyValueDOList() {
        return productCategoryPropertyValueDOList;
    }

    public void setProductCategoryPropertyValueDOList(List<ProductCategoryPropertyValueDO> productCategoryPropertyValueDOList) {
        this.productCategoryPropertyValueDOList = productCategoryPropertyValueDOList;
    }

    public Integer getIsMaterial() {
        return isMaterial;
    }

    public void setIsMaterial(Integer isMaterial) {
        this.isMaterial = isMaterial;
    }
}
