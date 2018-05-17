package com.lxzl.erp.common.domain.order.pojo;

import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.base.BasePO;
import com.lxzl.erp.common.domain.validGroup.AddGroup;
import com.lxzl.erp.common.domain.validGroup.UpdateGroup;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>Description: </p>
 *
 * @Auther: huahongbin
 * @Date: 2018/5/14
 */
public class OrderJointProduct extends BasePO {
    private Integer orderJointProductId; // 订单组合商品id
    private Integer orderId; // 订单id;

    @NotNull(message = ErrorCode.JOINT_PRODUCT_ID_IS_NULL, groups = {UpdateGroup.class, AddGroup.class})
    private Integer jointProductId; // 组合商品id
    @Min(value = 1, message = ErrorCode.ORDER_JOINT_PRODUCT_ERROR, groups = {AddGroup.class, UpdateGroup.class})
    @NotNull(message = ErrorCode.ORDER_JOINT_PRODUCT_ERROR, groups = {UpdateGroup.class, AddGroup.class})
    private Integer jointProductCount; // 组合商品数量

    List<OrderProduct> orderProductList; // 订单组合商品中商品项列表
    List<OrderMaterial> orderMaterialList; // 订单组合商品中配件项列表

    public Integer getOrderJointProductId() {
        return orderJointProductId;
    }

    public void setOrderJointProductId(Integer orderJointProductId) {
        this.orderJointProductId = orderJointProductId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getJointProductId() {
        return jointProductId;
    }

    public void setJointProductId(Integer jointProductId) {
        this.jointProductId = jointProductId;
    }

    public Integer getJointProductCount() {
        return jointProductCount;
    }

    public void setJointProductCount(Integer jointProductCount) {
        this.jointProductCount = jointProductCount;
    }

    public List<OrderProduct> getOrderProductList() {
        return orderProductList;
    }

    public void setOrderProductList(List<OrderProduct> orderProductList) {
        this.orderProductList = orderProductList;
    }

    public List<OrderMaterial> getOrderMaterialList() {
        return orderMaterialList;
    }

    public void setOrderMaterialList(List<OrderMaterial> orderMaterialList) {
        this.orderMaterialList = orderMaterialList;
    }
}
