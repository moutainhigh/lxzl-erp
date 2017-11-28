package com.lxzl.erp.core.service.deploymentOrder;

import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.deploymentOrder.ProcessDeploymentOrderParam;
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
     * @param deploymentOrderNo 调拨单号
     * @param verifyUser        审核人
     * @return 调度单编号
     */
    ServiceResult<String, String> commitDeploymentOrder(String deploymentOrderNo, Integer verifyUser);


    /**
     * 处理调拨单（备货）
     *
     * @param param 调拨单及设备信息
     * @return 调拨单编号
     */
    ServiceResult<String, String> processDeploymentOrder(ProcessDeploymentOrderParam param);

    /**
     * 确认调拨单
     *
     * @param deploymentOrderNo 调拨单号
     * @return 调拨单号
     */
    ServiceResult<String, String> confirmDeploymentOrder(String deploymentOrderNo);
}
