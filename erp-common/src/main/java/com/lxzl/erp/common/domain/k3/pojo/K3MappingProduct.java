package com.lxzl.erp.common.domain.k3.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePO;


@JsonIgnoreProperties(ignoreUnknown = true)
public class K3MappingProduct extends BasePO {

    private Integer k3MappingProductId;   //唯一标识
    private String erpProductCode;   //erp商品编码
    private String erpSkuCode;
    private String k3ProductCode;   //K3商品编码
    private String k3SkuCode;
    private String productName;   //商品名称


    public Integer getK3MappingProductId() {
        return k3MappingProductId;
    }

    public void setK3MappingProductId(Integer k3MappingProductId) {
        this.k3MappingProductId = k3MappingProductId;
    }

    public String getErpProductCode() {
        return erpProductCode;
    }

    public void setErpProductCode(String erpProductCode) {
        this.erpProductCode = erpProductCode;
    }

    public String getK3ProductCode() {
        return k3ProductCode;
    }

    public void setK3ProductCode(String k3ProductCode) {
        this.k3ProductCode = k3ProductCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getErpSkuCode() {
        return erpSkuCode;
    }

    public void setErpSkuCode(String erpSkuCode) {
        this.erpSkuCode = erpSkuCode;
    }

    public String getK3SkuCode() {
        return k3SkuCode;
    }

    public void setK3SkuCode(String k3SkuCode) {
        this.k3SkuCode = k3SkuCode;
    }
}