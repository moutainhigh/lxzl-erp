package com.lxzl.erp.core.service.k3;


import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.delivery.pojo.DeliveryOrder;
import com.lxzl.erp.common.domain.k3.pojo.returnOrder.K3ReturnOrder;
import com.lxzl.erp.dataaccess.domain.k3.returnOrder.K3ReturnOrderDO;
import com.lxzl.se.core.service.BaseService;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2018-02-19 13:37
 */
public interface K3CallbackService extends BaseService {

    ServiceResult<String, String> callbackDelivery(DeliveryOrder deliveryOrder);

    ServiceResult<String, String> callbackCancelOrder(String orderNo,Integer cancelOrderReasonType);

    ServiceResult<String,String> callbackReturnOrder(K3ReturnOrder k3ReturnOrder);

    ServiceResult<String, String> callbackReturnDetail(K3ReturnOrder k3ReturnOrder,K3ReturnOrderDO k3ReturnOrderDO,Boolean isHandleRent);
}
