package com.lxzl.erp.core.service.order;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.order.ExchangeOrderCommitParam;
import com.lxzl.erp.common.domain.order.pojo.ExchangeOrder;
import com.lxzl.erp.core.service.VerifyReceiver;

import java.util.List;

public interface ExchangeOrderService extends VerifyReceiver {

    /**
     * 创建变更单（修改单价、支付方式、租赁方案）
     *
     * @param exchangeOrder
     * @return
     */
    ServiceResult<String, String> createExchangeOrder(ExchangeOrder exchangeOrder);

    /**
     * 提交变更单
     *
     * @return 变更单编号
     */
    ServiceResult<String, String> commitExchangeOrder(ExchangeOrderCommitParam exchangeOrderCommitParam);

    /**
     * 根据变更单号获取变更明细
     *
     * @param exchangerOrderNo
     * @return
     */
    ServiceResult<String,ExchangeOrder> queryExchangeOrderByNo(String exchangerOrderNo);

    /**
     * 根据订单号，获取变更单列表
     *
     * @param orderNo
     * @return
     */
    ServiceResult<String,Page<ExchangeOrder>> queryByOrderNo(String orderNo);
}
