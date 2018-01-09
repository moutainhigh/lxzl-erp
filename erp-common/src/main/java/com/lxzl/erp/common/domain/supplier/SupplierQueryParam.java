package com.lxzl.erp.common.domain.supplier;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePageParam;
import com.lxzl.se.dataaccess.mysql.config.PageQuery;

import java.io.Serializable;
import java.util.Date;


@JsonIgnoreProperties(ignoreUnknown = true)
public class SupplierQueryParam extends BasePageParam {

    private Integer supplierId;   //字典ID，唯一
    private String supplierNo;   //供应商编号
    private String supplierName;   //供应商名称
    private String supplierCode;	//供应商自定义编码


    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getSupplierNo() {
        return supplierNo;
    }

    public void setSupplierNo(String supplierNo) {
        this.supplierNo = supplierNo;
    }

    public String getSupplierCode() { return supplierCode; }

    public void setSupplierCode(String supplierCode) { this.supplierCode = supplierCode; }
}