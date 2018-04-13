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
import org.junit.Test;

import java.math.BigDecimal;
import java.util.*;

public class CouponControllerTest extends ERPUnTransactionalTest{



    @Test
    public void addCouponBatch() throws Exception{
        CouponBatch couponBatch = new CouponBatch();
        couponBatch.setCouponBatchName("优惠批次修改测试");
        couponBatch.setCouponBatchDescribe("优惠测试修改测试");
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
        couponBatchDetail.setCouponBatchId(17);
        couponBatchDetail.setCouponTotalCount(10);
        BigDecimal faceValue = new BigDecimal(10.00);
        couponBatchDetail.setFaceValue(faceValue);
        couponBatchDetail.setEffectiveEndTime(date);
        couponBatchDetail.setIsOnline(0);
        couponBatchDetail.setRemark("线下新优惠券生成");

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
        couponQueryParam.setPageNo(1);
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
        coupon1.setCouponId(3121);
        Coupon coupon2 = new Coupon();
        coupon2.setCouponId(3122);
        Coupon coupon3 = new Coupon();
        coupon3.setCouponId(3123);
        Coupon coupon4 = new Coupon();
        coupon4.setCouponId(3124);
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
        couponProvideParam.setCouponBatchDetailId(95);
        List<CustomerProvide> customerProvideList = new ArrayList<>();
        CustomerProvide customerProvide1 = new CustomerProvide();
        customerProvide1.setCustomerNo("LXCC-1000-20180409-00004");
        customerProvide1.setProvideCount(2);
        CustomerProvide customerProvide2 = new CustomerProvide();
        customerProvide2.setCustomerNo("LXCC-1000-20180408-00003");
        customerProvide2.setProvideCount(2);
       customerProvideList.add(customerProvide1);
       customerProvideList.add(customerProvide2);
       couponProvideParam.setCustomerProvideList(customerProvideList);

        TestResult testResult = getJsonTestResult("/coupon/provideCoupon", couponProvideParam);
    }

    @Test
    public void findCouponByCustomerNo() throws Exception {
        Customer customer = new Customer();
        customer.setCustomerNo("sssss");

        TestResult testResult = getJsonTestResult("/coupon/findCouponByCustomerNo", customer);
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
        couponBatchDetail.setCouponBatchDetailId(29);

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
        order.setOrderNo("11111");
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
        coupon1.setCouponId(954);
        coupon1.setCouponBatchDetailId(15);
        coupon1.setCouponBatchId(15);
        coupon1.setFaceValue(new BigDecimal(15));
        Coupon coupon2 = new Coupon();
        coupon2.setCouponId(786);
        coupon2.setCouponBatchDetailId(12);
        coupon2.setCouponBatchId(31);
        coupon2.setFaceValue(new BigDecimal(25));
        List<Coupon> couponList = new ArrayList<>();
        couponList.add(coupon1);
        couponList.add(coupon2);
        UseCoupon useCoupon = new UseCoupon();
        useCoupon.setOrder(order);
        useCoupon.setCouponList(couponList);
        TestResult testResult = getJsonTestResult("/coupon/useCoupon", useCoupon);
    }
}
