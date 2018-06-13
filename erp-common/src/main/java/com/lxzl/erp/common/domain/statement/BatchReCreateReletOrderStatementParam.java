package com.lxzl.erp.common.domain.statement;

import java.util.List;

/**
 * @author: huanglong
 * @date: 2018/6/11/011 18:27
 * @e_mail: huanglong5945@163.com
 * @Description:
 */
public class BatchReCreateReletOrderStatementParam {
    private List<String> reletOrderNoList;

    public List<String> getReletOrderNoList() {
        return reletOrderNoList;
    }

    public void setReletOrderNoList(List<String> reletOrderNoList) {
        this.reletOrderNoList = reletOrderNoList;
    }
}
