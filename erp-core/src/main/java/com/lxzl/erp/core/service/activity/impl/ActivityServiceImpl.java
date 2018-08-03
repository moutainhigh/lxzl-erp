package com.lxzl.erp.core.service.activity.impl;

import com.alibaba.fastjson.JSONObject;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.MallSystemConfig;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.activity.ActivityOrderInterfaceParam;
import com.lxzl.erp.common.domain.activity.ActivityOrderParam;
import com.lxzl.erp.common.util.FastJsonUtil;
import com.lxzl.erp.core.service.activity.ActivityService;
import com.lxzl.se.common.domain.Result;
import com.lxzl.se.common.exception.BusinessException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service("activityServiceImpl")
public class ActivityServiceImpl implements ActivityService {

    @Override
    public ServiceResult<String, Result> getActivityOrder(ActivityOrderParam param) {
        ServiceResult<String, Result> serviceResult = new ServiceResult<>();
        param.setAdvancedState(1);
        ActivityOrderInterfaceParam activityOrderInterfaceParam = new ActivityOrderInterfaceParam();
        activityOrderInterfaceParam.setAppId(MallSystemConfig.mallSystemAppId);
        activityOrderInterfaceParam.setAppSecret(MallSystemConfig.mallSystemAppSecret);
        activityOrderInterfaceParam.setActivityOrderParam(param);
        HttpPost httppost = new HttpPost(MallSystemConfig.getActivityOrderUrl);
        httppost.setEntity(new StringEntity(FastJsonUtil.toJSONNoFeatures(activityOrderInterfaceParam),
                ContentType.create("application/json", "UTF-8")));
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(5000).setConnectionRequestTimeout(1000)
                .setSocketTimeout(5000).build();
        httppost.setConfig(requestConfig);
        String resultString;
        try (CloseableHttpClient httpclient = HttpClients.createDefault();
             CloseableHttpResponse httpresponse = httpclient.execute(httppost)) {
            resultString = EntityUtils.toString(httpresponse.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
//            serviceResult.setErrorCode(ErrorCode.MALL_SYSTEM_CONNECT_EXCEPTION);
            return serviceResult;
        }
        try {
            final JSONObject jsonObject = JSONObject.parseObject(resultString);
            boolean success = jsonObject.getBoolean("success");
            final JSONObject resultMap = jsonObject.getJSONObject("resultMap");
            final String description = jsonObject.getString("description");
            final String code = jsonObject.getString("code");
            Result result = new Result(code, description, success);
            result.setResultMap(resultMap);
            serviceResult.setResult(result);
            return serviceResult;
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }
}
