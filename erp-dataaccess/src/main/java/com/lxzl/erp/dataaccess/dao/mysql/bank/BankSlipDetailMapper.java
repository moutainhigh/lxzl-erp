package com.lxzl.erp.dataaccess.dao.mysql.bank;

import com.lxzl.erp.dataaccess.domain.bank.BankSlipDetailDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface BankSlipDetailMapper extends BaseMysqlDAO<BankSlipDetailDO> {

	List<BankSlipDetailDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);

    Integer findBankSlipDetailDOCountByParams(@Param("maps")Map<String, Object> maps);

	List<BankSlipDetailDO> findBankSlipDetailDOByParams(@Param("maps")Map<String, Object> maps);

    List<BankSlipDetailDO> findClaimedByBankSlipId(@Param("bankSlipId") Integer bankSlipId);

    void updateConfirmBankDetailDO(@Param("list") List<BankSlipDetailDO> bankSlipDetailDOList);

    void saveBankSlipDetailDOList(@Param("list") List<BankSlipDetailDO> bankSlipDetailDOList);

}