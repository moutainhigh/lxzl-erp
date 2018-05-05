package com.lxzl.erp.dataaccess.dao.mysql.printLog;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.printLog.PrintLogDO;import org.apache.ibatis.annotations.Param;
import java.util.List;
import org.springframework.stereotype.Repository;
import java.util.Map;

@Repository
public interface PrintLogMapper extends BaseMysqlDAO<PrintLogDO> {

	List<PrintLogDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);

    PrintLogDO findByNoAndType(@Param("referNo") String referNo,@Param("referType") Integer referType);
}