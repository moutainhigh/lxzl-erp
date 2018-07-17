package com.lxzl.erp.core.service.export.impl;

import com.lxzl.erp.common.constant.*;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.k3.pojo.returnOrder.ReturnReasonType;
import com.lxzl.erp.common.domain.statement.CheckStatementOrderDetailBase;
import com.lxzl.erp.common.domain.statement.StatementOrderDetailQueryParam;
import com.lxzl.erp.common.domain.statement.StatementOrderMonthQueryParam;
import com.lxzl.erp.common.domain.statement.pojo.CheckStatementOrder;
import com.lxzl.erp.common.domain.statement.pojo.CheckStatementOrderDetail;
import com.lxzl.erp.common.util.BigDecimalUtil;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.common.util.DateUtil;
import com.lxzl.erp.core.service.dingding.DingDingSupport.DingDingSupport;
import com.lxzl.erp.core.service.export.AmountExcelExportView;
import com.lxzl.erp.core.service.export.ExportExcelCustomFormatService;
import com.lxzl.erp.core.service.export.ExcelExportConfigGroup;
import com.lxzl.erp.core.service.export.ExcelExportService;
import com.lxzl.erp.core.service.export.impl.support.ExcelExportSupport;
import com.lxzl.erp.core.service.permission.PermissionSupport;
import com.lxzl.erp.core.service.statement.StatementService;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerMapper;
import com.lxzl.erp.dataaccess.dao.mysql.k3.K3ReturnOrderDetailMapper;
import com.lxzl.erp.dataaccess.dao.mysql.k3.K3ReturnOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.OrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.OrderMaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.OrderProductMapper;
import com.lxzl.erp.dataaccess.dao.mysql.statement.StatementOrderDetailMapper;
import com.lxzl.erp.dataaccess.dao.mysql.statement.StatementOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.statementOrderCorrect.StatementOrderCorrectMapper;
import com.lxzl.erp.dataaccess.domain.customer.CustomerDO;
import com.lxzl.erp.dataaccess.domain.k3.returnOrder.K3ReturnOrderDO;
import com.lxzl.erp.dataaccess.domain.k3.returnOrder.K3ReturnOrderDetailDO;
import com.lxzl.erp.dataaccess.domain.order.OrderDO;
import com.lxzl.erp.dataaccess.domain.order.OrderMaterialDO;
import com.lxzl.erp.dataaccess.domain.order.OrderProductDO;
import com.lxzl.erp.dataaccess.domain.statement.CheckStatementOrderDetailDO;
import com.lxzl.erp.dataaccess.domain.statementOrderCorrect.StatementOrderCorrectDO;
import com.lxzl.se.common.util.StringUtil;
import org.apache.commons.collections.MapUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/6/19
 * @Time : Created in 14:46
 */
@Service
public class ExportExcelCustomFormatServiceImpl implements ExportExcelCustomFormatService {

    @Autowired
    private StatementService statementService;

    @Autowired
    private ExcelExportService excelExportService;
    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private AmountExcelExportView amountExcelExportView;

    @Autowired
    private DingDingSupport dingDingSupport;

    @Autowired
    private StatementOrderMapper statementOrderMapper;

    @Autowired
    private PermissionSupport permissionSupport;

    @Autowired
    private K3ReturnOrderMapper k3ReturnOrderMapper;

    @Autowired
    private StatementOrderCorrectMapper statementOrderCorrectMapper;

    @Autowired
    private OrderProductMapper orderProductMapper;

    @Autowired
    private OrderMaterialMapper orderMaterialMapper;

    @Autowired
    private K3ReturnOrderDetailMapper k3ReturnOrderDetailMapper;

    @Autowired
    private StatementOrderDetailMapper statementOrderDetailMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @Override
    public ServiceResult<String, String> queryStatementOrderCheckParam(StatementOrderMonthQueryParam statementOrderMonthQueryParam, HttpServletResponse response) throws Exception {

//        String dir = statementOrderMonthQueryParam.getStatementOrderSubCompanyName();
//        File zip = new File("D:\\statement\\bj.zip");

//        String[] customerNameStr = statementOrderMonthQueryParam.getStatementOrderCustomerName().split(",");

        ServiceResult<String, String> result = new ServiceResult<>();

        List<String> fileNames = new ArrayList();// 用于存放生成的文件名称s

//        for (int i = 0; i < customerNameStr.length; i++) {
        String customerNoParam = statementOrderMonthQueryParam.getStatementOrderCustomerNo();
        Date statementOrderStartTime = statementOrderMonthQueryParam.getStatementOrderStartTime();
        Date statementOrderEndTime = statementOrderMonthQueryParam.getStatementOrderEndTime();
        //todo 获取有的预计支付时间
        XSSFWorkbook hssfWorkbook = null;
        BigDecimal beforePeriodUnpaid = new BigDecimal(0);
        BigDecimal allPeriodUnpaid = new BigDecimal(0);

        statementOrderMonthQueryParam = new StatementOrderMonthQueryParam();
        statementOrderMonthQueryParam.setStatementOrderCustomerNo(customerNoParam);
//        statementOrderMonthQueryParam.setStatementOrderStartTime(statementOrderStartTime);
        statementOrderMonthQueryParam.setStatementOrderEndTime(statementOrderEndTime);

        ServiceResult<String, List<CheckStatementOrder>> stringListServiceResult = statementService.exportQueryStatementOrderCheckParam(statementOrderMonthQueryParam);
        if (!ErrorCode.SUCCESS.equals(stringListServiceResult.getErrorCode())) {
            result.setErrorCode(stringListServiceResult.getErrorCode());
            return result;
        }
        List<CheckStatementOrder> checkStatementOrderList = stringListServiceResult.getResult();
        if (CollectionUtil.isEmpty(checkStatementOrderList)) {
            dingDingSupport.dingDingSendMessage(customerNoParam + "无对账单");
            return result;
        }

        Map<String, BigDecimal> totalEverPeriodAmountMap = new HashMap<>();
        Map<String, BigDecimal> totalEverPeriodPaidAmountMap = new HashMap<>();

        //算出总的未付
        for (CheckStatementOrder checkStatementOrder : checkStatementOrderList) {
            totalEverPeriodAmountMap.put(checkStatementOrder.getMonthTime(), BigDecimalUtil.add(totalEverPeriodAmountMap.get(checkStatementOrder.getMonthTime()), checkStatementOrder.getStatementAmount()));
            totalEverPeriodPaidAmountMap.put(checkStatementOrder.getMonthTime(), BigDecimalUtil.add(totalEverPeriodPaidAmountMap.get(checkStatementOrder.getMonthTime()), checkStatementOrder.getStatementPaidAmount()));
            allPeriodUnpaid = BigDecimalUtil.add(allPeriodUnpaid, BigDecimalUtil.sub(checkStatementOrder.getStatementAmount(), checkStatementOrder.getStatementPaidAmount()));
        }

        String previousSheetName = "";
        String customerName = "";
        //先付后用找月份和结算开始时间在同一月的那一期
        Map<String,CheckStatementOrderDetail> bigDecimalMap = new HashMap<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
        for (CheckStatementOrder checkStatementOrder : checkStatementOrderList) {
            List<CheckStatementOrderDetail> exportStatementOrderDetailList = checkStatementOrder.getStatementOrderDetailList();
            if (CollectionUtil.isEmpty(exportStatementOrderDetailList)) {
                continue;
            }
            String monthTime = checkStatementOrder.getMonthTime();
            for (CheckStatementOrderDetail exportStatementOrderDetail : exportStatementOrderDetailList) {
                if (BigDecimalUtil.compare(exportStatementOrderDetail.getUnitAmount(),BigDecimal.ZERO)>0) {
                    Date statementStartTimeDate = exportStatementOrderDetail.getStatementStartTime();
                    if (statementStartTimeDate != null) {
                        String statementStartTime = simpleDateFormat.format(statementStartTimeDate);
                        //订单商品或配件，先付后用（1），月份等于计算开始时间，取出字符串和对象
                        if ((OrderItemType.ORDER_ITEM_TYPE_PRODUCT.equals(exportStatementOrderDetail.getOrderItemType())||
                                OrderItemType.ORDER_ITEM_TYPE_MATERIAL.equals(exportStatementOrderDetail.getOrderItemType()))&&
                                OrderPayMode.PAY_MODE_PAY_BEFORE.equals(exportStatementOrderDetail.getPayMode())&&
                                monthTime.equals(statementStartTime)) {
                            //Key 商品/配件ID+结算单详情类型+结算单商品配件类型+支付类型+支付开始时间+支付结束时间+订单编号
                            String bigDecimalKey = exportStatementOrderDetail.getOrderItemReferId() +"-"+exportStatementOrderDetail.getOrderType()+"-"+
                                    exportStatementOrderDetail.getOrderItemType()+"-"+exportStatementOrderDetail.getPayMode()+"-"+
                                    exportStatementOrderDetail.getStatementStartTime()+"-"+exportStatementOrderDetail.getStatementEndTime()+"-"+exportStatementOrderDetail.getOrderNo();
                            if (!bigDecimalMap.containsKey(bigDecimalKey)) {
                                bigDecimalMap.put(bigDecimalKey,exportStatementOrderDetail);
                            }
                        }
                    }
                }
            }
        }
        //开始存储先付后用的金额
        if (MapUtils.isNotEmpty(bigDecimalMap)) {
            for (CheckStatementOrder checkStatementOrder : checkStatementOrderList) {
                List<CheckStatementOrderDetail> exportStatementOrderDetailList = checkStatementOrder.getStatementOrderDetailList();
                if (CollectionUtil.isEmpty(exportStatementOrderDetailList)) {
                    continue;
                }
                String monthTime = checkStatementOrder.getMonthTime();
                for (CheckStatementOrderDetail exportStatementOrderDetail : exportStatementOrderDetailList) {
                    if (BigDecimalUtil.compare(exportStatementOrderDetail.getUnitAmount(),BigDecimal.ZERO)>0) {
                        Date statementStartTimeDate = exportStatementOrderDetail.getStatementStartTime();
                        if (statementStartTimeDate != null) {
                            String statementStartTime = simpleDateFormat.format(statementStartTimeDate);
                            if (!monthTime.equals(statementStartTime)) {
                                //Key 商品/配件ID+结算单详情类型+结算单商品配件类型+支付类型+支付开始时间+支付结束时间+订单编号
                                String bigDecimalKey = exportStatementOrderDetail.getOrderItemReferId() +"-"+exportStatementOrderDetail.getOrderType()+"-"+
                                        exportStatementOrderDetail.getOrderItemType()+"-"+exportStatementOrderDetail.getPayMode()+"-"+
                                        exportStatementOrderDetail.getStatementStartTime()+"-"+exportStatementOrderDetail.getStatementEndTime()+"-"+exportStatementOrderDetail.getOrderNo();
                                if (bigDecimalMap.containsKey(bigDecimalKey)) {
                                    CheckStatementOrderDetail checkStatementOrderDetail = bigDecimalMap.get(bigDecimalKey);
                                    exportStatementOrderDetail.setStatementDetailEndAmount(checkStatementOrderDetail.getStatementDetailAmount());
                                    exportStatementOrderDetail.setStatementDetailRentEndAmount(checkStatementOrderDetail.getStatementDetailRentAmount());
                                }
                            }
                        }
                    }
                }
            }
        }
        // TODO: 2018\7\16 0016 查出续租退货的结算单
        CustomerDO customerDO = customerMapper.findByNo(statementOrderMonthQueryParam.getStatementOrderCustomerNo());
        if (customerDO == null) {
            result.setErrorCode(ErrorCode.CUSTOMER_NOT_EXISTS);
            return result;
        }
        Map<String, Object> maps = new HashMap<>();
        StatementOrderDetailQueryParam statementOrderDetailQueryParam = new StatementOrderDetailQueryParam();
        statementOrderDetailQueryParam.setCustomerId(customerDO.getId());
        maps.put("start", 0);
        maps.put("pageSize", Integer.MAX_VALUE);
        maps.put("statementOrderDetailQueryParam", statementOrderDetailQueryParam);
        //续租退货结算单
        List<CheckStatementOrderDetailDO> reletReturnListPage = statementOrderDetailMapper.exportReletReturnListPage(maps);
        if (CollectionUtil.isNotEmpty(reletReturnListPage)) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Set<Integer> k3ReturnIdSet = new HashSet<>();
            for(CheckStatementOrderDetailDO checkStatementOrderDetailDO:reletReturnListPage){
                if (checkStatementOrderDetailDO.getOrderId() != null) {
                    k3ReturnIdSet.add(checkStatementOrderDetailDO.getOrderId());
                }
            }
            //查出每个退货单的退货时间
            Map<Integer,Date> k3ReturnMap = new HashMap<>();
            if (CollectionUtil.isNotEmpty(k3ReturnIdSet)) {
                List<K3ReturnOrderDO> k3ReturnOrderDOList = k3ReturnOrderMapper.findByIdSet(k3ReturnIdSet);
                if (CollectionUtil.isNotEmpty(k3ReturnOrderDOList)) {
                    for (K3ReturnOrderDO k3ReturnOrderDO:k3ReturnOrderDOList) {
                        k3ReturnMap.put(k3ReturnOrderDO.getId(),k3ReturnOrderDO.getReturnTime());
                    }
                }
            }
            //存储退货月份和该月份的退货结算单
            Map<String,Map<String,Set<CheckStatementOrderDetailDO>>> orderReletReturnMap = new HashMap<>();
            Map<String,Map<String,CheckStatementOrderDetailDO>> returnReletReturnMap = new HashMap<>();
            if (MapUtils.isNotEmpty(k3ReturnMap)) {
                for(CheckStatementOrderDetailDO checkStatementOrderDetailDO:reletReturnListPage){
                    if (checkStatementOrderDetailDO.getOrderId() != null) {
                        if (k3ReturnMap.containsKey(checkStatementOrderDetailDO.getOrderId())) {
                            Date returnTime = k3ReturnMap.get(checkStatementOrderDetailDO.getOrderId());
                            Date statementExpectPayTime = checkStatementOrderDetailDO.getStatementExpectPayTime();
                            String statementExpectPayTimeString = dateFormat.format(statementExpectPayTime);
                            String returnTimeString = dateFormat.format(returnTime);
                            if (returnTimeString.equals(statementExpectPayTimeString)) {
                                String monthTimeString = simpleDateFormat.format(returnTime);
                                if (!orderReletReturnMap.containsKey(monthTimeString) && !returnReletReturnMap.containsKey(monthTimeString)) {
                                    Map<String,Set<CheckStatementOrderDetailDO>> orderMap = new HashMap<>();
                                    Map<String,CheckStatementOrderDetailDO> returnMap = new HashMap<>();
                                    Set<CheckStatementOrderDetailDO> reletStatementReturnSet = new HashSet<>();
                                    reletStatementReturnSet.add(checkStatementOrderDetailDO);
                                    String orderKey = null;
                                    String returnKey =null;
                                    if (OrderItemType.ORDER_ITEM_TYPE_RETURN_PRODUCT.equals(checkStatementOrderDetailDO.getOrderItemType())) {
                                        //订单的key：退货关联ID-续租关联ID-商品类型
                                        orderKey = checkStatementOrderDetailDO.getReturnReferId() +"-"+ checkStatementOrderDetailDO.getReletOrderItemReferId() +"-"+
                                                OrderItemType.ORDER_ITEM_TYPE_PRODUCT;
                                    } else if (OrderItemType.ORDER_ITEM_TYPE_RETURN_MATERIAL.equals(checkStatementOrderDetailDO.getOrderItemType())) {
                                        //订单的key：退货关联ID-续租关联ID-商品类型
                                        orderKey = checkStatementOrderDetailDO.getReturnReferId() +"-"+ checkStatementOrderDetailDO.getReletOrderItemReferId() +"-"+
                                                OrderItemType.ORDER_ITEM_TYPE_MATERIAL;
                                    }

                                    if (OrderItemType.ORDER_ITEM_TYPE_RETURN_PRODUCT.equals(checkStatementOrderDetailDO.getOrderItemType()) ||
                                            OrderItemType.ORDER_ITEM_TYPE_RETURN_MATERIAL.equals(checkStatementOrderDetailDO.getOrderItemType())) {
                                        //退货单的key：退货关联ID-续租关联ID-商品类型-退货单ID
                                        returnKey = checkStatementOrderDetailDO.getReturnReferId() +"-"+ checkStatementOrderDetailDO.getReletOrderItemReferId() +"-"+
                                                checkStatementOrderDetailDO.getOrderItemType() +"-"+ checkStatementOrderDetailDO.getOrderId();
                                    }
                                    if (orderKey != null) {
                                        orderMap.put(orderKey,reletStatementReturnSet);
                                        orderReletReturnMap.put(monthTimeString,orderMap);
                                    }
                                    if (returnKey != null) {
                                        returnMap.put(returnKey,checkStatementOrderDetailDO);
                                        returnReletReturnMap.put(monthTimeString,returnMap);
                                    }
                                }else {
                                    Map<String,Set<CheckStatementOrderDetailDO>> orderMap = orderReletReturnMap.get(monthTimeString);
                                    Map<String,CheckStatementOrderDetailDO> returnMap = returnReletReturnMap.get(monthTimeString);
                                    String orderKey = null;
                                    String returnKey =null;
                                    if (OrderItemType.ORDER_ITEM_TYPE_RETURN_PRODUCT.equals(checkStatementOrderDetailDO.getOrderItemType())) {
                                        //订单的key：退货关联ID-续租关联ID-商品类型
                                        orderKey = checkStatementOrderDetailDO.getReturnReferId() +"-"+ checkStatementOrderDetailDO.getReletOrderItemReferId() +"-"+
                                                OrderItemType.ORDER_ITEM_TYPE_PRODUCT;
                                    } else if (OrderItemType.ORDER_ITEM_TYPE_RETURN_MATERIAL.equals(checkStatementOrderDetailDO.getOrderItemType())) {
                                        //订单的key：退货关联ID-续租关联ID-商品类型
                                        orderKey = checkStatementOrderDetailDO.getReturnReferId() +"-"+ checkStatementOrderDetailDO.getReletOrderItemReferId() +"-"+
                                                OrderItemType.ORDER_ITEM_TYPE_MATERIAL;
                                    }
                                    if (OrderItemType.ORDER_ITEM_TYPE_RETURN_PRODUCT.equals(checkStatementOrderDetailDO.getOrderItemType()) ||
                                            OrderItemType.ORDER_ITEM_TYPE_RETURN_MATERIAL.equals(checkStatementOrderDetailDO.getOrderItemType())) {
                                        //退货单的key：退货关联ID-续租关联ID-商品类型-退货单ID
                                        returnKey = checkStatementOrderDetailDO.getReturnReferId() +"-"+ checkStatementOrderDetailDO.getReletOrderItemReferId() +"-"+
                                                checkStatementOrderDetailDO.getOrderItemType() +"-"+ checkStatementOrderDetailDO.getOrderId();
                                    }
                                    if (orderKey != null && !orderMap.containsKey(orderKey)) {
                                        Set<CheckStatementOrderDetailDO> reletStatementReturnSet = new HashSet<>();
                                        reletStatementReturnSet.add(checkStatementOrderDetailDO);
                                        orderMap.put(orderKey,reletStatementReturnSet);
                                    }else {
                                        Set<CheckStatementOrderDetailDO> reletStatementReturnSet = orderMap.get(orderKey);
                                        reletStatementReturnSet.add(checkStatementOrderDetailDO);
                                    }
                                    if (returnKey != null && !returnMap.containsKey(returnKey)) {
                                        returnMap.put(returnKey,checkStatementOrderDetailDO);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            // TODO: 2018\7\16 0016 先查出所有该客户的续租退货单，然后存入map集合
            if (MapUtils.isNotEmpty(orderReletReturnMap) && MapUtils.isNotEmpty(returnReletReturnMap)) {
                for (CheckStatementOrder checkStatementOrder : checkStatementOrderList) {
                    if (orderReletReturnMap.containsKey(checkStatementOrder.getMonthTime()) && returnReletReturnMap.containsKey(checkStatementOrder.getMonthTime())) {
                        Map<String,Set<CheckStatementOrderDetailDO>> orderMap = orderReletReturnMap.get(checkStatementOrder.getMonthTime());
                        Map<String,CheckStatementOrderDetailDO> returnMap = returnReletReturnMap.get(checkStatementOrder.getMonthTime());
                        for (CheckStatementOrderDetail exportStatementOrderDetail : checkStatementOrder.getStatementOrderDetailList()) {
                            if (MapUtils.isNotEmpty(orderMap) &&
                                    (OrderItemType.ORDER_ITEM_TYPE_PRODUCT.equals(exportStatementOrderDetail.getOrderItemType())||
                                    OrderItemType.ORDER_ITEM_TYPE_MATERIAL.equals(exportStatementOrderDetail.getOrderItemType()))&&
                                    OrderPayMode.PAY_MODE_PAY_BEFORE.equals(exportStatementOrderDetail.getPayMode()) &&
                                            exportStatementOrderDetail.getReletOrderItemReferId() != null) {
                                //订单的key：退货关联ID-续租关联ID-商品类型
                                String orderKey = exportStatementOrderDetail.getStatementOrderDetailId() +"-"+ exportStatementOrderDetail.getReletOrderItemReferId() +"-"+
                                        exportStatementOrderDetail.getOrderItemType();
                                if (orderMap.containsKey(orderKey)) {
                                    Set<CheckStatementOrderDetailDO> reletStatementReturnSet = orderMap.get(orderKey);
                                    if (CollectionUtil.isNotEmpty(reletStatementReturnSet)) {
                                        //这里添加退货单合并逻辑
                                        BigDecimal statementDetailAmount = BigDecimal.ZERO;
                                        BigDecimal statementDetailRentAmount = BigDecimal.ZERO;
                                        BigDecimal statementDetailDepositAmount = BigDecimal.ZERO;
                                        BigDecimal statementDetailOtherAmount = BigDecimal.ZERO;
                                        BigDecimal statementDetailPaidAmount = BigDecimal.ZERO;
                                        BigDecimal statementDetailPenaltyAmount = BigDecimal.ZERO;
                                        BigDecimal statementDetailPenaltyPaidAmount = BigDecimal.ZERO;
                                        BigDecimal statementDetailRentDepositAmount = BigDecimal.ZERO;
                                        for (CheckStatementOrderDetailDO checkStatementOrderDetail : reletStatementReturnSet) {
                                            //如果金额不为0，将数量进行累加，然后跟最外层的订单对账单进行相减，金额相加，之后退货单不存储
                                            statementDetailAmount = BigDecimalUtil.add(statementDetailAmount, checkStatementOrderDetail.getStatementDetailAmount());
                                            statementDetailRentAmount = BigDecimalUtil.add(statementDetailRentAmount, checkStatementOrderDetail.getStatementDetailRentAmount());
                                            statementDetailDepositAmount = BigDecimalUtil.add(statementDetailDepositAmount, checkStatementOrderDetail.getStatementDetailDepositAmount());
                                            statementDetailOtherAmount = BigDecimalUtil.add(statementDetailOtherAmount, checkStatementOrderDetail.getStatementDetailOtherAmount());
                                            statementDetailPaidAmount = BigDecimalUtil.add(statementDetailPaidAmount, BigDecimal.ZERO);
                                            statementDetailPenaltyAmount = BigDecimalUtil.add(statementDetailPenaltyAmount, checkStatementOrderDetail.getStatementDetailPenaltyAmount());
                                            statementDetailPenaltyPaidAmount = BigDecimalUtil.add(statementDetailPenaltyPaidAmount, checkStatementOrderDetail.getStatementDetailPenaltyPaidAmount());
                                            statementDetailRentDepositAmount = BigDecimalUtil.add(statementDetailRentDepositAmount, checkStatementOrderDetail.getStatementDetailRentDepositAmount());
                                        }
                                        //将负值变成正值
                                        statementDetailAmount = statementDetailAmount.multiply(new BigDecimal(-1));
                                        statementDetailRentAmount = statementDetailRentAmount.multiply(new BigDecimal(-1));
                                        statementDetailDepositAmount = statementDetailDepositAmount.multiply(new BigDecimal(-1));
                                        statementDetailOtherAmount = statementDetailOtherAmount.multiply(new BigDecimal(-1));
                                        statementDetailPaidAmount = statementDetailPaidAmount.multiply(new BigDecimal(-1));
                                        statementDetailPenaltyAmount = statementDetailPenaltyAmount.multiply(new BigDecimal(-1));
                                        statementDetailPenaltyPaidAmount = statementDetailPenaltyPaidAmount.multiply(new BigDecimal(-1));
                                        statementDetailRentDepositAmount = statementDetailRentDepositAmount.multiply(new BigDecimal(-1));
                                        //将扣减的金额加回去
                                        exportStatementOrderDetail.setStatementDetailAmount(BigDecimalUtil.add(statementDetailAmount, exportStatementOrderDetail.getStatementDetailAmount()));
                                        exportStatementOrderDetail.setStatementDetailAmount(BigDecimalUtil.compare(exportStatementOrderDetail.getStatementDetailAmount(), BigDecimal.ZERO) == -1 ? BigDecimal.ZERO : exportStatementOrderDetail.getStatementDetailAmount());
                                        exportStatementOrderDetail.setStatementDetailRentAmount(BigDecimalUtil.add(statementDetailRentAmount, exportStatementOrderDetail.getStatementDetailRentAmount()));
                                        exportStatementOrderDetail.setStatementDetailRentAmount(BigDecimalUtil.compare(exportStatementOrderDetail.getStatementDetailRentAmount(), BigDecimal.ZERO) == -1 ? BigDecimal.ZERO : exportStatementOrderDetail.getStatementDetailRentAmount());
                                        exportStatementOrderDetail.setStatementDetailDepositAmount(BigDecimalUtil.add(statementDetailDepositAmount, exportStatementOrderDetail.getStatementDetailDepositAmount()));
                                        exportStatementOrderDetail.setStatementDetailDepositAmount(BigDecimalUtil.compare(exportStatementOrderDetail.getStatementDetailDepositAmount(), BigDecimal.ZERO) == -1 ? BigDecimal.ZERO : exportStatementOrderDetail.getStatementDetailDepositAmount());
                                        exportStatementOrderDetail.setStatementDetailOtherAmount(BigDecimalUtil.add(statementDetailOtherAmount, exportStatementOrderDetail.getStatementDetailOtherAmount()));
                                        exportStatementOrderDetail.setStatementDetailOtherAmount(BigDecimalUtil.compare(exportStatementOrderDetail.getStatementDetailOtherAmount(), BigDecimal.ZERO) == -1 ? BigDecimal.ZERO : exportStatementOrderDetail.getStatementDetailOtherAmount());
                                        exportStatementOrderDetail.setStatementDetailPaidAmount(BigDecimalUtil.add(statementDetailPaidAmount, exportStatementOrderDetail.getStatementDetailPaidAmount()));
                                        exportStatementOrderDetail.setStatementDetailPaidAmount(BigDecimalUtil.compare(exportStatementOrderDetail.getStatementDetailPaidAmount(), BigDecimal.ZERO) == -1 ? BigDecimal.ZERO : exportStatementOrderDetail.getStatementDetailPaidAmount());
                                        exportStatementOrderDetail.setStatementDetailPenaltyAmount(BigDecimalUtil.add(statementDetailPenaltyAmount, exportStatementOrderDetail.getStatementDetailPenaltyAmount()));
                                        exportStatementOrderDetail.setStatementDetailPenaltyAmount(BigDecimalUtil.compare(exportStatementOrderDetail.getStatementDetailPenaltyAmount(), BigDecimal.ZERO) == -1 ? BigDecimal.ZERO : exportStatementOrderDetail.getStatementDetailPenaltyAmount());
                                        exportStatementOrderDetail.setStatementDetailPenaltyPaidAmount(BigDecimalUtil.add(statementDetailPenaltyPaidAmount, exportStatementOrderDetail.getStatementDetailPenaltyPaidAmount()));
                                        exportStatementOrderDetail.setStatementDetailPenaltyPaidAmount(BigDecimalUtil.compare(exportStatementOrderDetail.getStatementDetailPenaltyPaidAmount(), BigDecimal.ZERO) == -1 ? BigDecimal.ZERO : exportStatementOrderDetail.getStatementDetailPenaltyPaidAmount());
                                        exportStatementOrderDetail.setStatementDetailRentDepositAmount(BigDecimalUtil.add(statementDetailRentDepositAmount, exportStatementOrderDetail.getStatementDetailRentDepositAmount()));
                                        exportStatementOrderDetail.setStatementDetailRentDepositAmount(BigDecimalUtil.compare(exportStatementOrderDetail.getStatementDetailRentDepositAmount(), BigDecimal.ZERO) == -1 ? BigDecimal.ZERO : exportStatementOrderDetail.getStatementDetailRentDepositAmount());
                                    }
                                }
                            } else if (MapUtils.isNotEmpty(returnMap) &&
                                    (OrderItemType.ORDER_ITEM_TYPE_RETURN_PRODUCT.equals(exportStatementOrderDetail.getOrderItemType())||
                                            OrderItemType.ORDER_ITEM_TYPE_RETURN_MATERIAL.equals(exportStatementOrderDetail.getOrderItemType()))&&
                                    exportStatementOrderDetail.getReletOrderItemReferId() != null) {
                                //退货单的key：退货关联ID-续租关联ID-商品类型-退货单ID
                                String returnKey = exportStatementOrderDetail.getReturnReferId() +"-"+ exportStatementOrderDetail.getReletOrderItemReferId() +"-"+
                                        exportStatementOrderDetail.getOrderItemType() +"-"+ exportStatementOrderDetail.getOrderId();
                                if (returnMap.containsKey(returnKey)) {
                                    CheckStatementOrderDetailDO CheckStatementOrderDetailDO = returnMap.get(returnKey);
                                    exportStatementOrderDetail.setStatementDetailAmount(CheckStatementOrderDetailDO.getStatementDetailAmount());
                                    exportStatementOrderDetail.setStatementDetailRentAmount(CheckStatementOrderDetailDO.getStatementDetailRentAmount());
                                    exportStatementOrderDetail.setStatementDetailDepositAmount(CheckStatementOrderDetailDO.getStatementDetailDepositAmount());
                                    exportStatementOrderDetail.setStatementDetailOtherAmount(CheckStatementOrderDetailDO.getStatementDetailOtherAmount());
                                    exportStatementOrderDetail.setStatementDetailPaidAmount(BigDecimal.ZERO);
                                    exportStatementOrderDetail.setStatementDetailPenaltyAmount(CheckStatementOrderDetailDO.getStatementDetailPenaltyAmount());
                                    exportStatementOrderDetail.setStatementDetailPenaltyPaidAmount(CheckStatementOrderDetailDO.getStatementDetailPenaltyPaidAmount());
                                    exportStatementOrderDetail.setStatementDetailRentDepositAmount(CheckStatementOrderDetailDO.getStatementDetailRentDepositAmount());
                                }
                            }
                        }
                    }
                }
            }
        }


        for (CheckStatementOrder checkStatementOrder : checkStatementOrderList) {

            Date monthTime = simpleDateFormat.parse(checkStatementOrder.getMonthTime());
            if (monthTime.getTime()<statementOrderStartTime.getTime()) {
                continue;
            }
            if (StringUtil.isBlank(customerName)) {
                customerName = checkStatementOrder.getCustomerName();
            }
            List<CheckStatementOrderDetail> exportStatementOrderDetailList = checkStatementOrder.getStatementOrderDetailList();
            if (CollectionUtil.isEmpty(exportStatementOrderDetailList)) {
                continue;
            }
            //要保存到额集合
            List<CheckStatementOrderDetailBase> exportList = new ArrayList<>();
            for (CheckStatementOrderDetail exportStatementOrderDetail : exportStatementOrderDetailList) {
                //todo 分装对象来处理
                CheckStatementOrderDetailBase exportStatementOrderDetailBase = new CheckStatementOrderDetailBase();
                exportStatementOrderDetailBase.setOrderNo(exportStatementOrderDetail.getOrderNo()); //订单编号
                exportStatementOrderDetailBase.setOrderType(exportStatementOrderDetail.getOrderItemType());    // 单子类型，详见ORDER_TYPE
                exportStatementOrderDetailBase.setItemName(exportStatementOrderDetail.getItemName());  //商品名
                exportStatementOrderDetailBase.setItemSkuName(exportStatementOrderDetail.getItemSkuName());  //配置
                exportStatementOrderDetailBase.setItemCount(exportStatementOrderDetail.getItemCount());  //数量
                exportStatementOrderDetailBase.setUnitAmount(BigDecimalUtil.round(exportStatementOrderDetail.getUnitAmount(), BigDecimalUtil.STANDARD_SCALE));  //"单价（元/台/月）"
                exportStatementOrderDetailBase.setStatementRentAmount(exportStatementOrderDetail.getStatementDetailRentAmount());             // 租金小计
                exportStatementOrderDetailBase.setStatementRentDepositAmount(exportStatementOrderDetail.getStatementDetailRentDepositAmount());      // 租金押金
                exportStatementOrderDetailBase.setStatementDepositAmount(BigDecimalUtil.add(exportStatementOrderDetail.getStatementDetailDepositAmount(), exportStatementOrderDetail.getStatementDetailRentDepositAmount()));      // 设备押金
                exportStatementOrderDetailBase.setStatementOverdueAmount(exportStatementOrderDetail.getStatementDetailOverdueAmount());      // 逾期金额
                exportStatementOrderDetailBase.setStatementOtherAmount(exportStatementOrderDetail.getStatementDetailOtherAmount());      // 其它费用
                exportStatementOrderDetailBase.setStatementCorrectAmount(exportStatementOrderDetail.getStatementDetailCorrectAmount());      // 冲正金额
                exportStatementOrderDetailBase.setStatementAmount(exportStatementOrderDetail.getStatementDetailAmount());      // 应付金额
                exportStatementOrderDetailBase.setStatementExpectPayEndTime(exportStatementOrderDetail.getStatementExpectPayEndTime());      // 应付日期

                exportStatementOrderDetailBase.setStatementDetailEndAmount(exportStatementOrderDetail.getStatementDetailEndAmount());      // 本期应付金额
                exportStatementOrderDetailBase.setStatementDetailRentEndAmount(exportStatementOrderDetail.getStatementDetailRentEndAmount());      // 本期应付金额

                if (BigDecimalUtil.compare(exportStatementOrderDetailBase.getStatementDetailEndAmount(), BigDecimal.ZERO) == 0) {
                    exportStatementOrderDetailBase.setStatementDetailEndAmount(exportStatementOrderDetail.getStatementDetailAmount());      // 本期应付金额
                }
                if (BigDecimalUtil.compare(exportStatementOrderDetailBase.getStatementDetailRentEndAmount(), BigDecimal.ZERO) == 0) {
                    exportStatementOrderDetailBase.setStatementDetailRentEndAmount(exportStatementOrderDetail.getStatementDetailRentAmount());      // 本期应付租金金额
                }
                if (BigDecimalUtil.compare(exportStatementOrderDetailBase.getStatementAmount(), BigDecimal.ZERO) != 0) {
                    exportStatementOrderDetailBase.setStatementExpectPayTime(exportStatementOrderDetail.getStatementExpectPayTime());      // 应付日期
                }
                if (exportStatementOrderDetailBase.getStatementExpectPayEndTime() == null) {
                    exportStatementOrderDetailBase.setStatementExpectPayEndTime(exportStatementOrderDetail.getStatementExpectPayTime());      // 应付日期
                }

                exportStatementOrderDetailBase.setStatementDepositPaidAmount(exportStatementOrderDetail.getStatementDetailDepositPaidAmount());      // 支付押金
                exportStatementOrderDetailBase.setStatementDetailStatus(exportStatementOrderDetail.getStatementDetailStatus());      // 状态
                exportStatementOrderDetailBase.setIsNew(exportStatementOrderDetail.getIsNew());  //是否全新
                //获取订单类型  月或者天租
                exportStatementOrderDetailBase.setBusinessType(exportStatementOrderDetail.getOrderType());    //业务类型
                exportStatementOrderDetailBase.setPayMode(exportStatementOrderDetail.getPayMode());    //支付方式
                if (exportStatementOrderDetail.getDepositCycle() == null) {
                    exportStatementOrderDetail.setDepositCycle(0);
                }
                if (exportStatementOrderDetail.getPaymentCycle() == null) {
                    exportStatementOrderDetail.setPaymentCycle(0);
                }
                exportStatementOrderDetailBase.setDepositCycle(exportStatementOrderDetail.getDepositCycle());    //押金期数
                exportStatementOrderDetailBase.setPaymentCycle(exportStatementOrderDetail.getPaymentCycle());    //付款期数
                exportStatementOrderDetailBase.setRentProgramme("押" + exportStatementOrderDetail.getDepositCycle() + "付" + exportStatementOrderDetail.getPaymentCycle());

                //冲正单单号和原因保存
                List<StatementOrderCorrectDO> statementOrderCorrectDOList = statementOrderCorrectMapper.findStatementOrderIdAndItemId(exportStatementOrderDetail.getStatementOrderId(), exportStatementOrderDetail.getOrderId(), exportStatementOrderDetail.getOrderItemReferId());
                if (CollectionUtil.isNotEmpty(statementOrderCorrectDOList)) {
                    StringBuffer statementCorrectNo = new StringBuffer();
                    StringBuffer statementCorrectReason = new StringBuffer();
                    for (StatementOrderCorrectDO statementOrderCorrectDO : statementOrderCorrectDOList) {
                        statementCorrectNo.append(statementOrderCorrectDO.getStatementCorrectNo() + "/n");
                        statementCorrectReason.append(statementOrderCorrectDO.getStatementCorrectReason() + "/n");
                    }
                    exportStatementOrderDetailBase.setStatementCorrectNo(statementCorrectNo.toString());
                    exportStatementOrderDetailBase.setStatementCorrectReason(String.valueOf(statementCorrectReason));
                }

                //退货单和冲正单取No和原因
                if (OrderType.ORDER_TYPE_RETURN.equals(exportStatementOrderDetail.getOrderType())) {

                    K3ReturnOrderDO k3ReturnOrderDO = k3ReturnOrderMapper.findById(exportStatementOrderDetail.getOrderId());
                    StringBuffer k3ReturnOrderDONo = new StringBuffer();
                    StringBuffer returnReasonType = new StringBuffer();
                    if (k3ReturnOrderDO != null) {
                        k3ReturnOrderDONo.append(k3ReturnOrderDO.getReturnOrderNo());
                        returnReasonType.append(formatReturnReasonType(k3ReturnOrderDO.getReturnReasonType()));
                        //订单
                        K3ReturnOrderDetailDO k3ReturnOrderDetailDO = k3ReturnOrderDetailMapper.findById(exportStatementOrderDetail.getOrderItemReferId());
                        if (k3ReturnOrderDetailDO != null) {
                            OrderDO orderDO = orderMapper.findByOrderNo(k3ReturnOrderDetailDO.getOrderNo());
                            exportStatementOrderDetailBase.setOrderNo(orderDO.getOrderNo());
                            exportStatementOrderDetail.setOrderRentStartTime(orderDO.getRentStartTime());//租赁开始日期
                            exportStatementOrderDetail.setOrderExpectReturnTime(orderDO.getExpectReturnTime());//租赁结束日期
                            exportStatementOrderDetail.setOrderRentType(orderDO.getRentType());
                            exportStatementOrderDetail.setOrderRentTimeLength(orderDO.getRentTimeLength());
                            if (OrderItemType.ORDER_ITEM_TYPE_RETURN_PRODUCT.equals(exportStatementOrderDetail.getOrderItemType())) {
                                OrderProductDO orderProductDO = null;
                                if (CommonConstant.COMMON_CONSTANT_YES.equals(orderDO.getIsK3Order()) && k3ReturnOrderDetailDO.getOrderEntry() != null) {
                                    orderProductDO = orderProductMapper.findK3OrderProduct(orderDO.getId(), Integer.parseInt(k3ReturnOrderDetailDO.getOrderEntry()));
                                } else if (CommonConstant.COMMON_CONSTANT_NO.equals(orderDO.getIsK3Order())) {
                                    String orderItemId = k3ReturnOrderDetailDO.getOrderItemId();
                                    orderProductDO = orderProductMapper.findById(Integer.parseInt(orderItemId));
                                }
                                if (orderProductDO != null) {
                                    Integer isNewProduct = orderProductDO.getIsNewProduct();
                                    exportStatementOrderDetailBase.setIsNew(isNewProduct);
                                }
                            } else if (OrderItemType.ORDER_ITEM_TYPE_RETURN_MATERIAL.equals(exportStatementOrderDetail.getOrderItemType())) {
                                OrderMaterialDO orderMaterialDO = null;
                                if (CommonConstant.COMMON_CONSTANT_YES.equals(orderDO.getIsK3Order()) && k3ReturnOrderDetailDO.getOrderEntry() != null) {
                                    orderMaterialDO = orderMaterialMapper.findK3OrderMaterial(orderDO.getId(), Integer.parseInt(k3ReturnOrderDetailDO.getOrderEntry()));
                                } else if (CommonConstant.COMMON_CONSTANT_NO.equals(orderDO.getIsK3Order())) {
                                    String orderItemId = k3ReturnOrderDetailDO.getOrderItemId();
                                    orderMaterialDO = orderMaterialMapper.findById(Integer.parseInt(orderItemId));
                                }

                                if (orderMaterialDO != null) {
                                    Integer isNewMaterial = orderMaterialDO.getIsNewMaterial();
                                    exportStatementOrderDetailBase.setIsNew(isNewMaterial);
                                }
                            }
                        }


                        //冲正单单号和原因保存
                        List<StatementOrderCorrectDO> returnStatementOrderCorrectDOList = statementOrderCorrectMapper.findStatementOrderIdAndReferId(exportStatementOrderDetail.getStatementOrderId(), exportStatementOrderDetail.getOrderId());
                        if (CollectionUtil.isNotEmpty(returnStatementOrderCorrectDOList)) {
                            StringBuffer statementCorrectNo = new StringBuffer();
                            StringBuffer statementCorrectReason = new StringBuffer();
                            for (StatementOrderCorrectDO statementOrderCorrectDO : returnStatementOrderCorrectDOList) {
                                statementCorrectNo.append(statementOrderCorrectDO.getStatementCorrectNo() + "/n");
                                statementCorrectReason.append(statementOrderCorrectDO.getStatementCorrectReason() + "/n");
                            }
                            exportStatementOrderDetailBase.setStatementCorrectNo(String.valueOf(statementCorrectNo));
                            exportStatementOrderDetailBase.setStatementCorrectReason(String.valueOf(statementCorrectReason));
                        }

                    }
                    exportStatementOrderDetailBase.setK3ReturnOrderDONo(String.valueOf(k3ReturnOrderDONo));
                    exportStatementOrderDetailBase.setReturnReasonType(String.valueOf(returnReasonType));
                }

                //-------------------以下是全部结算单-----------------------------
                Date orderRentStartTime = exportStatementOrderDetail.getOrderRentStartTime();    //租赁开始日期
                Date orderExpectReturnTime = exportStatementOrderDetail.getOrderExpectReturnTime();    //租赁结束日期
                exportStatementOrderDetailBase.setRentStartTime(orderRentStartTime);    //租赁开始日期
                exportStatementOrderDetailBase.setExpectReturnTime(orderExpectReturnTime);    //租赁结束日期
                if (OrderRentType.RENT_TYPE_DAY.equals(exportStatementOrderDetail.getOrderRentType())) {
                    exportStatementOrderDetailBase.setDay((DateUtil.daysBetween(orderRentStartTime,orderExpectReturnTime) + 1));  //日
                    exportStatementOrderDetailBase.setMonth(0);  //月
                } else if (OrderRentType.RENT_TYPE_MONTH.equals(exportStatementOrderDetail.getOrderRentType())) {
                    int[] diff = DateUtil.getDiff(orderRentStartTime,orderExpectReturnTime);
                    exportStatementOrderDetailBase.setDay(diff[1]);   //日
                    exportStatementOrderDetailBase.setMonth(diff[0]);  //月
                }
                exportStatementOrderDetailBase.setMonth(exportStatementOrderDetailBase.getMonth() == null ? 0 : exportStatementOrderDetailBase.getMonth());
                exportStatementOrderDetailBase.setDay(exportStatementOrderDetailBase.getDay() == null ? 0 : exportStatementOrderDetailBase.getDay());
                String allRentTimeLength = exportStatementOrderDetailBase.getMonth() + "月" + exportStatementOrderDetailBase.getDay() + "天";
                exportStatementOrderDetailBase.setAllRentTimeLength(allRentTimeLength);    //租赁总期限
                exportStatementOrderDetailBase.setAllPeriodStartAndEnd(formatPeriodStartAndEnd(exportStatementOrderDetail.getOrderRentStartTime(), exportStatementOrderDetail.getOrderExpectReturnTime()) + allRentTimeLength);    //本期起止（总的期数起止）
                //-------------------以上是全部结算单-----------------------------

                //-------------------以下是本期结算单-----------------------------
                if (exportStatementOrderDetail.getStatementStartTime() == null) {
                    exportStatementOrderDetail.setStatementStartTime(exportStatementOrderDetail.getOrderRentStartTime());
                }
                if (exportStatementOrderDetail.getStatementEndTime() == null) {
                    exportStatementOrderDetail.setStatementEndTime(exportStatementOrderDetail.getOrderExpectReturnTime());
                }
                Date statementStartTime = exportStatementOrderDetail.getStatementStartTime();     //结算开始日期
                Date statementEndTime = exportStatementOrderDetail.getStatementEndTime();  //结算结束日期
                exportStatementOrderDetailBase.setStatementStartTime(statementStartTime);     //结算开始日期
                exportStatementOrderDetailBase.setStatementEndTime(statementEndTime);  //结算结束日期
                int[] diff = DateUtil.getDiff(statementStartTime,statementEndTime);
                int month = diff[0];
                int day = diff[1];
                String monthAndDays = month + "月" + day + "天";

                if (OrderRentType.RENT_TYPE_DAY.equals(exportStatementOrderDetail.getOrderRentType())) {
                    exportStatementOrderDetailBase.setRentTimeLength(exportStatementOrderDetail.getOrderRentTimeLength() + "天");  //期限
                    exportStatementOrderDetailBase.setStatementMonth(0); //月
                    exportStatementOrderDetailBase.setStatementDay((DateUtil.daysBetween(statementStartTime,statementEndTime) + 1)); //日
                    exportStatementOrderDetailBase.setUnitAmountInfo(exportStatementOrderDetailBase.getUnitAmount() + "/日");
                    exportStatementOrderDetail.setStatementStartTime(exportStatementOrderDetail.getOrderRentStartTime());
                    exportStatementOrderDetail.setStatementEndTime(exportStatementOrderDetail.getOrderExpectReturnTime());
                } else if (OrderRentType.RENT_TYPE_MONTH.equals(exportStatementOrderDetail.getOrderRentType())) {
                    exportStatementOrderDetailBase.setStatementMonth(month); //月
                    exportStatementOrderDetailBase.setStatementDay(day); //日
                    exportStatementOrderDetailBase.setUnitAmountInfo(exportStatementOrderDetailBase.getUnitAmount() + "/月");
                    exportStatementOrderDetailBase.setRentTimeLength(monthAndDays);  //期限
                }
                exportStatementOrderDetailBase.setRentTimeLength(exportStatementOrderDetailBase.getRentTimeLength() == null ? "无" : exportStatementOrderDetailBase.getRentTimeLength());
                exportStatementOrderDetailBase.setCurrentPeriodStartAndEnd(formatPeriodStartAndEnd(exportStatementOrderDetail.getStatementStartTime(), exportStatementOrderDetail.getStatementEndTime()) + exportStatementOrderDetailBase.getRentTimeLength());    //本期起止（当前期数起止）
                //-------------------以上是本期结算单-----------------------------


                exportList.add(exportStatementOrderDetailBase);
            }
            //对订单中有单价且有结算时间的商品进行存储
            Map<String,String> orderProductMap = new HashMap<>();
            if (CollectionUtil.isNotEmpty(exportList)) {
                for (CheckStatementOrderDetailBase checkStatementOrderDetailBase: exportList) {
                    if (OrderItemType.ORDER_ITEM_TYPE_PRODUCT.equals(checkStatementOrderDetailBase.getOrderType())
                            && BigDecimalUtil.compare(checkStatementOrderDetailBase.getUnitAmount(),BigDecimal.ZERO)>0) {
                        if (!orderProductMap.containsKey(checkStatementOrderDetailBase.getOrderNo())
                                && StringUtil.isNotEmpty(checkStatementOrderDetailBase.getCurrentPeriodStartAndEnd())) {
                            orderProductMap.put(checkStatementOrderDetailBase.getOrderNo(), checkStatementOrderDetailBase.getCurrentPeriodStartAndEnd());
                        }
                    }
                }
            }
            //对没有单价的商品处理时间
            for (CheckStatementOrderDetailBase checkStatementOrderDetailBase: exportList) {
                if (OrderItemType.ORDER_ITEM_TYPE_PRODUCT.equals(checkStatementOrderDetailBase.getOrderType())
                        && BigDecimalUtil.compare(checkStatementOrderDetailBase.getUnitAmount(),BigDecimal.ZERO)==0) {
                    if (!orderProductMap.isEmpty() && orderProductMap.containsKey(checkStatementOrderDetailBase.getOrderNo())) {
                        String currentPeriodStartAndEnd = orderProductMap.get(checkStatementOrderDetailBase.getOrderNo());
                        checkStatementOrderDetailBase.setCurrentPeriodStartAndEnd(currentPeriodStartAndEnd);
                    }
                }
            }

            //按照租赁日期进行排序
            Collections.sort(exportList, new Comparator<CheckStatementOrderDetailBase>() {
                @Override
                public int compare(CheckStatementOrderDetailBase o1, CheckStatementOrderDetailBase o2) {
                    // 返回值为int类型，大于0表示正序，小于0表示逆序
                    if (o1.getRentStartTime() == null && o2.getRentStartTime()!=null) {
                        return 1;
                    } else if (o1.getRentStartTime() != null && o2.getRentStartTime()==null) {
                        return -1;
                    } else if (o1.getRentStartTime() == null && o2.getRentStartTime()==null) {
                        return 1;
                    }else {
                        int i = Integer.valueOf(String.valueOf(o1.getRentStartTime().getTime()/1000 - o2.getRentStartTime().getTime()/1000));
                        return i;
                    }
                }
            });

            String sheetName = "";
            if (checkStatementOrder.getMonthTime() != null) {
                sheetName = checkStatementOrder.getMonthTime().replace("-", "年") + "月";
            }
            if(CollectionUtil.isEmpty(exportList)){
                continue;
            }


            XSSFSheet hssfSheet = null;
            //创建一个excel并且传入
            if (hssfWorkbook == null) {
                hssfWorkbook = new XSSFWorkbook();
            }
            if (hssfWorkbook.getSheet(sheetName) == null) {
                hssfSheet = hssfWorkbook.createSheet(sheetName);
                hssfSheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 27));
                XSSFRow rowNo1 = hssfSheet.createRow(0);
                rowNo1.setHeightInPoints(39);
                XSSFCell cellNo1 = rowNo1.createCell(0);
                cellNo1.setCellValue(customerName);
                rowNo1.setHeightInPoints(30);
                XSSFCellStyle cellStyle = hssfWorkbook.createCellStyle();
                Font font = hssfWorkbook.createFont();
                font.setFontName("宋体");//字体格式
                font.setFontHeightInPoints((short) 9);//字体大小
                font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体显示
                cellStyle.setFont(font);
                cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER); // 居中
                cellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);//垂直居中
                cellNo1.setCellStyle(cellStyle);
            }
            if (hssfSheet == null) {
                continue;
            }

            ServiceResult<String, XSSFWorkbook> serviceResult = excelExportService.getXSSFWorkbook(hssfWorkbook, hssfSheet, exportList, ExcelExportConfigGroup.statementOrderCheckConfig, sheetName, 2, 40, 63);
            hssfWorkbook = serviceResult.getResult();
            XSSFSheet sheetAt = hssfWorkbook.getSheet(sheetName);
            int lastRowNum = sheetAt.getLastRowNum();

            sheetAt.setColumnHidden(6, true);
            sheetAt.setColumnHidden(7, true);
            sheetAt.setColumnHidden(8, true);
            sheetAt.setColumnHidden(9, true);
            sheetAt.setColumnHidden(10, true);
            sheetAt.setColumnHidden(12, true);
            sheetAt.setColumnHidden(13, true);
            sheetAt.setColumnHidden(14, true);
            sheetAt.setColumnHidden(15, true);
            sheetAt.setColumnHidden(16, true);

            XSSFRow hssfRow1 = sheetAt.createRow(lastRowNum + 2);
            XSSFRow hssfRow2 = sheetAt.createRow(lastRowNum + 3);
            XSSFRow hssfRow3 = sheetAt.createRow(lastRowNum + 4);
            XSSFRow hssfRow4 = sheetAt.createRow(lastRowNum + 5);
            XSSFRow hssfRow5 = sheetAt.createRow(lastRowNum + 6);
            hssfRow1.setHeightInPoints(30);
            hssfRow2.setHeightInPoints(30);
            hssfRow3.setHeightInPoints(30);
            hssfRow4.setHeightInPoints(30);
            hssfRow5.setHeightInPoints(30);


            XSSFCell cell201 = hssfRow1.createCell(25);
            XSSFCell cell202 = hssfRow2.createCell(25);
            XSSFCell cell203 = hssfRow3.createCell(25);
            XSSFCell cell204 = hssfRow4.createCell(25);
            XSSFCell cell205 = hssfRow5.createCell(25);

            // 本期应付
            cell201.setCellValue(Double.parseDouble(amountExcelExportView.view(totalEverPeriodAmountMap.get(checkStatementOrder.getMonthTime())).toString()));
            ExcelExportSupport.setCellStyle(hssfWorkbook, cell201, HSSFColor.GREY_80_PERCENT.index, HSSFColor.LEMON_CHIFFON.index);
            // 本期已付
            cell202.setCellValue(Double.parseDouble(amountExcelExportView.view(totalEverPeriodPaidAmountMap.get(checkStatementOrder.getMonthTime())).toString()));
            ExcelExportSupport.setCellStyle(hssfWorkbook, cell202, HSSFColor.GREY_80_PERCENT.index, HSSFColor.LEMON_CHIFFON.index);
            // 本期未付
            BigDecimal currentPeriodUnpaid = BigDecimalUtil.sub(totalEverPeriodAmountMap.get(checkStatementOrder.getMonthTime()), totalEverPeriodPaidAmountMap.get(checkStatementOrder.getMonthTime()));
            cell203.setCellValue(currentPeriodUnpaid.setScale(2, BigDecimal.ROUND_DOWN).doubleValue());
            ExcelExportSupport.setCellStyle(hssfWorkbook, cell203, HSSFColor.GREY_80_PERCENT.index, HSSFColor.LEMON_CHIFFON.index);

            if (StringUtil.isNotBlank(previousSheetName) || !sheetName.equals(previousSheetName)) {
                beforePeriodUnpaid = BigDecimalUtil.sub(allPeriodUnpaid, currentPeriodUnpaid);
            }

            // 截止上期未付
            cell204.setCellValue(beforePeriodUnpaid.setScale(2, BigDecimal.ROUND_DOWN).doubleValue());
            ExcelExportSupport.setCellStyle(hssfWorkbook, cell204, HSSFColor.GREY_80_PERCENT.index, HSSFColor.TAN.index);

            // 累计未付
            cell205.setCellValue(allPeriodUnpaid.doubleValue());
            ExcelExportSupport.setCellStyle(hssfWorkbook, cell205, HSSFColor.GREY_80_PERCENT.index, HSSFColor.TAN.index);

            if (StringUtil.isNotBlank(previousSheetName) || !sheetName.equals(previousSheetName)) {
                allPeriodUnpaid = BigDecimalUtil.sub(allPeriodUnpaid, currentPeriodUnpaid);
            }

            XSSFCell cell151 = hssfRow1.createCell(20);
            XSSFCell cell152 = hssfRow2.createCell(20);
            XSSFCell cell153 = hssfRow3.createCell(20);
            XSSFCell cell154 = hssfRow4.createCell(20);
            XSSFCell cell155 = hssfRow5.createCell(20);

            createCell(hssfWorkbook, hssfRow1, 21, 4);
            createCell(hssfWorkbook, hssfRow2, 21, 4);
            createCell(hssfWorkbook, hssfRow3, 21, 4);
            createCell(hssfWorkbook, hssfRow4, 21, 4);
            createCell(hssfWorkbook, hssfRow5, 21, 4);

            cell151.setCellValue("本月应付");
            ExcelExportSupport.setCellStyle(hssfWorkbook, cell151, HSSFColor.GREY_80_PERCENT.index, HSSFColor.LEMON_CHIFFON.index);
            cell152.setCellValue("本月已付");
            ExcelExportSupport.setCellStyle(hssfWorkbook, cell152, HSSFColor.GREY_80_PERCENT.index, HSSFColor.LEMON_CHIFFON.index);
            cell153.setCellValue("本月未付");
            ExcelExportSupport.setCellStyle(hssfWorkbook, cell153, HSSFColor.GREY_80_PERCENT.index, HSSFColor.LEMON_CHIFFON.index);
            cell154.setCellValue("截止上期未付");
            ExcelExportSupport.setCellStyle(hssfWorkbook, cell154, HSSFColor.GREY_80_PERCENT.index, HSSFColor.TAN.index);
            cell155.setCellValue("累计未付");
            ExcelExportSupport.setCellStyle(hssfWorkbook, cell155, HSSFColor.GREY_80_PERCENT.index, HSSFColor.TAN.index);

            hssfSheet.addMergedRegion(new CellRangeAddress(lastRowNum + 2, lastRowNum + 2, 20, 24));
            hssfSheet.addMergedRegion(new CellRangeAddress(lastRowNum + 3, lastRowNum + 3, 20, 24));
            hssfSheet.addMergedRegion(new CellRangeAddress(lastRowNum + 4, lastRowNum + 4, 20, 24));
            hssfSheet.addMergedRegion(new CellRangeAddress(lastRowNum + 5, lastRowNum + 5, 20, 24));
            hssfSheet.addMergedRegion(new CellRangeAddress(lastRowNum + 6, lastRowNum + 6, 20, 24));

            if (StringUtil.isBlank(previousSheetName) || !previousSheetName.equals(sheetName)) {
                previousSheetName = sheetName;
            }
        }


//            String file = "D:\\statement\\20180628\\" + dir + "\\" + (customerName + "对账单") + ".xlsx";
//            fileNames.add(file);
//            FileOutputStream o = new FileOutputStream(file);
//            hssfWorkbook.write(o);
//        }

        response.reset();
        response.setHeader("Content-disposition", "attachment; filename=" + new String((customerName + "对账单").getBytes("GB2312"), "ISO_8859_1") + ".xlsx");
        response.setContentType("application/json;charset=utf-8");
        OutputStream stream = response.getOutputStream();
        hssfWorkbook.write(stream);
        stream.flush();
        stream.close();

//        File srcfile[] = new File[fileNames.size()];
//        for (int i = 0, n1 = fileNames.size(); i < n1; i++) {
//            srcfile[i] = new File(fileNames.get(i));
//        }
//
//        ZipFiles(srcfile, zip);
//        FileInputStream inStream = new FileInputStream(zip);
//        byte[] buf = new byte[4096];
//        int readLength;
//        while (((readLength = inStream.read(buf)) != -1)) {
//            response.getOutputStream().write(buf, 0, readLength);
//        }
//
//        inStream.close();
//        response.getOutputStream().close();


        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    //压缩文件
    public static void ZipFiles(java.io.File[] srcfile, java.io.File zipfile) {
        byte[] buf = new byte[1024];
        try {
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
                    zipfile));
            for (int i = 0; i < srcfile.length; i++) {
                FileInputStream in = new FileInputStream(srcfile[i]);
                out.putNextEntry(new ZipEntry(srcfile[i].getName()));
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.closeEntry();
                in.close();
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println("对账单");
    }


    private String formatPeriodStartAndEnd(Date periodStart, Date periodEnd) {
        if (periodStart == null || periodEnd == null) {
            return "";
        }
        return "开始:" + new SimpleDateFormat("yyyy-MM-dd").format(periodStart) +
                "\n结束:" + new SimpleDateFormat("yyyy-MM-dd").format(periodEnd) +
                "\n期限:";
    }

    private void createCell(XSSFWorkbook hssfWorkbook, XSSFRow hssfRow, Integer startColumn, Integer several) {
        for (int i = startColumn; i < startColumn + several; i++) {
            XSSFCell cell = hssfRow.createCell(i);
            ExcelExportSupport.setCellStyle(hssfWorkbook, cell, HSSFColor.GREY_80_PERCENT.index, HSSFColor.LIGHT_GREEN.index);
        }
    }

    String formatReturnReasonType(Integer returnReasonType) {

        if (returnReasonType.equals(ReturnReasonType.NOT_REFUNDABLE)) {
            return "客户方设备不愿或无法退还";
        } else if (returnReasonType.equals(ReturnReasonType.EXPIRATION_OF_NORMAL)) {
            return "期满正常收回";
        } else if (returnReasonType.equals(ReturnReasonType.RETIRING_IN_ADVANCE)) {
            return "提前退租";
        } else if (returnReasonType.equals(ReturnReasonType.NO_PAYMENT_ON_TIME)) {
            return "未按时付款或风险等原因上门收回";
        } else if (returnReasonType.equals(ReturnReasonType.EQUIPMENT_FAILURE)) {
            return "设备故障等我方原因导致退货";
        } else if (returnReasonType.equals(ReturnReasonType.SUBJECTIVE_FACTORS)) {
            return "主观因素等客户方原因导致退货";
        } else if (returnReasonType.equals(ReturnReasonType.REPLACEMENT_EQUIPMENT)) {
            return "更换设备";
        } else if (returnReasonType.equals(ReturnReasonType.COMPANY_CLOSURES)) {
            return "公司经营不善/倒闭";
        } else if (returnReasonType.equals(ReturnReasonType.IDLE_EQUIPMENT)) {
            return "项目结束闲置";
        } else if (returnReasonType.equals(ReturnReasonType.FOLLOW_THE_RENT)) {
            return "满三个月或六个月随租随还";
        } else if (returnReasonType.equals(ReturnReasonType.OTHER)) {
            return "其它',";
        }else if (returnReasonType.equals(ReturnReasonType.COMMODITY_FAILURE_REPLACEMENT)) {
            return "商品故障更换";
        }else if (returnReasonType.equals(ReturnReasonType.CONFIGURATION_UPGRADE_REPLACEMENT)) {
            return "配置升级更换";
        }else if (returnReasonType.equals(ReturnReasonType.ORDER_INVALIDATION )) {
            return "订单作废/取消";
        }else if (returnReasonType.equals(ReturnReasonType.PRICE_CAUSE_TO_PURCHASE)) {
            return "价格原因转购买";
        }else if (returnReasonType.equals(ReturnReasonType.PRICE_REASONS_FOR_SUPPLIERS)) {
            return "价格原因换供应商";
        }else if (returnReasonType.equals(ReturnReasonType.PURCHASE_OF_COMMODITY_QUALITY)) {
            return "商品质量问题转购买";
        }else if (returnReasonType.equals(ReturnReasonType.COMMODITY_QUALITY_PROBLEMS_FOR_SUPPLIERS)) {
            return "商品质量问题换供应商";
        }else if (returnReasonType.equals(ReturnReasonType.THE_SERVICE_IS_NOT_RETURNED_IN_TIME)) {
            return "服务不及时造成退货";
        }else if (returnReasonType.equals(ReturnReasonType.STAFF_LEAVING_OR_STUDENT_GRADUATION_IDLE)) {
            return "人员离职/学生毕业闲置";
        }
        return "";
    }
    
}
