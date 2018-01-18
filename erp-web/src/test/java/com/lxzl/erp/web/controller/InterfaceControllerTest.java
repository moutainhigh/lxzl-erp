package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.domain.customer.CustomerCompanyQueryParam;
import com.lxzl.erp.common.domain.customer.pojo.Customer;
import com.lxzl.erp.common.util.ConverterUtil;
import com.lxzl.erp.common.util.JSONUtil;
import com.lxzl.erp.dataaccess.dao.mysql.user.UserRoleMapper;
import com.lxzl.erp.dataaccess.domain.company.SubCompanyDO;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/1/17
 * @Time : Created in 9:54
 */
public class InterfaceControllerTest {

//    private UserRoleMapper userRoleMapper;
    @Test
    public void queryOrderByNo() throws Exception {
//        SubCompanyDO subCompanyDO = userRoleMapper.findSubCompanyByUserId(500005);
    }

    @Test
    public void queryAllOrder() throws Exception {


    }

    @Test
    public void queryCustomer1() throws Exception {
        CustomerCompanyQueryParam customerCompanyQueryParam = new CustomerCompanyQueryParam();
        customerCompanyQueryParam.setCompanyName("清华同方科技有限公司");

        RestTemplate rest = new RestTemplate();
        String customer = rest.postForObject("http://127.0.0.1:8080/interface/queryCustomer",customerCompanyQueryParam,String.class);
        Map<String,Object> jsonToBean = JSONUtil.convertJSONToBean(customer, Map.class);
        Map<String,Object> map = (Map<String,Object>)jsonToBean.get("resultMap");
        Map<String,Object>  data = (Map<String,Object>) map.get("data");
//        Customer toBean = JSONUtil.convertJSONToBean(data.toString(), Customer.class);
        Customer convert = ConverterUtil.convert(data, Customer.class);
        System.out.println(convert);
    }

    @Test
    public void queryCustomer() throws Exception {

        CloseableHttpClient client = HttpClients.createDefault();
        URI url = new URI("http://127.0.0.1:8080/interface/queryCustomer");

        HttpPost httpPost = new HttpPost(url);
        //装填参数
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("companyName", "百度"));
        httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));

        //设置header信息
        //指定报文头【Content-type】、【User-Agent】
        httpPost.setHeader("Content-type", ContentType.APPLICATION_JSON.toString());
        httpPost.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

        //执行请求操作，并拿到结果（同步阻塞）
        CloseableHttpResponse response = client.execute(httpPost);
        //获取结果实体
        HttpEntity entity = response.getEntity();
        String body = "";
        if (entity != null) {
            //按指定编码转换结果实体为String类型
            body = EntityUtils.toString(entity, "utf-8");
            System.out.println(body);
        }
        EntityUtils.consume(entity);
        //释放链接
        response.close();
    }

}