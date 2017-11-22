package com.lxzl.erp.dataaccess.dao.mysql.supplier;

import com.lxzl.erp.dataaccess.domain.supplier.SupplierDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SupplierMapper extends BaseMysqlDAO<SupplierDO> {

    SupplierDO findByNo(@Param("supplierNo") String supplierNo);

    List<SupplierDO> listPage(@Param("maps") Map<String, Object> paramMap);

    Integer listCount(@Param("maps") Map<String, Object> paramMap);
}