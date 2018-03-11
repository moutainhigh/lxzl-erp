package com.lxzl.erp.core.service.k3.support;

import com.lxzl.erp.common.constant.*;
import com.lxzl.erp.common.domain.customer.pojo.Customer;
import com.lxzl.erp.common.domain.k3.pojo.returnOrder.K3ReturnOrder;
import com.lxzl.erp.common.domain.material.pojo.Material;
import com.lxzl.erp.common.domain.order.pojo.Order;
import com.lxzl.erp.common.domain.product.pojo.Product;
import com.lxzl.erp.common.domain.supplier.pojo.Supplier;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.common.util.ConverterUtil;
import com.lxzl.erp.core.service.k3.WebServiceHelper;
import com.lxzl.erp.core.service.order.OrderService;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerMapper;
import com.lxzl.erp.dataaccess.dao.mysql.k3.K3ReturnOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.k3.K3SendRecordMapper;
import com.lxzl.erp.dataaccess.dao.mysql.material.MaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.OrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductMapper;
import com.lxzl.erp.dataaccess.dao.mysql.supplier.SupplierMapper;
import com.lxzl.erp.dataaccess.dao.mysql.user.UserMapper;
import com.lxzl.erp.dataaccess.domain.customer.CustomerDO;
import com.lxzl.erp.dataaccess.domain.k3.K3SendRecordDO;
import com.lxzl.erp.dataaccess.domain.k3.returnOrder.K3ReturnOrderDO;
import com.lxzl.erp.dataaccess.domain.material.MaterialDO;
import com.lxzl.erp.dataaccess.domain.order.OrderDO;
import com.lxzl.erp.dataaccess.domain.product.ProductDO;
import com.lxzl.erp.dataaccess.domain.supplier.SupplierDO;
import com.lxzl.erp.dataaccess.domain.user.UserDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class RecordTypeSupport {

    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private SupplierMapper supplierMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private MaterialMapper materialMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private K3ReturnOrderMapper k3ReturnOrderMapper;
    @Autowired
    private OrderService orderService;
    @Autowired
    private WebServiceHelper webServiceHelper;
    @Autowired
    private K3SendRecordMapper k3SendRecordMapper;

    public String getNoByRecordType(Integer recordType,Integer recordReferId){

        String recordRefer = null;
        if(PostK3Type.POST_K3_TYPE_CUSTOMER.equals(recordType)){
            CustomerDO customerDO = customerMapper.findById(recordReferId);
            if(customerDO != null){
                recordRefer = customerDO.getCustomerNo();
            }
        }else if(PostK3Type.POST_K3_TYPE_PRODUCT.equals(recordType)){
            ProductDO productDO = productMapper.findById(recordReferId);
            if(productDO != null){
                recordRefer = productDO.getProductNo();
            }
        }else if(PostK3Type.POST_K3_TYPE_MATERIAL.equals(recordType)){
            MaterialDO materialDO = materialMapper.findById(recordReferId);
            if(materialDO != null){
                recordRefer = materialDO.getMaterialNo();
            }
        }else if(PostK3Type.POST_K3_TYPE_SUPPLIER.equals(recordType)){
            SupplierDO supplierDO = supplierMapper.findById(recordReferId);
            if(supplierDO != null){
                recordRefer = supplierDO.getSupplierNo();
            }
        }else if(PostK3Type.POST_K3_TYPE_ORDER.equals(recordType)){
            OrderDO orderDO = orderMapper.findById(recordReferId);
            if(orderDO != null){
                recordRefer = orderDO.getOrderNo();
            }
        }else if(PostK3Type.POST_K3_TYPE_USER.equals(recordType)){
            UserDO userDO = userMapper.findByUserId(recordReferId);
            if(userDO != null){
                recordRefer = userDO.getUserName();
            }
        }else if(PostK3Type.POST_K3_TYPE_RETURN_ORDER.equals(recordType)){
            K3ReturnOrderDO k3ReturnOrderDO = k3ReturnOrderMapper.findById(recordReferId);
            if(k3ReturnOrderDO != null){
                recordRefer = k3ReturnOrderDO.getReturnOrderNo();
            }
        }
        return recordRefer;
    }

    public Object recordTypeAndRecordReferIdByClass(Integer recordType,Integer recordReferId){

        if(PostK3Type.POST_K3_TYPE_CUSTOMER.equals(recordType)){
            CustomerDO customerDO = customerMapper.findById(recordReferId);
            if (CustomerType.CUSTOMER_TYPE_COMPANY.equals(customerDO.getCustomerType())) {
                customerDO = customerMapper.findCustomerCompanyByNo(customerDO.getCustomerNo());
            } else {
                customerDO = customerMapper.findCustomerPersonByNo(customerDO.getCustomerNo());
            }
            return ConverterUtil.convert(customerDO, Customer.class);
        }else if(PostK3Type.POST_K3_TYPE_PRODUCT.equals(recordType)){
            ProductDO productDO = productMapper.findByProductId(recordReferId);
            return ConverterUtil.convert(productDO, Product.class);
        }else if(PostK3Type.POST_K3_TYPE_MATERIAL.equals(recordType)){
            MaterialDO materialDO = materialMapper.findById(recordReferId);
            return ConverterUtil.convert(materialDO, Material.class);
        }else if(PostK3Type.POST_K3_TYPE_SUPPLIER.equals(recordType)){
            SupplierDO supplierDO = supplierMapper.findById(recordReferId);
            return ConverterUtil.convert(supplierDO, Supplier.class);
        }else if(PostK3Type.POST_K3_TYPE_ORDER.equals(recordType)){
            OrderDO orderDO = orderMapper.findByOrderId(recordReferId);
            Order order =  orderService.queryOrderByNo(orderDO.getOrderNo()).getResult();
            return order;
        }else if(PostK3Type.POST_K3_TYPE_USER.equals(recordType)){
            UserDO userDO = userMapper.findByUserId(recordReferId);
            return ConverterUtil.convert(userDO,User.class);
        }else if(PostK3Type.POST_K3_TYPE_RETURN_ORDER.equals(recordType)){
            K3ReturnOrderDO k3ReturnOrderDO = k3ReturnOrderMapper.findById(recordReferId);
            return ConverterUtil.convert(k3ReturnOrderDO,K3ReturnOrder.class);
        }
        return null;
    }

    public Map<String, String> customerK3SendRecord(List<CustomerDO> customerDOList, List<CustomerDO> successCustomerDOList, List<CustomerDO> failCustomerDOList, Integer batchType,Integer intervalTime){

        if(BatchType.BATCH_TYPE_TOTAL.equals(batchType)){
            return customerDOK3SendType(customerDOList,intervalTime);
        }else if(BatchType.BATCH_TYPE_SUCCESS.equals(batchType)){
            return customerDOK3SendType(successCustomerDOList,intervalTime);
        }else if(BatchType.BATCH_TYPE_FAIL.equals(batchType)){
            return customerDOK3SendType(failCustomerDOList,intervalTime);
        }
        return null;
    }

    public Map<String, String>  productK3SendRecord(List<ProductDO> productDOList,List<ProductDO> successProductDOList,List<ProductDO> failProductDOList,Integer batchType,Integer intervalTime){
        if(BatchType.BATCH_TYPE_TOTAL.equals(batchType)){
            return productDOK3SendType(productDOList,intervalTime);
        }else if(BatchType.BATCH_TYPE_SUCCESS.equals(batchType)){
            return productDOK3SendType(successProductDOList,intervalTime);
        }else if(BatchType.BATCH_TYPE_FAIL.equals(batchType)){
            return productDOK3SendType(failProductDOList,intervalTime);
        }
        return null;
    }

    public Map<String, String>  materialK3SendRecord(List<MaterialDO> materialDOList,List<MaterialDO> successMaterialDOList,List<MaterialDO> failMaterialDOList,Integer batchType,Integer intervalTime){
        if(BatchType.BATCH_TYPE_TOTAL.equals(batchType)){
            return materialDOK3SendType(materialDOList,intervalTime);
        }else if(BatchType.BATCH_TYPE_SUCCESS.equals(batchType)){
            return materialDOK3SendType(successMaterialDOList,intervalTime);
        }else if(BatchType.BATCH_TYPE_FAIL.equals(batchType)){
            return materialDOK3SendType(failMaterialDOList,intervalTime);
        }
        return null;
    }

    public Map<String, String>  userK3SendRecord(List<UserDO> userDOList,List<UserDO> successUserDOList,List<UserDO> failUserDOList,Integer batchType,Integer intervalTime){
        if(BatchType.BATCH_TYPE_TOTAL.equals(batchType)){
            return userDOK3SendType(userDOList,intervalTime);
        }else if(BatchType.BATCH_TYPE_SUCCESS.equals(batchType)){
            return userDOK3SendType(successUserDOList,intervalTime);
        }else if(BatchType.BATCH_TYPE_FAIL.equals(batchType)){
            return userDOK3SendType(failUserDOList,intervalTime);
        }
        return null;
    }

    public Map<String, String>  orderK3SendRecord(List<OrderDO> orderDOList, List<OrderDO> successOrderDOList, List<OrderDO> failOrderDOList, Integer batchType,Integer intervalTime){
        if(BatchType.BATCH_TYPE_TOTAL.equals(batchType)){
            return orderDOK3SendType(orderDOList,intervalTime);
        }else if(BatchType.BATCH_TYPE_SUCCESS.equals(batchType)){
            return orderDOK3SendType(successOrderDOList,intervalTime);
        }else if(BatchType.BATCH_TYPE_FAIL.equals(batchType)){
            return orderDOK3SendType(failOrderDOList,intervalTime);
        }
        return null;
    }

    public Map<String, String> customerDOK3SendType(List<CustomerDO> typeList,Integer intervalTime){
        K3SendRecordDO k3SendRecordDO;
        String success = null;
        String fail = null;
        for(int i=0;i<typeList.size();i++){
            try {
                Object data = recordTypeAndRecordReferIdByClass(PostK3Type.POST_K3_TYPE_CUSTOMER,typeList.get(i).getId());
                webServiceHelper.post(PostK3OperatorType.POST_K3_OPERATOR_TYPE_NULL,PostK3Type.POST_K3_TYPE_CUSTOMER,data,false);
                Thread.sleep(intervalTime);
                k3SendRecordDO = k3SendRecordMapper.findByReferIdAndType(typeList.get(i).getId(), PostK3Type.POST_K3_TYPE_CUSTOMER);
                if(CommonConstant.COMMON_CONSTANT_YES.equals(k3SendRecordDO.getSendResult()) && CommonConstant.COMMON_CONSTANT_YES.equals(k3SendRecordDO.getReceiveResult())){
                    //推送成功的数据
                    if(success == null){
                        success = String.valueOf(k3SendRecordDO.getRecordReferId()) + ",";
                    }else{
                        success = success + String.valueOf(k3SendRecordDO.getRecordReferId()) + ",";
                    }
                }else{
                    //推送失败的数据
                    if(fail == null){
                        fail = String.valueOf(k3SendRecordDO.getRecordReferId()) + ",";
                    }else{
                        fail = fail + String.valueOf(k3SendRecordDO.getRecordReferId()) + ",";
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Map<String, String> strMap = new HashMap<>();
        strMap.put("success:",success);
        strMap.put("fail:",fail);
        return strMap;
    }

    public Map<String, String> productDOK3SendType(List<ProductDO> typeList,Integer intervalTime){
        K3SendRecordDO k3SendRecordDO;
        String success = null;
        String fail = null;
        for(int i=0;i<typeList.size();i++){

            try {
                Object data = recordTypeAndRecordReferIdByClass(PostK3Type.POST_K3_TYPE_PRODUCT,typeList.get(i).getId());
                webServiceHelper.post(PostK3OperatorType.POST_K3_OPERATOR_TYPE_NULL,PostK3Type.POST_K3_TYPE_PRODUCT,data,false);
                Thread.sleep(intervalTime);
                k3SendRecordDO = k3SendRecordMapper.findByReferIdAndType(typeList.get(i).getId(), PostK3Type.POST_K3_TYPE_PRODUCT);
                if(CommonConstant.COMMON_CONSTANT_YES.equals(k3SendRecordDO.getSendResult()) && CommonConstant.COMMON_CONSTANT_YES.equals(k3SendRecordDO.getReceiveResult())){
                    //推送成功的数据
                    if(success == null){
                        success = String.valueOf(k3SendRecordDO.getRecordReferId()) + ",";
                    }else{
                        success = success + String.valueOf(k3SendRecordDO.getRecordReferId()) + ",";
                    }
                }else{
                    //推送失败的数据
                    if(fail == null){
                        fail = String.valueOf(k3SendRecordDO.getRecordReferId()) + ",";
                    }else{
                        fail = fail + String.valueOf(k3SendRecordDO.getRecordReferId()) + ",";
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Map<String, String> strMap = new HashMap<>();
        strMap.put("success:",success);
        strMap.put("fail:",fail);
        return strMap;
    }

    public Map<String, String> materialDOK3SendType(List<MaterialDO> typeList,Integer intervalTime){
        K3SendRecordDO k3SendRecordDO;
        String success = null;
        String fail = null;
        for(int i=0;i<typeList.size();i++){
            try {
                Object data = recordTypeAndRecordReferIdByClass(PostK3Type.POST_K3_TYPE_MATERIAL,typeList.get(i).getId());
                webServiceHelper.post(PostK3OperatorType.POST_K3_OPERATOR_TYPE_NULL,PostK3Type.POST_K3_TYPE_MATERIAL,data,false);
                Thread.sleep(intervalTime);
                k3SendRecordDO = k3SendRecordMapper.findByReferIdAndType(typeList.get(i).getId(), PostK3Type.POST_K3_TYPE_MATERIAL);
                if(CommonConstant.COMMON_CONSTANT_YES.equals(k3SendRecordDO.getSendResult()) && CommonConstant.COMMON_CONSTANT_YES.equals(k3SendRecordDO.getReceiveResult())){
                    //推送成功的数据
                    if(success == null){
                        success = String.valueOf(k3SendRecordDO.getRecordReferId()) + ",";
                    }else{
                        success = success + String.valueOf(k3SendRecordDO.getRecordReferId()) + ",";
                    }
                }else{
                    //推送失败的数据
                    if(fail == null){
                        fail = String.valueOf(k3SendRecordDO.getRecordReferId()) + ",";
                    }else{
                        fail = fail + String.valueOf(k3SendRecordDO.getRecordReferId()) + ",";
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Map<String, String> strMap = new HashMap<>();
        strMap.put("success:",success);
        strMap.put("fail:",fail);
        return strMap;
    }

    public Map<String, String> userDOK3SendType(List<UserDO> typeList,Integer intervalTime){
        K3SendRecordDO k3SendRecordDO;
        String success = null;
        String fail = null;
        for(int i=0;i<typeList.size();i++){
            try {
                Object data = recordTypeAndRecordReferIdByClass(PostK3Type.POST_K3_TYPE_USER,typeList.get(i).getId());
                webServiceHelper.post(PostK3OperatorType.POST_K3_OPERATOR_TYPE_NULL,PostK3Type.POST_K3_TYPE_USER,data,false);
                Thread.sleep(intervalTime);
                k3SendRecordDO = k3SendRecordMapper.findByReferIdAndType(typeList.get(i).getId(), PostK3Type.POST_K3_TYPE_USER);
                if(CommonConstant.COMMON_CONSTANT_YES.equals(k3SendRecordDO.getSendResult()) && CommonConstant.COMMON_CONSTANT_YES.equals(k3SendRecordDO.getReceiveResult())){
                    //推送成功的数据
                    if(success == null){
                        success = String.valueOf(k3SendRecordDO.getRecordReferId()) + ",";
                    }else{
                        success = success + String.valueOf(k3SendRecordDO.getRecordReferId()) + ",";
                    }
                }else{
                    //推送失败的数据
                    if(fail == null){
                        fail = String.valueOf(k3SendRecordDO.getRecordReferId()) + ",";
                    }else{
                        fail = fail + String.valueOf(k3SendRecordDO.getRecordReferId()) + ",";
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Map<String, String> strMap = new HashMap<>();
        strMap.put("success:",success);
        strMap.put("fail:",fail);
        return strMap;
    }

    public Map<String, String> orderDOK3SendType(List<OrderDO> typeList,Integer intervalTime){
        K3SendRecordDO k3SendRecordDO;
        String success = null;
        String fail = null;
        for(int i=0;i<typeList.size();i++){
            try {
                Object data = recordTypeAndRecordReferIdByClass(PostK3Type.POST_K3_TYPE_ORDER,typeList.get(i).getId());
                Order order = (Order)data;
                if(OrderStatus.ORDER_STATUS_WAIT_DELIVERY.equals(order.getOrderStatus())){
                    webServiceHelper.post(PostK3OperatorType.POST_K3_OPERATOR_TYPE_ADD,PostK3Type.POST_K3_TYPE_ORDER,data,false);
                    Thread.sleep(intervalTime);
                    k3SendRecordDO = k3SendRecordMapper.findByReferIdAndType(typeList.get(i).getId(), PostK3Type.POST_K3_TYPE_ORDER);
                    if(CommonConstant.COMMON_CONSTANT_YES.equals(k3SendRecordDO.getSendResult()) && CommonConstant.COMMON_CONSTANT_YES.equals(k3SendRecordDO.getReceiveResult())){
                        //推送成功的数据
                        if(success == null){
                            success = String.valueOf(k3SendRecordDO.getRecordReferId()) + ",";
                        }else{
                            success = success + String.valueOf(k3SendRecordDO.getRecordReferId()) + ",";
                        }
                    }else{
                        //推送失败的数据
                        if(fail == null){
                            fail = String.valueOf(k3SendRecordDO.getRecordReferId()) + ",";
                        }else{
                            fail = fail + String.valueOf(k3SendRecordDO.getRecordReferId()) + ",";
                        }
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Map<String, String> strMap = new HashMap<>();
        strMap.put("success:",success);
        strMap.put("fail:",fail);
        return strMap;
    }

}
