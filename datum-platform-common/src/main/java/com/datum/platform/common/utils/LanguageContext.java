package com.datum.platform.common.utils;

import com.datum.platform.common.constants.SysConstant;

import java.util.Objects;

/**
 * @author admin
 * @date 2021/7/20
 */
public class LanguageContext {
    private static final ThreadLocal<String> LANGUAGE_LOCAL = new ThreadLocal<>();

    private LanguageContext() {
    }

    public static String get() {
        return LANGUAGE_LOCAL.get();
    }

    public static void set(String language) {
        LANGUAGE_LOCAL.remove();
        LANGUAGE_LOCAL.set(language);
    }

    public static void remove() {
        LANGUAGE_LOCAL.remove();
    }

    public static String getByLanguage(String zh, String en){
        if (Objects.nonNull(LanguageContext.get()) && LanguageContext.get().equals(SysConstant.EN_US)) {
            return en;
        }
        return zh;
    }
}
