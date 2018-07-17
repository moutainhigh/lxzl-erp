package com.lxzl.erp.core.service.workbench;

import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.k3.pojo.returnOrder.K3ReturnOrderQueryParam;
import com.lxzl.erp.common.domain.order.OrderQueryParam;

/**
 * @Author: your name
 * @Description：
 * @Date: Created in 17:07 2018/7/16
 * @Modified By:
 */
public interface WorkbenchService {

    ServiceResult<String,Integer> queryVerifingOrder(OrderQueryParam orderQueryParam);

    ServiceResult<String,Integer> queryReturnOrder(K3ReturnOrderQueryParam k3ReturnOrderQueryParam);
}
