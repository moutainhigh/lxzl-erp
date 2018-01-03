package com.lxzl.erp.common.domain.warehouse.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePO;

import java.util.Date;


@JsonIgnoreProperties(ignoreUnknown = true)
public class StockOrderBulkMaterial extends BasePO {

    private Integer stockOrderBulkMaterialId;   //唯一标识
    private String stockOrderNo;   //出入库单编号
    private Integer itemReferId;    //关联项ID
    private Integer itemReferType;    //关联项类型，1-采购收货单商品项，2-采购收货单物料项
    private Integer bulkMaterialId;   //散料ID
    private String bulkMaterialNo;   //散料编号唯一
    private String bulkMaterialName; //散料名称
    private Integer dataStatus;   //状态：0不可用；1可用；2删除
    private String remark;   //备注
    private Date createTime;   //添加时间
    private String createUser;   //添加人
    private Date updateTime;   //添加时间
    private String updateUser;   //修改人

    public Integer getStockOrderBulkMaterialId() {
        return stockOrderBulkMaterialId;
    }

    public void setStockOrderBulkMaterialId(Integer stockOrderBulkMaterialId) {
        this.stockOrderBulkMaterialId = stockOrderBulkMaterialId;
    }

    public String getStockOrderNo() {
        return stockOrderNo;
    }

    public void setStockOrderNo(String stockOrderNo) {
        this.stockOrderNo = stockOrderNo;
    }

    public Integer getBulkMaterialId() {
        return bulkMaterialId;
    }

    public void setBulkMaterialId(Integer bulkMaterialId) {
        this.bulkMaterialId = bulkMaterialId;
    }

    public String getBulkMaterialNo() {
        return bulkMaterialNo;
    }

    public void setBulkMaterialNo(String bulkMaterialNo) {
        this.bulkMaterialNo = bulkMaterialNo;
    }

    public Integer getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(Integer dataStatus) {
        this.dataStatus = dataStatus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public String getBulkMaterialName() {
        return bulkMaterialName;
    }

    public void setBulkMaterialName(String bulkMaterialName) {
        this.bulkMaterialName = bulkMaterialName;
    }

    public Integer getItemReferId() {
        return itemReferId;
    }

    public void setItemReferId(Integer itemReferId) {
        this.itemReferId = itemReferId;
    }

    public Integer getItemReferType() {
        return itemReferType;
    }

    public void setItemReferType(Integer itemReferType) {
        this.itemReferType = itemReferType;
    }
}