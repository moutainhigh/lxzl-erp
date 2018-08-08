package com.lxzl.erp.core.service.taskScheduler.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.RemoteQuartzConfig;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.base.TaskExecutorResult;
import com.lxzl.erp.common.domain.taskScheduler.TaskExecutorCommitParam;
import com.lxzl.erp.common.domain.taskScheduler.TaskExecutorQueryParam;
import com.lxzl.erp.common.domain.taskScheduler.TaskSchedulerCommitParam;
import com.lxzl.erp.common.domain.taskScheduler.TriggerCommitParam;
import com.lxzl.erp.common.domain.taskScheduler.pojo.HolidayDTO;
import com.lxzl.erp.common.domain.taskScheduler.pojo.TaskExecutor;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.common.util.http.client.HttpClientUtil;
import com.lxzl.erp.common.util.http.client.HttpHeaderBuilder;
import com.lxzl.erp.core.service.taskScheduler.TaskSchedulerService;
import com.lxzl.se.common.exception.BusinessException;
import com.lxzl.se.common.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/7/25
 * @Time : Created in 21:45
 */
@Service
public class TaskSchedulerServiceImpl implements TaskSchedulerService {

    public static final String TRIGGER_GROUP_SUCCESS_CODE = "A_000000";

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public ServiceResult<String, String> initTaskScheduler(TaskSchedulerCommitParam initTaskSchedulerCommitParam) {
        ServiceResult<String, String> result = new ServiceResult<>();
        if (CollectionUtil.isNotEmpty(initTaskSchedulerCommitParam.getHolidayDTOList())) {
            if (StringUtil.isEmpty(initTaskSchedulerCommitParam.getHolidayName())) {
                result.setErrorCode(ErrorCode.QUARTZ_HOLIDAY_NAME_NOT_NULL);
                return result;
            }
        }

        TaskExecutor taskExecutor = initTaskSchedulerCommitParam.getTaskExecutor();
        if(StringUtil.isNotBlank(taskExecutor.getRequestBody())){
            if(!isJSONValid(taskExecutor.getRequestBody())){
                result.setErrorCode(ErrorCode.QUARTZ_TASK_EXECUTOR_REQUEST_BODY_ERROR);
                return result;
            }
        }


        try {
            HttpHeaderBuilder headerBuilder = HttpHeaderBuilder.custom();
            headerBuilder.contentType("application/json");

            String requestJson = JSON.toJSONString(initTaskSchedulerCommitParam);
            JSONObject jsonObject = JSON.parseObject(requestJson);
            if (jsonObject.get("holidayDTOList") != null) {
                jsonObject.remove("holidayDTOList");
                jsonObject.put("holidayDTOs", initTaskSchedulerCommitParam.getHolidayDTOList());
            }
            String response = HttpClientUtil.post(RemoteQuartzConfig.remoteQuartzURL + "/initTask", jsonObject.toJSONString(), headerBuilder, "UTF-8");
            logger.info("manual charge response:{}", response);
            TaskExecutorResult taskExecutorResult = JSON.parseObject(response, TaskExecutorResult.class);
            if (TRIGGER_GROUP_SUCCESS_CODE.equals(taskExecutorResult.getCode())) {
                result.setErrorCode(ErrorCode.SUCCESS);
                return result;
            }
            throw new BusinessException(taskExecutorResult.getDescription());
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }

    @Override
    public ServiceResult<String, String> updateTaskScheduler(TaskSchedulerCommitParam initTaskSchedulerCommitParam) {
        ServiceResult<String, String> result = new ServiceResult<>();
        if (CollectionUtil.isNotEmpty(initTaskSchedulerCommitParam.getHolidayDTOList())) {
            if (StringUtil.isEmpty(initTaskSchedulerCommitParam.getHolidayName())) {
                result.setErrorCode(ErrorCode.QUARTZ_HOLIDAY_NAME_NOT_NULL);
                return result;
            }
        }
        try {
            HttpHeaderBuilder headerBuilder = HttpHeaderBuilder.custom();
            headerBuilder.contentType("application/json");
            String requestJson = JSON.toJSONString(initTaskSchedulerCommitParam);
            JSONObject jsonObject = JSON.parseObject(requestJson);
            if (jsonObject.get("holidayDTOList") != null) {
                jsonObject.remove("holidayDTOList");
                jsonObject.put("holidayDTOs", initTaskSchedulerCommitParam.getHolidayDTOList());
            }
            String response = HttpClientUtil.post(RemoteQuartzConfig.remoteQuartzURL + "/updateTaskScheduler", jsonObject.toJSONString(), headerBuilder, "UTF-8");
            logger.info("manual charge response:{}", response);
            TaskExecutorResult taskExecutorResult = JSON.parseObject(response, TaskExecutorResult.class);
            if (TRIGGER_GROUP_SUCCESS_CODE.equals(taskExecutorResult.getCode())) {
                result.setErrorCode(ErrorCode.SUCCESS);
                return result;
            }
            throw new BusinessException(taskExecutorResult.getDescription());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new BusinessException(e.getMessage());
        }
    }

    @Override
    public ServiceResult<String, String> pauseTaskTrigger(TriggerCommitParam triggerCommitParam) {
        ServiceResult<String, String> result = new ServiceResult<>();

        try {
            HttpHeaderBuilder headerBuilder = HttpHeaderBuilder.custom();
            headerBuilder.contentType("application/json");
            String requestJson = JSON.toJSONString(triggerCommitParam);

            String response = HttpClientUtil.post(RemoteQuartzConfig.remoteQuartzURL + "/pauseTaskTrigger", requestJson, headerBuilder, "UTF-8");
            logger.info("manual charge response:{}", response);
            TaskExecutorResult taskExecutorResult = JSON.parseObject(response, TaskExecutorResult.class);
            if (TRIGGER_GROUP_SUCCESS_CODE.equals(taskExecutorResult.getCode())) {
                result.setErrorCode(ErrorCode.SUCCESS);
                return result;
            }
            throw new BusinessException(taskExecutorResult.getDescription());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new BusinessException(e.getMessage());
        }
    }

    @Override
    public ServiceResult<String, String> resumeTaskTrigger(TriggerCommitParam triggerCommitParam) {
        ServiceResult<String, String> result = new ServiceResult<>();

        try {
            HttpHeaderBuilder headerBuilder = HttpHeaderBuilder.custom();
            headerBuilder.contentType("application/json");
            String requestJson = JSON.toJSONString(triggerCommitParam);
            String response = HttpClientUtil.post(RemoteQuartzConfig.remoteQuartzURL + "/resumeTaskTrigger", requestJson, headerBuilder, "UTF-8");
            logger.info("manual charge response:{}", response);
            TaskExecutorResult taskExecutorResult = JSON.parseObject(response, TaskExecutorResult.class);
            if (TRIGGER_GROUP_SUCCESS_CODE.equals(taskExecutorResult.getCode())) {
                result.setErrorCode(ErrorCode.SUCCESS);
                return result;
            }
            throw new BusinessException(taskExecutorResult.getDescription());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new BusinessException(e.getMessage());
        }
    }

    @Override
    public ServiceResult<String, String> deleteTaskTrigger(TriggerCommitParam triggerCommitParam) {
        ServiceResult<String, String> result = new ServiceResult<>();

        try {
            HttpHeaderBuilder headerBuilder = HttpHeaderBuilder.custom();
            headerBuilder.contentType("application/json");
            String requestJson = JSON.toJSONString(triggerCommitParam);
            String response = HttpClientUtil.post(RemoteQuartzConfig.remoteQuartzURL + "/deleteTrigger", requestJson, headerBuilder, "UTF-8");
            logger.info("manual charge response:{}", response);
            TaskExecutorResult taskExecutorResult = JSON.parseObject(response, TaskExecutorResult.class);
            if (TRIGGER_GROUP_SUCCESS_CODE.equals(taskExecutorResult.getCode())) {
                result.setErrorCode(ErrorCode.SUCCESS);
                return result;
            }
            throw new BusinessException(taskExecutorResult.getDescription());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new BusinessException(e.getMessage());
        }
    }

    @Override
    public ServiceResult<String, String> updateTaskExecutor(TaskExecutorCommitParam taskExecutorCommitParam) {
        ServiceResult<String, String> result = new ServiceResult<>();

        if(StringUtil.isNotBlank(taskExecutorCommitParam.getRequestBody())){
            if(!isJSONValid(taskExecutorCommitParam.getRequestBody())){
                result.setErrorCode(ErrorCode.QUARTZ_TASK_EXECUTOR_REQUEST_BODY_ERROR);
                return result;
            } 
        }
        try {
            HttpHeaderBuilder headerBuilder = HttpHeaderBuilder.custom();
            headerBuilder.contentType("application/json");
            String requestJson = JSON.toJSONString(taskExecutorCommitParam);
            String response = HttpClientUtil.post(RemoteQuartzConfig.remoteQuartzURL + "/updateTaskExecutor", requestJson, headerBuilder, "UTF-8");
            logger.info("manual charge response:{}", response);
            TaskExecutorResult taskExecutorResult = JSON.parseObject(response, TaskExecutorResult.class);
            if (TRIGGER_GROUP_SUCCESS_CODE.equals(taskExecutorResult.getCode())) {
                result.setErrorCode(ErrorCode.SUCCESS);
                return result;
            }
            throw new BusinessException(taskExecutorResult.getDescription());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new BusinessException(e.getMessage());
        }
    }

    @Override
    public ServiceResult<String, Page<TaskExecutor>> pageTaskExecutor(TaskExecutorQueryParam taskExecutorQueryParam) {
        ServiceResult<String, Page<TaskExecutor>> result = new ServiceResult<>();

        try {
            HttpHeaderBuilder headerBuilder = HttpHeaderBuilder.custom();
            headerBuilder.contentType("application/json");
            String requestJson = JSON.toJSONString(taskExecutorQueryParam);
            JSONObject jsonObject = JSON.parseObject(requestJson);
            jsonObject.put("pageNum", taskExecutorQueryParam.getPageNo());
            if (jsonObject.get("triggerName") == null || "".equals(jsonObject.get("triggerName"))) {
                jsonObject.remove("triggerName");
            }
            if (jsonObject.get("triggerGroup") == null || "".equals(jsonObject.get("triggerGroup"))) {
                jsonObject.remove("triggerGroup");
            }

            String response = HttpClientUtil.post(RemoteQuartzConfig.remoteQuartzURL + "/pageTaskExecutors", jsonObject.toJSONString(), headerBuilder, "UTF-8");

            logger.info("manual charge response:{}", response);
            TaskExecutorResult taskExecutorResult = JSON.parseObject(response, TaskExecutorResult.class);
            Map<String, Object> resultMap = taskExecutorResult.getResultMap();
            Page<TaskExecutor> page = JSON.parseObject(JSON.toJSONString(resultMap), Page.class);
            Object pageTotalNum = resultMap.get("pageTotalNum");
            Object listObject = resultMap.get("list");
            List<TaskExecutor> list = new ArrayList<>();
            if (listObject != null) {
                if (listObject instanceof List) {
                    list = JSON.parseArray(JSON.toJSONString(listObject), TaskExecutor.class);
                }
            }

            Object countObject = resultMap.get("total");
            Integer count = null;
            if (countObject != null) {
                if (countObject instanceof Integer) {
                    count = (Integer) countObject;
                }
            }

            if (CollectionUtil.isNotEmpty(list)) {
                for (TaskExecutor taskExecutor : list) {
                    List<String> holidays = taskExecutor.getHolidays();
                    if (CollectionUtil.isNotEmpty(holidays)) {
                        List<HolidayDTO> holidayDTOList = new ArrayList<>();
                        for (String holiday : holidays) {
                            if (holiday.split("-").length == 3) {
                                String[] split = holiday.split("-");
                                HolidayDTO holidayDTO = new HolidayDTO();
                                holidayDTO.setYear(split[0].matches("^[0-9]*$") ? Integer.valueOf(split[0]) : null);
                                holidayDTO.setMonth(split[1].matches("^[0-9]*$") ? Integer.valueOf(split[1]) : null);
                                holidayDTO.setDay(split[2].matches("^[0-9]*$") ? Integer.valueOf(split[2]) : null);
                                holidayDTOList.add(holidayDTO);
                            }
                        }
                        taskExecutor.setHolidayDTOList(holidayDTOList);
                    }
                }
            }

            page.setItemList(list);
            page.setTotalCount(count);
            page.setPageSize(taskExecutorQueryParam.getPageSize());
            page.setCurrentPage(taskExecutorQueryParam.getPageNo());
            page.setPageCount(Integer.valueOf(pageTotalNum.toString()));

            result.setErrorCode(ErrorCode.SUCCESS);
            result.setResult(page);
            return result;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new BusinessException(e.getMessage());
        }
    }

    @Override
    public ServiceResult<String, TaskExecutor> detailTaskExecutor(TaskExecutorQueryParam taskExecutorQueryParam) {
        ServiceResult<String, TaskExecutor> result = new ServiceResult<>();

        try {
            HttpHeaderBuilder headerBuilder = HttpHeaderBuilder.custom();
            headerBuilder.contentType("application/json");
            String requestJson = JSON.toJSONString(taskExecutorQueryParam);

            String response = HttpClientUtil.post(RemoteQuartzConfig.remoteQuartzURL + "/detailTaskExecutor", requestJson, headerBuilder, "UTF-8");

            logger.info("manual charge response:{}", response);
            TaskExecutorResult taskExecutorResult = JSON.parseObject(response, TaskExecutorResult.class);
            Map<String, Object> resultMap = taskExecutorResult.getResultMap();

            Object listObject = resultMap.get("list");
            List<TaskExecutor> list = new ArrayList<>();
            if (listObject != null) {
                if (listObject instanceof List) {
                    list = JSON.parseArray(JSON.toJSONString(listObject), TaskExecutor.class);
                }
            }
            TaskExecutor taskExecutor = null;
            if (CollectionUtil.isNotEmpty(list)) {
                taskExecutor = list.get(0);
            }

            List<String> holidays = taskExecutor.getHolidays();
            if (CollectionUtil.isNotEmpty(holidays)) {
                List<HolidayDTO> holidayDTOList = new ArrayList<>();
                for (String holiday : holidays) {
                    if (holiday.split("-").length == 3) {
                        String[] split = holiday.split("-");
                        HolidayDTO holidayDTO = new HolidayDTO();
                        holidayDTO.setYear(split[0].matches("^[0-9]*$") ? Integer.valueOf(split[0]) : null);
                        holidayDTO.setMonth(split[1].matches("^[0-9]*$") ? Integer.valueOf(split[1]) : null);
                        holidayDTO.setDay(split[2].matches("^[0-9]*$") ? Integer.valueOf(split[2]) : null);
                        holidayDTOList.add(holidayDTO);
                    }
                }
                taskExecutor.setHolidayDTOList(holidayDTOList);
            }

            result.setErrorCode(ErrorCode.SUCCESS);
            result.setResult(taskExecutor);
            return result;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new BusinessException(e.getMessage());
        }
    }

    public final static boolean isJSONValid(String test) {
        try {
            JSONObject.parseObject(test);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
