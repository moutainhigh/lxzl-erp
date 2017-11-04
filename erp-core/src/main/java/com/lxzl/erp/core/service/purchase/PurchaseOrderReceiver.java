package com.lxzl.erp.core.service.purchase;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.purchase.PurchaseOrderQueryParam;
import com.lxzl.erp.common.domain.purchase.pojo.PurchaseOrder;
import com.lxzl.erp.core.service.VerifyReceiver;


public interface PurchaseOrderReceiver extends VerifyReceiver {

    ServiceResult<String,Integer> add(PurchaseOrder purchaseOrder);
    ServiceResult<String,Integer> update(PurchaseOrder purchaseOrder);
    ServiceResult<String,PurchaseOrder> getById(Integer purchaseId);
    ServiceResult<String,Page<PurchaseOrder>> page(PurchaseOrderQueryParam purchaseOrderQueryParam);
    ServiceResult<String,Integer> commit(PurchaseOrder purchaseOrder);

}
