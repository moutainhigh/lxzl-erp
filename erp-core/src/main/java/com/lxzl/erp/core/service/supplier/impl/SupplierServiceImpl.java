package com.lxzl.erp.core.service.supplier.impl;

import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.supplier.SupplierQueryParam;
import com.lxzl.erp.common.domain.supplier.pojo.Supplier;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.common.util.ConverterUtil;
import com.lxzl.erp.core.service.basic.impl.support.GenerateNoSupport;
import com.lxzl.erp.core.service.supplier.SupplierService;
import com.lxzl.erp.dataaccess.dao.mysql.supplier.SupplierMapper;
import com.lxzl.erp.dataaccess.domain.supplier.SupplierDO;
import com.lxzl.se.common.util.StringUtil;
import com.lxzl.se.dataaccess.mysql.config.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

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

    @Autowired
    private GenerateNoSupport generateNoSupport;

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
        Page<Supplier> page = new Page<>(ConverterUtil.convertList(dataList, Supplier.class), dataCount, supplierQueryParam.getPageNo(), supplierQueryParam.getPageSize());
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
        result.setResult(ConverterUtil.convert(supplierDO, Supplier.class));
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
        //供应商名称（前端填入，校验不重复，判断中英文，若输入中文则判断空格，判断供应商）
        String supplierName = supplier.getSupplierName();
        if(supplierName.matches("^[A-Za-z0-9\\s]+$")){
            SupplierDO nameSupplierDO = supplierMapper.findByName(supplierName);
            if (nameSupplierDO != null) {
                result.setErrorCode(ErrorCode.SUPPLIER_IS_EXISTS);
                return result;
            }
        }else {
            if(supplierName.indexOf(" ") != -1) {
                result.setErrorCode(ErrorCode.SUPPLIER_NAME_IS_NULL);
                return result;
            }
            String checkName = supplierName.replaceAll(" ","");
            SupplierDO nameSupplierDO = supplierMapper.findByName(checkName);
            if (nameSupplierDO != null) {
                result.setErrorCode(ErrorCode.SUPPLIER_IS_EXISTS);
                return result;
            }
        }
        //判断校验自定义不能中文跟长度20
        String supplierCode = supplier.getSupplierCode();
        SupplierDO supplierCodeDO = supplierMapper.findByCode(supplierCode);
        if(!supplierCode.matches("^[A-Za-z0-9-]{0,20}$")){
            result.setErrorCode(ErrorCode.SUPPLIER_CODE_NOT_CN_LENGTH);
            return result;
        }
        if(supplierCodeDO != null){
            result.setErrorCode(ErrorCode.SUPPLIER_CODE_IS_EXISTS);
            return result;
        }
        //考虑英文私或公（限制长度100内），收款帐号为数字长度30
        if(supplier.getBeneficiaryBankName().length() > 100){
            result.setErrorCode(ErrorCode.BENEFICIARY_BANK_NAME_IS_LENGTH);
            return result;
        }
        if(supplier.getBeneficiaryName().length() > 100){
            result.setErrorCode(ErrorCode.BENEFICIARY_NAME_IS_LENGTH);
            return result;
        }
        String beneficiaryAccount = supplier.getBeneficiaryAccount();
        if(!beneficiaryAccount.matches("^[0-9-]{0,30}$")){
            result.setErrorCode(ErrorCode.BENEFICIARY_ACCOUNT_IS_MATH_LENGTH);
            return result;
        }

        SupplierDO supplierDO = ConverterUtil.convert(supplier, SupplierDO.class);
        supplierDO.setSupplierNo(generateNoSupport.generateSupplierNo(supplierDO.getCity()));
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
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, String> updateSupplier(Supplier supplier) {
        ServiceResult<String, String> result = new ServiceResult<>();
        User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
        Date currentTime = new Date();
        SupplierDO dbSupplierDO = supplierMapper.findByNo(supplier.getSupplierNo());
        if (dbSupplierDO == null) {
            result.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return result;
        }
        //供应商名称（前端填入，校验不重复，判断中英文，若输入中文则判断空格，判断供应商）
        if(StringUtil.isBlank(supplier.getSupplierName())){
            result.setErrorCode(ErrorCode.SUPPLIER_NAME_NOT_NULL);
            return result;
        }
        String supplierName = supplier.getSupplierName();
        if(supplierName.matches("^[A-Za-z0-9\\s]+$")){
            SupplierDO nameSupplierDO = supplierMapper.findByName(supplierName);
            if (nameSupplierDO != null && !nameSupplierDO.getSupplierName().equals(dbSupplierDO.getSupplierName())) {
                result.setErrorCode(ErrorCode.SUPPLIER_IS_EXISTS);
                return result;
            }
        }else {
            if(supplierName.indexOf(" ") != -1) {
                result.setErrorCode(ErrorCode.SUPPLIER_NAME_IS_NULL);
                return result;
            }
            String checkName = supplierName.replaceAll(" ","");
            SupplierDO nameSupplierDO = supplierMapper.findByName(checkName);
            if (nameSupplierDO != null && !nameSupplierDO.getSupplierName().equals(dbSupplierDO.getSupplierName())) {
                result.setErrorCode(ErrorCode.SUPPLIER_IS_EXISTS);
                return result;
            }
        }

        //判断校验自定义不能中文跟长度20
        String supplierCode = supplier.getSupplierCode();
        SupplierDO supplierCodeDO = supplierMapper.findByCode(supplierCode);
        if(!supplierCode.matches("^[A-Za-z0-9-]{0,20}$")){
            result.setErrorCode(ErrorCode.SUPPLIER_CODE_NOT_CN_LENGTH);
            return result;
        }
        if(supplierCodeDO != null && !supplierCodeDO.getSupplierCode().equals(dbSupplierDO.getSupplierCode())){
            result.setErrorCode(ErrorCode.SUPPLIER_CODE_IS_EXISTS);
            return result;
        }
        //考虑英文私或公（限制长度100内），收款帐号为数字长度30
        if(supplier.getBeneficiaryBankName().length() > 100){
            result.setErrorCode(ErrorCode.BENEFICIARY_BANK_NAME_IS_LENGTH);
            return result;
        }
        if(supplier.getBeneficiaryName().length() > 100){
            result.setErrorCode(ErrorCode.BENEFICIARY_NAME_IS_LENGTH);
            return result;
        }
        String beneficiaryAccount = supplier.getBeneficiaryAccount();
        if(!beneficiaryAccount.matches("^[0-9-]{0,30}$")){
            result.setErrorCode(ErrorCode.BENEFICIARY_ACCOUNT_IS_MATH_LENGTH);
            return result;
        }

        SupplierDO supplierDO = ConverterUtil.convert(supplier, SupplierDO.class);
        supplierDO.setId(dbSupplierDO.getId());
        supplierDO.setSupplierNo(dbSupplierDO.getSupplierNo());
        supplierDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        supplierDO.setUpdateUser(loginUser.getUserId().toString());
        supplierDO.setUpdateTime(currentTime);
        supplierMapper.update(supplierDO);

        result.setResult(supplierDO.getSupplierNo());
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    public ServiceResult<String, String> deleteSupplier(String supplierNo) {
        ServiceResult<String, String> result = new ServiceResult<>();
        User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
        Date currentTime = new Date();
        SupplierDO supplierDO = supplierMapper.findByNo(supplierNo);

        supplierDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
        supplierDO.setUpdateUser(loginUser.getUserId().toString());
        supplierDO.setUpdateTime(currentTime);
        supplierMapper.update(supplierDO);

        result.setResult(supplierDO.getSupplierNo());
        result.setErrorCode(ErrorCode.SUCCESS);
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
