package com.lxzl.erp.common.domain.warehouse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.se.dataaccess.mysql.config.PageQuery;

import java.io.Serializable;
import java.util.List;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-11-06 17:44
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class WarehouseQueryParam extends PageQuery implements Serializable {

    private Integer warehouseId;

    private String warehouseNo;

    private String warehouseName;

    private Integer subCompanyId;

    private String subCompanyName;

    private List<Integer> subCompanyIdList;

    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getWarehouseNo() {
        return warehouseNo;
    }

    public void setWarehouseNo(String warehouseNo) {
        this.warehouseNo = warehouseNo;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public Integer getSubCompanyId() {
        return subCompanyId;
    }

    public void setSubCompanyId(Integer subCompanyId) {
        this.subCompanyId = subCompanyId;
    }

    public String getSubCompanyName() {
        return subCompanyName;
    }

    public void setSubCompanyName(String subCompanyName) {
        this.subCompanyName = subCompanyName;
    }

    public List<Integer> getSubCompanyIdList() {
        return subCompanyIdList;
    }

    public void setSubCompanyIdList(List<Integer> subCompanyIdList) {
        this.subCompanyIdList = subCompanyIdList;
    }
}
