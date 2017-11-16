package com.lxzl.erp.core.service.customer.impl.support;

import com.lxzl.erp.common.domain.customer.pojo.CustomerRiskManagement;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.dataaccess.domain.customer.CustomerRiskManagementDO;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

public class CustomerRiskManagementConverter {

    public static List<CustomerRiskManagement> convertCustomerRiskManagementDOList(List<CustomerRiskManagementDO> customerRiskManagementDOList){
        List<CustomerRiskManagement> customerRiskManagementList = new ArrayList<>();
        if(CollectionUtil.isNotEmpty(customerRiskManagementDOList)){
            for(CustomerRiskManagementDO customerRiskManagementDO : customerRiskManagementDOList){
                customerRiskManagementList.add(convertCustomerRiskManagementDO(customerRiskManagementDO));
            }
        }
        return customerRiskManagementList;
    }

    public static CustomerRiskManagement convertCustomerRiskManagementDO(CustomerRiskManagementDO customerRiskManagementDO){
        CustomerRiskManagement customerRiskManagement = new CustomerRiskManagement();
        BeanUtils.copyProperties(customerRiskManagementDO,customerRiskManagement);
        customerRiskManagement.setCustomerRiskManagementId(customerRiskManagementDO.getId());
        return customerRiskManagement;
    }
    public static CustomerRiskManagementDO convertCustomerRiskManagement(CustomerRiskManagement customerRiskManagement){
        CustomerRiskManagementDO customerRiskManagementDO = new CustomerRiskManagementDO();
        BeanUtils.copyProperties(customerRiskManagement,customerRiskManagementDO);
        customerRiskManagementDO.setId(customerRiskManagement.getCustomerRiskManagementId());
        return customerRiskManagementDO;
    }
}
