package com.lxzl.erp.core.service.order.impl;

import com.lxzl.erp.common.constant.*;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.material.pojo.Material;
import com.lxzl.erp.common.domain.order.ExchangeOrderCommitParam;
import com.lxzl.erp.common.domain.order.pojo.*;
import com.lxzl.erp.common.domain.product.pojo.Product;
import com.lxzl.erp.common.domain.product.pojo.ProductSku;
import com.lxzl.erp.common.domain.statement.AmountNeedReturn;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.common.util.*;
import com.lxzl.erp.core.service.amount.support.AmountSupport;
import com.lxzl.erp.core.service.basic.impl.support.GenerateNoSupport;
import com.lxzl.erp.core.service.customer.impl.support.CustomerSupport;
import com.lxzl.erp.core.service.k3.K3Service;
import com.lxzl.erp.core.service.material.MaterialService;
import com.lxzl.erp.core.service.order.ExchangeOrderService;
import com.lxzl.erp.core.service.order.OrderService;
import com.lxzl.erp.core.service.order.impl.support.OrderSupport;
import com.lxzl.erp.core.service.order.impl.support.OrderTimeAxisSupport;
import com.lxzl.erp.core.service.product.ProductService;
import com.lxzl.erp.core.service.statement.StatementService;
import com.lxzl.erp.core.service.statement.impl.support.StatementOrderSupport;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.core.service.workflow.WorkflowService;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerConsignInfoMapper;
import com.lxzl.erp.dataaccess.dao.mysql.k3.K3ReturnOrderDetailMapper;
import com.lxzl.erp.dataaccess.dao.mysql.k3.K3ReturnOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.material.MaterialTypeMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.*;
import com.lxzl.erp.dataaccess.dao.mysql.replace.ReplaceOrderMapper;
import com.lxzl.erp.dataaccess.domain.customer.CustomerConsignInfoDO;
import com.lxzl.erp.dataaccess.domain.customer.CustomerRiskManagementDO;
import com.lxzl.erp.dataaccess.domain.k3.returnOrder.K3ReturnOrderDO;
import com.lxzl.erp.dataaccess.domain.k3.returnOrder.K3ReturnOrderDetailDO;
import com.lxzl.erp.dataaccess.domain.material.MaterialTypeDO;
import com.lxzl.erp.dataaccess.domain.order.*;
import com.lxzl.erp.dataaccess.domain.replace.ReplaceOrderDO;
import com.lxzl.se.common.exception.BusinessException;
import com.lxzl.se.common.util.StringUtil;
import com.lxzl.se.common.util.secret.MD5Util;
import org.apache.commons.collections.ListUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("exchangeOrderService")
public class ExchangeOrderServiceImpl implements ExchangeOrderService {
    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, String> createExchangeOrder(ExchangeOrder exchangeOrder) {
        User loginUser = userSupport.getCurrentUser();
        Date now = new Date();
        OrderDO orderDO = orderMapper.findByOrderNo(exchangeOrder.getOrderNo());
        //校验信息
        ServiceResult<String, String> result=this.verifyOrderInfo(orderDO,exchangeOrder);
        //如果有变更单就不允许更换
        List<ExchangeOrderDO> exchangeOrderDOList = exchangeOrderMapper.findByOrderNo(exchangeOrder.getOrderNo());
        if (CollectionUtil.isNotEmpty(exchangeOrderDOList)) {
            result.setErrorCode(ErrorCode.EXCHANGE_ORDER_EXISTS);
            return result;
        }
        if(!ErrorCode.SUCCESS.equals(result.getErrorCode())){
            return result;
        }
        ExchangeOrderDO exchangeOrderDO = new ExchangeOrderDO();
        BeanUtils.copyProperties(exchangeOrder, exchangeOrderDO);
        exchangeOrderDO.setExchangeOrderNo(generateNoSupport.generateExchangeOrderNo(now, orderDO.getOrderSubCompanyId().toString()));
        exchangeOrderDO.setCreateTime(now);
        exchangeOrderDO.setCreateUser(loginUser.getUserId().toString());
        exchangeOrderDO.setUpdateTime(now);
        exchangeOrderDO.setUpdateUser(loginUser.getUserId().toString());
        exchangeOrderDO.setBusinessType(ExchangeOrderBusinessType.UPDATE_ALL);
        exchangeOrderDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        exchangeOrderDO.setStatus(ExchangeOrderStatus.ORDER_STATUS_WAIT_COMMIT);
        exchangeOrderMapper.save(exchangeOrderDO);
        Boolean isUpd=false;
        if (!CollectionUtil.isEmpty(exchangeOrder.getExchangeOrderProductList())) {
            List<ExchangeOrderProductDO> exchangeOrderProductDOList = new ArrayList<>();
            Map<Integer, OrderProductDO> orderProductDOMap = ListUtil.listToMap(orderDO.getOrderProductDOList(), "id");
            //保存商品信息
            for (ExchangeOrderProduct exchangeOrderProduct : exchangeOrder.getExchangeOrderProductList()) {
                OrderProductDO orderProductDO = orderProductDOMap.get(exchangeOrderProduct.getOrderProductId());
                //补全信息
                ExchangeOrderProductDO exchangeOrderProductDO = new ExchangeOrderProductDO();
                BeanUtils.copyProperties(exchangeOrderProduct, exchangeOrderProductDO);
                exchangeOrderProductDO.setExchangeOrderId(exchangeOrderDO.getId());
                exchangeOrderProductDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                exchangeOrderProductDO.setIsNewProduct(orderProductDO.getIsNewProduct());
                exchangeOrderProductDO.setOldProductUnitAmount(orderProductDO.getProductUnitAmount());
                exchangeOrderProductDO.setProductName(orderProductDO.getProductName());
                exchangeOrderProductDO.setProductSkuId(orderProductDO.getProductSkuId());
                exchangeOrderProductDO.setProductSkuName(orderProductDO.getProductSkuName());
                //判断是否发生变更
                if(!(exchangeOrderProductDO.getDepositCycle().equals(orderProductDO.getDepositCycle())&&
                        exchangeOrderProductDO.getPaymentCycle().equals(orderProductDO.getPaymentCycle())&&
                        exchangeOrderProductDO.getPayMode().equals(orderProductDO.getPayMode())&&
                        exchangeOrderProductDO.getProductUnitAmount().equals(orderProductDO.getProductUnitAmount()))){
                    isUpd=true;
                }
                exchangeOrderProductDOList.add(exchangeOrderProductDO);
            }
            exchangeOrderProductMapper.saveList(exchangeOrderProductDOList);
        }
        if (!CollectionUtil.isEmpty(exchangeOrder.getExchangeOrderMaterialList())) {
            List<ExchangeOrderMaterialDO> exchangeOrderMaterialDOList = new ArrayList<>();
            Map<Integer, OrderMaterialDO> orderMaterialDOMap = ListUtil.listToMap(orderDO.getOrderMaterialDOList(), "id");
            //保存配件信息
            for (ExchangeOrderMaterial exchangeOrderMaterial : exchangeOrder.getExchangeOrderMaterialList()) {
                OrderMaterialDO orderMaterialDO = orderMaterialDOMap.get(exchangeOrderMaterial.getOrderProductId());
                //补全信息
                ExchangeOrderMaterialDO exchangeOrderMaterialDO = new ExchangeOrderMaterialDO();
                BeanUtils.copyProperties(exchangeOrderMaterial, exchangeOrderMaterialDO);
                exchangeOrderMaterialDO.setExchangeOrderId(exchangeOrderDO.getId());
                exchangeOrderMaterialDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                exchangeOrderMaterialDO.setIsNewMaterial(orderMaterialDO.getIsNewMaterial());
                exchangeOrderMaterialDO.setMaterialId(orderMaterialDO.getMaterialId());
                exchangeOrderMaterialDO.setMaterialName(orderMaterialDO.getMaterialName());
                exchangeOrderMaterialDO.setOldMaterialUnitAmount(orderMaterialDO.getMaterialUnitAmount());
                exchangeOrderMaterialDOList.add(exchangeOrderMaterialDO);
                //判断是否发生变更
                if(!(exchangeOrderMaterialDO.getDepositCycle().equals(orderMaterialDO.getDepositCycle())&&
                        exchangeOrderMaterialDO.getPaymentCycle().equals(orderMaterialDO.getPaymentCycle())&&
                        exchangeOrderMaterialDO.getPayMode().equals(orderMaterialDO.getPayMode())&&
                        exchangeOrderMaterialDO.getMaterialUnitAmount().equals(orderMaterialDO.getMaterialUnitAmount()))){
                    isUpd=true;
                }
            }
            exchangeOrderMaterialMapper.saveList(exchangeOrderMaterialDOList);
        }
        if(!isUpd){//如果没发生变更，回滚数据
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            result.setErrorCode(ErrorCode.NOT_EXCHANGE_ORDER);
            return result;
        }
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(exchangeOrderDO.getExchangeOrderNo());
        return result;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, String> updateExchangeOrder(ExchangeOrder exchangeOrder) {
        ServiceResult<String, String> result=new ServiceResult<>();
        User loginUser = userSupport.getCurrentUser();
        Date now = new Date();
        //校验信息
        ExchangeOrderDO exchangeOrderDO = exchangeOrderMapper.findByExchangeOrderNo(exchangeOrder.getExchangeOrderNo());
        if (null == exchangeOrderDO) {
            result.setErrorCode(ErrorCode.ORDER_NOT_EXISTS);
            return result;
        }
        //只有待提交的变更单才可以进行操作
        if (!(CommonConstant.DATA_STATUS_ENABLE.equals(exchangeOrderDO.getDataStatus()) && ExchangeOrderStatus.ORDER_STATUS_WAIT_COMMIT.equals(exchangeOrderDO.getStatus()))) {
            result.setErrorCode(ErrorCode.ORDER_NOT_EXISTS);
            return result;
        }
        OrderDO orderDO = orderMapper.findByOrderNo(exchangeOrderDO.getOrderNo());
        result=this.verifyOrderInfo(orderDO,exchangeOrder);
        if(!ErrorCode.SUCCESS.equals(result.getErrorCode())){
            return result;
        }
        //更新日期
        exchangeOrderDO.setRentStartTime(exchangeOrder.getRentStartTime());
        //更新备注
        exchangeOrderDO.setRemark(exchangeOrder.getRemark());
        exchangeOrderDO.setUpdateTime(now);
        exchangeOrderDO.setUpdateUser(loginUser.getUserId().toString());
        exchangeOrderMapper.update(exchangeOrderDO);
        Boolean isUpd=false;
        if (!CollectionUtil.isEmpty(exchangeOrder.getExchangeOrderProductList())) {
            List<ExchangeOrderProductDO> exchangeOrderProductDOList = new ArrayList<>();
            Map<Integer, OrderProductDO> orderProductDOMap = ListUtil.listToMap(orderDO.getOrderProductDOList(), "id");
            //保存商品信息
            for (ExchangeOrderProduct exchangeOrderProduct : exchangeOrder.getExchangeOrderProductList()) {
                ExchangeOrderProductDO exchangeOrderProductDO=new ExchangeOrderProductDO();
                exchangeOrderProductDO.setId(exchangeOrderProduct.getExchangeOrderProductId());
                exchangeOrderProductDO.setProductUnitAmount(exchangeOrderProduct.getProductUnitAmount());
                exchangeOrderProductDO.setDepositCycle(exchangeOrderProduct.getDepositCycle());
                exchangeOrderProductDO.setPaymentCycle(exchangeOrderProduct.getPaymentCycle());
                exchangeOrderProductDO.setPayMode(exchangeOrderProduct.getPayMode());
                OrderProductDO orderProductDO = orderProductDOMap.get(exchangeOrderProduct.getOrderProductId());
                //判断是否发生变更
                if(!(exchangeOrderProductDO.getDepositCycle().equals(orderProductDO.getDepositCycle())&&
                        exchangeOrderProductDO.getPaymentCycle().equals(orderProductDO.getPaymentCycle())&&
                        exchangeOrderProductDO.getPayMode().equals(orderProductDO.getPayMode())&&
                        exchangeOrderProductDO.getProductUnitAmount().equals(orderProductDO.getProductUnitAmount()))){
                    isUpd=true;
                }
                exchangeOrderProductDOList.add(exchangeOrderProductDO);
            }
            exchangeOrderProductMapper.updateList(exchangeOrderProductDOList);
        }
        if (!CollectionUtil.isEmpty(exchangeOrder.getExchangeOrderMaterialList())) {
            List<ExchangeOrderMaterialDO> exchangeOrderMaterialDOList = new ArrayList<>();
            Map<Integer, OrderMaterialDO> orderMaterialDOMap = ListUtil.listToMap(orderDO.getOrderMaterialDOList(), "id");
            //保存配件信息
            for (ExchangeOrderMaterial exchangeOrderMaterial : exchangeOrder.getExchangeOrderMaterialList()) {
                ExchangeOrderMaterialDO exchangeOrderMaterialDO=new ExchangeOrderMaterialDO();
                exchangeOrderMaterialDO.setId(exchangeOrderMaterial.getExchangeOrderMaterialId());
                exchangeOrderMaterialDO.setMaterialUnitAmount(exchangeOrderMaterial.getMaterialUnitAmount());
                exchangeOrderMaterialDO.setDepositCycle(exchangeOrderMaterial.getDepositCycle());
                exchangeOrderMaterialDO.setPaymentCycle(exchangeOrderMaterial.getPaymentCycle());
                exchangeOrderMaterialDO.setPayMode(exchangeOrderMaterial.getPayMode());
                OrderMaterialDO orderMaterialDO = orderMaterialDOMap.get(exchangeOrderMaterial.getOrderProductId());
                //判断是否发生变更
                if(!(exchangeOrderMaterialDO.getDepositCycle().equals(orderMaterialDO.getDepositCycle())&&
                        exchangeOrderMaterialDO.getPaymentCycle().equals(orderMaterialDO.getPaymentCycle())&&
                        exchangeOrderMaterialDO.getPayMode().equals(orderMaterialDO.getPayMode())&&
                        exchangeOrderMaterialDO.getMaterialUnitAmount().equals(orderMaterialDO.getMaterialUnitAmount()))){
                    isUpd=true;
                }
                exchangeOrderMaterialDOList.add(exchangeOrderMaterialDO);
            }
            exchangeOrderMaterialMapper.updateList(exchangeOrderMaterialDOList);
        }
        if(!isUpd){//如果没发生变更，回滚数据
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            result.setErrorCode(ErrorCode.NOT_EXCHANGE_ORDER);
            return result;
        }
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(exchangeOrderDO.getExchangeOrderNo());
        return result;
    }


    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, String> commitExchangeOrder(ExchangeOrderCommitParam exchangeOrderCommitParam) {
        ServiceResult<String, String> result = new ServiceResult<>();
        User loginUser = userSupport.getCurrentUser();
        String orderNo = exchangeOrderCommitParam.getExchangeOrderNo();
        Integer verifyUser = exchangeOrderCommitParam.getVerifyUser();
        String commitRemark = exchangeOrderCommitParam.getCommitRemark();
        ExchangeOrderDO exchangeOrderDO = exchangeOrderMapper.findByExchangeOrderNo(orderNo);
        if (null == exchangeOrderDO) {
            result.setErrorCode(ErrorCode.ORDER_NOT_EXISTS);
            return result;
        }
        //只有创建订单本人可以提交
        if (!exchangeOrderDO.getCreateUser().equals(loginUser.getUserId().toString())) {
            result.setErrorCode(ErrorCode.COMMIT_ONLY_SELF);
            return result;
        }
        //判断变更单状态
        if (!CommonConstant.DATA_STATUS_ENABLE.equals(exchangeOrderDO.getDataStatus()) && ExchangeOrderStatus.ORDER_STATUS_WAIT_COMMIT.equals(exchangeOrderDO.getStatus())) {
            result.setErrorCode(ErrorCode.EXCHANGE_ORDER_STATUS_ERROR);
            return result;
        }
        String verifyMatters = "订单变更审核，审核变更单的生效时间，订单项的单价、租赁方案、支付方式";
        exchangeOrderDO.setStatus(ExchangeOrderStatus.ORDER_STATUS_VERIFYING);
        exchangeOrderMapper.update(exchangeOrderDO);
        ServiceResult<String, String> workflowCommitResult = workflowService.commitWorkFlow(WorkflowType.WORKFLOW_TYPE_EXCHANGE_ORDER, orderNo, verifyUser, verifyMatters, commitRemark, exchangeOrderCommitParam.getImgIdList(), null);
        if (!ErrorCode.SUCCESS.equals(workflowCommitResult.getErrorCode())) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            result.setErrorCode(workflowCommitResult.getErrorCode());
            return result;
        }
        result.setResult(orderNo);
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    public ServiceResult<String, ExchangeOrder> queryExchangeOrderByNo(String exchangerOrderNo) {
        ServiceResult<String, ExchangeOrder> result = new ServiceResult<>();
        ExchangeOrderDO exchangeOrderDO = exchangeOrderMapper.findByExchangeOrderNo(exchangerOrderNo);
        if (null == exchangeOrderDO) {
            result.setErrorCode(ErrorCode.ORDER_NOT_EXISTS);
            return result;
        }
        List<ExchangeOrderProductDO> exchangeOrderProductDOLiost = exchangeOrderProductMapper.findByExchangeOrderId(exchangeOrderDO.getId());
        List<ExchangeOrderMaterialDO> exchangeOrderMaterialDO = exchangeOrderMaterialMapper.findByExchangeOrderId(exchangeOrderDO.getId());
        exchangeOrderDO.setExchangeOrderMaterialDOList(exchangeOrderMaterialDO);
        exchangeOrderDO.setExchangeOrderProductDOList(exchangeOrderProductDOLiost);
        ExchangeOrder exchangeOrder = ConverterUtil.convert(exchangeOrderDO, ExchangeOrder.class);
        OrderDO orderDO = orderMapper.findByOrderNo(exchangeOrder.getOrderNo());
        Order order = ConverterUtil.convert(orderDO, Order.class);
        exchangeOrder.setOrder(order);
        if (CollectionUtil.isNotEmpty(exchangeOrder.getExchangeOrderProductList())) {
            for (ExchangeOrderProduct exchangeOrderProduct : exchangeOrder.getExchangeOrderProductList()) {
                if (CollectionUtil.isNotEmpty(order.getOrderProductList())) {
                    for (OrderProduct orderProduct : order.getOrderProductList()) {
                        if (orderProduct.getOrderProductId().equals(exchangeOrderProduct.getOrderProductId())) {
                            exchangeOrderProduct.setOrderProduct(orderProduct);
                            break;
                        }
                    }
                }
            }
        }
        if (CollectionUtil.isNotEmpty(exchangeOrder.getExchangeOrderMaterialList())) {
            for (ExchangeOrderMaterial exchangeOrderMaterial : exchangeOrder.getExchangeOrderMaterialList()) {
                if (CollectionUtil.isNotEmpty(order.getOrderMaterialList())) {
                    for (OrderMaterial orderMaterial : order.getOrderMaterialList()) {
                        if (orderMaterial.getOrderMaterialId().equals(exchangeOrderMaterial.getOrderProductId())) {
                            exchangeOrderMaterial.setOrderMaterial(orderMaterial);
                            break;
                        }
                    }
                }
            }
        }
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(exchangeOrder);
        return result;
    }

    @Override
    public ServiceResult<String, Page<ExchangeOrder>> queryByOrderNo(String orderNo) {
        ServiceResult<String, Page<ExchangeOrder>> result = new ServiceResult<>();
         OrderDO orderDO= orderMapper.findByOrderNo(orderNo);
        if (null == orderDO) {
            result.setErrorCode(ErrorCode.ORDER_NOT_EXISTS);
            return result;
        }
        String originalOrderNo=orderDO.getOriginalOrderNo();
        Map<String, Object> maps = new HashMap<>();
        maps.put("start", 0);
        maps.put("pageSize", 100);
        maps.put("orderNo", originalOrderNo);
        maps.put("orderQueryParam", new HashMap<>());
        Integer count = exchangeOrderMapper.listCount(maps);
        List<ExchangeOrderDO> exchangeOrderDOList = exchangeOrderMapper.listPage(maps);
        List<ExchangeOrder> exchangeOrderList = ConverterUtil.convertList(exchangeOrderDOList, ExchangeOrder.class);
        Page<ExchangeOrder> page = new Page<>(exchangeOrderList, count, 0, 100);
        result.setResult(page);
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, String> cancelExchangeOrder(String exchangerOrderNo) {
        ServiceResult<String, String> result = new ServiceResult<>();
        Date currentTime = new Date();
        User loginUser = userSupport.getCurrentUser();
        ExchangeOrderDO exchangeOrderDO = exchangeOrderMapper.findByExchangeOrderNo(exchangerOrderNo);
        if (null == exchangeOrderDO) {
            result.setErrorCode(ErrorCode.ORDER_NOT_EXISTS);
            return result;
        }
        if (!CommonConstant.DATA_STATUS_ENABLE.equals(exchangeOrderDO.getDataStatus()) && ExchangeOrderStatus.ORDER_STATUS_WAIT_COMMIT.equals(exchangeOrderDO.getStatus())) {
            result.setErrorCode(ErrorCode.EXCHANGE_ORDER_STATUS_ERROR);
            return result;
        }

        //审核通过

        exchangeOrderDO.setStatus(ExchangeOrderStatus.ORDER_STATUS_CANCEL);
        exchangeOrderDO.setUpdateTime(currentTime);
        exchangeOrderDO.setUpdateUser(loginUser.getUserId().toString());
        exchangeOrderMapper.update(exchangeOrderDO);
        result.setResult(exchangeOrderDO.getExchangeOrderNo());
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public synchronized ServiceResult<String, String> generatedOrder(String exchangerOrderNo,Boolean isVerify) {
        ServiceResult<String, String> result = new ServiceResult<>();
        Date currentTime = new Date();
        User loginUser = userSupport.getCurrentUser();
        ExchangeOrderDO exchangeOrderDO = exchangeOrderMapper.findByExchangeOrderNo(exchangerOrderNo);
        if(isVerify) {
            if (null == exchangeOrderDO) {
                result.setErrorCode(ErrorCode.ORDER_NOT_EXISTS);
                return result;
            }

            if (!(CommonConstant.DATA_STATUS_ENABLE.equals(exchangeOrderDO.getDataStatus()) && ExchangeOrderStatus.ORDER_STATUS_CONFIRM.equals(exchangeOrderDO.getStatus()))) {
                result.setErrorCode(ErrorCode.EXCHANGE_ORDER_STATUS_ERROR);
                return result;
            }
        }
        exchangeOrderDO.setStatus(ExchangeOrderStatus.ORDER_STATUS_OK);
        exchangeOrderMapper.update(exchangeOrderDO);
        exchangeOrderDO.setExchangeOrderMaterialDOList(exchangeOrderMaterialMapper.findByExchangeOrderId(exchangeOrderDO.getId()));
        exchangeOrderDO.setExchangeOrderProductDOList(exchangeOrderProductMapper.findByExchangeOrderId(exchangeOrderDO.getId()));
        //1、查询原订单信息
        OrderDO orderDO = orderMapper.findByOrderNo(exchangeOrderDO.getOrderNo());
        Integer originalOrderId = orderDO.getId();
        //1.1更新原订单信息
        orderDO.setOrderStatus(OrderStatus.ORDER_STATUS_RETURN_BACK); //原订单改为已归还状态
        orderDO.setIsTurnRentOrder(CommonConstant.COMMON_TWO);
        orderDO.setUpdateTime(currentTime);
        orderDO.setUpdateUser(loginUser.getUserId().toString());
        orderDO.setIsExchangeOrder(CommonConstant.COMMON_CONSTANT_YES);
        orderMapper.update(orderDO);
        //1.2更新地址信息
        OrderConsignInfoDO oldOrderConsignInfoDO = orderConsignInfoMapper.findByOrderId(orderDO.getId());
        //1.3获取原订单信息，生成新订单
        Date originalExpectReturnTime = orderDO.getExpectReturnTime();
        Date originalRentStartTime = orderDO.getRentStartTime();
        String originalOrderNo = orderDO.getOriginalOrderNo();
        String nodeOrderNo = orderDO.getOrderNo();
        if (CommonConstant.COMMON_CONSTANT_YES.equals(orderDO.getIsOriginalOrder())) {//如果是原单
            originalOrderNo = nodeOrderNo;
        }
        //2.1根据原订单号，获取订单流
        List<OrderFlowDO> orderFlowDOList = orderFlowMapper.findByOriginalOrderNo(originalOrderNo);
        Date rentStartTime = orderDO.getRentStartTime();
        Date expectReturnTime = orderDO.getExpectReturnTime();
        Integer countOrderFlow = 0;
        if (CollectionUtil.isNotEmpty(orderFlowDOList)) {
            originalExpectReturnTime = orderFlowDOList.get(0).getOriginalExpectReturnTime();
            originalRentStartTime = orderFlowDOList.get(0).getOriginalRentStartTime();
            countOrderFlow = orderFlowDOList.size();
        }
        //2.2生成新订单号
        String orderNO = originalOrderNo + "-" + String.valueOf(countOrderFlow + 1);
        //2.3 新订单流
        OrderFlowDO orderFlowDO = this.initOrderFlowDO(originalOrderNo, nodeOrderNo, orderNO, originalExpectReturnTime, originalRentStartTime, rentStartTime, expectReturnTime, loginUser.getUserId().toString());
        orderFlowMapper.save(orderFlowDO);
        //2.4 新订单创建\处理
        OrderDO newOrderDO = orderDO;
        newOrderDO.setOrderNo(orderNO);
        newOrderDO.setId(null);
        newOrderDO.setOriginalOrderNo(originalOrderNo);
        newOrderDO.setIsOriginalOrder(CommonConstant.COMMON_CONSTANT_NO);
        newOrderDO.setIsExchangeOrder(CommonConstant.COMMON_CONSTANT_NO);
        newOrderDO.setOrderStatus(OrderStatus.ORDER_STATUS_CONFIRM);
        newOrderDO.setRentStartTime(exchangeOrderDO.getRentStartTime());
        //2.4.1重新计算价格
        int[] diff = com.lxzl.erp.common.util.DateUtil.getDiff(newOrderDO.getRentStartTime(), expectReturnTime);
        if (diff[1] > 0) {
            diff[0] += 1;
        }
        if (diff[0] == 0) {
            diff[0] += 1;
        }
        newOrderDO.setRentTimeLength(diff[0]);
        //生成新的订单
        if (CollectionUtil.isNotEmpty(newOrderDO.getOrderProductDOList())) {
            for (OrderProductDO orderProductDO : newOrderDO.getOrderProductDOList()) {
                //处理商品
                if (CollectionUtil.isNotEmpty(exchangeOrderDO.getExchangeOrderProductDOList())) {
                    for (ExchangeOrderProductDO exchangeOrderProductDO : exchangeOrderDO.getExchangeOrderProductDOList()) {
                        if (exchangeOrderProductDO.getOrderProductId().equals(orderProductDO.getId())) {
                            orderProductDO.setProductUnitAmount(exchangeOrderProductDO.getProductUnitAmount());
                            orderProductDO.setRentTimeLength(diff[0]);
                            orderProductDO.setDepositCycle(exchangeOrderProductDO.getDepositCycle());
                            orderProductDO.setPaymentCycle(exchangeOrderProductDO.getPaymentCycle());
                            orderProductDO.setPayMode(exchangeOrderProductDO.getPayMode());
                            break;
                        }
                    }
                } else {
                    break;
                }
            }
        }
        if (CollectionUtil.isNotEmpty(newOrderDO.getOrderMaterialDOList())) {
            for (OrderMaterialDO orderMaterialDO : newOrderDO.getOrderMaterialDOList()) {
                if (CollectionUtil.isNotEmpty(exchangeOrderDO.getExchangeOrderMaterialDOList())) {
                    for (ExchangeOrderMaterialDO exchangeOrderMaterialDO : exchangeOrderDO.getExchangeOrderMaterialDOList()) {
                        if (exchangeOrderMaterialDO.getOrderProductId().equals(orderMaterialDO.getId())) {
                            orderMaterialDO.setMaterialUnitAmount(exchangeOrderMaterialDO.getMaterialUnitAmount());
                            orderMaterialDO.setRentTimeLength(diff[0]);
                            orderMaterialDO.setDepositCycle(exchangeOrderMaterialDO.getDepositCycle());
                            orderMaterialDO.setPaymentCycle(exchangeOrderMaterialDO.getPaymentCycle());
                            orderMaterialDO.setPayMode(exchangeOrderMaterialDO.getPayMode());
                            break;
                        }
                    }
                } else {
                    break;
                }
            }
        }
        //计算价格，需要减去，多余的天数


        this.calculateOrderProductInfo(newOrderDO.getOrderProductDOList(), newOrderDO);
        this.calculateOrderMaterialInfo(newOrderDO.getOrderMaterialDOList(), newOrderDO);
        orderDO.setTotalOrderAmount(BigDecimalUtil.sub(BigDecimalUtil.add(BigDecimalUtil.add(BigDecimalUtil.add(orderDO.getTotalProductAmount(), orderDO.getTotalMaterialAmount()), orderDO.getLogisticsAmount()), orderDO.getTotalInsuranceAmount()), orderDO.getTotalDiscountAmount()));
        orderMapper.save(newOrderDO);
        CustomerConsignInfoDO userConsignInfoDO = customerConsignInfoMapper.findById(oldOrderConsignInfoDO.getCustomerConsignId());
        if (userConsignInfoDO != null) {
            orderService.updateOrderConsignInfo(oldOrderConsignInfoDO.getCustomerConsignId(), orderDO.getId(), loginUser, currentTime);
        }else {
            OrderConsignInfoDO dbOrderConsignInfoDO = orderConsignInfoMapper.findByOrderId(orderDO.getId());
            OrderConsignInfoDO orderConsignInfoDO = new OrderConsignInfoDO();
            orderConsignInfoDO.setOrderId(orderDO.getId());
            orderConsignInfoDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
            orderConsignInfoDO.setCustomerConsignId(oldOrderConsignInfoDO.getCustomerConsignId());
            orderConsignInfoDO.setConsigneeName(oldOrderConsignInfoDO.getConsigneeName());
            orderConsignInfoDO.setConsigneePhone(oldOrderConsignInfoDO.getConsigneePhone());
            orderConsignInfoDO.setProvince(oldOrderConsignInfoDO.getProvince());
            orderConsignInfoDO.setCity(oldOrderConsignInfoDO.getCity());
            orderConsignInfoDO.setDistrict(oldOrderConsignInfoDO.getDistrict());
            orderConsignInfoDO.setAddress(oldOrderConsignInfoDO.getAddress());
            if (dbOrderConsignInfoDO == null) {
                orderConsignInfoDO.setCreateUser(loginUser.getUserId().toString());
                orderConsignInfoDO.setUpdateUser(loginUser.getUserId().toString());
                orderConsignInfoDO.setCreateTime(currentTime);
                orderConsignInfoDO.setUpdateTime(currentTime);
                orderConsignInfoMapper.save(orderConsignInfoDO);
            } else {
                if (!dbOrderConsignInfoDO.getCustomerConsignId().equals(oldOrderConsignInfoDO.getCustomerConsignId())) {
                    dbOrderConsignInfoDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
                    dbOrderConsignInfoDO.setId(dbOrderConsignInfoDO.getId());
                    dbOrderConsignInfoDO.setUpdateUser(loginUser.getUserId().toString());
                    dbOrderConsignInfoDO.setUpdateTime(currentTime);
                    orderConsignInfoMapper.update(dbOrderConsignInfoDO);

                    orderConsignInfoDO.setCreateUser(loginUser.getUserId().toString());
                    orderConsignInfoDO.setUpdateUser(loginUser.getUserId().toString());
                    orderConsignInfoDO.setCreateTime(currentTime);
                    orderConsignInfoDO.setUpdateTime(currentTime);
                    orderConsignInfoMapper.save(orderConsignInfoDO);
                }else {
                    orderConsignInfoDO.setCreateUser(loginUser.getUserId().toString());
                    orderConsignInfoDO.setUpdateUser(loginUser.getUserId().toString());
                    orderConsignInfoDO.setCreateTime(currentTime);
                    orderConsignInfoDO.setUpdateTime(currentTime);
                    orderConsignInfoMapper.save(orderConsignInfoDO);
                }
            }
        }

        //2.4.2 订单商品项生成
        // 结算单用到的订单数据关系
        Map<String, String> orderItemUnionKeyMapping = new HashMap<>();
        //生成新的订单
        if (CollectionUtil.isNotEmpty(newOrderDO.getOrderProductDOList())) {
            for (OrderProductDO orderProductDO : newOrderDO.getOrderProductDOList()) {
                orderProductDO.setOrderId(newOrderDO.getId());
                orderProductDO.setTestMachineOrderProductId(orderProductDO.getId());//临时使用，存储原有的商品项id
                orderProductDO.setId(null);
            }
            orderService.saveOrderProductInfo(newOrderDO.getOrderProductDOList(), newOrderDO.getId(), loginUser, currentTime);
            for (OrderProductDO orderProductDO : newOrderDO.getOrderProductDOList()) {
                String key = newOrderDO.getId().toString() + "_" + OrderItemType.ORDER_ITEM_TYPE_PRODUCT.toString() + "_" + orderProductDO.getId();
                String value = originalOrderId.toString() + "_" + OrderItemType.ORDER_ITEM_TYPE_PRODUCT.toString() + "_" + orderProductDO.getTestMachineOrderProductId();
                orderItemUnionKeyMapping.put(key, value);
            }
        }
        if (CollectionUtil.isNotEmpty(newOrderDO.getOrderMaterialDOList())) {
            for (OrderMaterialDO orderMaterialDO : newOrderDO.getOrderMaterialDOList()) {
                orderMaterialDO.setOrderId(newOrderDO.getId());
                orderMaterialDO.setTestMachineOrderMaterialId(orderMaterialDO.getId());
                orderMaterialDO.setId(null);
            }
            orderService.saveOrderMaterialInfo(newOrderDO.getOrderMaterialDOList(), newOrderDO.getId(), loginUser, currentTime);
            for (OrderMaterialDO orderMaterialDO : newOrderDO.getOrderMaterialDOList()) {
                String key = newOrderDO.getId().toString() + "_" + OrderItemType.ORDER_ITEM_TYPE_MATERIAL.toString() + "_" + orderMaterialDO.getId();
                String value = originalOrderId.toString() + "_" + OrderItemType.ORDER_ITEM_TYPE_MATERIAL.toString() + "_" + orderMaterialDO.getTestMachineOrderMaterialId();
                orderItemUnionKeyMapping.put(key, value);
            }
        }
        //创建订单轴
        orderTimeAxisSupport.addOrderTimeAxis(newOrderDO.getId(), newOrderDO.getOrderStatus(), null, currentTime, loginUser.getUserId(), OperationType.CREATE_ORDER);
        //3、生成结算单
        ServiceResult<String, BigDecimal> createStatementOrderResult = statementService.createOrderStatement(newOrderDO.getOrderNo());
        if (!ErrorCode.SUCCESS.equals(createStatementOrderResult.getErrorCode())) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            result.setErrorCode(ErrorCode.SYSTEM_ERROR);
            result.setResult(newOrderDO.getOrderNo());
            return result;
        }
        newOrderDO.setFirstNeedPayAmount(createStatementOrderResult.getResult());
        //审核订单轴
        orderTimeAxisSupport.addOrderTimeAxis(newOrderDO.getId(), newOrderDO.getOrderStatus(), null, currentTime, loginUser.getUserId(), OperationType.VERIFY_ORDER_SUCCESS);

        //3、变更单给K3传数据
        Order order = ConverterUtil.convert(newOrderDO, Order.class);
        order.setTestMachineOrderNo(nodeOrderNo);
        //TODO 开启K3
        k3Service.testMachineOrderTurnRentOrder(order);

        //4、转移退货
        Map<String, Object> maps = new HashMap<>();
        maps.put("orderNo", nodeOrderNo);
        maps.put("nowDate", exchangeOrderDO.getRentStartTime());
        //获取所有退货列表，进行退货转移处理
        List<K3ReturnOrderDO> k3ReturnOrderDOList = k3ReturnOrderMapper.findReturnOrderByOrderNoAndDate(maps);
        if (CollectionUtil.isNotEmpty(k3ReturnOrderDOList)) {
            if (CollectionUtil.isNotEmpty(newOrderDO.getOrderProductDOList())) {
                for (OrderProductDO orderProductDO : newOrderDO.getOrderProductDOList()) {
                    this.updk3ReturnOrderProduct(k3ReturnOrderDOList, orderProductDO, null, newOrderDO.getOrderNo());
                }
            }
            if (CollectionUtil.isNotEmpty(newOrderDO.getOrderMaterialDOList())) {
                for (OrderMaterialDO orderMaterialDO : newOrderDO.getOrderMaterialDOList()) {
                    this.updk3ReturnOrderProduct(k3ReturnOrderDOList, null, orderMaterialDO, newOrderDO.getOrderNo());
                }
            }
        }

        //5、原订单结算单处理
        Map<String, AmountNeedReturn> oldAmountMap = statementOrderSupport.stopOldMonthRentOrder(nodeOrderNo, exchangeOrderDO.getRentStartTime());
        //修正当前客户下所有结算单的开始结束时间
        statementService.fixCustomerStatementOrderStatementTime(newOrderDO.getBuyerCustomerId());
        //6、新订单结算单处理
        String statementResult = statementOrderSupport.fillOldOrderAmountToNew(newOrderDO.getOrderNo(), oldAmountMap, orderItemUnionKeyMapping);
        if (!ErrorCode.SUCCESS.equals(statementResult)) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            result.setErrorCode(ErrorCode.SYSTEM_ERROR);
            result.setResult(newOrderDO.getOrderNo());
            return result;
        }

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(newOrderDO.getOrderNo());
        return result;
    }

    //task自动任务生成订单
    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, String> taskGeneratedOrder(){
        ServiceResult<String, String> result=new ServiceResult<>();
        //获取当前时间之前
        Date currentTime = new Date();
        List<ExchangeOrderDO> exchangeOrderDOList=exchangeOrderMapper.findByRentStartTime(currentTime);
        for (ExchangeOrderDO exchangeOrderDO:exchangeOrderDOList){
            generatedOrder(exchangeOrderDO.getExchangeOrderNo(),true);
        }
        result.setErrorCode(ErrorCode.SUCCESS);
       return result;
    }

    /**
     * 审核结果处理
     *
     * @param verifyResult
     * @param businessNo
     * @return
     */
    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public String receiveVerifyResult(boolean verifyResult, String businessNo) {
        Date currentTime = new Date();
        User loginUser = userSupport.getCurrentUser();
        ExchangeOrderDO exchangeOrderDO = exchangeOrderMapper.findByExchangeOrderNo(businessNo);
        if (CommonConstant.DATA_STATUS_ENABLE.equals(exchangeOrderDO.getDataStatus()) && ExchangeOrderStatus.ORDER_STATUS_WAIT_COMMIT.equals(exchangeOrderDO.getStatus())) {
            return ErrorCode.EXCHANGE_ORDER_STATUS_ERROR;
        }
        //审核通过
        if (verifyResult) {
            exchangeOrderDO.setStatus(ExchangeOrderStatus.ORDER_STATUS_CONFIRM);
            if(exchangeOrderDO.getRentStartTime().compareTo(currentTime)<=0){
                //比当前时间早，就自动触发生成订单
                exchangeOrderDO.setStatus(ExchangeOrderStatus.ORDER_STATUS_OK);
                this.generatedOrder(exchangeOrderDO.getExchangeOrderNo(),false);
            }
        } else {
            exchangeOrderDO.setStatus(ExchangeOrderStatus.ORDER_STATUS_WAIT_COMMIT);
        }
        exchangeOrderDO.setUpdateTime(currentTime);
        exchangeOrderDO.setUpdateUser(loginUser.getUserId().toString());
        exchangeOrderMapper.update(exchangeOrderDO);
        return ErrorCode.SUCCESS;
    }

    private OrderFlowDO initOrderFlowDO(String originalOrderNo, String nodeOrderNo, String orderNo, Date originalExpectReturnTime, Date originalRentStartTime, Date rentStartTime, Date expectReturnTime, String userId) {
        OrderFlowDO orderFlowDO = new OrderFlowDO();
        Date now = new Date();
        orderFlowDO.setOriginalOrderNo(originalOrderNo);
        orderFlowDO.setNodeOrderNo(nodeOrderNo);
        orderFlowDO.setOrderNo(orderNo);
        orderFlowDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        orderFlowDO.setOriginalExpectReturnTime(originalExpectReturnTime);
        orderFlowDO.setOriginalRentStartTime(originalRentStartTime);
        orderFlowDO.setRentStartTime(rentStartTime);
        orderFlowDO.setExpectReturnTime(expectReturnTime);
        orderFlowDO.setCreateTime(now);
        orderFlowDO.setCreateUser(userId);
        orderFlowDO.setUpdateUser(userId);
        orderFlowDO.setUpdateTime(now);
        return orderFlowDO;
    }

    private void updk3ReturnOrderProduct(List<K3ReturnOrderDO> k3ReturnOrderDOList, OrderProductDO orderProductDO, OrderMaterialDO orderMaterialDO, String orderNo) {
        Integer orderItemId = null == orderProductDO ? orderMaterialDO.getTestMachineOrderMaterialId() : orderProductDO.getTestMachineOrderProductId();
        for (K3ReturnOrderDO k3ReturnOrderDO : k3ReturnOrderDOList) {
            List<K3ReturnOrderDetailDO> k3ReturnOrderDetailDOList = k3ReturnOrderDO.getK3ReturnOrderDetailDOList();
            for (K3ReturnOrderDetailDO k3ReturnOrderDetailDO : k3ReturnOrderDetailDOList) {
                if (k3ReturnOrderDetailDO.getOrderItemId().equals(orderItemId.toString())) {
                    Date date = new Date();
                    k3ReturnOrderDetailDO.setOrderNo(orderNo);
                    k3ReturnOrderDetailDO.setOrderItemId(orderItemId.toString());
                    k3ReturnOrderDetailDO.setUpdateTime(date);
                    k3ReturnOrderDetailDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                    k3ReturnOrderDetailMapper.update(k3ReturnOrderDetailDO);
                }
            }
        }
    }


    /**
     * 校验信息
     *
     * @param orderDO
     * @param exchangeOrder
     * @return
     */
    private ServiceResult<String, String> verifyOrderInfo(OrderDO orderDO,ExchangeOrder exchangeOrder){
        ServiceResult<String, String> result = new ServiceResult<>();
        if (null == orderDO) {
            result.setErrorCode(ErrorCode.ORDER_NOT_EXISTS);
            return result;
        }
        //只能按月租
        if (OrderRentType.RENT_TYPE_DAY.equals(orderDO.getRentType())) {
            result.setErrorCode(ErrorCode.ONLY_MONTH_RENT_ALLOW_CHANGE_ORDER);
            return result;
        }

        if (exchangeOrder.getRentStartTime().compareTo(orderDO.getRentStartTime()) < 0) {
            //不能小于起租日
            result.setErrorCode(ErrorCode.EXCHANGE_ORDER_NO_SATRT_TIME);
            return result;
        }
        //不能超过最后一期时间
        Date expectReturnTime = orderSupport.generateExpectReturnTime(orderDO);
        if (exchangeOrder.getRentStartTime().compareTo(expectReturnTime) > 0) {
            //不能大于最后一期
            result.setErrorCode(ErrorCode.EXCHANGE_ORDER_NO_EXPECT_RETURN_TIME);
            return result;
        }

        //只有租赁中的订单才可以进行
        if (!(OrderStatus.ORDER_STATUS_CONFIRM.equals(orderDO.getOrderStatus()) || OrderStatus.ORDER_STATUS_PART_RETURN.equals(orderDO.getOrderStatus()))) {
            result.setErrorCode(ErrorCode.ORDER_NOT_EXISTS);
            return result;
        }

        //查询是否存在换货单
        //查询是否还有未完成的
        List<ReplaceOrderDO> replaceOrderDOList = replaceOrderMapper.findByOrderNoForCheck(exchangeOrder.getOrderNo());
        if (CollectionUtil.isNotEmpty(replaceOrderDOList)) {
            result.setErrorCode(ErrorCode.REPLACE_ORDER_EXISTS);
            return result;
        }

        //下单首月不能更改
        //月末结算，变更当月1号，如果是20号结算，就变更21号
        //获取当前月的结算日、如果是31号就是当前月的最后一天。如果是其他就跟着你月份走就可以了。
        Integer statementDays = statementOrderSupport.getCustomerStatementDate(orderDO.getStatementDate(), exchangeOrder.getRentStartTime());

        Date newRentStartTime = new Date();

        if (statementDays == 31) {//如果是月末。就获取一个月的第一天
            //获取第一个月的第一天
            newRentStartTime = DateUtil.getStart8MonthDate(exchangeOrder.getRentStartTime());
        } else {
            //如果是20号，或者是自然日 TODO 2月份特殊处理
            newRentStartTime = DateUtil.getMonthAndDay(exchangeOrder.getRentStartTime(), statementDays + 1);
        }
        //起租日，当月第一天
        Date rentStartTime = DateUtil.getStartMonthDate(orderDO.getRentStartTime());
        //起租日，当月最后一天
        Date rentEndTime = DateUtil.getEndMonthDate(orderDO.getRentStartTime());
        if (rentEndTime.compareTo(newRentStartTime) > 0) {
            if (rentStartTime.compareTo(newRentStartTime) < 0) {
                //下单首月不能更改
                result.setErrorCode(ErrorCode.EXCHANGE_ORDER_NO_FIRST_MONTH);
                return result;
            }
        }
        //生效时间不能超过最后一期的时间
        if (newRentStartTime.compareTo(expectReturnTime) > 0) {
            result.setErrorCode(ErrorCode.EXCHANGE_ORDER_NO_EXPECT_RETURN_TIME);
            return result;
        }
        exchangeOrder.setRentStartTime(newRentStartTime);
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    /**
     * 更新原订单数据
     */
    private void originalOrder(OrderDO orderDO,BigDecimal newAmount,Date expectReturnTime){
        BigDecimal productAmountTotal=BigDecimal.ZERO;
        BigDecimal materialAmountTotal=BigDecimal.ZERO;
        for (OrderProductDO orderProductDO : orderDO.getOrderProductDOList()) {
            BigDecimal productAmount = amountSupport.calculateRentAmount(orderDO.getRentStartTime(),orderDO.getExpectReturnTime(),orderProductDO.getProductUnitAmount(),orderProductDO.getProductCount());
            productAmountTotal=BigDecimalUtil.add(productAmountTotal,productAmount);
        }
        for (OrderMaterialDO orderMaterialDO:orderDO.getOrderMaterialDOList()) {
            BigDecimal materialAmount = amountSupport.calculateRentAmount(orderDO.getRentStartTime(),orderDO.getExpectReturnTime(),orderMaterialDO.getMaterialUnitAmount(),orderMaterialDO.getMaterialCount());
            materialAmountTotal=BigDecimalUtil.add(materialAmountTotal,materialAmount);
        }
        orderDO.setTotalProductAmount(productAmountTotal);
        orderDO.setTotalMaterialAmount(materialAmountTotal);
        orderDO.setTotalOrderAmount(BigDecimalUtil.sub(BigDecimalUtil.add(BigDecimalUtil.add(BigDecimalUtil.add(orderDO.getTotalProductAmount(), orderDO.getTotalMaterialAmount()), orderDO.getLogisticsAmount()), orderDO.getTotalInsuranceAmount()), orderDO.getTotalDiscountAmount()));

    }


    //TODO 重构
    public void calculateOrderProductInfo(List<OrderProductDO> orderProductDOList, OrderDO orderDO) {
        CustomerRiskManagementDO customerRiskManagementDO = customerSupport.getCustomerRiskManagementDO(orderDO.getBuyerCustomerId());
        if (orderProductDOList != null && !orderProductDOList.isEmpty()) {
            int productCount = 0;
            // 商品租赁总额
            BigDecimal productAmountTotal = new BigDecimal(0.0);
            // 保险总额
            BigDecimal totalInsuranceAmount = new BigDecimal(0.0);
            // 设备该交押金总额
            BigDecimal totalDepositAmount = new BigDecimal(0.0);
            // 设备信用押金总额
            BigDecimal totalCreditDepositAmount = new BigDecimal(0.0);
            // 租赁押金总额
            BigDecimal totalRentDepositAmount = new BigDecimal(0.0);
            for (OrderProductDO orderProductDO : orderProductDOList) {
                String skuName = "";
                BigDecimal depositAmount = BigDecimal.ZERO;
                BigDecimal creditDepositAmount = BigDecimal.ZERO;
                BigDecimal rentDepositAmount = BigDecimal.ZERO;
                BigDecimal skuPrice = BigDecimal.ZERO;
                Product product = new Product();
                if (!CommonConstant.COMMON_CONSTANT_YES.equals(orderDO.getIsK3Order())) {

                    ServiceResult<String, Product> productServiceResult = productService.queryProductBySkuId(orderProductDO.getProductSkuId());
                    if (!ErrorCode.SUCCESS.equals(productServiceResult.getErrorCode())) {
                        throw new BusinessException(productServiceResult.getErrorCode());
                    }
                    product = productServiceResult.getResult();
                    orderProductDO.setProductName(product.getProductName());
                    ProductSku productSku = CollectionUtil.isNotEmpty(product.getProductSkuList()) ? product.getProductSkuList().get(0) : null;
                    if (productSku == null) {
                        throw new BusinessException(ErrorCode.PRODUCT_SKU_IS_NULL_OR_NOT_EXISTS);
                    }
                    skuPrice = CommonConstant.COMMON_CONSTANT_YES.equals(orderProductDO.getIsNewProduct()) ? productSku.getNewSkuPrice() : productSku.getSkuPrice();
                    skuName = productSku.getSkuName();
                }

                // 小于等于90天的,不走风控，大于90天的，走风控授信
                if (OrderRentType.RENT_TYPE_DAY.equals(orderProductDO.getRentType()) && orderProductDO.getRentTimeLength() <= CommonConstant.ORDER_NEED_VERIFY_DAYS) {
                    if (orderProductDO.getProductCount() > 0) {
                        BigDecimal remainder = orderProductDO.getDepositAmount().divideAndRemainder(new BigDecimal(orderProductDO.getProductCount()))[1];
                        if (BigDecimalUtil.compare(remainder, BigDecimal.ZERO) != 0) {
                            throw new BusinessException(ErrorCode.ORDER_PRODUCT_DEPOSIT_ERROR);
                        }
                    }
                    depositAmount = orderProductDO.getDepositAmount();
                    totalDepositAmount = BigDecimalUtil.add(totalDepositAmount, depositAmount);
                } else if (customerRiskManagementDO != null && CommonConstant.COMMON_CONSTANT_YES.equals(customerRiskManagementDO.getIsFullDeposit())) {
                    depositAmount = BigDecimalUtil.mul(skuPrice, new BigDecimal(orderProductDO.getProductCount()));
                    totalDepositAmount = BigDecimalUtil.add(totalDepositAmount, depositAmount);
                } else {
                    rentDepositAmount = BigDecimalUtil.mul(BigDecimalUtil.mul(orderProductDO.getProductUnitAmount(), new BigDecimal(orderProductDO.getProductCount()), 2), new BigDecimal(orderProductDO.getDepositCycle()));
                    totalCreditDepositAmount = BigDecimalUtil.add(totalCreditDepositAmount, creditDepositAmount);
                    creditDepositAmount = BigDecimalUtil.mul(skuPrice, new BigDecimal(orderProductDO.getProductCount()));
                    totalCreditDepositAmount = BigDecimalUtil.add(totalCreditDepositAmount, creditDepositAmount);
                }
                orderProductDO.setRentDepositAmount(rentDepositAmount);
                orderProductDO.setDepositAmount(depositAmount);
                orderProductDO.setCreditDepositAmount(creditDepositAmount);
                orderProductDO.setProductSkuName(skuName);
                //更改计算方式
                BigDecimal productAmount = amountSupport.calculateRentAmount(orderDO.getRentStartTime(),orderDO.getExpectReturnTime(),orderProductDO.getProductUnitAmount(),orderProductDO.getProductCount());
                orderProductDO.setProductAmount(productAmount);
                orderProductDO.setProductSkuSnapshot(FastJsonUtil.toJSONString(product));
                BigDecimal thisOrderProductInsuranceAmount = BigDecimalUtil.mul(BigDecimalUtil.mul(orderProductDO.getInsuranceAmount(), new BigDecimal(orderProductDO.getRentTimeLength()), 2), new BigDecimal(orderProductDO.getProductCount()));
                productCount += orderProductDO.getProductCount();
                productAmountTotal = BigDecimalUtil.add(productAmountTotal, orderProductDO.getProductAmount());
                totalInsuranceAmount = BigDecimalUtil.add(totalInsuranceAmount, thisOrderProductInsuranceAmount);
            }
            orderDO.setTotalRentDepositAmount(BigDecimalUtil.add(orderDO.getTotalRentDepositAmount(), totalRentDepositAmount));
            orderDO.setTotalCreditDepositAmount(BigDecimalUtil.add(orderDO.getTotalCreditDepositAmount(), totalCreditDepositAmount));
            orderDO.setTotalInsuranceAmount(BigDecimalUtil.add(orderDO.getTotalInsuranceAmount(), totalInsuranceAmount));
            orderDO.setTotalDepositAmount(BigDecimalUtil.add(orderDO.getTotalDepositAmount(), totalDepositAmount));
            orderDO.setTotalMustDepositAmount(BigDecimalUtil.add(orderDO.getTotalMustDepositAmount(), totalDepositAmount));
            orderDO.setTotalProductDepositAmount(totalDepositAmount);
            orderDO.setTotalProductCreditDepositAmount(totalCreditDepositAmount);
            orderDO.setTotalProductRentDepositAmount(totalRentDepositAmount);
            orderDO.setTotalProductCount(productCount);
            orderDO.setTotalProductAmount(productAmountTotal);
        } else {
            orderDO.setTotalProductDepositAmount(BigDecimal.ZERO);
            orderDO.setTotalProductCreditDepositAmount(BigDecimal.ZERO);
            orderDO.setTotalProductRentDepositAmount(BigDecimal.ZERO);
            orderDO.setTotalProductCount(CommonConstant.COMMON_ZERO);
            orderDO.setTotalProductAmount(BigDecimal.ZERO);
        }
    }

    public void calculateOrderMaterialInfo(List<OrderMaterialDO> orderMaterialDOList, OrderDO orderDO) {
        CustomerRiskManagementDO customerRiskManagementDO = customerSupport.getCustomerRiskManagementDO(orderDO.getBuyerCustomerId());
        if (orderMaterialDOList != null && !orderMaterialDOList.isEmpty()) {
            int materialCount = 0;
            // 商品租赁总额
            BigDecimal materialAmountTotal = BigDecimal.ZERO;
            // 保险总额
            BigDecimal totalInsuranceAmount = BigDecimal.ZERO;
            // 设备该交押金总额
            BigDecimal totalDepositAmount = BigDecimal.ZERO;
            // 设备信用押金总额
            BigDecimal totalCreditDepositAmount = BigDecimal.ZERO;
            // 租赁押金总额
            BigDecimal totalRentDepositAmount = BigDecimal.ZERO;
            BigDecimal materialPrice = BigDecimal.ZERO;
            BigDecimal depositAmount = BigDecimal.ZERO;
            BigDecimal creditDepositAmount = BigDecimal.ZERO;
            BigDecimal rentDepositAmount = BigDecimal.ZERO;
            String materialName = "";
            Material material = new Material();
            for (OrderMaterialDO orderMaterialDO : orderMaterialDOList) {
                if (!CommonConstant.COMMON_CONSTANT_YES.equals(orderDO.getIsK3Order())) {
                    ServiceResult<String, Material> materialServiceResult = materialService.queryMaterialById(orderMaterialDO.getMaterialId());
                    if (!ErrorCode.SUCCESS.equals(materialServiceResult.getErrorCode())) {
                        throw new BusinessException(materialServiceResult.getErrorCode());
                    }
                    material = materialServiceResult.getResult();
                    if (material == null) {
                        throw new BusinessException(ErrorCode.MATERIAL_NOT_EXISTS);
                    }
                    orderMaterialDO.setMaterialName(material.getMaterialName());
                    materialName = material.getMaterialName();
                    materialPrice = CommonConstant.COMMON_CONSTANT_YES.equals(orderMaterialDO.getIsNewMaterial()) ? material.getNewMaterialPrice() : material.getMaterialPrice();
                }

                // 小于等于90天的,不走风控，大于90天的，走风控授信
                if (OrderRentType.RENT_TYPE_DAY.equals(orderMaterialDO.getRentType()) && orderMaterialDO.getRentTimeLength() <= CommonConstant.ORDER_NEED_VERIFY_DAYS) {
                    if (orderMaterialDO.getMaterialCount() > 0) {
                        BigDecimal remainder = orderMaterialDO.getDepositAmount().divideAndRemainder(new BigDecimal(orderMaterialDO.getMaterialCount()))[1];
                        if (BigDecimalUtil.compare(remainder, BigDecimal.ZERO) != 0) {
                            throw new BusinessException(ErrorCode.ORDER_MATERIAL_DEPOSIT_ERROR);
                        }
                    }

                    depositAmount = orderMaterialDO.getDepositAmount();
                    totalDepositAmount = BigDecimalUtil.add(totalDepositAmount, depositAmount);
                } else if (customerRiskManagementDO != null && CommonConstant.COMMON_CONSTANT_YES.equals(customerRiskManagementDO.getIsFullDeposit())) {
                    depositAmount = BigDecimalUtil.mul(materialPrice, new BigDecimal(orderMaterialDO.getMaterialCount()));
                    totalDepositAmount = BigDecimalUtil.add(totalDepositAmount, depositAmount);
                } else {
                    if (CommonConstant.COMMON_CONSTANT_YES.equals(orderMaterialDO.getIsNewMaterial())) {
                        rentDepositAmount = BigDecimalUtil.mul(BigDecimalUtil.mul(orderMaterialDO.getMaterialUnitAmount(), new BigDecimal(orderMaterialDO.getMaterialCount()), 2), new BigDecimal(orderMaterialDO.getDepositCycle()));
                        totalRentDepositAmount = BigDecimalUtil.add(totalRentDepositAmount, rentDepositAmount);
                    } else {
                        rentDepositAmount = BigDecimalUtil.mul(BigDecimalUtil.mul(orderMaterialDO.getMaterialUnitAmount(), new BigDecimal(orderMaterialDO.getMaterialCount()), 2), new BigDecimal(orderMaterialDO.getDepositCycle()));
                        totalRentDepositAmount = BigDecimalUtil.add(totalRentDepositAmount, rentDepositAmount);
                    }

                    MaterialTypeDO materialTypeDO = materialTypeMapper.findById(material.getMaterialType());
                    if (materialTypeDO != null && CommonConstant.COMMON_CONSTANT_YES.equals(materialTypeDO.getIsMainMaterial())) {
                        creditDepositAmount = BigDecimalUtil.mul(materialPrice, new BigDecimal(orderMaterialDO.getMaterialCount()));
                        totalCreditDepositAmount = BigDecimalUtil.add(totalCreditDepositAmount, creditDepositAmount);
                    }
                }

                orderMaterialDO.setRentDepositAmount(rentDepositAmount);
                orderMaterialDO.setDepositAmount(depositAmount);
                orderMaterialDO.setCreditDepositAmount(creditDepositAmount);
                orderMaterialDO.setMaterialName(StringUtil.isBlank(materialName) ? orderMaterialDO.getMaterialName() : materialName);
                //TODO 修改价格
                BigDecimal materialAmount = amountSupport.calculateRentAmount(orderDO.getRentStartTime(),orderDO.getExpectReturnTime(),orderMaterialDO.getMaterialUnitAmount(),orderMaterialDO.getMaterialCount());
                orderMaterialDO.setMaterialAmount(materialAmount);
                orderMaterialDO.setMaterialSnapshot(FastJsonUtil.toJSONString(material));
                BigDecimal thisOrderMaterialInsuranceAmount = BigDecimalUtil.mul(BigDecimalUtil.mul(orderMaterialDO.getInsuranceAmount(), new BigDecimal(orderMaterialDO.getRentTimeLength()), 2), new BigDecimal(orderMaterialDO.getMaterialCount()));
                materialCount += orderMaterialDO.getMaterialCount();
                materialAmountTotal = BigDecimalUtil.add(materialAmountTotal, orderMaterialDO.getMaterialAmount());
                totalInsuranceAmount = BigDecimalUtil.add(totalInsuranceAmount, thisOrderMaterialInsuranceAmount);
            }
            orderDO.setTotalRentDepositAmount(BigDecimalUtil.add(orderDO.getTotalRentDepositAmount(), totalRentDepositAmount));
            orderDO.setTotalCreditDepositAmount(BigDecimalUtil.add(orderDO.getTotalCreditDepositAmount(), totalCreditDepositAmount));
            orderDO.setTotalInsuranceAmount(BigDecimalUtil.add(orderDO.getTotalInsuranceAmount(), totalInsuranceAmount));
            orderDO.setTotalDepositAmount(BigDecimalUtil.add(orderDO.getTotalDepositAmount(), totalDepositAmount));
            orderDO.setTotalMustDepositAmount(BigDecimalUtil.add(orderDO.getTotalMustDepositAmount(), totalDepositAmount));
            orderDO.setTotalMaterialDepositAmount(totalDepositAmount);
            orderDO.setTotalMaterialCreditDepositAmount(totalCreditDepositAmount);
            orderDO.setTotalMaterialRentDepositAmount(totalRentDepositAmount);
            orderDO.setTotalMaterialCount(materialCount);
            orderDO.setTotalMaterialAmount(materialAmountTotal);
        } else {
            orderDO.setTotalMaterialDepositAmount(BigDecimal.ZERO);
            orderDO.setTotalMaterialCreditDepositAmount(BigDecimal.ZERO);
            orderDO.setTotalMaterialRentDepositAmount(BigDecimal.ZERO);
            orderDO.setTotalMaterialCount(CommonConstant.COMMON_ZERO);
            orderDO.setTotalMaterialAmount(BigDecimal.ZERO);
        }
    }

    @Autowired
    private AmountSupport amountSupport;

    @Autowired
    private CustomerSupport customerSupport;

    @Autowired
    private MaterialTypeMapper materialTypeMapper;

    @Autowired
    private StatementService statementService;

    @Autowired
    private MaterialService materialService;

    @Autowired
    private ProductService productService;


    @Autowired
    private ReplaceOrderMapper replaceOrderMapper;

    @Autowired
    private K3Service k3Service;

    @Autowired
    private StatementOrderSupport statementOrderSupport;

    @Autowired
    private OrderConsignInfoMapper orderConsignInfoMapper;

    @Autowired
    private K3ReturnOrderMapper k3ReturnOrderMapper;

    @Autowired
    private K3ReturnOrderDetailMapper k3ReturnOrderDetailMapper;


    @Autowired
    private OrderFlowMapper orderFlowMapper;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private WorkflowService workflowService;

    @Autowired
    private GenerateNoSupport generateNoSupport;

    @Autowired
    private UserSupport userSupport;

    @Autowired
    private OrderSupport orderSupport;

    @Autowired
    private ExchangeOrderMapper exchangeOrderMapper;

    @Autowired
    private ExchangeOrderProductMapper exchangeOrderProductMapper;

    @Autowired
    private ExchangeOrderMaterialMapper exchangeOrderMaterialMapper;

    @Autowired
    private OrderTimeAxisSupport orderTimeAxisSupport;

    @Autowired
    private CustomerConsignInfoMapper customerConsignInfoMapper;
}
