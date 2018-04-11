package com.lxzl.erp.core.service.coupon;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.coupon.*;
import com.lxzl.erp.common.domain.coupon.pojo.Coupon;
import com.lxzl.erp.common.domain.coupon.pojo.CouponBatch;
import com.lxzl.erp.common.domain.coupon.pojo.CouponBatchDetail;
import com.lxzl.erp.common.domain.customer.pojo.Customer;
import com.lxzl.erp.common.domain.order.pojo.Order;

import java.util.List;


/**
 * User : sunzhipeng
 * Date : Created in ${Date}
 * Time : Created in ${Time}
 */
public interface CouponService {


    ServiceResult<String,String> addCouponBatch(CouponBatch couponBatch);

    ServiceResult<String,String> updateCouponBatch(CouponBatch couponBatch);

    ServiceResult<String,String> deleteCouponBatch(CouponBatch couponBatch);

    ServiceResult<String,Page<CouponBatch>> pageCouponBatch(CouponBatchQueryParam couponBatchQueryParam);

    ServiceResult<String,String> addCouponBatchDetail(CouponBatchDetail couponBatchDetail);

    ServiceResult<String,Page<CouponBatchDetail>> pageCouponBatchDetail(CouponBatchDetailQueryParam couponBatchDetailQueryParam);

    ServiceResult<String,Page<Coupon>> pageCoupon(CouponQueryParam couponQueryParam);

    ServiceResult<String,String> deleteCoupon(List<Coupon> list);

    ServiceResult<String,String> provideCoupon(CouponProvideParam couponProvideParam);

    ServiceResult<String,List<Coupon>> findCouponByCustomerNo(Customer customer);

    ServiceResult<String,CouponBatch> findCouponBatchByCouponBatchDetail(CouponBatchDetail couponBatchDetail);

    ServiceResult<String,CouponBatch> findCouponBatchByCoupon(Coupon coupon);

    ServiceResult<String,String> cancelCouponByCouponBatchDetail(CouponBatchDetail couponBatchDetail);

    ServiceResult<String,String> cancelCouponByCouponBatch(CouponBatch couponBatch);

    ServiceResult<String,String> useCoupon(UseCoupon useCoupon);
}
