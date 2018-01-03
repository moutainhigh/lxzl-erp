package com.lxzl.erp.core.service.assembleOrder;

import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.assembleOder.pojo.AssembleOrder; /**
 * User : XiaoLuYu
 * Date : Created in ${Date}
 * Time : Created in ${Time}
 */
public interface AssembleOrderService {
    /**
     * 添加组装单
     * */
    ServiceResult<String,Integer> addAssembleOrder(AssembleOrder assembleOrder);
}
