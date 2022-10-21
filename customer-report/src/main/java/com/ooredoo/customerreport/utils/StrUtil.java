package com.ooredoo.customerreport.utils;

import org.springframework.util.StringUtils;

/**
 * @author aalbediwy
 */
public class StrUtil {

    public static String toWords(String s) {
        final String s1 = s.replaceAll(
                String.format("%s|%s|%s",
                        "(?<=[A-Z])(?=[A-Z][a-z])",
                        "(?<=[^A-Z])(?=[A-Z])",
                        "(?<=[A-Za-z])(?=[^A-Za-z])"
                ),
                " "
        );
        return StringUtils.capitalize(s1);
    }
}
