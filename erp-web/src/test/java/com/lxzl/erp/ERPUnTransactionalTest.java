package com.lxzl.erp;

import com.alibaba.fastjson.JSON;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.se.common.util.StringUtil;
import com.lxzl.se.unit.test.BaseUnTransactionalTest;
import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
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
@ContextConfiguration(locations = { "classpath*:spring-config.xml","classpath:spring/spring-mvc.xml" })
public class ERPUnTransactionalTest extends BaseUnTransactionalTest {
    private final Logger log = LoggerFactory.getLogger(getClass());

    protected MockMvc mockMvc;
    @Autowired
    protected WebApplicationContext wac;
    @Autowired
    protected MockHttpSession session;
    @Before
    public void setup(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        try {
            this.session = getLoginSession("admin","123456");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 测试结束后释放对象
     */
    @After
    public void destroy() {
        session.clearAttributes();
    }
    /**
     * 获取登入信息session
     * @return
     * @throws Exception
     */

    public MockHttpSession getLoginSession(String name ,String password) throws Exception{
        User user = new User();
        user.setUserName(name);
        user.setPassword(password);
        MvcResult result = this.mockMvc
                .perform((post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON.toJSONString(user))
                        .param("userName", name).param("password", password)))
                .andExpect(status().isOk())
                .andReturn();
        return (MockHttpSession)result.getRequest().getSession();
    }

    public MvcResult jsonTestRequest(String uri,Object o) throws Exception {
        MvcResult mvcResult =mockMvc.perform(post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON.toJSONString(o))
                .session(session)
        ).andExpect(status().isOk())
                .andReturn();

        return mvcResult;
    }
    public MvcResult jsonTestRequest(String uri) throws Exception {
        MvcResult mvcResult =mockMvc.perform(post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .session(session)
        ).andExpect(status().isOk())
                .andReturn();

        return mvcResult;
    }
    public TestResult getJsonTestResult(MvcResult mvcResult) throws UnsupportedEncodingException {
        TestResult testResult = null;
        String s = mvcResult.getResponse().getContentAsString();
        if(StringUtil.isEmpty(s)){
            log.info("您的请求没有响应，请确认请求地址 "+mvcResult.getRequest().getRequestURI()+" 是正确的");
            return testResult;
        }
        try {
            testResult = JSON.parseObject(s,TestResult.class);
        }catch (Exception e){
            log.info("JSON解析失败，此接口的返回类型不是Result对象！");
        }
        return testResult;
    }
    public TestResult getJsonTestResult(String uri, Object o) throws Exception {
        return getJsonTestResult(jsonTestRequest(uri,o));
    }
}
