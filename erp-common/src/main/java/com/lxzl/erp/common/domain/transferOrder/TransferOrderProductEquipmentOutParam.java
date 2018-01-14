package com.lxzl.erp.common.domain.transferOrder;

import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.validGroup.TransferOrder.DumpTransferOrderProductEquipmentOutGroup;
import com.lxzl.erp.common.domain.validGroup.TransferOrder.TransferOrderProductEquipmentOutGroup;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @Author: your name
 * @Description：
 * @Date: Created in 16:42 2018/1/14
 * @Modified By:
 */
public class TransferOrderProductEquipmentOutParam {

    @NotBlank(message = ErrorCode.TRANSFER_ORDER_NO_NOT_NULL ,groups = {TransferOrderProductEquipmentOutGroup.class,DumpTransferOrderProductEquipmentOutGroup.class})
    private String transferOrderNo;   //转移单编号

    @NotBlank(message = ErrorCode.TRANSFER_ORDER_PRODUCT_EQUIPMENT_NO_NOT_NULL ,groups = {TransferOrderProductEquipmentOutGroup.class, DumpTransferOrderProductEquipmentOutGroup.class})
    private String productEquipmentNo;//设备编号

    public String getTransferOrderNo() {
        return transferOrderNo;
    }

    public void setTransferOrderNo(String transferOrderNo) {
        this.transferOrderNo = transferOrderNo;
    }

    public String getProductEquipmentNo() {
        return productEquipmentNo;
    }

    public void setProductEquipmentNo(String productEquipmentNo) {
        this.productEquipmentNo = productEquipmentNo;
    }
}
