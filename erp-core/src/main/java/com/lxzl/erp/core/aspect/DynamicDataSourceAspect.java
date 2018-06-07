package com.lxzl.erp.core.aspect;

import com.lxzl.erp.core.annotation.DynamicDataSource;
import com.lxzl.se.dataaccess.mysql.source.DataSourceSwitcher;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * <p>Description: </p>
 *
 * @Auther: huahongbin
 * @Date: 2018/5/22
 */
@Component
@Aspect
@Order(3)
public class DynamicDataSourceAspect {

    private Logger logger = LoggerFactory.getLogger(DynamicDataSourceAspect.class);

    @Pointcut("@annotation(com.lxzl.erp.core.annotation.DynamicDataSource)")
    public void dynamicDataSourceMethodCut() {}

    @Pointcut("@within(com.lxzl.erp.core.annotation.DynamicDataSource)")
    public void dynamicDataSourceCut() {}

    @Before("dynamicDataSourceMethodCut() || dynamicDataSourceCut()")
    public void doBefore(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        Method method = ((MethodSignature)signature).getMethod();
        DynamicDataSource annotationClass = method.getAnnotation(DynamicDataSource.class);//获取方法上的注解
        if(annotationClass == null){
            annotationClass = joinPoint.getTarget().getClass().getAnnotation(DynamicDataSource.class);//获取类上面的注解
        }

        // 如果是代理方法获取不到注解，需要从目标对象获取注解
        if (annotationClass == null) {
            try {
                Method realMethod = joinPoint.getTarget().getClass().getDeclaredMethod(signature.getName(), method.getParameterTypes());
                annotationClass = realMethod.getAnnotation(DynamicDataSource.class);
                if (annotationClass == null) return;
            } catch (NoSuchMethodException e) {
               return;
            }
        }

        //获取注解上的数据源的值的信息
        String dataSourceKey = annotationClass.value();
        if (StringUtils.isNotBlank(dataSourceKey)) {
            DataSourceSwitcher.setDataSourceTypeInContext(dataSourceKey);
            logger.info("【AOP Switch DataSource】className: " + joinPoint.getTarget().getClass().getName() + "methodName: " + method.getName()
                    + "; dataSourceKey: " + dataSourceKey);
        }
    }

    @After("dynamicDataSourceMethodCut() || dynamicDataSourceCut()")
    public void after(JoinPoint point) {
        //清理掉当前设置的数据源，让默认的数据源不受影响
        DataSourceSwitcher.clearDataSourceType();
    }

}
