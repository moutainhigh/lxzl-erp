package com.lxzl.erp.common.domain.returnOrder.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;


@JsonIgnoreProperties(ignoreUnknown = true)
public class ReturnOrderProduct implements Serializable {

    private Integer returnOrderProductId;   //唯一标识
    private Integer returnOrderId;   //退还ID
    private String returnOrderNo;   //退还编号
    @NotNull(message = ErrorCode.PRODUCT_SKU_NOT_NULL)
    private Integer returnProductSkuId;   //退还商品SKU_ID
    @NotNull(message = ErrorCode.RETURN_COUNT_ERROR)
    @Min(value = 1, message = ErrorCode.RETURN_COUNT_ERROR)
    private Integer returnProductSkuCount;   //退还商品SKU数量
    private Integer realReturnProductSkuCount;   //实际退还商品数量
    private String returnProductSkuSnapshot;   //退还商品SKU快照
    private Integer dataStatus;   //状态：0不可用；1可用；2删除
    private String remark;   //备注
    private Date createTime;   //添加时间
    private String createUser;   //添加人
    private Date updateTime;   //添加时间
    private String updateUser;   //修改人

    private Integer canProcessCount;


    public Integer getReturnOrderProductId() {
        return returnOrderProductId;
    }

    public void setReturnOrderProductId(Integer returnOrderProductId) {
        this.returnOrderProductId = returnOrderProductId;
    }

    public Integer getReturnOrderId() {
        return returnOrderId;
    }

    public void setReturnOrderId(Integer returnOrderId) {
        this.returnOrderId = returnOrderId;
    }

    public String getReturnOrderNo() {
        return returnOrderNo;
    }

    public void setReturnOrderNo(String returnOrderNo) {
        this.returnOrderNo = returnOrderNo;
    }

    public Integer getReturnProductSkuId() {
        return returnProductSkuId;
    }

    public void setReturnProductSkuId(Integer returnProductSkuId) {
        this.returnProductSkuId = returnProductSkuId;
    }

    public Integer getReturnProductSkuCount() {
        return returnProductSkuCount;
    }

    public void setReturnProductSkuCount(Integer returnProductSkuCount) {
        this.returnProductSkuCount = returnProductSkuCount;
    }

    public Integer getRealReturnProductSkuCount() {
        return realReturnProductSkuCount;
    }

    public void setRealReturnProductSkuCount(Integer realReturnProductSkuCount) {
        this.realReturnProductSkuCount = realReturnProductSkuCount;
    }

    public String getReturnProductSkuSnapshot() {
        return returnProductSkuSnapshot;
    }

    public void setReturnProductSkuSnapshot(String returnProductSkuSnapshot) {
        this.returnProductSkuSnapshot = returnProductSkuSnapshot;
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

    public Integer getCanProcessCount() {
        return canProcessCount;
    }

    public void setCanProcessCount(Integer canProcessCount) {
        this.canProcessCount = canProcessCount;
    }
}