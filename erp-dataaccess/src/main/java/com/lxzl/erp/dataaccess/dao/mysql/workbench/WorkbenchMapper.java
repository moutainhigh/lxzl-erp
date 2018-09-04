package com.lxzl.erp.dataaccess.dao.mysql.workbench;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * @Author: your name
 * @Description：
 * @Date: Created in 18:40 2018/8/3
 * @Modified By:
 */
@Repository
public interface WorkbenchMapper extends BaseMysqlDAO {

    Map<String,Integer> findOrderWorkbenchCount(@Param("maps")Map<String, Object> maps);

    Map<String,Integer> findK3ReturnOrderWorkbenchCount(@Param("maps")Map<String, Object> maps);

    Map<String,Integer> findCustomerWorkbenchCount(@Param("maps")Map<String, Object> maps);

    Map<String,Integer> findWorkflowWorkbenchCountForSales(@Param("maps")Map<String, Object> maps);

    Map<String,Integer> findWorkflowBusinessAffairsWorkbenchCount(@Param("maps")Map<String, Object> paramMap);

    Map<String,Integer> findBankSlipDetailBusinessAffairsWorkbenchCount(@Param("maps")Map<String, Object> paramMap);

    Map<String,Integer> findStatementOrderBusinessAffairsWorkbenchCount(@Param("maps")Map<String, Object> paramMap);

    Map<String,Integer> findWorkflowWarehouseWorkbenchCount(@Param("maps")Map<String, Object> paramMap);

    Map<String,Integer> findOrderWarehouseWorkbenchCount(@Param("maps")Map<String, Object> paramMap);

    Map<String,Integer> findReturnOderWarehouseWorkbenchCount(@Param("maps")Map<String, Object> orderWarehouseWorkbenchParamMap);
}
