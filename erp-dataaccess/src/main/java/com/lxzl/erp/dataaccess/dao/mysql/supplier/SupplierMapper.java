package com.lxzl.erp.dataaccess.dao.mysql.supplier;

import com.lxzl.erp.dataaccess.domain.supplier.SupplierDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;
@Repository
public interface SupplierMapper extends BaseMysqlDAO<SupplierDO> {

    SupplierDO findByNo(@Param("supplierNo") String supplierNo);

    SupplierDO findByName(@Param("supplierName") String supplierName);

    SupplierDO findByCode(@Param("supplierCode") String supplierCode);

    List<SupplierDO> listPage(@Param("maps") Map<String, Object> paramMap);

    Integer listCount(@Param("maps") Map<String, Object> paramMap);

    List<SupplierDO> findAllSupplier();
    
}