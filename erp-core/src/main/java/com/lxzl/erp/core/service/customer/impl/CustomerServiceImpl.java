package com.lxzl.erp.core.service.customer.impl;

import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.CustomerType;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.constant.CustomerCompanyImgType;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.customer.CustomerCompanyQueryParam;
import com.lxzl.erp.common.domain.customer.CustomerConsignInfoQueryParam;
import com.lxzl.erp.common.domain.customer.CustomerPersonQueryParam;
import com.lxzl.erp.common.domain.customer.pojo.Customer;
import com.lxzl.erp.common.domain.customer.pojo.CustomerConsignInfo;
import com.lxzl.erp.common.domain.customer.pojo.CustomerRiskManagement;
import com.lxzl.erp.common.domain.img.pojo.Img;
import com.lxzl.erp.common.domain.payment.account.pojo.CustomerAccount;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.common.util.GenerateNoUtil;
import com.lxzl.erp.core.component.ConverterUtil;
import com.lxzl.erp.core.service.customer.CustomerService;
import com.lxzl.erp.core.service.customer.impl.support.CustomerRiskManagementConverter;
import com.lxzl.erp.core.service.payment.PaymentService;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.dataaccess.dao.mysql.customer.*;
import com.lxzl.erp.dataaccess.dao.mysql.img.ImgMapper;
import com.lxzl.erp.dataaccess.domain.customer.*;
import com.lxzl.erp.dataaccess.domain.img.ImgDO;
import com.lxzl.se.dataaccess.mysql.config.PageQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
public class CustomerServiceImpl implements CustomerService {
    private static final Logger logger = LoggerFactory.getLogger(ConverterUtil.class);
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private CustomerPersonMapper customerPersonMapper;
    @Autowired
    private CustomerCompanyMapper customerCompanyMapper;
    @Autowired
    private UserSupport userSupport;
    @Autowired
    private CustomerRiskManagementMapper customerRiskManagementMapper;

    @Autowired
    private CustomerConsignInfoMapper customerConsignInfoMapper;

    @Autowired
    private ImgMapper imgMapper;

    @Autowired
    private PaymentService paymentService;

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, String> addCompany(Customer customer) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        Date now = new Date();
        CustomerDO customerDO = new CustomerDO();
        customerDO.setCustomerNo(GenerateNoUtil.generateCustomerCompanyNo(now));
        customerDO.setCustomerType(CustomerType.CUSTOMER_TYPE_COMPANY);
        if (customer.getIsDisabled() == null) {
            customerDO.setIsDisabled(CommonConstant.COMMON_CONSTANT_YES);
        } else {
            customerDO.setIsDisabled(customer.getIsDisabled());
        }
        customerDO.setCustomerName(customer.getCustomerCompany().getCompanyName());
        customerDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        customerDO.setCreateTime(now);
        customerDO.setUpdateTime(now);
        customerDO.setCreateUser(userSupport.getCurrentUserId().toString());
        customerDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        customerMapper.save(customerDO);

        CustomerCompanyDO customerCompanyDO = CustomerConverter.convertCustomerCompany(customer.getCustomerCompany());
        customerCompanyDO.setCustomerId(customerDO.getId());
        customerCompanyDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        customerCompanyDO.setCreateTime(now);
        customerCompanyDO.setUpdateTime(now);
        customerCompanyDO.setCreateUser(userSupport.getCurrentUserId().toString());
        customerCompanyDO.setUpdateUser(userSupport.getCurrentUserId().toString());

        //营业执照照片操作
        ImgDO businessLicensePictureImgDO = imgMapper.findById(customer.getCustomerCompany().getBusinessLicensePictureImg().getImgId());
        if (businessLicensePictureImgDO == null){
            serviceResult.setErrorCode(ErrorCode.BUSINESS_LICENSE_PICTURE_IMG_NOT_EXISTS);
            return serviceResult;
        }
        if (!CustomerCompanyImgType.BUSINESS_LICENSE_PICTURE_IMG_TYPE.equals(businessLicensePictureImgDO.getImgType())) {
            serviceResult.setErrorCode(ErrorCode.BUSINESS_LICENSE_PICTURE_IMG_TYPE_IS_ERROR);
            return serviceResult;
        }
        //todo refId的长度或者类型是否需要更改
        businessLicensePictureImgDO.setRefId(customerCompanyDO.getId().toString());
        imgMapper.update(businessLicensePictureImgDO);

        //法人/股东身份证正面照片操作
        ImgDO legalPersonNoPictureFrontImgDO = imgMapper.findById(customer.getCustomerCompany().getLegalPersonNoPictureFrontImg().getImgId());
        if (legalPersonNoPictureFrontImgDO == null){
            serviceResult.setErrorCode(ErrorCode.LEGAL_PERSON_NO_PICTURE_FRONT_IMG_NOT_EXISTS);
            return serviceResult;
        }
        if (!CustomerCompanyImgType.LEGAL_PERSON_NO_PICTURE_FRONT_IMG_TYPE.equals(legalPersonNoPictureFrontImgDO.getImgType())) {
            serviceResult.setErrorCode(ErrorCode.LEGAL_PERSON_NO_PICTURE_FRONT_IMG_TYPE_IS_ERROR);
            return serviceResult;
        }
        legalPersonNoPictureFrontImgDO.setRefId(customerCompanyDO.getId().toString());
        imgMapper.update(legalPersonNoPictureFrontImgDO);

        //法人/股东身份证反面照片操作
        ImgDO legalPersonNoPictureBackImgDO = imgMapper.findById(customer.getCustomerCompany().getLegalPersonNoPictureBackImg().getImgId());
        if (legalPersonNoPictureBackImgDO == null){
            serviceResult.setErrorCode(ErrorCode.LEGAL_PERSON_NO_PICTURE_BACK_IMG_NOT_EXISTS);
            return serviceResult;
        }
        if (!CustomerCompanyImgType.LEGAL_PERSON_NO_PICTURE_BACK_IMG_TYPE.equals(legalPersonNoPictureBackImgDO.getImgType())) {
            serviceResult.setErrorCode(ErrorCode.LEGAL_PERSON_NO_PICTURE_BACK_IMG_TYPE_IS_ERROR);
            return serviceResult;
        }
        legalPersonNoPictureBackImgDO.setRefId(customerCompanyDO.getId().toString());
        imgMapper.update(legalPersonNoPictureBackImgDO);

        //经营场所租赁合同照片操作
        if (CollectionUtil.isNotEmpty(customer.getCustomerCompany().getManagerPlaceRentContractImgList())){
            for (Img managerPlaceRentContractImg:customer.getCustomerCompany().getManagerPlaceRentContractImgList()){
                ImgDO managerPlaceRentContractImgDO = imgMapper.findById(managerPlaceRentContractImg.getImgId());
                if (managerPlaceRentContractImgDO == null){
                    serviceResult.setErrorCode(ErrorCode.MANAGER_PLACE_RENT_CONTRACT_IMG_NOT_EXISTS);
                    return serviceResult;
                }
                if (!CustomerCompanyImgType.MANAGER_PLACE_RENT_CONTRACT_IMG_TYPE.equals(managerPlaceRentContractImgDO.getImgType())) {
                    serviceResult.setErrorCode(ErrorCode.MANAGER_PLACE_RENT_CONTRACT_IMG_TYPE_IS_ERROR);
                    return serviceResult;
                }
                managerPlaceRentContractImgDO.setRefId(customerCompanyDO.getId().toString());
                imgMapper.update(managerPlaceRentContractImgDO);
            }
        }

        //法人个人征信报告或附（法人个人征信授权书）照片操作
        if (CollectionUtil.isNotEmpty(customer.getCustomerCompany().getLegalPersonCreditReportImgList())){
            for (Img legalPersonCreditReportImg:customer.getCustomerCompany().getLegalPersonCreditReportImgList()){
                ImgDO legalPersonCreditReportImgDO = imgMapper.findById(legalPersonCreditReportImg.getImgId());
                if (legalPersonCreditReportImgDO == null){
                    serviceResult.setErrorCode(ErrorCode.LEGAL_PERSON_CREDIT_REPORT_IMG_NOT_EXISTS);
                    return serviceResult;
                }
                if (!CustomerCompanyImgType.LEGAL_PERSON_CREDIT_REPORT_IMG_TYPE.equals(legalPersonCreditReportImgDO.getImgType())) {
                    serviceResult.setErrorCode(ErrorCode.LEGAL_PERSON_CREDIT_REPORT_IMG_TYPE_IS_ERROR);
                    return serviceResult;
                }
                legalPersonCreditReportImgDO.setRefId(customerCompanyDO.getId().toString());
                imgMapper.update(legalPersonCreditReportImgDO);
            }
        }

        //固定资产证明照片操作
        if (CollectionUtil.isNotEmpty(customer.getCustomerCompany().getFixedAssetsProveImgList())){
            for (Img fixedAssetsProveImg:customer.getCustomerCompany().getFixedAssetsProveImgList()){
                ImgDO fixedAssetsProveImgDO = imgMapper.findById(fixedAssetsProveImg.getImgId());
                if (fixedAssetsProveImgDO == null){
                    serviceResult.setErrorCode(ErrorCode.FIXED_ASSETS_PROVE_IMG_NOT_EXISTS);
                    return serviceResult;
                }
                if (!CustomerCompanyImgType.FIXED_ASSETS_PROVE_IMG_TYPE.equals(fixedAssetsProveImgDO.getImgType())) {
                    serviceResult.setErrorCode(ErrorCode.FIXED_ASSETS_PROVE_IMG_TYPE_IS_ERROR);
                    return serviceResult;
                }
                fixedAssetsProveImgDO.setRefId(customerCompanyDO.getId().toString());
                imgMapper.update(fixedAssetsProveImgDO);
            }
        }

        //单位对公账户流水账单照片操作
        if (CollectionUtil.isNotEmpty(customer.getCustomerCompany().getPublicAccountFlowBillImgList())){
            for (Img publicAccountFlowBillImg:customer.getCustomerCompany().getPublicAccountFlowBillImgList()){
                ImgDO publicAccountFlowBillImgDO = imgMapper.findById(publicAccountFlowBillImg.getImgId());
                if (publicAccountFlowBillImgDO == null){
                    serviceResult.setErrorCode(ErrorCode.PUBLIC_ACCOUNT_FLOW_BILL_IMG_NOT_EXISTS);
                    return serviceResult;
                }
                if (!CustomerCompanyImgType.PUBLIC_ACCOUNT_FLOW_BILL_IMG_TYPE.equals(publicAccountFlowBillImgDO.getImgType())) {
                    serviceResult.setErrorCode(ErrorCode.PUBLIC_ACCOUNT_FLOW_BILL_IMG_TYPE_IS_ERROR);
                    return serviceResult;
                }
                publicAccountFlowBillImgDO.setRefId(customerCompanyDO.getId().toString());
                imgMapper.update(publicAccountFlowBillImgDO);
            }
        }

        //社保/公积金缴纳证明照片操作
        if (CollectionUtil.isNotEmpty(customer.getCustomerCompany().getSocialSecurityRoProvidentFundImgList())){
            for (Img socialSecurityRoProvidentFundImg:customer.getCustomerCompany().getSocialSecurityRoProvidentFundImgList()){
                ImgDO socialSecurityRoProvidentFundImgDO = imgMapper.findById(socialSecurityRoProvidentFundImg.getImgId());
                if (socialSecurityRoProvidentFundImgDO == null){
                    serviceResult.setErrorCode(ErrorCode.SOCIAL_SECURITY_RO_PROVIDENT_FUND_IMG_NOT_EXISTS);
                    return serviceResult;
                }
                if (!CustomerCompanyImgType.SOCIAL_SECURITY_RO_PROVIDENT_FUND_IMG_TYPE.equals(socialSecurityRoProvidentFundImgDO.getImgType())) {
                    serviceResult.setErrorCode(ErrorCode.SOCIAL_SECURITY_RO_PROVIDENT_FUND_IMG_TYPE_IS_ERROR);
                    return serviceResult;
                }
                socialSecurityRoProvidentFundImgDO.setRefId(customerCompanyDO.getId().toString());
                imgMapper.update(socialSecurityRoProvidentFundImgDO);
            }
        }

        //战略协议或合作协议照片操作
        if (CollectionUtil.isNotEmpty(customer.getCustomerCompany().getCooperationAgreementImgList())){
            for (Img cooperationAgreementImg:customer.getCustomerCompany().getCooperationAgreementImgList()){
                ImgDO cooperationAgreementImgDO = imgMapper.findById(cooperationAgreementImg.getImgId());
                if (cooperationAgreementImgDO == null){
                    serviceResult.setErrorCode(ErrorCode.COOPERATION_AGREEMENT_IMG_NOT_EXISTS);
                    return serviceResult;
                }
                if (!CustomerCompanyImgType.COOPERATION_AGREEMENT_IMG_TYPE.equals(cooperationAgreementImgDO.getImgType())) {
                    serviceResult.setErrorCode(ErrorCode.COOPERATION_AGREEMENT_IMG_TYPE_IS_ERROR);
                    return serviceResult;
                }
                cooperationAgreementImgDO.setRefId(customerCompanyDO.getId().toString());
                imgMapper.update(cooperationAgreementImgDO);
            }
        }

        //法人学历证明照片操作
        if (CollectionUtil.isNotEmpty(customer.getCustomerCompany().getLegalPersonEducationImgList())){
            for (Img legalPersonEducationImg:customer.getCustomerCompany().getLegalPersonEducationImgList()){
                ImgDO legalPersonEducationImgDO = imgMapper.findById(legalPersonEducationImg.getImgId());
                if (legalPersonEducationImgDO == null){
                    serviceResult.setErrorCode(ErrorCode.LEGAL_PERSON_EDUCATION_IMG_NOT_EXISTS);
                    return serviceResult;
                }
                if (!CustomerCompanyImgType.LEGAL_PERSON_EDUCATION_IMG_TYPE.equals(legalPersonEducationImgDO.getImgType())) {
                    serviceResult.setErrorCode(ErrorCode.LEGAL_PERSON_EDUCATION_IMG_TYPE_IS_ERROR);
                    return serviceResult;
                }
                legalPersonEducationImgDO.setRefId(customerCompanyDO.getId().toString());
                imgMapper.update(legalPersonEducationImgDO);
            }
        }

        //法人职称证明照片操作
        if (CollectionUtil.isNotEmpty(customer.getCustomerCompany().getLegalPersonPositionalTitleImgList())){
            for (Img legalPersonPositionalTitleImg:customer.getCustomerCompany().getLegalPersonPositionalTitleImgList()){
                ImgDO legalPersonPositionalTitleImgDO = imgMapper.findById(legalPersonPositionalTitleImg.getImgId());
                if (legalPersonPositionalTitleImgDO == null){
                    serviceResult.setErrorCode(ErrorCode.LEGAL_PERSON_POSITIONAL_TITLE_IMG_NOT_EXISTS);
                    return serviceResult;
                }
                if (!CustomerCompanyImgType.LEGAL_PERSON_POSITIONAL_TITLE_IMG_TYPE.equals(legalPersonPositionalTitleImgDO.getImgType())) {
                    serviceResult.setErrorCode(ErrorCode.LEGAL_PERSON_POSITIONAL_TITLE_IMG_TYPE_IS_ERROR);
                    return serviceResult;
                }
                legalPersonPositionalTitleImgDO.setRefId(customerCompanyDO.getId().toString());
                imgMapper.update(legalPersonPositionalTitleImgDO);
            }
        }

        //现场核查表照片操作
        if (CollectionUtil.isNotEmpty(customer.getCustomerCompany().getLocaleChecklistsImgList())){
            for (Img localeChecklistsImg:customer.getCustomerCompany().getLocaleChecklistsImgList()){
                ImgDO localeChecklistsImgDO = imgMapper.findById(localeChecklistsImg.getImgId());
                if (localeChecklistsImgDO == null){
                    serviceResult.setErrorCode(ErrorCode.LOCALE_CHECKLISTS_IMG_NOT_EXISTS);
                    return serviceResult;
                }
                if (!CustomerCompanyImgType.LOCALE_CHECKLISTS_IMG_TYPE.equals(localeChecklistsImgDO.getImgType())) {
                    serviceResult.setErrorCode(ErrorCode.LOCALE_CHECKLISTS_IMG_TYPE_IS_ERROR);
                    return serviceResult;
                }
                localeChecklistsImgDO.setRefId(customerCompanyDO.getId().toString());
                imgMapper.update(localeChecklistsImgDO);
            }
        }

        //其他材料照片操作
        if (CollectionUtil.isNotEmpty(customer.getCustomerCompany().getOtherDateImgList())){
            for (Img otherDateImg:customer.getCustomerCompany().getOtherDateImgList()){
                ImgDO otherDateImgDO = imgMapper.findById(otherDateImg.getImgId());
                if (otherDateImgDO == null){
                    serviceResult.setErrorCode(ErrorCode.OTHER_DATE_IMG_NOT_EXISTS);
                    return serviceResult;
                }
                if (!CustomerCompanyImgType.OTHER_DATE_IMG_TYPE.equals(otherDateImgDO.getImgType())) {
                    serviceResult.setErrorCode(ErrorCode.OTHER_DATE_IMG_TYPE_IS_ERROR);
                    return serviceResult;
                }
                otherDateImgDO.setRefId(customerCompanyDO.getId().toString());
                imgMapper.update(otherDateImgDO);
            }
        }

        customerCompanyMapper.save(customerCompanyDO);

        CustomerConsignInfo customerConsignInfo = new CustomerConsignInfo();
        customerConsignInfo.setCustomerNo(customerDO.getCustomerNo());
        customerConsignInfo.setConsigneeName(customerCompanyDO.getConnectRealName());
        customerConsignInfo.setConsigneePhone(customerCompanyDO.getConnectPhone());
        customerConsignInfo.setAddress(customerCompanyDO.getConsignAddress());
        //调用新增企业地址信息方法
        addCustomerConsignInfo(customerConsignInfo);

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(customer.getCustomerNo());
        return serviceResult;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, String> addPerson(Customer customer) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        Date now = new Date();
        CustomerDO customerDO = new CustomerDO();
        customerDO.setCustomerNo(GenerateNoUtil.generateCustomerPersonNo(now));
        customerDO.setCustomerType(CustomerType.CUSTOMER_TYPE_PERSON);
        if (customer.getIsDisabled() == null) {
            customerDO.setIsDisabled(CommonConstant.COMMON_CONSTANT_YES);
        } else {
            customerDO.setIsDisabled(customer.getIsDisabled());
        }
        customerDO.setCustomerName(customer.getCustomerPerson().getRealName());
        customerDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        customerDO.setCreateTime(now);
        customerDO.setUpdateTime(now);
        customerDO.setCreateUser(userSupport.getCurrentUserId().toString());
        customerDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        customerMapper.save(customerDO);

        CustomerPersonDO customerPersonDO = CustomerConverter.convertCustomerPerson(customer.getCustomerPerson());
        customerPersonDO.setCustomerId(customerDO.getId());
        customerPersonDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        customerPersonDO.setCreateTime(now);
        customerPersonDO.setUpdateTime(now);
        customerPersonDO.setCreateUser(userSupport.getCurrentUserId().toString());
        customerPersonDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        customerPersonMapper.save(customerPersonDO);

        CustomerConsignInfo customerConsignInfo = new CustomerConsignInfo();
        customerConsignInfo.setCustomerNo(customerDO.getCustomerNo());
        customerConsignInfo.setConsigneeName(customerPersonDO.getRealName());
        customerConsignInfo.setConsigneePhone(customerPersonDO.getPhone());
        customerConsignInfo.setAddress(customerPersonDO.getConsignAddress());
        //调用新增个人用户地址信息方法
        addCustomerConsignInfo(customerConsignInfo);

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(customer.getCustomerNo());
        return serviceResult;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, String> updateCompany(Customer customer) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        Date now = new Date();
        CustomerDO customerDO = customerMapper.findCustomerCompanyByNo(customer.getCustomerNo());
        if (customerDO == null || !CustomerType.CUSTOMER_TYPE_COMPANY.equals(customerDO.getCustomerType())) {
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_NOT_EXISTS);
            return serviceResult;
        }
        CustomerCompanyDO customerCompanyDO = customerCompanyMapper.findByCustomerId(customerDO.getId());
        CustomerCompanyDO newCustomerCompanyDO = CustomerConverter.convertCustomerCompany(customer.getCustomerCompany());
        newCustomerCompanyDO.setDataStatus(null);
        newCustomerCompanyDO.setCreateTime(null);
        newCustomerCompanyDO.setCreateUser(null);
        newCustomerCompanyDO.setUpdateTime(now);
        newCustomerCompanyDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        newCustomerCompanyDO.setId(customerCompanyDO.getId());
        customerCompanyMapper.update(newCustomerCompanyDO);

        customerDO.setCustomerName(newCustomerCompanyDO.getCompanyName());
        customerDO.setUpdateTime(now);
        customerDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        customerDO.setIsDisabled(customer.getIsDisabled());
        customerDO.setRemark(customer.getRemark());
        customerMapper.update(customerDO);

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(customer.getCustomerNo());
        return serviceResult;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, String> updatePerson(Customer customer) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        Date now = new Date();
        CustomerDO customerDO = customerMapper.findCustomerPersonByNo(customer.getCustomerNo());
        if (customerDO == null || !CustomerType.CUSTOMER_TYPE_PERSON.equals(customerDO.getCustomerType())) {
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_NOT_EXISTS);
            return serviceResult;
        }
        CustomerPersonDO customerPersonDO = customerPersonMapper.findByCustomerId(customerDO.getId());
        CustomerPersonDO newCustomerPersonDO = CustomerConverter.convertCustomerPerson(customer.getCustomerPerson());
        newCustomerPersonDO.setDataStatus(null);
        newCustomerPersonDO.setCreateTime(null);
        newCustomerPersonDO.setCreateUser(null);
        newCustomerPersonDO.setUpdateTime(now);
        newCustomerPersonDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        newCustomerPersonDO.setId(customerPersonDO.getId());
        customerPersonMapper.update(newCustomerPersonDO);

        customerDO.setCustomerName(newCustomerPersonDO.getRealName());
        customerDO.setUpdateTime(now);
        customerDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        customerDO.setIsDisabled(customer.getIsDisabled());
        customerDO.setRemark(customer.getRemark());
        customerMapper.update(customerDO);

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(customer.getCustomerNo());
        return serviceResult;
    }

    @Override
    public ServiceResult<String, Page<Customer>> pageCustomerCompany(CustomerCompanyQueryParam customerCompanyQueryParam) {
        ServiceResult<String, Page<Customer>> result = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(customerCompanyQueryParam.getPageNo(), customerCompanyQueryParam.getPageSize());
        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        maps.put("queryParam", customerCompanyQueryParam);

        Integer totalCount = customerMapper.findCustomerCompanyCountByParams(maps);
        List<CustomerDO> customerDOList = customerMapper.findCustomerCompanyByParams(maps);
        List<Customer> customerList = CustomerConverter.convertCustomerDOList(customerDOList);
        Page<Customer> page = new Page<>(customerList, totalCount, customerCompanyQueryParam.getPageNo(), customerCompanyQueryParam.getPageSize());

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(page);
        return result;
    }

    @Override
    public ServiceResult<String, Page<Customer>> pageCustomerPerson(CustomerPersonQueryParam customerPersonQueryParam) {
        ServiceResult<String, Page<Customer>> result = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(customerPersonQueryParam.getPageNo(), customerPersonQueryParam.getPageSize());
        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        maps.put("queryParam", customerPersonQueryParam);

        Integer totalCount = customerMapper.findCustomerPersonCountByParams(maps);
        List<CustomerDO> customerDOList = customerMapper.findCustomerPersonByParams(maps);
        List<Customer> customerPersonList = CustomerConverter.convertCustomerDOList(customerDOList);
        Page<Customer> page = new Page<>(customerPersonList, totalCount, customerPersonQueryParam.getPageNo(), customerPersonQueryParam.getPageSize());

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(page);
        return result;
    }

    @Override
    public ServiceResult<String, Customer> detailCustomerCompany(Customer customer) {
        ServiceResult<String, Customer> serviceResult = new ServiceResult<>();
        CustomerDO customerDO = customerMapper.findCustomerCompanyByNo(customer.getCustomerNo());
        if (customerDO == null || !CustomerType.CUSTOMER_TYPE_COMPANY.equals(customerDO.getCustomerType())) {
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_NOT_EXISTS);
            return serviceResult;
        }
        CustomerAccount customerAccount = paymentService.queryCustomerAccount(customerDO.getCustomerNo());
        Customer customerResult = CustomerConverter.convertCustomerDO(customerDO);
        customerResult.setCustomerAccount(customerAccount);

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(customerResult);
        return serviceResult;
    }

    @Override
    public ServiceResult<String, Customer> detailCustomerPerson(Customer customer) {
        ServiceResult<String, Customer> serviceResult = new ServiceResult<>();
        CustomerDO customerDO = customerMapper.findCustomerPersonByNo(customer.getCustomerNo());
        if (customerDO == null || !CustomerType.CUSTOMER_TYPE_PERSON.equals(customerDO.getCustomerType())) {
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_NOT_EXISTS);
            return serviceResult;
        }
        CustomerAccount customerAccount = paymentService.queryCustomerAccount(customerDO.getCustomerNo());
        Customer customerResult = CustomerConverter.convertCustomerDO(customerDO);
        customerResult.setCustomerAccount(customerAccount);

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(customerResult);
        return serviceResult;
    }

    @Override
    public ServiceResult<String, String> updateRisk(CustomerRiskManagement customerRiskManagement) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        CustomerDO customerDO = customerMapper.findByNo(customerRiskManagement.getCustomerNo());
        if (customerDO == null) {
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_NOT_EXISTS);
            return serviceResult;
        }
        Date now = new Date();
        if (customerDO.getCustomerRiskManagementDO() == null) {//没有风控信息则添加

            CustomerRiskManagementDO customerRiskManagementDO = CustomerRiskManagementConverter.convertCustomerRiskManagement(customerRiskManagement);
            customerRiskManagementDO.setCreditAmountUsed(BigDecimal.ZERO);
            customerRiskManagementDO.setCustomerId(customerDO.getId());
            customerRiskManagementDO.setRemark(customerRiskManagement.getRemark());
            customerRiskManagementDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
            customerRiskManagementDO.setCreateTime(now);
            customerRiskManagementDO.setUpdateTime(now);
            customerRiskManagementDO.setCreateUser(userSupport.getCurrentUserId().toString());
            customerRiskManagementDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            customerRiskManagementMapper.save(customerRiskManagementDO);
            serviceResult.setErrorCode(ErrorCode.SUCCESS);
            serviceResult.setResult(customerDO.getCustomerNo());
            return serviceResult;
        } else {//有风控信息则修改
            CustomerRiskManagementDO customerRiskManagementDOForUpdate = new CustomerRiskManagementDO();
            customerRiskManagementDOForUpdate.setId(customerDO.getCustomerRiskManagementDO().getId());
            customerRiskManagementDOForUpdate.setRemark(customerRiskManagement.getRemark());
            customerRiskManagementDOForUpdate.setUpdateTime(now);
            customerRiskManagementDOForUpdate.setUpdateUser(userSupport.getCurrentUserId().toString());
            customerRiskManagementDOForUpdate.setCreditAmount(customerRiskManagement.getCreditAmount());
            customerRiskManagementDOForUpdate.setDepositCycle(customerRiskManagement.getDepositCycle());
            customerRiskManagementDOForUpdate.setPaymentCycle(customerRiskManagement.getPaymentCycle());
            customerRiskManagementMapper.update(customerRiskManagementDOForUpdate);
            serviceResult.setErrorCode(ErrorCode.SUCCESS);
            serviceResult.setResult(customerDO.getCustomerNo());
        }

        return serviceResult;
    }

    /**
     * 增加客户收货地址信息
     *
     * @param customerConsignInfo
     * @return
     */
    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, Integer> addCustomerConsignInfo(CustomerConsignInfo customerConsignInfo) {

        ServiceResult<String, Integer> serviceResult = new ServiceResult<>();
        Date now = new Date();

        //通过CustomerNo来获取客户ID
        CustomerDO customerDO = customerMapper.findByNo(customerConsignInfo.getCustomerNo());
        if (customerDO == null) {
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_NOT_NULL);
            return serviceResult;
        }

        //获取地址信息的内容，存入客户地址信息表
        CustomerConsignInfoDO customerConsignInfoDO = ConverterUtil.convert(customerConsignInfo, CustomerConsignInfoDO.class);
        customerConsignInfoDO.setCustomerId(customerDO.getId());
        customerConsignInfoDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        customerConsignInfoDO.setCreateTime(now);
        customerConsignInfoDO.setUpdateTime(now);
        customerConsignInfoDO.setCreateUser(userSupport.getCurrentUserId().toString());
        customerConsignInfoDO.setUpdateUser(userSupport.getCurrentUserId().toString());

        //新增收货默认地址如果设置为默认，那么该客户其他收货地址设置为非默认地址
        //如果该条地址信息为该客户的唯一一条地址，那么就设置为默认地址
        //判断该条信息是否为该客户的唯一信息
        Integer customerConsignInfoCount = customerConsignInfoMapper.countByCustomerId(customerDO.getId());
        //如果新增地址信息是该客户的唯一收货信息，那么该地址信息为默认地址
        if (customerConsignInfoCount == 0) {
            customerConsignInfoDO.setIsMain(CommonConstant.COMMON_CONSTANT_YES);
        }
        //客户有多个地址时，如果新增地址设置为默认地址，那么其他地址就不能为默认地址
        if (CommonConstant.COMMON_CONSTANT_YES.equals(customerConsignInfoDO.getIsMain())) {
            //修改该客户下所有的默认地址的状态为0
            customerConsignInfoMapper.clearIsMainByCustomerId(customerDO.getId());
        }

        customerConsignInfoMapper.save(customerConsignInfoDO);
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(customerConsignInfoDO.getId());
        return serviceResult;

    }


    /**
     * 修改客户收货地址信息
     *
     * @param customerConsignInfo
     * @return
     */
    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, Integer> updateCustomerConsignInfo(CustomerConsignInfo customerConsignInfo) {
        ServiceResult<String, Integer> serviceResult = new ServiceResult<>();
        Date now = new Date();
        CustomerConsignInfoDO customerConsignInfoDO = customerConsignInfoMapper.findById(customerConsignInfo.getCustomerConsignInfoId());
        if (customerConsignInfoDO == null) {
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_CONSIGN_INFO_NOT_EXISTS);
            return serviceResult;
        }

        //如果传送过来的ismain是1
        if (CommonConstant.COMMON_CONSTANT_YES.equals(customerConsignInfo.getIsMain())) {
            customerConsignInfoMapper.clearIsMainByCustomerId(customerConsignInfoDO.getCustomerId());
        }

        customerConsignInfoDO.setConsigneeName(customerConsignInfo.getConsigneeName());
        customerConsignInfoDO.setConsigneePhone(customerConsignInfo.getConsigneePhone());
        customerConsignInfoDO.setProvince(customerConsignInfo.getProvince());
        customerConsignInfoDO.setCity(customerConsignInfo.getCity());
        customerConsignInfoDO.setDistrict(customerConsignInfo.getDistrict());
        customerConsignInfoDO.setAddress(customerConsignInfo.getAddress());
        customerConsignInfoDO.setIsMain(customerConsignInfo.getIsMain());
        customerConsignInfoDO.setRemark(customerConsignInfo.getRemark());
        customerConsignInfoDO.setDataStatus(customerConsignInfo.getDataStatus());
        customerConsignInfoDO.setUpdateTime(now);
        customerConsignInfoDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        customerConsignInfoMapper.update(customerConsignInfoDO);
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(customerConsignInfoDO.getId());
        return serviceResult;
    }

    /**
     * 删除客户收货信息
     *
     * @param customerConsignInfo
     * @return
     */
    @Override
    public ServiceResult<String, Integer> deleteCustomerConsignInfo(CustomerConsignInfo customerConsignInfo) {
        ServiceResult<String, Integer> serviceResult = new ServiceResult<>();
        Date now = new Date();
        CustomerConsignInfoDO customerConsignInfoDO = customerConsignInfoMapper.findById(customerConsignInfo.getCustomerConsignInfoId());
        if (customerConsignInfoDO == null) {
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_CONSIGN_INFO_NOT_EXISTS);
            return serviceResult;
        }
        customerConsignInfoDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
        customerConsignInfoDO.setUpdateTime(now);
        customerConsignInfoDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        customerConsignInfoMapper.update(customerConsignInfoDO);

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }

    /**
     * 单个信息详情
     *
     * @param customerConsignInfo
     * @return
     */
    @Override
    public ServiceResult<String, CustomerConsignInfo> detailCustomerConsignInfo(CustomerConsignInfo customerConsignInfo) {
        ServiceResult<String, CustomerConsignInfo> serviceResult = new ServiceResult<>();


        CustomerConsignInfoDO customerConsignInfoDO = customerConsignInfoMapper.findById(customerConsignInfo.getCustomerConsignInfoId());
        if (customerConsignInfoDO == null) {
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_CONSIGN_INFO_NOT_EXISTS);
            return serviceResult;
        }

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(ConverterUtil.convert(customerConsignInfoDO, CustomerConsignInfo.class));
        return serviceResult;
    }


    @Override
    public ServiceResult<String, Page<CustomerConsignInfo>> pageCustomerConsignInfo(CustomerConsignInfoQueryParam customerConsignInfoQueryParam) {
        ServiceResult<String, Page<CustomerConsignInfo>> serviceResult = new ServiceResult<>();

        CustomerDO customerDO = customerMapper.findByNo(customerConsignInfoQueryParam.getCustomerNo());
        if (customerDO == null) {
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_NOT_NULL);
            return serviceResult;
        }
        //获取该用户ID下所有的地址信息
        customerConsignInfoQueryParam.setCustomerId(customerDO.getId());

        PageQuery pageQuery = new PageQuery(customerConsignInfoQueryParam.getPageNo(), customerConsignInfoQueryParam.getPageSize());
        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        maps.put("queryParam", customerConsignInfoQueryParam);

        Integer totalCount = customerConsignInfoMapper.findCustomerConsignInfoCountByParams(maps);
        List<CustomerConsignInfoDO> customerConsignInfoDOList = customerConsignInfoMapper.findCustomerConsignInfoByParams(maps);
        List<CustomerConsignInfo> customerConsignInfoList = ConverterUtil.convertList(customerConsignInfoDOList, CustomerConsignInfo.class);
        Page<CustomerConsignInfo> page = new Page<>(customerConsignInfoList, totalCount, customerConsignInfoQueryParam.getPageNo(), customerConsignInfoQueryParam.getPageSize());

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(page);
        return serviceResult;
    }

    @Override
    public ServiceResult<String, Integer> updateAddressIsMain(CustomerConsignInfo customerConsignInfo) {
        ServiceResult<String, Integer> serviceResult = new ServiceResult<>();
        Date now = new Date();

        CustomerConsignInfoDO customerConsignInfoDO = customerConsignInfoMapper.findById(customerConsignInfo.getCustomerConsignInfoId());
        if (customerConsignInfoDO == null) {
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_CONSIGN_INFO_NOT_EXISTS);
            return serviceResult;
        }

        //如果传送过来的ismain是1
        if (CommonConstant.COMMON_CONSTANT_YES.equals(customerConsignInfo.getIsMain())) {
            customerConsignInfoMapper.clearIsMainByCustomerId(customerConsignInfoDO.getCustomerId());
        }

        customerConsignInfoDO.setIsMain(customerConsignInfo.getIsMain());
        customerConsignInfoDO.setUpdateTime(now);
        customerConsignInfoDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        customerConsignInfoMapper.update(customerConsignInfoDO);
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(customerConsignInfoDO.getId());
        return serviceResult;
    }

    @Override
    public void updateLastUseTime(Integer customerConsignInfoId) {
        try {
            CustomerConsignInfoDO customerConsignInfoDO = customerConsignInfoMapper.findById(customerConsignInfoId);
            customerConsignInfoDO.setLastUseTime(new Date());
            customerConsignInfoMapper.update(customerConsignInfoDO);
        } catch (Exception e) {
            logger.error("保存客户地址信息最后使用时间错误");
        } catch (Throwable t) {
            logger.error("保存客户地址信息最后使用时间错误");
        }
    }
}
