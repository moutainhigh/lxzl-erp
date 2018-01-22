package com.lxzl.erp;

import com.alibaba.fastjson.JSON;
import com.lxzl.se.common.util.StringUtil;
import com.lxzl.se.unit.test.BaseUnTransactionalTest;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.UnsupportedEncodingException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * User : LiuKe
 * Date : 2017/1/10
 * Time : 17:14
 */
@ContextConfiguration(locations = {"classpath*:spring-config.xml", "classpath:spring/spring-mvc.xml"})
public class ERPUnTransactionalNoLoginTest extends BaseUnTransactionalTest {
    private final Logger log = LoggerFactory.getLogger(getClass());

    protected MockMvc mockMvc;
    @Autowired
    protected WebApplicationContext wac;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }
    public MvcResult jsonTestRequest(String uri, Object o) throws Exception {

        MvcResult mvcResult = mockMvc.perform(post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON.toJSONString(o))
        ).andExpect(status().isOk())
                .andReturn();

        return mvcResult;
    }

    public MvcResult jsonTestRequest(String uri) throws Exception {
        MvcResult mvcResult = mockMvc.perform(post(uri)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk())
                .andReturn();

        return mvcResult;
    }

    public TestResult getJsonTestResult(MvcResult mvcResult) throws UnsupportedEncodingException {
        TestResult result = null;
        String s = mvcResult.getResponse().getContentAsString();
        if (StringUtil.isEmpty(s)) {
            log.info("您的请求没有响应，请确认请求地址 " + mvcResult.getRequest().getRequestURI() + " 是正确的");
            return result;
        }
        try {
            result = JSON.parseObject(s, TestResult.class);
        } catch (Exception e) {
            log.info("JSON解析失败，此接口的返回类型不是Result对象！");
        }
        return result;
    }

    public TestResult getJsonTestResult(String uri, Object o) throws Exception {
        return getJsonTestResult(jsonTestRequest(uri, o));
    }
}
