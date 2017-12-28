package com.lxzl.erp.core.service.customer.impl.support;

import com.lxzl.erp.common.domain.customer.pojo.CustomerRiskManagement;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.dataaccess.domain.customer.CustomerRiskManagementDO;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

public class CustomerRiskManagementConverter {


    public static CustomerRiskManagement convertCustomerRiskManagementDO(CustomerRiskManagementDO customerRiskManagementDO){
        if(customerRiskManagementDO==null) return null;
        CustomerRiskManagement customerRiskManagement = new CustomerRiskManagement();
        BeanUtils.copyProperties(customerRiskManagementDO,customerRiskManagement);
        customerRiskManagement.setCustomerRiskManagementId(customerRiskManagementDO.getId());
        return customerRiskManagement;
    }
    public static CustomerRiskManagementDO convertCustomerRiskManagement(CustomerRiskManagement customerRiskManagement){
        if(customerRiskManagement==null) return null;
        CustomerRiskManagementDO customerRiskManagementDO = new CustomerRiskManagementDO();
        BeanUtils.copyProperties(customerRiskManagement,customerRiskManagementDO);
        customerRiskManagementDO.setId(customerRiskManagement.getCustomerRiskManagementId());
        return customerRiskManagementDO;
    }
}
