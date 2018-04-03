package com.lxzl.erp.core.service.customer.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lxzl.erp.common.cache.CommonCache;
import com.lxzl.erp.common.constant.*;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.customer.*;
import com.lxzl.erp.common.domain.customer.pojo.*;
import com.lxzl.erp.common.domain.payment.account.pojo.CustomerAccount;
import com.lxzl.erp.common.domain.product.pojo.Product;
import com.lxzl.erp.common.domain.system.pojo.Image;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.common.domain.workflow.pojo.WorkflowLink;
import com.lxzl.erp.common.domain.workflow.pojo.WorkflowLinkDetail;
import com.lxzl.erp.common.domain.workflow.pojo.WorkflowVerifyUserGroup;
import com.lxzl.erp.common.util.*;
import com.lxzl.erp.core.service.basic.impl.support.GenerateNoSupport;
import com.lxzl.erp.core.service.customer.CustomerService;
import com.lxzl.erp.core.service.k3.WebServiceHelper;
import com.lxzl.erp.core.service.payment.PaymentService;
import com.lxzl.erp.core.service.permission.PermissionSupport;
import com.lxzl.erp.core.service.product.ProductService;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.core.service.workflow.WorkflowService;
import com.lxzl.erp.dataaccess.dao.mysql.company.SubCompanyCityCoverMapper;
import com.lxzl.erp.dataaccess.dao.mysql.company.SubCompanyMapper;
import com.lxzl.erp.dataaccess.dao.mysql.customer.*;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductSkuMapper;
import com.lxzl.erp.dataaccess.dao.mysql.system.ImgMysqlMapper;
import com.lxzl.erp.dataaccess.dao.mysql.user.UserMapper;
import com.lxzl.erp.dataaccess.domain.company.SubCompanyCityCoverDO;
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

@Service
public class CustomerServiceImpl implements CustomerService {
    private static final Logger logger = LoggerFactory.getLogger(ConverterUtil.class);

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, String> addCompany(Customer customer) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        Date now = new Date();
        CustomerCompany customerCompany = customer.getCustomerCompany();
        //将公司客户名带括号的，全角中文，半角中文，英文括号，统一转为（这种括号格式）
        customerCompany.setCompanyName(StrReplaceUtil.replaceAll(customerCompany.getCompanyName()));

        //将公司客户名称中所有除了中文，英文字母（大小写）的字符全部去掉
        String simpleCompanyName = StrReplaceUtil.nameToSimple(customerCompany.getCompanyName());
        CustomerCompanyDO ccdo = customerCompanyMapper.findBySimpleCompanyName(simpleCompanyName);

        //该公司简单名称已经存在，则返回错误代码信息
        if (ccdo != null) {
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_IS_EXISTS);
            return serviceResult;
        }
        //如果是否为法人代表申请，为是
        if (customerCompany.getIsLegalPersonApple() == 1) {
            customerCompany.setLegalPerson(customerCompany.getAgentPersonName());
            customerCompany.setLegalPersonNo(customerCompany.getAgentPersonNo());
            customerCompany.setLegalPersonPhone(customerCompany.getAgentPersonPhone());
        }
        //校验法人手机号,经办人电话,紧急联系人手机号
        if (customerCompany.getIsLegalPersonApple() == 0) {

            if (customerCompany.getLegalPerson() != null) {
                if (customerCompany.getAgentPersonName().equals(customerCompany.getLegalPerson())) {
                    serviceResult.setErrorCode(ErrorCode.LEGAL_PERSON_NAME_EQUAL_TO_LEGAL_PERSON_NAME);
                    return serviceResult;
                }
            }

            if (customerCompany.getLegalPersonNo() != null) {
                if (customerCompany.getAgentPersonNo().equals(customerCompany.getLegalPersonNo())) {
                    serviceResult.setErrorCode(ErrorCode.LEGAL_PERSON_NO_EQUAL_TO_LEGAL_PERSON_NO);
                    return serviceResult;
                }
            }
            if (customerCompany.getLegalPersonPhone() != null) {
                if (customerCompany.getLegalPersonPhone().equals(customerCompany.getAgentPersonPhone())) {
                    serviceResult.setErrorCode(ErrorCode.LEGAL_PERSON_PHONE_EQUAL_TO_AGENT_PERSON_PHONE);
                    return serviceResult;
                }
            }

            if (customerCompany.getConnectPhone() != null) {
                if (customerCompany.getAgentPersonPhone().equals(customerCompany.getConnectPhone())) {
                    serviceResult.setErrorCode(ErrorCode.AGENT_PERSON_PHONE_EQUAL_TO_CONNECT_PHONE);
                    return serviceResult;
                }
            }
        }

        if (CollectionUtil.isEmpty(customerCompany.getCustomerConsignInfoList())) {
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_CONSIGN_INFO_IS_NOT_NULL);
            return serviceResult;
        }

        //判断业务员和联合开发员
        serviceResult = judgeUserOwnerAndUnion(customer);
        if (!ErrorCode.SUCCESS.equals(serviceResult.getErrorCode())) {
            serviceResult.setErrorCode(serviceResult.getErrorCode());
            return serviceResult;
        }

        boolean flag = false;
        if (CommonConstant.COMMON_CONSTANT_YES.equals(customer.getIsDefaultConsignAddress())) {
            for (CustomerConsignInfo customerConsignInfo : customer.getCustomerCompany().getCustomerConsignInfoList()) {
                if (CommonConstant.COMMON_CONSTANT_YES.equals(customerConsignInfo.getIsBusinessAddress())) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                serviceResult.setErrorCode(ErrorCode.CUSTOMER_CONSIGN_INFO_IS_BUSINESS_ADDRESS_NOT_EXISTS);
                return serviceResult;
            }
        }

        CustomerDO customerDO = ConverterUtil.convert(customer, CustomerDO.class);
        //保存业务员所属分公司
        Integer ownerSubCompanyId = userSupport.getCompanyIdByUser(customerDO.getOwner());
        customerDO.setOwnerSubCompanyId(ownerSubCompanyId);
        customerDO.setCustomerNo(generateNoSupport.generateCustomerNo(now, CustomerType.CUSTOMER_TYPE_COMPANY));
        customerDO.setCustomerType(CustomerType.CUSTOMER_TYPE_COMPANY);
        customerDO.setIsDisabled(CommonConstant.COMMON_CONSTANT_NO);
        customerDO.setCustomerName(customerCompany.getCompanyName());
        customerDO.setCustomerStatus(CustomerStatus.STATUS_INIT);
        customerDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        customerDO.setCreateTime(now);
        customerDO.setUpdateTime(now);
        customerDO.setCreateUser(userSupport.getCurrentUserId().toString());
        customerDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        customerDO.setOwner(customer.getOwner());
        customerDO.setUnionUser(customer.getUnionUser());
        customerMapper.save(customerDO);


        //用于记录客户所需设备的计算结果
        ServiceResult<String, BigDecimal> setServiceResult = new ServiceResult<>();
        CustomerCompanyDO customerCompanyDO = ConverterUtil.convert(customer.getCustomerCompany(), CustomerCompanyDO.class);

        //设置首次所需设备
        if (CollectionUtil.isNotEmpty(customer.getCustomerCompany().getCustomerCompanyNeedFirstList())) {
            List<CustomerCompanyNeed> customerCompanyNeedFirstList = customer.getCustomerCompany().getCustomerCompanyNeedFirstList();
            //记录所有首次设备的总金额
            setServiceResult = setCustomerCompanyNeed(customerCompanyNeedFirstList);
            if (!ErrorCode.SUCCESS.equals(setServiceResult.getErrorCode())) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                serviceResult.setErrorCode(setServiceResult.getErrorCode());
                return serviceResult;
            }
            customerCompanyDO.setCustomerCompanyNeedFirstJson(JSON.toJSON(customerCompanyNeedFirstList).toString());
        }

        //判断后续所需设备
        if (CollectionUtil.isNotEmpty(customer.getCustomerCompany().getCustomerCompanyNeedLaterList())) {
            List<CustomerCompanyNeed> customerCompanyNeedLaterList = customer.getCustomerCompany().getCustomerCompanyNeedLaterList();
            //记录所有后续设备的总金额
            setServiceResult = setCustomerCompanyNeed(customerCompanyNeedLaterList);
            if (!ErrorCode.SUCCESS.equals(setServiceResult.getErrorCode())) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                serviceResult.setErrorCode(setServiceResult.getErrorCode());
                return serviceResult;
            }
            customerCompanyDO.setCustomerCompanyNeedLaterJson(JSON.toJSON(customerCompanyNeedLaterList).toString());
        }

        for (CustomerConsignInfoDO customerConsignInfoDO : customerCompanyDO.getCustomerConsignInfoList()) {
            if (CommonConstant.COMMON_CONSTANT_YES.equals(customerConsignInfoDO.getIsMain())) {
                //修改该客户下所有的默认地址的状态为0
                customerConsignInfoMapper.clearIsMainByCustomerId(customerDO.getId());
            }
            customerConsignInfoDO.setCustomerId(customerDO.getId());
            customerConsignInfoDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
            customerConsignInfoDO.setVerifyStatus(CustomerConsignVerifyStatus.VERIFY_STATUS_PENDING);
            customerConsignInfoDO.setCreateTime(now);
            customerConsignInfoDO.setCreateUser(userSupport.getCurrentUserId().toString());
            customerConsignInfoMapper.save(customerConsignInfoDO);
            if (CommonConstant.COMMON_CONSTANT_YES.equals(customerConsignInfoDO.getIsBusinessAddress())) {
                customerCompanyDO.setDefaultAddressReferId(customerConsignInfoDO.getId());
            }
        }

        customerCompanyDO.setCustomerId(customerDO.getId());
        customerCompanyDO.setCustomerNo(customerDO.getCustomerNo());
        customerCompanyDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        customerCompanyDO.setAddressVerifyStatus(CustomerConsignVerifyStatus.VERIFY_STATUS_PENDING);
        customerCompanyDO.setCreateTime(now);
        customerCompanyDO.setUpdateTime(now);
        //将公司简单名称添加存入实体类
        customerCompanyDO.setSimpleCompanyName(simpleCompanyName);
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

        webServiceHelper.post(PostK3OperatorType.POST_K3_OPERATOR_TYPE_NULL, PostK3Type.POST_K3_TYPE_CUSTOMER, ConverterUtil.convert(customerDO, Customer.class), true);
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
        //判断业务员和联合开发员
        serviceResult = judgeUserOwnerAndUnion(customer);
        if (!ErrorCode.SUCCESS.equals(serviceResult.getErrorCode())) {
            serviceResult.setErrorCode(serviceResult.getErrorCode());
            return serviceResult;
        }

        //判断紧急联系人和紧急联系人电话，不能和个人客户相同
        if (customer.getCustomerPerson().getConnectRealName() != null) {
            if (customer.getCustomerPerson().getConnectRealName().equals(customer.getCustomerPerson().getRealName())) {
                serviceResult.setErrorCode(ErrorCode.CUSTOMER_PERSON_CONNECT_REAL_NAME_NOT_MATCH_REAL_NAME);
                return serviceResult;
            }
        }
        if (customer.getCustomerPerson().getConnectPhone() != null) {
            if (customer.getCustomerPerson().getConnectPhone().equals(customer.getCustomerPerson().getPhone())) {
                serviceResult.setErrorCode(ErrorCode.CUSTOMER_PERSON_CONNECT_PHONE_NOT_MATCH_PHONE);
                return serviceResult;
            }
        }

        CustomerDO customerDO = ConverterUtil.convert(customer, CustomerDO.class);
        //保存业务员所属分公司
        Integer ownerSubCompanyId = userSupport.getCompanyIdByUser(customerDO.getOwner());
        customerDO.setOwnerSubCompanyId(ownerSubCompanyId);
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
        webServiceHelper.post(PostK3OperatorType.POST_K3_OPERATOR_TYPE_NULL, PostK3Type.POST_K3_TYPE_CUSTOMER, ConverterUtil.convert(customerDO, Customer.class), true);
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
        //将公司客户名带括号的，全角中文，半角中文，英文括号，统一转为（这种括号格式
        customerCompany.setCompanyName(StrReplaceUtil.replaceAll(customerCompany.getCompanyName()));

        //校验是否有公司和是否为公司客户
        CustomerDO customerDO = customerMapper.findCustomerCompanyByNo(customer.getCustomerNo());
        if (customerDO == null || !CustomerType.CUSTOMER_TYPE_COMPANY.equals(customerDO.getCustomerType())) {
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_NOT_EXISTS);
            return serviceResult;
        }


        //校验当前是否是跟单员,联合开发人,创建人
        serviceResult = verifyJurisdiction(customerDO);
        if (!ErrorCode.SUCCESS.equals(serviceResult.getErrorCode())) {
            return serviceResult;
        }

        //如果是否为法人代表申请，为是
        if (customerCompany.getIsLegalPersonApple() == 1) {
            customerCompany.setLegalPerson(customerCompany.getAgentPersonName());
            customerCompany.setLegalPersonNo(customerCompany.getAgentPersonNo());
            customerCompany.setLegalPersonPhone(customerCompany.getAgentPersonPhone());
        }

        //如果是否为法人代表申请，为否
        if (customerCompany.getIsLegalPersonApple() == 0) {
            if (customerCompany.getLegalPerson() != null) {
                if (customerCompany.getAgentPersonName().equals(customerCompany.getLegalPerson())) {
                    serviceResult.setErrorCode(ErrorCode.LEGAL_PERSON_NAME_EQUAL_TO_LEGAL_PERSON_NAME);
                    return serviceResult;
                }
            }

            if (customerCompany.getLegalPersonNo() != null) {
                if (customerCompany.getAgentPersonNo().equals(customerCompany.getLegalPersonNo())) {
                    serviceResult.setErrorCode(ErrorCode.LEGAL_PERSON_NO_EQUAL_TO_LEGAL_PERSON_NO);
                    return serviceResult;
                }
            }
            if (customerCompany.getLegalPersonPhone() != null) {
                if (customerCompany.getLegalPersonPhone().equals(customerCompany.getAgentPersonPhone())) {
                    serviceResult.setErrorCode(ErrorCode.LEGAL_PERSON_PHONE_EQUAL_TO_AGENT_PERSON_PHONE);
                    return serviceResult;
                }
            }

            if (customerCompany.getConnectPhone() != null) {
                if (customerCompany.getAgentPersonPhone().equals(customerCompany.getConnectPhone())) {
                    serviceResult.setErrorCode(ErrorCode.AGENT_PERSON_PHONE_EQUAL_TO_CONNECT_PHONE);
                    return serviceResult;
                }
            }

            if (CollectionUtil.isEmpty(customerCompany.getCustomerConsignInfoList())) {
                if (customerCompany.getAgentPersonPhone().equals(customerCompany.getConnectPhone())) {
                    serviceResult.setErrorCode(ErrorCode.CUSTOMER_CONSIGN_INFO_IS_NOT_NULL);
                    return serviceResult;
                }
            }
        }
        if (CommonConstant.COMMON_CONSTANT_NO.equals(customer.getIsDefaultConsignAddress())) {
            if (CollectionUtil.isEmpty(customerCompany.getCustomerConsignInfoList())) {
                if (customerCompany.getAgentPersonPhone().equals(customerCompany.getConnectPhone())) {
                    serviceResult.setErrorCode(ErrorCode.CUSTOMER_CONSIGN_INFO_IS_NOT_NULL);
                    return serviceResult;
                }
            }
        }

        CustomerDO dbCustomerDO = customerMapper.findByName(customerCompany.getCompanyName());
        if (dbCustomerDO != null && !dbCustomerDO.getCustomerNo().equals(customer.getCustomerNo())) {
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_IS_EXISTS);
            return serviceResult;
        }

        if (CustomerStatus.STATUS_COMMIT.equals(customerDO.getCustomerStatus())
                || CustomerStatus.STATUS_PASS.equals(customerDO.getCustomerStatus())) {
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_CAN_NOT_EDIT);
            return serviceResult;
        }

        //判断业务员和联合开发员
        serviceResult = judgeUserOwnerAndUnion(customer);
        if (!ErrorCode.SUCCESS.equals(serviceResult.getErrorCode())) {
            serviceResult.setErrorCode(serviceResult.getErrorCode());
            return serviceResult;
        }

        boolean flag = false;
        if (CommonConstant.COMMON_CONSTANT_YES.equals(customer.getIsDefaultConsignAddress())) {
            for (CustomerConsignInfo customerConsignInfo : customer.getCustomerCompany().getCustomerConsignInfoList()) {
                if (CommonConstant.COMMON_CONSTANT_YES.equals(customerConsignInfo.getIsBusinessAddress())) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                serviceResult.setErrorCode(ErrorCode.CUSTOMER_CONSIGN_INFO_IS_BUSINESS_ADDRESS_NOT_EXISTS);
                return serviceResult;
            }
        }


        CustomerCompanyDO customerCompanyDO = customerCompanyMapper.findByCustomerId(customerDO.getId());
        CustomerCompanyDO newCustomerCompanyDO = ConverterUtil.convert(customer.getCustomerCompany(), CustomerCompanyDO.class);

        List<CustomerConsignInfo> customerConsignInfoList = customer.getCustomerCompany().getCustomerConsignInfoList();
        List<CustomerConsignInfoDO> customerConsignInfoDOList = ConverterUtil.convertList(customerConsignInfoList, CustomerConsignInfoDO.class);
        ServiceResult<String, CustomerConsignInfoDO> customerConsignInfoDOServiceResult = updateCustomerConsignInfoDOInfo(customerConsignInfoDOList, customerDO.getId(), now, newCustomerCompanyDO);
        if (!ErrorCode.SUCCESS.equals(customerConsignInfoDOServiceResult.getErrorCode())) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            serviceResult.setErrorCode(customerConsignInfoDOServiceResult.getErrorCode());
            return serviceResult;
        }
        //如果经营地址改变了，状态改为未提交，需要重新审核！
        if(!customerCompanyDO.getCity().equals(newCustomerCompanyDO.getCity())
                || !customerCompanyDO.getAddress().equals(newCustomerCompanyDO.getAddress())){
            newCustomerCompanyDO.setAddressVerifyStatus(CustomerConsignVerifyStatus.VERIFY_STATUS_PENDING);
        }

        //用于接收所需设备的方法结果
        ServiceResult<String, BigDecimal> setServiceResult = new ServiceResult<>();
        //判断首次所需设备 list转json
        if (CollectionUtil.isNotEmpty(customer.getCustomerCompany().getCustomerCompanyNeedFirstList())) {
            List<CustomerCompanyNeed> customerCompanyNeedFirstList = customer.getCustomerCompany().getCustomerCompanyNeedFirstList();
            //记录所有所有设备的总金额
            setServiceResult = setCustomerCompanyNeed(customerCompanyNeedFirstList);
            if (!ErrorCode.SUCCESS.equals(setServiceResult.getErrorCode())) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                serviceResult.setErrorCode(setServiceResult.getErrorCode());
                return serviceResult;
            }
            newCustomerCompanyDO.setCustomerCompanyNeedFirstJson(JSON.toJSON(customerCompanyNeedFirstList).toString());
        } else {
            newCustomerCompanyDO.setCustomerCompanyNeedFirstJson("");
        }

        //判断后续所需设备
        if (CollectionUtil.isNotEmpty(customerCompany.getCustomerCompanyNeedLaterList())) {
            List<CustomerCompanyNeed> customerCompanyNeedLaterList = customerCompany.getCustomerCompanyNeedLaterList();
            //记录所有后续设备的总金额
            setServiceResult = setCustomerCompanyNeed(customerCompanyNeedLaterList);
            if (!ErrorCode.SUCCESS.equals(setServiceResult.getErrorCode())) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                serviceResult.setErrorCode(setServiceResult.getErrorCode());
                return serviceResult;
            }
            newCustomerCompanyDO.setCustomerCompanyNeedLaterJson(JSON.toJSON(customerCompanyNeedLaterList).toString());
        } else {
            newCustomerCompanyDO.setCustomerCompanyNeedLaterJson("");
        }
        newCustomerCompanyDO.setCompanyName(null);
        newCustomerCompanyDO.setSimpleCompanyName(null);
        newCustomerCompanyDO.setDataStatus(null);
        newCustomerCompanyDO.setCreateTime(null);
        newCustomerCompanyDO.setCreateUser(null);
        newCustomerCompanyDO.setUpdateTime(now);
        newCustomerCompanyDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        newCustomerCompanyDO.setId(customerCompanyDO.getId());

        customerCompanyMapper.update(newCustomerCompanyDO);

        //对经办人身份证正面操作
        List<Image> agentPersonNoPictureFrontImageList = new ArrayList<>();
        agentPersonNoPictureFrontImageList.add(customerCompany.getAgentPersonNoPictureFrontImage());
        serviceResult = updateImage(agentPersonNoPictureFrontImageList, ImgType.AGENT_PERSON_NO_PICTURE_FRONT_IMG_TYPE, customerDO.getId().toString(), userSupport.getCurrentUserId().toString(), now);
        if (!ErrorCode.SUCCESS.equals(serviceResult.getErrorCode())) {
            serviceResult.setErrorCode(serviceResult.getErrorCode(), serviceResult.getFormatArgs());
            return serviceResult;
        }

        //对经办人身份证反面操作
        List<Image> agentPersonNoPictureBackImageList = new ArrayList<>();
        agentPersonNoPictureBackImageList.add(customerCompany.getAgentPersonNoPictureBackImage());
        serviceResult = updateImage(agentPersonNoPictureBackImageList, ImgType.AGENT_PERSON_NO_PICTURE_BACK_IMG_TYPE, customerDO.getId().toString(), userSupport.getCurrentUserId().toString(), now);
        if (!ErrorCode.SUCCESS.equals(serviceResult.getErrorCode())) {
            serviceResult.setErrorCode(serviceResult.getErrorCode(), serviceResult.getFormatArgs());
            return serviceResult;
        }


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
        serviceResult = updateCustomerOwnerAndUnionUser(customerDO, customer, now);
        if (!ErrorCode.SUCCESS.equals(serviceResult.getErrorCode())) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            serviceResult.setErrorCode(serviceResult.getErrorCode(), serviceResult.getFormatArgs());
            return serviceResult;
        }

        //更改短租应收上限
        customerDO.setShortLimitReceivableAmount(customer.getShortLimitReceivableAmount());
        //更改发货方式
        customerDO.setDeliveryMode(customer.getDeliveryMode());

        //更改业务员所属分公司ID
        Integer ownerSubCompanyId = userSupport.getCompanyIdByUser(customerDO.getOwner());
        customerDO.setOwnerSubCompanyId(ownerSubCompanyId);
        customerDO.setIsDisabled(null);
        customerDO.setCustomerStatus(CustomerStatus.STATUS_INIT);
        customerDO.setCustomerName(null);
        customerDO.setFirstApplyAmount(customer.getFirstApplyAmount());
        customerDO.setLaterApplyAmount(customer.getLaterApplyAmount());
        customerDO.setRemark(customer.getRemark());
        customerDO.setUpdateTime(now);
        customerDO.setUpdateUser(userSupport.getCurrentUserId().toString());
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

        //校验当前是否是跟单员,联合开发人,创建人
        serviceResult = verifyJurisdiction(customerDO);
        if (!ErrorCode.SUCCESS.equals(serviceResult.getErrorCode())) {
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
        if (!ErrorCode.SUCCESS.equals(serviceResult.getErrorCode())) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            serviceResult.setErrorCode(serviceResult.getErrorCode());
            return serviceResult;
        }

        //判断紧急联系人和紧急联系人电话，不能和个人客户相同
        if (customer.getCustomerPerson().getConnectRealName() != null) {
            if (customer.getCustomerPerson().getConnectRealName().equals(customer.getCustomerPerson().getRealName())) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                serviceResult.setErrorCode(ErrorCode.CUSTOMER_PERSON_CONNECT_REAL_NAME_NOT_MATCH_REAL_NAME);
                return serviceResult;
            }
        }
        if (customer.getCustomerPerson().getConnectPhone() != null) {
            if (customer.getCustomerPerson().getConnectPhone().equals(customer.getCustomerPerson().getPhone())) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                serviceResult.setErrorCode(ErrorCode.CUSTOMER_PERSON_CONNECT_PHONE_NOT_MATCH_PHONE);
                return serviceResult;
            }
        }


        //更改开发员和联合开发员
        serviceResult = updateCustomerOwnerAndUnionUser(customerDO, customer, now);
        if (!ErrorCode.SUCCESS.equals(serviceResult.getErrorCode())) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            serviceResult.setErrorCode(serviceResult.getErrorCode(), serviceResult.getFormatArgs());
            return serviceResult;
        }

        //更改短租应收上限
        customerDO.setShortLimitReceivableAmount(customer.getShortLimitReceivableAmount());
        //更改发货方式
        //customerDO.setDeliveryMode(customer.getDeliveryMode());
        //更改业务员所属分公司ID
        Integer ownerSubCompanyId = userSupport.getCompanyIdByUser(customerDO.getOwner());
        customerDO.setOwnerSubCompanyId(ownerSubCompanyId);

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

        ServiceResult<String, String> serviceResult = verifyCommitCustomerData(customerDO);
        if (!ErrorCode.SUCCESS.equals(serviceResult.getErrorCode())) {
            result.setErrorCode(serviceResult.getErrorCode());
            return result;
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

        if (CustomerStatus.STATUS_REJECT.equals(customerStatus)) {
            customerDO.setFailReason(verifyRemark);
            customerDO.setPassReason(null);
        }

        if (CustomerStatus.STATUS_PASS.equals(customerStatus)) {
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
        if (customerDO == null) {
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
        if (customerDO == null) {
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
        //将公司客户名带括号的，全角中文，半角中文，英文括号，统一转为（这种括号格式
        customerCompanyQueryParam.setCompanyName(StrReplaceUtil.replaceAll(customerCompanyQueryParam.getCompanyName()));
        maps.put("customerCompanyQueryParam", customerCompanyQueryParam);
        maps.put("permissionParam", permissionSupport.getPermissionParam(PermissionType.PERMISSION_TYPE_USER));
        Integer totalCount = customerMapper.findCustomerCompanyCountByParams(maps);
        List<CustomerDO> customerDOList = customerMapper.findCustomerCompanyByParams(maps);
        if (CollectionUtil.isNotEmpty(customerDOList)) {
            for (CustomerDO customerDO : customerDOList) {
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
        if (CollectionUtil.isNotEmpty(customerDOList)) {
            for (CustomerDO customerDO : customerDOList) {
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

        List<CustomerConsignInfoDO> customerConsignInfoDO = customerConsignInfoMapper.findByCustomerId(customerDO.getId());
        customerDO.getCustomerCompanyDO().setCustomerConsignInfoList(customerConsignInfoDO);

        if (!authorityControl(customerDO)) {
            serviceResult.setErrorCode(ErrorCode.DATA_HAVE_NO_PERMISSION);
            return serviceResult;
        }

        //如果当前用户不是跟单员  并且 用户不是联合开发人 并且用户不是创建人,屏蔽手机，座机字段
        processCustomerPhone(customerDO);
        CustomerAccount customerAccount = paymentService.queryCustomerAccount(customerDO.getCustomerNo());
        Customer customerResult = ConverterUtil.convert(customerDO, Customer.class);
        customerResult.setCustomerAccount(customerAccount);

        //显示联合开发员的省，市，区
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
            List<CustomerCompanyNeed> customerCompanyNeedList = JSONObject.parseArray(customerCompanyNeedFirstJson, CustomerCompanyNeed.class);
            BigDecimal firstListTotalPrice = new BigDecimal(0);
            for (CustomerCompanyNeed customerCompanyNeed : customerCompanyNeedList) {
                firstListTotalPrice = firstListTotalPrice.add(customerCompanyNeed.getTotalPrice());
            }
            customerResult.getCustomerCompany().setFirstListTotalPrice(firstListTotalPrice);
            customerResult.getCustomerCompany().setCustomerCompanyNeedFirstList(JSONObject.parseArray(customerCompanyNeedFirstJson, CustomerCompanyNeed.class));
        }

        //后续所需设备，Json转list
        if (StringUtil.isNotEmpty(customerDO.getCustomerCompanyDO().getCustomerCompanyNeedLaterJson())) {
            String customerCompanyNeedLaterList = customerDO.getCustomerCompanyDO().getCustomerCompanyNeedLaterJson();
            List<CustomerCompanyNeed> customerCompanyNeedList = JSONObject.parseArray(customerCompanyNeedLaterList, CustomerCompanyNeed.class);
            BigDecimal laterListTotalPrice = new BigDecimal(0);
            for (CustomerCompanyNeed customerCompanyNeed : customerCompanyNeedList) {
                laterListTotalPrice = laterListTotalPrice.add(customerCompanyNeed.getTotalPrice());
            }
            customerResult.getCustomerCompany().setLaterListTotalPrice(laterListTotalPrice);
            customerResult.getCustomerCompany().setCustomerCompanyNeedLaterList(JSONObject.parseArray(customerCompanyNeedLaterList, CustomerCompanyNeed.class));
        }

        //加入默认地址关联ID
        if ((customerDO.getCustomerCompanyDO().getDefaultAddressReferId() != null)) {
            customerResult.setIsDefaultConsignAddress(CommonConstant.COMMON_CONSTANT_YES);
        } else {
            customerResult.setIsDefaultConsignAddress(CommonConstant.COMMON_CONSTANT_NO);
        }

        //开始加入附件
        //加入经办人身份证正面照片
        List<ImageDO> agentPersonNoPictureFrontImageList = imgMysqlMapper.findByRefIdAndType(customerDO.getId().toString(), ImgType.AGENT_PERSON_NO_PICTURE_FRONT_IMG_TYPE);
        if (CollectionUtil.isNotEmpty(agentPersonNoPictureFrontImageList)) {
            Image agentPersonNoPictureFrontImage = ConverterUtil.convert(agentPersonNoPictureFrontImageList.get(0), Image.class);
            customerResult.getCustomerCompany().setAgentPersonNoPictureFrontImage(agentPersonNoPictureFrontImage);
        }

        //加入经办人身份证反面照片
        List<ImageDO> agentPersonNoPictureBackImageList = imgMysqlMapper.findByRefIdAndType(customerDO.getId().toString(), ImgType.AGENT_PERSON_NO_PICTURE_BACK_IMG_TYPE);
        if (CollectionUtil.isNotEmpty(agentPersonNoPictureBackImageList)) {
            Image agentPersonNoPictureBackImage = ConverterUtil.convert(agentPersonNoPictureBackImageList.get(0), Image.class);
            customerResult.getCustomerCompany().setAgentPersonNoPictureBackImage(agentPersonNoPictureBackImage);
        }

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
            Integer companyId = userSupport.getCompanyIdByUser(customerResult.getOwner());
            SubCompanyDO subCompanyDO = subCompanyMapper.findById(companyId);
            customerResult.setCustomerArea(subCompanyDO.getSubCompanyName());
        }
        if (customerResult.getUnionUser() != null) {
            customerResult.setCustomerUnionUser(CommonCache.userMap.get(customerResult.getUnionUser()));
        }

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(customerResult);
        return serviceResult;
    }

    private String hidePhone(String phone) {

        String hidePhone = "";
        if (StringUtil.isBlank(phone)) {
            return hidePhone;
        }
        phone = phone.trim();
        if (phone.length() == 11) {
            //手机号码屏蔽中间四位
            hidePhone = phone.substring(0, 3) + "****" + phone.substring(7, 11);
        } else if (phone.length() > 4) {
            //非手机号码屏蔽最后四位
            hidePhone = phone.substring(0, phone.length() - 4) + "****";
        }
        return hidePhone;
    }

    private void processCustomerPhone(CustomerDO customerDO) {
        if (!userSupport.getCurrentUserId().equals(customerDO.getOwner()) &&
                !userSupport.getCurrentUserId().equals(customerDO.getUnionUser()) &&
                !userSupport.getCurrentUserId().equals(Integer.parseInt(customerDO.getCreateUser())) &&
                !userSupport.isSuperUser()) {
            CustomerCompanyDO customerCompanyDO = customerDO.getCustomerCompanyDO();
            if (customerCompanyDO != null) {
                customerCompanyDO.setConnectPhone(hidePhone(customerCompanyDO.getConnectPhone()));
                customerCompanyDO.setAgentPersonPhone(hidePhone(customerCompanyDO.getAgentPersonPhone()));
                customerCompanyDO.setLegalPersonPhone(hidePhone(customerCompanyDO.getLegalPersonPhone()));
                customerCompanyDO.setLandline(hidePhone(customerCompanyDO.getLandline()));
//                customerCompanyDO.setConnectPhone(null);
//                customerCompanyDO.setAgentPersonPhone(null);
//                customerCompanyDO.setLegalPersonPhone(null);
//                customerCompanyDO.setLandline(null);
            }
            CustomerPersonDO customerPersonDO = customerDO.getCustomerPersonDO();
            if (customerPersonDO != null) {
                customerPersonDO.setPhone(hidePhone(customerPersonDO.getPhone()));
                customerPersonDO.setConnectPhone(hidePhone(customerPersonDO.getConnectPhone()));
//                customerPersonDO.setPhone(null);
//                customerPersonDO.setConnectPhone(null);
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
            List<CustomerConsignInfoDO> customerConsignInfoDO = customerConsignInfoMapper.findByCustomerId(customerDO.getId());
            customerDO.getCustomerCompanyDO().setCustomerConsignInfoList(customerConsignInfoDO);
        } else if (CustomerType.CUSTOMER_TYPE_PERSON.equals(customerDO.getCustomerType())) {
            customerDO = customerMapper.findCustomerPersonByNo(customerNo);
            List<CustomerConsignInfoDO> customerConsignInfoDO = customerConsignInfoMapper.findByCustomerId(customerDO.getId());
            customerDO.getCustomerPersonDO().setCustomerConsignInfoDOList(customerConsignInfoDO);
        }
        CustomerAccount customerAccount = paymentService.queryCustomerAccount(customerDO.getCustomerNo());

        if (!authorityControl(customerDO)) {
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

        if (customerDO.getCustomerCompanyDO() != null) {
            //加入默认地址关联ID
            if (customerDO.getCustomerCompanyDO().getDefaultAddressReferId() != null) {
                customerResult.setIsDefaultConsignAddress(CommonConstant.COMMON_CONSTANT_YES);
            } else {
                customerResult.setIsDefaultConsignAddress(CommonConstant.COMMON_CONSTANT_NO);
            }
        }

        if (customerResult.getOwner() != null) {
            customerResult.setCustomerOwnerUser(CommonCache.userMap.get(customerResult.getOwner()));
            Integer companyId = userSupport.getCompanyIdByUser(customerResult.getOwner());
            SubCompanyDO subCompanyDO = subCompanyMapper.findById(companyId);
            customerResult.setCustomerArea(subCompanyDO.getSubCompanyName());
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

        if (customerDO.getCustomerCompanyDO() != null) {
            //加入默认地址关联ID
            if (customerDO.getCustomerCompanyDO().getDefaultAddressReferId() != null) {
                customerResult.setIsDefaultConsignAddress(CommonConstant.COMMON_CONSTANT_YES);
            } else {
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

    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, String> commitCustomerToWorkflow(CustomerCommitParam customerCommitParam) {
        ServiceResult<String, String> result = new ServiceResult<>();
        Date now = new Date();
        User loginUser = userSupport.getCurrentUser();
        CustomerDO customerDO = customerMapper.findByNo(customerCommitParam.getCustomerNo());
        if (customerDO == null) {
            result.setErrorCode(ErrorCode.CUSTOMER_NOT_EXISTS);
            return result;
        }

        List<CustomerConsignInfoDO> customerConsignInfoDOList = customerConsignInfoMapper.findByCustomerId(customerDO.getId());
        if (CollectionUtil.isEmpty(customerConsignInfoDOList)) {
            result.setErrorCode(ErrorCode.CUSTOMER_CONSIGN_NOT_EXISTS);
            return result;
        }
        //只有创建人和业务员和联合开发员才能提交功能
        if (!loginUser.getUserId().toString().equals(customerDO.getCreateUser()) &&
                !loginUser.getUserId().equals(customerDO.getOwner()) &&
                !loginUser.getUserId().equals(customerDO.getUnionUser()) &&
                !userSupport.isSuperUser()) {
            result.setErrorCode(ErrorCode.CUSTOMER_COMMIT_IS_CREATE_USER_AND_OWNER_AND_UNION_USER);
            return result;
        }

        if (!CustomerStatus.STATUS_INIT.equals(customerDO.getCustomerStatus()) && !CustomerStatus.STATUS_REJECT.equals(customerDO.getCustomerStatus())) {
            result.setErrorCode(ErrorCode.CUSTOMER_STATUS_ERROR);
            return result;
        }

        ServiceResult<String, String> serviceResult = verifyCommitCustomerData(customerDO);
        if (!ErrorCode.SUCCESS.equals(serviceResult.getErrorCode())) {
            result.setErrorCode(serviceResult.getErrorCode());
            return result;
        }

        ServiceResult<String, Boolean> needVerifyResult = workflowService.isNeedVerify(WorkflowType.WORKFLOW_TYPE_CUSTOMER);
        if (!ErrorCode.SUCCESS.equals(needVerifyResult.getErrorCode())) {
            result.setErrorCode(needVerifyResult.getErrorCode());
            return result;
        } else if (needVerifyResult.getResult()) {
            //调用提交审核服务
            if (CustomerType.CUSTOMER_TYPE_COMPANY.equals(customerDO.getCustomerType())) {
                customerCommitParam.setVerifyMatters("公司客户审核事项：1.申请额度 2.客户相关信息图片核对 3.统一信用码需信用网查询公司是否存在");
            } else {
                customerCommitParam.setVerifyMatters("个人客户审核事项：1.申请额度 2.客户相关信息图片核对");
            }

            ServiceResult<String, String> verifyResult = workflowService.commitWorkFlow(WorkflowType.WORKFLOW_TYPE_CUSTOMER, customerCommitParam.getCustomerNo(), customerCommitParam.getVerifyUserId(), customerCommitParam.getVerifyMatters(), customerCommitParam.getRemark(), customerCommitParam.getImgIdList(), null);
            //修改提交审核状态
            if (ErrorCode.SUCCESS.equals(verifyResult.getErrorCode())) {
                customerDO.setCustomerStatus(CustomerStatus.STATUS_COMMIT);
                customerDO.setUpdateUser(loginUser.getUserId().toString());
                customerDO.setUpdateTime(now);
                customerMapper.update(customerDO);
                return verifyResult;
            } else {
                result.setErrorCode(verifyResult.getErrorCode());
                return result;
            }
        } else {
            customerDO.setCustomerStatus(CustomerStatus.STATUS_PASS);
            customerDO.setUpdateUser(loginUser.getUserId().toString());
            customerDO.setUpdateTime(now);
            customerMapper.update(customerDO);
            result.setErrorCode(ErrorCode.SUCCESS);
            return result;
        }
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, String> rejectCustomer(CustomerRejectParam customerRejectParam) {
        ServiceResult<String, String> result = new ServiceResult<>();
        Date now = new Date();
        User loginUser = userSupport.getCurrentUser();
        CustomerDO customerDO = customerMapper.findByNo(customerRejectParam.getCustomerNo());
        if (customerDO == null) {
            result.setErrorCode(ErrorCode.CUSTOMER_NOT_EXISTS);
            return result;
        }

        if (!CustomerStatus.STATUS_PASS.equals(customerDO.getCustomerStatus())) {
            result.setErrorCode(ErrorCode.CUSTOMER_STATUS_IS_PASS_CAN_REJECT);
            return result;
        }

        ServiceResult<String, String> rejectPassResult = workflowService.rejectPassWorkFlow(WorkflowType.WORKFLOW_TYPE_CUSTOMER, customerRejectParam.getCustomerNo(), customerRejectParam.getRemark());
        if (!ErrorCode.SUCCESS.equals(rejectPassResult.getErrorCode())) {
            result.setErrorCode(rejectPassResult.getErrorCode());
            return result;
        }

        customerDO.setCustomerStatus(CustomerStatus.STATUS_REJECT);
        customerDO.setUpdateTime(now);
        customerDO.setUpdateUser(loginUser.getUserId().toString());
        customerMapper.update(customerDO);

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(customerDO.getCustomerNo());
        return result;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public String receiveVerifyResult(boolean verifyResult, String businessNo) {
        Date now = new Date();
        String data = businessNo.substring(0,2);
        CustomerDO customerDO = null;
        CustomerConsignInfoDO customerConsignInfoDO = null;
        if("LX".equals(data)){
            customerDO = customerMapper.findByNo(businessNo);
        }else{
            customerConsignInfoDO = customerConsignInfoMapper.findById(Integer.valueOf(businessNo));
        }
        try {
            if(customerDO != null){
                //不是审核中状态，拒绝处理
                if (!CustomerStatus.STATUS_COMMIT.equals(customerDO.getCustomerStatus())) {
                    return ErrorCode.BUSINESS_EXCEPTION;
                }
                if (verifyResult) {
                    customerDO.setCustomerStatus(CustomerStatus.STATUS_PASS);
                } else {
                    customerDO.setCustomerStatus(CustomerStatus.STATUS_REJECT);
                }
                customerDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                customerDO.setUpdateTime(now);
                customerMapper.update(customerDO);
                return ErrorCode.SUCCESS;
            }else if(customerConsignInfoDO != null){
                //不是审核中和初审通过状态，拒绝处理
                if (!CustomerConsignVerifyStatus.VERIFY_STATUS_COMMIT.equals(customerConsignInfoDO.getVerifyStatus())
                        && !CustomerConsignVerifyStatus.VERIFY_STATUS_FIRST_PASS.equals(customerConsignInfoDO.getVerifyStatus())) {
                    return ErrorCode.BUSINESS_EXCEPTION;
                }
                if (verifyResult) {
                    customerConsignInfoDO.setVerifyStatus(CustomerConsignVerifyStatus.VERIFY_STATUS_END_PASS);
                } else {
                    customerConsignInfoDO.setVerifyStatus(CustomerConsignVerifyStatus.VERIFY_STATUS_BACK);
                }
                customerConsignInfoDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                customerConsignInfoDO.setUpdateTime(now);
                customerConsignInfoMapper.update(customerConsignInfoDO);
                return ErrorCode.SUCCESS;
            }else{
                return ErrorCode.BUSINESS_EXCEPTION;
            }
        } catch (Exception e) {
            logger.error("【客户审核后，业务处理异常】", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            logger.error("【数据已回滚】");
            return ErrorCode.BUSINESS_EXCEPTION;
        }
    }


    boolean haveAuthority(Integer owner, Integer unionUser, Integer createUser) {
        List<Integer> dataAccessPassiveUserList = permissionSupport.getCanAccessPassiveUserList(userSupport.getCurrentUserId());
        return (!userSupport.getCurrentUserId().equals(owner) &&
                !userSupport.getCurrentUserId().equals(unionUser) &&
                !userSupport.getCurrentUserId().equals(createUser) &&
                !dataAccessPassiveUserList.contains(createUser) &&
                !dataAccessPassiveUserList.contains(owner) &&
                !dataAccessPassiveUserList.contains(unionUser) &&
                !userSupport.isSuperUser());
    }

    private boolean authorityControl(CustomerDO customerDO) {
        //判断审核人员给予权限观看
        boolean flag = false;
        ServiceResult<String, WorkflowLink> workflowLinkServiceResult = workflowService.getWorkflowLink(WorkflowType.WORKFLOW_TYPE_CUSTOMER, customerDO.getCustomerNo());
        if (ErrorCode.SUCCESS.equals(workflowLinkServiceResult.getErrorCode())) {
            List<WorkflowLinkDetail> workflowLinkDetailList = workflowLinkServiceResult.getResult().getWorkflowLinkDetailList();
            for (WorkflowLinkDetail workflowLinkDetail : workflowLinkDetailList) {
                for (WorkflowVerifyUserGroup workflowVerifyUserGroup : workflowLinkDetail.getWorkflowVerifyUserGroupList()) {
                    if (userSupport.getCurrentUserId().equals(workflowVerifyUserGroup.getVerifyUser())) {
                        flag = true;
                        break;
                    }
                }
            }
            if (!flag) {
                //如果当前用户是跟单员  并且 用户不是联合开发人 并且用户不是创建人  并且当前用户的可观察列表中不包含当前数据的创建人，则不允许看此条数据
                if (!haveAuthority(customerDO.getOwner(), customerDO.getUnionUser(), Integer.parseInt(customerDO.getCreateUser()))) {
                    flag = true;
                }
            }
        } else {
            //如果当前用户是跟单员  并且 用户不是联合开发人 并且用户不是创建人  并且当前用户的可观察列表中不包含当前数据的创建人，则不允许看此条数据
            if (!haveAuthority(customerDO.getOwner(), customerDO.getUnionUser(), Integer.parseInt(customerDO.getCreateUser()))) {
                flag = true;
            }
        }
        return flag;
    }


    @Override
    public ServiceResult<String, Customer> detailCustomerPerson(Customer customer) {
        ServiceResult<String, Customer> serviceResult = new ServiceResult<>();
        CustomerDO customerDO = customerMapper.findCustomerPersonByNo(customer.getCustomerNo());
        if (customerDO == null || !CustomerType.CUSTOMER_TYPE_PERSON.equals(customerDO.getCustomerType())) {
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_NOT_EXISTS);
            return serviceResult;
        }
        List<CustomerConsignInfoDO> customerConsignInfoDO = customerConsignInfoMapper.findByCustomerId(customerDO.getId());
        customerDO.getCustomerPersonDO().setCustomerConsignInfoDOList(customerConsignInfoDO);

        if (!authorityControl(customerDO)) {
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
            Integer companyId = userSupport.getCompanyIdByUser(customerResult.getOwner());
            SubCompanyDO subCompanyDO = subCompanyMapper.findById(companyId);
            customerResult.setCustomerArea(subCompanyDO.getSubCompanyName());
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
        //校验风控信息
        ServiceResult<String, String> result = verifyRiskManagement(customerRiskManagement);
        if (!ErrorCode.SUCCESS.equals(result.getErrorCode())) {
            return result;
        }

        Date now = new Date();
        //判断客户审核状态(如果为驳回状态则不可修改创建分控信息)
        if (customerDO.getCustomerStatus() == 3) {
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_REJECT_CAN_NOT_EDIT_RISK_MANAGEMENT);
            return serviceResult;
        }
        if (customerDO.getCustomerStatus() == 0) {
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_UNCOMMITTED_CAN_NOT_EDIT_RISK_MANAGEMENT);
            return serviceResult;
        }

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

            CustomerRiskManagementHistoryDO customerRiskManagementHistoryDO = ConverterUtil.convert(customerRiskManagementDO, CustomerRiskManagementHistoryDO.class);
            customerRiskManagementHistoryDO.setCustomerNo(customerDO.getCustomerNo());
            customerRiskManagementHistoryMapper.save(customerRiskManagementHistoryDO);

        } else {//有风控信息则修改
            //在执行更改方法前，进行原数据和更改传入数据的不同判断
            CustomerRiskManagementDO customerRiskManagementDO = ConverterUtil.convert(customerRiskManagement, CustomerRiskManagementDO.class);
            boolean judgeResult = customerDO.getCustomerRiskManagementDO().equals(customerRiskManagementDO);

            CustomerRiskManagementDO customerRiskManagementDOForUpdate = ConverterUtil.convert(customerRiskManagement, CustomerRiskManagementDO.class);
            customerRiskManagementDOForUpdate.setId(customerDO.getCustomerRiskManagementDO().getId());
            customerRiskManagementDOForUpdate.setRemark(customerRiskManagement.getRemark());
            customerRiskManagementDOForUpdate.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
            customerRiskManagementDOForUpdate.setCreateTime(now);
            customerRiskManagementDOForUpdate.setUpdateTime(now);
            customerRiskManagementDOForUpdate.setCreateUser(userSupport.getCurrentUserId().toString());
            customerRiskManagementDOForUpdate.setUpdateUser(userSupport.getCurrentUserId().toString());
            customerRiskManagementMapper.update(customerRiskManagementDOForUpdate);

            //判断风控信息的原有信息是否发生更改
            if (!judgeResult) {
                CustomerRiskManagementHistoryDO customerRiskManagementHistoryDO = ConverterUtil.convert(customerRiskManagementDOForUpdate, CustomerRiskManagementHistoryDO.class);
                customerRiskManagementHistoryDO.setCustomerId(customerDO.getId());
                customerRiskManagementHistoryDO.setCustomerNo(customerDO.getCustomerNo());
                customerRiskManagementHistoryMapper.save(customerRiskManagementHistoryDO);
            }
        }
        //跟新客户审核状态为成功
//        if (CustomerStatus.STATUS_COMMIT.equals(customerDO.getCustomerStatus())) {
//            customerDO.setCustomerStatus(CustomerStatus.STATUS_PASS);
//            customerMapper.update(customerDO);
//        }

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(customerDO.getCustomerNo());
        return serviceResult;
    }


    @Override
    public ServiceResult<String, String> updateRiskCreditAmountUsed(CustomerRiskManagement customerRiskManagement) {

        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        Date now = new Date();

        if (customerRiskManagement.getCreditAmountUsed() == null || BigDecimalUtil.compare(customerRiskManagement.getCreditAmountUsed(), BigDecimal.ZERO) < 0) {
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_RISK_MANAGEMENT_CREDIT_AMOUNT_USED_IS_NOT_NULL);
            return serviceResult;
        }
        CustomerDO customerDO = customerMapper.findByNo(customerRiskManagement.getCustomerNo());
        if (customerDO == null) {
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_NOT_EXISTS);
            return serviceResult;
        }
        if (customerDO.getCustomerRiskManagementDO() == null) {
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_RISK_MANAGEMENT_NOT_EXISTS);
            return serviceResult;
        }
        //判断已用授信额度大于授信额度
        if (BigDecimalUtil.compare(customerRiskManagement.getCreditAmountUsed(), customerDO.getCustomerRiskManagementDO().getCreditAmount()) == CommonConstant.YES) {
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_RISK_MANAGEMENT_CREDIT_AMOUNT_USED_GREATER_THAN_CREDIT_AMOUNT);
            return serviceResult;
        }

        CustomerRiskManagementDO customerRiskManagementDO = ConverterUtil.convert(customerRiskManagement, CustomerRiskManagementDO.class);
        customerRiskManagementDO.setId(customerDO.getCustomerRiskManagementDO().getId());
        customerRiskManagementDO.setUpdateTime(now);
        customerRiskManagementDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        customerRiskManagementMapper.update(customerRiskManagementDO);

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(customerDO.getCustomerNo());
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
        User loginUser = userSupport.getCurrentUser();

        //通过CustomerNo来获取客户ID
        CustomerDO customerDO = customerMapper.findByNo(customerConsignInfo.getCustomerNo());
        if (customerDO == null) {
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_NOT_EXISTS);
            return serviceResult;
        }

        //只有创建人和业务员和联合开发员才能功能
        if (!loginUser.getUserId().toString().equals(customerDO.getCreateUser()) &&
                !loginUser.getUserId().equals(customerDO.getOwner()) &&
                !loginUser.getUserId().equals(customerDO.getUnionUser())) {
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_CONSIGN_IS_CREATE_USER_AND_OWNER_AND_UNION_USER);
            return serviceResult;
        }

        //获取地址信息的内容，存入客户地址信息表
        CustomerConsignInfoDO customerConsignInfoDO = ConverterUtil.convert(customerConsignInfo, CustomerConsignInfoDO.class);
        customerConsignInfoDO.setCustomerId(customerDO.getId());
        customerConsignInfoDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        customerConsignInfoDO.setVerifyStatus(CustomerConsignVerifyStatus.VERIFY_STATUS_PENDING);
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
        User loginUser = userSupport.getCurrentUser();

        CustomerConsignInfoDO customerConsignInfoDO = customerConsignInfoMapper.findById(customerConsignInfo.getCustomerConsignInfoId());
        if (customerConsignInfoDO == null) {
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_CONSIGN_INFO_NOT_EXISTS);
            return serviceResult;
        }

        if (CustomerConsignVerifyStatus.VERIFY_STATUS_COMMIT.equals(customerConsignInfoDO.getVerifyStatus()) &&
                CustomerConsignVerifyStatus.VERIFY_STATUS_FIRST_PASS.equals(customerConsignInfoDO.getVerifyStatus()) &&
                CustomerConsignVerifyStatus.VERIFY_STATUS_END_PASS.equals(customerConsignInfoDO.getVerifyStatus())) {
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_CONSIGN_INFO_PASS_NOT_UPDATE_AND_DELETE);
            return serviceResult;
        }

        //通过CustomerNo来获取客户ID
        if (customerConsignInfoDO.getCustomerId() == null) {
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_NOT_EXISTS);
            return serviceResult;
        }
        CustomerDO customerDO = customerMapper.findById(customerConsignInfoDO.getCustomerId());
        if (customerDO == null) {
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_NOT_EXISTS);
            return serviceResult;
        }

        //只有创建人和业务员和联合开发员才能功能
        if (!loginUser.getUserId().toString().equals(customerDO.getCreateUser()) &&
                !loginUser.getUserId().equals(customerDO.getOwner()) &&
                !loginUser.getUserId().equals(customerDO.getUnionUser())) {
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_CONSIGN_IS_CREATE_USER_AND_OWNER_AND_UNION_USER);
            return serviceResult;
        }

        //查看被修改的地址信息是否有企业的关联地址信息
//        CustomerCompanyDO customerCompanyDO = customerCompanyMapper.findByDefaultAddressReferId(customerConsignInfoDO.getId());
//        if (customerCompanyDO != null) {
//            customerConsignInfo.setCustomerNo(customerCompanyDO.getCustomerNo());
//            serviceResult = addCustomerConsignInfo(customerConsignInfo);
//
//            serviceResult.setErrorCode(ErrorCode.SUCCESS);
//            serviceResult.setResult(serviceResult.getResult());
//            return serviceResult;
//        }
        if (CommonConstant.COMMON_CONSTANT_YES.equals(customerConsignInfoDO.getIsBusinessAddress())) {
            CustomerCompanyDO customerCompanyDO = customerCompanyMapper.findByDefaultAddressReferId(customerConsignInfoDO.getId());
            if (customerCompanyDO != null) {
                customerCompanyDO.setAgentPersonName(customerConsignInfoDO.getConsigneeName());
                customerCompanyDO.setAgentPersonPhone(customerConsignInfoDO.getConsigneePhone());
                customerCompanyDO.setProvince(customerConsignInfoDO.getProvince());
                customerCompanyDO.setCity(customerConsignInfoDO.getCity());
                customerCompanyDO.setDistrict(customerConsignInfoDO.getDistrict());
                customerCompanyDO.setAddress(customerConsignInfoDO.getAddress());
                customerCompanyDO.setUpdateTime(now);
                customerCompanyDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                customerCompanyMapper.update(customerCompanyDO);
            }
        }

        //如果传送过来的isMain是1,将该客户其他收货地址改为不是默认收货地址
        if (CommonConstant.COMMON_CONSTANT_YES.equals(customerConsignInfo.getIsMain())) {
            customerConsignInfoMapper.clearIsMainByCustomerId(customerConsignInfoDO.getCustomerId());
        }

        customerConsignInfoDO = ConverterUtil.convert(customerConsignInfo, CustomerConsignInfoDO.class);
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
        User loginUser = userSupport.getCurrentUser();

        CustomerConsignInfoDO customerConsignInfoDO = customerConsignInfoMapper.findById(customerConsignInfo.getCustomerConsignInfoId());
        if (customerConsignInfoDO == null) {
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_CONSIGN_INFO_NOT_EXISTS);
            return serviceResult;
        }

        CustomerDO customerDO = customerMapper.findById(customerConsignInfoDO.getCustomerId());
        if (customerDO == null) {
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_NOT_EXISTS);
            return serviceResult;
        }

        //只有创建人和业务员和联合开发员才能功能
        if (!loginUser.getUserId().toString().equals(customerDO.getCreateUser()) &&
                !loginUser.getUserId().equals(customerDO.getOwner()) &&
                !loginUser.getUserId().equals(customerDO.getUnionUser())) {
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_CONSIGN_IS_CREATE_USER_AND_OWNER_AND_UNION_USER);
            return serviceResult;
        }

        //查看被删除的地址信息是否有企业的关联地址信息
        CustomerCompanyDO customerCompanyDO = customerCompanyMapper.findByDefaultAddressReferId(customerConsignInfoDO.getId());
        if (customerCompanyDO != null) {
            customerCompanyDO.setDefaultAddressReferId(null);
            customerCompanyDO.setUpdateTime(now);
            customerCompanyDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            customerCompanyMapper.update(customerCompanyDO);
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
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_NOT_EXISTS);
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

        //对经办人身份证正面操作
        if (customer.getCustomerCompany().getAgentPersonNoPictureFrontImage() != null) {
            ImageDO agentPersonNoPictureFrontImageDO = imgMysqlMapper.findById(customer.getCustomerCompany().getAgentPersonNoPictureFrontImage().getImgId());
            if (agentPersonNoPictureFrontImageDO == null) {
                serviceResult.setErrorCode(ErrorCode.AGENT_PERSON_NO_PICTURE_FRONT_IMAGE_NOT_EXISTS);
                return serviceResult;
            }
            if (StringUtil.isNotEmpty(agentPersonNoPictureFrontImageDO.getRefId())) {
                serviceResult.setErrorCode(ErrorCode.IMG_REF_ID_HAD_VALUE, agentPersonNoPictureFrontImageDO.getId());
                return serviceResult;
            }
            agentPersonNoPictureFrontImageDO.setImgType(ImgType.AGENT_PERSON_NO_PICTURE_FRONT_IMG_TYPE);
            agentPersonNoPictureFrontImageDO.setRefId(customer.getCustomerId().toString());
            agentPersonNoPictureFrontImageDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            agentPersonNoPictureFrontImageDO.setUpdateTime(now);
            imgMysqlMapper.update(agentPersonNoPictureFrontImageDO);
        }

        //对经办人身份证反面操作
        if (customer.getCustomerCompany().getAgentPersonNoPictureBackImage() != null) {
            ImageDO agentPersonNoPictureBackImageDO = imgMysqlMapper.findById(customer.getCustomerCompany().getAgentPersonNoPictureBackImage().getImgId());
            if (agentPersonNoPictureBackImageDO == null) {
                serviceResult.setErrorCode(ErrorCode.AGENT_PERSON_NO_PICTURE_BACK_IMAGE_NOT_EXISTS);
                return serviceResult;
            }
            if (StringUtil.isNotEmpty(agentPersonNoPictureBackImageDO.getRefId())) {
                serviceResult.setErrorCode(ErrorCode.IMG_REF_ID_HAD_VALUE, agentPersonNoPictureBackImageDO.getId());
                return serviceResult;
            }
            agentPersonNoPictureBackImageDO.setImgType(ImgType.AGENT_PERSON_NO_PICTURE_BACK_IMG_TYPE);
            agentPersonNoPictureBackImageDO.setRefId(customer.getCustomerId().toString());
            agentPersonNoPictureBackImageDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            agentPersonNoPictureBackImageDO.setUpdateTime(now);
            imgMysqlMapper.update(agentPersonNoPictureBackImageDO);
        }


        //对营业执照图片操作
        if (customer.getCustomerCompany().getBusinessLicensePictureImage() != null) {
            ImageDO businessLicensePictureImageDO = imgMysqlMapper.findById(customer.getCustomerCompany().getBusinessLicensePictureImage().getImgId());
            if (businessLicensePictureImageDO == null) {
                serviceResult.setErrorCode(ErrorCode.BUSINESS_LICENSE_PICTURE_IMAGE_NOT_EXISTS);
                return serviceResult;
            }
            if (StringUtil.isNotEmpty(businessLicensePictureImageDO.getRefId())) {
                serviceResult.setErrorCode(ErrorCode.IMG_REF_ID_HAD_VALUE, businessLicensePictureImageDO.getId());
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
                serviceResult.setErrorCode(ErrorCode.IMG_REF_ID_HAD_VALUE, legalPersonNoPictureFrontImageDO.getId());
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
                serviceResult.setErrorCode(ErrorCode.IMG_REF_ID_HAD_VALUE, legalPersonNoPictureBackImageDO.getId());
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
                    serviceResult.setErrorCode(ErrorCode.IMG_REF_ID_HAD_VALUE, managerPlaceRentContractImageDO.getId());
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
                    serviceResult.setErrorCode(ErrorCode.IMG_REF_ID_HAD_VALUE, legalPersonCreditReportImageDO.getId());
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
                    serviceResult.setErrorCode(ErrorCode.IMG_REF_ID_HAD_VALUE, fixedAssetsProveImageDO.getId());
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
                    serviceResult.setErrorCode(ErrorCode.IMG_REF_ID_HAD_VALUE, publicAccountFlowBillImageDO.getId());
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
                    serviceResult.setErrorCode(ErrorCode.IMG_REF_ID_HAD_VALUE, socialSecurityRoProvidentFundImageDO.getId());
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
                    serviceResult.setErrorCode(ErrorCode.IMG_REF_ID_HAD_VALUE, cooperationAgreementImageDO.getId());
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
                    serviceResult.setErrorCode(ErrorCode.IMG_REF_ID_HAD_VALUE, legalPersonEducationImageDO.getId());
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
                    serviceResult.setErrorCode(ErrorCode.IMG_REF_ID_HAD_VALUE, legalPersonPositionalTitleImageDO.getId());
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
                    serviceResult.setErrorCode(ErrorCode.IMG_REF_ID_HAD_VALUE, localeChecklistsImageDO.getId());
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
                    serviceResult.setErrorCode(ErrorCode.IMG_REF_ID_HAD_VALUE, otherDateImageDO.getId());
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
                    serviceResult.setErrorCode(ErrorCode.IMG_REF_ID_HAD_VALUE, imgDO.getId());
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
    public ServiceResult<String, Customer> queryCustomerByCustomerName(String customerName) {
        ServiceResult<String, Customer> serviceResult = new ServiceResult<>();
        //客户信息
        CustomerDO customerDO = customerMapper.findByName(customerName);
        if (customerDO == null) {
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_NOT_EXISTS);
            return serviceResult;
        }
        //判断个人客户与公司客户信息
        Customer customerResult = new Customer();
        if (CustomerType.CUSTOMER_TYPE_COMPANY.equals(customerDO.getCustomerType())) {
            CustomerDO companyCustomerDO = customerMapper.findCustomerCompanyByNo(customerDO.getCustomerNo());
            //封装数据
            customerResult = ConverterUtil.convert(companyCustomerDO, Customer.class);
        } else if (CustomerType.CUSTOMER_TYPE_PERSON.equals(customerDO.getCustomerType())) {
            CustomerDO personCustomerDO = customerMapper.findCustomerPersonByNo(customerDO.getCustomerNo());
            //封装数据
            customerResult = ConverterUtil.convert(personCustomerDO, Customer.class);
        }

        CustomerAccount customerAccount = paymentService.queryCustomerAccountNoLogin(customerResult.getCustomerNo());
        if (customerAccount != null) {
            customerResult.setCustomerAccount(customerAccount);
        }
        //业务员信息
        UserDO ownerUserDO = userMapper.findByUserId(customerResult.getOwner());
        //联合开发人
        UserDO unionUserDO = userMapper.findByUserId(customerResult.getUnionUser());

        customerResult.setCustomerOwnerUser(ConverterUtil.convert(ownerUserDO, User.class));
        customerResult.setCustomerUnionUser(ConverterUtil.convert(unionUserDO, User.class));

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(customerResult);
        return serviceResult;
    }

    private ServiceResult<String, String> updateCustomerOwnerAndUnionUser(CustomerDO customerDO, Customer customer, Date now) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();

        Integer userDOOwner = customerDO.getOwner();
        Integer userDOUnion = customerDO.getUnionUser();
        Integer userOwner = customer.getOwner();
        Integer userUnion = customer.getUnionUser();

        //更改开发员
        if (!customerDO.getOwner().equals(customer.getOwner())) {
            Integer companyIdByUserDo = userSupport.getCompanyIdByUser(customerDO.getOwner());
            Integer companyIdByUser = userSupport.getCompanyIdByUser(customer.getOwner());
            //如果客户开发人不是电销人员，并且修改后的开发人是电销人员
            if (!CommonConstant.ELECTRIC_SALE_COMPANY_ID.equals(companyIdByUserDo) && CommonConstant.ELECTRIC_SALE_COMPANY_ID.equals(companyIdByUser)) {
                serviceResult.setErrorCode(ErrorCode.CUSTOMER_OWNER_NOT_CHANGE_ELECTRIC_SALE_COMPANY);
                return serviceResult;
            }
            //如果客户开发人是电销人员,并且修改后的开发人不是电销人员
            if (CommonConstant.ELECTRIC_SALE_COMPANY_ID.equals(companyIdByUserDo) && !CommonConstant.ELECTRIC_SALE_COMPANY_ID.equals(companyIdByUser)) {
                //设置客户的属地化时间
                customerDO.setLocalizationTime(now);
            }
            customerDO.setOwner(customer.getOwner());
        }

        //修改联合开发元员
        if (customer.getUnionUser() != null) {
            if (customerDO.getUnionUser() == null) {
                customerDO.setUnionUser(customer.getUnionUser());
            } else {
                if (!customerDO.getUnionUser().equals(customer.getUnionUser())) {
                    Integer companyIdByUserDo = userSupport.getCompanyIdByUser(customerDO.getUnionUser());
                    Integer companyIdByUser = userSupport.getCompanyIdByUser(customer.getUnionUser());
                    //如果联合开发员不是电销，并且修改后联合开发员是电销
                    if (!CommonConstant.ELECTRIC_SALE_COMPANY_ID.equals(companyIdByUserDo) && CommonConstant.ELECTRIC_SALE_COMPANY_ID.equals(companyIdByUser)) {
                        serviceResult.setErrorCode(ErrorCode.CUSTOMER_UNION_USER_NOT_CHANGE_ELECTRIC_SALE_COMPANY);
                        return serviceResult;
                    }
                    customerDO.setUnionUser(customer.getUnionUser());
                }
            }
        }

        if (userUnion == null && userDOUnion != null) {
            customerDO.setUnionUser(customer.getUnionUser());
        }

        //创建客户变更记录
        //如果开发员改变
        if (!userDOOwner.equals(userOwner)) {
            //如果传入联合开发员为null或者不为null，都可以以传入的联合开发员为传递值
            createCustomerUpdateLog(customerDO.getId(), userOwner, userUnion, now);
        }

        //如果开发员未改变
        if (userDOOwner.equals(userOwner)) {
            //联合开发员本来为空同时传入的联合开发员不为空
            if (userDOUnion == null && (userUnion != null)) {
                createCustomerUpdateLog(customerDO.getId(), userOwner, userUnion, now);
            } else if (userDOUnion != null && !userDOUnion.equals(userUnion)) {
                //联合开发员不为空，只有传入的联合开发员不同时，传入为null，也视为不同
                createCustomerUpdateLog(customerDO.getId(), userOwner, userUnion, now);
            }
        }

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }

    private void createCustomerUpdateLog(Integer customerId, Integer owner, Integer unionUser, Date now) {
        CustomerUpdateLogDO customerUpdateLogDO = new CustomerUpdateLogDO();
        customerUpdateLogDO.setCustomerId(customerId);
        customerUpdateLogDO.setOwner(owner);
        customerUpdateLogDO.setUnionUser(unionUser);
        customerUpdateLogDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        customerUpdateLogDO.setCreateUser(userSupport.getCurrentUserId().toString());
        customerUpdateLogDO.setCreateTime(now);
        customerUpdateLogMapper.save(customerUpdateLogDO);
    }

    private ServiceResult<String, String> judgeUserOwnerAndUnion(Customer customer) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        //判断开发员是否存在
        UserDO userOwnerDO = userMapper.findByUserId(customer.getOwner());
        if (userOwnerDO == null) {
            serviceResult.setErrorCode(ErrorCode.USER_OWNER_NOT_EXISTS);
            return serviceResult;
        }

        if (customer.getUnionUser() != null) {
            UserDO userUnionDO = userMapper.findByUserId(customer.getUnionUser());
            if (userUnionDO == null) {
                serviceResult.setErrorCode(ErrorCode.USER_UNION_NOT_EXISTS);
                return serviceResult;
            }
            //判断联合开发员是否是总公司
            boolean headUser = userSupport.isHeadUser(customer.getUnionUser());
            if (headUser) {
                serviceResult.setErrorCode(ErrorCode.CUSTOMER_UNION_USER_IS_NOT_HEADER_COMPANY);
                return serviceResult;
            }

            if (customer.getOwner().equals(customer.getUnionUser())) {
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
        customerConsignInfoDO.setIsBusinessAddress(CommonConstant.COMMON_CONSTANT_YES);

        customerConsignInfoMapper.save(customerConsignInfoDO);

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

    private ServiceResult<String, BigDecimal> setCustomerCompanyNeed(List<CustomerCompanyNeed> customerCompanyNeedList) {
        ServiceResult<String, BigDecimal> serviceResult = new ServiceResult<>();

        for (CustomerCompanyNeed customerCompanyNeed : customerCompanyNeedList) {
            ProductSkuDO productSkuDO = productSkuMapper.findById(customerCompanyNeed.getSkuId());
            if (productSkuDO == null) {
                serviceResult.setErrorCode(ErrorCode.CUSTOMER_COMPANY_NEED_SKU_ID_NOT_NULL);
                return serviceResult;
            }
            if (CommonConstant.COMMON_CONSTANT_YES.equals(customerCompanyNeed.getIsNew())) {
                customerCompanyNeed.setUnitPrice(productSkuDO.getNewSkuPrice());
            } else {
                customerCompanyNeed.setUnitPrice(productSkuDO.getSkuPrice());
            }
            if (customerCompanyNeed.getRentCount() == null) {
                serviceResult.setErrorCode(ErrorCode.CUSTOMER_COMPANY_NEED_RENT_COUNT_NOT_NULL);
                return serviceResult;
            }

            BigDecimal totalPrice = BigDecimalUtil.mul(customerCompanyNeed.getUnitPrice(), new BigDecimal(customerCompanyNeed.getRentCount()));
            customerCompanyNeed.setTotalPrice(totalPrice);
            ServiceResult<String, Product> productServiceResult = productService.queryProductBySkuId(customerCompanyNeed.getSkuId());
            customerCompanyNeed.setProduct(productServiceResult.getResult());
        }

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }

    /**
     * 校验风控信息
     *
     * @param : customerRiskManagement
     * @Author : XiaoLuYu
     * @Date : Created in 2018/3/6 20:46
     * @Return : com.lxzl.erp.common.domain.ServiceResult<java.lang.String,java.lang.String>
     */
    private ServiceResult<String, String> verifyRiskManagement(CustomerRiskManagement customerRiskManagement) {
        ServiceResult<String, String> result = new ServiceResult<>();
        if (CommonConstant.COMMON_CONSTANT_NO.equals(customerRiskManagement.getIsFullDeposit())) {
            //限制单台设备价值
            if (BigDecimalUtil.compare(customerRiskManagement.getSingleLimitPrice(), BigDecimal.ZERO) < 0) {
                result.setErrorCode(ErrorCode.SINGLE_LIMIT_PRICE_ERROR);
                return result;
            }
            //押金期数
            if (customerRiskManagement.getDepositCycle() == null) {
                result.setErrorCode(ErrorCode.CUSTOMER_RISK_MANAGEMENT_DEPOSIT_CYCLE_NOT_NULL);
                return result;
            }
            //押金期数范围
            if (0 > customerRiskManagement.getDepositCycle() || customerRiskManagement.getDepositCycle() > 120) {
                result.setErrorCode(ErrorCode.CUSTOMER_RISK_MANAGEMENT_DEPOSIT_CYCLE_ERROR);
                return result;
            }

            //付款期数
            if (customerRiskManagement.getPaymentCycle() == null) {
                result.setErrorCode(ErrorCode.CUSTOMER_RISK_MANAGEMENT_PAYMENT_CYCLE_NOT_NULL);
                return result;
            }
            //付款期数范围
            if (1 > customerRiskManagement.getPaymentCycle() || customerRiskManagement.getPaymentCycle() > 120) {
                result.setErrorCode(ErrorCode.CUSTOMER_RISK_MANAGEMENT_PAYMENT_CYCLE_ERROR);
                return result;
            }
            //支付方式
            if (customerRiskManagement.getPayMode() == null) {
                result.setErrorCode(ErrorCode.PAY_MODE_NOT_NULL);
                return result;
            }
            if (!OrderPayMode.PAY_MODE_PAY_BEFORE.equals(customerRiskManagement.getPayMode()) && !OrderPayMode.PAY_MODE_PAY_AFTER.equals(customerRiskManagement.getPayMode())) {
                result.setErrorCode(ErrorCode.PAY_MODE_ERROR);
                return result;
            }
            //是否限制苹果
            if (customerRiskManagement.getIsLimitApple() == null) {
                result.setErrorCode(ErrorCode.IS_LIMIT_APPLE_NOT_NULL);
                return result;
            } else {
                if (!CommonConstant.COMMON_CONSTANT_YES.equals(customerRiskManagement.getIsLimitApple()) && !CommonConstant.COMMON_CONSTANT_NO.equals(customerRiskManagement.getIsLimitApple())) {
                    result.setErrorCode(ErrorCode.IS_LIMIT_APPLE_ERROR);
                    return result;
                }
                if (CommonConstant.COMMON_CONSTANT_NO.equals(customerRiskManagement.getIsLimitApple())) {
                    //苹果押金期数
                    if (customerRiskManagement.getAppleDepositCycle() == null) {
                        result.setErrorCode(ErrorCode.CUSTOMER_RISK_MANAGEMENT_APPLE_DEPOSIT_CYCLE_NOT_NULL);
                        return result;
                    }
                    //苹果押金期数范围
                    if (0 > customerRiskManagement.getAppleDepositCycle() || customerRiskManagement.getAppleDepositCycle() > 120) {
                        result.setErrorCode(ErrorCode.CUSTOMER_RISK_MANAGEMENT_APPLE_DEPOSIT_CYCLE_ERROR);
                        return result;
                    }
                    //苹果付款期数
                    if (customerRiskManagement.getApplePaymentCycle() == null) {
                        result.setErrorCode(ErrorCode.CUSTOMER_RISK_MANAGEMENT_APPLE_PAYMENT_CYCLE_NOT_NULL);
                        return result;
                    }
                    //苹果付款期数范围
                    if (1 > customerRiskManagement.getApplePaymentCycle() || customerRiskManagement.getApplePaymentCycle() > 120) {
                        result.setErrorCode(ErrorCode.CUSTOMER_RISK_MANAGEMENT_APPLE_PAYMENT_CYCLE_ERROR);
                        return result;
                    }
                    //苹果支付方式
                    if (customerRiskManagement.getApplePayMode() == null) {
                        result.setErrorCode(ErrorCode.APPLE_PAY_MODE_NOT_NULL);
                        return result;
                    }
                    if (!OrderPayMode.PAY_MODE_PAY_BEFORE.equals(customerRiskManagement.getApplePayMode()) && !OrderPayMode.PAY_MODE_PAY_AFTER.equals(customerRiskManagement.getApplePayMode())) {
                        result.setErrorCode(ErrorCode.APPLE_PAY_MODE_ERROR);
                        return result;
                    }
                }

            }
            //是否限制全新
            if (customerRiskManagement.getIsLimitNew() == null) {
                result.setErrorCode(ErrorCode.IS_LIMIT_NEW_NOT_NULL);
                return result;
            } else {
                if (!CommonConstant.COMMON_CONSTANT_YES.equals(customerRiskManagement.getIsLimitNew()) && !CommonConstant.COMMON_CONSTANT_NO.equals(customerRiskManagement.getIsLimitNew())) {
                    result.setErrorCode(ErrorCode.IS_LIMIT_NEW_ERROR);
                    return result;
                }
                if (CommonConstant.COMMON_CONSTANT_NO.equals(customerRiskManagement.getIsLimitNew())) {
                    //全新押金期数
                    if (customerRiskManagement.getNewDepositCycle() == null) {
                        result.setErrorCode(ErrorCode.CUSTOMER_RISK_MANAGEMENT_NEW_DEPOSIT_CYCLE_NOT_NULL);
                        return result;
                    }
                    //全新押金期数范围
                    if (0 > customerRiskManagement.getNewDepositCycle() || customerRiskManagement.getNewDepositCycle() > 120) {
                        result.setErrorCode(ErrorCode.CUSTOMER_RISK_MANAGEMENT_NEW_DEPOSIT_CYCLE_ERROR);
                        return result;
                    }
                    //全新付款期数
                    if (customerRiskManagement.getNewPaymentCycle() == null) {
                        result.setErrorCode(ErrorCode.CUSTOMER_RISK_MANAGEMENT_NEW_PAYMENT_CYCLE_NOT_NULL);
                        return result;
                    }
                    //全新付款期数范围
                    if (1 > customerRiskManagement.getNewPaymentCycle() || customerRiskManagement.getNewPaymentCycle() > 120) {
                        result.setErrorCode(ErrorCode.CUSTOMER_RISK_MANAGEMENT_NEW_PAYMENT_CYCLE_ERROR);
                        return result;
                    }
                    //全新支付方式
                    if (customerRiskManagement.getNewPayMode() == null) {
                        result.setErrorCode(ErrorCode.NEW_PAY_MODE_NOT_NULL);
                        return result;
                    }
                    if (!OrderPayMode.PAY_MODE_PAY_BEFORE.equals(customerRiskManagement.getNewPayMode()) && !OrderPayMode.PAY_MODE_PAY_AFTER.equals(customerRiskManagement.getNewPayMode())) {
                        result.setErrorCode(ErrorCode.APPLE_PAY_MODE_ERROR);
                        return result;
                    }
                }
            }

        }
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    private ServiceResult<String, String> verifyCommitCustomerData(CustomerDO customerDO) {
        ServiceResult<String, String> result = new ServiceResult<>();
        StringBuilder errorCodeMsg = new StringBuilder();

        //判断是否有收货地址
        List<CustomerConsignInfoDO> customerConsignInfoDOList = customerConsignInfoMapper.findByCustomerId(customerDO.getId());
        if (CollectionUtil.isEmpty(customerConsignInfoDOList)) {
            errorCodeMsg.append("收货地址，");
        }
        //如果客户是企业
        if (CustomerType.CUSTOMER_TYPE_COMPANY.equals(customerDO.getCustomerType())) {
            CustomerCompanyDO customerCompanyDO = customerCompanyMapper.findByCustomerId(customerDO.getId());
            //判断客户资料中必填项是否填写

            if (CommonConstant.COMMON_CONSTANT_YES.equals(customerCompanyDO.getIsLegalPersonApple())) {
                if (customerCompanyDO.getLegalPerson() == null || customerCompanyDO.getLegalPersonNo() == null || customerCompanyDO.getLegalPersonPhone() == null) {
                    errorCodeMsg.append("法人身份证、姓名、电话，");
                }
            }
            //对图片附件进行判断
            List<ImageDO> imageDOList = imgMysqlMapper.findByRefId(customerCompanyDO.getCustomerId().toString());
            if (CollectionUtil.isEmpty(imageDOList)) {
                errorCodeMsg.append("图片信息不能为空，");
            }

            List<ImageDO> businessLicensePicture = imgMysqlMapper.findByRefIdAndType(customerCompanyDO.getCustomerId().toString(), ImgType.BUSINESS_LICENSE_PICTURE_IMG_TYPE);
            List<ImageDO> legalPersonNoPictureFront = imgMysqlMapper.findByRefIdAndType(customerCompanyDO.getCustomerId().toString(), ImgType.LEGAL_PERSON_NO_PICTURE_FRONT_IMG_TYPE);
            List<ImageDO> legalPersonNoPictureBack = imgMysqlMapper.findByRefIdAndType(customerCompanyDO.getCustomerId().toString(), ImgType.LEGAL_PERSON_NO_PICTURE_BACK_IMG_TYPE);

            if (businessLicensePicture.size() == 0) {
                errorCodeMsg.append("营业执照，");
            }
            if (legalPersonNoPictureFront.size() == 0) {
                errorCodeMsg.append("法人身份证正面照，");
            }
            if (legalPersonNoPictureBack.size() == 0) {
                errorCodeMsg.append("法人身份证反面照，");
            }

            //对注册资本判断
            if (customerCompanyDO.getRegisteredCapital() == null) {
                errorCodeMsg.append("注册资本，");
            }
            //对企业所属行业判断
            if (StringUtil.isEmpty(customerCompanyDO.getIndustry())) {
                errorCodeMsg.append("所属行业，");
            }
            //对设备用途判断
            if (StringUtil.isEmpty(customerCompanyDO.getProductPurpose())) {
                errorCodeMsg.append("设备用户，");
            }
            //对企业成立时间判断
            if (customerCompanyDO.getCompanyFoundTime() == null) {
                errorCodeMsg.append("成立时间，");
            }
            //对企业办公人数判断
            if (customerCompanyDO.getOfficeNumber() == null) {
                errorCodeMsg.append("办公人数，");
            }
            if (StringUtil.isBlank(customerCompanyDO.getConnectRealName())) {
                errorCodeMsg.append("紧急联系人，");
            }
            if (StringUtil.isBlank(customerCompanyDO.getConnectPhone())) {
                errorCodeMsg.append("紧急联系人手机号，");
            }
            if (StringUtil.isBlank(customerCompanyDO.getAddress())) {
                errorCodeMsg.append("公司经营地址，");
            }
            if (customerCompanyDO.getIsLegalPersonApple() == null) {
                errorCodeMsg.append("是否法人代表申请，");
            }
            if (customerCompanyDO.getProductPurpose() == null) {
                errorCodeMsg.append("设备用途，");
            }
            if (customerCompanyDO.getUnifiedCreditCode() == null) {
                errorCodeMsg.append("统一信用代码，");
            }
            if (StringUtil.isBlank(customerCompanyDO.getCustomerCompanyNeedFirstJson())) {
                errorCodeMsg.append("首次所租设备不能为空，");
            } else {
                List<CustomerCompanyNeed> customerCompanyNeedFirstList = JSONObject.parseArray(customerCompanyDO.getCustomerCompanyNeedFirstJson(), CustomerCompanyNeed.class);
                if (CollectionUtil.isEmpty(customerCompanyNeedFirstList)) {
                    errorCodeMsg.append("首次所租设备不能为空，");
                }
            }

            //对企业的经营面积判断
            if (customerCompanyDO.getOperatingArea() == null) {
                errorCodeMsg.append("经营面积,");
            }
            if (StringUtil.isNotBlank(errorCodeMsg.toString())) {
                errorCodeMsg.append("以上信息为长租必填项，请完善！");
                result.setErrorCode(errorCodeMsg.toString());
                return result;
            }
        }
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    /**
     * 校验是否是跟单员,联合开发人,创建人
     *
     * @param : customerDO
     * @Author : XiaoLuYu
     * @Date : Created in 2018/3/14 14:28
     * @Return : com.lxzl.erp.common.domain.ServiceResult<java.lang.String,java.lang.String>
     */
    private ServiceResult<String, String> verifyJurisdiction(CustomerDO customerDO) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        //开发人
        Integer owner = customerDO.getOwner();
        //联合开发人
        Integer unionUser = customerDO.getUnionUser();
        //创建人
        String createUser = customerDO.getCreateUser();
        Integer createUserId = Integer.parseInt(createUser);

        //当前登录用户
        Integer currentUserId = userSupport.getCurrentUserId();
        if (!currentUserId.equals(owner) && !currentUserId.equals(unionUser) && !currentUserId.equals(createUserId) && !userSupport.isSuperUser()) {
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_CAN_NOT_UPDATE_BY_CURRENT_USER);
            return serviceResult;
        }
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, String> updateOwnerAndUnionUser(Customer customer) {
        CustomerDO customerDO = customerMapper.findByNo(customer.getCustomerNo());
        Date now = new Date();
        //校验开发人和联合开发人是否相同
        ServiceResult<String, String> serviceResult = judgeUserOwnerAndUnion(customer);
        if (!ErrorCode.SUCCESS.equals(serviceResult.getErrorCode())) {
            return serviceResult;
        }
        //添加客户修改日志
        serviceResult = updateCustomerOwnerAndUnionUser(customerDO, customer, now);
        if (!ErrorCode.SUCCESS.equals(serviceResult.getErrorCode())) {
            return serviceResult;
        }
        customerDO.setOwnerSubCompanyId(userSupport.getCompanyIdByUser(customer.getOwner()));
        customerDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        customerDO.setUpdateTime(now);
        customerMapper.update(customerDO);

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }

    /**
     * 风控历史记录分页查询
     *
     * @param customerRiskManageHistoryQueryParam
     * @return
     */
    @Override
    public ServiceResult<String, Page<CustomerRiskManagementHistory>> pageCustomerRiskManagementHistory(CustomerRiskManageHistoryQueryParam customerRiskManageHistoryQueryParam) {
        ServiceResult<String, Page<CustomerRiskManagementHistory>> result = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(customerRiskManageHistoryQueryParam.getPageNo(), customerRiskManageHistoryQueryParam.getPageSize());
        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        maps.put("customerRiskManageHistoryQueryParam", customerRiskManageHistoryQueryParam);
        maps.put("permissionParam", permissionSupport.getPermissionParam(PermissionType.PERMISSION_TYPE_USER));
        Integer totalCount = customerRiskManagementHistoryMapper.findCustomerRiskHistoryCountByParams(maps);
        List<CustomerRiskManagementHistoryDO> customerRiskHistoryDOList = customerRiskManagementHistoryMapper.findCustomerRiskHistoryByParams(maps);
        List<CustomerRiskManagementHistory> customerRiskManagementHistoryList = ConverterUtil.convertList(customerRiskHistoryDOList, CustomerRiskManagementHistory.class);
        Page<CustomerRiskManagementHistory> page = new Page<>(customerRiskManagementHistoryList, totalCount, customerRiskManageHistoryQueryParam.getPageNo(), customerRiskManageHistoryQueryParam.getPageSize());
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(page);
        return result;
    }

    /**
     * 风控历史详情
     *
     * @param customerRiskManagementHistoryId
     * @return
     */
    @Override
    public ServiceResult<String, CustomerRiskManagementHistory> detailCustomerRiskManagementHistory(Integer customerRiskManagementHistoryId) {
        ServiceResult<String, CustomerRiskManagementHistory> serviceResult = new ServiceResult<>();
        CustomerRiskManagementHistoryDO customerRiskManagementHistoryDO = customerRiskManagementHistoryMapper.findById(customerRiskManagementHistoryId);
        CustomerRiskManagementHistory customerRiskManagementHistoryResult = ConverterUtil.convert(customerRiskManagementHistoryDO, CustomerRiskManagementHistory.class);
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(customerRiskManagementHistoryResult);
        return serviceResult;
    }

    private ServiceResult<String, CustomerConsignInfoDO> updateCustomerConsignInfoDOInfo(List<CustomerConsignInfoDO> customerConsignInfoDOList, Integer customerId, Date now, CustomerCompanyDO customerCompanyDO) {
        ServiceResult<String, CustomerConsignInfoDO> result = new ServiceResult<>();

        List<CustomerConsignInfoDO> dbCustomerConsignInfoDOList = customerConsignInfoMapper.findByCustomerId(customerId);
        List<CustomerConsignInfoDO> saveCustomerConsignInfoDOList = new ArrayList<>();
        Map<Integer, CustomerConsignInfoDO> updateCustomerConsignInfoDOMap = new HashMap<>();
        Map<Integer, CustomerConsignInfoDO> dbCustomerConsignInfoDOMap = ListUtil.listToMap(dbCustomerConsignInfoDOList, "id");
        if (CollectionUtil.isNotEmpty(customerConsignInfoDOList)) {
            for (CustomerConsignInfoDO customerConsignInfoDO : customerConsignInfoDOList) {
                CustomerConsignInfoDO dbCustomerConsignInfoDO = dbCustomerConsignInfoDOMap.get(customerConsignInfoDO.getId());
                if (dbCustomerConsignInfoDO != null) {
                    customerConsignInfoDO.setId(dbCustomerConsignInfoDO.getId());
                    updateCustomerConsignInfoDOMap.put(customerConsignInfoDO.getId(), customerConsignInfoDO);
                    dbCustomerConsignInfoDOMap.remove(customerConsignInfoDO.getId());
                } else {
                    saveCustomerConsignInfoDOList.add(customerConsignInfoDO);
                }
            }
        }

        if (saveCustomerConsignInfoDOList.size() > 0) {
            for (CustomerConsignInfoDO customerConsignInfoDO : saveCustomerConsignInfoDOList) {
                if (CommonConstant.COMMON_CONSTANT_YES.equals(customerConsignInfoDO.getIsMain())) {
                    //如果传送过来的isMain是1,将该客户其他收货地址改为不是默认收货地址
                    customerConsignInfoMapper.clearIsMainByCustomerId(customerConsignInfoDO.getCustomerId());
                    customerConsignInfoDO.setCustomerId(customerId);
                    customerConsignInfoDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                    customerConsignInfoDO.setVerifyStatus(CustomerConsignVerifyStatus.VERIFY_STATUS_PENDING);
                    customerConsignInfoDO.setCreateTime(now);
                    customerConsignInfoDO.setCreateUser(userSupport.getCurrentUserId().toString());
                    customerConsignInfoMapper.save(customerConsignInfoDO);
                } else {
                    customerConsignInfoDO.setCustomerId(customerId);
                    customerConsignInfoDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                    customerConsignInfoDO.setVerifyStatus(CustomerConsignVerifyStatus.VERIFY_STATUS_PENDING);
                    customerConsignInfoDO.setCreateTime(now);
                    customerConsignInfoDO.setCreateUser(userSupport.getCurrentUserId().toString());
                    customerConsignInfoMapper.save(customerConsignInfoDO);
                }
                if (CommonConstant.COMMON_CONSTANT_YES.equals(customerConsignInfoDO.getIsBusinessAddress())) {
                    customerCompanyDO.setDefaultAddressReferId(customerConsignInfoDO.getId());
                }
            }
        }
        if (updateCustomerConsignInfoDOMap.size() > 0) {
            for (Map.Entry<Integer, CustomerConsignInfoDO> entry : updateCustomerConsignInfoDOMap.entrySet()) {
                CustomerConsignInfoDO customerConsignInfoDO = entry.getValue();
                if (CommonConstant.COMMON_CONSTANT_YES.equals(customerConsignInfoDO.getIsMain())) {
                    //如果传送过来的isMain是1,将该客户其他收货地址改为不是默认收货地址
                    customerConsignInfoMapper.clearIsMainByCustomerId(customerConsignInfoDO.getCustomerId());
                    customerConsignInfoDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                    customerConsignInfoDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                    customerConsignInfoDO.setUpdateTime(now);
                    customerConsignInfoMapper.update(customerConsignInfoDO);
                } else {
                    customerConsignInfoDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                    customerConsignInfoDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                    customerConsignInfoDO.setUpdateTime(now);
                    customerConsignInfoMapper.update(customerConsignInfoDO);
                }
                if (CommonConstant.COMMON_CONSTANT_YES.equals(customerConsignInfoDO.getIsBusinessAddress())) {
                    customerCompanyDO.setDefaultAddressReferId(customerConsignInfoDO.getId());
                }
            }
        }

        if (dbCustomerConsignInfoDOMap.size() > 0) {
            for (Map.Entry<Integer, CustomerConsignInfoDO> entry : dbCustomerConsignInfoDOMap.entrySet()) {
                CustomerConsignInfoDO customerConsignInfoDO = entry.getValue();
                customerConsignInfoDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
                customerConsignInfoDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                customerConsignInfoDO.setUpdateTime(now);
                customerConsignInfoMapper.update(customerConsignInfoDO);
            }
        }
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    /**
     * 此方法只用于处理历史数据中公司客户的新增字段simple_company_name字段为空的情况，新增数据不会出现此种情况
     *
     * @return
     */

    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, String> customerCompanySimpleNameProcessing() {

        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        if (!userSupport.isSuperUser()) {
            serviceResult.setErrorCode(ErrorCode.DATA_HAVE_NO_PERMISSION);
            return serviceResult;
        }
        List<CustomerCompanyDO> list = customerCompanyMapper.findBySimpleCompanyNameIsNull();

        List<CustomerCompanyDO> newCustomerCompanyList = new ArrayList<>();
        StrReplaceUtil sutil = new StrReplaceUtil();

        for (CustomerCompanyDO customerCompany : list) {
            //将公司客户名带括号的，全角中文，半角中文，英文括号，统一转为（这种括号格式
            customerCompany.setCompanyName(sutil.replaceAll(customerCompany.getCompanyName()));
            //将公司客户名称中所有除了中文，英文字母（大小写）的字符全部去掉
            String simpleCompanyName = sutil.nameToSimple(customerCompany.getCompanyName());
            customerCompany.setSimpleCompanyName(simpleCompanyName);
            newCustomerCompanyList.add(customerCompany);

        }
        customerCompanyMapper.batchAddSimpleCompanyName(newCustomerCompanyList);
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;

    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, String> commitCustomerConsignInfo(CustomerConsignCommitParam customerConsignCommitParam) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        Date now = new Date();
        User loginUser = userSupport.getCurrentUser();

        CustomerConsignInfoDO customerConsignInfoDO = customerConsignInfoMapper.findById(customerConsignCommitParam.getCustomerConsignId());
        if(customerConsignInfoDO == null){
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_CONSIGN_NOT_EXISTS);
            return serviceResult;
        }

        CustomerDO customerDO = customerMapper.findById(customerConsignInfoDO.getCustomerId());
        if(customerDO == null){
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_NOT_EXISTS);
            return serviceResult;
        }

        //只有创建人和业务员和联合开发员才能提交功能
        if (!loginUser.getUserId().toString().equals(customerDO.getCreateUser()) &&
                !loginUser.getUserId().equals(customerDO.getOwner()) &&
                !loginUser.getUserId().equals(customerDO.getUnionUser())) {
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_COMMIT_IS_CREATE_USER_AND_OWNER_AND_UNION_USER);
            return serviceResult;
        }

        if(!CustomerConsignVerifyStatus.VERIFY_STATUS_PENDING.equals(customerConsignInfoDO.getVerifyStatus())
                && !CustomerConsignVerifyStatus.VERIFY_STATUS_FIRST_PASS.equals(customerConsignInfoDO.getVerifyStatus())
                && !CustomerConsignVerifyStatus.VERIFY_STATUS_BACK.equals(customerConsignInfoDO.getVerifyStatus()) ){
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_CONSIGN_INFO_NOT_PENDING);
            return serviceResult;
        }

        SubCompanyCityCoverDO subCompanyCityCoverDO = subCompanyCityCoverMapper.findByCityId(customerConsignInfoDO.getCity());
        if (subCompanyCityCoverDO == null) {
            subCompanyCityCoverDO = subCompanyCityCoverMapper.findByProvinceId(customerConsignInfoDO.getProvince());
            if (subCompanyCityCoverDO == null) {
                serviceResult.setErrorCode(ErrorCode.CUSTOMER_CONSIGN_INFO_NOT_CITY_AND_PROVINCE_IS_NULL);
                return serviceResult;
            }
        }

        ServiceResult<String, Boolean> needVerifyResult = workflowService.isNeedVerify(WorkflowType.WORKFLOW_TYPE_CUSTOMER_CONSIGN);
        if (!ErrorCode.SUCCESS.equals(needVerifyResult.getErrorCode())) {
            serviceResult.setErrorCode(needVerifyResult.getErrorCode());
            return serviceResult;
        } else if (needVerifyResult.getResult()) {
            if (customerConsignCommitParam.getVerifyUserId() == null) {
                serviceResult.setErrorCode(ErrorCode.VERIFY_USER_NOT_NULL);
                return serviceResult;
            }
            customerConsignCommitParam.setVerifyMatters("客户地址审核事项：1.地址需要核实 2.收货人姓名与收货人电话需要核实");

            ServiceResult<String, String> verifyResult = workflowService.commitWorkFlow(WorkflowType.WORKFLOW_TYPE_CUSTOMER_CONSIGN, String.valueOf(customerConsignCommitParam.getCustomerConsignId()), customerConsignCommitParam.getVerifyUserId(), customerConsignCommitParam.getVerifyMatters(), customerConsignCommitParam.getRemark(), customerConsignCommitParam.getImgIdList(), null);
            //修改提交审核状态
            if (ErrorCode.SUCCESS.equals(verifyResult.getErrorCode())) {
                customerConsignInfoDO.setVerifyStatus(CustomerConsignVerifyStatus.VERIFY_STATUS_COMMIT);
                customerConsignInfoDO.setUpdateUser(loginUser.getUserId().toString());
                customerConsignInfoDO.setUpdateTime(now);
                customerConsignInfoDO.setWorkflowType(WorkflowType.WORKFLOW_TYPE_CUSTOMER_CONSIGN);
                customerConsignInfoMapper.update(customerConsignInfoDO);
                return verifyResult;
            } else {
                serviceResult.setErrorCode(verifyResult.getErrorCode());
                return serviceResult;
            }
        } else {
            customerConsignInfoDO.setVerifyStatus(CustomerConsignVerifyStatus.VERIFY_STATUS_END_PASS);
            customerConsignInfoDO.setUpdateUser(loginUser.getUserId().toString());
            customerConsignInfoDO.setUpdateTime(now);
            customerConsignInfoMapper.update(customerConsignInfoDO);
            serviceResult.setErrorCode(ErrorCode.SUCCESS);
            return serviceResult;
        }
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public ServiceResult<String, Integer> addCustomerReturnVisit(ReturnVisit returnVisit) {
        ServiceResult<String, Integer> serviceResult = new ServiceResult<>();
        Date now = new Date();

        CustomerDO customerDO = customerMapper.findByNo(returnVisit.getCustomerNo());
        if (customerDO == null){
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_NOT_EXISTS);
            return serviceResult;
        }

        //todo 暂时先不用对客户回访进行条件判断，后期再进行条件的添加和修改
//        List<OrderDO> orderDOList = orderMapper.findByCustomerId(customerDO.getId());
//        if (CollectionUtil.isEmpty(orderDOList)){
//            serviceResult.setErrorCode(ErrorCode.CUSTOMER_ORDER_DO_NOT_HAVE_ORDER);
//            return serviceResult;
//        }
//
//        //如果该客户的所有订单都是已经归还完成了，也不用进行回访了
//        Integer returnCounT = 0; //记录订单已经完成归还的次数
//        for (OrderDO orderDO : orderDOList){
//            if (orderDO.getActualReturnTime().compareTo(now) < 0){
//                returnCounT++;
//            }
//        }
//
//        if (returnCounT.equals(orderDOList.size())){
//            serviceResult.setErrorCode(ErrorCode.CUSTOMER_ORDER_NOT_NEED_RETURN_VISIT);
//            return serviceResult;
//        }

        ReturnVisitDO returnVisitDO = ConverterUtil.convert(returnVisit,ReturnVisitDO.class);
        returnVisitDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        returnVisitDO.setCreateTime(now);
        returnVisitDO.setCreateUser(userSupport.getCurrentUserId().toString());
        returnVisitDO.setUpdateTime(now);
        returnVisitDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        returnVisitMapper.save(returnVisitDO);

        //对客户回访记录的图片做判断
        if (CollectionUtil.isNotEmpty(returnVisit.getCustomerReturnVisitImageList())){
            for (Image customerReturnVisitImage : returnVisit.getCustomerReturnVisitImageList()){
                ImageDO customerReturnVisitImageDO = imgMysqlMapper.findById(customerReturnVisitImage.getImgId());
                if (customerReturnVisitImageDO == null) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    serviceResult.setErrorCode(ErrorCode.CUSTOMER_RETURN_VISIT_IMAGE_NOT_EXISTS);
                    return serviceResult;
                }
                if (StringUtil.isNotEmpty(customerReturnVisitImageDO.getRefId())) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    serviceResult.setErrorCode(ErrorCode.IMG_REF_ID_HAD_VALUE, customerReturnVisitImageDO.getId());
                    return serviceResult;
                }

                customerReturnVisitImageDO.setImgType(ImgType.CUSTOMER_RETURN_VISIT_IMG_TYPE);
                customerReturnVisitImageDO.setRefId(returnVisitDO.getId().toString());
                customerReturnVisitImageDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                customerReturnVisitImageDO.setUpdateTime(now);
                imgMysqlMapper.update(customerReturnVisitImageDO);
            }
        }

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(returnVisitDO.getId());
        return serviceResult;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public ServiceResult<String, String> updateCustomerReturnVisit(ReturnVisit returnVisit) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        Date now = new Date();

        ReturnVisitDO returnVisitDO = returnVisitMapper.findById(returnVisit.getReturnVisitId());
        if (returnVisitDO == null){
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_RETURN_VISIT_NOT_EXISTS);
            return serviceResult;
        }

        returnVisitDO.setReturnVisitDescribe(returnVisit.getReturnVisitDescribe());
        returnVisitDO.setRemark(returnVisit.getRemark());
        returnVisitDO.setUpdateTime(now);
        returnVisitDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        returnVisitMapper.update(returnVisitDO);

        //对客户回访记录的图片做判断
        List<Image> customerReturnVisitImageList = returnVisit.getCustomerReturnVisitImageList();
        serviceResult = updateImage(customerReturnVisitImageList, ImgType.CUSTOMER_RETURN_VISIT_IMG_TYPE, returnVisitDO.getId().toString(), userSupport.getCurrentUserId().toString(), now);
        if (!ErrorCode.SUCCESS.equals(serviceResult.getErrorCode())) {
            serviceResult.setErrorCode(serviceResult.getErrorCode(), serviceResult.getFormatArgs());
            return serviceResult;
        }

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(returnVisitDO.getId().toString());
        return serviceResult;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public ServiceResult<String, String> deleteCustomerReturnVisit(ReturnVisit returnVisit) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        Date now = new Date();

        ReturnVisitDO returnVisitDO = returnVisitMapper.findDetailById(returnVisit.getReturnVisitId());
        if (returnVisitDO == null){
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_RETURN_VISIT_NOT_EXISTS);
            return serviceResult;
        }

        returnVisitDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
        returnVisitDO.setUpdateTime(now);
        returnVisitDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        returnVisitMapper.update(returnVisitDO);

        //处理相关联的回访图片
        List<ImageDO> customerReturnVisitImageDOList = returnVisitDO.getCustomerReturnVisitImageDOList();
        if (CollectionUtil.isNotEmpty(customerReturnVisitImageDOList)){
            for (ImageDO customerReturnVisitImageDO : customerReturnVisitImageDOList){
                customerReturnVisitImageDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
                customerReturnVisitImageDO.setUpdateTime(now);
                customerReturnVisitImageDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                imgMysqlMapper.update(customerReturnVisitImageDO);
            }
        }

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(returnVisitDO.getId().toString());
        return serviceResult;
    }

    @Override
    public ServiceResult<String, ReturnVisit> detailCustomerReturnVisit(ReturnVisit returnVisit) {
        ServiceResult<String, ReturnVisit> serviceResult = new ServiceResult<>();

        ReturnVisitDO returnVisitDO = returnVisitMapper.findDetailById(returnVisit.getReturnVisitId());
        if (returnVisitDO == null){
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_RETURN_VISIT_NOT_EXISTS);
            return serviceResult;
        }

        returnVisit = ConverterUtil.convert(returnVisitDO,ReturnVisit.class);

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(returnVisit);
        return serviceResult;
    }

    @Override
    public ServiceResult<String, Page<ReturnVisit>> pageCustomerReturnVisit(CustomerReturnVisitQueryParam customerReturnVisitQueryParam) {
        ServiceResult<String, Page<ReturnVisit>> result = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(customerReturnVisitQueryParam.getPageNo(), customerReturnVisitQueryParam.getPageSize());

        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        maps.put("customerReturnVisitQueryParam", customerReturnVisitQueryParam);

        Integer totalCount = returnVisitMapper.findReturnVisitCountByParams(maps);
        List<ReturnVisitDO> returnVisitDOList = returnVisitMapper.findReturnVisitByParams(maps);

        List<ReturnVisit> returnVisitList = ConverterUtil.convertList(returnVisitDOList,ReturnVisit.class);
        Page<ReturnVisit> page = new Page<>(returnVisitList, totalCount, customerReturnVisitQueryParam.getPageNo(), customerReturnVisitQueryParam.getPageSize());;

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(page);
        return result;
    }

    @Autowired
    private UserMapper userMapper;

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

    @Autowired
    private WorkflowService workflowService;

    @Autowired
    private CustomerRiskManagementHistoryMapper customerRiskManagementHistoryMapper;

    @Autowired
    private SubCompanyCityCoverMapper subCompanyCityCoverMapper;

    @Autowired
    private ReturnVisitMapper returnVisitMapper;
}