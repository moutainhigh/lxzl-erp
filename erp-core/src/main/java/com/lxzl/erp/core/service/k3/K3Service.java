package com.lxzl.erp.core.service.k3;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.k3.K3OrderQueryParam;
import com.lxzl.erp.common.domain.k3.K3SendRecordBatchParam;
import com.lxzl.erp.common.domain.k3.K3SendRecordParam;
import com.lxzl.erp.common.domain.k3.pojo.K3SendRecord;
import com.lxzl.erp.common.domain.k3.pojo.order.Order;
import com.lxzl.erp.core.service.VerifyReceiver;

import java.util.Map;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2018-02-11 20:54
 */
public interface K3Service {

    /**
     * 根据参数查询订单
     *
     * @param param 查询订单参数
     * @return 订单列表
     */
    ServiceResult<String, Page<Order>> queryAllOrder(K3OrderQueryParam param);

    ServiceResult<String, Order> queryOrder(String orderNo);


    ServiceResult<String, String> sendToK3(String returnOrderNo);

    /**
     * K3数据发送记录表分页
     *
     * @param k3SendRecordParam
     * @return
     */
    ServiceResult<String, Page<K3SendRecord>> queryK3SendRecord(K3SendRecordParam k3SendRecordParam);

    ServiceResult<String, Integer> sendAgainK3SendRecord(K3SendRecord k3SendRecord);

    ServiceResult<String, Map<String, String>> batchSendDataToK3(K3SendRecordBatchParam k3SendRecordBatchParam);

    ServiceResult<String, String> transferOrder(K3OrderQueryParam param);

}
