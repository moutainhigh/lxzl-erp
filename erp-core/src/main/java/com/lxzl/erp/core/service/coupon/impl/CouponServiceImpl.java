package com.lxzl.erp.core.service.coupon.impl;

import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.coupon.pojo.CouponBatch;
import com.lxzl.erp.common.util.ConverterUtil;
import com.lxzl.erp.core.service.coupon.CouponService;
import com.lxzl.erp.dataaccess.dao.mysql.coupon.CouponBatchDetailMapper;
import com.lxzl.erp.dataaccess.dao.mysql.coupon.CouponBatchMapper;
import com.lxzl.erp.dataaccess.dao.mysql.coupon.CouponMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CouponServiceImpl implements CouponService{
    private static final Logger logger = LoggerFactory.getLogger(ConverterUtil.class);
    @Autowired
    private CouponMapper couponMapper;
    @Autowired
    private CouponBatchDetailMapper couponBatchDetailMapper;
    @Autowired
    private CouponBatchMapper couponBatchMapper;

    @Override
    public ServiceResult<String, String> addCouponBatch(CouponBatch couponBatch) {
        ServiceResult<String,String> serviceResult = new ServiceResult<>();
        Date date = new Date();
        return serviceResult;
    }
}
