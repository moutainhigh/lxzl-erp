package com.lxzl.erp.core.service.supplier;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.supplier.SupplierQueryParam;
import com.lxzl.erp.common.domain.supplier.pojo.Supplier;
import com.lxzl.se.core.service.BaseService;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-11-08 20:42
 */
public interface SupplierService extends BaseService {

    /**
     * 查询供应商列表
     *
     * @param supplierQueryParam 查询供应商参数
     * @return 供应商列表
     */
    ServiceResult<String, Page<Supplier>> getSupplier(SupplierQueryParam supplierQueryParam);

    /**
     * 查询供应商信息
     *
     * @param supplierNo 供应商编号
     * @return 供应商信息
     */
    ServiceResult<String, Supplier> getSupplierByNo(String supplierNo);

    /**
     * 添加供应商
     *
     * @param supplier 供应商信息
     * @return 供应商编号
     */
    ServiceResult<String, String> addSupplier(Supplier supplier);

    /**
     * 编辑供应商
     *
     * @param supplier 供应商信息
     * @return 供应商编号
     */
    ServiceResult<String, Integer> updateSupplier(Supplier supplier);

    /**
     * 删除供应商
     *
     * @param supplierNo 供应商编号
     * @return 供应商编号
     */
    ServiceResult<String, String> deleteSupplier(String supplierNo);
}
