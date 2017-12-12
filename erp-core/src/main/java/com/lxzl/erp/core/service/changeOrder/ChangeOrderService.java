package com.lxzl.erp.core.service.changeOrder;

import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.changeOrder.AddChangeOrderParam;
import com.lxzl.erp.common.domain.changeOrder.ChangeOrderCommitParam;
import com.lxzl.erp.common.domain.changeOrder.StockUpByChangeParam;
import com.lxzl.erp.core.service.VerifyReceiver;

public interface ChangeOrderService extends VerifyReceiver {

    ServiceResult<String,String> add(AddChangeOrderParam addChangeOrderParam);

    ServiceResult<String,String> commit(ChangeOrderCommitParam changeOrderCommitParam);

    ServiceResult<String,String> stockUpByChange(StockUpByChangeParam stockUpByChangeParam);
}
