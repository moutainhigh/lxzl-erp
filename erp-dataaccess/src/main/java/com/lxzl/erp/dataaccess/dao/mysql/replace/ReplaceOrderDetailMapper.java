package com.lxzl.erp.dataaccess.dao.mysql.replace;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.replace.ReplaceOrderDetailDO;import org.apache.ibatis.annotations.Param;
import java.util.List;
import org.springframework.stereotype.Repository;
import java.util.Map;

@Repository
public interface ReplaceOrderDetailMapper extends BaseMysqlDAO<ReplaceOrderDetailDO> {

	List<ReplaceOrderDetailDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);
}