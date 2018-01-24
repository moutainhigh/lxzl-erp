package com.lxzl.erp.common.domain.customer;

import com.lxzl.erp.common.domain.base.BasePageParam;

import java.util.List;

public class CustomerCompanyQueryParam extends BasePageParam {

    private Integer customerId;
    private String customerNo;   //客户编号
    private String connectRealName;   //紧急联系人
    private String connectPhone;   //紧急联系人手机号
    private String companyName;   //公司名称
    private String fullCompanyName;   //公司完整名称
    private Integer province;   //省份ID，省份ID
    private Integer city;   //城市ID，对应城市ID
    private Integer district;   //区ID，对应区ID
    private String legalPerson;   //法人姓名
    private String legalPersonNo;   //法人身份证号
    private String businessLicenseNo;   //营业执照号
    private String industry;  //所属行业'
    private Integer officeNumber;  //办公人数'
    private String productPurpose;  //设备用途'
    private String agentPersonName;  //经办人姓名'
    private String agentPersonPhone;  //经办人电话'
    private String agentPersonNo;  //经办人身份证号码'
    private String unifiedCreditCode;  //统一信用代码'
    private String affiliatedEnterprise; //关联企业

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

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public Integer getOfficeNumber() {
        return officeNumber;
    }

    public void setOfficeNumber(Integer officeNumber) {
        this.officeNumber = officeNumber;
    }

    public String getProductPurpose() {
        return productPurpose;
    }

    public void setProductPurpose(String productPurpose) {
        this.productPurpose = productPurpose;
    }

    public String getAgentPersonName() {
        return agentPersonName;
    }

    public void setAgentPersonName(String agentPersonName) {
        this.agentPersonName = agentPersonName;
    }

    public String getAgentPersonPhone() {
        return agentPersonPhone;
    }

    public void setAgentPersonPhone(String agentPersonPhone) {
        this.agentPersonPhone = agentPersonPhone;
    }

    public String getAgentPersonNo() {
        return agentPersonNo;
    }

    public void setAgentPersonNo(String agentPersonNo) {
        this.agentPersonNo = agentPersonNo;
    }

    public String getUnifiedCreditCode() {
        return unifiedCreditCode;
    }

    public void setUnifiedCreditCode(String unifiedCreditCode) {
        this.unifiedCreditCode = unifiedCreditCode;
    }

    public String getAffiliatedEnterprise() {
        return affiliatedEnterprise;
    }

    public void setAffiliatedEnterprise(String affiliatedEnterprise) {
        this.affiliatedEnterprise = affiliatedEnterprise;
    }

    public List<Integer> getPassiveUserIdList() {
        return passiveUserIdList;
    }

    public void setPassiveUserIdList(List<Integer> passiveUserIdList) {
        this.passiveUserIdList = passiveUserIdList;
    }

    public String getFullCompanyName() {
        return fullCompanyName;
    }

    public void setFullCompanyName(String fullCompanyName) {
        this.fullCompanyName = fullCompanyName;
    }
}
