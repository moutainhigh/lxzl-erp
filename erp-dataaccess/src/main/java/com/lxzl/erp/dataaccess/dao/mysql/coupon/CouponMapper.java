package com.lxzl.erp.dataaccess.dao.mysql.coupon;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.coupon.CouponDO;import org.apache.ibatis.annotations.Param;
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

    CouponDO findByIdIgnoreDataStatus(Integer integer);
}