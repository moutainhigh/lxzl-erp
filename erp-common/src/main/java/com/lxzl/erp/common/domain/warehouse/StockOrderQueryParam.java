package com.lxzl.erp.common.domain.warehouse;

import com.lxzl.erp.common.domain.base.BasePageParam;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-11-20 17:38
 */
public class StockOrderQueryParam extends BasePageParam {

    /**
     * 出入库单编号
     */
    private String stockOrderNo;
    /**
     * 操作类型，1入库，2出库
     */
    private Integer operationType;
    /**
     * 起因，1采购入库，2货物调拨等
     */
    private Integer causeType;
    /**
     * 关联单编号
     */
    private String referNo;

    public String getStockOrderNo() {
        return stockOrderNo;
    }

    public void setStockOrderNo(String stockOrderNo) {
        this.stockOrderNo = stockOrderNo;
    }

    public Integer getOperationType() {
        return operationType;
    }

    public void setOperationType(Integer operationType) {
        this.operationType = operationType;
    }

    public Integer getCauseType() {
        return causeType;
    }

    public void setCauseType(Integer causeType) {
        this.causeType = causeType;
    }

    public String getReferNo() {
        return referNo;
    }

    public void setReferNo(String referNo) {
        this.referNo = referNo;
    }
}
