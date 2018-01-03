package com.lxzl.erp.common.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePageParam;

import java.util.Date;

/**
 * User : XiaoLuYu
 * Date : Created in ${Date}
 * Time : Created in ${Time}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransferOrderOrderQueryParam  extends BasePageParam {

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
