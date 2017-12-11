package com.lxzl.erp.core.service.changeOrder;

import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.changeOrder.AddChangeOrderParam;

public interface ChangeOrderService {

    ServiceResult<String,String> add(AddChangeOrderParam addChangeOrderParam);
}
