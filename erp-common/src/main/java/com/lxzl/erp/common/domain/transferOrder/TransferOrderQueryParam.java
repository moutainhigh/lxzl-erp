package com.lxzl.erp.common.domain.transferOrder;

import com.lxzl.erp.common.domain.base.BasePageParam;

/**
 * @Author: your name
 * @Description：
 * @Date: Created in 18:03 2018/1/3
 * @Modified By:
 */
public class TransferOrderQueryParam extends BasePageParam {
    private String transferOrderNo;   //转移单编号
    private String transferOrderName;   //转移单名称
    private Integer transferOrderStatus;   //转移单状态，0初始化，4审批中，8转移成功，16取消转移
    private Integer transferOrderMode;   //转移方式，1转入，2转出（凭空转入转出）
    private Integer transferOrderType;   //	转入类型：1外借入库转入，2试验机转入，3原有资产，99其他。 转出类型：51丢失，52售出，53试验机归还，99其他
    private Integer warehouseId;   //仓库ID，哪个库房转移

    public String getTransferOrderNo() {
        return transferOrderNo;
    }

    public void setTransferOrderNo(String transferOrderNo) {
        this.transferOrderNo = transferOrderNo;
    }

    public String getTransferOrderName() {
        return transferOrderName;
    }

    public void setTransferOrderName(String transferOrderName) {
        this.transferOrderName = transferOrderName;
    }

    public Integer getTransferOrderStatus() {
        return transferOrderStatus;
    }

    public void setTransferOrderStatus(Integer transferOrderStatus) {
        this.transferOrderStatus = transferOrderStatus;
    }

    public Integer getTransferOrderMode() {
        return transferOrderMode;
    }

    public void setTransferOrderMode(Integer transferOrderMode) {
        this.transferOrderMode = transferOrderMode;
    }

    public Integer getTransferOrderType() {
        return transferOrderType;
    }

    public void setTransferOrderType(Integer transferOrderType) {
        this.transferOrderType = transferOrderType;
    }

    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }
}
