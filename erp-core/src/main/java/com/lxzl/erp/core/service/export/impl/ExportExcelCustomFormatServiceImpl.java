package com.lxzl.erp.core.service.export.impl;

import com.lxzl.erp.common.constant.*;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.k3.pojo.returnOrder.ReturnReasonType;
import com.lxzl.erp.common.domain.statement.CheckStatementOrderDetailBase;
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
import com.lxzl.erp.dataaccess.dao.mysql.k3.K3ReturnOrderDetailMapper;
import com.lxzl.erp.dataaccess.dao.mysql.k3.K3ReturnOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.OrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.OrderMaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.OrderProductMapper;
import com.lxzl.erp.dataaccess.dao.mysql.statement.StatementOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.statementOrderCorrect.StatementOrderCorrectMapper;
import com.lxzl.erp.dataaccess.domain.k3.returnOrder.K3ReturnOrderDO;
import com.lxzl.erp.dataaccess.domain.k3.returnOrder.K3ReturnOrderDetailDO;
import com.lxzl.erp.dataaccess.domain.order.OrderDO;
import com.lxzl.erp.dataaccess.domain.order.OrderMaterialDO;
import com.lxzl.erp.dataaccess.domain.order.OrderProductDO;
import com.lxzl.erp.dataaccess.domain.statementOrderCorrect.StatementOrderCorrectDO;
import com.lxzl.se.common.util.StringUtil;
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
        statementOrderMonthQueryParam.setStatementOrderStartTime(statementOrderStartTime);
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
//        //处理没单价产品结算日期时间
//        Map<String,Map<Integer,CheckStatementOrderDetail>> monthMap = new HashMap<>();
//        for (CheckStatementOrder checkStatementOrder : checkStatementOrderList) {
//            List<CheckStatementOrderDetail> statementOrderDetailList = checkStatementOrder.getStatementOrderDetailList();
//
//        }



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

        for (CheckStatementOrder checkStatementOrder : checkStatementOrderList) {
            if (checkStatementOrder.getStatementExpectPayTime().getTime() > DateUtil.getEndMonthDate(new Date()).getTime()) {
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

            //按照租赁日期进行排序
            List<CheckStatementOrderDetail> statementOrderDetailList = checkStatementOrder.getStatementOrderDetailList();
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
            return "公司倒闭";
        } else if (returnReasonType.equals(ReturnReasonType.IDLE_EQUIPMENT)) {
            return "设备";
        } else if (returnReasonType.equals(ReturnReasonType.FOLLOW_THE_RENT)) {
            return "满三个月或六个月随租随还";
        } else if (returnReasonType.equals(ReturnReasonType.OTHER)) {
            return "其它',";
        }
        return "";
    }
    
}
