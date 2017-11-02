package com.lxzl.erp.core.service.purchase;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.purchase.PurchaseOrderQueryParam;
import com.lxzl.erp.common.domain.purchase.pojo.PurchaseOrder;

public interface PurchaseOrderService {

    ServiceResult<String,Integer> add(PurchaseOrder purchaseOrder);
    ServiceResult<String,Integer> update(PurchaseOrder purchaseOrder);
    ServiceResult<String,PurchaseOrder> getById(Integer purchaseId);
    ServiceResult<String,Page<PurchaseOrder>> page(PurchaseOrderQueryParam purchaseOrderQueryParam);

}
