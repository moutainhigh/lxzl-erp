package com.lxzl.erp.common.domain.changeOrder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.base.BasePageParam;

import javax.validation.constraints.NotNull;

/**
 * @author lk
 * @Description: TODO
 * @date 2018/2/8 17:07
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class ChangeOrderProductEquipmentPageParam extends BasePageParam{
    @NotNull(message = ErrorCode.CHANGE_ORDER_PRODUCT_ID_NOT_NULL)
    private Integer changeOrderProductId;

    public Integer getChangeOrderProductId() {
        return changeOrderProductId;
    }

    public void setChangeOrderProductId(Integer changeOrderProductId) {
        this.changeOrderProductId = changeOrderProductId;
    }
}
