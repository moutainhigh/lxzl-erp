package com.lxzl.erp.common.util.validate.constraints;

import com.lxzl.erp.common.util.validate.validator.DateValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * User : LiuKe
 * Date : 2016/11/24
 * Time : 17:15
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy=DateValidator.class)
public @interface Date {
    String pattern() default "yyyy-MM-dd HH:mm:ss";

    String message() default"must be date pattern";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}