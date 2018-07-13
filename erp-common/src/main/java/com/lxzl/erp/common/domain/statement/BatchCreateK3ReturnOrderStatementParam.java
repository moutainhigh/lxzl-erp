package com.lxzl.erp.common.domain.statement;

import java.util.List;

/**
 * <p>Description: </p>
 *
 * @Auther: huahongbin
 * @Date: 2018/7/12
 */
public class BatchCreateK3ReturnOrderStatementParam {
    private List<String> returnOrderNoList;

    public List<String> getReturnOrderNoList() {
        return returnOrderNoList;
    }

    public void setReturnOrderNoList(List<String> returnOrderNoList) {
        this.returnOrderNoList = returnOrderNoList;
    }
}
