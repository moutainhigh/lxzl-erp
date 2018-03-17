package com.lxzl.erp.common.domain.order.pojo;

import com.lxzl.erp.common.domain.base.BasePO;
import com.lxzl.erp.common.domain.product.pojo.ProductSkuProperty;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderProduct extends BasePO {
    private Integer orderProductId;             // 订单商品项ID
    private Integer orderId;                    // 订单ID
    private Integer rentType;                   // 租赁类型，1按天，2按月
    private Integer rentTimeLength;             // 租赁时长
    private Integer rentLengthType;             // 1短租，2长租
    private Integer productId;                  // 商品ID
    private String productName;                 // 商品名称
    private Integer productSkuId;               // 商品配置ID
    private String productSkuName;              // 配置名称
    private Integer productCount;               // 商品数量
    private BigDecimal productUnitAmount;       // 单价
    private BigDecimal productAmount;           // 总价
    private BigDecimal rentDepositAmount;       // 租金押金
    private BigDecimal depositAmount;           // 设备押金
    private BigDecimal creditDepositAmount;     // 授信额度
    private BigDecimal insuranceAmount;
    private String productSkuSnapshot;          // 商品快照
    private Integer dataStatus;
    private String remark;                      // 备注信息
    private List<ProductSkuProperty> productSkuPropertyList;
    private Integer depositCycle;               // 押金期数
    private Integer paymentCycle;               // 租金期数
    private Integer payMode;                    // 付款方式，1先付后用，2先用后付
    private Integer isNewProduct;               // 是否全新，1是0否
    private List<OrderProductEquipment> orderProductEquipmentList;

    private BigDecimal firstNeedPayAmount;      // 首付金额
    private BigDecimal firstNeedPayRentAmount;      // 首付租金金额
    private BigDecimal firstNeedPayDepositAmount;      // 首付押金金额

    public Integer getOrderProductId() {
        return orderProductId;
    }

    public void setOrderProductId(Integer orderProductId) {
        this.orderProductId = orderProductId;
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

    public List<ProductSkuProperty> getProductSkuPropertyList() {
        return productSkuPropertyList;
    }

    public void setProductSkuPropertyList(List<ProductSkuProperty> productSkuPropertyList) {
        this.productSkuPropertyList = productSkuPropertyList;
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

    public Integer getIsNewProduct() {
        return isNewProduct;
    }

    public void setIsNewProduct(Integer isNewProduct) {
        this.isNewProduct = isNewProduct;
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

    public List<OrderProductEquipment> getOrderProductEquipmentList() {
        return orderProductEquipmentList;
    }

    public void setOrderProductEquipmentList(List<OrderProductEquipment> orderProductEquipmentList) {
        this.orderProductEquipmentList = orderProductEquipmentList;
    }

    public BigDecimal getRentDepositAmount() {
        return rentDepositAmount;
    }

    public void setRentDepositAmount(BigDecimal rentDepositAmount) {
        this.rentDepositAmount = rentDepositAmount;
    }

    public BigDecimal getFirstNeedPayAmount() {
        return firstNeedPayAmount;
    }

    public void setFirstNeedPayAmount(BigDecimal firstNeedPayAmount) {
        this.firstNeedPayAmount = firstNeedPayAmount;
    }

    public Integer getRentLengthType() {
        return rentLengthType;
    }

    public void setRentLengthType(Integer rentLengthType) {
        this.rentLengthType = rentLengthType;
    }

    public BigDecimal getFirstNeedPayRentAmount() {
        return firstNeedPayRentAmount;
    }

    public void setFirstNeedPayRentAmount(BigDecimal firstNeedPayRentAmount) { this.firstNeedPayRentAmount = firstNeedPayRentAmount; }

    public BigDecimal getFirstNeedPayDepositAmount() { return firstNeedPayDepositAmount; }

    public void setFirstNeedPayDepositAmount(BigDecimal firstNeedPayDepositAmount) { this.firstNeedPayDepositAmount = firstNeedPayDepositAmount; }
}
