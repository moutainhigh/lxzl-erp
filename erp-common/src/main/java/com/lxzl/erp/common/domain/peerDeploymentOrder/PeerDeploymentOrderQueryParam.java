package com.lxzl.erp.common.domain.peerDeploymentOrder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePageParam;

import java.util.Date;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/1/15
 * @Time : Created in 10:24
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PeerDeploymentOrderQueryParam extends BasePageParam {

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
