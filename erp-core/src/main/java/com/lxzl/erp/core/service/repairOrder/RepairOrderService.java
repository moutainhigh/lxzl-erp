package com.lxzl.erp.core.service.repairOrder;

import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.repairOrder.pojo.RepairOrder;
import com.lxzl.erp.core.service.VerifyReceiver;

/**
 * @Author: your name
 * @Description：
 * @Date: Created in 15:43 2017/12/14
 * @Modified By:
 */
public interface RepairOrderService extends VerifyReceiver {

    /**
     * 增加设备维修单
     *
     *  @param repairOrder
     * @return 设备维修单编号
     */
    ServiceResult<String,String> addRepairOrder(RepairOrder repairOrder);

    /**
     * 提交审核设备维修单
     * @param repairOrderNo 设备维修单编号
     * @param verifyUser 审核人
     * @param commitRemark 提交备注
     * @return 订单编号
     */
    ServiceResult<String,String> commitRepairOrder(String repairOrderNo,Integer verifyUser,String commitRemark);


    /**
     * 维修中心接收设备维修单
     *
     * @param repairOrderNo 设备维修单编号
     * @return 设备维修单编号
     */
    ServiceResult<String,String> receiveRepairOrder(String repairOrderNo);

    /**
     * 取消设备维修单
     *
     * @param repairOrderNo 设备维修单编号
     * @return 设备维修单编号
     */
    ServiceResult<String,String> cancelRepairOrder(String repairOrderNo);

    /**
     * 修改设备维修单
     *
     * @param repairOrder
     * @return
     */
    ServiceResult<String,String> updateRepairOrder(RepairOrder repairOrder);
}
