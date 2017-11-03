package com.lxzl.erp.common.util.validate.constraints;


/**
 * User : LiuKe
 * Date : 2016/12/21
 * Time : 10:25
 */


import com.lxzl.erp.common.util.validate.validator.CollectionNotNullValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy=CollectionNotNullValidator.class)
public @interface CollectionNotNull {

    String message() default"collection must be not null";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}