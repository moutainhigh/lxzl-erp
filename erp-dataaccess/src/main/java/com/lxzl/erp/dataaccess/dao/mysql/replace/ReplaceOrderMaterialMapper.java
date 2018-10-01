package com.lxzl.erp.dataaccess.dao.mysql.replace;

import com.lxzl.erp.dataaccess.domain.replace.ReplaceOrderProductDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.replace.ReplaceOrderMaterialDO;import org.apache.ibatis.annotations.Param;
import java.util.List;
import org.springframework.stereotype.Repository;
import java.util.Map;
import java.util.Set;

@Repository
public interface ReplaceOrderMaterialMapper extends BaseMysqlDAO<ReplaceOrderMaterialDO> {

	List<ReplaceOrderMaterialDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);

    void saveList(@Param("list") List<ReplaceOrderProductDO> saveReplaceOrderProductDOList);

    void updateListForCancel(@Param("replaceOrderMaterialDOList") List<ReplaceOrderMaterialDO> replaceOrderMaterialDOList);

    ReplaceOrderMaterialDO findByOldMaterialIdAndReplaceId(@Param("oldOrderMaterialId") Integer oldOrderMaterialId,@Param("replaceOrderId") Integer replaceOrderId);

    ReplaceOrderMaterialDO findByNewMaterialIdAndReplaceId(@Param("newOrderMaterialId") Integer oldOrderMaterialId,@Param("replaceOrderId") Integer replaceOrderId);

    List<ReplaceOrderMaterialDO> listByOrderIds(@Param(value = "orderIds") Set<Integer> orderIds);
}