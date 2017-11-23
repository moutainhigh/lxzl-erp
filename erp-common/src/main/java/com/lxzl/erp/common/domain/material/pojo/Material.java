package com.lxzl.erp.common.domain.material.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-10-30 19:57
 */
public class Material implements Serializable {

    private Integer materialId;   //物料ID
    private String materialNo;   //物料唯一编号
    private String materialName;   //物料名称，取属性与属性值全称
    private Integer materialType;   //物料类型
    private Integer brandId;   //所属品牌ID
    private BigDecimal materialPrice;   //物料本身的价值(单价)
    private Double materialCapacityValue;   //物料字面量
    private Integer materialModelId;    // 物料型号ID
    private Integer isMainMaterial;         //是否四大件
    private Integer stock;              // 库存
    private BigDecimal timeRentPrice;   // 次租价格
    private BigDecimal dayRentPrice;    // 日租价格
    private BigDecimal monthRentPrice;  // 月租价格
    private String materialDesc;   //物料描述
    private Integer dataStatus;   //状态：0不可用；1可用；2删除
    private String remark;   //备注
    private Date createTime;   //添加时间
    private String createUser;   //添加人
    private Date updateTime;   //修改时间
    private String updateUser;   //修改人

    private List<MaterialImg> materialImgList;

    public Integer getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Integer materialId) {
        this.materialId = materialId;
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

    public Double getMaterialCapacityValue() {
        return materialCapacityValue;
    }

    public void setMaterialCapacityValue(Double materialCapacityValue) {
        this.materialCapacityValue = materialCapacityValue;
    }

    public Integer getIsMainMaterial() {
        return isMainMaterial;
    }

    public void setIsMainMaterial(Integer isMainMaterial) {
        this.isMainMaterial = isMainMaterial;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public BigDecimal getTimeRentPrice() {
        return timeRentPrice;
    }

    public void setTimeRentPrice(BigDecimal timeRentPrice) {
        this.timeRentPrice = timeRentPrice;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public List<MaterialImg> getMaterialImgList() {
        return materialImgList;
    }

    public void setMaterialImgList(List<MaterialImg> materialImgList) {
        this.materialImgList = materialImgList;
    }

    public Integer getMaterialModelId() {
        return materialModelId;
    }

    public void setMaterialModelId(Integer materialModelId) {
        this.materialModelId = materialModelId;
    }
}
