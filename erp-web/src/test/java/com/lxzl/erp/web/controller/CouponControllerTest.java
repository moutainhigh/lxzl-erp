package com.lxzl.erp.web.controller;


import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.domain.coupon.CouponBatchDetailQueryParam;
import com.lxzl.erp.common.domain.coupon.CouponBatchQueryParam;
import com.lxzl.erp.common.domain.coupon.CouponDeleteParam;
import com.lxzl.erp.common.domain.coupon.CouponQueryParam;
import com.lxzl.erp.common.domain.coupon.pojo.Coupon;
import com.lxzl.erp.common.domain.coupon.pojo.CouponBatch;
import com.lxzl.erp.common.domain.coupon.pojo.CouponBatchDetail;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CouponControllerTest extends ERPUnTransactionalTest{
    @Test
    public void addCouponBatch() throws Exception{
        CouponBatch couponBatch = new CouponBatch();
        couponBatch.setCouponBatchName("双十二特惠");
        couponBatch.setCouponBatchDescribe("这是一个双十二进行吐血大促的一个活动");
        couponBatch.setEffectiveStartTime(new Date());
        couponBatch.setCouponBatchTotalCount(100);
        couponBatch.setCouponType(1);
        TestResult testResult = getJsonTestResult("/coupon/addCouponBatch",couponBatch);
    }
    @Test
    public void updateCouponBatch() throws Exception{
        CouponBatch couponBatch = new CouponBatch();
        couponBatch.setCouponBatchId(3);
        couponBatch.setCouponBatchName("凌雄租赁大促销");
        couponBatch.setCouponType(2);
        couponBatch.setCouponBatchDescribe("双十二特惠，租一个苹果电脑送一个苹果手机");

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
        couponBatchDetail.setCouponBatchId(2);
        couponBatchDetail.setCouponTotalCount(5);
        BigDecimal faceValue = new BigDecimal(35.00);
        couponBatchDetail.setFaceValue(faceValue);
        couponBatchDetail.setIsOnline(1);
        couponBatchDetail.setEffectiveStartTime(date);
        couponBatchDetail.setEffectiveEndTime(date);
        couponBatchDetail.setRemark("优惠券生成");

        TestResult testResult = getJsonTestResult("/coupon/addCouponBatchDetail", couponBatchDetail);
    }

    @Test
    public void pageCouponBatchDetail() throws Exception {
        CouponBatchDetailQueryParam couponBatchDetailQueryParam = new CouponBatchDetailQueryParam();
        couponBatchDetailQueryParam.setPageNo(1);
        couponBatchDetailQueryParam.setPageSize(1);
//        customerPersonQueryParam.setIsDisabled(0);
//        customerPersonQueryParam.setCustomerStatus(CustomerStatus.STATUS_COMMIT);
        couponBatchDetailQueryParam.setCreateEndTime(new Date());
        TestResult testResult = getJsonTestResult("/coupon/pageCouponBatchDetail", couponBatchDetailQueryParam);
    }
    @Test
    public void pageCoupon() throws Exception {
        CouponQueryParam couponQueryParam = new CouponQueryParam();
        couponQueryParam.setPageNo(1);
        couponQueryParam.setPageSize(1);
        couponQueryParam.setCouponBatchId(2);
//        customerPersonQueryParam.setIsDisabled(0);
//        customerPersonQueryParam.setCustomerStatus(CustomerStatus.STATUS_COMMIT);
        TestResult testResult = getJsonTestResult("/coupon/pageCoupon", couponQueryParam);
    }
    @Test
    public void deleteCoupon() throws Exception {

        List<Coupon> list = new ArrayList<>();
//        Coupon coupon1 = new Coupon();
//        coupon1.setCouponId(1);
//        Coupon coupon2 = new Coupon();
//        coupon2.setCouponId(2);
        Coupon coupon3 = new Coupon();
        coupon3.setCouponId(3);
        Coupon coupon4 = new Coupon();
        coupon4.setCouponId(4);
//        list.add(coupon1);
//        list.add(coupon2);
        list.add(coupon3);
        list.add(coupon4);
        CouponDeleteParam couponDeleteParam = new CouponDeleteParam();
        couponDeleteParam.setCouponList(list);
        TestResult testResult = getJsonTestResult("/coupon/deleteCoupon", couponDeleteParam);
    }
}
