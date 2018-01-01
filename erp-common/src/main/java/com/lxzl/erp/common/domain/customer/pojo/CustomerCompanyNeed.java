package com.lxzl.erp.common.domain.customer.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.validGroup.customer.AddCustomerCompanyGroup;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;


/**
 * @Author: your name
 * @Description：
 * @Date: Created in 21:08 2017/12/29
 * @Modified By:
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerCompanyNeed {
    @NotNull(message = ErrorCode.CUSTOMER_COMPANY_NEED_SKU_ID_NOT_NULL,groups = {AddCustomerCompanyGroup.class})
    private Integer skuId;
    @NotNull(message = ErrorCode.CUSTOMER_COMPANY_NEED_UNIT_PRICE_NOT_NULL,groups = {AddCustomerCompanyGroup.class})
    private BigDecimal unitPrice;  //设备单台价值
    @NotNull(message = ErrorCode.CUSTOMER_COMPANY_NEED_RENT_COUNT_NOT_NULL,groups = {AddCustomerCompanyGroup.class})
    private Integer rentCount;  //租赁数量
    private BigDecimal totalPrice;  //总价值
    @NotNull(message = ErrorCode.CUSTOMER_COMPANY_NEED_RENT_LENGHT_NOT_NULL,groups = {AddCustomerCompanyGroup.class})
    private Integer rentLength;  //租赁期限

    private String skuName;

    public Integer getSkuId() {
        return skuId;
    }

    public void setSkuId(Integer skuId) {
        this.skuId = skuId;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getRentCount() {
        return rentCount;
    }

    public void setRentCount(Integer rentCount) {
        this.rentCount = rentCount;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getRentLength() {
        return rentLength;
    }

    public void setRentLength(Integer rentLength) {
        this.rentLength = rentLength;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }
}
