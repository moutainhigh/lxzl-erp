package com.lxzl.erp.core.service.product.impl.support;

import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.order.pojo.OrderMaterial;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.dataaccess.dao.mysql.order.OrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.OrderMaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.OrderProductMapper;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductSkuMapper;
import com.lxzl.erp.dataaccess.domain.order.OrderDO;
import com.lxzl.erp.dataaccess.domain.order.OrderMaterialDO;
import com.lxzl.erp.dataaccess.domain.order.OrderProductDO;
import com.lxzl.erp.dataaccess.domain.product.ProductSkuDO;
import com.lxzl.se.common.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

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
    @Autowired
    private OrderProductMapper orderProductMapper;
    @Autowired
    private OrderMaterialMapper orderMaterialMapper;
    @Autowired
    private OrderMapper orderMapper;

    public String operateSkuStock(Integer skuId, Integer opStock) {
        Date currentTime = new Date();
        ProductSkuDO productSkuDO = productSkuMapper.findById(skuId);
        if (productSkuDO == null) {
            return ErrorCode.PRODUCT_SKU_IS_NULL_OR_NOT_EXISTS;
        }
        if (opStock < 0 && productSkuDO.getStock() < Math.abs(opStock)) {
            return ErrorCode.STOCK_NOT_ENOUGH;
        }
        productSkuDO.setStock(productSkuDO.getStock() + opStock);
        productSkuDO.setUpdateTime(currentTime);
        productSkuMapper.update(productSkuDO);
        return ErrorCode.SUCCESS;
    }
    public boolean isMaterial(String productNo) {
        if (productNo.startsWith("20.")||productNo.startsWith("30.")){
            return true;
        }else{
            return false;
        }
    }
    public boolean isProduct(String productNo) {
       return !isMaterial(productNo);
    }

    /**
     * 兼容erp订单和k3订单商品项
     * @param orderNo
     * @param orderItemId
     * @param orderEntry
     * @return
     */
    public OrderProductDO getOrderProductDO(String orderNo , String orderItemId , String orderEntry){
        OrderProductDO orderProductDO = null;
        if(StringUtil.isNotEmpty(orderItemId)&&!"0".equals(orderItemId)){
            orderProductDO = orderProductMapper.findById(Integer.parseInt(orderItemId));
        }else if(StringUtil.isNotEmpty(orderEntry)){
            Integer oe = Integer.parseInt(orderEntry);
            OrderDO orderDO = orderMapper.findByOrderNo(orderNo);
            if(orderDO!=null&& CollectionUtil.isNotEmpty(orderDO.getOrderProductDOList())){
                List<OrderProductDO> orderProductDOList = orderDO.getOrderProductDOList();
                for(OrderProductDO op : orderProductDOList){
                    if(oe.equals(op.getFEntryID())){
                        orderProductDO = op;
                        break;
                    }
                }
            }
        }
        return orderProductDO;
    }
    /**
     * 兼容erp订单和k3订单配件项
     * @param orderNo
     * @param orderItemId
     * @param orderEntry
     * @return
     */
    public OrderMaterialDO getOrderMaterialDO(String orderNo , String orderItemId , String orderEntry){
        OrderMaterialDO orderMaterialDO = null;
        if(StringUtil.isNotEmpty(orderItemId)&&!"0".equals(orderItemId)){
            orderMaterialDO = orderMaterialMapper.findById(Integer.parseInt(orderItemId));
        }else if(StringUtil.isNotEmpty(orderEntry)){
            Integer oe = Integer.parseInt(orderEntry);
            OrderDO orderDO = orderMapper.findByOrderNo(orderNo);
            if(orderDO!=null&& CollectionUtil.isNotEmpty(orderDO.getOrderMaterialDOList())){
                List<OrderMaterialDO> orderMaterialDOList = orderDO.getOrderMaterialDOList();
                for(OrderMaterialDO om : orderMaterialDOList){
                    if(oe.equals(om.getFEntryID())){
                        orderMaterialDO = om;
                        break;
                    }
                }
            }
        }
        return orderMaterialDO;
    }
}
