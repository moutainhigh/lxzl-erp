package com.lxzl.erp.core.service.statistics.impl;

import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.order.OrderQueryParam;
import com.lxzl.erp.common.domain.product.ProductEquipmentQueryParam;
import com.lxzl.erp.common.domain.statistics.pojo.StatisticsIndexInfo;
import com.lxzl.erp.common.domain.supplier.SupplierQueryParam;
import com.lxzl.erp.core.service.statistics.StatisticsService;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.OrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductEquipmentMapper;
import com.lxzl.erp.dataaccess.dao.mysql.supplier.SupplierMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-12-24 16:25
 */
@Service("statisticsService")
public class StatisticsServiceImpl implements StatisticsService {
    @Override
    public ServiceResult<String, StatisticsIndexInfo> queryIndexInfo() {
        ServiceResult<String, StatisticsIndexInfo> result = new ServiceResult<>();
        StatisticsIndexInfo statisticsIndexInfo = new StatisticsIndexInfo();
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("start", 0);
        paramMap.put("pageSize", Integer.MAX_VALUE);
        paramMap.put("productEquipmentQueryParam", new ProductEquipmentQueryParam());
        Integer totalProductEquipmentCount = productEquipmentMapper.listCount(paramMap);
        statisticsIndexInfo.setTotalProductEquipmentCount(totalProductEquipmentCount);

        Integer totalCustomerCount = customerMapper.listCount(paramMap);
        statisticsIndexInfo.setTotalCustomerCount(totalCustomerCount);

        BigDecimal totalRentAmount = orderMapper.findPaidOrderAmount();
        statisticsIndexInfo.setTotalRentAmount(totalRentAmount);

        // 空参数
        paramMap.clear();
        paramMap.put("orderQueryParam", new OrderQueryParam());
        Integer totalOrderCount = orderMapper.findOrderCountByParams(paramMap);
        statisticsIndexInfo.setTotalOrderCount(totalOrderCount);

        List<Map<String, Object>> subCompanyRentAmountList = orderMapper.querySubCompanyOrderAmount(paramMap);
        Map<String, BigDecimal> subCompanyRentAmount = new HashMap<>();
        for (Map<String, Object> subCompanyRentAmountMap : subCompanyRentAmountList) {
            if (subCompanyRentAmountMap.get("total_order_amount") != null && subCompanyRentAmountMap.get("total_order_amount") instanceof BigDecimal) {
                subCompanyRentAmount.put(subCompanyRentAmountMap.get("sub_company_name").toString(), (BigDecimal) subCompanyRentAmountMap.get("total_order_amount"));
            } else {
                subCompanyRentAmount.put(subCompanyRentAmountMap.get("sub_company_name").toString(), BigDecimal.ZERO);
            }
        }
        statisticsIndexInfo.setSubCompanyRentAmount(subCompanyRentAmount);

        Map<Integer, BigDecimal> monthRentAmount = new HashMap<>();
        statisticsIndexInfo.setMonthRentAmount(monthRentAmount);

        result.setResult(statisticsIndexInfo);
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private SupplierMapper supplierMapper;

    @Autowired
    private ProductEquipmentMapper productEquipmentMapper;

    @Autowired
    private OrderMapper orderMapper;
}
