package com.lxzl.erp.common.domain.changeOrder.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.base.BasePO;
import com.lxzl.erp.common.util.validate.constraints.In;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;


@JsonIgnoreProperties(ignoreUnknown = true)
public class ChangeOrderProduct extends BasePO {

    private Integer changeOrderProductId;   //唯一标识
    private Integer changeOrderId;   //换货ID
    private String changeOrderNo;   //换货编号
    @NotNull(message = ErrorCode.PRODUCT_SKU_NOT_NULL)
    private Integer srcChangeProductSkuId;   //原商品SKU_ID
    @NotNull(message = ErrorCode.PRODUCT_SKU_NOT_NULL)
    private Integer destChangeProductSkuId;   //目标商品SKU_ID
    @NotNull(message = ErrorCode.CHANGE_COUNT_ERROR)
    @Min(value = 1, message = ErrorCode.CHANGE_COUNT_ERROR)
    private Integer changeProductSkuCount;   //换货商品SKU数量
    private Integer realChangeProductSkuCount;   //实际换货数量
    private String srcChangeProductSkuSnapshot;   //原商品SKU快照
    private String destChangeProductSkuSnapshot;   //目标商品SKU快照
    private Integer dataStatus;   //状态：0不可用；1可用；2删除
    private String remark;   //备注
    private Date createTime;   //添加时间
    private String createUser;   //添加人
    private Date updateTime;   //添加时间
    private String updateUser;   //修改人

    private Integer canProcessCount;    //可换数量
    @NotNull(message = ErrorCode.IS_NEW_VALUE_ERROR)
    @In(value = {CommonConstant.NO,CommonConstant.YES}, message = ErrorCode.IS_NEW_VALUE_ERROR)
    private Integer isNew;


    public Integer getChangeOrderProductId() {
        return changeOrderProductId;
    }

    public void setChangeOrderProductId(Integer changeOrderProductId) {
        this.changeOrderProductId = changeOrderProductId;
    }

    public Integer getChangeOrderId() {
        return changeOrderId;
    }

    public void setChangeOrderId(Integer changeOrderId) {
        this.changeOrderId = changeOrderId;
    }

    public String getChangeOrderNo() {
        return changeOrderNo;
    }

    public void setChangeOrderNo(String changeOrderNo) {
        this.changeOrderNo = changeOrderNo;
    }

    public Integer getSrcChangeProductSkuId() {
        return srcChangeProductSkuId;
    }

    public void setSrcChangeProductSkuId(Integer srcChangeProductSkuId) {
        this.srcChangeProductSkuId = srcChangeProductSkuId;
    }

    public Integer getDestChangeProductSkuId() {
        return destChangeProductSkuId;
    }

    public void setDestChangeProductSkuId(Integer destChangeProductSkuId) {
        this.destChangeProductSkuId = destChangeProductSkuId;
    }

    public Integer getChangeProductSkuCount() {
        return changeProductSkuCount;
    }

    public void setChangeProductSkuCount(Integer changeProductSkuCount) {
        this.changeProductSkuCount = changeProductSkuCount;
    }

    public Integer getRealChangeProductSkuCount() {
        return realChangeProductSkuCount;
    }

    public void setRealChangeProductSkuCount(Integer realChangeProductSkuCount) {
        this.realChangeProductSkuCount = realChangeProductSkuCount;
    }

    public String getSrcChangeProductSkuSnapshot() {
        return srcChangeProductSkuSnapshot;
    }

    public void setSrcChangeProductSkuSnapshot(String srcChangeProductSkuSnapshot) {
        this.srcChangeProductSkuSnapshot = srcChangeProductSkuSnapshot;
    }

    public String getDestChangeProductSkuSnapshot() {
        return destChangeProductSkuSnapshot;
    }

    public void setDestChangeProductSkuSnapshot(String destChangeProductSkuSnapshot) {
        this.destChangeProductSkuSnapshot = destChangeProductSkuSnapshot;
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

    public Integer getIsNew() {
        return isNew;
    }

    public void setIsNew(Integer isNew) {
        this.isNew = isNew;
    }
}