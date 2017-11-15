package com.lxzl.erp.common.domain.customer;

import com.lxzl.erp.common.domain.base.BasePageParam;

public class CustomerCompanyQueryParam extends BasePageParam {

    private Integer customerId;
    private String customerNo;   //客户编号
    private String connectRealName;   //联系人
    private String connectPhone;   //联系人手机号
    private String companyName;   //公司名称
    private Integer province;   //省份ID，省份ID
    private Integer city;   //城市ID，对应城市ID
    private Integer district;   //区ID，对应区ID
    private String legalPerson;   //法人姓名
    private String legalPersonNo;   //法人身份证号
    private String businessLicenseNo;   //营业执照号

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

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
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

    public String getLegalPerson() {
        return legalPerson;
    }

    public void setLegalPerson(String legalPerson) {
        this.legalPerson = legalPerson;
    }

    public String getLegalPersonNo() {
        return legalPersonNo;
    }

    public void setLegalPersonNo(String legalPersonNo) {
        this.legalPersonNo = legalPersonNo;
    }

    public String getBusinessLicenseNo() {
        return businessLicenseNo;
    }

    public void setBusinessLicenseNo(String businessLicenseNo) {
        this.businessLicenseNo = businessLicenseNo;
    }
}
