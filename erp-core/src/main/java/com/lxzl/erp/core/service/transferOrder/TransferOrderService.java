package com.lxzl.erp.core.service.transferOrder;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.transferOrder.*;
import com.lxzl.erp.common.domain.transferOrder.pojo.TransferOrder;
import com.lxzl.erp.common.domain.transferOrder.pojo.TransferOrderMaterialBulk;
import com.lxzl.erp.common.domain.transferOrder.pojo.TransferOrderProductEquipment;
import com.lxzl.erp.core.service.VerifyReceiver;


/**
 * @Author: your name
 * @Description：
 * @Date: Created in 15:39 2018/1/3
 * @Modified By:
 */
public interface TransferOrderService extends VerifyReceiver {

    /**
     * 增加转入转移单
     * @param transferOrder
     * @return
     */
    ServiceResult<String,String> createTransferOrderInto(TransferOrder transferOrder);

    /**
     * 修改转入的转移单
     * @param transferOrder
     * @return
     */
    ServiceResult<String,String> updateTransferOrderInto(TransferOrder transferOrder);


    /**
     * 取消增加转移单
     * @param transferOrder
     * @return
     */
    ServiceResult<String,String> cancelTransferOrder(TransferOrder transferOrder);

    /**
     * 提交转移单进行审核
     * @return
     */
    ServiceResult<String,String> commitTransferOrder(TransferOrderCommitParam transferOrderCommitParam);

    /**
     * 新增转移单转出
     * @param transferOrder
     * @return
     */
    ServiceResult<String,String> createTransferOrderOut(TransferOrder transferOrder);

    /**
     *  商品设备转出备货
     * @param transferOrderProductEquipmentOutParam
     * @return
     */
    ServiceResult<String,String> transferOrderProductEquipmentOut(TransferOrderProductEquipmentOutParam transferOrderProductEquipmentOutParam);

    /**
     * 商品设备转出清货
     */
    ServiceResult<String,String> dumpTransferOrderProductEquipmentOut(TransferOrderProductEquipmentOutParam transferOrderProductEquipmentOutParam);

    /**
     * 物料转出备货
     * * @param transferOrderMaterialOutParam
     */
    ServiceResult<String,String> transferOrderMaterialOut(TransferOrderMaterialOutParam transferOrderMaterialOutParam);

    /**
     * 物料转出清货
     * @param transferOrderMaterialOutParam
     * @return
     */
    ServiceResult<String,String> dumpTransferOrderMaterialOut(TransferOrderMaterialOutParam transferOrderMaterialOutParam);

    /**
     * 更改转出的转移单
     * @param transferOrder
     * @return
     */
    ServiceResult<String,String> updateTransferOrderOut(TransferOrder transferOrder);

//    /**
//     * 结束转移单
//     * @param transferOrder
//     * @return
//     */
//    ServiceResult<String,String> endTransferOrder(TransferOrder transferOrder);

    /**
     * 分页展示转移单
     * @param transferOrderQueryParam
     * @return
     */
    ServiceResult<String,Page<TransferOrder>> pageTransferOrder(TransferOrderQueryParam transferOrderQueryParam);

    /**
     * 根据编号查询转移单
     * @param transferOrderNo
     * @return
     */
    ServiceResult<String,TransferOrder> detailTransferOrderByNo(String transferOrderNo);

    /**
     * 转移单商品设备详情
     * @param transferOrderProductEquipmentQueryParam
     * @return
     */
    ServiceResult<String, Page<TransferOrderProductEquipment>> detailTransferOrderProductEquipmentById(TransferOrderProductEquipmentQueryParam transferOrderProductEquipmentQueryParam);

    /**
     * 转移单配件散料详情
     * @param transferOrderMaterialBulkQueryParam
     * @return
     */
    ServiceResult<String,Page<TransferOrderMaterialBulk>> detailTransferOrderMaterialBulkById(TransferOrderMaterialBulkQueryParam transferOrderMaterialBulkQueryParam);
}


