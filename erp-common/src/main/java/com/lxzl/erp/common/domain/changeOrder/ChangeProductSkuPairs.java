package com.lxzl.erp.common.domain.changeOrder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.validGroup.changeOrder.AddChangeOrderGroup;
import com.lxzl.erp.common.domain.validGroup.changeOrder.UpdateChangeOrderGroup;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ChangeProductSkuPairs {

    private Integer changeOrderProductId;
    @NotNull(message = ErrorCode.PRODUCT_SKU_NOT_NULL)
    private Integer productSkuIdSrc;
    @NotNull(message = ErrorCode.PRODUCT_SKU_NOT_NULL)
    private Integer productSkuIdDest;
    @NotNull(message = ErrorCode.CHANGE_COUNT_ERROR)
    @Min(value = 0, message = ErrorCode.CHANGE_COUNT_ERROR)
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
