package com.lxzl.erp.common.domain.customer;

import com.lxzl.erp.common.domain.base.BasePageParam;

/**
 * @创建人 liuzy
 * @创建日期 2018/3/27
 * @描述: 风控历史记录
 */
public class CustomerRiskManageHistoryQueryParam extends BasePageParam {

    private Integer customerRiskManagementHistoryId;//id
    private Integer customerId;//客户id
    private String customerNo; //客户编号

    public Integer getCustomerRiskManagementHistoryId() {
        return customerRiskManagementHistoryId;
    }

    public void setCustomerRiskManagementHistoryId(Integer customerRiskManagementHistoryId) {
        this.customerRiskManagementHistoryId = customerRiskManagementHistoryId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

}
