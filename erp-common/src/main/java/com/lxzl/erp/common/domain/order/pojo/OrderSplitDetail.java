package com.lxzl.erp.common.domain.order.pojo;

import com.lxzl.erp.common.domain.base.BasePO;

/**
 * @Auther: huahongbin
 * @Date: 2018/5/8 15:06
 * @Description: 订单拆单明细
 */
public class OrderSplitDetail extends BasePO {
    private Integer orderSplitDetailId;
    private Integer splitCount;
    private Integer isPeer;
    private Integer deliverySubCompanyId;
    private String remark;

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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getOrderSplitDetailId() {
        return orderSplitDetailId;
    }

    public void setOrderSplitDetailId(Integer orderSplitDetailId) {
        this.orderSplitDetailId = orderSplitDetailId;
    }
}
