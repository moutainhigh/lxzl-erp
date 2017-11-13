package com.lxzl.erp.common.domain.material;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePageParam;
import com.lxzl.se.dataaccess.mysql.config.PageQuery;

import java.io.Serializable;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-11-13 13:55
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BulkMaterialQueryParam extends BasePageParam {
    private Integer materialId;
    private String bulkMaterialNo;
    private String bulkMaterialName;
    private Integer bulkMaterialType;

    public Integer getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Integer materialId) {
        this.materialId = materialId;
    }

    public String getBulkMaterialNo() {
        return bulkMaterialNo;
    }

    public void setBulkMaterialNo(String bulkMaterialNo) {
        this.bulkMaterialNo = bulkMaterialNo;
    }

    public String getBulkMaterialName() {
        return bulkMaterialName;
    }

    public void setBulkMaterialName(String bulkMaterialName) {
        this.bulkMaterialName = bulkMaterialName;
    }

    public Integer getBulkMaterialType() {
        return bulkMaterialType;
    }

    public void setBulkMaterialType(Integer bulkMaterialType) {
        this.bulkMaterialType = bulkMaterialType;
    }
}
