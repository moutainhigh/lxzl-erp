package com.lxzl.erp.common.util.validate.validator;


import com.lxzl.erp.common.util.validate.constraints.Date;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * User : LiuKe
 * Date : 2016/11/24
 * Time : 17:02
 */
public class DateValidator implements ConstraintValidator<Date, String> {

    private SimpleDateFormat sdf = null;
    @Override
    public void initialize(Date constraintAnnotation) {
        sdf = new SimpleDateFormat(constraintAnnotation.pattern());
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try {
            if(value!=null){
                sdf.parse(value);
            }
            return true;
        } catch (ParseException e) {
            //do noting
            return false;
        }
    }
}
