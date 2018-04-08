package com.lxzl.erp.web.test;

import com.alibaba.fastjson.JSON;
import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.domain.k3.pojo.returnOrder.K3ReturnOrder;
import com.lxzl.erp.common.domain.k3.pojo.returnOrder.K3ReturnOrderDetail;
import com.lxzl.erp.common.domain.product.pojo.Product;
import com.lxzl.erp.common.domain.product.pojo.ProductSkuProperty;
import com.lxzl.erp.common.util.ConverterUtil;
import com.lxzl.erp.dataaccess.domain.product.ProductCategoryPropertyValueDO;
import com.lxzl.erp.dataaccess.domain.product.ProductDO;
import com.lxzl.erp.dataaccess.domain.product.ProductSkuPropertyDO;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lk
 * @Description: TODO
 * @date 2018/3/15 17:54
 */
public class SimpleTest {
    @Test
    public void test (){

        Product product = JSON.parseObject("{\n" +
                "  \"productImgList\": [],\n" +
                "  \"productDescImgList\": [],\n" +
                "  \"productSkuList\": [\n" +
                "    {\n" +
                "      \"createTime\": 1520882878000,\n" +
                "      \"createUserRealName\": \"管理员\",\n" +
                "      \"customCode\": \"\",\n" +
                "      \"dataStatus\": 1,\n" +
                "      \"dayRentPrice\": 8.48,\n" +
                "      \"monthRentPrice\": 159,\n" +
                "      \"newDayRentPrice\": 8.48,\n" +
                "      \"newMonthRentPrice\": 159,\n" +
                "      \"newProductSkuCount\": 0,\n" +
                "      \"newSkuPrice\": 2910,\n" +
                "      \"oldProductSkuCount\": 0,\n" +
                "      \"productId\": 2000003,\n" +
                "      \"productMaterialList\": [],\n" +
                "      \"productSkuPropertyList\": [\n" +
                "        {\n" +
                "          \"createUserRealName\": \"管理员\",\n" +
                "          \"dataStatus\": 1,\n" +
                "          \"isSku\": 1,\n" +
                "          \"productId\": 2000003,\n" +
                "          \"propertyId\": 14,\n" +
                "          \"propertyName\": \"CPU\",\n" +
                "          \"propertyValueId\": 92,\n" +
                "          \"propertyValueName\": \"i5 3代\",\n" +
                "          \"skuId\": 1047,\n" +
                "          \"skuPropertyId\": 5070,\n" +
                "          \"updateUserRealName\": \"管理员\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"createUserRealName\": \"管理员\",\n" +
                "          \"dataStatus\": 1,\n" +
                "          \"isSku\": 1,\n" +
                "          \"productId\": 2000003,\n" +
                "          \"propertyId\": 15,\n" +
                "          \"propertyName\": \"内存\",\n" +
                "          \"propertyValueId\": 104,\n" +
                "          \"propertyValueName\": \"4G\",\n" +
                "          \"skuId\": 1047,\n" +
                "          \"skuPropertyId\": 5071,\n" +
                "          \"updateUserRealName\": \"管理员\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"createUserRealName\": \"管理员\",\n" +
                "          \"dataStatus\": 1,\n" +
                "          \"isSku\": 1,\n" +
                "          \"productId\": 2000003,\n" +
                "          \"propertyId\": 17,\n" +
                "          \"propertyName\": \"固态硬盘\",\n" +
                "          \"propertyValueId\": 114,\n" +
                "          \"propertyValueName\": \"120G\",\n" +
                "          \"skuId\": 1047,\n" +
                "          \"skuPropertyId\": 5072,\n" +
                "          \"updateUserRealName\": \"管理员\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"createUserRealName\": \"管理员\",\n" +
                "          \"dataStatus\": 1,\n" +
                "          \"isSku\": 1,\n" +
                "          \"productId\": 2000003,\n" +
                "          \"propertyId\": 18,\n" +
                "          \"propertyName\": \"显卡\",\n" +
                "          \"propertyValueId\": 342,\n" +
                "          \"propertyValueName\": \"独显\",\n" +
                "          \"skuId\": 1047,\n" +
                "          \"skuPropertyId\": 5073,\n" +
                "          \"updateUserRealName\": \"管理员\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"createUserRealName\": \"管理员\",\n" +
                "          \"dataStatus\": 1,\n" +
                "          \"isSku\": 1,\n" +
                "          \"productId\": 2000003,\n" +
                "          \"propertyId\": 19,\n" +
                "          \"propertyName\": \"尺寸\",\n" +
                "          \"propertyValueId\": 328,\n" +
                "          \"propertyValueName\": \"14.1\",\n" +
                "          \"skuId\": 1047,\n" +
                "          \"skuPropertyId\": 5074,\n" +
                "          \"updateUserRealName\": \"管理员\"\n" +
                "        }\n" +
                "      ],\n" +
                "      \"shouldProductCategoryPropertyValueList\": [\n" +
                "        {\n" +
                "          \"categoryId\": 800002,\n" +
                "          \"categoryPropertyValueId\": 92,\n" +
                "          \"materialModelId\": 8,\n" +
                "          \"materialType\": 3,\n" +
                "          \"propertyId\": 14,\n" +
                "          \"propertyName\": \"CPU\",\n" +
                "          \"propertyValueName\": \"i5 3代\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"categoryId\": 800002,\n" +
                "          \"categoryPropertyValueId\": 104,\n" +
                "          \"materialType\": 1,\n" +
                "          \"propertyCapacityValue\": 4096,\n" +
                "          \"propertyId\": 15,\n" +
                "          \"propertyName\": \"内存\",\n" +
                "          \"propertyValueName\": \"4G\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"categoryId\": 800002,\n" +
                "          \"categoryPropertyValueId\": 114,\n" +
                "          \"materialType\": 8,\n" +
                "          \"propertyCapacityValue\": 122880,\n" +
                "          \"propertyId\": 17,\n" +
                "          \"propertyName\": \"固态硬盘\",\n" +
                "          \"propertyValueName\": \"120G\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"categoryId\": 800002,\n" +
                "          \"categoryPropertyValueId\": 342,\n" +
                "          \"materialType\": 5,\n" +
                "          \"propertyId\": 18,\n" +
                "          \"propertyName\": \"显卡\",\n" +
                "          \"propertyValueName\": \"独显\"\n" +
                "        }\n" +
                "      ],\n" +
                "      \"skuId\": 1047,\n" +
                "      \"skuName\": \"CPU:i5 3代/内存:4G/固态硬盘:120G/显卡:独显/尺寸:14.1\",\n" +
                "      \"skuPrice\": 2910,\n" +
                "      \"stock\": 0,\n" +
                "      \"updateTime\": 1521023902000,\n" +
                "      \"updateUserRealName\": \"管理员\",\n" +
                "      \"rowId\": \"bleOzolj0\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"productPropertyList\": [\n" +
                "    {\n" +
                "      \"propertyId\": 22,\n" +
                "      \"propertyValueId\": 135,\n" +
                "      \"isSku\": 1,\n" +
                "      \"remark\": \"\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"productName\": \"ThinkPad T430\",\n" +
                "  \"categoryId\": 800002,\n" +
                "  \"subtitle\": \"次新机\",\n" +
                "  \"unit\": \"300016\",\n" +
                "  \"isRent\": \"1\",\n" +
                "  \"productDesc\": \"\",\n" +
                "  \"isReturnAnyTime\": \"0\",\n" +
                "  \"k3ProductNo\": \"10.LPC.TH.T430\",\n" +
                "  \"brandId\": 18,\n" +
                "  \"productModel\": \"T430\",\n" +
                "  \"productId\": \"2000003\"\n" +
                "}",Product.class);

        List<ProductSkuProperty> productSkuPropertyList = product.getProductPropertyList();
        for (ProductSkuProperty productSkuProperty : productSkuPropertyList) {
            ProductSkuPropertyDO productSkuPropertyDO = ConverterUtil.convert(productSkuProperty, ProductSkuPropertyDO.class);
            System.out.println();
//            if (CommonConstant.COMMON_DATA_OPERATION_TYPE_ADD.equals(operationType)) {
//                productSkuPropertyDO.setIsSku(CommonConstant.COMMON_CONSTANT_YES);
//                productSkuPropertyDO.setSkuId(skuId);
//                productSkuPropertyDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
//                productSkuPropertyDO.setCreateUser(loginUser.getUserId().toString());
//                productSkuPropertyDO.setUpdateUser(loginUser.getUserId().toString());
//                productSkuPropertyDO.setCreateTime(currentTime);
//                productSkuPropertyDO.setUpdateTime(currentTime);
//                ProductSkuPropertyDO dbProductSkuPropertyDO = productSkuPropertyMapper.findByProductSkuIdAndPropertyValue(skuId, productSkuPropertyDO.getPropertyValueId());
//                if (dbProductSkuPropertyDO == null) {
//                    productSkuPropertyMapper.save(productSkuPropertyDO);
//                }
//            } else if (CommonConstant.COMMON_DATA_OPERATION_TYPE_DELETE.equals(operationType)) {
//                deleteProductSkuPropertyDO(productSkuPropertyDO, loginUser, currentTime);
//            }
        }
        System.out.println();
    }

    @Test
    public void callbackReturnOrderRest() throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        K3ReturnOrder k3ReturnOrder = new K3ReturnOrder();
        k3ReturnOrder.setReturnOrderNo("LXK3RO20180328035118307");
        k3ReturnOrder.setUpdateUserRealName("喻晓艳");

        List<K3ReturnOrderDetail> k3ReturnOrderDetailList = new ArrayList<>();
        K3ReturnOrderDetail k3ReturnOrderDetail = new K3ReturnOrderDetail();
        k3ReturnOrderDetail.setOrderNo("LXO-20180328-1000-01286");
        k3ReturnOrderDetail.setOrderItemId("1953");
        k3ReturnOrderDetail.setRealProductCount(666);
        k3ReturnOrderDetailList.add(k3ReturnOrderDetail);

        k3ReturnOrder.setK3ReturnOrderDetailList(k3ReturnOrderDetailList);
        String customer = restTemplate.postForObject("http://192.168.10.94:8085/k3Callback/callbackReturnOrder",k3ReturnOrder,String.class);
    }
}
