package com.lxzl.erp.dataaccess.dao.mysql.bank;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.bank.BankSlipDetailDO;import org.apache.ibatis.annotations.Param;
import java.util.List;
import org.springframework.stereotype.Repository;
import java.util.Map;

@Repository
public interface BankSlipDetailMapper extends BaseMysqlDAO<BankSlipDetailDO> {

	List<BankSlipDetailDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);
<<<<<<< HEAD

    Integer findBankSlipDetailDOCountByParams(@Param("maps")Map<String, Object> maps);

	List<BankSlipDetailDO> findBankSlipDetailDOByParams(@Param("maps")Map<String, Object> maps);
=======
>>>>>>> d180a3774cc1e9abbaa90e6157361f477411e6b1
}