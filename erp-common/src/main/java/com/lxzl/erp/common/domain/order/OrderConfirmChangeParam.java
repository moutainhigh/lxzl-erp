package com.lxzl.erp.common.domain.order;

import com.lxzl.erp.common.domain.system.pojo.Image;

import java.util.List;


/**
 * @Author: Sunzhipeng
 * @Description:确认订单时退货传递的参数
 * @Date: Created in 2018\5\21 0021 17:23
 */
public class OrderConfirmChangeParam {
    private String orderNo;
    private Integer orderId;
    private List<OrderItemParam> orderItemParamList;
    private Image deliveryNoteCustomerSignImg;//交货单客户签字
    private Integer changeReasonType;
    private String changeReason;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public List<OrderItemParam> getOrderItemParamList() {
        return orderItemParamList;
    }

    public void setOrderItemParamList(List<OrderItemParam> orderItemParamList) {
        this.orderItemParamList = orderItemParamList;
    }

    public Integer getChangeReasonType() {
        return changeReasonType;
    }

    public void setChangeReasonType(Integer changeReasonType) {
        this.changeReasonType = changeReasonType;
    }

    public String getChangeReason() {
        return changeReason;
    }

    public void setChangeReason(String changeReason) {
        this.changeReason = changeReason;
    }

    public Image getDeliveryNoteCustomerSignImg() {
        return deliveryNoteCustomerSignImg;
    }

    public void setDeliveryNoteCustomerSignImg(Image deliveryNoteCustomerSignImg) {
        this.deliveryNoteCustomerSignImg = deliveryNoteCustomerSignImg;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }
}
