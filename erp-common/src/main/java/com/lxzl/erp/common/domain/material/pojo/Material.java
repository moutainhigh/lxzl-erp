package com.lxzl.erp.common.domain.material.pojo;

import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.base.BasePO;
import com.lxzl.erp.common.domain.changeOrder.AddChangeOrderParam;
import com.lxzl.erp.common.domain.validGroup.returnOrder.AddReturnOrderGroup;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-10-30 19:57
 */
public class Material extends BasePO {

    private Integer materialId;   //物料ID
    @NotNull(message = ErrorCode.MATERIAL_NO_NOT_NULL,groups = {AddReturnOrderGroup.class, AddChangeOrderParam.class})
    private String materialNo;   //物料唯一编号
    private String k3MaterialNo;   //K3物料编号
    private String materialName;   //物料名称，取属性与属性值全称
    private String materialModel;   // 物料型号
    private Integer materialType;   //物料类型
    private Integer brandId;   //所属品牌ID
    private String brandName;   //所属品牌名称
    private BigDecimal materialPrice;   //物料本身的价值(单价)
    private Double materialCapacityValue;   //物料字面量
    private Integer materialModelId;    // 物料型号ID
    private Integer isMainMaterial;         //是否四大件
    private Integer isRent;             // 是否在租
    private Integer stock;              // 库存
    private BigDecimal dayRentPrice;    // 日租价格
    private BigDecimal monthRentPrice;  // 月租价格
    private BigDecimal newMaterialPrice;  //全新配件本身的价值(单价)
    private BigDecimal newDayRentPrice;  //全新天租赁价格
    private BigDecimal newMonthRentPrice;  //全新月租赁价格
    private Integer isConsumable;       // 是否是消耗品
    private String materialDesc;   //物料描述
    private Integer dataStatus;   //状态：0不可用；1可用；2删除
    private String remark;   //备注
    private Date createTime;   //添加时间
    private String createUser;   //添加人
    private Date updateTime;   //修改时间
    private String updateUser;   //修改人
    private Integer isReturnAnyTime;    //是否允许随时归还，0否1是

    private List<MaterialImg> materialImgList;

    private Integer rentCount;
    @NotNull(message = ErrorCode.RETURN_COUNT_ERROR,groups = {AddReturnOrderGroup.class, AddChangeOrderParam.class})
    @Min(value = 1 , message = ErrorCode.RETURN_COUNT_ERROR,groups = {AddReturnOrderGroup.class, AddChangeOrderParam.class})
    private Integer returnCount;
    private Integer canProcessCount;
    private String materialModelName;
    private String materialTypeName;

    private Integer newMaterialCount;
    private Integer oldMaterialCount;

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

    public Integer getNewMaterialCount() {
        return newMaterialCount;
    }

    public void setNewMaterialCount(Integer newMaterialCount) {
        this.newMaterialCount = newMaterialCount;
    }

    public Integer getOldMaterialCount() {
        return oldMaterialCount;
    }

    public void setOldMaterialCount(Integer oldMaterialCount) {
        this.oldMaterialCount = oldMaterialCount;
    }

    public String getMaterialModel() {
        return materialModel;
    }

    public void setMaterialModel(String materialModel) {
        this.materialModel = materialModel;
    }

    public BigDecimal getNewMaterialPrice() {
        return newMaterialPrice;
    }

    public void setNewMaterialPrice(BigDecimal newMaterialPrice) {
        this.newMaterialPrice = newMaterialPrice;
    }

    public BigDecimal getNewDayRentPrice() {
        return newDayRentPrice;
    }

    public void setNewDayRentPrice(BigDecimal newDayRentPrice) {
        this.newDayRentPrice = newDayRentPrice;
    }

    public BigDecimal getNewMonthRentPrice() {
        return newMonthRentPrice;
    }

    public void setNewMonthRentPrice(BigDecimal newMonthRentPrice) {
        this.newMonthRentPrice = newMonthRentPrice;
    }

    public String getMaterialTypeName() {
        return materialTypeName;
    }

    public void setMaterialTypeName(String materialTypeName) {
        this.materialTypeName = materialTypeName;
    }

    public Integer getIsReturnAnyTime() { return isReturnAnyTime; }

    public void setIsReturnAnyTime(Integer isReturnAnyTime) { this.isReturnAnyTime = isReturnAnyTime; }

    public String getK3MaterialNo() { return k3MaterialNo; }

    public void setK3MaterialNo(String k3MaterialNo) { this.k3MaterialNo = k3MaterialNo; }
}
