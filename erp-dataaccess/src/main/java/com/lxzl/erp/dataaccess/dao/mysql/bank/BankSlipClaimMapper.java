package com.lxzl.erp.dataaccess.dao.mysql.bank;

import com.lxzl.erp.dataaccess.domain.bank.BankSlipClaimDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface BankSlipClaimMapper extends BaseMysqlDAO<BankSlipClaimDO> {

    List<BankSlipClaimDO> listPage(@Param("maps") Map<String, Object> paramMap);

    Integer listCount(@Param("maps") Map<String, Object> paramMap);

    void deleteByBankSlipDetailId(@Param("bankSlipDetailId") Integer bankSlipDetailId, @Param("updateUser") String updateUser, @Param("updateTime") Date updateTime);

    void updateBankSlipClaimDO(@Param("list") List<BankSlipClaimDO> newDankSlipClaimDOList);

    List<BankSlipClaimDO> findByOtherSideAccountNo(@Param("otherSideAccountNo") String otherSideAccountNo);

    void saveBankSlipClaimDO(@Param("list") List<BankSlipClaimDO> bankSlipClaimDOList);

    List<BankSlipClaimDO> findBankSlipClaimPaySuccess();

    void deleteBankSlipClaimDO(@Param("updateUser")String updateUser, @Param("updateTime")Date updateTime,@Param("list")List<BankSlipClaimDO> bankSlipClaimDOList);

    Integer findAmountByBankSlipDetailId(@Param("bankSlipDetailId")Integer id);

}