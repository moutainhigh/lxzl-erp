package com.lxzl.erp.dataaccess.domain.order;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;

/**
 * <p>Description: </p>
 *
 * @Auther: huahongbin
 * @Date: 2018/6/20
 */
public class OrderStatementDateChangeLogDO extends BaseDO {
    private Integer id;
    private Integer statementDate; //结算时间（天），20和31两种情况，如果为空取系统设定
    private String orderNo;
    private Integer dataStatus;
    private String remark;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStatementDate() {
        return statementDate;
    }

    public void setStatementDate(Integer statementDate) {
        this.statementDate = statementDate;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(Integer dataStatus) {
        this.dataStatus = dataStatus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
