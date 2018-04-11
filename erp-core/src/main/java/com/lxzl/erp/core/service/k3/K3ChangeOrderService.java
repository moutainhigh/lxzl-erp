package com.lxzl.erp.core.service.k3;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.k3.K3ChangeOrderCommitParam;
import com.lxzl.erp.common.domain.k3.pojo.K3ChangeOrder;
import com.lxzl.erp.common.domain.k3.pojo.changeOrder.K3ChangeOrderQueryParam;
import com.lxzl.erp.core.service.VerifyReceiver;

/**
 * @Author: your name
 * @Description：
 * @Date: Created in 20:00 2018/4/10
 * @Modified By:
 */

public interface K3ChangeOrderService extends VerifyReceiver {

    ServiceResult<String, String> createChangeOrder(K3ChangeOrder k3ChangeOrder);

    ServiceResult<String,String> updateChangeOrder(K3ChangeOrder k3ChangeOrder);

    ServiceResult<String, String> sendChangeOrderToK3(String changeOrderNo);

    ServiceResult<String,String> addChangeOrder(K3ChangeOrder k3ChangeOrder);

    ServiceResult<String,String> deleteChangeOrder(Integer k3ChangeOrderDetailId);

    ServiceResult<String,Page<K3ChangeOrder>> queryChangeOrder(K3ChangeOrderQueryParam param);

    ServiceResult<String,K3ChangeOrder> queryChangeOrderByNo(String changeOrderNo);

    ServiceResult<String, String> cancelK3ChangeOrder(K3ChangeOrder k3ChangeOrder);

    ServiceResult<String, String> commitK3ChangeOrder(K3ChangeOrderCommitParam k3ChangeOrderCommitParam);

}
