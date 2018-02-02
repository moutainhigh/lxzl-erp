package com.lxzl.erp.common.domain.statistics.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author lk
 * @Description: 短租首页统计
 * @date 2018/1/30 17:07
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class StatisticsShortRent implements Serializable {
    private Integer newCustomerCount;   //新增客户
    private Integer orderCount;   //订单数（新客户下单、老客户下单）
    private Integer productCount;   //订单台数（新客户下单、老客户下单）
    private BigDecimal rentIncome;   //租金收入(租金和预付租金的和)

    public Integer getNewCustomerCount() {
        return newCustomerCount;
    }

    public void setNewCustomerCount(Integer newCustomerCount) {
        this.newCustomerCount = newCustomerCount;
    }

    public Integer getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Integer orderCount) {
        this.orderCount = orderCount;
    }

    public Integer getProductCount() {
        return productCount;
    }

    public void setProductCount(Integer productCount) {
        this.productCount = productCount;
    }

    public BigDecimal getRentIncome() {
        return rentIncome;
    }

    public void setRentIncome(BigDecimal rentIncome) {
        this.rentIncome = rentIncome;
    }
}
