package com.lxzl.erp.common.domain.order;

import java.io.Serializable;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-12-12 20:38
 */
public class LastRentPriceRequest implements Serializable {

    private Integer productSkuId;
    private Integer isNewProduct;
    private Integer materialId;
    private Integer isNewMaterial;
    private String customerNo;

    public Integer getProductSkuId() {
        return productSkuId;
    }

    public void setProductSkuId(Integer productSkuId) {
        this.productSkuId = productSkuId;
    }

    public Integer getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Integer materialId) {
        this.materialId = materialId;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public Integer getIsNewProduct() {
        return isNewProduct;
    }

    public void setIsNewProduct(Integer isNewProduct) {
        this.isNewProduct = isNewProduct;
    }

    public Integer getIsNewMaterial() {
        return isNewMaterial;
    }

    public void setIsNewMaterial(Integer isNewMaterial) {
        this.isNewMaterial = isNewMaterial;
    }
}
