package com.lxzl.erp.common.domain.statement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePageParam;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-12-12 8:51
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class StatementOrderDetailQueryParam extends BasePageParam {

    private Integer isNeedToPay;
    private Integer isOverdue;
    private Integer rentLengthType;
    private Integer customerId;
    private Integer subCompanyId;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date createStartTime;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date createEndTime;
    private Date statementOrderStartTime;
    private Date statementOrderEndTime;

    public Date getCreateStartTime() {
        return createStartTime;
    }

    public void setCreateStartTime(Date createStartTime) {
        this.createStartTime = createStartTime;
    }

    public Date getCreateEndTime() {
        return createEndTime;
    }

    public void setCreateEndTime(Date createEndTime) {
        this.createEndTime = createEndTime;
    }

    public Integer getRentLengthType() {
        return rentLengthType;
    }

    public void setRentLengthType(Integer rentLengthType) {
        this.rentLengthType = rentLengthType;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getIsNeedToPay() {
        return isNeedToPay;
    }

    public void setIsNeedToPay(Integer isNeedToPay) {
        this.isNeedToPay = isNeedToPay;
    }

    public Integer getIsOverdue() {
        return isOverdue;
    }

    public void setIsOverdue(Integer isOverdue) {
        this.isOverdue = isOverdue;
    }

    public Integer getSubCompanyId() {
        return subCompanyId;
    }

    public void setSubCompanyId(Integer subCompanyId) {
        this.subCompanyId = subCompanyId;
    }

    public Date getStatementOrderStartTime() {
        return statementOrderStartTime;
    }

    public void setStatementOrderStartTime(Date statementOrderStartTime) {
        this.statementOrderStartTime = statementOrderStartTime;
    }

    public Date getStatementOrderEndTime() {
        return statementOrderEndTime;
    }

    public void setStatementOrderEndTime(Date statementOrderEndTime) {
        this.statementOrderEndTime = statementOrderEndTime;
    }
}
