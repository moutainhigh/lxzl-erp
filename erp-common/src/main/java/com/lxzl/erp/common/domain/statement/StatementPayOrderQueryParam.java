package com.lxzl.erp.common.domain.statement;

import com.lxzl.erp.common.domain.base.BasePageParam;

import java.util.Date;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2018-01-23 19:43
 */
public class StatementPayOrderQueryParam extends BasePageParam {

    private Date createStartTime;
    private Date createEndTime;

    public Date getCreateStartTime() {
        return createStartTime;
    }

    public void setCreateStartTime(Date createStartTime) {
        this.createStartTime = createStartTime;
    }

    public Date getCreateEndTime() {
        return createEndTime;
    }

    public void setCreateEndTime(Date createEndTime) {
        this.createEndTime = createEndTime;
    }
}
