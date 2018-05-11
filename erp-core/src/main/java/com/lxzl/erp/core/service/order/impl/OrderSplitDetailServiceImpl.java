package com.lxzl.erp.core.service.order.impl;

import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.constant.OrderItemType;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.order.OrderSplitQueryParam;
import com.lxzl.erp.common.domain.order.pojo.OrderSplit;
import com.lxzl.erp.common.domain.order.pojo.OrderSplitDetail;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.common.util.ConverterUtil;
import com.lxzl.erp.common.util.ListUtil;
import com.lxzl.erp.common.util.validate.constraints.In;
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

import java.util.*;

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
    public ServiceResult<String, List<Integer>> addOrderSplitDetail(OrderSplit orderSplit) {

        Date currentTime = new Date();
        ServiceResult<String, List<Integer>> serviceResult = new ServiceResult<>();
        List<Integer> ids = new ArrayList<>();
        Integer totalSplitCount = 0;

        Integer orderItemType = orderSplit.getOrderItemType();
        Integer orderItemReferId = orderSplit.getOrderItemReferId();

        // 校验关联订单是否存在
        OrderDO orderDO = orderMapper.findByOrderItemTypeAndOrderItemReferId(orderItemType, orderItemReferId);
        if (orderDO == null) {
            serviceResult.setErrorCode(ErrorCode.ORDER_SPLIT_ORDER_ID_NOT_EXIST);
            return serviceResult;
        }

        List<OrderSplitDetail> orderSplitDetailList = orderSplit.getSplitDetailList();
        for (OrderSplitDetail orderSplitDetail : orderSplitDetailList) {
            Integer isPeer = orderSplitDetail.getIsPeer();
            totalSplitCount = totalSplitCount + orderSplitDetail.getSplitCount();

            // 非同行调拨时，分公司ID必填
            if (isPeer != null && isPeer.equals(0)) {
                if (orderSplitDetail.getDeliverySubCompanyId() == null) {
                    serviceResult.setErrorCode(ErrorCode.ORDER_SPLIT_IS_PEER_OR_SUB_COMPANY_ID_EXIST);
                    return serviceResult;
                }
            }

            OrderSplitDetailDO orderSplitDetailDO = combineOrderSplitDetailDO(orderItemType, orderItemReferId, orderSplitDetail);
            orderSplitDetailMapper.save(orderSplitDetailDO);
            ids.add(orderSplitDetailDO.getId());
        }

        // 校验拆分数量总数不能大于订单项数量
        if (OrderItemType.ORDER_ITEM_TYPE_PRODUCT.equals(orderItemType)) {
            List<OrderProductDO> orderProductDOList = orderDO.getOrderProductDOList();
            OrderProductDO orderProductDO = orderProductDOList.get(0);
            if (orderProductDO.getProductCount() != null && totalSplitCount.compareTo(orderProductDO.getProductCount()) > 0) {
                serviceResult.setErrorCode(ErrorCode.ORDER_SPLIT_SPLIT_COUNT_EXCEED);
                return serviceResult;
            }
        } else if(OrderItemType.ORDER_ITEM_TYPE_MATERIAL.equals(orderItemType)) {
            List<OrderMaterialDO> orderMaterialDOList = orderDO.getOrderMaterialDOList();
            OrderMaterialDO orderMaterialDO = orderMaterialDOList.get(0);
            if (orderMaterialDO.getMaterialCount() != null && totalSplitCount.compareTo(orderMaterialDO.getMaterialCount()) > 0) {
                serviceResult.setErrorCode(ErrorCode.ORDER_SPLIT_SPLIT_COUNT_EXCEED);
                return serviceResult;
            }
        }

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(ids);
        return serviceResult;
    }

    @Override
    public ServiceResult<String, List<OrderSplit>> queryOrderSplitDetailByOrderItemTypeAndOrderItemReferId(Integer orderItemType, Integer orderItemReferId) {
        ServiceResult<String, List<OrderSplit>> serviceResult = new ServiceResult();
        OrderSplitQueryParam orderSplitQueryParam = new OrderSplitQueryParam();
        orderSplitQueryParam.setOrderItemType(orderItemType);
        orderSplitQueryParam.setOrderItemReferId(orderItemReferId);
        Map<String, Object> maps = new HashMap<>();
        maps.put("start", 0);
        maps.put("pageSize", Integer.MAX_VALUE);
        maps.put("orderSplitQueryParam", orderSplitQueryParam);
        List<OrderSplitDetailDO> orderSplitDetailDOList = orderSplitDetailMapper.findOrderSplitDetailByParams(maps);
        List<OrderSplit> orderSplitList = null;
        if (orderSplitDetailDOList != null && !orderSplitDetailDOList.isEmpty()) {
            orderSplitList = ConverterUtil.convertList(orderSplitDetailDOList, OrderSplit.class);
        }

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(orderSplitList);
        return serviceResult;
    }

    @Override
    public ServiceResult<String, Integer> updateOrderSplit(OrderSplit orderSplit) {
        ServiceResult<String, Integer> serviceResult = new ServiceResult<>();
        List<OrderSplitDetail> orderSplitDetailList = orderSplit.getSplitDetailList();

        Integer orderItemType = orderSplit.getOrderItemType();
        Integer orderItemReferId = orderSplit.getOrderItemReferId();

        // 编辑时，拆单不能为空
        if (CollectionUtil.isEmpty(orderSplitDetailList)) {
            serviceResult.setErrorCode(ErrorCode.PARAM_IS_NOT_NULL);
            return serviceResult;
        }

        // 查询旧的拆单数据
        OrderSplitQueryParam orderSplitQueryParam = new OrderSplitQueryParam();
        orderSplitQueryParam.setOrderItemType(orderItemType);
        orderSplitQueryParam.setOrderItemReferId(orderItemReferId);
        Map<String, Object> maps = new HashMap<>();
        maps.put("start", 0);
        maps.put("pageSize", Integer.MAX_VALUE);
        maps.put("orderSplitQueryParam", orderSplitQueryParam);
        List<OrderSplitDetailDO> oldOrderSplitDetailDOList = orderSplitDetailMapper.findOrderSplitDetailByParams(maps);
        Map<Integer, OrderSplitDetailDO> oldOrderSplitDetailDOMap = ListUtil.listToMap(oldOrderSplitDetailDOList, "id");
        List<Integer> updateIds = new ArrayList<>(); // 要修改的拆单id列表
        List<Integer> deleteIds = new ArrayList<>(); // 要删除的拆单id列表

        List<OrderSplitDetailDO> addOrderSplitDetailDO = new ArrayList<>();
        List<OrderSplitDetailDO> updateOrderSplitDetailDO = new ArrayList<>();

        // 查找新增拆单
        for (OrderSplitDetail orderSplitDetail : orderSplitDetailList) {
            if (orderSplitDetail.getOrderSplitDetailId() == null) {
                OrderSplitDetailDO orderSplitDetailDO = combineOrderSplitDetailDO(orderItemType, orderItemReferId, orderSplitDetail);
                addOrderSplitDetailDO.add(orderSplitDetailDO);
            } else {
                updateIds.add(orderSplitDetail.getOrderSplitDetailId());
                OrderSplitDetailDO orderSplitDetailDO = combineOrderSplitDetailDO(orderItemType, orderItemReferId, orderSplitDetail);
                updateOrderSplitDetailDO.add(orderSplitDetailDO);
            }
        }

        // 查找要删除的拆单
        if (CollectionUtil.isEmpty(updateIds)) {
            deleteIds.addAll(updateIds);
        } else {
            for (OrderSplitDetailDO orderSplitDetailDO : oldOrderSplitDetailDOList) {
                if (!updateIds.contains(orderSplitDetailDO.getId())) {
                    deleteIds.add(orderSplitDetailDO.getId());
                }
            }
        }



        // 新增拆单
        if (CollectionUtil.isNotEmpty(addOrderSplitDetailDO)) {
            for (OrderSplitDetailDO orderSplitDetailDO : addOrderSplitDetailDO) {
                orderSplitDetailMapper.save(orderSplitDetailDO);
            }
        }

        // 修改拆单
        if (CollectionUtil.isNotEmpty(updateOrderSplitDetailDO)) {
            for (OrderSplitDetailDO orderSplitDetailDO : updateOrderSplitDetailDO) {
                orderSplitDetailMapper.update(orderSplitDetailDO);
            }
        }

        // 删除拆单
        if (CollectionUtil.isNotEmpty(deleteIds)) {
            orderSplitDetailMapper.deleteByIds(deleteIds);
        }

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(orderSplitDetailList.size());
        return serviceResult;
    }

    @Override
    public ServiceResult<String, Integer> deleteOrderSplit(Integer orderItemType, Integer orderItemReferId) {
        ServiceResult<String, Integer> serviceResult = new ServiceResult<>();
        Integer result = orderSplitDetailMapper.deleteByItemTypeAndItemId(orderItemType, orderItemReferId);
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(result);
        return serviceResult;
    }

    // 组合OrderSplitDetailDO
    private OrderSplitDetailDO combineOrderSplitDetailDO(Integer orderItemType, Integer orderItemReferId, OrderSplitDetail orderSplitDetail) {
        OrderSplitDetailDO orderSplitDetailDO = new OrderSplitDetailDO();
        Date now = new Date();
        Integer isPeer = orderSplitDetail.getIsPeer();

        // 校验关联订单是否存在
        OrderDO orderDO = orderMapper.findByOrderItemTypeAndOrderItemReferId(orderItemType, orderItemReferId);

        if (isPeer.equals(0)) {
            SubCompanyDO subCompanyDO = subCompanyMapper.findById(orderSplitDetail.getDeliverySubCompanyId());
            orderSplitDetailDO.setDeliverySubCompanyName(subCompanyDO.getSubCompanyName());
        }

        orderSplitDetailDO.setOrderItemReferId(orderItemReferId);
        orderSplitDetailDO.setOrderItemType(orderItemType);
        orderSplitDetailDO.setOrderId(orderDO.getId());
        orderSplitDetailDO.setOrderNo(orderDO.getOrderNo());
        orderSplitDetailDO.setSplitCount(orderSplitDetail.getSplitCount());
        orderSplitDetailDO.setIsPeer(orderSplitDetail.getIsPeer());
        orderSplitDetailDO.setDeliverySubCompanyId(orderSplitDetail.getDeliverySubCompanyId());
        orderSplitDetailDO.setCreateTime(now);
        orderSplitDetailDO.setUpdateTime(now);
        orderSplitDetailDO.setDataStatus(1);

        User loginUser = userSupport.getCurrentUser();
        if (loginUser != null) {
            orderSplitDetailDO.setCreateUser(loginUser.getUserId().toString());
            orderSplitDetailDO.setUpdateUser(loginUser.getUserId().toString());
        }

        if (orderSplitDetail.getOrderSplitDetailId() != null) {
            orderSplitDetailDO.setId(orderSplitDetail.getOrderSplitDetailId());
        }

        return orderSplitDetailDO;
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
