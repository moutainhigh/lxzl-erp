package com.lxzl.erp.common.domain.order.pojo;

import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.base.BasePO;
import com.lxzl.erp.common.util.validate.constraints.In;

import javax.validation.constraints.NotNull;

/**
 * @Auther: huahongbin
 * @Date: 2018/5/8 15:06
 * @Description: 订单拆单明细
 */
public class OrderSplitDetail extends BasePO {
    private Integer orderSplitDetailId;
    private Integer orderId;
    private String orderNo;
    @In(value = {1, 2}, message = ErrorCode.ORDER_SPLIT_ORDER_ITEM_TYPE_NOT_NULL)
    private Integer orderItemType;
    @NotNull(message = ErrorCode.ORDER_SPLIT_ORDER_ITEM_REFER_ID_NOT_NULL)
    private Integer orderItemReferId;
    @NotNull(message = ErrorCode.ORDER_SPLIT_SPLIT_COUNT_NOT_NULL)
    private Integer splitCount;
    @In(value = {0, 1}, message = ErrorCode.ORDER_SPLIT_IS_PEER_OR_SUB_COMPANY_ID_EXIST)
    private Integer isPeer;
    private Integer deliverySubCompanyId;
    private String deliverySubCompanyName;
    private String remark;

    public Integer getOrderSplitDetailId() {
        return orderSplitDetailId;
    }

    public void setOrderSplitDetailId(Integer orderSplitDetailId) {
        this.orderSplitDetailId = orderSplitDetailId;
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

    public Integer getOrderItemType() {
        return orderItemType;
    }

    public void setOrderItemType(Integer orderItemType) {
        this.orderItemType = orderItemType;
    }

    public Integer getOrderItemReferId() {
        return orderItemReferId;
    }

    public void setOrderItemReferId(Integer orderItemReferId) {
        this.orderItemReferId = orderItemReferId;
    }

    public Integer getSplitCount() {
        return splitCount;
    }

    public void setSplitCount(Integer splitCount) {
        this.splitCount = splitCount;
    }

    public Integer getIsPeer() {
        return isPeer;
    }

    public void setIsPeer(Integer isPeer) {
        this.isPeer = isPeer;
    }

    public Integer getDeliverySubCompanyId() {
        return deliverySubCompanyId;
    }

    public void setDeliverySubCompanyId(Integer deliverySubCompanyId) {
        this.deliverySubCompanyId = deliverySubCompanyId;
    }

    public String getDeliverySubCompanyName() {
        return deliverySubCompanyName;
    }

    public void setDeliverySubCompanyName(String deliverySubCompanyName) {
        this.deliverySubCompanyName = deliverySubCompanyName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
