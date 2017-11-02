package com.lxzl.erp.common.domain.materiel.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-10-30 19:57
 */
public class Materiel implements Serializable {
    private Integer id;

    private String materielNo;

    private String materielName;

    private Integer materielType;

    private BigDecimal materielPrice;

    private Integer dataStatus;

    private String remark;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMaterielNo() {
        return materielNo;
    }

    public void setMaterielNo(String materielNo) {
        this.materielNo = materielNo == null ? null : materielNo.trim();
    }

    public String getMaterielName() {
        return materielName;
    }

    public void setMaterielName(String materielName) {
        this.materielName = materielName == null ? null : materielName.trim();
    }

    public Integer getMaterielType() {
        return materielType;
    }

    public void setMaterielType(Integer materielType) {
        this.materielType = materielType;
    }

    public BigDecimal getMaterielPrice() {
        return materielPrice;
    }

    public void setMaterielPrice(BigDecimal materielPrice) {
        this.materielPrice = materielPrice;
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
        this.remark = remark == null ? null : remark.trim();
    }
}
