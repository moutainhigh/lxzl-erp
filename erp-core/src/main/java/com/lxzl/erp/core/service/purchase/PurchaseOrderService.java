package com.lxzl.erp.core.service.purchase;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.purchase.PurchaseOrderCommitParam;
import com.lxzl.erp.common.domain.purchase.PurchaseOrderQueryParam;
import com.lxzl.erp.common.domain.purchase.pojo.PurchaseOrder;
import com.lxzl.erp.core.service.VerifyReceiver;


public interface PurchaseOrderService extends VerifyReceiver {

    ServiceResult<String,Integer> add(PurchaseOrder purchaseOrder);
    ServiceResult<String,Integer> update(PurchaseOrder purchaseOrder);
    ServiceResult<String,PurchaseOrder> queryPurchaseOrderByNo(String purchaseNo);
    ServiceResult<String,Page<PurchaseOrder>> page(PurchaseOrderQueryParam purchaseOrderQueryParam);
    ServiceResult<String,Integer> commit(PurchaseOrderCommitParam purchaseOrderCommitParam);
    ServiceResult<String,Integer> delete(PurchaseOrder purchaseOrder);
}
