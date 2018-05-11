package com.lxzl.erp.core.service.order.impl;

import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.constant.OrderItemType;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.order.OrderSplitQueryParam;
import com.lxzl.erp.common.domain.order.pojo.OrderSplit;
import com.lxzl.erp.common.domain.order.pojo.OrderSplitDetail;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.common.util.ConverterUtil;
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
        ServiceResult<String, List<Integer>> serviceResult = new ServiceResult<>();

        // 更新集合不能为空
        List<OrderSplitDetail> orderSplitDetailList = orderSplit.getSplitDetailList();
        if (CollectionUtil.isEmpty(orderSplitDetailList)) {
            serviceResult.setErrorCode(ErrorCode.PARAM_IS_NOT_NULL);
            return serviceResult;
        }

        List<Integer> ids = new ArrayList<>();
        Integer peerCount = 0; // 同行调拨只能有一个
        Integer totalSplitCount = 0;

        Integer orderItemType = orderSplit.getOrderItemType();
        Integer orderItemReferId = orderSplit.getOrderItemReferId();
        List<OrderSplitDetailDO> addOrderSplitDetailDOList = new ArrayList<>();

        // 根据订单项id和类型查询已有的订单拆单
        List<OrderSplitDetailDO> oldOrderSplitDetailDOList = orderSplitDetailMapper.findByItemTypeAndItemId(orderItemType, orderItemReferId);

        // 加上已有的订单拆单数量
        if (CollectionUtil.isNotEmpty(oldOrderSplitDetailDOList)) {
            for (OrderSplitDetailDO orderSplitDetailDO : oldOrderSplitDetailDOList) {
                totalSplitCount = totalSplitCount + orderSplitDetailDO.getSplitCount();
                if (orderSplitDetailDO.getIsPeer().equals(1)) {
                    peerCount = peerCount + 1;
                }
            }
        }

        // 校验关联订单是否存在
        OrderDO orderDO = orderMapper.findByOrderItemTypeAndOrderItemReferId(orderItemType, orderItemReferId);
        if (orderDO == null) {
            serviceResult.setErrorCode(ErrorCode.ORDER_SPLIT_ORDER_ID_NOT_EXIST);
            return serviceResult;
        }

        // 校验OrderSplitDetail中的参数
        ServiceResult<String, Integer> checkResult = verifySplitDetailParam(orderSplitDetailList);
        if (!ErrorCode.SUCCESS.equals(checkResult.getErrorCode())) {
            serviceResult.setErrorCode(checkResult.getErrorCode());
            return serviceResult;
        }

        for (OrderSplitDetail orderSplitDetail : orderSplitDetailList) {
            totalSplitCount = totalSplitCount + orderSplitDetail.getSplitCount();
            OrderSplitDetailDO orderSplitDetailDO = combineOrderSplitDetailDO(orderItemType, orderItemReferId, orderSplitDetail);
            addOrderSplitDetailDOList.add(orderSplitDetailDO);

            if (orderSplitDetail.getIsPeer().equals(1)) {
                peerCount = peerCount + 1;
            }
        }

        if (peerCount.compareTo(1) > 0) {
            serviceResult.setErrorCode(ErrorCode.ORDER_SPLIT_PEER_COUNT_EXCEED);
            return serviceResult;
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

        // 增加
        if (CollectionUtil.isNotEmpty(addOrderSplitDetailDOList)) {
            for (OrderSplitDetailDO orderSplitDetailDO : addOrderSplitDetailDOList) {
                orderSplitDetailMapper.save(orderSplitDetailDO);
                ids.add(orderSplitDetailDO.getId());
            }
        }

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(ids);
        return serviceResult;
    }

    @Override
    public ServiceResult<String, List<OrderSplit>> queryOrderSplitDetailByOrderItemTypeAndOrderItemReferId(Integer orderItemType, Integer orderItemReferId) {
        ServiceResult<String, List<OrderSplit>> serviceResult = new ServiceResult();
        List<OrderSplitDetailDO> orderSplitDetailDOList = orderSplitDetailMapper.findByItemTypeAndItemId(orderItemType, orderItemReferId);
        List<OrderSplit> orderSplitList = new ArrayList<>();
        if (orderSplitDetailDOList != null && !orderSplitDetailDOList.isEmpty()) {
            orderSplitList = ConverterUtil.convertList(orderSplitDetailDOList, OrderSplit.class);
        }

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(orderSplitList);
        return serviceResult;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, Integer> updateOrderSplit(OrderSplit orderSplit) {
        ServiceResult<String, Integer> serviceResult = new ServiceResult<>();
        List<OrderSplitDetail> orderSplitDetailList = orderSplit.getSplitDetailList();

        // 更新时，拆单项数量不能为空
        if (CollectionUtil.isEmpty(orderSplitDetailList)) {
            serviceResult.setErrorCode(ErrorCode.PARAM_IS_NOT_NULL);
            return serviceResult;
        }

        // 校验OrderSplitDetail中的参数
        ServiceResult<String, Integer> checkResult = verifySplitDetailParam(orderSplitDetailList);
        if (!ErrorCode.SUCCESS.equals(checkResult.getErrorCode())) {
            serviceResult.setErrorCode(checkResult.getErrorCode());
            return serviceResult;
        }

        Integer orderItemType = orderSplit.getOrderItemType();
        Integer orderItemReferId = orderSplit.getOrderItemReferId();

        // 校验关联订单是否存在
        OrderDO orderDO = orderMapper.findByOrderItemTypeAndOrderItemReferId(orderItemType, orderItemReferId);
        if (orderDO == null) {
            serviceResult.setErrorCode(ErrorCode.ORDER_SPLIT_ORDER_ID_NOT_EXIST);
            return serviceResult;
        }

        // 更新后的拆单总数量
        Integer totalCount = 0;
        Integer peerCount = 0; // 同行调拨拆单不能超过1

        // 查询旧的拆单数据
        List<OrderSplitDetailDO> oldOrderSplitDetailDOList = orderSplitDetailMapper.findByItemTypeAndItemId(orderItemType, orderItemReferId);
        List<Integer> updateIds = new ArrayList<>(); // 要修改的拆单id列表
        List<Integer> deleteIds = new ArrayList<>(); // 要删除的拆单id列表

        List<OrderSplitDetailDO> addOrderSplitDetailDO = new ArrayList<>();
        List<OrderSplitDetailDO> updateOrderSplitDetailDO = new ArrayList<>();

        // 查找新增拆单
        for (OrderSplitDetail orderSplitDetail : orderSplitDetailList) {
            if (orderSplitDetail.getOrderSplitDetailId() == null) {
                OrderSplitDetailDO orderSplitDetailDO = combineOrderSplitDetailDO(orderItemType, orderItemReferId, orderSplitDetail);
                addOrderSplitDetailDO.add(orderSplitDetailDO);
                totalCount = totalCount + orderSplitDetailDO.getSplitCount(); // 加上新增的拆单数量
            } else {
                updateIds.add(orderSplitDetail.getOrderSplitDetailId());
                OrderSplitDetailDO orderSplitDetailDO = combineOrderSplitDetailDO(orderItemType, orderItemReferId, orderSplitDetail);
                updateOrderSplitDetailDO.add(orderSplitDetailDO);
                totalCount = totalCount  + orderSplitDetailDO.getSplitCount(); // 加上更新后的拆单数量
            }

            if (orderSplitDetail.getIsPeer().equals(1)) { // 计算修改后的同行调拨数量
                peerCount = peerCount + 1;
            }
        }

        if (peerCount.compareTo(1) > 0) {
            serviceResult.setErrorCode(ErrorCode.ORDER_SPLIT_PEER_COUNT_EXCEED);
            return serviceResult;
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

        // 校验拆分数量总数不能大于订单项数量
        if (OrderItemType.ORDER_ITEM_TYPE_PRODUCT.equals(orderItemType)) {
            List<OrderProductDO> orderProductDOList = orderDO.getOrderProductDOList();
            OrderProductDO orderProductDO = orderProductDOList.get(0);
            if (orderProductDO.getProductCount() != null && totalCount.compareTo(orderProductDO.getProductCount()) > 0) {
                serviceResult.setErrorCode(ErrorCode.ORDER_SPLIT_SPLIT_COUNT_EXCEED);
                return serviceResult;
            }
        } else if(OrderItemType.ORDER_ITEM_TYPE_MATERIAL.equals(orderItemType)) {
            List<OrderMaterialDO> orderMaterialDOList = orderDO.getOrderMaterialDOList();
            OrderMaterialDO orderMaterialDO = orderMaterialDOList.get(0);
            if (orderMaterialDO.getMaterialCount() != null && totalCount.compareTo(orderMaterialDO.getMaterialCount()) > 0) {
                serviceResult.setErrorCode(ErrorCode.ORDER_SPLIT_SPLIT_COUNT_EXCEED);
                return serviceResult;
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
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
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
        orderSplitDetailDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);

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

    private ServiceResult<String, Integer> verifySplitDetailParam(List<OrderSplitDetail> orderSplitDetails) {
        ServiceResult<String, Integer> serviceResult = new ServiceResult<>();
        for (OrderSplitDetail orderSplitDetail : orderSplitDetails) {
            Integer isPeer = orderSplitDetail.getIsPeer();
            Integer splitCount = orderSplitDetail.getSplitCount();

            if (isPeer == null) {
                serviceResult.setErrorCode(ErrorCode.PARAM_IS_ERROR);
                return serviceResult;
            }

            if (isPeer != null && !(isPeer.equals(1) || isPeer.equals(0))) {
                serviceResult.setErrorCode(ErrorCode.PARAM_IS_ERROR);
                return serviceResult;
            }

            if (splitCount == null) {
                serviceResult.setErrorCode(ErrorCode.ORDER_SPLIT_SPLIT_COUNT_NOT_NULL);
                return  serviceResult;
            }

            // 非同行调拨时，分公司ID必填
            if (isPeer != null && isPeer.equals(0)) {
                if (orderSplitDetail.getDeliverySubCompanyId() == null) {
                    serviceResult.setErrorCode(ErrorCode.ORDER_SPLIT_IS_PEER_OR_SUB_COMPANY_ID_EXIST);
                    return serviceResult;
                }
            }
        }

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return  serviceResult;
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
