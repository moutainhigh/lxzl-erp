package com.lxzl.erp.dataaccess.domain.changeOrder;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import org.springframework.data.annotation.Transient;


public class ChangeOrderMaterialDO extends BaseDO {

    private Integer id;
    private Integer changeOrderId;
    private String changeOrderNo;
    private Integer srcChangeMaterialId;
    private Integer destChangeMaterialId;
    private Integer changeMaterialCount;
    private Integer realChangeMaterialCount;
    private String srcChangeMaterialSnapshot;
    private String destChangeMaterialSnapshot;
    private Integer dataStatus;
    private String remark;
    private Integer isNew;

    @Transient
    private String srcChangeMaterialNo;
    @Transient
    private String destChangeMaterialNo;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getChangeOrderId() {
        return changeOrderId;
    }

    public void setChangeOrderId(Integer changeOrderId) {
        this.changeOrderId = changeOrderId;
    }

    public String getChangeOrderNo() {
        return changeOrderNo;
    }

    public void setChangeOrderNo(String changeOrderNo) {
        this.changeOrderNo = changeOrderNo;
    }

    public Integer getSrcChangeMaterialId() {
        return srcChangeMaterialId;
    }

    public void setSrcChangeMaterialId(Integer srcChangeMaterialId) {
        this.srcChangeMaterialId = srcChangeMaterialId;
    }

    public Integer getDestChangeMaterialId() {
        return destChangeMaterialId;
    }

    public void setDestChangeMaterialId(Integer destChangeMaterialId) {
        this.destChangeMaterialId = destChangeMaterialId;
    }

    public Integer getChangeMaterialCount() {
        return changeMaterialCount;
    }

    public void setChangeMaterialCount(Integer changeMaterialCount) {
        this.changeMaterialCount = changeMaterialCount;
    }

    public Integer getRealChangeMaterialCount() {
        return realChangeMaterialCount;
    }

    public void setRealChangeMaterialCount(Integer realChangeMaterialCount) {
        this.realChangeMaterialCount = realChangeMaterialCount;
    }

    public String getSrcChangeMaterialSnapshot() {
        return srcChangeMaterialSnapshot;
    }

    public void setSrcChangeMaterialSnapshot(String srcChangeMaterialSnapshot) {
        this.srcChangeMaterialSnapshot = srcChangeMaterialSnapshot;
    }

    public String getDestChangeMaterialSnapshot() {
        return destChangeMaterialSnapshot;
    }

    public void setDestChangeMaterialSnapshot(String destChangeMaterialSnapshot) {
        this.destChangeMaterialSnapshot = destChangeMaterialSnapshot;
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

    public Integer getIsNew() {
        return isNew;
    }

    public void setIsNew(Integer isNew) {
        this.isNew = isNew;
    }

    public String getSrcChangeMaterialNo() {
        return srcChangeMaterialNo;
    }

    public void setSrcChangeMaterialNo(String srcChangeMaterialNo) {
        this.srcChangeMaterialNo = srcChangeMaterialNo;
    }

    public String getDestChangeMaterialNo() {
        return destChangeMaterialNo;
    }

    public void setDestChangeMaterialNo(String destChangeMaterialNo) {
        this.destChangeMaterialNo = destChangeMaterialNo;
    }
}