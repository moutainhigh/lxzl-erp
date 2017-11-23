package com.lxzl.erp.common.domain.product.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Product implements Serializable {
    private Integer productId;
    private String productNo;
    private String productName;
    private Integer brandId;
    private Integer categoryId;
    private String subtitle;
    private Integer unit;
    private BigDecimal listPrice;
    private Integer isRent;
    private String productDesc;
    private String keyword;
    private String remark;
    private Integer dataStatus;

    private List<ProductImg> productImgList;           // 商品图片
    private List<ProductImg> productDescImgList;           // 商品图片
    private List<ProductSku> productSkuList;                // 商品SKU
    private List<ProductSkuProperty> productPropertyList;   // 商品属性
    private List<ProductCategoryProperty> productCategoryPropertyList;

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductNo() {
        return productNo;
    }

    public void setProductNo(String productNo) {
        this.productNo = productNo;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public Integer getUnit() {
        return unit;
    }

    public void setUnit(Integer unit) {
        this.unit = unit;
    }

    public BigDecimal getListPrice() {
        return listPrice;
    }

    public void setListPrice(BigDecimal listPrice) {
        this.listPrice = listPrice;
    }

    public Integer getIsRent() {
        return isRent;
    }

    public void setIsRent(Integer isRent) {
        this.isRent = isRent;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(Integer dataStatus) {
        this.dataStatus = dataStatus;
    }

    public List<ProductSku> getProductSkuList() {
        return productSkuList;
    }

    public void setProductSkuList(List<ProductSku> productSkuList) {
        this.productSkuList = productSkuList;
    }

    public List<ProductSkuProperty> getProductPropertyList() {
        return productPropertyList;
    }

    public void setProductPropertyList(List<ProductSkuProperty> productPropertyList) {
        this.productPropertyList = productPropertyList;
    }

    public List<ProductImg> getProductImgList() {
        return productImgList;
    }

    public void setProductImgList(List<ProductImg> productImgList) {
        this.productImgList = productImgList;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public List<ProductCategoryProperty> getProductCategoryPropertyList() {
        return productCategoryPropertyList;
    }

    public void setProductCategoryPropertyList(List<ProductCategoryProperty> productCategoryPropertyList) {
        this.productCategoryPropertyList = productCategoryPropertyList;
    }

    public List<ProductImg> getProductDescImgList() {
        return productDescImgList;
    }

    public void setProductDescImgList(List<ProductImg> productDescImgList) {
        this.productDescImgList = productDescImgList;
    }
}
