package com.lxzl.erp.common.domain.returnOrder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.constant.ReturnOrChangeMode;
import com.lxzl.erp.common.domain.returnOrder.pojo.ReturnOrderConsignInfo;
import com.lxzl.erp.common.util.validate.constraints.In;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateReturnOrderParam {

    @NotBlank(message = ErrorCode.RETURN_ORDER_NO_NOT_NULL)
    private String returnOrderNo;
    @Valid
    private List<ReturnSkuParam> productSkuList;
    @Valid
    private List<ReturnMaterialParam> materialList;
    @Valid
    @NotNull(message = ErrorCode.ORDER_CUSTOMER_CONSIGN_NOT_NULL)
    private ReturnOrderConsignInfo returnOrderConsignInfo;
    private String remark;
    @NotNull(message = ErrorCode.RETURN_ORDER_IS_CHARGING_IS_NOT_NULL)
    private Integer isCharging;

    @In(value = {ReturnOrChangeMode.RETURN_OR_CHANGE_MODE_TO_DOOR, ReturnOrChangeMode.RETURN_OR_CHANGE_MODE_MAIL}, message = ErrorCode.RETURN_OR_CHANGE_MODE_ERROR)
    @NotNull(message = ErrorCode.RETURN_OR_CHANGE_MODE_NOT_NULL)
    private Integer returnMode;   //退还方式，1-上门取件，2邮寄

    public String getReturnOrderNo() {
        return returnOrderNo;
    }

    public void setReturnOrderNo(String returnOrderNo) {
        this.returnOrderNo = returnOrderNo;
    }

    public List<ReturnSkuParam> getProductSkuList() {
        return productSkuList;
    }

    public void setProductSkuList(List<ReturnSkuParam> productSkuList) {
        this.productSkuList = productSkuList;
    }

    public List<ReturnMaterialParam> getMaterialList() {
        return materialList;
    }

    public void setMaterialList(List<ReturnMaterialParam> materialList) {
        this.materialList = materialList;
    }

    public ReturnOrderConsignInfo getReturnOrderConsignInfo() {
        return returnOrderConsignInfo;
    }

    public void setReturnOrderConsignInfo(ReturnOrderConsignInfo returnOrderConsignInfo) {
        this.returnOrderConsignInfo = returnOrderConsignInfo;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getIsCharging() {
        return isCharging;
    }

    public void setIsCharging(Integer isCharging) {
        this.isCharging = isCharging;
    }

    public Integer getReturnMode() {
        return returnMode;
    }

    public void setReturnMode(Integer returnMode) {
        this.returnMode = returnMode;
    }
}
