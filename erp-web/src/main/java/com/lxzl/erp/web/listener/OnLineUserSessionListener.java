package com.lxzl.erp.web.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: liangyouzhi
 * @Description：session监听器，统计在线用户数
 * @Date: Created in 15:40 2018/08/16
 * @Modified By:
 */

@WebListener
public class OnLineUserSessionListener implements HttpSessionListener {
    private static final Logger logger = LoggerFactory.getLogger(OnLineUserSessionListener.class);

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        try {
            Map<String, HttpSession> map = (Map<String, HttpSession>) se.getSession().getServletContext().getAttribute("onLineMap");
            if (map == null) {//说明这是第一次访问是，需要自己new 一个对象
                map = Collections.synchronizedMap(new HashMap<String, HttpSession>());//采用集合上锁，采用java 自带的上锁函数
                se.getSession().getServletContext().setAttribute("onLineMap", map);
            }
            logger.info("listener添加一个了");
            map.put(se.getSession().getId(), se.getSession());//以session 的id为key,session对象为value存在map中
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        try {
            Map<String, HttpSession> onLines = (Map<String, HttpSession>) se.getSession().getServletContext().getAttribute("onLineMap");
            //移除，对象
            onLines.remove(se.getSession().getId());
            logger.info("listener销毁了一个");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}
