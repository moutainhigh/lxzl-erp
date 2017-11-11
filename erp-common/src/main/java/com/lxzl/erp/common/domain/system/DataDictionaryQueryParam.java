package com.lxzl.erp.common.domain.system;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.se.dataaccess.mysql.config.PageQuery;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: gaochao
 * Date: 2016/11/8.
 * Time: 16:48.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataDictionaryQueryParam extends PageQuery implements Serializable {

    private Integer dataId;

    private String dataName;

    private Integer dataType;

    private Integer dataStatus;

    public Integer getDataId() {
        return dataId;
    }

    public void setDataId(Integer dataId) {
        this.dataId = dataId;
    }

    public String getDataName() {
        return dataName;
    }

    public void setDataName(String dataName) {
        this.dataName = dataName;
    }

    public Integer getDataType() {
        return dataType;
    }

    public void setDataType(Integer dataType) {
        this.dataType = dataType;
    }

    public Integer getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(Integer dataStatus) {
        this.dataStatus = dataStatus;
    }
}
