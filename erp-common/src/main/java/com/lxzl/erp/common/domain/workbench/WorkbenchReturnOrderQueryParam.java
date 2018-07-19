package com.lxzl.erp.common.domain.workbench;

import java.util.List;

/**
 * @Author: your name
 * @Description：
 * @Date: Created in 18:38 2018/7/18
 * @Modified By:
 */
public class WorkbenchReturnOrderQueryParam {
    private List<Integer> returnOrderStatus;

    public List<Integer> getReturnOrderStatus() {
        return returnOrderStatus;
    }

    public void setReturnOrderStatus(List<Integer> returnOrderStatus) {
        this.returnOrderStatus = returnOrderStatus;
    }
}