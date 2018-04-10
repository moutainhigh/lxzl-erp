package com.lxzl.erp.web.controller;


import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.domain.coupon.*;
import com.lxzl.erp.common.domain.coupon.pojo.Coupon;
import com.lxzl.erp.common.domain.coupon.pojo.CouponBatch;
import com.lxzl.erp.common.domain.coupon.pojo.CouponBatchDetail;
import com.lxzl.erp.common.domain.customer.pojo.Customer;
import org.junit.Test;

import javax.xml.crypto.Data;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        coupon1.setCouponId(105);
        Coupon coupon2 = new Coupon();
        coupon2.setCouponId(106);
        Coupon coupon3 = new Coupon();
        coupon3.setCouponId(107);
        Coupon coupon4 = new Coupon();
        coupon4.setCouponId(108);
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
        couponProvideParam.setCouponBatchDetailId(5);
        couponProvideParam.setCouponProvideAmount(3);
        Customer customer1 = new Customer();
        customer1.setCustomerNo("sssss");
        Customer customer2 = new Customer();
        customer2.setCustomerNo("aaaaa");
        Customer customer3 = new Customer();
        customer3.setCustomerNo("bbbbb");
        List<Customer> customerList = new ArrayList<>();
        customerList.add(customer1);
        customerList.add(customer2);
        customerList.add(customer3);
        couponProvideParam.setCustomerList(customerList);

        TestResult testResult = getJsonTestResult("/coupon/provideCoupon", couponProvideParam);
    }

    @Test
    public void findCouponByCustomerNo() throws Exception {
        Customer customer = new Customer();
        customer.setCustomerNo("sssss");

        TestResult testResult = getJsonTestResult("/coupon/findCouponByCustomerNo", customer);
    }

}
