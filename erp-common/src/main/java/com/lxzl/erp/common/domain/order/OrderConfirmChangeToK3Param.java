package com.lxzl.erp.common.domain.order;


import java.util.List;

/**
 * @Author: Sunzhipeng
 * @Description:
 * @Date: Created in 2018\5\25 0025 14:57
 */
public class OrderConfirmChangeToK3Param {
    private String orderNo;
    private Integer orderId;
    private List<ChangeOrderItemParam> changeOrderItemParamList;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public List<ChangeOrderItemParam> getChangeOrderItemParamList() {
        return changeOrderItemParamList;
    }

    public void setChangeOrderItemParamList(List<ChangeOrderItemParam> changeOrderItemParamList) {
        this.changeOrderItemParamList = changeOrderItemParamList;
    }
}
