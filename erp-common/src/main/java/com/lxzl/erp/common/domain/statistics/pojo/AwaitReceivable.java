package com.lxzl.erp.common.domain.statistics.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.Page;

import java.io.Serializable;
import java.math.BigDecimal;

/**
* 待收明细
* @Author : XiaoLuYu
* @Date : Created in 2018/4/10 15:34
*/

@JsonIgnoreProperties(ignoreUnknown = true)
public class AwaitReceivable implements Serializable {

    private Integer totalCount;    //总条数
    private BigDecimal totalAwaitReceivable;    //总待收
    private Page<AwaitReceivableDetail> awaitReceivableDetailPage;    //统计项分页

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public BigDecimal getTotalAwaitReceivable() {
        return totalAwaitReceivable;
    }

    public void setTotalAwaitReceivable(BigDecimal totalAwaitReceivable) {
        this.totalAwaitReceivable = totalAwaitReceivable;
    }

    public Page<AwaitReceivableDetail> getAwaitReceivableDetailPage() {
        return awaitReceivableDetailPage;
    }

    public void setAwaitReceivableDetailPage(Page<AwaitReceivableDetail> awaitReceivableDetailPage) {
        this.awaitReceivableDetailPage = awaitReceivableDetailPage;
    }
}

