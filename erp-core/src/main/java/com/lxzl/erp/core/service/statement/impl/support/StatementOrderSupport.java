package com.lxzl.erp.core.service.statement.impl.support;

import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.RentLengthType;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.statement.StatementOrderDetailQueryParam;
import com.lxzl.erp.common.domain.statement.StatementOrderQueryParam;
import com.lxzl.erp.common.domain.statistics.pojo.StatisticsUnReceivableDetailForSubCompany;
import com.lxzl.erp.common.domain.statistics.pojo.StatisticsUnReceivableForSubCompany;
import com.lxzl.erp.common.util.BigDecimalUtil;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.common.util.ListUtil;
import com.lxzl.erp.core.service.statistics.StatisticsService;
import com.lxzl.erp.dataaccess.dao.mysql.company.DepartmentMapper;
import com.lxzl.erp.dataaccess.dao.mysql.company.SubCompanyMapper;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerMapper;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerPersonMapper;
import com.lxzl.erp.dataaccess.dao.mysql.statement.StatementOrderDetailMapper;
import com.lxzl.erp.dataaccess.dao.mysql.statement.StatementOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.user.RoleMapper;
import com.lxzl.erp.dataaccess.dao.mysql.user.UserMapper;
import com.lxzl.erp.dataaccess.domain.customer.CustomerDO;
import com.lxzl.erp.dataaccess.domain.statement.StatementOrderDO;
import com.lxzl.erp.dataaccess.domain.statement.StatementOrderDetailDO;
import com.lxzl.erp.dataaccess.domain.user.RoleDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2018-02-06 10:06
 */
@Component
public class StatementOrderSupport {

    @Autowired
    private StatementOrderMapper statementOrderMapper;

    @Autowired
    private StatementOrderDetailMapper statementOrderDetailMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SubCompanyMapper subCompanyMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private CustomerPersonMapper customerPersonMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private StatisticsService statisticsService;

    public List<StatementOrderDO> getOverdueStatementOrderList(Integer customerId) {
        StatementOrderQueryParam param = new StatementOrderQueryParam();
        param.setIsNeedToPay(CommonConstant.COMMON_CONSTANT_YES);
        param.setIsOverdue(CommonConstant.COMMON_CONSTANT_YES);
        param.setStatementOrderCustomerId(customerId);
        Map<String, Object> maps = new HashMap<>();
        maps.put("start", 0);
        maps.put("pageSize", Integer.MAX_VALUE);
        maps.put("statementOrderQueryParam", param);
        return statementOrderMapper.listPage(maps);
    }

    public BigDecimal getShortRentReceivable(Integer customerId) {
        StatementOrderDetailQueryParam param = new StatementOrderDetailQueryParam();
        param.setCustomerId(customerId);
        param.setRentLengthType(RentLengthType.RENT_LENGTH_TYPE_SHORT);
        param.setIsNeedToPay(CommonConstant.COMMON_CONSTANT_YES);
        Map<String, Object> maps = new HashMap<>();
        maps.put("start", 0);
        maps.put("pageSize", Integer.MAX_VALUE);
        maps.put("statementOrderDetailQueryParam", param);
        List<StatementOrderDetailDO> statementOrderDetailDOList = statementOrderDetailMapper.listPage(maps);
        BigDecimal totalShortRentReceivable = BigDecimal.ZERO;
        if (CollectionUtil.isNotEmpty(statementOrderDetailDOList)) {
            for (StatementOrderDetailDO statementOrderDetailDO : statementOrderDetailDOList) {
                totalShortRentReceivable = BigDecimalUtil.add(totalShortRentReceivable, statementOrderDetailDO.getStatementDetailAmount());
            }
        }
        return totalShortRentReceivable;
    }


    public BigDecimal getSubCompanyShortRentReceivable(Integer subCompanyId) {
        StatementOrderDetailQueryParam param = new StatementOrderDetailQueryParam();
        param.setSubCompanyId(subCompanyId);
        param.setRentLengthType(RentLengthType.RENT_LENGTH_TYPE_SHORT);
        param.setIsNeedToPay(CommonConstant.COMMON_CONSTANT_YES);
        Map<String, Object> maps = new HashMap<>();
        maps.put("start", 0);
        maps.put("pageSize", Integer.MAX_VALUE);
        maps.put("statementOrderDetailQueryParam", param);
        List<StatementOrderDetailDO> statementOrderDetailDOList = statementOrderDetailMapper.listPage(maps);
        BigDecimal totalShortRentReceivable = BigDecimal.ZERO;
        if (CollectionUtil.isNotEmpty(statementOrderDetailDOList)) {
            for (StatementOrderDetailDO statementOrderDetailDO : statementOrderDetailDOList) {
                totalShortRentReceivable = BigDecimalUtil.add(totalShortRentReceivable, statementOrderDetailDO.getStatementDetailAmount());
            }
        }
        return totalShortRentReceivable;
    }

}
