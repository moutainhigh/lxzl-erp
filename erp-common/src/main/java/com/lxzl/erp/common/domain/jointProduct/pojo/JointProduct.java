package com.lxzl.erp.common.domain.jointProduct.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.base.BasePO;
import com.lxzl.erp.common.domain.validGroup.AddGroup;
import com.lxzl.erp.common.domain.validGroup.IdGroup;
import com.lxzl.erp.common.domain.validGroup.UpdateGroup;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public class JointProduct extends BasePO {
    @NotNull(message = ErrorCode.JOINT_PRODUCT_ID_IS_NULL ,groups = {UpdateGroup.class,IdGroup.class})
    private Integer jointProductId;   //唯一标识
    @NotBlank(message = ErrorCode.JOINT_PRODUCT_NAME_IS_NULL, groups = {AddGroup.class,UpdateGroup.class})
    private String jointProductName;   //组合商品名称
    private Integer dataStatus;   //状态：0不可用；1可用；2删除
    private String remark;   //备注
    private Date createTime;   //添加时间
    private String createUser;   //添加人
    private Date updateTime;   //修改时间
    private String updateUser;   //修改人

    @Valid
    private List<JointMaterial> jointMaterialList;   //组合商品物料项表
    @Valid
    private List<JointProductSku> jointProductSkuList;   //组合商品sku项表

    public Integer getJointProductId() {
        return jointProductId;
    }

    public void setJointProductId(Integer jointProductId) {
        this.jointProductId = jointProductId;
    }

    public String getJointProductName() {
        return jointProductName;
    }

    public void setJointProductName(String jointProductName) {
        this.jointProductName = jointProductName;
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

    public List<JointMaterial> getJointMaterialList() {
        return jointMaterialList;
    }

    public void setJointMaterialList(List<JointMaterial> jointMaterialList) {
        this.jointMaterialList = jointMaterialList;
    }

    public List<JointProductSku> getJointProductSkuList() {
        return jointProductSkuList;
    }

    public void setJointProductSkuList(List<JointProductSku> jointProductSkuList) {
        this.jointProductSkuList = jointProductSkuList;
    }
}