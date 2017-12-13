package com.lxzl.erp.common.domain.changeOrder.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.validGroup.IdGroup;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ChangeOrder implements Serializable {

    private Integer changeOrderId;   //唯一标识
    @NotBlank(message = ErrorCode.CHANGE_ORDER_NO_NOT_NULL,groups = {IdGroup.class})
    private String changeOrderNo;   //换货编号
    private Integer customerId;   //客户ID
    private String customerNo;   //客户编号
    private Date rentStartTime; //起租时间
    private Integer totalChangeProductCount;   //换货商品总数
    private Integer totalChangeMaterialCount;   //换货物料总数
    private Integer realTotalChangeProductCount;   //实际换货商品总数
    private Integer realTotalChangeMaterialCount;   //实际换货物料总数
    private BigDecimal totalPriceDiff;   //总差价（换货后总价格-换货前总价格）
    private BigDecimal serviceCost;   //服务费
    private BigDecimal damageCost;   //损坏加收费用
    private Integer isDamage;   //是否有损坏，0否1是
    private Integer changeReasonType;   //换货原因类型,0-升级 ，1-损坏，2-其他
    private String changeReason;   //换货原因
    private Integer changeMode;   //换货方式，1-上门取件，2邮寄
    private Integer changeOrderStatus;   //换货订单状态，1-待提交，5-审核中，9-待备货，13-备货中，17-已发货待取货，21-处理中，25-已完成
    private Integer dataStatus;   //状态：0不可用；1可用；2删除
    private String remark;   //备注
    private Integer owner;   //换货跟单员
    private Date createTime;   //添加时间
    private String createUser;   //添加人
    private Date updateTime;   //添加时间
    private String updateUser;   //修改人

    private String ownerName;
    private ChangeOrderConsignInfo changeOrderConsignInfo;
    private List<ChangeOrderProduct> changeOrderProductList;
    private List<ChangeOrderMaterial> changeOrderMaterialList;

    public Integer getChangeOrderId() {
        return changeOrderId;
    }

    public void setChangeOrderId(Integer changeOrderId) {
        this.changeOrderId = changeOrderId;
    }

    public String getChangeOrderNo() {
        return changeOrderNo;
    }

    public void setChangeOrderNo(String changeOrderNo) {
        this.changeOrderNo = changeOrderNo;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public Date getRentStartTime() {
        return rentStartTime;
    }

    public void setRentStartTime(Date rentStartTime) {
        this.rentStartTime = rentStartTime;
    }

    public Integer getTotalChangeProductCount() {
        return totalChangeProductCount;
    }

    public void setTotalChangeProductCount(Integer totalChangeProductCount) {
        this.totalChangeProductCount = totalChangeProductCount;
    }

    public Integer getTotalChangeMaterialCount() {
        return totalChangeMaterialCount;
    }

    public void setTotalChangeMaterialCount(Integer totalChangeMaterialCount) {
        this.totalChangeMaterialCount = totalChangeMaterialCount;
    }

    public Integer getRealTotalChangeProductCount() {
        return realTotalChangeProductCount;
    }

    public void setRealTotalChangeProductCount(Integer realTotalChangeProductCount) {
        this.realTotalChangeProductCount = realTotalChangeProductCount;
    }

    public Integer getRealTotalChangeMaterialCount() {
        return realTotalChangeMaterialCount;
    }

    public void setRealTotalChangeMaterialCount(Integer realTotalChangeMaterialCount) {
        this.realTotalChangeMaterialCount = realTotalChangeMaterialCount;
    }

    public BigDecimal getTotalPriceDiff() {
        return totalPriceDiff;
    }

    public void setTotalPriceDiff(BigDecimal totalPriceDiff) {
        this.totalPriceDiff = totalPriceDiff;
    }

    public BigDecimal getServiceCost() {
        return serviceCost;
    }

    public void setServiceCost(BigDecimal serviceCost) {
        this.serviceCost = serviceCost;
    }

    public BigDecimal getDamageCost() {
        return damageCost;
    }

    public void setDamageCost(BigDecimal damageCost) {
        this.damageCost = damageCost;
    }

    public Integer getIsDamage() {
        return isDamage;
    }

    public void setIsDamage(Integer isDamage) {
        this.isDamage = isDamage;
    }

    public Integer getChangeReasonType() {
        return changeReasonType;
    }

    public void setChangeReasonType(Integer changeReasonType) {
        this.changeReasonType = changeReasonType;
    }

    public String getChangeReason() {
        return changeReason;
    }

    public void setChangeReason(String changeReason) {
        this.changeReason = changeReason;
    }

    public Integer getChangeMode() {
        return changeMode;
    }

    public void setChangeMode(Integer changeMode) {
        this.changeMode = changeMode;
    }

    public Integer getChangeOrderStatus() {
        return changeOrderStatus;
    }

    public void setChangeOrderStatus(Integer changeOrderStatus) {
        this.changeOrderStatus = changeOrderStatus;
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

    public Integer getOwner() {
        return owner;
    }

    public void setOwner(Integer owner) {
        this.owner = owner;
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

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public ChangeOrderConsignInfo getChangeOrderConsignInfo() {
        return changeOrderConsignInfo;
    }

    public void setChangeOrderConsignInfo(ChangeOrderConsignInfo changeOrderConsignInfo) {
        this.changeOrderConsignInfo = changeOrderConsignInfo;
    }

    public List<ChangeOrderProduct> getChangeOrderProductList() {
        return changeOrderProductList;
    }

    public void setChangeOrderProductList(List<ChangeOrderProduct> changeOrderProductList) {
        this.changeOrderProductList = changeOrderProductList;
    }

    public List<ChangeOrderMaterial> getChangeOrderMaterialList() {
        return changeOrderMaterialList;
    }

    public void setChangeOrderMaterialList(List<ChangeOrderMaterial> changeOrderMaterialList) {
        this.changeOrderMaterialList = changeOrderMaterialList;
    }
}