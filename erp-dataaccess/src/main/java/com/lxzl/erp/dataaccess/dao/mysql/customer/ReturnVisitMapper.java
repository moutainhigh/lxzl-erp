package com.lxzl.erp.dataaccess.dao.mysql.customer;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.customer.ReturnVisitDO;import org.apache.ibatis.annotations.Param;
import java.util.List;
import org.springframework.stereotype.Repository;
import java.util.Map;

@Repository
public interface ReturnVisitMapper extends BaseMysqlDAO<ReturnVisitDO> {

	List<ReturnVisitDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);

    Integer findReturnVisitCountByParams(@Param("maps")Map<String, Object> maps);

	List<ReturnVisitDO> findReturnVisitByParams(@Param("maps")Map<String, Object> maps);

	ReturnVisitDO findDetailById(@Param("returnVisitId")Integer returnVisitId);
}