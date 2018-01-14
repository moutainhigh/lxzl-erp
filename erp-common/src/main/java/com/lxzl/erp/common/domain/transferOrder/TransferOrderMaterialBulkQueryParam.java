package com.lxzl.erp.common.domain.transferOrder;

import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.base.BasePageParam;
import com.lxzl.erp.common.domain.validGroup.QueryGroup;

import javax.validation.constraints.NotNull;

/**
 * @Author: your name
 * @Description：
 * @Date: Created in 15:44 2018/1/14
 * @Modified By:
 */
public class TransferOrderMaterialBulkQueryParam extends BasePageParam {
    @NotNull(message = ErrorCode.TRANSFER_ORDER_MATERIAL_ID_NOT_NULL,groups = {QueryGroup.class})
    private Integer transferOrderMaterialId;   //转移单配件物料项ID

    public Integer getTransferOrderMaterialId() {
        return transferOrderMaterialId;
    }

    public void setTransferOrderMaterialId(Integer transferOrderMaterialId) {
        this.transferOrderMaterialId = transferOrderMaterialId;
    }
}
