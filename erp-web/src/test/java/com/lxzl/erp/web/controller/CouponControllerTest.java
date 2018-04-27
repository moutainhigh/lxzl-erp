package com.lxzl.erp.web.controller;


import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.domain.coupon.*;
import com.lxzl.erp.common.domain.coupon.pojo.Coupon;
import com.lxzl.erp.common.domain.coupon.pojo.CouponBatch;
import com.lxzl.erp.common.domain.coupon.pojo.CouponBatchDetail;
import com.lxzl.erp.common.domain.customer.pojo.Customer;
import com.lxzl.erp.common.domain.order.pojo.Order;
import com.lxzl.erp.common.domain.order.pojo.OrderProduct;
import com.lxzl.erp.common.domain.statement.pojo.StatementOrderDetail;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.*;

public class CouponControllerTest extends ERPUnTransactionalTest{



    @Test
    public void addCouponBatch() throws Exception{
        CouponBatch couponBatch = new CouponBatch();
        couponBatch.setCouponBatchName("可领取优惠券总数");
        couponBatch.setCouponBatchDescribe("可领取优惠券总数");
        couponBatch.setEffectiveStartTime(new Date());
        couponBatch.setCouponBatchTotalCount(100);
        couponBatch.setCouponType(1);
        TestResult testResult = getJsonTestResult("/coupon/addCouponBatch",couponBatch);
    }
    @Test
    public void updateCouponBatch() throws Exception{
        CouponBatch couponBatch = new CouponBatch();
        couponBatch.setCouponBatchId(20);
        couponBatch.setCouponBatchName("凌雄租赁大促销");
        couponBatch.setCouponType(2);
        couponBatch.setCouponBatchDescribe("双十二特惠，租一个苹果电脑送一个苹果手机");
        couponBatch.setRemark("ashkajsh");
        couponBatch.setEffectiveStartTime(new Date());
        couponBatch.setEffectiveEndTime(new Date());

        TestResult testResult = getJsonTestResult("/coupon/updateCouponBatch",couponBatch);
    }
    @Test
    public void deleteCouponBatch() throws Exception{
        CouponBatch couponBatch = new CouponBatch();
        couponBatch.setCouponBatchId(1);

        TestResult testResult = getJsonTestResult("/coupon/deleteCouponBatch",couponBatch);
    }
    @Test
    public void pageCouponBatch() throws Exception {
        CouponBatchQueryParam couponBatchQueryParam = new CouponBatchQueryParam();
        couponBatchQueryParam.setPageNo(1);
        couponBatchQueryParam.setPageSize(2);
        couponBatchQueryParam.setCouponType(1);
//        customerPersonQueryParam.setIsDisabled(0);
//        customerPersonQueryParam.setCustomerStatus(CustomerStatus.STATUS_COMMIT);
        couponBatchQueryParam.setCouponBatchName("十");
        TestResult testResult = getJsonTestResult("/coupon/pageCouponBatch", couponBatchQueryParam);
    }
    @Test
    public void addCouponBatchDetail() throws Exception {
        Date date = new Date();
        CouponBatchDetail couponBatchDetail = new CouponBatchDetail();
        couponBatchDetail.setCouponBatchId(72);
        couponBatchDetail.setCouponTotalCount(5);
        BigDecimal faceValue = new BigDecimal(20.00);
        couponBatchDetail.setFaceValue(faceValue);
        couponBatchDetail.setEffectiveEndTime(date);
        couponBatchDetail.setIsOnline(1);
        couponBatchDetail.setRemark("33333");

        TestResult testResult = getJsonTestResult("/coupon/addCouponBatchDetail", couponBatchDetail);
    }

    @Test
    public void pageCouponBatchDetail() throws Exception {
        CouponBatchDetailQueryParam couponBatchDetailQueryParam = new CouponBatchDetailQueryParam();
        couponBatchDetailQueryParam.setPageNo(1);
        couponBatchDetailQueryParam.setPageSize(1);
        couponBatchDetailQueryParam.setCouponBatchId(2);
//        customerPersonQueryParam.setIsDisabled(0);
//        customerPersonQueryParam.setCustomerStatus(CustomerStatus.STATUS_COMMIT);
//        couponBatchDetailQueryParam.setCreateEndTime(new Date());
        TestResult testResult = getJsonTestResult("/coupon/pageCouponBatchDetail", couponBatchDetailQueryParam);
    }
    @Test
    public void pageCoupon() throws Exception {
        CouponQueryParam couponQueryParam = new CouponQueryParam();
        couponQueryParam.setPageNo(2);
        couponQueryParam.setPageSize(15);
 //       couponQueryParam.setCouponBatchId(4);
//        customerPersonQueryParam.setIsDisabled(0);
//        customerPersonQueryParam.setCustomerStatus(CustomerStatus.STATUS_COMMIT);
        TestResult testResult = getJsonTestResult("/coupon/pageCoupon", couponQueryParam);
    }
    @Test
    public void deleteCoupon() throws Exception {

        List<Coupon> list = new ArrayList<>();
        Coupon coupon1 = new Coupon();
        coupon1.setCouponId(4636);
        Coupon coupon2 = new Coupon();
        coupon2.setCouponId(4637);
        Coupon coupon3 = new Coupon();
        coupon3.setCouponId(4650);
        Coupon coupon4 = new Coupon();
        coupon4.setCouponId(4651);
        list.add(coupon1);
        list.add(coupon2);
        list.add(coupon3);
        list.add(coupon4);
        CouponDeleteParam couponDeleteParam = new CouponDeleteParam();
        couponDeleteParam.setCouponList(list);
        TestResult testResult = getJsonTestResult("/coupon/deleteCoupon", couponDeleteParam);
    }

    @Test
    public void provideCoupon() throws Exception {
        CouponProvideParam couponProvideParam = new CouponProvideParam();
        couponProvideParam.setCouponBatchDetailId(142);
        List<CustomerProvide> customerProvideList = new ArrayList<>();
        CustomerProvide customerProvide1 = new CustomerProvide();
        customerProvide1.setCustomerNo("LXCC-021-20180212-00809");
        customerProvide1.setProvideCount(1);
//        CustomerProvide customerProvide2 = new CustomerProvide();
//        customerProvide2.setCustomerNo("LXCC-1000-20180408-00003");
//        customerProvide2.setProvideCount(2);
       customerProvideList.add(customerProvide1);
//       customerProvideList.add(customerProvide2);
       couponProvideParam.setCustomerProvideList(customerProvideList);

        TestResult testResult = getJsonTestResult("/coupon/provideCoupon", couponProvideParam);
    }

    @Test
    public void findCouponByCustomerNo() throws Exception {
        CustomerOrderCouponParam customerOrderCouponParam = new CustomerOrderCouponParam();
        Customer customer = new Customer();
        customer.setCustomerNo("LXCC-1000-20180409-00004");
        Order order = new Order();
//        order.setOrderNo("LXO-20180313-027-00818");
        customerOrderCouponParam.setCustomer(customer);
        customerOrderCouponParam.setOrder(order);

        TestResult testResult = getJsonTestResult("/coupon/findCouponByCustomerNo", customerOrderCouponParam);
    }

    @Test
    public void findCouponBatchByID() throws Exception {
        CouponBatch couponBatch = new CouponBatch();
        couponBatch.setCouponBatchId(15);

        TestResult testResult = getJsonTestResult("/coupon/findCouponBatchByID", couponBatch);
    }

    @Test
    public void cancelCouponByCouponBatchDetail() throws Exception {
        CouponBatchDetail couponBatchDetail = new CouponBatchDetail();
        couponBatchDetail.setCouponBatchDetailId(118);

        TestResult testResult = getJsonTestResult("/coupon/cancelCouponByCouponBatchDetail", couponBatchDetail);
    }
    @Test
    public void cancelCouponByCouponBatch() throws Exception {
        CouponBatch couponBatch = new CouponBatch();
        couponBatch.setCouponBatchId(17);

        TestResult testResult = getJsonTestResult("/coupon/cancelCouponByCouponBatch", couponBatch);
    }
    @Test
    public void useCoupon() throws Exception {
        Order order = new Order();
        order.setOrderId(1111);
//        order.setOrderNo("LXO-20180313-027-00813");
        order.setOrderNo("LXO-20180313-027-00818");
        order.setBuyerCustomerNo("LXCC-0755-20180112-00130");
        OrderProduct orderProduct1 = new OrderProduct();
        orderProduct1.setOrderId(1111);
        orderProduct1.setOrderProductId(1);
        orderProduct1.setProductUnitAmount(new BigDecimal(80));
        orderProduct1.setProductCount(3);
        OrderProduct orderProduct2 = new OrderProduct();
        orderProduct2.setOrderId(1111);
        orderProduct2.setOrderProductId(2);
        orderProduct2.setProductUnitAmount(new BigDecimal(100));
        orderProduct2.setProductCount(2);
        List<OrderProduct> orderProductList = new ArrayList<>();
        orderProductList.add(orderProduct1);
        orderProductList.add(orderProduct2);
        order.setOrderProductList(orderProductList);
        Coupon coupon1 = new Coupon();
        coupon1.setCouponId(5443);
        Coupon coupon2 = new Coupon();
        coupon2.setCouponId(5442);
        List<Coupon> couponList = new ArrayList<>();
        couponList.add(coupon1);
        couponList.add(coupon2);
        order.setCouponList(couponList);
        TestResult testResult = getJsonTestResult("/coupon/useCoupon", order);
    }
    @Test
    public void pageCouponByCustomerNo() throws Exception {
        CustomerCouponQueryParam customerCouponQueryParam = new CustomerCouponQueryParam();
        customerCouponQueryParam.setPageNo(1);
        customerCouponQueryParam.setPageSize(15);
        Customer customer = new Customer();
        customer.setCustomerNo("LXCC-1000-20180409-00004");
//        Order order = new Order();
//        OrderProduct orderProduct = new OrderProduct();
        customerCouponQueryParam.setCustomer(customer);
        //       couponQueryParam.setCouponBatchId(4);
//        customerPersonQueryParam.setIsDisabled(0);
//        customerPersonQueryParam.setCustomerStatus(CustomerStatus.STATUS_COMMIT);
        TestResult testResult = getJsonTestResult("/coupon/pageCouponByCustomerNo", customerCouponQueryParam);
    }
    @Test
    public void findStatementCouponByCustomerNo() throws Exception {
        Customer customer = new Customer();
        customer.setCustomerNo("LXCC-1000-20180409-00004");
        TestResult testResult = getJsonTestResult("/coupon/findStatementCouponByCustomerNo", customer);
    }
    @Test
    public void useStatementCoupon() throws Exception {
        Coupon coupon = new Coupon();
        coupon.setCouponId(5273);
        StatementOrderDetail statementOrderDetail = new StatementOrderDetail();
        statementOrderDetail.setStatementOrderDetailId(2);
        StatementCouponParam statementCouponParam = new StatementCouponParam();
        statementCouponParam.setCoupon(coupon);
        statementCouponParam.setStatementOrderDetail(statementOrderDetail);
        TestResult testResult = getJsonTestResult("/coupon/useStatementCoupon", statementCouponParam);
    }
    @Test
    public void pageLockCouponByOrderNo() throws Exception {
        LockCouponQueryParam lockCouponQueryParam = new LockCouponQueryParam();
        Order order = new Order();
        order.setOrderNo("LXO-20180313-027-00818");
        lockCouponQueryParam.setOrder(order);
        TestResult testResult = getJsonTestResult("/coupon/pageLockCouponByOrderNo", lockCouponQueryParam);
    }
}
