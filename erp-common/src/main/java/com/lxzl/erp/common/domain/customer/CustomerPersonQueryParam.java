package com.lxzl.erp.common.domain.customer;

import com.lxzl.erp.common.domain.base.BasePageParam;

import java.util.List;

public class CustomerPersonQueryParam extends BasePageParam {

    private Integer customerId;
    private String customerNo;//客户编号
    private String realName;   //真实姓名
    private String phone;   //手机号
    private Integer province;   //省份ID，省份ID
    private Integer city;   //城市ID，对应城市ID
    private Integer district;   //区ID，对应区ID
    private String personNo;//身份证号'
    private String connectRealName;//紧急联系人姓名'
    private String connectPhone;//紧急联系人电话'
    private Integer customerStatus; //客户的状态，0初始化，1资料提交，2审核通过，3资料驳回
    private Integer isDisabled;   //是否禁用，1不可用；0可用
    private Integer ownerSubCompanyId; //业务员所在分公司

    private List<Integer> passiveUserIdList;

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getPersonNo() {
        return personNo;
    }

    public void setPersonNo(String personNo) {
        this.personNo = personNo;
    }

    public String getConnectRealName() {
        return connectRealName;
    }

    public void setConnectRealName(String connectRealName) {
        this.connectRealName = connectRealName;
    }

    public String getConnectPhone() {
        return connectPhone;
    }

    public void setConnectPhone(String connectPhone) {
        this.connectPhone = connectPhone;
    }

    public Integer getIsDisabled() {
        return isDisabled;
    }

    public void setIsDisabled(Integer isDisabled) {
        this.isDisabled = isDisabled;
    }

    public Integer getCustomerStatus() {
        return customerStatus;
    }

    public void setCustomerStatus(Integer customerStatus) {
        this.customerStatus = customerStatus;
    }

    public List<Integer> getPassiveUserIdList() { return passiveUserIdList; }

    public void setPassiveUserIdList(List<Integer> passiveUserIdList) { this.passiveUserIdList = passiveUserIdList; }

    public Integer getOwnerSubCompanyId() {
        return ownerSubCompanyId;
    }

    public void setOwnerSubCompanyId(Integer ownerSubCompanyId) {
        this.ownerSubCompanyId = ownerSubCompanyId;
    }
}
