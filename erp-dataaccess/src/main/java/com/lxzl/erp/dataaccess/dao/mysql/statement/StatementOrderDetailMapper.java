package com.lxzl.erp.dataaccess.dao.mysql.statement;

import com.lxzl.erp.common.domain.export.FinanceStatementOrderPayDetail;
import com.lxzl.erp.dataaccess.domain.order.OrderFlowDO;
import com.lxzl.erp.common.domain.statement.StatementOrderMonthQueryParam;
import com.lxzl.erp.common.domain.statement.pojo.dto.CheckStatementSummaryDTO;
import com.lxzl.erp.dataaccess.domain.statement.CheckStatementOrderDetailDO;
import com.lxzl.erp.dataaccess.domain.statement.StatementOrderDetailDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface StatementOrderDetailMapper extends BaseMysqlDAO<StatementOrderDetailDO> {

    List<StatementOrderDetailDO> findByOrderId(@Param("orderId") Integer orderId);

    List<StatementOrderDetailDO> findByOrderIdForOrderDetail(@Param("orderId") Integer orderId);

    StatementOrderDetailDO findByOrderIdForOrderLastDetail(@Param("orderId") Integer orderId, @Param("statementExpectPayTime") Date statementExpectPayTime);

    List<StatementOrderDetailDO> findByOrderIdAndOrderItemType(@Param("orderId") Integer orderId, @Param("orderItemType") Integer orderItemType, @Param("orderItemReferId") Integer orderItemReferId);

    List<StatementOrderDetailDO> findByReturnOrderId(@Param("orderId") Integer orderId);

    List<StatementOrderDetailDO> findByOrderItemTypeAndId(@Param("orderItemType") Integer orderItemType, @Param("orderItemId") Integer orderItemId);
    List<StatementOrderDetailDO> findByOrderItemTypeAndIdIngnoreOrderType(@Param("orderItemType") Integer orderItemType, @Param("orderItemId") Integer orderItemId);

    List<StatementOrderDetailDO> findByOrderTypeAndId(@Param("orderType") Integer orderType, @Param("orderId") Integer orderId);

    List<StatementOrderDetailDO> findByOrderTypeAndOrderNoList(@Param("orderType") Integer orderType, @Param("list") List<OrderFlowDO> list);

    List<StatementOrderDetailDO> findByStatementOrderId(@Param("statementOrderId") Integer statementOrderId);

    List<StatementOrderDetailDO> listPage(@Param("maps") Map<String, Object> paramMap);

    Integer listCount(@Param("maps") Map<String, Object> paramMap);

    Integer saveList(@Param("statementOrderDetailDOList") List<StatementOrderDetailDO> statementOrderDetailDOList);

    List<StatementOrderDetailDO> listAllForStatistics(@Param("maps") Map<String, Object> paramMap);

    List<StatementOrderDetailDO> listAllForHome(@Param("maps") Map<String, Object> paramMap);

    BigDecimal queryAllRentIncomeForHome(@Param("maps") Map<String, Object> paramMap);

    List<StatementOrderDetailDO> findByStatementOrderIdAndItemReferId(@Param("itemReferId") Integer itemReferId, @Param("statementOrderId") Integer statementOrderId, @Param("reletItemReferId") Integer reletItemReferId);

    Integer batchUpdate(@Param("list") List<StatementOrderDetailDO> list);

    void deleteByOrderId(@Param("orderId")Integer orderId,@Param("updateUser")String updateUser);

    List<StatementOrderDetailDO> findByReturnReferIdAndStatementType(@Param("returnReferId") Integer returnReferId, @Param("statementDetailType") Integer statementDetailType);

    List<StatementOrderDetailDO> listAllForRentInfo(@Param("maps") Map<String, Object> paramMap);

    void realDeleteStatementOrderDetailList(List<StatementOrderDetailDO> statementOrderDetailDOList);

    Integer queryStatementOrderDetailCountByParam(@Param("maps") Map<String, Object> maps);

    List<FinanceStatementOrderPayDetail> queryStatementOrderDetailByParam(@Param("maps") Map<String, Object> maps);

    /**
     * 查找续租单项关联结算(仅商品租金)
     * @param ids
     * @return
     */
    List<StatementOrderDetailDO> findProductRentByReletOrderItemReferIds(@Param("ids")List<Integer> ids);

    /**
     * 查找续租单项关联结算(仅物料租金)
     * @param ids
     * @return
     */
    List<StatementOrderDetailDO> findMaterialRentByReletOrderItemReferIds(@Param("ids")List<Integer> ids);

    /**
     * 续租单租金结算
     * @param id
     * @return
     */
    List<StatementOrderDetailDO> findPrdcByReletOrderItemReferIds(@Param("id")Integer id);
    List<StatementOrderDetailDO> findMtrByReletOrderItemReferIds(@Param("id")Integer id);

    /**
     * 删除结算详情
     * @param statementOrderDetailDOList
     */
    void deleteStatementOrderDetailList(List<StatementOrderDetailDO> statementOrderDetailDOList);

    /**
     * 查询退货结算详情
     * @param orderItemType
     * @param orderItemId
     * @return
     */
    List<StatementOrderDetailDO> findReturnByOrderItemTypeAndId(@Param("orderItemType") Integer orderItemType, @Param("orderItemId") Integer orderItemId);

    List<CheckStatementOrderDetailDO> exportListPage(@Param("maps") Map<String, Object> maps);
    List<CheckStatementOrderDetailDO> exportListPage1(@Param("maps") Map<String, Object> maps);
    List<CheckStatementOrderDetailDO> exportListPage2(@Param("maps") Map<String, Object> maps);
    List<CheckStatementOrderDetailDO> exportListPage3(@Param("maps") Map<String, Object> maps);
    List<CheckStatementOrderDetailDO> exportReturnListPage(@Param("maps") Map<String, Object> maps);
    List<CheckStatementOrderDetailDO> exportFirstReturnListPage(@Param("maps") Map<String, Object> maps);
    List<CheckStatementOrderDetailDO> findByOrderItemReferIdAndTime(@Param("orderItemReferId") Integer orderItemReferId,@Param("statementExpectPayTime") Date statementExpectPayTime);
    List<CheckStatementOrderDetailDO> exportReletReturnListPage(@Param("maps") Map<String, Object> maps);

    Map<String,Object> findThisPeriodsByOrderInfo(@Param("orderId") Integer orderId, @Param("orderItemReferId") Integer orderItemReferId, @Param("expectPayTime") Date expectPayTime, @Param("payMode") Integer payMode);

    List<StatementOrderDetailDO> findByIdList(@Param("ids")List<Integer> ids);

    /**
     * 查询订单项某一类型结算
     * @param orderItemType
     * @param orderItemReferId
     * @param statementDetailType
     * @return
     */
    List<StatementOrderDetailDO> findOrderItemStatementByType(@Param("orderItemType")Integer orderItemType,@Param("orderItemReferId")Integer orderItemReferId,@Param("statementDetailType")Integer statementDetailType);

    List<StatementOrderDetailDO> findByOrderIdAndStatementDetailType(@Param("orderId") Integer orderId, @Param("orderType") Integer orderType,@Param("statementDetailType") Integer statementDetailType);

    List<StatementOrderDetailDO> listByCustomerId(@Param(value = "queryParam") StatementOrderMonthQueryParam queryParam);

    List<StatementOrderDetailDO> listUnRentByOrderIds(@Param(value = "queryParam") StatementOrderMonthQueryParam queryParam);

    CheckStatementSummaryDTO sumStatementDetailAmountByCustomerNo(@Param(value = "queryParam") StatementOrderMonthQueryParam queryParam);

    List<StatementOrderDetailDO> findByOrderTypeAndSourceId(@Param("orderType") Integer orderType, @Param("sourceId") Integer sourceId);

}