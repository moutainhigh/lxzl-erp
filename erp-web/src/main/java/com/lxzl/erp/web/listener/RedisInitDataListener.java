package com.lxzl.erp.web.listener;

import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.constant.RedisKeyConstant;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.user.UserQueryParam;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.common.util.ListUtil;
import com.lxzl.erp.core.service.user.UserService;
import com.lxzl.erp.dataaccess.dao.redis.RedisManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.List;
import java.util.Map;

/**
 * 描述: redis系统启动初始化监听器
 *
 * @author gaochao
 * @date 2017-12-21 20:29
 */
@Service
public class RedisInitDataListener implements ServletContextListener {

    private final Logger log = LoggerFactory.getLogger(RedisInitDataListener.class);

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        UserService userService = WebApplicationContextUtils.getWebApplicationContext(servletContextEvent.getServletContext()).getBean(UserService.class);
        RedisManager redisManager = WebApplicationContextUtils.getWebApplicationContext(servletContextEvent.getServletContext()).getBean(RedisManager.class);

        UserQueryParam userQueryParam = new UserQueryParam();
        ServiceResult<String, List<User>> getUserResult = userService.getUserListByParam(userQueryParam);
        if (ErrorCode.SUCCESS.equals(getUserResult.getErrorCode())) {
            List<User> userList = getUserResult.getResult();
            Map<Integer, User> userMap = ListUtil.listToMap(userList, "userId");
            redisManager.add(RedisKeyConstant.USER_KEY, userMap);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
