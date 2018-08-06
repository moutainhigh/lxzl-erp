package com.lxzl.erp.common.domain.workbench.pojo;

import java.util.List;
import java.util.Map;

/**
 * @Author: your name
 * @Description：
 * @Date: Created in 17:31 2018/8/3
 * @Modified By:
 */
public class Workbench {
    private List<Map<String,Object>> orderListMap;
    private List<Map<String,Object>> k3ReturnOrderListMap;
    private List<Map<String,Object>> customerListMap;
    private List<Map<String,Object>> workflowListMap ;
    private List<Map<String,Object>> workflowBusinessAffairsListMap ;
    private List<Map<String,Object>> bankSlipDetailBusinessAffairsListMap ;
    private List<Map<String,Object>> statementOrderBusinessAffairsListMap ;

    public List<Map<String, Object>> getWorkflowBusinessAffairsListMap() {
        return workflowBusinessAffairsListMap;
    }

    public void setWorkflowBusinessAffairsListMap(List<Map<String, Object>> workflowBusinessAffairsListMap) {
        this.workflowBusinessAffairsListMap = workflowBusinessAffairsListMap;
    }

    public List<Map<String, Object>> getBankSlipDetailBusinessAffairsListMap() {
        return bankSlipDetailBusinessAffairsListMap;
    }

    public void setBankSlipDetailBusinessAffairsListMap(List<Map<String, Object>> bankSlipDetailBusinessAffairsListMap) {
        this.bankSlipDetailBusinessAffairsListMap = bankSlipDetailBusinessAffairsListMap;
    }

    public List<Map<String, Object>> getStatementOrderBusinessAffairsListMap() {
        return statementOrderBusinessAffairsListMap;
    }

    public void setStatementOrderBusinessAffairsListMap(List<Map<String, Object>> statementOrderBusinessAffairsListMap) {
        this.statementOrderBusinessAffairsListMap = statementOrderBusinessAffairsListMap;
    }

    public List<Map<String, Object>> getOrderListMap() {
        return orderListMap;
    }

    public void setOrderListMap(List<Map<String, Object>> orderListMap) {
        this.orderListMap = orderListMap;
    }

    public List<Map<String, Object>> getK3ReturnOrderListMap() {
        return k3ReturnOrderListMap;
    }

    public void setK3ReturnOrderListMap(List<Map<String, Object>> k3ReturnOrderListMap) {
        this.k3ReturnOrderListMap = k3ReturnOrderListMap;
    }

    public List<Map<String, Object>> getCustomerListMap() {
        return customerListMap;
    }

    public void setCustomerListMap(List<Map<String, Object>> customerListMap) {
        this.customerListMap = customerListMap;
    }

    public List<Map<String, Object>> getWorkflowListMap() {
        return workflowListMap;
    }

    public void setWorkflowListMap(List<Map<String, Object>> workflowListMap) {
        this.workflowListMap = workflowListMap;
    }
}
