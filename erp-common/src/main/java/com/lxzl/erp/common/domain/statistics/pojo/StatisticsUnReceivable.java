package com.lxzl.erp.common.domain.statistics.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.Page;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author lk
 * @Description: 未收汇总
 * @date 2018/1/17 14:30
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class StatisticsUnReceivable implements Serializable {

    private Integer totalCount;    //总条数
    private BigDecimal totalUnReceivable;    //总应收
    private Page<StatisticsUnReceivableDetail> statisticsUnReceivableDetailPage;    //统计项分页

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public BigDecimal getTotalUnReceivable() {
        return totalUnReceivable;
    }

    public void setTotalUnReceivable(BigDecimal totalUnReceivable) {
        this.totalUnReceivable = totalUnReceivable;
    }

    public Page<StatisticsUnReceivableDetail> getStatisticsUnReceivableDetailPage() {
        return statisticsUnReceivableDetailPage;
    }

    public void setStatisticsUnReceivableDetailPage(Page<StatisticsUnReceivableDetail> statisticsUnReceivableDetailPage) {
        this.statisticsUnReceivableDetailPage = statisticsUnReceivableDetailPage;
    }
}

