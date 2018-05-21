package com.lxzl.erp.core.service.k3.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lxzl.erp.common.constant.*;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.k3.K3OrderQueryParam;
import com.lxzl.erp.common.domain.k3.K3SendRecordBatchParam;
import com.lxzl.erp.common.domain.k3.K3SendRecordParam;
import com.lxzl.erp.common.domain.k3.pojo.K3SendRecord;
import com.lxzl.erp.common.domain.k3.pojo.order.Order;
import com.lxzl.erp.common.domain.k3.pojo.order.OrderConsignInfo;
import com.lxzl.erp.common.domain.k3.pojo.order.OrderMaterial;
import com.lxzl.erp.common.domain.k3.pojo.order.OrderProduct;
import com.lxzl.erp.common.domain.k3.pojo.returnOrder.K3ReturnOrderQueryParam;
import com.lxzl.erp.common.domain.material.pojo.Material;
import com.lxzl.erp.common.domain.product.pojo.Product;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.common.util.ConverterUtil;
import com.lxzl.erp.common.util.FastJsonUtil;
import com.lxzl.erp.common.util.ListUtil;
import com.lxzl.erp.common.util.http.client.HttpClientUtil;
import com.lxzl.erp.common.util.http.client.HttpHeaderBuilder;
import com.lxzl.erp.core.service.dingding.DingDingSupport.DingDingSupport;
import com.lxzl.erp.core.service.k3.K3Service;
import com.lxzl.erp.core.service.k3.WebServiceHelper;
import com.lxzl.erp.core.service.k3.support.RecordTypeSupport;
import com.lxzl.erp.core.service.order.OrderService;
import com.lxzl.erp.core.service.statement.StatementService;
import com.lxzl.erp.core.service.user.UserRoleService;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.dataaccess.dao.mysql.company.SubCompanyMapper;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerMapper;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerRiskManagementMapper;
import com.lxzl.erp.dataaccess.dao.mysql.k3.*;
import com.lxzl.erp.dataaccess.dao.mysql.material.MaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.OrderConsignInfoMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.OrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.OrderMaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.OrderProductMapper;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductMapper;
import com.lxzl.erp.dataaccess.dao.mysql.user.UserMapper;
import com.lxzl.erp.dataaccess.domain.company.SubCompanyDO;
import com.lxzl.erp.dataaccess.domain.customer.CustomerDO;
import com.lxzl.erp.dataaccess.domain.customer.CustomerRiskManagementDO;
import com.lxzl.erp.dataaccess.domain.k3.*;
import com.lxzl.erp.dataaccess.domain.k3.returnOrder.K3ReturnOrderDetailDO;
import com.lxzl.erp.dataaccess.domain.material.MaterialDO;
import com.lxzl.erp.dataaccess.domain.order.OrderConsignInfoDO;
import com.lxzl.erp.dataaccess.domain.order.OrderDO;
import com.lxzl.erp.dataaccess.domain.order.OrderMaterialDO;
import com.lxzl.erp.dataaccess.domain.order.OrderProductDO;
import com.lxzl.erp.dataaccess.domain.product.ProductDO;
import com.lxzl.erp.dataaccess.domain.user.UserDO;
import com.lxzl.se.common.exception.BusinessException;
import com.lxzl.se.common.util.StringUtil;
import com.lxzl.se.common.util.date.DateUtil;
import com.lxzl.se.dataaccess.mysql.config.PageQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
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

    private String k3OrderUrl = "http://103.239.207.170:9090/order/list";
    private String k3OrderDetailUrl = "http://103.239.207.170:9090/order/order";
    // k3历史退货单url
    private String k3HistoricalRefundListUrl = "http://103.239.207.170:9090/SEOutstock/list";
    String pw = "5113f85e846056594bed8e2ece8b1cbd";

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
            String response = HttpClientUtil.post(k3OrderDetailUrl, requestJson, headerBuilder, "UTF-8");

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
        String requestJson = JSONObject.toJSONString(requestData);
        info.append("获取历史退货单请求："+requestJson+"\n");
        HttpHeaderBuilder headerBuilder = HttpHeaderBuilder.custom();
        headerBuilder.contentType("application/json");
        try {
            String response = HttpClientUtil.post(k3HistoricalRefundListUrl, requestJson, headerBuilder, "UTF-8");
            result.setResult(response);
            return result;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new BusinessException(e.getMessage());
        }
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

}
