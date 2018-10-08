package com.lxzl.erp.web.dingdingTask;


import com.alibaba.fastjson.JSONObject;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.DingDingConfig;
import com.lxzl.erp.common.domain.DingDingMsgConfig;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.core.service.dingding.DingDingSupport.DingDingSupport;
import com.lxzl.erp.core.service.dynamicSql.DynamicSqlService;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @Author: pengbinjie
 * @Description：
 * @Date: Created in 19:43 2018/9/28
 * @Modified By:
 */


@Service("dingdingTimerSendMessage")
public class DingdingTimerSendMessage {
    @Autowired
    private DynamicSqlService dynamicSqlService;

    @Autowired
    private DingDingSupport dingDingSupport;

    public ServiceResult<String,List<String>> sendRobotDingdingMessage(){

        String url = DingDingConfig.dingDingGatewayUrl+DingDingMsgConfig.exceptionMsgUrl;
        //List<String> resList = Lists.newArrayList();  //报错：每个查询执行成功还是失败
        ServiceResult<String,List<String>> res = new ServiceResult<>();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String format = df.format(new Date());// new Date()为获取当前系统时间

        ServiceResult<String, List<List<Object>>> stringListServiceResult =
                dynamicSqlService.selectBySql("select t1.statement_order_id,t1.customer_id,t2.statement_order_no,t2.statement_status,sum(statement_detail_amount) as cc,t2.statement_amount as sa from erp_statement_order t2 left join erp_statement_order_detail t1 on t1.statement_order_id = t2.id where t1.data_status = 1 and t2.data_status = 1 group by t1.statement_order_id,t2.statement_amount,t2.statement_order_no,t1.customer_id having cc <> sa  order by cc");
        if (stringListServiceResult.getResult().size() > 0) {
            JSONObject jsonObject = new JSONObject();
            List<List<Object>> lists = stringListServiceResult.getResult();
            Integer count = lists.size();

            jsonObject.put("context", format + "\n" + "【结算单金额和结算单明细金额不等的数据为】：" + JSONObject.toJSONString(count));
            HttpClient httpclient = HttpClients.createDefault();
            HttpPost httppost = new HttpPost(url);
            httppost.addHeader("Content-Type", "application/json; charset=utf-8");
            StringEntity se = null;
            try {
                se = new StringEntity(jsonObject.toJSONString(), "utf-8");
                httppost.setEntity(se);
                HttpResponse response = httpclient.execute(httppost);
                //resList.add("结算单金额和结算单明细金额不等的数据[成功]");
            } catch (IOException e) {
                dingDingSupport.dingDingSendMessage("结算单金额和结算单明细金额不等的数据[失败]");
            }
        }

        stringListServiceResult =
                dynamicSqlService.selectBySql("select distinct t1.id,t1.statement_order_no,t2.order_id,t2.id from erp_statement_order t1 inner join erp_statement_order_detail t2 on t1.id = t2.statement_order_id where t1.data_status = 2 and t2.data_status = 1");
        if (stringListServiceResult.getResult().size() > 0) {
            JSONObject jsonObject = new JSONObject();
            List<List<Object>> lists = stringListServiceResult.getResult();
            Integer count = lists.size();
            jsonObject.put("context", format + "\n" + "【查询有结算单项没有结算单的记录】：" + JSONObject.toJSONString(count));

            HttpClient httpclient = HttpClients.createDefault();
            HttpPost httppost = new HttpPost(url);
            httppost.addHeader("Content-Type", "application/json; charset=utf-8");
            StringEntity se = null;
            try {
                se = new StringEntity(jsonObject.toJSONString(), "utf-8");
                httppost.setEntity(se);
                HttpResponse response = httpclient.execute(httppost);
            } catch (IOException e) {
               // resList.add("查询有结算单项没有结算单的记录[失败]");
                dingDingSupport.dingDingSendMessage("查询有结算单项没有结算单的记录[失败]");
            }
        }

        stringListServiceResult =
                dynamicSqlService.selectBySql("select t1.id, t1.statement_order_no, count(t2.id) as cc from erp_statement_order t1 left join erp_statement_order_detail t2 on t1.id =t2.statement_order_id and t2.data_status = 1 where t1.data_status = 1 group by t1.id having cc = 0");
        if (stringListServiceResult.getResult().size() > 0) {
            JSONObject jsonObject = new JSONObject();
            List<List<Object>> lists = stringListServiceResult.getResult();
            Integer count = lists.size();
            jsonObject.put("context", format + "\n" + "【查询有结算单没有结算单项目的记录】：" + JSONObject.toJSONString(count));

            HttpClient httpclient = HttpClients.createDefault();
            HttpPost httppost = new HttpPost(url);
            httppost.addHeader("Content-Type", "application/json; charset=utf-8");
            StringEntity se = null;
            try {
                se = new StringEntity(jsonObject.toJSONString(), "utf-8");
                httppost.setEntity(se);
                HttpResponse response = httpclient.execute(httppost);
            } catch (IOException e) {
                //resList.add("查询有结算单没有结算单项目的记录[失败]");
                dingDingSupport.dingDingSendMessage("查询有结算单没有结算单项目的记录[失败]");
            }
        }

        stringListServiceResult =
                dynamicSqlService.selectBySql("select sum(t2.statement_detail_rent_amount) as sra,t1.id,t1.statement_order_no,t1.statement_rent_amount from erp_statement_order t1 inner join erp_statement_order_detail t2 on t1.id = t2.statement_order_id where t1.data_status = 1 and t2.data_status = 1 GROUP BY t1.id,t1.statement_rent_amount,t1.statement_order_no HAVING sra <> t1.statement_rent_amount order by sra");
        if (stringListServiceResult.getResult().size() > 0) {
            JSONObject jsonObject = new JSONObject();
            List<List<Object>> lists = stringListServiceResult.getResult();
            Integer count = lists.size();
            jsonObject.put("context", format + "\n" + "【租赁明细和结算单租金对不上】：" + JSONObject.toJSONString(count));
            HttpClient httpclient = HttpClients.createDefault();
            HttpPost httppost = new HttpPost(url);
            httppost.addHeader("Content-Type", "application/json; charset=utf-8");
            StringEntity se = null;
            try {
                se = new StringEntity(jsonObject.toJSONString(), "utf-8");
                httppost.setEntity(se);
                HttpResponse response = httpclient.execute(httppost);
            } catch (IOException e) {
                dingDingSupport.dingDingSendMessage("租赁明细和结算单租金对不上[失败]");
            }
        }

        stringListServiceResult =
                dynamicSqlService.selectBySql("select distinct t2.order_no,t1.statement_detail_amount from erp_statement_order_detail t1 inner join erp_order t2 on t1.order_id = t2.id and t1.order_type = 1 and t1.data_status = 1 where t1.statement_detail_amount < 0");
        if (stringListServiceResult.getResult().size() > 0) {
            JSONObject jsonObject = new JSONObject();
            List<List<Object>> lists = stringListServiceResult.getResult();
            Integer count = lists.size();
            jsonObject.put("context", format + "\n" + "【结算单金额小于0的订单】：" + JSONObject.toJSONString(count));
            HttpClient httpclient = HttpClients.createDefault();
            HttpPost httppost = new HttpPost(url);
            httppost.addHeader("Content-Type", "application/json; charset=utf-8");
            StringEntity se = null;
            try {
                se = new StringEntity(jsonObject.toJSONString(), "utf-8");
                httppost.setEntity(se);
                HttpResponse response = httpclient.execute(httppost);
            } catch (IOException e) {
                dingDingSupport.dingDingSendMessage("结算单金额小于0的订单[失败]");
            }
        }

        stringListServiceResult =
                dynamicSqlService.selectBySql("select distinct customer_id , statement_expect_pay_time, count(id) as cc from erp_statement_order where data_status = 1 group by customer_id , statement_expect_pay_time having cc > 1");
        if (stringListServiceResult.getResult().size() > 0) {
            JSONObject jsonObject = new JSONObject();
            List<List<Object>> lists = stringListServiceResult.getResult();
            Integer count = lists.size();
            jsonObject.put("context", format + "\n" + "【同一个客户在同一天有两个结算单】：" + JSONObject.toJSONString(count));
            HttpClient httpclient = HttpClients.createDefault();
            HttpPost httppost = new HttpPost(url);
            httppost.addHeader("Content-Type", "application/json; charset=utf-8");
            StringEntity se = null;
            try {
                se = new StringEntity(jsonObject.toJSONString(), "utf-8");
                httppost.setEntity(se);
                HttpResponse response = httpclient.execute(httppost);
                //resList.add("同一个客户在同一天有两个结算单[成功]");
            } catch (IOException e) {
                dingDingSupport.dingDingSendMessage("同一个客户在同一天有两个结算单[失败]");
            }
        }

        stringListServiceResult =
                dynamicSqlService.selectBySql("select t1.statement_order_id,t1.id,t1.statement_correct_no,t1.statement_correct_amount,sum(t2.statement_correct_amount) as cc from erp_statement_order_correct t1 inner join erp_statement_order_correct_detail t2 on t1.id = t2.statement_order_correct_id where t1.data_status = 1 and t2.data_status = 1 group by t1.statement_order_id,t1.statement_correct_no,t1.statement_correct_amount having t1.statement_correct_amount <> cc");
        if (stringListServiceResult.getResult().size() > 0) {
            JSONObject jsonObject = new JSONObject();
            List<List<Object>> lists = stringListServiceResult.getResult();
            Integer count = lists.size();
            jsonObject.put("context", format + "\n" + "【冲正单金额和冲正单明细不等的情况】：" + JSONObject.toJSONString(count));
            HttpClient httpclient = HttpClients.createDefault();
            HttpPost httppost = new HttpPost(url);
            httppost.addHeader("Content-Type", "application/json; charset=utf-8");
            StringEntity se = null;
            try {
                se = new StringEntity(jsonObject.toJSONString(), "utf-8");
                httppost.setEntity(se);
                HttpResponse response = httpclient.execute(httppost);
                //resList.add("冲正单金额和冲正单明细不等的情况[成功]");
            } catch (IOException e) {
                dingDingSupport.dingDingSendMessage("冲正单金额和冲正单明细不等的情况[失败]");

            }
        }

        stringListServiceResult =
                dynamicSqlService.selectBySql("select id,m1.statement_order_id,m1.order_id,m1.order_item_refer_id,m1.create_time from erp_statement_order_detail m1 inner join (select count(id) as cc,t1.statement_order_id,t1.statement_start_time,t1.order_id,t1.order_item_refer_id,t1.statement_detail_amount from erp_statement_order_detail t1 where t1.data_status = 1 group by t1.statement_order_id,t1.statement_start_time,t1.order_id,t1.order_item_refer_id,t1.statement_detail_amount,statement_detail_type having cc >1) m2 on m1.statement_order_id = m2.statement_order_id and m1.order_id = m2.order_id and m1.order_item_refer_id = m2.order_item_refer_id  and m1.data_status = 1 order by m1.create_time");
        if (stringListServiceResult.getResult().size() > 0) {
            JSONObject jsonObject = new JSONObject();
            List<List<Object>> lists = stringListServiceResult.getResult();
            Integer count = lists.size();
            jsonObject.put("context", format + "\n" + "【同一个订单、同一个订单项在同一个结算单出现多次】：" + JSONObject.toJSONString(count));
            HttpClient httpclient = HttpClients.createDefault();
            HttpPost httppost = new HttpPost(url);
            httppost.addHeader("Content-Type", "application/json; charset=utf-8");
            StringEntity se = null;
            try {
                se = new StringEntity(jsonObject.toJSONString(), "utf-8");
                httppost.setEntity(se);
                HttpResponse response = httpclient.execute(httppost);
            } catch (IOException e) {
                dingDingSupport.dingDingSendMessage("同一个订单、同一个订单项在同一个结算单出现多次[失败]");
            }
        }
       res.setErrorCode(ErrorCode.SUCCESS);
     //res.setResult(resList);
        return res;
    }
}
