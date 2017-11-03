package com.lxzl.erp.common.domain.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.se.dataaccess.mysql.config.PageQuery;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductQueryParam extends PageQuery implements Serializable {

    private Integer productId;
    private String productName;
    private Integer isSale;

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getIsSale() {
        return isSale;
    }

    public void setIsSale(Integer isSale) {
        this.isSale = isSale;
    }
}
