package com.lxzl.erp.core.service.customer.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
import com.lxzl.erp.common.domain.customer.pojo.CustomerCompanyNeed;
import com.lxzl.erp.common.domain.customer.pojo.CustomerConsignInfo;
import com.lxzl.erp.common.domain.customer.pojo.CustomerRiskManagement;
import com.lxzl.erp.common.domain.payment.account.pojo.CustomerAccount;
import com.lxzl.erp.common.domain.system.pojo.Image;
import com.lxzl.erp.common.util.BigDecimalUtil;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.common.util.ConverterUtil;
import com.lxzl.erp.common.util.ListUtil;
import com.lxzl.erp.core.service.basic.impl.support.GenerateNoSupport;
import com.lxzl.erp.core.service.customer.CustomerService;
import com.lxzl.erp.core.service.customer.impl.support.CustomerRiskManagementConverter;
import com.lxzl.erp.core.service.payment.PaymentService;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.dataaccess.dao.mysql.customer.*;
import com.lxzl.erp.dataaccess.dao.mysql.system.ImgMysqlMapper;
import com.lxzl.erp.dataaccess.domain.customer.*;
import com.lxzl.erp.dataaccess.domain.system.ImageDO;
import com.lxzl.se.common.util.StringUtil;
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
    private GenerateNoSupport generateNoSupport;

    @Autowired
    private ImgMysqlMapper imgMysqlMapper;

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, String> addCompany(Customer customer) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        Date now = new Date();
        CustomerDO customerDO = new CustomerDO();
        customerDO.setCustomerNo(generateNoSupport.generateCustomerNo(now,CustomerType.CUSTOMER_TYPE_COMPANY));
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
        //设置首次所需设备
        List<CustomerCompanyNeed> customerCompanyNeedFirstList = customer.getCustomerCompany().getCustomerCompanyNeedFirstList();
        for (CustomerCompanyNeed customerCompanyNeed : customerCompanyNeedFirstList){
            BigDecimal totalPrice = BigDecimalUtil.mul(customerCompanyNeed.getUnitPrice(),new BigDecimal(customerCompanyNeed.getRentCount()));
            customerCompanyNeed.setTotalPrice(totalPrice);
        }
        customerCompanyDO.setCustomerCompanyNeedFirstJson(JSON.toJSON(customerCompanyNeedFirstList).toString());

        //判断后续所需设备
        if (CollectionUtil.isNotEmpty(customer.getCustomerCompany().getCustomerCompanyNeedLaterList())){
            List<CustomerCompanyNeed> customerCompanyNeedLaterList = customer.getCustomerCompany().getCustomerCompanyNeedFirstList();
            for (CustomerCompanyNeed customerCompanyNeed : customerCompanyNeedLaterList){
                BigDecimal totalPrice = BigDecimalUtil.mul(customerCompanyNeed.getUnitPrice(),new BigDecimal(customerCompanyNeed.getRentCount()));
                customerCompanyNeed.setTotalPrice(totalPrice);
            }
            customerCompanyDO.setCustomerCompanyNeedLaterJson(JSON.toJSON(customerCompanyNeedLaterList).toString());
        }

        customerCompanyDO.setCustomerId(customerDO.getId());
        customerCompanyDO.setCustomerNo(customerDO.getCustomerNo());
        customerCompanyDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        customerCompanyDO.setCreateTime(now);
        customerCompanyDO.setUpdateTime(now);
        customerCompanyDO.setCreateUser(userSupport.getCurrentUserId().toString());
        customerCompanyDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        customerCompanyMapper.save(customerCompanyDO);

        //对营业执照图片操作
        ImageDO businessLicensePictureImageDO = imgMysqlMapper.findById(customer.getCustomerCompany().getBusinessLicensePictureImage().getImgId());
        if (businessLicensePictureImageDO == null){
            serviceResult.setErrorCode(ErrorCode.BUSINESS_LICENSE_PICTURE_IMAGE_NOT_EXISTS);
            return serviceResult;
        }
        businessLicensePictureImageDO.setImgType(ImgType.BUSINESS_LICENSE_PICTURE_IMG_TYPE);
        businessLicensePictureImageDO.setRefId(customerCompanyDO.getId().toString());
        businessLicensePictureImageDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        businessLicensePictureImageDO.setUpdateTime(now);
        imgMysqlMapper.update(businessLicensePictureImageDO);

        //对身份证正面图片操作
        ImageDO legalPersonNoPictureFrontImageDO = imgMysqlMapper.findById(customer.getCustomerCompany().getLegalPersonNoPictureFrontImage().getImgId());
        if (legalPersonNoPictureFrontImageDO == null){
            serviceResult.setErrorCode(ErrorCode.LEGAL_PERSON_NO_PICTURE_FRONT_IMAGE_NOT_EXISTS);
            return serviceResult;
        }
        legalPersonNoPictureFrontImageDO.setImgType(ImgType.LEGAL_PERSON_NO_PICTURE_FRONT_IMG_TYPE);
        legalPersonNoPictureFrontImageDO.setRefId(customerCompanyDO.getId().toString());
        legalPersonNoPictureFrontImageDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        legalPersonNoPictureFrontImageDO.setUpdateTime(now);
        imgMysqlMapper.update(legalPersonNoPictureFrontImageDO);

        //对身份证反面图片操作
        ImageDO legalPersonNoPictureBackImageDO = imgMysqlMapper.findById(customer.getCustomerCompany().getLegalPersonNoPictureBackImage().getImgId());
        if (legalPersonNoPictureBackImageDO == null){
            serviceResult.setErrorCode(ErrorCode.LEGAL_PERSON_NO_PICTURE_BACK_IMAGE_NOT_EXISTS);
            return serviceResult;
        }
        legalPersonNoPictureBackImageDO.setImgType(ImgType.LEGAL_PERSON_NO_PICTURE_BACK_IMG_TYPE);
        legalPersonNoPictureBackImageDO.setRefId(customerCompanyDO.getId().toString());
        legalPersonNoPictureBackImageDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        legalPersonNoPictureBackImageDO.setUpdateTime(now);
        imgMysqlMapper.update(legalPersonNoPictureBackImageDO);

        //对经营场所租赁合同图片操作
        if (CollectionUtil.isNotEmpty(customer.getCustomerCompany().getManagerPlaceRentContractImageList())){
            for (Image managerPlaceRentContractImage : customer.getCustomerCompany().getManagerPlaceRentContractImageList()){
                ImageDO managerPlaceRentContractImageDO = imgMysqlMapper.findById(managerPlaceRentContractImage.getImgId());
                if (managerPlaceRentContractImageDO == null){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    serviceResult.setErrorCode(ErrorCode.MANAGER_PLACE_RENT_CONTRACT_IMAGE_NOT_EXISTS);
                    return serviceResult;
                }
                managerPlaceRentContractImageDO.setImgType(ImgType.MANAGER_PLACE_RENT_CONTRACT_IMG_TYPE);
                managerPlaceRentContractImageDO.setRefId(customerCompanyDO.getId().toString());
                managerPlaceRentContractImageDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                managerPlaceRentContractImageDO.setUpdateTime(now);
                imgMysqlMapper.update(managerPlaceRentContractImageDO);
            }
        }

        //对//法人个人征信报告或附（法人个人征信授权书）图片操作
        if (CollectionUtil.isNotEmpty(customer.getCustomerCompany().getLegalPersonCreditReportImageList())){
            for (Image legalPersonCreditReportImage : customer.getCustomerCompany().getLegalPersonCreditReportImageList()){
                ImageDO legalPersonCreditReportImageDO = imgMysqlMapper.findById(legalPersonCreditReportImage.getImgId());
                if (legalPersonCreditReportImageDO == null){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    serviceResult.setErrorCode(ErrorCode.LEGAL_PERSON_CREDIT_REPORT_IMAGE_NOT_EXISTS);
                    return serviceResult;
                }
                legalPersonCreditReportImageDO.setImgType(ImgType.LEGAL_PERSON_CREDIT_REPORT_IMG_TYPE);
                legalPersonCreditReportImageDO.setRefId(customerCompanyDO.getId().toString());
                legalPersonCreditReportImageDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                legalPersonCreditReportImageDO.setUpdateTime(now);
                imgMysqlMapper.update(legalPersonCreditReportImageDO);
            }
        }

        //对固定资产证明图片操作
        if (CollectionUtil.isNotEmpty(customer.getCustomerCompany().getFixedAssetsProveImageList())){
            for (Image fixedAssetsProveImage : customer.getCustomerCompany().getFixedAssetsProveImageList()){
                ImageDO fixedAssetsProveImageDO = imgMysqlMapper.findById(fixedAssetsProveImage.getImgId());
                if (fixedAssetsProveImageDO == null){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    serviceResult.setErrorCode(ErrorCode.FIXED_ASSETS_PROVE_IMAGE_NOT_EXISTS);
                    return serviceResult;
                }
                fixedAssetsProveImageDO.setImgType(ImgType.FIXED_ASSETS_PROVE_IMG_TYPE);
                fixedAssetsProveImageDO.setRefId(customerCompanyDO.getId().toString());
                fixedAssetsProveImageDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                fixedAssetsProveImageDO.setUpdateTime(now);
                imgMysqlMapper.update(fixedAssetsProveImageDO);
            }
        }

        //对单位对公账户流水账单图片操作
        if (CollectionUtil.isNotEmpty(customer.getCustomerCompany().getPublicAccountFlowBillImageList())){
            for (Image publicAccountFlowBillImage : customer.getCustomerCompany().getPublicAccountFlowBillImageList()){
                ImageDO publicAccountFlowBillImageDO = imgMysqlMapper.findById(publicAccountFlowBillImage.getImgId());
                if (publicAccountFlowBillImageDO == null){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    serviceResult.setErrorCode(ErrorCode.PUBLIC_ACCOUNT_FLOW_BILL_IMAGE_NOT_EXISTS);
                    return serviceResult;
                }
                publicAccountFlowBillImageDO.setImgType(ImgType.PUBLIC_ACCOUNT_FLOW_BILL_IMG_TYPE);
                publicAccountFlowBillImageDO.setRefId(customerCompanyDO.getId().toString());
                publicAccountFlowBillImageDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                publicAccountFlowBillImageDO.setUpdateTime(now);
                imgMysqlMapper.update(publicAccountFlowBillImageDO);
            }
        }

        //对社保/公积金缴纳证明图片操作
        if (CollectionUtil.isNotEmpty(customer.getCustomerCompany().getSocialSecurityRoProvidentFundImageList())){
            for (Image socialSecurityRoProvidentFundImage : customer.getCustomerCompany().getSocialSecurityRoProvidentFundImageList()){
                ImageDO socialSecurityRoProvidentFundImageDO = imgMysqlMapper.findById(socialSecurityRoProvidentFundImage.getImgId());
                if (socialSecurityRoProvidentFundImageDO == null){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    serviceResult.setErrorCode(ErrorCode.SOCIAL_SECURITY_RO_PROVIDENT_FUND_IMAGE_NOT_EXISTS);
                    return serviceResult;
                }
                socialSecurityRoProvidentFundImageDO.setImgType(ImgType.SOCIAL_SECURITY_RO_PROVIDENT_FUND_IMG_TYPE);
                socialSecurityRoProvidentFundImageDO.setRefId(customerCompanyDO.getId().toString());
                socialSecurityRoProvidentFundImageDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                socialSecurityRoProvidentFundImageDO.setUpdateTime(now);
                imgMysqlMapper.update(socialSecurityRoProvidentFundImageDO);
            }
        }

        //对战略协议或合作协议图片操作
        if (CollectionUtil.isNotEmpty(customer.getCustomerCompany().getCooperationAgreementImageList())){
            for (Image cooperationAgreementImage : customer.getCustomerCompany().getCooperationAgreementImageList()){
                ImageDO cooperationAgreementImageDO = imgMysqlMapper.findById(cooperationAgreementImage.getImgId());
                if (cooperationAgreementImageDO == null){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    serviceResult.setErrorCode(ErrorCode.COOPERATION_AGREEMENT_IMAGE_NOT_EXISTS);
                    return serviceResult;
                }
                cooperationAgreementImageDO.setImgType(ImgType.COOPERATION_AGREEMENT_IMG_TYPE);
                cooperationAgreementImageDO.setRefId(customerCompanyDO.getId().toString());
                cooperationAgreementImageDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                cooperationAgreementImageDO.setUpdateTime(now);
                imgMysqlMapper.update(cooperationAgreementImageDO);
            }
        }

        //对法人学历证明图片操作
        if (CollectionUtil.isNotEmpty(customer.getCustomerCompany().getLegalPersonEducationImageList())){
            for (Image legalPersonEducationImage : customer.getCustomerCompany().getLegalPersonEducationImageList ()){
                ImageDO legalPersonEducationImageDO = imgMysqlMapper.findById(legalPersonEducationImage.getImgId());
                if (legalPersonEducationImageDO == null){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    serviceResult.setErrorCode(ErrorCode.LEGAL_PERSON_EDUCATION_IMAGE_NOT_EXISTS);
                    return serviceResult;
                }
                legalPersonEducationImageDO.setImgType(ImgType.LEGAL_PERSON_EDUCATION_IMG_TYPE);
                legalPersonEducationImageDO.setRefId(customerCompanyDO.getId().toString());
                legalPersonEducationImageDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                legalPersonEducationImageDO.setUpdateTime(now);
                imgMysqlMapper.update(legalPersonEducationImageDO);
            }
        }

        //对法人职称证明图片操作
        if (CollectionUtil.isNotEmpty(customer.getCustomerCompany().getLegalPersonPositionalTitleImageList())){
            for (Image legalPersonPositionalTitleImage : customer.getCustomerCompany().getLegalPersonPositionalTitleImageList ()){
                ImageDO legalPersonPositionalTitleImageDO = imgMysqlMapper.findById(legalPersonPositionalTitleImage.getImgId());
                if (legalPersonPositionalTitleImageDO == null){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    serviceResult.setErrorCode(ErrorCode.LEGAL_PERSON_POSITIONAL_TITLE_IMAGE_NOT_EXISTS);
                    return serviceResult;
                }
                legalPersonPositionalTitleImageDO.setImgType(ImgType.LEGAL_PERSON_POSITIONAL_TITLE_IMG_TYPE);
                legalPersonPositionalTitleImageDO.setRefId(customerCompanyDO.getId().toString());
                legalPersonPositionalTitleImageDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                legalPersonPositionalTitleImageDO.setUpdateTime(now);
                imgMysqlMapper.update(legalPersonPositionalTitleImageDO);
            }
        }

        //对现场核查表图片操作
        if (CollectionUtil.isNotEmpty(customer.getCustomerCompany().getLocaleChecklistsImageList())){
            for (Image localeChecklistsImage : customer.getCustomerCompany().getLocaleChecklistsImageList ()){
                ImageDO localeChecklistsImageDO = imgMysqlMapper.findById(localeChecklistsImage.getImgId());
                if (localeChecklistsImageDO == null){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    serviceResult.setErrorCode(ErrorCode.LOCALE_CHECKLISTS_IMAGE_NOT_EXISTS);
                    return serviceResult;
                }
                localeChecklistsImageDO.setImgType(ImgType.LOCALE_CHECKLISTS_IMG_TYPE);
                localeChecklistsImageDO.setRefId(customerCompanyDO.getId().toString());
                localeChecklistsImageDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                localeChecklistsImageDO.setUpdateTime(now);
                imgMysqlMapper.update(localeChecklistsImageDO);
            }
        }

        //对其他材料图片操作
        if (CollectionUtil.isNotEmpty(customer.getCustomerCompany().getOtherDateImageList())){
            for (Image otherDateImage : customer.getCustomerCompany().getLocaleChecklistsImageList ()){
                ImageDO otherDateImageDO = imgMysqlMapper.findById(otherDateImage.getImgId());
                if (otherDateImageDO == null){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    serviceResult.setErrorCode(ErrorCode.OTHER_DATE_IMAGE_NOT_EXISTS);
                    return serviceResult;
                }
                otherDateImageDO.setImgType(ImgType.OTHER_DATE_IMG_TYPE);
                otherDateImageDO.setRefId(customerCompanyDO.getId().toString());
                otherDateImageDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                otherDateImageDO.setUpdateTime(now);
                imgMysqlMapper.update(otherDateImageDO);
            }
        }

        CustomerConsignInfo customerConsignInfo = new CustomerConsignInfo();
        customerConsignInfo.setCustomerNo(customerDO.getCustomerNo());
        customerConsignInfo.setConsigneeName(customerCompanyDO.getConnectRealName());
        customerConsignInfo.setConsigneePhone(customerCompanyDO.getConnectPhone());
        customerConsignInfo.setAddress(customerCompanyDO.getAddress());
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
        customerDO.setCustomerNo(generateNoSupport.generateCustomerNo(now,CustomerType.CUSTOMER_TYPE_PERSON));
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

        CustomerPersonDO customerPersonDO = ConverterUtil.convert(customer.getCustomerPerson(),CustomerPersonDO.class);
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
        customerConsignInfo.setAddress(customerPersonDO.getAddress());
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
        CustomerCompanyDO newCustomerCompanyDO = ConverterUtil.convert(customer.getCustomerCompany(),CustomerCompanyDO.class);

        //判断首次所需设备 list转json
        List<CustomerCompanyNeed> customerCompanyNeedFirstList = customer.getCustomerCompany().getCustomerCompanyNeedFirstList();
        for (CustomerCompanyNeed customerCompanyNeed : customerCompanyNeedFirstList){
            BigDecimal totalPrice = BigDecimalUtil.mul(customerCompanyNeed.getUnitPrice(),new BigDecimal(customerCompanyNeed.getRentCount()));
            customerCompanyNeed.setTotalPrice(totalPrice);
        }
        newCustomerCompanyDO.setCustomerCompanyNeedFirstJson(JSON.toJSON(customerCompanyNeedFirstList).toString());

        //判断后续所需设备
        if(CollectionUtil.isNotEmpty(customer.getCustomerCompany().getCustomerCompanyNeedLaterList())){
            List<CustomerCompanyNeed> customerCompanyNeedLaterList = customer.getCustomerCompany().getCustomerCompanyNeedFirstList();
            for (CustomerCompanyNeed customerCompanyNeed : customerCompanyNeedLaterList){
                BigDecimal totalPrice = BigDecimalUtil.mul(customerCompanyNeed.getUnitPrice(),new BigDecimal(customerCompanyNeed.getRentCount()));
                customerCompanyNeed.setTotalPrice(totalPrice);
            }
            newCustomerCompanyDO.setCustomerCompanyNeedLaterJson(JSON.toJSON(customerCompanyNeedLaterList).toString());
        }

        newCustomerCompanyDO.setDataStatus(null);
        newCustomerCompanyDO.setCreateTime(null);
        newCustomerCompanyDO.setCreateUser(null);
        newCustomerCompanyDO.setUpdateTime(now);
        newCustomerCompanyDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        newCustomerCompanyDO.setId(customerCompanyDO.getId());
        customerCompanyMapper.update(newCustomerCompanyDO);


        //对营业执照图片操作
        if (StringUtil.isEmpty(customer.getCustomerCompany().getBusinessLicensePictureImage().getRefId())){
            ImageDO newBusinessLicensePictureImageDO = imgMysqlMapper.findById(customer.getCustomerCompany().getBusinessLicensePictureImage().getImgId());
            if (newBusinessLicensePictureImageDO == null){
                serviceResult.setErrorCode(ErrorCode.BUSINESS_LICENSE_PICTURE_IMAGE_NOT_EXISTS);
                return serviceResult;
            }

            //先让原来图片状态变为不可用
            List<ImageDO> dbBusinessLicensePictureImageDOList=imgMysqlMapper.findByRefIdAndType(customerCompanyDO.getId().toString(),ImgType.BUSINESS_LICENSE_PICTURE_IMG_TYPE);
            dbBusinessLicensePictureImageDOList.get(0).setDataStatus(CommonConstant.DATA_STATUS_DELETE);
            imgMysqlMapper.update(dbBusinessLicensePictureImageDOList.get(0));

            newBusinessLicensePictureImageDO.setImgType(ImgType.BUSINESS_LICENSE_PICTURE_IMG_TYPE);
            newBusinessLicensePictureImageDO.setRefId(customerCompanyDO.getId().toString());
            newBusinessLicensePictureImageDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            newBusinessLicensePictureImageDO.setUpdateTime(now);
            imgMysqlMapper.update(newBusinessLicensePictureImageDO);
        }

        //对身份证正面图片操作
        if (StringUtil.isEmpty(customer.getCustomerCompany().getLegalPersonNoPictureFrontImage().getRefId())){
            ImageDO newLegalPersonNoPictureFrontImageDO = imgMysqlMapper.findById(customer.getCustomerCompany().getLegalPersonNoPictureFrontImage().getImgId());
            if (newLegalPersonNoPictureFrontImageDO == null){
                serviceResult.setErrorCode(ErrorCode.LEGAL_PERSON_NO_PICTURE_FRONT_IMAGE_NOT_EXISTS);
                return serviceResult;
            }

            //先让原来图片状态变为不可用
            List<ImageDO> dblegalPersonNoPictureFrontImageDOList=imgMysqlMapper.findByRefIdAndType(customerCompanyDO.getId().toString(),ImgType.LEGAL_PERSON_NO_PICTURE_FRONT_IMG_TYPE);
            dblegalPersonNoPictureFrontImageDOList.get(0).setDataStatus(CommonConstant.DATA_STATUS_DELETE);
            imgMysqlMapper.update(dblegalPersonNoPictureFrontImageDOList.get(0));

            newLegalPersonNoPictureFrontImageDO.setImgType(ImgType.LEGAL_PERSON_NO_PICTURE_FRONT_IMG_TYPE);
            newLegalPersonNoPictureFrontImageDO.setRefId(customerCompanyDO.getId().toString());
            newLegalPersonNoPictureFrontImageDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            newLegalPersonNoPictureFrontImageDO.setUpdateTime(now);
            imgMysqlMapper.update(newLegalPersonNoPictureFrontImageDO);
        }

        //对身份证反面图片操作
        if (StringUtil.isEmpty(customer.getCustomerCompany().getLegalPersonNoPictureBackImage().getRefId())){
            ImageDO newLegalPersonNoPictureBackimageDO= imgMysqlMapper.findById(customer.getCustomerCompany().getLegalPersonNoPictureBackImage().getImgId());
            if (newLegalPersonNoPictureBackimageDO == null){
                serviceResult.setErrorCode(ErrorCode.LEGAL_PERSON_NO_PICTURE_BACK_IMAGE_NOT_EXISTS);
                return serviceResult;
            }
            //先让原来图片状态变为不可用
            List<ImageDO> dbLegalPersonNoPictureBackimageDOList =imgMysqlMapper.findByRefIdAndType(customerCompanyDO.getId().toString(),ImgType.LEGAL_PERSON_NO_PICTURE_BACK_IMG_TYPE);
            dbLegalPersonNoPictureBackimageDOList.get(0).setDataStatus(CommonConstant.DATA_STATUS_DELETE);
            imgMysqlMapper.update(dbLegalPersonNoPictureBackimageDOList.get(0));

            //让新的图片成为该企业客户的该类型图片
            newLegalPersonNoPictureBackimageDO.setImgType(ImgType.LEGAL_PERSON_NO_PICTURE_BACK_IMG_TYPE);
            newLegalPersonNoPictureBackimageDO.setRefId(customerCompanyDO.getId().toString());
            newLegalPersonNoPictureBackimageDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            newLegalPersonNoPictureBackimageDO.setUpdateTime(now);
            imgMysqlMapper.update(newLegalPersonNoPictureBackimageDO);
        }

        //对经营场所租赁合同图片操作
        serviceResult = updateImage(customer.getCustomerCompany().getManagerPlaceRentContractImageList(),ImgType.MANAGER_PLACE_RENT_CONTRACT_IMG_TYPE,customerCompanyDO.getId().toString(),userSupport.getCurrentUserId().toString(),now);
        if (!ErrorCode.SUCCESS.equals(serviceResult.getErrorCode())){
            serviceResult.setErrorCode(serviceResult.getErrorCode());
            return serviceResult;
        }

        //对法人个人征信报告或附（法人个人征信授权书）图片操作
        serviceResult = updateImage(customer.getCustomerCompany().getLegalPersonCreditReportImageList(),ImgType.LEGAL_PERSON_CREDIT_REPORT_IMG_TYPE,customerCompanyDO.getId().toString(),userSupport.getCurrentUserId().toString(),now);
        if (!ErrorCode.SUCCESS.equals(serviceResult.getErrorCode())){
            serviceResult.setErrorCode(serviceResult.getErrorCode());
            return serviceResult;
        }

        //对固定资产证明图片操作
        serviceResult = updateImage(customer.getCustomerCompany().getFixedAssetsProveImageList(),ImgType.FIXED_ASSETS_PROVE_IMG_TYPE,customerCompanyDO.getId().toString(),userSupport.getCurrentUserId().toString(),now);
        if (!ErrorCode.SUCCESS.equals(serviceResult.getErrorCode())){
            serviceResult.setErrorCode(serviceResult.getErrorCode());
            return serviceResult;
        }

        //对单位对公账户流水账单图片操作
        serviceResult = updateImage(customer.getCustomerCompany().getPublicAccountFlowBillImageList(),ImgType.PUBLIC_ACCOUNT_FLOW_BILL_IMG_TYPE,customerCompanyDO.getId().toString(),userSupport.getCurrentUserId().toString(),now);
        if (!ErrorCode.SUCCESS.equals(serviceResult.getErrorCode())){
            serviceResult.setErrorCode(serviceResult.getErrorCode());
            return serviceResult;
        }

        //对社保/公积金缴纳证明图片操作
        serviceResult = updateImage(customer.getCustomerCompany().getSocialSecurityRoProvidentFundImageList(),ImgType.SOCIAL_SECURITY_RO_PROVIDENT_FUND_IMG_TYPE,customerCompanyDO.getId().toString(),userSupport.getCurrentUserId().toString(),now);
        if (!ErrorCode.SUCCESS.equals(serviceResult.getErrorCode())){
            serviceResult.setErrorCode(serviceResult.getErrorCode());
            return serviceResult;
        }

        //对战略协议或合作协议图片操作
        serviceResult = updateImage(customer.getCustomerCompany().getCooperationAgreementImageList(),ImgType.COOPERATION_AGREEMENT_IMG_TYPE,customerCompanyDO.getId().toString(),userSupport.getCurrentUserId().toString(),now);
        if (!ErrorCode.SUCCESS.equals(serviceResult.getErrorCode())){
            serviceResult.setErrorCode(serviceResult.getErrorCode());
            return serviceResult;
        }

        //对法人学历证明图片操作
        serviceResult = updateImage(customer.getCustomerCompany().getLegalPersonEducationImageList(),ImgType.LEGAL_PERSON_EDUCATION_IMG_TYPE,customerCompanyDO.getId().toString(),userSupport.getCurrentUserId().toString(),now);
        if (!ErrorCode.SUCCESS.equals(serviceResult.getErrorCode())){
            serviceResult.setErrorCode(serviceResult.getErrorCode());
            return serviceResult;
        }

        //对法人职称证明图片操作.
        serviceResult = updateImage(customer.getCustomerCompany().getLegalPersonPositionalTitleImageList(),ImgType.LEGAL_PERSON_POSITIONAL_TITLE_IMG_TYPE,customerCompanyDO.getId().toString(),userSupport.getCurrentUserId().toString(),now);
        if (!ErrorCode.SUCCESS.equals(serviceResult.getErrorCode())){
            serviceResult.setErrorCode(serviceResult.getErrorCode());
            return serviceResult;
        }

        //对现场核查表图片操作
        serviceResult = updateImage(customer.getCustomerCompany().getLocaleChecklistsImageList(),ImgType.LOCALE_CHECKLISTS_IMG_TYPE,customerCompanyDO.getId().toString(),userSupport.getCurrentUserId().toString(),now);
        if (!ErrorCode.SUCCESS.equals(serviceResult.getErrorCode())){
            serviceResult.setErrorCode(serviceResult.getErrorCode());
            return serviceResult;
        }

        //对其他材料图片操作
        serviceResult = updateImage(customer.getCustomerCompany().getOtherDateImageList(),ImgType.OTHER_DATE_IMG_TYPE,customerCompanyDO.getId().toString(),userSupport.getCurrentUserId().toString(),now);
        if (!ErrorCode.SUCCESS.equals(serviceResult.getErrorCode())){
            serviceResult.setErrorCode(serviceResult.getErrorCode());
            return serviceResult;
        }


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
        CustomerPersonDO newCustomerPersonDO = ConverterUtil.convert(customer.getCustomerPerson(),CustomerPersonDO.class);
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
        List<Customer> customerList = ConverterUtil.convertList(customerDOList,Customer.class);
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

        //首次所需设备，Json转list
        String customerCompanyNeedFirstList = customerDO.getCustomerCompanyDO().getCustomerCompanyNeedFirstJson();
        customerResult.getCustomerCompany().setCustomerCompanyNeedFirstList(JSONObject.parseArray(customerCompanyNeedFirstList,CustomerCompanyNeed.class));

        //后续所需设备，Json转list
        if (StringUtil.isNotEmpty(customerDO.getCustomerCompanyDO().getCustomerCompanyNeedLaterJson())){
            String customerCompanyNeedLaterList = customerDO.getCustomerCompanyDO().getCustomerCompanyNeedLaterJson();
            customerResult.getCustomerCompany().setCustomerCompanyNeedLaterList(JSONObject.parseArray(customerCompanyNeedLaterList,CustomerCompanyNeed.class));
        }

       //开始加入附件
        //加入营业执照
        List<ImageDO> businessLicensePictureImageList = imgMysqlMapper.findByRefIdAndType(customerResult.getCustomerCompany().getCustomerCompanyId().toString(),ImgType.BUSINESS_LICENSE_PICTURE_IMG_TYPE);
        if (CollectionUtil.isNotEmpty(businessLicensePictureImageList)){
            Image businessLicensePictureImage = ConverterUtil.convert(businessLicensePictureImageList.get(0),Image.class);
            customerResult.getCustomerCompany().setBusinessLicensePictureImage(businessLicensePictureImage);
        }

        //法人/股东身份证正面
        List<ImageDO> legalPersonNoPictureFrontImageList = imgMysqlMapper.findByRefIdAndType(customerResult.getCustomerCompany().getCustomerCompanyId().toString(),ImgType.LEGAL_PERSON_NO_PICTURE_FRONT_IMG_TYPE);
        if (CollectionUtil.isNotEmpty(legalPersonNoPictureFrontImageList)){
            Image legalPersonNoPictureFrontImage = ConverterUtil.convert(legalPersonNoPictureFrontImageList.get(0),Image.class);
            customerResult.getCustomerCompany().setLegalPersonNoPictureFrontImage(legalPersonNoPictureFrontImage);
        }

        //法人/股东身份证反
        List<ImageDO> legalPersonNoPictureBackImageList = imgMysqlMapper.findByRefIdAndType(customerResult.getCustomerCompany().getCustomerCompanyId().toString(),ImgType.LEGAL_PERSON_NO_PICTURE_BACK_IMG_TYPE);
        if (CollectionUtil.isNotEmpty(legalPersonNoPictureBackImageList)) {
            Image legalPersonNoPictureBackImage = ConverterUtil.convert(legalPersonNoPictureBackImageList.get(0),Image.class);
            customerResult.getCustomerCompany().setLegalPersonNoPictureBackImage(legalPersonNoPictureBackImage);
        }

        //经营场所租赁合同
        List<ImageDO> managerPlaceRentContractImageDOList = imgMysqlMapper.findByRefIdAndType(customerResult.getCustomerCompany().getCustomerCompanyId().toString(),ImgType.MANAGER_PLACE_RENT_CONTRACT_IMG_TYPE);
        if (CollectionUtil.isNotEmpty(managerPlaceRentContractImageDOList)){
            List<Image> managerPlaceRentContractImageList = ConverterUtil.convertList(managerPlaceRentContractImageDOList,Image.class);
            customerResult.getCustomerCompany().setManagerPlaceRentContractImageList(managerPlaceRentContractImageList);
        }

        //法人个人征信报告或附（法人个人征信授权书）
        List<ImageDO> legalPersonCreditReportImageDOList = imgMysqlMapper.findByRefIdAndType(customerResult.getCustomerCompany().getCustomerCompanyId().toString(),ImgType.LEGAL_PERSON_CREDIT_REPORT_IMG_TYPE);
        if (CollectionUtil.isNotEmpty(legalPersonCreditReportImageDOList)){
            List<Image> legalPersonCreditReportImageList = ConverterUtil.convertList(legalPersonCreditReportImageDOList,Image.class);
            customerResult.getCustomerCompany().setManagerPlaceRentContractImageList(legalPersonCreditReportImageList);
        }

        //固定资产证明
        List<ImageDO> fixedAssetsProveImageDOList = imgMysqlMapper.findByRefIdAndType(customerResult.getCustomerCompany().getCustomerCompanyId().toString(),ImgType.FIXED_ASSETS_PROVE_IMG_TYPE);
        if (CollectionUtil.isNotEmpty(fixedAssetsProveImageDOList)){
            List<Image> fixedAssetsProveImageList = ConverterUtil.convertList(fixedAssetsProveImageDOList,Image.class);
            customerResult.getCustomerCompany().setManagerPlaceRentContractImageList(fixedAssetsProveImageList);
        }

        //单位对公账户流水账单
        List<ImageDO> publicAccountFlowBillImageDOList = imgMysqlMapper.findByRefIdAndType(customerResult.getCustomerCompany().getCustomerCompanyId().toString(),ImgType.PUBLIC_ACCOUNT_FLOW_BILL_IMG_TYPE);
        if (CollectionUtil.isNotEmpty(publicAccountFlowBillImageDOList)){
            List<Image> publicAccountFlowBillImageList = ConverterUtil.convertList(publicAccountFlowBillImageDOList,Image.class);
            customerResult.getCustomerCompany().setManagerPlaceRentContractImageList(publicAccountFlowBillImageList);
        }

        //社保/公积金缴纳证明
        List<ImageDO> socialSecurityRoProvidentFundImageListDOList = imgMysqlMapper.findByRefIdAndType(customerResult.getCustomerCompany().getCustomerCompanyId().toString(),ImgType.SOCIAL_SECURITY_RO_PROVIDENT_FUND_IMG_TYPE);
        if (CollectionUtil.isNotEmpty(socialSecurityRoProvidentFundImageListDOList)){
            List<Image> socialSecurityRoProvidentFundImageList = ConverterUtil.convertList(socialSecurityRoProvidentFundImageListDOList,Image.class);
            customerResult.getCustomerCompany().setManagerPlaceRentContractImageList(socialSecurityRoProvidentFundImageList);
        }
        //战略协议或合作协议
        List<ImageDO> cooperationAgreementImageListDOList = imgMysqlMapper.findByRefIdAndType(customerResult.getCustomerCompany().getCustomerCompanyId().toString(),ImgType.COOPERATION_AGREEMENT_IMG_TYPE);
        if (CollectionUtil.isNotEmpty(cooperationAgreementImageListDOList)){
            List<Image> cooperationAgreementImageList = ConverterUtil.convertList(cooperationAgreementImageListDOList,Image.class);
            customerResult.getCustomerCompany().setManagerPlaceRentContractImageList(cooperationAgreementImageList);
        }

        //法人学历证明
        List<ImageDO> legalPersonEducationImageListDOList = imgMysqlMapper.findByRefIdAndType(customerResult.getCustomerCompany().getCustomerCompanyId().toString(),ImgType.LEGAL_PERSON_EDUCATION_IMG_TYPE);
        if (CollectionUtil.isNotEmpty(legalPersonEducationImageListDOList)){
            List<Image> legalPersonEducationImageList = ConverterUtil.convertList(legalPersonEducationImageListDOList,Image.class);
            customerResult.getCustomerCompany().setManagerPlaceRentContractImageList(legalPersonEducationImageList);
        }

        //法人职称证明
        List<ImageDO> legalPersonPositionalTitleImageListDOList = imgMysqlMapper.findByRefIdAndType(customerResult.getCustomerCompany().getCustomerCompanyId().toString(),ImgType.LEGAL_PERSON_POSITIONAL_TITLE_IMG_TYPE);
        if (CollectionUtil.isNotEmpty(legalPersonPositionalTitleImageListDOList)){
            List<Image> legalPersonPositionalTitleImageList = ConverterUtil.convertList(legalPersonPositionalTitleImageListDOList,Image.class);
            customerResult.getCustomerCompany().setManagerPlaceRentContractImageList(legalPersonPositionalTitleImageList);
        }

        //现场核查表
        List<ImageDO> localeChecklistsImageListDOList = imgMysqlMapper.findByRefIdAndType(customerResult.getCustomerCompany().getCustomerCompanyId().toString(),ImgType.LOCALE_CHECKLISTS_IMG_TYPE);
        if (CollectionUtil.isNotEmpty(localeChecklistsImageListDOList)){
            List<Image> localeChecklistsImageList = ConverterUtil.convertList(localeChecklistsImageListDOList,Image.class);
            customerResult.getCustomerCompany().setManagerPlaceRentContractImageList(localeChecklistsImageList);
        }

        //其他材料
        List<ImageDO> otherDateImageListDOList = imgMysqlMapper.findByRefIdAndType(customerResult.getCustomerCompany().getCustomerCompanyId().toString(),ImgType.OTHER_DATE_IMG_TYPE);
        if (CollectionUtil.isNotEmpty(otherDateImageListDOList)){
            List<Image> otherDateImageList = ConverterUtil.convertList(otherDateImageListDOList,Image.class);
            customerResult.getCustomerCompany().setManagerPlaceRentContractImageList(otherDateImageList);
        }

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
            CustomerRiskManagementDO customerRiskManagementDOForUpdate = CustomerRiskManagementConverter.convertCustomerRiskManagement(customerRiskManagement);
            customerRiskManagementDOForUpdate.setId(customerDO.getCustomerRiskManagementDO().getId());
            customerRiskManagementDOForUpdate.setRemark(customerRiskManagement.getRemark());
            customerRiskManagementDOForUpdate.setUpdateTime(now);
            customerRiskManagementDOForUpdate.setUpdateUser(userSupport.getCurrentUserId().toString());
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

    private ServiceResult<String,String> updateImage(List<Image> ImageList, Integer imgType, String refId,String user,Date now){
        ServiceResult<String,String> serviceResult = new ServiceResult<>();
        List<Image> updateImgList = new ArrayList<>();
        List<ImageDO> dbImgRecord = imgMysqlMapper.findByRefIdAndType(refId, imgType);
        Map<Integer, ImageDO> dbImgRecordMap = ListUtil.listToMap(dbImgRecord, "id");

        if (CollectionUtil.isNotEmpty(ImageList)){
            for (Image image : ImageList){
                if (image != null){
                    if (dbImgRecordMap.get(image.getImgId()) != null ){
                        dbImgRecordMap.remove(image.getImgId());
                    }else{
                        updateImgList.add(image);
                    }
                }
            }
        }

        if(CollectionUtil.isNotEmpty(updateImgList)){
            //对经营场所租赁合同图片操作
            if (ImgType.MANAGER_PLACE_RENT_CONTRACT_IMG_TYPE.equals(imgType)){
                for (Image managerPlaceRentContractImage : updateImgList){
                    ImageDO managerPlaceRentContractImageDO = imgMysqlMapper.findById(managerPlaceRentContractImage.getImgId());
                    if (managerPlaceRentContractImageDO == null){
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                        serviceResult.setErrorCode(ErrorCode.MANAGER_PLACE_RENT_CONTRACT_IMAGE_NOT_EXISTS);
                        return serviceResult;
                    }
                    managerPlaceRentContractImageDO.setImgType(ImgType.MANAGER_PLACE_RENT_CONTRACT_IMG_TYPE);
                    managerPlaceRentContractImageDO.setRefId(refId);
                    managerPlaceRentContractImageDO.setUpdateUser(user);
                    managerPlaceRentContractImageDO.setUpdateTime(now);
                    imgMysqlMapper.update(managerPlaceRentContractImageDO);
                }
            }

            //对//法人个人征信报告或附（法人个人征信授权书）图片操作
            if (ImgType.LEGAL_PERSON_CREDIT_REPORT_IMG_TYPE.equals(imgType)) {
                    for (Image legalPersonCreditReportImage : updateImgList){
                        ImageDO legalPersonCreditReportImageDO = imgMysqlMapper.findById(legalPersonCreditReportImage.getImgId());
                        if (legalPersonCreditReportImageDO == null){
                            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                            serviceResult.setErrorCode(ErrorCode.LEGAL_PERSON_CREDIT_REPORT_IMAGE_NOT_EXISTS);
                            return serviceResult;
                        }
                        legalPersonCreditReportImageDO.setImgType(ImgType.LEGAL_PERSON_CREDIT_REPORT_IMG_TYPE);
                        legalPersonCreditReportImageDO.setRefId(refId);
                        legalPersonCreditReportImageDO.setUpdateUser(user);
                        legalPersonCreditReportImageDO.setUpdateTime(now);
                        imgMysqlMapper.update(legalPersonCreditReportImageDO);
                    }
                }


            //对固定资产证明图片操作
            if (ImgType.FIXED_ASSETS_PROVE_IMG_TYPE.equals(imgType)) {
                    for (Image fixedAssetsProveImage : updateImgList){
                        ImageDO fixedAssetsProveImageDO = imgMysqlMapper.findById(fixedAssetsProveImage.getImgId());
                        if (fixedAssetsProveImageDO == null){
                            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                            serviceResult.setErrorCode(ErrorCode.FIXED_ASSETS_PROVE_IMAGE_NOT_EXISTS);
                            return serviceResult;
                        }
                        fixedAssetsProveImageDO.setImgType(ImgType.FIXED_ASSETS_PROVE_IMG_TYPE);
                        fixedAssetsProveImageDO.setRefId(refId);
                        fixedAssetsProveImageDO.setUpdateUser(user);
                        fixedAssetsProveImageDO.setUpdateTime(now);
                        imgMysqlMapper.update(fixedAssetsProveImageDO);
                    }
                }
            }
            //对单位对公账户流水账单图片操作
            if (ImgType.PUBLIC_ACCOUNT_FLOW_BILL_IMG_TYPE.equals(imgType)) {
                for (Image publicAccountFlowBillImage : updateImgList){
                    ImageDO publicAccountFlowBillImageDO = imgMysqlMapper.findById(publicAccountFlowBillImage.getImgId());
                    if (publicAccountFlowBillImageDO == null){
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                        serviceResult.setErrorCode(ErrorCode.PUBLIC_ACCOUNT_FLOW_BILL_IMAGE_NOT_EXISTS);
                        return serviceResult;
                    }
                    publicAccountFlowBillImageDO.setImgType(ImgType.PUBLIC_ACCOUNT_FLOW_BILL_IMG_TYPE);
                    publicAccountFlowBillImageDO.setRefId(refId);
                    publicAccountFlowBillImageDO.setUpdateUser(user);
                    publicAccountFlowBillImageDO.setUpdateTime(now);
                    imgMysqlMapper.update(publicAccountFlowBillImageDO);
                }
            }
            //对社保/公积金缴纳证明图片操作
            if (ImgType.SOCIAL_SECURITY_RO_PROVIDENT_FUND_IMG_TYPE.equals(imgType)) {
                for (Image socialSecurityRoProvidentFundImage : updateImgList){
                ImageDO socialSecurityRoProvidentFundImageDO = imgMysqlMapper.findById(socialSecurityRoProvidentFundImage.getImgId());
                if (socialSecurityRoProvidentFundImageDO == null){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    serviceResult.setErrorCode(ErrorCode.SOCIAL_SECURITY_RO_PROVIDENT_FUND_IMAGE_NOT_EXISTS);
                    return serviceResult;
                }
                socialSecurityRoProvidentFundImageDO.setImgType(ImgType.SOCIAL_SECURITY_RO_PROVIDENT_FUND_IMG_TYPE);
                socialSecurityRoProvidentFundImageDO.setRefId(refId);
                socialSecurityRoProvidentFundImageDO.setUpdateUser(user);
                socialSecurityRoProvidentFundImageDO.setUpdateTime(now);
                imgMysqlMapper.update(socialSecurityRoProvidentFundImageDO);
                }
            }
            //对战略协议或合作协议图片操作
            if (ImgType.COOPERATION_AGREEMENT_IMG_TYPE.equals(imgType)) {
                for (Image cooperationAgreementImage : updateImgList){
                    ImageDO cooperationAgreementImageDO = imgMysqlMapper.findById(cooperationAgreementImage.getImgId());
                    if (cooperationAgreementImageDO == null){
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                        serviceResult.setErrorCode(ErrorCode.COOPERATION_AGREEMENT_IMAGE_NOT_EXISTS);
                        return serviceResult;
                    }
                    cooperationAgreementImageDO.setImgType(ImgType.COOPERATION_AGREEMENT_IMG_TYPE);
                    cooperationAgreementImageDO.setRefId(refId);
                    cooperationAgreementImageDO.setUpdateUser(user);
                    cooperationAgreementImageDO.setUpdateTime(now);
                    imgMysqlMapper.update(cooperationAgreementImageDO);
                }
            }
            //对法人学历证明图片操作
            if (ImgType.LEGAL_PERSON_EDUCATION_IMG_TYPE.equals(imgType)) {
                for (Image legalPersonEducationImage : updateImgList){
                    ImageDO legalPersonEducationImageDO = imgMysqlMapper.findById(legalPersonEducationImage.getImgId());
                    if (legalPersonEducationImageDO == null){
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                        serviceResult.setErrorCode(ErrorCode.LEGAL_PERSON_EDUCATION_IMAGE_NOT_EXISTS);
                        return serviceResult;
                    }
                    legalPersonEducationImageDO.setImgType(ImgType.LEGAL_PERSON_EDUCATION_IMG_TYPE);
                    legalPersonEducationImageDO.setRefId(refId);
                    legalPersonEducationImageDO.setUpdateUser(user);
                    legalPersonEducationImageDO.setUpdateTime(now);
                    imgMysqlMapper.update(legalPersonEducationImageDO);
                }
            }
            //对法人职称证明图片操作
            if (ImgType.LEGAL_PERSON_POSITIONAL_TITLE_IMG_TYPE.equals(imgType)) {
                for (Image legalPersonPositionalTitleImage : updateImgList){
                    ImageDO legalPersonPositionalTitleImageDO = imgMysqlMapper.findById(legalPersonPositionalTitleImage.getImgId());
                    if (legalPersonPositionalTitleImageDO == null){
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                        serviceResult.setErrorCode(ErrorCode.LEGAL_PERSON_POSITIONAL_TITLE_IMAGE_NOT_EXISTS);
                        return serviceResult;
                    }
                    legalPersonPositionalTitleImageDO.setImgType(ImgType.LEGAL_PERSON_POSITIONAL_TITLE_IMG_TYPE);
                    legalPersonPositionalTitleImageDO.setRefId(refId);
                    legalPersonPositionalTitleImageDO.setUpdateUser(user);
                    legalPersonPositionalTitleImageDO.setUpdateTime(now);
                    imgMysqlMapper.update(legalPersonPositionalTitleImageDO);
                }
            }
            //对现场核查表图片操作
            if (ImgType.LOCALE_CHECKLISTS_IMG_TYPE.equals(imgType)) {
                for (Image localeChecklistsImage : updateImgList){
                    ImageDO localeChecklistsImageDO = imgMysqlMapper.findById(localeChecklistsImage.getImgId());
                    if (localeChecklistsImageDO == null){
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                        serviceResult.setErrorCode(ErrorCode.LOCALE_CHECKLISTS_IMAGE_NOT_EXISTS);
                        return serviceResult;
                    }
                    localeChecklistsImageDO.setImgType(ImgType.LOCALE_CHECKLISTS_IMG_TYPE);
                    localeChecklistsImageDO.setRefId(refId);
                    localeChecklistsImageDO.setUpdateUser(user);
                    localeChecklistsImageDO.setUpdateTime(now);
                    imgMysqlMapper.update(localeChecklistsImageDO);
                }
            }
            //对其他材料图片操作
            if (ImgType.OTHER_DATE_IMG_TYPE.equals(imgType)) {
                for (Image otherDateImage : updateImgList){
                    ImageDO otherDateImageDO = imgMysqlMapper.findById(otherDateImage.getImgId());
                    if (otherDateImageDO == null){
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                        serviceResult.setErrorCode(ErrorCode.OTHER_DATE_IMAGE_NOT_EXISTS);
                        return serviceResult;
                    }
                    otherDateImageDO.setImgType(ImgType.OTHER_DATE_IMG_TYPE);
                    otherDateImageDO.setRefId(refId);
                    otherDateImageDO.setUpdateUser(user);
                    otherDateImageDO.setUpdateTime(now);
                    imgMysqlMapper.update(otherDateImageDO);
                }
            }

            if(!dbImgRecordMap.isEmpty()){
                for (Map.Entry<Integer, ImageDO> entry : dbImgRecordMap.entrySet()){
                    ImageDO imageDO = entry.getValue();
                    imageDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
                    imageDO.setUpdateUser(user);
                    imageDO.setUpdateTime(now);
                    imgMysqlMapper.update(imageDO);
                }
            }

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }
}