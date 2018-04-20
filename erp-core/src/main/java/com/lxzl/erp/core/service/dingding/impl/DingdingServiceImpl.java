package com.lxzl.erp.core.service.dingding.impl;

import com.alibaba.fastjson.JSONObject;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.DingDingConfig;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.dingding.DingdingResultDTO;
import com.lxzl.erp.common.domain.dingding.DingdingSendTextMessageRequest;
import com.lxzl.erp.common.domain.dingding.member.DingdingUserDTO;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.common.util.FastJsonUtil;
import com.lxzl.erp.core.service.dingding.DingdingService;
import com.lxzl.erp.core.service.user.UserService;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author kai
 * @date 2018-02-05 15:00
 */
@Service("dingdingService")
public class DingdingServiceImpl implements DingdingService {
    private static final Logger logger = LoggerFactory.getLogger(DingdingServiceImpl.class);
    @Autowired
    private UserService userService;

    @Override
    public String sendUserGroupMessage(String userGroupUrl, DingdingSendTextMessageRequest request) {
        String respContent = null;
        try {
            HttpPost httpPost = new HttpPost(userGroupUrl);
            CloseableHttpClient client = HttpClients.createDefault();

            StringEntity entity = new StringEntity(FastJsonUtil.toJSONString(request), "UTF-8");//解决中文乱码问题
            entity.setContentEncoding("UTF-8");
            entity.setContentType("application/json");
            httpPost.setEntity(entity);
            HttpResponse resp = client.execute(httpPost);
            if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity he = resp.getEntity();
                respContent = EntityUtils.toString(he, "UTF-8");
            }
            logger.info("dingding send message success,{},response : {}", respContent);
        } catch (Exception e) {
            logger.error("dingding send message error,{}", e);
        }
        return respContent;
    }

    @Override
    public String getDingdingIdByPhone(String phone) {
        if (StringUtils.isBlank(phone)) {
            return null;
        }
        List<DingdingUserDTO> dingdingUserDTOS = getUsersFromDingdingGateway();
        if (dingdingUserDTOS == null) {
            logger.info("从钉钉中获取的用户数据失败-----" + phone);
            return null;
        }
        for (DingdingUserDTO dingdingUserDTO : dingdingUserDTOS) {
            if (dingdingUserDTO == null || StringUtils.isBlank(dingdingUserDTO.getMobile())) {
                continue;
            }
            if (StringUtils.equals(dingdingUserDTO.getMobile(), phone)) {
                return dingdingUserDTO.getUserId();
            }
        }
        return null;
    }

    @Override
    public ServiceResult<String, Object> applyApprovingWorkflow() {
        return null;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, Object> bindDingDingUsers() {
        ServiceResult<String, Object> result = new ServiceResult<>();
        result.setErrorCode(ErrorCode.SUCCESS);
        List<DingdingUserDTO> dingdingUserDTOS = getUsersFromDingdingGateway();
        if (dingdingUserDTOS == null) {
            logger.info("从钉钉网关获取用户信息失败：" + JSONObject.toJSONString(dingdingUserDTOS));
            result.setErrorCode(ErrorCode.BUSINESS_EXCEPTION);
            return result;
        }
        logger.info("获取钉钉的用户列表为：" + JSONObject.toJSONString(dingdingUserDTOS));
        List<User> usersFromDataBase = userService.findUsersByDingdingUsers(dingdingUserDTOS);
        // 更新数据库中钉钉编号
        int count = updateDingdingIdUsers(dingdingUserDTOS, usersFromDataBase);
        logger.info("更新钉钉id的条数为：" + count);
        List<DingdingUserDTO> notMatchUsers = getNotMatchUsers(dingdingUserDTOS, usersFromDataBase);
        logger.info("没有匹配上的钉钉用户为：" + JSONObject.toJSONString(notMatchUsers));
        // TODO doImportDingDingUsers执行导入用户数据
        result.setResult(notMatchUsers);
        return result;
    }

    /**
     * 获取没有匹配的钉钉用户列表信息
     */
    private List<DingdingUserDTO> getNotMatchUsers(List<DingdingUserDTO> dingdingUserDTOS, List<User> usersFromDataBase) {
        if (CollectionUtil.isEmpty(dingdingUserDTOS) || CollectionUtil.isEmpty(usersFromDataBase)) {
            return dingdingUserDTOS;
        }
        List<DingdingUserDTO> notMatchUsers = new ArrayList<>();
        for (DingdingUserDTO dingdingUserDTO : dingdingUserDTOS) {
            boolean notMatch = true;
            for (User user : usersFromDataBase) {
                if (isMatchUser(user, dingdingUserDTO)) {
                    notMatch = false;
                    break;
                }
            }
            if (notMatch) {
                notMatchUsers.add(dingdingUserDTO);
            }
        }
        return notMatchUsers;
    }

    /**
     * 更新用户信息的钉钉编号
     */
    private int updateDingdingIdUsers(List<DingdingUserDTO> dingdingUserDTOS, List<User> usersFromDataBase) {
        if (CollectionUtil.isEmpty(dingdingUserDTOS) || CollectionUtil.isEmpty(usersFromDataBase)) {
            return 0;
        }
        List<User> bindDingdingIdUsers = new ArrayList<>();
        for (User user : usersFromDataBase) {
            for (DingdingUserDTO dingdingUserDTO : dingdingUserDTOS) {
                if (isMatchUser(user, dingdingUserDTO)) {
                    user.setDingdingUserId(dingdingUserDTO.getUserId());
                }
            }
            bindDingdingIdUsers.add(user);
        }
        return userService.updateDingdingIdUsers(bindDingdingIdUsers);
    }

    /**
     * 是否是匹配的用户
     */
    private boolean isMatchUser(User user, DingdingUserDTO dingdingUserDTO) {
        boolean phoneEqual = StringUtils.equals(user.getPhone(), dingdingUserDTO.getMobile());
        boolean nameEqual = !phoneEqual && StringUtils.equals(user.getRealName(), dingdingUserDTO.getName());

        return phoneEqual || nameEqual;
    }

    /** 从钉钉网关获取钉钉用户数据传输对象列表 */
    private List<DingdingUserDTO> getUsersFromDingdingGateway() {
        String respContent = doGetUsersFromDingdingGateway();
        DingdingResultDTO dingdingResultDTO = JSONObject.parseObject(respContent, DingdingResultDTO.class);
        if (dingdingResultDTO == null || !dingdingResultDTO.getSuccess()) {
            logger.info("从钉钉网关获取用户信息失败：" + JSONObject.toJSONString(dingdingResultDTO));
            return null;
        }
        String resultMapStr = JSONObject.toJSONString(dingdingResultDTO.getResultMap());
        return JSONObject.parseArray(resultMapStr, DingdingUserDTO.class);
    }
    /**
     * 从钉钉网关获取用户列表信息
     */
    private String doGetUsersFromDingdingGateway() {
        String respContent = null;
        try {
            HttpGet httpGet = new HttpGet(DingDingConfig.getGetMemberListbyDepartmentUrl() + "?departmentId=1");
            CloseableHttpClient client = HttpClients.createDefault();
            httpGet.setHeader("content-type", "utf-8");
            HttpResponse resp = client.execute(httpGet);
            if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = resp.getEntity();
                respContent = EntityUtils.toString(entity, "UTF-8");
                logger.info("网关处返回的结果为：" + respContent);
                return respContent;
            }
        } catch (Exception e) {
            logger.error("importDingDingUsers message error,{}", e);
        }
        return respContent;
    }
}
