package com.lxzl.erp.core.service.impl;

import com.lxzl.erp.common.domain.statement.BatchReCreateOrderStatementParam;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.common.util.FastJsonUtil;
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

    private static final String BATCH_RECREATE_ORDER_STATEMENT_URL = "http://192.168.10.200:8081/erp/statementOrder/batchReCreateOrderStatement";
    private static final String LOGIN_URL = "http://192.168.10.200:8081/erp/user/login";
    private static final String LOGIN_USER_NAME = "admin";
    private static final String LOGIN_PASSWORD = "lxzl123.456";

    public void synchronizeOrderList2BatchReCreateOrderStatement(long millis, int orderNum) {
        CloseableHttpClient client = HttpClients.createDefault();
        List<String> orderNoList = orderMapper.findAllOrderNo();
        login(client, LOGIN_USER_NAME, LOGIN_PASSWORD);

        List<String> postOrderList;
        List<String> successOrderList = new ArrayList<>();
        List<String> failedOrderList = new ArrayList<>();
        BatchReCreateOrderStatementParam batchReCreateOrderStatementParam;

        logger.info("***********获取所有订单号调用批量重算接口 开始**************");
        logger.info("批量重算接口为：{}", BATCH_RECREATE_ORDER_STATEMENT_URL);
        logger.info("订单号数量为：{}", orderNoList.size());
        logger.info("调用接口睡眠间隔：{}毫秒", millis);
        int currentOrderIndex = 0;
        while (currentOrderIndex < orderNoList.size()) {
            postOrderList = new ArrayList<>();
            int toIndex = (currentOrderIndex + orderNum) > orderNoList.size() ? orderNoList.size() : (currentOrderIndex + orderNum);
            postOrderList.addAll(orderNoList.subList(currentOrderIndex, toIndex));

            batchReCreateOrderStatementParam = new BatchReCreateOrderStatementParam();
            batchReCreateOrderStatementParam.setOrderNoList(postOrderList);

            try {
                String respContent = null;
                HttpPost httpPost = new HttpPost(BATCH_RECREATE_ORDER_STATEMENT_URL);
                StringEntity entity = new StringEntity(FastJsonUtil.toJSONString(batchReCreateOrderStatementParam), "UTF-8");
                entity.setContentEncoding("UTF-8");
                entity.setContentType("application/json");
                httpPost.setEntity(entity);
                HttpResponse resp = client.execute(httpPost);
                if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    HttpEntity he = resp.getEntity();
                    respContent = EntityUtils.toString(he, "UTF-8");
                    successOrderList.addAll(postOrderList);
                    logger.info("订单号：{} 调用批量重算接口成功。返回消息：{}", combineListStr(postOrderList), respContent);
                } else {
                    failedOrderList.addAll(postOrderList);
                    logger.info("订单号：{} 调用批量重算接口失败。返回消息：{}", combineListStr(postOrderList), respContent);
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
            String respContent = null;
            HttpPost httpPost = new HttpPost(LOGIN_URL);
            StringEntity entity = new StringEntity(FastJsonUtil.toJSONString(user), "UTF-8");
            entity.setContentEncoding("UTF-8");
            entity.setContentType("application/json");
            httpPost.setEntity(entity);
            HttpResponse resp = httpClient.execute(httpPost);
            if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                logger.info("模拟登陆成功！");
            } else {
                logger.info("模拟登陆失败，返回消息：{}", respContent);
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
