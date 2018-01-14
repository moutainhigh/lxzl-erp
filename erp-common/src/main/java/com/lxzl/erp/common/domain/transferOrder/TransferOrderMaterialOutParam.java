package com.lxzl.erp.common.domain.transferOrder;

import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.validGroup.TransferOrder.DumpTransferOrderMaterialOutGroup;
import com.lxzl.erp.common.domain.validGroup.TransferOrder.TramsferOrderMaterialOutGroup;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * @Author: your name
 * @Description：
 * @Date: Created in 16:52 2018/1/14
 * @Modified By:
 */

public class TransferOrderMaterialOutParam {

    @NotNull(message = ErrorCode.TRANSFER_ORDER_NO_NOT_NULL,groups = {TramsferOrderMaterialOutGroup.class,DumpTransferOrderMaterialOutGroup.class})
    private String transferOrderNo;   //转移单NO
    private Integer materialId;   //物料ID
    @NotBlank(message = ErrorCode.TRANSFER_ORDER_MATERIAL_NO_NOT_NULL,groups = {TramsferOrderMaterialOutGroup.class,DumpTransferOrderMaterialOutGroup.class})
    private String materialNo;   //物料编号
    @NotNull(message = ErrorCode.TRANSFER_ORDER_MATERIAL_COUNT_NOT_NULL,groups = {TramsferOrderMaterialOutGroup.class,DumpTransferOrderMaterialOutGroup.class})
    private Integer materialCount;   //物料数量
    @NotNull(message = ErrorCode.TRANSFER_ORDER_IS_NEW_NOT_NULL,groups = {TramsferOrderMaterialOutGroup.class,DumpTransferOrderMaterialOutGroup.class})
    private Integer isNew;   //是否全新，1是，0否

    private String remark;   //备注

    public String getTransferOrderNo() {
        return transferOrderNo;
    }

    public void setTransferOrderNo(String transferOrderNo) {
        this.transferOrderNo = transferOrderNo;
    }

    public Integer getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Integer materialId) {
        this.materialId = materialId;
    }

    public String getMaterialNo() {
        return materialNo;
    }

    public void setMaterialNo(String materialNo) {
        this.materialNo = materialNo;
    }

    public Integer getMaterialCount() {
        return materialCount;
    }

    public void setMaterialCount(Integer materialCount) {
        this.materialCount = materialCount;
    }

    public Integer getIsNew() {
        return isNew;
    }

    public void setIsNew(Integer isNew) {
        this.isNew = isNew;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
