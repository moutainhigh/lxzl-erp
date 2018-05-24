package com.lxzl.erp.web.listener;

import com.lxzl.se.core.context.SpringContextHolder;
import com.lxzl.se.core.quartz.source.DynamicQuartz;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.List;
import java.util.Map;

/**
 * <p>Description: 关闭tomcat时调用schedule的关闭</p>
 *
 * @Auther: huahongbin
 * @Date: 2018/5/23
 */
public class ShutDownQuartzListener implements ServletContextListener {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("绝影");
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        Map<String, DynamicQuartz> map = SpringContextHolder.applicationContext.getBeansOfType(DynamicQuartz.class);
        if (map != null && map.size() > 0) {
            for (Map.Entry<String, DynamicQuartz> entry : map.entrySet()) {
                DynamicQuartz dynamicQuartz = entry.getValue();
                List<String> schedNames = dynamicQuartz.getAllScheduleNames(null);
                if (schedNames != null && schedNames.size() > 0) {
                    for (String schedName : schedNames) {
                        DynamicQuartz.QuartzSchedulerFactory quartzSchedulerFactory = dynamicQuartz.getQuartzSchedulerFactory(null, schedName);
                        Scheduler scheduler = quartzSchedulerFactory.getScheduler();
                        try {
                            logger.info("Quartz: waiting for job complete...");
                            scheduler.shutdown(true);
                            logger.info("Quartz: all threads are complete and exited...");
                        } catch (SchedulerException e) {
                            logger.error("Quartz: Shutdown scheduler {} failed.", schedName);
                        }
                    }
                }

            }
        }
    }
}
