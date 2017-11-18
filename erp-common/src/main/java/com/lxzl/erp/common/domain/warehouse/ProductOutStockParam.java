package com.lxzl.erp.common.domain.warehouse;

import com.lxzl.erp.common.domain.material.pojo.BulkMaterialOutStorage;
import com.lxzl.erp.common.domain.material.pojo.MaterialInStorage;
import com.lxzl.erp.common.domain.product.pojo.ProductEquipmentOutStorage;
import com.lxzl.erp.common.domain.product.pojo.ProductInStorage;

import java.io.Serializable;
import java.util.List;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-11-07 20:23
 */
public class ProductOutStockParam implements Serializable {
    private List<ProductEquipmentOutStorage> productEquipmentOutStorageList;
    private List<BulkMaterialOutStorage> bulkMaterialOutStorageList;
    private Integer srcWarehouseId;
    private Integer targetWarehouseId;
    private Integer causeType;
    private String referNo;


    public List<ProductEquipmentOutStorage> getProductEquipmentOutStorageList() {
        return productEquipmentOutStorageList;
    }

    public void setProductEquipmentOutStorageList(List<ProductEquipmentOutStorage> productEquipmentOutStorageList) {
        this.productEquipmentOutStorageList = productEquipmentOutStorageList;
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

    public List<BulkMaterialOutStorage> getBulkMaterialOutStorageList() {
        return bulkMaterialOutStorageList;
    }

    public void setBulkMaterialOutStorageList(List<BulkMaterialOutStorage> bulkMaterialOutStorageList) {
        this.bulkMaterialOutStorageList = bulkMaterialOutStorageList;
    }
}
