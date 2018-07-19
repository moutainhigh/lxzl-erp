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
    private List<K3ReturnOrderQueryParam> k3ReturnOrderQueryParamList;

    public List<K3ReturnOrderQueryParam> getK3ReturnOrderQueryParamList() {
        return k3ReturnOrderQueryParamList;
    }

    public void setK3ReturnOrderQueryParamList(List<K3ReturnOrderQueryParam> k3ReturnOrderQueryParamList) {
        this.k3ReturnOrderQueryParamList = k3ReturnOrderQueryParamList;
    }
}