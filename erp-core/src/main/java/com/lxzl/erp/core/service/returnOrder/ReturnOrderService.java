package com.lxzl.erp.core.service.returnOrder;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.product.pojo.ProductEquipment;
import com.lxzl.erp.common.domain.returnOrder.AddReturnOrderParam;
import com.lxzl.erp.common.domain.returnOrder.DoReturnEquipmentParam;
import com.lxzl.erp.common.domain.returnOrder.ReturnOrderPageParam;
import com.lxzl.erp.common.domain.returnOrder.pojo.ReturnOrder;

public interface ReturnOrderService {
    ServiceResult<String,String> create(AddReturnOrderParam addReturnOrderParam);
    ServiceResult<String, ProductEquipment> doReturnEquipment(DoReturnEquipmentParam doReturnEquipmentParam);
    ServiceResult<String, ReturnOrder> detail(ReturnOrder returnOrder);
    ServiceResult<String, Page<ReturnOrder>> page(ReturnOrderPageParam returnOrderPageParam);
}
