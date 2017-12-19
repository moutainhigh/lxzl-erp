package com.lxzl.erp.core.service.returnOrder;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.material.pojo.Material;
import com.lxzl.erp.common.domain.product.pojo.ProductEquipment;
import com.lxzl.erp.common.domain.returnOrder.*;
import com.lxzl.erp.common.domain.returnOrder.pojo.ReturnOrder;
import com.lxzl.erp.common.domain.returnOrder.pojo.ReturnOrderMaterialBulk;
import com.lxzl.erp.common.domain.returnOrder.pojo.ReturnOrderProductEquipment;
import com.lxzl.erp.core.service.VerifyReceiver;

public interface ReturnOrderService extends VerifyReceiver {
    ServiceResult<String, String> add(AddReturnOrderParam addReturnOrderParam);

    ServiceResult<String, ProductEquipment> doReturnEquipment(DoReturnEquipmentParam doReturnEquipmentParam);

    ServiceResult<String, Material> doReturnMaterial(DoReturnMaterialParam doReturnMaterialParam);

    ServiceResult<String, ReturnOrder> detail(ReturnOrder returnOrder);

    ServiceResult<String, Page<ReturnOrder>> page(ReturnOrderPageParam returnOrderPageParam);

    ServiceResult<String, String> end(ReturnOrder returnOrder);

    ServiceResult<String, String> cancel(ReturnOrder returnOrder);

    ServiceResult<String, Page<ReturnOrderProductEquipment>> pageReturnEquipment(ReturnEquipmentPageParam returnEquipmentPageParam);

    ServiceResult<String, Page<ReturnOrderMaterialBulk>> pageReturnBulk(ReturnBulkPageParam returnBulkPageParam);

    ServiceResult<String, String> update(UpdateReturnOrderParam updateReturnOrderParam);

    ServiceResult<String, String> commit(ReturnOrderCommitParam returnOrderCommitParam);
}
