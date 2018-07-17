package com.lxzl.erp.core.service.workbench.impl;

import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.constant.PermissionType;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.k3.pojo.returnOrder.K3ReturnOrderQueryParam;
import com.lxzl.erp.common.domain.order.OrderQueryParam;
import com.lxzl.erp.core.service.order.OrderService;
import com.lxzl.erp.core.service.permission.PermissionSupport;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.core.service.workbench.WorkbenchService;
import com.lxzl.erp.dataaccess.dao.mysql.k3.K3ReturnOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: your name
 * @Description：
 * @Date: Created in 17:08 2018/7/16
 * @Modified By:
 */
@Service("workbenchService")
public class WorkbenchServiceImpl implements WorkbenchService{

    @Override
    public ServiceResult<String, Integer> queryVerifingOrder(OrderQueryParam orderQueryParam) {
        ServiceResult<String, Integer> serviceResult = new ServiceResult<>();

        Map<String,Object> maps = new HashMap<>();
        maps.put("orderQueryParam",orderQueryParam);
        maps.put("permissionParam", permissionSupport.getPermissionParam(PermissionType.PERMISSION_TYPE_SUB_COMPANY_FOR_SERVICE, PermissionType.PERMISSION_TYPE_SUB_COMPANY_FOR_BUSINESS, PermissionType.PERMISSION_TYPE_USER));

        Integer count = orderMapper.findOrderCountByParams(maps);

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(count);
        return serviceResult;
    }

    @Override
    public ServiceResult<String, Integer> queryReturnOrder(K3ReturnOrderQueryParam k3ReturnOrderQueryParam) {
        ServiceResult<String, Integer> serviceResult = new ServiceResult<>();

        Map<String,Object> maps = new HashMap<>();
        maps.put("k3ReturnOrderQueryParam",k3ReturnOrderQueryParam);
        maps.put("permissionParam", permissionSupport.getPermissionParam(PermissionType.PERMISSION_TYPE_SUB_COMPANY_FOR_BUSINESS, PermissionType.PERMISSION_TYPE_USER));

        Integer count = k3ReturnOrderMapper.listCount(maps);

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(count);
        return serviceResult;
    }

    @Autowired
    private UserSupport userSupport;

    @Autowired
    private PermissionSupport permissionSupport;
    
    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private K3ReturnOrderMapper k3ReturnOrderMapper;
}
