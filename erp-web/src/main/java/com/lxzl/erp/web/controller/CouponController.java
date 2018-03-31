package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.coupon.pojo.CouponBatch;
import com.lxzl.erp.common.domain.validGroup.AddGroup;
import com.lxzl.erp.core.annotation.ControllerLog;
import com.lxzl.erp.core.component.ResultGenerator;
import com.lxzl.erp.core.service.coupon.CouponService;
import com.lxzl.se.common.domain.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * User : sunzhipeng
 * Date : Created in ${Date}
 * Time : Created in ${Time}
 */
@RequestMapping("/coupon")
@Controller
@ControllerLog
public class CouponController {
    @Autowired
    private CouponService couponService;
    @Autowired
    private ResultGenerator resultGenerator;

    @RequestMapping(value = "addCouponBatch",method = RequestMethod.POST)
    public Result addCouponBatch(@RequestBody @Validated(AddGroup.class)CouponBatch couponBatch , BindingResult validResult){
        ServiceResult<String, String> serviceResult = couponService.addCouponBatch(couponBatch);
        return resultGenerator.generate(serviceResult);
    }

}
