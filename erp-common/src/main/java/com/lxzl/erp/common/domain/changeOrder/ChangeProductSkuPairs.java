package com.lxzl.erp.common.domain.changeOrder;

import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.validGroup.changeOrder.AddChangeOrderGroup;
import com.lxzl.erp.common.domain.validGroup.changeOrder.UpdateChangeOrderGroup;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class ChangeProductSkuPairs {

    private Integer changeOrderProductId;
    @NotNull(message = ErrorCode.PRODUCT_SKU_NOT_NULL, groups = {AddChangeOrderGroup.class, UpdateChangeOrderGroup.class})
    private Integer productSkuIdSrc;
    @NotNull(message = ErrorCode.PRODUCT_SKU_NOT_NULL, groups = {AddChangeOrderGroup.class, UpdateChangeOrderGroup.class})
    private Integer productSkuIdDest;
    @NotNull(message = ErrorCode.CHANGE_COUNT_ERROR, groups = {AddChangeOrderGroup.class, UpdateChangeOrderGroup.class})
    @Min(value = 0, message = ErrorCode.CHANGE_COUNT_ERROR, groups = {AddChangeOrderGroup.class, UpdateChangeOrderGroup.class})
    private Integer changeCount;

    public Integer getChangeOrderProductId() {
        return changeOrderProductId;
    }

    public void setChangeOrderProductId(Integer changeOrderProductId) {
        this.changeOrderProductId = changeOrderProductId;
    }

    public Integer getProductSkuIdSrc() {
        return productSkuIdSrc;
    }

    public void setProductSkuIdSrc(Integer productSkuIdSrc) {
        this.productSkuIdSrc = productSkuIdSrc;
    }

    public Integer getProductSkuIdDest() {
        return productSkuIdDest;
    }

    public void setProductSkuIdDest(Integer productSkuIdDest) {
        this.productSkuIdDest = productSkuIdDest;
    }

    public Integer getChangeCount() {
        return changeCount;
    }

    public void setChangeCount(Integer changeCount) {
        this.changeCount = changeCount;
    }
}
