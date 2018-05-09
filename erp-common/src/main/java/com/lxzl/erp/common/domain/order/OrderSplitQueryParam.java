package com.lxzl.erp.common.domain.order;

import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.validGroup.order.OrderSplitQueryByTypeAndIdGroup;
import com.lxzl.se.dataaccess.mysql.config.PageQuery;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Auther: huahongbin
 * @Date: 2018/5/8 15:00
 * @Description: 订单拆单明细查询
 */
public class OrderSplitQueryParam extends PageQuery implements Serializable {

    private Integer orderId;
    private Integer orderNo;
    @NotNull(message = ErrorCode.PARAM_IS_NOT_NULL, groups = {OrderSplitQueryByTypeAndIdGroup.class})
    private Integer orderItemType;
    @NotNull(message = ErrorCode.PARAM_IS_NOT_NULL, groups = {OrderSplitQueryByTypeAndIdGroup.class})
    private Integer orderItemReferId;

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
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
}
