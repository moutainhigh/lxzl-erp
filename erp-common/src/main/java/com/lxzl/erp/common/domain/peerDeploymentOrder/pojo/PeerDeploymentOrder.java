package com.lxzl.erp.common.domain.peerDeploymentOrder.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.base.BasePO;
import com.lxzl.erp.common.domain.validGroup.AddGroup;
import com.lxzl.erp.common.domain.validGroup.IdGroup;
import com.lxzl.erp.common.domain.validGroup.UpdateGroup;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PeerDeploymentOrder extends BasePO {

    private Integer peerDeploymentOrderId;   //唯一标识
    @NotBlank(message = ErrorCode.PEER_DEPLOYMENT_ORDER_NO_NOT_NULL, groups = {IdGroup.class})
    private String peerDeploymentOrderNo;   //同行调配单编号
    @NotNull(message = ErrorCode.PEER_ID_NOT_NULL, groups = {AddGroup.class, UpdateGroup.class})
    private Integer peerId;   //同行ID
    @NotNull(message = ErrorCode.PEER_DEPLOYMENT_ORDER_START_TIME_NOT_NULL ,groups = {AddGroup.class,UpdateGroup.class})
    private Date rentStartTime;   //起租时间
    @NotNull(message = ErrorCode.PEER_DEPLOYMENT_ORDER_RENT_TYPE_NOT_NULL ,groups = {AddGroup.class,UpdateGroup.class})
    private Integer rentType;   //租赁方式，1按天租，2按月租
    @NotNull(message = ErrorCode.PEER_DEPLOYMENT_ORDER_TOTAL_DISCOUNT_AMOUNT_NOT_NULL, groups = {AddGroup.class, UpdateGroup.class})
    private Integer rentTimeLength;   //租赁期限
    @NotNull(message = ErrorCode.WAREHOUSE_ID_NOT_NULL, groups = {AddGroup.class, UpdateGroup.class})
    private Integer warehouseId;   //目标仓库ID
    private Integer warehousePositionId;   //目标仓位ID
    @NotNull(message = ErrorCode.WAREHOUSE_ID_NOT_NULL, groups = {AddGroup.class, UpdateGroup.class})
    private Integer deliveryMode;   //发货方式，1快递，2自提
    @Min(value = 0, message = ErrorCode.PEER_DEPLOYMENT_ORDER_TAX_RATE_ERROR, groups = {AddGroup.class, UpdateGroup.class})
    @Max(value = 1, message = ErrorCode.PEER_DEPLOYMENT_ORDER_TAX_RATE_ERROR, groups = {AddGroup.class, UpdateGroup.class})
    private Double taxRate;   //税率
    private Integer peerDeploymentOrderStatus;   //调配单状态，0未提交，4审批中，8处理中，12确认收货，16退回审批中，20退回处理中，24已退回，28取消
    private Integer totalProductCount;   //商品总数
    private BigDecimal totalProductAmount;   //商品总价
    private Integer totalMaterialCount;   //配件总数
    private BigDecimal totalMaterialAmount;   //配件总价
    private BigDecimal totalOrderAmount;   //订单总价
    private BigDecimal totalDiscountAmount;   //共计优惠金额
    private Date expectReturnTime;   //预计归还时间
    private Date realReturnTime;   //实际归还时间
    private Integer dataStatus;   //状态：0不可用；1可用；2删除
    private String remark;   //备注
    private Date createTime;   //添加时间
    private String createUser;   //添加人
    private Date updateTime;   //修改时间
    private String updateUser;   //修改人
    private Date confirmTime;   //确认收货时间

    @Valid
    private List<PeerDeploymentOrderProduct> peerDeploymentOrderProductList;
    @Valid
    private List<PeerDeploymentOrderMaterial> peerDeploymentOrderMaterialList;
    @Valid
    private PeerDeploymentOrderConsignInfo peerDeploymentOrderConsignInfo;

    // 审核人和提交审核信息,只提供给审核的时候用
    private Integer verifyUser;
    private String commitRemark;

    private String peerName;
    private String warehouseName;


    public Integer getPeerDeploymentOrderId() {
        return peerDeploymentOrderId;
    }

    public void setPeerDeploymentOrderId(Integer peerDeploymentOrderId) { this.peerDeploymentOrderId = peerDeploymentOrderId; }

    public String getPeerDeploymentOrderNo() {
        return peerDeploymentOrderNo;
    }

    public void setPeerDeploymentOrderNo(String peerDeploymentOrderNo) { this.peerDeploymentOrderNo = peerDeploymentOrderNo; }

    public Integer getPeerId() {
        return peerId;
    }

    public void setPeerId(Integer peerId) {
        this.peerId = peerId;
    }

    public Date getRentStartTime() {
        return rentStartTime;
    }

    public void setRentStartTime(Date rentStartTime) {
        this.rentStartTime = rentStartTime;
    }

    public Integer getRentType() {
        return rentType;
    }

    public void setRentType(Integer rentType) {
        this.rentType = rentType;
    }

    public Integer getRentTimeLength() {
        return rentTimeLength;
    }

    public void setRentTimeLength(Integer rentTimeLength) {
        this.rentTimeLength = rentTimeLength;
    }

    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }

    public Integer getWarehousePositionId() {
        return warehousePositionId;
    }

    public void setWarehousePositionId(Integer warehousePositionId) {
        this.warehousePositionId = warehousePositionId;
    }

    public Integer getDeliveryMode() {
        return deliveryMode;
    }

    public void setDeliveryMode(Integer deliveryMode) {
        this.deliveryMode = deliveryMode;
    }

    public Double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(Double taxRate) {
        this.taxRate = taxRate;
    }

    public Integer getPeerDeploymentOrderStatus() {
        return peerDeploymentOrderStatus;
    }

    public void setPeerDeploymentOrderStatus(Integer peerDeploymentOrderStatus) { this.peerDeploymentOrderStatus = peerDeploymentOrderStatus; }

    public Integer getTotalProductCount() {
        return totalProductCount;
    }

    public void setTotalProductCount(Integer totalProductCount) {
        this.totalProductCount = totalProductCount;
    }

    public BigDecimal getTotalProductAmount() {
        return totalProductAmount;
    }

    public void setTotalProductAmount(BigDecimal totalProductAmount) {
        this.totalProductAmount = totalProductAmount;
    }

    public Integer getTotalMaterialCount() {
        return totalMaterialCount;
    }

    public void setTotalMaterialCount(Integer totalMaterialCount) {
        this.totalMaterialCount = totalMaterialCount;
    }

    public BigDecimal getTotalMaterialAmount() {
        return totalMaterialAmount;
    }

    public void setTotalMaterialAmount(BigDecimal totalMaterialAmount) { this.totalMaterialAmount = totalMaterialAmount; }

    public BigDecimal getTotalOrderAmount() {
        return totalOrderAmount;
    }

    public void setTotalOrderAmount(BigDecimal totalOrderAmount) {
        this.totalOrderAmount = totalOrderAmount;
    }

    public BigDecimal getTotalDiscountAmount() {
        return totalDiscountAmount;
    }

    public void setTotalDiscountAmount(BigDecimal totalDiscountAmount) { this.totalDiscountAmount = totalDiscountAmount; }

    public Date getExpectReturnTime() {
        return expectReturnTime;
    }

    public void setExpectReturnTime(Date expectReturnTime) {
        this.expectReturnTime = expectReturnTime;
    }

    public Date getRealReturnTime() {
        return realReturnTime;
    }

    public void setRealReturnTime(Date realReturnTime) {
        this.realReturnTime = realReturnTime;
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

    public List<PeerDeploymentOrderProduct> getPeerDeploymentOrderProductList() { return peerDeploymentOrderProductList; }

    public void setPeerDeploymentOrderProductList(List<PeerDeploymentOrderProduct> peerDeploymentOrderProductList) { this.peerDeploymentOrderProductList = peerDeploymentOrderProductList; }

    public List<PeerDeploymentOrderMaterial> getPeerDeploymentOrderMaterialList() { return peerDeploymentOrderMaterialList; }

    public void setPeerDeploymentOrderMaterialList(List<PeerDeploymentOrderMaterial> peerDeploymentOrderMaterialList) { this.peerDeploymentOrderMaterialList = peerDeploymentOrderMaterialList; }

    public PeerDeploymentOrderConsignInfo getPeerDeploymentOrderConsignInfo() {
        return peerDeploymentOrderConsignInfo;
    }

    public void setPeerDeploymentOrderConsignInfo(PeerDeploymentOrderConsignInfo peerDeploymentOrderConsignInfo) { this.peerDeploymentOrderConsignInfo = peerDeploymentOrderConsignInfo; }

    public Integer getVerifyUser() {
        return verifyUser;
    }

    public void setVerifyUser(Integer verifyUser) {
        this.verifyUser = verifyUser;
    }

    public String getCommitRemark() {
        return commitRemark;
    }

    public void setCommitRemark(String commitRemark) {
        this.commitRemark = commitRemark;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public String getPeerName() {
        return peerName;
    }

    public void setPeerName(String peerName) {
        this.peerName = peerName;
    }

    public Date getConfirmTime() { return confirmTime; }

    public void setConfirmTime(Date confirmTime) { this.confirmTime = confirmTime; }
}