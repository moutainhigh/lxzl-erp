package com.lxzl.erp.common.domain.warehouse;

import com.lxzl.erp.common.domain.material.pojo.MaterialInStorage;
import com.lxzl.erp.common.domain.product.pojo.ProductInStorage;

import java.io.Serializable;
import java.util.List;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-11-07 20:23
 */
public class ProductInStockParam implements Serializable {
    private List<ProductInStorage> productInStorageList;
    private List<MaterialInStorage> materialInStorageList;
    private Integer srcWarehouseId;
    private Integer targetWarehouseId;
    private Integer causeType;
    private String referNo;
    private Integer itemReferId;

    public List<ProductInStorage> getProductInStorageList() {
        return productInStorageList;
    }

    public void setProductInStorageList(List<ProductInStorage> productInStorageList) {
        this.productInStorageList = productInStorageList;
    }

    public Integer getSrcWarehouseId() {
        return srcWarehouseId;
    }

    public void setSrcWarehouseId(Integer srcWarehouseId) {
        this.srcWarehouseId = srcWarehouseId;
    }

    public Integer getTargetWarehouseId() {
        return targetWarehouseId;
    }

    public void setTargetWarehouseId(Integer targetWarehouseId) {
        this.targetWarehouseId = targetWarehouseId;
    }

    public Integer getCauseType() {
        return causeType;
    }

    public void setCauseType(Integer causeType) {
        this.causeType = causeType;
    }

    public String getReferNo() {
        return referNo;
    }

    public void setReferNo(String referNo) {
        this.referNo = referNo;
    }

    public List<MaterialInStorage> getMaterialInStorageList() {
        return materialInStorageList;
    }

    public void setMaterialInStorageList(List<MaterialInStorage> materialInStorageList) {
        this.materialInStorageList = materialInStorageList;
    }

    public Integer getItemReferId() {
        return itemReferId;
    }

    public void setItemReferId(Integer itemReferId) {
        this.itemReferId = itemReferId;
    }
}
