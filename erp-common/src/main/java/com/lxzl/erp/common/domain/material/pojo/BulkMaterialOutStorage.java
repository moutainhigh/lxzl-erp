package com.lxzl.erp.common.domain.material.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-11-16 15:58
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BulkMaterialOutStorage implements Serializable {
    private Integer bulkMaterialId;
    private Integer bulkMaterialCount;

    public Integer getBulkMaterialId() {
        return bulkMaterialId;
    }

    public void setBulkMaterialId(Integer bulkMaterialId) {
        this.bulkMaterialId = bulkMaterialId;
    }

    public Integer getBulkMaterialCount() {
        return bulkMaterialCount;
    }

    public void setBulkMaterialCount(Integer bulkMaterialCount) {
        this.bulkMaterialCount = bulkMaterialCount;
    }
}
