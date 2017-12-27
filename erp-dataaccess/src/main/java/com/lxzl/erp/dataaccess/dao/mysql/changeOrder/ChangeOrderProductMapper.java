package com.lxzl.erp.dataaccess.dao.mysql.changeOrder;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.changeOrder.ChangeOrderProductDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
@Repository
public interface ChangeOrderProductMapper extends BaseMysqlDAO<ChangeOrderProductDO> {

	List<ChangeOrderProductDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);

	Integer batchSave(@Param("changeOrderId") Integer changeOrderId,@Param("changeOrderNo") String changeOrderNo, @Param("changeOrderProductDOList") List<ChangeOrderProductDO> changeOrderProductDOList);

	Integer batchUpdate(@Param("list") List<ChangeOrderProductDO> changeOrderProductDOList);

	List<ChangeOrderProductDO> findByChangeOrderId(@Param("changeOrderId") Integer changeOrderId);
}