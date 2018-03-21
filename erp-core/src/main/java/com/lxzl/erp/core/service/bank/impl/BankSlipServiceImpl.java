package com.lxzl.erp.core.service.bank.impl;

import com.lxzl.erp.common.constant.DepartmentType;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.bank.BankSlipQueryParam;
import com.lxzl.erp.common.domain.bank.pojo.BankSlip;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.common.util.ConverterUtil;
import com.lxzl.erp.core.service.bank.BankSlipService;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.dataaccess.dao.mysql.bank.BankSlipMapper;
import com.lxzl.erp.dataaccess.dao.mysql.company.DepartmentMapper;
import com.lxzl.erp.dataaccess.dao.mysql.user.RoleMapper;
import com.lxzl.erp.dataaccess.domain.bank.BankSlipDO;
import com.lxzl.se.dataaccess.mysql.config.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: your name
 * @Description：
 * @Date: Created in 19:49 2018/3/20
 * @Modified By:
 */
@Service
public class BankSlipServiceImpl implements BankSlipService {

    @Autowired
    private BankSlipMapper bankSlipMapper;

    @Autowired
    private UserSupport userSupport;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private DepartmentMapper departmentMapper;

    @Override
    public ServiceResult<String, Page<BankSlip>> pageBankSlip(BankSlipQueryParam bankSlipQueryParam) {
        ServiceResult<String, Page<BankSlip>> result = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(bankSlipQueryParam.getPageNo(), bankSlipQueryParam.getPageSize());

        User currentUser = userSupport.getCurrentUser();

        //财务人员显示的数据
        if (userSupport.isFinancePerson()){
            Integer departmentType = DepartmentType.DEPARTMENT_TYPE_FINANCE;

            Map<String, Object> maps = new HashMap<>();
            maps.put("start", pageQuery.getStart());
            maps.put("pageSize", pageQuery.getPageSize());
            maps.put("bankSlipQueryParam", bankSlipQueryParam);
            maps.put("currentUserForFinance",departmentType);

            Integer totalCount = bankSlipMapper.findBankSlipCountByParams(maps);
            List<BankSlipDO> bankSlipDOList = bankSlipMapper.findBankSlipByParams(maps);

            List<BankSlip> bankSlipList = ConverterUtil.convertList(bankSlipDOList, BankSlip.class);
            Page<BankSlip> page = new Page<>(bankSlipList, totalCount, bankSlipQueryParam.getPageNo(), bankSlipQueryParam.getPageSize());

            result.setErrorCode(ErrorCode.SUCCESS);
            result.setResult(page);
            return result;
        }else if (userSupport.isBusinessAffairsPerson() || userSupport.isBusinessPerson()){
            //商务和业务员显示的数据
            Integer departmentType = DepartmentType.DEPARTMENT_TYPE_BUSINESS_AFFAIRS;
            //默认是商务人员，如果是业务员再等于业务员部门ID
            if (userSupport.isBusinessPerson()){
                departmentType = DepartmentType.DEPARTMENT_TYPE_BUSINESS;
            }

            Map<String, Object> maps = new HashMap<>();
            maps.put("start", pageQuery.getStart());
            maps.put("pageSize", pageQuery.getPageSize());
            maps.put("bankSlipQueryParam", bankSlipQueryParam);
            maps.put("currentUserForBusiness",departmentType);

            Integer totalCount = bankSlipMapper.findBankSlipCountByParams(maps);
            List<BankSlipDO> bankSlipDOList = bankSlipMapper.findBankSlipByParams(maps);

            List<BankSlip> bankSlipList = ConverterUtil.convertList(bankSlipDOList, BankSlip.class);
            Page<BankSlip> page = new Page<>(bankSlipList, totalCount, bankSlipQueryParam.getPageNo(), bankSlipQueryParam.getPageSize());

            result.setErrorCode(ErrorCode.SUCCESS);
            result.setResult(page);
            return result;
        }

        Page<BankSlip> page = new Page<>();
        result.setResult(page);
        return result;
    }
}
