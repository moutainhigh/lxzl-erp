package com.lxzl.erp.core.service.customer.impl;

import com.lxzl.erp.common.domain.customer.pojo.Customer;
import com.lxzl.erp.common.domain.customer.pojo.CustomerCompany;
import com.lxzl.erp.common.domain.customer.pojo.CustomerPerson;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.dataaccess.domain.customer.CustomerCompanyDO;
import com.lxzl.erp.dataaccess.domain.customer.CustomerDO;
import com.lxzl.erp.dataaccess.domain.customer.CustomerPersonDO;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

public class CustomerConverter {

    public static Customer convertCustomerDO(CustomerDO customerDO){
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDO,customer);
        customer.setCustomerId(customerDO.getId());
        return customer;
    }

    public static List<CustomerCompany> convertCustomerCompanyDOList(List<CustomerCompanyDO> customerCompanyDOList){
        List<CustomerCompany> customerCompanyList  = new ArrayList<>();
        if(CollectionUtil.isNotEmpty(customerCompanyDOList)){
            for(CustomerCompanyDO customerCompanyDO : customerCompanyDOList){
                CustomerCompany customerCompany = convertCustomerCompanyDO(customerCompanyDO);
                customerCompanyList.add(customerCompany);
            }
        }
        return customerCompanyList;
    }
    public static List<CustomerPerson> convertCustomerPersonDOList(List<CustomerPersonDO> customerPersonDOList){
        List<CustomerPerson> customerPersonList  = new ArrayList<>();
        if(CollectionUtil.isNotEmpty(customerPersonDOList)){
            for(CustomerPersonDO customerPersonDO : customerPersonDOList){
                CustomerPerson customerPerson = convertCustomerPersonDO(customerPersonDO);
                customerPersonList.add(customerPerson);
            }
        }
        return customerPersonList;
    }
    public static CustomerDO convertCustomer(Customer customer){
        CustomerDO customerDO = new CustomerDO();
        BeanUtils.copyProperties(customer,customerDO);
        customerDO.setId(customer.getCustomerId());
        return customerDO;
    }
    public static CustomerPersonDO convertCustomerPerson(CustomerPerson customerPerson){
        CustomerPersonDO customerPersonDO = new CustomerPersonDO();
        BeanUtils.copyProperties(customerPerson,customerPersonDO);
        customerPersonDO.setId(customerPerson.getCustomerPersonId());
        return customerPersonDO;
    }

    public static CustomerCompanyDO convertCustomerCompany(CustomerCompany customerCompany){
        CustomerCompanyDO customerCompanyDO = new CustomerCompanyDO();
        BeanUtils.copyProperties(customerCompany,customerCompanyDO);
        customerCompanyDO.setId(customerCompany.getCustomerCompanyId());
        return customerCompanyDO;
    }
    public static CustomerCompany convertCustomerCompanyDO(CustomerCompanyDO customerCompanyDO){
        CustomerCompany customerCompany = new CustomerCompany();
        BeanUtils.copyProperties(customerCompanyDO,customerCompany);
        customerCompany.setCustomerCompanyId(customerCompanyDO.getId());
        return customerCompany;
    }

    public static CustomerPerson convertCustomerPersonDO(CustomerPersonDO customerPersonDO){
        CustomerPerson customerPerson = new CustomerPerson();
        BeanUtils.copyProperties(customerPersonDO,customerPerson);
        customerPerson.setCustomerPersonId(customerPersonDO.getId());
        return customerPerson;
    }
}
