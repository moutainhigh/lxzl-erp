package com.lxzl.erp.core.service.reletorder.impl.support;

import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.constant.OrderPayMode;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.dataaccess.domain.reletorder.ReletOrderDO;
import com.lxzl.erp.dataaccess.domain.reletorder.ReletOrderMaterialDO;
import com.lxzl.erp.dataaccess.domain.reletorder.ReletOrderProductDO;
import org.springframework.stereotype.Component;

/**
 * @author: huanglong
 * @date: 2018/8/13/013 17:38
 * @e_mail: huanglong5945@163.com
 * @Description:common helper of relet order
 */
@Component
public class ReletOrderSupport {
    /**
     * 续租时：若订单支付方式是首付百分比则修改续租单支付方式为先用后付
     *
     * @param
     * @return
     * @author ZhaoZiXuan
     * @date 2018/6/6 15:49
     */
    public String updateReletOrderPayModeOfBeforePercent(ReletOrderDO reletOrderDO) {
        if (CollectionUtil.isNotEmpty(reletOrderDO.getReletOrderProductDOList())) {

            for (ReletOrderProductDO reletOrderProductDO : reletOrderDO.getReletOrderProductDOList()) {

                if (OrderPayMode.PAY_MODE_PAY_BEFORE_PERCENT.equals(reletOrderProductDO.getPayMode())) {
                    reletOrderProductDO.setPayMode(OrderPayMode.PAY_MODE_PAY_AFTER);
                }
            }
        }

        if (CollectionUtil.isNotEmpty(reletOrderDO.getReletOrderMaterialDOList())) {

            for (ReletOrderMaterialDO reletOrderMaterialDO : reletOrderDO.getReletOrderMaterialDOList()) {

                if (OrderPayMode.PAY_MODE_PAY_BEFORE_PERCENT.equals(reletOrderMaterialDO.getPayMode())) {
                    reletOrderMaterialDO.setPayMode(OrderPayMode.PAY_MODE_PAY_AFTER);
                }
            }
        }

        return ErrorCode.SUCCESS;
    }
}
