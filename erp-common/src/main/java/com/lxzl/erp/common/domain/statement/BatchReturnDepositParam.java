package com.lxzl.erp.common.domain.statement;

import java.util.List;

/**
 * @author lk
 * @Description: TODO
 * @date 2018/5/11 16:45
 */
public class BatchReturnDepositParam {

    private List<String> orderNoList;

    public List<String> getOrderNoList() {
        return orderNoList;
    }

    public void setOrderNoList(List<String> orderNoList) {
        this.orderNoList = orderNoList;
    }

}
