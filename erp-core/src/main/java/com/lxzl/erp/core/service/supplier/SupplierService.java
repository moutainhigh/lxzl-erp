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
    ServiceResult<String, Page<Supplier>> getSupplier(SupplierQueryParam supplierQueryParam);
}
