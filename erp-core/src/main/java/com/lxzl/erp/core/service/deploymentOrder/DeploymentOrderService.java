package com.lxzl.erp.core.service.deploymentOrder;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.deploymentOrder.CommitDeploymentOrderParam;
import com.lxzl.erp.common.domain.deploymentOrder.DeploymentOrderQueryParam;
import com.lxzl.erp.common.domain.deploymentOrder.ProcessDeploymentOrderParam;
import com.lxzl.erp.common.domain.deploymentOrder.ReturnDeploymentOrderParam;
import com.lxzl.erp.common.domain.deploymentOrder.pojo.DeploymentOrder;
import com.lxzl.erp.core.service.VerifyReceiver;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-11-27 13:30
 */
public interface DeploymentOrderService extends VerifyReceiver {

    /**
     * 生成调度单
     *
     * @param deploymentOrder 调度单信息
     * @return 调度单编号
     */
    ServiceResult<String, String> createDeploymentOrder(DeploymentOrder deploymentOrder);

    /**
     * 更新调拨单
     *
     * @param deploymentOrder 调拨单信息
     * @return 调拨单编号
     */
    ServiceResult<String, String> updateDeploymentOrder(DeploymentOrder deploymentOrder);

    /**
     * 提交调度单
     *
     * @return 调度单编号
     */
    ServiceResult<String, String> commitDeploymentOrder(CommitDeploymentOrderParam param);


    /**
     * 处理调拨单（备货）
     *
     * @param param 调拨单及设备信息
     * @return 调拨单编号
     */
    ServiceResult<String, String> processDeploymentOrder(ProcessDeploymentOrderParam param);


    /**
     * 取消调拨单
     *
     * @param deploymentOrderNo 调拨单编号
     * @return 调拨单编号
     */
    ServiceResult<String, String> cancelDeploymentOrder(String deploymentOrderNo);

    /**
     * 调拨单发货
     *
     * @param deploymentOrderNo 调拨单编号
     * @return 调拨单编号
     */
    ServiceResult<String, String> deliveryDeploymentOrder(String deploymentOrderNo);


    /**
     * 确认调拨单
     *
     * @param deploymentOrderNo 调拨单号
     * @return 调拨单号
     */
    ServiceResult<String, String> confirmDeploymentOrder(String deploymentOrderNo);

    /**
     * 查询调拨单列表
     *
     * @param param 查询参数
     * @return 调拨单分页信息
     */
    ServiceResult<String, Page<DeploymentOrder>> queryDeploymentOrderPage(DeploymentOrderQueryParam param);


    /**
     * 查询调拨单详情
     *
     * @param deploymentOrderNo 调拨单编号
     * @return 调拨单信息
     */
    ServiceResult<String, DeploymentOrder> queryDeploymentOrderDetail(String deploymentOrderNo);

    /**
     * 退还调拨单
     *
     * @param param 退还调拨单
     * @return 调拨单号
     */
    ServiceResult<String, String> returnDeploymentOrder(ReturnDeploymentOrderParam param);
}
