package com.lxzl.erp.core.service.supplier.impl;

import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.supplier.SupplierQueryParam;
import com.lxzl.erp.common.domain.supplier.pojo.Supplier;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.common.util.GenerateNoUtil;
import com.lxzl.erp.core.service.supplier.SupplierService;
import com.lxzl.erp.core.service.supplier.impl.support.SupplierConverter;
import com.lxzl.erp.dataaccess.dao.mysql.supplier.SupplierMapper;
import com.lxzl.erp.dataaccess.domain.supplier.SupplierDO;
import com.lxzl.se.common.util.StringUtil;
import com.lxzl.se.dataaccess.mysql.config.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Date;
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

    @Autowired
    private HttpSession session;

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

    @Override
    public ServiceResult<String, Supplier> getSupplierByNo(String supplierNo) {
        ServiceResult<String, Supplier> result = new ServiceResult<>();
        SupplierDO supplierDO = supplierMapper.findByNo(supplierNo);
        if (supplierDO == null) {
            result.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return result;
        }
        result.setResult(SupplierConverter.convertSupplierDO(supplierDO));
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    public ServiceResult<String, String> addSupplier(Supplier supplier) {
        ServiceResult<String, String> result = new ServiceResult<>();
        User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
        Date currentTime = new Date();
        String verifyCode = verifySupplier(supplier);
        if (!ErrorCode.SUCCESS.equals(verifyCode)) {
            result.setErrorCode(verifyCode);
            return result;
        }

        SupplierDO supplierDO = SupplierConverter.convertSupplier(supplier);
        supplierDO.setSupplierNo(GenerateNoUtil.generateSupplierNo(currentTime));
        supplierDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        supplierDO.setUpdateUser(loginUser.getUserId().toString());
        supplierDO.setCreateUser(loginUser.getUserId().toString());
        supplierDO.setUpdateTime(currentTime);
        supplierDO.setCreateTime(currentTime);
        supplierMapper.save(supplierDO);

        result.setResult(supplierDO.getSupplierNo());
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    public ServiceResult<String, String> updateSupplier(Supplier supplier) {
        ServiceResult<String, String> result = new ServiceResult<>();
        User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
        Date currentTime = new Date();
        SupplierDO dbSupplierDO = supplierMapper.findByNo(supplier.getSupplierNo());
        if (dbSupplierDO == null) {
            result.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return result;
        }

        SupplierDO supplierDO = SupplierConverter.convertSupplier(supplier);
        supplierDO.setId(dbSupplierDO.getId());
        supplierDO.setSupplierNo(dbSupplierDO.getSupplierNo());
        supplierDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        supplierDO.setUpdateUser(loginUser.getUserId().toString());
        supplierDO.setCreateUser(loginUser.getUserId().toString());
        supplierDO.setUpdateTime(currentTime);
        supplierDO.setCreateTime(currentTime);
        supplierMapper.update(supplierDO);

        result.setResult(supplierDO.getSupplierNo());
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    public ServiceResult<String, String> deleteSupplier(String supplierNo) {
        ServiceResult<String, String> result = new ServiceResult<>();

        result.setErrorCode(ErrorCode.SYSTEM_DEVELOPING);
        return result;
    }


    private String verifySupplier(Supplier supplier) {
        if (supplier == null) {
            return ErrorCode.PARAM_IS_NOT_NULL;
        }
        if (StringUtil.isBlank(supplier.getSupplierName())) {
            return ErrorCode.PARAM_IS_NOT_ENOUGH;
        }

        return ErrorCode.SUCCESS;
    }


}
