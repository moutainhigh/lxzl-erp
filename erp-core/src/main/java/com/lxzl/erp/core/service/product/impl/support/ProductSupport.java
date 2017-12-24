package com.lxzl.erp.core.service.product.impl.support;

import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductSkuMapper;
import com.lxzl.erp.dataaccess.domain.product.ProductSkuDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-12-23 17:26
 */
@Component
public class ProductSupport {

    @Autowired
    private ProductSkuMapper productSkuMapper;

    public String operateSkuStock(Integer skuId, Integer opStock) {
        Date currentTime = new Date();
        ProductSkuDO productSkuDO = productSkuMapper.findById(skuId);
        if (productSkuDO == null) {
            return ErrorCode.PRODUCT_SKU_IS_NULL_OR_NOT_EXISTS;
        }
        if (productSkuDO.getStock() < opStock) {
            return ErrorCode.STOCK_NOT_ENOUGH;
        }
        productSkuDO.setStock(productSkuDO.getStock() - opStock);
        productSkuDO.setUpdateTime(currentTime);
        productSkuMapper.update(productSkuDO);
        return ErrorCode.SUCCESS;
    }

}
