package com.lxzl.erp.core.service.k3.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lxzl.erp.common.cache.CommonCache;
import com.lxzl.erp.common.constant.*;
import com.lxzl.erp.common.domain.K3Config;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.k3.K3OrderQueryParam;
import com.lxzl.erp.common.domain.k3.K3SendRecordBatchParam;
import com.lxzl.erp.common.domain.k3.K3SendRecordParam;
import com.lxzl.erp.common.domain.k3.QueryK3StockParam;
import com.lxzl.erp.common.domain.k3.pojo.K3ProductStock;
import com.lxzl.erp.common.domain.k3.pojo.K3SendRecord;
import com.lxzl.erp.common.domain.k3.pojo.order.Order;
import com.lxzl.erp.common.domain.k3.pojo.order.OrderConsignInfo;
import com.lxzl.erp.common.domain.k3.pojo.order.OrderMaterial;
import com.lxzl.erp.common.domain.k3.pojo.order.OrderProduct;
import com.lxzl.erp.common.domain.k3.pojo.returnOrder.K3ReturnOrderQueryParam;
import com.lxzl.erp.common.domain.material.pojo.Material;
import com.lxzl.erp.common.domain.order.ChangeOrderItemParam;
import com.lxzl.erp.common.domain.order.OrderConfirmChangeToK3Param;
import com.lxzl.erp.common.domain.product.pojo.Product;
import com.lxzl.erp.common.domain.product.pojo.ProductSku;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.common.util.*;
import com.lxzl.erp.common.util.http.client.HttpClientUtil;
import com.lxzl.erp.common.util.http.client.HttpHeaderBuilder;
import com.lxzl.erp.core.k3WebServiceSdk.ERPServer_Models.*;
import com.lxzl.erp.core.service.dingding.DingDingSupport.DingDingSupport;
import com.lxzl.erp.core.service.k3.K3Service;
import com.lxzl.erp.core.service.k3.K3Support;
import com.lxzl.erp.core.service.k3.WebServiceHelper;
import com.lxzl.erp.core.service.k3.support.RecordTypeSupport;
import com.lxzl.erp.core.service.order.OrderService;
import com.lxzl.erp.core.service.statement.StatementService;
import com.lxzl.erp.core.service.user.UserRoleService;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.dataaccess.dao.mysql.company.SubCompanyMapper;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerMapper;
import com.lxzl.erp.dataaccess.dao.mysql.k3.*;
import com.lxzl.erp.dataaccess.dao.mysql.material.MaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.*;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductMapper;
import com.lxzl.erp.dataaccess.dao.mysql.user.UserMapper;
import com.lxzl.erp.dataaccess.domain.company.SubCompanyDO;
import com.lxzl.erp.dataaccess.domain.customer.CustomerDO;
import com.lxzl.erp.dataaccess.domain.k3.*;
import com.lxzl.erp.dataaccess.domain.material.MaterialDO;
import com.lxzl.erp.dataaccess.domain.order.*;
import com.lxzl.erp.dataaccess.domain.product.ProductDO;
import com.lxzl.erp.dataaccess.domain.reletorder.ReletOrderDO;
import com.lxzl.erp.dataaccess.domain.reletorder.ReletOrderMaterialDO;
import com.lxzl.erp.dataaccess.domain.reletorder.ReletOrderProductDO;
import com.lxzl.erp.dataaccess.domain.user.UserDO;
import com.lxzl.se.common.exception.BusinessException;
import com.lxzl.se.common.util.StringUtil;
import com.lxzl.se.common.util.date.DateUtil;
import com.lxzl.se.dataaccess.mysql.config.PageQuery;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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

//    private String k3OrderUrl = K3Config.k3Server + "/order/list";
//    private String k3OrderDetailUrl = K3Config.k3Server + "/order/order";
//    // k3历史退货单url
//    private String k3HistoricalRefundListUrl = K3Config.k3Server + "/SEOutstock/list";
    String pw = "5113f85e846056594bed8e2ece8b1cbd";

//    private static final String k3ReletOrderUrl = "http://103.239.207.170:9090/OrderConfirml/OeletOrder";

    private static final String K3_RELET_ORDER_PW = "5113f85e846056594bed8e2ece8b1cbd";
    private static final Integer K3_RELET_ORDER_TYPE_NEW = 1; //订单类型  1新订单
    private static final Integer K3_RELET_ORDER_TYPE_OLD = 2; //订单类型  2老订单

    private static final Integer K3_RELET_ORDER_PRODUCT_TYPE_PRODUCT = 1;  //商品类型(1，商品 2，配件)
    private static final Integer K3_RELET_ORDER_PRODUCT_TYPE_MATERIAL = 2;

//    /**
//     * K3消息通讯线程
//     */
//    private ExecutorService k3ThreadExecutor = Executors.newCachedThreadPool(new ThreadFactoryDefault("sendK3Msg"));

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
            } else {
                K3MappingCustomerDO k3MappingCustomerDO = k3MappingCustomerMapper.findByErpCode(param.getBuyerCustomerNo());
                if (k3MappingCustomerDO != null && StringUtil.isNotBlank(k3MappingCustomerDO.getK3CustomerCode())) {
                    jsonObject.put("buyerCustomerNo", k3MappingCustomerDO.getK3CustomerCode());
                }
            }
            if (param.getSubCompanyId() == null) {
                jsonObject.remove("subCompanyId");
            }
            if (param.getRentType() == null) {
                jsonObject.remove("rentType");
            }
            if (param.getSubCompanyNo() == null) {
                jsonObject.remove("subCompanyNo");
            }
            if (param.getOrderSellerName() == null) {
                jsonObject.remove("orderSellerName");
            }
            if (userSupport.isSuperUser()) {
                jsonObject.remove("orderSellerId");
            } else {
                jsonObject.put("orderSellerId", userSupport.getCurrentUser().getRealName());
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
            if (!userSupport.isSuperUser()) {
                jsonObject.put("orderSellerName", userSupport.getCurrentUser().getRealName());
            }
            jsonObject.put("pw", pw);
            requestJson = jsonObject.toJSONString();
            String response = HttpClientUtil.post(K3Config.k3Server + "/order/list", requestJson, headerBuilder, "UTF-8");
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
                    K3MappingSubCompanyDO k3MappingSubCompanyDO = k3MappingSubCompanyMapper.findByK3Code(order.getOrderSubCompanyName());
                    if (k3MappingSubCompanyDO != null) {
                        SubCompanyDO subCompanyDO = subCompanyMapper.findBySubCompanyCode(k3MappingSubCompanyDO.getErpSubCompanyCode());
                        if (subCompanyDO != null) {
                            order.setOrderSubCompanyId(subCompanyDO.getId());
                        }
                        order.setOrderSubCompanyName(k3MappingSubCompanyDO.getSubCompanyName());
                    }

                    String measureList = obj.get("MeasureList").toString();
                    if (measureList != null && !"[]".equals(measureList)) {
                        List<OrderMaterial> orderMaterialList = JSON.parseArray(measureList, OrderMaterial.class);
                        convertOrderMaterial(orderMaterialList);
                        order.setOrderMaterialList(orderMaterialList);
                    }
                    String productList = obj.get("ProductList").toString();
                    if (productList != null && !"[]".equals(productList)) {
                        List<OrderProduct> orderProductList = JSON.parseArray(productList, OrderProduct.class);
                        convertOrderProduct(orderProductList);
                        order.setOrderProductList(orderProductList);
                    }
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
            jsonObject.put("pw", pw);
            String requestJson = jsonObject.toJSONString();
            String response = HttpClientUtil.post(K3Config.k3Server + "/order/order", requestJson, headerBuilder, "UTF-8");

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
                            List<com.lxzl.erp.common.domain.order.pojo.OrderProduct> dbOrderProductList = dbOrder.getOrderProductList();
                            Map<Integer, com.lxzl.erp.common.domain.order.pojo.OrderProduct> map = ListUtil.listToMap(dbOrderProductList, "orderProductId");
                            for (OrderProduct orderProduct : orderProductList) {
                                com.lxzl.erp.common.domain.order.pojo.OrderProduct dbOrderProduct = map.get(orderProduct.getOrderProductId());
                                if (dbOrderProduct != null) {
                                    orderProduct.setProductSkuId(dbOrderProduct.getProductSkuId());
                                    orderProduct.setProductSkuPropertyList(dbOrderProduct.getProductSkuPropertyList());
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
    public ServiceResult<String, Page<K3SendRecord>> queryK3SendRecord(K3SendRecordParam k3SendRecordParam) {
        ServiceResult<String, Page<K3SendRecord>> result = new ServiceResult<>();

        User loginUser = userSupport.getCurrentUser();
        //超级管理员权限控制
        if (!userRoleService.isSuperAdmin(loginUser.getUserId())) {
            result.setErrorCode(ErrorCode.USER_ROLE_IS_NOT_SUPER_ADMIN);
            return result;
        }

        PageQuery pageQuery = new PageQuery(k3SendRecordParam.getPageNo(), k3SendRecordParam.getPageSize());

        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        maps.put("k3SendRecordParam", k3SendRecordParam);

        Integer totalCount = k3SendRecordMapper.listCount(maps);
        List<K3SendRecordDO> k3SendRecordDOList = k3SendRecordMapper.listPage(maps);
        List<K3SendRecord> k3SendRecordList = ConverterUtil.convertList(k3SendRecordDOList, K3SendRecord.class);
        Page<K3SendRecord> page = new Page<>(k3SendRecordList, totalCount, k3SendRecordParam.getPageNo(), k3SendRecordParam.getPageSize());

        List<K3SendRecord> newK3SendRecordList = page.getItemList();
        for (int i = 0; i < page.getItemList().size(); i++) {
            Integer recordType = page.getItemList().get(i).getRecordType();
            Integer recordReferId = page.getItemList().get(i).getRecordReferId();
            newK3SendRecordList.get(i).setRecordReferNo(recordTypeSupport.getNoByRecordType(recordType, recordReferId));
        }
        page.setItemList(newK3SendRecordList);

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(page);
        return result;
    }

    @Override
    public ServiceResult<String, Integer> sendAgainK3SendRecord(K3SendRecord k3SendRecord) {
        ServiceResult<String, Integer> result = new ServiceResult<>();

        User loginUser = userSupport.getCurrentUser();
        //超级管理员权限控制
        if (!userRoleService.isSuperAdmin(loginUser.getUserId())) {
            result.setErrorCode(ErrorCode.USER_ROLE_IS_NOT_SUPER_ADMIN);
            return result;
        }

        K3SendRecordDO k3SendRecordDO = k3SendRecordMapper.findById(k3SendRecord.getK3SendRecordId());
        if (k3SendRecordDO == null) {
            result.setErrorCode(ErrorCode.K3_SEND_RECORD_ID_IS_NOT_EXISTS);
            return result;
        }
        Object data = recordTypeSupport.recordTypeAndRecordReferIdByClass(k3SendRecordDO.getRecordType(), k3SendRecordDO.getRecordReferId());

        if (PostK3Type.POST_K3_TYPE_ORDER.equals(k3SendRecordDO.getRecordType())) {
            com.lxzl.erp.common.domain.order.pojo.Order order = (com.lxzl.erp.common.domain.order.pojo.Order) data;
            if (OrderStatus.ORDER_STATUS_WAIT_DELIVERY.equals(order.getOrderStatus())) {
                webServiceHelper.post(PostK3OperatorType.POST_K3_OPERATOR_TYPE_ADD, k3SendRecordDO.getRecordType(), data, false);
            } else {
                result.setErrorCode(ErrorCode.ORDER_STATUS_ERROR);
                return result;
            }
        } else {
            webServiceHelper.post(PostK3OperatorType.POST_K3_OPERATOR_TYPE_NULL, k3SendRecordDO.getRecordType(), data, false);
        }

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(k3SendRecordDO.getRecordReferId());
        return result;
    }

    @Override
    public ServiceResult<String, Map<String, String>> batchSendDataToK3(K3SendRecordBatchParam k3SendRecordBatchParam) {
        ServiceResult<String, Map<String, String>> result = new ServiceResult<>();
        User loginUser = userSupport.getCurrentUser();
        //超级管理员权限控制
        if (!userRoleService.isSuperAdmin(loginUser.getUserId())) {
            result.setErrorCode(ErrorCode.USER_ROLE_IS_NOT_SUPER_ADMIN);
            return result;
        }
        K3SendRecordDO k3SendRecordDO = null;
        Map<String, String> strMap = new HashMap<>();
        if (PostK3Type.POST_K3_TYPE_CUSTOMER.equals(k3SendRecordBatchParam.getRecordType())) {
            List<CustomerDO> customerDOList = customerMapper.findByCustomerParam(k3SendRecordBatchParam.getStartTime(), k3SendRecordBatchParam.getEndTime());
            List<CustomerDO> successCustomerDOList = new ArrayList<>();
            List<CustomerDO> failCustomerDOList = new ArrayList<>();
            List<K3SendRecordDO> successK3SendRecordDOList = k3SendRecordMapper.findAllSuccessByType(PostK3Type.POST_K3_TYPE_CUSTOMER);
            Map<String, K3SendRecordDO> successK3SendRecordDOMap = ListUtil.listToMap(successK3SendRecordDOList, "recordReferId");
            for (int i = 0; i < customerDOList.size(); i++) {
                k3SendRecordDO = successK3SendRecordDOMap.get(customerDOList.get(i).getId());
                if (k3SendRecordDO != null) {
                    successCustomerDOList.add(customerDOList.get(i));
                } else {
                    failCustomerDOList.add(customerDOList.get(i));
                }
            }

            strMap = recordTypeSupport.customerK3SendRecord(customerDOList, successCustomerDOList, failCustomerDOList, k3SendRecordBatchParam.getBatchType(), k3SendRecordBatchParam.getIntervalTime());
        } else if (PostK3Type.POST_K3_TYPE_PRODUCT.equals(k3SendRecordBatchParam.getRecordType())) {
            List<ProductDO> productDOList = productMapper.findByProductParam(k3SendRecordBatchParam.getStartTime(), k3SendRecordBatchParam.getEndTime());
            List<ProductDO> successProductDOList = new ArrayList<>();
            List<ProductDO> failProductDOList = new ArrayList<>();
            List<K3SendRecordDO> successK3SendRecordDOList = k3SendRecordMapper.findAllSuccessByType(PostK3Type.POST_K3_TYPE_PRODUCT);
            Map<String, K3SendRecordDO> successK3SendRecordDOMap = ListUtil.listToMap(successK3SendRecordDOList, "recordReferId");
            for (int i = 0; i < productDOList.size(); i++) {
                k3SendRecordDO = successK3SendRecordDOMap.get(productDOList.get(i).getId());
                if (k3SendRecordDO != null) {
                    successProductDOList.add(productDOList.get(i));
                } else {
                    failProductDOList.add(productDOList.get(i));
                }
            }
            strMap = recordTypeSupport.productK3SendRecord(productDOList, successProductDOList, failProductDOList, k3SendRecordBatchParam.getBatchType(), k3SendRecordBatchParam.getIntervalTime());
        } else if (PostK3Type.POST_K3_TYPE_MATERIAL.equals(k3SendRecordBatchParam.getRecordType())) {
            List<MaterialDO> materialDOList = materialMapper.findByMaterialParam(k3SendRecordBatchParam.getStartTime(), k3SendRecordBatchParam.getEndTime());
            List<MaterialDO> successMaterialDOList = new ArrayList<>();
            List<MaterialDO> failMaterialDOList = new ArrayList<>();
            List<K3SendRecordDO> successK3SendRecordDOList = k3SendRecordMapper.findAllSuccessByType(PostK3Type.POST_K3_TYPE_MATERIAL);
            Map<String, K3SendRecordDO> successK3SendRecordDOMap = ListUtil.listToMap(successK3SendRecordDOList, "recordReferId");
            for (int i = 0; i < materialDOList.size(); i++) {
                k3SendRecordDO = successK3SendRecordDOMap.get(materialDOList.get(i).getId());
                if (k3SendRecordDO != null) {
                    successMaterialDOList.add(materialDOList.get(i));
                } else {
                    failMaterialDOList.add(materialDOList.get(i));
                }
            }
            strMap = recordTypeSupport.materialK3SendRecord(materialDOList, successMaterialDOList, failMaterialDOList, k3SendRecordBatchParam.getBatchType(), k3SendRecordBatchParam.getIntervalTime());
        } else if (PostK3Type.POST_K3_TYPE_USER.equals(k3SendRecordBatchParam.getRecordType())) {
            List<UserDO> userDOList = userMapper.findByUserParam(k3SendRecordBatchParam.getStartTime(), k3SendRecordBatchParam.getEndTime());
            List<UserDO> successUserDOList = new ArrayList<>();
            List<UserDO> failUserDOList = new ArrayList<>();
            List<K3SendRecordDO> successK3SendRecordDOList = k3SendRecordMapper.findAllSuccessByType(PostK3Type.POST_K3_TYPE_USER);
            Map<String, K3SendRecordDO> successK3SendRecordDOMap = ListUtil.listToMap(successK3SendRecordDOList, "recordReferId");
            for (int i = 0; i < userDOList.size(); i++) {
                k3SendRecordDO = successK3SendRecordDOMap.get(userDOList.get(i).getId());
                if (k3SendRecordDO != null) {
                    successUserDOList.add(userDOList.get(i));
                } else {
                    failUserDOList.add(userDOList.get(i));
                }
            }
            strMap = recordTypeSupport.userK3SendRecord(userDOList, successUserDOList, failUserDOList, k3SendRecordBatchParam.getBatchType(), k3SendRecordBatchParam.getIntervalTime());
        } else if (PostK3Type.POST_K3_TYPE_ORDER.equals(k3SendRecordBatchParam.getRecordType())) {
            List<OrderDO> orderDOList = orderMapper.findByOrderParam(k3SendRecordBatchParam.getStartTime(), k3SendRecordBatchParam.getEndTime());
            List<OrderDO> successOrderDOList = new ArrayList<>();
            List<OrderDO> failOrderDOList = new ArrayList<>();
            List<K3SendRecordDO> successK3SendRecordDOList = k3SendRecordMapper.findAllSuccessByType(PostK3Type.POST_K3_TYPE_ORDER);
            Map<String, K3SendRecordDO> successK3SendRecordDOMap = ListUtil.listToMap(successK3SendRecordDOList, "recordReferId");
            for (int i = 0; i < orderDOList.size(); i++) {
                k3SendRecordDO = successK3SendRecordDOMap.get(orderDOList.get(i).getId());
                if (k3SendRecordDO != null) {
                    successOrderDOList.add(orderDOList.get(i));
                } else {
                    failOrderDOList.add(orderDOList.get(i));
                }
            }
            strMap = recordTypeSupport.orderK3SendRecord(orderDOList, successOrderDOList, failOrderDOList, k3SendRecordBatchParam.getBatchType(), k3SendRecordBatchParam.getIntervalTime());
        }
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(strMap);
        return result;
    }


    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, String> transferOrder(K3OrderQueryParam param) {
        ServiceResult<String, String> result = new ServiceResult<>();

        Date currentTime = new Date();

        if (param.getSubCompanyId() != null) {
            K3MappingSubCompanyDO k3MappingSubCompanyDO = k3MappingSubCompanyMapper.findByErpCode(param.getSubCompanyId().toString());
            param.setSubCompanyNo(k3MappingSubCompanyDO.getK3SubCompanyCode());
        }

        ServiceResult<String, Page<Order>> queryOrderResult = queryAllOrder(param);
        if (ErrorCode.SUCCESS.equals(queryOrderResult.getErrorCode())) {
            Page<Order> orderPage = queryOrderResult.getResult();
            List<Order> orderList = orderPage.getItemList();

            if (CollectionUtil.isNotEmpty(orderList)) {
                for (Order k3Order : orderList) {
                    boolean verifyResult = verifyK3Order(k3Order);
                    if (!verifyResult) {
                        continue;
                    }

                    OrderDO dbOrderDO = orderMapper.findByOrderNo(k3Order.getOrderNo());
                    if (dbOrderDO != null) {
                        continue;
                    }

                    com.lxzl.erp.common.domain.order.pojo.Order order = new com.lxzl.erp.common.domain.order.pojo.Order();
                    BeanUtils.copyProperties(k3Order, order);

                    OrderDO orderDO = ConverterUtil.convert(order, OrderDO.class);
                    ServiceResult<String, Map<String, BigDecimal>> firstNeedPayAmountResult = statementService.calculateOrderFirstNeedPayAmount(orderDO);
                    Map<String, BigDecimal> map = firstNeedPayAmountResult.getResult();
                    if (ErrorCode.SUCCESS.equals(firstNeedPayAmountResult.getErrorCode())) {
                        orderDO.setFirstNeedPayAmount(map.get("thisNeedPayAmount,ALL"));
                    }
                    orderDO.setIsK3Order(CommonConstant.COMMON_CONSTANT_YES);
                    orderDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                    orderDO.setCreateTime(orderDO.getRentStartTime());
                    orderDO.setCreateUser(orderDO.getOrderSellerId().toString());
                    orderDO.setUpdateTime(orderDO.getRentStartTime());
                    orderDO.setUpdateUser(orderDO.getOrderSellerId().toString());

                    List<OrderProductDO> orderProductDOList = new ArrayList<>();
                    if (CollectionUtil.isNotEmpty(k3Order.getOrderProductList())) {
                        for (OrderProduct k3OrderProduct : k3Order.getOrderProductList()) {
                            OrderProductDO orderProductDO = new OrderProductDO();
                            BeanUtils.copyProperties(k3OrderProduct, orderProductDO);

                            ProductDO productDO = productMapper.findByK3ProductNo(k3OrderProduct.getProductNumber());

                            if (productDO != null) {
                                orderProductDO.setProductId(productDO.getId());
                                orderProductDO.setProductSkuId(0);
                                orderProductDO.setProductName(productDO.getProductName());
                                orderProductDO.setProductSkuSnapshot(FastJsonUtil.toJSONString(ConverterUtil.convert(productDO, Product.class)));
                            }

                            orderProductDO.setRentingProductCount(k3OrderProduct.getRentingProductCount());
                            orderProductDO.setDepositCycle(order.getDepositCycle());
                            orderProductDO.setPaymentCycle(order.getPaymentCycle());
                            orderProductDO.setProductNumber(k3OrderProduct.getProductNumber());
                            orderProductDO.setFEntryID(k3OrderProduct.getFEntryID());
                            orderProductDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                            orderProductDO.setCreateTime(orderDO.getRentStartTime());
                            orderProductDO.setCreateUser(orderDO.getOrderSellerId().toString());
                            orderProductDO.setUpdateTime(orderDO.getRentStartTime());
                            orderProductDO.setUpdateUser(orderDO.getOrderSellerId().toString());
                            orderProductDOList.add(orderProductDO);
                        }
                    }

                    orderDO.setOrderProductDOList(orderProductDOList);
                    // K3老订单，插入-1
                    orderDO.setDeliverySubCompanyId(-1);

                    List<OrderMaterialDO> orderMaterialDOList = new ArrayList<>();
                    if (CollectionUtil.isNotEmpty(k3Order.getOrderMaterialList())) {
                        for (OrderMaterial k3OrderMaterial : k3Order.getOrderMaterialList()) {
                            OrderMaterialDO orderMaterialDO = new OrderMaterialDO();
                            BeanUtils.copyProperties(k3OrderMaterial, orderMaterialDO);
                            MaterialDO materialDO = materialMapper.findByK3MaterialNo(k3OrderMaterial.getFNumber());

                            if (materialDO != null) {
                                orderMaterialDO.setMaterialId(materialDO.getId());
                                orderMaterialDO.setMaterialName(materialDO.getMaterialName());
                                orderMaterialDO.setMaterialSnapshot(FastJsonUtil.toJSONString(ConverterUtil.convert(materialDO, Material.class)));
                            }

                            orderMaterialDO.setRentingMaterialCount(k3OrderMaterial.getRentingMaterialCount());
                            orderMaterialDO.setOrderId(orderDO.getId());
                            orderMaterialDO.setDepositCycle(order.getDepositCycle());
                            orderMaterialDO.setPaymentCycle(order.getPaymentCycle());
                            orderMaterialDO.setProductNumber(k3OrderMaterial.getFNumber());
                            orderMaterialDO.setFEntryID(k3OrderMaterial.getFEntryID());
                            orderMaterialDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                            orderMaterialDO.setCreateTime(orderDO.getRentStartTime());
                            orderMaterialDO.setCreateUser(orderDO.getOrderSellerId().toString());
                            orderMaterialDO.setUpdateTime(orderDO.getRentStartTime());
                            orderMaterialDO.setUpdateUser(orderDO.getOrderSellerId().toString());
                            orderMaterialDOList.add(orderMaterialDO);
                        }
                    }

                    orderDO.setOrderMaterialDOList(orderMaterialDOList);

                    /*if (orderService.isCheckRiskManagement(orderDO)) {
                        CustomerRiskManagementDO customerRiskManagementDO = customerRiskManagementMapper.findByCustomerId(orderDO.getBuyerCustomerId());
                        if (customerRiskManagementDO == null) {
                            dingDingSupport.dingDingSendMessage(String.format("订单【%s】，风控信息不存在", k3Order.getOrderNo()));
                            continue;
                        }
                    }*/
                    orderService.calculateOrderProductInfo(orderDO.getOrderProductDOList(), orderDO);
                    orderService.calculateOrderMaterialInfo(orderDO.getOrderMaterialDOList(), orderDO);
                    orderMapper.save(orderDO);

                    for (OrderProductDO orderProductDO : orderDO.getOrderProductDOList()) {
                        orderProductDO.setOrderId(orderDO.getId());
                        orderProductMapper.save(orderProductDO);
                    }

                    for (OrderMaterialDO orderMaterialDO : orderDO.getOrderMaterialDOList()) {
                        orderMaterialDO.setOrderId(orderDO.getId());
                        orderMaterialMapper.save(orderMaterialDO);
                    }

                    OrderConsignInfo k3OrderConsignInfo = k3Order.getOrderConsignInfo();
                    com.lxzl.erp.common.domain.order.pojo.OrderConsignInfo orderConsignInfo = new com.lxzl.erp.common.domain.order.pojo.OrderConsignInfo();
                    BeanUtils.copyProperties(k3OrderConsignInfo, orderConsignInfo);

                    OrderConsignInfoDO orderConsignInfoDO = ConverterUtil.convert(orderConsignInfo, OrderConsignInfoDO.class);
                    orderConsignInfoDO.setOrderId(orderDO.getId());
                    orderConsignInfoDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                    orderConsignInfoDO.setCreateTime(orderDO.getRentStartTime());
                    orderConsignInfoDO.setCreateUser(orderDO.getOrderSellerId().toString());
                    orderConsignInfoDO.setUpdateTime(orderDO.getRentStartTime());
                    orderConsignInfoDO.setUpdateUser(orderDO.getOrderSellerId().toString());
                    orderConsignInfoMapper.save(orderConsignInfoDO);

                }
            }
            result.setErrorCode(ErrorCode.SUCCESS);
            return result;
        } else {
            result.setErrorCode(queryOrderResult.getErrorCode());
            return result;
        }
    }


    boolean verifyK3Order(Order k3Order) {

        int rentingCount = 0;
        // 订单没有在租数，要自动return false
        if (CollectionUtil.isNotEmpty(k3Order.getOrderProductList())) {
            for (OrderProduct orderProduct : k3Order.getOrderProductList()) {
                rentingCount += orderProduct.getRentingProductCount();
            }
        }
        if (CollectionUtil.isNotEmpty(k3Order.getOrderMaterialList())) {
            for (OrderMaterial orderMaterial : k3Order.getOrderMaterialList()) {
                rentingCount += orderMaterial.getRentingMaterialCount();
            }
        }
        if (rentingCount == 0) {
            return Boolean.FALSE;
        }



        K3MappingSubCompanyDO k3MappingSubCompanyDO = k3MappingSubCompanyMapper.findByK3Code(k3Order.getOrderSubCompanyName());
        if (k3MappingSubCompanyDO != null) {
            k3Order.setOrderSubCompanyId(Integer.parseInt(k3MappingSubCompanyDO.getK3SubCompanyCode()));
        }
        CustomerDO customerDO = customerMapper.findByName(k3Order.getBuyerCustomerName().trim());
        if (customerDO == null) {
            dingDingSupport.dingDingSendMessage(String.format("订单【%s】，客户不存在【%s】", k3Order.getOrderNo(), k3Order.getBuyerCustomerName()));
            return Boolean.FALSE;
        }
        k3Order.setBuyerCustomerNo(customerDO.getCustomerNo());
        k3Order.setBuyerCustomerId(customerDO.getId());


        // 校验K3传过来的订单是否合规，如果合规才存
        UserDO userDO = userMapper.findByUserRealName(k3Order.getOrderSellerName());
        if (userDO == null) {
            userDO = userMapper.findByUserId(customerDO.getOwner());
            if(userDO == null){
                dingDingSupport.dingDingSendMessage(String.format("订单【%s】，业务员不存在【%s】", k3Order.getOrderNo(), k3Order.getOrderSellerName()));
                return Boolean.FALSE;
            }
        }
        k3Order.setOrderSellerId(userDO.getId());
        return Boolean.TRUE;
    }

    @Override
    public ServiceResult<String, String> queryK3HistoricalRefundList(K3ReturnOrderQueryParam k3ReturnOrderQueryParam ,StringBuffer info) {
        if (k3ReturnOrderQueryParam == null) {
            k3ReturnOrderQueryParam = new K3ReturnOrderQueryParam();
            k3ReturnOrderQueryParam.setPageNo(1);
            k3ReturnOrderQueryParam.setPageSize(10);
        }
        ServiceResult<String, String> result = new ServiceResult<>();
        result.setErrorCode(ErrorCode.SUCCESS);
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("pageNo", k3ReturnOrderQueryParam.getPageNo());
        requestData.put("pageSize", k3ReturnOrderQueryParam.getPageSize());
        requestData.put("pw", pw);
        if (k3ReturnOrderQueryParam.getReturnStartTime() != null) {
            requestData.put("createStartTime", DateUtil.formatDate(k3ReturnOrderQueryParam.getReturnStartTime(), DateUtil.SHORT_DATE_FORMAT_STR));
        }
        if (k3ReturnOrderQueryParam.getReturnEndTime() != null) {
            requestData.put("createEndTime", DateUtil.formatDate(k3ReturnOrderQueryParam.getReturnEndTime(), DateUtil.SHORT_DATE_FORMAT_STR));
        }
        if (k3ReturnOrderQueryParam.getReturnOrderNo() != null) {
            requestData.put("returnOrderNo", k3ReturnOrderQueryParam.getReturnOrderNo());
        }
        String requestJson = JSONObject.toJSONString(requestData);
        info.append("获取历史退货单请求："+requestJson+"\n");
        HttpHeaderBuilder headerBuilder = HttpHeaderBuilder.custom();
        headerBuilder.contentType("application/json");
        try {
            String response = HttpClientUtil.post(K3Config.k3Server + "/SEOutstock/list", requestJson, headerBuilder, "UTF-8");
            result.setResult(response);
            return result;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new BusinessException(e.getMessage());
        }
    }

    @Override
    public ServiceResult<String, String> confirmOrder(OrderConfirmChangeToK3Param orderConfirmChangeToK3Param) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        OrderDO OrderDO = orderMapper.findByOrderNo(orderConfirmChangeToK3Param.getOrderNo());
        if (OrderDO == null){
            serviceResult.setErrorCode(ErrorCode.ORDER_NOT_EXISTS);
            return serviceResult;
        }
        FormSEOrderConfirml formSEOrderConfirml = new FormSEOrderConfirml();
        Map<String, Object> requestData = new HashMap<>();
        Map responseMap = new HashMap();
        String response = null;

        formSEOrderConfirml.setOrderNo(orderConfirmChangeToK3Param.getOrderNo());
        formSEOrderConfirml.setPW(pw);

        List<ChangeOrderItemParam> changeOrderItemParamList = orderConfirmChangeToK3Param.getChangeOrderItemParamList();
        if (CollectionUtil.isNotEmpty(changeOrderItemParamList)){
            List<FormSEOrderConfirmlEntry> entrys = new ArrayList<>();
            for (ChangeOrderItemParam changeOrderItemParam : changeOrderItemParamList){
                FormSEOrderConfirmlEntry formSEOrderConfirmlEntry = new FormSEOrderConfirmlEntry();
                formSEOrderConfirmlEntry.setOrderEntryId(changeOrderItemParam.getItemId());
                formSEOrderConfirmlEntry.setOrderItemType(changeOrderItemParam.getItemType());
                formSEOrderConfirmlEntry.setQty(new BigDecimal(changeOrderItemParam.getReturnCount()));
                entrys.add(formSEOrderConfirmlEntry);
            }
            formSEOrderConfirml.setEntrys(entrys);
        }
        requestData.put("formSEOrderConfirml",formSEOrderConfirml);
        String requestJson  = JSONObject.toJSONString(requestData);
        try{
            HttpHeaderBuilder headerBuilder = HttpHeaderBuilder.custom();
            headerBuilder.contentType("application/json");
            String k3confirmOrderUrl = K3Config.k3Server + "/OrderConfirml/ConfirmlOrder";  //k3确认收货url
            response = HttpClientUtil.post(k3confirmOrderUrl, requestJson, headerBuilder, "UTF-8");
            responseMap = JSONObject.parseObject(response,HashMap.class);
            if ("true".equals(responseMap.get("IsSuccess").toString())){
                serviceResult.setErrorCode(ErrorCode.SUCCESS);
                serviceResult.setResult(responseMap.get("Message").toString());
                return serviceResult;
            }else{
                serviceResult.setErrorCode(ErrorCode.K3_CONFIRM_ORDER_ERROR,responseMap.get("Message").toString());
                dingDingSupport.dingDingSendMessage(getErrorMessage(response,OrderDO.getOrderNo()));
                return serviceResult;
            }
        }catch (Exception e){
//            e.printStackTrace();
            dingDingSupport.dingDingSendMessage(getErrorMessage(response,OrderDO.getOrderNo()));
            serviceResult.setErrorCode(ErrorCode.K3_SERVER_ERROR);
            return serviceResult;
        }
    }

    private String getErrorMessage(String response, String orderNo) {
        StringBuffer sb = new StringBuffer(dingDingSupport.getEnvironmentString());
        sb.append("向K3推送【确认收货-").append(orderNo).append("】数据失败：");
        sb.append(JSON.toJSONString(response));
        return sb.toString();
    }

    @Override
    public ServiceResult<String, String> sendReletOrderInfoToK3(final ReletOrderDO reletOrderDO, final OrderDO orderDO) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        // 异步线程
//        k3ThreadExecutor.execute(new Runnable() {
//            @Override
//            public void run() {
//                sendReletOrderInfo(reletOrderDO,orderDO);
//
//            }
//        });
        sendReletOrderInfo(reletOrderDO,orderDO);
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }


    private String sendReletOrderInfo(ReletOrderDO reletOrderDO, OrderDO orderDO){
        Map<String, Object> requestData = new HashMap<>();
        Map responseMap = new HashMap();
        String response = null;
        ServiceResult<String, FormSEOrderOelet> k3ParamResult = getReletOrderToK3Param(reletOrderDO, orderDO);
        if (!ErrorCode.SUCCESS.equals(k3ParamResult.getErrorCode())) {
            return k3ParamResult.getErrorCode();
        }
        requestData.put("FormSEOrderOelet",k3ParamResult.getResult());
        String requestJson  = JSONObject.toJSONString(requestData);

        try{
            HttpHeaderBuilder headerBuilder = HttpHeaderBuilder.custom();
            headerBuilder.contentType("application/json");
            String k3ReletOrderUrl = K3Config.k3Server + "/OrderConfirml/OeletOrder";  //k3确认收货url
            response = HttpClientUtil.post(k3ReletOrderUrl, requestJson, headerBuilder, "UTF-8");
            responseMap = JSONObject.parseObject(response,HashMap.class);
            if ("true".equals(responseMap.get("IsSuccess").toString())){
                logger.info(responseMap.get("Message").toString());
                return ErrorCode.SUCCESS;
            }else{
//                serviceResult.setErrorCode(ErrorCode.K3_RELET_ORDER_ERROR,responseMap.get("Message").toString());
                StringBuffer sb = new StringBuffer(dingDingSupport.getEnvironmentString());
                sb.append("向K3推送【订单续租-").append(reletOrderDO.getOrderNo()).append("】数据失败：");
                sb.append(responseMap.get("Message").toString());
                sb.append("\r\n").append("请求参数：").append(requestJson);
                dingDingSupport.dingDingSendMessage(sb.toString());
                return ErrorCode.K3_RELET_ORDER_ERROR;
            }
        }catch (Exception e){
            StringBuffer sb = new StringBuffer(dingDingSupport.getEnvironmentString());
            sb.append("向K3推送【订单续租-").append(reletOrderDO.getOrderNo()).append("】数据失败：");
            sb.append(JSON.toJSONString(response));
            sb.append("\r\n").append("请求参数：").append(requestJson);
            dingDingSupport.dingDingSendMessage(sb.toString());
            return ErrorCode.K3_SERVER_ERROR;
        }
    }

    /**
     * 根据续租单和订单信息 获k3推送消息参数
     *
     * @author ZhaoZiXuan
     * @date 2018/6/1 19:08
     * @param
     * @return
     */
    private ServiceResult<String, FormSEOrderOelet> getReletOrderToK3Param(ReletOrderDO reletOrderDO, OrderDO orderDO){
        ServiceResult<String, FormSEOrderOelet> serviceResult = new ServiceResult<>();

        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        FormSEOrderOelet formSEOrderOelet = new FormSEOrderOelet();
        if (null == reletOrderDO || null == orderDO){
            serviceResult.setErrorCode(ErrorCode.PARAM_IS_ERROR);
            return serviceResult;
        }
        Integer orderType = orderDO.getIsK3Order() == CommonConstant.YES ? K3_RELET_ORDER_TYPE_OLD : K3_RELET_ORDER_TYPE_NEW;//订单类型（1-新订单  2-老订单）
        formSEOrderOelet.setOrderNo(reletOrderDO.getOrderNo());
        formSEOrderOelet.setPw(K3_RELET_ORDER_PW);
        formSEOrderOelet.setfAlterReason(reletOrderDO.getRemark());
        formSEOrderOelet.setOrderType(orderType);
        formSEOrderOelet.setfFetchDate(sdf.format(reletOrderDO.getExpectReturnTime()));

        List<FormSEOrderOeletEntry> entrys = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(reletOrderDO.getReletOrderProductDOList())){

            for (ReletOrderProductDO reletOrderProductDO : reletOrderDO.getReletOrderProductDOList()){

                FormSEOrderOeletEntry formSEOrderOeletEntry = new FormSEOrderOeletEntry();
                if (orderType == K3_RELET_ORDER_TYPE_OLD){
                    OrderProductDO orderProductDO = getOrderProductDOById(orderDO, reletOrderProductDO.getOrderProductId());
                    if (null == orderProductDO) {
                        serviceResult.setErrorCode(ErrorCode.PARAM_IS_ERROR);
                        return serviceResult;
                    }
                    formSEOrderOeletEntry.setOrderEntryId(orderProductDO.getFEntryID());
                }
                else {
                    formSEOrderOeletEntry.setOrderEntryId(reletOrderProductDO.getOrderProductId());
                }
                formSEOrderOeletEntry.setUnitPrice(reletOrderProductDO.getProductUnitAmount());
                formSEOrderOeletEntry.setOrderItemType(K3_RELET_ORDER_PRODUCT_TYPE_PRODUCT);
                entrys.add(formSEOrderOeletEntry);
            }

        }
        if (CollectionUtil.isNotEmpty(reletOrderDO.getReletOrderMaterialDOList())){
            for (ReletOrderMaterialDO reletOrderMaterialDO : reletOrderDO.getReletOrderMaterialDOList()){
                FormSEOrderOeletEntry formSEOrderOeletEntry = new FormSEOrderOeletEntry();
                if (orderType == K3_RELET_ORDER_TYPE_OLD){
                    OrderMaterialDO orderMaterialDO = getOrderMaterialDOById(orderDO, reletOrderMaterialDO.getOrderMaterialId());
                    if (null == orderMaterialDO) {
                        serviceResult.setErrorCode(ErrorCode.PARAM_IS_ERROR);
                        return serviceResult;
                    }
                    formSEOrderOeletEntry.setOrderEntryId(orderMaterialDO.getFEntryID());
                }
                else {
                    formSEOrderOeletEntry.setOrderEntryId(reletOrderMaterialDO.getOrderMaterialId());
                }
                formSEOrderOeletEntry.setUnitPrice(reletOrderMaterialDO.getMaterialUnitAmount());
                formSEOrderOeletEntry.setOrderItemType(K3_RELET_ORDER_PRODUCT_TYPE_MATERIAL);
                entrys.add(formSEOrderOeletEntry);
            }
        }
        formSEOrderOelet.setEntrys(entrys);
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(formSEOrderOelet);
        return serviceResult;
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

    @Override
    public ServiceResult<String, List<K3ProductStock>> queryK3Stock(QueryK3StockParam queryK3StockParam) {
        ServiceResult<String, List<K3ProductStock>> serviceResult = new ServiceResult<>();

        if(queryK3StockParam.getK3Code() == null){
            serviceResult.setErrorCode(ErrorCode.K3_SEL_STOCK_K3_CODE_NOT_NULL);
            return serviceResult;
        }

//        if(queryK3StockParam.getWarehouseType() == null){
//            queryK3StockParam.setWarehouseType(CommonConstant.K3_SEL_STOCK_WARE_TYPE_THREE);
//        }

        //如果是确认库存操作，并且客户数量为空的话就不能查询
        if(CommonConstant.COMMON_CONSTANT_YES.equals(queryK3StockParam.getQueryType()) && queryK3StockParam.getProductCount() == null){
            serviceResult.setErrorCode(ErrorCode.K3_SEL_STOCK_CUSTOMER_QUERY_COUNT_NOT_NULL_IN_CONFIRM_STOCK);
            return serviceResult;
        }

//        //如果是确认库存操作,并且仓库类型为借出仓和全部时，不能查询
//        if(CommonConstant.COMMON_CONSTANT_YES.equals(queryK3StockParam.getQueryType()) &&
//                (CommonConstant.K3_SEL_STOCK_WARE_TYPE_TWO.equals(queryK3StockParam.getWarehouseType()) || CommonConstant.K3_SEL_STOCK_WARE_TYPE_THREE.equals(queryK3StockParam.getWarehouseType()))){
//            serviceResult.setErrorCode(ErrorCode.K3_SEL_STOCK_CAN_NOT_QUERY_IN_CONFIRM_STOCK);
//            return serviceResult;
//        }

        //封装参数，方便给K3库存接口查询
        FromSEOrderConfirmlSelStock fromSEOrderConfirmlSelStock = new FromSEOrderConfirmlSelStock();
        K3MappingSubCompanyDO k3MappingSubCompanyDO = new K3MappingSubCompanyDO();
        if (queryK3StockParam.getSubCompanyId() != null){
            k3MappingSubCompanyDO = k3MappingSubCompanyMapper.findById(queryK3StockParam.getSubCompanyId());
            fromSEOrderConfirmlSelStock.setAcctKey(k3MappingSubCompanyDO.getK3SubCompanyCode()); //分公司仓位
        }
        fromSEOrderConfirmlSelStock.setfNumber(queryK3StockParam.getK3Code());     //物料编号
        fromSEOrderConfirmlSelStock.setWareType(queryK3StockParam.getWarehouseType()); //仓位类型

        //获取结果
        ServiceResult<String, String> queryK3Result = k3Support.queryK3Stock(fromSEOrderConfirmlSelStock);
        if (!ErrorCode.SUCCESS.equals(queryK3Result.getErrorCode())){
            serviceResult.setErrorCode(queryK3Result.getErrorCode());
            return serviceResult;
        }
        List<K3ProductStock> k3ProductStockList = new ArrayList<>();
        List queryList =   JSONArray.parseArray(queryK3Result.getResult());
        List<Map<String,Object>> listMap = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(queryList)){
            listMap = (List<Map<String,Object>>)queryList;
        }
        //对不同查询做判断
        if (CommonConstant.COMMON_CONSTANT_YES.equals(queryK3StockParam.getQueryType())){
            //确认库存操作
                for (Map<String,Object> k3StockMap : listMap){
                    if (MapUtils.isNotEmpty(k3StockMap)){
                        K3ProductStock k3ProductStock = new K3ProductStock();
                        if (queryK3StockParam.getProductCount() > (Integer)k3StockMap.get("FQty")){
                            k3ProductStock.setIsStockEnough(CommonConstant.COMMON_CONSTANT_NO);
                        }else{
                            k3ProductStock.setIsStockEnough(CommonConstant.COMMON_CONSTANT_YES);
                        }
                        k3ProductStock.setSubCompanyName(k3StockMap.get("StockName").toString());
                        k3ProductStockList.add(k3ProductStock);
                    }
                }
        }else{
            //查询库存操作
            for (Map<String,Object> k3StockMap : listMap){
                if (MapUtils.isNotEmpty(k3StockMap)){
                    K3ProductStock k3ProductStock = new K3ProductStock();
                    if (queryK3StockParam.getProductCount() != null){
                        if (queryK3StockParam.getProductCount() > (Integer)k3StockMap.get("FQty")){
                            k3ProductStock.setIsStockEnough(CommonConstant.COMMON_CONSTANT_NO);
                        }else{
                            k3ProductStock.setIsStockEnough(CommonConstant.COMMON_CONSTANT_YES);
                        }
                    }else{
                        k3ProductStock.setIsStockEnough(CommonConstant.COMMON_CONSTANT_YES);
                    }
                    k3ProductStock.setSubCompanyName(k3StockMap.get("StockName").toString());
                    k3ProductStock.setProductStockCount((Integer)k3StockMap.get("FQty"));
                    k3ProductStockList.add(k3ProductStock);
                }
            }
        }

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(k3ProductStockList);
        return serviceResult;
    }

    @Override
    public ServiceResult<String,String> testMachineOrderTurnRentOrder(com.lxzl.erp.common.domain.order.pojo.Order order) {
        ServiceResult<String,String> serviceResult = new ServiceResult<>();

        String response = null;
        Object postData = null;
        Map<String,Object> responseMap = new HashMap();
        try {
            postData = getTestMachineOrderToK3Param(order,PostK3OperatorType.POST_K3_OPERATOR_TYPE_ADD);
            String requestJson  = JSONObject.toJSONString(postData);
            HttpHeaderBuilder headerBuilder = HttpHeaderBuilder.custom();
            headerBuilder.contentType("application/json");
            String k3confirmOrderUrl = K3Config.k3Server + "/DataDelivery/ExchangeGoods";  //传递由测试机订单转租赁的订单信息给K3
            response = HttpClientUtil.post(k3confirmOrderUrl, requestJson, headerBuilder, "UTF-8");
            responseMap = JSONObject.parseObject(response,HashMap.class);
            if ("true".equals(responseMap.get("IsSuccess").toString())){
                serviceResult.setErrorCode(ErrorCode.SUCCESS);
                serviceResult.setResult(responseMap.get("Message").toString());

            }else{
                serviceResult.setErrorCode(ErrorCode.K3_TEST_MACHINE_ORDER_TURN_RENT_ORDER_ERROR);
                serviceResult.setResult(responseMap.get("Message").toString());
                dingDingSupport.dingDingSendMessage(getErrorMessageForTestMachineOrder(response,order.getOrderNo()));
            }
        }catch (Exception e){
            StringWriter errorInfo = new StringWriter();
            e.printStackTrace(new PrintWriter(errorInfo, true));
            dingDingSupport.dingDingSendMessage(errorInfo.toString());
            serviceResult.setErrorCode(ErrorCode.K3_SERVER_ERROR);
        }
        return serviceResult;
    }

    private String getErrorMessageForTestMachineOrder(String response, String orderNo) {
        StringBuffer sb = new StringBuffer(dingDingSupport.getEnvironmentString());
        sb.append("向K3推送【由测试机订单转租赁的订单-").append(orderNo).append("】数据失败：");
        sb.append(JSON.toJSONString(response));
        return sb.toString();
    }

    private Object getTestMachineOrderToK3Param(com.lxzl.erp.common.domain.order.pojo.Order erpOrder,Integer postK3OperatorType) {
        DataDeliveryOrder dataDeliveryOrder = new DataDeliveryOrder();

        if (PostK3OperatorType.POST_K3_OPERATOR_TYPE_UPDATE.equals(postK3OperatorType)) {
            dataDeliveryOrder.setIsReplace(true);// 是否覆盖
        } else if (PostK3OperatorType.POST_K3_OPERATOR_TYPE_ADD.equals(postK3OperatorType)) {
            dataDeliveryOrder.setIsReplace(false);
        }

        K3MappingCustomerDO k3MappingCustomerDO = k3MappingCustomerMapper.findByErpCode(erpOrder.getBuyerCustomerNo());
        if (k3MappingCustomerDO == null) {
            throw new BusinessException("需要先同步客户信息");
        }

        dataDeliveryOrder.setCustNumber(k3MappingCustomerDO.getK3CustomerCode());// 客户代码
        dataDeliveryOrder.setCustName(k3MappingCustomerDO.getCustomerName());// 客户名称
        dataDeliveryOrder.setBillNO(erpOrder.getOrderNo());// 单据编号
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        dataDeliveryOrder.setDate(sdf.format(erpOrder.getCreateTime()));// 日期

        String fetchStyleNumber = null;
        if (DeliveryMode.DELIVERY_MODE_EXPRESS.equals(erpOrder.getDeliveryMode())) {
            fetchStyleNumber = "FJH03";
        } else if (DeliveryMode.DELIVERY_MODE_SINCE.equals(erpOrder.getDeliveryMode())) {
            fetchStyleNumber = "FJH01";
        } else if (DeliveryMode.DELIVERY_MODE_LX_EXPRESS.equals(erpOrder.getDeliveryMode())) {
            fetchStyleNumber = "FJH02";
        }
        dataDeliveryOrder.setFetchStyleNumber(fetchStyleNumber);// 交货方式  FJH01 客户自提 FJH02 送货上门 FJH03 物流发货
        SubCompanyDO subCompanyDO = subCompanyMapper.findById(erpOrder.getOrderSubCompanyId());//所属分公司
        SubCompanyDO deliverySubCompanyDO = subCompanyMapper.findById(erpOrder.getDeliverySubCompanyId());//发货分公司
        SubCompanyDO orderSubCompanyDO = subCompanyMapper.findById(erpOrder.getOrderSubCompanyId());//如果是电销则为线上（XX）类型
        K3MappingSubCompanyDO k3MappingSubCompanyDO = k3MappingSubCompanyMapper.findByErpCode(subCompanyDO.getSubCompanyCode());
        K3MappingSubCompanyDO k3MappingDeliverySubCompanyDO = k3MappingSubCompanyMapper.findByErpCode(deliverySubCompanyDO.getSubCompanyCode());
        dataDeliveryOrder.setDeptNumber(k3MappingSubCompanyDO.getK3SubCompanyCode() + ".06");// 部门代码
        dataDeliveryOrder.setDeptName(k3MappingSubCompanyDO.getSubCompanyName() + "-" + "租赁业务部");
        Integer subCompanyId = userSupport.getCompanyIdByUser(erpOrder.getOrderSellerId());
        SubCompanyDO sellerSubCompanyDO = subCompanyMapper.findById(subCompanyId);
        String empNumber = k3Support.getK3CityCode(sellerSubCompanyDO.getSubCompanyCode()) + "." + erpOrder.getOrderSellerId();
        dataDeliveryOrder.setEmpNumber(k3Support.getK3UserCode(erpOrder.getOrderSellerId()));// 业务员代码
        dataDeliveryOrder.setEmpName(erpOrder.getOrderSellerName());// 业务员名称
        dataDeliveryOrder.setBillerName(erpOrder.getCreateUserRealName());// 制单人
        //主管用业务员代替
        dataDeliveryOrder.setManagerNumber(empNumber);//  主管代码
        dataDeliveryOrder.setManagerName(erpOrder.getOrderSellerName());//  主管名称
        //取待发货状态的订单的时间轴节点
        List<OrderTimeAxisDO> orderTimeAxisDOList = orderTimeAxisMapper.findByOrderId(erpOrder.getOrderId());
        if (CollectionUtil.isNotEmpty(orderTimeAxisDOList)) {
            for (OrderTimeAxisDO orderTimeAxisDO : orderTimeAxisDOList) {
                if (OrderStatus.ORDER_STATUS_WAIT_DELIVERY.equals(orderTimeAxisDO.getOrderStatus())) {
                    dataDeliveryOrder.setCheckDate(sdf.format(orderTimeAxisDO.getCreateTime()));// 审核日期
                    User user = CommonCache.userMap.get(Integer.parseInt(String.valueOf(orderTimeAxisDO.getCreateUser())));
                    dataDeliveryOrder.setCheckerName(user.getRealName());// 审核人
                }
            }
        }
        String remark = erpOrder.getBuyerRemark() == null ? "" : erpOrder.getBuyerRemark();
        dataDeliveryOrder.setExplanation(remark);// 摘要
        dataDeliveryOrder.setWillSendDate(sdf.format(erpOrder.getExpectDeliveryTime()));//交货日期
        if (RentLengthType.RENT_LENGTH_TYPE_LONG == erpOrder.getRentLengthType()) {
            dataDeliveryOrder.setOrderTypeNumber("L");
        } else if (OrderRentType.RENT_TYPE_DAY.equals(erpOrder.getRentType())) {
            dataDeliveryOrder.setOrderTypeNumber("R");// 订单类型 L	长租  R	短短租(天) X	销售   D	短租
        } else {
            dataDeliveryOrder.setOrderTypeNumber("D");
        }
        dataDeliveryOrder.setBusinessTypeNumber("ZY");// 经营类型  ZY	经营性租赁 RZ 融资性租赁
        if (CommonConstant.ELECTRIC_SALE_COMPANY_ID.equals(orderSubCompanyDO.getId())||
                CommonConstant.CHANNEL_CUSTOMER_COMPANY_ID.equals(orderSubCompanyDO.getId())) {
            dataDeliveryOrder.setOrderFromNumber("XS");// 订单来源 XS	线上 XX 线下
        } else {
            dataDeliveryOrder.setOrderFromNumber("XX");
        }

        dataDeliveryOrder.setDeliveryName(erpOrder.getOrderConsignInfo().getConsigneeName());// 提货人

        String provinceName = erpOrder.getOrderConsignInfo().getProvinceName() == null ? "" : erpOrder.getOrderConsignInfo().getProvinceName() + " ";
        String cityName = erpOrder.getOrderConsignInfo().getCityName() == null ? "" : erpOrder.getOrderConsignInfo().getCityName() + " ";
        String districtName = erpOrder.getOrderConsignInfo().getDistrictName() == null ? "" : erpOrder.getOrderConsignInfo().getDistrictName() + " ";
        String address = erpOrder.getOrderConsignInfo().getAddress() == null ? "" : erpOrder.getOrderConsignInfo().getAddress();
        address = provinceName + cityName + districtName + address;
        dataDeliveryOrder.setDeliveryAddress(address);// 交货地址
        dataDeliveryOrder.setDeliverPhone(erpOrder.getOrderConsignInfo().getConsigneePhone());// 收货人电话
        dataDeliveryOrder.setCompanyNumber(k3MappingSubCompanyDO.getK3SubCompanyCode());//所属分公司
        dataDeliveryOrder.setStatementDate(erpOrder.getStatementDate());//结算日
        //发货分公司
        if(erpOrder.getDeliverySubCompanyId()!=null&&erpOrder.getDeliverySubCompanyId()!=0&&k3MappingDeliverySubCompanyDO!=null){
            dataDeliveryOrder.setExecuteCompanyNumber(k3MappingDeliverySubCompanyDO.getK3SubCompanyCode()); //执行分公司
        }
        dataDeliveryOrder.setAreaPS("租赁");// 销售/租赁
        //对账部门代码填写订单部门
        dataDeliveryOrder.setAcctDeptNumber(dataDeliveryOrder.getDeptNumber());// 对账部门代码
        dataDeliveryOrder.setAcctDeptName(dataDeliveryOrder.getDeptName());// 对账部门
        if (erpOrder.getHighTaxRate() != null && erpOrder.getHighTaxRate() > 0) {
            dataDeliveryOrder.setInvoiceType("01");// 01:专票/02:普票/03:收据
        } else {
            dataDeliveryOrder.setInvoiceType("02");
        }

        int orderProductSize = CollectionUtil.isNotEmpty(erpOrder.getOrderProductList()) ? erpOrder.getOrderProductList().size() : 0;
        int orderMaterialSize = CollectionUtil.isNotEmpty(erpOrder.getOrderMaterialList()) ? erpOrder.getOrderMaterialList().size() : 0;
        int size = orderProductSize + orderMaterialSize;
        int index = 0;
        if (size > 0) {
            String startTime = sdf.format(erpOrder.getRentStartTime());
            String endTime = sdf.format(erpOrder.getExpectReturnTime());
            List<DataDeliveryOrderEntry> list = new ArrayList<>();
            if (CollectionUtil.isNotEmpty(erpOrder.getOrderProductList())) {
                for (com.lxzl.erp.common.domain.order.pojo.OrderProduct orderProduct : erpOrder.getOrderProductList()) {
                    Product product = JSON.parseObject(orderProduct.getProductSkuSnapshot(), Product.class);
                    ProductDO productDO = productMapper.findByProductId(orderProduct.getProductId()); //目前从DO里面取编码等，从快照里面取sku
                    K3MappingCategoryDO k3MappingCategoryDO = k3MappingCategoryMapper.findByErpCode(productDO.getCategoryId().toString());
                    K3MappingBrandDO k3MappingBrandDO = k3MappingBrandMapper.findByErpCode(productDO.getBrandId().toString());

//                    FormICItem formICItem = new FormICItem();
//                    formICItem.setModel(productDO.getProductModel());//型号名称
//                    formICItem.setName(productDO.getProductName());//商品名称
                    String number = "";
                    if (StringUtil.isNotEmpty(productDO.getK3ProductNo())) {
                        number = productDO.getK3ProductNo();
                    } else {
                        number = "10." + k3MappingCategoryDO.getK3CategoryCode() + "." + k3MappingBrandDO.getK3BrandCode() + "." + productDO.getProductModel();
                    }

                    if (CommonConstant.COMMON_CONSTANT_YES.equals(erpOrder.getIsPeer())) {
                        number = "90" + number.substring(2, number.length());
                    }
                    DataDeliveryOrderEntry dataDeliveryOrderEntry = new DataDeliveryOrderEntry();

                    dataDeliveryOrderEntry.setOrderItemId(orderProduct.getOrderProductId());
                    dataDeliveryOrderEntry.setNumber(number);//  设备代码
                    dataDeliveryOrderEntry.setName(product.getProductName());//  设备名称
                    dataDeliveryOrderEntry.setQty(new BigDecimal(orderProduct.getProductCount()));// 数量
                    dataDeliveryOrderEntry.setLeaseMonthCount(new BigDecimal(orderProduct.getRentTimeLength()));//  租赁月数
                    dataDeliveryOrderEntry.setPrice(orderProduct.getProductUnitAmount());//  含税单价
                    //计算平均税率
                    BigDecimal rate = BigDecimalUtil.add(BigDecimalUtil.mul(new BigDecimal(erpOrder.getHighTaxRate()), new BigDecimal(0.17d)), BigDecimalUtil.mul(new BigDecimal(erpOrder.getLowTaxRate()), new BigDecimal(0.06d)));
                    rate = rate.setScale(2, BigDecimal.ROUND_HALF_UP);
                    dataDeliveryOrderEntry.setAddRate(rate);//  税率
                    dataDeliveryOrderEntry.setAmount(orderProduct.getProductAmount());//  含税租赁金额

                    dataDeliveryOrderEntry.setDate(startTime);//  租赁开始日期
                    dataDeliveryOrderEntry.setEndDate(endTime);//  租赁截止日期
                    dataDeliveryOrderEntry.setYJMonthCount(new BigDecimal(orderProduct.getDepositCycle()));//  押金月数
                    dataDeliveryOrderEntry.setSFMonthCount(new BigDecimal(orderProduct.getPaymentCycle()));//  首付月数
                    dataDeliveryOrderEntry.setPayMonthCount(new BigDecimal(orderProduct.getPaymentCycle()));// 付款月数
                    dataDeliveryOrderEntry.setSFAmount(orderProduct.getFirstNeedPayRentAmount());//  首付租金
                    //暂时与设备配置名称用一个值
                    dataDeliveryOrderEntry.setEQConfigNumber(orderProduct.getProductSkuName());//  设备配置代码
//                    dataDeliveryOrderEntry.setEQConfigName(orderProduct.getProductSkuName());//  设备配置名称
                    dataDeliveryOrderEntry.setStartDate(startTime);//  起算日期
                    dataDeliveryOrderEntry.setYJAmount(orderProduct.getRentDepositAmount());//  租金押金金额
                    dataDeliveryOrderEntry.setEQYJAmount(orderProduct.getDepositAmount());//  设备押金金额
                    dataDeliveryOrderEntry.setPayAmountTotal(orderProduct.getFirstNeedPayAmount());//首付合计
                    ProductSku productSku = product.getProductSkuList().get(0);
                    dataDeliveryOrderEntry.setEQPrice(productSku.getSkuPrice());//  单台设备价值
                    dataDeliveryOrderEntry.setEQAmount(BigDecimalUtil.mul(productSku.getSkuPrice(), new BigDecimal(orderProduct.getProductCount())));//  设备价值
                    dataDeliveryOrderEntry.setSupplyNumber("");//  同行供应商
                    dataDeliveryOrderEntry.setOrderItemId(orderProduct.getOrderProductId());
                    if (OrderPayMode.PAY_MODE_PAY_AFTER.equals(orderProduct.getPayMode())) {
                        dataDeliveryOrderEntry.setPayMethodNumber("03");//  01	先付后用 02	先付后用(货到付款) 03	先用后付
                        dataDeliveryOrder.setPayMethodNumber("03");//订单里也要
                    } else if (OrderPayMode.PAY_MODE_PAY_BEFORE.equals(orderProduct.getPayMode())) {
                        dataDeliveryOrderEntry.setPayMethodNumber("01");
                        dataDeliveryOrder.setPayMethodNumber("03");//订单里也要
                    } else if (OrderPayMode.PAY_MODE_PAY_BEFORE_PERCENT.equals(orderProduct.getPayMode())) {
                        dataDeliveryOrderEntry.setPayMethodNumber("01");
                        dataDeliveryOrder.setPayMethodNumber("01");
                    }

                    if (OrderRentType.RENT_TYPE_DAY.equals(orderProduct.getRentType())) {
                        dataDeliveryOrderEntry.setStdPrice(productSku.getDayRentPrice());//  设备标准租金
                    } else if (OrderRentType.RENT_TYPE_MONTH.equals(orderProduct.getRentType())) {
                        dataDeliveryOrderEntry.setStdPrice(productSku.getMonthRentPrice());//  设备标准租金
                    }
                    if (CommonConstant.COMMON_CONSTANT_YES.equals(orderProduct.getIsNewProduct())) {
                        dataDeliveryOrderEntry.setEQType("N");//  新旧属性 N	新   O 次新 （字母）
                    } else if (CommonConstant.COMMON_CONSTANT_NO.equals(orderProduct.getIsNewProduct())) {
                        dataDeliveryOrderEntry.setEQType("O");
                    }
                    if (StringUtil.isEmpty(orderProduct.getRemark())) {
                        dataDeliveryOrderEntry.setNote("无");//  备注
                    } else {
                        dataDeliveryOrderEntry.setNote(orderProduct.getRemark());//  备注
                    }
                    dataDeliveryOrderEntry.setOriginalFBillNo(erpOrder.getTestMachineOrderNo());//原测试机订单号
                    if (erpOrder.getTestMachineOrderNo() != null && orderProduct.getTestMachineOrderProductId() != null) {
                        dataDeliveryOrderEntry.setOriginalFEntryId(orderProduct.getTestMachineOrderProductId());
                        dataDeliveryOrderEntry.setTransferQty(new BigDecimal(orderProduct.getProductCount()));
                    }
                    list.add(dataDeliveryOrderEntry);
                }
            }
            if (CollectionUtil.isNotEmpty(erpOrder.getOrderMaterialList())) {
                for (com.lxzl.erp.common.domain.order.pojo.OrderMaterial orderMaterial : erpOrder.getOrderMaterialList()) {
                    MaterialDO materialDO = materialMapper.findById(orderMaterial.getMaterialId());
                    K3MappingMaterialTypeDO k3MappingMaterialTypeDO = k3MappingMaterialTypeMapper.findByErpCode(materialDO.getMaterialType().toString());
                    K3MappingBrandDO k3MappingBrandDO = k3MappingBrandMapper.findByErpCode(materialDO.getBrandId().toString());
                    String number = "";
                    if (StringUtil.isNotEmpty(materialDO.getK3MaterialNo())) {
                        number = materialDO.getK3MaterialNo();
                    } else {
                        number = "20." + k3MappingMaterialTypeDO.getK3MaterialTypeCode() + "." + k3MappingBrandDO.getK3BrandCode() + "." + materialDO.getMaterialModel();
                    }
                    DataDeliveryOrderEntry dataDeliveryOrderEntry = new DataDeliveryOrderEntry();

                    dataDeliveryOrderEntry.setOrderItemId(orderMaterial.getOrderMaterialId());//ERP ID号
                    dataDeliveryOrderEntry.setNumber(number);//  设备代码
                    dataDeliveryOrderEntry.setName(materialDO.getMaterialName());//  设备名称
                    dataDeliveryOrderEntry.setQty(new BigDecimal(orderMaterial.getMaterialCount()));// 数量
                    dataDeliveryOrderEntry.setLeaseMonthCount(new BigDecimal(orderMaterial.getRentTimeLength()));//  租赁月数
                    dataDeliveryOrderEntry.setPrice(orderMaterial.getMaterialUnitAmount());//  含税单价
                    //计算平均税率
                    BigDecimal rate = BigDecimalUtil.add(BigDecimalUtil.mul(new BigDecimal(erpOrder.getHighTaxRate()), new BigDecimal(0.17d)), BigDecimalUtil.mul(new BigDecimal(erpOrder.getLowTaxRate()), new BigDecimal(0.06d)));

                    dataDeliveryOrderEntry.setAmount(orderMaterial.getMaterialAmount());//  含税租赁金额
                    dataDeliveryOrderEntry.setDate(startTime);//  租赁开始日期
                    dataDeliveryOrderEntry.setEndDate(endTime);//  租赁截止日期
                    dataDeliveryOrderEntry.setYJMonthCount(new BigDecimal(orderMaterial.getDepositCycle()));//  押金月数
                    dataDeliveryOrderEntry.setSFMonthCount(new BigDecimal(orderMaterial.getPaymentCycle()));//  首付月数
                    dataDeliveryOrderEntry.setPayMonthCount(new BigDecimal(orderMaterial.getPaymentCycle()));// 付款月数
                    dataDeliveryOrderEntry.setSFAmount(orderMaterial.getFirstNeedPayRentAmount());//  首付租金
                    dataDeliveryOrderEntry.setEQConfigNumber("");//  设备配置代码
//                    dataDeliveryOrderEntry.setEQConfigName("");//  设备配置名称
                    dataDeliveryOrderEntry.setStartDate(startTime);//  起算日期
                    dataDeliveryOrderEntry.setYJAmount(orderMaterial.getRentDepositAmount());//  租金押金金额
                    dataDeliveryOrderEntry.setEQYJAmount(orderMaterial.getDepositAmount());//设备押金金额
                    dataDeliveryOrderEntry.setPayAmountTotal(BigDecimalUtil.addAll(orderMaterial.getFirstNeedPayAmount()));// 首付合计
                    rate = rate.setScale(2, BigDecimal.ROUND_HALF_UP);
                    dataDeliveryOrderEntry.setAddRate(rate);//  税率
                    dataDeliveryOrderEntry.setEQPrice(materialDO.getMaterialPrice());//  单台设备价值
                    dataDeliveryOrderEntry.setEQAmount(BigDecimalUtil.mul(new BigDecimal(orderMaterial.getMaterialCount()), materialDO.getMaterialPrice()));//  设备价值
                    dataDeliveryOrderEntry.setSupplyNumber("");//  同行供应商
                    dataDeliveryOrderEntry.setOrderItemId(orderMaterial.getOrderMaterialId());
                    if (OrderPayMode.PAY_MODE_PAY_AFTER.equals(orderMaterial.getPayMode())) {
                        dataDeliveryOrderEntry.setPayMethodNumber("03");//  01	先付后用 02	先付后用(货到付款) 03	先用后付
                        dataDeliveryOrder.setPayMethodNumber("03");//订单里也要
                    } else if (OrderPayMode.PAY_MODE_PAY_BEFORE.equals(orderMaterial.getPayMode())) {
                        dataDeliveryOrderEntry.setPayMethodNumber("01");
                        dataDeliveryOrder.setPayMethodNumber("01");//订单里也要
                    } else if (OrderPayMode.PAY_MODE_PAY_BEFORE_PERCENT.equals(orderMaterial.getPayMode())) {
                        dataDeliveryOrderEntry.setPayMethodNumber("01");
                        dataDeliveryOrder.setPayMethodNumber("01");
                    }
                    if (OrderRentType.RENT_TYPE_DAY.equals(orderMaterial.getRentType())) {
                        dataDeliveryOrderEntry.setStdPrice(materialDO.getDayRentPrice());//  设备标准租金
                    } else if (OrderRentType.RENT_TYPE_MONTH.equals(orderMaterial.getRentType())) {
                        dataDeliveryOrderEntry.setStdPrice(materialDO.getMonthRentPrice());//  设备标准租金
                    }
                    if (CommonConstant.COMMON_CONSTANT_YES.equals(orderMaterial.getIsNewMaterial())) {
                        dataDeliveryOrderEntry.setEQType("N");//  新旧属性 N	新   O 次新 （字母）
                    } else if (CommonConstant.COMMON_CONSTANT_NO.equals(orderMaterial.getIsNewMaterial())) {
                        dataDeliveryOrderEntry.setEQType("O");
                    }
                    if (StringUtil.isEmpty(orderMaterial.getRemark())) {
                        dataDeliveryOrderEntry.setNote("无");//  备注
                    } else {
                        dataDeliveryOrderEntry.setNote(orderMaterial.getRemark());//  备注
                    }
                    dataDeliveryOrderEntry.setOriginalFBillNo(erpOrder.getTestMachineOrderNo()); //测试机订单号

                    if (erpOrder.getTestMachineOrderNo() != null && orderMaterial.getTestMachineOrderMaterialId() != null){
                        dataDeliveryOrderEntry.setOriginalFEntryId(orderMaterial.getTestMachineOrderMaterialId());//测试机订单行号
                        dataDeliveryOrderEntry.setTransferQty(new BigDecimal(orderMaterial.getMaterialCount()));//测试机明细原数量
                    }
                    list.add(dataDeliveryOrderEntry);
                }
            }
            dataDeliveryOrder.setEntrys(list);
        }
        return dataDeliveryOrder;
    }

    @Autowired
    private K3MappingBrandMapper k3MappingBrandMapper;

    @Autowired
    private K3MappingSubCompanyMapper k3MappingSubCompanyMapper;

    @Autowired
    private K3MappingCategoryMapper k3MappingCategoryMapper;

    @Autowired
    private K3MappingCustomerMapper k3MappingCustomerMapper;

    @Autowired
    private UserSupport userSupport;

    @Autowired
    private OrderService orderService;

    @Autowired
    private MaterialMapper materialMapper;

    @Autowired
    private OrderProductMapper orderProductMapper;

    @Autowired
    private OrderMaterialMapper orderMaterialMapper;

    @Autowired
    private OrderConsignInfoMapper orderConsignInfoMapper;

    @Autowired
    private StatementService statementService;

    @Autowired
    private K3SendRecordMapper k3SendRecordMapper;

    @Autowired
    private RecordTypeSupport recordTypeSupport;

    @Autowired
    private WebServiceHelper webServiceHelper;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DingDingSupport dingDingSupport;

    @Autowired
    private SubCompanyMapper subCompanyMapper;

    @Autowired
    private K3Support k3Support;

    @Autowired
    private OrderTimeAxisMapper orderTimeAxisMapper;

    @Autowired
    private K3MappingMaterialTypeMapper k3MappingMaterialTypeMapper;
}
