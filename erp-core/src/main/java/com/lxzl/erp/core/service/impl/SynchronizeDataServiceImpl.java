package com.lxzl.erp.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.base.PaymentResult;
import com.lxzl.erp.common.domain.statement.BatchReCreateOrderStatementParam;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.common.util.FastJsonUtil;
import com.lxzl.erp.common.util.http.client.HttpClientUtil;
import com.lxzl.erp.common.util.http.client.HttpHeaderBuilder;
import com.lxzl.erp.core.service.SynchronizeDataService;
import com.lxzl.erp.dataaccess.dao.mysql.order.OrderMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Description: </p>
 *
 * @Auther: huahongbin
 * @Date: 2018/6/9
 */
@Service
public class SynchronizeDataServiceImpl implements SynchronizeDataService {

    private static Logger logger = LoggerFactory.getLogger(SynchronizeDataServiceImpl.class);

    private static final String BATCH_RECREATE_ORDER_STATEMENT_URL = "https://www.52rental.com/lx-erp/statementOrder/batchReCreateOrderStatement";
    private static final String LOGIN_URL = "https://www.52rental.com/lx-erp/user/login";
    private static final String LOGIN_USER_NAME = "admin";
    private static final String LOGIN_PASSWORD = "lxzl@admin#5432";

    private static final String ORDER_NO_LIST = "LXO-20110305-010-00001";

    @Override
    public void synchronizeOrderList2BatchReCreateOrderStatement(long millis, int orderNum) {
        CloseableHttpClient client = HttpClients.createDefault();
//        List<String> orderNoList = orderMapper.findAllOrderNo();
        String[] orders = ORDER_NO_LIST.split(",");
        login(client, LOGIN_USER_NAME, LOGIN_PASSWORD);

        List<String> postOrderList;
        List<String> successOrderList = new ArrayList<>();
        List<String> failedOrderList = new ArrayList<>();
        BatchReCreateOrderStatementParam batchReCreateOrderStatementParam;

        logger.info("***********获取所有订单号调用批量重算接口 开始**************");
        logger.info("批量重算接口为：{}", BATCH_RECREATE_ORDER_STATEMENT_URL);
        logger.info("订单号数量为：{}", orders.length);
        logger.info("调用接口睡眠间隔：{}毫秒", millis);
        int currentOrderIndex = 0;
        while (currentOrderIndex < orders.length) {
            postOrderList = new ArrayList<>();
            int toIndex = (currentOrderIndex + orderNum) > orders.length ? orders.length : (currentOrderIndex + orderNum);
            for (int i = currentOrderIndex; i < toIndex; i++) {
                postOrderList.add(orders[i]);
            }

            batchReCreateOrderStatementParam = new BatchReCreateOrderStatementParam();
            batchReCreateOrderStatementParam.setOrderNoList(postOrderList);

            try {
                HttpHeaderBuilder headerBuilder = HttpHeaderBuilder.custom();
                headerBuilder.contentType("application/json");
                String response = HttpClientUtil.post(BATCH_RECREATE_ORDER_STATEMENT_URL, FastJsonUtil.toJSONString(batchReCreateOrderStatementParam), headerBuilder, "UTF-8");
                PaymentResult paymentResult = JSON.parseObject(response, PaymentResult.class);

                if (ErrorCode.SUCCESS.equals(paymentResult.getCode())) {
                    successOrderList.addAll(postOrderList);
                    logger.info("订单号：{} 调用批量重算接口成功。返回消息：{}", combineListStr(postOrderList), response);
                } else {
                    failedOrderList.addAll(postOrderList);
                    logger.info("订单号：{} 调用批量重算接口失败。返回消息：{}", combineListStr(postOrderList), response);
                }
            } catch (Exception e) {
                failedOrderList.addAll(postOrderList);
                logger.error("订单号：{} 调用批量重算接口失败。", combineListStr(postOrderList));
            }

            currentOrderIndex = currentOrderIndex + orderNum;

            // 线程睡眠一定时间
            try {
                Thread.sleep(millis);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.error("线程sleep异常");
            }
        }
        logger.info("接口调用成功的订单数量为{}，订单列表为：{}", successOrderList.size(), combineListStr(successOrderList));
        logger.info("接口调用失败的订单数量为{}，订单列表为：{}", failedOrderList.size(), combineListStr(failedOrderList));
        logger.info("***********获取所有订单号调用批量重算接口 结束**************");
    }

    private void login(HttpClient httpClient, String username, String password) {
        User user = new User();
        user.setUserName(username);
        user.setPassword(password);

        logger.info("***********模拟登陆 开始**************");
        logger.info("登陆接口为：{}", LOGIN_URL);
        logger.info("登陆用户名为：{}，登陆密码为：{}", username, password);
        try {
            HttpHeaderBuilder headerBuilder = HttpHeaderBuilder.custom();
            headerBuilder.contentType("application/json");
            String response = HttpClientUtil.post(LOGIN_URL, FastJsonUtil.toJSONString(user), headerBuilder, "UTF-8");
            PaymentResult paymentResult = JSON.parseObject(response, PaymentResult.class);
            if (ErrorCode.SUCCESS.equals(paymentResult.getCode())) {
                logger.info("模拟登陆成功！");
            } else {
                logger.info("模拟登陆失败，返回消息：{}", response);
            }
        } catch (Exception e) {
            logger.error("模拟登陆失败！");
        }
        logger.info("***********模拟登陆 结束**************");

    }

    private String combineListStr(List<String> orderNoList) {
        StringBuilder sb = new StringBuilder();
        if (CollectionUtil.isNotEmpty(orderNoList)) {
            for (String orderNo : orderNoList) {
                sb.append(orderNo);
                sb.append(",");
            }
        }
        return sb.toString();
    }

    @Autowired
    private OrderMapper orderMapper;
}
