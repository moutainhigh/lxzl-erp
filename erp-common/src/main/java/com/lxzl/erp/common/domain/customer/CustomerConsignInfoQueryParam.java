package com.lxzl.erp.common.domain.customer;

import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.base.BasePageParam;
import com.lxzl.erp.common.domain.validGroup.IdGroup;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Date;

public class CustomerConsignInfoQueryParam extends BasePageParam {


    @NotBlank(message = ErrorCode.CUSTOMER_NO_NOT_NULL,groups = {IdGroup.class})
    private String customerNo;  //客户编号
    private Integer customerConsignInfoId; //地址信息ID
    private Integer customerId;   //客户ID
    private String consigneeName;   //收货人姓名
    private String consigneePhone;   //收货人手机号
    private Integer province;   //省份ID，省份ID
    private Integer city;   //城市ID，对应城市ID
    private Integer district;   //区ID，对应区ID
    private String address;   //详细地址
    private Integer isMain;   //是否为默认地址，0否1是
    private Date lastUseTime;  //最后使用时间


    public Integer getCustomerConsignInfoId() {
        return customerConsignInfoId;
    }

    public void setCustomerConsignInfoId(Integer customerConsignInfoId) {this.customerConsignInfoId = customerConsignInfoId; }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getConsigneeName() {
        return consigneeName;
    }

    public void setConsigneeName(String consigneeName) {
        this.consigneeName = consigneeName;
    }

    public String getConsigneePhone() {
        return consigneePhone;
    }

    public void setConsigneePhone(String consigneePhone) {
        this.consigneePhone = consigneePhone;
    }

    public Integer getProvince() {
        return province;
    }

    public void setProvince(Integer province) {
        this.province = province;
    }

    public Integer getCity() {
        return city;
    }

    public void setCity(Integer city) {
        this.city = city;
    }

    public Integer getDistrict() {
        return district;
    }

    public void setDistrict(Integer district) {
        this.district = district;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getIsMain() {
        return isMain;
    }

    public void setIsMain(Integer isMain) {
        this.isMain = isMain;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public Date getLastUseTime() {
        return lastUseTime;
    }

    public void setLastUseTime(Date lastUseTime) {
        this.lastUseTime = lastUseTime;
    }

}
