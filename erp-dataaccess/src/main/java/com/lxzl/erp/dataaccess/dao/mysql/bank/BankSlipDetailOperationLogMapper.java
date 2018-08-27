package com.lxzl.erp.dataaccess.dao.mysql.bank;

import com.lxzl.erp.dataaccess.domain.bank.BankSlipClaimDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.bank.BankSlipDetailOperationLogDO;import org.apache.ibatis.annotations.Param;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Repository;
import java.util.Map;

@Repository
public interface BankSlipDetailOperationLogMapper extends BaseMysqlDAO<BankSlipDetailOperationLogDO> {

	List<BankSlipDetailOperationLogDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);

	void saveBankSlipDetailOperationLogDOList(@Param("list") List<BankSlipDetailOperationLogDO> bankSlipDetailOperationLogDOList);

	Integer findBankSlipDetailOperationLogCountByParams(@Param("maps") Map<String, Object> maps);

	List<BankSlipDetailOperationLogDO> findBankSlipDetailOperationLogByParams(@Param("maps") Map<String, Object> maps);

    void deleteByBankSlipDetailId(@Param("currentUser") String currentUser,@Param("now")  Date now,@Param("bankSlipDetailId") Integer bankSlipDetailId);
}