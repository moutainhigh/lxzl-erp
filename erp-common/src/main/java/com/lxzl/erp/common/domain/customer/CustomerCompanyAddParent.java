package com.lxzl.erp.common.domain.customer;

import java.util.List;

/**
 * @Author: Sunzhipeng
 * @Description:
 * @Date: Created in 2018\8\20 0020 17:16
 */
public class CustomerCompanyAddParent {
    private Integer parentCustomerId; //母公司ID
    private List<Integer> customerIdList;

    public Integer getParentCustomerId() {
        return parentCustomerId;
    }

    public void setParentCustomerId(Integer parentCustomerId) {
        this.parentCustomerId = parentCustomerId;
    }

    public List<Integer> getCustomerIdList() {
        return customerIdList;
    }

    public void setCustomerIdList(List<Integer> customerIdList) {
        this.customerIdList = customerIdList;
    }
}
