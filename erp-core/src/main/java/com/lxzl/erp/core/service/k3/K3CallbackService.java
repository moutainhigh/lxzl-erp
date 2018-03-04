package com.lxzl.erp.core.service.k3;


import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.delivery.pojo.DeliveryOrder;
import com.lxzl.erp.common.domain.k3.pojo.callback.K3DeliveryOrder;
import com.lxzl.se.core.service.BaseService;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2018-02-19 13:37
 */
public interface K3CallbackService extends BaseService {

    ServiceResult<String, String> callbackDelivery(DeliveryOrder deliveryOrder);
}
