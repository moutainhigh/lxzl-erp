package com.lxzl.erp.common.domain.order;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePageParam;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderQueryParam extends BasePageParam implements Serializable {

    private Integer orderId;
    private String orderNo;
    private String buyerRealName;
    private Integer buyerCustomerId;
    private String buyerCustomerNo;
    private String consigneeName;
    private String consigneePhone;
    private Integer rentType;
    private Integer orderStatus;
    private List<Integer> orderStatusList;
    private Date createStartTime;
    private Date createEndTime;
    private Integer orderSellerId;
    private String orderSellerName;                             // 业务员姓名
    private Date startRentStartTime;
    private Date endRentStartTime;
    private Integer isReletOrder;                               //是否为续租单

    private Date startExpectReturnTime;  //起始预计归还时间
    private Date endExpectReturnTime;
    private Integer rentLengthType;    //1 短租 2长租


    private List<Integer> passiveUserIdList;
    private Integer subCompanyId;
    private Integer deliverySubCompanyId;                       // 发货所属分公司
    private Integer deliveryMode;
    private Integer isPendingDelivery;
    private Integer payStatus;
    private Integer isPeer;
    private Integer isRecycleBin;//是否回收站
    private Integer isReturnOverDue;  //商品退还是否逾期

    private Integer isCanReletOrder; //是否为可续租的订单

    private Date startExpectDeliveryTime;  //送货开始时间
    private Date endExpectDeliveryTime;  //送货结束时间

    private String createUser;

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

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public List<Integer> getOrderStatusList() {
        return orderStatusList;
    }

    public void setOrderStatusList(List<Integer> orderStatusList) {
        this.orderStatusList = orderStatusList;
    }

    public Date getCreateStartTime() {
        return createStartTime;
    }

    public void setCreateStartTime(Date createStartTime) {
        this.createStartTime = createStartTime;
    }

    public Date getCreateEndTime() {
        return createEndTime;
    }

    public void setCreateEndTime(Date createEndTime) {
        this.createEndTime = createEndTime;
    }

    public String getBuyerRealName() {
        return buyerRealName;
    }

    public void setBuyerRealName(String buyerRealName) {
        this.buyerRealName = buyerRealName;
    }

    public String getConsigneeName() {
        return consigneeName;
    }

    public void setConsigneeName(String consigneeName) {
        this.consigneeName = consigneeName;
    }

    public String getConsigneePhone() {
        return consigneePhone;
    }

    public void setConsigneePhone(String consigneePhone) {
        this.consigneePhone = consigneePhone;
    }

    public Integer getOrderSellerId() {
        return orderSellerId;
    }

    public void setOrderSellerId(Integer orderSellerId) {
        this.orderSellerId = orderSellerId;
    }

    public String getOrderSellerName() {
        return orderSellerName;
    }

    public void setOrderSellerName(String orderSellerName) {
        this.orderSellerName = orderSellerName;
    }

    public List<Integer> getPassiveUserIdList() {
        return passiveUserIdList;
    }

    public void setPassiveUserIdList(List<Integer> passiveUserIdList) {
        this.passiveUserIdList = passiveUserIdList;
    }

    public Integer getSubCompanyId() {
        return subCompanyId;
    }

    public void setSubCompanyId(Integer subCompanyId) {
        this.subCompanyId = subCompanyId;
    }

    public Integer getDeliveryMode() {
        return deliveryMode;
    }

    public void setDeliveryMode(Integer deliveryMode) {
        this.deliveryMode = deliveryMode;
    }

    public String getBuyerCustomerNo() {
        return buyerCustomerNo;
    }

    public void setBuyerCustomerNo(String buyerCustomerNo) {
        this.buyerCustomerNo = buyerCustomerNo;
    }

    public Integer getIsPendingDelivery() {
        return isPendingDelivery;
    }

    public void setIsPendingDelivery(Integer isPendingDelivery) {
        this.isPendingDelivery = isPendingDelivery;
    }

    public Integer getDeliverySubCompanyId() { return deliverySubCompanyId; }

    public void setDeliverySubCompanyId(Integer deliverySubCompanyId) { this.deliverySubCompanyId = deliverySubCompanyId; }

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    public Integer getIsPeer() {
        return isPeer;
    }

    public void setIsPeer(Integer isPeer) {
        this.isPeer = isPeer;
    }

    public Integer getIsRecycleBin() {
        return isRecycleBin;
    }

    public void setIsRecycleBin(Integer isRecycleBin) {
        this.isRecycleBin = isRecycleBin;
    }

    public Date getstartRentStartTime() {
        return startRentStartTime;
    }

    public void setstartRentStartTime(Date startRentStartTime) {
        this.startRentStartTime = startRentStartTime;
    }

    public Date getendRentStartTime() {
        return endRentStartTime;
    }

    public void setendRentStartTime(Date endRentStartTime) {
        this.endRentStartTime = endRentStartTime;
    }
    public Integer getIsReturnOverDue() {
        return isReturnOverDue;
    }

    public void setIsReturnOverDue(Integer isReturnOverDue) {
        this.isReturnOverDue = isReturnOverDue;
    }

    public Integer getIsReletOrder() {
        return isReletOrder;
    }

    public void setIsReletOrder(Integer isReletOrder) {
        this.isReletOrder = isReletOrder;
    }

    public Date getStartExpectReturnTime() {
        return startExpectReturnTime;
    }

    public void setStartExpectReturnTime(Date startExpectReturnTime) {
        this.startExpectReturnTime = startExpectReturnTime;
    }

    public Date getEndExpectReturnTime() {
        return endExpectReturnTime;
    }

    public void setEndExpectReturnTime(Date endExpectReturnTime) {
        this.endExpectReturnTime = endExpectReturnTime;
    }

    public Integer getRentLengthType() {
        return rentLengthType;
    }

    public void setRentLengthType(Integer rentLengthType) {
        this.rentLengthType = rentLengthType;
    }

    public Integer getIsCanReletOrder() {
        return isCanReletOrder;
    }

    public void setIsCanReletOrder(Integer isCanReletOrder) {
        this.isCanReletOrder = isCanReletOrder;
    }

    public Date getStartExpectDeliveryTime() {
        return startExpectDeliveryTime;
    }

    public void setStartExpectDeliveryTime(Date startExpectDeliveryTime) {
        this.startExpectDeliveryTime = startExpectDeliveryTime;
    }

    public Date getEndExpectDeliveryTime() {
        return endExpectDeliveryTime;
    }

    public void setEndExpectDeliveryTime(Date endExpectDeliveryTime) {
        this.endExpectDeliveryTime = endExpectDeliveryTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }
}
