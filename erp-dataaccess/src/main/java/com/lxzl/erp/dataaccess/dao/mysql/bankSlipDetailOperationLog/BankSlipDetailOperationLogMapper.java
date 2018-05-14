package com.lxzl.erp.dataaccess.dao.mysql.bankSlipDetailOperationLog;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.bank.BankSlipDetailOperationLogDO;import org.apache.ibatis.annotations.Param;
import java.util.List;
import org.springframework.stereotype.Repository;
import java.util.Map;

@Repository
public interface BankSlipDetailOperationLogMapper extends BaseMysqlDAO<BankSlipDetailOperationLogDO> {

	List<BankSlipDetailOperationLogDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);

    void saveBankSlipDetailOperationLogDOList(@Param("list") List<BankSlipDetailOperationLogDO> bankSlipDetailOperationLogDOList);
}