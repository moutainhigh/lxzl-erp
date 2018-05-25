package com.lxzl.erp.core.service.reletorder;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.order.OrderCommitParam;
import com.lxzl.erp.common.domain.order.pojo.Order;
import com.lxzl.erp.common.domain.reletorder.ReletOrderCommitParam;
import com.lxzl.erp.common.domain.reletorder.ReletOrderCreateResult;
import com.lxzl.erp.common.domain.reletorder.ReletOrderQueryParam;
import com.lxzl.erp.common.domain.reletorder.pojo.ReletOrder;
import com.lxzl.erp.core.service.VerifyReceiver;
import com.lxzl.se.core.service.BaseService;

import java.util.Date;


public interface ReletOrderService extends VerifyReceiver {

    /**
     * 创建续租单
     *
     * @author ZhaoZiXuan
     * @date 2018/4/24 10:40
     * @param order 订单信息
     * @return 续租单Id
     */
    ServiceResult<String, ReletOrderCreateResult> createReletOrder(Order order);

    /**
     * 修改续租单
     *
     * @author ZhaoZiXuan
     * @date 2018/5/25 16:51
     * @param
     * @return
     */
    ServiceResult<String, ReletOrderCreateResult> updateReletOrder(ReletOrder reletOrder);

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


    /**
     * 提交续租单
     *
     * @return 续租单编号
     */
    ServiceResult<String, String> commitReletOrder(ReletOrderCommitParam reletOrderCommitParam);


    /**
     * 查询截至当前时间为止，待钉钉提醒的在租订单
     *
     * @author ZhaoZiXuan
     * @date 2018/5/22 16:03
     * @param
     * @return
     */
    ServiceResult<String, Boolean> handleReletSendMessage(Date currentTime);


    /**
     * 续租单 是否需要审核
     *
     * @author ZhaoZiXuan
     * @date 2018/5/25 11:54
     * @param
     * @return
     */
    ServiceResult<String, Boolean> isNeedVerify(ReletOrder reletOrder);


}
