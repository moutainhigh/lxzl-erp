package com.lxzl.erp.common.domain.k3;

/**
 * @Author: your name
 * @Description：
 * @Date: Created in 15:39 2018/8/8
 * @Modified By:
 */
public class QueryK3StockParam {
    private Integer subCompanyId; //分公司号（1-总公司，2-深圳分公司，3-上海分公司，4-北京分公司，5-广州分公司，6-南京分公司，7-厦门分公司，8-武汉分公司，9-成都分公司）
    private Integer warehouseType; //库位类型（1 分公司仓； 2 借出仓；3 全部（默认））
    private String k3Code;//物料编码
    private String productName; //商品名称
    private Integer productCount; //客户需求数量
    private Integer queryType; //查询类型，1-确认库存，0-查询库存

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getSubCompanyId() {
        return subCompanyId;
    }

    public void setSubCompanyId(Integer subCompanyId) {
        this.subCompanyId = subCompanyId;
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

    public Integer getProductCount() {
        return productCount;
    }

    public void setProductCount(Integer productCount) {
        this.productCount = productCount;
    }

    public Integer getQueryType() {
        return queryType;
    }

    public void setQueryType(Integer queryType) {
        this.queryType = queryType;
    }
}
