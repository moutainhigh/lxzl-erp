package com.lxzl.erp.common.domain.product.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.base.BasePO;
import com.lxzl.erp.common.domain.validGroup.AddGroup;
import com.lxzl.erp.common.domain.validGroup.CancelGroup;
import com.lxzl.erp.common.domain.validGroup.UpdateGroup;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductCategoryProperty extends BasePO {

    @NotNull(message = ErrorCode.CATEGORY_PROPERTY_ID_NOT_NULL,groups={CancelGroup.class,UpdateGroup.class})
    private Integer categoryPropertyId;
    @NotBlank(message = ErrorCode.PROPERTY_NAME_NOT_NULL,groups={AddGroup.class})
    private String propertyName;
    @NotNull(message = ErrorCode.CATEGORY_ID_NOT_NULL,groups={AddGroup.class})
    private Integer categoryId;
    @NotNull(message = ErrorCode.PROPERTY_TYPE_NOT_NULL,groups={AddGroup.class})
    private Integer propertyType;
    private Integer materialType;   //必须的，如果为空，请传空
    private Integer isInput;
    private Integer isCheckbox;
    private Integer isRequired;
    private Integer dataOrder;
    private Integer dataStatus;
    private String remark;
    private String createUser;
    private Date createTime;
    private String updateUser;
    private Date updateTime;

    @Valid
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

    public Integer getMaterialType() {
        return materialType;
    }

    public void setMaterialType(Integer materialType) {
        this.materialType = materialType;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
