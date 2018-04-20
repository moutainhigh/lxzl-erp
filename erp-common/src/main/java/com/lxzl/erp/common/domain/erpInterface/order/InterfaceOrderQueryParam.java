package com.lxzl.erp.common.domain.erpInterface.order;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.base.BasePageParam;
import com.lxzl.erp.common.domain.validGroup.customer.QueryCustomerNoGroup;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/1/29
 * @Time : Created in 19:59
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class InterfaceOrderQueryParam extends BasePageParam implements Serializable {
    private Integer orderId;
    @NotNull(message = ErrorCode.ORDER_NO_NOT_NULL,groups = {QueryCustomerNoGroup.class})
    private String orderNo;
    private String buyerRealName;
    private Integer buyerCustomerId;
    private String buyerCustomerNo;
    private String consigneeName;
    private String consigneePhone;
    private Integer rentType;
    private Integer orderStatus;
    private Date createStartTime;
    private Date createEndTime;
    private Integer orderSellerId;

    private List<Integer> passiveUserIdList;
    private Integer subCompanyId;
    private Integer deliveryMode;
    private Integer isPendingDelivery;
    private Integer deliverySubCompanyId;
    private List<Integer> orderStatusList;

    @NotNull(message = ErrorCode.BUSINESS_APP_ID_NOT_NULL)
    private String erpAppId;   //业务系统APP ID由ERP系统生成，提供给业务系统
    @NotNull(message = ErrorCode.BUSINESS_APP_SECRET_NOT_NULL)
    private String erpAppSecret;   //业务系统app secret由ERP系统生成，提供给业务系统
    private String erpOperateUser;   //系统名称

    private Integer payStatus;
    private Integer isPeer;
    private Integer isRecycleBin;//是否回收站

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

    public String getBuyerRealName() {
        return buyerRealName;
    }

    public void setBuyerRealName(String buyerRealName) {
        this.buyerRealName = buyerRealName;
    }

    public Integer getBuyerCustomerId() {
        return buyerCustomerId;
    }

    public void setBuyerCustomerId(Integer buyerCustomerId) {
        this.buyerCustomerId = buyerCustomerId;
    }

    public String getBuyerCustomerNo() {
        return buyerCustomerNo;
    }

    public void setBuyerCustomerNo(String buyerCustomerNo) {
        this.buyerCustomerNo = buyerCustomerNo;
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

    public Integer getOrderSellerId() {
        return orderSellerId;
    }

    public void setOrderSellerId(Integer orderSellerId) {
        this.orderSellerId = orderSellerId;
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

    public Integer getIsPendingDelivery() {
        return isPendingDelivery;
    }

    public void setIsPendingDelivery(Integer isPendingDelivery) {
        this.isPendingDelivery = isPendingDelivery;
    }

    public String getErpAppId() {
        return erpAppId;
    }

    public void setErpAppId(String erpAppId) {
        this.erpAppId = erpAppId;
    }

    public String getErpAppSecret() {
        return erpAppSecret;
    }

    public void setErpAppSecret(String erpAppSecret) {
        this.erpAppSecret = erpAppSecret;
    }

    public String getErpOperateUser() {
        return erpOperateUser;
    }

    public void setErpOperateUser(String erpOperateUser) {
        this.erpOperateUser = erpOperateUser;
    }

    public Integer getDeliverySubCompanyId() {
        return deliverySubCompanyId;
    }

    public void setDeliverySubCompanyId(Integer deliverySubCompanyId) {
        this.deliverySubCompanyId = deliverySubCompanyId;
    }

    public List<Integer> getOrderStatusList() {
        return orderStatusList;
    }

    public void setOrderStatusList(List<Integer> orderStatusList) {
        this.orderStatusList = orderStatusList;
    }

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
}
