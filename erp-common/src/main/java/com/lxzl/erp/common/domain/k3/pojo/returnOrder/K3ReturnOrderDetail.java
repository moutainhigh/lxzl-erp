package com.lxzl.erp.common.domain.k3.pojo.returnOrder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.base.BasePO;
import com.lxzl.erp.common.domain.validGroup.AddGroup;
import org.hibernate.validator.constraints.NotBlank;
import java.util.Date;


@JsonIgnoreProperties(ignoreUnknown = true)
public class K3ReturnOrderDetail extends BasePO {

    private Integer k3ReturnOrderDetailId;   //唯一标识
    private Integer returnOrderId;   //K3退货单ID
    @NotBlank(message = ErrorCode.ORDER_NO_NOT_NULL, groups = {AddGroup.class})
    private String orderNo;   //订单号
    @NotBlank(message = ErrorCode.ORDER_ITEM_ID_NOT_NULL, groups = {AddGroup.class})
    private String orderItemId;    // 订单项ID
    private String orderEntry;   //订单行号
    @NotBlank(message = ErrorCode.PRODUCT_NO_IS_NULL, groups = {AddGroup.class})
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
}