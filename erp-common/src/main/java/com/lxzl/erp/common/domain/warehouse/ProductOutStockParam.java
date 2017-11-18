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
    private List<Integer> productEquipmentIdList;
    private List<Integer> bulkMaterialIdList;
    private Integer srcWarehouseId;
    private Integer targetWarehouseId;
    private Integer causeType;
    private String referNo;


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

    public List<Integer> getProductEquipmentIdList() {
        return productEquipmentIdList;
    }

    public void setProductEquipmentIdList(List<Integer> productEquipmentIdList) {
        this.productEquipmentIdList = productEquipmentIdList;
    }

    public List<Integer> getBulkMaterialIdList() {
        return bulkMaterialIdList;
    }

    public void setBulkMaterialIdList(List<Integer> bulkMaterialIdList) {
        this.bulkMaterialIdList = bulkMaterialIdList;
    }
}
