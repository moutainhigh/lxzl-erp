package com.lxzl.erp.core.annotation;

import java.lang.annotation.*;

/**
 * <p>Description: 动态选择数据源注解</p>
 *
 * @Auther: huahongbin
 * @Date: 2018/5/22
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DynamicDataSource {
    String value() default "";
}
