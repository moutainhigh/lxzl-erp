package com.lxzl.erp.core.service.transferOrder;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.transferOrder.pojo.TransferOrder;
import com.lxzl.erp.common.domain.transferOrder.TransferOrderQueryParam;
import com.lxzl.erp.common.domain.transferOrder.pojo.TransferOrderMaterial;
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
     * @param transferOrderNo
     * @param verifyUser
     * @param commitRemark
     * @return
     */
    ServiceResult<String,String> commitTransferOrder(String transferOrderNo, Integer verifyUser, String commitRemark);

    /**
     * 新增转移单转出
     * @param transferOrder
     * @return
     */
    ServiceResult<String,String> createTransferOrderOut(TransferOrder transferOrder);

    /**
     *  商品设备转出备货
     * @param transferOrder
     * @return
     */
    ServiceResult<String,String> transferOrderProductEquipmentOut(TransferOrder transferOrder);

    /**
     * 商品设备转出清货
     */
    ServiceResult<String,String> dumpTransferOrderProductEquipmentOut(TransferOrder transferOrder);

    /**
     * 物料转出备货
     */
    ServiceResult<String,String> transferOrderMaterialOut(TransferOrderMaterial transferOrderMaterial);

    /**
     * 物料转出清货
     * @param transferOrderMaterial
     * @return
     */
    ServiceResult<String,String> dumpTransferOrderMaterialOut(TransferOrderMaterial transferOrderMaterial);

    /**
     * 更改转出的转移单
     * @param transferOrder
     * @return
     */
    ServiceResult<String,String> updateTransferOrderOut(TransferOrder transferOrder);

    /**
     * 分页展示转移单
     * @param transferOrderQueryParam
     * @return
     */
    ServiceResult<String,Page<TransferOrder>> pageTransferOrder(TransferOrderQueryParam transferOrderQueryParam);

    /**
     * 根据ID查询转移单
     * @param transferOrderId
     * @return
     */
    ServiceResult<String,TransferOrder> detailTransferOrderById(Integer transferOrderId);


}


