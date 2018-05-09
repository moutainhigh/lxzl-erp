package com.lxzl.erp.core.service.order.impl;

import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.constant.OrderItemType;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.order.OrderSplitQueryParam;
import com.lxzl.erp.common.domain.order.pojo.OrderSplitDetail;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.common.util.ConverterUtil;
import com.lxzl.erp.core.service.order.OrderSplitDetailService;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.dataaccess.dao.mysql.company.SubCompanyMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.OrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.OrderSplitDetailMapper;
import com.lxzl.erp.dataaccess.domain.company.SubCompanyDO;
import com.lxzl.erp.dataaccess.domain.order.OrderDO;
import com.lxzl.erp.dataaccess.domain.order.OrderMaterialDO;
import com.lxzl.erp.dataaccess.domain.order.OrderProductDO;
import com.lxzl.erp.dataaccess.domain.order.OrderSplitDetailDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: huahongbin
 * @Date: 2018/5/8 15:27
 * @Description:
 */
@Service("orderSplitDetailServiceImpl")
public class OrderSplitDetailServiceImpl implements OrderSplitDetailService {
    private static Logger logger = LoggerFactory.getLogger(OrderSplitDetailServiceImpl.class);

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, Integer> addOrderSplitDetail(OrderSplitDetail orderSplitDetail) {
        User loginUser = userSupport.getCurrentUser();
        Date currentTime = new Date();
        ServiceResult<String, Integer> serviceResult = new ServiceResult<>();
        Integer isPeer = orderSplitDetail.getIsPeer();
        Integer orderItemType = orderSplitDetail.getOrderItemType();
        Integer orderItemReferId = orderSplitDetail.getOrderItemReferId();

        // 非同行调拨时，分公司ID必填
        if (isPeer != null && isPeer.equals(0)) {
            if (orderSplitDetail.getDeliverySubCompanyId() == null) {
                serviceResult.setErrorCode(ErrorCode.ORDER_SPLIT_IS_PEER_OR_SUB_COMPANY_ID_EXIST);
                return serviceResult;
            }
        }

        // 校验关联订单是否存在
        OrderDO orderDO = orderMapper.findByOrderItemTypeAndOrderItemReferId(orderItemType, orderItemReferId);
        if (orderDO == null) {
            serviceResult.setErrorCode(ErrorCode.ORDER_SPLIT_ORDER_ID_NOT_EXIST);
            return serviceResult;
        }

        // 校验拆分数量不能大于订单项数量
        if (OrderItemType.ORDER_ITEM_TYPE_PRODUCT.equals(orderItemType)) {
            List<OrderProductDO> orderProductDOList = orderDO.getOrderProductDOList();
            OrderProductDO orderProductDO = orderProductDOList.get(0);
            if (orderProductDO.getProductCount() != null && orderSplitDetail.getSplitCount().compareTo(orderProductDO.getProductCount()) > 0) {
                serviceResult.setErrorCode(ErrorCode.ORDER_SPLIT_SPLIT_COUNT_EXCEED);
                return serviceResult;
            }
        } else if(OrderItemType.ORDER_ITEM_TYPE_MATERIAL.equals(orderItemType)) {
            List<OrderMaterialDO> orderMaterialDOList = orderDO.getOrderMaterialDOList();
            OrderMaterialDO orderMaterialDO = orderMaterialDOList.get(0);
            if (orderMaterialDO.getMaterialCount() != null && orderSplitDetail.getSplitCount().compareTo(orderMaterialDO.getMaterialCount()) > 0) {
                serviceResult.setErrorCode(ErrorCode.ORDER_SPLIT_SPLIT_COUNT_EXCEED);
                return serviceResult;
            }
        }
        orderSplitDetail.setOrderId(orderDO.getId());
        orderSplitDetail.setOrderNo(orderDO.getOrderNo());

        if (isPeer.equals(0)) {
            SubCompanyDO subCompanyDO = subCompanyMapper.findById(orderSplitDetail.getDeliverySubCompanyId());
            if (subCompanyDO == null) {
                serviceResult.setErrorCode(ErrorCode.ORDER_SPLIT_SUB_COMPANY_ID_NOT_EXIST);
                return serviceResult;
            }
            orderSplitDetail.setDeliverySubCompanyName(subCompanyDO.getSubCompanyName());
        }

        OrderSplitDetailDO orderSplitDetailDO = ConverterUtil.convert(orderSplitDetail, OrderSplitDetailDO.class);

        if (loginUser != null) {
            orderSplitDetailDO.setCreateUser(loginUser.getUserId().toString());
            orderSplitDetailDO.setUpdateUser(loginUser.getUserId().toString());
        }
        orderSplitDetailDO.setCreateTime(currentTime);
        orderSplitDetailDO.setUpdateTime(currentTime);
        orderSplitDetailDO.setDataStatus(1);

        orderSplitDetailMapper.save(orderSplitDetailDO);
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(orderSplitDetailDO.getId());
        return serviceResult;
    }

    @Override
    public ServiceResult<String, List<OrderSplitDetail>> queryOrderSplitDetailByOrderItemTypeAndOrderItemReferId(Integer orderItemType, Integer orderItemReferId) {
        ServiceResult<String, List<OrderSplitDetail>> serviceResult = new ServiceResult();
        OrderSplitQueryParam orderSplitQueryParam = new OrderSplitQueryParam();
        orderSplitQueryParam.setOrderItemType(orderItemType);
        orderSplitQueryParam.setOrderItemReferId(orderItemReferId);
        Map<String, Object> maps = new HashMap<>();
        maps.put("start", 0);
        maps.put("pageSize", Integer.MAX_VALUE);
        maps.put("orderSplitQueryParam", orderSplitQueryParam);
        List<OrderSplitDetailDO> orderSplitDetailDOList = orderSplitDetailMapper.findOrderSplitDetailByParams(maps);
        List<OrderSplitDetail> orderSplitDetailList = null;
        if (orderSplitDetailDOList != null && !orderSplitDetailDOList.isEmpty()) {
            orderSplitDetailList = ConverterUtil.convertList(orderSplitDetailDOList, OrderSplitDetail.class);
        }

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(orderSplitDetailList);
        return serviceResult;
    }

    @Autowired
    private OrderSplitDetailMapper orderSplitDetailMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private SubCompanyMapper subCompanyMapper;

    @Autowired
    private UserSupport userSupport;
}
