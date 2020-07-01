package com.ay.mall.config.converter;

import com.ay.mall.util.DateTimeUtil;
import org.dozer.CustomConverter;

import java.util.Date;


public class DateConverter  implements CustomConverter {

    @Override
    public Object convert(Object destinationFieldValue,
                          Object sourceFieldValue, Class<?> destinationClass,
                          Class<?> sourceClass) {
        Object returnVale = null;
        if (sourceFieldValue!=null){
            if (sourceFieldValue instanceof Date){
                returnVale= DateTimeUtil.dateToStr((Date)sourceFieldValue);
            }
        }
        return returnVale;
    }
}
