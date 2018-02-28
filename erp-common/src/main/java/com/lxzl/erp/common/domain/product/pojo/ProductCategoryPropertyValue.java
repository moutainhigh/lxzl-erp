package com.lxzl.erp.common.domain.product.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.base.BasePO;
import com.lxzl.erp.common.domain.validGroup.AddGroup;
import com.lxzl.erp.common.domain.validGroup.CancelGroup;
import com.lxzl.erp.common.domain.validGroup.UpdateGroup;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductCategoryPropertyValue extends BasePO {

    @NotNull(message = ErrorCode.CATEGORY_PROPERTY_VALUE_ID_NOT_NULL,groups={CancelGroup.class,UpdateGroup.class})
    private Integer categoryPropertyValueId;
    @NotBlank(message = ErrorCode.PROPERTY_VALUE_NAME_NOT_NULL,groups={AddGroup.class,UpdateGroup.class})
    private String propertyValueName;
    private Integer propertyId;
    @NotNull(message = ErrorCode.CATEGORY_ID_NOT_NULL,groups={AddGroup.class})
    private Integer categoryId;
    private Integer referId;
    private Double propertyCapacityValue;   //如果是内存和硬盘，请填写值
    private Integer materialModelId;       //如果不是内存和硬盘，请填写值
    private Integer dataOrder;
    private Integer dataStatus;
    private String remark;
    private String createUser;
    private Date createTime;
    private String updateUser;
    private Date updateTime;

    private String propertyName; //分类名称
    private Integer materialType; //物料类型

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
