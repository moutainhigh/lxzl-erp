package com.lxzl.erp.core.service.k3.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lxzl.erp.common.constant.*;
import com.lxzl.erp.common.domain.ApplicationConfig;
import com.lxzl.erp.common.domain.K3Config;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.customer.pojo.Customer;
import com.lxzl.erp.common.domain.k3.K3ReturnOrderCommitParam;
import com.lxzl.erp.common.domain.k3.OrderForReturnQueryParam;
import com.lxzl.erp.common.domain.k3.pojo.order.Order;
import com.lxzl.erp.common.domain.k3.pojo.order.OrderMaterial;
import com.lxzl.erp.common.domain.k3.pojo.order.OrderProduct;
import com.lxzl.erp.common.domain.k3.pojo.returnOrder.K3HistoricalReturnOrder;
import com.lxzl.erp.common.domain.k3.pojo.returnOrder.K3ReturnOrder;
import com.lxzl.erp.common.domain.k3.pojo.returnOrder.K3ReturnOrderDetail;
import com.lxzl.erp.common.domain.k3.pojo.returnOrder.K3ReturnOrderQueryParam;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.common.util.ConverterUtil;
import com.lxzl.erp.common.util.ListUtil;
import com.lxzl.erp.common.util.http.client.HttpClientUtil;
import com.lxzl.erp.common.util.http.client.HttpHeaderBuilder;
import com.lxzl.erp.core.k3WebServiceSdk.ERPServer_Models.FormSEOutStock;
import com.lxzl.erp.core.k3WebServiceSdk.ErpServer.ERPServiceLocator;
import com.lxzl.erp.core.k3WebServiceSdk.ErpServer.IERPService;
import com.lxzl.erp.core.service.customer.CustomerService;
import com.lxzl.erp.core.service.dingding.DingDingSupport.DingDingSupport;
import com.lxzl.erp.core.service.k3.K3CallbackService;
import com.lxzl.erp.core.service.k3.K3ReturnOrderService;
import com.lxzl.erp.core.service.k3.K3Service;
import com.lxzl.erp.core.service.k3.PostK3ServiceManager;
import com.lxzl.erp.core.service.k3.converter.ConvertK3DataService;
import com.lxzl.erp.core.service.permission.PermissionSupport;
import com.lxzl.erp.core.service.product.impl.support.ProductSupport;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.core.service.workflow.WorkflowService;
import com.lxzl.erp.dataaccess.dao.mysql.company.SubCompanyMapper;
import com.lxzl.erp.dataaccess.dao.mysql.k3.*;
import com.lxzl.erp.dataaccess.dao.mysql.material.MaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.OrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.OrderMaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.OrderProductMapper;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductMapper;
import com.lxzl.erp.dataaccess.domain.company.SubCompanyDO;
import com.lxzl.erp.dataaccess.domain.k3.*;
import com.lxzl.erp.dataaccess.domain.k3.returnOrder.K3ReturnOrderDO;
import com.lxzl.erp.dataaccess.domain.k3.returnOrder.K3ReturnOrderDetailDO;
import com.lxzl.erp.dataaccess.domain.material.MaterialDO;
import com.lxzl.erp.dataaccess.domain.order.OrderDO;
import com.lxzl.erp.dataaccess.domain.order.OrderMaterialDO;
import com.lxzl.erp.dataaccess.domain.order.OrderProductDO;
import com.lxzl.erp.dataaccess.domain.product.ProductDO;
import com.lxzl.se.common.exception.BusinessException;
import com.lxzl.se.common.util.CommonUtil;
import com.lxzl.se.common.util.StringUtil;
import com.lxzl.se.common.util.date.DateUtil;
import com.lxzl.se.dataaccess.mysql.config.PageQuery;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author: your name
 * @Description：
 * @Date: Created in 20:01 2018/4/10
 * @Modified By:
 */
@Service("k3ReturnOrderService")
public class K3ReturnOrderServiceImpl implements K3ReturnOrderService {

    private static final Logger logger = LoggerFactory.getLogger(K3ReturnOrderServiceImpl.class);

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, String> createReturnOrder(K3ReturnOrder k3ReturnOrder) {
        ServiceResult<String, String> result = new ServiceResult<>();
        User loginUser = userSupport.getCurrentUser();
        Date currentTime = new Date();
        if (k3ReturnOrder == null) {
            result.setErrorCode(ErrorCode.PARAM_IS_NOT_NULL);
            return result;
        }
        //退货日期不能小于三月五号
        Calendar calendar = Calendar.getInstance();
        calendar.set(2018, 2, 5, 0, 0, 0);
        Date minDate = calendar.getTime();
        if (minDate.compareTo(k3ReturnOrder.getReturnTime()) > 0) {
            result.setErrorCode(ErrorCode.RETURN_TIME_LESS_MIN_TIME);
            return result;
        }
        //发货分公司检查
        SubCompanyDO subCompanyDO = subCompanyMapper.findById(k3ReturnOrder.getDeliverySubCompanyId());
        if (subCompanyDO == null) {
            result.setErrorCode(ErrorCode.DELIVERY_COMPANY_NOT_EXIT);
            return result;
        }

        //       //商品物料唯一性校验
//        Set<String> primaryKeySet = new HashSet<String>();
//        for (K3ReturnOrderDetail k3ReturnOrderDetail : k3ReturnOrder.getK3ReturnOrderDetailList()) {
//            primaryKeySet.add(k3ReturnOrderDetail.getOrderItemId() + "_" + k3ReturnOrderDetail.getProductNo());
//        }
//        if (primaryKeySet.size() < k3ReturnOrder.getK3ReturnOrderDetailList().size()) {
//            result.setErrorCode(ErrorCode.HAS_SAME_PRODUCT);
//            return result;
//        }
        //itemId校验
        if (!varifyOrderItemId(k3ReturnOrder.getK3ReturnOrderDetailList())) {
            result.setErrorCode(ErrorCode.PRODUCT_IS_NULL_OR_NOT_EXISTS);
            return result;
        }

        ServiceResult<String, Customer> customerResult = customerService.queryCustomerByNo(k3ReturnOrder.getK3CustomerNo());
        if (!ErrorCode.SUCCESS.equals(customerResult.getErrorCode())) {
            result.setErrorCode(customerResult.getErrorCode());
            return result;
        }
        Customer customer = customerResult.getResult();
        k3ReturnOrder.setK3CustomerName(customer.getCustomerName());

        K3ReturnOrderDO k3ReturnOrderDO = ConverterUtil.convert(k3ReturnOrder, K3ReturnOrderDO.class);

        k3ReturnOrderDO.setReturnOrderNo("LXK3RO" + DateUtil.formatDate(currentTime, "yyyyMMddHHmmssSSS"));
        k3ReturnOrderDO.setReturnOrderStatus(ReturnOrderStatus.RETURN_ORDER_STATUS_WAIT_COMMIT);
        k3ReturnOrderDO.setSuccessStatus(CommonConstant.COMMON_CONSTANT_NO);
        k3ReturnOrderDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        k3ReturnOrderDO.setCreateTime(currentTime);
        k3ReturnOrderDO.setCreateUser(loginUser.getUserId().toString());
        k3ReturnOrderDO.setUpdateTime(currentTime);
        k3ReturnOrderDO.setUpdateUser(loginUser.getUserId().toString());
        if (k3ReturnOrderDO.getLogisticsAmount() == null) k3ReturnOrderDO.setLogisticsAmount(BigDecimal.ZERO);
        if (k3ReturnOrderDO.getServiceAmount() == null) k3ReturnOrderDO.setServiceAmount(BigDecimal.ZERO);
        k3ReturnOrderMapper.save(k3ReturnOrderDO);
        if (CollectionUtil.isNotEmpty(k3ReturnOrder.getK3ReturnOrderDetailList())) {
            Map<String, Order> orderCatch = new HashMap<String, Order>();
            for (K3ReturnOrderDetail k3ReturnOrderDetail : k3ReturnOrder.getK3ReturnOrderDetailList()) {
                //添加退货商品数量不能小于零的校验
                if (k3ReturnOrderDetail.getProductCount()<= 0) {
                    result.setErrorCode(ErrorCode.RETURN_COUNT_ERROR);
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    return result;
                }
                if (!orderCatch.containsKey(k3ReturnOrderDetail.getOrderNo())) {
                    ServiceResult<String, Order> serviceResult = k3Service.queryOrder(k3ReturnOrderDetail.getOrderNo());
                    if (!ErrorCode.SUCCESS.equals(serviceResult.getErrorCode())) {
                        result.setErrorCode(ErrorCode.ORDER_NOT_EXISTS);
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                        return result;
                    }
                    Order order = serviceResult.getResult();
                    //退货日期不能大于起租日期
                    if (order.getRentStartTime().compareTo(k3ReturnOrderDO.getReturnTime()) > 0) {
                        result.setErrorCode(ErrorCode.RETURN_TIME_LESS_RENT_TIME);
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                        return result;
                    }
                    orderCatch.put(k3ReturnOrderDetail.getOrderNo(), order);
                }


                K3ReturnOrderDetailDO k3ReturnOrderDetailDO = ConverterUtil.convert(k3ReturnOrderDetail, K3ReturnOrderDetailDO.class);
                k3ReturnOrderDetailDO.setReturnOrderId(k3ReturnOrderDO.getId());
                k3ReturnOrderDetailDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                k3ReturnOrderDetailDO.setCreateTime(currentTime);
                k3ReturnOrderDetailDO.setCreateUser(loginUser.getUserId().toString());
                k3ReturnOrderDetailDO.setUpdateTime(currentTime);
                k3ReturnOrderDetailDO.setUpdateUser(loginUser.getUserId().toString());
                k3ReturnOrderDetailMapper.save(k3ReturnOrderDetailDO);
            }
        }
        result.setResult(k3ReturnOrderDO.getReturnOrderNo());
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    public ServiceResult<String, String> addReturnOrder(K3ReturnOrder k3ReturnOrder) {

        ServiceResult<String, String> result = new ServiceResult<>();
        User loginUser = userSupport.getCurrentUser();
        Date currentTime = new Date();
        K3ReturnOrderDO k3ReturnOrderDO = k3ReturnOrderMapper.findByNo(k3ReturnOrder.getReturnOrderNo());
        if (k3ReturnOrderDO == null) {
            result.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return result;
        }
        List<K3ReturnOrderDetail> k3ReturnOrderDetailList = k3ReturnOrder.getK3ReturnOrderDetailList();
        if (CollectionUtil.isEmpty(k3ReturnOrderDetailList)) {
            result.setErrorCode(ErrorCode.PARAM_IS_NOT_ENOUGH);
            return result;
        }
        if (!ReturnOrderStatus.RETURN_ORDER_STATUS_WAIT_COMMIT.equals(k3ReturnOrderDO.getReturnOrderStatus())
                && !ReturnOrderStatus.RETURN_ORDER_STATUS_BACKED.equals(k3ReturnOrderDO.getReturnOrderStatus())) {
            result.setErrorCode(ErrorCode.K3_RETURN_ORDER_STATUS_CAN_NOT_OPERATE);
            return result;
        }
//        //添加商品时，重复性校验
//        Set<String> orientProductKeys = new HashSet<String>();
//        for (K3ReturnOrderDetail orderDetail : k3ReturnOrderDetailList)
//            orientProductKeys.add(orderDetail.getOrderItemId() + "_" + orderDetail.getProductNo());
//        List<K3ReturnOrderDetailDO> orderDetailList = k3ReturnOrderDetailMapper.findListByReturnOrderId(k3ReturnOrderDO.getId());
//        if (CollectionUtil.isNotEmpty(orderDetailList)) for (K3ReturnOrderDetailDO orderDetail : orderDetailList)
//            orientProductKeys.add(orderDetail.getOrderItemId() + "_" + orderDetail.getProductNo());
//        if (orientProductKeys.size() < k3ReturnOrderDetailList.size() + (CollectionUtil.isNotEmpty(orderDetailList) ? orderDetailList.size() : 0)) {
//            result.setErrorCode(ErrorCode.HAS_SAME_PRODUCT);
//            return result;
//        }
        //itemId校验
        if (!varifyOrderItemId(k3ReturnOrder.getK3ReturnOrderDetailList())) {
            result.setErrorCode(ErrorCode.PRODUCT_IS_NULL_OR_NOT_EXISTS);
            return result;
        }
        for (K3ReturnOrderDetail k3ReturnOrderDetail : k3ReturnOrder.getK3ReturnOrderDetailList()) {
            K3ReturnOrderDetailDO k3ReturnOrderDetailDO = ConverterUtil.convert(k3ReturnOrderDetail, K3ReturnOrderDetailDO.class);
            k3ReturnOrderDetailDO.setReturnOrderId(k3ReturnOrderDO.getId());
            k3ReturnOrderDetailDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
            k3ReturnOrderDetailDO.setCreateTime(currentTime);
            k3ReturnOrderDetailDO.setCreateUser(loginUser.getUserId().toString());
            k3ReturnOrderDetailDO.setUpdateTime(currentTime);
            k3ReturnOrderDetailDO.setUpdateUser(loginUser.getUserId().toString());
            k3ReturnOrderDetailMapper.save(k3ReturnOrderDetailDO);
        }

        result.setResult(k3ReturnOrderDO.getReturnOrderNo());
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    private boolean varifyOrderItemId(List<K3ReturnOrderDetail> k3ReturnOrderDetailList) {
        if (CollectionUtil.isNotEmpty(k3ReturnOrderDetailList)) {
            for (K3ReturnOrderDetail k3ReturnOrderDetail : k3ReturnOrderDetailList) {
                OrderDO orderDO = orderMapper.findByOrderNo(k3ReturnOrderDetail.getOrderNo());
                if (orderDO == null || CommonConstant.COMMON_CONSTANT_YES.equals(orderDO.getIsK3Order()))
                    continue;//如果为k3数据则不验证
                if (productSupport.isMaterial(k3ReturnOrderDetail.getProductNo())) {
                    OrderMaterialDO orderMaterialDO = orderMaterialMapper.findById(Integer.parseInt(k3ReturnOrderDetail.getOrderItemId()));
                    if (orderMaterialDO != null) continue;
                    return false;
                } else {
                    OrderProductDO orderProductDO = orderProductMapper.findById(Integer.parseInt(k3ReturnOrderDetail.getOrderItemId()));
                    if (orderProductDO != null) continue;
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public ServiceResult<String, String> deleteReturnOrder(Integer k3ReturnOrderDetailId) {
        ServiceResult<String, String> result = new ServiceResult<>();
        User loginUser = userSupport.getCurrentUser();
        Date currentTime = new Date();
        K3ReturnOrderDetailDO k3ReturnOrderDetailDO = k3ReturnOrderDetailMapper.findById(k3ReturnOrderDetailId);
        if (k3ReturnOrderDetailDO == null) {
            result.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return result;
        }
        K3ReturnOrderDO k3ReturnOrderDO = k3ReturnOrderMapper.findById(k3ReturnOrderDetailDO.getReturnOrderId());
        if (!ReturnOrderStatus.RETURN_ORDER_STATUS_WAIT_COMMIT.equals(k3ReturnOrderDO.getReturnOrderStatus())
                && !ReturnOrderStatus.RETURN_ORDER_STATUS_BACKED.equals(k3ReturnOrderDO.getReturnOrderStatus())) {
            result.setErrorCode(ErrorCode.K3_RETURN_ORDER_STATUS_CAN_NOT_OPERATE);
            return result;
        }
        //退货单商品项不能全部删除（校验至少一个商品项）
        List<K3ReturnOrderDetailDO> orderDetailList = k3ReturnOrderDetailMapper.findListByReturnOrderId(k3ReturnOrderDO.getId());
        if (CollectionUtil.isEmpty(orderDetailList)) {
            result.setErrorCode(ErrorCode.RETURN_DETAIL_LIST_NOT_NULL);
            return result;
        }
        if (orderDetailList.size() <= 1) {
            result.setErrorCode(ErrorCode.PRODUCT_ITEM_ALL_DELETE);
            return result;
        }

        k3ReturnOrderDetailDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
        k3ReturnOrderDetailDO.setUpdateTime(currentTime);
        k3ReturnOrderDetailDO.setUpdateUser(loginUser.getUserId().toString());
        k3ReturnOrderDetailMapper.update(k3ReturnOrderDetailDO);

        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    public ServiceResult<String, String> sendReturnOrderToK3(String returnOrderNo) {
        ServiceResult<String, String> result = new ServiceResult<>();
        User loginUser = userSupport.getCurrentUser();
        Date currentTime = new Date();

        K3ReturnOrderDO k3ReturnOrderDO = k3ReturnOrderMapper.findByNo(returnOrderNo);
        if (k3ReturnOrderDO == null) {
            result.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return result;
        }
        K3SendRecordDO k3SendRecordDO = k3SendRecordMapper.findByReferIdAndType(k3ReturnOrderDO.getId(), PostK3Type.POST_K3_TYPE_RETURN_ORDER);
        K3ReturnOrder k3ReturnOrder = ConverterUtil.convert(k3ReturnOrderDO, K3ReturnOrder.class);
        if (k3SendRecordDO == null) {
            //创建推送记录，此时发送状态失败，接收状态失败
            k3SendRecordDO = new K3SendRecordDO();
            k3SendRecordDO.setRecordType(PostK3Type.POST_K3_TYPE_RETURN_ORDER);
            k3SendRecordDO.setSendResult(CommonConstant.COMMON_CONSTANT_NO);
            k3SendRecordDO.setReceiveResult(CommonConstant.COMMON_CONSTANT_NO);
            k3SendRecordDO.setRecordJson(JSON.toJSONString(k3ReturnOrder));
            k3SendRecordDO.setSendTime(new Date());
            k3SendRecordDO.setRecordReferId(k3SendRecordDO.getId());
            k3SendRecordMapper.save(k3SendRecordDO);
            logger.info("【推送消息】" + JSON.toJSONString(k3ReturnOrder));
        }

        ServiceResult<String, String> serviceResult = sendReturnOrderToK3Method(k3ReturnOrder,k3SendRecordDO);

        if (ErrorCode.K3_SERVER_ERROR.equals(serviceResult.getErrorCode())) {
            result.setErrorCode(ErrorCode.K3_SERVER_ERROR);
            return result;
        }

        if (ErrorCode.SUCCESS.equals(serviceResult.getErrorCode())) {
            k3ReturnOrderDO.setReturnOrderStatus(ReturnOrderStatus.RETURN_ORDER_STATUS_PROCESSING);
            k3ReturnOrderDO.setUpdateTime(currentTime);
            k3ReturnOrderDO.setUpdateUser(loginUser.getUserId().toString());
            k3ReturnOrderMapper.update(k3ReturnOrderDO);
            result.setErrorCode(ErrorCode.SUCCESS);
            return result;
        }

        return result;
    }

    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, String> sendReturnOrderToK3Method(K3ReturnOrder k3ReturnOrder,K3SendRecordDO k3SendRecordDO) {
        ServiceResult<String, String> result = new ServiceResult<>();
        com.lxzl.erp.core.k3WebServiceSdk.ERPServer_Models.ServiceResult response = null;
        try {
            ConvertK3DataService convertK3DataService = postK3ServiceManager.getService(PostK3Type.POST_K3_TYPE_RETURN_ORDER);
            Object postData = convertK3DataService.getK3PostWebServiceData(null, k3ReturnOrder);
            IERPService service = new ERPServiceLocator().getBasicHttpBinding_IERPService();
            response = service.addSEOutstock((FormSEOutStock) postData);
            //修改推送记录
            if (response == null) {
                k3SendRecordDO.setReceiveResult(CommonConstant.COMMON_CONSTANT_NO);
                logger.info("【PUSH DATA TO K3 RESPONSE FAIL】 ： " + JSON.toJSONString(response));
                dingDingSupport.dingDingSendMessage(getErrorMessage(response, k3SendRecordDO));
                result.setErrorCode(ErrorCode.K3_SERVER_ERROR);
                return result;
            } else if (response.getStatus() != 0) {
                k3SendRecordDO.setReceiveResult(CommonConstant.COMMON_CONSTANT_NO);
                logger.info("【PUSH DATA TO K3 RESPONSE FAIL】 ： " + JSON.toJSONString(response));
                dingDingSupport.dingDingSendMessage(getErrorMessage(response, k3SendRecordDO));
                throw new BusinessException(response.getResult());
            } else {
                k3SendRecordDO.setReceiveResult(CommonConstant.COMMON_CONSTANT_YES);
//                if (response.getData() != null && response.getData().length > 0) {
//                    Map<String, String> map = new HashMap<>();
//                    for (ResultData resultData : response.getData()) {
//                        map.put(resultData.getKey(), resultData.getValue());
//                    }
//                    if (map.containsKey("EQAmount")) {
//                        //恢复信用额度
//                        BigDecimal b = new BigDecimal(Double.parseDouble(map.get("EQAmount")));
//                        if (BigDecimalUtil.compare(b, BigDecimal.ZERO) != 0) {
//                            K3MappingCustomerDO k3MappingCustomerDO = k3MappingCustomerMapper.findByK3Code(k3ReturnOrderDO.getK3CustomerNo());
//                            CustomerDO customerDO = customerMapper.findByNo(k3MappingCustomerDO.getErpCustomerCode());
//                            customerSupport.subCreditAmountUsed(customerDO.getId(), b);
//                        }
//                    }
//                }
                logger.info("【PUSH DATA TO K3 RESPONSE SUCCESS】 ： " + JSON.toJSONString(response));
            }
            k3SendRecordDO.setSendResult(CommonConstant.COMMON_CONSTANT_YES);
            k3SendRecordDO.setResponseJson(JSON.toJSONString(response));
            k3SendRecordMapper.update(k3SendRecordDO);
            logger.info("【返回结果】" + response);
        } catch (Exception e) {
            dingDingSupport.dingDingSendMessage(getErrorMessage(response, k3SendRecordDO));
            //将K3返回的具体错误信息返回，不返回自己定义的K3退货失败
            throw new BusinessException(response.getResult());
        }
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    public ServiceResult<String, Page<K3ReturnOrder>> queryReturnOrder(K3ReturnOrderQueryParam k3ReturnOrderQueryParam) {
        ServiceResult<String, Page<K3ReturnOrder>> result = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(k3ReturnOrderQueryParam.getPageNo(), k3ReturnOrderQueryParam.getPageSize());

        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        maps.put("k3ReturnOrderQueryParam", k3ReturnOrderQueryParam);
        maps.put("permissionParam", permissionSupport.getPermissionParam(PermissionType.PERMISSION_TYPE_SUB_COMPANY_FOR_BUSINESS,PermissionType.PERMISSION_TYPE_USER));

        Integer totalCount = k3ReturnOrderMapper.listCount(maps);
        List<K3ReturnOrderDO> orderDOList = k3ReturnOrderMapper.listPage(maps);
        List<K3ReturnOrder> orderList = ConverterUtil.convertList(orderDOList, K3ReturnOrder.class);
        Page<K3ReturnOrder> page = new Page<>(orderList, totalCount, k3ReturnOrderQueryParam.getPageNo(), k3ReturnOrderQueryParam.getPageSize());
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(page);
        return result;
    }

    @Override
    public ServiceResult<String, K3ReturnOrder> queryReturnOrderByNo(String returnOrderNo) {
        ServiceResult<String, K3ReturnOrder> result = new ServiceResult<>();

        K3ReturnOrderDO k3ReturnOrderDO = k3ReturnOrderMapper.findByNo(returnOrderNo);
        if (k3ReturnOrderDO == null) {
            result.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return result;
        }
        K3ReturnOrder k3ReturnOrder= ConverterUtil.convert(k3ReturnOrderDO, K3ReturnOrder.class);
        //增加退货单订单商品项和物料项
        List<K3ReturnOrderDetail> k3ReturnOrderDetailList=k3ReturnOrder.getK3ReturnOrderDetailList();
        if(CollectionUtil.isNotEmpty(k3ReturnOrderDetailList)){
            for(K3ReturnOrderDetail k3ReturnOrderDetail:k3ReturnOrderDetailList){
                boolean isMaterial=productSupport.isMaterial(k3ReturnOrderDetail.getProductNo());
                if(isMaterial){
                    OrderMaterialDO materialDO= orderMaterialMapper.findById(Integer.parseInt(k3ReturnOrderDetail.getOrderItemId()));
                    k3ReturnOrderDetail.setOrderMaterial(ConverterUtil.convert(materialDO,OrderMaterial.class));
                }else {
                    OrderProductDO orderProductDO=orderProductMapper.findById(Integer.parseInt(k3ReturnOrderDetail.getOrderItemId()));
                    k3ReturnOrderDetail.setOrderProduct(ConverterUtil.convert(orderProductDO,OrderProduct.class));
                }
            }
        }

        result.setResult(k3ReturnOrder);
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, String> updateReturnOrder(K3ReturnOrder k3ReturnOrder) {
        ServiceResult<String, String> result = new ServiceResult<>();
        User loginUser = userSupport.getCurrentUser();
        Date currentTime = new Date();
        //退货日期不能小于三月五号
        Calendar calendar = Calendar.getInstance();
        calendar.set(2018, 2, 5, 0, 0, 0);
        Date minDate = calendar.getTime();
        if (minDate.compareTo(k3ReturnOrder.getReturnTime()) > 0) {
            result.setErrorCode(ErrorCode.RETURN_TIME_LESS_MIN_TIME);
            return result;
        }
        if (k3ReturnOrder == null) {
            result.setErrorCode(ErrorCode.PARAM_IS_NOT_NULL);
            return result;
        }
        K3ReturnOrderDO dbK3ReturnOrderDO = k3ReturnOrderMapper.findByNo(k3ReturnOrder.getReturnOrderNo());
        if (dbK3ReturnOrderDO == null) {
            result.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return result;
        }
        if (!ReturnOrderStatus.RETURN_ORDER_STATUS_WAIT_COMMIT.equals(dbK3ReturnOrderDO.getReturnOrderStatus())
                && !ReturnOrderStatus.RETURN_ORDER_STATUS_BACKED.equals(dbK3ReturnOrderDO.getReturnOrderStatus())) {
            result.setErrorCode(ErrorCode.K3_RETURN_ORDER_STATUS_CAN_NOT_UPDATE);
            return result;
        }
        //发货分公司检查
        SubCompanyDO subCompanyDO = subCompanyMapper.findById(k3ReturnOrder.getDeliverySubCompanyId());
        if (subCompanyDO == null) {
            result.setErrorCode(ErrorCode.DELIVERY_COMPANY_NOT_EXIT);
            return result;
        }
        //退货日期校验(退货时间不能大于起租时间)
        Map<String, Order> orderCatch = new HashMap<String, Order>();
        List<K3ReturnOrderDetailDO> orderDetailList = k3ReturnOrderDetailMapper.findListByReturnOrderId(dbK3ReturnOrderDO.getId());
        if (CollectionUtil.isNotEmpty(orderDetailList)) {
            for (K3ReturnOrderDetailDO k3ReturnOrderDetail : orderDetailList) {
                if (!orderCatch.containsKey(k3ReturnOrderDetail.getOrderNo())) {
                    ServiceResult<String, Order> serviceResult = k3Service.queryOrder(k3ReturnOrderDetail.getOrderNo());
                    if (!ErrorCode.SUCCESS.equals(serviceResult.getErrorCode())) {
                        result.setErrorCode(ErrorCode.ORDER_NOT_EXISTS);
                        return result;
                    }
                    Order order = serviceResult.getResult();
                    if (order.getRentStartTime().compareTo(k3ReturnOrder.getReturnTime()) > 0) {
                        result.setErrorCode(ErrorCode.RETURN_TIME_LESS_RENT_TIME);
                        return result;
                    }
                    orderCatch.put(k3ReturnOrderDetail.getOrderNo(), order);
                }
            }
        }
        K3ReturnOrderDO k3ReturnOrderDO = ConverterUtil.convert(k3ReturnOrder, K3ReturnOrderDO.class);
        k3ReturnOrderDO.setId(dbK3ReturnOrderDO.getId());
        k3ReturnOrderDO.setUpdateTime(currentTime);
        k3ReturnOrderDO.setUpdateUser(loginUser.getUserId().toString());
        if (k3ReturnOrderDO.getLogisticsAmount() == null) k3ReturnOrderDO.setLogisticsAmount(BigDecimal.ZERO);
        if (k3ReturnOrderDO.getServiceAmount() == null) k3ReturnOrderDO.setServiceAmount(BigDecimal.ZERO);
        k3ReturnOrderMapper.update(k3ReturnOrderDO);
        result.setResult(k3ReturnOrderDO.getReturnOrderNo());
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    public ServiceResult<String, String> cancelK3ReturnOrder(K3ReturnOrder k3ReturnOrder) {
        ServiceResult<String, String> result = new ServiceResult<>();
        Date now = new Date();

        K3ReturnOrderDO k3ReturnOrderDO = k3ReturnOrderMapper.findByNo(k3ReturnOrder.getReturnOrderNo());
        if (k3ReturnOrderDO == null) {
            result.setErrorCode(ErrorCode.K3_RETURN_ORDER_IS_NOT_NULL);
            return result;
        }
        //判断何时可以取消
        if (!ReturnOrderStatus.RETURN_ORDER_STATUS_WAIT_COMMIT.equals(k3ReturnOrderDO.getReturnOrderStatus()) &&
                !ReturnOrderStatus.RETURN_ORDER_STATUS_VERIFYING.equals(k3ReturnOrderDO.getReturnOrderStatus())
                && !ReturnOrderStatus.RETURN_ORDER_STATUS_BACKED.equals(k3ReturnOrderDO.getReturnOrderStatus())) {
            result.setErrorCode(ErrorCode.K3_RETURN_ORDER_STATUS_CAN_NOT_CANCEL);
            return result;
        }

        //判断状态审核中执行工作流取消审核
        if (ReturnOrderStatus.RETURN_ORDER_STATUS_VERIFYING.equals(k3ReturnOrderDO.getReturnOrderStatus())) {
            ServiceResult<String, String> cancelWorkFlowResult = workflowService.cancelWorkFlow(WorkflowType.WORKFLOW_TYPE_K3_RETURN, k3ReturnOrderDO.getReturnOrderNo());
            if (!ErrorCode.SUCCESS.equals(cancelWorkFlowResult.getErrorCode())) {
                result.setErrorCode(cancelWorkFlowResult.getErrorCode());
                return result;
            }
        }
        k3ReturnOrderDO.setUpdateTime(now);
        k3ReturnOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        k3ReturnOrderDO.setReturnOrderStatus(ReturnOrderStatus.RETURN_ORDER_STATUS_CANCEL);
        k3ReturnOrderMapper.update(k3ReturnOrderDO);
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(k3ReturnOrderDO.getReturnOrderNo());
        return result;
    }


    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, String> commitK3ReturnOrder(K3ReturnOrderCommitParam k3ReturnOrderCommitParam) {
        ServiceResult<String, String> result = new ServiceResult<>();
        User loginUser = userSupport.getCurrentUser();
        Date now = new Date();
        K3ReturnOrderDO k3ReturnOrderDO = k3ReturnOrderMapper.findByNo(k3ReturnOrderCommitParam.getReturnOrderNo());
        if (k3ReturnOrderDO == null) {
            result.setErrorCode(ErrorCode.K3_RETURN_ORDER_IS_NOT_NULL);
            return result;
        } else if (!ReturnOrderStatus.RETURN_ORDER_STATUS_WAIT_COMMIT.equals(k3ReturnOrderDO.getReturnOrderStatus())
                && !ReturnOrderStatus.RETURN_ORDER_STATUS_BACKED.equals(k3ReturnOrderDO.getReturnOrderStatus())) {
            //只有待提交和已驳回状态的换货单可以提交
            result.setErrorCode(ErrorCode.K3_RETURN_ORDER_COMMITTED_CAN_NOT_COMMIT_AGAIN);
            return result;
        }
        if (!k3ReturnOrderDO.getCreateUser().equals(loginUser.getUserId().toString())) {
            //只有创建换货单本人可以提交
            result.setErrorCode(ErrorCode.COMMIT_ONLY_SELF);
            return result;
        }
        if (CollectionUtil.isEmpty(k3ReturnOrderDO.getK3ReturnOrderDetailDOList())) {
            result.setErrorCode(ErrorCode.K3_RETURN_ORDER_DETAIL_COMMITTED_NOT_NULL);
            return result;
        }

        Map<Integer, Integer> rentingProductCountMap = new HashMap<>();
        Map<Integer, Integer> rentingMaterialCountMap = new HashMap<>();
        Map<Integer, Integer> nowProductCountMap = new HashMap<>();
        Map<Integer, Integer> nowMaterialCountMap = new HashMap<>();
        Integer productId = 0;
        Integer materialId = 0;
        List<K3ReturnOrderDetailDO> k3ReturnOrderDetailDOList = k3ReturnOrderDO.getK3ReturnOrderDetailDOList();
        for (K3ReturnOrderDetailDO k3ReturnOrderDetailDO : k3ReturnOrderDetailDOList) {
            OrderDO orderDO = orderMapper.findByOrderNo(k3ReturnOrderDetailDO.getOrderNo());
            if (orderDO != null) {
                //如果通过订单号查找到的订单状态是未发货状态的就不能进行提交
                if (!OrderStatus.ORDER_STATUS_DELIVERED.equals(orderDO.getOrderStatus())
                        && !OrderStatus.ORDER_STATUS_CONFIRM.equals(orderDO.getOrderStatus())&&
                        !OrderStatus.ORDER_STATUS_PART_RETURN.equals(orderDO.getOrderStatus())) {
                    result.setErrorCode(ErrorCode.K3_RETURN_ORDER_DETAIL_ORDER_STATUS_NOT_DELIVERED);
                    return result;
                }

                //对退货单提交的数量进行判断
                String productNo = k3ReturnOrderDetailDO.getProductNo();
                if (productSupport.isMaterial(productNo)) {
                    //物料
                    materialId = Integer.parseInt(k3ReturnOrderDetailDO.getOrderItemId());
                    if (materialId != null && materialId != 0) {
                        OrderMaterialDO orderMaterialDO = orderMaterialMapper.findById(materialId);
                        rentingMaterialCountMap.put(materialId, orderMaterialDO.getRentingMaterialCount());
                        nowMaterialCountMap.put(materialId, k3ReturnOrderDetailDO.getProductCount());
                    }
                } else {
                    //设备，查询数量的在租数量
                    productId = Integer.parseInt(k3ReturnOrderDetailDO.getOrderItemId());
                    if (productId != null && productId != 0) {
                        OrderProductDO orderProductDO = orderProductMapper.findById(productId);
                        rentingProductCountMap.put(productId, orderProductDO.getRentingProductCount());
                        nowProductCountMap.put(productId, k3ReturnOrderDetailDO.getProductCount());
                    }
                }
            }
        }

        Map<Integer, Integer> productCountMap = new HashMap<>();
        Map<Integer, Integer> materialCountMap = new HashMap<>();
        List<K3ReturnOrderDO> k3ReturnOrderDOList = k3ReturnOrderMapper.findByCustomerNo(k3ReturnOrderDO.getK3CustomerNo());
        if (CollectionUtil.isNotEmpty(k3ReturnOrderDOList)) {
            for (K3ReturnOrderDO dBK3ReturnOrderDO : k3ReturnOrderDOList) {
                if (ReturnOrderStatus.RETURN_ORDER_STATUS_VERIFYING.equals(dBK3ReturnOrderDO.getReturnOrderStatus())
                        || ReturnOrderStatus.RETURN_ORDER_STATUS_PROCESSING.equals(dBK3ReturnOrderDO.getReturnOrderStatus())) {
                    List<K3ReturnOrderDetailDO> dBK3ReturnOrderDetailDOList = k3ReturnOrderDetailMapper.findListByReturnOrderId(dBK3ReturnOrderDO.getId());
                    for (K3ReturnOrderDetailDO k3ReturnOrderDetailDO : dBK3ReturnOrderDetailDOList) {
                        String productNo = k3ReturnOrderDetailDO.getProductNo();
                        if (productSupport.isMaterial(productNo)) {
                            //物料
                            materialId = Integer.parseInt(k3ReturnOrderDetailDO.getOrderItemId());
                            if (materialCountMap.get(materialId) == null) {
                                materialCountMap.put(materialId, k3ReturnOrderDetailDO.getProductCount());
                            } else {
                                materialCountMap.put(materialId, k3ReturnOrderDetailDO.getProductCount() + materialCountMap.get(materialId));
                            }

                        } else {
                            //设备
                            productId = Integer.parseInt(k3ReturnOrderDetailDO.getOrderItemId());
                            if (productCountMap.get(productId) == null) {
                                productCountMap.put(productId, k3ReturnOrderDetailDO.getProductCount());
                            } else {
                                productCountMap.put(productId, k3ReturnOrderDetailDO.getProductCount() + productCountMap.get(productId));
                            }
                        }
                    }
                }
            }
        }

        //比较设备项
        if (nowProductCountMap.size() > 0) {
            for (Integer orderProductId : nowProductCountMap.keySet()) {
                Integer rentingProductCount = rentingProductCountMap.get(orderProductId) == null ? 0 : rentingProductCountMap.get(orderProductId);//在租数
                Integer processProductCount = productCountMap.get(orderProductId) == null ? 0 : productCountMap.get(orderProductId); //处理中和审核中数量
                Integer nowProductCount = nowProductCountMap.get(orderProductId) == null ? 0 : nowProductCountMap.get(orderProductId); //处理中和审核中数量

                if (processProductCount + nowProductCount - rentingProductCount > 0) {
                    result.setErrorCode(ErrorCode.K3_RETURN_ORDER_PRODUCT_COUNT_NOT_ENOUGH);
                    return result;
                }
            }
        }

        //比较物料项
        if (nowMaterialCountMap.size() > 0) {
            for (Integer orderMaterialId : rentingMaterialCountMap.keySet()) {
                Integer rentingMaterialCount = rentingMaterialCountMap.get(orderMaterialId) == null ? 0 : rentingMaterialCountMap.get(orderMaterialId);//在租数
                Integer processMaterialCount = materialCountMap.get(orderMaterialId) == null ? 0 : materialCountMap.get(orderMaterialId); //处理中和审核中数量
                Integer nowMaterialCount = nowMaterialCountMap.get(orderMaterialId) == null ? 0 : nowMaterialCountMap.get(orderMaterialId); //处理中和审核中数量

                if (processMaterialCount + nowMaterialCount - rentingMaterialCount > 0) {
                    result.setErrorCode(ErrorCode.K3_RETURN_ORDER_MATERIAL_COUNT_NOT_ENOUGH);
                    return result;
                }
            }
        }

        ServiceResult<String, Boolean> needVerifyResult = workflowService.isNeedVerify(WorkflowType.WORKFLOW_TYPE_K3_RETURN);
        if (!ErrorCode.SUCCESS.equals(needVerifyResult.getErrorCode())) {
            result.setErrorCode(needVerifyResult.getErrorCode());
            return result;
        } else if (needVerifyResult.getResult()) {
            if (k3ReturnOrderCommitParam.getVerifyUserId() == null) {
                result.setErrorCode(ErrorCode.VERIFY_USER_NOT_NULL);
                return result;
            }
            //调用提交审核服务
            k3ReturnOrderCommitParam.setVerifyMatters("K3退货单审核事项：1.服务费和运费 2.退还方式 3.商品与配件的退货数量");
            ServiceResult<String, String> verifyResult = workflowService.commitWorkFlow(WorkflowType.WORKFLOW_TYPE_K3_RETURN, k3ReturnOrderCommitParam.getReturnOrderNo(), k3ReturnOrderCommitParam.getVerifyUserId(), k3ReturnOrderCommitParam.getVerifyMatters(), k3ReturnOrderCommitParam.getRemark(), k3ReturnOrderCommitParam.getImgIdList(), null);
            //修改提交审核状态
            if (ErrorCode.SUCCESS.equals(verifyResult.getErrorCode())) {
                k3ReturnOrderDO.setReturnOrderStatus(ReturnOrderStatus.RETURN_ORDER_STATUS_VERIFYING);
                k3ReturnOrderDO.setUpdateUser(loginUser.getUserId().toString());
                k3ReturnOrderDO.setUpdateTime(now);
                k3ReturnOrderMapper.update(k3ReturnOrderDO);
                return verifyResult;
            } else {
                result.setErrorCode(verifyResult.getErrorCode());
                return result;
            }
        } else {
            k3ReturnOrderDO.setReturnOrderStatus(ReturnOrderStatus.RETURN_ORDER_STATUS_PROCESSING);
            k3ReturnOrderDO.setUpdateUser(loginUser.getUserId().toString());
            k3ReturnOrderDO.setUpdateTime(now);
            k3ReturnOrderMapper.update(k3ReturnOrderDO);

            result = sendReturnOrderToK3(k3ReturnOrderCommitParam.getReturnOrderNo());
            if (!ErrorCode.SUCCESS.equals(result.getErrorCode())) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            }
            return result;
        }
    }


    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public String receiveVerifyResult(boolean verifyResult, String businessNo) {
        K3ReturnOrderDO k3ReturnOrderDO = k3ReturnOrderMapper.findByNo(businessNo);
        try {
            if (k3ReturnOrderDO != null) {//k3退货单
                //不是审核中状态的收货单，拒绝处理
                if (!ReturnOrderStatus.RETURN_ORDER_STATUS_VERIFYING.equals(k3ReturnOrderDO.getReturnOrderStatus())) {
                    return ErrorCode.BUSINESS_EXCEPTION;
                }
                if (verifyResult) {
                    ServiceResult result = sendReturnOrderToK3(businessNo);
                    if (!ErrorCode.SUCCESS.equals(result.getErrorCode().toString())) {
                        return result.getErrorCode().toString();
                    }
                    k3ReturnOrderDO.setReturnOrderStatus(ReturnOrderStatus.RETURN_ORDER_STATUS_PROCESSING);
                } else {
                    k3ReturnOrderDO.setReturnOrderStatus(ReturnOrderStatus.RETURN_ORDER_STATUS_BACKED);
                }
                k3ReturnOrderMapper.update(k3ReturnOrderDO);
                return ErrorCode.SUCCESS;
            } else {
                return ErrorCode.BUSINESS_EXCEPTION;
            }
        } catch (BusinessException e) {
            throw e;
        }catch (Exception e) {
            if (k3ReturnOrderDO != null) {
                logger.error("【K3退货单审核后，业务处理异常】", e);
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                logger.error("【数据已回滚】");
            }
            return ErrorCode.BUSINESS_EXCEPTION;
        }
    }

    @Override
    public ServiceResult<String, String> strongCancelReturnOrder(String returnOrderNo) {
        ServiceResult<String, String> result = new ServiceResult<>();

        K3ReturnOrderDO k3ReturnOrderDO = k3ReturnOrderMapper.findByNo(returnOrderNo);
        if (k3ReturnOrderDO == null) {
            result.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return result;
        }
        //只有结束状态的K3退货单可以强制取消
        if (!ReturnOrderStatus.RETURN_ORDER_STATUS_END.equals(k3ReturnOrderDO.getReturnOrderStatus())) {
            result.setErrorCode(ErrorCode.RETURN_ORDER_STATUS_CAN_NOT_CANCEL);
            return result;
        }

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(returnOrderNo);
        return result;
    }

    @Override
    public ServiceResult<String, String> revokeReturnOrder(String returnOrderNo) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        K3ReturnOrderDO k3ReturnOrderDO = k3ReturnOrderMapper.findByNo(returnOrderNo);
        if (k3ReturnOrderDO == null) {
            serviceResult.setErrorCode(ErrorCode.RETURN_ORDER_NOT_EXISTS);
            return serviceResult;
        }
        if (!ReturnOrderStatus.RETURN_ORDER_STATUS_PROCESSING.equals(k3ReturnOrderDO.getReturnOrderStatus())) {
            serviceResult.setErrorCode(ErrorCode.RETURN_ORDER_STATUS_CAN_NOT_CANCEL);
            return serviceResult;
        }
        try {
            HttpHeaderBuilder headerBuilder = HttpHeaderBuilder.custom();
            headerBuilder.contentType("application/json");
            String url = K3Config.k3Server + "/seoutstock/billcancel";
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("billno", returnOrderNo);
            logger.info("revoke return revoke request : " + jsonObject.toJSONString());
            String response = HttpClientUtil.post(url, jsonObject.toJSONString(), headerBuilder, "UTF-8");
            logger.info("revoke return revoke response : " + response);
            com.lxzl.erp.core.k3WebServiceSdk.ERPServer_Models.ServiceResult result = JSON.parseObject(response, com.lxzl.erp.core.k3WebServiceSdk.ERPServer_Models.ServiceResult.class);
            if (result.getStatus() == 0) {
                k3ReturnOrderDO.setReturnOrderStatus(ReturnOrderStatus.RETURN_ORDER_STATUS_CANCEL);
                k3ReturnOrderDO.setUpdateTime(new Date());
                k3ReturnOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                k3ReturnOrderMapper.update(k3ReturnOrderDO);
            } else {
                throw new BusinessException(result.getResult());
            }
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public ServiceResult<String, Integer> importK3HistoricalRefundList(K3ReturnOrderQueryParam k3ReturnOrderQueryParam) {
        StringBuffer info = new StringBuffer();
        ServiceResult<String, Integer> importResult = new ServiceResult<>();

        // 从k3服务器获取历史退货单信息
        ServiceResult<String, String> k3HistoricalRefundListFromK3 = k3Service.queryK3HistoricalRefundList(k3ReturnOrderQueryParam,info);
        if (!ErrorCode.SUCCESS.equals(k3HistoricalRefundListFromK3.getErrorCode())) {
            importResult.setErrorCode(ErrorCode.K3_SERVER_ERROR);
            return importResult;
        }
        String k3ResultJsonStr = k3HistoricalRefundListFromK3.getResult();
        if (logger.isInfoEnabled()) {
            logger.info("从k3获取到的历史退货单数据：" + k3ResultJsonStr);
        }
        // 数据处理
        List<K3HistoricalReturnOrder> billDatas = processHistoricalRefundData(k3ResultJsonStr);
        // 过滤已存在退货单列表信息
        List<K3HistoricalReturnOrder> needSaveBillDatas = filterExistsReturnOrders(billDatas);
        info.append("共"+billDatas.size()+"条数据，其中"+(needSaveBillDatas.size())+"条数据系统中不存在，\n");
        // 过滤退货单详情列表信息
//        needSaveBillDatas = filterReturnOrderDetails(needSaveBillDatas,info);
        // 保存k3数据列表
        saveBillDatas(needSaveBillDatas,info);
        Integer totalCount = billDatas==null?0:billDatas.size();
        importResult.setResult(totalCount);
        importResult.setErrorCode(ErrorCode.SUCCESS);
        return importResult;
    }

    @Override
    public ServiceResult<String, String> batchImportK3HistoricalRefundList(Integer startPage) {
        ServiceResult<String, String> importResult = new ServiceResult<>();
        K3ReturnOrderQueryParam k3ReturnOrderQueryParam = new K3ReturnOrderQueryParam();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startParam = null;
        Date now = new Date();
        try {
            startParam = simpleDateFormat.parse("2017-1-1 00:00:00");
        } catch (ParseException e) {
        }
        k3ReturnOrderQueryParam.setReturnStartTime(startParam);
        k3ReturnOrderQueryParam.setReturnEndTime(now);
        int pageSize = 200;
        k3ReturnOrderQueryParam.setPageSize(pageSize);
        int pageNo = startPage==null||startPage==0?1:startPage;
        int totalCount = 0;
        while(pageNo>0){
            k3ReturnOrderQueryParam.setPageNo(pageNo++);
            ServiceResult<String,Integer> result = importK3HistoricalRefundList(k3ReturnOrderQueryParam);
            if(ErrorCode.SUCCESS.equals(result.getErrorCode())){
                Integer total = result.getResult();
                totalCount = totalCount+total;
                if(total==0){
                    pageNo = 0;
                }
            }else{
                pageNo = 0 ;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Date endTime = new Date();
        dingDingSupport.dingDingSendMessage("共批量导入了"+totalCount+"条数据，耗时"+ ((endTime.getTime()-now.getTime())/1000)+"秒");
        importResult.setErrorCode(ErrorCode.SUCCESS);
        return importResult;
    }

    /**
     * 创建退货单时查询erp订单列表展示
     * @param param
     * @return
     */
    @Override
    public ServiceResult<String, Page<Order>> queryOrderForReturn(OrderForReturnQueryParam param) {
        ServiceResult<String, Page<Order>> result = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(param.getPageNo(), param.getPageSize());

        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        maps.put("orderForReturnQueryParam", param);
        //如果查询的状态不是确认收货（20），部分退还（22）,不查询数据库直接返回空集合
        if (param.getOrderStatus()!=null
                && !OrderStatus.ORDER_STATUS_CONFIRM.equals(param.getOrderStatus())
                && !OrderStatus.ORDER_STATUS_PART_RETURN.equals(param.getOrderStatus())) {
            List<Order> orderList = new ArrayList<>();
            Page<Order> page = new Page<>(orderList, CommonConstant.COMMON_ZERO, param.getPageNo(), param.getPageSize());
            result.setErrorCode(ErrorCode.SUCCESS);
            result.setResult(page);
            return result;
        }

        Integer totalCount = orderMapper.findOrderForReturnCountParam(maps);
        List<OrderDO> orderDOList =orderMapper.findOrderForReturnParam(maps);
        List<Order> orderList = ConverterUtil.convertList(orderDOList, Order.class);
        Page<Order> page = new Page<>(orderList, totalCount, param.getPageNo(), param.getPageSize());
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(page);
        return result;
    }
    /**
     * 创建退货单时从ERP获取订单数据
     * @param k3ReturnOrder
     * @return
     */
    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, String> createReturnOrderFromERP(K3ReturnOrder k3ReturnOrder) {
        ServiceResult<String, String> result = new ServiceResult<>();
        User loginUser = userSupport.getCurrentUser();
        Date currentTime = new Date();
        if (k3ReturnOrder == null) {
            result.setErrorCode(ErrorCode.PARAM_IS_NOT_NULL);
            return result;
        }
        //退货日期不能小于三月五号
        Calendar calendar = Calendar.getInstance();
        calendar.set(2018, 2, 5, 0, 0, 0);
        Date minDate = calendar.getTime();
        if (minDate.compareTo(k3ReturnOrder.getReturnTime()) > 0) {
            result.setErrorCode(ErrorCode.RETURN_TIME_LESS_MIN_TIME);
            return result;
        }
        //发货分公司检查
        SubCompanyDO subCompanyDO = subCompanyMapper.findById(k3ReturnOrder.getDeliverySubCompanyId());
        if (subCompanyDO == null) {
            result.setErrorCode(ErrorCode.DELIVERY_COMPANY_NOT_EXIT);
            return result;
        }

        //       //商品物料唯一性校验
//        Set<String> primaryKeySet = new HashSet<String>();
//        for (K3ReturnOrderDetail k3ReturnOrderDetail : k3ReturnOrder.getK3ReturnOrderDetailList()) {
//            primaryKeySet.add(k3ReturnOrderDetail.getOrderItemId() + "_" + k3ReturnOrderDetail.getProductNo());
//        }
//        if (primaryKeySet.size() < k3ReturnOrder.getK3ReturnOrderDetailList().size()) {
//            result.setErrorCode(ErrorCode.HAS_SAME_PRODUCT);
//            return result;
//        }
        //itemId校验
        if (!varifyOrderItemId(k3ReturnOrder.getK3ReturnOrderDetailList())) {
            result.setErrorCode(ErrorCode.PRODUCT_IS_NULL_OR_NOT_EXISTS);
            return result;
        }

        ServiceResult<String, Customer> customerResult = customerService.queryCustomerByNo(k3ReturnOrder.getK3CustomerNo());
        if (!ErrorCode.SUCCESS.equals(customerResult.getErrorCode())) {
            result.setErrorCode(customerResult.getErrorCode());
            return result;
        }
        Customer customer = customerResult.getResult();
        k3ReturnOrder.setK3CustomerName(customer.getCustomerName());

        K3ReturnOrderDO k3ReturnOrderDO = ConverterUtil.convert(k3ReturnOrder, K3ReturnOrderDO.class);

        k3ReturnOrderDO.setReturnOrderNo("LXK3RO" + DateUtil.formatDate(currentTime, "yyyyMMddHHmmssSSS"));
        k3ReturnOrderDO.setReturnOrderStatus(ReturnOrderStatus.RETURN_ORDER_STATUS_WAIT_COMMIT);
        k3ReturnOrderDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        k3ReturnOrderDO.setCreateTime(currentTime);
        k3ReturnOrderDO.setCreateUser(loginUser.getUserId().toString());
        k3ReturnOrderDO.setUpdateTime(currentTime);
        k3ReturnOrderDO.setUpdateUser(loginUser.getUserId().toString());
        if (k3ReturnOrderDO.getLogisticsAmount() == null) k3ReturnOrderDO.setLogisticsAmount(BigDecimal.ZERO);
        if (k3ReturnOrderDO.getServiceAmount() == null) k3ReturnOrderDO.setServiceAmount(BigDecimal.ZERO);
        k3ReturnOrderMapper.save(k3ReturnOrderDO);
        if (CollectionUtil.isNotEmpty(k3ReturnOrder.getK3ReturnOrderDetailList())) {
            Map<String, Order> orderCatch = new HashMap<String, Order>();
            for (K3ReturnOrderDetail k3ReturnOrderDetail : k3ReturnOrder.getK3ReturnOrderDetailList()) {
                if (!orderCatch.containsKey(k3ReturnOrderDetail.getOrderNo())) {
                    // 改成从erp里查询订单
                    OrderDO orderDO = orderMapper.findByOrderNo(k3ReturnOrderDetail.getOrderNo());
                    Order order = ConverterUtil.convert(orderDO, Order.class);
                    if (orderDO.getRentStartTime().compareTo(k3ReturnOrderDO.getReturnTime()) > 0) {
                        result.setErrorCode(ErrorCode.RETURN_TIME_LESS_RENT_TIME);
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                        return result;
                    }
                    orderCatch.put(k3ReturnOrderDetail.getOrderNo(), order);
                }


                K3ReturnOrderDetailDO k3ReturnOrderDetailDO = ConverterUtil.convert(k3ReturnOrderDetail, K3ReturnOrderDetailDO.class);
                k3ReturnOrderDetailDO.setReturnOrderId(k3ReturnOrderDO.getId());
                k3ReturnOrderDetailDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                k3ReturnOrderDetailDO.setCreateTime(currentTime);
                k3ReturnOrderDetailDO.setCreateUser(loginUser.getUserId().toString());
                k3ReturnOrderDetailDO.setUpdateTime(currentTime);
                k3ReturnOrderDetailDO.setUpdateUser(loginUser.getUserId().toString());
                k3ReturnOrderDetailMapper.save(k3ReturnOrderDetailDO);
            }
        }
        result.setResult(k3ReturnOrderDO.getReturnOrderNo());
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }
    /**
     * 修改退货单时从ERP获取订单数据
     * @param k3ReturnOrder
     * @return
     */
    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, String> updateReturnOrderFromERP(K3ReturnOrder k3ReturnOrder) {
        ServiceResult<String, String> result = new ServiceResult<>();
        User loginUser = userSupport.getCurrentUser();
        Date currentTime = new Date();
        //退货日期不能小于三月五号
        Calendar calendar = Calendar.getInstance();
        calendar.set(2018, 2, 5, 0, 0, 0);
        Date minDate = calendar.getTime();
        if (minDate.compareTo(k3ReturnOrder.getReturnTime()) > 0) {
            result.setErrorCode(ErrorCode.RETURN_TIME_LESS_MIN_TIME);
            return result;
        }
        if (k3ReturnOrder == null) {
            result.setErrorCode(ErrorCode.PARAM_IS_NOT_NULL);
            return result;
        }
        K3ReturnOrderDO dbK3ReturnOrderDO = k3ReturnOrderMapper.findByNo(k3ReturnOrder.getReturnOrderNo());
        if (dbK3ReturnOrderDO == null) {
            result.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return result;
        }
        if (!ReturnOrderStatus.RETURN_ORDER_STATUS_WAIT_COMMIT.equals(dbK3ReturnOrderDO.getReturnOrderStatus())
                && !ReturnOrderStatus.RETURN_ORDER_STATUS_BACKED.equals(dbK3ReturnOrderDO.getReturnOrderStatus())) {
            result.setErrorCode(ErrorCode.K3_RETURN_ORDER_STATUS_CAN_NOT_UPDATE);
            return result;
        }
        //发货分公司检查
        SubCompanyDO subCompanyDO = subCompanyMapper.findById(k3ReturnOrder.getDeliverySubCompanyId());
        if (subCompanyDO == null) {
            result.setErrorCode(ErrorCode.DELIVERY_COMPANY_NOT_EXIT);
            return result;
        }
        //退货日期校验(退货时间不能大于起租时间)
        Map<String, Order> orderCatch = new HashMap<String, Order>();
        List<K3ReturnOrderDetailDO> orderDetailList = k3ReturnOrderDetailMapper.findListByReturnOrderId(dbK3ReturnOrderDO.getId());
        if (CollectionUtil.isNotEmpty(orderDetailList)) {
            for (K3ReturnOrderDetailDO k3ReturnOrderDetail : orderDetailList) {
                if (!orderCatch.containsKey(k3ReturnOrderDetail.getOrderNo())) {
                    //改成从erp里查询订单
                    OrderDO orderDO = orderMapper.findByOrderNo(k3ReturnOrderDetail.getOrderNo());
                    Order order = ConverterUtil.convert(orderDO, Order.class);
                    if (order.getRentStartTime().compareTo(k3ReturnOrder.getReturnTime()) > 0) {
                        result.setErrorCode(ErrorCode.RETURN_TIME_LESS_RENT_TIME);
                        return result;
                    }
                    orderCatch.put(k3ReturnOrderDetail.getOrderNo(), order);
                }
            }
        }
        K3ReturnOrderDO k3ReturnOrderDO = ConverterUtil.convert(k3ReturnOrder, K3ReturnOrderDO.class);
        k3ReturnOrderDO.setId(dbK3ReturnOrderDO.getId());
        k3ReturnOrderDO.setUpdateTime(currentTime);
        k3ReturnOrderDO.setUpdateUser(loginUser.getUserId().toString());
        if (k3ReturnOrderDO.getLogisticsAmount() == null) k3ReturnOrderDO.setLogisticsAmount(BigDecimal.ZERO);
        if (k3ReturnOrderDO.getServiceAmount() == null) k3ReturnOrderDO.setServiceAmount(BigDecimal.ZERO);
        k3ReturnOrderMapper.update(k3ReturnOrderDO);
        result.setResult(k3ReturnOrderDO.getReturnOrderNo());
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    /**
     * 创建退货单时根据订单编号查询订单详情
     * @param orderNo
     * @return
     */
    @Override
    public ServiceResult<String, Order> queryOrderByNoForReturn(String orderNo) {
        ServiceResult<String, Order> result = new ServiceResult<>();
        if (orderNo == null) {
            result.setErrorCode(ErrorCode.ID_NOT_NULL);
            return result;
        }
        OrderDO orderDO = orderMapper.findByOrderNo(orderNo);
        if (orderDO == null) {
            result.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return result;
        }
        List<OrderProductDO> orderProductDOList = orderDO.getOrderProductDOList();
        List<OrderMaterialDO> orderMaterialDOList = orderDO.getOrderMaterialDOList();
        Set<Integer> productIdSet = new HashSet<>();
        Set<String> productDOCategoryIdSet = new HashSet<>();
        Set<String> productDOBrandIdSet = new HashSet<>();
        Map<Integer,ProductDO> productDOMap = new HashMap<>();
        Map<Integer,K3MappingCategoryDO> k3MappingCategoryDOMap = new HashMap<>();
        Map<Integer,K3MappingBrandDO> k3MappingBrandDOMap = new HashMap<>();

        if (CollectionUtil.isNotEmpty(orderProductDOList)) {
            for (OrderProductDO orderProductDO:orderProductDOList) {
                productIdSet.add(orderProductDO.getProductId());
            }
            List<ProductDO> productDOList = productMapper.findByIds(productIdSet);
            if (CollectionUtil.isNotEmpty(productDOList)) {
                for (ProductDO productDO:productDOList) {
                    productDOCategoryIdSet.add(productDO.getCategoryId().toString());
                    productDOBrandIdSet.add(productDO.getBrandId().toString());
                }
                List<K3MappingCategoryDO> k3MappingCategoryDOList = k3MappingCategoryMapper.findByErpCodeList(productDOCategoryIdSet);
                List<K3MappingBrandDO> k3MappingBrandDOList = k3MappingBrandMapper.findByErpCodeList(productDOBrandIdSet);
                for (OrderProductDO orderProductDO:orderProductDOList) {
                    for (ProductDO productDO:productDOList) {
                        if (orderProductDO.getProductId().equals(productDO.getId())) {
                            productDOMap.put(orderProductDO.getId(),productDO);
                        }
                        for (K3MappingCategoryDO k3MappingCategoryDO:k3MappingCategoryDOList) {
                            if (productDO.getCategoryId().toString().equals(k3MappingCategoryDO.getErpCategoryCode())) {
                                k3MappingCategoryDOMap.put(orderProductDO.getId(),k3MappingCategoryDO);
                            }
                        }
                        for (K3MappingBrandDO k3MappingBrandDO:k3MappingBrandDOList) {
                            if (productDO.getBrandId().toString().equals(k3MappingBrandDO.getErpBrandCode())) {
                                k3MappingBrandDOMap.put(orderProductDO.getId(),k3MappingBrandDO);
                            }
                        }
                    }
                }
                for (OrderProductDO orderProductDO:orderProductDOList) {
                    ProductDO productDO = productDOMap.get(orderProductDO.getId());
                    K3MappingCategoryDO k3MappingCategoryDO = k3MappingCategoryDOMap.get(orderProductDO.getId());
                    K3MappingBrandDO k3MappingBrandDO = k3MappingBrandDOMap.get(orderProductDO.getId());
                    String number = "";
                    if (StringUtil.isNotEmpty(productDO.getK3ProductNo())) {
                        number = productDO.getK3ProductNo();
                    } else {
                        number = "10." + k3MappingCategoryDO.getK3CategoryCode() + "." + k3MappingBrandDO.getK3BrandCode() + "." + productDO.getProductModel();
                    }
                    if(CommonConstant.COMMON_CONSTANT_YES.equals(orderDO.getIsPeer())){
                        number = "90"+number.substring(2,number.length());
                    }
                    if (CommonConstant.COMMON_CONSTANT_NO.equals(orderDO.getIsK3Order())) {
                        orderProductDO.setFEntryID(orderProductDO.getId());
                    }
                    orderProductDO.setProductNumber(number);
                }
            }
        }
        Set<Integer> materialIdSet = new HashSet<>();
        Set<String> materialDOMaterialTypeSet = new HashSet<>();
        Set<String> materialDOBrandIdSet = new HashSet<>();
        Map<Integer,MaterialDO> materialDOMap = new HashMap<>();
        Map<Integer,K3MappingMaterialTypeDO> k3MappingMaterialTypeMap = new HashMap<>();
        Map<Integer,K3MappingBrandDO> materialk3MappingBrandDOMap = new HashMap<>();
        Map<Integer,String> FNumberMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(orderMaterialDOList)) {
            for (OrderMaterialDO orderMaterialDO:orderMaterialDOList) {
                materialIdSet.add(orderMaterialDO.getMaterialId());
            }
            List<MaterialDO> materialDOList = materialMapper.findByIds(materialIdSet);
            if (CollectionUtil.isNotEmpty(materialDOList)) {
                for (MaterialDO materialDO:materialDOList) {
                    materialDOMaterialTypeSet.add(materialDO.getMaterialType().toString());
                    materialDOBrandIdSet.add(materialDO.getBrandId().toString());
                }
                List<K3MappingMaterialTypeDO> k3MappingMaterialTypeDOList = k3MappingMaterialTypeMapper.findByErpCodeList(materialDOMaterialTypeSet);
                List<K3MappingBrandDO> materialk3MappingBrandDOList = k3MappingBrandMapper.findByErpCodeList(materialDOBrandIdSet);
                for (OrderMaterialDO orderMaterialDO:orderMaterialDOList) {
                    for (MaterialDO materialDO:materialDOList) {
                        if (orderMaterialDO.getMaterialId().equals(materialDO.getId())) {
                            materialDOMap.put(orderMaterialDO.getId(),materialDO);
                        }
                        for (K3MappingMaterialTypeDO k3MappingMaterialTypeDO:k3MappingMaterialTypeDOList) {
                            if (materialDO.getMaterialType().toString().equals(k3MappingMaterialTypeDO.getErpMaterialTypeCode())) {
                                k3MappingMaterialTypeMap.put(orderMaterialDO.getId(),k3MappingMaterialTypeDO);
                            }
                        }
                        for (K3MappingBrandDO materialk3MappingBrandDO:materialk3MappingBrandDOList) {
                            if (materialDO.getBrandId().toString().equals(materialk3MappingBrandDO.getErpBrandCode())) {
                                materialk3MappingBrandDOMap.put(orderMaterialDO.getId(),materialk3MappingBrandDO);
                            }
                        }
                    }
                }
                for (OrderMaterialDO orderMaterialDO:orderMaterialDOList) {
                    if (CommonConstant.COMMON_CONSTANT_NO.equals(orderDO.getIsK3Order())) {
                        orderMaterialDO.setFEntryID(orderMaterialDO.getId());
                    }
                    MaterialDO materialDO = materialDOMap.get(orderMaterialDO.getId());
                    K3MappingMaterialTypeDO k3MappingMaterialTypeDO = k3MappingMaterialTypeMap.get(orderMaterialDO.getId());
                    K3MappingBrandDO k3MappingBrandDO = materialk3MappingBrandDOMap.get(orderMaterialDO.getId());
                    String number = "";
                    if (StringUtil.isNotEmpty(materialDO.getK3MaterialNo())) {
                        number = materialDO.getK3MaterialNo();
                    } else {
                        number = "20." + k3MappingMaterialTypeDO.getK3MaterialTypeCode() + "." + k3MappingBrandDO.getK3BrandCode() + "." + materialDO.getMaterialModel();
                    }
                    FNumberMap.put(orderMaterialDO.getFEntryID(),number);
                }
            }
        }
        Order order = ConverterUtil.convert(orderDO, Order.class);

        List<OrderMaterial> orderMaterialList = order.getOrderMaterialList();

        if (!FNumberMap.isEmpty()) {
            for (OrderMaterial orderMaterial:orderMaterialList) {
                orderMaterial.setFNumber(FNumberMap.get(orderMaterial.getFEntryID()));
            }
        }

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(order);
        return result;
    }

    /**
     * 处理历史订单数据
     */
    private List<K3HistoricalReturnOrder> processHistoricalRefundData(String k3ResultJsonStr) {
        Map<String, Object> k3ResultMap = JSONObject.parseObject(k3ResultJsonStr, HashMap.class);
        // 获取data的json字符串
        String dataStr = JSONObject.toJSONString(k3ResultMap.get("Data"));
        // 获取结果data的map
        Map<String, Object> k3ResultData = JSONObject.parseObject(dataStr, HashMap.class);
        // 获取bills数组json字符串
        String billsStr = JSONArray.toJSONString(k3ResultData.get("bills"));
        return JSONArray.parseArray(billsStr, K3HistoricalReturnOrder.class);
    }

    /**
     * 保存k3历史退货订单数据，并发送钉钉
     */
    private void saveBillDatas(List<K3HistoricalReturnOrder> needSaveBillDatas,StringBuffer info) {
        // 获取待保存退货订单数据
        List<K3ReturnOrderDO> k3ReturnOrderDOList = saveK3ReturnOrders(needSaveBillDatas,info);

        // 保存k3回调接口的处理退货单数据
        info.append("实际保存退货单数量:"+k3ReturnOrderDOList.size()+"\n");
        Integer notSuccessCount = doCallbackReturnOrder(k3ReturnOrderDOList);
        info.append("订单处理成功的退货单数量:"+(k3ReturnOrderDOList.size()-notSuccessCount)+"\n");
        dingDingSupport.dingDingSendMessage(info.toString());
    }

    /**
     * 执行保存k3回调接口的处理退货单数据
     */
    private int doCallbackReturnOrder(List<K3ReturnOrderDO> k3ReturnOrderDOList) {
        int notSuccessCount = 0;

        for (K3ReturnOrderDO k3ReturnOrderDO : k3ReturnOrderDOList){
            if(!ReturnOrderStatus.RETURN_ORDER_STATUS_END.equals(k3ReturnOrderDO.getReturnOrderStatus())){
                notSuccessCount++;
                continue;
            }
            ServiceResult<String, String> callBackResult = null;
            try{
                K3ReturnOrder k3ReturnOrder = ConverterUtil.convert(k3ReturnOrderDO,K3ReturnOrder.class);
                callBackResult = k3CallbackService.callbackReturnDetail(k3ReturnOrder,k3ReturnOrderDO);
                if (!ErrorCode.SUCCESS.equals(callBackResult.getErrorCode())) {
                    logger.info("调用回调接口失败：" + ErrorCode.getMessage(callBackResult.getErrorCode()));
                    notSuccessCount++;
                }
            }catch (Exception e){
                notSuccessCount++;
            }
        }
        return notSuccessCount;
    }

    /**
     * 过滤已存在的k3退货订单列表信息
     */
    private List<K3HistoricalReturnOrder> filterExistsReturnOrders(List<K3HistoricalReturnOrder> billDatas) {
        if (CollectionUtil.isEmpty(billDatas)) {
            return billDatas;
        }
        List<String> returnOrderNos = new ArrayList<>();
        for (K3HistoricalReturnOrder k3HistoricalReturnOrder : billDatas) {
            if (k3HistoricalReturnOrder.getK3ReturnOrder() != null) {
                returnOrderNos.add(k3HistoricalReturnOrder.getK3ReturnOrder().getReturnOrderNo());
            }
        }
        // 根据退货单单号列表查询退货单列表信息
        List<K3ReturnOrderDO> k3ReturnOrderDOS = k3ReturnOrderMapper.listByReturnOrderNos(returnOrderNos);
        if (logger.isInfoEnabled()) {
            logger.info("根据退货单号列表获取的数据为：" + JSONArray.toJSONString(k3ReturnOrderDOS));
        }
        // 过滤系统中已存在的k3退货单数据
        return filterExistsReturnOrders(billDatas, k3ReturnOrderDOS);
    }

    /**
     * 过滤已存在的k3退货订单列表信息
     */
    private List<K3HistoricalReturnOrder> filterExistsReturnOrders(List<K3HistoricalReturnOrder> billDatas, List<K3ReturnOrderDO> k3ReturnOrderDOS) {
        List<K3HistoricalReturnOrder> needSavabillDatas = new ArrayList<>();
        Map<String,K3ReturnOrderDO> map = new HashMap<>();
        for(K3ReturnOrderDO k3ReturnOrderDO : k3ReturnOrderDOS){
            map.put(k3ReturnOrderDO.getReturnOrderNo(),k3ReturnOrderDO);
        }
        for(K3HistoricalReturnOrder k3HistoricalReturnOrder : billDatas){
            if(!map.containsKey(k3HistoricalReturnOrder.getK3ReturnOrder().getReturnOrderNo())){
                needSavabillDatas.add(k3HistoricalReturnOrder);
            }
        }
        return needSavabillDatas;
    }


    /** 当前退货单是否需要保存---是返回true---否则返回false */
    private boolean isNeedSaveBill(K3HistoricalReturnOrder billData) {
        boolean needAddFlag = true;
        if (billData == null || billData.getK3ReturnOrder() == null) {
            needAddFlag = false;
        }
        if (billData.getK3ReturnOrder().getReturnReasonType() == null) {
            logger.error("=====================k3ReturnOrder.returnReasonType:退货原因为空================");
            needAddFlag = false;
        }
        if (billData.getK3ReturnOrder().getEqAmount() == null) {
            logger.error("=====================k3ReturnOrder.eqAmount:需恢复的信用额度为空================");
            needAddFlag = false;
        }
        if (billData.getK3ReturnOrder().getDeliverySubCompanyId() == null) {
            logger.error("=====================k3ReturnOrder.deliverySubCompanyId:发货分公司id为空================");
            needAddFlag = false;
        }
        return needAddFlag;
    }
    /**
     * <p>
     * 过滤退货单详情信息
     * </p>
     * <pre>
     *     过滤规则：退货单详情的订单不存在erp系统中，则移除指定的退货单详情数据
     * </pre>
     * @author daiqi
     * @date 2018/4/27 19:23
     * @param
     * @return java.util.List<com.lxzl.erp.common.domain.k3.pojo.returnOrder.K3HistoricalReturnOrder>
     */
    private List<K3HistoricalReturnOrder> filterReturnOrderDetails(final List<K3HistoricalReturnOrder> k3HistoricalReturnOrders,StringBuffer info) {
        if (CollectionUtil.isEmpty(k3HistoricalReturnOrders)) {
            return k3HistoricalReturnOrders;
        }
        List<K3HistoricalReturnOrder> returnOrders = k3HistoricalReturnOrders;
        Set<String> orderSet = new HashSet<>() ;
        // 1 循环将订单放入set中
        for (K3HistoricalReturnOrder returnOrder : k3HistoricalReturnOrders) {
            for (K3ReturnOrderDetail orderDetail : returnOrder.getK3ReturnOrderDetails()) {
                if (orderDetail == null || StringUtils.isBlank(orderDetail.getOrderNo())) {
                    continue;
                }
                orderSet.add(orderDetail.getOrderNo());
            }
        }
        // 2 从数据库中获取订单列表信息---并以orderNo为key添加到map
        List<OrderDO> orderDOSFromDataBase = orderMapper.listByOrderNOs(orderSet);
        logger.info("根据订单号列表获取的订单信息列表为：" + JSONObject.toJSONString(orderDOSFromDataBase));
        Map<String, OrderDO> maps = new HashMap<>();
        for (OrderDO orderDO : orderDOSFromDataBase) {
            maps.put(orderDO.getOrderNo(), orderDO);
        }
        logger.info("根据订单号列表获取的订单信息map中的数据为：" + JSONObject.toJSONString(maps));
        // 3 匹配历史订单数据详情信息是否存在订单表中---存在即设置到需要保存的订单详情列表
        for (K3HistoricalReturnOrder returnOrder : k3HistoricalReturnOrders) {
            List<K3ReturnOrderDetail> needSaveOrderDetails = new ArrayList<>();
            for (K3ReturnOrderDetail orderDetail : returnOrder.getK3ReturnOrderDetails()) {
                if (maps.containsKey(orderDetail.getOrderNo())) {
                    needSaveOrderDetails.add(orderDetail);
                }
            }
            returnOrder.setK3ReturnOrderDetails(needSaveOrderDetails);
        }
        // 4 返回过滤后的k3历史退货单信息
        return returnOrders;
    }

    /**
     * 保存退货单列表信息
     */
    private List<K3ReturnOrderDO> saveK3ReturnOrders(List<K3HistoricalReturnOrder> billDatas,StringBuffer info) {
        //待保存的退货单列表
        List<K3ReturnOrderDO> k3ReturnOrderDOList = new ArrayList<>();
        //待保存的退货单详情列表
        List<K3ReturnOrderDetailDO> waitSavek3ReturnOrderDetailDOList = new ArrayList<>();
        User loginUser = userSupport.getCurrentUser();
        String userId = null;
        if (loginUser != null) {
            userId = String.valueOf(loginUser.getUserId());
        }
        Map<String,OrderDO> orderCache = new HashMap<>();
        Set<String> notErpOrderNo = new HashSet<>();

        //退货详情缓存 key 为退货单号
        Map<String,List<K3ReturnOrderDetailDO>> k3ReturnOrderDetailDOMap = new HashMap<>();

        //没有退货单编号的退货单
        Integer noReturnOrderNoCount = 0 ;
        //没有发货分公司的退货单
        Map<String,K3ReturnOrderDO> map1 = new HashMap<>();
        //没有信用额度的退货单
        Map<String,K3ReturnOrderDO> map2 = new HashMap<>();
        //不含系统订单的的退货单或行号或订单项ID错误的退货单
        Map<String,K3ReturnOrderDO> map3 = new HashMap<>();
        //客户编号错误的退货单
        Map<String,K3ReturnOrderDO> map4 = new HashMap<>();

        //map3对应的错误原因
        Map<String,String> map5 = new HashMap<>();
        Map<String,K3ReturnOrderDO> k3ReturnOrderDOMap = new HashMap<>();

        for(K3HistoricalReturnOrder k3HistoricalReturnOrder : billDatas){

            K3ReturnOrder k3ReturnOrder = k3HistoricalReturnOrder.getK3ReturnOrder();
            K3ReturnOrderDO k3ReturnOrderDO = ConverterUtil.convert(k3ReturnOrder,K3ReturnOrderDO.class);
            String returnOrderNo = k3ReturnOrderDO.getReturnOrderNo();
            List<K3ReturnOrderDetail> k3ReturnOrderDetailList = k3HistoricalReturnOrder.getK3ReturnOrderDetails();
            if(StringUtil.isEmpty(returnOrderNo)){
                noReturnOrderNoCount++;
                continue;
            }
            //如果全部不是我们erp系统订单，则不处理该退货单
            boolean allNotErpOrder = true;
            for(K3ReturnOrderDetail k3ReturnOrderDetail : k3ReturnOrderDetailList){
                if(notErpOrderNo.contains(k3ReturnOrderDetail.getOrderNo())){
                    continue;
                }else if(!orderCache.containsKey(k3ReturnOrderDetail.getOrderNo())){
                    OrderDO orderDO = orderMapper.findByOrderNo(k3ReturnOrderDetail.getOrderNo());
                    if(orderDO==null){
                        notErpOrderNo.add(k3ReturnOrderDetail.getOrderNo());
                        continue;
                    }
                    orderCache.put(orderDO.getOrderNo(),orderDO);
                }

                allNotErpOrder = false;
                if(k3ReturnOrderDetailDOMap.get(returnOrderNo)==null){
                    k3ReturnOrderDetailDOMap.put(returnOrderNo,new ArrayList<K3ReturnOrderDetailDO>());
                }
                OrderDO orderDO = orderCache.get(k3ReturnOrderDetail.getOrderNo());
                if(CommonConstant.COMMON_CONSTANT_YES.equals(orderDO.getIsK3Order())){
                    //如果是K3老订单则取行号
                    if(k3ReturnOrderDetail.getOrderEntry()==null){
                        map5.put(returnOrderNo,"K3老订单未传递行号");
                        continue;
                    }
                    Map<Integer,OrderMaterialDO> orderMaterialDOMap = ListUtil.listToMap(orderDO.getOrderMaterialDOList(),"FEntryID");
                    Map<Integer,OrderProductDO> orderProductDOMap = ListUtil.listToMap(orderDO.getOrderProductDOList(),"FEntryID");
                    Integer orderItemId = null;
                    if(productSupport.isMaterial(k3ReturnOrderDetail.getProductNo())){
                        OrderMaterialDO orderMaterialDO  = orderMaterialDOMap.get(Integer.parseInt(k3ReturnOrderDetail.getOrderEntry()));
                        if(orderMaterialDO!=null){
                            orderItemId = orderMaterialDO.getId();
                        }
                    }else{
                        OrderProductDO orderProductDO  = orderProductDOMap.get(Integer.parseInt(k3ReturnOrderDetail.getOrderEntry()));
                        if(orderProductDO!=null){
                            orderItemId = orderProductDO.getId();
                        }
                    }
                    if(orderItemId==null){
                        String itemString = productSupport.isMaterial(k3ReturnOrderDetail.getProductNo())?"配件项":"商品项";
                        map5.put(returnOrderNo,"K3老订单"+orderDO.getOrderNo()+"，"+itemString+"行号（"+k3ReturnOrderDetail.getOrderEntry()+"）没有对应的"+itemString+"ID");
                        continue;
                    }else{
                        k3ReturnOrderDetail.setOrderItemId(orderItemId.toString());
                    }
                }else{
                    //如果是erp系统订单
                    if(k3ReturnOrderDetail.getOrderItemId()==null){
                        map5.put(returnOrderNo,"erp系统订单需要orderItemId");
                        continue;
                    }
                }
                k3ReturnOrderDetailDOMap.get(returnOrderNo).add(ConverterUtil.convert(k3ReturnOrderDetail,K3ReturnOrderDetailDO.class));
            }
            if(allNotErpOrder){
                map3.put(returnOrderNo,k3ReturnOrderDO);
                continue;
            }
            if(k3ReturnOrder.getReturnReasonType()==null||k3ReturnOrder.getReturnReasonType()==0){
                //如果没有传退货原因类型，默认为11，其他
                k3ReturnOrderDO.setReturnReasonType(11);
            }
            if(k3ReturnOrder.getDeliverySubCompanyId()==null||k3ReturnOrder.getDeliverySubCompanyId()==0){
                map1.put(returnOrderNo,k3ReturnOrderDO);
                continue;
            }
            if(k3ReturnOrder.getEqAmount()==null){
                map2.put(returnOrderNo,k3ReturnOrderDO);
                continue;
            }
            K3MappingCustomerDO k3MappingCustomerDO = k3MappingCustomerMapper.findByK3Code(k3ReturnOrderDO.getK3CustomerNo());
            if(k3MappingCustomerDO!=null){
                k3ReturnOrderDO.setK3CustomerNo(k3MappingCustomerDO.getErpCustomerCode());
            }else{
                map4.put(returnOrderNo,k3ReturnOrderDO);
                continue;
            }
            userId = userId==null?CommonConstant.SUPER_USER_ID.toString():userId;
            k3ReturnOrderDO.setCreateUser(userId);
            k3ReturnOrderDO.setUpdateUser(userId);
            k3ReturnOrderDO.setCreateTime(new Date());
            k3ReturnOrderDO.setUpdateTime(new Date());
            k3ReturnOrderDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
            k3ReturnOrderDO.setSuccessStatus(CommonConstant.COMMON_CONSTANT_NO);
            k3ReturnOrderDOList.add(k3ReturnOrderDO);
            k3ReturnOrderDOMap.put(k3ReturnOrderDO.getReturnOrderNo(),k3ReturnOrderDO);
//            k3ReturnOrderMapper.save(k3ReturnOrderDO);

            List<K3ReturnOrderDetailDO> k3ReturnOrderDetailDOList = k3ReturnOrderDetailDOMap.get(k3ReturnOrder.getReturnOrderNo());
            k3ReturnOrderDO.setK3ReturnOrderDetailDOList(k3ReturnOrderDetailDOList);
            for(K3ReturnOrderDetailDO k3ReturnOrderDetailDO : k3ReturnOrderDetailDOList){
                if(ReturnOrderStatus.RETURN_ORDER_STATUS_END.equals(k3ReturnOrderDO.getReturnOrderStatus())){
                    k3ReturnOrderDetailDO.setRealProductCount(k3ReturnOrderDetailDO.getProductCount());
                }else{
                    k3ReturnOrderDetailDO.setRealProductCount(CommonConstant.COMMON_ZERO);
                }
//                k3ReturnOrderDetailDO.setReturnOrderId(k3ReturnOrderDO.getId());
                k3ReturnOrderDetailDO.setReturnOrderNo(k3ReturnOrderDO.getReturnOrderNo());
                k3ReturnOrderDetailDO.setCreateUser(userId);
                k3ReturnOrderDetailDO.setUpdateUser(userId);
                k3ReturnOrderDetailDO.setCreateTime(new Date());
                k3ReturnOrderDetailDO.setUpdateTime(new Date());
                k3ReturnOrderDetailDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                waitSavek3ReturnOrderDetailDOList.add(k3ReturnOrderDetailDO);
//                k3ReturnOrderDetailMapper.save(k3ReturnOrderDetailDO);
            }

        }
        if(map3.size()!=0){
            info.append(map3.size()+"条数据单项全部不是erp系统订单，无需处理：(退货单号)"+JSON.toJSONString(map3.keySet())+"\n");
        }
        if(noReturnOrderNoCount!=0){
            info.append(noReturnOrderNoCount+"条数据没有退货单号，\n");
        }
        if(map1.size()!=0){
            info.append(map1.size()+"条数据没有发货分公司：(退货单号)"+JSON.toJSONString(map1.keySet())+"\n");
        }
        if(map2.size()!=0){
            info.append(map2.size()+"条数据没有需恢复的信用额度：(退货单号)"+JSON.toJSONString(map2.keySet())+"\n");
        }
        if(map4.size()!=0){
            Set<String> customerNoSet = new HashSet<>();
            for(String key : map4.keySet()){
                K3ReturnOrderDO k3ReturnOrderDO = map4.get(key);
                customerNoSet.add(k3ReturnOrderDO.getK3CustomerNo()+"（"+k3ReturnOrderDO.getK3CustomerName()+"）");
            }
            info.append(map4.size()+"条数据客户编号错误：(退货单号)"+JSON.toJSONString(map4.keySet())+"(客户编号)"+JSON.toJSONString(customerNoSet)+"\n");
        }
        if(map5.size()!=0){
            info.append(map5.size()+"订单数据错误：(退货单号)"+JSON.toJSONString(map5)+"\n");
        }
        info.append("共需保存"+(billDatas.size()-map3.size())+"条数据，\n");
        info.append("由于数据错误而不保存的数据共"+(noReturnOrderNoCount+map1.size()+map2.size()+map4.size()+map5.size())+"条，\n");

        if(k3ReturnOrderDOList.size()>0){
            k3ReturnOrderMapper.saveList(k3ReturnOrderDOList);
        }
        for(K3ReturnOrderDetailDO k3ReturnOrderDetailDO : waitSavek3ReturnOrderDetailDOList){
            K3ReturnOrderDO k3ReturnOrderDO = k3ReturnOrderDOMap.get(k3ReturnOrderDetailDO.getReturnOrderNo());
            k3ReturnOrderDetailDO.setReturnOrderId(k3ReturnOrderDO.getId());
        }
        if(waitSavek3ReturnOrderDetailDOList.size()>0){
            k3ReturnOrderDetailMapper.saveList(waitSavek3ReturnOrderDetailDOList);
        }

        return k3ReturnOrderDOList;
    }


    /**
     * 保存退货单列表详情信息
     */
    private List<K3ReturnOrderDetailDO> saveK3ReturnOrderDetails(List<K3HistoricalReturnOrder> billDatas) {
        List<K3ReturnOrderDetail> k3ReturnOrderDetails = new ArrayList<>();
        for (K3HistoricalReturnOrder historicalReturnOrder : billDatas) {
            if (historicalReturnOrder.getK3ReturnOrderDetails() == null) {
                break;
            }
            k3ReturnOrderDetails.addAll(historicalReturnOrder.getK3ReturnOrderDetails());
        }
        if (k3ReturnOrderDetails == null || k3ReturnOrderDetails.size() == 0) {
            return null;
        }
        // 保存
        User loginUser = userSupport.getCurrentUser();
        String userId = null;
        if (loginUser != null) {
            userId = String.valueOf(loginUser.getUserId());
        }
        List<K3ReturnOrderDetailDO> k3ReturnOrderDetailDOs = ConverterUtil.convertList(k3ReturnOrderDetails, K3ReturnOrderDetailDO.class);
        for (K3ReturnOrderDetailDO k3ReturnOrderDetailDO : k3ReturnOrderDetailDOs) {
            if(k3ReturnOrderDetailDO.getReturnOrderId()==null){
                continue;
            }
            if (k3ReturnOrderDetailDO.getOrderItemId() == null) {
                k3ReturnOrderDetailDO.setOrderItemId(StringUtils.EMPTY);
            }
            k3ReturnOrderDetailDO.setCreateUser(userId);
            k3ReturnOrderDetailDO.setUpdateUser(userId);
            k3ReturnOrderDetailDO.setCreateTime(new Date());
            k3ReturnOrderDetailDO.setUpdateTime(new Date());
            k3ReturnOrderDetailDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
            k3ReturnOrderDetailMapper.save(k3ReturnOrderDetailDO);
        }
        return k3ReturnOrderDetailDOs;
    }

    private String getErrorMessage(com.lxzl.erp.core.k3WebServiceSdk.ERPServer_Models.ServiceResult response, K3SendRecordDO k3SendRecordDO) {
        StringBuffer sb = new StringBuffer(dingDingSupport.getEnvironmentString());
        sb.append("向K3推送【退货-").append(k3SendRecordDO.getRecordReferId()).append("】数据失败：");
        sb.append(JSON.toJSONString(response));
        return sb.toString();
    }


    @Autowired
    private K3ReturnOrderMapper k3ReturnOrderMapper;

    @Autowired
    private K3ReturnOrderDetailMapper k3ReturnOrderDetailMapper;

    @Autowired
    private UserSupport userSupport;

    @Autowired
    private WorkflowService workflowService;

    @Autowired
    private K3SendRecordMapper k3SendRecordMapper;

    @Autowired
    private PostK3ServiceManager postK3ServiceManager;

    @Autowired
    private DingDingSupport dingDingSupport;

    @Autowired
    private K3Service k3Service;

    @Autowired
    private CustomerService customerService;
    @Autowired
    private PermissionSupport permissionSupport;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderProductMapper orderProductMapper;

    @Autowired
    private OrderMaterialMapper orderMaterialMapper;

    @Autowired
    private SubCompanyMapper subCompanyMapper;
    @Autowired
    private ProductSupport productSupport;
    @Autowired
    private K3CallbackService k3CallbackService;
    @Autowired
    private K3MappingCustomerMapper k3MappingCustomerMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private K3MappingCategoryMapper k3MappingCategoryMapper;
    @Autowired
    private K3MappingBrandMapper k3MappingBrandMapper;
    @Autowired
    private MaterialMapper materialMapper;
    @Autowired
    private K3MappingMaterialTypeMapper k3MappingMaterialTypeMapper;

}
