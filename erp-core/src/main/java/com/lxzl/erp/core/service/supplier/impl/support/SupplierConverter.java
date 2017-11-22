package com.lxzl.erp.core.service.supplier.impl.support;

import com.lxzl.erp.common.domain.supplier.pojo.Supplier;
import com.lxzl.erp.dataaccess.domain.supplier.SupplierDO;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-11-08 20:49
 */
public class SupplierConverter {
    public static List<Supplier> convertSupplierDOList(List<SupplierDO> supplierDOList) {
        List<Supplier> supplierList = new ArrayList<>();
        if (supplierDOList != null && !supplierDOList.isEmpty()) {
            for (SupplierDO supplierDO : supplierDOList) {
                supplierList.add(convertSupplierDO(supplierDO));
            }
        }
        return supplierList;
    }

    public static Supplier convertSupplierDO(SupplierDO supplierDO) {
        Supplier supplier = new Supplier();
        if (supplierDO.getId() != null) {
            supplier.setSupplierId(supplierDO.getId());
        }
        BeanUtils.copyProperties(supplierDO, supplier);
        return supplier;
    }

    public static SupplierDO convertSupplier(Supplier supplier) {
        SupplierDO supplierDO = new SupplierDO();
        if (supplier.getSupplierId() != null) {
            supplierDO.setId(supplier.getSupplierId());
        }
        BeanUtils.copyProperties(supplier, supplierDO);
        return supplierDO;
    }
}
