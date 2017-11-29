package com.lxzl.erp.common.domain.returnOrder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.material.pojo.Material;
import com.lxzl.erp.common.domain.product.pojo.ProductSku;
import com.lxzl.erp.common.domain.returnOrder.pojo.ReturnOrderConsignInfo;
import com.lxzl.erp.common.domain.validGroup.returnOrder.AddReturnOrderGroup;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AddReturnOrderParam {

    @NotBlank(message = ErrorCode.CUSTOMER_NO_NOT_NULL,groups = {AddReturnOrderGroup.class})
    private String customerNo;
    @Valid
    private List<ProductSku> productSkuList;
    @Valid
    private List<Material> materialList;
    @Valid
    @NotNull(message = ErrorCode.ORDER_CUSTOMER_CONSIGN_NOT_NULL,groups = {AddReturnOrderGroup.class})
    private ReturnOrderConsignInfo returnOrderConsignInfo;
    private String remark;
    @NotNull(message = ErrorCode.RETURN_ORDER_IS_CHARGING_IS_NOT_NULL,groups = {AddReturnOrderGroup.class})
    private Integer isCharging;
    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public List<ProductSku> getProductSkuList() {
        return productSkuList;
    }

    public void setProductSkuList(List<ProductSku> productSkuList) {
        this.productSkuList = productSkuList;
    }

    public List<Material> getMaterialList() {
        return materialList;
    }

    public void setMaterialList(List<Material> materialList) {
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
}
