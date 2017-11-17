package com.lxzl.erp.dataaccess.dao.mysql.warehouse;

import com.lxzl.erp.dataaccess.domain.warehouse.WarehouseDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface WarehouseMapper extends BaseMysqlDAO<WarehouseDO>{


    List<WarehouseDO> listPage(@Param("maps") Map<String, Object> paramMap);

    Integer listCount(@Param("maps") Map<String, Object> paramMap);

    WarehouseDO finByNo(@Param("warehouseNo") String warehouseNo);

    /**
     * 根据ID和仓库类型查询仓库，本接口只适用于非普通仓库，因为只有非普通仓库才能确定唯一性
     * @param subCompanyId 公司ID
     * @param warehouseType 仓库类型
     * @return 唯一仓库
     */
    WarehouseDO finByCompanyAndType(@Param("subCompanyId") Integer subCompanyId,
                                    @Param("warehouseType") Integer warehouseType);

}