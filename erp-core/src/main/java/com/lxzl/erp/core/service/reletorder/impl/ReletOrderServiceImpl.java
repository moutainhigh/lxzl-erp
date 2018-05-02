package com.lxzl.erp.core.service.reletorder.impl;

import com.lxzl.erp.common.constant.*;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.material.pojo.Material;
import com.lxzl.erp.common.domain.order.pojo.Order;
import com.lxzl.erp.common.domain.product.pojo.Product;
import com.lxzl.erp.common.domain.product.pojo.ProductSku;
import com.lxzl.erp.common.domain.reletorder.ReletOrderCommitParam;
import com.lxzl.erp.common.domain.reletorder.ReletOrderQueryParam;
import com.lxzl.erp.common.domain.reletorder.pojo.ReletOrder;
import com.lxzl.erp.common.domain.reletorder.pojo.ReletOrderMaterial;
import com.lxzl.erp.common.domain.reletorder.pojo.ReletOrderProduct;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.common.util.*;
import com.lxzl.erp.core.service.basic.impl.support.GenerateNoSupport;
import com.lxzl.erp.core.service.material.MaterialService;
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
import com.lxzl.erp.dataaccess.dao.mysql.material.MaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.material.MaterialTypeMapper;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductSkuMapper;
import com.lxzl.erp.dataaccess.dao.mysql.reletorder.ReletOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.reletorder.ReletOrderMaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.reletorder.ReletOrderProductMapper;
import com.lxzl.erp.dataaccess.domain.company.SubCompanyDO;
import com.lxzl.erp.dataaccess.domain.customer.CustomerDO;
import com.lxzl.erp.dataaccess.domain.customer.CustomerRiskManagementDO;
import com.lxzl.erp.dataaccess.domain.material.MaterialDO;
import com.lxzl.erp.dataaccess.domain.material.MaterialTypeDO;
import com.lxzl.erp.dataaccess.domain.product.ProductSkuDO;
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
import java.text.SimpleDateFormat;
import java.util.*;


@Service("reletOrderService")
public class ReletOrderServiceImpl implements ReletOrderService {

    private static Logger logger = LoggerFactory.getLogger(ReletOrderServiceImpl.class);

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, Integer> createReletOrder(Order order){
        ServiceResult<String, Integer> result = new ServiceResult<>();
        User loginUser = userSupport.getCurrentUser();
        Date currentTime = new Date();

        //查询订单信息
        ServiceResult<String, Order> orderServiceResult = orderService.queryOrderByNo(order.getOrderNo());
        if (!ErrorCode.SUCCESS.equals(orderServiceResult.getErrorCode())) {
            result.setErrorCode(orderServiceResult.getErrorCode());
            return result;
        }
        ReletOrder reletOrder = new ReletOrder(orderServiceResult.getResult());


        String verifyCreateOrderCode = verifyOperateOrder(reletOrder);
        if (!ErrorCode.SUCCESS.equals(verifyCreateOrderCode)) {
            result.setErrorCode(verifyCreateOrderCode);
            return result;
        }

        CustomerDO customerDO = customerMapper.findByNo(reletOrder.getBuyerCustomerNo());
        ReletOrderDO reletOrderDO = ConverterUtil.convert(reletOrder, ReletOrderDO.class);

        // 校验客户风控信息
        verifyCustomerRiskInfo(reletOrderDO);
        calculateReletOrderProductInfo(reletOrderDO.getReletOrderProductDOList(), reletOrderDO);
        calculateReletOrderMaterialInfo(reletOrderDO.getReletOrderMaterialDOList(), reletOrderDO);

        SubCompanyDO subCompanyDO = subCompanyMapper.findById(reletOrder.getDeliverySubCompanyId());
        if (reletOrder.getDeliverySubCompanyId() == null || subCompanyDO == null) {
            result.setErrorCode(ErrorCode.SUB_COMPANY_NOT_EXISTS);
            return result;
        }
        reletOrderDO.setOrderSubCompanyId(userSupport.getCurrentUserCompanyId());
        reletOrderDO.setDeliverySubCompanyId(reletOrder.getDeliverySubCompanyId());

        SubCompanyDO orderSubCompanyDO = subCompanyMapper.findById(reletOrderDO.getOrderSubCompanyId());
        reletOrderDO.setTotalOrderAmount(BigDecimalUtil.sub(BigDecimalUtil.add(reletOrderDO.getTotalProductAmount(), reletOrderDO.getTotalMaterialAmount()), reletOrderDO.getTotalDiscountAmount()));
        reletOrderDO.setReletOrderNo(generateNoSupport.generateReletOrderNo(currentTime, orderSubCompanyDO != null ? orderSubCompanyDO.getSubCompanyCode() : null));

        reletOrderDO.setOrderSellerId(customerDO.getOwner());

        //添加客户的结算时间（天）
        Date rentStartTime = reletOrder.getRentStartTime();
        Integer statementDate = customerDO.getStatementDate();

        //计算结算时间
        Integer statementDays = statementOrderSupport.getCustomerStatementDate(statementDate, rentStartTime);

        //获取
        reletOrderDO.setStatementDate(statementDays);
        reletOrderDO.setReletOrderStatus(ReletOrderStatus.RELET_ORDER_STATUS_RELET);
        reletOrderDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        reletOrderDO.setCreateUser(loginUser.getUserId().toString());
        reletOrderDO.setUpdateUser(loginUser.getUserId().toString());
        reletOrderDO.setCreateTime(currentTime);
        reletOrderDO.setUpdateTime(currentTime);
        //添加当前客户名称
        reletOrderDO.setBuyerCustomerName(customerDO.getCustomerName());

        Date expectReturnTime = generateExpectReturnTime(reletOrderDO);
        reletOrderDO.setExpectReturnTime(expectReturnTime);
        //保存续租单 商品项 配件项
        reletOrderMapper.save(reletOrderDO);
        saveReletOrderProductInfo(reletOrderDO.getReletOrderProductDOList(), reletOrderDO.getId(), loginUser, currentTime);
        saveReletOrderMaterialInfo(reletOrderDO.getReletOrderMaterialDOList(), reletOrderDO.getId(), loginUser, currentTime);

        // 续租单生成结算单 ，使用订单Id
        ServiceResult<String, BigDecimal> createStatementOrderResult = statementService.createReletOrderStatement(reletOrderDO);
        if (!ErrorCode.SUCCESS.equals(createStatementOrderResult.getErrorCode())) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            result.setErrorCode(createStatementOrderResult.getErrorCode());
            return result;
        }


        //TODO 续租时间轴

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(reletOrderDO.getId());
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
    public ServiceResult<String, ReletOrder> queryReletOrderDetailById(Integer reletOrderId){
        ServiceResult<String, ReletOrder> result = new ServiceResult<>();

        if (null == reletOrderId){
            result.setErrorCode(ErrorCode.RELET_ID_NOT_NULL);
            return result;
        }
        ReletOrderDO reletOrderDO = reletOrderMapper.findDetailByReletOrderId(reletOrderId);
        ReletOrder reletOrder = ConverterUtil.convert(reletOrderDO, ReletOrder.class);
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(reletOrder);
        return result;
    }


    //region 续租单提交以及审核流程
    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, String> commitReletOrder(ReletOrderCommitParam reletOrderCommitParam){
        ServiceResult<String, String> result = new ServiceResult<>();
        Date currentTime = new Date();
        User loginUser = userSupport.getCurrentUser();
        String reletOrderNo = reletOrderCommitParam.getReletOrderNo();
        Integer verifyUser = reletOrderCommitParam.getVerifyUser();
        String commitRemark = reletOrderCommitParam.getCommitRemark();
        ReletOrderDO reletOrderDO = reletOrderMapper.findByReletOrderNo(reletOrderNo);
        if (CollectionUtil.isEmpty(reletOrderDO.getReletOrderProductDOList())
                && CollectionUtil.isEmpty(reletOrderDO.getReletOrderMaterialDOList())) {
            result.setErrorCode(ErrorCode.RELET_ORDER_LIST_NOT_NULL);
            return result;
        }

        //只有创建订单本人可以提交
        if (!reletOrderDO.getCreateUser().equals(loginUser.getUserId().toString())) {
            result.setErrorCode(ErrorCode.COMMIT_ONLY_SELF);
            return result;
        }

        CustomerDO customerDO = customerMapper.findByNo(reletOrderDO.getBuyerCustomerNo());
        if (customerDO == null) {
            result.setErrorCode(ErrorCode.CUSTOMER_NOT_EXISTS);
            return result;
        }

        String verifyOrderShortRentReceivableResult = verifyOrderShortRentReceivable(customerDO, reletOrderDO);
        if (!ErrorCode.SUCCESS.equals(verifyOrderShortRentReceivableResult)) {
            result.setErrorCode(verifyOrderShortRentReceivableResult);
            return result;
        }


//        ServiceResult<String, Boolean> isNeedVerifyResult = isNeedVerify(reletOrderNo);
//        if (!ErrorCode.SUCCESS.equals(isNeedVerifyResult.getErrorCode())) {
//            result.setErrorCode(isNeedVerifyResult.getErrorCode(), isNeedVerifyResult.getFormatArgs());
//            return result;
//        }
//
//        // 是否需要审批
//        boolean isNeedVerify = isNeedVerifyResult.getResult();
//
//        String orderRemark = null;
//        if (OrderRentType.RENT_TYPE_DAY.equals(reletOrderDO.getRentType())) {
//            orderRemark = "租赁类型：天租";
//        } else if (OrderRentType.RENT_TYPE_MONTH.equals(reletOrderDO.getRentType())) {
//            orderRemark = "租赁类型：月租";
//        }
//
//        if (isNeedVerify) {
//            //如果要审核，判断审核注意事项
//            ServiceResult<String, String> verifyMattersResult = getVerifyMatters(reletOrderDO);
//            if (!ErrorCode.SUCCESS.equals(verifyMattersResult.getErrorCode())) {
//                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//                result.setErrorCode(verifyMattersResult.getErrorCode());
//                return result;
//            }
//            String verifyMatters = verifyMattersResult.getResult();
//
//            ServiceResult<String, String> workflowCommitResult = workflowService.commitWorkFlow(WorkflowType.WORKFLOW_TYPE_ORDER_INFO, reletOrderDO.getOrderNo(), verifyUser, verifyMatters, commitRemark, reletOrderCommitParam.getImgIdList(), orderRemark);
//            if (!ErrorCode.SUCCESS.equals(workflowCommitResult.getErrorCode())) {
//                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//                result.setErrorCode(workflowCommitResult.getErrorCode());
//                return result;
//            }
//            orderTimeAxisSupport.addOrderTimeAxis(reletOrderDO.getId(), reletOrderDO.getReletOrderStatus(), null, currentTime, loginUser.getUserId());
//        }
        //TODO 审批流程 & 续租时间轴


        //审核通过
        String code = receiveVerifyResult(true, reletOrderDO.getOrderNo());
        if (!ErrorCode.SUCCESS.equals(code)) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            result.setErrorCode(ErrorCode.SYSTEM_EXCEPTION);
            return result;
        }

        reletOrderDO.setReletOrderStatus(ReletOrderStatus.RELET_ORDER_STATUS_RELET);
        reletOrderDO.setUpdateUser(loginUser.getUserId().toString());
        reletOrderDO.setUpdateTime(currentTime);
        reletOrderMapper.update(reletOrderDO);

        result.setResult(reletOrderNo);
        result.setErrorCode(ErrorCode.SUCCESS);
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
                CustomerDO customerDO = customerMapper.findById(reletOrderDO.getBuyerCustomerId());
                // 审核通过时，对当前订单做短租应收额度校验
                String verifyOrderShortRentReceivableResult = verifyOrderShortRentReceivable(customerDO, reletOrderDO);
                if (!ErrorCode.SUCCESS.equals(verifyOrderShortRentReceivableResult)) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return verifyOrderShortRentReceivableResult;
                }

                reletOrderDO.setReletOrderStatus(ReletOrderStatus.RELET_ORDER_STATUS_RELET);
                // 续租单生成结算单 ，使用订单Id
                ServiceResult<String, BigDecimal> createStatementOrderResult = statementService.createReletOrderStatement(reletOrderDO);
                if (!ErrorCode.SUCCESS.equals(createStatementOrderResult.getErrorCode())) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return createStatementOrderResult.getErrorCode();
                }
                //reletOrderDO.setFirstNeedPayAmount(createStatementOrderResult.getResult());
                //orderTimeAxisSupport.addOrderTimeAxis(orderDO.getId(), orderDO.getOrderStatus(), null, currentTime, loginUser.getUserId());
                //TODO 续租单时间轴

                reletOrderDO.setUpdateTime(currentTime);
                reletOrderDO.setUpdateUser(loginUser.getUserId().toString());
                reletOrderMapper.update(reletOrderDO);
                //获取订单详细信息，发送给k3
                //Order order = queryOrderByNo(orderDO.getOrderNo()).getResult();
                //webServiceHelper.post(PostK3OperatorType.POST_K3_OPERATOR_TYPE_ADD, PostK3Type.POST_K3_TYPE_ORDER, order, true);
            } else {
                reletOrderDO.setReletOrderStatus(ReletOrderStatus.RELET_ORDER_STATUS_WAIT_COMMIT);
                //TODO 续租单时间轴

                //orderTimeAxisSupport.addOrderTimeAxis(orderDO.getId(), OrderStatus.ORDER_STATUS_REJECT, null, currentTime, loginUser.getUserId());
                reletOrderDO.setUpdateTime(currentTime);
                reletOrderDO.setUpdateUser(loginUser.getUserId().toString());
                reletOrderMapper.update(reletOrderDO);
            }


        } catch (Exception e) {
            e.printStackTrace();
            logger.error("审批订单通知失败： {} {}", businessNo, e.toString());
            return ErrorCode.BUSINESS_EXCEPTION;
        }
        return ErrorCode.SUCCESS;
    }


    /**
     * 审核注意事项
     *
     * @author ZhaoZiXuan
     * @date 2018/4/25 13:39
     * @param   reletOrderDO
     * @return
     */
    private ServiceResult<String, String> getVerifyMatters(ReletOrderDO reletOrderDO) {
        ServiceResult<String, String> result = new ServiceResult<>();

        String verifyProduct = null;
        BigDecimal productPrice = null;
        if (CollectionUtil.isNotEmpty(reletOrderDO.getReletOrderProductDOList())) {
            Integer count = 1;
            ReletOrderProductDO reletOrderProductDO;
            for (int i = 0; i < reletOrderDO.getReletOrderProductDOList().size(); i++) {
                reletOrderProductDO = reletOrderDO.getReletOrderProductDOList().get(i);
                ProductSkuDO productSkuDO = productSkuMapper.findById(reletOrderProductDO.getProductSkuId());
                if (productSkuDO == null) {
                    result.setErrorCode(ErrorCode.PRODUCT_IS_NULL_OR_NOT_EXISTS);
                    return result;
                }
                //得到
                if (OrderRentType.RENT_TYPE_DAY.equals(reletOrderDO.getRentType())) {
                    productPrice = CommonConstant.COMMON_CONSTANT_YES.equals(reletOrderProductDO.getIsNewProduct()) ? productSkuDO.getNewDayRentPrice() : productSkuDO.getDayRentPrice();
                } else if (OrderRentType.RENT_TYPE_MONTH.equals(reletOrderDO.getRentType())) {
                    productPrice = CommonConstant.COMMON_CONSTANT_YES.equals(reletOrderProductDO.getIsNewProduct()) ? productSkuDO.getNewMonthRentPrice() : productSkuDO.getMonthRentPrice();
                }
                // 订单价格低于商品租赁价
                if (BigDecimalUtil.compare(reletOrderProductDO.getProductUnitAmount(), productPrice) < 0) {
                    if (verifyProduct == null) {
                        if (OrderRentType.RENT_TYPE_DAY.equals(reletOrderDO.getRentType())) {
                            verifyProduct = CommonConstant.COMMON_CONSTANT_YES.equals(reletOrderProductDO.getIsNewProduct()) ?
                                    "商品项：" + count + "；租赁方式：天租，全新。商品名称：【" + reletOrderProductDO.getProductName() + "】，订单租赁价格：" + AmountUtil.getCommaFormat(reletOrderProductDO.getProductUnitAmount()) + "，预设租赁价格：" + AmountUtil.getCommaFormat(productPrice) + "。" :
                                    "商品项：" + count + "；租赁方式：天租，次新。商品名称：【" + reletOrderProductDO.getProductName() + "】，订单租赁价格：" + AmountUtil.getCommaFormat(reletOrderProductDO.getProductUnitAmount()) + "，预设租赁价格：" + AmountUtil.getCommaFormat(productPrice) + "。";
                        } else {
                            verifyProduct = CommonConstant.COMMON_CONSTANT_YES.equals(reletOrderProductDO.getIsNewProduct()) ?
                                    "商品项：" + count + "；租赁方式：月租，全新。商品名称：【" + reletOrderProductDO.getProductName() + "】，订单租赁价格：" + AmountUtil.getCommaFormat(reletOrderProductDO.getProductUnitAmount()) + "，预设租赁价格：" + AmountUtil.getCommaFormat(productPrice) + "。" :
                                    "商品项：" + count + "；租赁方式：月租，次新。商品名称：【" + reletOrderProductDO.getProductName() + "】，订单租赁价格：" + AmountUtil.getCommaFormat(reletOrderProductDO.getProductUnitAmount()) + "，预设租赁价格：" + AmountUtil.getCommaFormat(productPrice) + "。";
                        }
                    } else {
                        if (OrderRentType.RENT_TYPE_DAY.equals(reletOrderDO.getRentType())) {
                            verifyProduct = CommonConstant.COMMON_CONSTANT_YES.equals(reletOrderProductDO.getIsNewProduct()) ?
                                    "商品项：" + count + "；租赁方式：天租，全新。商品名称：【" + reletOrderProductDO.getProductName() + "】，订单租赁价格：" + AmountUtil.getCommaFormat(reletOrderProductDO.getProductUnitAmount()) + "，预设租赁价格：" + AmountUtil.getCommaFormat(productPrice) + "。" :
                                    "商品项：" + count + "；租赁方式：天租，次新。商品名称：【" + reletOrderProductDO.getProductName() + "】，订单租赁价格：" + AmountUtil.getCommaFormat(reletOrderProductDO.getProductUnitAmount()) + "，预设租赁价格：" + AmountUtil.getCommaFormat(productPrice) + "。";
                        } else {
                            verifyProduct = CommonConstant.COMMON_CONSTANT_YES.equals(reletOrderProductDO.getIsNewProduct()) ?
                                    verifyProduct + count + "；租赁方式：月租，全新。商品名称：【" + reletOrderProductDO.getProductName() + "】，订单租赁价格：" + AmountUtil.getCommaFormat(reletOrderProductDO.getProductUnitAmount()) + "，预设租赁价格：" + AmountUtil.getCommaFormat(productPrice) + "。" :
                                    verifyProduct + count + "；租赁方式：月租，次新。商品名称：【" + reletOrderProductDO.getProductName() + "】，订单租赁价格：" + AmountUtil.getCommaFormat(reletOrderProductDO.getProductUnitAmount()) + "，预设租赁价格：" + AmountUtil.getCommaFormat(productPrice) + "。";
                        }
                    }
                    count++;
                }
            }
        }

        String verifyMaterial = null;
        BigDecimal materialPrice = null;
        if (CollectionUtil.isNotEmpty(reletOrderDO.getReletOrderMaterialDOList())) {
            Integer count = 1;
            ReletOrderMaterialDO reletOrderMaterialDO;
            for (int i = 0; i < reletOrderDO.getReletOrderMaterialDOList().size(); i++) {
                reletOrderMaterialDO = reletOrderDO.getReletOrderMaterialDOList().get(i);
                MaterialDO materialDO = materialMapper.findById(reletOrderMaterialDO.getMaterialId());
                if (materialDO == null) {
                    result.setErrorCode(ErrorCode.MATERIAL_NOT_EXISTS);
                    return result;
                }
                if (OrderRentType.RENT_TYPE_DAY.equals(reletOrderDO.getRentType())) {
                    materialPrice = CommonConstant.COMMON_CONSTANT_YES.equals(reletOrderMaterialDO.getIsNewMaterial()) ? materialDO.getNewDayRentPrice() : materialDO.getDayRentPrice();
                } else if (OrderRentType.RENT_TYPE_MONTH.equals(reletOrderDO.getRentType())) {
                    materialPrice = CommonConstant.COMMON_CONSTANT_YES.equals(reletOrderMaterialDO.getIsNewMaterial()) ? materialDO.getNewMonthRentPrice() : materialDO.getMonthRentPrice();
                }
                // 订单价格低于商品租赁价
                if (BigDecimalUtil.compare(reletOrderMaterialDO.getMaterialUnitAmount(), materialPrice) < 0) {
                    if (verifyMaterial == null) {
                        if (OrderRentType.RENT_TYPE_DAY.equals(reletOrderDO.getRentType())) {
                            verifyMaterial = CommonConstant.COMMON_CONSTANT_YES.equals(reletOrderMaterialDO.getIsNewMaterial()) ?
                                    "配件项：" + count + "；租赁方式：天租，全新。【配件名称：" + reletOrderMaterialDO.getMaterialName() + "】，订单租赁价格：" + AmountUtil.getCommaFormat(reletOrderMaterialDO.getMaterialUnitAmount()) + "，预设租赁价格：" + AmountUtil.getCommaFormat(materialPrice) + "。" :
                                    "配件项：" + count + "；租赁方式：天租，次新。【配件名称：" + reletOrderMaterialDO.getMaterialName() + "】，订单租赁价格：" + AmountUtil.getCommaFormat(reletOrderMaterialDO.getMaterialUnitAmount()) + "，预设租赁价格：" + AmountUtil.getCommaFormat(materialPrice) + "。";
                        } else {
                            verifyMaterial = CommonConstant.COMMON_CONSTANT_YES.equals(reletOrderMaterialDO.getIsNewMaterial()) ?
                                    "配件项：" + count + "；租赁方式：月租，全新。【配件名称：" + reletOrderMaterialDO.getMaterialName() + "】，订单租赁价格：" + AmountUtil.getCommaFormat(reletOrderMaterialDO.getMaterialUnitAmount()) + "，预设租赁价格：" + AmountUtil.getCommaFormat(materialPrice) + "。" :
                                    "配件项：" + count + "；租赁方式：月租，次新。【配件名称：" + reletOrderMaterialDO.getMaterialName() + "】，订单租赁价格：" + AmountUtil.getCommaFormat(reletOrderMaterialDO.getMaterialUnitAmount()) + "，预设租赁价格：" + AmountUtil.getCommaFormat(materialPrice) + "。";
                        }
                    } else {
                        if (OrderRentType.RENT_TYPE_DAY.equals(reletOrderDO.getRentType())) {
                            verifyMaterial = CommonConstant.COMMON_CONSTANT_YES.equals(reletOrderMaterialDO.getIsNewMaterial()) ?
                                    verifyMaterial + count + "；租赁方式：天租，全新。【配件名称：" + reletOrderMaterialDO.getMaterialName() + "】，订单租赁价格：" + AmountUtil.getCommaFormat(reletOrderMaterialDO.getMaterialUnitAmount()) + "，预设租赁价格：" + AmountUtil.getCommaFormat(materialPrice) + "。" :
                                    verifyMaterial + count + "；租赁方式：天租，次新。【配件名称：" + reletOrderMaterialDO.getMaterialName() + "】，订单租赁价格：" + AmountUtil.getCommaFormat(reletOrderMaterialDO.getMaterialUnitAmount()) + "，预设租赁价格：" + AmountUtil.getCommaFormat(materialPrice) + "。";
                        } else {
                            verifyMaterial = CommonConstant.COMMON_CONSTANT_YES.equals(reletOrderMaterialDO.getIsNewMaterial()) ?
                                    verifyMaterial + count + "；租赁方式：月租，全新。【配件名称：" + reletOrderMaterialDO.getMaterialName() + "】，订单租赁价格：" + AmountUtil.getCommaFormat(reletOrderMaterialDO.getMaterialUnitAmount()) + "，预设租赁价格：" + AmountUtil.getCommaFormat(materialPrice) + "。" :
                                    verifyMaterial + count + "；租赁方式：月租，次新。【配件名称：" + reletOrderMaterialDO.getMaterialName() + "】，订单租赁价格：" + AmountUtil.getCommaFormat(reletOrderMaterialDO.getMaterialUnitAmount()) + "，预设租赁价格：" + AmountUtil.getCommaFormat(materialPrice) + "。";
                        }
                    }
                    count++;
                }
            }
        }
        String verifyMatters;
        if (verifyProduct == null) {
            verifyMatters = verifyMaterial;
        } else if (verifyMaterial == null) {
            verifyMatters = verifyProduct;
        } else {
            verifyMatters = verifyProduct + verifyMaterial;
        }

        result.setResult(verifyMatters);
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    public ServiceResult<String, Boolean> isNeedVerify(String reletOrderNo) {
        ServiceResult<String, Boolean> result = new ServiceResult<>();
        ReletOrderDO reletOrderDO = reletOrderMapper.findByReletOrderNo(reletOrderNo);
        if (reletOrderDO == null) {
            result.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return result;
        }
        if (!ReletOrderStatus.RELET_ORDER_STATUS_WAIT_COMMIT.equals(reletOrderDO.getReletOrderStatus())) {
            result.setErrorCode(ErrorCode.ORDER_STATUS_ERROR);
            return result;
        }

        Boolean isNeedVerify = false;
        if (CollectionUtil.isNotEmpty(reletOrderDO.getReletOrderProductDOList())) {
            for (ReletOrderProductDO reletOrderProductDO : reletOrderDO.getReletOrderProductDOList()) {
                ServiceResult<String, Product> productServiceResult = productService.queryProductBySkuId(reletOrderProductDO.getProductSkuId());
                if (!ErrorCode.SUCCESS.equals(productServiceResult.getErrorCode())) {
                    result.setErrorCode(productServiceResult.getErrorCode());
                    return result;
                }
                Product product = productServiceResult.getResult();
                if (product == null) {
                    result.setErrorCode(ErrorCode.PRODUCT_IS_NULL_OR_NOT_EXISTS);
                    return result;
                }
                ProductSku thisProductSku = CollectionUtil.isNotEmpty(product.getProductSkuList()) ? product.getProductSkuList().get(0) : null;
                if (thisProductSku == null) {
                    result.setErrorCode(ErrorCode.PRODUCT_SKU_IS_NULL_OR_NOT_EXISTS);
                    return result;
                }

                BigDecimal productUnitAmount = null;
                if (OrderRentType.RENT_TYPE_DAY.equals(reletOrderDO.getRentType())) {
                    productUnitAmount = CommonConstant.COMMON_CONSTANT_YES.equals(reletOrderProductDO.getIsNewProduct()) ? thisProductSku.getNewDayRentPrice() : thisProductSku.getDayRentPrice();
                } else if (OrderRentType.RENT_TYPE_MONTH.equals(reletOrderDO.getRentType())) {
                    productUnitAmount = CommonConstant.COMMON_CONSTANT_YES.equals(reletOrderProductDO.getIsNewProduct()) ? thisProductSku.getNewMonthRentPrice() : thisProductSku.getMonthRentPrice();
                }
                // 订单价格低于商品租赁价，需要商务审批
                if (BigDecimalUtil.compare(reletOrderProductDO.getProductUnitAmount(), productUnitAmount) < 0) {
                    isNeedVerify = true;
                    break;
                }
            }
        }

        if (CollectionUtil.isNotEmpty(reletOrderDO.getReletOrderMaterialDOList())) {
            for (ReletOrderMaterialDO reletOrderMaterialDO : reletOrderDO.getReletOrderMaterialDOList()) {
                ServiceResult<String, Material> materialServiceResult = materialService.queryMaterialById(reletOrderMaterialDO.getMaterialId());
                if (!ErrorCode.SUCCESS.equals(materialServiceResult.getErrorCode())) {
                    result.setErrorCode(materialServiceResult.getErrorCode());
                    return result;
                }
                Material material = materialServiceResult.getResult();
                if (material == null) {
                    result.setErrorCode(ErrorCode.MATERIAL_NOT_EXISTS);
                    return result;
                }

                BigDecimal materialUnitAmount = null;
                if (OrderRentType.RENT_TYPE_DAY.equals(reletOrderDO.getRentType())) {
                    materialUnitAmount = CommonConstant.COMMON_CONSTANT_YES.equals(reletOrderMaterialDO.getIsNewMaterial()) ? material.getNewDayRentPrice() : material.getDayRentPrice();
                } else if (OrderRentType.RENT_TYPE_MONTH.equals(reletOrderDO.getRentType())) {
                    materialUnitAmount = CommonConstant.COMMON_CONSTANT_YES.equals(reletOrderMaterialDO.getIsNewMaterial()) ? material.getNewMonthRentPrice() : material.getMonthRentPrice();
                }
                // 订单价格低于商品租赁价，需要商务审批
                if (BigDecimalUtil.compare(reletOrderMaterialDO.getMaterialUnitAmount(), materialUnitAmount) < 0) {
                    isNeedVerify = true;
                    break;
                }
            }
        }

        // 检查是否需要审批流程
        if (isNeedVerify) {
            ServiceResult<String, Boolean> isMeedVerifyResult = workflowService.isNeedVerify(WorkflowType.WORKFLOW_TYPE_ORDER_INFO);
            if (!ErrorCode.SUCCESS.equals(isMeedVerifyResult.getErrorCode())) {
                result.setErrorCode(isMeedVerifyResult.getErrorCode());
                return result;
            }
            if (!isMeedVerifyResult.getResult()) {
                result.setErrorCode(ErrorCode.WORKFLOW_HAVE_NO_CONFIG);
                return result;
            }
        }

        result.setResult(isNeedVerify);
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }
    //endregion


    private void saveReletOrderProductInfo(List<ReletOrderProductDO> reletOrderProductDOList, Integer reletOrderId, User loginUser, Date currentTime) {

        List<ReletOrderProductDO> saveOrderProductDOList = new ArrayList<>();
        Map<Integer, ReletOrderProductDO> updateOrderProductDOMap = new HashMap<>();
        List<ReletOrderProductDO> dbOrderProductDOList = reletOrderProductMapper.findByReletOrderId(reletOrderId);
        Map<Integer, ReletOrderProductDO> dbOrderProductDOMap = ListUtil.listToMap(dbOrderProductDOList, "id");
        if (CollectionUtil.isNotEmpty(reletOrderProductDOList)) {
            for (ReletOrderProductDO orderProductDO : reletOrderProductDOList) {

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
                orderProductDO.setReletOrderId(reletOrderId);
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
                orderProductDO.setReletOrderId(reletOrderId);
                orderProductDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                orderProductDO.setUpdateUser(loginUser.getUserId().toString());
                orderProductDO.setUpdateTime(currentTime);
                reletOrderProductMapper.update(orderProductDO);
            }
        }

        if (dbOrderProductDOMap.size() > 0) {
            for (Map.Entry<Integer, ReletOrderProductDO> entry : dbOrderProductDOMap.entrySet()) {
                ReletOrderProductDO orderProductDO = entry.getValue();
                orderProductDO.setReletOrderId(reletOrderId);
                orderProductDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
                orderProductDO.setUpdateUser(loginUser.getUserId().toString());
                orderProductDO.setUpdateTime(currentTime);
                reletOrderProductMapper.update(orderProductDO);
            }
        }
    }

    private void saveReletOrderMaterialInfo(List<ReletOrderMaterialDO> reletOrderMaterialDOList, Integer orderId, User loginUser, Date currentTime) {

        List<ReletOrderMaterialDO> saveOrderMaterialDOList = new ArrayList<>();
        Map<Integer, ReletOrderMaterialDO> updateOrderMaterialDOMap = new HashMap<>();
        List<ReletOrderMaterialDO> dbOrderMaterialDOList = reletOrderMaterialMapper.findByReletOrderId(orderId);
        Map<Integer, ReletOrderMaterialDO> dbOrderMaterialDOMap = ListUtil.listToMap(dbOrderMaterialDOList, "id");
        if (CollectionUtil.isNotEmpty(reletOrderMaterialDOList)) {
            for (ReletOrderMaterialDO orderMaterialDO : reletOrderMaterialDOList) {
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
                orderMaterialDO.setReletOrderId(orderId);
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
                orderMaterialDO.setReletOrderId(orderId);
                orderMaterialDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                orderMaterialDO.setUpdateUser(loginUser.getUserId().toString());
                orderMaterialDO.setUpdateTime(currentTime);
                reletOrderMaterialMapper.update(orderMaterialDO);
            }
        }

        if (dbOrderMaterialDOMap.size() > 0) {
            for (Map.Entry<Integer, ReletOrderMaterialDO> entry : dbOrderMaterialDOMap.entrySet()) {
                ReletOrderMaterialDO orderMaterialDO = entry.getValue();
                orderMaterialDO.setReletOrderId(orderId);
                orderMaterialDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
                orderMaterialDO.setUpdateUser(loginUser.getUserId().toString());
                orderMaterialDO.setUpdateTime(currentTime);
                reletOrderMaterialMapper.update(orderMaterialDO);
            }
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
        //CustomerRiskManagementDO customerRiskManagementDO = customerRiskManagementMapper.findByCustomerId(reletOrderDO.getBuyerCustomerId());
        if (reletOrderProductDOList != null && !reletOrderProductDOList.isEmpty()) {
            int productCount = 0;
            // 商品租赁总额
            BigDecimal productAmountTotal = new BigDecimal(0.0);


            for (ReletOrderProductDO reletOrderProductDO : reletOrderProductDOList) {

                String skuName = "";
                BigDecimal skuPrice = BigDecimal.ZERO;
                Product product = new Product();

                ServiceResult<String, Product> productServiceResult = productService.queryProductBySkuId(reletOrderProductDO.getProductSkuId());
                if (!ErrorCode.SUCCESS.equals(productServiceResult.getErrorCode())) {
                    throw new BusinessException(productServiceResult.getErrorCode());
                }
                product = productServiceResult.getResult();
                reletOrderProductDO.setProductName(product.getProductName());
                ProductSku productSku = CollectionUtil.isNotEmpty(product.getProductSkuList()) ? product.getProductSkuList().get(0) : null;
                if (productSku == null) {
                    throw new BusinessException(ErrorCode.PRODUCT_SKU_IS_NULL_OR_NOT_EXISTS);
                }
                skuPrice = CommonConstant.COMMON_CONSTANT_YES.equals(reletOrderProductDO.getIsNewProduct()) ? productSku.getNewSkuPrice() : productSku.getSkuPrice();
                skuName = productSku.getSkuName();


                reletOrderProductDO.setProductSkuName(skuName);
                reletOrderProductDO.setProductAmount(BigDecimalUtil.mul(BigDecimalUtil.mul(reletOrderProductDO.getProductUnitAmount(), new BigDecimal(reletOrderDO.getRentTimeLength()), 2), new BigDecimal(reletOrderProductDO.getProductCount())));
                reletOrderProductDO.setProductSkuSnapshot(FastJsonUtil.toJSONString(product));

                productCount += reletOrderProductDO.getProductCount();
                productAmountTotal = BigDecimalUtil.add(productAmountTotal, reletOrderProductDO.getProductAmount());
            }

            reletOrderDO.setTotalProductCount(productCount);
            reletOrderDO.setTotalProductAmount(productAmountTotal);
        }
    }


    private void calculateReletOrderMaterialInfo(List<ReletOrderMaterialDO> reletOrderMaterialDOList, ReletOrderDO reletOrderDO) {
        //CustomerRiskManagementDO customerRiskManagementDO = customerRiskManagementMapper.findByCustomerId(orderDO.getBuyerCustomerId());
        if (reletOrderMaterialDOList != null && !reletOrderMaterialDOList.isEmpty()) {
            int materialCount = 0;
            // 商品租赁总额
            BigDecimal materialAmountTotal = BigDecimal.ZERO;
            String materialName = "";
            Material material = new Material();

            for (ReletOrderMaterialDO reletOrderMaterialDO : reletOrderMaterialDOList) {

                ServiceResult<String, Material> materialServiceResult = materialService.queryMaterialById(reletOrderMaterialDO.getMaterialId());
                if (!ErrorCode.SUCCESS.equals(materialServiceResult.getErrorCode())) {
                    throw new BusinessException(materialServiceResult.getErrorCode());
                }
                material = materialServiceResult.getResult();
                if (material == null) {
                    throw new BusinessException(ErrorCode.MATERIAL_NOT_EXISTS);
                }
                MaterialTypeDO materialTypeDO = materialTypeMapper.findById(material.getMaterialType());
                reletOrderMaterialDO.setMaterialName(material.getMaterialName());
                materialName = material.getMaterialName();

                reletOrderMaterialDO.setMaterialName(StringUtil.isBlank(materialName) ? reletOrderMaterialDO.getMaterialName() : materialName);
                reletOrderMaterialDO.setMaterialAmount(BigDecimalUtil.mul(BigDecimalUtil.mul(reletOrderMaterialDO.getMaterialUnitAmount(), new BigDecimal(reletOrderDO.getRentTimeLength()), 2), new BigDecimal(reletOrderMaterialDO.getMaterialCount())));
                reletOrderMaterialDO.setMaterialSnapshot(FastJsonUtil.toJSONString(material));

                materialCount += reletOrderMaterialDO.getMaterialCount();
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


    private String verifyOperateOrder(ReletOrder reletOrder) {
        if (reletOrder == null) {
            return ErrorCode.PARAM_IS_NOT_NULL;
        }
        if ((reletOrder.getReletOrderProductList() == null || reletOrder.getReletOrderProductList().isEmpty())
                && (reletOrder.getReletOrderMaterialList() == null || reletOrder.getReletOrderMaterialList().isEmpty())) {
            return ErrorCode.RELET_ORDER_LIST_NOT_NULL;
        }

        if (reletOrder.getOrderId() == null){

            return ErrorCode.RELET_ORDER_ID_NOT_NULL;
        }

        CustomerDO customerDO = customerMapper.findByNo(reletOrder.getBuyerCustomerNo());
        if (customerDO == null) {
            return ErrorCode.CUSTOMER_NOT_EXISTS;
        }
        reletOrder.setBuyerCustomerId(customerDO.getId());

        // 判断逾期情况，如果客户存在未支付的逾期的结算单，不能产生新订单
        List<StatementOrderDO> overdueStatementOrderList = statementOrderSupport.getOverdueStatementOrderList(customerDO.getId());


        if (reletOrder.getRentStartTime() == null) {
            return ErrorCode.RELET_ORDER_HAVE_NO_RENT_START_TIME;
        }

        if (reletOrder.getRentType() == null) {
            return ErrorCode.RELET_ORDER_RENT_TYPE_IS_NULL;
        }
        if (reletOrder.getRentTimeLength() == null || reletOrder.getRentTimeLength() <= 0) {
            return ErrorCode.RELET_ORDER_RENT_TIME_LENGTH_IS_ZERO_OR_IS_NULL;
        }
        if (!OrderRentType.inThisScope(reletOrder.getRentType())) {
            return ErrorCode.RELET_ORDER_RENT_TYPE_OR_LENGTH_ERROR;
        }
        reletOrder.setRentLengthType(OrderRentType.RENT_TYPE_MONTH.equals(reletOrder.getRentType()) && reletOrder.getRentTimeLength() >= CommonConstant.ORDER_RENT_TYPE_LONG_MIN ? OrderRentLengthType.RENT_LENGTH_TYPE_LONG : OrderRentLengthType.RENT_LENGTH_TYPE_SHORT);

        if (CollectionUtil.isNotEmpty(reletOrder.getReletOrderProductList())) {

            for (ReletOrderProduct reletOrderProduct : reletOrder.getReletOrderProductList()) {

                if (reletOrderProduct.getProductCount() == null || reletOrderProduct.getProductCount() <= 0) {
                    return ErrorCode.RELET_ORDER_PRODUCT_COUNT_ERROR;
                }
                ServiceResult<String, Product> productServiceResult = productService.queryProductBySkuId(reletOrderProduct.getProductSkuId());
                if (!ErrorCode.SUCCESS.equals(productServiceResult.getErrorCode()) || productServiceResult.getResult() == null) {
                    return ErrorCode.PRODUCT_IS_NULL_OR_NOT_EXISTS;
                }
                Product product = productServiceResult.getResult();
                if (CommonConstant.COMMON_CONSTANT_NO.equals(product.getIsRent())) {
                    return ErrorCode.PRODUCT_IS_NOT_RENT;
                }
                if (CollectionUtil.isEmpty(product.getProductSkuList())) {
                    return ErrorCode.PRODUCT_SKU_IS_NULL_OR_NOT_EXISTS;
                }

                if (reletOrderProduct.getRentingProductCount() == null){
                    return ErrorCode.RELET_ORDER_RENTING_PRODUCT_COUNT_ERROR;
                }


                // 如果为长租，但凡有逾期，就不可以下单，如果为短租，在可用额度范围内，就可以下单
                Integer rentLengthType = OrderRentType.RENT_TYPE_MONTH.equals(reletOrder.getRentType()) && reletOrder.getRentTimeLength() >= CommonConstant.ORDER_RENT_TYPE_LONG_MIN ? OrderRentLengthType.RENT_LENGTH_TYPE_LONG : OrderRentLengthType.RENT_LENGTH_TYPE_SHORT;
                if (OrderRentLengthType.RENT_LENGTH_TYPE_LONG.equals(rentLengthType)
                        && CollectionUtil.isNotEmpty(overdueStatementOrderList)) {
                    return ErrorCode.CUSTOMER_HAVE_OVERDUE_STATEMENT_ORDER;
                }
            }
        }

        if (CollectionUtil.isNotEmpty(reletOrder.getReletOrderMaterialList())) {

            for (ReletOrderMaterial reletOrderMaterial : reletOrder.getReletOrderMaterialList()) {

                if (reletOrderMaterial.getMaterialCount() == null || reletOrderMaterial.getMaterialCount() <= 0) {
                    return ErrorCode.RELET_ORDER_MATERIAL_COUNT_ERROR;
                }
                if (reletOrderMaterial.getMaterialId() == null) {
                    return ErrorCode.MATERIAL_NOT_EXISTS;
                }
                ServiceResult<String, Material> materialServiceResult = materialService.queryMaterialById(reletOrderMaterial.getMaterialId());

                if (!ErrorCode.SUCCESS.equals(materialServiceResult.getErrorCode())
                        || materialServiceResult.getResult() == null) {
                    return ErrorCode.MATERIAL_NOT_EXISTS;
                }
                if (reletOrderMaterial.getMaterialUnitAmount() == null || BigDecimalUtil.compare(reletOrderMaterial.getMaterialUnitAmount(), BigDecimal.ZERO) < 0) {
                    return ErrorCode.RELET_ORDER_MATERIAL_AMOUNT_ERROR;
                }

                if (reletOrderMaterial.getRentingMaterialCount() == null){
                    return ErrorCode.RELET_ORDER_RENTING_MATERIAL_COUNT_ERROR;
                }


                Material material = materialServiceResult.getResult();

                // 如果为长租，但凡有逾期，就不可以下单，如果为短租，在可用额度范围内，就可以下单
                if (OrderRentLengthType.RENT_LENGTH_TYPE_LONG.equals(reletOrder.getRentLengthType())
                        && CollectionUtil.isNotEmpty(overdueStatementOrderList)) {
                    return ErrorCode.CUSTOMER_HAVE_OVERDUE_STATEMENT_ORDER;
                }
            }
        }
        return verifyOrderShortRentReceivable(customerDO, ConverterUtil.convert(reletOrder, ReletOrderDO.class));
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


    @Autowired
    private UserSupport userSupport;

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
    private WorkflowService workflowService;

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
}
