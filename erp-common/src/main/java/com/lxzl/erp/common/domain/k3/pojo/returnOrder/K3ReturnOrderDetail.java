package com.lxzl.erp.common.domain.k3.pojo.returnOrder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.base.BasePO;
import com.lxzl.erp.common.domain.k3.group.AddK3ReturnOrderGroup;
import com.lxzl.erp.common.domain.k3.pojo.order.OrderMaterial;
import com.lxzl.erp.common.domain.k3.pojo.order.OrderProduct;
import com.lxzl.erp.common.domain.validGroup.AddGroup;
import org.hibernate.validator.constraints.NotBlank;

import java.beans.Transient;
import java.util.Date;


@JsonIgnoreProperties(ignoreUnknown = true)
public class K3ReturnOrderDetail extends BasePO {

    private Integer k3ReturnOrderDetailId;   //唯一标识
    private Integer returnOrderId;   //K3退货单ID
    private String returnOrderNo;//K3退货单号
    @NotBlank(message = ErrorCode.ORDER_NO_NOT_NULL, groups = {AddGroup.class , AddK3ReturnOrderGroup.class})
    private String orderNo;   //订单号
    @NotBlank(message = ErrorCode.ORDER_ITEM_ID_NOT_NULL, groups = {AddGroup.class, AddK3ReturnOrderGroup.class})
    private String orderItemId;    // 订单项ID
    @NotBlank(message = ErrorCode.ORDER_ROW_ID_NOT_NULL, groups = {AddK3ReturnOrderGroup.class})
    private String orderEntry;   //订单行号
    @NotBlank(message = ErrorCode.PRODUCT_NO_IS_NULL, groups = {AddGroup.class, AddK3ReturnOrderGroup.class})
    private String productNo;   //产品代码
    private String productName;   //产品名称
    private Integer productCount;   //退货数量
    private Integer realProductCount;   //实际退货数量
    private Integer dataStatus;   //状态：0不可用；1可用；2删除
    private String remark;   //备注
    private Date createTime;   //添加时间
    private String createUser;   //添加人
    private Date updateTime;   //修改时间
    private String updateUser;   //修改人
    private OrderProduct orderProduct;//关联订单商品
    private OrderMaterial orderMaterial;//关联商品物料

    private Date returnTime;  //退货时间
    private Integer orderItemType;//商品类型,1-商品,2-配件
    @NotBlank(message = ErrorCode.ORIGINAL_ORDER_NO_NOT_NULL, groups = {AddGroup.class , AddK3ReturnOrderGroup.class})
    private String originalOrderNo;  //原订单单号

    public Integer getK3ReturnOrderDetailId() {
        return k3ReturnOrderDetailId;
    }

    public void setK3ReturnOrderDetailId(Integer k3ReturnOrderDetailId) {
        this.k3ReturnOrderDetailId = k3ReturnOrderDetailId;
    }

    public Integer getReturnOrderId() {
        return returnOrderId;
    }

    public void setReturnOrderId(Integer returnOrderId) {
        this.returnOrderId = returnOrderId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderEntry() {
        return orderEntry;
    }

    public void setOrderEntry(String orderEntry) {
        this.orderEntry = orderEntry;
    }

    public String getProductNo() {
        return productNo;
    }

    public void setProductNo(String productNo) {
        this.productNo = productNo;
    }

    public Integer getProductCount() {
        return productCount;
    }

    public void setProductCount(Integer productCount) {
        this.productCount = productCount;
    }

    public Integer getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(Integer dataStatus) {
        this.dataStatus = dataStatus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(String orderItemId) {
        this.orderItemId = orderItemId;
    }

    public Integer getRealProductCount() {
        return realProductCount;
    }

    public void setRealProductCount(Integer realProductCount) {
        this.realProductCount = realProductCount;
    }

    public String getReturnOrderNo() {
        return returnOrderNo;
    }

    public void setReturnOrderNo(String returnOrderNo) {
        this.returnOrderNo = returnOrderNo;
    }

    public OrderProduct getOrderProduct() {
        return orderProduct;
    }

    public void setOrderProduct(OrderProduct orderProduct) {
        this.orderProduct = orderProduct;
    }

    public OrderMaterial getOrderMaterial() {
        return orderMaterial;
    }

    public void setOrderMaterial(OrderMaterial orderMaterial) {
        this.orderMaterial = orderMaterial;
    }

    public Date getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(Date returnTime) {
        this.returnTime = returnTime;
    }

    public Integer getOrderItemType() {
        return orderItemType;
    }

    public void setOrderItemType(Integer orderItemType) {
        this.orderItemType = orderItemType;
    }

    public String getOriginalOrderNo() {
        return originalOrderNo;
    }

    public void setOriginalOrderNo(String originalOrderNo) {
        this.originalOrderNo = originalOrderNo;
    }
}