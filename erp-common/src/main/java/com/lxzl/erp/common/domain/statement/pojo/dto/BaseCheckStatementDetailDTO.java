package com.lxzl.erp.common.domain.statement.pojo.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.lxzl.erp.common.constant.OrderRentType;
import com.lxzl.erp.common.constant.StatementOrderStatus;
import com.lxzl.erp.common.domain.k3.pojo.returnOrder.K3ReturnOrder;
import com.lxzl.erp.common.domain.k3.pojo.returnOrder.K3ReturnOrderDetail;
import com.lxzl.erp.common.domain.k3.pojo.returnOrder.ReturnReasonType;
import com.lxzl.erp.common.domain.order.pojo.Order;
import com.lxzl.erp.common.domain.order.pojo.OrderMaterial;
import com.lxzl.erp.common.domain.order.pojo.OrderProduct;
import com.lxzl.erp.common.domain.reletorder.pojo.ReletOrder;
import com.lxzl.erp.common.domain.reletorder.pojo.ReletOrderMaterial;
import com.lxzl.erp.common.domain.reletorder.pojo.ReletOrderProduct;
import com.lxzl.erp.common.util.BigDecimalUtil;
import com.lxzl.erp.common.util.CheckStatementUtil;
import com.lxzl.erp.common.util.DateUtil;
import com.lxzl.erp.common.util.JSONUtil;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author daiqi
 * @create 2018-07-09 9:07
 */
public abstract class BaseCheckStatementDetailDTO implements CheckStatementDetailDTOInf {

    private Map<String, String> importData = new LinkedHashMap<>();

    protected CheckStatementMapContainer mapContainer;

    /**
     * 订单原始id
     */
    private Integer orderOriginalId;
    /**
     * 结算单详情编号
     */
    private Integer statementOrderDetailId;
    /**
     * 退还时间
     */
    private Date returnTime;
    /**
     * 结算单ID
     */
    private Integer statementOrderId;
    /**
     * 客户ID
     */
    private Integer customerId;
    /**
     * 客户名称
     */
    private Integer customerName;
    /**
     * 单子类型，详见ORDER_TYPE
     */
    private Integer orderType;
    /**
     * 订单ID
     */
    private Integer orderId;
    /**
     * 订单项类型，1为商品，2为物料
     */
    private Integer orderItemType;
    /**
     * 订单项关联ID
     */
    private Integer orderItemReferId;
    /**
     * 退款的时候，关联的结算单detail ID
     */
    private Integer returnReferId;
    /**
     * 退货订单号
     */
    private String returnOrderNo;
    /**
     * 结算单明细类型
     */
    private Integer statementDetailType;
    /**
     * 结算单期数
     */
    private Integer statementDetailPhase;
    /**
     * 结算单预计支付时间
     */
    private Date statementExpectPayTime;
    /**
     * 结算单金额
     */
    private BigDecimal statementDetailAmount;
    /**
     * 结算单其他金额
     */
    private BigDecimal statementDetailOtherAmount;
    /**
     * 结算详情其他支付金额
     */
    private BigDecimal statementDetailOtherPaidAmount;
    /**
     * 已付总金额
     */
    private BigDecimal statementDetailPaidAmount;
    /**
     * 结算租金押金金额
     */
    private BigDecimal statementDetailRentDepositAmount;
    /**
     * 已付租金押金金额
     */
    private BigDecimal statementDetailRentDepositPaidAmount;
    /**
     * 退还租金押金金额
     */
    private BigDecimal statementDetailRentDepositReturnAmount;
    /**
     * 退还租金押金时间
     */
    private Date statementDetailRentDepositReturnTime;
    /**
     * 结算押金金额
     */
    private BigDecimal statementDetailDepositAmount;
    /**
     * 已付押金金额
     */
    private BigDecimal statementDetailDepositPaidAmount;
    /**
     * 退还押金金额
     */
    private BigDecimal statementDetailDepositReturnAmount;
    /**
     * 退还押金时间
     */
    private Date statementDetailDepositReturnTime;
    /**
     * 结算单租金金额
     */
    private BigDecimal statementDetailRentAmount;
    /**
     * 租金已付金额
     */
    private BigDecimal statementDetailRentPaidAmount;
    /**
     * 结算单支付时间
     */
    private Date statementDetailPaidTime;
    /**
     * 逾期金额
     */
    private BigDecimal statementDetailOverdueAmount;
    /**
     * 逾期支付金额
     */
    private BigDecimal statementDetailOverduePaidAmount;
    /**
     * 逾期天数
     */
    private Integer statementDetailOverdueDays;
    /**
     * 逾期数量
     */
    private Integer statementDetailOverduePhaseCount;
    /**
     * 违约金
     */
    private BigDecimal statementDetailPenaltyAmount;
    /**
     * 已付违约金
     */
    private BigDecimal statementDetailPenaltyPaidAmount;
    /**
     * 结算状态，0未结算，1已结算
     */
    private Integer statementDetailStatus;
    /**
     * 结算开始时间
     */
    private Date statementStartTime;
    /**
     * 结算结束时间
     */
    private Date statementEndTime;
    /**
     * 结算单冲正金额
     */
    private BigDecimal statementDetailCorrectAmount;
    /**
     * 结算单优惠券优惠总和
     */
    private BigDecimal statementCouponAmount;
    /**
     * 续租订单项ID  查询时结算单关联续租
     */
    private Integer reletOrderItemReferId;
    /**
     * 订单编号
     */
    private String orderNo;
    /**
     * 订单项名称
     */

    private String itemName;
    /**
     * 订单项详细名称
     */
    private String itemSkuName;
    /**
     * 订单项数量
     */
    private Integer itemCount;
    /**
     * 每项的金额
     */
    private BigDecimal unitAmount;
    /**
     * 租赁类型
     */
    private Integer itemRentType;
    /**
     * 订单类型
     */
    private Integer orderRentType;
    /**
     * 订单起始时间
     */
    private Date orderRentStartTime;
    /**
     * 订单预计支付时间
     */
    private Date orderExpectReturnTime;
    /**
     * 订单时长
     */
    private Integer orderRentTimeLength;
    /**
     * 是否全新
     */
    private Integer isNew;
    /**
     * 支付方式
     */
    private Integer payMode;
    /**
     * 押金周期
     */
    private Integer depositCycle;
    /**
     * 付款周期
     */
    private Integer paymentCycle;
    /**
     * 本期付款月数
     */
    private Date statementExpectPayEndTime;
    /**
     * 本期应付数
     */
    private BigDecimal statementDetailEndAmount;
    /**
     * 本期应付租金
     */
    private BigDecimal statementDetailRentEndAmount;

    /**
     * 本月应付金额
     */
    private BigDecimal monthPayableAmount;
    /**
     * 本月已付金额
     */
    private BigDecimal monthPaidAmount;
    /**
     * 本月未付金额
     */
    private BigDecimal monthUnpaidAmount;
    /**
     * 退货原因字符串
     */
    private Integer returnReasonType;
    /**
     * 当月月份
     */
    private String month;
    /**
     * 订单项实际id
     */
    private Integer orderItemActualId;

    public Integer getSortItemType() {
        return 0;
    }

    public final BaseCheckStatementDetailDTO build() {
        loadAmountData();
        if (MapUtils.isEmpty(importData)) {
            this.loadData();
        }
        buildImportData();
        buildOther();
        return this;
    }

    public BaseCheckStatementDetailDTO loadAmountData() {
        if (this.itemCount == null) {
            this.itemCount = 0;
        }
        this.statementDetailAmount = CheckStatementUtil.initBigDecimal(this.statementDetailAmount);
        this.statementDetailOtherAmount = CheckStatementUtil.initBigDecimal(this.statementDetailOtherAmount);
        this.statementDetailOtherPaidAmount = CheckStatementUtil.initBigDecimal(this.statementDetailOtherPaidAmount);
        this.statementDetailPaidAmount = CheckStatementUtil.initBigDecimal(this.statementDetailPaidAmount);
        this.statementDetailRentDepositAmount = CheckStatementUtil.initBigDecimal(this.statementDetailRentDepositAmount);
        this.statementDetailRentDepositPaidAmount = CheckStatementUtil.initBigDecimal(this.statementDetailRentDepositPaidAmount);
        this.statementDetailRentDepositReturnAmount = CheckStatementUtil.initBigDecimal(this.statementDetailRentDepositReturnAmount);
        this.statementDetailDepositAmount = CheckStatementUtil.initBigDecimal(this.statementDetailDepositAmount);
        this.statementDetailDepositPaidAmount = CheckStatementUtil.initBigDecimal(this.statementDetailDepositPaidAmount);
        this.statementDetailDepositReturnAmount = CheckStatementUtil.initBigDecimal(this.statementDetailDepositReturnAmount);
        this.statementDetailRentAmount = CheckStatementUtil.initBigDecimal(this.statementDetailRentAmount);
        this.statementDetailRentPaidAmount = CheckStatementUtil.initBigDecimal(this.statementDetailRentPaidAmount);
        this.statementDetailOverdueAmount = CheckStatementUtil.initBigDecimal(this.statementDetailOverdueAmount);
        this.statementDetailOverduePaidAmount = CheckStatementUtil.initBigDecimal(this.statementDetailOverduePaidAmount);
        this.statementDetailPenaltyAmount = CheckStatementUtil.initBigDecimal(this.statementDetailPenaltyAmount);
        this.statementDetailCorrectAmount = CheckStatementUtil.initBigDecimal(this.statementDetailCorrectAmount);
        this.unitAmount = CheckStatementUtil.initBigDecimal(this.unitAmount);
        this.statementDetailEndAmount = CheckStatementUtil.initBigDecimal(this.statementDetailEndAmount);
        this.statementDetailRentEndAmount = CheckStatementUtil.initBigDecimal(this.statementDetailRentEndAmount);
        this.monthUnpaidAmount = CheckStatementUtil.initBigDecimal(this.monthUnpaidAmount);
        this.monthPaidAmount = CheckStatementUtil.initBigDecimal(this.monthPaidAmount);
        this.monthPayableAmount = CheckStatementUtil.initBigDecimal(this.monthPayableAmount);
        return this;
    }

    public BaseCheckStatementDetailDTO buildImportData() {
        this.importData.put("orderNo", getOrderNoStr());
        this.importData.put("businessType", getBusinessTypeStr());
        this.importData.put("orderItemType", getOrderItemTypeStr());
        this.importData.put("itemName", getItemNameStr());
        this.importData.put("itemSkuName", getItemSkuNameStr());
        this.importData.put("isNew", getIsNewStr());
        this.importData.put("rentStartTime", getRentStartTimeStr());
        this.importData.put("expectReturnTime", getExpectReturnTimeStr());
        this.importData.put("month", getMonthStr());
        this.importData.put("day", getDayStr());
        this.importData.put("allRentTimeLength", getAllRentTimeLengthStr());
        this.importData.put("allPeriodStartAndEnd", getAllPeriodStartAndEndStr());
        this.importData.put("statementStartTime", getStatementStartTimeStr());
        this.importData.put("statementEndTime", getStatementEndTimeStr());
        this.importData.put("statementMonth", getStatementMonthStr());
        this.importData.put("statementDay", getStatementDayStr());
        this.importData.put("rentTimeLength", getRentTimeLengthStr());
        this.importData.put("currentPeriodStartAndEnd", getCurrentPeriodStartAndEndStr());
        this.importData.put("payMode", getPayModeStr());
        this.importData.put("rentProgramme", getRentProgrammeStr());
        this.importData.put("itemCount", getItemCountStr());
        this.importData.put("unitAmountInfo", getUnitAmountInfoStr());
        this.importData.put("statementDetailRentAmount", getStatementDetailRentAmountStr());
        this.importData.put("statementDepositAmount", getStatementDepositAmountStr());
        this.importData.put("statementOverdueAmount", getStatementOverdueAmountStr());
        this.importData.put("statementOtherAmount", getStatementOtherAmountStr());
        this.importData.put("statementCorrectAmount", getStatementCorrectAmountStr());
        this.importData.put("statementDetailAmount", getStatementDetailAmountStr());
        this.importData.put("statementExpectPayTime", getStatementExpectPayTimeStr());
        this.importData.put("monthPayableAmount", getMonthPayableAmountStr());
        this.importData.put("statementDetailStatus", getStatementDetailStatusStr());
        this.importData.put("k3ReturnOrderDONo", getK3ReturnOrderDONoStr());
        this.importData.put("returnReasonType", getReturnReasonTypeStr());
        this.importData.put("statementCorrectNo", getStatementCorrectNoStr());
        this.importData.put("statementCorrectReason", getStatementCorrectReasonStr());
        return this;
    }

    protected BaseCheckStatementDetailDTO loadData() {
        return this;
    }

    /**
     * 子类通过继承重写该方法来进行构建的拓展
     */
    protected BaseCheckStatementDetailDTO buildOther() {
        return this;
    }

    public final Map<String, String> getImportData() {
        return importData;
    }

    public final void putImportData(String key, String value) {
        this.importData.put(key, value);
    }

    public BaseCheckStatementDetailDTO mapContainer(CheckStatementMapContainer mapContainer) {
        this.mapContainer = mapContainer;
        return this;
    }

    public Order getOrderById(Integer id) {
        Map<Integer, Order> orderMap = this.mapContainer.getIdOrderMap();
        return orderMap.get(id);
    }

    public Order getOrderByNo(String orderNo) {
        Map<String, Order> orderMap = this.mapContainer.getNoOrderMap();
        return orderMap.get(orderNo);
    }

    public K3ReturnOrderDetail getK3ReturnOrderDetailById(Integer id) {
        return this.mapContainer.getIdK3ReturnOrderDetailMap().get(id);
    }

    public K3ReturnOrder getK3ReturnOrderById(Integer id) {
        return this.mapContainer.getIdK3ReturnOrderMap().get(id);
    }

    public OrderProduct getOrderProductById(Integer id) {
        return this.mapContainer.getIdOrderProductMap().get(id);
    }

    public OrderMaterial getOrderMaterialById(Integer id) {
        return this.mapContainer.getIdOrderMaterialMap().get(id);
    }

    public ReletOrder getReletOrderById(Integer id) {
        return this.mapContainer.getIdReletOrderMap().get(id);
    }

    public ReletOrderProduct getReletOrderProductById(Integer id) {
        return this.mapContainer.getIdReletOrderProductMap().get(id);
    }

    public ReletOrderMaterial getReletOrderMaterialById(Integer id) {
        return this.mapContainer.getIdReletOrderMaterialMap().get(id);
    }

    public BaseCheckStatementDetailDTO clone() {
        BaseCheckStatementDetailDTO cloneDTO = JSONUtil.parseObject(this, this.getClass());
        return cloneDTO.mapContainer(this.mapContainer);
    }

    public BaseCheckStatementDetailDTO buildMonthAmount(Date statementExpectPayTime, CheckStatementStatisticsDTO statementStatisticsDTO) {
        String thisMonthStr = statementStatisticsDTO.getMonth();
        this.monthPayableAmount = BigDecimal.ZERO;
        this.monthPaidAmount = BigDecimal.ZERO;
        this.monthUnpaidAmount = BigDecimal.ZERO;
        BigDecimal detailAmount = BigDecimalUtil.add(this.statementDetailRentAmount, this.statementDetailDepositAmount);
        BigDecimal otherAmount = BigDecimalUtil.add(this.statementDetailOverdueAmount, this.statementDetailOtherAmount);
        BigDecimal statementDetailAmountTemp = BigDecimalUtil.add(detailAmount, otherAmount);
        statementDetailAmountTemp = BigDecimalUtil.add(this.statementDetailCorrectAmount, statementDetailAmountTemp);
        statementDetailAmountTemp = BigDecimalUtil.add(this.statementDetailRentDepositAmount, statementDetailAmountTemp);

        if (this.statementExpectPayTime == null) {
            return this;
        }
        String statementExpectPayTimeMonth = DateFormatUtils.format(statementExpectPayTime, "yyyy-MM");
        if (StringUtils.equals(thisMonthStr, statementExpectPayTimeMonth)) {
            this.monthPayableAmount = statementDetailAmountTemp;
            if (StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED.equals(this.statementDetailStatus)) {
                this.monthPaidAmount = statementDetailAmountTemp;
            } else {
                this.monthUnpaidAmount = statementDetailAmountTemp;
            }
        }
        return this;
    }

    /**
     * 合并结算单的数据
     */
    public void mergeToTarget(BaseCheckStatementDetailDTO targetDetail, CheckStatementStatisticsDTO statementStatisticsDTO) {

    }

    public void mergeAmountToTarget(BaseCheckStatementDetailDTO targetDetail) {
        targetDetail.setStatementDetailAmount(BigDecimalUtil.add(this.getStatementDetailAmount(), targetDetail.getStatementDetailAmount()));
        targetDetail.setStatementDetailOtherAmount(BigDecimalUtil.add(this.getStatementDetailOtherAmount(), targetDetail.getStatementDetailOtherAmount()));
        targetDetail.setStatementDetailOtherPaidAmount(BigDecimalUtil.add(this.getStatementDetailOtherPaidAmount(), targetDetail.getStatementDetailOtherPaidAmount()));
        targetDetail.setStatementDetailRentDepositAmount(BigDecimalUtil.add(this.getStatementDetailRentDepositAmount(), targetDetail.getStatementDetailRentDepositAmount()));
        targetDetail.setStatementDetailRentDepositPaidAmount(BigDecimalUtil.add(this.getStatementDetailRentDepositPaidAmount(), targetDetail.getStatementDetailRentDepositPaidAmount()));
        targetDetail.setStatementDetailRentDepositReturnAmount(BigDecimalUtil.add(this.getStatementDetailRentDepositReturnAmount(), targetDetail.getStatementDetailRentDepositReturnAmount()));
        targetDetail.setStatementDetailDepositAmount(BigDecimalUtil.add(this.getStatementDetailDepositAmount(), targetDetail.getStatementDetailDepositAmount()));
        targetDetail.setStatementDetailDepositPaidAmount(BigDecimalUtil.add(this.getStatementDetailDepositPaidAmount(), targetDetail.getStatementDetailDepositPaidAmount()));
        targetDetail.setStatementDetailDepositReturnAmount(BigDecimalUtil.add(this.getStatementDetailDepositReturnAmount(), targetDetail.getStatementDetailDepositReturnAmount()));
        targetDetail.setStatementDetailRentAmount(BigDecimalUtil.add(this.getStatementDetailRentAmount(), targetDetail.getStatementDetailRentAmount()));
        targetDetail.setStatementDetailRentPaidAmount(BigDecimalUtil.add(this.getStatementDetailRentPaidAmount(), targetDetail.getStatementDetailRentPaidAmount()));
        targetDetail.setStatementDetailOverdueAmount(BigDecimalUtil.add(this.getStatementDetailOverdueAmount(), targetDetail.getStatementDetailOverdueAmount()));
        targetDetail.setStatementDetailOverduePaidAmount(BigDecimalUtil.add(this.getStatementDetailOverduePaidAmount(), targetDetail.getStatementDetailOverduePaidAmount()));
        targetDetail.setStatementDetailCorrectAmount(BigDecimalUtil.add(this.getStatementDetailCorrectAmount(), targetDetail.getStatementDetailCorrectAmount()));
    }

    @JSONField(serialize = false)
    public boolean isAddTheMonth(CheckStatementStatisticsDTO statementStatisticsDTO) {
        return true;
    }

    @JSONField(serialize = false)
    public boolean isShowTheMonth(CheckStatementStatisticsDTO statementStatisticsDTO) {
        boolean showFlag = false;
        if (this.getItemCount() != 0) {
            showFlag = true;
        }
        if (BigDecimal.ZERO.compareTo(this.statementDetailAmount) != 0) {
            showFlag = true;
        }
        if (BigDecimal.ZERO.compareTo(this.statementDetailOtherAmount) != 0) {
            showFlag = true;
        }
        if (BigDecimal.ZERO.compareTo(this.statementDetailOtherPaidAmount) != 0) {
            showFlag = true;
        }
        if (BigDecimal.ZERO.compareTo(this.statementDetailPaidAmount) != 0) {
            showFlag = true;
        }
        if (BigDecimal.ZERO.compareTo(this.statementDetailRentDepositAmount) != 0) {
            showFlag = true;
        }
        if (BigDecimal.ZERO.compareTo(this.statementDetailRentDepositPaidAmount) != 0) {
            showFlag = true;
        }
        if (BigDecimal.ZERO.compareTo(this.statementDetailRentDepositReturnAmount) != 0) {
            showFlag = true;
        }
        if (BigDecimal.ZERO.compareTo(this.statementDetailDepositAmount) != 0) {
            showFlag = true;
        }
        if (BigDecimal.ZERO.compareTo(this.statementDetailDepositPaidAmount) != 0) {
            showFlag = true;
        }
        if (BigDecimal.ZERO.compareTo(this.statementDetailDepositReturnAmount) != 0) {
            showFlag = true;
        }
        if (BigDecimal.ZERO.compareTo(this.statementDetailRentAmount) != 0) {
            showFlag = true;
        }
        if (BigDecimal.ZERO.compareTo(this.statementDetailRentPaidAmount) != 0) {
            showFlag = true;
        }
        if (BigDecimal.ZERO.compareTo(this.statementDetailOverdueAmount) != 0) {
            showFlag = true;
        }
        if (BigDecimal.ZERO.compareTo(this.statementDetailOverduePaidAmount) != 0) {
            showFlag = true;
        }
        if (BigDecimal.ZERO.compareTo(this.statementDetailPenaltyAmount) != 0) {
            showFlag = true;
        }
        if (BigDecimal.ZERO.compareTo(this.statementDetailCorrectAmount) != 0) {
            showFlag = true;
        }
        if (BigDecimal.ZERO.compareTo(this.statementDetailEndAmount) != 0) {
            showFlag = true;
        }
        if (BigDecimal.ZERO.compareTo(this.statementDetailRentEndAmount) != 0) {
            showFlag = true;
        }
        return showFlag;
    }

    @JSONField(serialize = false)
    public boolean isMergeOrder(CheckStatementStatisticsDTO statementStatisticsDTO) {
        return false;
    }

    @JSONField(serialize = false)
    public boolean isMergeReturnOrder(CheckStatementStatisticsDTO statementStatisticsDTO) {
        return false;
    }

    @JSONField(serialize = false)
    public String getCacheKey(CheckStatementStatisticsDTO statementStatisticsDTO) {

        String cacheKey = this.getOrderOriginalId() + "_" + this.getOrderItemActualId() + "_" + this.getOrderItemType();
        cacheKey = cacheKey + "_" + DateFormatUtils.format(getStatementExpectPayTime(), "yyyyMMdd");
        return cacheKey;
    }

    public void setId(Integer id) {
        this.statementOrderDetailId = id;
    }

    public Integer getOrderOriginalItemType(CheckStatementStatisticsDTO statementStatisticsDTO) {
        return null;
    }

    @Override
    public String getOrderNoStr() {
        return this.orderNo;
    }

    @Override
    public String getBusinessTypeStr() {
        return CheckStatementUtil.getBusinessTypeStr(this.orderType);
    }

    @Override
    public final String getOrderItemTypeStr() {
        return CheckStatementUtil.getOrderItemTypeStr(this.getOrderItemType());
    }

    @Override
    public final String getItemNameStr() {
        return this.itemName;
    }

    @Override
    public final String getItemSkuNameStr() {
        return this.itemSkuName;
    }

    @Override
    public final String getIsNewStr() {
        return CheckStatementUtil.getIsNewStr(this.isNew);
    }

    @Override
    public final String getRentStartTimeStr() {
        if (this.orderRentStartTime == null) {
            return CheckStatementUtil.PLACEHOLDER_DEFAULT;
        }
        return DateFormatUtils.format(this.orderRentStartTime, "yyyy/MM/dd");
    }

    @Override
    public final String getExpectReturnTimeStr() {
        return DateFormatUtils.format(this.orderExpectReturnTime, "yyyy/MM/dd");
    }

    @Override
    public final String getItemCountStr() {
        return this.itemCount.toString();
    }

    @Override
    public String getMonthStr() {
        if (OrderRentType.RENT_TYPE_MONTH.equals(itemRentType)) {
            int[] rentMonthDay = DateUtil.getDiff(this.orderRentStartTime, this.orderExpectReturnTime);
            return String.valueOf(rentMonthDay[0]);
        } else if (OrderRentType.RENT_TYPE_DAY.equals(itemRentType)) {
            return String.valueOf(0);
        }
        return String.valueOf(0);
    }

    @Override
    public String getDayStr() {
        if (OrderRentType.RENT_TYPE_MONTH.equals(itemRentType)) {
            int[] rentMonthDay = DateUtil.getDiff(this.orderRentStartTime, this.orderExpectReturnTime);
            return String.valueOf(rentMonthDay[1]);
        } else if (OrderRentType.RENT_TYPE_DAY.equals(itemRentType)) {
            return String.valueOf(DateUtil.daysBetween(this.orderRentStartTime, this.orderExpectReturnTime) + 1);
        }
        return String.valueOf(1);
    }

    @Override
    public String getAllRentTimeLengthStr() {
        String month = getMonthStr();
        String day = getDayStr();
        if (OrderRentType.RENT_TYPE_MONTH.equals(itemRentType)) {
            return month + "月" + day + "天";
        } else if (OrderRentType.RENT_TYPE_DAY.equals(itemRentType)) {
            return day + "天";
        }
        return day + "天";
    }

    @Override
    public String getAllPeriodStartAndEndStr() {
        String allRentTimeLength = getAllRentTimeLengthStr();
        return CheckStatementUtil.getPeriod(this.orderRentStartTime, this.orderExpectReturnTime, allRentTimeLength);
    }

    @Override
    public final String getStatementStartTimeStr() {
        if (this.statementStartTime == null) {
            return CheckStatementUtil.PLACEHOLDER_DEFAULT;
        }
        return DateFormatUtils.format(this.statementStartTime, "yyyy/MM/dd");
    }

    @Override
    public final String getStatementEndTimeStr() {
        if (this.statementEndTime == null) {
            return CheckStatementUtil.PLACEHOLDER_DEFAULT;
        }
        return DateFormatUtils.format(this.statementEndTime, "yyyy/MM/dd");
    }

    @Override
    public final String getStatementMonthStr() {
        if (this.statementStartTime == null|| this.statementEndTime == null) {
            return CheckStatementUtil.PLACEHOLDER_DEFAULT;
        }
        if (OrderRentType.RENT_TYPE_MONTH.equals(itemRentType)) {
            int[] statementMonthDay = DateUtil.getDiff(this.statementStartTime, this.statementEndTime);
            return String.valueOf(statementMonthDay[0]);
        } else if (OrderRentType.RENT_TYPE_DAY.equals(itemRentType)) {
            return String.valueOf(0);
        }
        return String.valueOf(0);
    }

    @Override
    public final String getStatementDayStr() {
        if (this.statementStartTime == null|| this.statementEndTime == null) {
            return CheckStatementUtil.PLACEHOLDER_DEFAULT;
        }
        if (OrderRentType.RENT_TYPE_MONTH.equals(itemRentType)) {
            int[] statementMonthDay = DateUtil.getDiff(this.statementStartTime, this.statementEndTime);
            return String.valueOf(statementMonthDay[1]);
        } else if (OrderRentType.RENT_TYPE_DAY.equals(itemRentType)) {
            return String.valueOf(DateUtil.daysBetween(this.statementStartTime, this.statementEndTime) + 1);
        }
        return String.valueOf(1);

    }

    @Override
    public final String getRentTimeLengthStr() {
        if (this.statementStartTime == null|| this.statementEndTime == null) {
            return CheckStatementUtil.PLACEHOLDER_DEFAULT;
        }
        String statementMonth = getStatementMonthStr();
        String statementDay = getStatementDayStr();
        if (OrderRentType.RENT_TYPE_MONTH.equals(itemRentType)) {
            return statementMonth + "月" + statementDay + "天";
        } else if (OrderRentType.RENT_TYPE_DAY.equals(itemRentType)) {
            return statementDay + "天";
        }
        return statementDay + "天";
    }

    @Override
    public final String getCurrentPeriodStartAndEndStr() {
        if (this.statementStartTime == null|| this.statementEndTime == null) {
            return CheckStatementUtil.PLACEHOLDER_DEFAULT;
        }
        String rentTimeLength = getRentTimeLengthStr();
        return CheckStatementUtil.getPeriod(this.statementStartTime, this.statementEndTime, rentTimeLength);
    }

    @Override
    public final String getPayModeStr() {
        return CheckStatementUtil.getPayModeStr(this.payMode);
    }

    @Override
    public final String getRentProgrammeStr() {
        if (this.depositCycle == null || this.paymentCycle == null) {
            return CheckStatementUtil.PLACEHOLDER_DEFAULT;
        }
        return "押" + this.depositCycle + "付" + this.paymentCycle;
    }


    @Override
    public final String getUnitAmountInfoStr() {
        String dayOrMonth = "/月";
        if (OrderRentType.RENT_TYPE_DAY.equals(itemRentType)) {
            return CheckStatementUtil.roundScale(this.unitAmount) + "/日";
        }
        return CheckStatementUtil.roundScale(this.unitAmount) + dayOrMonth;
    }

    @Override
    public final String getStatementDetailRentAmountStr() {
        return this.statementDetailRentAmount.toString();
    }

    @Override
    public final String getStatementDepositAmountStr() {
        return BigDecimalUtil.add(this.statementDetailRentDepositAmount, this.statementDetailDepositAmount).toString();
    }

    @Override
    public final String getStatementOverdueAmountStr() {
        return statementDetailOverdueAmount.toString();
    }

    @Override
    public final String getStatementOtherAmountStr() {
        return statementDetailOtherAmount.toString();
    }

    @Override
    public final String getStatementCorrectAmountStr() {
        return statementDetailCorrectAmount.toString();
    }

    @Override
    public final String getStatementDetailAmountStr() {
        return statementDetailAmount.toString();
    }

    @Override
    public final String getStatementExpectPayTimeStr() {
        if (this.statementExpectPayTime == null) {
            return CheckStatementUtil.PLACEHOLDER_DEFAULT;
        }
        return DateFormatUtils.format(this.statementExpectPayTime, "yyyy/MM/dd");
    }

    @Override
    public String getMonthPayableAmountStr() {
        return this.monthPayableAmount.toString();
    }

    @Override
    public final String getStatementDetailStatusStr() {
        if (this.monthPayableAmount.compareTo(BigDecimal.ZERO) != 0) {
            return CheckStatementUtil.getStatementDetailStatusStr(this.statementDetailStatus);
        }
        return null;
    }

    @Override
    public final String getK3ReturnOrderDONoStr() {
        return this.returnOrderNo;
    }

    @Override
    public final String getReturnReasonTypeStr() {
        return ReturnReasonType.getReturnReasonTypeStr(this.getReturnReasonType());
    }

    @Override
    public final String getStatementCorrectNoStr() {
        return null;
    }

    @Override
    public final String getStatementCorrectReasonStr() {
        return null;
    }

    public Integer getOrderOriginalId() {
        return orderOriginalId;
    }

    public void setOrderOriginalId(Integer orderOriginalId) {
        this.orderOriginalId = orderOriginalId;
    }

    public final Integer getStatementOrderDetailId() {
        return statementOrderDetailId;
    }

    public void setStatementOrderDetailId(Integer statementOrderDetailId) {
        this.statementOrderDetailId = statementOrderDetailId;
    }

    public Integer getStatementOrderId() {
        return statementOrderId;
    }

    public void setStatementOrderId(Integer statementOrderId) {
        this.statementOrderId = statementOrderId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getCustomerName() {
        return customerName;
    }

    public void setCustomerName(Integer customerName) {
        this.customerName = customerName;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getOrderItemType() {
        return orderItemType;
    }

    public void setOrderItemType(Integer orderItemType) {
        this.orderItemType = orderItemType;
    }

    public Integer getOrderItemReferId() {
        return orderItemReferId;
    }

    public void setOrderItemReferId(Integer orderItemReferId) {
        this.orderItemReferId = orderItemReferId;
    }

    public Integer getReturnReferId() {
        return returnReferId;
    }

    public void setReturnReferId(Integer returnReferId) {
        this.returnReferId = returnReferId;
    }

    public String getReturnOrderNo() {
        return returnOrderNo;
    }

    public void setReturnOrderNo(String returnOrderNo) {
        this.returnOrderNo = returnOrderNo;
    }

    public Integer getStatementDetailType() {
        return statementDetailType;
    }

    public void setStatementDetailType(Integer statementDetailType) {
        this.statementDetailType = statementDetailType;
    }

    public Integer getStatementDetailPhase() {
        return statementDetailPhase;
    }

    public void setStatementDetailPhase(Integer statementDetailPhase) {
        this.statementDetailPhase = statementDetailPhase;
    }

    public Date getStatementExpectPayTime() {
        return statementExpectPayTime;
    }

    public void setStatementExpectPayTime(Date statementExpectPayTime) {
        this.statementExpectPayTime = statementExpectPayTime;
    }

    public BigDecimal getStatementDetailAmount() {
        return statementDetailAmount;
    }

    public void setStatementDetailAmount(BigDecimal statementDetailAmount) {
        this.statementDetailAmount = statementDetailAmount;
    }

    public BigDecimal getStatementDetailOtherAmount() {
        return statementDetailOtherAmount;
    }

    public void setStatementDetailOtherAmount(BigDecimal statementDetailOtherAmount) {
        this.statementDetailOtherAmount = statementDetailOtherAmount;
    }

    public BigDecimal getStatementDetailOtherPaidAmount() {
        return statementDetailOtherPaidAmount;
    }

    public void setStatementDetailOtherPaidAmount(BigDecimal statementDetailOtherPaidAmount) {
        this.statementDetailOtherPaidAmount = statementDetailOtherPaidAmount;
    }

    public BigDecimal getStatementDetailPaidAmount() {
        return statementDetailPaidAmount;
    }

    public void setStatementDetailPaidAmount(BigDecimal statementDetailPaidAmount) {
        this.statementDetailPaidAmount = statementDetailPaidAmount;
    }

    public BigDecimal getStatementDetailRentDepositAmount() {
        return statementDetailRentDepositAmount;
    }

    public void setStatementDetailRentDepositAmount(BigDecimal statementDetailRentDepositAmount) {
        this.statementDetailRentDepositAmount = statementDetailRentDepositAmount;
    }

    public BigDecimal getStatementDetailRentDepositPaidAmount() {
        return statementDetailRentDepositPaidAmount;
    }

    public void setStatementDetailRentDepositPaidAmount(BigDecimal statementDetailRentDepositPaidAmount) {
        this.statementDetailRentDepositPaidAmount = statementDetailRentDepositPaidAmount;
    }

    public BigDecimal getStatementDetailRentDepositReturnAmount() {
        return statementDetailRentDepositReturnAmount;
    }

    public void setStatementDetailRentDepositReturnAmount(BigDecimal statementDetailRentDepositReturnAmount) {
        this.statementDetailRentDepositReturnAmount = statementDetailRentDepositReturnAmount;
    }

    public Date getStatementDetailRentDepositReturnTime() {
        return statementDetailRentDepositReturnTime;
    }

    public void setStatementDetailRentDepositReturnTime(Date statementDetailRentDepositReturnTime) {
        this.statementDetailRentDepositReturnTime = statementDetailRentDepositReturnTime;
    }

    public BigDecimal getStatementDetailDepositAmount() {
        return statementDetailDepositAmount;
    }

    public void setStatementDetailDepositAmount(BigDecimal statementDetailDepositAmount) {
        this.statementDetailDepositAmount = statementDetailDepositAmount;
    }

    public BigDecimal getStatementDetailDepositPaidAmount() {
        return statementDetailDepositPaidAmount;
    }

    public void setStatementDetailDepositPaidAmount(BigDecimal statementDetailDepositPaidAmount) {
        this.statementDetailDepositPaidAmount = statementDetailDepositPaidAmount;
    }

    public BigDecimal getStatementDetailDepositReturnAmount() {
        return statementDetailDepositReturnAmount;
    }

    public void setStatementDetailDepositReturnAmount(BigDecimal statementDetailDepositReturnAmount) {
        this.statementDetailDepositReturnAmount = statementDetailDepositReturnAmount;
    }

    public Date getStatementDetailDepositReturnTime() {
        return statementDetailDepositReturnTime;
    }

    public void setStatementDetailDepositReturnTime(Date statementDetailDepositReturnTime) {
        this.statementDetailDepositReturnTime = statementDetailDepositReturnTime;
    }

    public BigDecimal getStatementDetailRentAmount() {
        return statementDetailRentAmount;
    }

    public void setStatementDetailRentAmount(BigDecimal statementDetailRentAmount) {
        this.statementDetailRentAmount = statementDetailRentAmount;
    }

    public BigDecimal getStatementDetailRentPaidAmount() {
        return statementDetailRentPaidAmount;
    }

    public void setStatementDetailRentPaidAmount(BigDecimal statementDetailRentPaidAmount) {
        this.statementDetailRentPaidAmount = statementDetailRentPaidAmount;
    }

    public Date getStatementDetailPaidTime() {
        return statementDetailPaidTime;
    }

    public void setStatementDetailPaidTime(Date statementDetailPaidTime) {
        this.statementDetailPaidTime = statementDetailPaidTime;
    }

    public BigDecimal getStatementDetailOverdueAmount() {
        return statementDetailOverdueAmount;
    }

    public void setStatementDetailOverdueAmount(BigDecimal statementDetailOverdueAmount) {
        this.statementDetailOverdueAmount = statementDetailOverdueAmount;
    }

    public BigDecimal getStatementDetailOverduePaidAmount() {
        return statementDetailOverduePaidAmount;
    }

    public void setStatementDetailOverduePaidAmount(BigDecimal statementDetailOverduePaidAmount) {
        this.statementDetailOverduePaidAmount = statementDetailOverduePaidAmount;
    }

    public Integer getStatementDetailOverdueDays() {
        return statementDetailOverdueDays;
    }

    public void setStatementDetailOverdueDays(Integer statementDetailOverdueDays) {
        this.statementDetailOverdueDays = statementDetailOverdueDays;
    }

    public Integer getStatementDetailOverduePhaseCount() {
        return statementDetailOverduePhaseCount;
    }

    public void setStatementDetailOverduePhaseCount(Integer statementDetailOverduePhaseCount) {
        this.statementDetailOverduePhaseCount = statementDetailOverduePhaseCount;
    }

    public BigDecimal getStatementDetailPenaltyAmount() {
        return statementDetailPenaltyAmount;
    }

    public void setStatementDetailPenaltyAmount(BigDecimal statementDetailPenaltyAmount) {
        this.statementDetailPenaltyAmount = statementDetailPenaltyAmount;
    }

    public BigDecimal getStatementDetailPenaltyPaidAmount() {
        return statementDetailPenaltyPaidAmount;
    }

    public void setStatementDetailPenaltyPaidAmount(BigDecimal statementDetailPenaltyPaidAmount) {
        this.statementDetailPenaltyPaidAmount = statementDetailPenaltyPaidAmount;
    }

    public Integer getStatementDetailStatus() {
        return statementDetailStatus;
    }

    public void setStatementDetailStatus(Integer statementDetailStatus) {
        this.statementDetailStatus = statementDetailStatus;
    }

    public Date getStatementStartTime() {
        return statementStartTime;
    }

    public void setStatementStartTime(Date statementStartTime) {
        this.statementStartTime = statementStartTime;
    }

    public Date getStatementEndTime() {
        return statementEndTime;
    }

    public void setStatementEndTime(Date statementEndTime) {
        this.statementEndTime = statementEndTime;
    }

    public BigDecimal getStatementDetailCorrectAmount() {
        return statementDetailCorrectAmount;
    }

    public void setStatementDetailCorrectAmount(BigDecimal statementDetailCorrectAmount) {
        this.statementDetailCorrectAmount = statementDetailCorrectAmount;
    }

    public BigDecimal getStatementCouponAmount() {
        return statementCouponAmount;
    }

    public void setStatementCouponAmount(BigDecimal statementCouponAmount) {
        this.statementCouponAmount = statementCouponAmount;
    }

    public Integer getReletOrderItemReferId() {
        return reletOrderItemReferId;
    }

    public void setReletOrderItemReferId(Integer reletOrderItemReferId) {
        this.reletOrderItemReferId = reletOrderItemReferId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemSkuName() {
        return itemSkuName;
    }

    public void setItemSkuName(String itemSkuName) {
        this.itemSkuName = itemSkuName;
    }

    public Integer getItemCount() {
        return itemCount;
    }

    public void setItemCount(Integer itemCount) {
        this.itemCount = itemCount;
    }

    public BigDecimal getUnitAmount() {
        return unitAmount;
    }

    public void setUnitAmount(BigDecimal unitAmount) {
        this.unitAmount = unitAmount;
    }

    public Integer getItemRentType() {
        return itemRentType;
    }

    public void setItemRentType(Integer itemRentType) {
        this.itemRentType = itemRentType;
    }

    public Integer getOrderRentType() {
        return orderRentType;
    }

    public void setOrderRentType(Integer orderRentType) {
        this.orderRentType = orderRentType;
    }

    public Date getOrderRentStartTime() {
        return orderRentStartTime;
    }

    public void setOrderRentStartTime(Date orderRentStartTime) {
        this.orderRentStartTime = orderRentStartTime;
    }

    public Date getOrderExpectReturnTime() {
        return orderExpectReturnTime;
    }

    public void setOrderExpectReturnTime(Date orderExpectReturnTime) {
        this.orderExpectReturnTime = orderExpectReturnTime;
    }

    public Integer getOrderRentTimeLength() {
        return orderRentTimeLength;
    }

    public void setOrderRentTimeLength(Integer orderRentTimeLength) {
        this.orderRentTimeLength = orderRentTimeLength;
    }

    public Integer getIsNew() {
        return isNew;
    }

    public void setIsNew(Integer isNew) {
        this.isNew = isNew;
    }

    public Integer getPayMode() {
        return payMode;
    }

    public void setPayMode(Integer payMode) {
        this.payMode = payMode;
    }

    public Integer getDepositCycle() {
        return depositCycle;
    }

    public void setDepositCycle(Integer depositCycle) {
        this.depositCycle = depositCycle;
    }

    public Integer getPaymentCycle() {
        return paymentCycle;
    }

    public void setPaymentCycle(Integer paymentCycle) {
        this.paymentCycle = paymentCycle;
    }

    public Date getStatementExpectPayEndTime() {
        return statementExpectPayEndTime;
    }

    public void setStatementExpectPayEndTime(Date statementExpectPayEndTime) {
        this.statementExpectPayEndTime = statementExpectPayEndTime;
    }

    public BigDecimal getStatementDetailEndAmount() {
        return statementDetailEndAmount;
    }

    public void setStatementDetailEndAmount(BigDecimal statementDetailEndAmount) {
        this.statementDetailEndAmount = statementDetailEndAmount;
    }

    public BigDecimal getStatementDetailRentEndAmount() {
        return statementDetailRentEndAmount;
    }

    public void setStatementDetailRentEndAmount(BigDecimal statementDetailRentEndAmount) {
        this.statementDetailRentEndAmount = statementDetailRentEndAmount;
    }

    public BigDecimal getMonthPayableAmount() {
        return monthPayableAmount;
    }

    public void setMonthPayableAmount(BigDecimal monthPayableAmount) {
        this.monthPayableAmount = monthPayableAmount;
    }

    public BigDecimal getMonthPaidAmount() {
        return monthPaidAmount;
    }

    public void setMonthPaidAmount(BigDecimal monthPaidAmount) {
        this.monthPaidAmount = monthPaidAmount;
    }

    public BigDecimal getMonthUnpaidAmount() {
        return monthUnpaidAmount;
    }

    public void setMonthUnpaidAmount(BigDecimal monthUnpaidAmount) {
        this.monthUnpaidAmount = monthUnpaidAmount;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Date getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(Date returnTime) {
        this.returnTime = returnTime;
    }

    public Integer getOrderItemActualId() {
        return orderItemActualId;
    }

    public void setOrderItemActualId(Integer orderItemActualId) {
        this.orderItemActualId = orderItemActualId;
    }

    public Integer getReturnReasonType() {
        return returnReasonType;
    }

    public void setReturnReasonType(Integer returnReasonType) {
        this.returnReasonType = returnReasonType;
    }
}
