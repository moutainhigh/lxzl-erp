package com.lxzl.erp.common.domain.customer.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.base.BasePO;
import com.lxzl.erp.common.domain.system.pojo.Image;
import com.lxzl.erp.common.domain.validGroup.customer.AddCustomerCompanyGroup;
import com.lxzl.erp.common.domain.validGroup.customer.QueryCustomerGroup;
import com.lxzl.erp.common.domain.validGroup.customer.UpdateCustomerCompanyGroup;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerCompany extends BasePO {

    private Integer customerCompanyId;   //唯一标识
    private Integer customerId;   //客户ID
    private String customerNo;  //客户编码
    private String landline;   //座机电话
    @NotBlank(message = ErrorCode.CUSTOMER_COMPANY_CONNECT_NAME_NOT_NULL,groups = {AddCustomerCompanyGroup.class,UpdateCustomerCompanyGroup.class})
    private String connectRealName;   //紧急联系人
    @NotBlank(message = ErrorCode.CUSTOMER_COMPANY_CONNECT_PHONE_NOT_NULL,groups = {AddCustomerCompanyGroup.class,UpdateCustomerCompanyGroup.class})
    private String connectPhone;   //紧急联系人手机号
    @NotBlank(message = ErrorCode.CUSTOMER_COMPANY_NAME_NOT_NULL,groups = {AddCustomerCompanyGroup.class,UpdateCustomerCompanyGroup.class,QueryCustomerGroup.class})
    private String companyName;   //公司名称
    private String companyAbb;   //公司简称
    private Integer province;   //省份ID，省份ID
    private Integer city;   //城市ID，对应城市ID
    private Integer district;   //区ID，对应区ID
    @NotBlank(message = ErrorCode.CUSTOMER_COMPANY_ADDRESS_NOT_NULL,groups = {AddCustomerCompanyGroup.class,UpdateCustomerCompanyGroup.class})
    private String address;   //详细地址
    private Integer isLegalPersonApple;   //是否法人代表申请
    private String legalPerson;   //法人姓名
    private String legalPersonNo;   //法人身份证号
    private String legalPersonPhone;   //法人手机号
    private String businessLicenseNo;   //营业执照号
    private Integer dataStatus;   //状态：0不可用；1可用；2删除
    private String remark;   //备注
    private Date createTime;   //添加时间
    private String createUser;   //添加人
    private Date updateTime;   //添加时间
    private String updateUser;   //修改人

    private Integer customerOrigin; //客户来源,1地推活动，2展会了解，3业务联系，4百度推广，5朋友推荐，6其他广告
    private Date companyFoundTime;  //企业成立时间
    private String industry;  //所属行业
    private BigDecimal registeredCapital;  //注册资本
    private Integer officeNumber;  //办公人数
//    @NotBlank(message = ErrorCode.PRODUCT_PURPOSE_NOT_NULL,groups = {AddCustomerCompanyGroup.class,UpdateCustomerCompanyGroup.class})
    private String productPurpose;  //设备用途
//    @Valid
//    @NotEmpty(message = ErrorCode.CUSTOMER_COMPANY_NEED_FIRST_NOT_NULL,groups = {AddCustomerCompanyGroup.class,UpdateCustomerCompanyGroup.class})
    private List<CustomerCompanyNeed> customerCompanyNeedFirstList;  //首次所需设备
    private List<CustomerCompanyNeed> customerCompanyNeedLaterList;  //后续所需设备
    private String agentPersonName;  //经办人姓名
    private String agentPersonPhone;  //经办人电话
    private String agentPersonNo;  //经办人身份证号码
    private String unifiedCreditCode;  //统一信用代码
    private Double operatingArea; //经营面积
    private Integer unitInsuredNumber; //单位参保人数
    private String affiliatedEnterprise; //关联企业


    private String provinceName;// 省名
    private String cityName; //城市名
    private String districtName; //地区名




    @Valid
//    @NotNull(message = ErrorCode.BUSINESS_LICENSE_PICTURE_IMAGE_NOT_NULL,groups = {AddCustomerCompanyGroup.class,UpdateCustomerCompanyGroup.class})
    private Image businessLicensePictureImage;//营业执照
    @Valid
//    @NotNull(message = ErrorCode.LEGAL_PERSON_NO_PICTURE_FRONT_IMAGE_NOT_NULL,groups = {AddCustomerCompanyGroup.class,UpdateCustomerCompanyGroup.class})
    private Image legalPersonNoPictureFrontImage;//法人/股东身份证正面
    @Valid
//    @NotNull(message = ErrorCode.LEGAL_PERSON_NO_PICTURE_BACK_IMAGE_NOT_NULL,groups = {AddCustomerCompanyGroup.class,UpdateCustomerCompanyGroup.class})
    private Image legalPersonNoPictureBackImage;//法人/股东身份证反面
    @Valid
    private List<Image> managerPlaceRentContractImageList;//经营场所租赁合同
    @Valid
    private List<Image> legalPersonCreditReportImageList;//法人个人征信报告或附（法人个人征信授权书）
    @Valid
    private List<Image> fixedAssetsProveImageList;//固定资产证明
    @Valid
    private List<Image> publicAccountFlowBillImageList;//单位对公账户流水账单
    @Valid
    private List<Image> socialSecurityRoProvidentFundImageList;//社保/公积金缴纳证明
    @Valid
    private List<Image> cooperationAgreementImageList;//战略协议或合作协议
    @Valid
    private List<Image> legalPersonEducationImageList;//法人学历证明
    @Valid
    private List<Image> legalPersonPositionalTitleImageList;//法人职称证明
    @Valid
    private List<Image> localeChecklistsImageList;//现场核查表
    @Valid
    private List<Image> otherDateImageList;//其他材料


    public Integer getCustomerCompanyId(){
        return customerCompanyId;
    }

    public void setCustomerCompanyId(Integer customerCompanyId){
        this.customerCompanyId = customerCompanyId;
    }

    public Integer getCustomerId(){
        return customerId;
    }

    public void setCustomerId(Integer customerId){
        this.customerId = customerId;
    }

    public String getLandline(){
        return landline;
    }

    public void setLandline(String landline){
        this.landline = landline;
    }

    public String getConnectRealName(){
        return connectRealName;
    }

    public void setConnectRealName(String connectRealName){
        this.connectRealName = connectRealName;
    }

    public String getConnectPhone(){
        return connectPhone;
    }

    public void setConnectPhone(String connectPhone){
        this.connectPhone = connectPhone;
    }

    public String getCompanyName(){
        return companyName;
    }

    public void setCompanyName(String companyName){
        this.companyName = companyName;
    }

    public String getCompanyAbb(){
        return companyAbb;
    }

    public void setCompanyAbb(String companyAbb){
        this.companyAbb = companyAbb;
    }

    public Integer getProvince(){
        return province;
    }

    public void setProvince(Integer province){
        this.province = province;
    }

    public Integer getCity(){
        return city;
    }

    public void setCity(Integer city){
        this.city = city;
    }

    public Integer getDistrict(){
        return district;
    }

    public void setDistrict(Integer district){
        this.district = district;
    }

    public String getAddress(){
        return address;
    }

    public void setAddress(String address){
        this.address = address;
    }

    public String getLegalPerson(){
        return legalPerson;
    }

    public void setLegalPerson(String legalPerson){
        this.legalPerson = legalPerson;
    }

    public String getLegalPersonNo(){
        return legalPersonNo;
    }

    public void setLegalPersonNo(String legalPersonNo){
        this.legalPersonNo = legalPersonNo;
    }

    public String getBusinessLicenseNo(){
        return businessLicenseNo;
    }

    public void setBusinessLicenseNo(String businessLicenseNo){
        this.businessLicenseNo = businessLicenseNo;
    }

    public Integer getDataStatus(){
        return dataStatus;
    }

    public void setDataStatus(Integer dataStatus){
        this.dataStatus = dataStatus;
    }

    public String getRemark(){
        return remark;
    }

    public void setRemark(String remark){
        this.remark = remark;
    }

    public Date getCreateTime(){
        return createTime;
    }

    public void setCreateTime(Date createTime){
        this.createTime = createTime;
    }

    public String getCreateUser(){
        return createUser;
    }

    public void setCreateUser(String createUser){
        this.createUser = createUser;
    }

    public Date getUpdateTime(){
        return updateTime;
    }

    public void setUpdateTime(Date updateTime){
        this.updateTime = updateTime;
    }

    public String getUpdateUser(){
        return updateUser;
    }

    public void setUpdateUser(String updateUser){
        this.updateUser = updateUser;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }


    public BigDecimal getRegisteredCapital() {
        return registeredCapital;
    }

    public void setRegisteredCapital(BigDecimal registeredCapital) {
        this.registeredCapital = registeredCapital;
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

    public List<CustomerCompanyNeed> getCustomerCompanyNeedFirstList() {
        return customerCompanyNeedFirstList;
    }

    public void setCustomerCompanyNeedFirstList(List<CustomerCompanyNeed> customerCompanyNeedFirstList) {
        this.customerCompanyNeedFirstList = customerCompanyNeedFirstList;
    }

    public List<CustomerCompanyNeed> getCustomerCompanyNeedLaterList() {
        return customerCompanyNeedLaterList;
    }

    public void setCustomerCompanyNeedLaterList(List<CustomerCompanyNeed> customerCompanyNeedLaterList) {
        this.customerCompanyNeedLaterList = customerCompanyNeedLaterList;
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

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public Integer getIsLegalPersonApple() {
        return isLegalPersonApple;
    }

    public void setIsLegalPersonApple(Integer isLegalPersonApple) {
        this.isLegalPersonApple = isLegalPersonApple;
    }

    public Integer getOfficeNumber() {
        return officeNumber;
    }

    public Double getOperatingArea() {
        return operatingArea;
    }

    public void setOperatingArea(Double operatingArea) {
        this.operatingArea = operatingArea;
    }

    public Integer getUnitInsuredNumber() {
        return unitInsuredNumber;
    }

    public void setUnitInsuredNumber(Integer unitInsuredNumber) {
        this.unitInsuredNumber = unitInsuredNumber;
    }

    public String getAffiliatedEnterprise() {
        return affiliatedEnterprise;
    }

    public void setAffiliatedEnterprise(String affiliatedEnterprise) {
        this.affiliatedEnterprise = affiliatedEnterprise;
    }

    public String getLegalPersonPhone() {
        return legalPersonPhone;
    }

    public void setLegalPersonPhone(String legalPersonPhone) {
        this.legalPersonPhone = legalPersonPhone;
    }

    public Integer getCustomerOrigin() {
        return customerOrigin;
    }

    public void setCustomerOrigin(Integer customerOrigin) {
        this.customerOrigin = customerOrigin;
    }

    public Date getCompanyFoundTime() {
        return companyFoundTime;
    }

    public void setCompanyFoundTime(Date companyFoundTime) {
        this.companyFoundTime = companyFoundTime;
    }

    public Image getBusinessLicensePictureImage() {
        return businessLicensePictureImage;
    }

    public void setBusinessLicensePictureImage(Image businessLicensePictureImage) {
        this.businessLicensePictureImage = businessLicensePictureImage;
    }

    public Image getLegalPersonNoPictureFrontImage() {
        return legalPersonNoPictureFrontImage;
    }

    public void setLegalPersonNoPictureFrontImage(Image legalPersonNoPictureFrontImage) {
        this.legalPersonNoPictureFrontImage = legalPersonNoPictureFrontImage;
    }

    public Image getLegalPersonNoPictureBackImage() {
        return legalPersonNoPictureBackImage;
    }

    public void setLegalPersonNoPictureBackImage(Image legalPersonNoPictureBackImage) {
        this.legalPersonNoPictureBackImage = legalPersonNoPictureBackImage;
    }

    public List<Image> getManagerPlaceRentContractImageList() {
        return managerPlaceRentContractImageList;
    }

    public void setManagerPlaceRentContractImageList(List<Image> managerPlaceRentContractImageList) {
        this.managerPlaceRentContractImageList = managerPlaceRentContractImageList;
    }

    public List<Image> getLegalPersonCreditReportImageList() {
        return legalPersonCreditReportImageList;
    }

    public void setLegalPersonCreditReportImageList(List<Image> legalPersonCreditReportImageList) {
        this.legalPersonCreditReportImageList = legalPersonCreditReportImageList;
    }

    public List<Image> getFixedAssetsProveImageList() {
        return fixedAssetsProveImageList;
    }

    public void setFixedAssetsProveImageList(List<Image> fixedAssetsProveImageList) {
        this.fixedAssetsProveImageList = fixedAssetsProveImageList;
    }

    public List<Image> getPublicAccountFlowBillImageList() {
        return publicAccountFlowBillImageList;
    }

    public void setPublicAccountFlowBillImageList(List<Image> publicAccountFlowBillImageList) {
        this.publicAccountFlowBillImageList = publicAccountFlowBillImageList;
    }

    public List<Image> getSocialSecurityRoProvidentFundImageList() {
        return socialSecurityRoProvidentFundImageList;
    }

    public void setSocialSecurityRoProvidentFundImageList(List<Image> socialSecurityRoProvidentFundImageList) {
        this.socialSecurityRoProvidentFundImageList = socialSecurityRoProvidentFundImageList;
    }

    public List<Image> getCooperationAgreementImageList() {
        return cooperationAgreementImageList;
    }

    public void setCooperationAgreementImageList(List<Image> cooperationAgreementImageList) {
        this.cooperationAgreementImageList = cooperationAgreementImageList;
    }

    public List<Image> getLegalPersonEducationImageList() {
        return legalPersonEducationImageList;
    }

    public void setLegalPersonEducationImageList(List<Image> legalPersonEducationImageList) {
        this.legalPersonEducationImageList = legalPersonEducationImageList;
    }

    public List<Image> getLegalPersonPositionalTitleImageList() {
        return legalPersonPositionalTitleImageList;
    }

    public void setLegalPersonPositionalTitleImageList(List<Image> legalPersonPositionalTitleImageList) {
        this.legalPersonPositionalTitleImageList = legalPersonPositionalTitleImageList;
    }

    public List<Image> getLocaleChecklistsImageList() {
        return localeChecklistsImageList;
    }

    public void setLocaleChecklistsImageList(List<Image> localeChecklistsImageList) {
        this.localeChecklistsImageList = localeChecklistsImageList;
    }

    public List<Image> getOtherDateImageList() {
        return otherDateImageList;
    }

    public void setOtherDateImageList(List<Image> otherDateImageList) {
        this.otherDateImageList = otherDateImageList;
    }

}
