package com.lxzl.erp.dataaccess.dao.mysql.printLog;

import com.lxzl.erp.dataaccess.domain.printLog.PrintLogDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface PrintLogMapper extends BaseMysqlDAO<PrintLogDO> {

	List<PrintLogDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);

	List<PrintLogDO> findBankSlipByParams(@Param("maps") Map<String, Object> paramMap);

    Integer findBankSlipCountByParams(@Param("maps") Map<String, Object> paramMap);
}