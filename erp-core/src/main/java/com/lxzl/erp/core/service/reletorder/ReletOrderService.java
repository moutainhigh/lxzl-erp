package com.lxzl.erp.core.service.reletorder;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.order.pojo.Order;
import com.lxzl.erp.common.domain.reletorder.ReletOrderCommitParam;
import com.lxzl.erp.common.domain.reletorder.ReletOrderQueryParam;
import com.lxzl.erp.common.domain.reletorder.pojo.ReletOrder;
import com.lxzl.erp.core.service.VerifyReceiver;
import com.lxzl.se.core.service.BaseService;


public interface ReletOrderService extends BaseService {

    /**
     * 创建续租单
     *
     * @author ZhaoZiXuan
     * @date 2018/4/24 10:40
     * @param order 订单信息
     * @return 续租单Id
     */
    ServiceResult<String, Integer> createReletOrder(Order order);


    /**
     * 根据参数查询续租单
     *
     * @author ZhaoZiXuan
     * @date 2018/4/26 14:51
     * @param  param 查询订单参数
     * @return   续租单列表
     */
    ServiceResult<String, Page<ReletOrder>> queryAllReletOrder(ReletOrderQueryParam param);


    /**
     * 根据续租单id查询续租单详情
     *
     * @author ZhaoZiXuan
     * @date 2018/4/27 16:47
     * @param   reletOrderId 续租单id
     * @return   续租单详情
     */
    ServiceResult<String, ReletOrder> queryReletOrderDetailById(Integer reletOrderId);





}
