package com.lxzl.erp.core.service.export.impl;

import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.constant.OrderItemType;
import com.lxzl.erp.common.constant.OrderRentType;
import com.lxzl.erp.common.constant.OrderType;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.k3.pojo.returnOrder.ReturnReasonType;
import com.lxzl.erp.common.domain.statement.CheckStatementOrderDetailBase;
import com.lxzl.erp.common.domain.statement.StatementOrderMonthQueryParam;
import com.lxzl.erp.common.domain.statement.pojo.CheckStatementOrder;
import com.lxzl.erp.common.domain.statement.pojo.CheckStatementOrderDetail;
import com.lxzl.erp.common.util.BigDecimalUtil;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.common.util.DateUtil;
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
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

        ServiceResult<String, String> result = new ServiceResult<>();
        //todo 获取有的预计支付时间
        XSSFWorkbook hssfWorkbook = null;
        BigDecimal beforePeriodUnpaid = new BigDecimal(0);
        BigDecimal allPeriodUnpaid = new BigDecimal(0);
        ServiceResult<String, List<CheckStatementOrder>> stringListServiceResult = statementService.exportQueryStatementOrderCheckParam(statementOrderMonthQueryParam);
        List<CheckStatementOrder> checkStatementOrderList = stringListServiceResult.getResult();

        //算出总的未付
        for (CheckStatementOrder checkStatementOrder : checkStatementOrderList) {
            allPeriodUnpaid = BigDecimalUtil.add(allPeriodUnpaid, BigDecimalUtil.sub(checkStatementOrder.getStatementAmount(), checkStatementOrder.getStatementPaidAmount()));
        }

        for (CheckStatementOrder checkStatementOrder : checkStatementOrderList) {
            List<CheckStatementOrderDetail> ExportStatementOrderDetailList = checkStatementOrder.getStatementOrderDetailList();
            //要保存到额集合
            List<CheckStatementOrderDetailBase> exportList = new ArrayList<>();
            //本期未付
            BigDecimal currentPeriodUnpaid = BigDecimalUtil.sub(checkStatementOrder.getStatementAmount(), checkStatementOrder.getStatementPaidAmount());
            for (CheckStatementOrderDetail exportStatementOrderDetail : ExportStatementOrderDetailList) {
                //todo 分装对象来处理
                CheckStatementOrderDetailBase exportStatementOrderDetailBase = new CheckStatementOrderDetailBase();
                exportStatementOrderDetailBase.setOrderNo(exportStatementOrderDetail.getOrderNo()); //订单编号
                exportStatementOrderDetailBase.setOrderType(exportStatementOrderDetail.getOrderItemType());    // 单子类型，详见ORDER_TYPE
                exportStatementOrderDetailBase.setItemName(exportStatementOrderDetail.getItemName());  //商品名
                exportStatementOrderDetailBase.setItemCount(exportStatementOrderDetail.getItemCount());  //数量
                exportStatementOrderDetailBase.setUnitAmount(exportStatementOrderDetail.getUnitAmount());  //"单价（元/台/月）"
                exportStatementOrderDetailBase.setStatementRentAmount(exportStatementOrderDetail.getStatementDetailRentAmount());             // 租金小计
                exportStatementOrderDetailBase.setStatementRentDepositAmount(exportStatementOrderDetail.getStatementDetailRentDepositAmount());      // 租金押金
                exportStatementOrderDetailBase.setStatementDepositAmount(exportStatementOrderDetail.getStatementDetailDepositAmount());      // 设备押金
                exportStatementOrderDetailBase.setStatementOverdueAmount(exportStatementOrderDetail.getStatementDetailOverdueAmount());      // 逾期金额
                exportStatementOrderDetailBase.setStatementOtherAmount(exportStatementOrderDetail.getStatementDetailOtherAmount());      // 其它费用
                exportStatementOrderDetailBase.setStatementCorrectAmount(exportStatementOrderDetail.getStatementDetailCorrectAmount());      // 冲正金额
                exportStatementOrderDetailBase.setStatementAmount(BigDecimalUtil.sub(exportStatementOrderDetail.getStatementDetailAmount(), exportStatementOrderDetail.getStatementDetailPaidAmount()));      // 应付金额
                exportStatementOrderDetailBase.setStatementExpectPayTime(exportStatementOrderDetail.getStatementExpectPayTime());      // 应付日期
                exportStatementOrderDetailBase.setStatementDepositPaidAmount(exportStatementOrderDetail.getStatementDetailDepositPaidAmount());      // 支付押金
                exportStatementOrderDetailBase.setStatementDetailStatus(exportStatementOrderDetail.getStatementDetailStatus());      // 状态
                exportStatementOrderDetailBase.setIsNew(exportStatementOrderDetail.getIsNew());  //是否全新
                //获取订单类型  月或者天租
                exportStatementOrderDetailBase.setBusinessType(exportStatementOrderDetail.getOrderType());    //业务类型


                //冲正单单号和原因保存
                List<StatementOrderCorrectDO> statementOrderCorrectDOList = statementOrderCorrectMapper.findStatementOrderIdAndItemId(exportStatementOrderDetail.getStatementOrderId(), exportStatementOrderDetail.getOrderId(), exportStatementOrderDetail.getOrderItemReferId());
                if (CollectionUtil.isNotEmpty(statementOrderCorrectDOList)) {
                    StringBuffer statementCorrectNo = new StringBuffer();
                    StringBuffer statementCorrectReason = new StringBuffer();
                    for (StatementOrderCorrectDO statementOrderCorrectDO : statementOrderCorrectDOList) {
                        statementCorrectNo.append(statementOrderCorrectDO.getStatementCorrectNo() + "/n");
                        statementCorrectReason.append(statementOrderCorrectDO.getStatementCorrectReason() + "/n");
                    }
                    exportStatementOrderDetailBase.setStatementCorrectNo(String.valueOf(statementCorrectNo));
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
                        if(k3ReturnOrderDetailDO != null){
                            OrderDO orderDO = orderMapper.findByOrderNo(k3ReturnOrderDetailDO.getOrderNo());
                            exportStatementOrderDetailBase.setOrderNo(orderDO.getOrderNo());
                            exportStatementOrderDetail.setOrderRentStartTime(orderDO.getRentStartTime());//租赁开始日期
                            exportStatementOrderDetail.setOrderExpectReturnTime(orderDO.getExpectReturnTime());//租赁结束日期
                            exportStatementOrderDetail.setOrderRentType(orderDO.getRentType());
                            exportStatementOrderDetail.setOrderRentTimeLength(orderDO.getRentTimeLength());
                            if (OrderItemType.ORDER_ITEM_TYPE_RETURN_PRODUCT.equals(exportStatementOrderDetail.getOrderItemType())) {
                                OrderProductDO orderProductDO = orderProductMapper.findById(Integer.parseInt(k3ReturnOrderDetailDO.getOrderItemId()));
                                Integer isNewProduct = orderProductDO.getIsNewProduct();
                                exportStatementOrderDetailBase.setIsNew(isNewProduct);
                            } else if (OrderItemType.ORDER_ITEM_TYPE_RETURN_MATERIAL.equals(exportStatementOrderDetail.getOrderItemType())) {
                                OrderMaterialDO orderMaterialDO = orderMaterialMapper.findById(Integer.parseInt(k3ReturnOrderDetailDO.getOrderItemId()));
                                Integer isNewMaterial = orderMaterialDO.getIsNewMaterial();
                                exportStatementOrderDetailBase.setIsNew(isNewMaterial);
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
                exportStatementOrderDetailBase.setRentStartTime(exportStatementOrderDetail.getOrderRentStartTime());    //租赁开始日期
                exportStatementOrderDetailBase.setExpectReturnTime(exportStatementOrderDetail.getOrderExpectReturnTime());    //租赁结束日期
                if (OrderRentType.RENT_TYPE_DAY.equals(exportStatementOrderDetail.getOrderRentType())) {
                    exportStatementOrderDetailBase.setDay(exportStatementOrderDetail.getOrderRentTimeLength());  //日
                    exportStatementOrderDetailBase.setMonth(0);  //月
                } else if (OrderRentType.RENT_TYPE_MONTH.equals(exportStatementOrderDetail.getOrderRentType())) {
                    exportStatementOrderDetailBase.setDay(0);   //日
                    exportStatementOrderDetailBase.setMonth(exportStatementOrderDetail.getOrderRentTimeLength());  //月
                }
                String allRentTimeLength = exportStatementOrderDetailBase.getMonth() + "月" + exportStatementOrderDetailBase.getDay() + "天";
                exportStatementOrderDetailBase.setAllRentTimeLength(allRentTimeLength);    //租赁总期限
                exportStatementOrderDetailBase.setAllPeriodStartAndEnd(formatPeriodStartAndEnd(exportStatementOrderDetail.getOrderRentStartTime(), exportStatementOrderDetail.getOrderExpectReturnTime()) + allRentTimeLength);    //本期起止（总的期数起止）
                //-------------------以上是全部结算单-----------------------------

                //-------------------以下是本期结算单-----------------------------
                exportStatementOrderDetailBase.setStatementStartTime(exportStatementOrderDetail.getStatementStartTime());     //结算开始日期
                exportStatementOrderDetailBase.setStatementEndTime(exportStatementOrderDetail.getStatementEndTime());  //结算结束日期
                String monthAndDays = DateUtil.getMonthAndDays(exportStatementOrderDetail.getStatementStartTime(), exportStatementOrderDetail.getStatementEndTime());
                int month = monthAndDays.indexOf("月");
                int day = monthAndDays.indexOf("天");
                exportStatementOrderDetailBase.setStatementMonth(Integer.parseInt(monthAndDays.substring(0, month))); //月
                exportStatementOrderDetailBase.setStatementDay(Integer.parseInt(monthAndDays.substring(month + 1, day))); //日
                exportStatementOrderDetailBase.setRentTimeLength(monthAndDays);  //期限
                exportStatementOrderDetailBase.setCurrentPeriodStartAndEnd(formatPeriodStartAndEnd(exportStatementOrderDetail.getStatementStartTime(), exportStatementOrderDetail.getStatementEndTime()) + exportStatementOrderDetailBase.getRentTimeLength());    //本期起止（当前期数起止）
                //-------------------以上是本期结算单-----------------------------


                exportList.add(exportStatementOrderDetailBase);
            }

            String sheetName = "";
            if (checkStatementOrder.getMonthTime() != null) {
                sheetName = checkStatementOrder.getMonthTime().replace("-", "年") + "月";
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
                cellNo1.setCellValue(checkStatementOrder.getCustomerName());
                rowNo1.setHeightInPoints(30);
                XSSFCellStyle cellStyle = hssfWorkbook.createCellStyle();
                Font font = hssfWorkbook.createFont();
                font.setFontName("微软雅黑");//字体格式
                font.setFontHeightInPoints((short) 14);//字体大小
                font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体显示
                cellStyle.setFont(font);
                cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER); // 居中
                cellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);//垂直居中
                cellNo1.setCellStyle(cellStyle);
            }

            ServiceResult<String, XSSFWorkbook> serviceResult = excelExportService.getXSSFWorkbook(hssfWorkbook, hssfSheet, exportList, ExcelExportConfigGroup.statementOrderCheckConfig, sheetName, 2, 40, 63);
            hssfWorkbook = serviceResult.getResult();
            XSSFSheet sheetAt = hssfWorkbook.getSheet(sheetName);
            int lastRowNum = sheetAt.getLastRowNum();

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

            cell201.setCellValue(amountExcelExportView.view(checkStatementOrder.getStatementAmount()).toString());
            ExcelExportSupport.setCellStyle(hssfWorkbook, cell201, HSSFColor.GREY_80_PERCENT.index, HSSFColor.LIGHT_GREEN.index);
            cell202.setCellValue(amountExcelExportView.view(checkStatementOrder.getStatementPaidAmount()).toString());
            ExcelExportSupport.setCellStyle(hssfWorkbook, cell202, HSSFColor.GREY_80_PERCENT.index, HSSFColor.LIGHT_GREEN.index);
            cell203.setCellValue(currentPeriodUnpaid.toString());
            ExcelExportSupport.setCellStyle(hssfWorkbook, cell203, HSSFColor.GREY_80_PERCENT.index, HSSFColor.LIGHT_GREEN.index);
            if (beforePeriodUnpaid != null) {
                cell204.setCellValue(beforePeriodUnpaid.toString());
                ExcelExportSupport.setCellStyle(hssfWorkbook, cell204, HSSFColor.GREY_80_PERCENT.index, HSSFColor.LIGHT_GREEN.index);
            }
            //这是上一期
            beforePeriodUnpaid = currentPeriodUnpaid;

            cell205.setCellValue(allPeriodUnpaid.toString());
            ExcelExportSupport.setCellStyle(hssfWorkbook, cell205, HSSFColor.GREY_80_PERCENT.index, HSSFColor.LIGHT_GREEN.index);

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

            cell151.setCellValue("本期应付");
            ExcelExportSupport.setCellStyle(hssfWorkbook, cell151, HSSFColor.GREY_80_PERCENT.index, HSSFColor.LIGHT_GREEN.index);
            cell152.setCellValue("本期已付");
            ExcelExportSupport.setCellStyle(hssfWorkbook, cell152, HSSFColor.GREY_80_PERCENT.index, HSSFColor.LIGHT_GREEN.index);
            cell153.setCellValue("本期未付");
            ExcelExportSupport.setCellStyle(hssfWorkbook, cell153, HSSFColor.GREY_80_PERCENT.index, HSSFColor.LIGHT_GREEN.index);
            cell154.setCellValue("上期未付");
            ExcelExportSupport.setCellStyle(hssfWorkbook, cell154, HSSFColor.GREY_80_PERCENT.index, HSSFColor.SEA_GREEN.index);
            cell155.setCellValue("累计未付");
            ExcelExportSupport.setCellStyle(hssfWorkbook, cell155, HSSFColor.GREY_80_PERCENT.index, HSSFColor.SEA_GREEN.index);

            hssfSheet.addMergedRegion(new CellRangeAddress(lastRowNum + 2, lastRowNum + 2, 20, 24));
            hssfSheet.addMergedRegion(new CellRangeAddress(lastRowNum + 3, lastRowNum + 3, 20, 24));
            hssfSheet.addMergedRegion(new CellRangeAddress(lastRowNum + 4, lastRowNum + 4, 20, 24));
            hssfSheet.addMergedRegion(new CellRangeAddress(lastRowNum + 5, lastRowNum + 5, 20, 24));
            hssfSheet.addMergedRegion(new CellRangeAddress(lastRowNum + 6, lastRowNum + 6, 20, 24));
        }

        response.reset();
        response.setHeader("Content-disposition", "attachment; filename=" + new String("对账单".getBytes("GB2312"), "ISO_8859_1") + ".xlsx");
        response.setContentType("application/json;charset=utf-8");
        OutputStream stream = response.getOutputStream();
        hssfWorkbook.write(stream);
        stream.flush();
        stream.close();
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    private String formatPeriodStartAndEnd(Date periodStart, Date periodEnd) {
        if (periodStart == null || periodEnd == null) {
            return "";
        }
        return "开始:" + new SimpleDateFormat("yyyy-MM-dd").format(periodStart) +
                "开始:" + new SimpleDateFormat("yyyy-MM-dd").format(periodEnd) +
                "期限:";
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
