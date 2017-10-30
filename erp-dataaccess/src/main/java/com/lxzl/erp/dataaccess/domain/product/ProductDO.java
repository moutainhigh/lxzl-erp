package com.lxzl.erp.dataaccess.domain.product;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductDO extends BaseDO {
    private Integer id;
    private String productName;
    private Integer brandId;
    private Integer categoryId;
    private String subtitle;
    private Integer unit;
    private BigDecimal listPrice;
    private Integer isSale;
    private String productDesc;
    private String keyword;
    private String remark;
    private Integer dataStatus;

    private List<ProductImgDO> productImgDOList;           // 商品图片
    private List<ProductImgDO> productDescImgDOList;           // 商品详情图片

    private List<ProductSkuDO> productSkuDOList;
    private List<ProductSkuPropertyDO> productPropertyDOList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getIsSale() {
        return isSale;
    }

    public void setIsSale(Integer isSale) {
        this.isSale = isSale;
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

    public List<ProductSkuDO> getProductSkuDOList() {
        return productSkuDOList;
    }

    public void setProductSkuDOList(List<ProductSkuDO> productSkuDOList) {
        this.productSkuDOList = productSkuDOList;
    }

    public List<ProductSkuPropertyDO> getProductPropertyDOList() {
        return productPropertyDOList;
    }

    public void setProductPropertyDOList(List<ProductSkuPropertyDO> productPropertyDOList) {
        this.productPropertyDOList = productPropertyDOList;
    }

    public List<ProductImgDO> getProductImgDOList() {
        return productImgDOList;
    }

    public void setProductImgDOList(List<ProductImgDO> productImgDOList) {
        this.productImgDOList = productImgDOList;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public List<ProductImgDO> getProductDescImgDOList() {
        return productDescImgDOList;
    }

    public void setProductDescImgDOList(List<ProductImgDO> productDescImgDOList) {
        this.productDescImgDOList = productDescImgDOList;
    }
}
