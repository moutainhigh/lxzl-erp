package com.lxzl.erp.dataaccess.domain.material;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import org.springframework.data.annotation.Transient;

import java.math.BigDecimal;
import java.util.List;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-10-30 19:57
 */
public class MaterialDO extends BaseDO {
    private Integer id;
    private String materialNo;
    private String materialName;
    private Integer materialType;
    private Integer isMainMaterial;
    private Double materialCapacityValue;
    private Integer materialModelId;
    private Integer brandId;
    private String brandName;
    private Integer isRent;
    private BigDecimal materialPrice;
    private Integer stock;
    private BigDecimal dayRentPrice;
    private BigDecimal monthRentPrice;
    private Integer isConsumable;
    private String materialDesc;
    private Integer dataStatus;
    private String remark;
    private List<MaterialImgDO> materialImgDOList;
    @Transient
    private Integer rentCount;
    @Transient
    private Integer returnCount;
    @Transient
    private Integer canProcessCount;
    @Transient
    private String materialModelName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMaterialNo() {
        return materialNo;
    }

    public void setMaterialNo(String materialNo) {
        this.materialNo = materialNo;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public Integer getMaterialType() {
        return materialType;
    }

    public void setMaterialType(Integer materialType) {
        this.materialType = materialType;
    }

    public Integer getIsMainMaterial() {
        return isMainMaterial;
    }

    public void setIsMainMaterial(Integer isMainMaterial) {
        this.isMainMaterial = isMainMaterial;
    }

    public Double getMaterialCapacityValue() {
        return materialCapacityValue;
    }

    public void setMaterialCapacityValue(Double materialCapacityValue) {
        this.materialCapacityValue = materialCapacityValue;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public BigDecimal getMaterialPrice() {
        return materialPrice;
    }

    public void setMaterialPrice(BigDecimal materialPrice) {
        this.materialPrice = materialPrice;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public BigDecimal getDayRentPrice() {
        return dayRentPrice;
    }

    public void setDayRentPrice(BigDecimal dayRentPrice) {
        this.dayRentPrice = dayRentPrice;
    }

    public BigDecimal getMonthRentPrice() {
        return monthRentPrice;
    }

    public void setMonthRentPrice(BigDecimal monthRentPrice) {
        this.monthRentPrice = monthRentPrice;
    }

    public String getMaterialDesc() {
        return materialDesc;
    }

    public void setMaterialDesc(String materialDesc) {
        this.materialDesc = materialDesc;
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

    public List<MaterialImgDO> getMaterialImgDOList() {
        return materialImgDOList;
    }

    public void setMaterialImgDOList(List<MaterialImgDO> materialImgDOList) {
        this.materialImgDOList = materialImgDOList;
    }

    public Integer getMaterialModelId() {
        return materialModelId;
    }

    public void setMaterialModelId(Integer materialModelId) {
        this.materialModelId = materialModelId;
    }

    public Integer getIsRent() {
        return isRent;
    }

    public void setIsRent(Integer isRent) {
        this.isRent = isRent;
    }

    public Integer getRentCount() {
        return rentCount;
    }

    public void setRentCount(Integer rentCount) {
        this.rentCount = rentCount;
    }

    public Integer getReturnCount() {
        return returnCount;
    }

    public void setReturnCount(Integer returnCount) {
        this.returnCount = returnCount;
    }

    public Integer getCanProcessCount() {
        return canProcessCount;
    }

    public void setCanProcessCount(Integer canProcessCount) {
        this.canProcessCount = canProcessCount;
    }

    public String getMaterialModelName() {
        return materialModelName;
    }

    public void setMaterialModelName(String materialModelName) {
        this.materialModelName = materialModelName;
    }

    public Integer getIsConsumable() {
        return isConsumable;
    }

    public void setIsConsumable(Integer isConsumable) {
        this.isConsumable = isConsumable;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }
}
