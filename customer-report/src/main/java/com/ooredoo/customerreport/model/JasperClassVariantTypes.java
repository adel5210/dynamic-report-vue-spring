package com.ooredoo.customerreport.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.util.Pair;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author aalbediwy
 */
@Getter
@AllArgsConstructor
public enum JasperClassVariantTypes {

    BIG_DECIMAL(BigDecimal.class, BigDecimal.ZERO),
    STRING(String.class, ""),
    BOOLEAN(Boolean.class, false),
    LIST(List.class, new ArrayList<>()),
    DOUBLE(Double.class, 0),
    INTEGER(Integer.class, 0),
    DATE(Date.class, new Date()), //Current Date
    LONG(Long.class, 0)
    ;

    private final Class className;
    private final Object defaultValue;

    public static JasperClassVariantTypes fromClassName(String className){
        return Arrays.stream(values())
                .filter(f->f.getClassName().getName().equals(className))
                .findFirst()
                .orElse(null);
    }


}
