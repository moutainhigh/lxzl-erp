package com.lxzl.erp.core.service.customer.impl;

import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.CustomerType;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.customer.CustomerCompanyQueryParam;
import com.lxzl.erp.common.domain.customer.CustomerPersonQueryParam;
import com.lxzl.erp.common.domain.customer.pojo.Customer;
import com.lxzl.erp.common.domain.customer.pojo.CustomerCompany;
import com.lxzl.erp.common.domain.customer.pojo.CustomerPerson;
import com.lxzl.erp.common.util.GenerateNoUtil;
import com.lxzl.erp.core.service.customer.CustomerService;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerCompanyMapper;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerMapper;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerPersonMapper;
import com.lxzl.erp.dataaccess.domain.customer.CustomerCompanyDO;
import com.lxzl.erp.dataaccess.domain.customer.CustomerDO;
import com.lxzl.erp.dataaccess.domain.customer.CustomerPersonDO;
import com.lxzl.se.common.util.StringUtil;
import com.lxzl.se.dataaccess.mysql.config.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private CustomerPersonMapper customerPersonMapper;
    @Autowired
    private CustomerCompanyMapper customerCompanyMapper;
    @Autowired
    private UserSupport userSupport;
    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, String> add(Customer customer) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        String checkResult = check(customer);
        if(!ErrorCode.SUCCESS.equals(checkResult)){
            serviceResult.setErrorCode(checkResult);
            return serviceResult;
        }
        Date now = new Date();
        CustomerDO customerDO = new CustomerDO();
        customerDO.setCustomerNo(GenerateNoUtil.generateCustomerNo(now));
        customerDO.setCustomerType(customer.getCustomerType());
        customerDO.setIsDisabled(CommonConstant.COMMON_CONSTANT_YES);
        customerDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        customerDO.setCreateTime(now);
        customerDO.setUpdateTime(now);
        customerDO.setCreateUser(userSupport.getCurrentUserId().toString());
        customerDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        customerMapper.save(customerDO);

        if(CustomerType.CUSTOMER_TYPE_COMPANY.equals(customer.getCustomerType())){
            CustomerCompanyDO customerCompanyDO  = CustomerConverter.convertCustomerCompany(customer.getCustomerCompany());
            customerCompanyDO.setCustomerId(customerDO.getId());
            customerCompanyDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
            customerCompanyDO.setCreateTime(now);
            customerCompanyDO.setUpdateTime(now);
            customerCompanyDO.setCreateUser(userSupport.getCurrentUserId().toString());
            customerCompanyDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            customerCompanyMapper.save(customerCompanyDO);
        }else if(CustomerType.CUSTOMER_TYPE_PERSON.equals(customer.getCustomerType())){
            CustomerPersonDO customerPersonDO  = CustomerConverter.convertCustomerPerson(customer.getCustomerPerson());
            customerPersonDO.setCustomerId(customerDO.getId());
            customerPersonDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
            customerPersonDO.setCreateTime(now);
            customerPersonDO.setUpdateTime(now);
            customerPersonDO.setCreateUser(userSupport.getCurrentUserId().toString());
            customerPersonDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            customerPersonMapper.save(customerPersonDO);
        }
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(customer.getCustomerNo());
        return serviceResult;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, String> update(Customer customer) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        Date now = new Date();
        CustomerDO customerDO = customerMapper.findByNo(customer.getCustomerNo());
        if(customerDO==null){
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_NOT_NULL);
            return serviceResult;
        }
        customer.setCustomerType(customerDO.getCustomerType());
        String checkResult = check(customer);
        if(!ErrorCode.SUCCESS.equals(checkResult)){
            serviceResult.setErrorCode(checkResult);
            return serviceResult;
        }

        if(CustomerType.CUSTOMER_TYPE_COMPANY.equals(customer.getCustomerType())){
            CustomerCompanyDO customerCompanyDO = customerCompanyMapper.findByCustomerId(customerDO.getId());
            CustomerCompanyDO newCustomerCompanyDO = CustomerConverter.convertCustomerCompany(customer.getCustomerCompany());
            newCustomerCompanyDO.setDataStatus(null);
            newCustomerCompanyDO.setCreateTime(null);
            newCustomerCompanyDO.setCreateUser(null);
            newCustomerCompanyDO.setUpdateTime(now);
            newCustomerCompanyDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            newCustomerCompanyDO.setId(customerCompanyDO.getId());
            customerCompanyMapper.update(newCustomerCompanyDO);
        }
        if(CustomerType.CUSTOMER_TYPE_PERSON.equals(customer.getCustomerType())){
            CustomerPersonDO customerPersonDO = customerPersonMapper.findByCustomerId(customerDO.getId());
            CustomerPersonDO newCustomerPersonDO = CustomerConverter.convertCustomerPerson(customer.getCustomerPerson());
            newCustomerPersonDO.setDataStatus(null);
            newCustomerPersonDO.setCreateTime(null);
            newCustomerPersonDO.setCreateUser(null);
            newCustomerPersonDO.setUpdateTime(now);
            newCustomerPersonDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            newCustomerPersonDO.setId(customerPersonDO.getId());
            customerPersonMapper.update(newCustomerPersonDO);
        }
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
    public ServiceResult<String, Page<CustomerCompany>> pageCustomerCompany(CustomerCompanyQueryParam customerCompanyQueryParam) {
        ServiceResult<String, Page<CustomerCompany>> result = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(customerCompanyQueryParam.getPageNo(), customerCompanyQueryParam.getPageSize());
        customerCompanyQueryParam.setCustomerId(null);
        if(StringUtil.isNotEmpty(customerCompanyQueryParam.getCustomerNo())){
            CustomerDO customerDO = customerMapper.findByNo(customerCompanyQueryParam.getCustomerNo());
            if(customerDO==null){
                result.setErrorCode(ErrorCode.SUCCESS);
                Page<CustomerCompany> page = new Page<>(new ArrayList<CustomerCompany>(), 0, customerCompanyQueryParam.getPageNo(), customerCompanyQueryParam.getPageSize());
                result.setResult(page);
                return result;
            }
            customerCompanyQueryParam.setCustomerId(customerDO.getId());
        }
        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        maps.put("queryParam", customerCompanyQueryParam);

        Integer totalCount = customerCompanyMapper.findCustomerCompanyCountByParams(maps);
        List<CustomerCompanyDO> purchaseOrderDOList = customerCompanyMapper.findCustomerCompanyByParams(maps);
        List<CustomerCompany> customerCompanyList = CustomerConverter.convertCustomerCompanyDOList(purchaseOrderDOList);
        Page<CustomerCompany> page = new Page<>(customerCompanyList, totalCount, customerCompanyQueryParam.getPageNo(), customerCompanyQueryParam.getPageSize());

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(page);
        return result;
    }

    @Override
    public ServiceResult<String, Page<CustomerPerson>> pageCustomerPerson(CustomerPersonQueryParam customerPersonQueryParam) {
        ServiceResult<String, Page<CustomerPerson>> result = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(customerPersonQueryParam.getPageNo(), customerPersonQueryParam.getPageSize());
        customerPersonQueryParam.setCustomerId(null);
        if(StringUtil.isNotEmpty(customerPersonQueryParam.getCustomerNo())){
            CustomerDO customerDO = customerMapper.findByNo(customerPersonQueryParam.getCustomerNo());
            if(customerDO==null){
                result.setErrorCode(ErrorCode.SUCCESS);
                Page<CustomerPerson> page = new Page<>(new ArrayList<CustomerPerson>(), 0, customerPersonQueryParam.getPageNo(), customerPersonQueryParam.getPageSize());
                result.setResult(page);
                return result;
            }
            customerPersonQueryParam.setCustomerId(customerDO.getId());
        }
        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        maps.put("queryParam", customerPersonQueryParam);

        Integer totalCount = customerPersonMapper.findCustomerPersonCountByParams(maps);
        List<CustomerPersonDO> customerPersonDOList = customerPersonMapper.findCustomerPersonByParams(maps);
        List<CustomerPerson> customerPersonList = CustomerConverter.convertCustomerPersonDOList(customerPersonDOList);
        Page<CustomerPerson> page = new Page<>(customerPersonList, totalCount, customerPersonQueryParam.getPageNo(), customerPersonQueryParam.getPageSize());

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(page);
        return result;
    }

    private String check(Customer customer){
        if(CustomerType.CUSTOMER_TYPE_COMPANY.equals(customer.getCustomerType())){

            if(customer.getCustomerCompany()==null){
                return ErrorCode.CUSTOMER_COMPANY_NOT_NULL;
            }
            if(StringUtil.isEmpty(customer.getCustomerCompany().getCompanyName())){
                return ErrorCode.CUSTOMER_COMPANY_NAME_NOT_NULL;
            }
            if(StringUtil.isEmpty(customer.getCustomerCompany().getConnectRealName())){
                return ErrorCode.CUSTOMER_COMPANY_CONNECT_NAME_NOT_NULL;
            }
        }
        if(CustomerType.CUSTOMER_TYPE_PERSON.equals(customer.getCustomerType())){

            if(customer.getCustomerPerson()==null){
                return ErrorCode.CUSTOMER_PERSON_NOT_NULL;
            }
            if(StringUtil.isEmpty(customer.getCustomerPerson().getRealName())){
                return ErrorCode.CUSTOMER_PERSON_NAME_NOT_NULL;
            }
        }
        return ErrorCode.SUCCESS;
    }
}
