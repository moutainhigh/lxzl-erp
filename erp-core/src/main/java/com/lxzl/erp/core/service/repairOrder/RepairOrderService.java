package com.lxzl.erp.core.service.repairOrder;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.repairOrder.RepairOrderBulkMaterialQueryParam;
import com.lxzl.erp.common.domain.repairOrder.RepairOrderEquipmentQueryParam;
import com.lxzl.erp.common.domain.repairOrder.RepairOrderQueryParam;
import com.lxzl.erp.common.domain.repairOrder.pojo.RepairOrder;
import com.lxzl.erp.common.domain.repairOrder.pojo.RepairOrderBulkMaterial;
import com.lxzl.erp.common.domain.repairOrder.pojo.RepairOrderEquipment;
import com.lxzl.erp.core.service.VerifyReceiver;
import com.lxzl.erp.dataaccess.domain.repairOrder.RepairOrderDO;

import java.util.List;

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

    /**
     *设备详情页面
     *
     * @param repairOrderNo 设备维修单编号
     * @return
     */
    ServiceResult<String,RepairOrder> queryRepairOrderByNo(String repairOrderNo);

    /**
     * 设备维修单分页
     *
     * @param repairOrderQueryParam
     * @return
     */
    ServiceResult<String,Page<RepairOrder>> pageRepairOrder(RepairOrderQueryParam repairOrderQueryParam);

    /**
     * 设备维修单明细分页列表
     *
     * @param repairOrderEquipmentQueryParam
     * @return
     */
    ServiceResult<String,Page<RepairOrderEquipment>> pageRepairEquipment(RepairOrderEquipmentQueryParam repairOrderEquipmentQueryParam);

    /**
     *散料维修单明单分页列表
     *
     * @param repairOrderBulkMaterialQueryParam
     * @return
     */
    ServiceResult<String,Page<RepairOrderBulkMaterial>> pageRepairBulkMaterial(RepairOrderBulkMaterialQueryParam repairOrderBulkMaterialQueryParam);

    /**
     * 根据明细单Id来确认完成时间
     *
     * @param repairOrderEquipmentList
     * @param repairOrderBulkMaterialList
     * @return
     */
    ServiceResult<String,Integer> fix(List<RepairOrderEquipment> repairOrderEquipmentList, List<RepairOrderBulkMaterial> repairOrderBulkMaterialList);


    /**
     * 设备维修单结束维修
     *
     * @param repairOrder
     * @return
     */
    ServiceResult<String,String> end(RepairOrder repairOrder);
}
