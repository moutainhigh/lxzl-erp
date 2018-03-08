package com.lxzl.erp.web.service;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.common.constant.CustomerType;
import com.lxzl.erp.common.constant.PostK3OperatorType;
import com.lxzl.erp.common.constant.PostK3Type;
import com.lxzl.erp.common.domain.customer.CustomerCompanyQueryParam;
import com.lxzl.erp.common.domain.customer.pojo.Customer;
import com.lxzl.erp.common.domain.customer.pojo.CustomerCompany;
import com.lxzl.erp.common.domain.customer.pojo.CustomerPerson;
import com.lxzl.erp.common.domain.k3.pojo.returnOrder.K3ReturnOrder;
import com.lxzl.erp.common.domain.material.pojo.Material;
import com.lxzl.erp.common.domain.order.OrderQueryParam;
import com.lxzl.erp.common.domain.order.pojo.Order;
import com.lxzl.erp.common.domain.product.ProductQueryParam;
import com.lxzl.erp.common.domain.product.pojo.Product;
import com.lxzl.erp.common.domain.supplier.pojo.Supplier;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.common.util.ConverterUtil;
import com.lxzl.erp.core.service.customer.CustomerService;
import com.lxzl.erp.core.service.k3.WebServiceHelper;
import com.lxzl.erp.core.service.order.OrderService;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerCompanyMapper;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerMapper;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerPersonMapper;
import com.lxzl.erp.dataaccess.dao.mysql.k3.K3MappingCustomerMapper;
import com.lxzl.erp.dataaccess.dao.mysql.k3.K3ReturnOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.k3.K3SendRecordMapper;
import com.lxzl.erp.dataaccess.dao.mysql.material.MaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.OrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductMapper;
import com.lxzl.erp.dataaccess.dao.mysql.returnOrder.ReturnOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.supplier.SupplierMapper;
import com.lxzl.erp.dataaccess.dao.mysql.user.UserMapper;
import com.lxzl.erp.dataaccess.domain.customer.CustomerCompanyDO;
import com.lxzl.erp.dataaccess.domain.customer.CustomerDO;
import com.lxzl.erp.dataaccess.domain.customer.CustomerPersonDO;
import com.lxzl.erp.dataaccess.domain.k3.K3SendRecordDO;
import com.lxzl.erp.dataaccess.domain.k3.returnOrder.K3ReturnOrderDO;
import com.lxzl.erp.dataaccess.domain.material.MaterialDO;
import com.lxzl.erp.dataaccess.domain.order.OrderDO;
import com.lxzl.erp.dataaccess.domain.product.ProductDO;
import com.lxzl.erp.dataaccess.domain.returnOrder.ReturnOrderDO;
import com.lxzl.erp.dataaccess.domain.supplier.SupplierDO;
import com.lxzl.erp.dataaccess.domain.user.UserDO;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostTest extends ERPUnTransactionalTest {
    @Autowired
    private WebServiceHelper webServiceHelper;

    @Test
    public void postProduct() throws InterruptedException {

        ProductDO productDO = productMapper.findByProductId(2000057);
        webServiceHelper.post(PostK3OperatorType.POST_K3_OPERATOR_TYPE_NULL,PostK3Type.POST_K3_TYPE_PRODUCT, ConverterUtil.convert(productDO, Product.class),true);
        Thread.sleep(30000);
    }
    @Test
    public void postAllProduct() throws InterruptedException {
        Map<String,Object> maps = new HashMap<>();
        ProductQueryParam productQueryParam = new ProductQueryParam();
        maps.put("start", 0);
        maps.put("pageSize", Integer.MAX_VALUE);
        maps.put("productQueryParam", productQueryParam);
        List<ProductDO> productDOList = productMapper.findProductByParams(maps);
        for(ProductDO productDO : productDOList){
            Thread.sleep(500);
            webServiceHelper.post(PostK3OperatorType.POST_K3_OPERATOR_TYPE_NULL,PostK3Type.POST_K3_TYPE_PRODUCT, ConverterUtil.convert(productDO, Product.class),true);
        }
        Thread.sleep(30000);
    }

    @Test
    public void postMaterial() throws InterruptedException {
        MaterialDO materialDO = materialMapper.findByNo("LX-YWB-20180303-00001");
        webServiceHelper.post(PostK3OperatorType.POST_K3_OPERATOR_TYPE_NULL,PostK3Type.POST_K3_TYPE_MATERIAL, ConverterUtil.convert(materialDO, Material.class),true);
        Thread.sleep(100000);
    }
    @Test
    public void postAllMaterial() throws InterruptedException {
        List<MaterialDO> materialDOList = materialMapper.findAllMaterial();
        for(MaterialDO materialDO : materialDOList){
            Thread.sleep(500);
            webServiceHelper.post(PostK3OperatorType.POST_K3_OPERATOR_TYPE_NULL,PostK3Type.POST_K3_TYPE_MATERIAL, ConverterUtil.convert(materialDO, Material.class),true);
        }
        Thread.sleep(30000);
    }
    @Test
    public void postCustomer() throws InterruptedException {
        String customerNo  = "LXCC-1000-20180306-00314";
        CustomerDO customerDO = customerMapper.findByNo(customerNo);
        if (CustomerType.CUSTOMER_TYPE_COMPANY.equals(customerDO.getCustomerType())) {
            customerDO = customerMapper.findCustomerCompanyByNo(customerNo);
        } else {
            customerDO = customerMapper.findCustomerPersonByNo(customerNo);
        }
        webServiceHelper.post(PostK3OperatorType.POST_K3_OPERATOR_TYPE_NULL,PostK3Type.POST_K3_TYPE_CUSTOMER, ConverterUtil.convert(customerDO, Customer.class),true);
        Thread.sleep(30000);
     }

    @Test
    public void postSupplier() throws InterruptedException {
        SupplierDO supplierDO = supplierMapper.findById(709);
        webServiceHelper.post(PostK3OperatorType.POST_K3_OPERATOR_TYPE_NULL,PostK3Type.POST_K3_TYPE_SUPPLIER, ConverterUtil.convert(supplierDO, Supplier.class),true);
        Thread.sleep(30000);
    }
    @Test
    public void postAllSupplier() throws InterruptedException {
        List<SupplierDO> supplierDOList = supplierMapper.findAllSupplier();
        for(SupplierDO supplierDO : supplierDOList){
            Thread.sleep(500);
            webServiceHelper.post(PostK3OperatorType.POST_K3_OPERATOR_TYPE_NULL,PostK3Type.POST_K3_TYPE_SUPPLIER, ConverterUtil.convert(supplierDO, Supplier.class),true);
        }

        Thread.sleep(30000);
    }
    @Test
    public void postOrder() throws InterruptedException {

       Order order =  orderService.queryOrderByNo("LXO-20180306-2000-00069").getResult();
        webServiceHelper.post(PostK3OperatorType.POST_K3_OPERATOR_TYPE_ADD,PostK3Type.POST_K3_TYPE_ORDER, order,true);
        Thread.sleep(30000);
    }
    @Test
    public void postAllOrder() throws InterruptedException {
        Map<String,Object> maps = new HashMap<>();
        OrderQueryParam orderQueryParam = new OrderQueryParam();
        maps.put("start", 0);
        maps.put("pageSize", Integer.MAX_VALUE);
        maps.put("orderQueryParam", orderQueryParam);
        List<OrderDO> orderList = orderMapper.findOrderByParams(maps) ;
        for(OrderDO orderDO : orderList){
            Order order =  orderService.queryOrderByNo(orderDO.getOrderNo()).getResult();
//            order.setOrderNo("LXO-20180208-700032-00009");
            Thread.sleep(500);
            webServiceHelper.post(PostK3OperatorType.POST_K3_OPERATOR_TYPE_ADD,PostK3Type.POST_K3_TYPE_ORDER, order,true);
        }

        Thread.sleep(300000);
    }
    @Test
    public void postAllCustomer() throws InterruptedException {
        CustomerCompanyQueryParam customerCompanyQueryParam = new CustomerCompanyQueryParam();
        customerCompanyQueryParam.setPageNo(1);
        customerCompanyQueryParam.setPageSize(Integer.MAX_VALUE);
        Map<String,Object> maps = new HashMap<>();
        maps.put("start", 0);
        maps.put("pageSize", Integer.MAX_VALUE);
        maps.put("orderQueryParam", customerCompanyQueryParam);
        List<CustomerDO> customerDOList = customerMapper.findCustomerCompanyByParams(maps);

        for(CustomerDO customerDO : customerDOList){

//            Thread.sleep(500);
            Customer customer = customerService.detailCustomerTemp(customerDO.getCustomerNo());
            if(CustomerType.CUSTOMER_TYPE_PERSON.equals(customer.getCustomerType())){
                CustomerPersonDO customerPersonDO = customerPersonMapper.findByCustomerId(customer.getCustomerId());
                customer.setCustomerPerson(ConverterUtil.convert(customerPersonDO,CustomerPerson.class));
            }
            if(CustomerType.CUSTOMER_TYPE_COMPANY.equals(customer.getCustomerType())){
                CustomerCompanyDO customerCompanyDO = customerCompanyMapper.findByCustomerId(customer.getCustomerId());
                customer.setCustomerCompany(ConverterUtil.convert(customerCompanyDO,CustomerCompany.class));
            }
            webServiceHelper.post(PostK3OperatorType.POST_K3_OPERATOR_TYPE_NULL,PostK3Type.POST_K3_TYPE_CUSTOMER, customer,true);
        }

        Thread.sleep(100000);
    }
    @Test
    public void postAllFailCustomer() throws InterruptedException {
        List<K3SendRecordDO> k3SendRecordDOList = k3SendRecordMapper.findAllFailByType(PostK3Type.POST_K3_TYPE_CUSTOMER);

        for(K3SendRecordDO k3SendRecordDO : k3SendRecordDOList){

            Thread.sleep(200);
            CustomerDO customerDO = customerMapper.findById(k3SendRecordDO.getRecordReferId());
            Customer customer = ConverterUtil.convert(customerDO,Customer.class);
            if(CustomerType.CUSTOMER_TYPE_PERSON.equals(customerDO.getCustomerType())){
                CustomerPersonDO customerPersonDO = customerPersonMapper.findByCustomerId(k3SendRecordDO.getRecordReferId());
                customer.setCustomerPerson(ConverterUtil.convert(customerPersonDO,CustomerPerson.class));
            }
            if(CustomerType.CUSTOMER_TYPE_COMPANY.equals(customerDO.getCustomerType())){
                CustomerCompanyDO customerCompanyDO = customerCompanyMapper.findByCustomerId(k3SendRecordDO.getRecordReferId());
                customer.setCustomerCompany(ConverterUtil.convert(customerCompanyDO,CustomerCompany.class));
            }
            webServiceHelper.post(PostK3OperatorType.POST_K3_OPERATOR_TYPE_NULL,PostK3Type.POST_K3_TYPE_CUSTOMER, customer,true);
        }

        Thread.sleep(100000);
    }

    @Test
    public void postUser() throws InterruptedException {

        UserDO userDO = userMapper.findByUserId(500004);
        webServiceHelper.post(PostK3OperatorType.POST_K3_OPERATOR_TYPE_NULL,PostK3Type.POST_K3_TYPE_USER, ConverterUtil.convert(userDO,User.class),true);
        Thread.sleep(30000);
    }

    @Test
    public void postAllUser() throws InterruptedException {
        List<UserDO> userDOList = userMapper.listAllUser();
        for(UserDO userDO : userDOList){
            Thread.sleep(500);
            webServiceHelper.post(PostK3OperatorType.POST_K3_OPERATOR_TYPE_NULL,PostK3Type.POST_K3_TYPE_USER, ConverterUtil.convert(userDO,User.class),true);
        }
        Thread.sleep(30000);
    }

    @Test
    public void postReturnOrder() throws InterruptedException {

        K3ReturnOrderDO k3ReturnOrderDO = k3ReturnOrderMapper.findByNo("53f0140d84834f4e8502619ca88bb4a4");
        webServiceHelper.post(PostK3OperatorType.POST_K3_OPERATOR_TYPE_NULL,PostK3Type.POST_K3_TYPE_K3_RETURN_ORDER, ConverterUtil.convert(k3ReturnOrderDO,K3ReturnOrder.class),true);
        Thread.sleep(100000);
    }


    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private MaterialMapper materialMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderService orderService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private CustomerCompanyMapper customerCompanyMapper;
    @Autowired
    private CustomerPersonMapper customerPersonMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private SupplierMapper supplierMapper;
    @Autowired
    private K3ReturnOrderMapper k3ReturnOrderMapper;
    @Autowired
    private K3SendRecordMapper k3SendRecordMapper;
}
