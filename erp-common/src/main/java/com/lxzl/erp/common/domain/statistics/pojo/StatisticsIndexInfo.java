package com.lxzl.erp.common.domain.statistics.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

/**
 * 描述: 统计首页数据
 *
 * @author gaochao
 * @date 2017-12-25 9:04
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class StatisticsIndexInfo implements Serializable {

    /**
     * 平台租赁总额
     */
    private BigDecimal totalRentAmount;

    /**
     * 平台总用户量
     */
    private Integer totalCustomerCount;

    /**
     * 平台总设备数量
     */
    private Integer totalProductEquipmentCount;

    /**
     * 分公司业绩表
     */
    private Map<String, BigDecimal> subCompanyRentAmount;

    /**
     * 月度业绩表
     */
    private Map<Integer, BigDecimal> monthRentAmount;

    public BigDecimal getTotalRentAmount() {
        return totalRentAmount;
    }

    public void setTotalRentAmount(BigDecimal totalRentAmount) {
        this.totalRentAmount = totalRentAmount;
    }

    public Integer getTotalCustomerCount() {
        return totalCustomerCount;
    }

    public void setTotalCustomerCount(Integer totalCustomerCount) {
        this.totalCustomerCount = totalCustomerCount;
    }

    public Integer getTotalProductEquipmentCount() {
        return totalProductEquipmentCount;
    }

    public void setTotalProductEquipmentCount(Integer totalProductEquipmentCount) {
        this.totalProductEquipmentCount = totalProductEquipmentCount;
    }

    public Map<String, BigDecimal> getSubCompanyRentAmount() {
        return subCompanyRentAmount;
    }

    public void setSubCompanyRentAmount(Map<String, BigDecimal> subCompanyRentAmount) {
        this.subCompanyRentAmount = subCompanyRentAmount;
    }

    public Map<Integer, BigDecimal> getMonthRentAmount() {
        return monthRentAmount;
    }

    public void setMonthRentAmount(Map<Integer, BigDecimal> monthRentAmount) {
        this.monthRentAmount = monthRentAmount;
    }
}
