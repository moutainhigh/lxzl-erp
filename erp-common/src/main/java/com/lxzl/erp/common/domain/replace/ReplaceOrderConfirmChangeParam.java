package com.lxzl.erp.common.domain.replace;

import com.lxzl.erp.common.domain.replace.pojo.ReplaceOrder;
import com.lxzl.erp.common.domain.system.pojo.Image;


/**
 * @Author: Sunzhipeng
 * @Description:
 * @Date: Created in 2018\9\12 0012 15:53
 */
public class ReplaceOrderConfirmChangeParam {
    private ReplaceOrder replaceOrder;
    private Image deliveryNoteCustomerSignImg;//交货单客户签字
    private String changeReason;

    public ReplaceOrder getReplaceOrder() {
        return replaceOrder;
    }

    public void setReplaceOrder(ReplaceOrder replaceOrder) {
        this.replaceOrder = replaceOrder;
    }

    public Image getDeliveryNoteCustomerSignImg() {
        return deliveryNoteCustomerSignImg;
    }

    public void setDeliveryNoteCustomerSignImg(Image deliveryNoteCustomerSignImg) {
        this.deliveryNoteCustomerSignImg = deliveryNoteCustomerSignImg;
    }

    public String getChangeReason() {
        return changeReason;
    }

    public void setChangeReason(String changeReason) {
        this.changeReason = changeReason;
    }
}
