package com.lxzl.erp.common.domain.system.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePO;

import java.io.Serializable;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: gaochao
 * Date: 2016/11/2.
 * Time: 10:42.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataDictionary extends BasePO {

    private Integer value;

    private String label;

    private Integer parentDictionaryId;

    private Integer dataType;

    private Integer dataOrder;

    private Integer dataStatus;

    private String remark;

    private List<DataDictionary> children;

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
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

    public List<DataDictionary> getChildren() {
        return children;
    }

    public void setChildren(List<DataDictionary> children) {
        this.children = children;
    }
}
