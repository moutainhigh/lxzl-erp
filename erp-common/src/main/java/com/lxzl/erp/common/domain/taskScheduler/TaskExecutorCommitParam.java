package com.lxzl.erp.common.domain.taskScheduler;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.validGroup.UpdateGroup;

import javax.validation.constraints.NotNull;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/7/25
 * @Time : Created in 21:51
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskExecutorCommitParam{

        @NotNull(message = ErrorCode.QUARTZ_TASK_EXECUTOR_ID_NOT_NULL,groups = {UpdateGroup.class})
        private Long id;  //数据id
        private Integer retryCount;  //重试次数
        private Integer retryInterval;  //重试间隔
        private String requestUrl;  //请求的url
        private String requestBody;  //请求的json字符串
        private String jobCallbackClassName;  //工作回调处理的className、若要清理className则传空串
        private Integer asyn;  //异步标志 0 同步 1 异步


        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Integer getRetryCount() {
            return retryCount;
        }

        public void setRetryCount(Integer retryCount) {
            this.retryCount = retryCount;
        }

        public Integer getRetryInterval() {
            return retryInterval;
        }

        public void setRetryInterval(Integer retryInterval) {
            this.retryInterval = retryInterval;
        }

        public String getRequestUrl() {
            return requestUrl;
        }

        public void setRequestUrl(String requestUrl) {
            this.requestUrl = requestUrl;
        }

        public String getRequestBody() {
            return requestBody;
        }

        public void setRequestBody(String requestBody) {
            this.requestBody = requestBody;
        }

        public String getJobCallbackClassName() {
            return jobCallbackClassName;
        }

        public void setJobCallbackClassName(String jobCallbackClassName) {
            this.jobCallbackClassName = jobCallbackClassName;
        }

        public Integer getAsyn() {
            return asyn;
        }

        public void setAsyn(Integer asyn) {
            this.asyn = asyn;
        }
}
