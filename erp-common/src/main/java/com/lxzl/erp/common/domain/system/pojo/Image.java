package com.lxzl.erp.common.domain.system.pojo;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.base.BasePO;
import com.lxzl.erp.common.domain.validGroup.customer.AddCustomerCompanyGroup;
import com.lxzl.erp.common.domain.validGroup.customer.AddCustomerReturnVisit;
import com.lxzl.erp.common.domain.validGroup.customer.UpdateCustomerCompanyGroup;
import com.lxzl.erp.common.domain.validGroup.customer.UpdateCustomerReturnVisit;

import javax.validation.constraints.NotNull;

/**
 * Created by IntelliJ IDEA.
 * ManagementUser: gaochao
 * Date: 2016/11/3.
 * Time: 9:26.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Image extends BasePO {

    @NotNull(message = ErrorCode.IMG_ID_NOT_NULL,groups = {AddCustomerCompanyGroup.class,UpdateCustomerCompanyGroup.class,AddCustomerReturnVisit.class, UpdateCustomerReturnVisit.class})
    private Integer imgId;

    private Integer imgType;

    private String originalName;
//    @NotBlank(message = ErrorCode.IMG_REF_ID_NOT_NULL,groups = {UpdateCustomerCompanyGroup.class})
    private String refId;

    private String imgUrl;

    private Integer imgOrder;

    private Integer dataStatus;

    private String remark;

    private String imgDomain;

    public Integer getImgId() {
        return imgId;
    }

    public void setImgId(Integer imgId) {
        this.imgId = imgId;
    }

    public Integer getImgType() {
        return imgType;
    }

    public void setImgType(Integer imgType) {
        this.imgType = imgType;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
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

    public String getImgDomain() {
        return imgDomain;
    }

    public void setImgDomain(String imgDomain) {
        this.imgDomain = imgDomain;
    }

    public Integer getImgOrder() {
        return imgOrder;
    }

    public void setImgOrder(Integer imgOrder) {
        this.imgOrder = imgOrder;
    }
}
