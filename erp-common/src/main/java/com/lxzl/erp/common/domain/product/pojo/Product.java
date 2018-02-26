package com.lxzl.erp.common.domain.product.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePO;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Product extends BasePO {
    private Integer productId;
    private String productNo;
    private String productName;
    private String productModel;
    private Integer brandId;
    private String brandName;
    private Integer categoryId;
    private String categoryName;
    private String subtitle;
    private Integer unit;
    private BigDecimal listPrice;
    private Integer isRent;
    private String productDesc;
    private String keyword;
    private String remark;
    private Integer dataStatus;
    protected Date createTime;
    protected Date updateTime;
    private Integer isReturnAnyTime;    //是否允许随时归还，0否1是

    private List<ProductImg> productImgList;           // 商品图片
    private List<ProductImg> productDescImgList;           // 商品图片
    @Valid
    private List<ProductSku> productSkuList;                // 商品SKU
    private List<ProductSkuProperty> productPropertyList;   // 商品属性
    private List<ProductCategoryProperty> productCategoryPropertyList;

    private Integer newProductCount;     // 全新设备数量
    private Integer oldProductCount;     // 次新设备数量

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

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getProductModel() {
        return productModel;
    }

    public void setProductModel(String productModel) {
        this.productModel = productModel;
    }

    public Integer getNewProductCount() {
        return newProductCount;
    }

    public void setNewProductCount(Integer newProductCount) {
        this.newProductCount = newProductCount;
    }

    public Integer getOldProductCount() {
        return oldProductCount;
    }

    public void setOldProductCount(Integer oldProductCount) {
        this.oldProductCount = oldProductCount;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getIsReturnAnyTime() { return isReturnAnyTime; }

    public void setIsReturnAnyTime(Integer isReturnAnyTime) { this.isReturnAnyTime = isReturnAnyTime; }
}
