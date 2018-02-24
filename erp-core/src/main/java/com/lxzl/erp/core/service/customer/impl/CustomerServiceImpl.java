package com.lxzl.erp.core.service.customer.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lxzl.erp.common.cache.CommonCache;
import com.lxzl.erp.common.constant.*;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.customer.CustomerCompanyQueryParam;
import com.lxzl.erp.common.domain.customer.CustomerConsignInfoQueryParam;
import com.lxzl.erp.common.domain.customer.CustomerPersonQueryParam;
import com.lxzl.erp.common.domain.customer.pojo.*;
import com.lxzl.erp.common.domain.payment.account.pojo.CustomerAccount;
import com.lxzl.erp.common.domain.product.pojo.Product;
import com.lxzl.erp.common.domain.system.pojo.Image;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.common.util.BigDecimalUtil;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.common.util.ConverterUtil;
import com.lxzl.erp.common.util.ListUtil;
import com.lxzl.erp.core.service.basic.impl.support.GenerateNoSupport;
import com.lxzl.erp.core.service.businessSystemConfig.BusinessSystemConfigService;
import com.lxzl.erp.core.service.customer.CustomerService;
import com.lxzl.erp.core.service.k3.WebServiceHelper;
import com.lxzl.erp.core.service.payment.PaymentService;
import com.lxzl.erp.core.service.permission.PermissionSupport;
import com.lxzl.erp.core.service.product.ProductService;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.dataaccess.dao.mysql.company.SubCompanyMapper;
import com.lxzl.erp.dataaccess.dao.mysql.customer.*;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductSkuMapper;
import com.lxzl.erp.dataaccess.dao.mysql.system.ImgMysqlMapper;
import com.lxzl.erp.dataaccess.dao.mysql.user.UserMapper;
import com.lxzl.erp.dataaccess.dao.mysql.user.UserRoleMapper;
import com.lxzl.erp.dataaccess.domain.company.SubCompanyDO;
import com.lxzl.erp.dataaccess.domain.customer.*;
import com.lxzl.erp.dataaccess.domain.product.ProductSkuDO;
import com.lxzl.erp.dataaccess.domain.system.ImageDO;
import com.lxzl.erp.dataaccess.domain.user.UserDO;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CustomerServiceImpl implements CustomerService {
    private static final Logger logger = LoggerFactory.getLogger(ConverterUtil.class);

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, String> addCompany(Customer customer) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        Date now = new Date();
        CustomerCompany customerCompany = customer.getCustomerCompany();

        //校验法人手机号,经办人电话,紧急联系人手机号
        if(customerCompany.getIsLegalPersonApple() == 0){
            if(customerCompany.getLegalPersonPhone() != null && customerCompany.getLegalPersonPhone() != ""){
                String regExp = "^1[0-9]{10}$";
                Pattern pattern = Pattern.compile(regExp);
                Matcher matcher = pattern.matcher(customerCompany.getLegalPersonPhone());
                if(!matcher.matches() || customerCompany.getLegalPersonPhone().length()!= 11){
                    serviceResult.setErrorCode(ErrorCode.PHONE_ERROR);
                    return serviceResult;
                }
            }
            if(customerCompany.getLegalPersonPhone().equals(customerCompany.getAgentPersonPhone()) ){
                serviceResult.setErrorCode(ErrorCode.LEGAL_PERSON_PHONE_EQUAL_TO_AGENT_PERSON_PHONE);
                return serviceResult;
            }
            if(customerCompany.getAgentPersonPhone().equals(customerCompany.getConnectPhone())){
                serviceResult.setErrorCode(ErrorCode.AGENT_PERSON_PHONE_EQUAL_TO_CONNECT_PHONE);
                return serviceResult;
            }
            if(customerCompany.getConnectPhone().equals(customerCompany.getLegalPersonPhone())){
                serviceResult.setErrorCode(ErrorCode.CONNECT_PHONE_EQUAL_TO_LEGAL_PERSON_PHONE);
                return serviceResult;
            }
            if(customerCompany.getAgentPersonNo().equals(customerCompany.getLegalPersonNo())){
                serviceResult.setErrorCode(ErrorCode.LEGAL_PERSON_NO_EQUAL_TO_LEGAL_PERSON_NO);
                return serviceResult;
            }
            if(customerCompany.getAgentPersonNo().equals(customerCompany.getLegalPerson())){
                serviceResult.setErrorCode(ErrorCode.LEGAL_PERSON_NAME_EQUAL_TO_LEGAL_PERSON_NAME);
                return serviceResult;
            }
        }

        if(customerCompany.getIsLegalPersonApple() == 1){
            if(customerCompany.getAgentPersonPhone().equals(customerCompany.getConnectPhone())){
                serviceResult.setErrorCode(ErrorCode.AGENT_PERSON_PHONE_EQUAL_TO_CONNECT_PHONE);
                return serviceResult;
            }
            customerCompany.setLegalPerson(customerCompany.getAgentPersonName());
            customerCompany.setLegalPersonNo(customerCompany.getAgentPersonNo());
            customerCompany.setLegalPersonPhone(customerCompany.getAgentPersonPhone());
        }

        CustomerDO dbCustomerDO = customerMapper.findByName(customerCompany.getCompanyName());
        if (dbCustomerDO != null) {
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_IS_EXISTS);
            return serviceResult;
        }
        CustomerDO customerDO = ConverterUtil.convert(customer, CustomerDO.class);
        customerDO.setCustomerNo(generateNoSupport.generateCustomerNo(now, CustomerType.CUSTOMER_TYPE_COMPANY));
        customerDO.setCustomerType(CustomerType.CUSTOMER_TYPE_COMPANY);
        customerDO.setIsDisabled(CommonConstant.COMMON_CONSTANT_NO);
        customerDO.setCustomerName(customer.getCustomerCompany().getCompanyName());
        customerDO.setCustomerStatus(CustomerStatus.STATUS_INIT);
        customerDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        customerDO.setCreateTime(now);
        customerDO.setUpdateTime(now);
        customerDO.setCreateUser(userSupport.getCurrentUserId().toString());
        customerDO.setUpdateUser(userSupport.getCurrentUserId().toString());

        //判断业务员和联合开发员
        serviceResult = judgeUserOwnerAndUnion(customer);
        if (!ErrorCode.SUCCESS.equals(serviceResult.getErrorCode())){
            serviceResult.setErrorCode(serviceResult.getErrorCode());
            return serviceResult;
        }

        customerDO.setOwner(customer.getOwner());
        customerDO.setUnionUser(customer.getUnionUser());
        customerMapper.save(customerDO);

        CustomerCompanyDO customerCompanyDO = ConverterUtil.convert(customer.getCustomerCompany(), CustomerCompanyDO.class);
        //设置首次所需设备
        if (CollectionUtil.isNotEmpty(customer.getCustomerCompany().getCustomerCompanyNeedFirstList())) {
            List<CustomerCompanyNeed> customerCompanyNeedFirstList = customer.getCustomerCompany().getCustomerCompanyNeedFirstList();
            serviceResult = setCustomerCompanyNeed(customerCompanyNeedFirstList);
            if (!ErrorCode.SUCCESS.equals(serviceResult.getErrorCode())){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                serviceResult.setErrorCode(serviceResult.getErrorCode());
                return serviceResult;
            }
            customerCompanyDO.setCustomerCompanyNeedFirstJson(JSON.toJSON(customerCompanyNeedFirstList).toString());
        }

        //判断后续所需设备
        if (CollectionUtil.isNotEmpty(customer.getCustomerCompany().getCustomerCompanyNeedLaterList())) {
            List<CustomerCompanyNeed> customerCompanyNeedLaterList = customer.getCustomerCompany().getCustomerCompanyNeedLaterList();
            serviceResult = setCustomerCompanyNeed(customerCompanyNeedLaterList);
            if (!ErrorCode.SUCCESS.equals(serviceResult.getErrorCode())){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                serviceResult.setErrorCode(serviceResult.getErrorCode());
                return serviceResult;
            }
            customerCompanyDO.setCustomerCompanyNeedFirstJson(JSON.toJSON(customerCompanyNeedLaterList).toString());
        }

        //如果客户选择了将详细地址作为收货地址
        if (CommonConstant.COMMON_CONSTANT_YES.equals(customer.getIsDefaultConsignAddress())){
            saveCustomerCompanyConsignInfo(customerDO,customerCompanyDO,now,userSupport.getCurrentUserId());
        }

        customerCompanyDO.setCustomerId(customerDO.getId());
        customerCompanyDO.setCustomerNo(customerDO.getCustomerNo());
        customerCompanyDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        customerCompanyDO.setCreateTime(now);
        customerCompanyDO.setUpdateTime(now);
        customerCompanyDO.setCreateUser(userSupport.getCurrentUserId().toString());
        customerCompanyDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        customerCompanyMapper.save(customerCompanyDO);

        customer.setCustomerId(customerDO.getId());
        ServiceResult<String, String> serviceResult1 = saveImage(customer, now);
        if (!ErrorCode.SUCCESS.equals(serviceResult1.getErrorCode())) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            serviceResult1.setErrorCode(serviceResult1.getErrorCode(), serviceResult1.getFormatArgs());
            return serviceResult1;
        }

        webServiceHelper.post(PostK3OperatorType.POST_K3_OPERATOR_TYPE_NULL,PostK3Type.POST_K3_TYPE_CUSTOMER, ConverterUtil.convert(customerDO, Customer.class));
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(customerDO.getCustomerNo());
        return serviceResult;
    }


    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, String> addPerson(Customer customer) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        Date now = new Date();

        CustomerDO dbCustomerDO = customerMapper.findByName(customer.getCustomerPerson().getRealName());
        if (dbCustomerDO != null) {
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_IS_EXISTS);
            return serviceResult;
        }
        CustomerDO customerDO = ConverterUtil.convert(customer, CustomerDO.class);
        customerDO.setCustomerNo(generateNoSupport.generateCustomerNo(now, CustomerType.CUSTOMER_TYPE_PERSON));
        customerDO.setCustomerType(CustomerType.CUSTOMER_TYPE_PERSON);
        customerDO.setIsDisabled(CommonConstant.COMMON_CONSTANT_NO);
        customerDO.setCustomerName(customer.getCustomerPerson().getRealName());
        customerDO.setCustomerStatus(CustomerStatus.STATUS_INIT);
        customerDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        customerDO.setCreateTime(now);
        customerDO.setUpdateTime(now);
        customerDO.setCreateUser(userSupport.getCurrentUserId().toString());
        customerDO.setUpdateUser(userSupport.getCurrentUserId().toString());

        //判断业务员和联合开发员
        serviceResult = judgeUserOwnerAndUnion(customer);
        if (!ErrorCode.SUCCESS.equals(serviceResult.getErrorCode())){
            serviceResult.setErrorCode(serviceResult.getErrorCode());
            return serviceResult;
        }
        customerDO.setOwner(customer.getOwner());
        customerDO.setUnionUser(customer.getUnionUser());
        customerMapper.save(customerDO);

        CustomerPersonDO customerPersonDO = ConverterUtil.convert(customer.getCustomerPerson(), CustomerPersonDO.class);
        customerPersonDO.setCustomerId(customerDO.getId());
        customerPersonDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        customerPersonDO.setCreateTime(now);
        customerPersonDO.setUpdateTime(now);
        customerPersonDO.setCreateUser(userSupport.getCurrentUserId().toString());
        customerPersonDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        customerPersonMapper.save(customerPersonDO);

//        //如果客户选择了将详细地址作为收货地址
//        if (CommonConstant.COMMON_CONSTANT_YES.equals(customer.getIsDefaultConsignAddress())) {
//            saveCustomerPersonConsignInfo(customerDO,customerPersonDO,now,userSupport.getCurrentUserId());
//        }
        webServiceHelper.post(PostK3OperatorType.POST_K3_OPERATOR_TYPE_NULL,PostK3Type.POST_K3_TYPE_CUSTOMER, ConverterUtil.convert(customerDO, Customer.class));
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(customerDO.getCustomerNo());
        return serviceResult;
    }


    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, String> updateCompany(Customer customer) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        Date now = new Date();
        CustomerCompany customerCompany = customer.getCustomerCompany();

        //校验法人手机号,经办人电话,紧急联系人手机号
        if(CommonConstant.COMMON_CONSTANT_NO.equals(customerCompany.getIsLegalPersonApple())){
            if(customerCompany.getLegalPersonPhone() != null && customerCompany.getLegalPersonPhone() != ""){
                String regExp = "^1[0-9]{10}$";
                Pattern pattern = Pattern.compile(regExp);
                Matcher matcher = pattern.matcher(customerCompany.getLegalPersonPhone());
                if(!matcher.matches() || customerCompany.getLegalPersonPhone().length()!= 11){
                    serviceResult.setErrorCode(ErrorCode.PHONE_ERROR);
                    return serviceResult;
                }
            }
            if(customerCompany.getLegalPersonPhone().equals(customerCompany.getAgentPersonPhone()) ){
                serviceResult.setErrorCode(ErrorCode.LEGAL_PERSON_PHONE_EQUAL_TO_AGENT_PERSON_PHONE);
                return serviceResult;
            }
            if(customerCompany.getAgentPersonPhone().equals(customerCompany.getConnectPhone())){
                serviceResult.setErrorCode(ErrorCode.AGENT_PERSON_PHONE_EQUAL_TO_CONNECT_PHONE);
                return serviceResult;
            }
            if(customerCompany.getConnectPhone().equals(customerCompany.getLegalPersonPhone())){
                serviceResult.setErrorCode(ErrorCode.CONNECT_PHONE_EQUAL_TO_LEGAL_PERSON_PHONE);
                return serviceResult;
            }
            if(customerCompany.getAgentPersonNo().equals(customerCompany.getLegalPersonNo())){
                serviceResult.setErrorCode(ErrorCode.LEGAL_PERSON_NO_EQUAL_TO_LEGAL_PERSON_NO);
                return serviceResult;
            }
            if(customerCompany.getAgentPersonNo().equals(customerCompany.getLegalPerson())){
                serviceResult.setErrorCode(ErrorCode.LEGAL_PERSON_NAME_EQUAL_TO_LEGAL_PERSON_NAME);
                return serviceResult;
            }
        }

        if(customer.getCustomerCompany().getIsLegalPersonApple() == 1){
            if(customer.getCustomerCompany().getAgentPersonPhone().equals(customer.getCustomerCompany().getConnectPhone())){
                serviceResult.setErrorCode(ErrorCode.AGENT_PERSON_PHONE_EQUAL_TO_CONNECT_PHONE);
                return serviceResult;
            }
            customer.getCustomerCompany().setLegalPerson(customer.getCustomerCompany().getAgentPersonName());
            customer.getCustomerCompany().setLegalPersonNo(customer.getCustomerCompany().getAgentPersonNo());
            customer.getCustomerCompany().setLegalPersonPhone(customer.getCustomerCompany().getAgentPersonPhone());
        }

        CustomerDO dbCustomerDO = customerMapper.findByName(customer.getCustomerCompany().getCompanyName());
        if (dbCustomerDO != null && !dbCustomerDO.getCustomerNo().equals(customer.getCustomerNo())) {
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_IS_EXISTS);
            return serviceResult;
        }

        CustomerDO customerDO = customerMapper.findCustomerCompanyByNo(customer.getCustomerNo());
        if (customerDO == null || !CustomerType.CUSTOMER_TYPE_COMPANY.equals(customerDO.getCustomerType())) {
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_NOT_EXISTS);
            return serviceResult;
        }

        if (CustomerStatus.STATUS_COMMIT.equals(customerDO.getCustomerStatus())
                || CustomerStatus.STATUS_PASS.equals(customerDO.getCustomerStatus())) {
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_CAN_NOT_EDIT);
            return serviceResult;
        }

        //判断业务员和联合开发员
        serviceResult = judgeUserOwnerAndUnion(customer);
        if (!ErrorCode.SUCCESS.equals(serviceResult.getErrorCode())){
            serviceResult.setErrorCode(serviceResult.getErrorCode());
            return serviceResult;
        }

        CustomerCompanyDO customerCompanyDO = customerCompanyMapper.findByCustomerId(customerDO.getId());
        CustomerCompanyDO newCustomerCompanyDO = ConverterUtil.convert(customer.getCustomerCompany(), CustomerCompanyDO.class);

        //如果客户选择了将详细地址作为收货地址
        if (CommonConstant.COMMON_CONSTANT_YES.equals(customer.getIsDefaultConsignAddress())){
            if(customerCompanyDO.getDefaultAddressReferId() == null){
                //如果默认地址选项有值，并且客户的默认地址关联ID为null
                saveCustomerCompanyConsignInfo(customerDO,newCustomerCompanyDO,now,userSupport.getCurrentUserId());
            }else{
                CustomerConsignInfoDO customerConsignInfoDO = customerConsignInfoMapper.findById(customerCompanyDO.getDefaultAddressReferId());
                customerConsignInfoDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                customerConsignInfoDO.setUpdateTime(now);
                customerConsignInfoDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                customerConsignInfoMapper.update(customerConsignInfoDO);
                newCustomerCompanyDO.setDefaultAddressReferId(customerConsignInfoDO.getId());
            }

        }else if (CommonConstant.COMMON_CONSTANT_NO.equals(customer.getIsDefaultConsignAddress())){
            if(customerCompanyDO.getDefaultAddressReferId() == null){
                saveCustomerCompanyConsignInfo(customerDO,newCustomerCompanyDO,now,userSupport.getCurrentUserId());
                newCustomerCompanyDO.setDefaultAddressReferId(null);
            }else{
                //如果默认地址选项没有值，并且客户的默认地址关联ID不为null
                CustomerConsignInfoDO customerConsignInfoDO = customerConsignInfoMapper.findById(customerCompanyDO.getDefaultAddressReferId());
                customerConsignInfoDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
                customerConsignInfoDO.setUpdateTime(now);
                customerConsignInfoDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                customerConsignInfoMapper.update(customerConsignInfoDO);
                newCustomerCompanyDO.setDefaultAddressReferId(null);
            }
        }

        //判断首次所需设备 list转json
        if (CollectionUtil.isNotEmpty(customer.getCustomerCompany().getCustomerCompanyNeedFirstList())) {
            List<CustomerCompanyNeed> customerCompanyNeedFirstList = customer.getCustomerCompany().getCustomerCompanyNeedFirstList();
            serviceResult = setCustomerCompanyNeed(customerCompanyNeedFirstList);
            if (!ErrorCode.SUCCESS.equals(serviceResult.getErrorCode())){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                serviceResult.setErrorCode(serviceResult.getErrorCode());
                return serviceResult;
            }
            newCustomerCompanyDO.setCustomerCompanyNeedFirstJson(JSON.toJSON(customerCompanyNeedFirstList).toString());
        }

        //判断后续所需设备
        if (CollectionUtil.isNotEmpty(customerCompany.getCustomerCompanyNeedLaterList())) {
            List<CustomerCompanyNeed> customerCompanyNeedLaterList = customerCompany.getCustomerCompanyNeedLaterList();
            serviceResult = setCustomerCompanyNeed(customerCompanyNeedLaterList);
            if (!ErrorCode.SUCCESS.equals(serviceResult.getErrorCode())){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                serviceResult.setErrorCode(serviceResult.getErrorCode());
                return serviceResult;
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
        List<Image> businessLicensePictureImageList = new ArrayList<>();
        businessLicensePictureImageList.add(customerCompany.getBusinessLicensePictureImage());
        serviceResult = updateImage(businessLicensePictureImageList, ImgType.BUSINESS_LICENSE_PICTURE_IMG_TYPE, customerDO.getId().toString(), userSupport.getCurrentUserId().toString(), now);
        if (!ErrorCode.SUCCESS.equals(serviceResult.getErrorCode())) {
            serviceResult.setErrorCode(serviceResult.getErrorCode(), serviceResult.getFormatArgs());
            return serviceResult;
        }


        //对身份证正面图片操作
        List<Image> legalPersonNoPictureFrontImageList = new ArrayList<>();
        legalPersonNoPictureFrontImageList.add(customerCompany.getLegalPersonNoPictureFrontImage());
        serviceResult = updateImage(legalPersonNoPictureFrontImageList, ImgType.LEGAL_PERSON_NO_PICTURE_FRONT_IMG_TYPE, customerDO.getId().toString(), userSupport.getCurrentUserId().toString(), now);
        if (!ErrorCode.SUCCESS.equals(serviceResult.getErrorCode())) {
            serviceResult.setErrorCode(serviceResult.getErrorCode(), serviceResult.getFormatArgs());
            return serviceResult;
        }


        //对身份证反面图片操作
        List<Image> legalPersonNoPictureBackImageList = new ArrayList<>();
        legalPersonNoPictureBackImageList.add(customerCompany.getLegalPersonNoPictureBackImage());
        serviceResult = updateImage(legalPersonNoPictureBackImageList, ImgType.LEGAL_PERSON_NO_PICTURE_BACK_IMG_TYPE, customerDO.getId().toString(), userSupport.getCurrentUserId().toString(), now);
        if (!ErrorCode.SUCCESS.equals(serviceResult.getErrorCode())) {
            serviceResult.setErrorCode(serviceResult.getErrorCode(), serviceResult.getFormatArgs());
            return serviceResult;
        }

        //对经营场所租赁合同图片操作
        serviceResult = updateImage(customerCompany.getManagerPlaceRentContractImageList(), ImgType.MANAGER_PLACE_RENT_CONTRACT_IMG_TYPE, customerDO.getId().toString(), userSupport.getCurrentUserId().toString(), now);
        if (!ErrorCode.SUCCESS.equals(serviceResult.getErrorCode())) {
            serviceResult.setErrorCode(serviceResult.getErrorCode(), serviceResult.getFormatArgs());
            return serviceResult;
        }

        //对法人个人征信报告或附（法人个人征信授权书）图片操作
        serviceResult = updateImage(customerCompany.getLegalPersonCreditReportImageList(), ImgType.LEGAL_PERSON_CREDIT_REPORT_IMG_TYPE, customerDO.getId().toString(), userSupport.getCurrentUserId().toString(), now);
        if (!ErrorCode.SUCCESS.equals(serviceResult.getErrorCode())) {
            serviceResult.setErrorCode(serviceResult.getErrorCode(), serviceResult.getFormatArgs());
            return serviceResult;
        }

        //对固定资产证明图片操作
        serviceResult = updateImage(customerCompany.getFixedAssetsProveImageList(), ImgType.FIXED_ASSETS_PROVE_IMG_TYPE, customerDO.getId().toString(), userSupport.getCurrentUserId().toString(), now);
        if (!ErrorCode.SUCCESS.equals(serviceResult.getErrorCode())) {
            serviceResult.setErrorCode(serviceResult.getErrorCode(), serviceResult.getFormatArgs());
            return serviceResult;
        }

        //对单位对公账户流水账单图片操作
        serviceResult = updateImage(customerCompany.getPublicAccountFlowBillImageList(), ImgType.PUBLIC_ACCOUNT_FLOW_BILL_IMG_TYPE, customerDO.getId().toString(), userSupport.getCurrentUserId().toString(), now);
        if (!ErrorCode.SUCCESS.equals(serviceResult.getErrorCode())) {
            serviceResult.setErrorCode(serviceResult.getErrorCode(), serviceResult.getFormatArgs());
            return serviceResult;
        }

        //对社保/公积金缴纳证明图片操作
        serviceResult = updateImage(customerCompany.getSocialSecurityRoProvidentFundImageList(), ImgType.SOCIAL_SECURITY_RO_PROVIDENT_FUND_IMG_TYPE, customerDO.getId().toString(), userSupport.getCurrentUserId().toString(), now);
        if (!ErrorCode.SUCCESS.equals(serviceResult.getErrorCode())) {
            serviceResult.setErrorCode(serviceResult.getErrorCode(), serviceResult.getFormatArgs());
            return serviceResult;
        }

        //对战略协议或合作协议图片操作
        serviceResult = updateImage(customerCompany.getCooperationAgreementImageList(), ImgType.COOPERATION_AGREEMENT_IMG_TYPE, customerDO.getId().toString(), userSupport.getCurrentUserId().toString(), now);
        if (!ErrorCode.SUCCESS.equals(serviceResult.getErrorCode())) {
            serviceResult.setErrorCode(serviceResult.getErrorCode(), serviceResult.getFormatArgs());
            return serviceResult;
        }

        //对法人学历证明图片操作
        serviceResult = updateImage(customerCompany.getLegalPersonEducationImageList(), ImgType.LEGAL_PERSON_EDUCATION_IMG_TYPE, customerDO.getId().toString(), userSupport.getCurrentUserId().toString(), now);
        if (!ErrorCode.SUCCESS.equals(serviceResult.getErrorCode())) {
            serviceResult.setErrorCode(serviceResult.getErrorCode(), serviceResult.getFormatArgs());
            return serviceResult;
        }

        //对法人职称证明图片操作.
        serviceResult = updateImage(customerCompany.getLegalPersonPositionalTitleImageList(), ImgType.LEGAL_PERSON_POSITIONAL_TITLE_IMG_TYPE, customerDO.getId().toString(), userSupport.getCurrentUserId().toString(), now);
        if (!ErrorCode.SUCCESS.equals(serviceResult.getErrorCode())) {
            serviceResult.setErrorCode(serviceResult.getErrorCode(), serviceResult.getFormatArgs());
            return serviceResult;
        }

        //对现场核查表图片操作
        serviceResult = updateImage(customerCompany.getLocaleChecklistsImageList(), ImgType.LOCALE_CHECKLISTS_IMG_TYPE, customerDO.getId().toString(), userSupport.getCurrentUserId().toString(), now);
        if (!ErrorCode.SUCCESS.equals(serviceResult.getErrorCode())) {
            serviceResult.setErrorCode(serviceResult.getErrorCode(), serviceResult.getFormatArgs());
            return serviceResult;
        }

        //对其他材料图片操作
        serviceResult = updateImage(customerCompany.getOtherDateImageList(), ImgType.OTHER_DATE_IMG_TYPE, customerDO.getId().toString(), userSupport.getCurrentUserId().toString(), now);
        if (!ErrorCode.SUCCESS.equals(serviceResult.getErrorCode())) {
            serviceResult.setErrorCode(serviceResult.getErrorCode(), serviceResult.getFormatArgs());
            return serviceResult;
        }

        //更改开发员和联合开发员
        serviceResult = updateCustomerOwnerAndUnionUser(customerDO,customer,now);
        if (!ErrorCode.SUCCESS.equals(serviceResult.getErrorCode())){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            serviceResult.setErrorCode(serviceResult.getErrorCode(), serviceResult.getFormatArgs());
            return serviceResult;
        }

        customerDO.setIsDisabled(null);
        customerDO.setCustomerStatus(CustomerStatus.STATUS_INIT);
        customerDO.setCustomerName(newCustomerCompanyDO.getCompanyName());
        customerDO.setUpdateTime(now);
        customerDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        customerDO.setFirstApplyAmount(customer.getFirstApplyAmount());
        customerDO.setLaterApplyAmount(customer.getLaterApplyAmount());
        customerDO.setRemark(customer.getRemark());
        customerMapper.update(customerDO);

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(customerDO.getCustomerNo());
        return serviceResult;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, String> updatePerson(Customer customer) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        Date now = new Date();
        CustomerDO dbCustomerDO = customerMapper.findByName(customer.getCustomerPerson().getRealName());
        if (dbCustomerDO != null && !dbCustomerDO.getCustomerNo().equals(customer.getCustomerNo())) {
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_IS_EXISTS);
            return serviceResult;
        }
        CustomerDO customerDO = customerMapper.findCustomerPersonByNo(customer.getCustomerNo());
        if (customerDO == null || !CustomerType.CUSTOMER_TYPE_PERSON.equals(customerDO.getCustomerType())) {
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_NOT_EXISTS);
            return serviceResult;
        }

        if (CustomerStatus.STATUS_COMMIT.equals(customerDO.getCustomerStatus())
                || CustomerStatus.STATUS_PASS.equals(customerDO.getCustomerStatus())) {
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_CAN_NOT_EDIT);
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

        //判断业务员和联合开发员
        serviceResult = judgeUserOwnerAndUnion(customer);
        if (!ErrorCode.SUCCESS.equals(serviceResult.getErrorCode())){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            serviceResult.setErrorCode(serviceResult.getErrorCode());
            return serviceResult;
        }

        //更改开发员和联合开发员
        serviceResult = updateCustomerOwnerAndUnionUser(customerDO,customer,now);
        if (!ErrorCode.SUCCESS.equals(serviceResult.getErrorCode())){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            serviceResult.setErrorCode(serviceResult.getErrorCode(), serviceResult.getFormatArgs());
            return serviceResult;
        }

        //更改联合开发人
        customerDO.setIsDisabled(null);
        customerDO.setFirstApplyAmount(customer.getFirstApplyAmount());
        customerDO.setLaterApplyAmount(customer.getLaterApplyAmount());
        customerDO.setCustomerStatus(CustomerStatus.STATUS_INIT);
        customerDO.setCustomerName(newCustomerPersonDO.getRealName());
        customerDO.setUpdateTime(now);
        customerDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        customerDO.setRemark(customer.getRemark());
        customerMapper.update(customerDO);

//        //如果客户选择了将详细地址作为收货地址
//        if (CommonConstant.COMMON_CONSTANT_YES.equals(customer.getIsDefaultConsignAddress())){
//            List<CustomerConsignInfoDO> customerConsignInfoDOList = customerConsignInfoMapper.findByCustomerIdAndConsigneeNameAndConsigneePhoneAndAddress(customerDO.getId(),newCustomerPersonDO.getRealName(),newCustomerPersonDO.getPhone(),newCustomerPersonDO.getAddress());
//            if (CollectionUtil.isEmpty(customerConsignInfoDOList)){
//                saveCustomerPersonConsignInfo(customerDO,newCustomerPersonDO,now,userSupport.getCurrentUserId());
//            }
//        }

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(customerDO.getCustomerNo());
        return serviceResult;
    }

    @Override
    public ServiceResult<String, String> commitCustomer(Customer customer) {
        ServiceResult<String, String> result = new ServiceResult<>();
        Date currentTime = new Date();
        CustomerDO customerDO = customerMapper.findByNo(customer.getCustomerNo());
        if (customerDO == null) {
            result.setErrorCode(ErrorCode.CUSTOMER_NOT_EXISTS);
            return result;
        }
        if (!CustomerStatus.STATUS_INIT.equals(customerDO.getCustomerStatus())
                && !CustomerStatus.STATUS_REJECT.equals(customerDO.getCustomerStatus())) {
            result.setErrorCode(ErrorCode.CUSTOMER_STATUS_ERROR);
            return result;
        }
        if (CommonConstant.COMMON_CONSTANT_YES.equals(customerDO.getIsDisabled())) {
            result.setErrorCode(ErrorCode.CUSTOMER_IS_DISABLED);
            return result;
        }

        //判断是否有收货地址
        List<CustomerConsignInfoDO> customerConsignInfoDOList = customerConsignInfoMapper.findByCustomerId(customerDO.getId());
        if (CollectionUtil.isEmpty(customerConsignInfoDOList)){
            result.setErrorCode(ErrorCode.CUSTOMER_CONSIGN_ADDRESS_NOT_NULL);
            return result;
        }

        //如果客户是企业
        if (CustomerType.CUSTOMER_TYPE_COMPANY.equals(customerDO.getCustomerType())){
            CustomerCompanyDO customerCompanyDO = customerCompanyMapper.findByCustomerId(customerDO.getId());
            //判断客户资料中必填项是否填写

            //1.租赁设备明细
            String customerCompanyNeedFirstJson = customerCompanyDO.getCustomerCompanyNeedFirstJson();
            List<CustomerCompanyNeed> customerCompanyNeedList = JSONObject.parseArray(customerCompanyNeedFirstJson, CustomerCompanyNeed.class);

            //todo 后面需要放开的字段
            //对租赁设备进行判断
            /*if(CollectionUtil.isEmpty(customerCompanyNeedList)){
                result.setErrorCode(ErrorCode.CUSTOMER_COMPANY_NEED_FIRST_NOT_NULL);
                return result;
            }*/
            if (CollectionUtil.isNotEmpty(customerCompanyNeedList)) {
                for (CustomerCompanyNeed customerCompanyNeed : customerCompanyNeedList) {
                    ProductSkuDO productSkuDO = productSkuMapper.findById(customerCompanyNeed.getSkuId());
                    if (productSkuDO == null) {
                        result.setErrorCode(ErrorCode.CUSTOMER_COMPANY_NEED_SKU_ID_NOT_NULL);
                        return result;
                    }
                    if (customerCompanyNeed.getUnitPrice() == null) {
                        result.setErrorCode(ErrorCode.CUSTOMER_COMPANY_NEED_UNIT_PRICE_NOT_NULL);
                        return result;
                    }
                    if (customerCompanyNeed.getRentCount() == null) {
                        result.setErrorCode(ErrorCode.CUSTOMER_COMPANY_NEED_RENT_COUNT_NOT_NULL);
                        return result;
                    }
                    if (customerCompanyNeed.getTotalPrice() == null) {
                        result.setErrorCode(ErrorCode.CUSTOMER_COMPANY_NEED_TOTAL_PRICE_NOT_NULL);
                        return result;
                    }
                }
            }

            //对图片附件进行判断
            List<ImageDO> imageDOList = imgMysqlMapper.findByRefId(customerCompanyDO.getCustomerId().toString());
            if (CollectionUtil.isEmpty(imageDOList)){
                result.setErrorCode(ErrorCode.CUSTOMER_COMPANY_IMAGES_NOT_NULL);
                return result;
            }

            List<ImageDO> businessLicensePicture = imgMysqlMapper.findByRefIdAndType(customerCompanyDO.getCustomerId().toString(), ImgType.BUSINESS_LICENSE_PICTURE_IMG_TYPE);
            List<ImageDO> legalPersonNoPictureFront = imgMysqlMapper.findByRefIdAndType(customerCompanyDO.getCustomerId().toString(), ImgType.LEGAL_PERSON_NO_PICTURE_FRONT_IMG_TYPE);
            List<ImageDO> legalPersonNoPictureBack = imgMysqlMapper.findByRefIdAndType(customerCompanyDO.getCustomerId().toString(), ImgType.LEGAL_PERSON_NO_PICTURE_BACK_IMG_TYPE);

            if (businessLicensePicture.size() == 0 ){
                result.setErrorCode(ErrorCode.BUSINESS_LICENSE_PICTURE_IMAGE_NOT_NULL);
                return result;
            }
            if (legalPersonNoPictureFront.size() == 0){
                result.setErrorCode(ErrorCode.LEGAL_PERSON_NO_PICTURE_FRONT_IMAGE_NOT_NULL);
                return result;
            }
            if (legalPersonNoPictureBack.size() == 0){
                result.setErrorCode(ErrorCode.LEGAL_PERSON_NO_PICTURE_BACK_IMAGE_NOT_NULL);
                return result;
            }

            //对注册资本判断
            if (customerCompanyDO.getRegisteredCapital() == null){
                result.setErrorCode(ErrorCode.CUSTOMER_COMPANY_REGISTERED_CAPITAL_NOT_NULL);
                return result;
            }
            //对企业行业判断
            if(StringUtil.isEmpty(customerCompanyDO.getIndustry())){
                result.setErrorCode(ErrorCode.CUSTOMER_COMPANY_INDUSTRY_NOT_NULL);
                return result;
            }

            //对企业的设备用途判断
            if (StringUtil.isEmpty(customerCompanyDO.getProductPurpose())){
                result.setErrorCode(ErrorCode.PRODUCT_PURPOSE_NOT_NULL);
                return result;
            }

            //对企业成立时间判断
            if (customerCompanyDO.getCompanyFoundTime() == null){
                result.setErrorCode(ErrorCode.CUSTOMER_COMPANY_FOUND_TIME_NOT_NULL);
                return result;
            }

            //对企业办公人数判断
            if (customerCompanyDO.getOfficeNumber() == null){
                result.setErrorCode(ErrorCode.CUSTOMER_COMPANY_OFFICE_NUMBER_NOT_NULL);
                return result;
            }

            //对企业的经营面积判断
            if (customerCompanyDO.getOperatingArea() == null){
                result.setErrorCode(ErrorCode.CUSTOMER_COMPANY_OPERATING_AREA_NOT_NULL);
                return result;
            }
        }

        customerDO.setCustomerStatus(CustomerStatus.STATUS_COMMIT);
        customerDO.setUpdateTime(currentTime);
        customerDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        customerMapper.update(customerDO);
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    public ServiceResult<String, String> verifyCustomer(String customerNo, Integer customerStatus, String verifyRemark) {
        ServiceResult<String, String> result = new ServiceResult<>();
        Date currentTime = new Date();
        CustomerDO customerDO = customerMapper.findByNo(customerNo);
        if (customerDO == null) {
            result.setErrorCode(ErrorCode.CUSTOMER_NOT_EXISTS);
            return result;
        }

        if (!CustomerStatus.STATUS_COMMIT.equals(customerDO.getCustomerStatus())
                && !CustomerStatus.STATUS_PASS.equals(customerDO.getCustomerStatus())) {
            result.setErrorCode(ErrorCode.CUSTOMER_STATUS_ERROR);
            return result;
        }
        if (!CustomerStatus.STATUS_PASS.equals(customerStatus)
                && !CustomerStatus.STATUS_REJECT.equals(customerStatus)) {
            result.setErrorCode(ErrorCode.CUSTOMER_STATUS_ERROR);
            return result;
        }

        if (CustomerStatus.STATUS_REJECT.equals(customerStatus) && StringUtil.isNotBlank(verifyRemark)) {
            customerDO.setFailReason(verifyRemark);
        }

        if (CustomerStatus.STATUS_PASS.equals(customerStatus) && StringUtil.isNotBlank(verifyRemark)) {
            customerDO.setPassReason(verifyRemark);
        }

        customerDO.setCustomerStatus(customerStatus);
        customerDO.setUpdateTime(currentTime);
        customerDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        customerMapper.update(customerDO);

        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    public ServiceResult<String, String> addShortReceivableAmount(Customer customer) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();

        CustomerDO customerDO = customerMapper.findByNo(customer.getCustomerNo());
        if (customerDO == null){
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_NOT_EXISTS);
            return serviceResult;
        }

        customerDO.setShortLimitReceivableAmount(customer.getShortLimitReceivableAmount());
        customerDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        customerDO.setUpdateTime(new Date());
        customerMapper.update(customerDO);

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(customerDO.getCustomerNo());
        return serviceResult;
    }

    @Override
    public ServiceResult<String, String> addStatementDate(Customer customer) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();

        CustomerDO customerDO = customerMapper.findByNo(customer.getCustomerNo());
        if (customerDO == null){
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_NOT_EXISTS);
            return serviceResult;
        }

        customerDO.setStatementDate(customer.getStatementDate());
        customerDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        customerDO.setUpdateTime(new Date());
        customerMapper.update(customerDO);

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(customerDO.getCustomerNo());
        return serviceResult;
    }


    @Override
    public ServiceResult<String, Page<Customer>> pageCustomerCompany(CustomerCompanyQueryParam customerCompanyQueryParam) {
        ServiceResult<String, Page<Customer>> result = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(customerCompanyQueryParam.getPageNo(), customerCompanyQueryParam.getPageSize());

        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        maps.put("customerCompanyQueryParam", customerCompanyQueryParam);
        maps.put("permissionParam", permissionSupport.getPermissionParam(PermissionType.PERMISSION_TYPE_USER));

        Integer totalCount = customerMapper.findCustomerCompanyCountByParams(maps);
        List<CustomerDO> customerDOList = customerMapper.findCustomerCompanyByParams(maps);
        if(CollectionUtil.isNotEmpty(customerDOList)){
            for(CustomerDO customerDO : customerDOList){
                //如果当前用户不是跟单员  并且 用户不是联合开发人 并且用户不是创建人,屏蔽手机，座机字段
                processCustomerPhone(customerDO);
            }
        }

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
        maps.put("customerPersonQueryParam", customerPersonQueryParam);
        maps.put("permissionParam", permissionSupport.getPermissionParam(PermissionType.PERMISSION_TYPE_USER));

        Integer totalCount = customerMapper.findCustomerPersonCountByParams(maps);
        List<CustomerDO> customerDOList = customerMapper.findCustomerPersonByParams(maps);
        if(CollectionUtil.isNotEmpty(customerDOList)){
            for(CustomerDO customerDO : customerDOList){
                //如果当前用户不是跟单员  并且 用户不是联合开发人 并且用户不是创建人,屏蔽手机，座机字段
                processCustomerPhone(customerDO);
            }
        }
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
        //如果当前用户不是跟单员  并且 用户不是联合开发人 并且用户不是创建人  并且当前用户的可观察列表中不包含当前数据的创建人，则不允许看此条数据
        if (!haveAuthority(customerDO.getOwner(), customerDO.getUnionUser(), Integer.parseInt(customerDO.getCreateUser()))) {
            serviceResult.setErrorCode(ErrorCode.DATA_HAVE_NO_PERMISSION);
            return serviceResult;
        }
        //如果当前用户不是跟单员  并且 用户不是联合开发人 并且用户不是创建人,屏蔽手机，座机字段
        processCustomerPhone(customerDO);
        CustomerAccount customerAccount = paymentService.queryCustomerAccount(customerDO.getCustomerNo());
        Customer customerResult = ConverterUtil.convert(customerDO, Customer.class);
        customerResult.setCustomerAccount(customerAccount);

        //显示联合开发原的省，市，区
        if (customerDO.getUnionUser() != null) {
            Integer companyId = userSupport.getCompanyIdByUser(customerDO.getUnionUser());
            SubCompanyDO subCompanyDO = subCompanyMapper.findById(companyId);
            customerResult.setUnionAreaProvinceName(subCompanyDO.getProvinceName());
            customerResult.setUnionAreaCityName(subCompanyDO.getCityName());
            customerResult.setUnionAreaDistrictName(subCompanyDO.getDistrictName());
        }

        //首次所需设备，Json转list
        if (StringUtil.isNotEmpty(customerDO.getCustomerCompanyDO().getCustomerCompanyNeedFirstJson())) {
            String customerCompanyNeedFirstJson = customerDO.getCustomerCompanyDO().getCustomerCompanyNeedFirstJson();
            customerResult.getCustomerCompany().setCustomerCompanyNeedFirstList(JSONObject.parseArray(customerCompanyNeedFirstJson, CustomerCompanyNeed.class));
        }

        //后续所需设备，Json转list
        if (StringUtil.isNotEmpty(customerDO.getCustomerCompanyDO().getCustomerCompanyNeedLaterJson())) {
            String customerCompanyNeedLaterList = customerDO.getCustomerCompanyDO().getCustomerCompanyNeedLaterJson();
            customerResult.getCustomerCompany().setCustomerCompanyNeedLaterList(JSONObject.parseArray(customerCompanyNeedLaterList, CustomerCompanyNeed.class));
        }

        //加入默认地址关联ID
        if((customerDO.getCustomerCompanyDO().getDefaultAddressReferId() != null)){
            customerResult.setIsDefaultConsignAddress(CommonConstant.COMMON_CONSTANT_YES);
        }else{
            customerResult.setIsDefaultConsignAddress(CommonConstant.COMMON_CONSTANT_NO);
        }

        //开始加入附件
        //加入营业执照
        List<ImageDO> businessLicensePictureImageList = imgMysqlMapper.findByRefIdAndType(customerDO.getId().toString(), ImgType.BUSINESS_LICENSE_PICTURE_IMG_TYPE);
        if (CollectionUtil.isNotEmpty(businessLicensePictureImageList)) {
            Image businessLicensePictureImage = ConverterUtil.convert(businessLicensePictureImageList.get(0), Image.class);
            customerResult.getCustomerCompany().setBusinessLicensePictureImage(businessLicensePictureImage);
        }

        //法人/股东身份证正面
        List<ImageDO> legalPersonNoPictureFrontImageList = imgMysqlMapper.findByRefIdAndType(customerDO.getId().toString(), ImgType.LEGAL_PERSON_NO_PICTURE_FRONT_IMG_TYPE);
        if (CollectionUtil.isNotEmpty(legalPersonNoPictureFrontImageList)) {
            Image legalPersonNoPictureFrontImage = ConverterUtil.convert(legalPersonNoPictureFrontImageList.get(0), Image.class);
            customerResult.getCustomerCompany().setLegalPersonNoPictureFrontImage(legalPersonNoPictureFrontImage);
        }

        //法人/股东身份证反
        List<ImageDO> legalPersonNoPictureBackImageList = imgMysqlMapper.findByRefIdAndType(customerDO.getId().toString(), ImgType.LEGAL_PERSON_NO_PICTURE_BACK_IMG_TYPE);
        if (CollectionUtil.isNotEmpty(legalPersonNoPictureBackImageList)) {
            Image legalPersonNoPictureBackImage = ConverterUtil.convert(legalPersonNoPictureBackImageList.get(0), Image.class);
            customerResult.getCustomerCompany().setLegalPersonNoPictureBackImage(legalPersonNoPictureBackImage);
        }

        //经营场所租赁合同
        List<ImageDO> managerPlaceRentContractImageDOList = imgMysqlMapper.findByRefIdAndType(customerDO.getId().toString(), ImgType.MANAGER_PLACE_RENT_CONTRACT_IMG_TYPE);
        if (CollectionUtil.isNotEmpty(managerPlaceRentContractImageDOList)) {
            List<Image> managerPlaceRentContractImageList = ConverterUtil.convertList(managerPlaceRentContractImageDOList, Image.class);
            customerResult.getCustomerCompany().setManagerPlaceRentContractImageList(managerPlaceRentContractImageList);
        }

        //法人个人征信报告或附（法人个人征信授权书）
        List<ImageDO> legalPersonCreditReportImageDOList = imgMysqlMapper.findByRefIdAndType(customerDO.getId().toString(), ImgType.LEGAL_PERSON_CREDIT_REPORT_IMG_TYPE);
        if (CollectionUtil.isNotEmpty(legalPersonCreditReportImageDOList)) {
            List<Image> legalPersonCreditReportImageList = ConverterUtil.convertList(legalPersonCreditReportImageDOList, Image.class);
            customerResult.getCustomerCompany().setLegalPersonCreditReportImageList(legalPersonCreditReportImageList);
        }

        //固定资产证明
        List<ImageDO> fixedAssetsProveImageDOList = imgMysqlMapper.findByRefIdAndType(customerDO.getId().toString(), ImgType.FIXED_ASSETS_PROVE_IMG_TYPE);
        if (CollectionUtil.isNotEmpty(fixedAssetsProveImageDOList)) {
            List<Image> fixedAssetsProveImageList = ConverterUtil.convertList(fixedAssetsProveImageDOList, Image.class);
            customerResult.getCustomerCompany().setFixedAssetsProveImageList(fixedAssetsProveImageList);
        }

        //单位对公账户流水账单
        List<ImageDO> publicAccountFlowBillImageDOList = imgMysqlMapper.findByRefIdAndType(customerDO.getId().toString(), ImgType.PUBLIC_ACCOUNT_FLOW_BILL_IMG_TYPE);
        if (CollectionUtil.isNotEmpty(publicAccountFlowBillImageDOList)) {
            List<Image> publicAccountFlowBillImageList = ConverterUtil.convertList(publicAccountFlowBillImageDOList, Image.class);
            customerResult.getCustomerCompany().setPublicAccountFlowBillImageList(publicAccountFlowBillImageList);
        }

        //社保/公积金缴纳证明
        List<ImageDO> socialSecurityRoProvidentFundImageListDOList = imgMysqlMapper.findByRefIdAndType(customerDO.getId().toString(), ImgType.SOCIAL_SECURITY_RO_PROVIDENT_FUND_IMG_TYPE);
        if (CollectionUtil.isNotEmpty(socialSecurityRoProvidentFundImageListDOList)) {
            List<Image> socialSecurityRoProvidentFundImageList = ConverterUtil.convertList(socialSecurityRoProvidentFundImageListDOList, Image.class);
            customerResult.getCustomerCompany().setSocialSecurityRoProvidentFundImageList(socialSecurityRoProvidentFundImageList);
        }
        //战略协议或合作协议
        List<ImageDO> cooperationAgreementImageListDOList = imgMysqlMapper.findByRefIdAndType(customerDO.getId().toString(), ImgType.COOPERATION_AGREEMENT_IMG_TYPE);
        if (CollectionUtil.isNotEmpty(cooperationAgreementImageListDOList)) {
            List<Image> cooperationAgreementImageList = ConverterUtil.convertList(cooperationAgreementImageListDOList, Image.class);
            customerResult.getCustomerCompany().setCooperationAgreementImageList(cooperationAgreementImageList);
        }

        //法人学历证明
        List<ImageDO> legalPersonEducationImageListDOList = imgMysqlMapper.findByRefIdAndType(customerDO.getId().toString(), ImgType.LEGAL_PERSON_EDUCATION_IMG_TYPE);
        if (CollectionUtil.isNotEmpty(legalPersonEducationImageListDOList)) {
            List<Image> legalPersonEducationImageList = ConverterUtil.convertList(legalPersonEducationImageListDOList, Image.class);
            customerResult.getCustomerCompany().setLegalPersonEducationImageList(legalPersonEducationImageList);
        }

        //法人职称证明
        List<ImageDO> legalPersonPositionalTitleImageListDOList = imgMysqlMapper.findByRefIdAndType(customerDO.getId().toString(), ImgType.LEGAL_PERSON_POSITIONAL_TITLE_IMG_TYPE);
        if (CollectionUtil.isNotEmpty(legalPersonPositionalTitleImageListDOList)) {
            List<Image> legalPersonPositionalTitleImageList = ConverterUtil.convertList(legalPersonPositionalTitleImageListDOList, Image.class);
            customerResult.getCustomerCompany().setLegalPersonPositionalTitleImageList(legalPersonPositionalTitleImageList);
        }

        //现场核查表
        List<ImageDO> localeChecklistsImageListDOList = imgMysqlMapper.findByRefIdAndType(customerDO.getId().toString(), ImgType.LOCALE_CHECKLISTS_IMG_TYPE);
        if (CollectionUtil.isNotEmpty(localeChecklistsImageListDOList)) {
            List<Image> localeChecklistsImageList = ConverterUtil.convertList(localeChecklistsImageListDOList, Image.class);
            customerResult.getCustomerCompany().setLocaleChecklistsImageList(localeChecklistsImageList);
        }

        //其他材料
        List<ImageDO> otherDateImageListDOList = imgMysqlMapper.findByRefIdAndType(customerDO.getId().toString(), ImgType.OTHER_DATE_IMG_TYPE);
        if (CollectionUtil.isNotEmpty(otherDateImageListDOList)) {
            List<Image> otherDateImageList = ConverterUtil.convertList(otherDateImageListDOList, Image.class);
            customerResult.getCustomerCompany().setOtherDateImageList(otherDateImageList);
        }

        if (customerResult.getOwner() != null) {
            customerResult.setCustomerOwnerUser(CommonCache.userMap.get(customerResult.getOwner()));
        }
        if (customerResult.getUnionUser() != null) {
            customerResult.setCustomerUnionUser(CommonCache.userMap.get(customerResult.getUnionUser()));
        }

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(customerResult);
        return serviceResult;
    }
    private String hidePhone(String phone){
        String hidePhone = phone;
        if(StringUtil.isNotEmpty(phone)&&phone.length()>=4){
            hidePhone = phone.substring(0,phone.length()-4)+"****";
        }
        return hidePhone;
    }

    private void processCustomerPhone(CustomerDO customerDO){
        if (!haveAuthority(customerDO.getOwner(), customerDO.getUnionUser(), Integer.parseInt(customerDO.getCreateUser()))) {
            CustomerCompanyDO customerCompanyDO = customerDO.getCustomerCompanyDO();
            if(customerCompanyDO!=null){
//                customerCompanyDO.setConnectPhone(hidePhone(customerCompanyDO.getConnectPhone()));
//                customerCompanyDO.setAgentPersonPhone(hidePhone(customerCompanyDO.getAgentPersonPhone()));
//                customerCompanyDO.setLegalPersonPhone(hidePhone(customerCompanyDO.getLegalPersonPhone()));
//                customerCompanyDO.setLandline(hidePhone(customerCompanyDO.getLandline()));
                customerCompanyDO.setConnectPhone(null);
                customerCompanyDO.setAgentPersonPhone(null);
                customerCompanyDO.setLegalPersonPhone(null);
                customerCompanyDO.setLandline(null);
            }
            CustomerPersonDO customerPersonDO = customerDO.getCustomerPersonDO();
            if(customerPersonDO!=null){
//                customerPersonDO.setPhone(hidePhone(customerPersonDO.getPhone()));
//                customerPersonDO.setConnectPhone(hidePhone(customerPersonDO.getConnectPhone()));
                customerPersonDO.setPhone(null);
                customerPersonDO.setConnectPhone(null);
            }
        }
    }

    @Override
    public ServiceResult<String, Customer> detailCustomer(String customerNo) {
        ServiceResult<String, Customer> serviceResult = new ServiceResult<>();
        CustomerDO customerDO = customerMapper.findByNo(customerNo);
        if (customerDO == null) {
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_NOT_EXISTS);
            return serviceResult;
        }
        if (CustomerType.CUSTOMER_TYPE_COMPANY.equals(customerDO.getCustomerType())) {
            customerDO = customerMapper.findCustomerCompanyByNo(customerNo);
        } else if (CustomerType.CUSTOMER_TYPE_PERSON.equals(customerDO.getCustomerType())) {
            customerDO = customerMapper.findCustomerCompanyByNo(customerNo);
        }

        CustomerAccount customerAccount = paymentService.queryCustomerAccount(customerDO.getCustomerNo());
        //如果当前用户不是跟单员  并且 用户不是联合开发人 并且用户不是创建人  并且当前用户的可观察列表中不包含当前数据的创建人，则不允许看此条数据
        if (!haveAuthority(customerDO.getOwner(), customerDO.getUnionUser(), Integer.parseInt(customerDO.getCreateUser()))) {
            serviceResult.setErrorCode(ErrorCode.DATA_HAVE_NO_PERMISSION);
            return serviceResult;
        }
        //如果当前用户不是跟单员  并且 用户不是联合开发人 并且用户不是创建人,屏蔽手机，座机字段
        processCustomerPhone(customerDO);
        Customer customerResult = ConverterUtil.convert(customerDO, Customer.class);
        //显示联合开发原的省，市，区
        if (customerDO.getUnionUser() != null) {
            Integer companyId = userSupport.getCompanyIdByUser(customerDO.getUnionUser());
            SubCompanyDO subCompanyDO = subCompanyMapper.findById(companyId);
            customerResult.setUnionAreaProvinceName(subCompanyDO.getProvinceName());
            customerResult.setUnionAreaCityName(subCompanyDO.getCityName());
            customerResult.setUnionAreaDistrictName(subCompanyDO.getDistrictName());
        }
        customerResult.setCustomerAccount(customerAccount);

        if (customerDO.getCustomerCompanyDO() != null){
            //加入默认地址关联ID
            if(customerDO.getCustomerCompanyDO().getDefaultAddressReferId() != null){
                customerResult.setIsDefaultConsignAddress(CommonConstant.COMMON_CONSTANT_YES);
            }else{
                customerResult.setIsDefaultConsignAddress(CommonConstant.COMMON_CONSTANT_NO);
            }
        }

        if (customerResult.getOwner() != null) {
            customerResult.setCustomerOwnerUser(CommonCache.userMap.get(customerResult.getOwner()));
        }
        if (customerResult.getUnionUser() != null) {
            customerResult.setCustomerUnionUser(CommonCache.userMap.get(customerResult.getUnionUser()));
        }
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(customerResult);
        return serviceResult;
    }

    @Override
    public Customer detailCustomerTemp(String customerNo) {
        CustomerDO customerDO = customerMapper.findByNo(customerNo);
        if (CustomerType.CUSTOMER_TYPE_COMPANY.equals(customerDO.getCustomerType())) {
            customerDO = customerMapper.findCustomerCompanyByNo(customerNo);
        } else if (CustomerType.CUSTOMER_TYPE_PERSON.equals(customerDO.getCustomerType())) {
            customerDO = customerMapper.findCustomerCompanyByNo(customerNo);
        }

        CustomerAccount customerAccount = paymentService.queryCustomerAccount(customerDO.getCustomerNo());
        List<Integer> dataAccessPassiveUserList = permissionSupport.getCanAccessPassiveUserList(userSupport.getCurrentUserId());


        Customer customerResult = ConverterUtil.convert(customerDO, Customer.class);
        //显示联合开发原的省，市，区
        if (customerDO.getUnionUser() != null) {
            Integer companyId = userSupport.getCompanyIdByUser(customerDO.getUnionUser());
            SubCompanyDO subCompanyDO = subCompanyMapper.findById(companyId);
            customerResult.setUnionAreaProvinceName(subCompanyDO.getProvinceName());
            customerResult.setUnionAreaCityName(subCompanyDO.getCityName());
            customerResult.setUnionAreaDistrictName(subCompanyDO.getDistrictName());
        }
        customerResult.setCustomerAccount(customerAccount);

        if (customerDO.getCustomerCompanyDO() != null){
            //加入默认地址关联ID
            if(customerDO.getCustomerCompanyDO().getDefaultAddressReferId() != null){
                customerResult.setIsDefaultConsignAddress(CommonConstant.COMMON_CONSTANT_YES);
            }else{
                customerResult.setIsDefaultConsignAddress(CommonConstant.COMMON_CONSTANT_NO);
            }
        }

        if (customerResult.getOwner() != null) {
            customerResult.setCustomerOwnerUser(CommonCache.userMap.get(customerResult.getOwner()));
        }
        if (customerResult.getUnionUser() != null) {
            customerResult.setCustomerUnionUser(CommonCache.userMap.get(customerResult.getUnionUser()));
        }

        return customerResult;
    }
    @Override
    public ServiceResult<String, Customer> queryCustomerByNo(String customerNo) {
        ServiceResult<String, Customer> serviceResult = new ServiceResult<>();
        CustomerDO customerDO = customerMapper.findByNo(customerNo);
        if (customerDO == null) {
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_NOT_EXISTS);
            return serviceResult;
        }
        if (CustomerType.CUSTOMER_TYPE_COMPANY.equals(customerDO.getCustomerType())) {
            customerDO = customerMapper.findCustomerCompanyByNo(customerNo);
        } else if (CustomerType.CUSTOMER_TYPE_PERSON.equals(customerDO.getCustomerType())) {
            customerDO = customerMapper.findCustomerCompanyByNo(customerNo);
        }
        CustomerAccount customerAccount = paymentService.queryCustomerAccount(customerDO.getCustomerNo());
        Customer customerResult = ConverterUtil.convert(customerDO, Customer.class);
        //显示联合开发原的省，市，区
        if (customerDO.getUnionUser() != null) {
            Integer companyId = userSupport.getCompanyIdByUser(customerDO.getUnionUser());
            SubCompanyDO subCompanyDO = subCompanyMapper.findById(companyId);
            customerResult.setUnionAreaProvinceName(subCompanyDO.getProvinceName());
            customerResult.setUnionAreaCityName(subCompanyDO.getCityName());
            customerResult.setUnionAreaDistrictName(subCompanyDO.getDistrictName());
        }
        customerResult.setCustomerAccount(customerAccount);

        if (customerResult.getOwner() != null) {
            customerResult.setCustomerOwnerUser(CommonCache.userMap.get(customerResult.getOwner()));
        }
        if (customerResult.getUnionUser() != null) {
            customerResult.setCustomerUnionUser(CommonCache.userMap.get(customerResult.getUnionUser()));
        }
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(customerResult);
        return serviceResult;
    }


    boolean haveAuthority(Integer owner, Integer unionUser, Integer createUser) {
        List<Integer> dataAccessPassiveUserList = permissionSupport.getCanAccessPassiveUserList(userSupport.getCurrentUserId());
        return !(!userSupport.getCurrentUserId().equals(owner) &&
                !userSupport.getCurrentUserId().equals(unionUser) &&
                !userSupport.getCurrentUserId().equals(createUser) &&
                !dataAccessPassiveUserList.contains(createUser) &&
                !dataAccessPassiveUserList.contains(owner) &&
                !dataAccessPassiveUserList.contains(unionUser) &&
                !userSupport.isSuperUser());
    }


    @Override
    public ServiceResult<String, Customer> detailCustomerPerson(Customer customer) {
        ServiceResult<String, Customer> serviceResult = new ServiceResult<>();
        CustomerDO customerDO = customerMapper.findCustomerPersonByNo(customer.getCustomerNo());
        if (customerDO == null || !CustomerType.CUSTOMER_TYPE_PERSON.equals(customerDO.getCustomerType())) {
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_NOT_EXISTS);
            return serviceResult;
        }
        //如果当前用户不是跟单员  并且 用户不是联合开发人 并且用户不是创建人  并且当前用户的可观察列表中不包含当前数据的创建人，则不允许看此条数据
        if (!haveAuthority(customerDO.getOwner(), customerDO.getUnionUser(), Integer.parseInt(customerDO.getCreateUser()))) {
            serviceResult.setErrorCode(ErrorCode.DATA_HAVE_NO_PERMISSION);
            return serviceResult;
        }
        //如果当前用户不是跟单员  并且 用户不是联合开发人 并且用户不是创建人,屏蔽手机，座机字段
        processCustomerPhone(customerDO);
        CustomerAccount customerAccount = paymentService.queryCustomerAccount(customerDO.getCustomerNo());
        Customer customerResult = ConverterUtil.convert(customerDO, Customer.class);
        //显示联合开发原的省，市，区
        if (customerDO.getUnionUser() != null) {
            Integer companyId = userSupport.getCompanyIdByUser(customerDO.getUnionUser());
            SubCompanyDO subCompanyDO = subCompanyMapper.findById(companyId);
            customerResult.setUnionAreaProvinceName(subCompanyDO.getProvinceName());
            customerResult.setUnionAreaCityName(subCompanyDO.getCityName());
            customerResult.setUnionAreaDistrictName(subCompanyDO.getDistrictName());
        }
        customerResult.setCustomerAccount(customerAccount);

        if (customerResult.getOwner() != null) {
            customerResult.setCustomerOwnerUser(CommonCache.userMap.get(customerResult.getOwner()));
        }
        if (customerResult.getUnionUser() != null) {
            customerResult.setCustomerUnionUser(CommonCache.userMap.get(customerResult.getUnionUser()));
        }
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

            CustomerRiskManagementDO customerRiskManagementDO = ConverterUtil.convert(customerRiskManagement, CustomerRiskManagementDO.class);
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
            CustomerRiskManagementDO customerRiskManagementDOForUpdate = ConverterUtil.convert(customerRiskManagement, CustomerRiskManagementDO.class);
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
        maps.put("customerConsignInfoQueryParam", customerConsignInfoQueryParam);

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

        //如果传送过来的isMain是1
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

    @Override
    public ServiceResult<String, String> disabledCustomer(Customer customer) {
        ServiceResult<String, String> result = new ServiceResult<>();
        String currentUserId = userSupport.getCurrentUserId().toString();
        Date currentTime = new Date();
        CustomerDO customerDO = customerMapper.findByNo(customer.getCustomerNo());

        if (customerDO == null) {
            result.setErrorCode(ErrorCode.CUSTOMER_NOT_EXISTS);
            return result;
        }

        customerDO.setIsDisabled(CommonConstant.COMMON_CONSTANT_YES);
        customerDO.setUpdateTime(currentTime);
        customerDO.setUpdateUser(currentUserId);
        customerMapper.update(customerDO);

        result.setResult(customerDO.getCustomerNo());
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    public ServiceResult<String, String> enableCustomer(Customer customer) {
        ServiceResult<String, String> result = new ServiceResult<>();
        String currentUserId = userSupport.getCurrentUserId().toString();
        Date currentTime = new Date();
        CustomerDO customerDO = customerMapper.findByNo(customer.getCustomerNo());

        if (customerDO == null) {
            result.setErrorCode(ErrorCode.CUSTOMER_NOT_EXISTS);
            return result;
        }
        customerDO.setIsDisabled(CommonConstant.COMMON_CONSTANT_NO);
        customerDO.setUpdateTime(currentTime);
        customerDO.setUpdateUser(currentUserId);
        customerMapper.update(customerDO);

        result.setResult(customerDO.getCustomerNo());
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    private ServiceResult<String, String> saveImage(Customer customer, Date now) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();


        //对营业执照图片操作
        if (customer.getCustomerCompany().getBusinessLicensePictureImage() != null) {
            ImageDO businessLicensePictureImageDO = imgMysqlMapper.findById(customer.getCustomerCompany().getBusinessLicensePictureImage().getImgId());
            if (businessLicensePictureImageDO == null) {
                serviceResult.setErrorCode(ErrorCode.BUSINESS_LICENSE_PICTURE_IMAGE_NOT_EXISTS);
                return serviceResult;
            }
            if (StringUtil.isNotEmpty(businessLicensePictureImageDO.getRefId())) {
                serviceResult.setErrorCode(ErrorCode.IMG_REF_ID_NEED_NULL, businessLicensePictureImageDO.getId());
                return serviceResult;
            }
            businessLicensePictureImageDO.setImgType(ImgType.BUSINESS_LICENSE_PICTURE_IMG_TYPE);
            businessLicensePictureImageDO.setRefId(customer.getCustomerId().toString());
            businessLicensePictureImageDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            businessLicensePictureImageDO.setUpdateTime(now);
            imgMysqlMapper.update(businessLicensePictureImageDO);
        }

        if (customer.getCustomerCompany().getLegalPersonNoPictureFrontImage() != null) {
            //对身份证正面图片操作
            ImageDO legalPersonNoPictureFrontImageDO = imgMysqlMapper.findById(customer.getCustomerCompany().getLegalPersonNoPictureFrontImage().getImgId());
            if (legalPersonNoPictureFrontImageDO == null) {
                serviceResult.setErrorCode(ErrorCode.LEGAL_PERSON_NO_PICTURE_FRONT_IMAGE_NOT_EXISTS);
                return serviceResult;
            }
            if (StringUtil.isNotEmpty(legalPersonNoPictureFrontImageDO.getRefId())) {
                serviceResult.setErrorCode(ErrorCode.IMG_REF_ID_NEED_NULL, legalPersonNoPictureFrontImageDO.getId());
                return serviceResult;
            }
            legalPersonNoPictureFrontImageDO.setImgType(ImgType.LEGAL_PERSON_NO_PICTURE_FRONT_IMG_TYPE);
            legalPersonNoPictureFrontImageDO.setRefId(customer.getCustomerId().toString());
            legalPersonNoPictureFrontImageDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            legalPersonNoPictureFrontImageDO.setUpdateTime(now);
            imgMysqlMapper.update(legalPersonNoPictureFrontImageDO);
        }

        if (customer.getCustomerCompany().getLegalPersonNoPictureBackImage() != null) {
            //对身份证反面图片操作
            ImageDO legalPersonNoPictureBackImageDO = imgMysqlMapper.findById(customer.getCustomerCompany().getLegalPersonNoPictureBackImage().getImgId());
            if (legalPersonNoPictureBackImageDO == null) {
                serviceResult.setErrorCode(ErrorCode.LEGAL_PERSON_NO_PICTURE_BACK_IMAGE_NOT_EXISTS);
                return serviceResult;
            }
            if (StringUtil.isNotEmpty(legalPersonNoPictureBackImageDO.getRefId())) {
                serviceResult.setErrorCode(ErrorCode.IMG_REF_ID_NEED_NULL, legalPersonNoPictureBackImageDO.getId());
                return serviceResult;
            }
            legalPersonNoPictureBackImageDO.setImgType(ImgType.LEGAL_PERSON_NO_PICTURE_BACK_IMG_TYPE);
            legalPersonNoPictureBackImageDO.setRefId(customer.getCustomerId().toString());
            legalPersonNoPictureBackImageDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            legalPersonNoPictureBackImageDO.setUpdateTime(now);
            imgMysqlMapper.update(legalPersonNoPictureBackImageDO);
        }

        //对经营场所租赁合同图片操作
        if (CollectionUtil.isNotEmpty(customer.getCustomerCompany().getManagerPlaceRentContractImageList())) {
            for (Image managerPlaceRentContractImage : customer.getCustomerCompany().getManagerPlaceRentContractImageList()) {
                ImageDO managerPlaceRentContractImageDO = imgMysqlMapper.findById(managerPlaceRentContractImage.getImgId());
                if (managerPlaceRentContractImageDO == null) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    serviceResult.setErrorCode(ErrorCode.MANAGER_PLACE_RENT_CONTRACT_IMAGE_NOT_EXISTS);
                    return serviceResult;
                }
                if (StringUtil.isNotEmpty(managerPlaceRentContractImageDO.getRefId())) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    serviceResult.setErrorCode(ErrorCode.IMG_REF_ID_NEED_NULL, managerPlaceRentContractImageDO.getId());
                    return serviceResult;
                }
                managerPlaceRentContractImageDO.setImgType(ImgType.MANAGER_PLACE_RENT_CONTRACT_IMG_TYPE);
                managerPlaceRentContractImageDO.setRefId(customer.getCustomerId().toString());
                managerPlaceRentContractImageDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                managerPlaceRentContractImageDO.setUpdateTime(now);
                imgMysqlMapper.update(managerPlaceRentContractImageDO);
            }
        }

        //对//法人个人征信报告或附（法人个人征信授权书）图片操作
        if (CollectionUtil.isNotEmpty(customer.getCustomerCompany().getLegalPersonCreditReportImageList())) {
            for (Image legalPersonCreditReportImage : customer.getCustomerCompany().getLegalPersonCreditReportImageList()) {
                ImageDO legalPersonCreditReportImageDO = imgMysqlMapper.findById(legalPersonCreditReportImage.getImgId());
                if (legalPersonCreditReportImageDO == null) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    serviceResult.setErrorCode(ErrorCode.LEGAL_PERSON_CREDIT_REPORT_IMAGE_NOT_EXISTS);
                    return serviceResult;
                }
                if (StringUtil.isNotEmpty(legalPersonCreditReportImageDO.getRefId())) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    serviceResult.setErrorCode(ErrorCode.IMG_REF_ID_NEED_NULL, legalPersonCreditReportImageDO.getId());
                    return serviceResult;
                }
                legalPersonCreditReportImageDO.setImgType(ImgType.LEGAL_PERSON_CREDIT_REPORT_IMG_TYPE);
                legalPersonCreditReportImageDO.setRefId(customer.getCustomerId().toString());
                legalPersonCreditReportImageDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                legalPersonCreditReportImageDO.setUpdateTime(now);
                imgMysqlMapper.update(legalPersonCreditReportImageDO);
            }
        }

        //对固定资产证明图片操作
        if (CollectionUtil.isNotEmpty(customer.getCustomerCompany().getFixedAssetsProveImageList())) {
            for (Image fixedAssetsProveImage : customer.getCustomerCompany().getFixedAssetsProveImageList()) {
                ImageDO fixedAssetsProveImageDO = imgMysqlMapper.findById(fixedAssetsProveImage.getImgId());
                if (fixedAssetsProveImageDO == null) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    serviceResult.setErrorCode(ErrorCode.FIXED_ASSETS_PROVE_IMAGE_NOT_EXISTS);
                    return serviceResult;
                }
                if (StringUtil.isNotEmpty(fixedAssetsProveImageDO.getRefId())) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    serviceResult.setErrorCode(ErrorCode.IMG_REF_ID_NEED_NULL, fixedAssetsProveImageDO.getId());
                    return serviceResult;
                }
                fixedAssetsProveImageDO.setImgType(ImgType.FIXED_ASSETS_PROVE_IMG_TYPE);
                fixedAssetsProveImageDO.setRefId(customer.getCustomerId().toString());
                fixedAssetsProveImageDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                fixedAssetsProveImageDO.setUpdateTime(now);
                imgMysqlMapper.update(fixedAssetsProveImageDO);
            }
        }

        //对单位对公账户流水账单图片操作
        if (CollectionUtil.isNotEmpty(customer.getCustomerCompany().getPublicAccountFlowBillImageList())) {
            for (Image publicAccountFlowBillImage : customer.getCustomerCompany().getPublicAccountFlowBillImageList()) {
                ImageDO publicAccountFlowBillImageDO = imgMysqlMapper.findById(publicAccountFlowBillImage.getImgId());
                if (publicAccountFlowBillImageDO == null) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    serviceResult.setErrorCode(ErrorCode.PUBLIC_ACCOUNT_FLOW_BILL_IMAGE_NOT_EXISTS);
                    return serviceResult;
                }
                if (StringUtil.isNotEmpty(publicAccountFlowBillImageDO.getRefId())) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    serviceResult.setErrorCode(ErrorCode.IMG_REF_ID_NEED_NULL, publicAccountFlowBillImageDO.getId());
                    return serviceResult;
                }
                publicAccountFlowBillImageDO.setImgType(ImgType.PUBLIC_ACCOUNT_FLOW_BILL_IMG_TYPE);
                publicAccountFlowBillImageDO.setRefId(customer.getCustomerId().toString());
                publicAccountFlowBillImageDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                publicAccountFlowBillImageDO.setUpdateTime(now);
                imgMysqlMapper.update(publicAccountFlowBillImageDO);
            }
        }

        //对社保/公积金缴纳证明图片操作
        if (CollectionUtil.isNotEmpty(customer.getCustomerCompany().getSocialSecurityRoProvidentFundImageList())) {
            for (Image socialSecurityRoProvidentFundImage : customer.getCustomerCompany().getSocialSecurityRoProvidentFundImageList()) {
                ImageDO socialSecurityRoProvidentFundImageDO = imgMysqlMapper.findById(socialSecurityRoProvidentFundImage.getImgId());
                if (socialSecurityRoProvidentFundImageDO == null) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    serviceResult.setErrorCode(ErrorCode.SOCIAL_SECURITY_RO_PROVIDENT_FUND_IMAGE_NOT_EXISTS);
                    return serviceResult;
                }
                if (StringUtil.isNotEmpty(socialSecurityRoProvidentFundImageDO.getRefId())) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    serviceResult.setErrorCode(ErrorCode.IMG_REF_ID_NEED_NULL, socialSecurityRoProvidentFundImageDO.getId());
                    return serviceResult;
                }
                socialSecurityRoProvidentFundImageDO.setImgType(ImgType.SOCIAL_SECURITY_RO_PROVIDENT_FUND_IMG_TYPE);
                socialSecurityRoProvidentFundImageDO.setRefId(customer.getCustomerId().toString());
                socialSecurityRoProvidentFundImageDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                socialSecurityRoProvidentFundImageDO.setUpdateTime(now);
                imgMysqlMapper.update(socialSecurityRoProvidentFundImageDO);
            }
        }

        //对战略协议或合作协议图片操作
        if (CollectionUtil.isNotEmpty(customer.getCustomerCompany().getCooperationAgreementImageList())) {
            for (Image cooperationAgreementImage : customer.getCustomerCompany().getCooperationAgreementImageList()) {
                ImageDO cooperationAgreementImageDO = imgMysqlMapper.findById(cooperationAgreementImage.getImgId());
                if (cooperationAgreementImageDO == null) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    serviceResult.setErrorCode(ErrorCode.COOPERATION_AGREEMENT_IMAGE_NOT_EXISTS);
                    return serviceResult;
                }
                if (StringUtil.isNotEmpty(cooperationAgreementImageDO.getRefId())) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    serviceResult.setErrorCode(ErrorCode.IMG_REF_ID_NEED_NULL, cooperationAgreementImageDO.getId());
                    return serviceResult;
                }
                cooperationAgreementImageDO.setImgType(ImgType.COOPERATION_AGREEMENT_IMG_TYPE);
                cooperationAgreementImageDO.setRefId(customer.getCustomerId().toString());
                cooperationAgreementImageDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                cooperationAgreementImageDO.setUpdateTime(now);
                imgMysqlMapper.update(cooperationAgreementImageDO);
            }
        }

        //对法人学历证明图片操作
        if (CollectionUtil.isNotEmpty(customer.getCustomerCompany().getLegalPersonEducationImageList())) {
            for (Image legalPersonEducationImage : customer.getCustomerCompany().getLegalPersonEducationImageList()) {
                ImageDO legalPersonEducationImageDO = imgMysqlMapper.findById(legalPersonEducationImage.getImgId());
                if (legalPersonEducationImageDO == null) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    serviceResult.setErrorCode(ErrorCode.LEGAL_PERSON_EDUCATION_IMAGE_NOT_EXISTS);
                    return serviceResult;
                }
                if (StringUtil.isNotEmpty(legalPersonEducationImageDO.getRefId())) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    serviceResult.setErrorCode(ErrorCode.IMG_REF_ID_NEED_NULL, legalPersonEducationImageDO.getId());
                    return serviceResult;
                }
                legalPersonEducationImageDO.setImgType(ImgType.LEGAL_PERSON_EDUCATION_IMG_TYPE);
                legalPersonEducationImageDO.setRefId(customer.getCustomerId().toString());
                legalPersonEducationImageDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                legalPersonEducationImageDO.setUpdateTime(now);
                imgMysqlMapper.update(legalPersonEducationImageDO);
            }
        }

        //对法人职称证明图片操作
        if (CollectionUtil.isNotEmpty(customer.getCustomerCompany().getLegalPersonPositionalTitleImageList())) {
            for (Image legalPersonPositionalTitleImage : customer.getCustomerCompany().getLegalPersonPositionalTitleImageList()) {
                ImageDO legalPersonPositionalTitleImageDO = imgMysqlMapper.findById(legalPersonPositionalTitleImage.getImgId());
                if (legalPersonPositionalTitleImageDO == null) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    serviceResult.setErrorCode(ErrorCode.LEGAL_PERSON_POSITIONAL_TITLE_IMAGE_NOT_EXISTS);
                    return serviceResult;
                }
                if (StringUtil.isNotEmpty(legalPersonPositionalTitleImageDO.getRefId())) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    serviceResult.setErrorCode(ErrorCode.IMG_REF_ID_NEED_NULL, legalPersonPositionalTitleImageDO.getId());
                    return serviceResult;
                }
                legalPersonPositionalTitleImageDO.setImgType(ImgType.LEGAL_PERSON_POSITIONAL_TITLE_IMG_TYPE);
                legalPersonPositionalTitleImageDO.setRefId(customer.getCustomerId().toString());
                legalPersonPositionalTitleImageDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                legalPersonPositionalTitleImageDO.setUpdateTime(now);
                imgMysqlMapper.update(legalPersonPositionalTitleImageDO);
            }
        }

        //对现场核查表图片操作
        if (CollectionUtil.isNotEmpty(customer.getCustomerCompany().getLocaleChecklistsImageList())) {
            for (Image localeChecklistsImage : customer.getCustomerCompany().getLocaleChecklistsImageList()) {
                ImageDO localeChecklistsImageDO = imgMysqlMapper.findById(localeChecklistsImage.getImgId());
                if (localeChecklistsImageDO == null) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    serviceResult.setErrorCode(ErrorCode.LOCALE_CHECKLISTS_IMAGE_NOT_EXISTS);
                    return serviceResult;
                }
                if (StringUtil.isNotEmpty(localeChecklistsImageDO.getRefId())) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    serviceResult.setErrorCode(ErrorCode.IMG_REF_ID_NEED_NULL, localeChecklistsImageDO.getId());
                    return serviceResult;
                }
                localeChecklistsImageDO.setImgType(ImgType.LOCALE_CHECKLISTS_IMG_TYPE);
                localeChecklistsImageDO.setRefId(customer.getCustomerId().toString());
                localeChecklistsImageDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                localeChecklistsImageDO.setUpdateTime(now);
                imgMysqlMapper.update(localeChecklistsImageDO);
            }
        }

        //对其他材料图片操作
        if (CollectionUtil.isNotEmpty(customer.getCustomerCompany().getOtherDateImageList())) {
            for (Image otherDateImage : customer.getCustomerCompany().getOtherDateImageList()) {
                ImageDO otherDateImageDO = imgMysqlMapper.findById(otherDateImage.getImgId());
                if (otherDateImageDO == null) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    serviceResult.setErrorCode(ErrorCode.OTHER_DATE_IMAGE_NOT_EXISTS);
                    return serviceResult;
                }
                if (StringUtil.isNotEmpty(otherDateImageDO.getRefId())) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    serviceResult.setErrorCode(ErrorCode.IMG_REF_ID_NEED_NULL, otherDateImageDO.getId());
                    return serviceResult;
                }
                otherDateImageDO.setImgType(ImgType.OTHER_DATE_IMG_TYPE);
                otherDateImageDO.setRefId(customer.getCustomerId().toString());
                otherDateImageDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                otherDateImageDO.setUpdateTime(now);
                imgMysqlMapper.update(otherDateImageDO);
            }
        }
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }

    private ServiceResult<String, String> updateImage(List<Image> imageList, Integer imgType, String refId, String user, Date now) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        List<Image> updateImgList = new ArrayList<>();
        List<ImageDO> dbImgRecord = imgMysqlMapper.findByRefIdAndType(refId, imgType);
        Map<Integer, ImageDO> dbImgRecordMap = ListUtil.listToMap(dbImgRecord, "id");

        if (CollectionUtil.isNotEmpty(imageList)) {
            for (Image image : imageList) {
                if (image != null) {
                    if (dbImgRecordMap.get(image.getImgId()) != null) {
                        dbImgRecordMap.remove(image.getImgId());
                    } else {
                        updateImgList.add(image);
                    }
                }
            }
        }

        if (CollectionUtil.isNotEmpty(updateImgList)) {
            for (Image img : updateImgList) {
                ImageDO imgDO = imgMysqlMapper.findById(img.getImgId());
                if (imgDO == null) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    serviceResult.setErrorCode(ErrorCode.IMAGE_NOT_EXISTS);
                    return serviceResult;
                }
                if (StringUtil.isNotEmpty(imgDO.getRefId()) && !imgDO.getRefId().equals(refId)) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    serviceResult.setErrorCode(ErrorCode.IMG_REF_ID_NEED_NULL, imgDO.getId());
                    return serviceResult;
                }
                imgDO.setImgType(imgType);
                imgDO.setRefId(refId);
                imgDO.setUpdateUser(user);
                imgDO.setUpdateTime(now);
                imgMysqlMapper.update(imgDO);
            }
        }

        if (!dbImgRecordMap.isEmpty()) {
            for (Map.Entry<Integer, ImageDO> entry : dbImgRecordMap.entrySet()) {
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

    @Override
    public ServiceResult<String, Customer> queryCustomerByCompanyName(String companyName) {
        ServiceResult<String, Customer> serviceResult = new ServiceResult<>();
        CustomerCompanyQueryParam customerCompanyQueryParam = new CustomerCompanyQueryParam();
        customerCompanyQueryParam.setFullCompanyName(companyName);
        Map<String, Object> map = new HashMap<>();
        map.put("start", 0);
        map.put("pageSize", Integer.MAX_VALUE);
        map.put("customerCompanyQueryParam", customerCompanyQueryParam);
        List<CustomerCompanyDO> customerCompanyDOList = customerCompanyMapper.findCustomerCompanyByParams(map);
        if (CollectionUtil.isEmpty(customerCompanyDOList)) {
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_NOT_EXISTS);
            return serviceResult;
        }

        //公司客户信息
        CustomerCompanyDO customerCompanyDO = customerCompanyDOList.get(0);
        //客户信息
        CustomerDO customerDO = customerMapper.findById(customerCompanyDO.getCustomerId());
        if (customerDO == null) {
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_NOT_EXISTS);
            return serviceResult;
        }

        //客户分控信息
        CustomerRiskManagementDO customerRiskManagementDO = customerRiskManagementMapper.findByCustomerId(customerDO.getId());
        if (customerRiskManagementDO != null) {
            customerDO.setCustomerRiskManagementDO(customerRiskManagementDO);
        }
        //业务员信息
        UserDO ownerUserDO = userMapper.findByUserId(customerDO.getOwner());
        if (ownerUserDO != null) {
            customerDO.setOwnerName(ownerUserDO.getUserName());
        }
        customerDO.setCustomerCompanyDO(customerCompanyDO);
        //封装数据
        Customer customerResult = ConverterUtil.convert(customerDO, Customer.class);
        //联合开发人
        UserDO unionUserDO = userMapper.findByUserId(customerDO.getUnionUser());
        if (unionUserDO != null) {
            customerResult.setUnionUserName(unionUserDO.getUserName());
            SubCompanyDO subCompanyDO = userRoleMapper.findSubCompanyByUserId(unionUserDO.getId());

            subCompanyDO = subCompanyMapper.findById(subCompanyDO.getId());
            customerResult.setUnionAreaProvinceName(subCompanyDO.getProvinceName());
            customerResult.setUnionAreaCityName(subCompanyDO.getCityName());
            customerResult.setUnionAreaDistrictName(subCompanyDO.getDistrictName());
        }

        CustomerAccount customerAccount = paymentService.queryCustomerAccountNoLogin(customerDO.getCustomerNo());

        if (customerAccount != null) {
            customerResult.setCustomerAccount(customerAccount);
        }
        customerResult.setCustomerOwnerUser(ConverterUtil.convert(ownerUserDO, User.class));
        customerResult.setCustomerUnionUser(ConverterUtil.convert(unionUserDO, User.class));

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(customerResult);
        return serviceResult;
    }

    private ServiceResult<String,String> updateCustomerOwnerAndUnionUser(CustomerDO customerDO,Customer customer,Date now) {
        ServiceResult<String,String> serviceResult = new ServiceResult<>();

        Integer userDOOwner = customerDO.getOwner();
        Integer userDOUnion = customerDO.getUnionUser();
        Integer userOwner = customer.getOwner();
        Integer userUnion = customer.getUnionUser();

        //更改开发员
        if (!customerDO.getOwner().equals(customer.getOwner())){
            Integer companyIdByUserDo = userSupport.getCompanyIdByUser(customerDO.getOwner());
            Integer companyIdByUser = userSupport.getCompanyIdByUser(customer.getOwner());
            //如果客户开发人不是电销人员，并且修改后的开发人是电销人员
            if (!CommonConstant.ELECTRIC_SALE_COMPANY_ID.equals(companyIdByUserDo) && CommonConstant.ELECTRIC_SALE_COMPANY_ID.equals(companyIdByUser)){
                serviceResult.setErrorCode(ErrorCode.CUSTOMER_OWNER_NOT_CHANGE_ELECTRIC_SALE_COMPANY);
                return serviceResult;
            }
            //如果客户开发人是电销人员,并且修改后的开发人不是电销人员
            if (CommonConstant.ELECTRIC_SALE_COMPANY_ID.equals(companyIdByUserDo) && !CommonConstant.ELECTRIC_SALE_COMPANY_ID.equals(companyIdByUser)){
                //设置客户的属地化时间
                customerDO.setLocalizationTime(now);
            }
            customerDO.setOwner(customer.getOwner());
        }

        //修改联合开发元员
        if(customer.getUnionUser() != null){
            if (customerDO.getUnionUser() == null){
                customerDO.setUnionUser(customer.getUnionUser());
            }else{
                if (!customerDO.getUnionUser().equals(customer.getUnionUser())){
                    Integer companyIdByUserDo = userSupport.getCompanyIdByUser(customerDO.getUnionUser());
                    Integer companyIdByUser = userSupport.getCompanyIdByUser(customer.getUnionUser());
                    //如果联合开发员不是电销，并且修改后联合开发员是电销
                    if (!CommonConstant.ELECTRIC_SALE_COMPANY_ID.equals(companyIdByUserDo) && CommonConstant.ELECTRIC_SALE_COMPANY_ID.equals(companyIdByUser)){
                        serviceResult.setErrorCode(ErrorCode.CUSTOMER_UNION_USER_NOT_CHANGE_ELECTRIC_SALE_COMPANY);
                        return serviceResult;
                    }
                    customerDO.setUnionUser(customer.getUnionUser());
                }
            }
        }

        if (userUnion == null && userDOUnion != null){
            customerDO.setUnionUser(customer.getUnionUser());
        }

        //创建客户变更记录
        //如果开发员改变
        if (!userDOOwner.equals(userOwner)){
            //如果传入联合开发员为null或者不为null，都可以以传入的联合开发员为传递值
            createCustomerUpdateLog(customerDO.getId(), userOwner, userUnion, now);
        }

        //如果开发员未改变
        if (userDOOwner.equals(userOwner)){
            //联合开发员本来为空同时传入的联合开发员不为空
            if (userDOUnion == null && (userUnion != null)){
                createCustomerUpdateLog(customerDO.getId(), userOwner, userUnion, now);
            }else if (userDOUnion != null && !userDOUnion.equals(userUnion)) {
                //联合开发员不为空，只有传入的联合开发员不同时，传入为null，也视为不同
                createCustomerUpdateLog(customerDO.getId(), userOwner, userUnion, now);
            }
        }

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }

    private void createCustomerUpdateLog(Integer customerId,Integer owner,Integer unionUser,Date now){
        CustomerUpdateLogDO customerUpdateLogDO = new CustomerUpdateLogDO();
        customerUpdateLogDO.setCustomerId(customerId);
        customerUpdateLogDO.setOwner(owner);
        customerUpdateLogDO.setUnionUser(unionUser);
        customerUpdateLogDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        customerUpdateLogDO.setCreateUser(userSupport.getCurrentUserId().toString());
        customerUpdateLogDO.setCreateTime(now);
        customerUpdateLogMapper.save(customerUpdateLogDO);
    }

    private ServiceResult<String,String> judgeUserOwnerAndUnion(Customer customer) {
        ServiceResult<String,String> serviceResult = new ServiceResult<>();
        //判断开发员是否存在
        UserDO userOwnerDO = userMapper.findByUserId(customer.getOwner());
        if (userOwnerDO == null){
            serviceResult.setErrorCode(ErrorCode.USER_OWNER_NOT_EXISTS);
            return serviceResult;
        }

        if (customer.getUnionUser() != null){
            UserDO userUnionDO = userMapper.findByUserId(customer.getUnionUser());
            if (userUnionDO == null){
                serviceResult.setErrorCode(ErrorCode.USER_UNION_NOT_EXISTS);
                return serviceResult;
            }
            //判断联合开发员是否是总公司
            boolean headUser = userSupport.isHeadUser(customer.getUnionUser());
            if (headUser){
                serviceResult.setErrorCode(ErrorCode.CUSTOMER_UNION_USER_IS_NOT_HEADER_COMPANY);
                return serviceResult;
            }

            if (customer.getOwner().equals(customer.getUnionUser())){
                serviceResult.setErrorCode(ErrorCode.CUSTOMER_OWNER_USER_AND_UNION_USER_NOT_SAME);
                return serviceResult;
            }
        }

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }

    private void saveCustomerCompanyConsignInfo(CustomerDO customerDO, CustomerCompanyDO customerCompanyDO, Date now, Integer currentUserId) {
        CustomerConsignInfoDO customerConsignInfoDO = new CustomerConsignInfoDO();
        customerConsignInfoDO.setCustomerId(customerDO.getId());
        customerConsignInfoDO.setConsigneeName(customerCompanyDO.getAgentPersonName());
        customerConsignInfoDO.setConsigneePhone(customerCompanyDO.getAgentPersonPhone());
        customerConsignInfoDO.setProvince(customerCompanyDO.getProvince());
        customerConsignInfoDO.setCity(customerCompanyDO.getCity());
        customerConsignInfoDO.setDistrict(customerCompanyDO.getDistrict());
        customerConsignInfoDO.setAddress(customerCompanyDO.getAddress());
        customerConsignInfoDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        customerConsignInfoDO.setCreateTime(now);
        customerConsignInfoDO.setCreateUser(currentUserId.toString());
        customerConsignInfoDO.setUpdateUser(currentUserId.toString());
        customerConsignInfoDO.setUpdateTime(now);

        customerConsignInfoMapper.save(customerConsignInfoDO);

        //调用新增企业地址信息方法，注释添加地址方法
        customerCompanyDO.setDefaultAddressReferId(customerConsignInfoDO.getId());

    }

    private void saveCustomerPersonConsignInfo(CustomerDO customerDO, CustomerPersonDO customerPersonDO, Date now, Integer currentUserId) {
        CustomerConsignInfo customerConsignInfo = new CustomerConsignInfo();
        customerConsignInfo.setCustomerId(customerDO.getId());
        customerConsignInfo.setCustomerNo(customerDO.getCustomerNo());
        customerConsignInfo.setConsigneeName(customerPersonDO.getRealName());
        customerConsignInfo.setConsigneePhone(customerPersonDO.getPhone());
        customerConsignInfo.setProvince(customerPersonDO.getProvince());
        customerConsignInfo.setCity(customerPersonDO.getCity());
        customerConsignInfo.setDistrict(customerPersonDO.getDistrict());
        customerConsignInfo.setAddress(customerPersonDO.getAddress());
        customerConsignInfo.setCreateTime(now);
        customerConsignInfo.setCreateUser(currentUserId.toString());
        customerConsignInfo.setUpdateUser(currentUserId.toString());
        customerConsignInfo.setUpdateTime(now);
        //调用新增企业地址信息方法，注释添加地址方法
        addCustomerConsignInfo(customerConsignInfo);
    }

    public ServiceResult<String,String> setCustomerCompanyNeed(List<CustomerCompanyNeed> customerCompanyNeedList) {
        ServiceResult<String,String> serviceResult = new ServiceResult<>();

        for (CustomerCompanyNeed customerCompanyNeed : customerCompanyNeedList) {
            ProductSkuDO productSkuDO = productSkuMapper.findById(customerCompanyNeed.getSkuId());
            if (productSkuDO == null){
                serviceResult.setErrorCode(ErrorCode.CUSTOMER_COMPANY_NEED_SKU_ID_NOT_NULL);
                return serviceResult;
            }
            customerCompanyNeed.setUnitPrice(productSkuDO.getSkuPrice());
            if (customerCompanyNeed.getRentCount() == null){
                serviceResult.setErrorCode(ErrorCode.CUSTOMER_COMPANY_NEED_RENT_COUNT_NOT_NULL);
                return serviceResult;
            }
            BigDecimal totalPrice = BigDecimalUtil.mul(productSkuDO.getSkuPrice(), new BigDecimal(customerCompanyNeed.getRentCount()));
            customerCompanyNeed.setTotalPrice(totalPrice);
            ServiceResult<String, Product> productServiceResult = productService.queryProductBySkuId(customerCompanyNeed.getSkuId());
            customerCompanyNeed.setProduct(productServiceResult.getResult());
        }

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }


    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private CustomerUpdateLogMapper customerUpdateLogMapper;

    @Autowired
    private ProductSkuMapper productSkuMapper;

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

    @Autowired
    private ProductService productService;

    @Autowired
    private SubCompanyMapper subCompanyMapper;

    @Autowired
    private PermissionSupport permissionSupport;

    @Autowired
    private WebServiceHelper webServiceHelper;
}