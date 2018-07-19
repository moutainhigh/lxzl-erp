package com.lxzl.erp.common.domain.workbench;

import java.util.List;

/**
 * @Author: your name
 * @Description：
 * @Date: Created in 18:04 2018/7/18
 * @Modified By:
 */
public class WorkbenchOrderQueryParam {
    private List<Integer> orderStatusList;

    public List<Integer> getOrderStatusList() {
        return orderStatusList;
    }

    public void setOrderStatusList(List<Integer> orderStatusList) {
        this.orderStatusList = orderStatusList;
    }
}