package com.lxzl.erp.dataaccess.dao.mysql.bank;

import com.lxzl.erp.common.domain.bank.pojo.BankSlip;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.bank.BankSlipDO;import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Repository;
import java.util.Map;

@Repository
public interface BankSlipMapper extends BaseMysqlDAO<BankSlipDO> {

	List<BankSlipDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);

	Integer findBankSlipCountByParams(@Param("maps")Map<String, Object> maps);

	List<BankSlipDO> findBankSlipByParams(@Param("maps")Map<String, Object> maps);

	BankSlipDO findBySubCompanyIdAndDayAndBankType(@Param("subCompanyId") Integer subCompanyId,@Param("slipDay")  Date day,@Param("bankType") Integer bankType);

	BankSlipDO findBankSlipAndBankSlipDetailByParams(@Param("maps") Map<String, Object> map);

    BankSlipDO findDetailById(@Param("id") Integer bankSlipId);

    void updateBankSlipDO(@Param("list") List<BankSlipDO> bankSlipDOList);

}