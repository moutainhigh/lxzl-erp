package com.lxzl.erp.core.service.purchaseApply.impl;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.purchaseApply.PurchaseApplyOrderCommitParam;
import com.lxzl.erp.common.domain.purchaseApply.PurchaseApplyOrderPageParam;
import com.lxzl.erp.common.domain.purchaseApply.pojo.PurchaseApplyOrder;
import com.lxzl.erp.core.service.purchaseApply.PurchaseApplyOrderService;
import org.springframework.stereotype.Service;

@Service
public class PurchaseApplyOrderServiceImpl implements PurchaseApplyOrderService {

    @Override
    public ServiceResult<String, String> add(PurchaseApplyOrder purchaseApplyOrder) {
        return null;
    }

    @Override
    public ServiceResult<String, String> update(PurchaseApplyOrder purchaseApplyOrder) {
        return null;
    }

    @Override
    public ServiceResult<String, String> commit(PurchaseApplyOrderCommitParam purchaseApplyOrderCommitParam) {
        return null;
    }

    @Override
    public String cancel(String purchaseApplyOrderNo) {
        return null;
    }

    @Override
    public ServiceResult<String, PurchaseApplyOrder> queryByNo(String purchaseApplyOrderNo) {
        return null;
    }

    @Override
    public ServiceResult<String, Page<PurchaseApplyOrder>> queryAll(PurchaseApplyOrderPageParam purchaseApplyOrderPageParam) {
        return null;
    }

    @Override
    public boolean receiveVerifyResult(boolean verifyResult, String businessNo) {
        return false;
    }
}
