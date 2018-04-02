package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.coupon.CouponBatchDetailQueryParam;
import com.lxzl.erp.common.domain.coupon.CouponBatchQueryParam;
import com.lxzl.erp.common.domain.coupon.CouponDeleteParam;
import com.lxzl.erp.common.domain.coupon.CouponQueryParam;
import com.lxzl.erp.common.domain.coupon.pojo.Coupon;
import com.lxzl.erp.common.domain.coupon.pojo.CouponBatch;
import com.lxzl.erp.common.domain.coupon.pojo.CouponBatchDetail;
import com.lxzl.erp.common.domain.validGroup.AddGroup;
import com.lxzl.erp.common.domain.validGroup.IdGroup;
import com.lxzl.erp.common.domain.validGroup.UpdateGroup;
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

import javax.validation.Valid;
import java.util.ArrayList;

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

    /**
     * 增加优惠卷批次
     * @param couponBatch
     * @param validResult
     * @return
     */
    @RequestMapping(value = "addCouponBatch",method = RequestMethod.POST)
    public Result addCouponBatch(@RequestBody @Validated(AddGroup.class)CouponBatch couponBatch , BindingResult validResult){
        ServiceResult<String, String> serviceResult = couponService.addCouponBatch(couponBatch);
        return resultGenerator.generate(serviceResult);
    }

    /**
     * 修改优惠卷批次
     * @param couponBatch
     * @param validResult
     * @return
     */
    @RequestMapping(value = "updateCouponBatch",method = RequestMethod.POST)
    public Result updateCouponBatch(@RequestBody @Validated(UpdateGroup.class)CouponBatch couponBatch , BindingResult validResult){
        ServiceResult<String, String> serviceResult = couponService.updateCouponBatch(couponBatch);
        return resultGenerator.generate(serviceResult);
    }

    /**
     * 删除优惠卷批次
     * @param couponBatch
     * @param validResult
     * @return
     */
    @RequestMapping(value = "deleteCouponBatch",method = RequestMethod.POST)
    public Result deleteCouponBatch(@RequestBody @Validated(UpdateGroup.class)CouponBatch couponBatch , BindingResult validResult){
        ServiceResult<String, String> serviceResult = couponService.deleteCouponBatch(couponBatch);
        return resultGenerator.generate(serviceResult);
    }
    /**
     * 优惠卷批次列表
     * @param couponBatchQueryParam
     * @return
     */
    @RequestMapping(value = "pageCouponBatch",method = RequestMethod.POST)
    public Result pageCouponBatch(@RequestBody CouponBatchQueryParam couponBatchQueryParam, BindingResult validResult){
        ServiceResult<String,Page<CouponBatch>> serviceResult = couponService.pageCouponBatch(couponBatchQueryParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    /**
     * 批次增卷
     * @param couponBatchDetail
     * @return
     */
    @RequestMapping(value = "addCouponBatchDetail",method = RequestMethod.POST)
    public Result addCouponBatchDetail(@RequestBody @Validated(AddGroup.class) CouponBatchDetail couponBatchDetail, BindingResult validResult){
        ServiceResult<String, String> serviceResult = couponService.addCouponBatchDetail(couponBatchDetail);
        return resultGenerator.generate(serviceResult);
    }

    /**
     * 优惠卷批次列表
     * @param couponBatchDetailQueryParam
     * @return
     */
    @RequestMapping(value = "pageCouponBatchDetail",method = RequestMethod.POST)
    public Result pageCouponBatchDetail(@RequestBody CouponBatchDetailQueryParam couponBatchDetailQueryParam, BindingResult validResult){
        ServiceResult<String,Page<CouponBatchDetail>> serviceResult = couponService.pageCouponBatchDetail(couponBatchDetailQueryParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }
    /**
     * 优惠券分页查询
     * @param couponQueryParam
     * @return
     */
    @RequestMapping(value = "pageCoupon",method = RequestMethod.POST)
    public Result pageCoupon(@RequestBody CouponQueryParam couponQueryParam, BindingResult validResult){
        ServiceResult<String,Page<Coupon>> serviceResult = couponService.pageCoupon(couponQueryParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }
    /**
     * 批量作废优惠券
     * @param couponDeleteParam
     * @return
     */
    @RequestMapping(value = "deleteCoupon",method = RequestMethod.POST)
    public Result deleteCoupon(@RequestBody @Validated(IdGroup.class) CouponDeleteParam couponDeleteParam, BindingResult validResult){
        ServiceResult<String,String> serviceResult = couponService.deleteCoupon(couponDeleteParam.getCouponList());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

}
