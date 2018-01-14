package com.lxzl.erp.common.domain.transferOrder;

import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.base.BasePageParam;
import com.lxzl.erp.common.domain.validGroup.QueryGroup;

import javax.validation.constraints.NotNull;

/**
 * @Author: your name
 * @Description：
 * @Date: Created in 15:43 2018/1/14
 * @Modified By:
 */
public class TransferOrderProductEquipmentQueryParam extends BasePageParam {
    @NotNull(message = ErrorCode.TRANSFER_ORDER_PRODUCT_ID_NOT_NULL,groups = {QueryGroup.class})
    private Integer transferOrderProductId;   //转移单商品项ID

    public Integer getTransferOrderProductId() {
        return transferOrderProductId;
    }

    public void setTransferOrderProductId(Integer transferOrderProductId) {
        this.transferOrderProductId = transferOrderProductId;
    }
}
