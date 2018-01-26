package com.lxzl.erp.common.domain.statistics.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.Page;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author lk
 * @Description: 收入统计
 * @date 2018/1/17 14:30
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class StatisticsIncome implements Serializable {

    private Integer totalCount;    //总条数
    private BigDecimal totalDeposit;    //总设备押金
    private BigDecimal totalRentDeposit;    //总租金押金
    private BigDecimal totalReturnDeposit;    //总退设备押金
    private BigDecimal totalReturnRentDeposit;    //总退租金押金
    private BigDecimal totalRent;    //总租金
    private BigDecimal totalPrepayRent;    //总预付租金
    private BigDecimal totalOtherPaid;    //总其他费用
    private BigDecimal totalIncome;    //总收入
    private Page<StatisticsIncomeDetail> statisticsIncomeDetailPage;    //统计项分页


    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public BigDecimal getTotalDeposit() {
        return totalDeposit;
    }

    public void setTotalDeposit(BigDecimal totalDeposit) {
        this.totalDeposit = totalDeposit;
    }

    public BigDecimal getTotalRentDeposit() {
        return totalRentDeposit;
    }

    public void setTotalRentDeposit(BigDecimal totalRentDeposit) {
        this.totalRentDeposit = totalRentDeposit;
    }

    public BigDecimal getTotalReturnDeposit() {
        return totalReturnDeposit;
    }

    public void setTotalReturnDeposit(BigDecimal totalReturnDeposit) {
        this.totalReturnDeposit = totalReturnDeposit;
    }

    public BigDecimal getTotalReturnRentDeposit() {
        return totalReturnRentDeposit;
    }

    public void setTotalReturnRentDeposit(BigDecimal totalReturnRentDeposit) {
        this.totalReturnRentDeposit = totalReturnRentDeposit;
    }

    public BigDecimal getTotalRent() {
        return totalRent;
    }

    public void setTotalRent(BigDecimal totalRent) {
        this.totalRent = totalRent;
    }

    public BigDecimal getTotalPrepayRent() {
        return totalPrepayRent;
    }

    public void setTotalPrepayRent(BigDecimal totalPrepayRent) {
        this.totalPrepayRent = totalPrepayRent;
    }

    public BigDecimal getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(BigDecimal totalIncome) {
        this.totalIncome = totalIncome;
    }

    public BigDecimal getTotalOtherPaid() {
        return totalOtherPaid;
    }

    public void setTotalOtherPaid(BigDecimal totalOtherPaid) {
        this.totalOtherPaid = totalOtherPaid;
    }

    public Page<StatisticsIncomeDetail> getStatisticsIncomeDetailPage() {
        return statisticsIncomeDetailPage;
    }

    public void setStatisticsIncomeDetailPage(Page<StatisticsIncomeDetail> statisticsIncomeDetailPage) {
        this.statisticsIncomeDetailPage = statisticsIncomeDetailPage;
    }
}

