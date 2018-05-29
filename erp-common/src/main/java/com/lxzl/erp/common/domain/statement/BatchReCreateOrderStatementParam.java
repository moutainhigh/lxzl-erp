package com.lxzl.erp.common.domain.statement;

import java.util.List;

/**
 * @author lk
 * @Description: TODO
 * @date 2018/5/11 16:45
 */
public class BatchReCreateOrderStatementParam {

    private List<String> orderNoList;
    private List<String> customerNoList;

    public List<String> getOrderNoList() {
        return orderNoList;
    }

    public void setOrderNoList(List<String> orderNoList) {
        this.orderNoList = orderNoList;
    }

    public List<String> getCustomerNoList() {
        return customerNoList;
    }

    public void setCustomerNoList(List<String> customerNoList) {
        this.customerNoList = customerNoList;
    }
}
