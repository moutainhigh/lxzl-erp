package com.lxzl.erp.core.service.Job.impl.support;

import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.TaskSchedulerSystemConfig;
import com.lxzl.se.common.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/8/22
 * @Time : Created in 14:39
 */
@Component
public class JobSupport {

    private static Logger logger = LoggerFactory.getLogger(JobSupport.class);

    public static ServiceResult<String, String> verifyAppIdAndAppSecret(HttpServletRequest request){
        ServiceResult<String, String> serviceResult = new ServiceResult<>();

        String appId = request.getHeader("appId");
        String appSecret = request.getHeader("appSecret");


        if(StringUtil.isBlank(appId)){
            serviceResult.setErrorCode(ErrorCode.QUARTZ_APP_ID_ERROR);
            return serviceResult;
        }

        if(StringUtil.isBlank(appSecret)){
            serviceResult.setErrorCode(ErrorCode.QUARTZ_APP_SECRET_ERROR);
            return serviceResult;
        }

        if(!TaskSchedulerSystemConfig.taskSchedulerSystemAppId.equals(appId) || !TaskSchedulerSystemConfig.taskSchedulerAppSecret.equals(appSecret)){
            logger.error("配对错误！");
            serviceResult.setErrorCode(ErrorCode.QUARTZ_SYSTEM_ERROR);
            return serviceResult;
        }
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }

}
