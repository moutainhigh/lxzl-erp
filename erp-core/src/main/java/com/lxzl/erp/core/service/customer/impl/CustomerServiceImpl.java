package com.lxzl.erp.core.service.customer.impl;

import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.CustomerType;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.constant.ImgType;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.customer.CustomerCompanyQueryParam;
import com.lxzl.erp.common.domain.customer.CustomerConsignInfoQueryParam;
import com.lxzl.erp.common.domain.customer.CustomerPersonQueryParam;
import com.lxzl.erp.common.domain.customer.pojo.Customer;
import com.lxzl.erp.common.domain.customer.pojo.CustomerConsignInfo;
import com.lxzl.erp.common.domain.customer.pojo.CustomerRiskManagement;
import com.lxzl.erp.common.domain.payment.account.pojo.CustomerAccount;
import com.lxzl.erp.common.domain.system.pojo.Image;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.common.util.ConverterUtil;
import com.lxzl.erp.common.util.GenerateNoUtil;
import com.lxzl.erp.core.service.customer.CustomerService;
import com.lxzl.erp.core.service.customer.impl.support.CustomerRiskManagementConverter;
import com.lxzl.erp.core.service.payment.PaymentService;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.dataaccess.dao.mysql.customer.*;
import com.lxzl.erp.dataaccess.dao.mysql.img.ImageMapper;
import com.lxzl.erp.dataaccess.domain.customer.*;
import com.lxzl.erp.dataaccess.domain.system.ImageDO;
import com.lxzl.se.dataaccess.mysql.config.PageQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

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
    private PaymentService paymentService;

    @Autowired
    private ImageMapper imageMapper;

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

        CustomerCompanyDO customerCompanyDO = ConverterUtil.convert(customer.getCustomerCompany(), CustomerCompanyDO.class);
        customerCompanyDO.setCustomerId(customerDO.getId());
        customerCompanyDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        customerCompanyDO.setCreateTime(now);
        customerCompanyDO.setUpdateTime(now);
        customerCompanyDO.setCreateUser(userSupport.getCurrentUserId().toString());
        customerCompanyDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        customerCompanyMapper.save(customerCompanyDO);

        //对营业执照图片操作
        ImageDO businessLicensePictureImageDO = imageMapper.findById(customer.getCustomerCompany().getBusinessLicensePictureImage().getImgId());
        if (businessLicensePictureImageDO == null){
            serviceResult.setErrorCode(ErrorCode.BUSINESS_LICENSE_PICTURE_IMAGE_NOT_EXISTS);
            return serviceResult;
        }
        if(!ImgType.BUSINESS_LICENSE_PICTURE_IMG_TYPE.equals(businessLicensePictureImageDO.getImgType())){
            serviceResult.setErrorCode(ErrorCode.BUSINESS_LICENSE_PICTURE_IMAGE_TYPE_IS_ERROR);
            return serviceResult;
        }
        businessLicensePictureImageDO.setRefId(customerCompanyDO.getId().toString());
        businessLicensePictureImageDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        businessLicensePictureImageDO.setUpdateTime(now);
        imageMapper.update(businessLicensePictureImageDO);

        //对身份证正面图片操作
        ImageDO legalPersonNoPictureFrontImageDO = imageMapper.findById(customer.getCustomerCompany().getLegalPersonNoPictureFrontImage().getImgId());
        if (legalPersonNoPictureFrontImageDO == null){
            serviceResult.setErrorCode(ErrorCode.LEGAL_PERSON_NO_PICTURE_FRONT_IMAGE_NOT_EXISTS);
            return serviceResult;
        }
        if(!ImgType.LEGAL_PERSON_NO_PICTURE_FRONT_IMG_TYPE.equals(legalPersonNoPictureFrontImageDO.getImgType())){
            serviceResult.setErrorCode(ErrorCode.LEGAL_PERSON_NO_PICTURE_FRONT_IMAGE_TYPE_IS_ERROR);
            return serviceResult;
        }
        legalPersonNoPictureFrontImageDO.setRefId(customerCompanyDO.getId().toString());
        legalPersonNoPictureFrontImageDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        legalPersonNoPictureFrontImageDO.setUpdateTime(now);
        imageMapper.update(legalPersonNoPictureFrontImageDO);

        //对身份证反面图片操作
        ImageDO legalPersonNoPictureBackimageDO = imageMapper.findById(customer.getCustomerCompany().getLegalPersonNoPictureBackImage().getImgId());
        if (legalPersonNoPictureBackimageDO == null){
            serviceResult.setErrorCode(ErrorCode.LEGAL_PERSON_NO_PICTURE_BACK_IMAGE_NOT_EXISTS);
            return serviceResult;
        }
        if(!ImgType.LEGAL_PERSON_NO_PICTURE_BACK_IMG_TYPE.equals(legalPersonNoPictureBackimageDO.getImgType())){
            serviceResult.setErrorCode(ErrorCode.LEGAL_PERSON_NO_PICTURE_BACK_IMAGE_TYPE_IS_ERROR);
            return serviceResult;
        }
        legalPersonNoPictureBackimageDO.setRefId(customerCompanyDO.getId().toString());
        legalPersonNoPictureBackimageDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        legalPersonNoPictureBackimageDO.setUpdateTime(now);
        imageMapper.update(legalPersonNoPictureBackimageDO);

        //对经营场所租赁合同图片操作
        if (CollectionUtil.isNotEmpty(customer.getCustomerCompany().getManagerPlaceRentContractImageList())){
            for (Image managerPlaceRentContractImage : customer.getCustomerCompany().getManagerPlaceRentContractImageList()){
                ImageDO managerPlaceRentContractimageDO = imageMapper.findById(managerPlaceRentContractImage.getImgId());
                if (managerPlaceRentContractimageDO == null){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    serviceResult.setErrorCode(ErrorCode.MANAGER_PLACE_RENT_CONTRACT_IMAGE_NOT_EXISTS);
                    return serviceResult;
                }
                if(!ImgType.MANAGER_PLACE_RENT_CONTRACT_IMG_TYPE.equals(managerPlaceRentContractimageDO.getImgType())){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    serviceResult.setErrorCode(ErrorCode.MANAGER_PLACE_RENT_CONTRACT_IMAGE_TYPE_IS_ERROR);
                    return serviceResult;
                }
                managerPlaceRentContractimageDO.setRefId(customerCompanyDO.getId().toString());
                managerPlaceRentContractimageDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                managerPlaceRentContractimageDO.setUpdateTime(now);
                imageMapper.update(managerPlaceRentContractimageDO);
            }
        }

        //对//法人个人征信报告或附（法人个人征信授权书）图片操作
        if (CollectionUtil.isNotEmpty(customer.getCustomerCompany().getLegalPersonCreditReportImageList())){
            for (Image legalPersonCreditReportimage : customer.getCustomerCompany().getLegalPersonCreditReportImageList()){
                ImageDO legalPersonCreditReportimageDO = imageMapper.findById(legalPersonCreditReportimage.getImgId());
                if (legalPersonCreditReportimageDO == null){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    serviceResult.setErrorCode(ErrorCode.LEGAL_PERSON_CREDIT_REPORT_IMAGE_NOT_EXISTS);
                    return serviceResult;
                }
                if(!ImgType.LEGAL_PERSON_CREDIT_REPORT_IMG_TYPE.equals(legalPersonCreditReportimageDO.getImgType())){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    serviceResult.setErrorCode(ErrorCode.LEGAL_PERSON_CREDIT_REPORT_IMAGE_TYPE_IS_ERROR);
                    return serviceResult;
                }
                legalPersonCreditReportimageDO.setRefId(customerCompanyDO.getId().toString());
                legalPersonCreditReportimageDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                legalPersonCreditReportimageDO.setUpdateTime(now);
                imageMapper.update(legalPersonCreditReportimageDO);
            }
        }

        //对固定资产证明图片操作
        if (CollectionUtil.isNotEmpty(customer.getCustomerCompany().getFixedAssetsProveImageList())){
            for (Image fixedAssetsProveimage : customer.getCustomerCompany().getFixedAssetsProveImageList()){
                ImageDO fixedAssetsProveimageDO = imageMapper.findById(fixedAssetsProveimage.getImgId());
                if (fixedAssetsProveimageDO == null){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    serviceResult.setErrorCode(ErrorCode.FIXED_ASSETS_PROVE_IMAGE_NOT_EXISTS);
                    return serviceResult;
                }
                if(!ImgType.FIXED_ASSETS_PROVE_IMG_TYPE.equals(fixedAssetsProveimageDO.getImgType())){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    serviceResult.setErrorCode(ErrorCode.FIXED_ASSETS_PROVE_IMAGE_TYPE_IS_ERROR);
                    return serviceResult;
                }
                fixedAssetsProveimageDO.setRefId(customerCompanyDO.getId().toString());
                fixedAssetsProveimageDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                fixedAssetsProveimageDO.setUpdateTime(now);
                imageMapper.update(fixedAssetsProveimageDO);
            }
        }

        //对单位对公账户流水账单图片操作
        if (CollectionUtil.isNotEmpty(customer.getCustomerCompany().getPublicAccountFlowBillImageList())){
            for (Image publicAccountFlowBillimage : customer.getCustomerCompany().getPublicAccountFlowBillImageList()){
                ImageDO publicAccountFlowBillimageDO = imageMapper.findById(publicAccountFlowBillimage.getImgId());
                if (publicAccountFlowBillimageDO == null){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    serviceResult.setErrorCode(ErrorCode.PUBLIC_ACCOUNT_FLOW_BILL_IMAGE_NOT_EXISTS);
                    return serviceResult;
                }
                if(!ImgType.PUBLIC_ACCOUNT_FLOW_BILL_IMG_TYPE.equals(publicAccountFlowBillimageDO.getImgType())){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    serviceResult.setErrorCode(ErrorCode.PUBLIC_ACCOUNT_FLOW_BILL_IMAGE_TYPE_IS_ERROR);
                    return serviceResult;
                }
                publicAccountFlowBillimageDO.setRefId(customerCompanyDO.getId().toString());
                publicAccountFlowBillimageDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                publicAccountFlowBillimageDO.setUpdateTime(now);
                imageMapper.update(publicAccountFlowBillimageDO);
            }
        }

        //对社保/公积金缴纳证明图片操作
        if (CollectionUtil.isNotEmpty(customer.getCustomerCompany().getSocialSecurityRoProvidentFundImageList())){
            for (Image socialSecurityRoProvidentFundimage : customer.getCustomerCompany().getSocialSecurityRoProvidentFundImageList()){
                ImageDO socialSecurityRoProvidentFundimageDO = imageMapper.findById(socialSecurityRoProvidentFundimage.getImgId());
                if (socialSecurityRoProvidentFundimageDO == null){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    serviceResult.setErrorCode(ErrorCode.SOCIAL_SECURITY_RO_PROVIDENT_FUND_IMAGE_NOT_EXISTS);
                    return serviceResult;
                }
                if(!ImgType.SOCIAL_SECURITY_RO_PROVIDENT_FUND_IMG_TYPE.equals(socialSecurityRoProvidentFundimageDO.getImgType())){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    serviceResult.setErrorCode(ErrorCode.SOCIAL_SECURITY_RO_PROVIDENT_FUND_IMAGE_TYPE_IS_ERROR);
                    return serviceResult;
                }
                socialSecurityRoProvidentFundimageDO.setRefId(customerCompanyDO.getId().toString());
                socialSecurityRoProvidentFundimageDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                socialSecurityRoProvidentFundimageDO.setUpdateTime(now);
                imageMapper.update(socialSecurityRoProvidentFundimageDO);
            }
        }

        //对战略协议或合作协议图片操作
        if (CollectionUtil.isNotEmpty(customer.getCustomerCompany().getCooperationAgreementImageList())){
            for (Image cooperationAgreementimage : customer.getCustomerCompany().getCooperationAgreementImageList()){
                ImageDO cooperationAgreementimageDO = imageMapper.findById(cooperationAgreementimage.getImgId());
                if (cooperationAgreementimageDO == null){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    serviceResult.setErrorCode(ErrorCode.COOPERATION_AGREEMENT_IMAGE_NOT_EXISTS);
                    return serviceResult;
                }
                if(!ImgType.COOPERATION_AGREEMENT_IMG_TYPE.equals(cooperationAgreementimageDO.getImgType())){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    serviceResult.setErrorCode(ErrorCode.COOPERATION_AGREEMENT_IMAGE_TYPE_IS_ERROR);
                    return serviceResult;
                }
                cooperationAgreementimageDO.setRefId(customerCompanyDO.getId().toString());
                cooperationAgreementimageDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                cooperationAgreementimageDO.setUpdateTime(now);
                imageMapper.update(cooperationAgreementimageDO);
            }
        }

        //对法人学历证明图片操作
        if (CollectionUtil.isNotEmpty(customer.getCustomerCompany().getLegalPersonEducationImageList())){
            for (Image legalPersonEducationimage : customer.getCustomerCompany().getLegalPersonEducationImageList ()){
                ImageDO legalPersonEducationimageDO = imageMapper.findById(legalPersonEducationimage.getImgId());
                if (legalPersonEducationimageDO == null){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    serviceResult.setErrorCode(ErrorCode.LEGAL_PERSON_EDUCATION_IMAGE_NOT_EXISTS);
                    return serviceResult;
                }
                if(!ImgType.LEGAL_PERSON_EDUCATION_IMG_TYPE.equals(legalPersonEducationimageDO.getImgType())){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    serviceResult.setErrorCode(ErrorCode.LEGAL_PERSON_EDUCATION_IMAGE_TYPE_IS_ERROR);
                    return serviceResult;
                }
                legalPersonEducationimageDO.setRefId(customerCompanyDO.getId().toString());
                legalPersonEducationimageDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                legalPersonEducationimageDO.setUpdateTime(now);
                imageMapper.update(legalPersonEducationimageDO);
            }
        }

        //对法人职称证明图片操作
        if (CollectionUtil.isNotEmpty(customer.getCustomerCompany().getLegalPersonPositionalTitleImageList())){
            for (Image legalPersonPositionalTitleimage : customer.getCustomerCompany().getLegalPersonPositionalTitleImageList ()){
                ImageDO legalPersonPositionalTitleimageDO = imageMapper.findById(legalPersonPositionalTitleimage.getImgId());
                if (legalPersonPositionalTitleimageDO == null){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    serviceResult.setErrorCode(ErrorCode.LEGAL_PERSON_POSITIONAL_TITLE_IMAGE_NOT_EXISTS);
                    return serviceResult;
                }
                if(!ImgType.LEGAL_PERSON_POSITIONAL_TITLE_IMG_TYPE.equals(legalPersonPositionalTitleimageDO.getImgType())){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    serviceResult.setErrorCode(ErrorCode.LEGAL_PERSON_POSITIONAL_TITLE_IMAGE_TYPE_IS_ERROR);
                    return serviceResult;
                }
                legalPersonPositionalTitleimageDO.setRefId(customerCompanyDO.getId().toString());
                legalPersonPositionalTitleimageDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                legalPersonPositionalTitleimageDO.setUpdateTime(now);
                imageMapper.update(legalPersonPositionalTitleimageDO);
            }
        }

        //对现场核查表图片操作
        if (CollectionUtil.isNotEmpty(customer.getCustomerCompany().getLocaleChecklistsImageList())){
            for (Image localeChecklistsimage : customer.getCustomerCompany().getLocaleChecklistsImageList ()){
                ImageDO localeChecklistsimageDO = imageMapper.findById(localeChecklistsimage.getImgId());
                if (localeChecklistsimageDO == null){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    serviceResult.setErrorCode(ErrorCode.LOCALE_CHECKLISTS_IMAGE_NOT_EXISTS);
                    return serviceResult;
                }
                if(!ImgType.LOCALE_CHECKLISTS_IMG_TYPE.equals(localeChecklistsimageDO.getImgType())){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    serviceResult.setErrorCode(ErrorCode.LOCALE_CHECKLISTS_IMAGE_TYPE_IS_ERROR);
                    return serviceResult;
                }
                localeChecklistsimageDO.setRefId(customerCompanyDO.getId().toString());
                localeChecklistsimageDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                localeChecklistsimageDO.setUpdateTime(now);
                imageMapper.update(localeChecklistsimageDO);
            }
        }

        //对其他材料图片操作
        if (CollectionUtil.isNotEmpty(customer.getCustomerCompany().getOtherDateImageList())){
            for (Image otherDateImage : customer.getCustomerCompany().getLocaleChecklistsImageList ()){
                ImageDO otherDateImageDO = imageMapper.findById(otherDateImage.getImgId());
                if (otherDateImageDO == null){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    serviceResult.setErrorCode(ErrorCode.OTHER_DATE_IMAGE_NOT_EXISTS);
                    return serviceResult;
                }
                if(!ImgType.OTHER_DATE_IMG_TYPE.equals(otherDateImageDO.getImgType())){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    serviceResult.setErrorCode(ErrorCode.OTHER_DATE_IMAGE_TYPE_IS_ERROR);
                    return serviceResult;
                }
                otherDateImageDO.setRefId(customerCompanyDO.getId().toString());
                otherDateImageDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                otherDateImageDO.setUpdateTime(now);
                imageMapper.update(otherDateImageDO);
            }
        }

        CustomerConsignInfo customerConsignInfo = new CustomerConsignInfo();
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

        CustomerPersonDO customerPersonDO = ConverterUtil.convert(customer.getCustomerPerson(), CustomerPersonDO.class);
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
        CustomerCompanyDO newCustomerCompanyDO = ConverterUtil.convert(customer.getCustomerCompany(), CustomerCompanyDO.class);

        //todo 对附件的处理
        if (!customerCompanyDO.getId().equals(customer.getCustomerCompany().getLegalPersonNoPictureFrontImage().getRefId())){

        }





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
        CustomerPersonDO newCustomerPersonDO = ConverterUtil.convert(customer.getCustomerPerson(), CustomerPersonDO.class);
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
        List<Customer> customerList = ConverterUtil.convertList(customerDOList, Customer.class);
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
        List<Customer> customerPersonList = ConverterUtil.convertList(customerDOList, Customer.class);
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
        Customer customerResult = ConverterUtil.convert(customerDO, Customer.class);
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
        Customer customerResult = ConverterUtil.convert(customerDO, Customer.class);
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
