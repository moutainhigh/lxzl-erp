package com.lxzl.erp.core.service.k3.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.k3.K3OrderQueryParam;
import com.lxzl.erp.common.domain.k3.pojo.order.Order;
import com.lxzl.erp.common.domain.k3.pojo.order.OrderConsignInfo;
import com.lxzl.erp.common.domain.k3.pojo.order.OrderMaterial;
import com.lxzl.erp.common.domain.k3.pojo.order.OrderProduct;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.common.util.FastJsonUtil;
import com.lxzl.erp.common.util.http.client.HttpClientUtil;
import com.lxzl.erp.common.util.http.client.HttpHeaderBuilder;
import com.lxzl.erp.core.service.k3.K3Service;
import com.lxzl.erp.dataaccess.dao.mysql.k3.K3MappingBrandMapper;
import com.lxzl.erp.dataaccess.dao.mysql.k3.K3MappingCategoryMapper;
import com.lxzl.erp.dataaccess.domain.k3.K3MappingBrandDO;
import com.lxzl.erp.dataaccess.domain.k3.K3MappingCategoryDO;
import com.lxzl.se.common.exception.BusinessException;
import com.lxzl.se.common.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2018-02-11 20:54
 */
@Service
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
            }
            if (param.getCreateEndTime() == null) {
                jsonObject.remove("createEndTime");
            }
            requestJson = jsonObject.toJSONString();
            String response = HttpClientUtil.post(k3OrderUrl, requestJson, headerBuilder, "UTF-8");

            logger.info("query charge page response:{}", response);
            JSONObject postResult = JSON.parseObject(response);

            JSONObject orderBills = (JSONObject) postResult.get("Data");
            List<JSONObject> k3OrderList = (List<JSONObject>) orderBills.get("bills");
            maxCount = (Integer) orderBills.get("maxCount");

            if (CollectionUtil.isNotEmpty(k3OrderList)) {
                for (JSONObject obj : k3OrderList) {
                    String orderBill = obj.get("OrderBill").toString();
                    Order order = JSON.parseObject(orderBill, Order.class);
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
                        orderProduct.setBrandName(k3MappingCategoryDO.getCategoryName());
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

        List<Order> orderList = new ArrayList<>();
        try {
            HttpHeaderBuilder headerBuilder = HttpHeaderBuilder.custom();
            headerBuilder.contentType("application/json");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("orderNo", orderNo);
            String requestJson = jsonObject.toJSONString();
            String response = HttpClientUtil.post(k3OrderUrl, requestJson, headerBuilder, "UTF-8");

            logger.info("query charge page response:{}", response);
            JSONObject postResult = JSON.parseObject(response);

            JSONObject orderBills = (JSONObject) postResult.get("Data");
            List<JSONObject> k3OrderList = (List<JSONObject>) orderBills.get("bills");

            if (CollectionUtil.isNotEmpty(k3OrderList)) {
                for (JSONObject obj : k3OrderList) {
                    String orderBill = obj.get("OrderBill").toString();
                    Order order = JSON.parseObject(orderBill, Order.class);
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
                        orderList.add(order);
                        break;
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e.getMessage());
        }
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(CollectionUtil.isNotEmpty(orderList) ? orderList.get(0) : null);
        return result;
    }


    @Autowired
    private K3MappingBrandMapper k3MappingBrandMapper;

    @Autowired
    private K3MappingCategoryMapper k3MappingCategoryMapper;
}
