package com.lxzl.erp.common.util.validate.constraints;

import com.lxzl.erp.common.util.validate.validator.InValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * User : LiuKe
 * Date : 2017/1/17
 * Time : 10:10
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy=InValidator.class)
public @interface In {

    String message() default "value not in array";

    Class<?>[] groups() default {};

    int[] value() default {};

    Class<? extends Payload>[] payload() default { };

}
