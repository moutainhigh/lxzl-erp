package com.lxzl.erp.core.service.k3;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.k3.K3ChangeOrderCommitParam;
import com.lxzl.erp.common.domain.k3.K3OrderQueryParam;
import com.lxzl.erp.common.domain.k3.K3ReturnOrderCommitParam;
import com.lxzl.erp.common.domain.k3.pojo.K3ChangeOrder;
import com.lxzl.erp.common.domain.k3.pojo.changeOrder.K3ChangeOrderQueryParam;
import com.lxzl.erp.common.domain.k3.pojo.order.Order;
import com.lxzl.erp.common.domain.k3.pojo.returnOrder.K3ReturnOrder;
import com.lxzl.erp.common.domain.k3.pojo.returnOrder.K3ReturnOrderDetail;
import com.lxzl.erp.common.domain.k3.pojo.returnOrder.K3ReturnOrderQueryParam;
import com.lxzl.erp.core.service.VerifyReceiver;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2018-02-11 20:54
 */
public interface K3Service extends VerifyReceiver {

    /**
     * 根据参数查询订单
     *
     * @param param 查询订单参数
     * @return 订单列表
     */
    ServiceResult<String, Page<Order>> queryAllOrder(K3OrderQueryParam param);

    ServiceResult<String, Order> queryOrder(String orderNo);

    ServiceResult<String, String> createReturnOrder(K3ReturnOrder k3ReturnOrder);

    ServiceResult<String, String> updateReturnOrder(K3ReturnOrder k3ReturnOrder);

    ServiceResult<String, String> addReturnOrder(K3ReturnOrder k3ReturnOrder);

    ServiceResult<String, String> deleteReturnOrder(Integer k3ReturnOrderDetailId);

    ServiceResult<String, Page<K3ReturnOrder>> queryReturnOrder(K3ReturnOrderQueryParam k3ReturnOrderQueryParam);

    ServiceResult<String, K3ReturnOrder> queryReturnOrderByNo(String returnOrderNo);

    ServiceResult<String, String> cancelK3ReturnOrder(K3ReturnOrder k3ReturnOrder);

    ServiceResult<String, String> commitK3ReturnOrder(K3ReturnOrderCommitParam k3ReturnOrderCommitParam);

    ServiceResult<String, String> sendToK3(String returnOrderNo);

    ServiceResult<String, String> createChangeOrder(K3ChangeOrder k3ChangeOrder);

    ServiceResult<String, String> sendChangeOrderToK3(String changeOrderNo);

    ServiceResult<String,String> updateChangeOrder(K3ChangeOrder k3ChangeOrder);

    ServiceResult<String,String> addChangeOrder(K3ChangeOrder k3ChangeOrder);

    ServiceResult<String,String> deleteChangeOrder(Integer k3ChangeOrderDetailId);

    ServiceResult<String,Page<K3ChangeOrder>> queryChangeOrder(K3ChangeOrderQueryParam param);

    ServiceResult<String,K3ChangeOrder> queryChangeOrderByNo(String changeOrderNo);

    ServiceResult<String, String> cancelK3ChangeOrder(K3ChangeOrder k3ChangeOrder);

    ServiceResult<String, String> commitK3ChangeOrder(K3ChangeOrderCommitParam k3ChangeOrderCommitParam);
}
