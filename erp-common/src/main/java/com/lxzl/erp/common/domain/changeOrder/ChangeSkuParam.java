package com.lxzl.erp.common.domain.changeOrder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ChangeSkuParam {

    @NotNull(message = ErrorCode.PRODUCT_SKU_NOT_NULL, groups = {AddChangeOrderParam.class})
    private Integer skuId;
    //退还数量换货数量
    @NotNull(message = ErrorCode.CHANGE_COUNT_ERROR,groups = {AddChangeOrderParam.class})
    @Min(value=1,message = ErrorCode.CHANGE_COUNT_ERROR,groups = {AddChangeOrderParam.class})
    private Integer changeCount;

    public Integer getSkuId() {
        return skuId;
    }

    public void setSkuId(Integer skuId) {
        this.skuId = skuId;
    }

    public Integer getChangeCount() {
        return changeCount;
    }

    public void setChangeCount(Integer changeCount) {
        this.changeCount = changeCount;
    }
}
