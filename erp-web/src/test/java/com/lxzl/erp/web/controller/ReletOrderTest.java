package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPTransactionalTest;
import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.constant.OrderPayMode;
import com.lxzl.erp.common.constant.OrderRentType;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.messagethirdchannel.pojo.MessageThirdChannel;
import com.lxzl.erp.common.domain.order.pojo.Order;
import com.lxzl.erp.common.domain.order.pojo.OrderMaterial;
import com.lxzl.erp.common.domain.order.pojo.OrderProduct;
import com.lxzl.erp.common.domain.reletorder.ReletOrderCommitParam;
import com.lxzl.erp.common.domain.reletorder.ReletOrderQueryParam;
import com.lxzl.erp.common.domain.reletorder.pojo.ReletOrder;
import com.lxzl.erp.common.domain.reletorder.pojo.ReletOrderMaterial;
import com.lxzl.erp.common.domain.reletorder.pojo.ReletOrderProduct;
import com.lxzl.erp.common.domain.user.UserQueryParam;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.common.util.BigDecimalUtil;
import com.lxzl.erp.common.util.DateUtil;
import com.lxzl.erp.core.service.reletorder.ReletOrderService;
import com.lxzl.erp.core.service.user.UserService;
import com.lxzl.erp.dataaccess.dao.mysql.company.SubCompanyMapper;
import com.lxzl.erp.dataaccess.dao.mysql.reletorder.ReletOrderMapper;
import com.lxzl.erp.dataaccess.domain.company.SubCompanyDO;
import com.lxzl.erp.dataaccess.domain.reletorder.ReletOrderDO;
import org.junit.Test;
import org.junit.experimental.theories.suppliers.TestedOn;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 续租单测试
 *
 * @author ZhaoZiXuan
 * @date 2018/4/24 9:53
 */
public class ReletOrderTest extends ERPUnTransactionalTest {

    @Autowired
    private ReletOrderMapper reletOrderMapper;

    @Autowired
    private ReletOrderService reletOrderService;

    @Test
    public void testCreateReletByOrderNo() throws  Exception{
        Order order = new Order();
//        order.setOrderNo("LXO-20180523-1000-00053");
        order.setOrderNo("LXO-20180529-027-00164");
//        order.setOrderNo("LXSE2018010930");
        order.setRentTimeLength(25);

        List<OrderProduct> orderProductList = new ArrayList<>();
        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setOrderProductId(2986);
        BigDecimal productUnitAmount = new BigDecimal(20.00000);
        orderProduct.setProductUnitAmount(productUnitAmount);
        orderProductList.add(orderProduct);

//        OrderProduct orderProduct1 = new OrderProduct();
//        orderProduct1.setOrderProductId(2757);
//        BigDecimal productUnitAmount1 = new BigDecimal(600.00000);
//        orderProduct1.setProductUnitAmount(productUnitAmount1);
//        orderProductList.add(orderProduct1);

//        OrderProduct orderProduct2 = new OrderProduct();
//        orderProduct2.setOrderProductId(2764);
//        BigDecimal productUnitAmount2 = new BigDecimal(150.00000);
//        orderProduct2.setProductUnitAmount(productUnitAmount2);
//        orderProductList.add(orderProduct2);
//
//        OrderProduct orderProduct3 = new OrderProduct();
//        orderProduct3.setOrderProductId(2765);
//        BigDecimal productUnitAmount3 = new BigDecimal(150.00000);
//        orderProduct3.setProductUnitAmount(productUnitAmount3);
//        orderProductList.add(orderProduct3);

        order.setOrderProductList(orderProductList);  //商品项

        List<OrderMaterial> orderMaterialList = new ArrayList<>();
        OrderMaterial orderMaterial = new OrderMaterial();
        orderMaterial.setOrderMaterialId(5105);
        BigDecimal materialUnitAmount = new BigDecimal(20.00000);
        orderMaterial.setMaterialUnitAmount(materialUnitAmount);
        orderMaterialList.add(orderMaterial);

//        OrderMaterial orderMaterial1 = new OrderMaterial();
//        orderMaterial1.setOrderMaterialId(4717);
//        BigDecimal materialUnitAmount1 = new BigDecimal(10.00000);
//        orderMaterial1.setMaterialUnitAmount(materialUnitAmount1);
//        orderMaterialList.add(orderMaterial1);
//
//        OrderMaterial orderMaterial2 = new OrderMaterial();
//        orderMaterial2.setOrderMaterialId(4349);
//        BigDecimal materialUnitAmount2 = new BigDecimal(600.00000);
//        orderMaterial2.setMaterialUnitAmount(materialUnitAmount2);
//        orderMaterialList.add(orderMaterial2);
//
        order.setOrderMaterialList(orderMaterialList);   //配件项

        TestResult testResult = getJsonTestResult("/reletOrder/create", order);
    }


    @Test
    public void testUpdateReletByOrderNo() throws  Exception{
        ReletOrder reletOrder = new ReletOrder();
        reletOrder.setReletOrderNo("LXR-20180605-027-00028");
        reletOrder.setRentTimeLength(90);

        List<ReletOrderProduct> orderProductList = new ArrayList<>();
        ReletOrderProduct orderProduct = new ReletOrderProduct();
        orderProduct.setOrderProductId(2986);
        BigDecimal productUnitAmount = new BigDecimal(20.10000);
        orderProduct.setProductUnitAmount(productUnitAmount);
        orderProductList.add(orderProduct);

//        ReletOrderProduct orderProduct1 = new ReletOrderProduct();
//        orderProduct1.setOrderProductId(25);
//        BigDecimal productUnitAmount1 = new BigDecimal(600.30000);
//        orderProduct1.setProductUnitAmount(productUnitAmount1);
//        orderProductList.add(orderProduct1);

        reletOrder.setReletOrderProductList(orderProductList);

//        List<ReletOrderMaterial> orderMaterialList = new ArrayList<>();
//        ReletOrderMaterial orderMaterial = new ReletOrderMaterial();
//        orderMaterial.setOrderMaterialId(4359);
//        BigDecimal materialUnitAmount = new BigDecimal(1.00000);
//        orderMaterial.setMaterialUnitAmount(materialUnitAmount);
//        orderMaterialList.add(orderMaterial);

//        ReletOrderMaterial orderMaterial1 = new ReletOrderMaterial();
//        orderMaterial1.setOrderMaterialId(26);
//        BigDecimal materialUnitAmount1 = new BigDecimal(600.00000);
//        orderMaterial1.setMaterialUnitAmount(materialUnitAmount1);
//        orderMaterialList.add(orderMaterial1);
//
//        ReletOrderMaterial orderMaterial2 = new ReletOrderMaterial();
//        orderMaterial2.setOrderMaterialId(27);
//        BigDecimal materialUnitAmount2 = new BigDecimal(600.00000);
//        orderMaterial2.setMaterialUnitAmount(materialUnitAmount2);
//        orderMaterialList.add(orderMaterial2);

//        reletOrder.setReletOrderMaterialList(orderMaterialList);

        TestResult testResult = getJsonTestResult("/reletOrder/update", reletOrder);
    }

    @Test
    public void testCommitReletByOrderNo() throws  Exception{
        ReletOrderCommitParam reletOrderCommitParam = new ReletOrderCommitParam();
        reletOrderCommitParam.setReletOrderNo("LXR-20180619-027-00107");
        reletOrderCommitParam.setVerifyUser(500359);
        TestResult testResult = getJsonTestResult("/reletOrder/commit", reletOrderCommitParam);
    }

    @Test
    public void testIsNeedVerify() throws  Exception{
        ReletOrder reletOrder = new ReletOrder();
        reletOrder.setReletOrderNo("LXR-20180619-027-00107");
        TestResult testResult = getJsonTestResult("/reletOrder/isNeedVerify", reletOrder);
    }

    @Test
    public void testcancelReletOrderByNo() throws  Exception{
        ReletOrder reletOrder = new ReletOrder();
        reletOrder.setReletOrderNo("LXR-20180601-2001-00001");
        TestResult testResult = getJsonTestResult("/reletOrder/cancelReletOrderByNo", reletOrder);
    }


    @Test
    public void testhandleReletSendMessage() throws  Exception{
        reletOrderService.handleReletSendMessage(new Date());
    }


    @Test
    public void test() throws  Exception{
        //获取此订单是否有续租单
        boolean isReletOrder = false;
        ReletOrder reletOrder = new ReletOrder();
//        Integer orderId = 3002009;
//        testInteger(orderId);
//        System.out.println(orderId);

        reletOrder.setOrderId(213);
        testObj(reletOrder);
        System.out.println(reletOrder.getOrderId());
    }

    @Test
    public void testInteger() throws  Exception{
        BigDecimal zeroValue = new BigDecimal(0.0000);
        BigDecimal zeroValue1 = new BigDecimal(0);

        if (BigDecimalUtil.compare(zeroValue, BigDecimal.ZERO) == 0) {
            System.out.println("相等");
        }
        else if (BigDecimalUtil.compare(BigDecimal.ZERO, zeroValue1) < 0) {
            System.out.println("小于");
        }
        else {
            System.out.println("大于");
        }
    }

    private void testObj(ReletOrder reletOrder){
        ReletOrder reletOrder1 = new ReletOrder();
        reletOrder1.setOrderId(33333);
        reletOrder = reletOrder1;
    }

    @Test
    public void testTime(){
        Date currentTime = new Date();
        Date statementEndTime = com.lxzl.se.common.util.date.DateUtil.monthInterval(currentTime, 1);
        Calendar statementEndTimeCalendar = Calendar.getInstance();
        statementEndTimeCalendar.setTime(statementEndTime);
//        int statementEndMonthDays = DateUtil.getActualMaximum(statementEndTime);

        statementEndTimeCalendar.set(Calendar.DAY_OF_MONTH, 20);
        statementEndTime = statementEndTimeCalendar.getTime();

        System.out.println(statementEndTime);
    }

    @Test
    public void testCreateOrder() throws Exception {
        Order order = new Order();
//        order.setOrderId(3000010);
//        order.setOrderNo("LXO-20180305-027-00010");
//
//        order.setBuyerCustomerNo("LXCP-027-20180315-00669"); //客户编号
        order.setBuyerRemark("2018.4.25 16:52 测试zzx");

//        order.setOrderId(3001287);
//        order.setOrderNo("LXO-20180328-1000-01286");
//
//        order.setBuyerCustomerNo("LXCC-0755-20180112-00001"); //客户编号
//        order.setBuyerRemark("2018.4.25 16:52 测试zzx");


        order.setRentType(OrderRentType.RENT_TYPE_DAY); //租赁类型
        order.setRentTimeLength(6);//租赁时长



        order.setOrderId(3000005);
        order.setOrderNo("LXO-20180305-027-00005");

        order.setBuyerCustomerNo("LXCC-027-20180305-00004"); //客户编号
//        order.setBuyerRemark("2018.4.25 16:52 测试zzx");


        order.setRentStartTime(new Date());
        order.setRentStartTime(new Date());

        order.setOrderSubCompanyId(8);  //订单所属分公司
        order.setDeliverySubCompanyId(8); //订单发货分公司


        List<OrderProduct> orderProductList = new ArrayList<>();
        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setOrderProductId(9);
        orderProduct.setPayMode(OrderPayMode.PAY_MODE_PAY_BEFORE);
        orderProduct.setProductSkuId(104);
        orderProduct.setProductCount(2);
        orderProduct.setIsNewProduct(0);

        orderProduct.setRentingProductCount(2);
        orderProduct.setProductUnitAmount(new BigDecimal(16.66666));
        orderProductList.add(orderProduct);

        OrderProduct orderProduct1 = new OrderProduct();
        orderProduct1.setOrderProductId(9);
        orderProduct1.setPayMode(OrderPayMode.PAY_MODE_PAY_AFTER);
        orderProduct1.setProductSkuId(218);
        orderProduct1.setProductCount(5);
        orderProduct1.setIsNewProduct(0);

        orderProduct1.setRentingProductCount(3);
        orderProduct1.setProductUnitAmount(new BigDecimal(33.33333));
        orderProductList.add(orderProduct1);

        order.setOrderProductList(orderProductList);

        List<OrderMaterial> orderMaterialList = new ArrayList<>();

        OrderMaterial orderMaterial = new OrderMaterial();
        orderMaterial.setOrderMaterialId(41);
        orderMaterial.setPayMode(OrderPayMode.PAY_MODE_PAY_AFTER);
        orderMaterial.setMaterialId(40);
        orderMaterial.setMaterialCount(3);
        orderMaterial.setIsNewMaterial(1);

        orderMaterial.setRentingMaterialCount(3);
        orderMaterial.setMaterialUnitAmount(new BigDecimal(600.0));
        orderMaterialList.add(orderMaterial);

        OrderMaterial orderMaterial1 = new OrderMaterial();
        orderMaterial1.setOrderMaterialId(41);
        orderMaterial1.setPayMode(OrderPayMode.PAY_MODE_PAY_AFTER);
        orderMaterial1.setMaterialId(40);
        orderMaterial1.setMaterialCount(3);
        orderMaterial1.setIsNewMaterial(0);

        orderMaterial1.setRentingMaterialCount(3);
        orderMaterial1.setMaterialUnitAmount(new BigDecimal(600.0));
        orderMaterialList.add(orderMaterial1);

        order.setOrderMaterialList(orderMaterialList);


        TestResult testResult = getJsonTestResult("/reletOrder/create", order);
    }






    @Test
    public void testQueryAllReletOrder() throws Exception{
        ReletOrderQueryParam reletOrderQueryParam = new ReletOrderQueryParam();
 //       reletOrderQueryParam.setBuyerCustomerId(704447);
//        reletOrderQueryParam.setBuyerCustomerNo("LXO-20180305-0755-00028");
//        reletOrderQueryParam.setBuyerRealName("湖北华天翼建设工程有限公司");
////        reletOrderQueryParam.setCreateEndTime();
////        reletOrderQueryParam.setCreateStartTime();
//        reletOrderQueryParam.setOrderId(3000005);
//        reletOrderQueryParam.setOrderNo("LXO-20180305-027-00010");
//
//        //reletOrderQueryParam.setOrderStatus(0);
//        List<Integer> statusList = new ArrayList<>();
//        statusList.add(0);
//        statusList.add(8);
//        reletOrderQueryParam.setOrderStatusList(statusList);
//
//        reletOrderQueryParam.setOrderSellerId(500038);
//        reletOrderQueryParam.setPayStatus(0);
//
//        reletOrderQueryParam.setSubCompanyId(1);
//        reletOrderQueryParam.setDeliverySubCompanyId(8);
        reletOrderQueryParam.setOrderNo("LXO-20180623-027-00157");
        TestResult testResult = getJsonTestResult("/reletOrder/page", reletOrderQueryParam);
    }


    @Test
    public void testQueryReletOrderDetailById() throws Exception{
        ReletOrderQueryParam reletOrderQueryParam = new ReletOrderQueryParam();

        reletOrderQueryParam.setReletOrderNo("LXR-20180530-1000-00007");

        TestResult testResult = getJsonTestResult("/reletOrder/queryReletOrderByNo", reletOrderQueryParam);
    }

    @Test
    public void testReletDingDingMessage() throws Exception{
        ReletOrderDO reletOrderDO = reletOrderMapper.findByReletOrderNo("LXR-20180625-027-00111");
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String messageContent = "【" + reletOrderDO.getOrderSubCompanyName() + "】，业务员：" + reletOrderDO.getOrderSellerName()
                + "，客户名称：" + reletOrderDO.getBuyerCustomerName() + "，订单：【" + reletOrderDO.getOrderNo()
                + "】于 " + sdf.format(reletOrderDO.getUpdateTime())+ " 续租成功。";
        System.out.println(messageContent);
        UserQueryParam userQueryParam = new UserQueryParam();
        userQueryParam.setDepartmentType(300006);
        userQueryParam.setSubCompanyId(reletOrderDO.getOrderSubCompanyId());
        ServiceResult<String, Page<User>> serviceResult = userService.userPage(userQueryParam);
        if (ErrorCode.SUCCESS.equals(serviceResult.getErrorCode()) && serviceResult.getResult().getTotalCount() > 0){
            for (User user : serviceResult.getResult().getItemList()) {

                MessageThirdChannel messageThirdChannel = new MessageThirdChannel();
                messageThirdChannel.setReceiverUserId(user.getUserId());
                messageThirdChannel.setMessageContent(messageContent);
                messageThirdChannel.setMessageTitle("订单续租成功");

            }
        }
    }

    @Autowired
    private UserService userService;

}
