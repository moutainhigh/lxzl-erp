package com.lxzl.erp.common.domain.changeOrder.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.validGroup.ExtendGroup;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;


@JsonIgnoreProperties(ignoreUnknown = true)
public class ChangeOrderMaterial implements Serializable {
    @NotNull(message = ErrorCode.CHANGE_ORDER_MATERIAL_ID_NOT_NULL, groups = {ExtendGroup.class})
    private Integer changeOrderMaterialId;   //唯一标识
    private Integer changeOrderId;   //换货ID
    private String changeOrderNo;   //换货编号
    @NotBlank(message = ErrorCode.CHANGE_ORDER_SRC_MATERIAL_NO_NOT_NULL, groups = {ExtendGroup.class})
    private String srcChangeMaterialNo;   //原物料编号
    private Integer changeMaterialIdSrc;   //原物料ID
    private Integer changeMaterialIdDest;   //目标后物料ID
    private Integer changeMaterialCount;   //换货物料数量
    @NotNull(message = ErrorCode.CHANGE_COUNT_ERROR, groups = {ExtendGroup.class})
    @Min(value = 0, message = ErrorCode.CHANGE_COUNT_ERROR, groups = {ExtendGroup.class})
    private Integer realChangeMaterialCount;   //实际换货物料数量
    private String changeMaterialSnapshotSrc;   //换货前物料快照
    private String changeMaterialSnapshotDest;   //换货后物料快照
    private Integer dataStatus;   //状态：0不可用；1可用；2删除
    private String remark;   //备注
    private Date createTime;   //添加时间
    private String createUser;   //添加人
    private Date updateTime;   //添加时间
    private String updateUser;   //修改人


    private Integer canProcessCount;

    public Integer getChangeOrderMaterialId() {
        return changeOrderMaterialId;
    }

    public void setChangeOrderMaterialId(Integer changeOrderMaterialId) {
        this.changeOrderMaterialId = changeOrderMaterialId;
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

    public Integer getChangeMaterialIdSrc() {
        return changeMaterialIdSrc;
    }

    public void setChangeMaterialIdSrc(Integer changeMaterialIdSrc) {
        this.changeMaterialIdSrc = changeMaterialIdSrc;
    }

    public Integer getChangeMaterialIdDest() {
        return changeMaterialIdDest;
    }

    public void setChangeMaterialIdDest(Integer changeMaterialIdDest) {
        this.changeMaterialIdDest = changeMaterialIdDest;
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

    public String getChangeMaterialSnapshotSrc() {
        return changeMaterialSnapshotSrc;
    }

    public void setChangeMaterialSnapshotSrc(String changeMaterialSnapshotSrc) {
        this.changeMaterialSnapshotSrc = changeMaterialSnapshotSrc;
    }

    public String getChangeMaterialSnapshotDest() {
        return changeMaterialSnapshotDest;
    }

    public void setChangeMaterialSnapshotDest(String changeMaterialSnapshotDest) {
        this.changeMaterialSnapshotDest = changeMaterialSnapshotDest;
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

    public String getSrcChangeMaterialNo() {
        return srcChangeMaterialNo;
    }

    public void setSrcChangeMaterialNo(String srcChangeMaterialNo) {
        this.srcChangeMaterialNo = srcChangeMaterialNo;
    }

    public Integer getCanProcessCount() {
        return canProcessCount;
    }

    public void setCanProcessCount(Integer canProcessCount) {
        this.canProcessCount = canProcessCount;
    }
}