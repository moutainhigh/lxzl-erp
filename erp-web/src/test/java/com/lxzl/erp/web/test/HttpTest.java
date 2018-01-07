package com.lxzl.erp.web.test;

import com.alibaba.fastjson.JSON;
import com.lxzl.erp.common.domain.user.LoginParam;
import com.lxzl.erp.common.util.http.client.HttpClientUtil;
import org.junit.Test;


public class HttpTest {
    @Test
    public void test() throws Exception {
        LoginParam loginParam = new LoginParam();
        loginParam.setUserName("maotao");
        loginParam.setPassword("123456");
        String s = HttpClientUtil.post("http://192.168.10.94:8080/user/login", JSON.toJSONString(loginParam));
    }
}
