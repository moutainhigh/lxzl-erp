package com.lxzl.erp.core.annotation;

import java.lang.annotation.*;

/**
 * User : LiuKe
 * Date : 2016/12/23
 * Time : 15:51
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ControllerLog {

    /**
     * the log for controller
     */
    Class<?>[] value() default {};

}