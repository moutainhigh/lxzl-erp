package com.lxzl.erp.core.service.purchaseApply;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.purchaseApply.PurchaseApplyOrderCommitParam;
import com.lxzl.erp.common.domain.purchaseApply.PurchaseApplyOrderPageParam;
import com.lxzl.erp.common.domain.purchaseApply.pojo.PurchaseApplyOrder;
import com.lxzl.erp.core.service.VerifyReceiver;

public interface PurchaseApplyOrderService extends VerifyReceiver {

    ServiceResult<String, String> add(PurchaseApplyOrder purchaseApplyOrder);

    ServiceResult<String, String> update(PurchaseApplyOrder purchaseApplyOrder);

    ServiceResult<String, String> commit(PurchaseApplyOrderCommitParam purchaseApplyOrderCommitParam);

    String cancel(String purchaseApplyOrderNo);

    ServiceResult<String, PurchaseApplyOrder> queryByNo(String purchaseApplyOrderNo);

    ServiceResult<String, Page<PurchaseApplyOrder>> queryAll(PurchaseApplyOrderPageParam purchaseApplyOrderPageParam);
}
