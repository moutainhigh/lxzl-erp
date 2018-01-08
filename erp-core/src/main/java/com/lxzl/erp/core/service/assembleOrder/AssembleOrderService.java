package com.lxzl.erp.core.service.assembleOrder;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.assembleOder.AssembleOrderQueryParam;
import com.lxzl.erp.common.domain.assembleOder.pojo.AssembleOrder;
import com.lxzl.erp.common.domain.jointProduct.JointProductQueryParam;
import com.lxzl.erp.common.domain.jointProduct.pojo.JointProduct;

/**
 * User : XiaoLuYu
 * Date : Created in ${Date}
 * Time : Created in ${Time}
 */
public interface AssembleOrderService {
    /**
     * 添加组装单
     */
    ServiceResult<String, Integer> addAssembleOrder(AssembleOrder assembleOrder);
    /**
     * 查询组装单
     */
    ServiceResult<String, AssembleOrder> queryAssembleOrderByAssembleOrderId(Integer assembleOrderId);
    /**
     * 分页查询组装单
     */
    ServiceResult<String, Page<AssembleOrder>> pageAssembleOrder(AssembleOrderQueryParam assembleOrderQueryParam);
}
