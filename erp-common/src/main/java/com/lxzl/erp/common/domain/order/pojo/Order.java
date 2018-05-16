package com.lxzl.erp.common.domain.order.pojo;

import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.base.BasePO;
import com.lxzl.erp.common.domain.coupon.pojo.Coupon;
import com.lxzl.erp.common.domain.k3.pojo.returnOrder.K3ReturnOrderDetail;
import com.lxzl.erp.common.domain.reletorder.pojo.ReletOrder;
import com.lxzl.erp.common.domain.statement.pojo.StatementOrder;
import com.lxzl.erp.common.domain.validGroup.IdGroup;
import com.lxzl.erp.common.domain.workflow.pojo.WorkflowLink;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Order extends BasePO {

    private Integer orderId;                    // 订单号
    @NotBlank(message = ErrorCode.ORDER_NO_NOT_NULL,groups = {IdGroup.class})
    private String orderNo;                     // 订单编号
    private Integer deliveryMode;               // 发货方式，1快递，2自提，3凌雄配送
    private Integer buyerCustomerId;            // 客户ID
    private Date expectDeliveryTime;            // 预计发货时间
    private Date rentStartTime;                 // 起租时间
    private Integer rentType;                   // 租赁类型，1按天，2按月
    private Integer rentTimeLength;             // 租赁时长
    private Integer rentLengthType;             // 1短租，2长租
    private Integer depositCycle;               // 押金期数（无需填写）
    private Integer paymentCycle;               // 付款期数（无需填写）
    private BigDecimal totalDepositAmount;      // 总押金
    private BigDecimal totalMustDepositAmount;  // 必付押金（无需填写）
    private BigDecimal totalCreditDepositAmount;    // 总授信押金
    private BigDecimal totalRentDepositAmount;      // 总租金押金
    private BigDecimal totalInsuranceAmount;
    private Integer totalProductCount;              // 总设备数
    private BigDecimal totalProductAmount;          // 总设备金额
    private BigDecimal totalProductDepositAmount;   // 总设备押金金额
    private BigDecimal totalProductCreditDepositAmount;    // 总授信押金
    private BigDecimal totalProductRentDepositAmount;       // 总租金押金
    private Integer totalMaterialCount;                 // 总配件数量
    private BigDecimal totalMaterialAmount;             // 总配件金额
    private BigDecimal totalMaterialDepositAmount;      // 总配件押金金额
    private BigDecimal totalMaterialCreditDepositAmount;    // 总配件授信押金金额
    private BigDecimal totalMaterialRentDepositAmount;      // 总配件租金押金金额
    private BigDecimal totalOrderAmount;                    // 订单总金额
    private BigDecimal totalPaidOrderAmount;                // 已支付订单总金额
    private BigDecimal totalDiscountAmount;                 // 总优惠金额
    private BigDecimal logisticsAmount;                     // 运费
    private Integer orderSellerId;                          // 业务员ID
    private Integer orderSubCompanyId;                      // 分公司ID
    private Integer deliverySubCompanyId;                       // 发货所属分公司
    private Integer orderStatus;                            // 订单状态
    private BigDecimal firstNeedPayAmount;                      // 首付租金
    private Integer payStatus;                                  // 支付状态
    private Integer isPeer;                                  // 是否同行调拨，0-否，1是
    private Date payTime;                                       // 支付时间
    private Date deliveryTime;                                  // 发货时间
    private Date confirmDeliveryTime;                           // 确认发货时间
    private Date expectReturnTime;                              // 预计归还时间
    private Date actualReturnTime;                              // 实际归还时间
    private Integer highTaxRate;                                // 17%税率占比
    private Integer lowTaxRate;                                 // 6%税率占比
    private String buyerRemark;                                 // 租户备注
    private String productSummary;                                 // 商品摘要
    private Integer dataStatus;
    private String remark;                                      // 备注信息
    private Date createTime;                                    // 订单创建时间

    private Integer customerConsignId;
    private String buyerCustomerNo;                             // 客户编号
    private String buyerCustomerName;                           // 客户姓名
    private String orderSellerName;                             // 业务员姓名
    private String orderSubCompanyName;                         // 分公司名称
    private String deliverySubCompanyName;                         // 发货所属分公司名称
    private Integer isK3Order;                                  // 是否是K3订单，1是0否
    private Integer statementDate;                              //结算时间（天），20和31两种情况，如果为空取系统设定
    private String orderMessage;                                //订单消息

    private List<OrderProduct> orderProductList;                // 订单商品项
    private List<OrderMaterial> orderMaterialList;              // 订单配件项
    @Valid
    private List<OrderJointProduct> orderJointProductList;      // 订单组合商品项
    private OrderConsignInfo orderConsignInfo;                  // 收货地址信息

    private List<OrderTimeAxis> orderTimeAxisList;

    private List<ReletOrder> reletOrderList;

    // 审核人和提交审核信息,只提供给审核的时候用
    private Integer verifyUser;                                 // 审核人ID
    private String commitRemark;                                // 提交审核备注

    private StatementOrder statementOrder;

    private BigDecimal totalProductFirstNeedPayAmount;  //首付商品总金额
    private BigDecimal totalMaterialFirstNeedPayAmount; //首付配件总金额

    private Integer cancelOrderReasonType;                      //取消订单原因类型，1-下错单，2-变更数量，3-变更单价，4-变更配件，5-变更结算日，6-变更支付方式，7-变更时间/租期，8-变更型号/配置，9-变更收货人信息，10-同行调货选错，12-设备故障换货，13-客户名称错误，14-客户取消订单，15-缺货取消，16-实际出货与订单不符
    private WorkflowLink workflowLink;

    private  List<K3ReturnOrderDetail> k3ReturnOrderDetailList; //退货单项列表

    private List<Coupon> couponList;//该订单使用的设备优惠券

    public List<Coupon> getCouponList() {
        return couponList;
    }

    public void setCouponList(List<Coupon> couponList) {
        this.couponList = couponList;
    }

    public List<OrderProduct> getOrderProductList() {
        return orderProductList;
    }

    public void setOrderProductList(List<OrderProduct> orderProductList) {
        this.orderProductList = orderProductList;
    }

    public List<ReletOrder> getReletOrderList() {
        return reletOrderList;
    }

    public void setReletOrderList(List<ReletOrder> reletOrderList) {
        this.reletOrderList = reletOrderList;
    }

    public OrderConsignInfo getOrderConsignInfo() {
        return orderConsignInfo;
    }

    public void setOrderConsignInfo(OrderConsignInfo orderConsignInfo) {
        this.orderConsignInfo = orderConsignInfo;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
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

    public Integer getCustomerConsignId() {
        return customerConsignId;
    }

    public void setCustomerConsignId(Integer customerConsignId) {
        this.customerConsignId = customerConsignId;
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

    public Integer getBuyerCustomerId() {
        return buyerCustomerId;
    }

    public void setBuyerCustomerId(Integer buyerCustomerId) {
        this.buyerCustomerId = buyerCustomerId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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

    public Date getExpectDeliveryTime() {
        return expectDeliveryTime;
    }

    public void setExpectDeliveryTime(Date expectDeliveryTime) {
        this.expectDeliveryTime = expectDeliveryTime;
    }

    public Integer getVerifyUser() {
        return verifyUser;
    }

    public void setVerifyUser(Integer verifyUser) {
        this.verifyUser = verifyUser;
    }

    public List<OrderMaterial> getOrderMaterialList() {
        return orderMaterialList;
    }

    public void setOrderMaterialList(List<OrderMaterial> orderMaterialList) {
        this.orderMaterialList = orderMaterialList;
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

    public String getBuyerCustomerNo() {
        return buyerCustomerNo;
    }

    public void setBuyerCustomerNo(String buyerCustomerNo) {
        this.buyerCustomerNo = buyerCustomerNo;
    }

    public String getBuyerCustomerName() {
        return buyerCustomerName;
    }

    public void setBuyerCustomerName(String buyerCustomerName) {
        this.buyerCustomerName = buyerCustomerName;
    }

    public String getOrderSellerName() {
        return orderSellerName;
    }

    public void setOrderSellerName(String orderSellerName) {
        this.orderSellerName = orderSellerName;
    }

    public BigDecimal getTotalMustDepositAmount() {
        return totalMustDepositAmount;
    }

    public void setTotalMustDepositAmount(BigDecimal totalMustDepositAmount) {
        this.totalMustDepositAmount = totalMustDepositAmount;
    }

    public BigDecimal getTotalPaidOrderAmount() {
        return totalPaidOrderAmount;
    }

    public void setTotalPaidOrderAmount(BigDecimal totalPaidOrderAmount) {
        this.totalPaidOrderAmount = totalPaidOrderAmount;
    }

    public String getCommitRemark() {
        return commitRemark;
    }

    public void setCommitRemark(String commitRemark) {
        this.commitRemark = commitRemark;
    }

    public String getOrderSubCompanyName() {
        return orderSubCompanyName;
    }

    public void setOrderSubCompanyName(String orderSubCompanyName) {
        this.orderSubCompanyName = orderSubCompanyName;
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

    public List<OrderTimeAxis> getOrderTimeAxisList() {
        return orderTimeAxisList;
    }

    public void setOrderTimeAxisList(List<OrderTimeAxis> orderTimeAxisList) {
        this.orderTimeAxisList = orderTimeAxisList;
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

    public Integer getDeliveryMode() {
        return deliveryMode;
    }

    public void setDeliveryMode(Integer deliveryMode) {
        this.deliveryMode = deliveryMode;
    }

    public StatementOrder getStatementOrder() {
        return statementOrder;
    }

    public void setStatementOrder(StatementOrder statementOrder) {
        this.statementOrder = statementOrder;
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

    public BigDecimal getTotalProductFirstNeedPayAmount() { return totalProductFirstNeedPayAmount; }

    public void setTotalProductFirstNeedPayAmount(BigDecimal totalProductFirstNeedPayAmount) { this.totalProductFirstNeedPayAmount = totalProductFirstNeedPayAmount; }

    public BigDecimal getTotalMaterialFirstNeedPayAmount() { return totalMaterialFirstNeedPayAmount; }

    public void setTotalMaterialFirstNeedPayAmount(BigDecimal totalMaterialFirstNeedPayAmount) { this.totalMaterialFirstNeedPayAmount = totalMaterialFirstNeedPayAmount; }

    public Integer getIsPeer() {
        return isPeer;
    }

    public void setIsPeer(Integer isPeer) {
        this.isPeer = isPeer;
    }

    public Integer getIsK3Order() {
        return isK3Order;
    }

    public void setIsK3Order(Integer isK3Order) {
        this.isK3Order = isK3Order;
    }

    public Integer getStatementDate() {
        return statementDate;
    }

    public void setStatementDate(Integer statementDate) {
        this.statementDate = statementDate;
    }

    public String getOrderMessage() {
        return orderMessage;
    }

    public void setOrderMessage(String orderMessage) {
        this.orderMessage = orderMessage;
    }

    public Integer getCancelOrderReasonType() {
        return cancelOrderReasonType;
    }

    public void setCancelOrderReasonType(Integer cancelOrderReasonType) {
        this.cancelOrderReasonType = cancelOrderReasonType;
    }

    public List<K3ReturnOrderDetail> getK3ReturnOrderDetailList() {
        return k3ReturnOrderDetailList;
    }

    public void setK3ReturnOrderDetailList(List<K3ReturnOrderDetail> k3ReturnOrderDetailList) {
        this.k3ReturnOrderDetailList = k3ReturnOrderDetailList;
    }

    public WorkflowLink getWorkflowLink() {
        return workflowLink;
    }

    public void setWorkflowLink(WorkflowLink workflowLink) {
        this.workflowLink = workflowLink;
    }

    public List<OrderJointProduct> getOrderJointProductList() {
        return orderJointProductList;
    }

    public void setOrderJointProductList(List<OrderJointProduct> orderJointProductList) {
        this.orderJointProductList = orderJointProductList;
    }
}
