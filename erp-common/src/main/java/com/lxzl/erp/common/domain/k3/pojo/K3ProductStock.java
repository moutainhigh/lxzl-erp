package com.lxzl.erp.common.domain.k3.pojo;

/**
 * @Author: your name
 * @Description：
 * @Date: Created in 17:04 2018/8/10
 * @Modified By:
 */
public class K3ProductStock {
    private String subCompanyCode; //分公司号（1000-总部,0755-深圳,021-上海,010-北京,020-广州,025-南京,0592-厦门,027-武汉,028-成都,2000-电销）
    private String subCompanyName; //分公司名称
    private Integer warehouseType; //库位类型（1 分公司仓； 2 借出仓；3 全部（默认））
    private String k3Code;//物料编码
    private String productName; //商品名称
    private Integer isStockEnough; //需求是否充足;1-库存充足，0-库存不足
    private Integer productStockCount; //库存数量

    public String getSubCompanyCode() {
        return subCompanyCode;
    }

    public void setSubCompanyCode(String subCompanyCode) {
        this.subCompanyCode = subCompanyCode;
    }

    public String getSubCompanyName() {
        return subCompanyName;
    }

    public void setSubCompanyName(String subCompanyName) {
        this.subCompanyName = subCompanyName;
    }

    public Integer getWarehouseType() {
        return warehouseType;
    }

    public void setWarehouseType(Integer warehouseType) {
        this.warehouseType = warehouseType;
    }

    public String getK3Code() {
        return k3Code;
    }

    public void setK3Code(String k3Code) {
        this.k3Code = k3Code;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getIsStockEnough() {
        return isStockEnough;
    }

    public void setIsStockEnough(Integer isStockEnough) {
        this.isStockEnough = isStockEnough;
    }

    public Integer getProductStockCount() {
        return productStockCount;
    }

    public void setProductStockCount(Integer productStockCount) {
        this.productStockCount = productStockCount;
    }
}
