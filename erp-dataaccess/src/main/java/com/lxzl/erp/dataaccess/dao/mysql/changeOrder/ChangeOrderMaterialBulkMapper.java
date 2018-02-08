package com.lxzl.erp.dataaccess.dao.mysql.changeOrder;

import com.lxzl.erp.dataaccess.domain.changeOrder.ChangeOrderMaterialBulkDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface ChangeOrderMaterialBulkMapper extends BaseMysqlDAO<ChangeOrderMaterialBulkDO> {

    List<ChangeOrderMaterialBulkDO> listPage(@Param("maps") Map<String, Object> paramMap);

    Integer listCount(@Param("maps") Map<String, Object> paramMap);

    List<ChangeOrderMaterialBulkDO> findByChangeOrderNo(@Param("changeOrderNo") String changeOrderNo);

    void deleteByChangeOrderMaterialId(@Param("changeOrderMaterialId") Integer changeOrderMaterialId, @Param("updateUser") String updateUser, @Param("updateTime") Date updateTime);

    List<ChangeOrderMaterialBulkDO> findByChangeOrderMaterialId(@Param("changeOrderMaterialId") Integer changeOrderMaterialId);

}