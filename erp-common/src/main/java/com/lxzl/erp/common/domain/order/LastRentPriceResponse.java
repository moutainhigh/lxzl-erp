package com.lxzl.erp.common.domain.order;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-12-12 20:33
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LastRentPriceResponse implements Serializable {
    private Integer productSkuId;                   // SKU ID
    private BigDecimal productSkuLastDayPrice;      // 商品上次租赁天的价格
    private BigDecimal productSkuLastMonthPrice;    // 商品上次租赁月的价格
    private BigDecimal productSkuDayPrice;          // 商品公司设定天租赁价格
    private BigDecimal productSkuMonthPrice;        // 商品公司设定月租赁价格
    private Integer materialId;                     // 物料ID
    private BigDecimal materialLastDayPrice;        // 物料上次租赁天的价格
    private BigDecimal materialLastMonthPrice;      // 物料上次租赁月的价格
    private BigDecimal materialDayPrice;            // 物料公司设定天租赁价格
    private BigDecimal materialMonthPrice;          // 物料公司设定月租赁价格
    private String customerNo;                      // 客户NO

    public Integer getProductSkuId() {
        return productSkuId;
    }

    public void setProductSkuId(Integer productSkuId) {
        this.productSkuId = productSkuId;
    }

    public BigDecimal getProductSkuDayPrice() {
        return productSkuDayPrice;
    }

    public void setProductSkuDayPrice(BigDecimal productSkuDayPrice) {
        this.productSkuDayPrice = productSkuDayPrice;
    }

    public BigDecimal getProductSkuMonthPrice() {
        return productSkuMonthPrice;
    }

    public void setProductSkuMonthPrice(BigDecimal productSkuMonthPrice) {
        this.productSkuMonthPrice = productSkuMonthPrice;
    }

    public Integer getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Integer materialId) {
        this.materialId = materialId;
    }

    public BigDecimal getMaterialDayPrice() {
        return materialDayPrice;
    }

    public void setMaterialDayPrice(BigDecimal materialDayPrice) {
        this.materialDayPrice = materialDayPrice;
    }

    public BigDecimal getMaterialMonthPrice() {
        return materialMonthPrice;
    }

    public void setMaterialMonthPrice(BigDecimal materialMonthPrice) {
        this.materialMonthPrice = materialMonthPrice;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public BigDecimal getProductSkuLastDayPrice() {
        return productSkuLastDayPrice;
    }

    public void setProductSkuLastDayPrice(BigDecimal productSkuLastDayPrice) {
        this.productSkuLastDayPrice = productSkuLastDayPrice;
    }

    public BigDecimal getProductSkuLastMonthPrice() {
        return productSkuLastMonthPrice;
    }

    public void setProductSkuLastMonthPrice(BigDecimal productSkuLastMonthPrice) {
        this.productSkuLastMonthPrice = productSkuLastMonthPrice;
    }

    public BigDecimal getMaterialLastDayPrice() {
        return materialLastDayPrice;
    }

    public void setMaterialLastDayPrice(BigDecimal materialLastDayPrice) {
        this.materialLastDayPrice = materialLastDayPrice;
    }

    public BigDecimal getMaterialLastMonthPrice() {
        return materialLastMonthPrice;
    }

    public void setMaterialLastMonthPrice(BigDecimal materialLastMonthPrice) {
        this.materialLastMonthPrice = materialLastMonthPrice;
    }
}
