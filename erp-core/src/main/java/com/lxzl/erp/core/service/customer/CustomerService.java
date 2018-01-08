package com.lxzl.erp.core.service.customer;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.customer.CustomerCompanyQueryParam;
import com.lxzl.erp.common.domain.customer.CustomerConsignInfoQueryParam;
import com.lxzl.erp.common.domain.customer.CustomerPersonQueryParam;
import com.lxzl.erp.common.domain.customer.pojo.*;

public interface CustomerService {
    ServiceResult<String,String> addCompany(Customer customer);
    ServiceResult<String,String> addPerson(Customer customer);
    ServiceResult<String,String> updateCompany(Customer customer);
    ServiceResult<String,String> updatePerson(Customer customer);
    ServiceResult<String,String> commitCustomer(String customerNo);
    ServiceResult<String,String> verifyCustomer(String customerNo, Integer customerStatus, String verifyRemark);
    ServiceResult<String,Page<Customer>> pageCustomerCompany(CustomerCompanyQueryParam customerCompanyQueryParam);
    ServiceResult<String,Page<Customer>> pageCustomerPerson(CustomerPersonQueryParam customerPersonQueryParam);
    ServiceResult<String,Customer> detailCustomer(String customerNo);
    ServiceResult<String,Customer> detailCustomerCompany(Customer customer);
    ServiceResult<String,Customer> detailCustomerPerson(Customer customer);
    ServiceResult<String,String> updateRisk(CustomerRiskManagement customerRiskManagement);

    /**
     * 新增收货地址信息
     *
     * @param customerConsignInfo 
     * @return 新增的收获地址信息ID
     */
    ServiceResult<String,Integer> addCustomerConsignInfo(CustomerConsignInfo customerConsignInfo);

    /**
     * 更新收货地址信息
     *
     * @param customerConsignInfo
     * @return 更新完成后收获地址信息ID
     */
    ServiceResult<String,Integer> updateCustomerConsignInfo(CustomerConsignInfo customerConsignInfo);

    /**
     *删除收货地址信息
     *
     * @param customerConsignInfo 收货地址ID
     * @return
     */
    ServiceResult<String,Integer> deleteCustomerConsignInfo(CustomerConsignInfo customerConsignInfo);

    /**
     *收货地址详情查看
     *
     * @param customerConsignInfo 收货地址ID
     * @return 收货地址信息
     */
    ServiceResult<String,CustomerConsignInfo> detailCustomerConsignInfo(CustomerConsignInfo customerConsignInfo);

    /**
     *收货地址信息分页显示
     *
     * @param customerConsignInfoQueryParam 客户的编号
     * @return 该客户所有的地址信息
     */
    ServiceResult<String,Page<CustomerConsignInfo>> pageCustomerConsignInfo(CustomerConsignInfoQueryParam customerConsignInfoQueryParam);

    /**
     *修改收货地址的默认地址
     *
     * @param customerConsignInfo 地址信息的ID
     * @return 修改的地址信息的ID
     */
    ServiceResult<String,Integer> updateAddressIsMain(CustomerConsignInfo customerConsignInfo);

    /**
     * 刷新客户地址最后使用时间
     *
     * @param customerConsignInfo 地址信息的ID
     */
    void updateLastUseTime(Integer customerConsignInfo);
}
