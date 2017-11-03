package com.lxzl.erp.common.util.validate.validator;

import com.lxzl.erp.common.util.validate.constraints.CollectionNotNull;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Collection;

/**
 * User : LiuKe
 * Date : 2016/12/21
 * Time : 10:36
 */
public class CollectionNotNullValidator implements ConstraintValidator<CollectionNotNull, Collection> {


    @Override
    public void initialize(CollectionNotNull constraintAnnotation) {

    }

    @Override
    public boolean isValid(Collection value, ConstraintValidatorContext context) {
        if (value == null||value.size()==0){
            return false;
        }
        return true;
    }
}
