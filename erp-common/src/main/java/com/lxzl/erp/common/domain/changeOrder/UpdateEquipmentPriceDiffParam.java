package com.lxzl.erp.common.domain.changeOrder;

import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.changeOrder.pojo.ChangeOrderProductEquipment;
import com.lxzl.erp.common.domain.validGroup.changeOrder.UpdatePriceDiffGroup;
import com.lxzl.erp.common.util.validate.constraints.CollectionNotNull;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author lk
 * @Description: TODO
 * @date 2018/2/8 20:12
 */
public class UpdateEquipmentPriceDiffParam {

    @NotNull(message = ErrorCode.CHANGE_ORDER_NO_NOT_NULL,groups = {UpdatePriceDiffGroup.class})
    private String changeOrderNo;
    @Valid
    @CollectionNotNull(message = ErrorCode.RECORD_NOT_EXISTS,groups = {UpdatePriceDiffGroup.class})
    private List<ChangeOrderProductEquipment> changeOrderProductEquipmentList;

    public String getChangeOrderNo() {
        return changeOrderNo;
    }

    public void setChangeOrderNo(String changeOrderNo) {
        this.changeOrderNo = changeOrderNo;
    }

    public List<ChangeOrderProductEquipment> getChangeOrderProductEquipmentList() {
        return changeOrderProductEquipmentList;
    }

    public void setChangeOrderProductEquipmentList(List<ChangeOrderProductEquipment> changeOrderProductEquipmentList) {
        this.changeOrderProductEquipmentList = changeOrderProductEquipmentList;
    }
}
