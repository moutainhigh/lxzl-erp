package com.lxzl.erp.core.service.order.impl;

import com.lxzl.erp.common.constant.*;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.order.ExchangeOrderCommitParam;
import com.lxzl.erp.common.domain.order.pojo.ExchangeOrder;
import com.lxzl.erp.common.domain.order.pojo.ExchangeOrderMaterial;
import com.lxzl.erp.common.domain.order.pojo.ExchangeOrderProduct;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.common.util.ConverterUtil;
import com.lxzl.erp.core.service.basic.impl.support.GenerateNoSupport;
import com.lxzl.erp.core.service.order.ExchangeOrderService;
import com.lxzl.erp.core.service.order.impl.support.OrderSupport;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.core.service.workflow.WorkflowService;
import com.lxzl.erp.dataaccess.dao.mysql.order.ExchangeOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.ExchangeOrderMaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.ExchangeOrderProductMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.OrderMapper;
import com.lxzl.erp.dataaccess.domain.order.ExchangeOrderDO;
import com.lxzl.erp.dataaccess.domain.order.ExchangeOrderMaterialDO;
import com.lxzl.erp.dataaccess.domain.order.ExchangeOrderProductDO;
import com.lxzl.erp.dataaccess.domain.order.OrderDO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.*;

@Service("exchangeOrderService")
public class ExchangeOrderServiceImpl implements ExchangeOrderService {
    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, String> createExchangeOrder(ExchangeOrder exchangeOrder) {
        ServiceResult<String, String> result = new ServiceResult<>();
        User loginUser = userSupport.getCurrentUser();
        Date now = new Date();
        OrderDO orderDO = orderMapper.findByOrderNo(exchangeOrder.getOrderNo());
        if (null == orderDO) {
            result.setErrorCode(ErrorCode.ORDER_NOT_EXISTS);
            return result;
        }
        //TODO 开始时间都是一个月的开始时间
        //只允许有一个有效的变更单
        //判断是否有操作这个订单的权限
        //只有租赁中的订单才可以进行
        //保存变更信息
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
        if (!CollectionUtil.isEmpty(exchangeOrder.getExchangeOrderProductList())) {
            List<ExchangeOrderProductDO> exchangeOrderProductDOList = new ArrayList<>();
            //保存商品信息
            for (ExchangeOrderProduct exchangeOrderProduct : exchangeOrder.getExchangeOrderProductList()) {
                ExchangeOrderProductDO exchangeOrderProductDO = new ExchangeOrderProductDO();
                BeanUtils.copyProperties(exchangeOrderProduct, exchangeOrderProductDO);
                exchangeOrderProductDO.setExchangeOrderId(exchangeOrderDO.getId());
                exchangeOrderProductDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                exchangeOrderProductDOList.add(exchangeOrderProductDO);
            }
            exchangeOrderProductMapper.saveList(exchangeOrderProductDOList);
        }
        if (!CollectionUtil.isEmpty(exchangeOrder.getExchangeOrderMaterial())) {
            List<ExchangeOrderMaterialDO> exchangeOrderMaterialDOList = new ArrayList<>();
            //保存配件信息
            for (ExchangeOrderMaterial exchangeOrderMaterial : exchangeOrder.getExchangeOrderMaterial()) {
                ExchangeOrderMaterialDO exchangeOrderMaterialDO = new ExchangeOrderMaterialDO();
                BeanUtils.copyProperties(exchangeOrderMaterial, exchangeOrderMaterialDO);
                exchangeOrderMaterialDO.setExchangeOrderId(exchangeOrderDO.getId());
                exchangeOrderMaterialDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                exchangeOrderMaterialDOList.add(exchangeOrderMaterialDO);
            }
            exchangeOrderMaterialMapper.saveList(exchangeOrderMaterialDOList);
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
        String orderNo = exchangeOrderCommitParam.getOrderNo();
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
        String verifyMatters = "例行审核";
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
        ExchangeOrder exchangeOrder= ConverterUtil.convert(exchangeOrderDO, ExchangeOrder.class);
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(exchangeOrder);
        return result;
    }

    @Override
    public ServiceResult<String, Page<ExchangeOrder>> queryByOrderNo(String orderNo) {
        ServiceResult<String,Page<ExchangeOrder>> result = new ServiceResult<>();
        Map<String, Object> maps = new HashMap<>();
        maps.put("start", 0);
        maps.put("pageSize", 100);
        maps.put("orderQueryParam", orderNo);
        Integer count = exchangeOrderMapper.listCount(maps);
        List<ExchangeOrderDO> exchangeOrderDOList =exchangeOrderMapper.listPage(maps);
        List<ExchangeOrder> exchangeOrderList=ConverterUtil.convertList(exchangeOrderDOList,ExchangeOrder.class);
        Page<ExchangeOrder> page = new Page<>(exchangeOrderList, count, 0, 100);
        result.setResult(page);
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
    public String receiveVerifyResult(boolean verifyResult, String businessNo) {
        Date currentTime = new Date();
        User loginUser = userSupport.getCurrentUser();
        OrderDO orderDO = orderMapper.findByOrderNo(businessNo);
        if (orderDO == null || !OrderStatus.ORDER_STATUS_VERIFYING.equals(orderDO.getOrderStatus())) {
            return ErrorCode.BUSINESS_EXCEPTION;
        }
        ExchangeOrderDO exchangeOrderDO = exchangeOrderMapper.findByExchangeOrderNo(businessNo);
        if (CommonConstant.DATA_STATUS_ENABLE.equals(exchangeOrderDO.getDataStatus()) && ExchangeOrderStatus.ORDER_STATUS_WAIT_COMMIT.equals(exchangeOrderDO.getStatus())) {
            return ErrorCode.EXCHANGE_ORDER_STATUS_ERROR;
        }
        //审核通过
        if (verifyResult) {
            exchangeOrderDO.setDataStatus(ExchangeOrderStatus.ORDER_STATUS_CONFIRM);
        } else {
            exchangeOrderDO.setDataStatus(ExchangeOrderStatus.ORDER_STATUS_WAIT_COMMIT);
        }
        exchangeOrderDO.setUpdateTime(currentTime);
        exchangeOrderDO.setUpdateUser(loginUser.getUserId().toString());
        exchangeOrderMapper.update(exchangeOrderDO);
        return ErrorCode.SUCCESS;
    }

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

}
