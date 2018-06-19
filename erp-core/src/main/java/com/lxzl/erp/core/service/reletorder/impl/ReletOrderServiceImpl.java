package com.lxzl.erp.core.service.reletorder.impl;

import ch.qos.logback.core.joran.conditional.ElseAction;
import com.lxzl.erp.common.constant.*;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.material.pojo.Material;
import com.lxzl.erp.common.domain.messagethirdchannel.pojo.MessageThirdChannel;
import com.lxzl.erp.common.domain.order.OrderQueryParam;
import com.lxzl.erp.common.domain.order.pojo.Order;
import com.lxzl.erp.common.domain.order.pojo.OrderMaterial;
import com.lxzl.erp.common.domain.order.pojo.OrderProduct;
import com.lxzl.erp.common.domain.product.pojo.Product;
import com.lxzl.erp.common.domain.product.pojo.ProductSku;
import com.lxzl.erp.common.domain.reletorder.ReletOrderCommitParam;
import com.lxzl.erp.common.domain.reletorder.ReletOrderCreateResult;
import com.lxzl.erp.common.domain.reletorder.ReletOrderQueryParam;
import com.lxzl.erp.common.domain.reletorder.pojo.ReletOrder;
import com.lxzl.erp.common.domain.reletorder.pojo.ReletOrderMaterial;
import com.lxzl.erp.common.domain.reletorder.pojo.ReletOrderProduct;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.common.util.*;
import com.lxzl.erp.core.service.basic.impl.support.GenerateNoSupport;
import com.lxzl.erp.core.service.k3.K3Service;
import com.lxzl.erp.core.service.material.MaterialService;
import com.lxzl.erp.core.service.messagethirdchannel.MessageThirdChannelService;
import com.lxzl.erp.core.service.order.OrderService;
import com.lxzl.erp.core.service.order.impl.support.OrderTimeAxisSupport;
import com.lxzl.erp.core.service.permission.PermissionSupport;
import com.lxzl.erp.core.service.product.ProductService;
import com.lxzl.erp.core.service.reletorder.ReletOrderService;
import com.lxzl.erp.core.service.statement.StatementService;
import com.lxzl.erp.core.service.statement.impl.support.StatementOrderSupport;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.core.service.workflow.WorkflowService;
import com.lxzl.erp.dataaccess.dao.mysql.company.SubCompanyMapper;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerMapper;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerRiskManagementMapper;
import com.lxzl.erp.dataaccess.dao.mysql.k3.K3OrderStatementConfigMapper;
import com.lxzl.erp.dataaccess.dao.mysql.material.MaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.material.MaterialTypeMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.OrderConsignInfoMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.OrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductSkuMapper;
import com.lxzl.erp.dataaccess.dao.mysql.reletorder.ReletOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.reletorder.ReletOrderMaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.reletorder.ReletOrderProductMapper;
import com.lxzl.erp.dataaccess.domain.company.SubCompanyDO;
import com.lxzl.erp.dataaccess.domain.customer.CustomerConsignInfoDO;
import com.lxzl.erp.dataaccess.domain.customer.CustomerDO;
import com.lxzl.erp.dataaccess.domain.customer.CustomerRiskManagementDO;
import com.lxzl.erp.dataaccess.domain.k3.K3OrderStatementConfigDO;
import com.lxzl.erp.dataaccess.domain.order.OrderConsignInfoDO;
import com.lxzl.erp.dataaccess.domain.order.OrderDO;
import com.lxzl.erp.dataaccess.domain.order.OrderMaterialDO;
import com.lxzl.erp.dataaccess.domain.order.OrderProductDO;
import com.lxzl.erp.dataaccess.domain.reletorder.ReletOrderDO;
import com.lxzl.erp.dataaccess.domain.reletorder.ReletOrderMaterialDO;
import com.lxzl.erp.dataaccess.domain.reletorder.ReletOrderProductDO;
import com.lxzl.erp.dataaccess.domain.statement.StatementOrderDO;
import com.lxzl.se.common.exception.BusinessException;
import com.lxzl.se.common.util.StringUtil;
import com.lxzl.se.common.util.date.DateUtil;
import com.lxzl.se.dataaccess.mongo.config.PageQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


@Service("reletOrderService")
public class ReletOrderServiceImpl implements ReletOrderService {

    private static Logger logger = LoggerFactory.getLogger(ReletOrderServiceImpl.class);

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, ReletOrderCreateResult> createReletOrder(Order order) {
        ServiceResult<String, ReletOrderCreateResult> result = new ServiceResult<>();
        ReletOrderCreateResult reletOrderCreateResult = new ReletOrderCreateResult();
        User loginUser = userSupport.getCurrentUser();
        Date currentTime = new Date();
        Date returnTime;

        if (order == null) {
            result.setErrorCode(ErrorCode.PARAM_IS_NOT_NULL);
            return result;
        }
        if (StringUtil.isEmpty(order.getOrderNo())) {
            result.setErrorCode(ErrorCode.RELET_ORDER_NO_NOT_NULL);
            return result;
        }
        //查询订单信息    订单信息由前端传给后台  只包括租赁时长和单价
        ServiceResult<String, Order> orderServiceResult = orderService.queryOrderByNo(order.getOrderNo());
        if (!ErrorCode.SUCCESS.equals(orderServiceResult.getErrorCode())) {
            result.setErrorCode(orderServiceResult.getErrorCode());
            return result;
        }

        //查询是否有续租单信息
        ReletOrderDO recentlyReletOrderInDB = reletOrderMapper.findRecentlyReletOrderByOrderNo(order.getOrderNo());
        if (null != recentlyReletOrderInDB) {

            if (!ReletOrderStatus.canReletOrderByCurrentStatus(recentlyReletOrderInDB.getReletOrderStatus())){
                result.setErrorCode(ErrorCode.RELET_ORDER_EXISTS_RELET_REQUEST);
                reletOrderCreateResult.setReletOrderNo(recentlyReletOrderInDB.getReletOrderNo());
                result.setResult(reletOrderCreateResult);
                return result;
            }

            if (currentTime.compareTo(recentlyReletOrderInDB.getRentStartTime()) < 0){  //如果当前续租还没开始  不允许再次续租
                result.setErrorCode(ErrorCode.RELET_ORDER_NOT_ALLOWED_MORE_THAN_ONE_SUCCESS_RECORD);
                reletOrderCreateResult.setReletOrderNo(recentlyReletOrderInDB.getReletOrderNo());
                result.setResult(reletOrderCreateResult);
                return result;
            }
        }

        //到期时间=订单的归还时间
        returnTime = orderServiceResult.getResult().getExpectReturnTime();

        String validReletTimeRangeCode = validReletTimeRange(returnTime, currentTime, orderServiceResult.getResult().getRentType());
        if (!ErrorCode.SUCCESS.equals(validReletTimeRangeCode)) {
            result.setErrorCode(validReletTimeRangeCode);
            return result;
        }

        //合法性
        String verifyCreateOrderCode = verifyOperateOrder(orderServiceResult.getResult());
        if (!ErrorCode.SUCCESS.equals(verifyCreateOrderCode)) {
            result.setErrorCode(verifyCreateOrderCode);
            return result;
        }

        ReletOrder reletOrder = new ReletOrder(orderServiceResult.getResult());
        ReletOrderDO reletOrderDO = ConverterUtil.convert(reletOrder, ReletOrderDO.class);

        //支付方式为首付百分比的订单 修改续租单支付方式为先用后付
        String updateReletOrderPayModeCode = updateReletOrderPayModeOfBeforePercent(reletOrderDO);
        if (!ErrorCode.SUCCESS.equals(updateReletOrderPayModeCode)) {
            result.setErrorCode(updateReletOrderPayModeCode);
            return result;
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(returnTime);
        cal.add(Calendar.DAY_OF_MONTH, 1);
        reletOrderDO.setRentStartTime(cal.getTime());//更新起租时间= 到期时间后一天

        //续租时允许客户修改时长 和 单价
        ServiceResult<String, Boolean> syncTargetOrderInfoResult = syncOrderToReletOrder(order, reletOrderDO);
        if (!ErrorCode.SUCCESS.equals(syncTargetOrderInfoResult.getErrorCode())) {
            result.setErrorCode(syncTargetOrderInfoResult.getErrorCode());
            return result;
        }
        Boolean isNeedVerify = syncTargetOrderInfoResult.getResult();

        //计算续租费用
        calculateReletOrderProductInfo(reletOrderDO.getReletOrderProductDOList(), reletOrderDO);
        calculateReletOrderMaterialInfo(reletOrderDO.getReletOrderMaterialDOList(), reletOrderDO);

//        SubCompanyDO subCompanyDO = subCompanyMapper.findById(reletOrder.getDeliverySubCompanyId());
//        if (reletOrder.getDeliverySubCompanyId() == null || subCompanyDO == null) {
//            result.setErrorCode(ErrorCode.SUB_COMPANY_NOT_EXISTS);
//            return result;
//        }

        SubCompanyDO orderSubCompanyDO = subCompanyMapper.findById(reletOrderDO.getOrderSubCompanyId());
        reletOrderDO.setTotalOrderAmount(BigDecimalUtil.sub(BigDecimalUtil.add(reletOrderDO.getTotalProductAmount(), reletOrderDO.getTotalMaterialAmount()), reletOrderDO.getTotalDiscountAmount()));
        reletOrderDO.setReletOrderNo(generateNoSupport.generateReletOrderNo(currentTime, orderSubCompanyDO != null ? orderSubCompanyDO.getSubCompanyCode() : null));

        //获取
        reletOrderDO.setStatementDate(reletOrder.getStatementDate());
        reletOrderDO.setReletOrderStatus(ReletOrderStatus.RELET_ORDER_STATUS_WAIT_COMMIT);
        reletOrderDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        reletOrderDO.setCreateUser(loginUser.getUserId().toString());
        reletOrderDO.setUpdateUser(loginUser.getUserId().toString());
        reletOrderDO.setCreateTime(currentTime);
        reletOrderDO.setUpdateTime(currentTime);

        Date expectReturnTime = generateExpectReturnTime(reletOrderDO);
        reletOrderDO.setExpectReturnTime(expectReturnTime);
        //保存续租单 商品项 配件项
        reletOrderMapper.save(reletOrderDO);
        saveReletOrderProductInfo(reletOrderDO, loginUser, currentTime);
        saveReletOrderMaterialInfo(reletOrderDO, loginUser, currentTime);

        //TODO 续租时间轴

        //获取审核人列表
        reletOrderCreateResult.setReletOrderNo(reletOrderDO.getReletOrderNo());
        Integer isNeedVerifyCode = isNeedVerify ? 1 : 0;
        reletOrderCreateResult.setIsNeedVerify(isNeedVerifyCode);
        if (isNeedVerify){
            ServiceResult<String, List<User>> getVerifyUsersResult = workflowService.getNextVerifyUsers(WorkflowType.WORKFLOW_TYPE_RELET_ORDER_INFO, reletOrderDO.getReletOrderNo());
            if (!ErrorCode.SUCCESS.equals(getVerifyUsersResult.getErrorCode())) {
                result.setErrorCode(getVerifyUsersResult.getErrorCode());
                return result;
            }
            reletOrderCreateResult.setVerifyUsers(getVerifyUsersResult.getResult());
        }

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(reletOrderCreateResult);
        return result;
    }



    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, ReletOrderCreateResult> updateReletOrder(ReletOrder reletOrder) {
        ServiceResult<String, ReletOrderCreateResult> result = new ServiceResult<>();
        ReletOrderCreateResult reletOrderCreateResult = new ReletOrderCreateResult();
        User loginUser = userSupport.getCurrentUser();
        Date currentTime = new Date();

        if (reletOrder == null) {
            result.setErrorCode(ErrorCode.PARAM_IS_NOT_NULL);
            return result;
        }
        if (StringUtil.isEmpty(reletOrder.getReletOrderNo())) {
            result.setErrorCode(ErrorCode.RELET_ORDER_NO_NOT_NULL);
            return result;
        }

        ReletOrderDO reletOrderDO = reletOrderMapper.findByReletOrderNo(reletOrder.getReletOrderNo());
        if (reletOrderDO == null || !ReletOrderStatus.RELET_ORDER_STATUS_WAIT_COMMIT.equals(reletOrderDO.getReletOrderStatus())) {
            result.setErrorCode(ErrorCode.RELET_ORDER_ONLY_WAIT_COMMIT_STATUS_ALLOWED_UPDATE);
            return result;
        }

        //续租时允许客户修改时长 和 单价
        ServiceResult<String, Boolean> syncTargetOrderInfoResult = syncReletOrderToReletOrderDO(reletOrder, reletOrderDO);
        if (!ErrorCode.SUCCESS.equals(syncTargetOrderInfoResult.getErrorCode())) {
            result.setErrorCode(syncTargetOrderInfoResult.getErrorCode());
            return result;
        }
        Boolean isNeedVerify = syncTargetOrderInfoResult.getResult(); //是否审核

        //计算续租费用
        calculateReletOrderProductInfo(reletOrderDO.getReletOrderProductDOList(), reletOrderDO);
        calculateReletOrderMaterialInfo(reletOrderDO.getReletOrderMaterialDOList(), reletOrderDO);

        reletOrderDO.setTotalOrderAmount(BigDecimalUtil.sub(BigDecimalUtil.add(reletOrderDO.getTotalProductAmount(), reletOrderDO.getTotalMaterialAmount()), reletOrderDO.getTotalDiscountAmount()));

        reletOrderDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        reletOrderDO.setUpdateUser(loginUser.getUserId().toString());
        reletOrderDO.setUpdateTime(currentTime);

        Date expectReturnTime = generateExpectReturnTime(reletOrderDO);
        reletOrderDO.setExpectReturnTime(expectReturnTime);

        //保存续租单 商品项 配件项
        reletOrderMapper.update(reletOrderDO);
        saveReletOrderProductInfo(reletOrderDO, loginUser, currentTime);
        saveReletOrderMaterialInfo(reletOrderDO, loginUser, currentTime);

        //获取审核人列表
        reletOrderCreateResult.setReletOrderNo(reletOrderDO.getReletOrderNo());
        Integer isNeedVerifyCode = isNeedVerify ? 1 : 0;
        reletOrderCreateResult.setIsNeedVerify(isNeedVerifyCode);
        if (isNeedVerify){
            ServiceResult<String, List<User>> getVerifyUsersResult = workflowService.getNextVerifyUsers(WorkflowType.WORKFLOW_TYPE_RELET_ORDER_INFO, reletOrderDO.getReletOrderNo());
            if (!ErrorCode.SUCCESS.equals(getVerifyUsersResult.getErrorCode())) {
                result.setErrorCode(getVerifyUsersResult.getErrorCode());
                return result;
            }
            reletOrderCreateResult.setVerifyUsers(getVerifyUsersResult.getResult());
        }

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(reletOrderCreateResult);
        return result;
    }



    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, String> commitReletOrder(ReletOrderCommitParam reletOrderCommitParam) {
        ServiceResult<String, String> result = new ServiceResult<>();
        Date currentTime = new Date();
        User loginUser = userSupport.getCurrentUser();
        String reletOrderNo = reletOrderCommitParam.getReletOrderNo();
        Integer verifyUser = reletOrderCommitParam.getVerifyUser();
        String commitRemark = reletOrderCommitParam.getCommitRemark();
        ReletOrderDO reletOrderDO = reletOrderMapper.findByReletOrderNo(reletOrderNo);
        if (reletOrderDO == null) {
            result.setErrorCode(ErrorCode.RELET_ORDER_NOT_EXISTS);
            return result;
        }
        if (!ReletOrderStatus.RELET_ORDER_STATUS_WAIT_COMMIT.equals(reletOrderDO.getReletOrderStatus())) {
            result.setErrorCode(ErrorCode.RELET_ORDER_ONLY_WAIT_COMMIT_STATUS_ALLOWED_COMMIT);
            return result;
        }

        if (CollectionUtil.isEmpty(reletOrderDO.getReletOrderProductDOList())
                && CollectionUtil.isEmpty(reletOrderDO.getReletOrderMaterialDOList())) {
            result.setErrorCode(ErrorCode.RELET_ORDER_LIST_NOT_NULL);
            return result;
        }
        //只有创建续租单本人可以提交
        if (!reletOrderDO.getCreateUser().equals(loginUser.getUserId().toString())) {
            result.setErrorCode(ErrorCode.COMMIT_ONLY_SELF);
            return result;
        }
        //获取原订单信息
        OrderDO orderDO = orderMapper.findByOrderNo(reletOrderDO.getOrderNo());
        //合法性
        String verifyCode = verifyReletOrderOperate(orderDO, reletOrderDO);
        if (!ErrorCode.SUCCESS.equals(verifyCode)) {
            result.setErrorCode(verifyCode);
            return result;
        }

        ServiceResult<String, Boolean> isNeedVerfyResult = checkIsNeedVerify(orderDO, reletOrderDO);
        if (!ErrorCode.SUCCESS.equals(isNeedVerfyResult.getErrorCode())) {
            result.setErrorCode(isNeedVerfyResult.getErrorCode());
            return result;
        }
        if (isNeedVerfyResult.getResult()) {
            String orderRemark = "续租";
            ServiceResult<String, String> verifyMattersResult = getVerifyMatters(reletOrderDO);
            if (!ErrorCode.SUCCESS.equals(verifyMattersResult.getErrorCode())) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                result.setErrorCode(verifyMattersResult.getErrorCode());
                return result;
            }
            String verifyMatters = verifyMattersResult.getResult();

            ServiceResult<String, String> workflowCommitResult = workflowService.commitWorkFlow(WorkflowType.WORKFLOW_TYPE_RELET_ORDER_INFO, reletOrderDO.getReletOrderNo(), verifyUser, verifyMatters, commitRemark, reletOrderCommitParam.getImgIdList(), orderRemark);
            if (!ErrorCode.SUCCESS.equals(workflowCommitResult.getErrorCode())) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                result.setErrorCode(workflowCommitResult.getErrorCode());
                return result;
            }

            reletOrderDO.setReletOrderStatus(ReletOrderStatus.RELET_ORDER_STATUS_VERIFYING);
        } else {  //不需要审核时
            reletOrderDO.setReletOrderStatus(ReletOrderStatus.RELET_ORDER_STATUS_RELETTING);

            orderDO.setReletOrderId(reletOrderDO.getId());
            orderDO.setExpectReturnTime(reletOrderDO.getExpectReturnTime());
//            orderDO.setOrderStatus(OrderStatus.ORDER_STATUS_RELET);
            orderDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
            orderDO.setUpdateUser(loginUser.getUserId().toString());
            orderDO.setUpdateTime(currentTime);
            //更新订单 续租状态
            orderMapper.update(orderDO);

            // 续租单生成结算单 ，关联订单Id
            ServiceResult<String, BigDecimal> createStatementOrderResult = statementService.createReletOrderStatement(reletOrderDO);
            if (!ErrorCode.SUCCESS.equals(createStatementOrderResult.getErrorCode())) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                result.setErrorCode(createStatementOrderResult.getErrorCode());
                return result;
            }

            //推送K3消息
            k3Service.sendReletOrderInfoToK3(reletOrderDO, orderDO);


        }

        reletOrderDO.setUpdateUser(loginUser.getUserId().toString());
        reletOrderDO.setUpdateTime(currentTime);
        reletOrderMapper.update(reletOrderDO);

        result.setResult(reletOrderNo);
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }


    /**
     * 审核注意事项
     *
     * @param reletOrderDO
     * @return
     */
    private ServiceResult<String, String> getVerifyMatters(ReletOrderDO reletOrderDO) {
        ServiceResult<String, String> result = new ServiceResult<>();
        String orderRentType = null;
        if (OrderRentType.RENT_TYPE_DAY.equals(reletOrderDO.getRentType())) {
            orderRentType = "租赁类型：天租";
        } else if (OrderRentType.RENT_TYPE_MONTH.equals(reletOrderDO.getRentType())) {
            orderRentType = "租赁类型：月租";
        }
        String strReletInfo = "续租单号：【" + reletOrderDO.getReletOrderNo() + "】，"+ orderRentType +"，续租时长："
                + reletOrderDO.getRentTimeLength() + "。";
        StringBuilder verifyMatters = new StringBuilder(strReletInfo);

        if (CollectionUtil.isNotEmpty(reletOrderDO.getReletOrderProductDOList())) {
            for (ReletOrderProductDO reletOrderProductDO : reletOrderDO.getReletOrderProductDOList()) {
                String verifyProduct = "商品名称：【" + reletOrderProductDO.getProductName() + "】，商品单价："
                        + AmountUtil.getCommaFormat(reletOrderProductDO.getProductUnitAmount()) + "。" ;
                verifyMatters.append(verifyProduct);
            }

        }

        if (CollectionUtil.isNotEmpty(reletOrderDO.getReletOrderMaterialDOList())) {
            for (ReletOrderMaterialDO reletOrderMaterialDO : reletOrderDO.getReletOrderMaterialDOList()) {
                String verifyMaterial = "配件名称：【" + reletOrderMaterialDO.getMaterialName() + "】，配件单价："
                        + AmountUtil.getCommaFormat(reletOrderMaterialDO.getMaterialUnitAmount()) + "。" ;
                verifyMatters.append(verifyMaterial);
            }

        }

        result.setResult(verifyMatters.toString());
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }


    @Override
    public ServiceResult<String, Page<ReletOrder>> queryAllReletOrder(ReletOrderQueryParam reletOrderQueryParam) {
        ServiceResult<String, Page<ReletOrder>> result = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(reletOrderQueryParam.getPageNo(), reletOrderQueryParam.getPageSize());

        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        maps.put("reletOrderQueryParam", reletOrderQueryParam);
        maps.put("permissionParam", permissionSupport.getPermissionParam(PermissionType.PERMISSION_TYPE_SUB_COMPANY_FOR_SERVICE, PermissionType.PERMISSION_TYPE_SUB_COMPANY_FOR_BUSINESS, PermissionType.PERMISSION_TYPE_USER));

        Integer totalCount = reletOrderMapper.findReletOrderCountByParams(maps);
        List<ReletOrderDO> reletOrderDOList = reletOrderMapper.findReletOrderByParams(maps);
        List<ReletOrder> reletOrderList = ConverterUtil.convertList(reletOrderDOList, ReletOrder.class);
        Page<ReletOrder> page = new Page<>(reletOrderList, totalCount, reletOrderQueryParam.getPageNo(), reletOrderQueryParam.getPageSize());
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(page);
        return result;
    }


    @Override
    public ServiceResult<String, ReletOrder> queryReletOrderByNo(String reletOrderNo) {
        ServiceResult<String, ReletOrder> result = new ServiceResult<>();

        if (null == reletOrderNo) {
            result.setErrorCode(ErrorCode.PARAM_IS_NOT_NULL);
            return result;
        }
        ReletOrderDO reletOrderDO = reletOrderMapper.findByReletOrderNo(reletOrderNo);
        ReletOrder reletOrder = ConverterUtil.convert(reletOrderDO, ReletOrder.class);
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(reletOrder);
        return result;
    }


    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public String receiveVerifyResult(boolean verifyResult, String businessNo) {
        try {
            Date currentTime = new Date();
            User loginUser = userSupport.getCurrentUser();
            ReletOrderDO reletOrderDO = reletOrderMapper.findByReletOrderNo(businessNo);
            if (reletOrderDO == null || !ReletOrderStatus.RELET_ORDER_STATUS_VERIFYING.equals(reletOrderDO.getReletOrderStatus())) {
                return ErrorCode.BUSINESS_EXCEPTION;
            }

            if (verifyResult) {

                // 只有审批通过的续租单才生成 结算单
                OrderDO orderDO = orderMapper.findByOrderNo(reletOrderDO.getOrderNo());
                if (orderDO == null){
                    return ErrorCode.ORDER_NOT_EXISTS;
                }

                //判断在租数 是否一致
                String verifyCode = verifyReletOrderOperate(orderDO, reletOrderDO);
                if (!ErrorCode.SUCCESS.equals(verifyCode)) {
                    return verifyCode;
                }

                orderDO.setReletOrderId(reletOrderDO.getId());
                orderDO.setExpectReturnTime(reletOrderDO.getExpectReturnTime());
//                orderDO.setOrderStatus(OrderStatus.ORDER_STATUS_RELET);
                orderDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                orderDO.setUpdateUser(loginUser.getUserId().toString());
                orderDO.setUpdateTime(currentTime);
                //更新订单 续租状态
                orderMapper.update(orderDO);

                // 续租单生成结算单 ，关联订单Id
                ServiceResult<String, BigDecimal> createStatementOrderResult = statementService.createReletOrderStatement(reletOrderDO);
                if (!ErrorCode.SUCCESS.equals(createStatementOrderResult.getErrorCode())) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return createStatementOrderResult.getErrorCode();
                }

                //推送K3消息
                k3Service.sendReletOrderInfoToK3(reletOrderDO, orderDO);

                reletOrderDO.setReletOrderStatus(ReletOrderStatus.RELET_ORDER_STATUS_RELETTING);

            } else {
                reletOrderDO.setReletOrderStatus(ReletOrderStatus.RELET_ORDER_STATUS_WAIT_COMMIT);
            }

            reletOrderDO.setUpdateTime(currentTime);
            reletOrderDO.setUpdateUser(loginUser.getUserId().toString());
            reletOrderMapper.update(reletOrderDO);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("审批订单续租通知失败： {} {}", businessNo, e.toString());
            return ErrorCode.BUSINESS_EXCEPTION;
        }
        return ErrorCode.SUCCESS;
    }


    @Override
    public ServiceResult<String, Boolean> handleReletSendMessage(Date currentTime) {

        ServiceResult<String, Boolean> result = new ServiceResult<>();
        result.setErrorCode(ErrorCode.SUCCESS);
        //短租
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentTime);
        cal.add(Calendar.DAY_OF_MONTH, 2);  //按天租 提前3天
        OrderQueryParam orderQueryParam = new OrderQueryParam();
        orderQueryParam.setRentType(OrderRentType.RENT_TYPE_DAY);
        orderQueryParam.setStartExpectReturnTime(DateUtil.getFirstOfDay(cal.getTime()));
        orderQueryParam.setEndExpectReturnTime(DateUtil.getEndOfDay(cal.getTime()));
        orderQueryParam.setPageSize(Integer.MAX_VALUE);
        ServiceResult<String, Object> shortRentMessage = ReletedOrderSendDingDingMessage(orderQueryParam);
        if (!ErrorCode.SUCCESS.equals(shortRentMessage.getErrorCode())) {
            result.setErrorCode(shortRentMessage.getErrorCode());
        }

        //长租
        Calendar calLong = Calendar.getInstance();
        calLong.setTime(currentTime);
        calLong.add(Calendar.DAY_OF_MONTH, 9);  //按月租  提前10天
        OrderQueryParam orderQueryParamLong = new OrderQueryParam();
        orderQueryParamLong.setRentType(OrderRentType.RENT_TYPE_MONTH);
        orderQueryParamLong.setStartExpectReturnTime(DateUtil.getFirstOfDay(calLong.getTime()));
        orderQueryParamLong.setEndExpectReturnTime(DateUtil.getEndOfDay(calLong.getTime()));
        orderQueryParamLong.setPageSize(Integer.MAX_VALUE);
        ServiceResult<String, Object> longRentMessage = ReletedOrderSendDingDingMessage(orderQueryParamLong);
        if (!ErrorCode.SUCCESS.equals(longRentMessage.getErrorCode())) {
            result.setErrorCode(longRentMessage.getErrorCode());
        }

        return result;
    }

    /**
     * (未续租)订单到期提醒,短期提前3天，长期提前10天（钉钉消息，接受者为订单销售员id）
     *
     * @param
     * @return
     * @author ZhaoZiXuan
     * @date 2018/5/24 11:05
     */
    private ServiceResult<String, Object> ReletedOrderSendDingDingMessage(OrderQueryParam orderQueryParam) {
        ServiceResult<String, Object> result = new ServiceResult<>();
        if (null == orderQueryParam) {
            return result;
        }
        PageQuery pageQuery = new PageQuery(orderQueryParam.getPageNo(), orderQueryParam.getPageSize());
        result.setErrorCode(ErrorCode.SUCCESS);
        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        maps.put("orderQueryParam", orderQueryParam);
//        maps.put("permissionParam", permissionSupport.getPermissionParam(PermissionType.PERMISSION_TYPE_SUB_COMPANY_FOR_SERVICE, PermissionType.PERMISSION_TYPE_SUB_COMPANY_FOR_BUSINESS, PermissionType.PERMISSION_TYPE_USER));
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //订单到期时 提醒续租
        List<OrderDO> orderDOList = orderMapper.findOrderByParams(maps);
        if (null != orderDOList && orderDOList.size() > 0) {
            for (OrderDO orderDO : orderDOList) {
                MessageThirdChannel messageThirdChannel = new MessageThirdChannel();
                messageThirdChannel.setReceiverUserId(orderDO.getOrderSellerId());
                messageThirdChannel.setMessageContent(orderDO.getOrderNo() + "订单将于" + sdf.format(orderDO.getExpectReturnTime()) + "到期，继续使用请及时续租.");
                messageThirdChannel.setMessageTitle("续租提醒");
                result = messageThirdChannelService.sendMessage(messageThirdChannel);
            }
        }
        return result;
    }

    @Override
    public ServiceResult<String, String> cancelReletOrderByNo(ReletOrder reletOrder){
        ServiceResult<String, String> result = new ServiceResult<>();
        if (StringUtil.isBlank(reletOrder.getReletOrderNo())){
            result.setErrorCode(ErrorCode.PARAM_IS_NOT_NULL);
            return result;
        }
        Date currentTime = new Date();
        User loginUser = userSupport.getCurrentUser();

        ReletOrderDO reletOrderDO = reletOrderMapper.findByReletOrderNo(reletOrder.getReletOrderNo());
        if (reletOrderDO == null) {
            result.setErrorCode(ErrorCode.RELET_ORDER_NOT_EXISTS);
            return result;
        }

        if (ReletOrderStatus.RELET_ORDER_STATUS_WAIT_COMMIT.equals(reletOrderDO.getReletOrderStatus())){
            reletOrderDO.setReletOrderNo(reletOrder.getReletOrderNo());
            reletOrderDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
            reletOrderDO.setUpdateUser(loginUser.getUserId().toString());
            reletOrderDO.setUpdateTime(currentTime);
            reletOrderMapper.update(reletOrderDO);
        }
        else {
            result.setErrorCode(ErrorCode.RELET_ORDER_STATUS_CAN_NOT_CANCEL);
            return result;
        }

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(reletOrderDO.getReletOrderNo());
        return result;
    }

    @Override
    public ServiceResult<String, Boolean> isNeedVerify(ReletOrder reletOrder){
        ServiceResult<String, Boolean> result = new ServiceResult<>();
        if (reletOrder.getReletOrderNo() == null) {
            result.setErrorCode(ErrorCode.PARAM_IS_NOT_NULL);
            return result;
        }
        ReletOrderDO reletOrderDO = reletOrderMapper.findByReletOrderNo(reletOrder.getReletOrderNo());
        if (reletOrderDO == null) {
            result.setErrorCode(ErrorCode.RELET_ORDER_NOT_EXISTS);
            return result;
        }

        OrderDO orderDO = orderMapper.findByOrderNo(reletOrderDO.getOrderNo());
        result = checkIsNeedVerify(orderDO, reletOrderDO);
        return result;
    }

    /**
     * 是否需要审核 （续租单 单价浮动在10%以内不需要审核）
     *
     * @param
     * @return
     * @author ZhaoZiXuan
     * @date 2018/5/23 14:28
     */
    private ServiceResult<String, Boolean> checkIsNeedVerify(OrderDO order, ReletOrderDO reletOrderDO) {
        ServiceResult<String, Boolean> result = new ServiceResult<>();
        boolean isNeedVerify = false;

//        if (reletOrderDO.getReletOrderStatus() != ReletOrderStatus.RELET_ORDER_STATUS_WAIT_COMMIT){
//            result.setErrorCode(ErrorCode.SUCCESS);
//            result.setResult(isNeedVerify);
//            return result;
//        }

        if (CollectionUtil.isNotEmpty(reletOrderDO.getReletOrderProductDOList())) {

            for (ReletOrderProductDO reletOrderProductDO : reletOrderDO.getReletOrderProductDOList()) {

                OrderProductDO orderProduct = getOrderProductDOById(order, reletOrderProductDO.getOrderProductId());
                if (null == orderProduct || null == orderProduct.getProductUnitAmount()
                        || BigDecimalUtil.compare(orderProduct.getProductUnitAmount(), BigDecimal.ZERO) < 0) {
                    result.setErrorCode(ErrorCode.RELET_ORDER_UNIT_AMOUNT_ERROR);
                    return result;
                }

                if (checkReletOrderUnitAmountScope(reletOrderProductDO.getProductUnitAmount(), orderProduct.getProductUnitAmount())){
                    isNeedVerify = true;
                    break;
                }
            }
        }

        if (CollectionUtil.isNotEmpty(reletOrderDO.getReletOrderMaterialDOList()) && !isNeedVerify) {

            for (ReletOrderMaterialDO reletOrderMaterialDO : reletOrderDO.getReletOrderMaterialDOList()) {

                OrderMaterialDO orderMaterial = getOrderMaterialDOById(order, reletOrderMaterialDO.getOrderMaterialId());
                if (null == orderMaterial || null == orderMaterial.getMaterialUnitAmount()
                        || BigDecimalUtil.compare(orderMaterial.getMaterialUnitAmount(), BigDecimal.ZERO) < 0) {
                    result.setErrorCode(ErrorCode.RELET_ORDER_UNIT_AMOUNT_ERROR);
                    return result;
                }

                if (checkReletOrderUnitAmountScope(reletOrderMaterialDO.getMaterialUnitAmount(), orderMaterial.getMaterialUnitAmount())){
                    isNeedVerify = true;
                    break;
                }
            }
        }

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(isNeedVerify);
        return result;
    }

    /**
     * 比较现在价格 和 原来价格 差价
     *
     * @author ZhaoZiXuan
     * @date 2018/5/25 14:14
     * @param
     * @return
     */
    private Boolean checkReletOrderUnitAmountScope(BigDecimal unitAmount,BigDecimal originUnitAmount){
        BigDecimal floorValue = new BigDecimal(1);
        BigDecimal ceilValue = new BigDecimal(1.1);

        BigDecimal resultFloorValue = BigDecimalUtil.mul(originUnitAmount, floorValue);
        BigDecimal resultCeilValue = BigDecimalUtil.mul(originUnitAmount, ceilValue);
        BigDecimal zeroValue = new BigDecimal(0);
        //原价和现价都为零时  无需审核
        if (BigDecimalUtil.compare(unitAmount, zeroValue) == 0 && BigDecimalUtil.compare(originUnitAmount, zeroValue) == 0){
            return false;
        }

        if (BigDecimalUtil.compare(unitAmount, resultFloorValue) < 0
                || BigDecimalUtil.compare(unitAmount, resultCeilValue) >= 0) {
            return true;
        }
        return false;
    }


    /**
     * 同步续租单 时长和单价  到订单对象
     *
     * @param
     * @return
     * @author ZhaoZiXuan
     * @date 2018/5/24 12:00
     */
    private String syncReletOrderToOrder(ReletOrderDO reletOrderDO, OrderDO orderDO) {

        if (null == reletOrderDO || null == orderDO) {
            return ErrorCode.PARAM_IS_NOT_NULL;
        }

        if (reletOrderDO.getRentTimeLength() <= 0) {
            return ErrorCode.RELET_ORDER_RENT_TIME_LENGTH_ERROR;
        }
        orderDO.setRentTimeLength(reletOrderDO.getRentTimeLength()); //租赁时长

        orderDO.setRentStartTime(reletOrderDO.getRentStartTime());  //起租时间
        orderDO.setExpectReturnTime(reletOrderDO.getExpectReturnTime()); //预计归还时间
        orderDO.setTotalOrderAmount(reletOrderDO.getTotalOrderAmount());  //订单总金额

        orderDO.setTotalProductCount(reletOrderDO.getTotalProductCount());
        orderDO.setTotalProductAmount(reletOrderDO.getTotalProductAmount());
        orderDO.setTotalMaterialCount(reletOrderDO.getTotalMaterialCount());
        orderDO.setTotalMaterialAmount(reletOrderDO.getTotalMaterialAmount());
        orderDO.setTotalOrderAmount(reletOrderDO.getTotalOrderAmount());
        orderDO.setTotalPaidOrderAmount(reletOrderDO.getTotalPaidOrderAmount());


        if (CollectionUtil.isNotEmpty(reletOrderDO.getReletOrderProductDOList())) {

            for (ReletOrderProductDO reletOrderProductDO : reletOrderDO.getReletOrderProductDOList()) {

                OrderProductDO orderProduct = getOrderProductDOById(orderDO, reletOrderProductDO.getOrderProductId());
                if (null == orderProduct || null == orderProduct.getProductUnitAmount()
                        || BigDecimalUtil.compare(orderProduct.getProductUnitAmount(), BigDecimal.ZERO) < 0) {
                    return ErrorCode.RELET_ORDER_UNIT_AMOUNT_ERROR;
                }
                orderProduct.setProductUnitAmount(reletOrderProductDO.getProductUnitAmount());
                orderProduct.setProductAmount(reletOrderProductDO.getProductAmount());
            }
        }

        if (CollectionUtil.isNotEmpty(reletOrderDO.getReletOrderMaterialDOList())) {

            for (ReletOrderMaterialDO reletOrderMaterialDO : reletOrderDO.getReletOrderMaterialDOList()) {

                OrderMaterialDO orderMaterial = getOrderMaterialDOById(orderDO, reletOrderMaterialDO.getOrderMaterialId());
                if (null == orderMaterial || null == orderMaterial.getMaterialUnitAmount()
                        || BigDecimalUtil.compare(orderMaterial.getMaterialUnitAmount(), BigDecimal.ZERO) < 0) {
                    return ErrorCode.RELET_ORDER_UNIT_AMOUNT_ERROR;
                }
                orderMaterial.setMaterialUnitAmount(reletOrderMaterialDO.getMaterialUnitAmount());
                orderMaterial.setMaterialAmount(reletOrderMaterialDO.getMaterialAmount());
            }
        }

        return ErrorCode.SUCCESS;
    }


    /**
     * 同步订单信息到 目标订单中 （租赁时长， 单价）
     *
     * @param
     * @return  是否需要审核
     * @author ZhaoZiXuan
     * @date 2018/5/23 9:47
     */
    private ServiceResult<String, Boolean> syncOrderToReletOrder(Order order, ReletOrderDO reletOrderDO) {

        ServiceResult<String, Boolean> result = new ServiceResult<>();
        boolean isNeedVerify = false;

        OrderDO orderDO = ConverterUtil.convert(order, OrderDO.class);

        if (null == orderDO || null == reletOrderDO) {
            result.setErrorCode(ErrorCode.PARAM_IS_NOT_NULL);
            return result;
        }

        if (orderDO.getRentTimeLength() <= 0) {
            result.setErrorCode(ErrorCode.RELET_ORDER_RENT_TIME_LENGTH_ERROR);
            return result;
        }
        reletOrderDO.setRentTimeLength(orderDO.getRentTimeLength()); //租赁时长

        if (CollectionUtil.isNotEmpty(reletOrderDO.getReletOrderProductDOList())) {

            for (ReletOrderProductDO targetOrderProduct : reletOrderDO.getReletOrderProductDOList()) {

                OrderProductDO orderProduct = getOrderProductDOById(orderDO, targetOrderProduct.getOrderProductId());
                if (null == orderProduct || null == orderProduct.getProductUnitAmount()
                        || BigDecimalUtil.compare(orderProduct.getProductUnitAmount(), BigDecimal.ZERO) < 0) {
                    result.setErrorCode(ErrorCode.RELET_ORDER_UNIT_AMOUNT_ERROR);
                    return result;
                }

                if (!isNeedVerify && checkReletOrderUnitAmountScope(orderProduct.getProductUnitAmount(), targetOrderProduct.getProductUnitAmount())){
                    isNeedVerify = true;
                }
                targetOrderProduct.setProductUnitAmount(orderProduct.getProductUnitAmount());
            }
        }

        if (CollectionUtil.isNotEmpty(reletOrderDO.getReletOrderMaterialDOList())) {

            for (ReletOrderMaterialDO targetOrderMaterial : reletOrderDO.getReletOrderMaterialDOList()) {

                OrderMaterialDO orderMaterial = getOrderMaterialDOById(orderDO, targetOrderMaterial.getOrderMaterialId());
                if (null == orderMaterial || null == orderMaterial.getMaterialUnitAmount()
                        || BigDecimalUtil.compare(orderMaterial.getMaterialUnitAmount(), BigDecimal.ZERO) < 0) {
                    result.setErrorCode(ErrorCode.RELET_ORDER_UNIT_AMOUNT_ERROR);
                    return result;
                }

                if (!isNeedVerify && checkReletOrderUnitAmountScope(orderMaterial.getMaterialUnitAmount(), targetOrderMaterial.getMaterialUnitAmount())){
                    isNeedVerify = true;
                }
                targetOrderMaterial.setMaterialUnitAmount(orderMaterial.getMaterialUnitAmount());
            }
        }

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(isNeedVerify);
        return result;
    }


    /**
     * 同步续租单 VO到DO （租赁时长， 单价）
     *
     * @param
     * @return  是否需要审核
     * @author ZhaoZiXuan
     * @date 2018/5/23 9:47
     */
    private ServiceResult<String, Boolean> syncReletOrderToReletOrderDO(ReletOrder reletOrder, ReletOrderDO reletOrderDO) {

        ServiceResult<String, Boolean> result = new ServiceResult<>();
        boolean isNeedVerify = false;

        if (null == reletOrder || null == reletOrderDO) {
            result.setErrorCode(ErrorCode.PARAM_IS_NOT_NULL);
            return result;
        }

        if (reletOrder.getRentTimeLength() <= 0) {
            result.setErrorCode(ErrorCode.RELET_ORDER_RENT_TIME_LENGTH_ERROR);
            return result;
        }
        reletOrderDO.setRentTimeLength(reletOrder.getRentTimeLength()); //租赁时长

        if (CollectionUtil.isNotEmpty(reletOrderDO.getReletOrderProductDOList())) {

            for (ReletOrderProductDO targetOrderProduct : reletOrderDO.getReletOrderProductDOList()) {

                ReletOrderProduct orderProduct = getReletOrderProductById(reletOrder, targetOrderProduct.getOrderProductId());
                if (null == orderProduct || null == orderProduct.getProductUnitAmount()
                        || BigDecimalUtil.compare(orderProduct.getProductUnitAmount(), BigDecimal.ZERO) < 0) {
                    result.setErrorCode(ErrorCode.RELET_ORDER_UNIT_AMOUNT_ERROR);
                    return result;
                }

                if (!isNeedVerify && checkReletOrderUnitAmountScope(orderProduct.getProductUnitAmount(), targetOrderProduct.getProductUnitAmount())){
                    isNeedVerify = true;
                }
                targetOrderProduct.setProductUnitAmount(orderProduct.getProductUnitAmount());
            }
        }

        if (CollectionUtil.isNotEmpty(reletOrderDO.getReletOrderMaterialDOList())) {

            for (ReletOrderMaterialDO targetOrderMaterial : reletOrderDO.getReletOrderMaterialDOList()) {

                ReletOrderMaterial orderMaterial = getReletOrderMaterialById(reletOrder, targetOrderMaterial.getOrderMaterialId());
                if (null == orderMaterial || null == orderMaterial.getMaterialUnitAmount()
                        || BigDecimalUtil.compare(orderMaterial.getMaterialUnitAmount(), BigDecimal.ZERO) < 0) {
                    result.setErrorCode(ErrorCode.RELET_ORDER_UNIT_AMOUNT_ERROR);
                    return result;
                }

                if (!isNeedVerify && checkReletOrderUnitAmountScope(orderMaterial.getMaterialUnitAmount(), targetOrderMaterial.getMaterialUnitAmount())){
                    isNeedVerify = true;
                }
                targetOrderMaterial.setMaterialUnitAmount(orderMaterial.getMaterialUnitAmount());
            }
        }

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(isNeedVerify);
        return result;
    }

    private ReletOrderProduct getReletOrderProductById(ReletOrder reletOrder, Integer orderProductId) {

        if (CollectionUtil.isNotEmpty(reletOrder.getReletOrderProductList())) {

            for (ReletOrderProduct reletOrderProduct : reletOrder.getReletOrderProductList()) {
                if (reletOrderProduct.getOrderProductId() != null && reletOrderProduct.getOrderProductId().equals(orderProductId)) {
                    return reletOrderProduct;
                }
            }
        }
        return null;
    }


    private ReletOrderMaterial getReletOrderMaterialById(ReletOrder reletOrder, Integer orderMaterialId) {

        if (CollectionUtil.isNotEmpty(reletOrder.getReletOrderMaterialList())) {

            for (ReletOrderMaterial reletOrderMaterial : reletOrder.getReletOrderMaterialList()) {
                if (reletOrderMaterial.getOrderMaterialId() != null && reletOrderMaterial.getOrderMaterialId().equals(orderMaterialId)) {
                    return reletOrderMaterial;
                }
            }
        }
        return null;
    }


    /**
     * 通过商品项id查找 订单DO中的商品项信息
     *
     * @param
     * @return
     * @author ZhaoZiXuan
     * @date 2018/5/23 9:42
     */
    private OrderProductDO getOrderProductDOById(OrderDO order, Integer id) {

        if (CollectionUtil.isNotEmpty(order.getOrderProductDOList())) {

            for (OrderProductDO orderProduct : order.getOrderProductDOList()) {
                if (orderProduct.getId().equals(id)) {
                    return orderProduct;
                }
            }
        }
        return null;
    }

    /**
     * 通过配件项id查找 订单中的配件项信息
     *
     * @param
     * @return
     * @author ZhaoZiXuan
     * @date 2018/5/23 9:44
     */
    private OrderMaterialDO getOrderMaterialDOById(OrderDO order, Integer id) {

        if (CollectionUtil.isNotEmpty(order.getOrderMaterialDOList())) {

            for (OrderMaterialDO orderMaterial : order.getOrderMaterialDOList()) {
                if (orderMaterial.getId().equals(id)) {
                    return orderMaterial;
                }
            }
        }
        return null;
    }


    private void saveReletOrderProductInfo(ReletOrderDO reletOrderDO, User loginUser, Date currentTime) {

        List<ReletOrderProductDO> saveOrderProductDOList = new ArrayList<>();
        Map<Integer, ReletOrderProductDO> updateOrderProductDOMap = new HashMap<>();
        List<ReletOrderProductDO> dbOrderProductDOList = reletOrderProductMapper.findByReletOrderId(reletOrderDO.getId());
        Map<Integer, ReletOrderProductDO> dbOrderProductDOMap = ListUtil.listToMap(dbOrderProductDOList, "id");
        if (CollectionUtil.isNotEmpty(reletOrderDO.getReletOrderProductDOList())) {
            for (ReletOrderProductDO orderProductDO : reletOrderDO.getReletOrderProductDOList()) {

                ReletOrderProductDO dbOrderProductDO = dbOrderProductDOMap.get(orderProductDO.getId());
                if (dbOrderProductDO != null) {
                    orderProductDO.setId(dbOrderProductDO.getId());
                    updateOrderProductDOMap.put(orderProductDO.getId(), orderProductDO);
                    dbOrderProductDOMap.remove(orderProductDO.getId());
                } else {
                    saveOrderProductDOList.add(orderProductDO);
                }
            }
        }

        if (saveOrderProductDOList.size() > 0) {
            for (ReletOrderProductDO orderProductDO : saveOrderProductDOList) {
                orderProductDO.setReletOrderId(reletOrderDO.getId());
                orderProductDO.setReletOrderNo(reletOrderDO.getReletOrderNo());
                orderProductDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                orderProductDO.setCreateUser(loginUser.getUserId().toString());
                orderProductDO.setUpdateUser(loginUser.getUserId().toString());
                orderProductDO.setCreateTime(currentTime);
                orderProductDO.setUpdateTime(currentTime);
                reletOrderProductMapper.save(orderProductDO);
            }
        }

        if (updateOrderProductDOMap.size() > 0) {
            for (Map.Entry<Integer, ReletOrderProductDO> entry : updateOrderProductDOMap.entrySet()) {
                ReletOrderProductDO orderProductDO = entry.getValue();
                orderProductDO.setReletOrderId(reletOrderDO.getId());
                orderProductDO.setReletOrderNo(reletOrderDO.getReletOrderNo());
                orderProductDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                orderProductDO.setUpdateUser(loginUser.getUserId().toString());
                orderProductDO.setUpdateTime(currentTime);
                reletOrderProductMapper.update(orderProductDO);
            }
        }

        if (dbOrderProductDOMap.size() > 0) {
            for (Map.Entry<Integer, ReletOrderProductDO> entry : dbOrderProductDOMap.entrySet()) {
                ReletOrderProductDO orderProductDO = entry.getValue();
                orderProductDO.setReletOrderId(reletOrderDO.getId());
                orderProductDO.setReletOrderNo(reletOrderDO.getReletOrderNo());
                orderProductDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
                orderProductDO.setUpdateUser(loginUser.getUserId().toString());
                orderProductDO.setUpdateTime(currentTime);
                reletOrderProductMapper.update(orderProductDO);
            }
        }
    }


    private void saveReletOrderMaterialInfo(ReletOrderDO reletOrderDO, User loginUser, Date currentTime) {

        List<ReletOrderMaterialDO> saveOrderMaterialDOList = new ArrayList<>();
        Map<Integer, ReletOrderMaterialDO> updateOrderMaterialDOMap = new HashMap<>();
        List<ReletOrderMaterialDO> dbOrderMaterialDOList = reletOrderMaterialMapper.findByReletOrderId(reletOrderDO.getId());
        Map<Integer, ReletOrderMaterialDO> dbOrderMaterialDOMap = ListUtil.listToMap(dbOrderMaterialDOList, "id");
        if (CollectionUtil.isNotEmpty(reletOrderDO.getReletOrderMaterialDOList())) {
            for (ReletOrderMaterialDO orderMaterialDO : reletOrderDO.getReletOrderMaterialDOList()) {
                ReletOrderMaterialDO dbOrderMaterialDO = dbOrderMaterialDOMap.get(orderMaterialDO.getId());
                if (dbOrderMaterialDO != null) {
                    orderMaterialDO.setId(dbOrderMaterialDO.getId());
                    updateOrderMaterialDOMap.put(orderMaterialDO.getId(), orderMaterialDO);
                    dbOrderMaterialDOMap.remove(orderMaterialDO.getId());
                } else {
                    saveOrderMaterialDOList.add(orderMaterialDO);
                }
            }
        }

        if (saveOrderMaterialDOList.size() > 0) {
            for (ReletOrderMaterialDO orderMaterialDO : saveOrderMaterialDOList) {
                orderMaterialDO.setReletOrderId(reletOrderDO.getId());
                orderMaterialDO.setReletOrderNo(reletOrderDO.getReletOrderNo());
                orderMaterialDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                orderMaterialDO.setCreateUser(loginUser.getUserId().toString());
                orderMaterialDO.setUpdateUser(loginUser.getUserId().toString());
                orderMaterialDO.setCreateTime(currentTime);
                orderMaterialDO.setUpdateTime(currentTime);
                reletOrderMaterialMapper.save(orderMaterialDO);
            }
        }

        if (updateOrderMaterialDOMap.size() > 0) {
            for (Map.Entry<Integer, ReletOrderMaterialDO> entry : updateOrderMaterialDOMap.entrySet()) {
                ReletOrderMaterialDO orderMaterialDO = entry.getValue();
                orderMaterialDO.setReletOrderId(reletOrderDO.getId());
                orderMaterialDO.setReletOrderNo(reletOrderDO.getReletOrderNo());
                orderMaterialDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                orderMaterialDO.setUpdateUser(loginUser.getUserId().toString());
                orderMaterialDO.setUpdateTime(currentTime);
                reletOrderMaterialMapper.update(orderMaterialDO);
            }
        }

        if (dbOrderMaterialDOMap.size() > 0) {
            for (Map.Entry<Integer, ReletOrderMaterialDO> entry : dbOrderMaterialDOMap.entrySet()) {
                ReletOrderMaterialDO orderMaterialDO = entry.getValue();
                orderMaterialDO.setReletOrderId(reletOrderDO.getId());
                orderMaterialDO.setReletOrderNo(reletOrderDO.getReletOrderNo());
                orderMaterialDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
                orderMaterialDO.setUpdateUser(loginUser.getUserId().toString());
                orderMaterialDO.setUpdateTime(currentTime);
                reletOrderMaterialMapper.update(orderMaterialDO);
            }
        }
    }

    /**
     * 保存续租订单的地址信息
     *
     * @author ZhaoZiXuan
     * @date 2018/5/28 10:34
     * @param
     * @return
     */
    private void saveReletOrderConsignInfo(Integer orderIdByRelet, Integer orderId, User loginUser, Date currentTime) {

        OrderConsignInfoDO dbOrderConsignInfoDO = orderConsignInfoMapper.findByOrderId(orderId);
        if (dbOrderConsignInfoDO != null){
            dbOrderConsignInfoDO.setOrderId(orderIdByRelet);
            dbOrderConsignInfoDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
            dbOrderConsignInfoDO.setCreateUser(loginUser.getUserId().toString());
            dbOrderConsignInfoDO.setUpdateUser(loginUser.getUserId().toString());
            dbOrderConsignInfoDO.setCreateTime(currentTime);
            dbOrderConsignInfoDO.setUpdateTime(currentTime);
            orderConsignInfoMapper.save(dbOrderConsignInfoDO);
        }

    }

    private Date generateExpectReturnTime(ReletOrderDO reletOrderDO) {
        Date expectReturnTime = null;
        if (CollectionUtil.isNotEmpty(reletOrderDO.getReletOrderProductDOList())) {
            for (ReletOrderProductDO orderProductDO : reletOrderDO.getReletOrderProductDOList()) {
                Date thisExpectReturnTime = calculationOrderExpectReturnTime(reletOrderDO.getRentStartTime(), reletOrderDO.getRentType(), reletOrderDO.getRentTimeLength());
                if (thisExpectReturnTime != null) {
                    expectReturnTime = expectReturnTime == null || expectReturnTime.getTime() < thisExpectReturnTime.getTime() ? thisExpectReturnTime : expectReturnTime;
                }
            }
        }
        if (CollectionUtil.isNotEmpty(reletOrderDO.getReletOrderMaterialDOList())) {
            for (ReletOrderMaterialDO orderMaterialDO : reletOrderDO.getReletOrderMaterialDOList()) {
                Date thisExpectReturnTime = calculationOrderExpectReturnTime(reletOrderDO.getRentStartTime(), reletOrderDO.getRentType(), reletOrderDO.getRentTimeLength());
                if (thisExpectReturnTime != null) {
                    expectReturnTime = expectReturnTime == null || expectReturnTime.getTime() < thisExpectReturnTime.getTime() ? thisExpectReturnTime : expectReturnTime;
                }
            }
        }
        return expectReturnTime;
    }

    /**
     * 计算订单预计归还时间
     */
    private Date calculationOrderExpectReturnTime(Date rentStartTime, Integer rentType, Integer rentTimeLength) {
        if (OrderRentType.RENT_TYPE_DAY.equals(rentType)) {
            return DateUtil.dateInterval(rentStartTime, rentTimeLength - 1);
        } else if (OrderRentType.RENT_TYPE_MONTH.equals(rentType)) {
            return DateUtil.dateInterval(DateUtil.monthInterval(rentStartTime, rentTimeLength), -1);
        }
        return null;
    }


    private void calculateReletOrderProductInfo(List<ReletOrderProductDO> reletOrderProductDOList, ReletOrderDO reletOrderDO) {

        if (CollectionUtil.isNotEmpty(reletOrderProductDOList)) {
            int productCount = 0;
            // 商品租赁总额
            BigDecimal productAmountTotal = new BigDecimal(0.0);

            for (ReletOrderProductDO reletOrderProductDO : reletOrderProductDOList) {

                reletOrderProductDO.setProductAmount(BigDecimalUtil.mul(BigDecimalUtil.mul(reletOrderProductDO.getProductUnitAmount(), new BigDecimal(reletOrderDO.getRentTimeLength()), 2), new BigDecimal(reletOrderProductDO.getRentingProductCount())));

                productCount += reletOrderProductDO.getRentingProductCount();
                productAmountTotal = BigDecimalUtil.add(productAmountTotal, reletOrderProductDO.getProductAmount());
            }

            reletOrderDO.setTotalProductCount(productCount);
            reletOrderDO.setTotalProductAmount(productAmountTotal);
        }
    }


    private void calculateReletOrderMaterialInfo(List<ReletOrderMaterialDO> reletOrderMaterialDOList, ReletOrderDO reletOrderDO) {

        if (CollectionUtil.isNotEmpty(reletOrderMaterialDOList)) {
            int materialCount = 0;
            // 商品租赁总额
            BigDecimal materialAmountTotal = BigDecimal.ZERO;

            for (ReletOrderMaterialDO reletOrderMaterialDO : reletOrderMaterialDOList) {

                reletOrderMaterialDO.setMaterialAmount(BigDecimalUtil.mul(BigDecimalUtil.mul(reletOrderMaterialDO.getMaterialUnitAmount(), new BigDecimal(reletOrderDO.getRentTimeLength()), 2), new BigDecimal(reletOrderMaterialDO.getRentingMaterialCount())));

                materialCount += reletOrderMaterialDO.getRentingMaterialCount();
                materialAmountTotal = BigDecimalUtil.add(materialAmountTotal, reletOrderMaterialDO.getMaterialAmount());
            }
            reletOrderDO.setTotalMaterialCount(materialCount);
            reletOrderDO.setTotalMaterialAmount(materialAmountTotal);
        }
    }


    private void verifyCustomerRiskInfo(ReletOrderDO reletOrderDO) {
        CustomerRiskManagementDO customerRiskManagementDO = customerRiskManagementMapper.findByCustomerId(reletOrderDO.getBuyerCustomerId());
        boolean isCheckRiskManagement = isCheckRiskManagement(reletOrderDO);
        if (isCheckRiskManagement) {
            if (customerRiskManagementDO == null) {
                throw new BusinessException(ErrorCode.CUSTOMER_RISK_MANAGEMENT_NOT_EXISTS);
            }
        }

        if (CollectionUtil.isNotEmpty(reletOrderDO.getReletOrderProductDOList())) {
            for (ReletOrderProductDO reletOrderProductDO : reletOrderDO.getReletOrderProductDOList()) {
                if (OrderRentType.RENT_TYPE_DAY.equals(reletOrderDO.getRentType())
                        && reletOrderDO.getRentTimeLength() >= CommonConstant.ORDER_NEED_VERIFY_DAYS) {
                    throw new BusinessException(ErrorCode.ORDER_RENT_LENGTH_MORE_THAN_90);
                }

                // 如果走风控
                if (isCheckRiskManagement) {
                    ServiceResult<String, Product> productServiceResult = productService.queryProductBySkuId(reletOrderProductDO.getProductSkuId());
                    if (!productServiceResult.getErrorCode().equals(ErrorCode.SUCCESS)) {
                        throw new BusinessException(ErrorCode.PRODUCT_SKU_IS_NULL_OR_NOT_EXISTS);
                    }
                    Product product = productServiceResult.getResult();
                    ProductSku productSku = productServiceResult.getResult().getProductSkuList().get(0);

                    if (BrandId.BRAND_ID_APPLE.equals(product.getBrandId()) && CommonConstant.COMMON_CONSTANT_YES.equals(customerRiskManagementDO.getIsLimitApple())) {
                        throw new BusinessException(ErrorCode.CUSTOMER_RISK_MANAGEMENT_APPLE_LIMIT);
                    }
                    if (CommonConstant.COMMON_CONSTANT_YES.equals(reletOrderProductDO.getIsNewProduct()) && CommonConstant.COMMON_CONSTANT_YES.equals(customerRiskManagementDO.getIsLimitNew())) {
                        throw new BusinessException(ErrorCode.CUSTOMER_RISK_MANAGEMENT_NEW_LIMIT);
                    }
                    BigDecimal skuPrice = CommonConstant.COMMON_CONSTANT_YES.equals(reletOrderProductDO.getIsNewProduct()) ? productSku.getNewSkuPrice() : productSku.getSkuPrice();
                    if (customerRiskManagementDO.getSingleLimitPrice() != null && BigDecimalUtil.compare(skuPrice, customerRiskManagementDO.getSingleLimitPrice()) > 0) {
                        throw new BusinessException(ErrorCode.CUSTOMER_RISK_MANAGEMENT_PRICE_LIMIT);
                    }

                    Integer payCycle, payMode;  //续租时无押金depositCycle
                    boolean productIsCheckRiskManagement = isCheckRiskManagement(reletOrderDO, reletOrderProductDO, null);
                    if (!productIsCheckRiskManagement) {
                        if (reletOrderProductDO.getPayMode() == null) {
                            throw new BusinessException(ErrorCode.ORDER_PAY_MODE_NOT_NULL);
                        }
                        // 如果不走风控，就押0付0
                        //reletOrderProductDO.setDepositCycle(0);  无押金
                        reletOrderProductDO.setPaymentCycle(0);
                        continue;
                    }
                    // 判断用户是否为全额押金用户，如果为全额押金用户，即全额押金，并且押0付1
                    if (CommonConstant.COMMON_CONSTANT_YES.equals(customerRiskManagementDO.getIsFullDeposit())) {
                        //depositCycle = 0;
                        payCycle = 1;
                        payMode = OrderPayMode.PAY_MODE_PAY_BEFORE;
                    } else {
                        // 查看客户风控信息是否齐全
                        /*if (!customerSupport.isFullRiskManagement(orderDO.getBuyerCustomerId())) {
                            throw new BusinessException(ErrorCode.CUSTOMER_RISK_MANAGEMENT_NOT_FULL);
                        }*/
                        if (BrandId.BRAND_ID_APPLE.equals(product.getBrandId())) {
                            // 商品品牌为苹果品牌
                            //depositCycle = customerRiskManagementDO.getAppleDepositCycle();
                            payCycle = customerRiskManagementDO.getApplePaymentCycle();
                            payMode = customerRiskManagementDO.getApplePayMode();
                        } else if (CommonConstant.COMMON_CONSTANT_YES.equals(reletOrderProductDO.getIsNewProduct())) {
                            //depositCycle = customerRiskManagementDO.getNewDepositCycle();
                            payCycle = customerRiskManagementDO.getNewPaymentCycle();
                            payMode = customerRiskManagementDO.getNewPayMode();
                        } else {
                            //depositCycle = customerRiskManagementDO.getDepositCycle();
                            payCycle = customerRiskManagementDO.getPaymentCycle();
                            payMode = customerRiskManagementDO.getPayMode();
                        }
                    }
                    if (OrderRentType.RENT_TYPE_MONTH.equals(reletOrderDO.getRentType())) {
                        payCycle = payCycle > reletOrderDO.getRentTimeLength() ? reletOrderDO.getRentTimeLength() : payCycle;
                    }
                    //reletOrderProductDO.setDepositCycle(depositCycle);
                    reletOrderProductDO.setPaymentCycle(payCycle);
                    reletOrderProductDO.setPayMode(payMode);
                } else {
                    if (reletOrderProductDO.getPayMode() == null) {
                        throw new BusinessException(ErrorCode.ORDER_PAY_MODE_NOT_NULL);
                    } else if (OrderPayMode.PAY_MODE_PAY_BEFORE_PERCENT.equals(reletOrderProductDO.getPayMode())) {
                        //reletOrderProductDO.setDepositCycle(0);
                        reletOrderProductDO.setPaymentCycle(0);
                    }
                }
            }
        }

        if (CollectionUtil.isNotEmpty(reletOrderDO.getReletOrderMaterialDOList())) {
            for (ReletOrderMaterialDO reletOrderMaterialDO : reletOrderDO.getReletOrderMaterialDOList()) {
                if (OrderRentType.RENT_TYPE_DAY.equals(reletOrderDO.getRentType())
                        && reletOrderDO.getRentTimeLength() >= CommonConstant.ORDER_NEED_VERIFY_DAYS) {
                    throw new BusinessException(ErrorCode.ORDER_RENT_LENGTH_MORE_THAN_90);
                }

                // 如果走风控
                if (isCheckRiskManagement) {
                    ServiceResult<String, Material> materialResult = materialService.queryMaterialById(reletOrderMaterialDO.getMaterialId());
                    Material material = materialResult.getResult();
                    if (BrandId.BRAND_ID_APPLE.equals(material.getBrandId()) && CommonConstant.COMMON_CONSTANT_YES.equals(customerRiskManagementDO.getIsLimitApple())) {
                        throw new BusinessException(ErrorCode.CUSTOMER_RISK_MANAGEMENT_APPLE_LIMIT);
                    }
                    if (CommonConstant.COMMON_CONSTANT_YES.equals(reletOrderMaterialDO.getIsNewMaterial()) && CommonConstant.COMMON_CONSTANT_YES.equals(customerRiskManagementDO.getIsLimitNew())) {
                        throw new BusinessException(ErrorCode.CUSTOMER_RISK_MANAGEMENT_NEW_LIMIT);
                    }
                    BigDecimal materialPrice = CommonConstant.COMMON_CONSTANT_YES.equals(reletOrderMaterialDO.getIsNewMaterial()) ? material.getNewMaterialPrice() : material.getMaterialPrice();
                    if (customerRiskManagementDO.getSingleLimitPrice() != null && BigDecimalUtil.compare(materialPrice, customerRiskManagementDO.getSingleLimitPrice()) >= 0) {
                        throw new BusinessException(ErrorCode.CUSTOMER_RISK_MANAGEMENT_PRICE_LIMIT);
                    }
                    boolean materialIsCheckRiskManagement = isCheckRiskManagement(reletOrderDO, null, reletOrderMaterialDO);
                    if (!materialIsCheckRiskManagement) {
                        if (reletOrderMaterialDO.getPayMode() == null) {
                            throw new BusinessException(ErrorCode.ORDER_PAY_MODE_NOT_NULL);
                        }
                        // 如果不走风控，就押0付0
                        //reletOrderMaterialDO.setDepositCycle(0);
                        reletOrderMaterialDO.setPaymentCycle(0);
                        continue;
                    }
                    Integer payCycle, payMode;//depositCycle
                    // 判断用户是否为全额押金用户，如果为全额押金用户，即全额押金，并且押0付1
                    if (CommonConstant.COMMON_CONSTANT_YES.equals(customerRiskManagementDO.getIsFullDeposit())) {
                        //depositCycle = 0;
                        payCycle = 1;
                        payMode = OrderPayMode.PAY_MODE_PAY_BEFORE;
                    } else if (BrandId.BRAND_ID_APPLE.equals(material.getBrandId())) {
                        // 商品品牌为苹果品牌
                        //depositCycle = customerRiskManagementDO.getAppleDepositCycle();
                        payCycle = customerRiskManagementDO.getApplePaymentCycle();
                        payMode = customerRiskManagementDO.getApplePayMode();
                    } else if (CommonConstant.COMMON_CONSTANT_YES.equals(reletOrderMaterialDO.getIsNewMaterial())) {
                        //depositCycle = customerRiskManagementDO.getNewDepositCycle();
                        payCycle = customerRiskManagementDO.getNewPaymentCycle();
                        payMode = customerRiskManagementDO.getNewPayMode();
                    } else {
                        //depositCycle = customerRiskManagementDO.getDepositCycle();
                        payCycle = customerRiskManagementDO.getPaymentCycle();
                        payMode = customerRiskManagementDO.getPayMode();
                    }
                    if (OrderRentType.RENT_TYPE_MONTH.equals(reletOrderDO.getRentType())) {
                        //depositCycle = depositCycle > orderMaterialDO.getRentTimeLength() ? orderMaterialDO.getRentTimeLength() : depositCycle;
                        payCycle = payCycle > reletOrderDO.getRentTimeLength() ? reletOrderDO.getRentTimeLength() : payCycle;
                    }
                    //orderMaterialDO.setDepositCycle(depositCycle);
                    reletOrderMaterialDO.setPaymentCycle(payCycle);
                    reletOrderMaterialDO.setPayMode(payMode);
                } else {
                    if (reletOrderMaterialDO.getPayMode() == null) {
                        throw new BusinessException(ErrorCode.ORDER_PAY_MODE_NOT_NULL);
                    } else if (OrderPayMode.PAY_MODE_PAY_BEFORE_PERCENT.equals(reletOrderMaterialDO.getPayMode())) {
                        //reletOrderMaterialDO.setDepositCycle(0);
                        reletOrderMaterialDO.setPaymentCycle(0);
                    }

                }
            }
        }
    }


    private boolean isCheckRiskManagement(ReletOrderDO reletOrderDO) {

        BigDecimal totalProductAmount = BigDecimal.ZERO;
        BigDecimal totalMaterialAmount = BigDecimal.ZERO;
        Integer totalProductCount = 0;
        if (CollectionUtil.isNotEmpty(reletOrderDO.getReletOrderProductDOList())) {
            for (ReletOrderProductDO reletOrderProductDO : reletOrderDO.getReletOrderProductDOList()) {
                boolean isCheckRiskManagement = isCheckRiskManagement(reletOrderDO, reletOrderProductDO, null);
                if (isCheckRiskManagement) {
                    return true;
                }
                ServiceResult<String, Product> productServiceResult = productService.queryProductBySkuId(reletOrderProductDO.getProductSkuId());
                if (ErrorCode.SUCCESS.equals(productServiceResult.getErrorCode())) {
                    ProductSku productSku = productServiceResult.getResult().getProductSkuList().get(0);
                    BigDecimal skuPrice = CommonConstant.COMMON_CONSTANT_YES.equals(reletOrderProductDO.getIsNewProduct()) ? productSku.getNewSkuPrice() : productSku.getSkuPrice();
                    totalProductAmount = BigDecimalUtil.add(totalProductAmount, BigDecimalUtil.mul(new BigDecimal(reletOrderProductDO.getProductCount()), skuPrice));
                    totalProductCount += reletOrderProductDO.getProductCount();
                }
            }
        }

        if (CollectionUtil.isNotEmpty(reletOrderDO.getReletOrderMaterialDOList())) {
            for (ReletOrderMaterialDO reletOrderMaterialDO : reletOrderDO.getReletOrderMaterialDOList()) {

                boolean isCheckRiskManagement = isCheckRiskManagement(reletOrderDO, null, reletOrderMaterialDO);
                if (isCheckRiskManagement) {
                    return true;
                }
                ServiceResult<String, Material> materialResult = materialService.queryMaterialById(reletOrderMaterialDO.getMaterialId());
                if (ErrorCode.SUCCESS.equals(materialResult.getErrorCode())) {
                    Material material = materialResult.getResult();
                    BigDecimal materialPrice = CommonConstant.COMMON_CONSTANT_YES.equals(reletOrderMaterialDO.getIsNewMaterial()) ? material.getNewMaterialPrice() : material.getMaterialPrice();
                    totalMaterialAmount = BigDecimalUtil.add(totalMaterialAmount, BigDecimalUtil.mul(new BigDecimal(reletOrderMaterialDO.getMaterialCount()), materialPrice));
                }
            }
        }
        BigDecimal totalAmount = BigDecimalUtil.add(totalProductAmount, totalMaterialAmount);
        if (totalProductCount >= CommonConstant.ORDER_NEED_VERIFY_PRODUCT_COUNT || BigDecimalUtil.compare(totalAmount, CommonConstant.ORDER_NEED_VERIFY_PRODUCT_AMOUNT) >= 0) {
            return true;
        }
        return false;
    }


    private boolean isCheckRiskManagement(ReletOrderDO reletOrderDO, ReletOrderProductDO orderProductDO, ReletOrderMaterialDO orderMaterialDO) {
        if (orderProductDO != null || orderMaterialDO != null) {
            if (OrderRentType.RENT_TYPE_DAY.equals(reletOrderDO.getRentType())
                    && reletOrderDO.getRentTimeLength() >= CommonConstant.ORDER_NEED_VERIFY_DAYS) {
                return true;
            }
            if (OrderRentType.RENT_TYPE_MONTH.equals(reletOrderDO.getRentType())) {
                return true;
            }
        }
        return false;
    }


    /**
     * 验证续租时间是否在合法续租范围
     *
     * @param returnTime 到期时间
     * @return
     * @author ZhaoZiXuan
     * @date 2018/5/9 15:16
     */
    private String validReletTimeRange(Date returnTime, Date currentTime, Integer rentType) {
        Integer dayCount = com.lxzl.erp.common.util.DateUtil.daysBetween(returnTime, currentTime);
        if ((OrderRentType.RENT_TYPE_MONTH.equals(rentType) && dayCount < -9)
                || (OrderRentType.RENT_TYPE_DAY.equals(rentType) && dayCount < -2)) {  //订单： 按月租前10天 和  按天租前3天 可续租
            return ErrorCode.RELET_ORDER_NOT_IN_RELET_TIME_SCOPE;
        }
        return ErrorCode.SUCCESS;
    }


    private String verifyOperateOrder(Order order) {
        Integer intRentingCount = 0;
        if (order == null) {
            return ErrorCode.SYSTEM_ERROR;
        }

        //确认收货，部分归还，状态时 可续租
        if (!OrderStatus.ORDER_STATUS_CONFIRM.equals(order.getOrderStatus())
                && !OrderStatus.ORDER_STATUS_PART_RETURN.equals(order.getOrderStatus())) {
            return ErrorCode.RELET_ORDER_NOT_IN_RELET_STATUS_SCOPE;
        }

        if ((order.getOrderProductList() == null || order.getOrderProductList().isEmpty())
                && (order.getOrderMaterialList() == null || order.getOrderMaterialList().isEmpty())) {
            return ErrorCode.RELET_ORDER_LIST_NOT_NULL;
        }
        if (StringUtil.isEmpty(order.getBuyerCustomerNo())) {
            return ErrorCode.SYSTEM_ERROR;
        }

        CustomerDO customerDO = customerMapper.findByNo(order.getBuyerCustomerNo());
        if (customerDO == null) {
            return ErrorCode.CUSTOMER_NOT_EXISTS;
        }

        // 判断逾期情况，如果客户存在未支付的逾期的结算单，不能产生新订单
        List<StatementOrderDO> overdueStatementOrderList = statementOrderSupport.getOverdueStatementOrderList(customerDO.getId());


        if (CollectionUtil.isNotEmpty(order.getOrderProductList())) {

            for (OrderProduct reletOrderProduct : order.getOrderProductList()) {

                intRentingCount += reletOrderProduct.getRentingProductCount() == null ? 0 : reletOrderProduct.getRentingProductCount();


                // 如果为长租，但凡有逾期，就不可以下单，如果为短租，在可用额度范围内，就可以下单
                Integer rentLengthType = OrderRentType.RENT_TYPE_MONTH.equals(order.getRentType()) && order.getRentTimeLength() >= CommonConstant.ORDER_RENT_TYPE_LONG_MIN ? OrderRentLengthType.RENT_LENGTH_TYPE_LONG : OrderRentLengthType.RENT_LENGTH_TYPE_SHORT;
                if (OrderRentLengthType.RENT_LENGTH_TYPE_LONG.equals(rentLengthType)
                        && CollectionUtil.isNotEmpty(overdueStatementOrderList)) {
                    return ErrorCode.CUSTOMER_HAVE_OVERDUE_STATEMENT_ORDER;
                }
            }
        }

        if (CollectionUtil.isNotEmpty(order.getOrderMaterialList())) {

            for (OrderMaterial reletOrderMaterial : order.getOrderMaterialList()) {

                intRentingCount += reletOrderMaterial.getRentingMaterialCount() == null ? 0 : reletOrderMaterial.getRentingMaterialCount();

                // 如果为长租，但凡有逾期，就不可以下单，如果为短租，在可用额度范围内，就可以下单
                if (OrderRentLengthType.RENT_LENGTH_TYPE_LONG.equals(order.getRentLengthType())
                        && CollectionUtil.isNotEmpty(overdueStatementOrderList)) {
                    return ErrorCode.CUSTOMER_HAVE_OVERDUE_STATEMENT_ORDER;
                }
            }
        }

        if (intRentingCount <= 0) {
            return ErrorCode.RELET_ORDER_RENT_COUNT_ERROR;
        }
        return verifyOrderShortRentReceivable(customerDO, ConverterUtil.convert(order, ReletOrderDO.class));
    }


    /**
     * 校验续租单提交时 合法性 （在租数量 不一致无法提交）
     *
     * @author ZhaoZiXuan
     * @date 2018/6/7 17:08
     * @param
     * @return
     */
    private String verifyReletOrderOperate(OrderDO orderDO, ReletOrderDO reletOrderDO) {

        if (orderDO == null || reletOrderDO == null) {
            return ErrorCode.SYSTEM_ERROR;
        }

        if (CollectionUtil.isNotEmpty(reletOrderDO.getReletOrderProductDOList())) {

            for (ReletOrderProductDO reletOrderProductDO : reletOrderDO.getReletOrderProductDOList()) {

                OrderProductDO orderProductDO = getOrderProductDOById(orderDO, reletOrderProductDO.getOrderProductId());
                if (!orderProductDO.getRentingProductCount().equals(reletOrderProductDO.getRentingProductCount())) {

                    return ErrorCode.RELET_ORDER_RENT_COUNT_ERROR;
                }

            }
        }

        if (CollectionUtil.isNotEmpty(reletOrderDO.getReletOrderMaterialDOList())) {

            for (ReletOrderMaterialDO reletOrderMaterialDO : reletOrderDO.getReletOrderMaterialDOList()) {

                OrderMaterialDO orderMaterial = getOrderMaterialDOById(orderDO, reletOrderMaterialDO.getOrderMaterialId());
                if (!orderMaterial.getRentingMaterialCount().equals(reletOrderMaterialDO.getRentingMaterialCount())) {

                    return ErrorCode.RELET_ORDER_RENT_COUNT_ERROR;
                }
            }
        }

        return ErrorCode.SUCCESS;
    }


    private String verifyOrderShortRentReceivable(CustomerDO customerDO, ReletOrderDO reletOrderDO) {

        Integer subCompanyId = reletOrderDO.getOrderSubCompanyId();
        subCompanyId = subCompanyId == null ? userSupport.getCurrentUserCompanyId() : subCompanyId;

        BigDecimal customerTotalShortRentReceivable = statementOrderSupport.getShortRentReceivable(customerDO.getId());
        //分公司的应收短期上线
        BigDecimal subCompanyTotalShortRentReceivable = statementOrderSupport.getSubCompanyShortRentReceivable(subCompanyId);
        if (BigDecimalUtil.compare(subCompanyTotalShortRentReceivable, BigDecimal.ZERO) < 0) {
            return ErrorCode.SHORT_RECEIVABLE_CALCULATE_FAIL;
        }
        //得到分公司设置的短期上线
        SubCompanyDO subCompanyDO = subCompanyMapper.findById(subCompanyId);
        BigDecimal shortLimitReceivableAmount = customerDO.getShortLimitReceivableAmount() == null ? new BigDecimal(Integer.MAX_VALUE) : customerDO.getShortLimitReceivableAmount();
        BigDecimal subCompanyShortLimitReceivableAmount = subCompanyDO.getShortLimitReceivableAmount() == null ? new BigDecimal(Integer.MAX_VALUE) : subCompanyDO.getShortLimitReceivableAmount();

        //续租无运费和押金
        if (CollectionUtil.isNotEmpty(reletOrderDO.getReletOrderProductDOList())) {
            for (ReletOrderProductDO reletOrderProductDO : reletOrderDO.getReletOrderProductDOList()) {
                if (OrderRentLengthType.RENT_LENGTH_TYPE_SHORT.equals(reletOrderDO.getRentLengthType())) {
                    BigDecimal thisTotalAmount = BigDecimalUtil.mul(BigDecimalUtil.mul(new BigDecimal(reletOrderProductDO.getProductCount()), reletOrderProductDO.getProductUnitAmount()), new BigDecimal(reletOrderDO.getRentTimeLength()));

                    customerTotalShortRentReceivable = BigDecimalUtil.add(customerTotalShortRentReceivable, thisTotalAmount);
                    if (BigDecimalUtil.compare(shortLimitReceivableAmount, customerTotalShortRentReceivable) < 0) {
                        return ErrorCode.CUSTOMER_SHORT_LIMIT_RECEIVABLE_OVERFLOW;
                    }
                    //检验分公司是否超出
                    subCompanyTotalShortRentReceivable = BigDecimalUtil.add(subCompanyTotalShortRentReceivable, thisTotalAmount);
                    if (BigDecimalUtil.compare(subCompanyShortLimitReceivableAmount, subCompanyTotalShortRentReceivable) < 0) {
                        return ErrorCode.SUB_COMPANY_SHORT_LIMIT_RECEIVABLE_OVERFLOW;
                    }
                }
            }
        }

        if (CollectionUtil.isNotEmpty(reletOrderDO.getReletOrderMaterialDOList())) {
            for (ReletOrderMaterialDO reletOrderMaterialDO : reletOrderDO.getReletOrderMaterialDOList()) {
                if (OrderRentLengthType.RENT_LENGTH_TYPE_SHORT.equals(reletOrderDO.getRentLengthType())) {
                    BigDecimal thisTotalAmount = BigDecimalUtil.mul(BigDecimalUtil.mul(new BigDecimal(reletOrderMaterialDO.getMaterialCount()), reletOrderMaterialDO.getMaterialUnitAmount()), new BigDecimal(reletOrderDO.getRentTimeLength()));

                    customerTotalShortRentReceivable = BigDecimalUtil.add(customerTotalShortRentReceivable, thisTotalAmount);
                    if (BigDecimalUtil.compare(shortLimitReceivableAmount, customerTotalShortRentReceivable) < 0) {
                        return ErrorCode.CUSTOMER_SHORT_LIMIT_RECEIVABLE_OVERFLOW;
                    }
                    //检验分公司是否超出
                    subCompanyTotalShortRentReceivable = BigDecimalUtil.add(subCompanyTotalShortRentReceivable, thisTotalAmount);
                    if (BigDecimalUtil.compare(subCompanyShortLimitReceivableAmount, subCompanyTotalShortRentReceivable) < 0) {
                        return ErrorCode.SUB_COMPANY_SHORT_LIMIT_RECEIVABLE_OVERFLOW;
                    }
                }
            }
        }

        return ErrorCode.SUCCESS;
    }

    /**
     * 续租时：若订单支付方式是首付百分比则修改续租单支付方式为先用后付
     *
     * @author ZhaoZiXuan
     * @date 2018/6/6 15:49
     * @param
     * @return
     */
    private String updateReletOrderPayModeOfBeforePercent(ReletOrderDO reletOrderDO){
        if (CollectionUtil.isNotEmpty(reletOrderDO.getReletOrderProductDOList())) {

            for (ReletOrderProductDO reletOrderProductDO : reletOrderDO.getReletOrderProductDOList()) {

                if (OrderPayMode.PAY_MODE_PAY_BEFORE_PERCENT.equals(reletOrderProductDO.getPayMode())){
                    reletOrderProductDO.setPayMode(OrderPayMode.PAY_MODE_PAY_AFTER);
                }
            }
        }

        if (CollectionUtil.isNotEmpty(reletOrderDO.getReletOrderMaterialDOList())) {

            for (ReletOrderMaterialDO reletOrderMaterialDO : reletOrderDO.getReletOrderMaterialDOList()) {

                if (OrderPayMode.PAY_MODE_PAY_BEFORE_PERCENT.equals(reletOrderMaterialDO.getPayMode())){
                    reletOrderMaterialDO.setPayMode(OrderPayMode.PAY_MODE_PAY_AFTER);
                }
            }
        }

        return ErrorCode.SUCCESS;
    }

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private UserSupport userSupport;

    @Autowired
    private OrderConsignInfoMapper orderConsignInfoMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private ReletOrderMapper reletOrderMapper;

    @Autowired
    private ReletOrderProductMapper reletOrderProductMapper;

    @Autowired
    private ReletOrderMaterialMapper reletOrderMaterialMapper;

    @Autowired
    private StatementOrderSupport statementOrderSupport;

    @Autowired
    private ProductService productService;

    @Autowired
    private MaterialService materialService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private SubCompanyMapper subCompanyMapper;

    @Autowired
    private CustomerRiskManagementMapper customerRiskManagementMapper;

    @Autowired
    private MaterialTypeMapper materialTypeMapper;

    @Autowired
    private GenerateNoSupport generateNoSupport;

    @Autowired
    private ProductSkuMapper productSkuMapper;

    @Autowired
    private MaterialMapper materialMapper;

    @Autowired
    private OrderTimeAxisSupport orderTimeAxisSupport;

    @Autowired
    private StatementService statementService;

    @Autowired
    private PermissionSupport permissionSupport;

    @Autowired
    private MessageThirdChannelService messageThirdChannelService;

    @Autowired
    private WorkflowService workflowService;

    @Autowired
    private K3Service k3Service;

    @Autowired
    private K3OrderStatementConfigMapper k3OrderStatementConfigMapper;
}
