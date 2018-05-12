package com.lxzl.erp.common.domain.order.pojo;

import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.base.BasePO;
import com.lxzl.erp.common.util.validate.constraints.In;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>Description: </p>
 *
 * @Auther: huahongbin
 * @Date: 2018/5/11
 */
public class OrderSplit extends BasePO {
    @In(value = {1, 2}, message = ErrorCode.ORDER_SPLIT_ORDER_ITEM_TYPE_NOT_NULL)
    private Integer orderItemType;
    @NotNull(message = ErrorCode.ORDER_SPLIT_ORDER_ITEM_REFER_ID_NOT_NULL)
    private Integer orderItemReferId;

    @Valid
    private List<OrderSplitDetail> splitDetailList;

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

    public List<OrderSplitDetail> getSplitDetailList() {
        return splitDetailList;
    }

    public void setSplitDetailList(List<OrderSplitDetail> splitDetailList) {
        this.splitDetailList = splitDetailList;
    }
}
