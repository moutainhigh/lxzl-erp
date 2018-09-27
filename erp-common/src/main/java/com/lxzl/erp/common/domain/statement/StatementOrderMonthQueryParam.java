package com.lxzl.erp.common.domain.statement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePageParam;
import com.lxzl.erp.common.util.JSONUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author kai
 * @date 2018 2-3 09:11
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class StatementOrderMonthQueryParam extends BasePageParam {

    private String statementOrderCustomerName;
    private Integer statementOrderCustomerId;
    private String statementOrderCustomerNo;
    @DateTimeFormat(pattern = "yyyy-MM")
    private Date statementOrderStartTime;
    @DateTimeFormat(pattern = "yyyy-MM")
    private Date statementOrderEndTime;
    private String statementOrderSubCompanyName;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date monthTime;
    private Integer subCompanyId;//分公司ID
    private String ownerName;//业务员
    private Integer statementDetailStatus;
    private List<Integer> passiveUserIdList;//控制数据权限
    private Integer customerId;
    /** 订单id列表 */
    private Set<Integer> orderIds;

    private Set<Integer> replaceOrderIds;

    private Integer queryOrderType;

    public Integer getSubCompanyId() {
        return subCompanyId;
    }

    public void setSubCompanyId(Integer subCompanyId) {
        this.subCompanyId = subCompanyId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public Integer getStatementOrderCustomerId() {
        return statementOrderCustomerId;
    }

    public void setStatementOrderCustomerId(Integer statementOrderCustomerId) {
        this.statementOrderCustomerId = statementOrderCustomerId;
    }

    public String getStatementOrderCustomerNo() {
        return statementOrderCustomerNo;
    }

    public void setStatementOrderCustomerNo(String statementOrderCustomerNo) {
        this.statementOrderCustomerNo = statementOrderCustomerNo;
    }

    public Date getMonthTime() {
        return monthTime;
    }

    public void setMonthTime(Date monthTime) {
        this.monthTime = monthTime;
    }

    public String getStatementOrderCustomerName() {
        return statementOrderCustomerName;
    }

    public void setStatementOrderCustomerName(String statementOrderCustomerName) {
        this.statementOrderCustomerName = statementOrderCustomerName;
    }

    public List<Integer> getPassiveUserIdList() {
        return passiveUserIdList;
    }

    public void setPassiveUserIdList(List<Integer> passiveUserIdList) {
        this.passiveUserIdList = passiveUserIdList;
    }

    public String getStatementOrderSubCompanyName() {
        return statementOrderSubCompanyName;
    }

    public void setStatementOrderSubCompanyName(String statementOrderSubCompanyName) {
        this.statementOrderSubCompanyName = statementOrderSubCompanyName;
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

    public Date getStatementOrderEndTimeAddOne() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(statementOrderEndTime);
        calendar.add(Calendar.MONTH, 1);
        return calendar.getTime();
    }

    public void setStatementOrderEndTime(Date statementOrderEndTime) {
        this.statementOrderEndTime = statementOrderEndTime;
    }

    public Integer getStatementDetailStatus() {
        return statementDetailStatus;
    }

    public void setStatementDetailStatus(Integer statementDetailStatus) {
        this.statementDetailStatus = statementDetailStatus;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Set<Integer> getOrderIds() {
        return orderIds;
    }

    public void setOrderIds(Set<Integer> orderIds) {
        this.orderIds = orderIds;
    }

    public StatementOrderMonthQueryParam clone() {
        return JSONUtil.parseObject(this, this.getClass());
    }

    public Integer getQueryOrderType() {
        return queryOrderType;
    }

    public void setQueryOrderType(Integer queryOrderType) {
        this.queryOrderType = queryOrderType;
    }

    public Set<Integer> getReplaceOrderIds() {
        return replaceOrderIds;
    }

    public void setReplaceOrderIds(Set<Integer> replaceOrderIds) {
        this.replaceOrderIds = replaceOrderIds;
    }
}
