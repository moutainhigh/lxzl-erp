package com.lxzl.erp.common.domain.changeOrder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.constant.ReturnOrChangeMode;
import com.lxzl.erp.common.domain.changeOrder.pojo.ChangeOrderConsignInfo;
import com.lxzl.erp.common.domain.validGroup.changeOrder.AddChangeOrderGroup;
import com.lxzl.erp.common.util.validate.constraints.In;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AddChangeOrderParam {

    @NotBlank(message = ErrorCode.CUSTOMER_NO_NOT_NULL)
    private String customerNo;
    @Valid
    private List<ChangeProductSkuPairs> changeProductSkuPairsList;
    @Valid
    private List<ChangeMaterialPairs> changeMaterialPairsList;
    @Valid
    @NotNull(message = ErrorCode.ORDER_CUSTOMER_CONSIGN_NOT_NULL)
    private ChangeOrderConsignInfo changeOrderConsignInfo;
    private String remark;
    private String changeReason;
    @In(value = {ReturnOrChangeMode.RETURN_OR_CHANGE_MODE_TO_DOOR, ReturnOrChangeMode.RETURN_OR_CHANGE_MODE_MAIL}, message = ErrorCode.RETURN_OR_CHANGE_MODE_ERROR)
    @NotNull(message = ErrorCode.RETURN_OR_CHANGE_MODE_NOT_NULL)
    private Integer changeMode;   //换货方式，1-上门取件，2邮寄

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public List<ChangeProductSkuPairs> getChangeProductSkuPairsList() {
        return changeProductSkuPairsList;
    }

    public void setChangeProductSkuPairsList(List<ChangeProductSkuPairs> changeProductSkuPairsList) {
        this.changeProductSkuPairsList = changeProductSkuPairsList;
    }

    public List<ChangeMaterialPairs> getChangeMaterialPairsList() {
        return changeMaterialPairsList;
    }

    public void setChangeMaterialPairsList(List<ChangeMaterialPairs> changeMaterialPairsList) {
        this.changeMaterialPairsList = changeMaterialPairsList;
    }

    public ChangeOrderConsignInfo getChangeOrderConsignInfo() {
        return changeOrderConsignInfo;
    }

    public void setChangeOrderConsignInfo(ChangeOrderConsignInfo changeOrderConsignInfo) {
        this.changeOrderConsignInfo = changeOrderConsignInfo;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getChangeReason() {
        return changeReason;
    }

    public void setChangeReason(String changeReason) {
        this.changeReason = changeReason;
    }

    public Integer getChangeMode() {
        return changeMode;
    }

    public void setChangeMode(Integer changeMode) {
        this.changeMode = changeMode;
    }
}
