package com.lxzl.erp.dataaccess.domain.system;


import com.lxzl.se.dataaccess.mysql.domain.BaseDO;

import java.util.List;

public class DataDictionaryDO extends BaseDO {

    private Integer id;

    private String dataName;

    private Integer parentDictionaryId;

    private Integer dataType;

    private Integer dataOrder;

    private Integer dataStatus;

    private String remark;

    private List<DataDictionaryDO> children;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDataName() {
        return dataName;
    }

    public void setDataName(String dataName) {
        this.dataName = dataName;
    }

    public Integer getParentDictionaryId() {
        return parentDictionaryId;
    }

    public void setParentDictionaryId(Integer parentDictionaryId) {
        this.parentDictionaryId = parentDictionaryId;
    }

    public Integer getDataType() {
        return dataType;
    }

    public void setDataType(Integer dataType) {
        this.dataType = dataType;
    }

    public Integer getDataOrder() {
        return dataOrder;
    }

    public void setDataOrder(Integer dataOrder) {
        this.dataOrder = dataOrder;
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

    public List<DataDictionaryDO> getChildren() {
        return children;
    }

    public void setChildren(List<DataDictionaryDO> children) {
        this.children = children;
    }
}
