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
import com.lxzl.erp.dataaccess.dao.mysql.k3.K3ReturnOrderDetailMapper;
import com.lxzl.erp.dataaccess.dao.mysql.k3.K3ReturnOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.k3.K3SendRecordMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.OrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.OrderMaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.OrderProductMapper;
import com.lxzl.erp.dataaccess.domain.company.SubCompanyDO;
import com.lxzl.erp.dataaccess.domain.k3.K3SendRecordDO;
import com.lxzl.erp.dataaccess.domain.k3.returnOrder.K3ReturnOrderDO;
import com.lxzl.erp.dataaccess.domain.k3.returnOrder.K3ReturnOrderDetailDO;
import com.lxzl.erp.dataaccess.domain.order.OrderDO;
import com.lxzl.erp.dataaccess.domain.order.OrderMaterialDO;
import com.lxzl.erp.dataaccess.domain.order.OrderProductDO;
import com.lxzl.se.common.exception.BusinessException;
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
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, String> sendReturnOrderToK3(String returnOrderNo) {
        ServiceResult<String, String> result = new ServiceResult<>();
        User loginUser = userSupport.getCurrentUser();
        Date currentTime = new Date();

        K3ReturnOrderDO k3ReturnOrderDO = k3ReturnOrderMapper.findByNo(returnOrderNo);
        if (k3ReturnOrderDO == null) {
            result.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return result;
        }
        IERPService service = null;
        K3SendRecordDO k3SendRecordDO = null;
        com.lxzl.erp.core.k3WebServiceSdk.ERPServer_Models.ServiceResult response = null;
        try {
            k3SendRecordDO = k3SendRecordMapper.findByReferIdAndType(k3ReturnOrderDO.getId(), PostK3Type.POST_K3_TYPE_RETURN_ORDER);
            ConvertK3DataService convertK3DataService = postK3ServiceManager.getService(PostK3Type.POST_K3_TYPE_RETURN_ORDER);
            K3ReturnOrder k3ReturnOrder = ConverterUtil.convert(k3ReturnOrderDO, K3ReturnOrder.class);
            Object postData = convertK3DataService.getK3PostWebServiceData(null, k3ReturnOrder);
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
            service = new ERPServiceLocator().getBasicHttpBinding_IERPService();
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
                result.setErrorCode(ErrorCode.K3_RETURN_ORDER_FAIL);
                return result;
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
            result.setErrorCode(ErrorCode.K3_SERVER_ERROR);
            return result;
        }
        k3ReturnOrderDO.setReturnOrderStatus(ReturnOrderStatus.RETURN_ORDER_STATUS_PROCESSING);
        k3ReturnOrderDO.setUpdateTime(currentTime);
        k3ReturnOrderDO.setUpdateUser(loginUser.getUserId().toString());
        k3ReturnOrderMapper.update(k3ReturnOrderDO);
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
        maps.put("permissionParam", permissionSupport.getPermissionParam(PermissionType.PERMISSION_TYPE_USER));

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
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
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
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
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
        } catch (Exception e) {
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
    public ServiceResult<String, String> importK3HistoricalRefundList(K3ReturnOrderQueryParam k3ReturnOrderQueryParam) {
        ServiceResult<String, String> importResult = new ServiceResult<>();
        importResult.setErrorCode(ErrorCode.SUCCESS);
        // 从k3服务器获取历史退货单信息
        ServiceResult<String, String> k3HistoricalRefundListFromK3 = k3Service.queryK3HistoricalRefundList(k3ReturnOrderQueryParam);
        if (!ErrorCode.SUCCESS.equals(k3HistoricalRefundListFromK3.getErrorCode())) {
            importResult.setErrorCode(k3HistoricalRefundListFromK3.getErrorCode());
            importResult.setResult(k3HistoricalRefundListFromK3.getResult());
            return importResult;
        }
        String k3ResultJsonStr = k3HistoricalRefundListFromK3.getResult();
        if (logger.isInfoEnabled()) {
            logger.info("从k3获取到的历史退货单数据：" + k3ResultJsonStr);
        }
        // 数据处理
        List<K3HistoricalReturnOrder> billDatas = processHistoricalRefundData(k3ResultJsonStr);
        // 过滤退货单列表信息
        List<K3HistoricalReturnOrder> needSaveBillDatas = filterReturnOrders(billDatas);
        // 过滤退货单详情列表信息
        needSaveBillDatas = filterReturnOrderDetails(needSaveBillDatas);
        // 保存k3数据列表
        return saveBillDatas(needSaveBillDatas);
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
     * 保存k3历史退货订单数据
     */
    private ServiceResult<String, String> saveBillDatas(List<K3HistoricalReturnOrder> needSaveBillDatas) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        if (logger.isInfoEnabled()) {
            logger.info("需要保存的k3退货单数据为：" + JSONArray.toJSONString(needSaveBillDatas));
        }
        if (needSaveBillDatas == null || needSaveBillDatas.size() == 0) {
            return serviceResult;
        }
        // 保存退货订单数据
        List<K3ReturnOrderDO> k3ReturnOrderDOS = saveK3ReturnOrders(needSaveBillDatas);
        // 根据保存后的k3退货单列表信息设置其对应k3退货详情列表的returnOrderId的值
        for (K3ReturnOrderDO k3ReturnOrderDO : k3ReturnOrderDOS) {
            for (K3HistoricalReturnOrder k3HistoricalReturnOrder : needSaveBillDatas) {
                if (k3ReturnOrderDO.getReturnOrderNo().equals(k3HistoricalReturnOrder.getK3ReturnOrder().getReturnOrderNo())) {
                    k3HistoricalReturnOrder.setReturnOrderIdToDetails(k3ReturnOrderDO.getId());
                    break;
                }
            }
        }
        // 保存退货订单详情数据
        saveK3ReturnOrderDetails(needSaveBillDatas);
        // 保存k3回调接口的处理退货单数据
        Integer notSuccessCount = doCallbackReturnOrder(needSaveBillDatas);
        Integer successCount = needSaveBillDatas.size() - notSuccessCount;
        serviceResult.setErrorCode(ErrorCode.K3_HISTORICAL_RETURN_CODE, successCount, notSuccessCount);
        return serviceResult;
    }

    /**
     * 执行保存k3回调接口的处理退货单数据
     */
    private int doCallbackReturnOrder(List<K3HistoricalReturnOrder> needSaveBillDatas) {
        int notSuccessCount = 0;
        for (K3HistoricalReturnOrder k3HistoricalReturnOrder : needSaveBillDatas) {
            K3ReturnOrder k3ReturnOrder = k3HistoricalReturnOrder.getK3ReturnOrder();
            ServiceResult<String, String> callBackResult = k3CallbackService.callbackReturnOrder(k3ReturnOrder);
            if (!StringUtils.equals(ErrorCode.SUCCESS, callBackResult.getErrorCode())) {
                logger.info("调用回调接口失败：" + ErrorCode.getMessage(callBackResult.getErrorCode()));
                notSuccessCount++;
                // 不成功改变状态
                K3ReturnOrderDO k3ReturnOrderDO = k3ReturnOrderMapper.findByNo(k3ReturnOrder.getReturnOrderNo());
                k3ReturnOrderDO.setSuccessStatus(CommonConstant.COMMON_CONSTANT_NO);
                k3ReturnOrderMapper.update(k3ReturnOrderDO);
            }
        }
        return notSuccessCount;
    }

    /**
     * 获取需要保存的退货单信息
     */
    private List<K3HistoricalReturnOrder> filterReturnOrders(List<K3HistoricalReturnOrder> billDatas) {
        if (billDatas == null || billDatas.size() == 0) {
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
        // 获取需要新增的k3退货单数据
        List<K3HistoricalReturnOrder> needAddBillDatas = filterReturnOrders(billDatas, k3ReturnOrderDOS);

        return needAddBillDatas;
    }

    /**
     * 过滤k3退货订单列表信息
     */
    private List<K3HistoricalReturnOrder> filterReturnOrders(List<K3HistoricalReturnOrder> billDatas, List<K3ReturnOrderDO> k3ReturnOrderDOS) {
        List<K3HistoricalReturnOrder> needSavabillDatas = new ArrayList<>();
        List<K3HistoricalReturnOrder> notNeedSavabillDatas = new ArrayList<>();
        for (K3HistoricalReturnOrder billData : billDatas) {
            boolean needAddFlag = true;
            if (k3ReturnOrderDOS != null) {
                for (K3ReturnOrderDO returnOrderDO : k3ReturnOrderDOS) {
                    if (StringUtils.equals(returnOrderDO.getReturnOrderNo(), billData.getK3ReturnOrder().getReturnOrderNo())) {
                        needAddFlag = false;
                        break;
                    }
                }
            }
            // 校验该退货订单是否需要保存
            if (needAddFlag) {
                needAddFlag = isNeedSaveBill(billData);
            }
            // 需要add添加到需要保存的列表中
            if (needAddFlag) {
                needSavabillDatas.add(billData);
            } else {
                notNeedSavabillDatas.add(billData);
            }
        }
        logger.error("k3未保存的数据为：" + JSONArray.toJSONString(notNeedSavabillDatas));
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
    private List<K3HistoricalReturnOrder> filterReturnOrderDetails(final List<K3HistoricalReturnOrder> k3HistoricalReturnOrders) {
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
    private List<K3ReturnOrderDO> saveK3ReturnOrders(List<K3HistoricalReturnOrder> billDatas) {
        List<K3ReturnOrder> k3ReturnOrders = new ArrayList<>();
        for (K3HistoricalReturnOrder historicalReturnOrder : billDatas) {
            if (historicalReturnOrder.getK3ReturnOrder() == null) {
                break;
            }
            k3ReturnOrders.add(historicalReturnOrder.getK3ReturnOrder());
        }
        if (k3ReturnOrders == null || k3ReturnOrders.size() == 0) {
            return null;
        }
        User loginUser = userSupport.getCurrentUser();
        String userId = null;
        if (loginUser != null) {
            userId = String.valueOf(loginUser.getUserId());
        }

        List<K3ReturnOrderDO> k3ReturnOrderDOS = ConverterUtil.convertList(k3ReturnOrders, K3ReturnOrderDO.class);
        for (K3ReturnOrderDO k3ReturnOrderDO : k3ReturnOrderDOS) {
            k3ReturnOrderDO.setCreateUser(userId);
            k3ReturnOrderDO.setUpdateUser(userId);
            k3ReturnOrderDO.setCreateTime(new Date());
            k3ReturnOrderDO.setUpdateTime(new Date());
            k3ReturnOrderDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
            k3ReturnOrderDO.setSuccessStatus(CommonConstant.COMMON_CONSTANT_YES);
            k3ReturnOrderMapper.save(k3ReturnOrderDO);
        }

        return k3ReturnOrderDOS;
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
        String type = null;
        if ("erp-prod".equals(ApplicationConfig.application)) {
            type = "【线上环境】";
        } else if ("erp-dev".equals(ApplicationConfig.application)) {
            type = "【开发环境】";
        } else if ("erp-adv".equals(ApplicationConfig.application)) {
            type = "【预发环境】";
        } else if ("erp-test".equals(ApplicationConfig.application)) {
            type = "【测试环境】";
        }
        StringBuffer sb = new StringBuffer(type);
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

}
