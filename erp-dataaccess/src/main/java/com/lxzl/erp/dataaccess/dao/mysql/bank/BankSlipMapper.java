package com.lxzl.erp.dataaccess.dao.mysql.bank;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.bank.BankSlipDO;import org.apache.ibatis.annotations.Param;
import java.util.List;
import org.springframework.stereotype.Repository;
import java.util.Map;

@Repository
public interface BankSlipMapper extends BaseMysqlDAO<BankSlipDO> {

	List<BankSlipDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);
<<<<<<< HEAD

	Integer findBankSlipCountByParams(@Param("maps")Map<String, Object> maps);

	List<BankSlipDO> findBankSlipByParams(@Param("maps")Map<String, Object> maps);
=======
>>>>>>> d180a3774cc1e9abbaa90e6157361f477411e6b1
}