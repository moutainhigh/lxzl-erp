package com.lxzl.erp.core.service.k3.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.constant.OrderStatus;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.k3.K3OrderQueryParam;
import com.lxzl.erp.common.domain.k3.pojo.K3ChangeOrder;
import com.lxzl.erp.common.domain.k3.pojo.K3ChangeOrderDetail;
import com.lxzl.erp.common.domain.k3.pojo.changeOrder.K3ChangeOrderQueryParam;
import com.lxzl.erp.common.domain.k3.pojo.order.Order;
import com.lxzl.erp.common.domain.k3.pojo.order.OrderConsignInfo;
import com.lxzl.erp.common.domain.k3.pojo.order.OrderMaterial;
import com.lxzl.erp.common.domain.k3.pojo.order.OrderProduct;
import com.lxzl.erp.common.domain.k3.pojo.returnOrder.K3ReturnOrder;
import com.lxzl.erp.common.domain.k3.pojo.returnOrder.K3ReturnOrderDetail;
import com.lxzl.erp.common.domain.k3.pojo.returnOrder.K3ReturnOrderQueryParam;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.common.util.ConverterUtil;
import com.lxzl.erp.common.util.FastJsonUtil;
import com.lxzl.erp.common.util.ListUtil;
import com.lxzl.erp.common.util.http.client.HttpClientUtil;
import com.lxzl.erp.common.util.http.client.HttpHeaderBuilder;
import com.lxzl.erp.core.service.k3.K3Service;
import com.lxzl.erp.core.service.order.OrderService;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.dataaccess.dao.mysql.k3.*;
import com.lxzl.erp.dataaccess.domain.k3.K3ChangeOrderDO;
import com.lxzl.erp.dataaccess.domain.k3.K3ChangeOrderDetailDO;
import com.lxzl.erp.dataaccess.domain.k3.K3MappingBrandDO;
import com.lxzl.erp.dataaccess.domain.k3.K3MappingCategoryDO;
import com.lxzl.erp.dataaccess.domain.k3.K3MappingCustomerDO;
import com.lxzl.erp.dataaccess.domain.k3.returnOrder.K3ReturnOrderDO;
import com.lxzl.erp.dataaccess.domain.k3.returnOrder.K3ReturnOrderDetailDO;
import com.lxzl.erp.dataaccess.domain.order.OrderDO;
import com.lxzl.se.common.exception.BusinessException;
import com.lxzl.se.common.util.StringUtil;
import com.lxzl.se.common.util.UUIDUtil;
import com.lxzl.se.common.util.date.DateUtil;
import com.lxzl.se.dataaccess.mysql.config.PageQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2018-02-11 20:54
 */
@Service("k3Service")
public class K3ServiceImpl implements K3Service {

    private static final Logger logger = LoggerFactory.getLogger(K3ServiceImpl.class);

    private String k3OrderUrl = "http://103.239.207.170:9090/api/OrderSearch";

    @Override
    public ServiceResult<String, Page<Order>> queryAllOrder(K3OrderQueryParam param) {
        ServiceResult<String, Page<Order>> result = new ServiceResult<>();

        List<Order> orderList = new ArrayList<>();
        Integer maxCount;
        try {
            HttpHeaderBuilder headerBuilder = HttpHeaderBuilder.custom();
            headerBuilder.contentType("application/json");
            String requestJson = FastJsonUtil.toJSONString(param);
            JSONObject jsonObject = JSON.parseObject(requestJson);
            // 过滤
            if (StringUtil.isBlank(param.getOrderNo())) {
                jsonObject.remove("orderNo");
            }
            if (StringUtil.isBlank(param.getBuyerRealName())) {
                jsonObject.remove("buyerRealName");
            }
            if (StringUtil.isBlank(param.getBuyerCustomerNo())) {
                jsonObject.remove("buyerCustomerNo");
            }
            if (param.getSubCompanyId() == null) {
                jsonObject.remove("subCompanyId");
            }
            if (param.getRentType() == null) {
                jsonObject.remove("rentType");
            }
            if (param.getCreateStartTime() == null) {
                jsonObject.remove("createStartTime");
            } else {
                jsonObject.put("createStartTime", DateUtil.formatDate(param.getCreateStartTime(), DateUtil.SHORT_DATE_FORMAT_STR));
            }
            if (param.getCreateEndTime() == null) {
                jsonObject.remove("createEndTime");
            } else {
                jsonObject.put("createEndTime", DateUtil.formatDate(param.getCreateEndTime(), DateUtil.SHORT_DATE_FORMAT_STR));
            }
            requestJson = jsonObject.toJSONString();
            String response = HttpClientUtil.post(k3OrderUrl, requestJson, headerBuilder, "UTF-8");

            logger.info("query k3 order page response:{}", response);
            JSONObject postResult = JSON.parseObject(response);

            JSONObject orderBills = (JSONObject) postResult.get("Data");
            List<JSONObject> k3OrderList = (List<JSONObject>) orderBills.get("bills");
            maxCount = (Integer) orderBills.get("maxCount");

            if (CollectionUtil.isNotEmpty(k3OrderList)) {
                for (JSONObject obj : k3OrderList) {
                    String orderBill = obj.get("OrderBill").toString();
                    Order order = JSON.parseObject(orderBill, Order.class);
                    convertOrderInfo(order);
                    String address = obj.get("Address").toString();
                    OrderConsignInfo orderConsignInfo = JSON.parseObject(address, OrderConsignInfo.class);
                    orderConsignInfo.setConsigneePhone("");
                    order.setOrderConsignInfo(orderConsignInfo);
                    String measureList = obj.get("MeasureList").toString();
                    List<OrderMaterial> orderMaterialList = JSON.parseArray(measureList, OrderMaterial.class);
                    convertOrderMaterial(orderMaterialList);
                    order.setOrderMaterialList(orderMaterialList);
                    String productList = obj.get("ProductList").toString();
                    List<OrderProduct> orderProductList = JSON.parseArray(productList, OrderProduct.class);
                    convertOrderProduct(orderProductList);
                    order.setOrderProductList(orderProductList);

                    orderList.add(order);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e.getMessage());
        }
        Page<Order> page = new Page<>(orderList, maxCount, param.getPageNo(), param.getPageSize());
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(page);
        return result;
    }

    private void convertOrderInfo(Order order) {
        if (order.getOrderStatus() == null) {
            if (order.getOrderStatus() == 0) {
                order.setOrderStatus(OrderStatus.ORDER_STATUS_WAIT_COMMIT);
            } else if (order.getOrderStatus() == 1) {
                order.setOrderStatus(OrderStatus.ORDER_STATUS_WAIT_DELIVERY);
            } else if (order.getOrderStatus() == 2) {
                order.setOrderStatus(OrderStatus.ORDER_STATUS_DELIVERED);
            } else if (order.getOrderStatus() == 3) {
                order.setOrderStatus(OrderStatus.ORDER_STATUS_OVER);
            }
        }

        if (order.getBuyerCustomerNo() != null) {
            K3MappingCustomerDO k3MappingCustomerDO = k3MappingCustomerMapper.findByK3Code(order.getBuyerCustomerNo());
            if (k3MappingCustomerDO != null) {
                order.setBuyerCustomerNo(k3MappingCustomerDO.getErpCustomerCode());
            }
        }
    }

    private void convertOrderProduct(List<OrderProduct> orderProductList) {
        if (CollectionUtil.isNotEmpty(orderProductList)) {
            for (int i = 0; i < orderProductList.size(); i++) {
                OrderProduct orderProduct = orderProductList.get(i);
                String productNumber = orderProduct.getProductNumber();
                if (StringUtil.isBlank(productNumber)) {
                    continue;
                }
                String[] number = productNumber.split("\\.");
                if (number.length >= 2) {
                    K3MappingCategoryDO k3MappingCategoryDO = k3MappingCategoryMapper.findByK3Code(number[1]);
                    if (k3MappingCategoryDO != null) {
                        orderProduct.setCategoryName(k3MappingCategoryDO.getCategoryName());
                    }
                }
                if (number.length >= 3) {
                    K3MappingBrandDO k3MappingBrandDO = k3MappingBrandMapper.findByK3Code(number[2]);
                    if (k3MappingBrandDO != null) {
                        orderProduct.setBrandName(k3MappingBrandDO.getBrandName());
                    }
                }
            }
        }
    }

    private void convertOrderMaterial(List<OrderMaterial> orderMaterialList) {
        if (CollectionUtil.isNotEmpty(orderMaterialList)) {
            for (int i = 0; i < orderMaterialList.size(); i++) {
                OrderMaterial orderMaterial = orderMaterialList.get(i);
                String fNumber = orderMaterial.getFNumber();
                if (StringUtil.isBlank(fNumber)) {
                    continue;
                }
                String[] number = fNumber.split("\\.");
                if (number.length >= 2) {
                    K3MappingCategoryDO k3MappingCategoryDO = k3MappingCategoryMapper.findByK3Code(number[1]);
                    if (k3MappingCategoryDO != null) {
                        orderMaterial.setMaterialTypeStr(k3MappingCategoryDO.getCategoryName());
                    }
                }
                if (number.length >= 3) {
                    K3MappingBrandDO k3MappingBrandDO = k3MappingBrandMapper.findByK3Code(number[2]);
                    if (k3MappingBrandDO != null) {
                        orderMaterial.setBrandName(k3MappingBrandDO.getBrandName());
                    }
                }
            }
        }
    }

    @Override
    public ServiceResult<String, Order> queryOrder(String orderNo) {
        ServiceResult<String, Order> result = new ServiceResult<>();

        Order order = new Order();
        try {
            HttpHeaderBuilder headerBuilder = HttpHeaderBuilder.custom();
            headerBuilder.contentType("application/json");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("orderNo", orderNo);
            String requestJson = jsonObject.toJSONString();
            String response = HttpClientUtil.post(k3OrderUrl, requestJson, headerBuilder, "UTF-8");

            logger.info("query k3 order page response:{}", response);
            JSONObject postResult = JSON.parseObject(response);

            JSONObject orderBills = (JSONObject) postResult.get("Data");
            List<JSONObject> k3OrderList = (List<JSONObject>) orderBills.get("bills");
            if (CollectionUtil.isNotEmpty(k3OrderList)) {
                for (JSONObject obj : k3OrderList) {
                    String orderBill = obj.get("OrderBill").toString();
                    order = JSON.parseObject(orderBill, Order.class);
                    convertOrderInfo(order);
                    String address = obj.get("Address").toString();
                    OrderConsignInfo orderConsignInfo = JSON.parseObject(address, OrderConsignInfo.class);
                    orderConsignInfo.setConsigneePhone("");
                    order.setOrderConsignInfo(orderConsignInfo);
                    String measureList = obj.get("MeasureList").toString();
                    List<OrderMaterial> orderMaterialList = JSON.parseArray(measureList, OrderMaterial.class);
                    convertOrderMaterial(orderMaterialList);
                    order.setOrderMaterialList(orderMaterialList);
                    String productList = obj.get("ProductList").toString();
                    List<OrderProduct> orderProductList = JSON.parseArray(productList, OrderProduct.class);
                    convertOrderProduct(orderProductList);
                    order.setOrderProductList(orderProductList);

                    if (orderNo.equals(order.getOrderNo())) {
                        ServiceResult<String, com.lxzl.erp.common.domain.order.pojo.Order> erpOrderResult = orderService.queryOrderByNo(order.getOrderNo());
                        if (ErrorCode.SUCCESS.equals(erpOrderResult.getErrorCode())) {
                            com.lxzl.erp.common.domain.order.pojo.Order dbOrder = erpOrderResult.getResult();
                            order.setCreateUserRealName(dbOrder.getCreateUserRealName());
                            order.getOrderConsignInfo().setConsigneePhone(dbOrder.getOrderConsignInfo().getConsigneePhone());
                            order.setActualReturnTime(dbOrder.getActualReturnTime());
                            order.setHighTaxRate(dbOrder.getHighTaxRate());
                            order.setLowTaxRate(dbOrder.getLowTaxRate());
                            order.setOrderStatus(dbOrder.getOrderStatus());
                            List< com.lxzl.erp.common.domain.order.pojo.OrderProduct> dbOrderProductList = dbOrder.getOrderProductList();
                            Map<Integer ,com.lxzl.erp.common.domain.order.pojo.OrderProduct> map = ListUtil.listToMap(dbOrderProductList,"id");
                            for(OrderProduct orderProduct : orderProductList){
                                com.lxzl.erp.common.domain.order.pojo.OrderProduct dbOrderProduct = map.get(orderProduct.getOrderProductId());
                                if(dbOrderProduct!=null){
                                    orderProduct.setProductSkuId(dbOrderProduct.getProductSkuId());
                                    orderProduct.setProductSkuName(dbOrderProduct.getProductSkuName());
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e.getMessage());
        }
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(order);
        return result;
    }


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

        K3ReturnOrderDO k3ReturnOrderDO = ConverterUtil.convert(k3ReturnOrder, K3ReturnOrderDO.class);
        k3ReturnOrderDO.setReturnOrderNo(UUIDUtil.getUUID());
        k3ReturnOrderDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        k3ReturnOrderDO.setCreateTime(currentTime);
        k3ReturnOrderDO.setCreateUser(loginUser.getUserId().toString());
        k3ReturnOrderDO.setUpdateTime(currentTime);
        k3ReturnOrderDO.setUpdateUser(loginUser.getUserId().toString());
        k3ReturnOrderMapper.save(k3ReturnOrderDO);
        if (CollectionUtil.isNotEmpty(k3ReturnOrder.getK3ReturnOrderDetailList())) {
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
        }
        result.setResult(k3ReturnOrderDO.getReturnOrderNo());
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, String> updateReturnOrder(K3ReturnOrder k3ReturnOrder) {
        ServiceResult<String, String> result = new ServiceResult<>();
        User loginUser = userSupport.getCurrentUser();
        Date currentTime = new Date();

        if (k3ReturnOrder == null) {
            result.setErrorCode(ErrorCode.PARAM_IS_NOT_NULL);
            return result;
        }
        K3ReturnOrderDO dbK3ReturnOrderDO = k3ReturnOrderMapper.findByNo(k3ReturnOrder.getReturnOrderNo());
        if (dbK3ReturnOrderDO == null) {
            result.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return result;
        }


        K3ReturnOrderDO k3ReturnOrderDO = ConverterUtil.convert(k3ReturnOrder, K3ReturnOrderDO.class);
        k3ReturnOrderDO.setId(dbK3ReturnOrderDO.getId());
        k3ReturnOrderDO.setUpdateTime(currentTime);
        k3ReturnOrderDO.setUpdateUser(loginUser.getUserId().toString());
        k3ReturnOrderMapper.update(k3ReturnOrderDO);
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
        if (CollectionUtil.isEmpty(k3ReturnOrder.getK3ReturnOrderDetailList())) {
            result.setErrorCode(ErrorCode.PARAM_IS_NOT_ENOUGH);
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

        k3ReturnOrderDetailDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
        k3ReturnOrderDetailDO.setUpdateTime(currentTime);
        k3ReturnOrderDetailDO.setUpdateUser(loginUser.getUserId().toString());
        k3ReturnOrderDetailMapper.update(k3ReturnOrderDetailDO);

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

        result.setResult(ConverterUtil.convert(k3ReturnOrderDO, K3ReturnOrder.class));
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    public ServiceResult<String, String> sendToK3(String returnOrderNo) {
        ServiceResult<String, String> result = new ServiceResult<>();
        User loginUser = userSupport.getCurrentUser();
        Date currentTime = new Date();

        K3ReturnOrderDO k3ReturnOrderDO = k3ReturnOrderMapper.findByNo(returnOrderNo);
        if (k3ReturnOrderDO == null) {
            result.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return result;
        }

        k3ReturnOrderDO.setReturnOrderStatus(CommonConstant.COMMON_CONSTANT_YES);
        k3ReturnOrderDO.setUpdateTime(currentTime);
        k3ReturnOrderDO.setUpdateUser(loginUser.getUserId().toString());
        k3ReturnOrderMapper.update(k3ReturnOrderDO);
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, String> createChangeOrder(K3ChangeOrder k3ChangeOrder) {
        ServiceResult<String, String> result = new ServiceResult<>();
        User loginUser = userSupport.getCurrentUser();
        Date now = new Date();
        if (k3ChangeOrder == null) {
            result.setErrorCode(ErrorCode.PARAM_IS_NOT_NULL);
            return result;
        }
        K3ChangeOrderDO k3ChangeOrderDO = ConverterUtil.convert(k3ChangeOrder, K3ChangeOrderDO.class);
        k3ChangeOrderDO.setChangeOrderNo(UUIDUtil.getUUID());
        k3ChangeOrderDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        k3ChangeOrderDO.setChangeOrderStatus(CommonConstant.COMMON_CONSTANT_NO);
        k3ChangeOrderDO.setCreateTime(now);
        k3ChangeOrderDO.setUpdateTime(now);
        k3ChangeOrderDO.setCreateUser(loginUser.getUserId().toString());
        k3ChangeOrderDO.setUpdateUser(loginUser.getUserId().toString());
        k3ChangeOrderMapper.save(k3ChangeOrderDO);

        List<K3ChangeOrderDetail> k3ChangeOrderDetailList = k3ChangeOrder.getK3ChangeOrderDetailList();
        if (CollectionUtil.isNotEmpty(k3ChangeOrderDetailList)) {
            for (K3ChangeOrderDetail k3ChangeOrderDetail : k3ChangeOrderDetailList) {
                K3ChangeOrderDetailDO k3ChangeOrderDetailDO = ConverterUtil.convert(k3ChangeOrderDetail, K3ChangeOrderDetailDO.class);
                k3ChangeOrderDetailDO.setChangeOrderId(k3ChangeOrderDO.getId());
                k3ChangeOrderDetailDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                k3ChangeOrderDetailDO.setCreateTime(now);
                k3ChangeOrderDetailDO.setCreateUser(loginUser.getUserId().toString());
                k3ChangeOrderDetailDO.setUpdateTime(now);
                k3ChangeOrderDetailDO.setUpdateUser(loginUser.getUserId().toString());
                k3ChangeOrderDetailMapper.save(k3ChangeOrderDetailDO);
            }
        }

        result.setResult(k3ChangeOrderDO.getChangeOrderNo());
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, String> updateChangeOrder(K3ChangeOrder k3ChangeOrder) {
        ServiceResult<String, String> result = new ServiceResult<>();
        User loginUser = userSupport.getCurrentUser();
        Date currentTime = new Date();

        if (k3ChangeOrder == null) {
            result.setErrorCode(ErrorCode.PARAM_IS_NOT_NULL);
            return result;
        }
        K3ChangeOrderDO dbK3ChangeOrderDO = k3ChangeOrderMapper.findByNo(k3ChangeOrder.getChangeOrderNo());
        if (dbK3ChangeOrderDO == null) {
            result.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return result;
        }


        K3ChangeOrderDO k3ChangeOrderDO = ConverterUtil.convert(k3ChangeOrder, K3ChangeOrderDO.class);
        k3ChangeOrderDO.setId(dbK3ChangeOrderDO.getId());
        k3ChangeOrderDO.setUpdateTime(currentTime);
        k3ChangeOrderDO.setUpdateUser(loginUser.getUserId().toString());
        k3ChangeOrderMapper.update(k3ChangeOrderDO);
        result.setResult(k3ChangeOrderDO.getChangeOrderNo());
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, String> addChangeOrder(K3ChangeOrder k3ChangeOrder) {
        ServiceResult<String, String> result = new ServiceResult<>();
        User loginUser = userSupport.getCurrentUser();
        Date currentTime = new Date();
        K3ChangeOrderDO k3ChangeOrderDO = k3ChangeOrderMapper.findByNo(k3ChangeOrder.getChangeOrderNo());
        if (k3ChangeOrderDO == null) {
            result.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return result;
        }
        if (CollectionUtil.isEmpty(k3ChangeOrder.getK3ChangeOrderDetailList())) {
            result.setErrorCode(ErrorCode.PARAM_IS_NOT_ENOUGH);
            return result;
        }
        for (K3ChangeOrderDetail k3ChangeOrderDetail : k3ChangeOrder.getK3ChangeOrderDetailList()) {
            K3ChangeOrderDetailDO k3ChangeOrderDetailDO = ConverterUtil.convert(k3ChangeOrderDetail, K3ChangeOrderDetailDO.class);
            k3ChangeOrderDetailDO.setChangeOrderId(k3ChangeOrderDO.getId());
            k3ChangeOrderDetailDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
            k3ChangeOrderDetailDO.setCreateTime(currentTime);
            k3ChangeOrderDetailDO.setCreateUser(loginUser.getUserId().toString());
            k3ChangeOrderDetailDO.setUpdateTime(currentTime);
            k3ChangeOrderDetailDO.setUpdateUser(loginUser.getUserId().toString());
            k3ChangeOrderDetailMapper.save(k3ChangeOrderDetailDO);
        }

        result.setResult(k3ChangeOrderDO.getChangeOrderNo());
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    public ServiceResult<String, String> deleteChangeOrder(Integer k3ChangeOrderDetailId) {
        ServiceResult<String, String> result = new ServiceResult<>();
        User loginUser = userSupport.getCurrentUser();
        Date currentTime = new Date();
        K3ChangeOrderDetailDO k3ChangeOrderDetailDO = k3ChangeOrderDetailMapper.findById(k3ChangeOrderDetailId);
        if (k3ChangeOrderDetailDO == null) {
            result.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return result;
        }

        k3ChangeOrderDetailDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
        k3ChangeOrderDetailDO.setUpdateTime(currentTime);
        k3ChangeOrderDetailDO.setUpdateUser(loginUser.getUserId().toString());
        k3ChangeOrderDetailMapper.update(k3ChangeOrderDetailDO);

        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    public ServiceResult<String, Page<K3ChangeOrder>> queryChangeOrder(K3ChangeOrderQueryParam param) {
        ServiceResult<String, Page<K3ChangeOrder>> result = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(param.getPageNo(), param.getPageSize());

        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        maps.put("k3ChangeOrderQueryParam", param);

        Integer totalCount = k3ChangeOrderMapper.listCount(maps);
        List<K3ChangeOrderDO> orderDOList = k3ChangeOrderMapper.listPage(maps);
        List<K3ChangeOrder> orderList = ConverterUtil.convertList(orderDOList, K3ChangeOrder.class);
        Page<K3ChangeOrder> page = new Page<>(orderList, totalCount, param.getPageNo(), param.getPageSize());
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(page);
        return result;
    }

    @Override
    public ServiceResult<String, K3ChangeOrder> queryChangeOrderByNo(String changeOrderNo) {
        ServiceResult<String, K3ChangeOrder> result = new ServiceResult<>();

        K3ChangeOrderDO k3ChangeOrderDO = k3ChangeOrderMapper.findByNo(changeOrderNo);
        if (k3ChangeOrderDO == null) {
            result.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return result;
        }

        result.setResult(ConverterUtil.convert(k3ChangeOrderDO, K3ChangeOrder.class));
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    public ServiceResult<String, String> sendChangeOrderToK3(String changeOrderNo) {
        ServiceResult<String, String> result = new ServiceResult<>();
        User loginUser = userSupport.getCurrentUser();
        Date currentTime = new Date();

        K3ChangeOrderDO k3ChangeOrderDO = k3ChangeOrderMapper.findByNo(changeOrderNo);
        if (k3ChangeOrderDO == null) {
            result.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return result;
        }
        if (CollectionUtil.isEmpty(k3ChangeOrderDO.getK3ChangeOrderDetailDOList())) {
            result.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return result;
        }
        k3ChangeOrderDO.setChangeOrderStatus(CommonConstant.COMMON_CONSTANT_YES);
        k3ChangeOrderDO.setUpdateTime(currentTime);
        k3ChangeOrderDO.setUpdateUser(loginUser.getUserId().toString());
        k3ChangeOrderMapper.update(k3ChangeOrderDO);
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Autowired
    private K3MappingBrandMapper k3MappingBrandMapper;

    @Autowired
    private K3MappingCategoryMapper k3MappingCategoryMapper;

    @Autowired
    private K3ReturnOrderMapper k3ReturnOrderMapper;

    @Autowired
    private K3ChangeOrderMapper k3ChangeOrderMapper;

    @Autowired
    private K3ReturnOrderDetailMapper k3ReturnOrderDetailMapper;
    @Autowired
    private K3ChangeOrderDetailMapper k3ChangeOrderDetailMapper;

    @Autowired
    private K3MappingCustomerMapper k3MappingCustomerMapper;
    @Autowired
    private UserSupport userSupport;

    @Autowired
    private OrderService orderService;
}
