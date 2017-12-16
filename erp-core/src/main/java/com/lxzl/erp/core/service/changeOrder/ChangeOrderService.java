package com.lxzl.erp.core.service.changeOrder;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.changeOrder.*;
import com.lxzl.erp.common.domain.changeOrder.pojo.ChangeOrder;
import com.lxzl.erp.common.domain.changeOrder.pojo.ChangeOrderMaterial;
import com.lxzl.erp.common.domain.changeOrder.pojo.ChangeOrderMaterialBulk;
import com.lxzl.erp.common.domain.changeOrder.pojo.ChangeOrderProductEquipment;
import com.lxzl.erp.core.service.VerifyReceiver;

public interface ChangeOrderService extends VerifyReceiver {

    ServiceResult<String, String> add(AddChangeOrderParam addChangeOrderParam);

    ServiceResult<String, String> update(UpdateChangeOrderParam updateChangeOrderParam);

    ServiceResult<String, String> commit(ChangeOrderCommitParam changeOrderCommitParam);

    ServiceResult<String, String> stockUpForChange(StockUpForChangeParam stockUpForChangeParam);

    ServiceResult<String, String> delivery(ChangeOrder changeOrder);

    ServiceResult<String, String> end(ChangeOrder changeOrder);

    ServiceResult<String, String> cancel(ChangeOrder changeOrder);

    ServiceResult<String, ChangeOrder> detail(ChangeOrder changeOrder);

    ServiceResult<String, Page<ChangeOrder>> page(ChangeOrderPageParam changeOrderPageParam);

    ServiceResult<String, Page<ChangeOrderProductEquipment>> pageChangeOrderProductEquipment(ChangeOrder changeOrder);

    ServiceResult<String, Page<ChangeOrderMaterialBulk>> pageChangeOrderMaterialBulk(ChangeOrder changeOrder);

    ServiceResult<String, String> doChangeEquipment(ChangeOrderProductEquipment changeOrderProductEquipment);

    ServiceResult<String, String> doChangeMaterial(ChangeOrderMaterial changeOrderMaterial);

    ServiceResult<String, String> processNoChangeEquipment(ProcessNoChangeEquipmentParam processNoChangeEquipmentParam);
}
