package com.lxzl.erp.dataaccess.domain.order;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderDO extends BaseDO {

    private Integer id;
    private String orderNo;
    private Integer buyerCustomerId;
    private Integer rentType;
    private Integer rentTimeLength;
    private Integer payMode;
    private Integer productCountTotal;
    private BigDecimal productAmountTotal;
    private BigDecimal orderAmountTotal;
    private BigDecimal discountAmountTotal;
    private BigDecimal logisticsAmount;
    private Integer orderStatus;
    private Integer payStatus;
    private Date payTime;
    private Date deliveryTime;
    private Date confirmDeliveryTime;
    private Date expectReturnTime;
    private Date actualReturnTime;
    private String buyerRemark;
    private Integer dataStatus;
    private String remark;

    private List<OrderProductDO> orderProductDOList;
    private OrderConsignInfoDO orderConsignInfoDO;

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

    public Integer getRentType() {
        return rentType;
    }

    public void setRentType(Integer rentType) {
        this.rentType = rentType;
    }

    public Integer getProductCountTotal() {
        return productCountTotal;
    }

    public void setProductCountTotal(Integer productCountTotal) {
        this.productCountTotal = productCountTotal;
    }

    public BigDecimal getProductAmountTotal() {
        return productAmountTotal;
    }

    public void setProductAmountTotal(BigDecimal productAmountTotal) {
        this.productAmountTotal = productAmountTotal;
    }

    public BigDecimal getOrderAmountTotal() {
        return orderAmountTotal;
    }

    public void setOrderAmountTotal(BigDecimal orderAmountTotal) {
        this.orderAmountTotal = orderAmountTotal;
    }

    public BigDecimal getDiscountAmountTotal() {
        return discountAmountTotal;
    }

    public void setDiscountAmountTotal(BigDecimal discountAmountTotal) {
        this.discountAmountTotal = discountAmountTotal;
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

    public Integer getRentTimeLength() {
        return rentTimeLength;
    }

    public void setRentTimeLength(Integer rentTimeLength) {
        this.rentTimeLength = rentTimeLength;
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

    public Integer getPayMode() {
        return payMode;
    }

    public void setPayMode(Integer payMode) {
        this.payMode = payMode;
    }

}
