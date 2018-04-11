package com.lxzl.erp.dataaccess.dao.mysql.coupon;

import com.lxzl.erp.common.domain.coupon.pojo.Coupon;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.coupon.CouponDO;import org.apache.ibatis.annotations.Param;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Repository;
import java.util.Map;

@Repository
public interface CouponMapper extends BaseMysqlDAO<CouponDO> {

	List<CouponDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);

    void addList(@Param("list") List<CouponDO> list);

    Integer findCouponCountByParams(@Param("maps") Map<String, Object> maps);

    List<CouponDO> findCouponByParams(@Param("maps") Map<String, Object> maps);

    void deleteCouponList(@Param("couponDOList") List<CouponDO> couponDOList);

    Integer findCouponStatusCountIsZeroByCouponBatchDetailId(Integer couponBatchDetailId);

    void updateList(@Param("couponDOList") List<CouponDO> couponDOList);

    List<CouponDO> findByCouponStatus(@Param("couponBatchDetailId") Integer couponBatchDetailId,@Param("totalCouponProvideAmount") Integer totalCouponProvideAmount);

    List<CouponDO> findByCustomerNo(@Param("customerNo") String customerNo);

    List<CouponDO> findCouponDOList(@Param("couponDOIdList") List<Integer> couponDOIdList);

    List<CouponDO> findByCouponBatchDetailID(@Param("couponBatchDetailId") Integer couponBatchDetailId);

    void updateUseList(@Param("couponDOList") List<CouponDO> couponDOList);

    void cancelCoupon(@Param("couponBatchDetailId") Integer couponBatchDetailId,@Param("updateTime") Date updateTime, @Param("updateUser") String updateUser);
}