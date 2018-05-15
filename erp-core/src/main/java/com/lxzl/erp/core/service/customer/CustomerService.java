package com.lxzl.erp.core.service.customer;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.customer.*;
import com.lxzl.erp.common.domain.customer.pojo.*;
import com.lxzl.erp.core.service.VerifyReceiver;

public interface CustomerService extends VerifyReceiver {
    ServiceResult<String,String> addCompany(Customer customer);
    ServiceResult<String,String> addPerson(Customer customer);
    ServiceResult<String,String> updateCompany(Customer customer);
    ServiceResult<String,String> updatePerson(Customer customer);
    ServiceResult<String,String> commitCustomer(Customer customer);
    ServiceResult<String,String> verifyCustomer(String customerNo, Integer customerStatus, String verifyRemark);
    ServiceResult<String,Page<Customer>> pageCustomerCompany(CustomerCompanyQueryParam customerCompanyQueryParam);
    ServiceResult<String,Page<Customer>> pageCustomerPerson(CustomerPersonQueryParam customerPersonQueryParam);
    ServiceResult<String,Customer> detailCustomer(String customerNo);
    Customer detailCustomerTemp(String customerNo);
    ServiceResult<String,Customer> detailCustomerCompany(Customer customer);
    ServiceResult<String,Customer> detailCustomerPerson(Customer customer);
    ServiceResult<String,String> updateRisk(CustomerRiskManagement customerRiskManagement);

    ServiceResult<String,String> updateRiskCreditAmountUsed(CustomerRiskManagement customerRiskManagement);

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

    /**
     * 修改企业客户启用
     * @param customer
     * @return
     */
    ServiceResult<String,String> disabledCustomer(Customer customer);

    /**
     * 修改企业客户禁用
     * @param customer
     * @return
     */
    ServiceResult<String,String> enableCustomer(Customer customer);

    /**
    * 通过公司名称查找
    * @Author : XiaoLuYu--kai
    * @Date : Created in 2018/1/17 9:46  update in 2018/3/7 11:27
    * @param : customerName
    * @Return : com.lxzl.erp.common.domain.ServiceResult<java.lang.String,com.lxzl.erp.common.domain.Page<com.lxzl.erp.common.domain.customer.pojo.Customer>>
    */
    ServiceResult<String, Customer> queryCustomerByCustomerName(String customerName);

    /**
     * 增加短租应收上限金额
     * @param customer
     * @return
     */
    ServiceResult<String,String> addShortReceivableAmount(Customer customer);

    /**
     * 增加客户结算时间
     *
     */
    ServiceResult<String,String> addStatementDate(Customer customer);


    /**
     * 根据编号查询客户信息
     */
    ServiceResult<String, Customer> queryCustomerByNo(String customerNo);

    /**
     * 提交客户审核到工作流
     *
     * @param customerCommitParam
     * @return
     */
    ServiceResult<String,String> commitCustomerToWorkflow(CustomerCommitParam customerCommitParam);

    /**
     * 驳回客户信息（已审批通过的）
     *
     * @param customerRejectParam
     * @return
     */
    ServiceResult<String,String> rejectCustomer(CustomerRejectParam customerRejectParam);
    /**
     * 审核通过修改业务员和联合开发人
     *
     * @param customer
     * @return
     */
    ServiceResult<String, String> updateOwnerAndUnionUser(Customer customer);


    /**
     *  客户风控信息历史记录分页查询
     * @param customerRiskManageHistoryQueryParam
     * @return
     */
    ServiceResult<String,Page<CustomerRiskManagementHistory>> pageCustomerRiskManagementHistory(CustomerRiskManageHistoryQueryParam customerRiskManageHistoryQueryParam);

    /**
     * 客户风控信息历史记录详情
     * @param customerRiskManagementHistoryId
     * @return
     */
    ServiceResult<String,CustomerRiskManagementHistory> detailCustomerRiskManagementHistory(Integer customerRiskManagementHistoryId);

    /**
     *  此方法只用于处理历史数据中公司客户的新增字段simple_company_name字段为空的情况，新增数据不会出现此种情况
     *
     * @return
     */
    ServiceResult<String,String> customerCompanySimpleNameProcessing();

    /**
     * 提交客户地址审核
     *
     * @param customerConsignCommitParam
     * @return
     */
    ServiceResult<String,String> commitCustomerConsignInfo(CustomerConsignCommitParam customerConsignCommitParam);

    /**
     * 新增客户回访记录
     *
     * @param returnVisit
     * @return
     */
    ServiceResult<String,Integer> addCustomerReturnVisit(ReturnVisit returnVisit);

    /**
     * 更改客户回访记录
     *
     * @param returnVisit
     * @return
     */
    ServiceResult<String,String> updateCustomerReturnVisit(ReturnVisit returnVisit);

    /**
     * 取消客户回访记录
     *
     * @param returnVisit
     * @return
     */
    ServiceResult<String,String> deleteCustomerReturnVisit(ReturnVisit returnVisit);

    /**
     * 客户回访详情
     *
     * @param returnVisit
     * @return
     */
    ServiceResult<String,ReturnVisit> detailCustomerReturnVisit(ReturnVisit returnVisit);

    /**
     * 客户回访列表
     *
     * @param customerReturnVisitQueryParam
     * @return
     */
    ServiceResult<String,Page<ReturnVisit>> pageCustomerReturnVisit(CustomerReturnVisitQueryParam customerReturnVisitQueryParam);

    /**
     *将已授信的公司的erp_customer表中的is_risk字段设置为1
     */
    ServiceResult<String,String> setIsRisk();

}
