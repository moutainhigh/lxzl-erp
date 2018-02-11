package com.lxzl.erp.web.service;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.common.constant.CustomerType;
import com.lxzl.erp.common.constant.PostK3OperatorType;
import com.lxzl.erp.common.constant.PostK3Type;
import com.lxzl.erp.common.domain.customer.pojo.Customer;
import com.lxzl.erp.common.domain.material.pojo.Material;
import com.lxzl.erp.common.domain.order.pojo.Order;
import com.lxzl.erp.common.domain.product.pojo.Product;
import com.lxzl.erp.common.util.ConverterUtil;
import com.lxzl.erp.core.service.k3.WebServiceHelper;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerMapper;
import com.lxzl.erp.dataaccess.dao.mysql.material.MaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductMapper;
import com.lxzl.erp.dataaccess.domain.customer.CustomerDO;
import com.lxzl.erp.dataaccess.domain.material.MaterialDO;
import com.lxzl.erp.dataaccess.domain.product.ProductDO;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PostTest extends ERPUnTransactionalTest {
    @Autowired
    private WebServiceHelper webServiceHelper;

    @Test
    public void postProduct() throws InterruptedException {
        ProductDO productDO = productMapper.findByProductId(2000063);
        webServiceHelper.post(PostK3OperatorType.POST_K3_OPERATOR_TYPE_NULL,PostK3Type.POST_K3_TYPE_PRODUCT, ConverterUtil.convert(productDO, Product.class));
        Thread.sleep(30000);
    }

    @Test
    public void postMaterial() throws InterruptedException {
        MaterialDO materialDO = materialMapper.findByNo("M201712211828335601367");
        webServiceHelper.post(PostK3OperatorType.POST_K3_OPERATOR_TYPE_NULL,PostK3Type.POST_K3_TYPE_MATERIAL, ConverterUtil.convert(materialDO, Material.class));
        Thread.sleep(100000);
    }

    @Test
    public void postCustomer() throws InterruptedException {
        String customerNo  = "CP201712060843154191841";
        CustomerDO customerDO = customerMapper.findByNo(customerNo);
        if (CustomerType.CUSTOMER_TYPE_COMPANY.equals(customerDO.getCustomerType())) {
            customerDO = customerMapper.findCustomerCompanyByNo(customerNo);
        } else {
            customerDO = customerMapper.findCustomerPersonByNo(customerNo);
        }
        webServiceHelper.post(PostK3OperatorType.POST_K3_OPERATOR_TYPE_NULL,PostK3Type.POST_K3_TYPE_CUSTOMER, ConverterUtil.convert(customerDO, Customer.class));
        Thread.sleep(30000);
    }

    @Test
    public void postSupplier() throws InterruptedException {
        webServiceHelper.post(PostK3OperatorType.POST_K3_OPERATOR_TYPE_NULL,PostK3Type.POST_K3_TYPE_SUPPLIER, null);
        Thread.sleep(30000);
    }

    @Test
    public void postOrder() throws InterruptedException {
        Order order = new Order();
        order.setOrderNo("LXO-20180208-700032-00009");
        webServiceHelper.post(PostK3OperatorType.POST_K3_OPERATOR_TYPE_ADD,PostK3Type.POST_K3_TYPE_ORDER, order);
        Thread.sleep(30000);
    }
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private MaterialMapper materialMapper;
}
