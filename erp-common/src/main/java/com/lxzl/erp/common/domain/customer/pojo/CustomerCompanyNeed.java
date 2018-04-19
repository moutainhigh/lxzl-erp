package com.lxzl.erp.common.domain.customer.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.product.pojo.Product;
import com.lxzl.erp.common.domain.validGroup.customer.AddCustomerCompanyGroup;
import com.lxzl.erp.common.domain.validGroup.customer.UpdateCustomerCompanyGroup;

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
    @NotNull(message = ErrorCode.CUSTOMER_COMPANY_NEED_SKU_ID_NOT_NULL,groups = {AddCustomerCompanyGroup.class,UpdateCustomerCompanyGroup.class})
    private Integer skuId;
//    @NotNull(message = ErrorCode.CUSTOMER_COMPANY_NEED_UNIT_PRICE_NOT_NULL,groups = {AddCustomerCompanyGroup.class,UpdateCustomerCompanyGroup.class})
    private BigDecimal unitPrice;  //设备单台价值
    @NotNull(message = ErrorCode.CUSTOMER_COMPANY_NEED_RENT_COUNT_NOT_NULL,groups = {AddCustomerCompanyGroup.class,UpdateCustomerCompanyGroup.class})
    private Integer rentCount;  //租赁数量
    @NotNull(message = ErrorCode.CUSTOMER_COMPANY_NEED_IS_NEW_NOT_NULL,groups = {AddCustomerCompanyGroup.class,UpdateCustomerCompanyGroup.class})
    private Integer isNew;  //是否为新机,新是1，旧是0
    private BigDecimal totalPrice;  //总价值

    @NotNull(message = ErrorCode.CUSTOMER_COMPANY_NEED_RENT_LENGTH_NOT_NULL,groups = {AddCustomerCompanyGroup.class,UpdateCustomerCompanyGroup.class})
    private Integer rentTimeLength;  //租赁时长
    @NotNull(message = ErrorCode.CUSTOMER_COMPANY_NEED_RENT_LENGTH_NOT_NULL,groups = {AddCustomerCompanyGroup.class,UpdateCustomerCompanyGroup.class})
    private Integer rentType;  //租赁类型，1按天，2按月

    private Product product; //商品

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

    public Integer getRentTimeLength() {
        return rentTimeLength;
    }

    public void setRentTimeLength(Integer rentTimeLength) {
        this.rentTimeLength = rentTimeLength;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getIsNew() {
        return isNew;
    }

    public void setIsNew(Integer isNew) {
        this.isNew = isNew;
    }

    public Integer getRentType() {
        return rentType;
    }

    public void setRentType(Integer rentType) {
        this.rentType = rentType;
    }
}
