package com.lxzl.erp.common.domain.customer.pojo;

/**
 * @Author: Sunzhipeng
 * @Description: 客户在租商品、配件、付费配件数量
 * @Date: Created in 2018\6\28 0028 14:00
 */
public class CustomerRentCount {
    private Integer rentProductCount;//再租商品数
    private Integer rentMaterialCount;//再租商品数
    private Integer rentAmountMaterialCount;//再租付费配件数

    public Integer getRentProductCount() {
        return rentProductCount;
    }

    public void setRentProductCount(Integer rentProductCount) {
        this.rentProductCount = rentProductCount;
    }

    public Integer getRentMaterialCount() {
        return rentMaterialCount;
    }

    public void setRentMaterialCount(Integer rentMaterialCount) {
        this.rentMaterialCount = rentMaterialCount;
    }

    public Integer getRentAmountMaterialCount() {
        return rentAmountMaterialCount;
    }

    public void setRentAmountMaterialCount(Integer rentAmountMaterialCount) {
        this.rentAmountMaterialCount = rentAmountMaterialCount;
    }
}
