package com.lxzl.erp.dataaccess.dao.mysql.replace;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.replace.ReplaceOrderProductDO;import org.apache.ibatis.annotations.Param;
import java.util.List;
import org.springframework.stereotype.Repository;
import java.util.Map;
import java.util.Set;

@Repository
public interface ReplaceOrderProductMapper extends BaseMysqlDAO<ReplaceOrderProductDO> {

	List<ReplaceOrderProductDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);

    void saveList(@Param("list") List<ReplaceOrderProductDO> saveReplaceOrderProductDOList);

    void updateListForCancel(@Param("replaceOrderProductDOList") List<ReplaceOrderProductDO> replaceOrderProductDOList);

    ReplaceOrderProductDO findByOldProductIdAndRepalceId(@Param("oldProductId")Integer oldProductId,@Param("replaceOrderId")Integer replaceOrderId);
    ReplaceOrderProductDO findByNewProductIdAndRepalceId(@Param("newProductId")Integer oldProductId,@Param("replaceOrderId")Integer replaceOrderId);

    List<ReplaceOrderProductDO> listByOrderIds(@Param(value = "orderIds") Set<Integer> orderIds);
}