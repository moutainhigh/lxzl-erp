package com.lxzl.erp.common.util.validate.validator;


import com.lxzl.erp.common.util.validate.constraints.NotIn;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashMap;
import java.util.Map;

/**
 * User : LiuKe
 * Date : 2017/1/17
 * Time : 10:12
 */

public class NotInValidator implements ConstraintValidator<NotIn, Integer> {

    private int[] values;
    @Override
    public void initialize(NotIn constraintAnnotation) {
        values = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(Integer o, ConstraintValidatorContext context) {

        Map<Integer,String> map = new HashMap<>();

        for(int value : values){
            map.put(value,"");
        }
        if(o==null){
            return true;
        }else if(map.get(o)==null){
            return true;
        }else{
            return false;
        }
    }
}
