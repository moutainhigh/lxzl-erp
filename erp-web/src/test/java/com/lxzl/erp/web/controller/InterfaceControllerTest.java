package com.lxzl.erp.web.controller;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/1/17
 * @Time : Created in 9:54
 */
public class InterfaceControllerTest {
    @Test
    public void queryOrderByNo() throws Exception {
    }

    @Test
    public void queryAllOrder() throws Exception {
    }

    @Test
    public void queryCustomer() throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("127.0.0.1:8080/interface/queryCustomer");
    }

}