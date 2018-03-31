package com.lxzl.erp.core.service.coupon;

import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.coupon.pojo.CouponBatch;
import org.springframework.beans.factory.annotation.Autowired;

public interface CouponService {


    ServiceResult<String,String> addCouponBatch(CouponBatch couponBatch);
}
