package com.lxzl.erp.dataaccess.dao.mysql.coupon;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.coupon.CouponBatchDetailDO;import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Repository;
import java.util.Map;

@Repository
public interface CouponBatchDetailMapper extends BaseMysqlDAO<CouponBatchDetailDO> {

	List<CouponBatchDetailDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);

    Integer findCouponBatchDetailCountByParams(@Param("maps") Map<String, Object> maps);

	List<CouponBatchDetailDO> findCouponBatchDetailByParams(@Param("maps") Map<String, Object> maps);


    void updateCouponBatchDetailDOList(@Param("couponBatchDetailDOList") List<CouponBatchDetailDO> couponBatchDetailDOList);

	List<CouponBatchDetailDO> findByCouponBatchID(@Param("couponBatchId") Integer couponBatchId);

    void updateUseList(@Param("couponBatchDetailDOList") List<CouponBatchDetailDO> couponBatchDetailDOList);
}