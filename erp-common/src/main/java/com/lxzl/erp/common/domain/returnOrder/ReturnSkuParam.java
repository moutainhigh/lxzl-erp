package com.lxzl.erp.common.domain.returnOrder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.changeOrder.AddChangeOrderParam;
import com.lxzl.erp.common.domain.validGroup.returnOrder.AddReturnOrderGroup;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReturnSkuParam {

    @NotNull(message = ErrorCode.PRODUCT_SKU_NOT_NULL)
    private Integer skuId;
    //退还数量换货数量
    @NotNull(message = ErrorCode.RETURN_COUNT_ERROR)
    @Min(value = 1, message = ErrorCode.RETURN_COUNT_ERROR)
    private Integer returnCount;

    public Integer getSkuId() {
        return skuId;
    }

    public void setSkuId(Integer skuId) {
        this.skuId = skuId;
    }

    public Integer getReturnCount() {
        return returnCount;
    }

    public void setReturnCount(Integer returnCount) {
        this.returnCount = returnCount;
    }
}
