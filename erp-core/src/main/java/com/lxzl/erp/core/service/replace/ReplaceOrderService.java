package com.lxzl.erp.core.service.replace;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.replace.ReplaceOrderQueryParam;
import com.lxzl.erp.common.domain.replace.pojo.ReplaceOrder;
import com.lxzl.se.core.service.BaseService;

/**
 * @Author: Sunzhipeng
 * @Description:
 * @Date: Created in 2018\9\5 0005 10:02
 */
public interface ReplaceOrderService extends BaseService {
    ServiceResult<String,String> add(ReplaceOrder replaceOrder);

    ServiceResult<String,String> update(ReplaceOrder replaceOrder);

    ServiceResult cancel(ReplaceOrder replaceOrder);

    ServiceResult<String,Page<ReplaceOrder>> queryAllReplaceOrder(ReplaceOrderQueryParam param);

    ServiceResult<String,ReplaceOrder> queryReplaceOrderByNo(String replaceOrderNo);

    ServiceResult<String,String> confirmReplaceOrder(ReplaceOrder replaceOrder);

    ServiceResult<String,String> replaceOrderDeliveryCallBack(ReplaceOrder replaceOrder);
}
