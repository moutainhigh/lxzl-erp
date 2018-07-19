package com.lxzl.erp.common.domain.workbench;

import com.lxzl.erp.common.domain.order.OrderQueryParam;

import java.util.List;

/**
 * @Author: your name
 * @Description：
 * @Date: Created in 18:04 2018/7/18
 * @Modified By:
 */
public class WorkbenchOrderQueryParam {
    private List<OrderQueryParam> orderQueryParamList;

    public List<OrderQueryParam> getOrderQueryParamList() {
        return orderQueryParamList;
    }

    public void setOrderQueryParamList(List<OrderQueryParam> orderQueryParamList) {
        this.orderQueryParamList = orderQueryParamList;
    }
}