package com.lxzl.erp.core.service.k3.support;

import com.lxzl.erp.common.constant.CustomerType;
import com.lxzl.erp.common.constant.PostK3Type;
import com.lxzl.erp.common.domain.customer.pojo.Customer;
import com.lxzl.erp.common.domain.k3.pojo.returnOrder.K3ReturnOrder;
import com.lxzl.erp.common.domain.material.pojo.Material;
import com.lxzl.erp.common.domain.order.pojo.Order;
import com.lxzl.erp.common.domain.product.pojo.Product;
import com.lxzl.erp.common.domain.supplier.pojo.Supplier;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.common.util.ConverterUtil;
import com.lxzl.erp.core.service.order.OrderService;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerMapper;
import com.lxzl.erp.dataaccess.dao.mysql.k3.K3ReturnOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.material.MaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.OrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductMapper;
import com.lxzl.erp.dataaccess.dao.mysql.supplier.SupplierMapper;
import com.lxzl.erp.dataaccess.dao.mysql.user.UserMapper;
import com.lxzl.erp.dataaccess.domain.customer.CustomerDO;
import com.lxzl.erp.dataaccess.domain.k3.returnOrder.K3ReturnOrderDO;
import com.lxzl.erp.dataaccess.domain.material.MaterialDO;
import com.lxzl.erp.dataaccess.domain.order.OrderDO;
import com.lxzl.erp.dataaccess.domain.product.ProductDO;
import com.lxzl.erp.dataaccess.domain.supplier.SupplierDO;
import com.lxzl.erp.dataaccess.domain.user.UserDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
        }else if(PostK3Type.POST_K3_TYPE_K3_RETURN_ORDER.equals(recordType)){
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
        }else if(PostK3Type.POST_K3_TYPE_K3_RETURN_ORDER.equals(recordType)){
            K3ReturnOrderDO k3ReturnOrderDO = k3ReturnOrderMapper.findById(recordReferId);
            return ConverterUtil.convert(k3ReturnOrderDO,K3ReturnOrder.class);
        }
        return false;
    }


}
