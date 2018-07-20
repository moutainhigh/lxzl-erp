package com.lxzl.erp.common.domain.statement;

import java.util.List;

/**
 * @author: huanglong
 * @date: 2018/7/19/019 10:45
 * @e_mail: huanglong5945@163.com
 * @Description:
 */
public class StatementDetailPayParam {
    private List<Integer> mergeStatementItemList;

    public List<Integer> getMergeStatementItemList() {
        return mergeStatementItemList;
    }

    public void setMergeStatementItemList(List<Integer> mergeStatementItemList) {
        this.mergeStatementItemList = mergeStatementItemList;
    }
}
