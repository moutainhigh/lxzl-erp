package com.lxzl.erp.dataaccess.dao.mysql.bank;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.bank.BankSlipDetailManualClaimAndLocalizationConfigDO;import org.apache.ibatis.annotations.Param;
import java.util.List;
import org.springframework.stereotype.Repository;
import java.util.Map;

@Repository
public interface BankSlipDetailManualClaimAndLocalizationConfigMapper extends BaseMysqlDAO<BankSlipDetailManualClaimAndLocalizationConfigDO> {

	List<BankSlipDetailManualClaimAndLocalizationConfigDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);

	List<BankSlipDetailManualClaimAndLocalizationConfigDO> findAllConfig();
}