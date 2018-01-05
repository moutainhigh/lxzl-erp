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
import com.lxzl.erp.common.domain.product.pojo.Product;
import com.lxzl.erp.common.domain.system.pojo.Image;
import com.lxzl.erp.common.util.BigDecimalUtil;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.common.util.ConverterUtil;
import com.lxzl.erp.common.util.ListUtil;
import com.lxzl.erp.core.service.basic.impl.support.GenerateNoSupport;
import com.lxzl.erp.core.service.customer.CustomerService;
import com.lxzl.erp.core.service.customer.impl.support.CustomerRiskManagementConverter;
import com.lxzl.erp.core.service.dataAccess.DataAccessSupport;
import com.lxzl.erp.core.service.payment.PaymentService;
import com.lxzl.erp.core.service.product.ProductService;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.dataaccess.dao.mysql.company.SubCompanyMapper;
import com.lxzl.erp.dataaccess.dao.mysql.customer.*;
import com.lxzl.erp.dataaccess.dao.mysql.system.ImgMysqlMapper;
import com.lxzl.erp.dataaccess.domain.company.SubCompanyDO;
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

    @Autowired
    private ProductService productService;

    @Autowired
    private SubCompanyMapper subCompanyMapper;

    @Autowired
    private DataAccessSupport dataAccessSupport;


    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, String> addCompany(Customer customer) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        Date now = new Date();
        CustomerDO dbCustomerDO = customerMapper.findByName(customer.getCustomerCompany().getCompanyName());
        if(dbCustomerDO != null){
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_IS_EXISTS);
            return serviceResult;
        }
        CustomerDO customerDO = new CustomerDO();
        customerDO.setCustomerNo(generateNoSupport.generateCustomerNo(now, CustomerType.CUSTOMER_TYPE_COMPANY));
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
        //加入跟单员和联合开发员
        customerDO.setOwner(userSupport.getCurrentUserId());
        if (customer.getUnionUser() != null) {
            customerDO.setUnionUser(customer.getUnionUser());
        }
        customerMapper.save(customerDO);

        CustomerCompanyDO customerCompanyDO = ConverterUtil.convert(customer.getCustomerCompany(), CustomerCompanyDO.class);
        //设置首次所需设备
        if (CollectionUtil.isNotEmpty(customer.getCustomerCompany().getCustomerCompanyNeedFirstList())) {
            List<CustomerCompanyNeed> customerCompanyNeedFirstList = customer.getCustomerCompany().getCustomerCompanyNeedFirstList();
            for (CustomerCompanyNeed customerCompanyNeed : customerCompanyNeedFirstList) {
                BigDecimal totalPrice = BigDecimalUtil.mul(customerCompanyNeed.getUnitPrice(), new BigDecimal(customerCompanyNeed.getRentCount()));
                customerCompanyNeed.setTotalPrice(totalPrice);
                ServiceResult<String, Product> productServiceResult = productService.queryProductBySkuId(customerCompanyNeed.getSkuId());
                customerCompanyNeed.setProduct(productServiceResult.getResult());
            }
            customerCompanyDO.setCustomerCompanyNeedFirstJson(JSON.toJSON(customerCompanyNeedFirstList).toString());
        }

        //判断后续所需设备
        if (CollectionUtil.isNotEmpty(customer.getCustomerCompany().getCustomerCompanyNeedLaterList())) {
            List<CustomerCompanyNeed> customerCompanyNeedLaterList = customer.getCustomerCompany().getCustomerCompanyNeedLaterList();
            for (CustomerCompanyNeed customerCompanyNeed : customerCompanyNeedLaterList) {
                BigDecimal totalPrice = BigDecimalUtil.mul(customerCompanyNeed.getUnitPrice(), new BigDecimal(customerCompanyNeed.getRentCount()));
                customerCompanyNeed.setTotalPrice(totalPrice);
                ServiceResult<String, Product> productServiceResult = productService.queryProductBySkuId(customerCompanyNeed.getSkuId());
                customerCompanyNeed.setProduct(productServiceResult.getResult());

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

        customer.setCustomerId(customerDO.getId());
        ServiceResult<String, String> serviceResult1 = saveImage(customer, now);
        if (!ErrorCode.SUCCESS.equals(serviceResult1.getErrorCode())) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            serviceResult1.setErrorCode(serviceResult1.getErrorCode(), serviceResult1.getFormatArgs());
            return serviceResult1;
        }

        CustomerConsignInfo customerConsignInfo = new CustomerConsignInfo();
        customerConsignInfo.setCustomerId(customerDO.getId());
        customerConsignInfo.setCustomerNo(customerDO.getCustomerNo());
        customerConsignInfo.setConsigneeName(customerCompanyDO.getConnectRealName());
        customerConsignInfo.setConsigneePhone(customerCompanyDO.getConnectPhone());
        customerConsignInfo.setProvince(customerCompanyDO.getProvince());
        customerConsignInfo.setCity(customerCompanyDO.getCity());
        customerConsignInfo.setDistrict(customerCompanyDO.getDistrict());
        customerConsignInfo.setAddress(customerCompanyDO.getAddress());
        customerConsignInfo.setCreateTime(now);
        customerConsignInfo.setCreateUser(userSupport.getCurrentUserId().toString());
        customerConsignInfo.setUpdateUser(userSupport.getCurrentUserId().toString());
        customerConsignInfo.setUpdateTime(now);
        //调用新增企业地址信息方法
        addCustomerConsignInfo(customerConsignInfo);

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
        if(dbCustomerDO != null){
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_IS_EXISTS);
            return serviceResult;
        }
        CustomerDO customerDO = new CustomerDO();
        customerDO.setCustomerNo(generateNoSupport.generateCustomerNo(now, CustomerType.CUSTOMER_TYPE_PERSON));
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
        //加入跟单员和联合开发员
        customerDO.setOwner(userSupport.getCurrentUserId());
        if (customer.getUnionUser() != null) {
            customerDO.setUnionUser(customer.getUnionUser());
        }
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
        customerConsignInfo.setCustomerId(customerDO.getId());
        customerConsignInfo.setCustomerNo(customerDO.getCustomerNo());
        customerConsignInfo.setConsigneeName(customerPersonDO.getRealName());
        customerConsignInfo.setConsigneePhone(customerPersonDO.getPhone());
        customerConsignInfo.setProvince(customerPersonDO.getProvince());
        customerConsignInfo.setCity(customerPersonDO.getCity());
        customerConsignInfo.setDistrict(customerPersonDO.getDistrict());
        customerConsignInfo.setAddress(customerPersonDO.getAddress());
        customerConsignInfo.setCreateTime(now);
        customerConsignInfo.setCreateUser(userSupport.getCurrentUserId().toString());
        customerConsignInfo.setUpdateUser(userSupport.getCurrentUserId().toString());
        customerConsignInfo.setUpdateTime(now);

        //调用新增个人用户地址信息方法
        addCustomerConsignInfo(customerConsignInfo);

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(customerDO.getCustomerNo());
        return serviceResult;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, String> updateCompany(Customer customer) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        Date now = new Date();
        CustomerDO dbCustomerDO = customerMapper.findByName(customer.getCustomerCompany().getCompanyName());
        if(dbCustomerDO != null && !dbCustomerDO.getCustomerNo().equals(customer.getCustomerNo())){
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_IS_EXISTS);
            return serviceResult;
        }
        CustomerDO customerDO = customerMapper.findCustomerCompanyByNo(customer.getCustomerNo());
        if (customerDO == null || !CustomerType.CUSTOMER_TYPE_COMPANY.equals(customerDO.getCustomerType())) {
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_NOT_EXISTS);
            return serviceResult;
        }

        CustomerCompanyDO customerCompanyDO = customerCompanyMapper.findByCustomerId(customerDO.getId());
        CustomerCompanyDO newCustomerCompanyDO = ConverterUtil.convert(customer.getCustomerCompany(), CustomerCompanyDO.class);

        //判断首次所需设备 list转json
        if (CollectionUtil.isNotEmpty(customer.getCustomerCompany().getCustomerCompanyNeedFirstList())) {
            List<CustomerCompanyNeed> customerCompanyNeedFirstList = customer.getCustomerCompany().getCustomerCompanyNeedFirstList();
            for (CustomerCompanyNeed customerCompanyNeed : customerCompanyNeedFirstList) {
                BigDecimal totalPrice = BigDecimalUtil.mul(customerCompanyNeed.getUnitPrice(), new BigDecimal(customerCompanyNeed.getRentCount()));
                customerCompanyNeed.setTotalPrice(totalPrice);
                ServiceResult<String, Product> productServiceResult = productService.queryProductBySkuId(customerCompanyNeed.getSkuId());
                customerCompanyNeed.setProduct(productServiceResult.getResult());
            }
            newCustomerCompanyDO.setCustomerCompanyNeedFirstJson(JSON.toJSON(customerCompanyNeedFirstList).toString());
        }

        //判断后续所需设备
        if (CollectionUtil.isNotEmpty(customer.getCustomerCompany().getCustomerCompanyNeedLaterList())) {
            List<CustomerCompanyNeed> customerCompanyNeedLaterList = customer.getCustomerCompany().getCustomerCompanyNeedFirstList();
            for (CustomerCompanyNeed customerCompanyNeed : customerCompanyNeedLaterList) {
                BigDecimal totalPrice = BigDecimalUtil.mul(customerCompanyNeed.getUnitPrice(), new BigDecimal(customerCompanyNeed.getRentCount()));
                customerCompanyNeed.setTotalPrice(totalPrice);
                ServiceResult<String, Product> productServiceResult = productService.queryProductBySkuId(customerCompanyNeed.getSkuId());
                customerCompanyNeed.setProduct(productServiceResult.getResult());
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
        businessLicensePictureImageList.add(customer.getCustomerCompany().getBusinessLicensePictureImage());
        serviceResult = updateImage(businessLicensePictureImageList, ImgType.BUSINESS_LICENSE_PICTURE_IMG_TYPE, customerDO.getId().toString(), userSupport.getCurrentUserId().toString(), now);
        if (!ErrorCode.SUCCESS.equals(serviceResult.getErrorCode())) {
            serviceResult.setErrorCode(serviceResult.getErrorCode(), serviceResult.getFormatArgs());
            return serviceResult;
        }


        //对身份证正面图片操作
        List<Image> legalPersonNoPictureFrontImageList = new ArrayList<>();
        legalPersonNoPictureFrontImageList.add(customer.getCustomerCompany().getLegalPersonNoPictureFrontImage());
        serviceResult = updateImage(legalPersonNoPictureFrontImageList, ImgType.LEGAL_PERSON_NO_PICTURE_FRONT_IMG_TYPE, customerDO.getId().toString(), userSupport.getCurrentUserId().toString(), now);
        if (!ErrorCode.SUCCESS.equals(serviceResult.getErrorCode())) {
            serviceResult.setErrorCode(serviceResult.getErrorCode(), serviceResult.getFormatArgs());
            return serviceResult;
        }


        //对身份证反面图片操作
        List<Image> legalPersonNoPictureBackImageList = new ArrayList<>();
        legalPersonNoPictureBackImageList.add(customer.getCustomerCompany().getLegalPersonNoPictureBackImage());
        serviceResult = updateImage(legalPersonNoPictureBackImageList, ImgType.LEGAL_PERSON_NO_PICTURE_BACK_IMG_TYPE, customerDO.getId().toString(), userSupport.getCurrentUserId().toString(), now);
        if (!ErrorCode.SUCCESS.equals(serviceResult.getErrorCode())) {
            serviceResult.setErrorCode(serviceResult.getErrorCode(), serviceResult.getFormatArgs());
            return serviceResult;
        }

        //对经营场所租赁合同图片操作
        serviceResult = updateImage(customer.getCustomerCompany().getManagerPlaceRentContractImageList(), ImgType.MANAGER_PLACE_RENT_CONTRACT_IMG_TYPE, customerDO.getId().toString(), userSupport.getCurrentUserId().toString(), now);
        if (!ErrorCode.SUCCESS.equals(serviceResult.getErrorCode())) {
            serviceResult.setErrorCode(serviceResult.getErrorCode(), serviceResult.getFormatArgs());
            return serviceResult;
        }

        //对法人个人征信报告或附（法人个人征信授权书）图片操作
        serviceResult = updateImage(customer.getCustomerCompany().getLegalPersonCreditReportImageList(), ImgType.LEGAL_PERSON_CREDIT_REPORT_IMG_TYPE, customerDO.getId().toString(), userSupport.getCurrentUserId().toString(), now);
        if (!ErrorCode.SUCCESS.equals(serviceResult.getErrorCode())) {
            serviceResult.setErrorCode(serviceResult.getErrorCode(), serviceResult.getFormatArgs());
            return serviceResult;
        }

        //对固定资产证明图片操作
        serviceResult = updateImage(customer.getCustomerCompany().getFixedAssetsProveImageList(), ImgType.FIXED_ASSETS_PROVE_IMG_TYPE, customerCompanyDO.getId().toString(), userSupport.getCurrentUserId().toString(), now);
        if (!ErrorCode.SUCCESS.equals(serviceResult.getErrorCode())) {
            serviceResult.setErrorCode(serviceResult.getErrorCode(), serviceResult.getFormatArgs());
            return serviceResult;
        }

        //对单位对公账户流水账单图片操作
        serviceResult = updateImage(customer.getCustomerCompany().getPublicAccountFlowBillImageList(), ImgType.PUBLIC_ACCOUNT_FLOW_BILL_IMG_TYPE, customerDO.getId().toString(), userSupport.getCurrentUserId().toString(), now);
        if (!ErrorCode.SUCCESS.equals(serviceResult.getErrorCode())) {
            serviceResult.setErrorCode(serviceResult.getErrorCode(), serviceResult.getFormatArgs());
            return serviceResult;
        }

        //对社保/公积金缴纳证明图片操作
        serviceResult = updateImage(customer.getCustomerCompany().getSocialSecurityRoProvidentFundImageList(), ImgType.SOCIAL_SECURITY_RO_PROVIDENT_FUND_IMG_TYPE, customerDO.getId().toString(), userSupport.getCurrentUserId().toString(), now);
        if (!ErrorCode.SUCCESS.equals(serviceResult.getErrorCode())) {
            serviceResult.setErrorCode(serviceResult.getErrorCode(), serviceResult.getFormatArgs());
            return serviceResult;
        }

        //对战略协议或合作协议图片操作
        serviceResult = updateImage(customer.getCustomerCompany().getCooperationAgreementImageList(), ImgType.COOPERATION_AGREEMENT_IMG_TYPE, customerDO.getId().toString(), userSupport.getCurrentUserId().toString(), now);
        if (!ErrorCode.SUCCESS.equals(serviceResult.getErrorCode())) {
            serviceResult.setErrorCode(serviceResult.getErrorCode(), serviceResult.getFormatArgs());
            return serviceResult;
        }

        //对法人学历证明图片操作
        serviceResult = updateImage(customer.getCustomerCompany().getLegalPersonEducationImageList(), ImgType.LEGAL_PERSON_EDUCATION_IMG_TYPE, customerDO.getId().toString(), userSupport.getCurrentUserId().toString(), now);
        if (!ErrorCode.SUCCESS.equals(serviceResult.getErrorCode())) {
            serviceResult.setErrorCode(serviceResult.getErrorCode(), serviceResult.getFormatArgs());
            return serviceResult;
        }

        //对法人职称证明图片操作.
        serviceResult = updateImage(customer.getCustomerCompany().getLegalPersonPositionalTitleImageList(), ImgType.LEGAL_PERSON_POSITIONAL_TITLE_IMG_TYPE, customerDO.getId().toString(), userSupport.getCurrentUserId().toString(), now);
        if (!ErrorCode.SUCCESS.equals(serviceResult.getErrorCode())) {
            serviceResult.setErrorCode(serviceResult.getErrorCode(), serviceResult.getFormatArgs());
            return serviceResult;
        }

        //对现场核查表图片操作
        serviceResult = updateImage(customer.getCustomerCompany().getLocaleChecklistsImageList(), ImgType.LOCALE_CHECKLISTS_IMG_TYPE, customerDO.getId().toString(), userSupport.getCurrentUserId().toString(), now);
        if (!ErrorCode.SUCCESS.equals(serviceResult.getErrorCode())) {
            serviceResult.setErrorCode(serviceResult.getErrorCode(), serviceResult.getFormatArgs());
            return serviceResult;
        }

        //对其他材料图片操作
        serviceResult = updateImage(customer.getCustomerCompany().getOtherDateImageList(), ImgType.OTHER_DATE_IMG_TYPE, customerDO.getId().toString(), userSupport.getCurrentUserId().toString(), now);
        if (!ErrorCode.SUCCESS.equals(serviceResult.getErrorCode())) {
            serviceResult.setErrorCode(serviceResult.getErrorCode(), serviceResult.getFormatArgs());
            return serviceResult;
        }
        //更改联合开发人
        if (customer.getUnionUser() != null) {
            customerDO.setUnionUser(customer.getUnionUser());
            customerDO.setUpdateTime(now);
            customerDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        }
        customerDO.setCustomerName(newCustomerCompanyDO.getCompanyName());
        customerDO.setUpdateTime(now);
        customerDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        customerDO.setIsDisabled(customer.getIsDisabled());
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
        if(dbCustomerDO != null && !dbCustomerDO.getCustomerNo().equals(customer.getCustomerNo())){
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_IS_EXISTS);
            return serviceResult;
        }
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

        //更改联合开发人
        if (customer.getUnionUser() != null) {
            customerDO.setUnionUser(customer.getUnionUser());
        }
        customerDO.setCustomerName(newCustomerPersonDO.getRealName());
        customerDO.setUpdateTime(now);
        customerDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        customerDO.setIsDisabled(customer.getIsDisabled());
        customerDO.setRemark(customer.getRemark());

        customerMapper.update(customerDO);

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(customerDO.getCustomerNo());
        return serviceResult;
    }

    @Override
    public ServiceResult<String, Page<Customer>> pageCustomerCompany(CustomerCompanyQueryParam customerCompanyQueryParam) {
        ServiceResult<String, Page<Customer>> result = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(customerCompanyQueryParam.getPageNo(), customerCompanyQueryParam.getPageSize());

        dataAccessSupport.setDataAccessPassiveUserList(customerCompanyQueryParam);

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

        dataAccessSupport.setDataAccessPassiveUserList(customerPersonQueryParam);

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
        //显示联合开发原的省，市，区
        if (customerDO.getUnionUser() != null) {
            Integer companyId = userSupport.getCompanyIdByUser(customerDO.getUnionUser());
            SubCompanyDO subCompanyDO = subCompanyMapper.findById(companyId);
            customerResult.setUnionAreaProvinceName(subCompanyDO.getProvinceName());
            customerResult.setUnionAreaCityName(subCompanyDO.getCityName());
            customerResult.setUnionAreaDistrictName(subCompanyDO.getDistrictName());
        }
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
            for (Image otherDateImage : customer.getCustomerCompany().getLocaleChecklistsImageList()) {
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
}