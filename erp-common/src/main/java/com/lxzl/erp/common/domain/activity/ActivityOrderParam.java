package com.lxzl.erp.common.domain.activity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePageParam;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ActivityOrderParam extends BasePageParam {
    private Integer id; //id
    private Integer activityId; //活动id
    private String activityName; //活动名
    private String orderNumber; //订单编号
    private String payOrderNo; //支付订单编号
    private Integer payType; //支付类型 1:网银 2:微信公众号 3:支付宝 4:余额
    private Integer rentTimeLength; //租期(单位：月)
    private Integer monthRent; //租金(总额)
    private Integer discountAmount; //折扣金额
    private Integer totalOrderAmount; //总额(租金-优惠金额)
    private String couponCode; //优惠券码
    private Integer productId; //产品id
    private String productName; //产品名称
    private String customerNo; //客戶编码
    private String customerName; //客户名称
    private String phone; //电话号码
    private String idCardNo; //身份证号
    private String deliveryAddress; //收货地址
    private String schoolName; //学校名称
    private String schoolNumber; //学号
    private String remark; //备注
    private Date orderTime; //订单日期
    private Date rentStartTime; //起租时间
    private Integer status; //订单状态：0未激活；1可用(未付款)；2支付中；3已付款(未发货)；4已发货；5租赁中；6已退款；7已还货；8付款失败
    private Integer dataStatus; //数据状态：0不可用；1可用；2删除
    private Date createTime; //添加时间
    private Date updateTime; //修改时间

    private String fuzzyOrderNumber;
    private String fuzzyCustomerName;
    private String fuzzyCouponCode;
    private Date orderStartTime;
    private Date orderEndTime;
    private Integer advancedState;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getPayOrderNo() {
        return payOrderNo;
    }

    public void setPayOrderNo(String payOrderNo) {
        this.payOrderNo = payOrderNo;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public Integer getRentTimeLength() {
        return rentTimeLength;
    }

    public void setRentTimeLength(Integer rentTimeLength) {
        this.rentTimeLength = rentTimeLength;
    }

    public Integer getMonthRent() {
        return monthRent;
    }

    public void setMonthRent(Integer monthRent) {
        this.monthRent = monthRent;
    }

    public Integer getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(Integer discountAmount) {
        this.discountAmount = discountAmount;
    }

    public Integer getTotalOrderAmount() {
        return totalOrderAmount;
    }

    public void setTotalOrderAmount(Integer totalOrderAmount) {
        this.totalOrderAmount = totalOrderAmount;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
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

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getSchoolNumber() {
        return schoolNumber;
    }

    public void setSchoolNumber(String schoolNumber) {
        this.schoolNumber = schoolNumber;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public Date getRentStartTime() {
        return rentStartTime;
    }

    public void setRentStartTime(Date rentStartTime) {
        this.rentStartTime = rentStartTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(Integer dataStatus) {
        this.dataStatus = dataStatus;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getFuzzyOrderNumber() {
        return fuzzyOrderNumber;
    }

    public void setFuzzyOrderNumber(String fuzzyOrderNumber) {
        this.fuzzyOrderNumber = fuzzyOrderNumber;
    }

    public String getFuzzyCustomerName() {
        return fuzzyCustomerName;
    }

    public void setFuzzyCustomerName(String fuzzyCustomerName) {
        this.fuzzyCustomerName = fuzzyCustomerName;
    }

    public String getFuzzyCouponCode() {
        return fuzzyCouponCode;
    }

    public void setFuzzyCouponCode(String fuzzyCouponCode) {
        this.fuzzyCouponCode = fuzzyCouponCode;
    }

    public Date getOrderStartTime() {
        return orderStartTime;
    }

    public void setOrderStartTime(Date orderStartTime) {
        this.orderStartTime = orderStartTime;
    }

    public Date getOrderEndTime() {
        return orderEndTime;
    }

    public void setOrderEndTime(Date orderEndTime) {
        this.orderEndTime = orderEndTime;
    }

    public Integer getAdvancedState() {
        return advancedState;
    }

    public void setAdvancedState(Integer advancedState) {
        this.advancedState = advancedState;
    }
}