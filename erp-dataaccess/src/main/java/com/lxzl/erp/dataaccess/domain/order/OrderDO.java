package com.lxzl.erp.dataaccess.domain.order;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.data.annotation.Transient;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderDO extends BaseDO {

    private Integer id;
    private String orderNo;
    private Integer deliveryMode;
    private Integer buyerCustomerId;
    private Date expectDeliveryTime;
    private Date rentStartTime;
    private Integer rentType;                   // 租赁类型，1按天，2按月
    private Integer rentTimeLength;             // 租赁时长
    private Integer rentLengthType;             // 1短租，2长租
    private BigDecimal totalDepositAmount;
    private BigDecimal totalMustDepositAmount;
    private BigDecimal totalCreditDepositAmount;
    private BigDecimal totalRentDepositAmount;
    private BigDecimal totalInsuranceAmount;
    private Integer totalProductCount;
    private BigDecimal totalProductAmount;
    private BigDecimal totalProductDepositAmount;
    private BigDecimal totalProductCreditDepositAmount;
    private BigDecimal totalProductRentDepositAmount;
    private Integer totalMaterialCount;
    private BigDecimal totalMaterialAmount;
    private BigDecimal totalMaterialDepositAmount;
    private BigDecimal totalMaterialCreditDepositAmount;
    private BigDecimal totalMaterialRentDepositAmount;
    private BigDecimal totalOrderAmount;
    private BigDecimal totalPaidOrderAmount;
    private BigDecimal totalDiscountAmount;
    private BigDecimal logisticsAmount;
    private Integer orderSellerId;
    private Integer orderSubCompanyId;
    private Integer orderStatus;
    private BigDecimal firstNeedPayAmount;
    private Integer payStatus;
    private Integer isPeer;
    private Date payTime;
    private Date deliveryTime;
    private Date confirmDeliveryTime;
    private Date expectReturnTime;
    private Date actualReturnTime;
    private Integer highTaxRate;
    private Integer lowTaxRate;
    private String buyerRemark;
    private String productSummary;
    private Integer dataStatus;
    private String remark;
    private Integer owner;
    private Integer deliverySubCompanyId;                       // 发货所属分公司

    private List<OrderProductDO> orderProductDOList;
    private List<OrderMaterialDO> orderMaterialDOList;
    private OrderConsignInfoDO orderConsignInfoDO;
    private List<OrderTimeAxisDO> orderTimeAxisDOList;

    @Transient
    private String buyerCustomerName;
    @Transient
    private String buyerCustomerNo;
    @Transient
    private String orderSellerName;
    @Transient
    private String orderSubCompanyName;
    @Transient
    private String deliverySubCompanyName;                         // 发货所属分公司名称

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Integer getBuyerCustomerId() {
        return buyerCustomerId;
    }

    public void setBuyerCustomerId(Integer buyerCustomerId) {
        this.buyerCustomerId = buyerCustomerId;
    }

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

    public BigDecimal getTotalOrderAmount() {
        return totalOrderAmount;
    }

    public void setTotalOrderAmount(BigDecimal totalOrderAmount) {
        this.totalOrderAmount = totalOrderAmount;
    }

    public BigDecimal getTotalPaidOrderAmount() {
        return totalPaidOrderAmount;
    }

    public void setTotalPaidOrderAmount(BigDecimal totalPaidOrderAmount) {
        this.totalPaidOrderAmount = totalPaidOrderAmount;
    }

    public BigDecimal getTotalDiscountAmount() {
        return totalDiscountAmount;
    }

    public void setTotalDiscountAmount(BigDecimal totalDiscountAmount) {
        this.totalDiscountAmount = totalDiscountAmount;
    }

    public BigDecimal getLogisticsAmount() {
        return logisticsAmount;
    }

    public void setLogisticsAmount(BigDecimal logisticsAmount) {
        this.logisticsAmount = logisticsAmount;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public String getBuyerRemark() {
        return buyerRemark;
    }

    public void setBuyerRemark(String buyerRemark) {
        this.buyerRemark = buyerRemark;
    }

    public String getProductSummary() {
        return productSummary;
    }

    public void setProductSummary(String productSummary) {
        this.productSummary = productSummary;
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

    public List<OrderProductDO> getOrderProductDOList() {
        return orderProductDOList;
    }

    public void setOrderProductDOList(List<OrderProductDO> orderProductDOList) {
        this.orderProductDOList = orderProductDOList;
    }

    public OrderConsignInfoDO getOrderConsignInfoDO() {
        return orderConsignInfoDO;
    }

    public void setOrderConsignInfoDO(OrderConsignInfoDO orderConsignInfoDO) {
        this.orderConsignInfoDO = orderConsignInfoDO;
    }

    public Date getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Date deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public Date getExpectReturnTime() {
        return expectReturnTime;
    }

    public void setExpectReturnTime(Date expectReturnTime) {
        this.expectReturnTime = expectReturnTime;
    }

    public Date getActualReturnTime() {
        return actualReturnTime;
    }

    public void setActualReturnTime(Date actualReturnTime) {
        this.actualReturnTime = actualReturnTime;
    }

    public Date getConfirmDeliveryTime() {
        return confirmDeliveryTime;
    }

    public void setConfirmDeliveryTime(Date confirmDeliveryTime) {
        this.confirmDeliveryTime = confirmDeliveryTime;
    }

    public BigDecimal getTotalDepositAmount() {
        return totalDepositAmount;
    }

    public void setTotalDepositAmount(BigDecimal totalDepositAmount) {
        this.totalDepositAmount = totalDepositAmount;
    }

    public BigDecimal getTotalCreditDepositAmount() {
        return totalCreditDepositAmount;
    }

    public void setTotalCreditDepositAmount(BigDecimal totalCreditDepositAmount) {
        this.totalCreditDepositAmount = totalCreditDepositAmount;
    }

    public BigDecimal getTotalInsuranceAmount() {
        return totalInsuranceAmount;
    }

    public void setTotalInsuranceAmount(BigDecimal totalInsuranceAmount) {
        this.totalInsuranceAmount = totalInsuranceAmount;
    }

    public Integer getOrderSellerId() {
        return orderSellerId;
    }

    public void setOrderSellerId(Integer orderSellerId) {
        this.orderSellerId = orderSellerId;
    }

    public Integer getOrderSubCompanyId() {
        return orderSubCompanyId;
    }

    public void setOrderSubCompanyId(Integer orderSubCompanyId) {
        this.orderSubCompanyId = orderSubCompanyId;
    }

    public Date getRentStartTime() {
        return rentStartTime;
    }

    public void setRentStartTime(Date rentStartTime) {
        this.rentStartTime = rentStartTime;
    }

    public Integer getOwner() {
        return owner;
    }

    public void setOwner(Integer owner) {
        this.owner = owner;
    }

    public List<OrderMaterialDO> getOrderMaterialDOList() {
        return orderMaterialDOList;
    }

    public void setOrderMaterialDOList(List<OrderMaterialDO> orderMaterialDOList) {
        this.orderMaterialDOList = orderMaterialDOList;
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

    public void setTotalMaterialAmount(BigDecimal totalMaterialAmount) {
        this.totalMaterialAmount = totalMaterialAmount;
    }

    public String getOrderSellerName() {
        return orderSellerName;
    }

    public void setOrderSellerName(String orderSellerName) {
        this.orderSellerName = orderSellerName;
    }

    public String getBuyerCustomerName() {
        return buyerCustomerName;
    }

    public void setBuyerCustomerName(String buyerCustomerName) {
        this.buyerCustomerName = buyerCustomerName;
    }

    public BigDecimal getTotalMustDepositAmount() {
        return totalMustDepositAmount;
    }

    public void setTotalMustDepositAmount(BigDecimal totalMustDepositAmount) {
        this.totalMustDepositAmount = totalMustDepositAmount;
    }

    public String getOrderSubCompanyName() {
        return orderSubCompanyName;
    }

    public void setOrderSubCompanyName(String orderSubCompanyName) {
        this.orderSubCompanyName = orderSubCompanyName;
    }

    public String getBuyerCustomerNo() {
        return buyerCustomerNo;
    }

    public void setBuyerCustomerNo(String buyerCustomerNo) {
        this.buyerCustomerNo = buyerCustomerNo;
    }

    public BigDecimal getFirstNeedPayAmount() {
        return firstNeedPayAmount;
    }

    public void setFirstNeedPayAmount(BigDecimal firstNeedPayAmount) {
        this.firstNeedPayAmount = firstNeedPayAmount;
    }

    public BigDecimal getTotalRentDepositAmount() {
        return totalRentDepositAmount;
    }

    public void setTotalRentDepositAmount(BigDecimal totalRentDepositAmount) {
        this.totalRentDepositAmount = totalRentDepositAmount;
    }

    public List<OrderTimeAxisDO> getOrderTimeAxisDOList() {
        return orderTimeAxisDOList;
    }

    public void setOrderTimeAxisDOList(List<OrderTimeAxisDO> orderTimeAxisDOList) {
        this.orderTimeAxisDOList = orderTimeAxisDOList;
    }

    public BigDecimal getTotalProductDepositAmount() {
        return totalProductDepositAmount;
    }

    public void setTotalProductDepositAmount(BigDecimal totalProductDepositAmount) {
        this.totalProductDepositAmount = totalProductDepositAmount;
    }

    public BigDecimal getTotalProductCreditDepositAmount() {
        return totalProductCreditDepositAmount;
    }

    public void setTotalProductCreditDepositAmount(BigDecimal totalProductCreditDepositAmount) {
        this.totalProductCreditDepositAmount = totalProductCreditDepositAmount;
    }

    public BigDecimal getTotalProductRentDepositAmount() {
        return totalProductRentDepositAmount;
    }

    public void setTotalProductRentDepositAmount(BigDecimal totalProductRentDepositAmount) {
        this.totalProductRentDepositAmount = totalProductRentDepositAmount;
    }

    public BigDecimal getTotalMaterialDepositAmount() {
        return totalMaterialDepositAmount;
    }

    public void setTotalMaterialDepositAmount(BigDecimal totalMaterialDepositAmount) {
        this.totalMaterialDepositAmount = totalMaterialDepositAmount;
    }

    public BigDecimal getTotalMaterialCreditDepositAmount() {
        return totalMaterialCreditDepositAmount;
    }

    public void setTotalMaterialCreditDepositAmount(BigDecimal totalMaterialCreditDepositAmount) {
        this.totalMaterialCreditDepositAmount = totalMaterialCreditDepositAmount;
    }

    public BigDecimal getTotalMaterialRentDepositAmount() {
        return totalMaterialRentDepositAmount;
    }

    public void setTotalMaterialRentDepositAmount(BigDecimal totalMaterialRentDepositAmount) {
        this.totalMaterialRentDepositAmount = totalMaterialRentDepositAmount;
    }

    public Integer getHighTaxRate() {
        return highTaxRate;
    }

    public void setHighTaxRate(Integer highTaxRate) {
        this.highTaxRate = highTaxRate;
    }

    public Integer getLowTaxRate() {
        return lowTaxRate;
    }

    public void setLowTaxRate(Integer lowTaxRate) {
        this.lowTaxRate = lowTaxRate;
    }

    public Date getExpectDeliveryTime() {
        return expectDeliveryTime;
    }

    public void setExpectDeliveryTime(Date expectDeliveryTime) {
        this.expectDeliveryTime = expectDeliveryTime;
    }

    public Integer getDeliveryMode() {
        return deliveryMode;
    }

    public void setDeliveryMode(Integer deliveryMode) {
        this.deliveryMode = deliveryMode;
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

    public Integer getRentLengthType() {
        return rentLengthType;
    }

    public void setRentLengthType(Integer rentLengthType) {
        this.rentLengthType = rentLengthType;
    }

    public Integer getDeliverySubCompanyId() { return deliverySubCompanyId; }

    public void setDeliverySubCompanyId(Integer deliverySubCompanyId) { this.deliverySubCompanyId = deliverySubCompanyId; }

    public String getDeliverySubCompanyName() { return deliverySubCompanyName; }

    public void setDeliverySubCompanyName(String deliverySubCompanyName) { this.deliverySubCompanyName = deliverySubCompanyName; }

    public Integer getIsPeer() {
        return isPeer;
    }

    public void setIsPeer(Integer isPeer) {
        this.isPeer = isPeer;
    }
}
