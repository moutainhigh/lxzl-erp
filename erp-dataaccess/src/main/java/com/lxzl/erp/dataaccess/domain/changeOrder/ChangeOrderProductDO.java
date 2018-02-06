package com.lxzl.erp.dataaccess.domain.changeOrder;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;


public class ChangeOrderProductDO extends BaseDO {

    private Integer id;
    private Integer changeOrderId;
    private String changeOrderNo;
    private Integer srcChangeProductSkuId;
    private Integer destChangeProductSkuId;
    private Integer changeProductSkuCount;
    private Integer realChangeProductSkuCount;
    private String srcChangeProductSkuSnapshot;
    private String destChangeProductSkuSnapshot;
    private Integer dataStatus;
    private String remark;
    private Integer isNew;

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

    public Integer getSrcChangeProductSkuId() {
        return srcChangeProductSkuId;
    }

    public void setSrcChangeProductSkuId(Integer srcChangeProductSkuId) {
        this.srcChangeProductSkuId = srcChangeProductSkuId;
    }

    public Integer getDestChangeProductSkuId() {
        return destChangeProductSkuId;
    }

    public void setDestChangeProductSkuId(Integer destChangeProductSkuId) {
        this.destChangeProductSkuId = destChangeProductSkuId;
    }

    public Integer getChangeProductSkuCount() {
        return changeProductSkuCount;
    }

    public void setChangeProductSkuCount(Integer changeProductSkuCount) {
        this.changeProductSkuCount = changeProductSkuCount;
    }

    public Integer getRealChangeProductSkuCount() {
        return realChangeProductSkuCount;
    }

    public void setRealChangeProductSkuCount(Integer realChangeProductSkuCount) {
        this.realChangeProductSkuCount = realChangeProductSkuCount;
    }

    public String getSrcChangeProductSkuSnapshot() {
        return srcChangeProductSkuSnapshot;
    }

    public void setSrcChangeProductSkuSnapshot(String srcChangeProductSkuSnapshot) {
        this.srcChangeProductSkuSnapshot = srcChangeProductSkuSnapshot;
    }

    public String getDestChangeProductSkuSnapshot() {
        return destChangeProductSkuSnapshot;
    }

    public void setDestChangeProductSkuSnapshot(String destChangeProductSkuSnapshot) {
        this.destChangeProductSkuSnapshot = destChangeProductSkuSnapshot;
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
}