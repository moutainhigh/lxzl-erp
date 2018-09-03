package com.lxzl.erp.dataaccess.domain.order;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.data.annotation.Transient;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderProductDO extends BaseDO {
    private Integer id;
    private Integer orderId;
    private Integer rentType;
    private Integer rentTimeLength;
    private Integer rentLengthType;
    private Integer productId;
    private String productName;
    private Integer productSkuId;
    private String productSkuName;
    private Integer productCount;
    private Integer stableProductCount;               // 下单商品总数，该字段只在订单未提交时可变化
    private BigDecimal productUnitAmount;
    private BigDecimal productAmount;
    private BigDecimal rentDepositAmount;
    private BigDecimal depositAmount;
    private BigDecimal creditDepositAmount;
    private BigDecimal insuranceAmount;
    private String productSkuSnapshot;
    private Integer depositCycle;
    private Integer paymentCycle;
    private Integer payMode;
    private Integer dataStatus;
    private String remark;
    private Integer isNewProduct;
    private Integer rentingProductCount;        // 在租商品总数
    private Integer isItemDelivered;    //是否已发货，0否1是


    private String serialNumber;        // 序号

    private Integer orderJointProductId; // 订单组合商品id
    private Integer jointProductProductId; // 关联的组合商品商品项id
    @Transient
    private Integer identityNo; // 标识号，只在业务逻辑处理时使用

    // 以下为K3的数据字段
    private Integer FEntryID;
    private String productNumber;

    public Integer getJointProductProductId() {
        return jointProductProductId;
    }

    public void setJointProductProductId(Integer jointProductProductId) {
        this.jointProductProductId = jointProductProductId;
    }

    public Integer getIdentityNo() {
        return identityNo;
    }

    public void setIdentityNo(Integer identityNo) {
        this.identityNo = identityNo;
    }

    public Integer getOrderJointProductId() {
        return orderJointProductId;
    }

    public void setOrderJointProductId(Integer orderJointProductId) {
        this.orderJointProductId = orderJointProductId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getProductSkuId() {
        return productSkuId;
    }

    public void setProductSkuId(Integer productSkuId) {
        this.productSkuId = productSkuId;
    }

    public String getProductSkuName() {
        return productSkuName;
    }

    public void setProductSkuName(String productSkuName) {
        this.productSkuName = productSkuName;
    }

    public Integer getProductCount() {
        return productCount;
    }

    public void setProductCount(Integer productCount) {
        this.productCount = productCount;
    }

    public BigDecimal getProductUnitAmount() {
        return productUnitAmount;
    }

    public void setProductUnitAmount(BigDecimal productUnitAmount) {
        this.productUnitAmount = productUnitAmount;
    }

    public BigDecimal getProductAmount() {
        return productAmount;
    }

    public void setProductAmount(BigDecimal productAmount) {
        this.productAmount = productAmount;
    }

    public String getProductSkuSnapshot() {
        return productSkuSnapshot;
    }

    public void setProductSkuSnapshot(String productSkuSnapshot) {
        this.productSkuSnapshot = productSkuSnapshot;
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

    public BigDecimal getDepositAmount() {
        return depositAmount;
    }

    public void setDepositAmount(BigDecimal depositAmount) {
        this.depositAmount = depositAmount;
    }

    public BigDecimal getCreditDepositAmount() {
        return creditDepositAmount;
    }

    public void setCreditDepositAmount(BigDecimal creditDepositAmount) {
        this.creditDepositAmount = creditDepositAmount;
    }

    public BigDecimal getInsuranceAmount() {
        return insuranceAmount;
    }

    public void setInsuranceAmount(BigDecimal insuranceAmount) {
        this.insuranceAmount = insuranceAmount;
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

    public Integer getDepositCycle() {
        return depositCycle;
    }

    public void setDepositCycle(Integer depositCycle) {
        this.depositCycle = depositCycle;
    }

    public Integer getPaymentCycle() {
        return paymentCycle;
    }

    public void setPaymentCycle(Integer paymentCycle) {
        this.paymentCycle = paymentCycle;
    }

    public Integer getPayMode() {
        return payMode;
    }

    public void setPayMode(Integer payMode) {
        this.payMode = payMode;
    }

    public Integer getIsNewProduct() {
        return isNewProduct;
    }

    public void setIsNewProduct(Integer isNewProduct) {
        this.isNewProduct = isNewProduct;
    }

    public BigDecimal getRentDepositAmount() {
        return rentDepositAmount;
    }

    public void setRentDepositAmount(BigDecimal rentDepositAmount) {
        this.rentDepositAmount = rentDepositAmount;
    }

    public Integer getRentLengthType() {
        return rentLengthType;
    }

    public void setRentLengthType(Integer rentLengthType) {
        this.rentLengthType = rentLengthType;
    }

    public Integer getRentingProductCount() {
        return rentingProductCount;
    }

    public void setRentingProductCount(Integer rentingProductCount) {
        this.rentingProductCount = rentingProductCount;
    }

    public Integer getFEntryID() {
        return FEntryID;
    }

    public void setFEntryID(Integer FEntryID) {
        this.FEntryID = FEntryID;
    }

    public String getProductNumber() {
        return productNumber;
    }

    public void setProductNumber(String productNumber) {
        this.productNumber = productNumber;
    }

    public String getSerialNumber() { return serialNumber; }

    public void setSerialNumber(String serialNumber) { this.serialNumber = serialNumber; }

    public Integer getStableProductCount() {
        return stableProductCount;
    }

    public void setStableProductCount(Integer stableProductCount) {
        this.stableProductCount = stableProductCount;
    }

    public Integer getIsItemDelivered() {
        return isItemDelivered;
    }

    public void setIsItemDelivered(Integer isItemDelivered) {
        this.isItemDelivered = isItemDelivered;
    }

}
