package com.lxzl.erp.common.domain.workbench;

import com.lxzl.erp.common.domain.k3.pojo.returnOrder.K3ReturnOrderQueryParam;

import java.util.List;

/**
 * @Author: your name
 * @Description：
 * @Date: Created in 18:38 2018/7/18
 * @Modified By:
 */
public class WorkbenchReturnOrderQueryParam {
    private List<Integer> returnOrderStatusList;

    public List<Integer> getReturnOrderStatusList() {
        return returnOrderStatusList;
    }

    public void setReturnOrderStatusList(List<Integer> returnOrderStatusList) {
        this.returnOrderStatusList = returnOrderStatusList;
    }
}