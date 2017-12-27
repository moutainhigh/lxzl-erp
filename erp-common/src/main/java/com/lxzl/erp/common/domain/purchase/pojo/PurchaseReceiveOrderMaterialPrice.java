package com.lxzl.erp.common.domain.purchase.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.validGroup.UpdateGroup;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PurchaseReceiveOrderMaterialPrice {
    @NotNull(message = ErrorCode.MATERIAL_COUNT_ERROR,groups = {UpdateGroup.class})
    @Min(value =1 ,message = ErrorCode.COUNT_MORE_THAN_ZERO,groups = {UpdateGroup.class})
    private Integer count;
    @NotNull(message = ErrorCode.MATERIAL_PRICE_ERROR,groups = {UpdateGroup.class})
    @Min(value =0 ,message = ErrorCode.MATERIAL_PRICE_ERROR,groups = {UpdateGroup.class})
    private BigDecimal price;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
