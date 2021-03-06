package com.lxzl.erp.dataaccess.dao.mysql.bank;

import com.lxzl.erp.common.domain.bank.pojo.BankSlipClaimDetail;
import com.lxzl.erp.common.domain.bank.pojo.BankSlipClaimPage;
import com.lxzl.erp.common.domain.bank.pojo.dto.BankSipAutomaticClaimDTO;
import com.lxzl.erp.dataaccess.domain.bank.BankSlipClaimDO;
import com.lxzl.erp.dataaccess.domain.customer.CustomerCompanyDO;
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

    List<BankSlipClaimDO> findAmountByBankSlipDetailIdAndCreateUser(@Param("bankSlipDetailId")Integer id,@Param("userId") String userId);

    List<BankSipAutomaticClaimDTO> findBankSlipClaimPaySuccessByName(@Param("list") List<CustomerCompanyDO> customerCompanyDOList);

    BankSlipClaimPage findBankSlipClaimPageCountAndAmountByParams(@Param("maps") Map<String, Object> maps);

    List<BankSlipClaimDetail> findBankSlipClaimDetailByParams(@Param("maps") Map<String, Object> maps);
}