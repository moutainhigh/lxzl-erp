package com.lxzl.erp.core.service.supplier.impl;

import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.supplier.SupplierQueryParam;
import com.lxzl.erp.common.domain.supplier.pojo.Supplier;
import com.lxzl.erp.core.service.supplier.SupplierService;
import com.lxzl.erp.core.service.supplier.impl.support.SupplierConverter;
import com.lxzl.erp.dataaccess.dao.mysql.supplier.SupplierMapper;
import com.lxzl.erp.dataaccess.domain.supplier.SupplierDO;
import com.lxzl.se.dataaccess.mysql.config.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-11-08 20:47
 */
@Service("supplierService")
public class SupplierServiceImpl implements SupplierService {

    @Autowired
    private SupplierMapper supplierMapper;

    @Override
    public ServiceResult<String, Page<Supplier>> getSupplier(SupplierQueryParam supplierQueryParam) {
        ServiceResult<String, Page<Supplier>> result = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(supplierQueryParam.getPageNo(), supplierQueryParam.getPageSize());
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("start", pageQuery.getStart());
        paramMap.put("pageSize", pageQuery.getPageSize());
        paramMap.put("supplierQueryParam", supplierQueryParam);
        Integer dataCount = supplierMapper.listCount(paramMap);
        List<SupplierDO> dataList = supplierMapper.listPage(paramMap);
        Page<Supplier> page = new Page<>(SupplierConverter.convertSupplierDOList(dataList), dataCount, supplierQueryParam.getPageNo(), supplierQueryParam.getPageSize());
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(page);
        return result;
    }
}
