package com.lxzl.erp.dataaccess.dao.mysql.delayedTask;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.delayedTask.DelayedTaskConfigExportStatementDO;import org.apache.ibatis.annotations.Param;
import java.util.List;
import org.springframework.stereotype.Repository;
import java.util.Map;

@Repository
public interface DelayedTaskConfigExportStatementMapper extends BaseMysqlDAO<DelayedTaskConfigExportStatementDO> {

	List<DelayedTaskConfigExportStatementDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);

    DelayedTaskConfigExportStatementDO findByCustomerNo(@Param("statementOrderCustomerNo") String statementOrderCustomerNo);
}