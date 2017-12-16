package com.lxzl.erp.core.service.customer.impl;

import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.CustomerType;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.customer.CustomerCompanyQueryParam;
import com.lxzl.erp.common.domain.customer.CustomerConsignInfoQueryParam;
import com.lxzl.erp.common.domain.customer.CustomerPersonQueryParam;
import com.lxzl.erp.common.domain.customer.pojo.Customer;
import com.lxzl.erp.common.domain.customer.pojo.CustomerConsignInfo;
import com.lxzl.erp.common.domain.customer.pojo.CustomerRiskManagement;
import com.lxzl.erp.common.util.ConverterUtil;
import com.lxzl.erp.common.util.GenerateNoUtil;
import com.lxzl.erp.core.service.customer.CustomerService;
import com.lxzl.erp.core.service.customer.impl.support.CustomerRiskManagementConverter;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.dataaccess.dao.mysql.customer.*;
import com.lxzl.erp.dataaccess.domain.customer.*;
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

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, String> addCompany(Customer customer) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        Date now = new Date();
        CustomerDO customerDO = new CustomerDO();
        customerDO.setCustomerNo(GenerateNoUtil.generateCustomerCompanyNo(now));
        customerDO.setCustomerType(CustomerType.CUSTOMER_TYPE_COMPANY);
        if(customer.getIsDisabled()==null){
            customerDO.setIsDisabled(CommonConstant.COMMON_CONSTANT_YES);
        }else{
            customerDO.setIsDisabled(customer.getIsDisabled());
        }
        customerDO.setCustomerName(customer.getCustomerCompany().getCompanyName());
        customerDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        customerDO.setCreateTime(now);
        customerDO.setUpdateTime(now);
        customerDO.setCreateUser(userSupport.getCurrentUserId().toString());
        customerDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        customerMapper.save(customerDO);

        CustomerCompanyDO customerCompanyDO  = CustomerConverter.convertCustomerCompany(customer.getCustomerCompany());
        customerCompanyDO.setCustomerId(customerDO.getId());
        customerCompanyDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        customerCompanyDO.setCreateTime(now);
        customerCompanyDO.setUpdateTime(now);
        customerCompanyDO.setCreateUser(userSupport.getCurrentUserId().toString());
        customerCompanyDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        customerCompanyMapper.save(customerCompanyDO);

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
        customerDO.setCustomerNo(GenerateNoUtil.generateCustomerPersonNo(now));
        customerDO.setCustomerType(CustomerType.CUSTOMER_TYPE_PERSON);
        if(customer.getIsDisabled()==null){
            customerDO.setIsDisabled(CommonConstant.COMMON_CONSTANT_YES);
        }else{
            customerDO.setIsDisabled(customer.getIsDisabled());
        }
        customerDO.setCustomerName(customer.getCustomerPerson().getRealName());
        customerDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        customerDO.setCreateTime(now);
        customerDO.setUpdateTime(now);
        customerDO.setCreateUser(userSupport.getCurrentUserId().toString());
        customerDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        customerMapper.save(customerDO);

        CustomerPersonDO customerPersonDO  = CustomerConverter.convertCustomerPerson(customer.getCustomerPerson());
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
        if(customerDO==null||!CustomerType.CUSTOMER_TYPE_COMPANY.equals(customerDO.getCustomerType())){
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
        if(customerDO==null||!CustomerType.CUSTOMER_TYPE_PERSON.equals(customerDO.getCustomerType())){
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
        if(customerDO==null||!CustomerType.CUSTOMER_TYPE_COMPANY.equals(customerDO.getCustomerType())){
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_NOT_EXISTS);
            return serviceResult;
        }
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(CustomerConverter.convertCustomerDO(customerDO));
        return serviceResult;
    }

    @Override
    public ServiceResult<String, Customer> detailCustomerPerson(Customer customer) {
        ServiceResult<String, Customer> serviceResult = new ServiceResult<>();
        CustomerDO customerDO = customerMapper.findCustomerPersonByNo(customer.getCustomerNo());
        if(customerDO==null||!CustomerType.CUSTOMER_TYPE_PERSON.equals(customerDO.getCustomerType())){
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_NOT_EXISTS);
            return serviceResult;
        }
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(CustomerConverter.convertCustomerDO(customerDO));
        return serviceResult;
    }

    @Override
    public ServiceResult<String, String> updateRisk(CustomerRiskManagement customerRiskManagement) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        CustomerDO customerDO = customerMapper.findByNo(customerRiskManagement.getCustomerNo());
        if(customerDO==null){
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_NOT_EXISTS);
            return serviceResult;
        }
        Date now = new Date();
        if(customerDO.getCustomerRiskManagementDO()==null){//没有风控信息则添加

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
        }else{//有风控信息则修改
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
        if (customerDO == null){
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_NOT_NULL);
            return serviceResult;
        }

        //获取地址信息的内容，存入客户地址信息表
        CustomerConsignInfoDO customerConsignInfoDO =ConverterUtil.convert(customerConsignInfo,CustomerConsignInfoDO.class);
        customerConsignInfoDO.setCustomerId(customerDO.getId());
        customerConsignInfoDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        customerConsignInfoDO.setCreateTime(now);
        customerConsignInfoDO.setUpdateTime(now);
        customerConsignInfoDO.setCreateUser(userSupport.getCurrentUserId().toString());
        customerConsignInfoDO.setUpdateUser(userSupport.getCurrentUserId().toString());

        //新增收货默认地址如果设置为默认，那么该客户其他收货地址设置为非默认地址
        //如果该条地址信息为该客户的唯一一条地址，那么就设置为默认地址
        //判断该条信息是否为该客户的唯一信息
        Integer customerConsignInfoCount =  customerConsignInfoMapper.countByCustomerId(customerDO.getId());
        //如果新增地址信息是该客户的唯一收货信息，那么该地址信息为默认地址
        if (customerConsignInfoCount == 0 ) {
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
     * @param customerConsignInfo
     * @return
     */
    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, Integer> updateCustomerConsignInfo(CustomerConsignInfo customerConsignInfo) {
        ServiceResult<String, Integer> serviceResult = new ServiceResult<>();
        Date now = new Date();
        CustomerConsignInfoDO customerConsignInfoDO = customerConsignInfoMapper.findById(customerConsignInfo.getCustomerConsignInfoId());
        if(customerConsignInfoDO==null){
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_CONSIGN_INFO_NOT_EXISTS);
            return serviceResult;
        }

        //如果传送过来的ismain是1
        if (CommonConstant.COMMON_CONSTANT_YES.equals(customerConsignInfo.getIsMain())){
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
     * @param customerConsignInfo
     * @return
     */
    @Override
    public ServiceResult<String, Integer> deleteCustomerConsignInfo(CustomerConsignInfo customerConsignInfo) {
        ServiceResult<String, Integer> serviceResult = new ServiceResult<>();
        Date now = new Date();
        CustomerConsignInfoDO customerConsignInfoDO = customerConsignInfoMapper.findById(customerConsignInfo.getCustomerConsignInfoId());
        if(customerConsignInfoDO == null){
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
     * @param customerConsignInfo
     * @return
     */
    @Override
    public ServiceResult<String, CustomerConsignInfo> detailCustomerConsignInfo(CustomerConsignInfo customerConsignInfo) {
        ServiceResult<String, CustomerConsignInfo> serviceResult = new ServiceResult<>();


        CustomerConsignInfoDO customerConsignInfoDO = customerConsignInfoMapper.findById(customerConsignInfo.getCustomerConsignInfoId());
        if(customerConsignInfoDO==null){
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_CONSIGN_INFO_NOT_EXISTS);
            return serviceResult;
        }

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(ConverterUtil.convert(customerConsignInfoDO,CustomerConsignInfo.class));
        return serviceResult;
    }


    @Override
    public ServiceResult<String, Page<CustomerConsignInfo>> pageCustomerConsignInfo(CustomerConsignInfoQueryParam customerConsignInfoQueryParam) {
        ServiceResult<String, Page<CustomerConsignInfo>> serviceResult = new ServiceResult<>();

        CustomerDO customerDO = customerMapper.findByNo(customerConsignInfoQueryParam.getCustomerNo());
        if (customerDO == null){
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
        List<CustomerConsignInfo> customerConsignInfoList = ConverterUtil.convertList(customerConsignInfoDOList,CustomerConsignInfo.class);
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
        try{
            CustomerConsignInfoDO customerConsignInfoDO = customerConsignInfoMapper.findById(customerConsignInfoId);
            customerConsignInfoDO.setLastUseTime(new Date());
            customerConsignInfoMapper.update(customerConsignInfoDO);
        }catch (Exception e){
            logger.error("保存客户地址信息最后使用时间错误");
        }catch (Throwable t){
            logger.error("保存客户地址信息最后使用时间错误");
        }
    }
}
