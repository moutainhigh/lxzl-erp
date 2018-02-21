package com.lxzl.erp.common.domain.k3.pojo.callback;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;
import java.util.List;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2018-02-19 13:39
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class K3DeliveryOrder {

    private String orderNo;
    private Date deliveryTime;
    private List<String> equipmentNo;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Date getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Date deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public List<String> getEquipmentNo() {
        return equipmentNo;
    }

    public void setEquipmentNo(List<String> equipmentNo) {
        this.equipmentNo = equipmentNo;
    }
}
